package ru.smirnovv.index.irisRecognition.GaborRecognition.compare;

import org.opencv.core.Mat;

/**
 * A class that provides irisCode comparison.
 * Класс, обеспечивающий сравнение irisCode.
 */
public class HammingDistanceCompare extends IrisCodeCompare {
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
    @Override
    public double compare(final Mat iris, final Mat template) {
        double result = 0;
        double size = iris.cols() * iris.rows();

        // Counts Hamming distance.
        // Подсчет расстояния Хэмминга.
        for (int x = 0; x < iris.rows(); x = x + 8) {
            for (int y = 0; y < iris.cols(); y = y + 4) {
                if ((iris.get(x, y)[0] <= 128 && template.get(x, y)[0] <= 128) ||
                        (!(iris.get(x, y)[0] <= 128) && !(template.get(x, y)[0] <= 128))) {
                    result = result + 1 / size;
                }
            }
        }

        return -1.0 * result;
    }
}
