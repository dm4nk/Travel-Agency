package com.sharashkina_kontora.travel_agency.view.components;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class MainPageComponent extends VerticalLayout {
    private final H1 label = new H1();

    public MainPageComponent() {
        log.debug("Opened Main");
        add(label);
        changeTextToMainPage();
    }

    public void changeTextToMainPage() {
        label.setText("MAIN PAGE");
    }
}
