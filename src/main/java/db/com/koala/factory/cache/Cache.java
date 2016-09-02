package db.com.koala.factory.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    int duration() default Integer.MAX_VALUE;
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
