package com.devplant.basics.security.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.service.notification.INotificationService;

@Service
public class ReservationCheckService {

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private List<INotificationService> notificationServices;

    @Transactional
    public void checkReservations() {
        LocalDateTime now = LocalDateTime.now();

        Pageable pageRequest = new PageRequest(0, 1000);
        Page<BookStock> stockPage;
        do {
            stockPage = bookStockRepository.findAll(pageRequest);

            stockPage.getContent().forEach(stock -> {
                // only approved stocks
                if (stock.isApproved()) {

                    if (!stock.isPickedUp() && stock.getLatestPickupDate().isBefore(now)) {
                        // Not Picked up on time ? reset it
                        notificationServices.forEach(service -> service.notifyUserAboutPickupExpired(stock.getBook().getName(), stock.getUser().getUsername(), stock.getId()));
                        stock.setUser(null);
                        stock.setLatestReturnDate(null);
                        stock.setLatestPickupDate(null);
                        stock.setRequestDate(null);
                        stock.setAvailable(true);
                        stock.setApproved(false);
                        stock.setPickedUp(false);
                        stock.setLastReturnOverDueNotificationDate(null);
                        bookStockRepository.save(stock);
                    } else if (stock.isPickedUp() && stock.getLatestReturnDate().isBefore(now) &&
                            (stock.getLastReturnOverDueNotificationDate() == null || stock.getLastReturnOverDueNotificationDate().plusDays(1).isBefore(now))) {
                        notificationServices.forEach(service -> service.notifyUserAboutReturnDateExpired(stock.getBook().getName(), stock.getUser().getUsername(), stock.getId()));

                        stock.setOverdue(true);
                        stock.setLastReturnOverDueNotificationDate(LocalDateTime.now());
                        bookStockRepository.save(stock);
                    }

                }
            });

            pageRequest = stockPage.nextPageable();
        } while (stockPage.getSize() > 0 && pageRequest != null);
    }
}
