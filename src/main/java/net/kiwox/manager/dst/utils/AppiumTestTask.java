package net.kiwox.manager.dst.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.kiwox.manager.dst.wrappers.AppiumTestTaskItem;

import static net.kiwox.manager.dst.utils.Utils.*;

public class AppiumTestTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppiumTestTask.class);
	
    private IAppiumTestTaskListener listener;
    private Long probeId;
    private int maxRetries;
    private Queue<AppiumTestTaskItem> queue;
    private boolean running;

    public AppiumTestTask(IAppiumTestTaskListener listener, Long probeId, int maxRetries) {
        this.listener = listener;
        this.probeId = probeId;
        this.maxRetries = maxRetries;
        this.queue = new PriorityBlockingQueue<>();
        this.running = false;
    }

    @Override
    public void run() {
        running = true;
        String currentApn = null;
        int retries = 0;
        try {
            while (!queue.isEmpty()) {
                AppiumTestTaskItem item = queue.peek();
                boolean onlyPing = false;
                if (!isNull(item.getApn())) {
                    onlyPing = item.getApn().equals(currentApn);
                }
                if (listener.runTest(probeId, item, onlyPing)) {
                    if (!isNull(item.getApn())) {
                        currentApn = item.getApn();
                    }
                    retries = 0;
                } else {
                    ++retries;
                }
                queue.poll();

                if (retries >= maxRetries) {
                    currentApn = null;
                    retries = 0;
                    while (!queue.isEmpty()) {
                        AppiumTestTaskItem i = queue.peek();
                        if (!isNull(i.getApn())){
                            if (!i.getApn().equals(item.getApn())) {
                                break;
                            }
                        }
                        listener.markDisconnected(i.getId());
                        queue.poll();
                    }
                }
            }
        } catch (Exception e) {
        	LOGGER.error("Error in appium test thread", e);
		} finally {
            running = false;
        }
    }

    public synchronized void updateQueue(List<AppiumTestTaskItem> items) {
        int maxPriority = 0;
        Iterator<AppiumTestTaskItem> it = queue.iterator();
        while (it.hasNext()) {
            maxPriority = Math.max(maxPriority, it.next().getPriority());
        }

        Map<String, Integer> apns = new HashMap<>();
        for (AppiumTestTaskItem item : items) {
            int priority;
            if (apns.containsKey(item.getApn())) {
                priority = apns.get(item.getApn());
            } else {
                priority = ++maxPriority;
                apns.put(item.getApn(), priority);
            }
            item.setPriority(priority);

            if (!queue.contains(item)) {
                queue.add(item);
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

}
