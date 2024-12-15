package gorzela.izabela.BatchDemo;


import gorzela.izabela.BatchDemo.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;


@Configuration
public class BatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);
    @Value("${file.input}")
    private String fileInput;

    @Bean
    public FlatFileItemReader<Person> personReaderBean() {

        return new FlatFileItemReaderBuilder<Person>()
                .name("personReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("id", "name", "creationDate")
                .fieldSetMapper(fieldSet -> Person.builder()
                        .id(fieldSet.readInt("id"))
                        .name(fieldSet.readString("name"))
                        .creationDate(Instant.ofEpochMilli(fieldSet.readDate("creationDate", "dd.MM.yyyy").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                        .build())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public ItemWriter<String> personWriterBean() {
        return new ItemWriter<String>() {
            @Override
            public void write(Chunk<? extends String> chunk) throws Exception {
                for(String personAsString : chunk) {
                    System.out.println(personAsString);
                }
            }
        };
    }

    @Bean
    public CompositeItemProcessor<Person, String> compositeItemProcessorBean(ItemProcessor<Person, Person> filteringProcessor, ItemProcessor<Person, String> mappingProcessor) throws Exception {
        CompositeItemProcessor<Person, String> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(filteringProcessor, mappingProcessor));
        processor.afterPropertiesSet();
        return processor;
    }

    @Bean
    public Step step1Bean(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          FlatFileItemReader<Person> personReader,
                          CompositeItemProcessor<Person, String> compositeItemProcessor,
                          ItemWriter<String> personWriter
    ) {
        return new StepBuilder("step1", jobRepository)
                .<Person, String> chunk(10, transactionManager)
                .reader(personReader)
                .processor(compositeItemProcessor)
                .writer(personWriter)
                .build();
    }

    @Bean
    public JobExecutionListener personJobListenerBean() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(final JobExecution jobExecution) {
                log.info("-----> Job has started <-----");
            }
            @Override
            public void afterJob(final JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("-----> Job has ended successfully <-----");
                } else {
                    log.info("-----> Job has ended with error <-----");
                }
            }
        };
    }

    @Bean
    public Job personJobBean(JobRepository jobRepository, JobExecutionListener personJobListener, Step step1) {
        return new JobBuilder("UserJob", jobRepository)
                .listener(personJobListener)
                .flow(step1)
                .end()
                .build();
    }
}