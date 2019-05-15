package dataset;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import core.MultiVarDiffStruct;
import core.Result;
import core.VarDiffStruct;

public abstract class Convolution implements MultiVarDiffStruct<double[][][], double[]> {

    public class Memory implements Function<double[], Pair<double[][][], double[][]>> {
        final Node root;
        final int rows, cols;
        final double normH, normV;

        Memory(Node root, int rows, int cols, double normH, double normV) {
            this.root = root;
            this.rows = rows;
            this.cols = cols;
            this.normH = normH;
            this.normV = normV;
        }

        void normalize(double[] array, double scale) {
            for (int i = 0; i < array.length; i++) {
                array[i] *= scale;
            }
        }

        @Override
        public Pair<double[][][], double[][]> apply(double[] dy) {
            double[][][] dx = new double[rows][cols][];
            double[] dh = new double[horzFold.numBoundVars()];
            double[] dv = new double[vertFold.numBoundVars()];

            root.backward(dy, dx, dh, dv, 0, 0);

            if (normH > 0) {
                normalize(dh, 1.0 / normH);
            }

            if (normV > 0) {
                normalize(dv, 1.0 / normV);
            }

            return Pair.of(dx, new double[][] { dh, dv });
        }

    }

    abstract class Node {
        final double[] values;
        final int level;

        Node(int level, double[] values) {
            this.level = level;
            this.values = values;
        }

        void backward(double[] dy, double[][][] dx, double[] sdh, double[] sdv, double sumErrors, double normErrors) {
            double error = 0;
            for (int i = 0; i < dy.length; i++) {
                error += dy[i] * dy[i];
            }
            sumErrors += Math.sqrt(error) * weight();
            normErrors += weight();

            error = sumErrors / normErrors;
            for (int i = 0; i < depth; i++) {
                dy[i] *= error;
            }

            backwardE(dy, dx, sdh, sdv, sumErrors, normErrors);
        }

        abstract void backwardE(double[] dy, double[][][] dx, double[] sdh, double[] sdv, double sumErrors, double normErrors);

        double weight() {
            return level;
        }

    }

    abstract class Fold extends Node {
        final Node first, secnd;
        final Function<double[], Pair<double[], double[]>> derivative;

        Fold(Node first, Node secnd, Function<double[], Pair<double[], double[]>> derivative, double[] values) {
            super(Math.max(first.level, secnd.level) + 1, values);
            this.first = first;
            this.secnd = secnd;
            this.derivative = derivative;
        }

        void addWithWeight(double[] src, double[] dst) {
            double scale = weight();
            for (int i = 0; i < src.length; i++) {
                dst[i] += src[i] * scale;
            }
        }

        abstract double[] backward(double[] dy, double[] sdh, double[] sdv);

        @Override
        void backwardE(double[] dy, double[][][] dInput, double[] sdh, double[] sdv, double sumErrors, double normErrors) {
            double[] dx = backward(dy, sdh, sdv);

            double[] da = new double[depth];
            double[] db = new double[depth];

            System.arraycopy(dx, 0, da, 0, depth);
            System.arraycopy(dx, depth, db, 0, depth);

            first.backward(da, dInput, sdh, sdv, sumErrors, normErrors);
            secnd.backward(db, dInput, sdh, sdv, sumErrors, normErrors);
        }

    }

    class HorzFold extends Fold {
        HorzFold(Node first, Node secnd, Function<double[], Pair<double[], double[]>> derivative, double[] values) {
            super(first, secnd, derivative, values);
        }

        @Override
        double[] backward(double[] dy, double[] sdh, double[] sdv) {
            Pair<double[], double[]> dxh = derivative.apply(dy);
            addWithWeight(sdh, dxh.getRight());
            return dxh.getLeft();
        }
    }

    class VertFold extends Fold {
        VertFold(Node first, Node secnd, Function<double[], Pair<double[], double[]>> derivative, double[] values) {
            super(first, secnd, derivative, values);
        }

        @Override
        double[] backward(double[] dy, double[] sdh, double[] sdv) {
            Pair<double[], double[]> dxv = derivative.apply(dy);
            addWithWeight(sdv, dxv.getRight());
            return dxv.getLeft();
        }
    }

    class DatasetCell extends Node {
        final int row, col;

        DatasetCell(double[] values, int row, int col) {
            super(1, values);
            this.row = row;
            this.col = col;
        }

        @Override
        void backwardE(double[] dy, double[][][] dx, double[] sdh, double[] sdv, double sumErrors, double normErrors) {
            dx[row][col] = dy;
        }

    }

    public final int depth;
    public final VarDiffStruct<double[], double[]> horzFold;
    public final VarDiffStruct<double[], double[]> vertFold;

    public Convolution(int depth, VarDiffStruct<double[], double[]> horzFold, VarDiffStruct<double[], double[]> vertFold) {
        this.depth = depth;
        this.horzFold = horzFold;
        this.vertFold = vertFold;
    }

    Fold buildNode(boolean horizontal, double[] foldBoundVar, Node first, Node secnd) {
        double[] x = new double[depth * 2];
        System.arraycopy(first.values, 0, x, 0, depth);
        System.arraycopy(secnd.values, 0, x, depth, depth);

        if (horizontal) {
            Result<Pair<double[], double[]>, double[]> result = horzFold.result(x, foldBoundVar);
            return new HorzFold(first, secnd, result.derivative(), result.value());
        } else {
            Result<Pair<double[], double[]>, double[]> result = vertFold.result(x, foldBoundVar);
            return new VertFold(first, secnd, result.derivative(), result.value());
        }
    }

    @Override
    public Result<Pair<double[][][], double[][]>, double[]> result(double[][][] freeVar, double[]... bounVar) {
        return result(freeVar, bounVar[0], bounVar[1]);
    }

    public Result<Pair<double[][][], double[][]>, double[]> result(double[][][] dataset, double[] horzBoundVar, double[] vertBoundVar) {
        final int rows = dataset.length, cols = dataset[0].length;

        Node[][] nodes = new Node[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                nodes[row][col] = new DatasetCell(dataset[row][col], row, col);
            }
        }
        return result(rows, cols, nodes, horzBoundVar, vertBoundVar);
    }

    abstract Result<Pair<double[][][], double[][]>, double[]> result(int rows, int cols, Node[][] nodes, double[] horzBoundVar, double[] vertBoundVar);

    @Override
    public int[] numBoundVars() {
        return new int[] { horzFold.numBoundVars(), vertFold.numBoundVars() };
    }

}
