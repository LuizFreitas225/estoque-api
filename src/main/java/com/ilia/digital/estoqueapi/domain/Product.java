package com.ilia.digital.estoqueapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@EqualsAndHashCode
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @JsonIgnore
    @OneToOne(mappedBy = "product", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private ProductStock productStock;

    @NotEmpty(message = "The name of product cannot is empty")
    private  String name;
    @Column(unique = true)
    @NotEmpty(message = "The code of product cannot is empty")
    private String code;
    private String description;
    private Float price;
    private ProductCategory productCategory;
}
