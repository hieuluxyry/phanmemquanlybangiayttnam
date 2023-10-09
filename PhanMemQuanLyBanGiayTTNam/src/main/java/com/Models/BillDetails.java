package com.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class BillDetails implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @ManyToOne
    @JoinColumn(name = "idBill")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "idProductDetails")
    private ProductDetails productDetails;

    @Column(name = "Amount")
    private int amount;

    @Column(name = "UnitPrice", columnDefinition = "Decimal(20,0)")
    private Float unitPrice;

    @ManyToOne
    @JoinColumn(name = "IdPromotion")
    private Promotion promotion;

    @Column(name = "Status")
    private int status;

    @Column(name = "Note", columnDefinition = "Nvarchar(50)")
    private String note;

    public BillDetails(Bill bill, ProductDetails productDetails, int amount, Float unitPrice, int status, Promotion promotion) {
        this.bill = bill;
        this.productDetails = productDetails;
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.status = status;
        this.promotion = promotion;
    }

}
