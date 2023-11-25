package com.vickllny.distributedcache.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.LinkedList;
import java.util.List;

@Aspect
@Component
public class CacheTransactionSynchronization implements TransactionSynchronization {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTransactionSynchronization.class);


    private static final ThreadLocal<List<Runnable>> TASK_LIST = ThreadLocal.withInitial(LinkedList::new);

    @Before(value = "@annotation(org.springframework.transaction.annotation.Transactional)")
    public void before(){
        TransactionSynchronizationManager.registerSynchronization(this);
    }

    @Override
    public void afterCommit() {
        LOGGER.debug("afterCommit...");
        try {
            if(!TASK_LIST.get().isEmpty()){
                for (final Runnable task : TASK_LIST.get()) {
                    task.run();
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void afterCompletion(final int status) {
        TASK_LIST.get().clear();
    }

    public static void addTask(final Runnable task){
        TASK_LIST.get().add(task);
    }
}
