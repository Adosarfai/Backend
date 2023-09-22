package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Privilege;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer userId;

    @Column(name = "username", length = 30, nullable = false)
    @Setter
    private String username;

    @JsonIgnore
    @Column(name = "email", nullable = false)
    @Setter
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 64, nullable = false)
    @Setter
    private String password;

    @Column(name = "privilege", nullable = false)
    private Privilege privilege;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;
}
