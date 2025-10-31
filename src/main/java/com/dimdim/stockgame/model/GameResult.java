package com.dimdim.stockgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResult {
    private String companyName;
    private int initialQuantity;
    private int finalQuantity;
    private String marketMovement; // "HIGH", "LOW", "NEUTRAL"
    private int changeAmount;
    private String message;
}
