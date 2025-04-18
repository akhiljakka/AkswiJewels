package com.akswi.akswi.controller;

import com.akswi.akswi.entity.Product;
import com.akswi.akswi.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public List<Product> search(@RequestParam("query") String query) {
        return searchService.searchProducts(query);
    }
}
