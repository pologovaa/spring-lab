package factory.scheduled;

import factory.singleton.Singleton;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class ScheduledTaskManagerImpl implements ScheduledTaskManager {
    private volatile List<Task> tasks = new ArrayList<>();
    private Map<Task, ScheduledFuture> executedTasks = new HashMap<>();

    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            //TODO: execute tasks in separate threads according to their schedule
            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (!task.isBeingExecuted() && LocalDateTime.now().isAfter(task.getFromTime()) &&
                        LocalDateTime.now().isBefore(task.getToTime())) {
                    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
                    ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
                                try {
                                    task.getMethod().invoke(task.getObject());
                                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            },
                            0, task.getFrequency(), task.getTimeUnit());
                    task.setBeingExecuted(true);
                    executedTasks.put(task, scheduledFuture);
                }
                if (task.isBeingExecuted() && LocalDateTime.now().isAfter(task.getToTime())) {
                    executedTasks.get(task).cancel(false);
                    iterator.remove();
                }
//                task.getMethod().invoke(task.getObject());
            }
        }

    }
}
