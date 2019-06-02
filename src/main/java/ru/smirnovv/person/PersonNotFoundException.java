package ru.smirnovv.person;

import ru.smirnovv.NotFoundException;

/**
 * This exception indicates that the requested person was not found.
 */
public class PersonNotFoundException extends NotFoundException {
    /**
     * Constructs an instance with the specified detail message.
     * Создает экземпляр с указанным подробным сообщением.
     *
     * @param message the detail message.
     *                подробное сообщение.
     */
    public PersonNotFoundException(final String message) {
        super(message);
    }
}
