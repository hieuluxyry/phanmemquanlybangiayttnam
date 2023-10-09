package com.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Customer implements Serializable {

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

    @Column(name = "PhoneNumber", columnDefinition = "Varchar(30)")
    private String phoneNumber;

    @Column(name = "Email", columnDefinition = "Varchar(MAX)")
    private String email;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Bill> listBill;

    public Customer(String code, String firstName, String lastName, String phoneNumber, String email) {
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Customer(String id) {
        this.id = id;
    }

}
