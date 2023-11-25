package com.vickllny.distributedcache.tx;

import com.vickllny.distributedcache.utils.ContextUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.util.*;

public class CacheTransactionManager implements PlatformTransactionManager {

    private static final ThreadLocal<Deque<Runnable>> TASK =
            ThreadLocal.withInitial(LinkedList::new);

    private final PlatformTransactionManager transactionManager;

    public CacheTransactionManager(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Override
    public TransactionStatus getTransaction(final TransactionDefinition definition) throws TransactionException {
        return transactionManager.getTransaction(definition);
    }

    @Override
    public void commit(final TransactionStatus status) throws TransactionException {
        try {
            transactionManager.commit(status);
            if(!TASK.get().isEmpty()){
                for (final Runnable task : TASK.get()) {
                    task.run();
                }
            }
        }finally {
            TASK.get().clear();
        }
    }

    @Override
    public void rollback(final TransactionStatus status) throws TransactionException {
        transactionManager.rollback(status);
    }

    public static void addTask(final Runnable task){
        if(null == ContextUtils.getBean(CacheTransactionManager.class)){
            return;
        }
        TASK.get().offer(task);
    }
}
