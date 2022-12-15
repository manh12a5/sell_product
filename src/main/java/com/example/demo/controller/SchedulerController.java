package com.example.demo.controller;

import com.example.demo.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerController {

    @Autowired
    private ICartService cartService;

    @Scheduled(cron = "${custom.scheduler.cart.cron: 0 0 * * * ?}")
    public void removeGuestCart() {
        log.info("Run cron remove guest cart");
        cartService.removeGuestCart();
    }

}
