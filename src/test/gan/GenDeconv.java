package test.gan;
import  static java.lang.Math.*;
import core.VectorDiffStruct;
import java.util.Random;
public class GenDeconv extends VectorDiffStruct {
    public GenDeconv() {
        super(36, 2352, 64, 496, 496);
    }
    @Override
    public void init(double[] w) {
        {
            Random random = new Random();
            for (int i = 1008; i < 1328; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 336; i < 656; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 672; i < 992; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 0; i < 320; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 2016; i < 2336; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1344; i < 1664; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
        {
            Random random = new Random();
            for (int i = 1680; i < 2000; i++)
                w[i] = 0.22360679774997896 * random.nextGaussian();
        }
    }
    @Override
    public void forward(double[] x, double[] w, double[] y, double[] f) {
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 0] += x[i * 20 + j + 0] * w[j * 16 + k + 1008];
        }
        {
            int ap = 0, bp = 1328;
            for (int cp = 16; cp < 32; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 16, yp = 32;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += (1 / (1 + exp(-(f[xp]))));
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 48] += x[i * 20 + j + 0] * w[j * 16 + k + 336];
        }
        {
            int ap = 48, bp = 656;
            for (int cp = 64; cp < 80; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 64, yp = 80;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += (1 / (1 + exp(-(f[xp]))));
            }
        }
        {
            int ap = 80, bp = 20;
            for (int cp = 96; cp < 112; cp++)
                f[cp] += f[ap++] * x[bp++];
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 112] += x[i * 20 + j + 0] * w[j * 16 + k + 672];
        }
        {
            int ap = 112, bp = 992;
            for (int cp = 128; cp < 144; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 128, yp = 144;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += (1 / (1 + exp(-(f[xp]))));
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 160] += x[i * 20 + j + 0] * w[j * 16 + k + 0];
        }
        {
            int ap = 160, bp = 320;
            for (int cp = 176; cp < 192; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 176, yp = 192;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            int ap = 144, bp = 192;
            for (int cp = 208; cp < 224; cp++)
                f[cp] += f[ap++] * f[bp++];
        }
        {
            int ap = 96, bp = 208;
            for (int cp = 224; cp < 240; cp++)
                f[cp] += f[ap++] + f[bp++];
        }
        {
            int xp = 224, yp = 240;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            int ap = 32, bp = 240;
            for (int cp = 256; cp < 272; cp++)
                f[cp] += f[ap++] * f[bp++];
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 272] += x[i * 20 + j + 0] * w[j * 16 + k + 2016];
        }
        {
            int ap = 272, bp = 2336;
            for (int cp = 288; cp < 304; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 288, yp = 304;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += (1 / (1 + exp(-(f[xp]))));
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 320] += x[i * 20 + j + 0] * w[j * 16 + k + 1344];
        }
        {
            int ap = 320, bp = 1664;
            for (int cp = 336; cp < 352; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 336, yp = 352;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += (1 / (1 + exp(-(f[xp]))));
            }
        }
        {
            int ap = 352, bp = 20;
            for (int cp = 368; cp < 384; cp++)
                f[cp] += f[ap++] * x[bp++];
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++)
                f[i * 16 + k + 384] += x[i * 20 + j + 0] * w[j * 16 + k + 1680];
        }
        {
            int ap = 384, bp = 2000;
            for (int cp = 400; cp < 416; cp++)
                f[cp] += f[ap++] + w[bp++];
        }
        {
            int xp = 400, yp = 416;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += (1 / (1 + exp(-(f[xp]))));
            }
        }
        {
            int ap = 416, bp = 192;
            for (int cp = 432; cp < 448; cp++)
                f[cp] += f[ap++] * f[bp++];
        }
        {
            int ap = 368, bp = 432;
            for (int cp = 448; cp < 464; cp++)
                f[cp] += f[ap++] + f[bp++];
        }
        {
            int xp = 448, yp = 464;
            for (int i = 0; i < 16; i++, xp++) {
                f[yp++] += tanh(f[xp]);
            }
        }
        {
            int ap = 304, bp = 464;
            for (int cp = 480; cp < 496; cp++)
                f[cp] += f[ap++] * f[bp++];
        }
        {
            int tp = 0;
            for (int fp = 256; fp < 272; fp++)
                y[tp++] += f[fp];
            for (int fp = 224; fp < 240; fp++)
                y[tp++] += f[fp];
            for (int fp = 480; fp < 496; fp++)
                y[tp++] += f[fp];
            for (int fp = 448; fp < 464; fp++)
                y[tp++] += f[fp];
        }
    }
    @Override
    public void backward(double[] x, double[] w, double[] y, double[] dx, double[] dw, double[] dy, double[] f, double[] b) {
        {
            int dtp = 0;
            for (int dfp = 256; dfp < 272; dfp++)
                f[dfp] += y[dtp++];
            for (int dfp = 224; dfp < 240; dfp++)
                f[dfp] += y[dtp++];
            for (int dfp = 480; dfp < 496; dfp++)
                f[dfp] += y[dtp++];
            for (int dfp = 448; dfp < 464; dfp++)
                f[dfp] += y[dtp++];
        }
        {
            int ap = 304, bp = 464;
            int dap = 176, dbp = 16;
            for (int dcp = 0; dcp < 16; dcp++) {
                b[dap++] += b[dcp] * f[bp++];
                b[dbp++] += b[dcp] * f[ap++];
            }
        }
        {
            int xp = 448, yp = 464, dxp = 32, dyp = 16;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 112, dbp = 48;
            for (int dcp = 32; dcp < 48; dcp++) {
                b[dap++] += b[dcp];
                b[dbp++] += b[dcp];
            }
        }
        {
            int ap = 416, bp = 192;
            int dap = 64, dbp = 288;
            for (int dcp = 48; dcp < 64; dcp++) {
                b[dap++] += b[dcp] * f[bp++];
                b[dbp++] += b[dcp] * f[ap++];
            }
        }
        {
            int xp = 400, yp = 416, dxp = 80, dyp = 64;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - (f[yp])) * (f[yp]);
            }
        }
        {
            int dap = 96, dbp = 2000;
            for (int dcp = 80; dcp < 96; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 96] * w[j * 16 + k + 1680];
                dw[j * 16 + k + 1680] += x[i * 20 + j + 0] * b[i * 16 + k + 96];
            }
        }
        {
            int ap = 352, bp = 20;
            int dap = 128, dbp = 20;
            for (int dcp = 112; dcp < 128; dcp++) {
                b[dap++] += b[dcp] * x[bp++];
                dx[dbp++] += b[dcp] * f[ap++];
            }
        }
        {
            int xp = 336, yp = 352, dxp = 144, dyp = 128;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - (f[yp])) * (f[yp]);
            }
        }
        {
            int dap = 160, dbp = 1664;
            for (int dcp = 144; dcp < 160; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 160] * w[j * 16 + k + 1344];
                dw[j * 16 + k + 1344] += x[i * 20 + j + 0] * b[i * 16 + k + 160];
            }
        }
        {
            int xp = 288, yp = 304, dxp = 192, dyp = 176;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - (f[yp])) * (f[yp]);
            }
        }
        {
            int dap = 208, dbp = 2336;
            for (int dcp = 192; dcp < 208; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 208] * w[j * 16 + k + 2016];
                dw[j * 16 + k + 2016] += x[i * 20 + j + 0] * b[i * 16 + k + 208];
            }
        }
        {
            int ap = 32, bp = 240;
            int dap = 448, dbp = 240;
            for (int dcp = 224; dcp < 240; dcp++) {
                b[dap++] += b[dcp] * f[bp++];
                b[dbp++] += b[dcp] * f[ap++];
            }
        }
        {
            int xp = 224, yp = 240, dxp = 256, dyp = 240;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 384, dbp = 272;
            for (int dcp = 256; dcp < 272; dcp++) {
                b[dap++] += b[dcp];
                b[dbp++] += b[dcp];
            }
        }
        {
            int ap = 144, bp = 192;
            int dap = 336, dbp = 288;
            for (int dcp = 272; dcp < 288; dcp++) {
                b[dap++] += b[dcp] * f[bp++];
                b[dbp++] += b[dcp] * f[ap++];
            }
        }
        {
            int xp = 176, yp = 192, dxp = 304, dyp = 288;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - f[yp] * f[yp]);
            }
        }
        {
            int dap = 320, dbp = 320;
            for (int dcp = 304; dcp < 320; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 320] * w[j * 16 + k + 0];
                dw[j * 16 + k + 0] += x[i * 20 + j + 0] * b[i * 16 + k + 320];
            }
        }
        {
            int xp = 128, yp = 144, dxp = 352, dyp = 336;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - (f[yp])) * (f[yp]);
            }
        }
        {
            int dap = 368, dbp = 992;
            for (int dcp = 352; dcp < 368; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 368] * w[j * 16 + k + 672];
                dw[j * 16 + k + 672] += x[i * 20 + j + 0] * b[i * 16 + k + 368];
            }
        }
        {
            int ap = 80, bp = 20;
            int dap = 400, dbp = 20;
            for (int dcp = 384; dcp < 400; dcp++) {
                b[dap++] += b[dcp] * x[bp++];
                dx[dbp++] += b[dcp] * f[ap++];
            }
        }
        {
            int xp = 64, yp = 80, dxp = 416, dyp = 400;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - (f[yp])) * (f[yp]);
            }
        }
        {
            int dap = 432, dbp = 656;
            for (int dcp = 416; dcp < 432; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 432] * w[j * 16 + k + 336];
                dw[j * 16 + k + 336] += x[i * 20 + j + 0] * b[i * 16 + k + 432];
            }
        }
        {
            int xp = 16, yp = 32, dxp = 464, dyp = 448;
            for (int i = 0; i < 16; i++, xp++, yp++) {
                b[dxp++] += b[dyp++] * (1 - (f[yp])) * (f[yp]);
            }
        }
        {
            int dap = 480, dbp = 1328;
            for (int dcp = 464; dcp < 480; dcp++) {
                b[dap++] += b[dcp];
                dw[dbp++] += b[dcp];
            }
        }
        {
            for (int i = 0; i < 1; i++)
            for (int j = 0; j < 20; j++)
            for (int k = 0; k < 16; k++) {
                dx[i * 20 + j + 0] += b[i * 16 + k + 480] * w[j * 16 + k + 1008];
                dw[j * 16 + k + 1008] += x[i * 20 + j + 0] * b[i * 16 + k + 480];
            }
        }
    }
}
