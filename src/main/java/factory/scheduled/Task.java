package factory.scheduled;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Data
@AllArgsConstructor
public class Task {

    private Object object;
    private Method method;
    private Scheduled schedule;
}
