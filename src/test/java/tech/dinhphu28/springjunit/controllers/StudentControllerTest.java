package tech.dinhphu28.springjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.dinhphu28.springjunit.entities.Gender;
import tech.dinhphu28.springjunit.entities.Student;
import tech.dinhphu28.springjunit.exceptions.StudentNotFoundException;
import tech.dinhphu28.springjunit.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController underTest;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
    }

    @Test
    void canGetAllStudents() throws Exception {
        // given
        Student student1 = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        Student student2 = new Student(
                "Anwar",
                "anwar@gmail.com",
                Gender.MALE
        );
        Student student3 = new Student(
                "Jack5M",
                "jack5m@gmail.com",
                Gender.OTHER
        );
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findAll()).thenReturn(students);

        // when
        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/students")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", is("Jack5M")));
    }

    @Test
    void canGetStudentById() throws Exception {
        // given
        Long studentId = 435634564367L;
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        BDDMockito.given(studentRepository.findById(studentId)).willReturn(Optional.of(student));

        // when
        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/students/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Jamila")));
    }

    @Test
    void willThrowWhenGetStudentByIdNotFound() throws Exception {
        // given
        Long studentId = 435634564367L;
        BDDMockito.given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        // when
        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/students/" + studentId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound());

        assertThatThrownBy(() -> underTest.getStudentById(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");
    }

    @Test
    void canCreateStudent() throws Exception {
        // given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        Student studentSaved = new Student(
                436356L,
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        BDDMockito.given(studentRepository.save(student)).willReturn(studentSaved);

        String content = objectWriter.writeValueAsString(student);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        // when
        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Jamila")));
    }
}