# Spring Batch with example


The code presents one of Spring Batch abilities - reading and processing csv flat file.

The task of the processor is to filter entities according to the following rules:
- only entities with name prefixed with "The Greatest" and creation date is in March

Records with creation date after 2020-01-01 should have a year masked like ####-03-01.  
Filtered records should be printed out on the screen.

Implementation includes error handling (no input found, skipping bad records).