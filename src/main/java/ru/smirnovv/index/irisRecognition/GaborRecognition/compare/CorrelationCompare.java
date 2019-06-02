package ru.smirnovv.index.irisRecognition.GaborRecognition.compare;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.TM_SQDIFF_NORMED;
import static org.opencv.imgproc.Imgproc.matchTemplate;

/**
 * A class that provides irisCode comparison.
 * Класс, обеспечивающий сравнение irisCode.
 */
public class CorrelationCompare extends IrisCodeCompare {
    /**
     * Compares the irisCode.
     * Производит сравнение irisCode.
     *
     * @param iris     the irisCode.
     *                 irisCode.
     * @param template the irisCode.
     *                 irisCode
     * @return the percent of coincidence of the iris.
     * процент совпадения РОГ.
     * @see Imgproc
     */
    @Override
    public double compare(final Mat iris, final Mat template) {
        Mat result = iris.clone();

        // Pattern comparison.
        // Сравнение шаблона.
        matchTemplate(iris, template, result, TM_SQDIFF_NORMED);

        return -1.0 * result.get(0, 0)[0];
    }
}
