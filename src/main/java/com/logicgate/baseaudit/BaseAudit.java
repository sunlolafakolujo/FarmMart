package com.logicgate.baseaudit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BaseAudit implements Serializable {
    @JsonIgnore
    @ToString.Exclude
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date createdDate;

    @JsonIgnore
    @ToString.Exclude
    @Temporal(TemporalType.DATE)
    @UpdateTimestamp
    private Date modifiedDate;
}
