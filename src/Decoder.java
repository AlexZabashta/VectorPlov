import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class Decoder extends VectorDiffStruct {
    public Decoder() {
        super(40, 1961, 1, 332, 332);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 40; i < 1240; i++)
                w[i] = 0.15811388300841897 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1300; i < 1900; i++)
                w[i] = 0.18257418583505536 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1940; i < 1960; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            int xp = 0, sp = 0;
            for (int yp = 0; yp < 40; yp++)
                f[yp] += x[xp++] * exp(w[sp++]);
        }
        {
            for (int xp = 0, yp = 40; yp < 80; yp++)
                f[yp] += f[xp++];
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 40; j++)
            for (int k = 0; k < 30; k++)
                f[i * 30 + k + 80] += f[i * 40 + j + 40] * w[j * 30 + k + 40];
        }
        {
            int ap = 80, bp = 1240;
            for (int cp = 110; cp < 140; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 110, yp = 140;
            for (int i = 0; i < 30; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            int xp = 140, mp = 1270;
            for (int yp = 170; yp < 200; yp++)
                f[yp] += (f[xp++] - w[mp++]);
        }
        {
            for (int xp = 170, yp = 200; yp < 230; yp++)
                f[yp] += f[xp++];
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 30; j++)
            for (int k = 0; k < 20; k++)
                f[i * 20 + k + 230] += f[i * 30 + j + 200] * w[j * 20 + k + 1300];
        }
        {
            int ap = 230, bp = 1900;
            for (int cp = 250; cp < 270; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 250, yp = 270;
            for (int i = 0; i < 20; i++, xp++) {
                f[yp++] += max(0.001 * f[xp], f[xp]);
            }
        }
        {
            int xp = 270, mp = 1920;
            for (int yp = 290; yp < 310; yp++)
                f[yp] += (f[xp++] - w[mp++]);
        }
        {
            for (int xp = 290, yp = 310; yp < 330; yp++)
                f[yp] += f[xp++];
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 1; k++)
                f[i * 1 + k + 330] += f[i * 20 + j + 310] * w[j * 1 + k + 1940];
        }
        {
            int ap = 330, bp = 1960;
            for (int cp = 331; cp < 332; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 331, yp = 0;
            for (int i = 0; i < 1; i++, xp++) {
                y[yp++] += tanh(f[xp]);
            }
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            int xp = 331, yp = 0, dxp = 0, dyp = 0;
            for (int i = 0; i < 1; i++, xp++, yp++) {
                b[dxp++] += dy[dyp++] * (1 - y[yp] * y[yp]);
            }
        }
        {
            int dap = 1, dbp = 1960;
            for (int dcp = 0; dcp < 1; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 1; k++) {
                b[i * 20 + j + 2] += b[i * 1 + k + 1] * w[j * 1 + k + 1940];
                dw[j * 1 + k + 1940] += f[i * 20 + j + 310] * b[i * 1 + k + 1];
            }
        }
        {
            double sum = 0.0000001;
            for (int i = 2; i < 22; i++)
                 sum += b[i] * b[i];
            double inv = 1 / sqrt(sum);
            for (int dxp = 22, dyp = 2; dxp < 42; dxp++)
                b[dxp] += b[dyp++] * inv;
        }
        {
            int xp = 270, mp = 1920, dmp = 1920, dyp = 22;
            for (int dxp = 42; dxp < 62; dxp++) {
                dw[dmp++] += 0.01 * (w[mp++] - f[xp++]);
                b[dxp] += b[dyp++];
            }
        }
        {
            int xp = 250, yp = 270, dxp = 62, dyp = 42;
            for (int i = 0; i < 20; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 82, dbp = 1900;
            for (int dcp = 62; dcp < 82; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 30; j++)
            for (int k = 0; k < 20; k++) {
                b[i * 30 + j + 102] += b[i * 20 + k + 82] * w[j * 20 + k + 1300];
                dw[j * 20 + k + 1300] += f[i * 30 + j + 200] * b[i * 20 + k + 82];
            }
        }
        {
            double sum = 0.0000001;
            for (int i = 102; i < 132; i++)
                 sum += b[i] * b[i];
            double inv = 1 / sqrt(sum);
            for (int dxp = 132, dyp = 102; dxp < 162; dxp++)
                b[dxp] += b[dyp++] * inv;
        }
        {
            int xp = 140, mp = 1270, dmp = 1270, dyp = 132;
            for (int dxp = 162; dxp < 192; dxp++) {
                dw[dmp++] += 0.01 * (w[mp++] - f[xp++]);
                b[dxp] += b[dyp++];
            }
        }
        {
            int xp = 110, yp = 140, dxp = 192, dyp = 162;
            for (int i = 0; i < 30; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * ((f[xp] < 0) ? 0.001 : 1);
            }
        }
        {
            int dap = 222, dbp = 1240;
            for (int dcp = 192; dcp < 222; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 40; j++)
            for (int k = 0; k < 30; k++) {
                b[i * 40 + j + 252] += b[i * 30 + k + 222] * w[j * 30 + k + 40];
                dw[j * 30 + k + 40] += f[i * 40 + j + 40] * b[i * 30 + k + 222];
            }
        }
        {
            double sum = 0.0000001;
            for (int i = 252; i < 292; i++)
                 sum += b[i] * b[i];
            double inv = 1 / sqrt(sum);
            for (int dxp = 292, dyp = 252; dxp < 332; dxp++)
                b[dxp] += b[dyp++] * inv;
        }
        {
            int sp = 0, dxp = 0, dsp = 0, dyp = 292;
            for (int yp = 0; yp < 40; yp++) {
                double sq = f[yp] * f[yp];
                dw[dsp++] += 0.01 * (sq * (sq - 1));
                dx[dxp++] += b[dyp++] * exp(w[sp++]);
            }
        }
    }
}
