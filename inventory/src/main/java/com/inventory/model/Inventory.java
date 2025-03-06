package com.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Inventory")
public class Inventory {
    @Id
    @NotBlank(message = "product sku-code should not be blank.")
    @NotNull(message = "product sku-code should not be empty.")
    private String skuCode;

    @NotNull(message = "product quantity has to be specified.")
    @Min(value = 1, message = "specify quantity > 0.")
    private int quantity;
}
