package test.meta;
import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class Decoder extends VectorDiffStruct {
    public Decoder() {
        super(64, 3109, 1, 425, 425);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 64; i < 1920; i++)
                w[i] = 0.125 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1978; i < 2558; i++)
                w[i] = 0.18569533817705186 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 2598; i < 2898; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 2928; i < 3078; i++)
                w[i] = 0.2581988897471611 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 3098; i < 3108; i++)
                w[i] = 0.31622776601683794 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            for (int xp = 0, yp = 0; yp < 64; yp++)
                f[yp] += x[xp++];
        }
        {
            int xp = 0, sp = 0;
            for (int yp = 64; yp < 128; yp++)
                f[yp] += f[xp++] * exp(w[sp++]);
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 64; j++)
            for (int k = 0; k < 29; k++)
                f[i * 29 + k + 128] += f[i * 64 + j + 64] * w[j * 29 + k + 64];
        }
        {
            int ap = 128, bp = 1920;
            for (int cp = 157; cp < 186; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 157, yp = 186;
            for (int i = 0; i < 29; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            int xp = 186, mp = 1949;
            for (int yp = 215; yp < 244; yp++)
                f[yp] += (f[xp++] - w[mp++]);
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 29; j++)
            for (int k = 0; k < 20; k++)
                f[i * 20 + k + 244] += f[i * 29 + j + 215] * w[j * 20 + k + 1978];
        }
        {
            int ap = 244, bp = 2558;
            for (int cp = 264; cp < 284; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 264, yp = 284;
            for (int i = 0; i < 20; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            int xp = 284, mp = 2578;
            for (int yp = 304; yp < 324; yp++)
                f[yp] += (f[xp++] - w[mp++]);
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++)
                f[i * 15 + k + 324] += f[i * 20 + j + 304] * w[j * 15 + k + 2598];
        }
        {
            int ap = 324, bp = 2898;
            for (int cp = 339; cp < 354; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 339, yp = 354;
            for (int i = 0; i < 15; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            int xp = 354, mp = 2913;
            for (int yp = 369; yp < 384; yp++)
                f[yp] += (f[xp++] - w[mp++]);
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 10; k++)
                f[i * 10 + k + 384] += f[i * 15 + j + 369] * w[j * 10 + k + 2928];
        }
        {
            int ap = 384, bp = 3078;
            for (int cp = 394; cp < 404; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 394, yp = 404;
            for (int i = 0; i < 10; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            int xp = 404, mp = 3088;
            for (int yp = 414; yp < 424; yp++)
                f[yp] += (f[xp++] - w[mp++]);
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 10; j++)
            for (int k = 0; k < 1; k++)
                f[i * 1 + k + 424] += f[i * 10 + j + 414] * w[j * 1 + k + 3098];
        }
        {
            int ap = 424, bp = 3108;
            for (int cp = 0; cp < 1; cp++)
                y[cp] += f[ap++] + w[bp++];
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            int dap = 0, dbp = 3108;
            for (int dcp = 0; dcp < 1; dcp++) {
                b[dap++] += dy[dcp];
                dw[dbp++] += dy[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 10; j++)
            for (int k = 0; k < 1; k++) {
                b[i * 10 + j + 1] += b[i * 1 + k + 0] * w[j * 1 + k + 3098];
                dw[j * 1 + k + 3098] += f[i * 10 + j + 414] * b[i * 1 + k + 0];
            }
        }
        {
            int xp = 404, mp = 3088, dmp = 3088, dyp = 1;
            for (int dxp = 11; dxp < 21; dxp++) {
                dw[dmp++] += 0.01 * (w[mp++] - f[xp++]);
                b[dxp] += b[dyp++];
            }
        }
        {
            int xp = 394, yp = 404, dxp = 21, dyp = 11;
            for (int i = 0; i < 10; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 31, dbp = 3078;
            for (int dcp = 21; dcp < 31; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 15; j++)
            for (int k = 0; k < 10; k++) {
                b[i * 15 + j + 41] += b[i * 10 + k + 31] * w[j * 10 + k + 2928];
                dw[j * 10 + k + 2928] += f[i * 15 + j + 369] * b[i * 10 + k + 31];
            }
        }
        {
            int xp = 354, mp = 2913, dmp = 2913, dyp = 41;
            for (int dxp = 56; dxp < 71; dxp++) {
                dw[dmp++] += 0.01 * (w[mp++] - f[xp++]);
                b[dxp] += b[dyp++];
            }
        }
        {
            int xp = 339, yp = 354, dxp = 71, dyp = 56;
            for (int i = 0; i < 15; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 86, dbp = 2898;
            for (int dcp = 71; dcp < 86; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 15; k++) {
                b[i * 20 + j + 101] += b[i * 15 + k + 86] * w[j * 15 + k + 2598];
                dw[j * 15 + k + 2598] += f[i * 20 + j + 304] * b[i * 15 + k + 86];
            }
        }
        {
            int xp = 284, mp = 2578, dmp = 2578, dyp = 101;
            for (int dxp = 121; dxp < 141; dxp++) {
                dw[dmp++] += 0.01 * (w[mp++] - f[xp++]);
                b[dxp] += b[dyp++];
            }
        }
        {
            int xp = 264, yp = 284, dxp = 141, dyp = 121;
            for (int i = 0; i < 20; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 161, dbp = 2558;
            for (int dcp = 141; dcp < 161; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 29; j++)
            for (int k = 0; k < 20; k++) {
                b[i * 29 + j + 181] += b[i * 20 + k + 161] * w[j * 20 + k + 1978];
                dw[j * 20 + k + 1978] += f[i * 29 + j + 215] * b[i * 20 + k + 161];
            }
        }
        {
            int xp = 186, mp = 1949, dmp = 1949, dyp = 181;
            for (int dxp = 210; dxp < 239; dxp++) {
                dw[dmp++] += 0.01 * (w[mp++] - f[xp++]);
                b[dxp] += b[dyp++];
            }
        }
        {
            int xp = 157, yp = 186, dxp = 239, dyp = 210;
            for (int i = 0; i < 29; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 268, dbp = 1920;
            for (int dcp = 239; dcp < 268; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 64; j++)
            for (int k = 0; k < 29; k++) {
                b[i * 64 + j + 297] += b[i * 29 + k + 268] * w[j * 29 + k + 64];
                dw[j * 29 + k + 64] += f[i * 64 + j + 64] * b[i * 29 + k + 268];
            }
        }
        {
            int sp = 0, dxp = 361, dsp = 0, dyp = 297;
            for (int yp = 64; yp < 128; yp++) {
                double sq = f[yp] * f[yp];
                dw[dsp++] += 0.01 * (sq * (sq - 1));
                b[dxp++] += b[dyp++] * exp(w[sp++]);
            }
        }
        {
            double sum = 0.0000001;
            for (int i = 361; i < 425; i++)
                 sum += b[i] * b[i];
            double inv = 1 / sqrt(sum);
            for (int dxp = 0, dyp = 361; dxp < 64; dxp++)
                dx[dxp] += b[dyp++] * inv;
        }
    }
}
