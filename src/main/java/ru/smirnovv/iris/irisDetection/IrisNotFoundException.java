package ru.smirnovv.iris.irisDetection;

import ru.smirnovv.NotFoundException;

/**
 * This exception indicates that the iris not detected.
 * Исключение, указывающие на то, что РОГ не обнаружен.
 */
public class IrisNotFoundException extends NotFoundException {
    /**
     * Constructs an instance with the specified detail message.
     * Создает экземпляр с указанным подробным сообщением.
     *
     * @param message the detail message.
     *                подробное сообщение.
     */
    public IrisNotFoundException(final String message) {
        super(message);
    }
}
