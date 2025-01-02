package com.siukatech.poc.dgsdemo.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BookContext {
    private Map<String, Author> authorMap;
    private Map<String, List<Review>> reviewListMap;
}
