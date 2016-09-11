package db.com.koala.factory.scheduled;

import db.com.koala.factory.context.ObjectConfigurator;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class ScheduledAnnotationObjectConfigurator implements ObjectConfigurator {
    private ScheduledTaskManager taskManager;

    public ScheduledAnnotationObjectConfigurator() {
        taskManager = new ScheduledTaskManager();
        Executors.newSingleThreadExecutor().submit(taskManager::run);
    }

    @Override
    public void configure(Object t) throws Exception {
        Class<?> type = t.getClass();
        Set<Method> allMethods = ReflectionUtils.getAllMethods(type);
        allMethods.stream().filter(method -> method.isAnnotationPresent(Scheduled.class)).forEach(method -> {
            taskManager.addTask(new Task(t, method));
        });
    }
}
