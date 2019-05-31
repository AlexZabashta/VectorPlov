package test.best;
import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class Decoder extends VectorDiffStruct {
    public Decoder() {
        super(40, 1783, 3, 174, 174);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 0; i < 920; i++)
                w[i] = 0.15811388300841897 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 943; i < 1403; i++)
                w[i] = 0.20851441405707477 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1423; i < 1723; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1738; i < 1783; i++)
                w[i] = 0.2581988897471611 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 40; j++)
            for (int k = 0; k < 23; k++)
                f[i * 23 + k + 0] += x[i * 40 + j + 0] * w[j * 23 + k + 0];
        }
        {
            int ap = 0, bp = 920;
            for (int cp = 23; cp < 46; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 23, yp = 46;
            for (int i = 0; i < 23; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 23; j++)
            for (int k = 0; k < 20; k++)
                f[i * 20 + k + 69] += f[i * 23 + j + 46] * w[j * 20 + k + 943];
        }
        {
            int ap = 69, bp = 1403;
            for (int cp = 89; cp < 109; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 89, yp = 109;
            for (int i = 0; i < 20; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++)
                f[i * 15 + k + 129] += f[i * 20 + j + 109] * w[j * 15 + k + 1423];
        }
        {
            int ap = 129, bp = 1723;
            for (int cp = 144; cp < 159; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 144, yp = 159;
            for (int i = 0; i < 15; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 3; k++)
                y[i * 3 + k + 0] += f[i * 15 + j + 159] * w[j * 3 + k + 1738];
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 3; k++) {
                b[i * 15 + j + 0] += dy[i * 3 + k + 0] * w[j * 3 + k + 1738];
                dw[j * 3 + k + 1738] += f[i * 15 + j + 159] * dy[i * 3 + k + 0];
            }
        }
        {
            int xp = 144, yp = 159, dxp = 15, dyp = 0;
            for (int i = 0; i < 15; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 30, dbp = 1723;
            for (int dcp = 15; dcp < 30; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++) {
                b[i * 20 + j + 45] += b[i * 15 + k + 30] * w[j * 15 + k + 1423];
                dw[j * 15 + k + 1423] += f[i * 20 + j + 109] * b[i * 15 + k + 30];
            }
        }
        {
            int xp = 89, yp = 109, dxp = 65, dyp = 45;
            for (int i = 0; i < 20; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 85, dbp = 1403;
            for (int dcp = 65; dcp < 85; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 23; j++)
            for (int k = 0; k < 20; k++) {
                b[i * 23 + j + 105] += b[i * 20 + k + 85] * w[j * 20 + k + 943];
                dw[j * 20 + k + 943] += f[i * 23 + j + 46] * b[i * 20 + k + 85];
            }
        }
        {
            int xp = 23, yp = 46, dxp = 128, dyp = 105;
            for (int i = 0; i < 23; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 151, dbp = 920;
            for (int dcp = 128; dcp < 151; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 40; j++)
            for (int k = 0; k < 23; k++) {
                dx[i * 40 + j + 0] += b[i * 23 + k + 151] * w[j * 23 + k + 0];
                dw[j * 23 + k + 0] += x[i * 40 + j + 0] * b[i * 23 + k + 151];
            }
        }
    }
}
