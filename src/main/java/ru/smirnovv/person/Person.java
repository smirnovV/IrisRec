package ru.smirnovv.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * An entry that represents a registered in the system person.
 */
@SuppressWarnings("magicNumber")
@Entity
public class Person {
    /**
     * The id of the person.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person-sequence")
    @SequenceGenerator(name = "person-sequence", sequenceName = "person_sequence",
            allocationSize = Integer.MAX_VALUE)
    private Long id;

    /**
     * The name of the person.
     * The name must contain Latin characters, numbers, '-', '.',
     * start with a capital letter, no longer than 50 characters and not empty.
     */
    @NotEmpty
    @Size(min = 1, max = 50)
    @Pattern(regexp = "([A-Z][\\w .-]*)")
    private String name;

    /**
     * Constructs an instance.
     */
    public Person() {
    }

    /**
     * Constructs an instance with the specified properties.
     *
     * @param name the name of the person.
     */
    public Person(final String name) {
        this.name = name;
    }

    /**
     * Returns the name of the person.
     *
     * @return the name of the person.
     */
    public final String getName() {
        return name;
    }

    /**
     * Updates the name of the person.
     *
     * @param name the name of the person.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the id of the person.
     *
     * @return the id of the person.
     */
    public final Long getId() {
        return id;
    }
}
