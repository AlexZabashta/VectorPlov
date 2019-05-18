package test.meta;
import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class Simple extends VectorDiffStruct {
    public Simple() {
        super(29, 1086, 1, 136, 136);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 0; i < 580; i++)
                w[i] = 0.18569533817705186 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 600; i < 900; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 915; i < 1065; i++)
                w[i] = 0.2581988897471611 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1075; i < 1085; i++)
                w[i] = 0.31622776601683794 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 29; j++)
            for (int k = 0; k < 20; k++)
                f[i * 20 + k + 0] += x[i * 29 + j + 0] * w[j * 20 + k + 0];
        }
        {
            int ap = 0, bp = 580;
            for (int cp = 20; cp < 40; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 20, yp = 40;
            for (int i = 0; i < 20; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++)
                f[i * 15 + k + 60] += f[i * 20 + j + 40] * w[j * 15 + k + 600];
        }
        {
            int ap = 60, bp = 900;
            for (int cp = 75; cp < 90; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 75, yp = 90;
            for (int i = 0; i < 15; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 10; k++)
                f[i * 10 + k + 105] += f[i * 15 + j + 90] * w[j * 10 + k + 915];
        }
        {
            int ap = 105, bp = 1065;
            for (int cp = 115; cp < 125; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 115, yp = 125;
            for (int i = 0; i < 10; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 10; j++)
            for (int k = 0; k < 1; k++)
                f[i * 1 + k + 135] += f[i * 10 + j + 125] * w[j * 1 + k + 1075];
        }
        {
            int ap = 135, bp = 1085;
            for (int cp = 0; cp < 1; cp++)
                y[cp] += f[ap++] + w[bp++];
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            int dap = 0, dbp = 1085;
            for (int dcp = 0; dcp < 1; dcp++) {
                b[dap++] += dy[dcp];
                dw[dbp++] += dy[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 10; j++)
            for (int k = 0; k < 1; k++) {
                b[i * 10 + j + 1] += b[i * 1 + k + 0] * w[j * 1 + k + 1075];
                dw[j * 1 + k + 1075] += f[i * 10 + j + 125] * b[i * 1 + k + 0];
            }
        }
        {
            int xp = 115, yp = 125, dxp = 11, dyp = 1;
            for (int i = 0; i < 10; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 21, dbp = 1065;
            for (int dcp = 11; dcp < 21; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 10; k++) {
                b[i * 15 + j + 31] += b[i * 10 + k + 21] * w[j * 10 + k + 915];
                dw[j * 10 + k + 915] += f[i * 15 + j + 90] * b[i * 10 + k + 21];
            }
        }
        {
            int xp = 75, yp = 90, dxp = 46, dyp = 31;
            for (int i = 0; i < 15; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 61, dbp = 900;
            for (int dcp = 46; dcp < 61; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++) {
                b[i * 20 + j + 76] += b[i * 15 + k + 61] * w[j * 15 + k + 600];
                dw[j * 15 + k + 600] += f[i * 20 + j + 40] * b[i * 15 + k + 61];
            }
        }
        {
            int xp = 20, yp = 40, dxp = 96, dyp = 76;
            for (int i = 0; i < 20; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 116, dbp = 580;
            for (int dcp = 96; dcp < 116; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 29; j++)
            for (int k = 0; k < 20; k++) {
                dx[i * 29 + j + 0] += b[i * 20 + k + 116] * w[j * 20 + k + 0];
                dw[j * 20 + k + 0] += x[i * 29 + j + 0] * b[i * 20 + k + 116];
            }
        }
    }
}
