package ru.smirnovv.person;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for {@link PersonController}.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void deletePersons() {
        personRepository.deleteAll();
    }

    @Test
    public void shouldReturnPersonList() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));
        Person personB = personRepository.save(new Person("TestB"));
        Person personC = personRepository.save(new Person("TestC"));

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].name").value(is(personA.getName())))
                .andExpect(jsonPath("$.content[1].id").exists())
                .andExpect(jsonPath("$.content[1].name").value(is(personB.getName())))
                .andExpect(jsonPath("$.content[2].id").exists())
                .andExpect(jsonPath("$.content[2].name").value(is(personC.getName())));
    }

    @Test
    public void shouldAddNewPerson() throws Exception {
        mockMvc.perform(post("/person")
                .param("name", "Test")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(is("Test")));
    }

    @Test
    public void shouldNotAddPersonWhenNameIsNotProvided() throws Exception {
        mockMvc.perform(post("/person")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Required String parameter 'name' is not present")));
    }

    @Test
    public void shouldNotAddPersonWhenNameIsEmpty() throws Exception {
        mockMvc.perform(post("/person")
                .param("name", "")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must not be empty!")));
    }

    @Test
    public void shouldNotAddPersonWhenNameBeginWithSmallLetter() throws Exception {
        mockMvc.perform(post("/person")
                .param("name", "test")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must contain Latin characters, " +
                                "numbers, signs '-', '.' and start with a capital letter!")));
    }

    @Test
    public void shouldNotAddPersonWhenNameContentIncorrectLetter() throws Exception {
        mockMvc.perform(post("/person")
                .param("name", "Test=")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must contain Latin characters, " +
                                "numbers, signs '-', '.' and start with a capital letter!")));
    }

    @Test
    public void shouldNotAddNewPersonWhenLengthNameGreaterMaxLength() throws Exception {
        mockMvc.perform(post("/person")
                .param("name", "TestTestTestTestTestTestTestTestTestTestTestTestTest")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must be no longer than 50 characters!")));
    }

    @Test
    public void shouldReturnPerson() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(get("/person/{id}", personA.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(is("TestA")));
    }

    @Test
    public void shouldPersonNotFoundException() throws Exception {
        mockMvc.perform(get("/person/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/10")))
                .andExpect(jsonPath("$.status").value(is(404)))
                .andExpect(jsonPath("$.message").value(
                        is("Person 10 not found.")));
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(put("/person/{id}", personA.getId())
                .param("name", "TestB")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(is("TestB")));
    }

    @Test
    public void shouldNotUpdatePersonWhenPersonNotFound() throws Exception {
        mockMvc.perform(put("/person/10")
                .param("name", "TestB")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/10")))
                .andExpect(jsonPath("$.status").value(is(404)))
                .andExpect(jsonPath("$.message").value(
                        is("Person 10 not found.")));
    }

    @Test
    public void shouldNotUpdatePersonWhenNameIsNotProvided() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(put("/person/{id}", personA.getId())
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/" + personA.getId())))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Required String parameter 'name' is not present")));
    }

    @Test
    public void shouldNotUpdatePersonWhenNameIsEmpty() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(put("/person/{id}", personA.getId())
                .param("name", "")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/" + personA.getId())))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must not be empty!")));
    }

    @Test
    public void shouldNotUpdatePersonWhenNameBeginWithSmallLetter() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(put("/person/{id}", personA.getId())
                .param("name", "test")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/" + personA.getId())))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must contain Latin characters, " +
                                "numbers, signs '-', '.' and start with a capital letter!")));
    }

    @Test
    public void shouldNotUpdatePersonWhenNameContentIncorrectLetter() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(put("/person/{id}", personA.getId())
                .param("name", "Test=")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/" + personA.getId())))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must contain Latin characters, " +
                                "numbers, signs '-', '.' and start with a capital letter!")));
    }

    @Test
    public void shouldNotUpdatePersonWhenLengthNameGreaterMaxLength() throws Exception {
        Person personA = personRepository.save(new Person("TestA"));

        mockMvc.perform(put("/person/{id}", personA.getId())
                .param("name", "TestTestTestTestTestTestTestTestTestTestTestTestTest")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/person/" + personA.getId())))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid name! The name must be no longer than 50 characters!")));
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        Person person = personRepository.save(new Person("Test"));
        Person personA = personRepository.save(new Person("TestA"));
        Person personB = personRepository.save(new Person("TestB"));

        mockMvc.perform(delete("/person/{id}", person.getId()))
                .andExpect(status().isOk());

        personRepository.delete(person);

        assertEquals(personRepository.count(), 2);
        assertTrue(personRepository.findById(personA.getId()).isPresent());
        assertTrue(personRepository.findById(personB.getId()).isPresent());
    }
}