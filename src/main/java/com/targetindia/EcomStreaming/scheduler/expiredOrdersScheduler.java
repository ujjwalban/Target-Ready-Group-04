package com.targetindia.EcomStreaming.scheduler;

import com.targetindia.EcomStreaming.service.ArchivedOrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class expiredOrdersScheduler {
    @Autowired
    private ArchivedOrdersService archivedOrdersService;

    @Scheduled(cron = "@daily")
    public void archiveExpired(){
        archivedOrdersService.archiveExpiredOrders();
    }
}
