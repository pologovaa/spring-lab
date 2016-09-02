package db.com.koala.factory.scheduled;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Data
public class Task {

    private Object object;
    private Method method;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private int frequency;
    private TimeUnit timeUnit;


    public Task(Object object, Method method) {
        this.object = object;
        this.method = method;
        Scheduled annotation = method.getAnnotation(Scheduled.class);
        this.fromTime = LocalDateTime.parse(annotation.from());
        this.toTime = LocalDateTime.parse(annotation.to());
        this.frequency = annotation.frequency();
        this.timeUnit = annotation.timeUnit();
    }
}
