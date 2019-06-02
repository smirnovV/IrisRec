package ru.smirnovv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static java.lang.System.getProperty;
import static java.lang.System.load;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * A service that search people by iris.
 */
@SuppressWarnings({"hideUtilityClassConstructor", "PMD.TooManyStaticImports"})
@SpringBootApplication
public class Application {
    static {
        if (getProperty("os.name").matches("Linux")) {
            load(Application.class.getClassLoader().getResource("").getPath()
                    + "opencv/linux/libopencv_java410.so");
        }
    }

    /**
     * The entry point that bootstraps application.
     *
     * @param args the program arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * The class that handles the global exception.
     */
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        /**
         * Handles {@link NotFoundException} and returns the response with error information.
         *
         * @param request   the request where the exception was thrown.
         * @param exception the thrown exception.
         * @return a response entity with error details.
         * @see ErrorType
         */
        @ExceptionHandler(NotFoundException.class)
        public final ResponseEntity<ErrorType> handleNotFoundException(
                final HttpServletRequest request, final NotFoundException exception) {
            return new ResponseEntity<>(
                    new ErrorType(request.getRequestURI(), NOT_FOUND.value(), exception.getMessage()),
                    NOT_FOUND);
        }

        /**
         * Handles exception and returns the response with error information.
         *
         * @param request   the request where the exception was thrown.
         * @param exception the thrown exception.
         * @return a response entity with error details.
         * @see ErrorType
         */
        @ExceptionHandler(Exception.class)
        public final ResponseEntity<ErrorType> internalServerException(
                final HttpServletRequest request, final Exception exception) {
            return new ResponseEntity<>(
                    new ErrorType(request.getRequestURI(), INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
                    INTERNAL_SERVER_ERROR);
        }

        /**
         * Handles {@link MissingServletRequestParameterException} and
         * returns the response with error information.
         *
         * @param request   the request where the exception was thrown.
         * @param exception the thrown exception.
         * @return a response entity with error details.
         * @see ErrorType
         */
        @ExceptionHandler(MissingServletRequestParameterException.class)
        public final ResponseEntity<ErrorType> missingRequestParameterException(
                final HttpServletRequest request, final MissingServletRequestParameterException exception) {
            return new ResponseEntity<>(
                    new ErrorType(request.getRequestURI(), BAD_REQUEST.value(), exception.getMessage()),
                    BAD_REQUEST);
        }
    }
}
