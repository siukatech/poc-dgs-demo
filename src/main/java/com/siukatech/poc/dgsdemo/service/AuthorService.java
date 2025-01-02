package com.siukatech.poc.dgsdemo.service;

import com.siukatech.poc.dgsdemo.model.Author;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthorService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final Map<String, Author> authorMap = Map.of(
            "A1", new Author("Peter", "Chan")
            , "A2", new Author("Alan", "Tsang")
            , "A3", new Author("Mary", "Lok")
    );

    private final Map<String, String> bookAuthorMap = Map.of(
            "B1", "A1"
            , "B2", "A2"
            , "B3", "A3"
            , "B4", "A1"
            , "B5", "A2"
    );

    public Author getAuthorForBook(String bookId) {
        String authorId = this.bookAuthorMap.get(bookId);
        return this.authorMap.get(authorId);
    }

    public Map<String, Author> getAuthorForBooks(List<String> bookIds) {
        Map<String, Author> bookIdAuthorMap = this.bookAuthorMap.entrySet()
                .stream().filter(entry -> bookIds.contains(entry.getKey()))
                .map(entry -> Map.entry(entry.getKey(), authorMap.get(entry.getValue())))
                .collect(Collectors.toMap(entry2 -> entry2.getKey(), entry2 -> entry2.getValue()))
                ;
        logger.info("forBooks - bookIdAuthorMap: [{}], bookIdAuthorMap.size: [{}]", bookIdAuthorMap, (bookIdAuthorMap==null?"NULL":bookIdAuthorMap.size()));
        return bookIdAuthorMap;
    }

}
