package com.sharashkina_kontora.travel_agency.view.components.amin;

import com.sharashkina_kontora.travel_agency.domain.*;
import com.sharashkina_kontora.travel_agency.service.*;
import com.sharashkina_kontora.travel_agency.view.MainView;
import com.sharashkina_kontora.travel_agency.view.data_providers.DataProvider;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringComponent
@UIScope
public class AdminPage extends VerticalLayout {
    private static final String ERROR_LENGTH_MSG = "Length must be less then 255 characters";
    //location
    private final Crud<Location> locationCrud;
    private final LocationService locationService;
    private final DataProvider<Location> locationDataProvider;
    //tour
    private final Crud<Tour> tourCrud;
    private final TourService tourService;
    private final ComboBox<Location> location = new ComboBox<>("location");
    private final MultiSelectListBox<Flight> flights = new MultiSelectListBox<>();
    private final DataProvider<Tour> tourDataProvider;
    //flight
    private final Crud<Flight> flighCrud;
    private final FlightService flightService;
    private final DataProvider<Flight> flightDataProvider;
    //order
    private final Crud<Order> orderCrud;
    private final OrderService orderService;
    private final ComboBox<User> user = new ComboBox<>("user");
    private final ComboBox<Status> status = new ComboBox<>("status");
    private final ComboBox<Tour> tour = new ComboBox<>("tour");
    private final UserService userService;
    private final DataProvider<Order> orderDataProvider;

    private final Notification notificationError = new Notification();

    public AdminPage(LocationService locationService, TourService tourService, FlightService flightService, OrderService orderService, UserService userService) {
        this.locationService = locationService;
        this.tourService = tourService;
        this.flightService = flightService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderDataProvider = new DataProvider<>(orderService);
        this.flightDataProvider = new DataProvider<>(flightService);
        this.tourDataProvider = new DataProvider<>(tourService);
        this.locationDataProvider = new DataProvider<>(locationService);

        locationCrud = configureLocationCrud();
        tourCrud = configureTourCrud();
        flighCrud = configureFlightCrud();
        orderCrud = configureOrderCrud();

        add(new H2("Locations"), locationCrud,
                new H2("Tours"), tourCrud,
                new H2("Flights"), flighCrud,
                new H2("Orders"), orderCrud);

        initNotification();
    }

    private Crud<Location> configureLocationCrud() {
        final Crud<Location> locationCrud;
        locationCrud = new Crud<>(
                Location.class,
                createLocationEditor()
        );

        setupLocationGrid(locationCrud);
        setupLocationDataProvider(locationCrud);

        return locationCrud;
    }

    private Crud<Tour> configureTourCrud() {
        final Crud<Tour> tourCrud;
        tourCrud = new Crud<>(
                Tour.class,
                createTourEditor()
        );

        setupTourGrid(tourCrud);
        setupTourDataProvider(tourCrud);

        tourCrud.addEditListener(event -> {
            location.setItems(locationService.findAll());
            flights.setItems(flightService.findAll());
            location.setValue(event.getItem().getLocation());
            flights.setValue(event.getItem().getFlights());
        });

        return tourCrud;
    }

    private Crud<Flight> configureFlightCrud() {
        final Crud<Flight> flightCrud;
        flightCrud = new Crud<>(
                Flight.class,
                createFlightEditor()
        );

        setupFlightGrid(flightCrud);
        setupFlightDataProvider(flightCrud);

        return flightCrud;
    }

    private Crud<Order> configureOrderCrud() {
        final Crud<Order> orderCrud;
        orderCrud = new Crud<>(
                Order.class,
                createOrderEditor()
        );

        setupOrderGrid(orderCrud);
        setupOrderDataProvider(orderCrud);

        orderCrud.addEditListener(event -> tour.setEnabled(false));
        orderCrud.addNewListener(event -> tour.setEnabled(true));

        return orderCrud;
    }

    private CrudEditor<Location> createLocationEditor() {
        TextField city = new TextField("city");
        TextField country = new TextField("country");
        FormLayout form = new FormLayout(country, city);

        Binder<Location> binder = new Binder<>(Location.class);
        binder.forField(city)
                .asRequired()
                .withValidator(new StringLengthValidator(ERROR_LENGTH_MSG, 0, 255))
                .bind(Location::getCity, Location::setCity);

        binder.forField(country)
                .asRequired()
                .withValidator(new StringLengthValidator(ERROR_LENGTH_MSG, 0, 255))
                .bind(Location::getCountry, Location::setCountry);

        return new BinderCrudEditor<>(binder, form);
    }

    private CrudEditor<Tour> createTourEditor() {
        TextField name = new TextField("name");
        IntegerField freePlaces = new IntegerField("free places");
        IntegerField price = new IntegerField("price");
        IntegerField duration = new IntegerField("duration");
        location.setItems(locationService.findAll());
        flights.setItems(flightService.findAll());

        FormLayout form = new FormLayout(name, freePlaces, price, duration, location, flights);

        Binder<Tour> binder = new BeanValidationBinder<>(Tour.class);

        binder.forField(name)
                .asRequired()
                .withValidator(new StringLengthValidator(ERROR_LENGTH_MSG, 0, 255))
                .bind(Tour::getName, Tour::setName);

        binder.forField(freePlaces)
                .asRequired()
                .withValidator(new IntegerRangeValidator("Must be between 1 and 9999", 1, 9999))
                .bind(Tour::getFreePlaces, Tour::setFreePlaces);

        binder.forField(price)
                .asRequired()
                .withValidator(new IntegerRangeValidator("Must be between 1 and 9_999_999", 0, 9_999_999))
                .bind(Tour::getPrice, Tour::setPrice);

        binder.forField(duration)
                .asRequired()
                .withValidator(new IntegerRangeValidator("Must be between 1 and 365", 0, 365))
                .withConverter(Integer::longValue, Long::intValue, "Something went wrong")
                .bind(Tour::getDuration, Tour::setDuration);

        binder.forField(location)
                .asRequired()
                .bind(Tour::getLocation, Tour::setLocation);

        binder.forField(flights)
                .bind(Tour::getFlights, Tour::setFlights);

        return new BinderCrudEditor<>(binder, form);
    }

    private CrudEditor<Flight> createFlightEditor() {
        TextField name = new TextField("name");
        TextField departureAirport = new TextField("departureAirport");
        TextField arrivalAirport = new TextField("arrivalAirport");
        DatePicker date = new DatePicker("date");

        FormLayout form = new FormLayout(name, departureAirport, arrivalAirport, date);

        Binder<Flight> binder = new BeanValidationBinder<>(Flight.class);
        binder.forField(name)
                .asRequired()
                .withValidator(new StringLengthValidator(ERROR_LENGTH_MSG, 0, 255))
                .bind(Flight::getName, Flight::setName);

        binder.forField(departureAirport)
                .asRequired()
                .withValidator(new StringLengthValidator(ERROR_LENGTH_MSG, 0, 255))
                .bind(Flight::getDepartureAirport, Flight::setDepartureAirport);

        binder.forField(arrivalAirport)
                .asRequired()
                .withValidator(new StringLengthValidator(ERROR_LENGTH_MSG, 0, 255))
                .bind(Flight::getArrivalAirport, Flight::setArrivalAirport);

        binder.forField(date)
                .asRequired()
                .bind(Flight::getDate, Flight::setDate);

        return new BinderCrudEditor<>(binder, form);
    }

    private CrudEditor<Order> createOrderEditor() {
        tour.setItems(tourService.findAll());
        user.setItems(userService.findAll());
        status.setItems(Status.values());

        FormLayout form = new FormLayout(tour, user, status);

        Binder<Order> binder = new BeanValidationBinder<>(Order.class);

        binder.forField(tour)
                .asRequired()
                .bind(Order::getTour, Order::setTour);

        binder.forField(user)
                .asRequired()
                .bind(Order::getUser, Order::setUser);

        binder.forField(status)
                .asRequired()
                .bind(Order::getStatus, Order::setStatus);

        return new BinderCrudEditor<>(binder, form);
    }

    private void setupLocationGrid(Crud<Location> locationCrud) {
        Grid<Location> grid = locationCrud.getGrid();

        List<String> visibleColumns = Arrays.asList(
                "id",
                "country",
                "city",
                "vaadin-crud-edit-column"
        );
        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("country"),
                grid.getColumnByKey("city"),
                grid.getColumnByKey("vaadin-crud-edit-column")
        );

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void setupTourGrid(Crud<Tour> tourCrud) {
        Grid<Tour> grid = tourCrud.getGrid();

        List<String> visibleColumns = Arrays.asList(
                "id",
                "name",
                "freePlaces",
                "price",
                "duration",
                "location",
                "flights",
                "vaadin-crud-edit-column"
        );

        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("name"),
                grid.getColumnByKey("freePlaces"),
                grid.getColumnByKey("price"),
                grid.getColumnByKey("duration"),
                grid.getColumnByKey("location"),
                grid.getColumnByKey("flights"),
                grid.getColumnByKey("vaadin-crud-edit-column")
        );

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void setupFlightGrid(Crud<Flight> flightCrud) {
        Grid<Flight> grid = flightCrud.getGrid();

        List<String> visibleColumns = Arrays.asList(
                "id",
                "name",
                "departureAirport",
                "arrivalAirport",
                "date",
                "vaadin-crud-edit-column"
        );

        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("name"),
                grid.getColumnByKey("departureAirport"),
                grid.getColumnByKey("arrivalAirport"),
                grid.getColumnByKey("date"),
                grid.getColumnByKey("vaadin-crud-edit-column")
        );

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void setupOrderGrid(Crud<Order> orderCrud) {
        Grid<Order> grid = orderCrud.getGrid();

        List<String> visibleColumns = Arrays.asList(
                "id",
                "user",
                "tour",
                "status",
                "vaadin-crud-edit-column"
        );

        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("user"),
                grid.getColumnByKey("tour"),
                grid.getColumnByKey("status"),
                grid.getColumnByKey("vaadin-crud-edit-column")
        );

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void setupLocationDataProvider(Crud<Location> locationCrud) {
        locationCrud.setDataProvider(locationDataProvider);
        locationCrud.addDeleteListener(deleteEvent -> {
                    try {
                        locationDataProvider.delete(deleteEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Can't delete Location in use " + deleteEvent.getItem());
                        openTourDialogWithText("Can't delete Location in use");
                    }
                    tourDataProvider.refreshAll();
                }
        );
        locationCrud.addSaveListener(saveEvent -> {
                    try {
                        locationDataProvider.save(saveEvent.getItem());
                        log.debug("Saved location " + saveEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tourDataProvider.refreshAll();
                }
        );
    }

    private void setupTourDataProvider(Crud<Tour> tourCrud) {
        tourCrud.setDataProvider(tourDataProvider);
        tourCrud.addDeleteListener(deleteEvent -> {
                    try {
                        tourDataProvider.delete(deleteEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    orderDataProvider.refreshAll();
                    flightDataProvider.refreshAll();
                }
        );
        tourCrud.addSaveListener(saveEvent -> {
                    try {
                        tourDataProvider.save(saveEvent.getItem());
                        log.debug("Saved tour " + saveEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    orderDataProvider.refreshAll();
                    flightDataProvider.refreshAll();
                }
        );
    }

    private void setupFlightDataProvider(Crud<Flight> flightCrud) {
        flightCrud.setDataProvider(flightDataProvider);
        flightCrud.addDeleteListener(deleteEvent -> {
                    try {
                        flightDataProvider.delete(deleteEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Can't delete Flight in use " + deleteEvent.getItem());
                        openTourDialogWithText("Can't delete Flight in use");
                    }
                    tourDataProvider.refreshAll();
                }
        );
        flightCrud.addSaveListener(saveEvent -> {
                    try {
                        flightDataProvider.save(saveEvent.getItem());
                        log.debug("Saved flight " + saveEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tourDataProvider.refreshAll();
                }
        );
    }

    private void setupOrderDataProvider(Crud<Order> orderCrud) {
        orderCrud.setDataProvider(orderDataProvider);
        orderCrud.addDeleteListener(deleteEvent ->
                {
                    try {
                        orderDataProvider.delete(deleteEvent.getItem());
                        log.debug("Deleted order " + deleteEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tourDataProvider.refreshAll();
                }
        );
        orderCrud.addSaveListener(saveEvent ->
                {
                    try {
                        orderDataProvider.save(saveEvent.getItem());
                        log.debug("Saved order " + saveEvent.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("No free spaces left in tour " + saveEvent.getItem());
                        openTourDialogWithText("No free spaces left in tour");
                    }
                    tourDataProvider.refreshAll();
                }
        );
    }

    private void openTourDialogWithText(String text) {
        notificationError.setText(text);
        notificationError.open();
    }

    private void initNotification() {
        notificationError.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notificationError.setPosition(Notification.Position.BOTTOM_CENTER);
        notificationError.setDuration(MainView.NOTIFICATION_DURATION);
        notificationError.setText("Can't delete item in use");
    }
}
