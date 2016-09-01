package factory.scheduled;

import factory.singleton.Singleton;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Singleton
public class ScheduledTaskManagerImpl implements ScheduledTaskManager {
    private volatile List<Task> tasks = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            //TODO: execute tasks in separate threads according to their schedule
            System.out.println("Running");
            Thread.sleep(2000);
            for (Task task : tasks) {
                task.getMethod().invoke(task.getObject());
                Thread.sleep(2000);
            }
        }

    }
}
