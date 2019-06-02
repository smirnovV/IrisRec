package ru.smirnovv.iris.irisDetection.localization.daugman;

import org.opencv.core.Mat;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders.Border;
import ru.smirnovv.iris.irisDetection.localization.Localization;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 *
 */
//@Component
public class DaugmanLocalization extends Localization {
    /**
     *
     */
    private final DaugmanParameters parameters;

    /**
     *
     * @param parameters
     */
    public DaugmanLocalization(final DaugmanParameters parameters) {
        this.parameters = parameters;
    }

//            super(20, 60, 60, 20);
//        this.area = 2;
    /**
     *
     * @param irisImage
     * @return
     */
    public IrisBorders localize(final Mat irisImage) {
        Border inside = border(irisImage, parameters, null);

        DaugmanParameters outsideSettings = this.parameters.outsideParameters(irisImage, inside);

        Border outside = border(irisImage, outsideSettings, inside);

        return new IrisBorders(inside, outside);
    }

    /**
     *
     * @param in
     * @param parameters
     * @param inside
     * @return
     */
    private Border border(final Mat in, final DaugmanParameters parameters, final Border inside) {
        Mat mat = in.clone();

        int gauss_sigma = 3;

        //Outside Border
        threshold(mat, mat, parameters.getThreshold(), 100, THRESH_BINARY_INV);

        int x, y;
        if (inside != null) {
            x = outsideX(mat, parameters.getDistance(), in.rows() - parameters.getDistance(), inside.getX(), parameters.getArea());
            y = outsideY(mat, parameters.getDistance(), in.cols() - parameters.getDistance(), inside.getY(), parameters.getArea());
        } else {
            x = insideX(mat, parameters.getDistance(), in.rows() - parameters.getDistance());
            y = insideY(mat, parameters.getDistance(), in.cols() - parameters.getDistance());
        }


        threshold(mat, mat, parameters.getThreshold(), 100, THRESH_BINARY_INV);
        double max = 0;
        int resX = 0;
        int resY = 0;
        int resR = 0;

        for (int i = x - parameters.getArea(); i < x + parameters.getArea(); i++) {
            for (int j = y - parameters.getArea(); j < y + parameters.getArea(); j++) {
                for (int r = parameters.getMinR(); r < parameters.getMaxR(); r++) {
                    double sum = 0;

                    for (int k = r - 3 * gauss_sigma; k < r + 3 * gauss_sigma; k++) {
                        double def = funcGauss(r - k + 1, parameters) - funcGauss(r - k, parameters);
                        sum = sum + def * sumCircle(i, j, k, mat);
                    }

                    if (sum > max) {
                        max = sum;
                        resX = i;
                        resY = j;
                        resR = r;
                    }
                }
            }
        }

        return new Border(resX, resY, resR);
    }

    /**
     *
     * @param x
     * @param parameters
     * @return
     */
    private double funcGauss(final int x, final DaugmanParameters parameters) {
        double gauss_1div_sigma_sqrt_2_pi = 1 / (3 * Math.sqrt(6.28));
        double gauss_2_sqr_sigma = 18.0;

        return gauss_1div_sigma_sqrt_2_pi *
                Math.exp(-1.0 * Math.pow(x, 2) / gauss_2_sqr_sigma);
    }

    /**
     *
     * @param X
     * @param Y
     * @param R
     * @param in
     * @return
     */
    private int sumCircle(final int X, final int Y, final int R, final Mat in) {

        int sum = 0;

        int x1 = X + R;
        int y1 = Y;

        do {
            if (0 <= x1 && x1 < in.rows() && 0 <= y1 && y1 < in.cols())
                sum = sum + (int) (in.get(x1, y1)[0]);
            if (0 <= X - (x1 - X) && X - (x1 - X) < in.rows() && 0 <= y1 && y1 < in.cols())
                sum = sum + (int) in.get(X - (x1 - X), y1)[0];
            if (0 <= X - (x1 - X) && X - (x1 - X) < in.rows() && 0 <= Y - (y1 - Y) && Y - (y1 - Y) < in.cols())
                sum = sum + (int) in.get(X - (x1 - X), Y - (y1 - Y))[0];
            if (0 <= x1 && x1 < in.rows() && 0 <= Y - (y1 - Y) && Y - (y1 - Y) < in.cols())
                sum = sum + (int) in.get(x1, Y - (y1 - Y))[0];

            ++y1;
            double x1_1 = Math.sqrt(R * R - (y1 - Y) * (y1 - Y));
            x1 = (int) Math.round(X + x1_1);

        } while (x1 != X);

        return sum;
    }

    /**
     *
     * @param image
     * @param begin
     * @param end
     * @return
     */
    private int insideX(final Mat image, final int begin, final int end) {
        int max = 0;
        int x = 0;

        for (int i = begin; i < end; i++) {
            int sum = 0;
            for (int j = 0; j < image.cols(); j++) {
                sum = sum + (int) image.get(i, j)[0];
            }
            if (sum > max) {
                max = sum;
                x = i;
            }
        }
        return x;
    }

    /**
     *
     * @param image
     * @param begin
     * @param end
     * @return
     */
    private int insideY(final Mat image, final int begin, final int end) {
        int max = 0;
        int y = 0;

        for (int i = begin; i < end; i++) {
            int sum = 0;
            for (int j = 0; j < image.rows(); j++) {
                sum = sum + (int) image.get(j, i)[0];
            }
            if (sum > max) {
                max = sum;
                y = i;
            }
        }

        return y;
    }

    /**
     *
     * @param image
     * @param begin
     * @param end
     * @param areaX
     * @param areaSize
     * @return
     */
    private int outsideX(final Mat image, final int begin, final int end, final int areaX, final int areaSize) {
        int max = 0;
        int x = 0;

        for (int i = begin; i < end; i++) {
            int sum = 0;
            for (int j = 0; j < image.cols(); j++) {
                sum = sum + (int) image.get(i, j)[0];
            }
            if (sum > max && areaX - areaSize <= i && i <= areaX + areaSize) {
                max = sum;
                x = i;
            }
        }

        return x;
    }

    /**
     *
     * @param image
     * @param begin
     * @param end
     * @param areaY
     * @param areaSize
     * @return
     */
    private int outsideY(final Mat image, final int begin, final int end, final int areaY, final int areaSize) {
        int max = 0;
        int y = 0;

        for (int i = begin; i < end; i++) {
            int sum = 0;
            for (int j = 0; j < image.rows(); j++) {
                sum = sum + (int) image.get(j, i)[0];
            }
            if (sum > max && areaY - areaSize <= i && i <= areaY + areaSize) {
                max = sum;
                y = i;
            }
        }

        return y;
    }
}
