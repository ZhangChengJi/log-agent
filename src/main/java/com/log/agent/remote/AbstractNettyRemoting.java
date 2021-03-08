package com.log.agent.remote;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class AbstractNettyRemoting {
    /**
     * The Message executor.
     */

    protected final ScheduledExecutorService timerExecutor = new ScheduledThreadPoolExecutor(1);
    public AbstractNettyRemoting() {

    }

}
