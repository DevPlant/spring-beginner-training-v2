package com.devplant.basics.security.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.devplant.basics.security.service.ReservationCheckService;

@Service
public class BookReservationChecker {

    @Autowired
    private ReservationCheckService reservationCheckService;

    @Scheduled(fixedRate = 120000)
    public void checkReservations() {
        reservationCheckService.checkReservations();
    }

}
