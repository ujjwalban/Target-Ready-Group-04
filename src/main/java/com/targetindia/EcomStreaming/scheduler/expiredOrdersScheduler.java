package com.targetindia.EcomStreaming.scheduler;

import com.targetindia.EcomStreaming.service.ArchivedOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class expiredOrdersScheduler {
    @Autowired
    private ArchivedOrdersService archivedOrdersService;

    @Scheduled(cron = "@daily")
    public void archiveExpired(){
        archivedOrdersService.archiveExpiredOrders();
    }
}
