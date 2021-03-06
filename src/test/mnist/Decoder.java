package test.mnist;
import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class Decoder extends VectorDiffStruct {
    public Decoder() {
        super(28, 634, 10, 68, 68);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 0; i < 448; i++)
                w[i] = 0.1889822365046136 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 464; i < 624; i++)
                w[i] = 0.25 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 28; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 0] += x[i * 28 + j + 0] * w[j * 16 + k + 0];
        }
        {
            int ap = 0, bp = 448;
            for (int cp = 16; cp < 32; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 16, yp = 32;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 16; j++)
            for (int k = 0; k < 10; k++)
                f[i * 10 + k + 48] += f[i * 16 + j + 32] * w[j * 10 + k + 464];
        }
        {
            int ap = 48, bp = 624;
            for (int cp = 58; cp < 68; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 58, yp = 0;
            for (int i = 0; i < 10; i++, xp++) {
                y[yp++] += tanh(f[xp]);
            }
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            int xp = 58, yp = 0, dxp = 0, dyp = 0;
            for (int i = 0; i < 10; i++, xp++, yp++) {
                b[dxp++] += dy[dyp++] * (1 - y[yp] * y[yp]);
            }
        }
        {
            int dap = 10, dbp = 624;
            for (int dcp = 0; dcp < 10; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 16; j++)
            for (int k = 0; k < 10; k++) {
                b[i * 16 + j + 20] += b[i * 10 + k + 10] * w[j * 10 + k + 464];
                dw[j * 10 + k + 464] += f[i * 16 + j + 32] * b[i * 10 + k + 10];
            }
        }
        {
            int xp = 16, yp = 32, dxp = 36, dyp = 20;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 52, dbp = 448;
            for (int dcp = 36; dcp < 52; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 28; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 28 + j + 0] += b[i * 16 + k + 52] * w[j * 16 + k + 0];
                dw[j * 16 + k + 0] += x[i * 28 + j + 0] * b[i * 16 + k + 52];
            }
        }
    }
}
