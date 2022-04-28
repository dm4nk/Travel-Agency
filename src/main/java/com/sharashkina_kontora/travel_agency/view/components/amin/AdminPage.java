package com.sharashkina_kontora.travel_agency.view.components.amin;

import com.sharashkina_kontora.travel_agency.domain.Flight;
import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.service.FlightService;
import com.sharashkina_kontora.travel_agency.service.LocationService;
import com.sharashkina_kontora.travel_agency.service.TourService;
import com.sharashkina_kontora.travel_agency.view.data_providers.DataProvider;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
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
    //tour
    private final Crud<Tour> tourCrud;
    private final TourService tourService;
    private final ComboBox<Location> location = new ComboBox<>("location");
    private final MultiSelectListBox<Flight> flights = new MultiSelectListBox<>();
    //flight
    private final Crud<Flight> flighCrud;
    private final FlightService flightService;

    public AdminPage(LocationService locationService, TourService tourService, FlightService flightService) {
        this.locationService = locationService;
        this.tourService = tourService;
        this.flightService = flightService;

        locationCrud = configureLocationCrud();
        tourCrud = configureTourCrud();
        flighCrud = configureFlightCrud();

        add(locationCrud, tourCrud, flighCrud);
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
        DateTimePicker date = new DateTimePicker("date");

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
    }

    private void setupLocationDataProvider(Crud<Location> locationCrud) {
        DataProvider<Location> dataProvider = new DataProvider<>(locationService);
        locationCrud.setDataProvider(dataProvider);
        locationCrud.addDeleteListener(deleteEvent ->
                dataProvider.delete(deleteEvent.getItem())
        );
        locationCrud.addSaveListener(saveEvent ->
                dataProvider.save(saveEvent.getItem())
        );
    }

    private void setupTourDataProvider(Crud<Tour> tourCrud) {
        DataProvider<Tour> dataProvider = new DataProvider<>(tourService);
        tourCrud.setDataProvider(dataProvider);
        tourCrud.addDeleteListener(deleteEvent ->
                dataProvider.delete(deleteEvent.getItem())
        );
        tourCrud.addSaveListener(saveEvent ->
                dataProvider.save(saveEvent.getItem())
        );
    }


    private void setupFlightDataProvider(Crud<Flight> flightCrud) {
        DataProvider<Flight> dataProvider = new DataProvider<>(flightService);
        flightCrud.setDataProvider(dataProvider);
        flightCrud.addDeleteListener(deleteEvent ->
                dataProvider.delete(deleteEvent.getItem())
        );
        flightCrud.addSaveListener(saveEvent ->
                dataProvider.save(saveEvent.getItem())
        );
    }
}
