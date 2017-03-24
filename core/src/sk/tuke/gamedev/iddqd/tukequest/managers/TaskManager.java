package sk.tuke.gamedev.iddqd.tukequest.managers;

import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages asynchronous tasks and provides an easy way to cancel them based on String name.
 * Wraps {@link com.badlogic.gdx.utils.Timer.Task} class to allow lambda syntax.
 * Provides overloads for repeatable tasks.
 * <p>
 * Created by Steve on 24.03.2017.
 */
public enum TaskManager {

    INSTANCE;

    private HashMap<String, List<Timer.Task>> taskMap = new HashMap<>();

    public Timer.Task scheduleTimer(String type, int delaySeconds, Runnable action) {
        return Timer.schedule(storeScheduledTimer(type, action), delaySeconds);
    }

    public Timer.Task scheduleTimer(String type, int delaySeconds, float intervalSeconds, Runnable action) {
        return Timer.schedule(storeScheduledTimer(type, action), delaySeconds, intervalSeconds);
    }

    public Timer.Task scheduleTimer(String type, int delaySeconds, float intervalSeconds, int repeatCount, Runnable action) {
        return Timer.schedule(storeScheduledTimer(type, action), delaySeconds, intervalSeconds, repeatCount);
    }

    private Timer.Task storeScheduledTimer(String type, Runnable action) {
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                action.run();
            }
        };
        if (type != null) {
            if (!taskMap.containsKey(type)) {
                taskMap.put(type, new LinkedList<>());
            }
            taskMap.get(type).add(task);
        }
        return task;
    }

    public void removeTimers(String type) {
        List<Timer.Task> tasks = taskMap.get(type);
        if (tasks == null) {
            return;
        }
        for (Timer.Task task : tasks) {
            task.cancel();
        }
        taskMap.remove(type);
    }

}
