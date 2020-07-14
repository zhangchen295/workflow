package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User {
    public User(){
    }
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Date birthday;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private String address;
}
