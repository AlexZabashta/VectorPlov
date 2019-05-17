package test.best;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import scheme.Builder;
import scheme.Node;
import tojava.CompilerToSrc;

public class BuildEHVD {

    public static void buildAndSave(String name, boolean lastLinear, int... sizes) {
        Node node = Builder.buildLayers(lastLinear, sizes);
        List<String> programm = CompilerToSrc.compile(null, name, node.preCompile());
        try (PrintWriter writer = new PrintWriter(name + ".java")) {
            for (String line : programm) {
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        buildAndSave("Encoder", false, 2, 16, 32, 64);
        buildAndSave("HVFold", false, 128, 96, 64);
        buildAndSave("Decoder", false, 64, 29, 20, 15, 3);
        buildAndSave("Simple", false, 29, 20, 15, 3);
    }
}
