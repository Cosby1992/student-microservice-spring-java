package dk.cosby.cph.si.studentmicroservice.repository;

import dk.cosby.cph.si.studentmicroservice.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {



}
