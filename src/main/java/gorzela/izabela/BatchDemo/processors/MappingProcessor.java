package gorzela.izabela.BatchDemo.processors;

import gorzela.izabela.BatchDemo.domain.Person;
import gorzela.izabela.BatchDemo.mappers.PersonToStringMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MappingProcessor implements ItemProcessor<Person, String> {

    private final PersonToStringMapper personToStringMapper;
    MappingProcessor(PersonToStringMapper personToStringMapper) {
        this.personToStringMapper = personToStringMapper;
    }

    @Override
    public String process(@NonNull Person person) throws Exception {
        return personToStringMapper.mapPersonToString(person);
    }
}