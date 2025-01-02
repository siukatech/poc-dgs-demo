package com.siukatech.poc.dgsdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {
    private String reviewId;
    private String bookId;
    private int starRating;
}
