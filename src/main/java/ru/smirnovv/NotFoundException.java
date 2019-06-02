package ru.smirnovv;

/**
 * This exception indicates that the requested resource was not found.
 * Исключение, указывающие на то, что запрашиваемый ресурс не найден.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Constructs an instance with the specified detail message.
     * Создает экземпляр с указанным подробным сообщением.
     *
     * @param message the detail message.
     *                подробное сообщение.
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
