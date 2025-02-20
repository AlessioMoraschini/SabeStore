package com.am.design.development.utilities.impl;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class AmTaskExecutor {

    @Value("${task_executor.core_pool_size:1}")
    private int corePoolSize;

    @Value("${task_executor.max_pool_size:1}")
    private int maxPoolSize;

    @Getter
    private Executor executor;

    @PostConstruct
    public void init() {
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.initialize();
        this.executor = taskExecutor;
    }

}
