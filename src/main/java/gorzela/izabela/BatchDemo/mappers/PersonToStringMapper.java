package gorzela.izabela.BatchDemo.mappers;

import gorzela.izabela.BatchDemo.domain.Person;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PersonToStringMapper {

    public String mapPersonToString(Person person) {
        StringBuilder strBuilder = new StringBuilder()
                .append(person.id().toString())
                .append(", ")
                .append(person.name())
                .append(", ")
                .append(dateToString(person.creationDate()));
        return strBuilder.toString();
    }

    private String dateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String dateStr = formatter.format(date);
        return date.isAfter(LocalDate.of(2020, 1,1)) ? "####" + dateStr.substring(4, 10) : dateStr;
    }
}