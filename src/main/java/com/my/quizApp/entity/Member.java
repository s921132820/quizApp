package com.my.quizApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "member")
@Data
public class Member {
    @Id
    private Long no;
    private String id;
    private String password;
    private boolean status;
    private int answerTrue;
    private int answerFalse;
}
