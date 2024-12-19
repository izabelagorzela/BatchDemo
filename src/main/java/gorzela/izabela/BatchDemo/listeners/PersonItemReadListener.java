package gorzela.izabela.BatchDemo.listeners;

import gorzela.izabela.BatchDemo.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
public class PersonItemReadListener implements ItemReadListener<Person> {

    private final static Logger logger = LoggerFactory.getLogger(PersonItemReadListener.class);

    @Override
    public void onReadError(Exception ex) {
        if (ex instanceof FlatFileParseException parseException) {
            String errorStatement = ex.getMessage() + " has occurred";
            logger.error("-----> " +  errorStatement + " <-----");
        }
        else {
            logger.error("-----> An error occur <-----", ex);
        }
    }
}