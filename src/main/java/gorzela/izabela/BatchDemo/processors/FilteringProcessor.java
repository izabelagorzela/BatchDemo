package gorzela.izabela.BatchDemo.processors;

import gorzela.izabela.BatchDemo.domain.Person;
import gorzela.izabela.BatchDemo.filters.Filter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilteringProcessor implements ItemProcessor<Person, Person> {

    private final List<Filter> filters;
    FilteringProcessor(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public Person process(@NonNull Person person) throws Exception {
        for(Filter filter : filters) {
            if(!filter.filterPerson(person)) {
                return null;
            }
        } return person;
    }
}