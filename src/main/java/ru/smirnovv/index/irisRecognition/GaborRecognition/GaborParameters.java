package ru.smirnovv.index.irisRecognition.GaborRecognition;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Parameters for the Gabor filter.
 * Параметры для фильтра Габора.
 */
public class GaborParameters {
    /**
     * The real part of the mask of Gabor.
     * Реальная часть маски Габора.
     */
    private final Mat realKernel;

    /**
     * The imaginary part of the mask of Gabor.
     * Мнимая часть маски Габора.
     */
    private final Mat imKernel;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param realKernel the real part of the mask of Gabor.
     *      реальная часть маски Габора.
     * @param imKernel the imaginary part of the mask of Gabor.
     *      мнимая часть маски Габора.
     */
    public GaborParameters(final Mat realKernel, final Mat imKernel) {
        this.realKernel = realKernel;
        this.imKernel = imKernel;
    }

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param sigma sigma for Gabor function.
     *              sigma для функции Габора.
     * @param theta theta for Gabor function.
     *              theta для функции Габора.
     * @param lambd lambd for Gabor function.
     *              lambd для функции Габора.
     * @param gamma gamma for Gabor function.
     *              gamma для функции Габора.
     */
    public GaborParameters(final double sigma,
                           final double theta,
                           final double lambd,
                           final double gamma) {
        realKernel = Imgproc.getGaborKernel(new Size(6 * sigma + 1, 6 * sigma + 1),
                sigma, theta, lambd, gamma, 0, 5);
        imKernel = Imgproc.getGaborKernel(new Size(6 * sigma + 1, 6 * sigma + 1),
                sigma, theta, lambd, gamma, Math.PI / 2, 5);
    }

    /**
     * Returns the real part of the mask of Gabor.
     * Возвращает реальную часть маски Габора.
     *
     * @return the real part of the mask of Gabor.
     * реальная часть маски Габора.
     */
    public Mat getRealKernel() {
        return realKernel;
    }

    /**
     * Returns the imaginary part of the mask of Gabor.
     * Возвращает мнимую часть маски Габора.
     *
     * @return the imaginary part of the mask of Gabor.
     * мнимая часть маски Габора.
     */
    public Mat getImKernel() {
        return imKernel;
    }
}
