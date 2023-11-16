package com.binarfood.binarfoodapp.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerService {

    @Scheduled(cron = "0 0 12 * * MON-FRI")
    public void remindLunchPromo() {
        log.info("Ingat, ada promo makan siang di BinarFud!");
    }

    @Scheduled(cron = "0 0 18 * * MON-SUN")
    public void remindDinnerPromo() {
        log.info("Ingat, ada promo makan malam di BinarFud!");
    }

}
