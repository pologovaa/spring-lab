package db.com.koala.factory.injectrandomdata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomDataType {
    Class value();
}
