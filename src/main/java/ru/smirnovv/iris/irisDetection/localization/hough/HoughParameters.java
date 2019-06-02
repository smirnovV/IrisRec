package ru.smirnovv.iris.irisDetection.localization.hough;

import org.opencv.core.Mat;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders.Border;
import ru.smirnovv.iris.irisDetection.localization.LocalizationParameters;

/**
 * Parameters for the  Hough localization.
 * Параметры для локализации методом Хафа.
 */
public class HoughParameters extends LocalizationParameters {
    /**
     * The type of localization(
     * true - the center mismatch drops).
     * Тип локализации(
     * true - несоответствие центров опускается).
     */
    final private boolean type;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param type      the type of localization.
     *                  тип локализации.
     * @param minR      the minimum border radius.
     *                  минимальный радиус границы.
     * @param maxR      the maximum border radius.
     *                  максимальный радиус границы.
     * @param threshold the binarization threshold.
     *                  порог бинаризации.
     * @param distance  the distance from the edge of the image.
     *                  расстояние от края изображения.
     */
    public HoughParameters(final boolean type,
                           final int minR,
                           final int maxR,
                           final int threshold,
                           final int distance) {
        super(minR, maxR, threshold, distance);
        this.type = type;
    }

    /**
     * Returns the type of localization.
     * Возвращает тип локализации.
     *
     * @return the type of localization.
     *                  тип локализации.
     */
    public boolean isType() {
        return type;
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
    public HoughParameters outsideParameters(final Mat irisImage,
                                             final Border insideBorder) {
        LocalizationParameters localizationParameters = super.outsideParameters(irisImage, insideBorder);

        return new HoughParameters(true,
                localizationParameters.getMinR(),
                localizationParameters.getMaxR(),
                localizationParameters.getThreshold(),
                localizationParameters.getDistance());
    }
}
