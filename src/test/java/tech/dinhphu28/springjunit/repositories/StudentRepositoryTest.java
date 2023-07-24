package tech.dinhphu28.springjunit.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.dinhphu28.springjunit.entities.Gender;
import tech.dinhphu28.springjunit.entities.Student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenStudentExistsEmail() {
        // given
        String email = "jamila@gmail.com";
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        underTest.save(student);

        // when
        boolean exists = underTest.selectExistsEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckWhenStudentEmailDoesNotExists() {
        // given
        String email = "jamila@gmail.com";

        // when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}