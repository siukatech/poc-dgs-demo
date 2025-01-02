package com.siukatech.poc.dgsdemo.datafetcher;

import com.netflix.graphql.dgs.*;
import com.siukatech.poc.dgsdemo.model.Author;
import com.siukatech.poc.dgsdemo.model.Book;
import com.siukatech.poc.dgsdemo.model.BookContext;
import com.siukatech.poc.dgsdemo.model.Review;
import com.siukatech.poc.dgsdemo.service.AuthorService;
import com.siukatech.poc.dgsdemo.service.BookService;
import com.siukatech.poc.dgsdemo.service.ReviewService;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
public class BooksDataFetcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final BookService bookService;
    private final AuthorService authorService;
    private final ReviewService reviewService;

    public BooksDataFetcher(BookService bookService, AuthorService authorService, ReviewService reviewService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.reviewService = reviewService;
    }

    @DgsQuery
    public DataFetcherResult<List<Book>> books(DataFetchingEnvironment dfe, @InputArgument String titleFilter) {
        List<Book> books = this.bookService.getBooks(titleFilter);
        boolean hasAuthorEle = dfe.getSelectionSet().contains("author");
        boolean hasReviewsEle = dfe.getSelectionSet().contains("reviews");
        Map<String, Author> bookIdAuthorMap = null;
        Map<String, List<Review>> bookIdReviewsMap = null;
        BookContext bookContext = new BookContext();
        logger.info("books - dfe.getSelectionSet: [{}]", dfe.getSelectionSet());
        logger.info("books - titleFilter: [{}], books: [{}], hasAuthorEle: [{}], hasReviewsEle: [{}]"
                , titleFilter, books.size()
                , String.valueOf(hasAuthorEle)
                , String.valueOf(hasReviewsEle)
        );
        List<String> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
        if (hasAuthorEle) {
            bookIdAuthorMap = this.authorService.getAuthorForBooks(bookIds);
            bookContext.setAuthorMap(bookIdAuthorMap);
//            return DataFetcherResult.<List<Book>>newResult()
//                    .data(books)
//                    .localContext(bookIdAuthorMap)
//                    .build();
        }
//        else {
//            return DataFetcherResult.<List<Book>>newResult()
//                    .data(books)
//                    .build();
//        }
        if (hasReviewsEle) {
            bookIdReviewsMap = this.reviewService.getReviewsForBooks(bookIds);
            bookContext.setReviewListMap(bookIdReviewsMap);
        }
        return DataFetcherResult.<List<Book>>newResult().data(books).localContext(bookContext).build();
    }

    @DgsData(parentType = "Book", field = "author")
    public Author author(DgsDataFetchingEnvironment dfe) {
        Book book = dfe.getSource();
        Author author = null;

        //Map<String, Author> bookIdAuthorMap = dfe.getLocalContext();
        BookContext bookContext = dfe.getLocalContext();
        Map<String, Author> bookIdAuthorMap = bookContext.getAuthorMap();
        logger.info("author - bookIdAuthorMap: [{}], bookIdAuthorMap.size: [{}]", bookIdAuthorMap, (bookIdAuthorMap==null?"NULL":bookIdAuthorMap.size()));
        if (bookIdAuthorMap != null) {
            author = bookIdAuthorMap.get(book.getId());
        }
        else {
            author = authorService.getAuthorForBook(book.getId());
        }
        return author;
    }

    @DgsData(parentType = "Book", field = "reviews")
    public List<Review> reviews(DgsDataFetchingEnvironment dfe) {
        Book book = dfe.getSource();
        List<Review> reviews = null;

        BookContext bookContext = dfe.getLocalContext();
        Map<String, List<Review>> bookIdReviewListMap = bookContext.getReviewListMap();
        logger.info("review - bookIdReviewListMap: [{}], bookIdReviewListMap.size: [{}]", bookIdReviewListMap, (bookIdReviewListMap==null?"NULL":bookIdReviewListMap.size()));
        if (bookIdReviewListMap != null) {
            reviews = bookIdReviewListMap.get(book.getId());
        }
        else {
            reviews = reviewService.getReviewsForBook(book.getId());
        }
        return reviews;
    }

}



