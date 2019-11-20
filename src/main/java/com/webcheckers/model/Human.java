package com.webcheckers.model;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import lombok.Data;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Human extends Player {
    @Id
    private String userName;
    private String email;
    private String password;

}
