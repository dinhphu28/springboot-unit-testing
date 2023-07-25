package tech.dinhphu28.springjunit.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tech.dinhphu28.springjunit.entities.Student;
import tech.dinhphu28.springjunit.exceptions.BadRequestException;
import tech.dinhphu28.springjunit.exceptions.StudentNotFoundException;
import tech.dinhphu28.springjunit.repositories.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping(
            value = "{studentId}",
            produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public Student getStudentById(@PathVariable("studentId") Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student with id " + studentId + " does not exists"));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student) {
        if(student == null || student.getId() == null || student.getEmail() == null) {
            throw new BadRequestException("Required request body is missing");
        }

        return studentRepository.save(student);
    }
}
