package gorzela.izabela.BatchDemo.filters;

import gorzela.izabela.BatchDemo.domain.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NameFilter implements Filter {
    @Value("${filter.namePrefix}")
    private String namePrefix;
    @Override
    public boolean filterPerson(Person person) {
        return person.name().indexOf(namePrefix) == 0;
    }
}