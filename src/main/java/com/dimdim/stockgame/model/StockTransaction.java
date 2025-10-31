package com.dimdim.stockgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockTransaction {
    private String companyId;
    private int quantity;
}
