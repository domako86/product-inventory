package com.example.productinventory.entities;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
@Schema(description = "Product entity")
@Entity
public class Product {
    @Schema(description = "Unique identifier of the product", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Name of the product", example = "Pikachu Plushie")
    @NotBlank
    private String name;
    @Schema(description = "Description of the product", example = "Electric pokémon")
    private String description;
    @Schema(description = "Price of the product", example = "9.99")
    @NotNull
    private BigDecimal price;
    @Schema(description = "Quantity of the products in stock", example = "10")
    @NotNull
    private Integer quantity;
    @Schema(description = "Version of the product for the optimistic locking", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Version
    private Integer version;

    public Product() {
    }

    public Product(Long id, String name, String description, BigDecimal price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
