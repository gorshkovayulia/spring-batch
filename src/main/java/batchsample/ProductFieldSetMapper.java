package batchsample;

import org.springframework.batch.item.file.transform.FieldSet;

import java.net.BindException;

/**
 * Focuses on retrieving the data from the flat file and converts values into batchsample.Product domain objects
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product> {


    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = new Product();

        product.setId(fieldSet.readString("PRODUCT_ID"));
        product.setName(fieldSet.readString("NAME"));
        product.setDescription(fieldSet.readString("DESCRIPTION"));
        product.setPrice(fieldSet.readBigDecimal("PRICE"));

        return product;
    }
}
