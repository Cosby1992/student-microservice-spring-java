package dk.cosby.cph.si.studentmicroservice.seed;

import dk.cosby.cph.si.studentmicroservice.model.Students;
import dk.cosby.cph.si.studentmicroservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class StudentSeeder implements CommandLineRunner {

        @Autowired
        StudentRepository studentRepo;

        @Override
        public void run(String... args) throws Exception {
            loadStudentData();
        }

        private void loadStudentData() {
            if (studentRepo.count() == 0) {
                Students user1 = new Students("John Doe", "johndoeisalwaystaken4@gmail.com", new Timestamp(calcStudentDateOfBirth(14)), "10/B");
                Students user2 = new Students("John Dùzzeldorv the 2nd", "duzzle69@live.dk", new Timestamp(calcStudentDateOfBirth(15)), "7/C");
                studentRepo.save(user1);
                studentRepo.save(user2);
            }
            System.out.println(studentRepo.count());
        }

        private long calcStudentDateOfBirth(int age) {
            long time = new Date().getTime();
            long result = 0;

            result = (long) time-(age*365L*24L*60L*60L*1000L);

            return result;

        }
    }
