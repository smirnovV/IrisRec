package ru.smirnovv.iris.irisDetection.localization;

import org.opencv.core.Mat;

import static ru.smirnovv.iris.irisDetection.localization.IrisBorders.Border;

/**
 * Parameters for the localization.
 * Параметры для локализации.
 */
public class LocalizationParameters {
    /**
     * The minimum border radius.
     * минимальный радиус границы.
     */
    final private int minR;

    /**
     * The maximum border radius.
     * Максимальный радиус границы.
     */
    final private int maxR;

    /**
     * The binarization threshold.
     * Порог бинаризации.
     */
    final private int threshold;

    /**
     * The distance from the edge of the image.
     * Расстояние от края изображения.
     */
    final private int distance;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param minR      the minimum border radius.
     *                  минимальный радиус границы.
     * @param maxR      the maximum border radius.
     *                  максимальный радиус границы.
     * @param threshold the binarization threshold.
     *                  порог бинаризации.
     * @param distance  the distance from the edge of the image.
     *                  расстояние от края изображения.
     */
    public LocalizationParameters(final int minR,
                                  final int maxR,
                                  final int threshold,
                                  final int distance) {
        this.minR = minR;
        this.maxR = maxR;
        this.threshold = threshold;
        this.distance = distance;
    }

    /**
     * Returns the minimum border radius.
     * Возвращает минимальный радиус границы.
     *
     * @return the minimum border radius.
     *         минимальный радиус границы.
     */
    public final int getMinR() {
        return minR;
    }

    /**
     * Returns the maximum border radius.
     * Возвращает максимальный радиус границы.
     *
     * @return the maximum border radius.
     *         максимальный радиус границы.
     */
    public final int getMaxR() {
        return maxR;
    }

    /**
     * Returns the binarization threshold.
     * Возвращает порог бинаризации.
     *
     * @return the binarization threshold.
     *         порог бинаризации.
     */
    public final int getThreshold() {
        return threshold;
    }

    /**
     * Returns the distance from the edge of the image.
     * Возвращает расстояние от края изображения.
     *
     * @return the distance from the edge of the image.
     *         расстояние от края изображения.
     */
    public final int getDistance() {
        return distance;
    }

    /**
     * Creates parameters for the outside border.
     * Создает параметры для внешней границы.
     *
     * @param irisImage    the iris image.
     *                     изображение РОГ.
     * @param insideBorder the inside border.
     *                     внутренняя граница.
     * @return the localization parameters.
     *         параметры локализации внешней границы.
     */
    protected LocalizationParameters outsideParameters(final Mat irisImage,
                                                       final Border insideBorder) {
        // The border radius.
        // Радиус границ.
        int minR = (insideBorder.getR() * 2);
        int maxR = (insideBorder.getR() * 4);

        // The binarization threshold.
        // Порог бинаризации.
        int xin = insideBorder.getX();
        int yin = insideBorder.getY();
        int rin = insideBorder.getR();
        int ron = insideBorder.getR() * 2;

        double c1 = 1.0d * (1d / 64d);
        double c2 = 2.0d * (3.14d / 256d);

        // Average and dispersion of neighborhood of the inside border.
        // Средняя яркость и дисперсия окрестности внутренней границы.
        double size = 16384;
        double aver = 0;
        double disp = 0;

        for (int y = 0; y < 64; ++y) {
            for (int x = 0; x < 256; ++x) {
                double p = c1 * y;
                double O = c2 * x;

                int newX = (int) Math.round((1 - p) * (ron - rin) * Math.cos(O) + (xin + rin * Math.cos(O)));
                int newY = (int) Math.round((1 - p) * (ron - rin) * Math.sin(O) + (yin + rin * Math.sin(O)));

                aver = aver + irisImage.get(newX, newY)[0] / size;
            }
        }

        for (int y = 0; y < 64; ++y) {
            for (int x = 0; x < 256; ++x) {
                double p = c1 * y;
                double O = c2 * x;

                int newX = (int) Math.round((1 - p) * (ron - rin) * Math.cos(O) + (xin + rin * Math.cos(O)));
                int newY = (int) Math.round((1 - p) * (ron - rin) * Math.sin(O) + (yin + rin * Math.sin(O)));

                disp = disp + Math.pow(irisImage.get(newX, newY)[0] - aver, 2) / (256 * 64);
            }
        }

        return new LocalizationParameters(minR, maxR, (int) (aver + Math.sqrt(disp) * 2 / 5), distance);
    }
}
