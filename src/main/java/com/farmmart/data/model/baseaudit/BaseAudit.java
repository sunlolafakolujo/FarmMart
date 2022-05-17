package com.farmmart.data.model.baseaudit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@MappedSuperclass
public class BaseAudit implements Serializable {

    @JsonIgnore
    @JsonFormat(pattern = "dd/MM/yyyy")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @JsonIgnore
    @JsonFormat(pattern = "dd/MM/yyyy")
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
}
