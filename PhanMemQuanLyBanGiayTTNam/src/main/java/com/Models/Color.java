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
public class Color implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @Column(name = "Code", columnDefinition = "Varchar(20)", unique = true)
    private String code;
    @Column(name = "Name", columnDefinition = "Nvarchar(30)")
    private String name;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    private List<ProductDetails> listProductDetails;
    
    public Color(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
