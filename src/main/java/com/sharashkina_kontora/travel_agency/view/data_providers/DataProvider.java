package com.sharashkina_kontora.travel_agency.view.data_providers;

import com.sharashkina_kontora.travel_agency.service.BasicPageableService;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
public class DataProvider<T> extends AbstractBackEndDataProvider<T, CrudFilter> {
    private final BasicPageableService<T> basicPageableService;

    private Consumer<Long> sizeChangeListener;

    public DataProvider(BasicPageableService<T> basicPageableService) {
        this.basicPageableService = basicPageableService;
    }

    @Override
    protected Stream<T> fetchFromBackEnd(Query<T, CrudFilter> query) {
        if (query.getFilter().isPresent()) {
            return basicPageableService.findAll().stream()
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        } else {
            int offset = query.getOffset();
            int limit = query.getLimit();
            log.debug("Limit: " + limit + ", Offset: " + offset);
            Pageable pageable = PageRequest.of(offset, limit);
            return basicPageableService.findAll(pageable).stream().skip(offset).limit(limit);
        }
    }

    @Override
    protected int sizeInBackEnd(Query<T, CrudFilter> query) {
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }
        log.debug("Size: " + count);
        return (int) count;
    }

    public Predicate<T> predicate(CrudFilter filter) {
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<T>) t -> {
                    try {
                        Object value = valueOf(constraint.getKey(), t);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .reduce(Predicate::and)
                .orElse(e -> true);
    }

    public Comparator<T> comparator(CrudFilter filter) {
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<T> comparator = Comparator.comparing(t ->
                                (Comparable) valueOf(sortClause.getKey(), t)
                        );

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;

                    } catch (Exception ex) {
                        return (Comparator<T>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    public Object valueOf(String fieldName, T t) {
        try {
            Field field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(t);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void delete(T t) {
        basicPageableService.delete(t);
    }

    public T save(T t) {
        return basicPageableService.save(t);
    }
}
