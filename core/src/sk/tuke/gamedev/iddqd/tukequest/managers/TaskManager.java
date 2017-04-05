package sk.tuke.gamedev.iddqd.tukequest.managers;

import com.badlogic.gdx.utils.Timer;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;

import java.util.*;

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
                Log.t(this, type + "run started");
                action.run();
                if (!this.isScheduled()) {
                    removeTask(type, this);
                    Log.i(TaskManager.this, type + " ran and was removed");
                } else {
                    Log.i(TaskManager.this, type + " ran");
                }
            }
        };
        if (type != null) {
            if (!taskMap.containsKey(type)) {
                taskMap.put(type, new LinkedList<>());
            }
            taskMap.get(type).add(task);
        }
        Log.i(this, type + " scheduled");
        return task;
    }

    private void removeTask(String type, Timer.Task task) {
        if (type == null) {
            return;
        }
        List<Timer.Task> tasks = taskMap.get(type);
        if (tasks == null) {
            Log.w(this, "Invalid state, taskMap contained empty list during removeTask for " + type);
            return;
        }
        tasks.remove(task);
        if (tasks.isEmpty()) {
            taskMap.remove(type);
        }
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
        Log.i(this, type + " was removed");
    }

    @Deprecated
    public void removeAllTimers() {
        Set<String> keys = new HashSet<>();
        keys.addAll(taskMap.keySet());
        keys.forEach(this::removeTimers);
        Log.i(this, "All timers were removed");
    }

    public Set<String> scheduledTimers() {
        return taskMap.keySet();
    }

}
