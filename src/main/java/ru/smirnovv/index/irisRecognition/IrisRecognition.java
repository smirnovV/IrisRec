package ru.smirnovv.index.irisRecognition;

import org.flywaydb.core.internal.util.Pair;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.smirnovv.iris.Cryptographer;
import ru.smirnovv.iris.Iris;
import ru.smirnovv.iris.irisDetection.IrisDetector;
import ru.smirnovv.person.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides iris recognition.
 * Класс, обеспечивающий распознавание РОГ.
 */
@Component
public class IrisRecognition {
    /**
     * A class that detects iris.
     * Класс, детектирующий РОГ.
     */
    private final IrisDetector irisDetector;

    /**
     * A class that provides iris comparison.
     * Класс, обеспечивающий сравнение РОГ.
     */
    private final IrisCompare irisCompare;

    /**
     * A class that manages encode data.
     * Класс, обеспечивающий шифрование данных.
     */
    private final Cryptographer cryptographer;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param irisDetector  a class that detects iris.
     *                      класс, детектирующий РОГ.
     * @param irisCompare   A class that provides iris comparison.
     *                      класс, обеспечивающий сравнение РОГ.
     * @param cryptographer a class that manages encode data.
     *                      класс, обеспечивающий шифрование данных.
     */
    @Autowired
    public IrisRecognition(final IrisDetector irisDetector,
                           @Qualifier("SURFRecognition") final IrisCompare irisCompare,
                           final Cryptographer cryptographer) {
        this.irisDetector = irisDetector;
        this.irisCompare = irisCompare;
        this.cryptographer = cryptographer;
    }

    /**
     * Authorizes person by the iris.
     * Производит авторизацию человека с использованием РОГ.
     *
     * @param irises    the irises from the database.
     *                  РОГ из базы данных.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return {@code true} if match found or not.
     * подтверждение нахождения совпадения.
     */
    public final Boolean authorize(final List<Iris> irises, final byte[] irisImage) {
        // Detects iris and create a normalized image.
        // Детектирование РОГ и создание нормализованного изображения.
        Mat iris = new Mat(128, 256, CvType.CV_8U);
        iris.put(0, 0, irisDetector.createIrisCode(irisImage));

        // Searches match of the iris until a match is found.
        // Поиск совпадения РОГ, до тех пор пока не найдется совпадение.
        for (Iris temp : irises) {
            // Decrypt iris.
            // Дешифровка РОГ.
            Mat irisTemplate = new Mat(128, 256, CvType.CV_8U);
            irisTemplate.put(0, 0, cryptographer.decrypt(temp.getIrisCode()));

            // Compares irises.
            // Сравнение РОГ.
            if (irisCompare.authorize(iris, irisTemplate)) {
                return true;
            }
        }

        // Returns {@code false} if matches not found.
        // Вернуть {@code false} если совпадении не обнаружено.
        return false;
    }

    /**
     * Identify the person.
     * Производит идентификацию человека с использованием РОГ.
     *
     * @param irises    the irises from the database.
     *                  РОГ из базы данных.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the found person.
     * найденный человек.
     */
    public final Person identify(final List<Iris> irises, final byte[] irisImage) {
        // Detects iris and create a normalized image.
        // Детектирование РОГ и создание нормализованного изображения.
        Mat iris = new Mat(128, 256, CvType.CV_8U);
        iris.put(0, 0, irisDetector.createIrisCode(irisImage));

        // Searches match of the iris until a match is found.
        // Поиск совпадения РОГ, до тех пор пока не найдется совпадение.
        for (Iris temp : irises) {
            // Decrypt iris.
            // Дешифровка РОГ.
            Mat irisTemplate = new Mat(128, 256, CvType.CV_8U);
            irisTemplate.put(0, 0, cryptographer.decrypt(temp.getIrisCode()));

            // Compares irises.
            // Сравнение РОГ.
            if (irisCompare.authorize(iris, irisTemplate)) {
                return temp.getPerson();
            }
        }

        // Returns null if matches not found.
        // Вернуть "null" если совпадении не обнаружено.
        return null;
    }

    /**
     * Finds people with the most appropriate iris.
     * Находит людей с наиболее схожим РОГ.
     *
     * @param irises    the irises from the database.
     *                  РОГ из базы данных.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the found persons.
     * список найденных людей.
     */
    public final List<Pair<Person, Double>> find(final List<Iris> irises, final byte[] irisImage) {
        // Detects iris and creates a normalized image.
        // Детектирование РОГ и создание нормализованного изображения.
        Mat iris = new Mat(128, 256, CvType.CV_8U);
        iris.put(0, 0, irisDetector.createIrisCode(irisImage));

        // A list to store found persons.
        // Список для хранения найденных людей.
        List<Pair<Person, Double>> result = new ArrayList<>();

        // Searches match of the irises.
        for (Iris temp : irises) {
            // Decrypt iris.
            // Дешифровка РОГ.
            Mat irisTemplate = new Mat(128, 256, CvType.CV_8U);
            irisTemplate.put(0, 0, cryptographer.decrypt(temp.getIrisCode()));

            // Compares irises.
            // Сравнение РОГ.
            result.add(Pair.of(temp.getPerson(),
                    irisCompare.compare(iris, irisTemplate)));


            // Sorts results by a percentage of matches.
            // Сортирует результат по проценту совпадения.
            if (result.size() == 4) {
                result.sort((pairLeft, pairRight)
                        -> (int) (100000 * pairLeft.getRight() - 100000 * pairRight.getRight()));

                result.remove(3);
            }
        }

        return result;
    }
}
