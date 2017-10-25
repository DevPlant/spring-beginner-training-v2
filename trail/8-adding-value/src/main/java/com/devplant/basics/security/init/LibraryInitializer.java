package com.devplant.basics.security.init;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.properties.LibraryProperties;
import com.devplant.basics.security.repository.AuthorRepository;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.repository.BookStockRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("library-data-import")
public class LibraryInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LibraryProperties libraryProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AsyncRecordProcessor asyncRecordProcessor;

    @Override
    public void run(String... strings) throws Exception {

        Resource bookResource = applicationContext.getResource(libraryProperties.getBookDataFile());

        Resource authorResource = applicationContext.getResource(libraryProperties.getAuthorDataFile());

        try (InputStreamReader bookReader = new InputStreamReader(bookResource.getInputStream());
             InputStreamReader authorReader = new InputStreamReader(authorResource.getInputStream())) {

            CSVParser authorParser = new CSVParser(authorReader, CSVFormat.DEFAULT);
            List<CSVRecord> authorRecords = getRecords(authorParser);


            List<CompletableFuture<Author>> authorFutures = new ArrayList<>();
            authorRecords.forEach(record -> authorFutures.add(asyncRecordProcessor.createAuthor(record)));

            // Save all authors
            CompletableFuture.allOf(authorFutures.toArray(new CompletableFuture[0])).join();
            authorRepository.save(authorFutures.stream().map(this::getResult).collect(Collectors.toList()));


            // do the books
            CSVParser bookParser = new CSVParser(bookReader, CSVFormat.DEFAULT);
            List<CSVRecord> bookRecords = getRecords(bookParser);

            List<CompletableFuture<Void>> bookFutures = new ArrayList<>();
            bookRecords.forEach(record -> bookFutures.add(asyncRecordProcessor.processBook(record)));
            CompletableFuture.allOf(bookFutures.toArray(new CompletableFuture[0])).join();

            log.info("Import completed - Authors: {}, Books: {}, Total Stocks: {}", authorRepository.count(), bookRepository.count(), bookStockRepository.count());
        }
    }

    private Author getResult(CompletableFuture<Author> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    private List<CSVRecord> getRecords(CSVParser csvParser) throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        records.remove(0);
        return records;
    }

    @Service
    public static class AsyncRecordProcessor {

        @Autowired
        private AuthorRepository authorRepository;

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private BookStockRepository bookStockRepository;

        @Async
        public CompletableFuture<Author> createAuthor(CSVRecord csvRecord) {
            String authorName = csvRecord.get(0);
            String authorBio = csvRecord.get(1);
            Author author = Author.builder().name(authorName).bio(authorBio).build();

            return CompletableFuture.completedFuture(author);
        }

        @Async
        public CompletableFuture<Void> processBook(CSVRecord csvRecord) {

            String bookTitle = csvRecord.get(0);
            String authorName = csvRecord.get(1);
            String isbn = csvRecord.get(2);
            String synopsis = csvRecord.get(3);
            Integer year = Integer.parseInt(csvRecord.get(4));
            Integer stock = Integer.parseInt(csvRecord.get(5));

            Book book = Book.builder()
                    .name(bookTitle)
                    .isbn(isbn)
                    .year(year)
                    .author(authorRepository.findByName(authorName))
                    .synopsis(synopsis).build();

            List<BookStock> stocks = new ArrayList<>();
            IntStream.range(0, stock).forEach(i -> {
                        BookStock bookStock = BookStock.builder().available(true).book(book).build();
                        stocks.add(bookStock);
                    }
            );

            bookRepository.save(book);
            bookStockRepository.save(stocks);

            return CompletableFuture.completedFuture(null);
        }

    }


}
