package com.sharashkina_kontora.travel_agency.view.components.amin;

import com.sharashkina_kontora.travel_agency.data_providers.LocationDataProvider;
import com.sharashkina_kontora.travel_agency.data_providers.TourDataProvider;
import com.sharashkina_kontora.travel_agency.domain.Flight;
import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.service.FlightService;
import com.sharashkina_kontora.travel_agency.service.LocationService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringComponent
@UIScope
public class AdminPage extends VerticalLayout {
    private final Crud<Location> locationCrud;
    private final LocationService locationService;
    private final LocationDataProvider locationDataProvider;
    private final FlightService flightService;
    private final Crud<Tour> tourCrud;
    private final ComboBox<Location> location = new ComboBox<>("location");
    private final MultiSelectListBox<Flight> flights = new MultiSelectListBox<>();
    private final TourDataProvider tourDataProvider;

    public AdminPage(LocationService locationService, LocationDataProvider locationDataProvider, FlightService flightService, TourDataProvider tourDataProvider) {
        this.locationService = locationService;
        this.locationDataProvider = locationDataProvider;
        this.flightService = flightService;
        this.tourDataProvider = tourDataProvider;

        locationCrud = configureLocationCrud();

        tourCrud = configureTourCrud();

        add(locationCrud, tourCrud);
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

    private CrudEditor<Location> createLocationEditor() {
        TextField city = new TextField("city");
        TextField country = new TextField("country");
        FormLayout form = new FormLayout(country, city);

        Binder<Location> binder = new Binder<>(Location.class);
        binder.forField(city).asRequired().bind(Location::getCity, Location::setCity);
        binder.forField(country).asRequired().bind(Location::getCountry, Location::setCountry);

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
        binder.forField(name).asRequired().bind(Tour::getName, Tour::setName);

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

    private void setupLocationDataProvider(Crud<Location> locationCrud) {
        locationCrud.setDataProvider(locationDataProvider);
        locationCrud.addDeleteListener(deleteEvent ->
                locationDataProvider.delete(deleteEvent.getItem())
        );
        locationCrud.addSaveListener(saveEvent ->
                locationDataProvider.save(saveEvent.getItem())
        );
    }

    private void setupTourDataProvider(Crud<Tour> tourCrud) {
        tourCrud.setDataProvider(tourDataProvider);
        tourCrud.addDeleteListener(deleteEvent ->
                tourDataProvider.delete(deleteEvent.getItem())
        );
        tourCrud.addSaveListener(saveEvent ->
                tourDataProvider.save(saveEvent.getItem())
        );
    }
}
