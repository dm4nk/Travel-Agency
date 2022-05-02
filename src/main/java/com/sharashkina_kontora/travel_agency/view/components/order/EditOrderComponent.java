package com.sharashkina_kontora.travel_agency.view.components.order;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.service.OrderService;
import com.sharashkina_kontora.travel_agency.service.TourService;
import com.sharashkina_kontora.travel_agency.view.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class EditOrderComponent extends FormLayout implements KeyNotifier {
    private final OrderService orderService;
    private final TourService tourService;

    private final ComboBox<Tour> tour = new ComboBox<>();
    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final HorizontalLayout actions = new HorizontalLayout(save, cancel);
    private final BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);
    private final Dialog dialog = new Dialog();
    private final Notification notification = new Notification();
    @Setter
    private ChangeHandler changeHandler;
    private Order order;

    public EditOrderComponent(OrderService orderService, TourService tourService) {
        this.orderService = orderService;
        this.tourService = tourService;

        add(tour, actions);

        binder.forField(tour)
                .asRequired("Tour is required")
                .bind(Order::getTour, Order::setTour);

        save.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> {
            try {
                save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        save.addClickListener(e -> {
            try {
                save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addClickListener(e -> dialog.close());

        initNotification();
    }

    public void editOrder(Order newOrder, Tour withTour) {
        if (newOrder == null) {
            return;
        }

        if (newOrder.getId() != null) {
            this.order = orderService.findById(newOrder.getId()).orElse(newOrder);
        } else {
            this.order = newOrder;
        }

        tour.setItems(tourService.findAll());
        tour.setEnabled(withTour == null);
        binder.setBean(order);

        tour.setValue(withTour);

        dialog.open();
        dialog.add(this);
    }

    private void save() throws Exception {
        if (binder.validate().isOk()) {
            orderService.save(order);
            changeHandler.onChange(order.getUser());
            dialog.close();
        }
    }

    private void initNotification() {
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.setDuration(MainView.NOTIFICATION_DURATION);
        notification.setText("Failed to delete order");
    }

    public interface ChangeHandler {
        void onChange(User user);
    }
}
