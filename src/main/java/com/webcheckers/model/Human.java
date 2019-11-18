package com.webcheckers.model;

import lombok.Data;

@Data
public class Human extends Player {
    private String userName;
    private String email;
    private String password;

}
