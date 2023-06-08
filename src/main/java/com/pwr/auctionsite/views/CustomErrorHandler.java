package com.pwr.auctionsite.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void error(ErrorEvent errorEvent) {
        log.error(errorEvent.toString());
        log.error(errorEvent.getThrowable().getMessage());
        Arrays.stream(errorEvent.getThrowable().getStackTrace()).forEach(e -> log.error(String.valueOf(e)));
        if (UI.getCurrent() != null) {
            UI.getCurrent().access(() -> Notification.show("Error happened. Please see the logs"));
        }
    }
}