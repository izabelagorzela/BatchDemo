package gorzela.izabela.BatchDemo.filters;

import gorzela.izabela.BatchDemo.domain.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DateFilter implements Filter {
    @Value("${filter.monthValue}")
    private Integer monthValue;
    @Override
    public boolean filterPerson(Person person) {
        return person.creationDate().getMonthValue() == monthValue;
    }
}