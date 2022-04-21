package com.sharashkina_kontora.travel_agency.view.components.order;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.OrderService;
import com.sharashkina_kontora.travel_agency.view.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@UIScope
@SpringComponent
public class ShowOrderComponent extends VerticalLayout {
    private final OrderService orderService;

    private final Button cancel = new Button("Cancel");
    private final Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private final HorizontalLayout actions = new HorizontalLayout(delete, cancel);

    private final Dialog dialog = new Dialog();
    private final Notification notification = new Notification();

    private final H2 id = new H2();
    private final H3 status = new H3();
    private final H3 price = new H3();
    private final H3 duration = new H3();
    private final H3 location = new H3();
    private List<H4> flightList = new ArrayList<>();

    @Setter
    private ChangeHandler changeHandler;
    private Order order;

    public ShowOrderComponent(OrderService orderService) {
        this.orderService = orderService;
        setSizeFull();
        delete.getElement().getThemeList().add("error");
        initNotification();
        add(actions);
        cancel.addClickListener(event -> dialog.close());

        add(id, status, price, duration, location);

        delete.addClickListener(e -> delete());
    }

    public void showOrder(Order order) {
        this.order = order;
        Tour tour = order.getTour();

        id.setText("ID:" + order.getId() + " " + tour.getName());
        status.setText("STATUS: " + order.getStatus());
        price.setText("PRICE: $" + tour.getPrice());
        duration.setText("DURATION: " + tour.getDuration() + " d");
        location.setText("LOCATION: " + tour.getLocation());

        flightList.forEach(this::remove);

        flightList = new ArrayList<>();
        tour.getFlights().forEach(flight -> {
            flightList.add(new H4(flight.toString()));
        });

        flightList.forEach(this::add);
        dialog.open();
        dialog.add(this);
    }

    private void delete() {
        try {
            orderService.delete(order);
            changeHandler.onChange(order.getUser());
            dialog.close();
        } catch (Exception e) {
            notification.open();
        }
    }

    private void initNotification() {
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.setDuration(MainView.NOTIFICATION_DURATION);
        notification.setText("Order Deleted");
    }

    public interface ChangeHandler {
        void onChange(User user);
    }
}
