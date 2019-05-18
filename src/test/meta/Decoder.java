package test.meta;
import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class Decoder extends VectorDiffStruct {
    public Decoder() {
        super(64, 2971, 1, 223, 223);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 0; i < 1856; i++)
                w[i] = 0.125 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1885; i < 2465; i++)
                w[i] = 0.18569533817705186 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 2485; i < 2785; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 2800; i < 2950; i++)
                w[i] = 0.2581988897471611 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 2960; i < 2970; i++)
                w[i] = 0.31622776601683794 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 64; j++)
            for (int k = 0; k < 29; k++)
                f[i * 29 + k + 0] += x[i * 64 + j + 0] * w[j * 29 + k + 0];
        }
        {
            int ap = 0, bp = 1856;
            for (int cp = 29; cp < 58; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 29, yp = 58;
            for (int i = 0; i < 29; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 29; j++)
            for (int k = 0; k < 20; k++)
                f[i * 20 + k + 87] += f[i * 29 + j + 58] * w[j * 20 + k + 1885];
        }
        {
            int ap = 87, bp = 2465;
            for (int cp = 107; cp < 127; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 107, yp = 127;
            for (int i = 0; i < 20; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++)
                f[i * 15 + k + 147] += f[i * 20 + j + 127] * w[j * 15 + k + 2485];
        }
        {
            int ap = 147, bp = 2785;
            for (int cp = 162; cp < 177; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 162, yp = 177;
            for (int i = 0; i < 15; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 10; k++)
                f[i * 10 + k + 192] += f[i * 15 + j + 177] * w[j * 10 + k + 2800];
        }
        {
            int ap = 192, bp = 2950;
            for (int cp = 202; cp < 212; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 202, yp = 212;
            for (int i = 0; i < 10; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 10; j++)
            for (int k = 0; k < 1; k++)
                f[i * 1 + k + 222] += f[i * 10 + j + 212] * w[j * 1 + k + 2960];
        }
        {
            int ap = 222, bp = 2970;
            for (int cp = 0; cp < 1; cp++)
                y[cp] += f[ap++] + w[bp++];
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            int dap = 0, dbp = 2970;
            for (int dcp = 0; dcp < 1; dcp++) {
                b[dap++] += dy[dcp];
                dw[dbp++] += dy[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 10; j++)
            for (int k = 0; k < 1; k++) {
                b[i * 10 + j + 1] += b[i * 1 + k + 0] * w[j * 1 + k + 2960];
                dw[j * 1 + k + 2960] += f[i * 10 + j + 212] * b[i * 1 + k + 0];
            }
        }
        {
            int xp = 202, yp = 212, dxp = 11, dyp = 1;
            for (int i = 0; i < 10; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 21, dbp = 2950;
            for (int dcp = 11; dcp < 21; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 10; k++) {
                b[i * 15 + j + 31] += b[i * 10 + k + 21] * w[j * 10 + k + 2800];
                dw[j * 10 + k + 2800] += f[i * 15 + j + 177] * b[i * 10 + k + 21];
            }
        }
        {
            int xp = 162, yp = 177, dxp = 46, dyp = 31;
            for (int i = 0; i < 15; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 61, dbp = 2785;
            for (int dcp = 46; dcp < 61; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++) {
                b[i * 20 + j + 76] += b[i * 15 + k + 61] * w[j * 15 + k + 2485];
                dw[j * 15 + k + 2485] += f[i * 20 + j + 127] * b[i * 15 + k + 61];
            }
        }
        {
            int xp = 107, yp = 127, dxp = 96, dyp = 76;
            for (int i = 0; i < 20; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 116, dbp = 2465;
            for (int dcp = 96; dcp < 116; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 29; j++)
            for (int k = 0; k < 20; k++) {
                b[i * 29 + j + 136] += b[i * 20 + k + 116] * w[j * 20 + k + 1885];
                dw[j * 20 + k + 1885] += f[i * 29 + j + 58] * b[i * 20 + k + 116];
            }
        }
        {
            int xp = 29, yp = 58, dxp = 165, dyp = 136;
            for (int i = 0; i < 29; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 194, dbp = 1856;
            for (int dcp = 165; dcp < 194; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 64; j++)
            for (int k = 0; k < 29; k++) {
                dx[i * 64 + j + 0] += b[i * 29 + k + 194] * w[j * 29 + k + 0];
                dw[j * 29 + k + 0] += x[i * 64 + j + 0] * b[i * 29 + k + 194];
            }
        }
    }
}
