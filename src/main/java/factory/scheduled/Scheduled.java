package factory.scheduled;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {
    String from();
    String to();
    int frequency();
    TimeUnit timeUnit();
}
