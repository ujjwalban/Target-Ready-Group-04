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

    private static final Logger log = LoggerFactory.getLogger(expiredOrdersScheduler.class);
    @Autowired
    private ArchivedOrdersService archivedOrdersService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "@daily")
    public void archiveExpired()
    {
        log.info("The time is now {}", dateFormat.format(new Date()));
        archivedOrdersService.archiveExpiredOrders();
    }
}
