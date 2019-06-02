package ru.smirnovv.iris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnovv.person.Person;

import static org.springframework.util.Assert.notNull;

/**
 * A REST controller that serves registered in the system irises.
 * REST-контроллер, управляющий зарегистрированным РОГ.
 */
@RestController
@RequestMapping("/iris")
public class IrisController {
    /**
     * A service that manages registered in the system irises.
     * Сервис, управляющий зарегистрированным РОГ.
     */
    private final IrisService irisService;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param irisService a service that manages registered in the system irises.
     *                    сервис, управляющий зарегистрированным РОГ.
     */
    @Autowired
    public IrisController(final IrisService irisService) {
        notNull(irisService, "Argument 'irisService' can not be null");
        this.irisService = irisService;
    }

    /**
     * Lists all irises registered in the system.
     * Вернуть список все зарегистрированных РОГ.
     *
     * @param pageable a paging information.
     *                 Информация о страницах.
     * @return the page of retrieved irises.
     * Найденные РОГ.
     */
    @GetMapping
    public final Page<Iris> list(@PageableDefault(sort = "id") final Pageable pageable) {
        return irisService.list(pageable);
    }

    /**
     * Lists all irises of the person in the system.
     * Вернуть список все зарегистрированных РОГ человека.
     *
     * @param personId the id of the person.
     *                 id человека.
     * @param pageable a paging information.
     *                 Информация о страницах.
     * @return the page of retrieved irises.
     * Найденные РОГ.
     */
    @GetMapping(params = "personId")
    public final Page<Iris> listIrisesByPersonId(@RequestParam final long personId,
                                                 @PageableDefault(sort = "id") final Pageable pageable) {
        return irisService.listIrisesByPersonId(personId, pageable);
    }

    /**
     * Returns the person by irisId if it exists.
     * Возвращает человека по id РОГ.
     *
     * @param id the id of the iris.
     *           id РОГ.
     * @return the found person.
     * Найденный человек
     */
    @GetMapping("/{id}")
    public final Person getPersonByIrisId(@PathVariable final long id) {
        return irisService.getPersonByIrisId(id);
    }

    /**
     * Adds a new iris.
     * Добавить новый РОГ.
     *
     * @param personId  the id of the person.
     *                  id человека.
     * @param irisImage the image of the iris.
     *                  изображение РОГ.
     * @return the added iris.
     * Добавленный РОГ
     */
    @PostMapping(headers = "personId", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public final Iris add(@RequestHeader final long personId, @RequestBody final byte[] irisImage) {
        return irisService.add(personId, irisImage);
    }

    /**
     * Removes the iris by id if it exists.
     * Удалить РОГ по id, если найден
     *
     * @param id the id of the iris.
     *           id РОГ.
     */
    @DeleteMapping("/{id}")
    public final void remove(@PathVariable final long id) {
        irisService.remove(id);
    }

    /**
     * Removes all irises of the person in the system.
     * Удалить все РОГ человека из системы.
     *
     * @param personId the id of the person.
     *                 id человека.
     */
    @DeleteMapping(params = "personId")
    public final void removeIrisesByPersonId(@RequestParam final long personId) {
        irisService.removeIrisesByPersonId(personId);
    }
}
