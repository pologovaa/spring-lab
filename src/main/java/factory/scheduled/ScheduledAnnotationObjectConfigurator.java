package factory.scheduled;

import factory.context.ObjectConfigurator;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class ScheduledAnnotationObjectConfigurator implements ObjectConfigurator {
    private ScheduledTaskManager taskManager;

    public ScheduledAnnotationObjectConfigurator() {
        taskManager = new ScheduledTaskManagerImpl();
        new Thread(taskManager::run).start();
    }

    @Override
    public void configure(Object t) throws Exception {
        Class<?> type = t.getClass();
        Set<Method> allMethods = ReflectionUtils.getAllMethods(type);
        for (Method method : allMethods) {
            if (method.isAnnotationPresent(Scheduled.class)) {
                taskManager.addTask(new Task(t, method, method.getAnnotation(Scheduled.class)));
            }
        }
    }
}
