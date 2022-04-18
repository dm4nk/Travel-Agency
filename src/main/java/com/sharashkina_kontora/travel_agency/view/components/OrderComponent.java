package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
@CssImport("./styles/styles.css")
public class OrderComponent extends HorizontalLayout {
    public OrderComponent(Order order) {
//        getElement().getStyle()
//                .set("background-color", "hsla(214, 53%, 23%, 0.16")
//                .set("border-radius", "15px")
//                .set("border", "1px solid gray");
        getElement().getClassList().add("order-component");
        setSizeFull();

        VerticalLayout rightPart = new VerticalLayout();

        Tour tour = order.getTour();

        tour.getFlights().forEach(flight ->
                rightPart.add(
                        new HorizontalLayout(
                                new H5(flight.getDepartureAirport()),
                                //VaadinIcon.ARROWS_LONG_RIGHT.create(),
                                new H5("\u27A2"),
                                new H5(flight.getArrivalAirport()
                                )
                        )
                )
        );

        VerticalLayout leftPart = new VerticalLayout();
        leftPart.add(new H2(tour.getName() + ": " + order.getStatus()),
                new H4("location: " + tour.getLocation().getCountry() + ", " + tour.getLocation().getCity()),
                new H4("price: " + tour.getPrice()),
                new H4("duration: " + tour.getDuration()));
        add(leftPart, rightPart);
    }


}
