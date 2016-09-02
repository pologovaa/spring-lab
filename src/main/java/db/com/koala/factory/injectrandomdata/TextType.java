package db.com.koala.factory.injectrandomdata;

import org.fluttercode.datafactory.impl.DataFactory;

import java.util.function.Supplier;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
public enum TextType {
    ADDRESS(() -> new DataFactory().getAddress()),
    EMAIL_ADDRESS(() -> new DataFactory().getEmailAddress()),
    NAME(() -> new DataFactory().getName()),
    FIRST_NAME(() -> new DataFactory().getFirstName()),
    LAST_NAME(() -> new DataFactory().getLastName()),
    CITY(() -> new DataFactory().getCity()),
    DEFAULT(() -> null);

    private Supplier<Object> supplier;

    TextType(Supplier<Object> supplier) {
        this.supplier = supplier;
    }

    public Object getRandomValue() {
        return supplier.get();
    }

}
