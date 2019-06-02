package ru.smirnovv.index.irisRecognition.GaborRecognition;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnovv.index.irisRecognition.GaborRecognition.compare.HammingDistanceCompare;
import ru.smirnovv.index.irisRecognition.GaborRecognition.compare.IrisCodeCompare;
import ru.smirnovv.index.irisRecognition.IrisCompare;

/**
 * A class that provides iris comparison.
 * Класс, обеспечивающий сравнение РОГ.
 */
@Component
public class GaborCompare extends IrisCompare {
    /**
     * Parameters for Gabor filter.
     * Параметры для фильтра Габора.
     */
    private final GaborParameters gaborParameters;

    /**
     * A class that provides IrisCode comparison.
     * Класс, обеспечивающий сравнение IrisCode.
     */
    private final IrisCodeCompare irisCodeCompare;

    /**
     * Identification threshold.
     * Порог идентификации.
     */
    private final double threshold;

    /**
     * Constructs an instance.
     * Конструктор без параметров.
     */
    @Autowired
    public GaborCompare() {
        this.gaborParameters = new GaborParameters(10, 0, 3, 1);
        this.irisCodeCompare = new HammingDistanceCompare();
        this.threshold = -0.3;
    }

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param gaborParameters parameters for Gabor filter.
     *                        параметры для фильтра Габора.
     * @param irisCodeCompare a class that provides IrisCode comparison.
     *                        класс, обеспечивающий сравнение IrisCode.
     * @param threshold       identification threshold.
     *                        порог идентификации.
     */
    public GaborCompare(GaborParameters gaborParameters, IrisCodeCompare irisCodeCompare, double threshold) {
        this.gaborParameters = gaborParameters;
        this.irisCodeCompare = irisCodeCompare;
        this.threshold = threshold;
    }

    /**
     * Creates a parameterized image of iris.
     * Создает параметризованное изображение РОГ.
     *
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @param kernel    the mask of the Gabor filter.
     *                  маска фильтра Габора.
     * @return a parameterized image of iris.
     *         параметризованное изображение РОГ.
     */
    private Mat parameterize(final Mat irisImage, final Mat kernel) {
        Mat iris = irisImage.clone();
        Imgproc.filter2D(irisImage, iris, 0, kernel);
        return iris;
    }

    /**
     * Compares the iris.
     * Производит сравнение РОГ.
     *
     * @param irisImage    the image of the iris.
     *                     изображение РОГ.
     * @param irisTemplate the image of the iris.
     *                     изображение РОГ.
     * @return the percent of coincidence of the iris.
     *         процент совпадения РОГ.
     */
    public double compare(final Mat irisImage, final Mat irisTemplate) {
        // Applies Gabor filters.
        // Применение фильтров Габора.
        Mat realPartOfIrisImage = parameterize(irisImage, gaborParameters.getRealKernel());
        Mat imPartOfIrisImage = parameterize(irisImage, gaborParameters.getImKernel());
        Mat realPartOfIrisTemplate = parameterize(irisTemplate, gaborParameters.getRealKernel());
        Mat imPartOfIrisTemplate = parameterize(irisTemplate, gaborParameters.getImKernel());

        // Creates a matrices for rotation iris.
        // Создание матриц для алгоритма поворота РОГ.
        Mat realShiftLeft = realPartOfIrisImage.clone();
        Mat imShiftLeft = imPartOfIrisImage.clone();
        Mat realShiftRight = realPartOfIrisImage.clone();
        Mat imShiftRight = imPartOfIrisImage.clone();

        // Searches matches.
        // Поиск совпадений.
        double min = (irisCodeCompare.compare(realPartOfIrisImage, realPartOfIrisTemplate)
                + irisCodeCompare.compare(imPartOfIrisImage, imPartOfIrisTemplate)) / 2;

        for (int i = 0; i < 2; ++i) {
            // Rotations iris.
            // Вращение РОГ.
            realShiftLeft = irisCodeCompare.shiftLeft(realShiftLeft);
            imShiftLeft = irisCodeCompare.shiftLeft(imShiftLeft);
            realShiftRight = irisCodeCompare.shiftRight(realShiftRight);
            imShiftRight = irisCodeCompare.shiftRight(imShiftRight);

            // Searches matches.
            // Поиск совпадений.
            double result = (irisCodeCompare.compare(realShiftLeft, realPartOfIrisTemplate)
                    + irisCodeCompare.compare(imShiftLeft, imPartOfIrisTemplate)) / 2;
            if (result < min) {
                min = result;
            }

            result = (irisCodeCompare.compare(realShiftRight, realPartOfIrisTemplate)
                    + irisCodeCompare.compare(imShiftRight, imPartOfIrisTemplate)) / 2;
            if (result < min) {
                min = result;
            }
        }

        return min;
    }

    /**
     * Compares the iris to authorize a person.
     * Производит сравнение РОГ для авторизации человека.
     *
     * @param irisImage    the image of the iris.
     *                     изображение РОГ.
     * @param irisTemplate the image of the iris.
     *                     изображение РОГ.
     * @return {@code true} if the iris coincided.
     *                      подтверждение совпадения РОГ.
     */
    public boolean authorize(final Mat irisImage, final Mat irisTemplate) {
        return compare(irisImage, irisTemplate) < threshold;
    }
}
