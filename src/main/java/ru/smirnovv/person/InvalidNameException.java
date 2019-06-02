package ru.smirnovv.person;

/**
 * This exception indicates that the name does not conform to the naming syntax.
 * The name must contain Latin characters, numbers, '-', '.',
 * start with a capital letter, no longer than 50 characters and not empty.
 * Исключение, указывающие на то, что имя не соответствует необходимым параметрам.
 * Оно должно содержать латинские буквы, цифры, '-', '.',
 * начинаться с заглавной буквы, не длиннее 50 символов.
 */
public class InvalidNameException extends RuntimeException {
    /**
     * Constructs an instance with the specified detail message.
     * Создает экземпляр с указанным подробным сообщением.
     *
     * @param message the detail message.
     *                подробное сообщение.
     */
    public InvalidNameException(final String message) {
        super(message);
    }
}
