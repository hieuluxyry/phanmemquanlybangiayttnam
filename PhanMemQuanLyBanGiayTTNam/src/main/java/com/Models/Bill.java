package com.Models;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table
public class Bill implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @ManyToOne
    @JoinColumn(name = "IdCustomer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "IdStaff")
    private Staff staff;

    @Column(name = "Code", columnDefinition = "Varchar(20)", unique = true)
    private String code;

    @Column(name = "DateCreated")
    private Date dateCreated;

    @Column(name = "DateOfPayment")
    private Date dateOfPayment;

    @Column(name = "DeliveryDate")
    private Date deliveryDate;

    @Column(name = "ReceivedDate")
    private Date receivedDate;

    @Column(name = "ExpectedDate")
    private Date ExpectedDate;

    @Column(name = "Status")
    private int status;

    @Column(name = "Address", columnDefinition = "Nvarchar(MAX)")
    private String address;

    @Column(name = "Note", columnDefinition = "Nvarchar(MAX)")
    private String note;

    @Column(name = "NameCustomer", columnDefinition = "Nvarchar(30)")
    private String nameCustomer;

    @Column(name = "PhoneNumberCustomer", columnDefinition = "Varchar(30)")
    private String phoneNumberCustomer;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    private List<BillDetails> listBillDetails;

    public Bill(Customer customer, Staff staff, String code, Date dateCreated, int status, String address, String note, String nameCustomer, String phoneNumberCustomer) {
        this.customer = customer;
        this.staff = staff;
        this.code = code;
        this.dateCreated = dateCreated;
        this.status = status;
        this.address = address;
        this.note = note;
        this.nameCustomer = nameCustomer;
        this.phoneNumberCustomer = phoneNumberCustomer;
    }

    public Bill(Staff staff, String code, Date dateCreated, int status, String address, String note, String nameCustomer, String phoneNumberCustomer) {
        this.staff = staff;
        this.code = code;
        this.dateCreated = dateCreated;
        this.status = status;
        this.address = address;
        this.note = note;
        this.nameCustomer = nameCustomer;
        this.phoneNumberCustomer = phoneNumberCustomer;
    }


    public Bill(String id) {
        this.id = id;
    }

}
