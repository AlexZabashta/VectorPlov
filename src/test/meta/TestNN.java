package test.meta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.Pair;

import clsf.Dataset;
import core.MultiVarDiffStruct;
import core.ParallelVDiffStruct;
import core.Pipe;
import core.Result;
import dataset.Convolution;
import dataset.SymConvolution;
import grad.MAdaGrad;
import grad.MSGD;
import mfextraction.CMFExtractor;
import mfextraction.KNNLandMark;

public class TestNN {

    final CMFExtractor extractor = new CMFExtractor();
    final int numMF = extractor.length();
    final ToDoubleFunction<Dataset> knnScore = new KNNLandMark();

    final double[] min = new double[numMF];
    final double[] max = new double[numMF];

    final HVFold hvFold = new HVFold();
    final Convolution convolution = new SymConvolution(hvFold.ySize, hvFold, hvFold);

    final Encoder encoder = new Encoder();
    final Decoder decoder = new Decoder();
    final Simple simple = new Simple();

    final MultiVarDiffStruct<double[][][], double[][][]> pencoder = MultiVarDiffStruct.convert(new ParallelVDiffStruct(false, encoder));
    final MultiVarDiffStruct<double[], double[]> mdecoder = MultiVarDiffStruct.convert(decoder);
    final Pipe<double[][][], ?, double[]> net = Pipe.of(pencoder, convolution, mdecoder);

    final int numFeatures = 16;
    final int numObjectsPerClass = 64;

    String[] classNames = { "zero", "one" };

    final int numClasses = classNames.length;
    final int numObjects = numObjectsPerClass * numClasses;

    final double[] enc, dec, hor, ver, sim;

    public TestNN() {
        Arrays.fill(min, Double.POSITIVE_INFINITY);
        Arrays.fill(max, Double.NEGATIVE_INFINITY);

        enc = new double[encoder.numBoundVars()];
        dec = new double[decoder.numBoundVars()];
        hor = new double[hvFold.numBoundVars()];
        ver = new double[hvFold.numBoundVars()];
        sim = new double[simple.numBoundVars()];

        encoder.init(enc);
        decoder.init(dec);
        hvFold.init(hor);
        hvFold.init(ver);
        simple.init(sim);
    }

    class PredictResult {
        final double ty;
        final Result<Pair<double[], double[]>, double[]> sp;
        final Result<Pair<double[][][], double[][]>, double[]> cp;

        public PredictResult(double ty, Result<Pair<double[], double[]>, double[]> sp, Result<Pair<double[][][], double[][]>, double[]> cp) {
            this.ty = ty;
            this.sp = sp;
            this.cp = cp;
        }

    }

    class Forward implements Callable<PredictResult> {

        final Dataset dataset;

        public Forward(Dataset dataset) {
            this.dataset = dataset;
        }

        @Override
        public PredictResult call() throws Exception {
            double ty = knnScore.applyAsDouble(dataset) * 2 - 0.8589644085915296;

            double[] rmf = extractor.apply(dataset);
            double[] nmf = new double[numMF];
            for (int i = 0; i < numMF; i++) {
                nmf[i] = (rmf[i] - min[i]) / (max[i] - min[i]) * 2 - 1;
            }
            Result<Pair<double[], double[]>, double[]> sp = simple.result(nmf, sim);

            double[][][] obj = new double[numObjects][numFeatures][2];
            for (int oid = 0; oid < numObjects; oid++) {
                for (int fid = 0; fid < numFeatures; fid++) {
                    obj[oid][fid][0] = dataset.data[oid][fid];
                    obj[oid][fid][1] = dataset.labels[oid];
                }
            }
            Result<Pair<double[][][], double[][]>, double[]> cp = net.result(obj, enc, hor, ver, dec);

            return new PredictResult(ty, sp, cp);
        }
    }

    void run() throws InterruptedException, FileNotFoundException, ExecutionException {

        final List<Dataset> datasets = new ArrayList<>();

        for (File datafolder : new File("csv").listFiles()) {
            try {
                double[][] data = new double[numObjects][numFeatures];
                int[] labels = new int[numObjects];

                String[] header = new String[numFeatures];

                for (int f = 0; f < numFeatures; f++) {
                    header[f] = "f" + f;
                }

                for (int oid = 0, label = 0; label < numClasses; label++) {
                    try (CSVParser parser = new CSVParser(new FileReader(datafolder.getPath() + File.separator + classNames[label] + ".csv"), CSVFormat.DEFAULT.withHeader(header))) {
                        for (CSVRecord record : parser) {
                            for (int fid = 0; fid < numFeatures; fid++) {
                                data[oid][fid] = Double.parseDouble(record.get(fid));
                            }
                            labels[oid++] = label;
                        }
                    }
                }

                datasets.add(new Dataset(datafolder.getName(), true, data, false, labels));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        int numMF = extractor.length();

        final int numData = datasets.size();
        List<Dataset> train = new ArrayList<>();
        List<Dataset> test = new ArrayList<>();

        for (int i = 0; i < numData; i++) {
            double[] vector = extractor.apply(datasets.get(i));
            for (int j = 0; j < numMF; j++) {
                min[j] = Math.min(min[j], vector[j]);
                max[j] = Math.max(max[j], vector[j]);
            }
        }

        Collections.sort(datasets, Comparator.comparing(dataset -> dataset.name));

        for (int i = 0; i < numData; i++) {
            if (i % 10 == 0) {
                test.add(datasets.get(i));
            } else {
                train.add(datasets.get(i));
            }
        }

        System.out.println(numData);

        // Consumer<double[][]> grad = new MAdaGrad(new double[][] { enc, hor, ver, dec, sim }, 0.0001, 0.9, 0.999);
        Consumer<double[][]> grad = new MSGD(new double[][] { enc, hor, ver, dec, sim }, 0.001);

        int batch = 12;
        ExecutorService executor = Executors.newFixedThreadPool(6);

        try (PrintWriter out = new PrintWriter("result.txt")) {
            for (int epoch = 1; epoch < 1000; epoch++) {
                double trainS = 0, trainC = 0;
                Collections.shuffle(train);

                int trainPointer = 0;

                while (trainPointer < train.size()) {
                    List<Future<PredictResult>> results = new ArrayList<>();
                    for (int i = 0; trainPointer < train.size() && i < batch; i++) {
                        results.add(executor.submit(new Forward(train.get(trainPointer++))));
                    }

                    List<Future<double[][]>> fdeltas = new ArrayList<>();

                    for (Future<PredictResult> future : results) {
                        PredictResult result = future.get();

                        double diffS = result.sp.value()[0] - result.ty;
                        double diffC = result.cp.value()[0] - result.ty;

                        trainS += diffS * diffS;
                        trainC += diffC * diffC;

                        fdeltas.add(executor.submit(new Callable<double[][]>() {

                            @Override
                            public double[][] call() throws Exception {
                                double[] dyc = { diffC };
                                double[][] delta = Arrays.copyOf(result.cp.apply(dyc).getRight(), 5);

                                double[] dys = { diffS };
                                delta[4] = result.sp.apply(dys).getRight();

                                return delta;
                            }
                        }));
                    }
                    System.out.printf(Locale.ENGLISH, "%d train %.4f %.4f%n", epoch, Math.sqrt(trainS / trainPointer), Math.sqrt(trainC / trainPointer));

                    List<double[][]> deltas = new ArrayList<>();
                    for (Future<double[][]> fdelta : fdeltas) {
                        deltas.add(fdelta.get());
                    }

                    for (double[][] delta : deltas) {
                        grad.accept(delta);
                    }
                }

                String trainResult = String.format(Locale.ENGLISH, "%d train %.4f %.4f", epoch, Math.sqrt(trainS / train.size()), Math.sqrt(trainC / train.size()));
                System.out.println(trainResult);
                out.println(trainResult);
                out.flush();

                List<Future<double[]>> results = new ArrayList<>();

                for (Dataset dataset : test) {
                    results.add(executor.submit(new Callable<double[]>() {
                        @Override
                        public double[] call() throws Exception {
                            PredictResult result = (new Forward(dataset)).call();
                            double diffS = result.sp.value()[0] - result.ty;
                            double diffC = result.cp.value()[0] - result.ty;
                            return new double[] { diffS, diffC };
                        }
                    }));
                }

                double testS = 0, testC = 0;

                for (Future<double[]> future : results) {
                    double[] result = future.get();

                    double diffS = result[0];
                    double diffC = result[1];

                    testS += diffS * diffS;
                    testC += diffC * diffC;
                }

                String testResult = String.format(Locale.ENGLISH, "%d test %.4f %.4f", epoch, Math.sqrt(testS / test.size()), Math.sqrt(testC / test.size()));
                System.out.println(testResult);
                out.println(testResult);
                out.flush();

            }
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        new TestNN().run();
    }
}