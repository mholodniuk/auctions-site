package com.pwr.auctionsite;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@Theme(value = "flowcrmtutorial")
@PWA(name = "Flow CRM Tutorial", shortName = "Flow CRM Tutorial", offlineResources = "")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
