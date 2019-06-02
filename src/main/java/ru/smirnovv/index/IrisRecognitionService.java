package ru.smirnovv.index;

import org.flywaydb.core.internal.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnovv.index.irisRecognition.IrisRecognition;
import ru.smirnovv.iris.IrisRepository;
import ru.smirnovv.person.Person;

import java.util.List;

/**
 * A service that manages iris recognition.
 * Сервис, управляющий распознаванием по РОГ.
 */
@Service
public class IrisRecognitionService {
    /**
     * A repository that manages registered in the system irises.
     * Репозиторий, управляющий зарегистрированными РОГ.
     */
    private final IrisRepository irisRepository;

    /**
     * A class that provides iris recognition.
     * Класс, обеспечивающий распознавание радужной оболочки.
     */
    private final IrisRecognition irisRecognition;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param irisRepository  a repository that manages registered in the system irises.
     *                        репозиторий, управляющий зарегистрированными РОГ.
     * @param irisRecognition a class that provides iris recognition.
     *                        класс, обеспечивающий распознавание радужной оболочки.
     */
    @Autowired
    public IrisRecognitionService(IrisRepository irisRepository,
                                  IrisRecognition irisRecognition) {
        this.irisRepository = irisRepository;
        this.irisRecognition = irisRecognition;
    }

    /**
     * Identify the person.
     * Производит идентификацию человека с использованием РОГ.
     *
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the found person.
     *         найденный человек.
     */
    public final Person identify(byte[] irisImage) {
        return irisRecognition.identify(irisRepository.findAll(), irisImage);
    }

    /**
     * Authorizes person by the iris.
     * Производит авторизацию человека с использованием РОГ.
     *
     * @param personId  the id of the person.
     *                  id человека.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return {@code true} if authorization succeeded or not.
     *                      подтверждение аутентификации пользователя.
     */
    public final Boolean authorize(final long personId, final byte[] irisImage) {
        return irisRecognition.authorize(irisRepository.findAllByPersonId(personId), irisImage);
    }

    /**
     * Finds people with the most appropriate iris.
     * Находит людей с наиболее схожим РОГ.
     *
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the found persons.
     *         список найденных людей.
     */
    public final List<Pair<Person, Double>> find(final byte[] irisImage) {
        return irisRecognition.find(irisRepository.findAll(), irisImage);
    }
}
