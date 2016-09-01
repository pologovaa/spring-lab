package factory.scheduled;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public interface ScheduledTaskManager extends Runnable {
    void addTask(Task task);
}
