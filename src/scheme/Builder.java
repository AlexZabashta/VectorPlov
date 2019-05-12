package scheme;

public class Builder {
    public static Node buildLayers(int... layers) {
        int offset = 0;
        Node node = new Variable("x", 0, layers[0]);
        StringFunction tanh = new Tanh();

        for (int i = 1; i < layers.length; i++) {

            // Variable mu = new Variable("w", offset, offset += layers[i - 1]);
            // node = new ZeroMean(node, mu, 0.01);

            // Variable sigma = new Variable("w", offset, offset += layers[i - 1]);
            // node = new UnitStd(node, sigma, 0.01);

            node = new UnitDerivative(node);

            Variable w = new Variable("w", offset, offset += layers[i - 1] * layers[i]);
            Multiplication mul = new Multiplication(1, layers[i - 1], layers[i], node, w);
            Variable b = new Variable("w", offset, offset += layers[i]);

            Sum sum = new Sum(mul, b);
            ApplyFunction act = new ApplyFunction(sum, tanh);

            node = act;
        }

        return node;
    }
}
