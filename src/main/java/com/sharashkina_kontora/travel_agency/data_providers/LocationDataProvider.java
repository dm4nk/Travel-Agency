package com.sharashkina_kontora.travel_agency.data_providers;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.service.LocationService;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Consumer;
import java.util.stream.Stream;

@SpringComponent
public class LocationDataProvider extends AbstractDataProvider<Location, CrudFilter> {
    private final LocationService locationService;

    private Consumer<Long> sizeChangeListener;

    public LocationDataProvider(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    protected Stream<Location> fetchFromBackEnd(Query<Location, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Pageable pageable = PageRequest.of(offset, limit);
        Stream<Location> stream = locationService.findAll(pageable).stream();

        if (query.getFilter().isPresent()) {
            stream = stream
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<Location, CrudFilter> query) {
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    public void delete(Location location) {
        locationService.delete(location);
    }

    public Location save(Location location) {
        return locationService.save(location);
    }
}
