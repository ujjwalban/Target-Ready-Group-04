package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.service.ArchivedOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/target")
public class ArchiveController {

    @Autowired
    private ArchivedOrdersService archivedOrdersService;

    @PostMapping("/expired-orders")
    public String archiveExpiredOrders() {
        archivedOrdersService.archiveExpiredOrders();
        return "Expired orders have been archived.";
    }
}
