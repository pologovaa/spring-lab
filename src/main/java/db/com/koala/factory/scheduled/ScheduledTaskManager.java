package db.com.koala.factory.scheduled;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class ScheduledTaskManager {
    private List<Task> tasks = new ArrayList<>();
    private Map<Task, ScheduledFuture> executedTasks = new HashMap<>();
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    public void addTask(Task task) {
        tasks.add(task);
    }

    @SneakyThrows
    public void run() {
        while (true) {
            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (!executedTasks.containsKey(task) && LocalDateTime.now().isAfter(task.getFromTime()) &&
                        LocalDateTime.now().isBefore(task.getToTime())) {
                    ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
                                try {
                                    task.getMethod().invoke(task.getObject());
                                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            },
                            0, task.getFrequency(), task.getTimeUnit());
                    executedTasks.put(task, scheduledFuture);
                }
                if (executedTasks.containsKey(task) && LocalDateTime.now().isAfter(task.getToTime())) {
                    executedTasks.get(task).cancel(false);
                    iterator.remove();
                }
            }
        }

    }
}
