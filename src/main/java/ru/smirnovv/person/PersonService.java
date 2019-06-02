package ru.smirnovv.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.Assert.notNull;

/**
 * A service that manages registered in the system persons.
 * Сервис, управляющий зарегистрированным людьми.
 */
@SuppressWarnings({"designForExtension", "magicNumber"})
@Service
public class PersonService {
    /**
     * A repository that manages registered in the system persons.
     * Репозиторий, управляющий зарегистрированными людьми.
     */
    private final PersonRepository personRepository;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param personRepository a repository that manages registered in the system persons.
     *                         репозиторий, управляющий зарегистрированными людьми.
     */
    @Autowired
    public PersonService(final PersonRepository personRepository) {
        notNull(personRepository, "Argument 'personRepository' can not be null");
        this.personRepository = personRepository;
    }

    /**
     * Checks name for validity.
     * Проверка имени на валидность.
     *
     * @param name the name of the person.
     *             имя человека
     * @throws InvalidNameException is thrown when a name does not conform to the naming syntax.
     *                              данное исключение, выбрасывается если имя не валидно.
     */
    public static void checkName(final String name) {
        if (name.isEmpty()) {
            throw new InvalidNameException("Invalid name! The name must not be empty!");
        } else if (!name.matches("([A-Z][A-z .-]*)")) {
            throw new InvalidNameException("Invalid name! The name must contain Latin characters, "
                    + "numbers, signs '-', '.' and start with a capital letter!");
        } else if (name.length() > 50) {
            throw new InvalidNameException("Invalid name! The name must be no longer than 50 characters!");
        }
    }

    /**
     * Lists all persons registered in the system.
     * Возвращает список все зарегистрированных людей.
     *
     * @param pageable a paging information.
     *                 информация о страницах.
     * @return the page of retrieved persons.
     * страница зарегистрированных людей.
     */
    @Transactional(readOnly = true)
    public Page<Person> list(final Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    /**
     * Adds a new person.
     * Зарегистрировать нового человека.
     *
     * @param name the name of the person.
     *             имя человека.
     * @return the added person.
     * Зарегистрированный человек.
     * @throws InvalidNameException is thrown when a name does not conform to the naming syntax.
     *                              данное исключение, выбрасывается если имя не валидно.
     */
    @Transactional
    public Person add(final String name) throws InvalidNameException {
        checkName(name);

        return personRepository.save(new Person(name));
    }

    /**
     * Returns the person by id if it exists.
     * Возвращаетт человека по id, если найден.
     *
     * @param id the id of the person.
     *           id человека.
     * @return the found person.
     * найденный человек
     * @throws PersonNotFoundException is thrown when a person with such id does not exist.
     *                                 данное исключение, выбрасывается если человек с данным id не найден.
     */
    @Transactional(readOnly = true)
    public Person getPersonById(final long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundException("Person " + id + " not found."));
    }

    /**
     * Updates name of the person by id if name passed the verification.
     * Обновляет имя человека по id если имя прошло верификацию.
     *
     * @param id   the id of the person.
     *             id человека.
     * @param name the new name of the person.
     *             имя человека.
     * @return the updated person.
     * обновленный человек.
     * @throws PersonNotFoundException is thrown when a person with such id does not exist.
     *                                 данное исключение, выбрасывается если человек с данным id не найден.
     * @throws InvalidNameException    is thrown when a name does not conform to the naming syntax.
     *                                 данное исключение, выбрасывается если имя не валидно.
     */
    @Transactional
    public Person update(final long id, final String name) throws PersonNotFoundException, InvalidNameException {
        checkName(name);

        final Person person = getPersonById(id);
        person.setName(name);
        return personRepository.save(person);
    }

    /**
     * Removes the person by id if it exists.
     * Удалить человека по id, если найден.
     *
     * @param id the id of the person.
     *           id человека.
     */
    @Transactional
    public void remove(final long id) {
        personRepository.deleteById(id);
    }
}
