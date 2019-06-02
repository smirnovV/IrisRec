package ru.smirnovv.iris;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A repository that manages registered in the system irises.
 * Репозиторий, управляющий зарегистрированными РОГ.
 */
public interface IrisRepository extends JpaRepository<Iris, Long> {
    /**
     * Finds all irises of the person in the system.
     * Найти все РОГ человека из системы.
     *
     * @param personId the id of the person.
     *                 id человека
     * @param pageable a paging information.
     *                 Информация о страницах.
     * @return the page of retrieved irises.
     * Найденные РОГ.
     */
    Page<Iris> findAllByPersonId(Long personId, Pageable pageable);

    /**
     * Finds all irises of the person in the system.
     * Найти все РОГ человека из системы.
     *
     * @param personId the id of the person.
     *                 id человека
     * @return the page of retrieved irises.
     * Найденные РОГ.
     */
    List<Iris> findAllByPersonId(Long personId);

    /**
     * Removes all irises of the person in the system.
     * Удалить все РОГ человека из системы.
     *
     * @param personId the id of the person.
     *                 id человека.
     */
    void deleteIrisesByPersonId(Long personId);
}
