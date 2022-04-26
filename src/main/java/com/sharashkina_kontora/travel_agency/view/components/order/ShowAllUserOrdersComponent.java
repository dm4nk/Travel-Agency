package com.sharashkina_kontora.travel_agency.view.components.order;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Status;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
@SpringComponent
@CssImport("./styles/styles.css")
public class ShowAllUserOrdersComponent extends HorizontalLayout {
    private final ShowOrderComponent showOrderComponent;

    public ShowAllUserOrdersComponent(ShowOrderComponent showOrderComponent) {
        this.showOrderComponent = showOrderComponent;
    }

    public HorizontalLayout initComponent(Order order) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.getElement().getClassList().add("order-component");
        layout.setSizeFull();
        Tour tour = order.getTour();

        VerticalLayout rightPart = new VerticalLayout();
        VerticalLayout leftPart = new VerticalLayout();

        leftPart.add(new H2(tour.getName() + ": " + order.getStatus()));

        rightPart.add(new H4("location: " + tour.getLocation()),
                new H4("price: " + tour.getPrice()),
                new H4("duration: " + tour.getDuration()));

        H1 id = new H1("ID:" + order.getId());
        if (order.getStatus() == Status.DONE)
            id.getElement().getThemeList().add("success");

        if (order.getStatus() == Status.DONE)
            id.getElement().getThemeList().add("error");

        layout.add(id, leftPart, rightPart);
        layout.addClickListener(e -> showOrderComponent.showOrder(order));

        return layout;
    }
}
