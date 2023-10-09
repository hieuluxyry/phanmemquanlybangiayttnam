package com.Models;

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
@Entity
@Table
public class ProductDetails implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @Column(name = "Code", columnDefinition = "Varchar(20)", unique = true)
    private String code;

    @Column(name = "Name", columnDefinition = "Nvarchar(30)")
    private String name;

    @ManyToOne
    @JoinColumn(name = "IdSubstance")
    private Substance substance;

    @ManyToOne
    @JoinColumn(name = "IdSize")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "IdColor")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "IdProductType")
    private ProductType productType;

    @Column(name = "Describe", columnDefinition = "Nvarchar(MAX)")
    private String describe;

    @Column(name = "Amount")
    private int amount;

    @Column(name = "ImportPrice", columnDefinition = "Decimal(20,0)")
    private Float importPrice;

    @Column(name = "ExportPrice", columnDefinition = "Decimal(20,0)")
    private Float exportPrice;

    @Column(name = "Status")
    private int status;

    @OneToMany(mappedBy = "productDetails", fetch = FetchType.LAZY)
    private List<BillDetails> listBillDetails;

    @OneToMany(mappedBy = "productDetails", fetch = FetchType.LAZY)
    private List<PromotionDetails> listPromotionDetails;

    public ProductDetails(String code, String name, Substance substance, Size size, Color color, ProductType productType, String describe, int amount, Float exportPrice, int status) {
        this.code = code;
        this.name = name;
        this.substance = substance;
        this.size = size;
        this.color = color;
        this.productType = productType;
        this.describe = describe;
        this.amount = amount;
        this.exportPrice = exportPrice;
        this.status = status;
    }

    public ProductDetails(String id) {
        this.id = id;
    }
}
