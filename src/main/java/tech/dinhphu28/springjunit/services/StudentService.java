package tech.dinhphu28.springjunit.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.dinhphu28.springjunit.entities.Student;
import tech.dinhphu28.springjunit.exceptions.BadRequestException;
import tech.dinhphu28.springjunit.exceptions.StudentNotFoundException;
import tech.dinhphu28.springjunit.repositories.StudentRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        Boolean existsEmail = studentRepository.selectExistsEmail(student.getEmail());
        if(existsEmail) {
            throw new BadRequestException("Email " + student.getEmail() + " taken");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }
}
