package com.sharashkina_kontora.travel_agency.data_providers;

import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.service.TourService;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Consumer;
import java.util.stream.Stream;

@SpringComponent
@Slf4j
public class TourDataProvider extends AbstractDataProvider<Tour, CrudFilter> {
    private final TourService tourService;

    private Consumer<Long> sizeChangeListener;

    public TourDataProvider(TourService tourService) {
        this.tourService = tourService;
    }

    @Override
    protected Stream<Tour> fetchFromBackEnd(Query<Tour, CrudFilter> query) {
        if (query.getFilter().isPresent()) {
            return tourService.findAll().stream()
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        } else {
            int offset = query.getOffset();
            int limit = query.getLimit();
            log.debug("Limit: " + limit + ", Offset: " + offset);
            Pageable pageable = PageRequest.of(offset, limit);
            return tourService.findAll(pageable).stream().skip(offset).limit(limit);
        }
    }

    @Override
    protected int sizeInBackEnd(Query<Tour, CrudFilter> query) {
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }
        log.debug("Size: " + count);
        return (int) count;
    }

    public void delete(Tour tour) {
        tourService.delete(tour);
    }

    public Tour save(Tour tour) {
        return tourService.save(tour);
    }
}
