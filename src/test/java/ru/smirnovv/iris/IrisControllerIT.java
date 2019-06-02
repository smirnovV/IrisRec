package ru.smirnovv.iris;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.smirnovv.person.Person;
import ru.smirnovv.person.PersonRepository;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for {@link IrisController}.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IrisControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IrisRepository irisRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private IrisService service;

    @Before
    public void deletePersons() {
        irisRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Before
    public void deleteIrises() {
        irisRepository.deleteAll();
    }


    @Test
    public void shouldReturnIrisList() throws Exception {
        Person person = personRepository.save(new Person("Test"));
        Iris irisA = irisRepository.save(new Iris(person, new byte[3]));
        Iris irisB = irisRepository.save(new Iris(person, new byte[3]));

        mockMvc.perform(get("/iris"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].person.id").exists())
                .andExpect(jsonPath("$.content[0].person.name").value(irisA.getPerson().getName()))
                .andExpect(jsonPath("$.content[1].id").exists())
                .andExpect(jsonPath("$.content[1].person.id").exists())
                .andExpect(jsonPath("$.content[1].person.name").value(irisB.getPerson().getName()));
    }

    @Test
    public void shouldReturnIrisListByPersonId() throws Exception {
        Person person = personRepository.save(new Person("Test"));
        Iris iris = irisRepository.save(new Iris(person, new byte[3]));

        mockMvc.perform(get("/iris").param("personId", person.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].person.id").exists())
                .andExpect(jsonPath("$.content[0].person.name").value(iris.getPerson().getName()));
    }

    @Test
    public void shouldReturnPerson() throws Exception {
        Person person = personRepository.save(new Person("Test"));
        Iris iris = irisRepository.save(new Iris(person, new byte[3]));

        mockMvc.perform(get("/iris/{id}", iris.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    public void shouldIrisNotFoundException() throws Exception {
        mockMvc.perform(get("/iris/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value("/iris/10"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Iris 10 not found."));
    }

    @Test
    public void shouldDeleteIris() throws Exception {
        Person person = personRepository.save(new Person("Test"));
        Iris iris = irisRepository.save(new Iris(person, new byte[3]));
        Iris irisA = irisRepository.save(new Iris(person, new byte[3]));
        Iris irisB = irisRepository.save(new Iris(person, new byte[3]));

        mockMvc.perform(delete("/iris/{id}", iris.getId()))
                .andExpect(status().isOk());

        assertEquals(personRepository.count(), 1);

        assertEquals(irisRepository.count(), 2);
        assertTrue(irisRepository.findById(irisA.getId()).isPresent());
        assertTrue(irisRepository.findById(irisB.getId()).isPresent());
    }

    @Test
    public void shouldDeleteIrisesByPersonId() throws Exception {


        Person person = personRepository.save(new Person("Test"));
        Person personA = personRepository.save(new Person("Person"));
        Person personB = personRepository.save(new Person("PersonA"));

        irisRepository.save(new Iris(person, new byte[3]));
        irisRepository.save(new Iris(person, new byte[3]));
        Iris irisA = irisRepository.save(new Iris(personA, new byte[3]));
        Iris irisB = irisRepository.save(new Iris(personA, new byte[3]));
        Iris irisC = irisRepository.save(new Iris(personB, new byte[3]));
        Iris irisD = irisRepository.save(new Iris(personB, new byte[3]));


        mockMvc.perform(delete("/iris").param("personId", person.getId().toString()))
                .andExpect(status().isOk());

        assertEquals(personRepository.count(), 3);

        assertEquals(irisRepository.count(), 4);
        assertTrue(irisRepository.findById(irisA.getId()).isPresent());
        assertTrue(irisRepository.findById(irisB.getId()).isPresent());
        assertTrue(irisRepository.findById(irisC.getId()).isPresent());
        assertTrue(irisRepository.findById(irisD.getId()).isPresent());
    }

    @Test
    public void shouldDeletePersonAndIrises() {
        Person person = personRepository.save(new Person("Test"));
        Person personA = personRepository.save(new Person("Person"));
        Person personB = personRepository.save(new Person("PersonA"));

        irisRepository.save(new Iris(person, new byte[3]));
        irisRepository.save(new Iris(person, new byte[3]));
        Iris irisA = irisRepository.save(new Iris(personA, new byte[3]));
        Iris irisB = irisRepository.save(new Iris(personA, new byte[3]));
        Iris irisC = irisRepository.save(new Iris(personB, new byte[3]));
        Iris irisD = irisRepository.save(new Iris(personB, new byte[3]));

        personRepository.delete(person);

        assertEquals(personRepository.count(), 2);
        assertTrue(personRepository.findById(personA.getId()).isPresent());
        assertTrue(personRepository.findById(personB.getId()).isPresent());

        assertEquals(irisRepository.count(), 4);
        assertTrue(irisRepository.findById(irisA.getId()).isPresent());
        assertTrue(irisRepository.findById(irisB.getId()).isPresent());
        assertTrue(irisRepository.findById(irisC.getId()).isPresent());
        assertTrue(irisRepository.findById(irisD.getId()).isPresent());
    }


}
