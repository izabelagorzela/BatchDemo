package gorzela.izabela.BatchDemo.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class EmptyInputListener implements StepExecutionListener {

    private final static Logger logger = LoggerFactory.getLogger(EmptyInputListener.class);

    @Nullable
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getReadCount() > 0) {
            return stepExecution.getExitStatus();
        } else {
            logger.error("-----> The input file is empty <-----");
            return ExitStatus.FAILED;
        }
    }
}