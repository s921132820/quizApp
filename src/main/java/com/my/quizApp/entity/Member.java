package com.my.quizApp.entity;


import com.my.basicCRUD.entity.BaseEntity; // BaseEntity를 import
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Data
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;
    private String id;
    private String password;
    private boolean status;
    @Column(name = "answer_true")
    private int answerTrue;
    @Column(name = "answer_false")
    private int answerFalse;
}
