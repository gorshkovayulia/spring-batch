package batchsample;

import org.springframework.batch.item.file.transform.FieldSet;

import java.net.BindException;

/**
 * Is used to convert the line split by the `LineTokenizer` into a domain object
 */
public interface FieldSetMapper<T> {
    /**
     * @param fieldSet comes from the LineTokenizer
     */
    T mapFieldSet(FieldSet fieldSet) throws BindException;
}
