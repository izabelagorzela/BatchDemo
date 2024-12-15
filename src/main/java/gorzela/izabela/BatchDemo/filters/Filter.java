package gorzela.izabela.BatchDemo.filters;

import gorzela.izabela.BatchDemo.domain.Person;

public interface Filter {

    boolean filterPerson(Person person);
}