package com.akswi.akswi.controller;

import com.akswi.akswi.dto.StatisticsDTO;
import com.akswi.akswi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow your frontend origin
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public StatisticsDTO getStatistics(@RequestParam("range") String range) {
        return orderService.getStatistics(range);
    }
}
