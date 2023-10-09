package com.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class PromotionDetails implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "idPromotion")
    private Promotion promotion;

    @Id
    @ManyToOne
    @JoinColumn(name = "idProductDetails")
    private ProductDetails productDetails;
    
    @Column(name = "Status")
    private int status;
}
