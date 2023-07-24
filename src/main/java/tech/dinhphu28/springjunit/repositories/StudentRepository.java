package tech.dinhphu28.springjunit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dinhphu28.springjunit.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query( value = """
    select case when count(s) > 0 then\s
    true else false end\s
    from Student s\s
    where s.email = ?1\s
    """)
    Boolean selectExistsEmail(String email);
}