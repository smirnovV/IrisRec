package ru.smirnovv.index;

import org.flywaydb.core.internal.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnovv.person.Person;

import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * A REST controller that serves iris recognition.
 * REST-контроллер, управляющий распознаванием по РОГ.
 */
@RestController
@RequestMapping("/index")
public class IrisRecognitionController {
    /**
     * A service that manages iris recognition.
     * Сервис, управляющий распознаванием по РОГ.
     */
    private final IrisRecognitionService irisRecognitionService;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param irisRecognitionService a service that manages iris recognition.
     *                               сервис, управляющий распознаванием по РОГ.
     */
    @Autowired
    public IrisRecognitionController(final IrisRecognitionService irisRecognitionService) {
        notNull(irisRecognitionService, "Argument 'irisRecognitionService' can not be null");
        this.irisRecognitionService = irisRecognitionService;
    }

    /**
     * Authorizes person by the iris.
     * Производит авторизацию человека с использованием РОГ.
     *
     * @param personId  the id of the person.
     *                  id человека.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return {@code true} if authorization succeeded or not.
     *                      подтверждение аутентификации пользователя.
     */
    @PutMapping(headers = "personId", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public final Boolean authorize(@RequestHeader final long personId,
                                   @RequestBody final byte[] irisImage) {
        return irisRecognitionService.authorize(personId, irisImage);
    }

    /**
     * Identify the person.
     * Производит идентификацию человека с использованием РОГ.
     *
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the found person.
     *         найденный человек.
     */
    @PutMapping(consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public final Person identify(@RequestBody final byte[] irisImage) {
        return irisRecognitionService.identify(irisImage);
    }

    /**
     * Finds people with the most appropriate iris.
     * Находит людей с наиболее схожим РОГ.
     *
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the found persons.
     *         список найденных людей.
     */
    @GetMapping(consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public final List<Pair<Person, Double>> find(@RequestBody final byte[] irisImage) {
        return irisRecognitionService.find(irisImage);
    }
}
