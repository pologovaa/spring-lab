package factory.scheduled;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scheduled {
    String from() default "-999999999-01-01T00:00";
    String to() default "+999999999-12-31T23:59:59.999999999";
    int frequency();
    TimeUnit timeUnit();
}
