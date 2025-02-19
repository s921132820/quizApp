package com.my.basicCRUD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Data
@MappedSuperclass
public class BaseEntity {
    @CreatedDate // 입력 시 자동으로 현재 날짜 시각 저장
    @Column(updatable = false) // 저장 이 후에 수정 불가하도록 설정
    private LocalDateTime createAt;

    @LastModifiedDate // 수정 발생 시 수정된 시각 자동 저장
    @Column(insertable = false)
    private LocalDateTime updateAt;
}
