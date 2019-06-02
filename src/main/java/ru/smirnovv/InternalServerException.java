package ru.smirnovv;

/**
 * This exception indicates that the server error has occurred.
 * Исключение, указывающие на то, что произошла серверная ошибка.
 */
public class InternalServerException extends RuntimeException {
    /**
     * Constructs an instance with the specified detail message.
     * Создает экземпляр с указанным подробным сообщением.
     *
     * @param message the detail message.
     *                подробное сообщение.
     */
    public InternalServerException(final String message) {
        super(message);
    }
}


