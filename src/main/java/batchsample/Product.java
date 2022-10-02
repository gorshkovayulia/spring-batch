package batchsample;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Product domain class maps the different columns of the flat file
 */
@Data
public class Product {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;

}
