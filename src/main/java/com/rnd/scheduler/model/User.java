package com.rnd.scheduler.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rnd.scheduler.model.Calendar;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private Long groupId;
    @OneToMany(mappedBy= "user")
    private List<Calendar> calendar;
}
