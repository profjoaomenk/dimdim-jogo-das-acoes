package com.dimdim.stockgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String id;
    private String name;
    private String icon;
    private double basePrice;
    private String description;
}
