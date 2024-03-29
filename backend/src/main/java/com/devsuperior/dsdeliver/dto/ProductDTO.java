package com.devsuperior.dsdeliver.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.devsuperior.dsdeliver.entities.Product;

import static java.util.stream.Collectors.toList;

public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imageUri;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Double price, String description, String imageUri) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUri = imageUri;
    }

    public ProductDTO(Product entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.price = entity.getPrice();
            this.description = entity.getDescription();
            this.imageUri = entity.getImageUri();
        } else {
            this.id = null;
            this.name = "Produto Não Disponível";
            this.price = 0.0;
            this.description = "Descrição Não Disponível";
            this.imageUri = "URI Não Disponível";
        }
    }

    public static List<ProductDTO> fromAll(List<Product> list) {
        if (list == null){
            list = new ArrayList<>();
        }

        if (!list.isEmpty()){
            return list.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<ProductDTO>();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
