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

public class Promotion implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @Column(name = "Code", columnDefinition = "Varchar(20)", unique = true)
    private String code;

    @Column(name = "Name", columnDefinition = "Nvarchar(MAX)")
    private String name;
    
    @Column(name = "decreaseNumber")
    private int decreaseNumber;

    @Column(name = "StartDay")
    private Date startDay;

    @Column(name = "EndDay")
    private Date endDay;

    @Column(name = "Status")
    private int status;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionDetails> listPromotionDetail;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<BillDetails> listBillDetail;

    public Promotion(String code, String name, int decreaseNumber, Date startDay, Date endDay, int status) {
        this.code = code;
        this.name = name;
        this.decreaseNumber = decreaseNumber;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
    }

    public Promotion(String code, int decreaseNumber, int status) {
        this.code = code;
        this.decreaseNumber = decreaseNumber;
        this.status = status;
    }

    public Promotion(String id) {
        this.id = id;
    }
}
