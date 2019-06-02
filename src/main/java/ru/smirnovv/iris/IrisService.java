package ru.smirnovv.iris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnovv.iris.irisDetection.IrisDetector;
import ru.smirnovv.iris.irisDetection.IrisNotFoundException;
import ru.smirnovv.person.Person;
import ru.smirnovv.person.PersonNotFoundException;
import ru.smirnovv.person.PersonRepository;

/**
 * A service that manages registered in the system irises.
 * Сервис, управляющий зарегистрированным РОГ.
 */
@SuppressWarnings("designForExtension")
@Service
public class IrisService {
    /**
     * A repository that manages registered in the system irises.
     * Репозиторий, управляющий зарегистрированными РОГ.
     */
    private final IrisRepository irisRepository;

    /**
     * A repository that manages registered in the system persons.
     * Репозиторий, управляющий зарегистрированными людьми.
     */
    private final PersonRepository personRepository;

    /**
     * A class that provides iris detection.
     * Класс, обеспечивающий детектирование РОГ.
     */
    private final IrisDetector irisDetector;

    /**
     * A class that manages encode data.
     * Класс, обеспечивающий шифрование данных.
     */
    private final Cryptographer cryptographer;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param irisRepository   a repository that manages registered in the system irises.
     *                         репозиторий, управляющий зарегистрированными РОГ.
     * @param personRepository a repository that manages registered in the system persons.
     *                         репозиторий, управляющий зарегистрированными людьми.
     * @param irisDetector     a class that provides iris detection.
     *                         класс, обеспечивающий детектирование РОГ.
     * @param cryptographer    a class that manages encode data.
     *                         класс, обеспечивающий шифрование данных.
     */
    @Autowired
    public IrisService(IrisRepository irisRepository, PersonRepository personRepository, IrisDetector irisDetector, Cryptographer cryptographer) {
        this.irisRepository = irisRepository;
        this.personRepository = personRepository;
        this.irisDetector = irisDetector;
        this.cryptographer = cryptographer;
    }

    /**
     * Lists all irises registered in the system.
     * Возвращает список все зарегистрированных РОГ.
     *
     * @param pageable a paging information.
     *                 Информация о страницах.
     * @return the page of retrieved irises.
     * страница зарегистрированных РОГ.
     */
    @Transactional(readOnly = true)
    public Page<Iris> list(final Pageable pageable) {
        return irisRepository.findAll(pageable);
    }

    /**
     * Lists all irises of the person in the system.
     * Возвращает список все зарегистрированных РОГ человека.
     *
     * @param personId the id of the person.
     *                 id человека.
     * @param pageable a paging information.
     *                 Информация о страницах.
     * @return the page of retrieved irises.
     * страница зарегистрированных РОГ.
     */
    @Transactional(readOnly = true)
    public Page<Iris> listIrisesByPersonId(final long personId, final Pageable pageable) {
        return irisRepository.findAllByPersonId(personId, pageable);
    }

    /**
     * Returns the person by irisId if it exists.
     * Возвращает человека по id РОГ.
     *
     * @param id the id of the iris.
     *           id РОГ.
     * @return the found person.
     * Найденный человек
     * @throws IrisNotFoundException is thrown when a iris with such id does not exist.
     *                               данное исключение, выбрасывается если РОГ с данным id не найден.
     */
    @Transactional(readOnly = true)
    public Person getPersonByIrisId(final long id) throws IrisNotFoundException {
        return irisRepository.findById(id).orElseThrow(
                () -> new IrisNotFoundException("Iris " + id + " not found.")).getPerson();
    }

    /**
     * Adds a new iris.
     * Добавить новый РОГ.
     *
     * @param personId  the id of the person.
     *                  id человека.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the added iris.
     * Добавленный РОГ
     * @throws PersonNotFoundException   is thrown when a person with such id does not exist.
     *                                   данное исключение, выбрасывается если человек с данным id не найден.
     * @throws InvalidIrisImageException is thrown when a image of the iris does not conform to the needed parameters.
     *                                   данное исключение, выбрасывается если изображение РОГ не соответствует требованиям.
     * @throws IrisNotFoundException     is thrown when a iris not found or the eye is not fully visible..
     *                                   данное исключение, выбрасывается если РОГ не обнаружено.
     */
    @Transactional
    public Iris add(final long personId, final byte[] irisImage) throws PersonNotFoundException,
            InvalidIrisImageException, IrisNotFoundException {
        return irisRepository.save(new Iris(
                personRepository.findById(personId).orElseThrow(
                        () -> new PersonNotFoundException("Person " + personId + " not found.")),
                cryptographer.encrypt(irisDetector.createIrisCode(irisImage))));
    }

    /**
     * Removes the iris by id if it exists.
     * Удалить РОГ по id, если найден.
     *
     * @param id the id of the iris.
     *           id РОГ.
     */
    @Transactional
    public void remove(final long id) {
        irisRepository.deleteById(id);
    }

    /**
     * Removes all irises of the person in the system.
     * Удалить все РОГ человека из системы.
     *
     * @param personId the id of the person.
     *                 id человека.
     */
    @Transactional
    public void removeIrisesByPersonId(final long personId) {
        irisRepository.deleteIrisesByPersonId(personId);
    }
}
