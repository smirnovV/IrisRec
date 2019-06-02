package ru.smirnovv.iris.irisDetection.localization.daugman;

import org.opencv.core.Mat;
import ru.smirnovv.iris.irisDetection.localization.LocalizationParameters;

import static ru.smirnovv.iris.irisDetection.localization.IrisBorders.Border;

/**
 * Parameters for the  Daugman localization.
 * Параметры для локализации методом Даугман.
 */
public class DaugmanParameters extends LocalizationParameters {
    /**
     * The area near the centers of the inside border.
     * Область около центра внутренней границы
     */
    final private int area;

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
     * @param area      the area near the inside border.
     *                  область около центра внутренней границы.
     */
    public DaugmanParameters(int minR,
                             int maxR,
                             int threshold,
                             int distance,
                             int area) {
        super(minR, maxR, threshold, distance);
        this.area = area;
    }

    /**
     * Returns the area near the inside border.
     * Возвразает область около центра внутренней границы.
     *
     * @return the area near the inside border.
     * область около центра внутренней границы.
     */
    public int getArea() {
        return area;
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
     * параметры локализации внешней границы.
     */
    public DaugmanParameters outsideParameters(final Mat irisImage,
                                               final Border insideBorder) {
        LocalizationParameters localizationParameters = super.outsideParameters(irisImage, insideBorder);

        return new DaugmanParameters(localizationParameters.getMinR(),
                localizationParameters.getMaxR(),
                localizationParameters.getThreshold(),
                localizationParameters.getDistance(),
                3);
    }
}
