package com.sharashkina_kontora.travel_agency.data_providers;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.SortDirection;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.function.Predicate;

public abstract class AbstractDataProvider<T, E> extends AbstractBackEndDataProvider<T, E> {
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

    }

    public T save(T t) {
        return null;
    }
}
