package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.view.components.tour.ShowAllToursComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SpringComponent
@UIScope
@Slf4j
public class MainPageComponent {
    private final ShowAllToursComponent showAllToursComponent;

    public MainPageComponent(ShowAllToursComponent showAllToursComponent) {
        this.showAllToursComponent = showAllToursComponent;
    }

    public Component initComponent(User user) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        String about = null;
        try {
            about = new Scanner(new File("frontend/html/about.html")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        verticalLayout.add(new Html(about));

        verticalLayout.add(showAllToursComponent.initComponent(user));

        String contacts = null;
        try {
            contacts = new Scanner(new File("frontend/html/contacts.html")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        verticalLayout.add(new Html(contacts));

        return verticalLayout;
    }
}
