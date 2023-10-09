package com.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Staff implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @Column(name = "Code", columnDefinition = "Varchar(20)", unique = true)
    private String code;

    @Column(name = "FirstName", columnDefinition = "Nvarchar(30)")
    private String firstName;

    @Column(name = "LastName", columnDefinition = "Nvarchar(30)")
    private String lastName;

    @Column(name = "Sex", columnDefinition = "Nvarchar(10)")
    private String sex;

    @Column(name = "DateOfBirth")
    private Date dateOfBirth;

    @Column(name = "Address", columnDefinition = "Nvarchar(100)")
    private String address;

    @Column(name = "PhoneNumber", columnDefinition = "Varchar(30)")
    private String phoneNumber;

    @Column(name = "Email", columnDefinition = "Varchar(MAX)")
    private String email;

    @Column(name = "Password", columnDefinition = "Varchar(MAX)")
    private String password;

    @Column(name = "Status")
    private int status;

    @Column(name = "Role")
    private int role;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private List<Bill> listBill;

    public Staff(String id, String code, String firstName, String lastName, String sex, Date dateOfBirth, String address, String phoneNumber, String email, String password, int status, int role) {
        this.id = id;
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public Staff(String id) {
        this.id = id;
    }
}
