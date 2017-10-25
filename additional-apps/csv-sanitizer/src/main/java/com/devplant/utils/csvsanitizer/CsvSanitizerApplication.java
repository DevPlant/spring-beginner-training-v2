package com.devplant.utils.csvsanitizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableConfigurationProperties(CsvSanitizerApplication.SanitizerProperties.class)
@SpringBootApplication
public class CsvSanitizerApplication implements CommandLineRunner {

    @Autowired
    private SanitizerProperties sanitizerProperties;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(CsvSanitizerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Resource resource = applicationContext.getResource(sanitizerProperties.inFile);

        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
            CSVPrinter bookPrinter = new CSVPrinter(new FileWriter(sanitizerProperties.bookOutFile), CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL).withHeader(sanitizerProperties.bookHeaders.toArray(new String[0])));
            CSVPrinter authorPrinter = new CSVPrinter(new FileWriter(sanitizerProperties.authorOutFile), CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL).withHeader(sanitizerProperties.authorHeaders.toArray(new String[0])));

            Random rd = new Random();
            List<CSVRecord> records = parser.getRecords();
            records.remove(0);

            Map<String, List<String>> bookRecords = new HashMap<>();
            Map<String, List<String>> authorRecords = new HashMap<>();
            records.forEach(record ->

            {
                List<String> bookRecord = new ArrayList<>();
                List<String> authorRecord = new ArrayList<>();

                for (Integer i : sanitizerProperties.bookColumns) {
                    bookRecord.add(Jsoup.clean(record.get(i), new Whitelist()));
                }

                List<String> possibleSynopsisValues = new ArrayList<>();

                for (Integer i : sanitizerProperties.bookSynopsisColumns) {
                    possibleSynopsisValues.add(Jsoup.clean(record.get(i), new Whitelist()));
                }

                bookRecord.add(getFirstValue(possibleSynopsisValues));


                String year = record.get(sanitizerProperties.bookYearColumn);

                bookRecord.add(getYear(year));
                bookRecord.add(String.valueOf(rd.nextInt(10)));
                bookRecords.putIfAbsent(bookRecord.get(0), bookRecord);


                for (Integer i : sanitizerProperties.authorColumns) {
                    authorRecord.add(Jsoup.clean(record.get(i), new Whitelist()));
                }

                List<String> authorBioColumns = new ArrayList<>();
                for (Integer i : sanitizerProperties.authorBioColumns) {
                    authorBioColumns.add(Jsoup.clean(record.get(i), new Whitelist()));
                }

                authorRecord.add(getFirstValue(authorBioColumns));


                authorRecords.putIfAbsent(authorRecord.get(0), authorRecord);


            });

            bookRecords.values().forEach(value -> {
                try {
                    bookPrinter.printRecord(value);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });

            authorRecords.values().forEach(value -> {
                try {
                    authorPrinter.printRecord(value);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });

            bookPrinter.flush();
            bookPrinter.close();

            authorPrinter.flush();
            authorPrinter.close();
        }
    }

    private String getYear(String year) {
        if (!StringUtils.isBlank(year)) {
            try {
                String yearValue = year.split(" ")[1];
                return Integer.parseInt(yearValue) + "";
            } catch (Exception e) {
                return "2012";
            }
        } else {
            return "2012";
        }
    }


    private String getFirstValue(List<String> possibleValues) {
        for (String value : possibleValues) {
            if (!StringUtils.isBlank(value)) {
                return value.length() > 3990 ? value.substring(0, 3990) + " ..." : value;
            }
        }
        return null;
    }

    @Data
    @ConfigurationProperties("sanitizer")
    public static class SanitizerProperties {

        private List<Integer> bookColumns = Arrays.asList(1, 2, 8);
        private List<Integer> bookSynopsisColumns = Arrays.asList(21, 20, 19);
        private List<Integer> authorColumns = Arrays.asList(2);
        private List<Integer> authorBioColumns = Arrays.asList(4);
        private Integer bookYearColumn = 13;
        private List<String> bookHeaders = Arrays.asList("Title", "Author Name", "ISBN", "Synopsis", "Year", "Stock");
        private List<String> authorHeaders = Arrays.asList("Author", "Author Bio");

        private String oldSeparator = ",";
        private String newSeparator = ",";
        private String inFile;

        private File bookOutFile;
        private File authorOutFile;
    }
}
