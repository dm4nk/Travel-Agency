package com.sharashkina_kontora.travel_agency.view.components.tour;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Status;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.TourService;
import com.sharashkina_kontora.travel_agency.view.components.order.EditOrderComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
@SpringComponent
public class ShowAllToursComponent {
    private final TourService tourService;
    private final EditOrderComponent editOrderComponent;

    public ShowAllToursComponent(TourService tourService, EditOrderComponent editOrderComponent) {
        this.tourService = tourService;
        this.editOrderComponent = editOrderComponent;
    }

    public Component initComponent(User user) {
        VerticalLayout verticalLayout = new VerticalLayout();
        tourService.findAll().forEach(tour -> verticalLayout.add(createTourComponent(tour, user)));
        return verticalLayout;
    }

    private Component createTourComponent(Tour tour, User user) {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("order-component");

        HorizontalLayout innerLayout = new HorizontalLayout();

        H2 tourName = new H2(tour.getName());
        H3 to = new H3("TO: " + tour.getLocation() + " FOR: " + tour.getDuration() + " days");
        innerLayout.add(tourName, to);
        innerLayout.setAlignSelf(HorizontalLayout.Alignment.CENTER, to);

        layout.add(innerLayout);

        FormLayout flightsLayout = new FormLayout();
        layout.add(new H4("Flights: "));
        tour.getFlights().forEach(flight -> flightsLayout.add(new Paragraph(flight.toString())));

        layout.add(flightsLayout);
        HorizontalLayout priceLayout = new HorizontalLayout();
        if (user != null) {
            Button addTourButton = new Button("Add to orders", VaadinIcon.PLUS.create());
            addTourButton.addClickListener(event -> editOrderComponent.editOrder(Order.builder().user(user).status(Status.PLANNED).build(), tour));
            priceLayout.add(addTourButton);
            priceLayout.setAlignSelf(FlexComponent.Alignment.END, addTourButton);
        }

        Paragraph price = new Paragraph(tour.getFreePlaces() + " places left, $" + tour.getPrice());
        priceLayout.addAndExpand(price);

        layout.add(priceLayout);

        return layout;
    }
}
