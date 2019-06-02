package ru.smirnovv.iris.irisDetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.springframework.stereotype.Component;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders;
import ru.smirnovv.iris.irisDetection.localization.Localization;
import ru.smirnovv.iris.irisDetection.normalization.Normalization;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imdecode;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * A class that provides iris detection.
 * Класс, обеспечивающий детектирование РОГ.
 */
@Component
public class IrisDetector {
    /**
     * A class that provides localization of iris.
     * Класс, обеспечивающий локализацию РОГ.
     */
    final private Localization localization;

    /**
     * A class that provides normalization of iris.
     * Класс, обеспечивающий нормализацию РОГ.
     */
    final private Normalization normalization;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param localization  a class that provides localization of iris.
     *                      класс, обеспечивающий локализацию РОГ.
     * @param normalization a class that provides normalization of iris.
     *                      класс, обеспечивающий нормализацию РОГ.
     */
    public IrisDetector(Localization localization, Normalization normalization) {
        this.localization = localization;
        this.normalization = normalization;
    }

    /**
     * Converts byte array to the mat.
     * Конвертирует массив байт в матрицу.
     *
     * @param image the iris image.
     *              изображение РОГ.
     * @return the iris image.
     * изображение РОГ.
     */
    private Mat createIrisImage(byte[] image) {
        // Converts byte array to the mat.
        // Конвертирует массив байт в матрицу.
        Mat irisImage = imdecode(new MatOfByte(image), IMREAD_COLOR);

        // Converts RGB to GrayScale.
        // Конвертирует RGB в GrayScale.
        cvtColor(irisImage, irisImage, COLOR_BGR2GRAY);

        return irisImage;
    }

    /**
     * Detects iris.
     *
     * @param image the iris image.
     *              изображение РОГ.
     * @return irisCode.
     */
    public byte[] createIrisCode(byte[] image) {
        try {
            Mat irisImage = createIrisImage(image);

            // Локализация РОГ.
            IrisBorders borders = localization.localize(irisImage);

            // Нормализация РОГ.
            Mat normalizedIris = normalization.normalize(irisImage, borders);

            // Converts the mat to byte array.
            // Конвертирует матрицу в массив байт.
            int length = (int) (normalizedIris.total() * normalizedIris.elemSize());
            byte[] buffer = new byte[length];
            normalizedIris.get(0, 0, buffer);

            return buffer;
        } catch (Exception exception) {
            // If iris not found throw exception.
            // Если РОГ не обнаружен выбросить исключение.
            throw new IrisNotFoundException("Iris not found or the eye is not fully visible.");
        }
    }
}
