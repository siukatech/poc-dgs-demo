package com.siukatech.poc.dgsdemo.service;

import com.siukatech.poc.dgsdemo.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final List<Book> books = List.of(
            Book.builder().id("B1").title("Stranger Things").releaseYear(2016).build()
            , Book.builder().id("B2").title("Ozark").releaseYear(2017).build()
            , Book.builder().id("B3").title("The Crown").releaseYear(2016).build()
            , Book.builder().id("B4").title("Dead to Me").releaseYear(2019).build()
            , Book.builder().id("B5").title("Orange is the New Black").releaseYear(2013).build()
    );

    public List<Book> getBooks(String titleFilter) {
        logger.info("books - titleFilter: [{}]", titleFilter);
        if(titleFilter == null) {
            return books;
        }
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().indexOf(titleFilter.toLowerCase()) >= 0
//                        || b.getAuthor().getFullName().contains(titleFilter)
                )
                .collect(Collectors.toList());
    }

}
