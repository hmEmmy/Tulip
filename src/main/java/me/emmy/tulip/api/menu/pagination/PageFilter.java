package me.emmy.tulip.api.menu.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class PageFilter<T> {

    @Getter
    private final String name;
    private final Predicate<T> predicate;
    @Getter
    @Setter
    private boolean enabled;

    public boolean test(T t) {
        return !enabled || predicate.test(t);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PageFilter && ((PageFilter) object).getName().equals(name);
    }

}
