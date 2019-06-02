package ru.smirnovv.iris.irisDetection.normalization.polarСoordinateSystem;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders;
import ru.smirnovv.iris.irisDetection.normalization.Normalization;

import static java.lang.Math.*;

/**
 * A class that provides normalization of iris.
 * Класс, обеспечивающий нормализацию РОГ.
 */
@Component
public final class PolarCoordinateNormalization extends Normalization {
    /**
     * Parameters for the normalization.
     * Параметры для нормализации.
     */
    private final PolarCoordinateParameters polarCoordinateParameters;

    /**
     * Constructs an instance.
     * Конструктор без параметров.
     */
    @Autowired
    public PolarCoordinateNormalization() {
        polarCoordinateParameters = new PolarCoordinateParameters(true, 128, 256);
    }

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param parameters parameters for the normalization.
     *      параметры для нормализации.
     */
    public PolarCoordinateNormalization(PolarCoordinateParameters parameters) {
        this.polarCoordinateParameters = parameters;
    }

    /**
     * Normalizes iris.
     * Нормализует РОГ.
     *
     * @param originImage the origin iris image.
     *                    оригинальное изображение РОГ.
     * @param irisBorders the borders of iris.
     *                    границы РОГ.
     * @return the normalized iris image.
     *                     нормализованное изображение РОГ.
     */
    public Mat normalize(Mat originImage, IrisBorders irisBorders) {
        if (polarCoordinateParameters.isType()) {
            return transformWithDifferentCentres(originImage, irisBorders);
        } else {
            return transformWithSameCentres(originImage, irisBorders);
        }
    }

    /**
     * Normalizes the iris when the centers match.
     * Нормализует РОГ.
     *
     * @param originImage the origin iris image.
     *                    оригинальное изображение РОГ.
     * @param irisBorders the borders of iris.
     *                    границы РОГ.
     * @return the normalized iris image.
     *                     нормализованное изображение РОГ.
     */
    private Mat transformWithSameCentres(Mat originImage, IrisBorders irisBorders) {
        // Create normalized image
        // Создание нормализованного изображения
        Mat result = new Mat(polarCoordinateParameters.getCols(), polarCoordinateParameters.getRows(), originImage.type());

        // Borders.
        // Границы.
        int xin = irisBorders.getInsideBorder().getX();
        int rin = irisBorders.getInsideBorder().getR();
        int yin = irisBorders.getInsideBorder().getY();
        int ron = rin + (irisBorders.getOutsideBorder().getR() - rin) * 4 / 5;

        // Коэффициенты преобразования.
        double c1 = 1.0d * (1d / (double) polarCoordinateParameters.getCols());
        double c2 = 2.0d * (3.14d / (double) polarCoordinateParameters.getRows());

        // Заполняем нормализованное изображение.
        for (int y = 0; y < polarCoordinateParameters.getCols(); ++y) {
            for (int x = 0; x < polarCoordinateParameters.getRows(); ++x) {
                double p = c1 * y;
                double O = c2 * x;

                // Рассчет координата оригинального изображения.
                int oldX = (int) round((1 - p) * (ron - rin) * cos(O) + (xin + rin * cos(O)));
                int oldY = (int) round((1 - p) * (ron - rin) * sin(O) + (yin + rin * sin(O)));

                result.put(y, x, originImage.get(oldX, oldY));
            }
        }

        return result;
    }

    /**
     * Normalizes the iris when the centers do not match.
     * Нормализует РОГ.
     *
     * @param originImage the origin iris image.
     *                    оригинальное изображение РОГ.
     * @param irisBorders the borders of iris.
     *                    границы РОГ.
     * @return the normalized iris image.
     *                     нормализованное изображение РОГ.
     */
    private Mat transformWithDifferentCentres(Mat originImage, IrisBorders irisBorders) {
        // Create normalized image
        // Создание нормализованного изображения
        Mat result = new Mat(polarCoordinateParameters.getCols(), polarCoordinateParameters.getRows(), originImage.type());

        // Borders.
        // Границы.
        int xin = irisBorders.getInsideBorder().getX();
        int xon = irisBorders.getOutsideBorder().getX();
        int yin = irisBorders.getInsideBorder().getY();
        int yon = irisBorders.getOutsideBorder().getY();
        int rin = irisBorders.getInsideBorder().getR();
        int ron = rin + (irisBorders.getOutsideBorder().getR() - rin) * 4 / 5;

        // Коэффициенты преобразования.
        double c1 = 1.0d * (1d / (double) polarCoordinateParameters.getCols());
        double c2 = 2.0d * (3.14d / (double) polarCoordinateParameters.getRows());

        // Заполняем нормализованное изображение.
        for (int y = 0; y < polarCoordinateParameters.getCols(); ++y) {
            for (int x = 0; x < polarCoordinateParameters.getRows(); ++x) {
                double p = c1 * y;
                double O = c2 * x;

                // Рассчет координата оригинального изображения.
                int oldX = (int) round((1 - p) * (xon + ron * cos(O)) + p * (xin + rin * cos(O)));
                int oldY = (int) round((1 - p) * (yon + ron * sin(O)) + p * (yin + rin * sin(O)));

                result.put(y, x, originImage.get(oldX, oldY));
            }
        }

        return result;
    }
}
