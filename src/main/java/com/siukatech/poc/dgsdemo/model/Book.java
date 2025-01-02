package com.siukatech.poc.dgsdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Book {
    private String id;
    private String title;
    private Integer releaseYear;
    private Author author;
    private List<Review> reviews;

}