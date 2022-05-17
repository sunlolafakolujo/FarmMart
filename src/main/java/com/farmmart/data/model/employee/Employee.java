package com.farmmart.data.model.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.baseaudit.BaseAudit;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.RelationshipWithNextOfKin;
import com.farmmart.data.model.staticdata.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
//import java.time.Period;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String otherNames;

    @Enumerated(EnumType.STRING)
    private Status employeeStatus;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    @Positive
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nextOfKin;

    @Enumerated(EnumType.STRING)
    private RelationshipWithNextOfKin relationshipWithNextOfKin;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate hiredDate;

    //private LocalDate retirementDate; //TODO use function to calculate

    private String endDate;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private AppUser appUser;

    //TODO:-> Calculate retirement based on DoB, Hired Date and Current Date.

    //TODO:-> Add Education qualification(qualification,field, year), Work experience(company name,stateYear, endYear, duties(10000))
//    @ToString.Include
//    public String retirement(){
//        if (Period.between(getHiredDate(), LocalDate.now()).getYears()==35 || Period.between(getDob(),LocalDate.now()).getYears()==60){
//            System.out.println("Employee is due for retirement ");
//        }else{
//            return "Not due for retirement";
//        }
//
//        return "";
//    }

}
