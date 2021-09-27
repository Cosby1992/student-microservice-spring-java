package dk.cosby.cph.si.studentmicroservice.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Students {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String mail;
    private Timestamp date_of_birth;
    private String average_grade;

    public Students() {
        super();
    }

    public Students(String name, String mail, Timestamp date_of_birth, String average_grade) {
        this.name = name;
        this.mail = mail;
        this.date_of_birth = date_of_birth;
        this.average_grade = average_grade;
    }
}
