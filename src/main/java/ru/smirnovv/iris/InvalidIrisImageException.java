package ru.smirnovv.iris;

/**
 * This exception indicates that the image of the iris
 * does not conform to the needed parameters.
 * Исключение, указывающие на то, что изображение РОГ
 * не соответствует необходимым параметрам.
 */
public class InvalidIrisImageException extends RuntimeException {

    /**
     * Constructs an instance with the specified detail message.
     * Создает экземпляр с указанным подробным сообщением.
     *
     * @param message the detail message.
     *                подробное сообщение.
     */
    public InvalidIrisImageException(final String message) {
        super(message);
    }
}
