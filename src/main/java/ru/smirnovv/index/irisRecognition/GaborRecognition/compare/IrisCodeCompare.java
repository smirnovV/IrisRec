package ru.smirnovv.index.irisRecognition.GaborRecognition.compare;

import org.opencv.core.Mat;

/**
 * An abstract class that provides irisCode comparison.
 * Абстрактный класс, обеспечивающий сравнение irisCode.
 */
public abstract class IrisCodeCompare {
    /**
     * Compares the irisCode.
     * Производит сравнение irisCode.
     *
     * @param iris     the irisCode.
     *                 irisCode.
     * @param template the irisCode.
     *                 irisCode
     * @return the percent of coincidence of the iris.
     *         процент совпадения РОГ.
     */
    public abstract double compare(final Mat iris, final Mat template);

    /**
     * Rotates  iris image to the left.
     * Вращает РОГ влево.
     *
     * @param image the iris image.
     *              изображение РОГ.
     * @return the iris image.
     *         изображение РОГ.
     */
    public final Mat shiftLeft(final Mat image) {
        Mat result = image.clone();

        for (int x = 0; x < result.rows(); ++x) {
            for (int y = 0; y < result.cols(); ++y) {
                result.put(x, y, image.get(x, (result.cols() + y - 1) % result.cols()));
            }
        }

        return result;
    }

    /**
     * Rotates  iris image to the right.
     * Вращает РОГ вправо.
     *
     * @param image the iris image.
     *              изображение РОГ.
     * @return the iris image.
     *         изображение РОГ.
     */
    public final Mat shiftRight(final Mat image) {
        Mat result = image.clone();

        for (int x = 0; x < result.rows(); ++x) {
            for (int y = 0; y < result.cols(); ++y) {
                result.put(x, y, image.get(x, (y + 1) % result.cols()));
            }
        }

        return result;
    }
}
