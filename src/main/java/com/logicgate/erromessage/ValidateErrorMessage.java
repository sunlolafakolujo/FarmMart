package com.logicgate.erromessage;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ValidateErrorMessage {
    private HttpStatus httpStatus;
    private String message;
    private String description;
    private Date createdDate;
}
