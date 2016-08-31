package factory;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(RandomDataTypes.class)
public @interface RandomDataType {
    Class value();
}
