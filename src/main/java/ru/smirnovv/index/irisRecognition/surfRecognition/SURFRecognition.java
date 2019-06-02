package ru.smirnovv.index.irisRecognition.surfRecognition;

import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.xfeatures2d.SURF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnovv.index.irisRecognition.IrisCompare;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides iris comparison.
 * Класс, обеспечивающий сравнение РОГ.
 */
@Component
public class SURFRecognition extends IrisCompare {
    /**
     * SURF detector.
     * SURF детектор.
     */
    private final SURF surf;

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
    public SURFRecognition() {
        this.surf = SURF.create(50, 4, 3, false, false);
        this.threshold = 0.02;
    }

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param surf      SURF detector.
     *                  SURF детектор.
     * @param threshold Identification threshold.
     *                  Порог идентификации.
     */
    public SURFRecognition(SURF surf, double threshold) {
        this.surf = surf;
        this.threshold = threshold;
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
    @Override
    public boolean authorize(final Mat irisImage, final Mat irisTemplate) {
        return compare(irisImage, irisTemplate) < threshold;
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
    @Override
    public final double compare(final Mat irisImage, final Mat irisTemplate) {
        // Key points and descriptors.
        // Особые точки и описательные элементы.
        MatOfKeyPoint keyPointImage = new MatOfKeyPoint();
        MatOfKeyPoint keyPointTempl = new MatOfKeyPoint();
        Mat descriptorImage = new Mat();
        Mat descriptorTempl = new Mat();

        // Searches key points and creates descriptors.
        // Поиск особых точек и построение описательного элемента.
        surf.detectAndCompute(irisImage, new Mat(), keyPointImage, descriptorImage);
        surf.detectAndCompute(irisTemplate, new Mat(), keyPointTempl, descriptorTempl);

        // Searches matches.
        // Поиск совпадений.
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        List<MatOfDMatch> knnMatches = new ArrayList<>();
        matcher.knnMatch(descriptorImage, descriptorTempl, knnMatches, 2);

        float ratioThresh = 0.7f;
        List<DMatch> listOfGoodMatches = new ArrayList<>();
        for (MatOfDMatch knnMatch : knnMatches) {
            if (knnMatch.rows() > 1) {
                DMatch[] matches = knnMatch.toArray();
                if (matches[0].distance < ratioThresh * matches[1].distance) {
                    listOfGoodMatches.add(matches[0]);
                }
            }
        }

        return -1.0 * (double) listOfGoodMatches.size() / (double) knnMatches.size();
    }
}
