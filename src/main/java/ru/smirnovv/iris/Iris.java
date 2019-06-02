package ru.smirnovv.iris;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.smirnovv.person.Person;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * An entry that represents a registered in the system iris.
 * Сущность, представленная зарегистрированным РОГ.
 */
@Entity
public class Iris {
    /**
     * The id of the iris.
     * Id РОГ.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iris_sequence")
    @SequenceGenerator(name = "iris_sequence", sequenceName = "iris_sequence",
            allocationSize = Integer.MAX_VALUE)
    private Long id;

    /**
     * The person who owns the iris.
     * Человек, которому принадлежит РОГ.
     */
    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Person_id",
            foreignKey = @ForeignKey(name = "Person_id"))
    private Person person;

    /**
     * The iris code.
     */
    @NotEmpty
    private byte[] irisCode;

    /**
     * Constructs an instance.
     * Конструктор без параметров.
     */
    public Iris() {
    }

    /**
     * Constructs an instance with the specified properties.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param person   the person who owns the iris.
     *                 человек, которому принадлежит РОГ.
     * @param irisCode the iris code.
     *                 iris сode.
     */
    public Iris(@NotNull Person person, @NotEmpty byte[] irisCode) {
        this.person = person;
        this.irisCode = irisCode;
    }

    /**
     * Returns the id of the iris.
     * Возвращает id РОГ.
     *
     * @return the id of the iris.
     * id РОГ.
     */
    public final Long getId() {
        return id;
    }

    /**
     * Returns the person who owns the iris.
     * Возвращает человека, которому принадлежит РОГ.
     *
     * @return the person who owns the iris.
     * человек, которому принадлежит РОГ.
     */
    public final Person getPerson() {
        return person;
    }

    /**
     * Replaces the person who owns the iris.
     * Заменить владельца РОГ.
     *
     * @param person the person who owns the iris.
     *               новый человек.
     */
    public final void setPerson(final Person person) {
        this.person = person;
    }


    /**
     * Returns the iris code.
     * Возвращает iris code.
     *
     * @return the iris code.
     * iris code.
     */
    public byte[] getIrisCode() {
        return irisCode;
    }
}
