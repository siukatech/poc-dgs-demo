package com.siukatech.poc.dgsdemo.service;

import com.siukatech.poc.dgsdemo.model.Review;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final List<Review> reviews = List.of(
            new Review("R1", "B1", 1)
            , new Review("R2", "B2", 2)
            , new Review("R3", "B3", 3)
            , new Review("R4", "B4", 4)
            , new Review("R5", "B5", 5)
            , new Review("R6", "B1", 5)
            , new Review("R7", "B2", 4)
            , new Review("R8", "B3", 3)
    );

    public List<Review> getReviewsForBook(String bookId) {
        return this.reviews.stream().filter(review -> bookId.equals(review.getBookId())).collect(Collectors.toList());
    }

    public Map<String, List<Review>> getReviewsForBooks(List<String> bookIds) {
        return this.reviews.stream().filter(review -> bookIds.contains(review.getBookId()))
                .collect(Collectors.groupingBy(review -> review.getBookId()))
                ;
    }

}
