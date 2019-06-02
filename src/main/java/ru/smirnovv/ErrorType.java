package ru.smirnovv;

/**
 * The special class that contains error information.
 */
public class ErrorType {
    /**
     * The request where the exception was thrown.
     */
    private final String url;

    /**
     * HTTP status code.
     */
    private final int status;

    /**
     * The detail message.
     */
    private final String message;

    /**
     * Constructs an instance with the specified properties.
     *
     * @param url     the request where the exception was thrown.
     * @param status  HTTP status code.
     * @param message the detail message.
     */
    public ErrorType(final String url, final int status, final String message) {
        this.url = url;
        this.status = status;
        this.message = message;
    }

    /**
     * Returns the request where the exception was thrown.
     *
     * @return the request where the exception was thrown.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Returns HTTP status code.
     *
     * @return HTTP status code.
     */
    public final int getStatus() {
        return status;
    }

    /**
     * Returns message the detail message.
     *
     * @return message the detail message.
     */
    public final String getMessage() {
        return message;
    }
}
