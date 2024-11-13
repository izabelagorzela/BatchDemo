package gorzela.izabela.BatchDemo;


import gorzela.izabela.BatchDemo.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class BatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);
    @Value("${file.input}")
    private String fileInput;
    @Bean
    FlatFileItemReader<Person> personReader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("id", "name", "dateCreation")
                .fieldSetMapper(new RecordFieldSetMapper<>(Person.class))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Person> personWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO person (id, name, dateCreation) VALUES (:id, :name, :dateCreation)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job personJob(JobExecutionListener personJobListener, JobRepository jobRepository, Step step1) {
        return new JobBuilder("UserJob", jobRepository)
                .listener(personJobListener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      FlatFileItemReader<Person> personReader,
                      ItemProcessor<Person, Person> personProcessor,
                      JdbcBatchItemWriter<Person> personWriter
    ) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person> chunk(10, transactionManager)
                .reader(personReader)
                .processor(personProcessor)
                .writer(personWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Person, Person> personProcessor() {
        return new ItemProcessor<Person, Person>() {
            @Override
            public Person process(@NonNull Person person) throws Exception {
                return person;
            }
        };
    }

    @Bean
    public JobExecutionListener personJobListener() {
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
}