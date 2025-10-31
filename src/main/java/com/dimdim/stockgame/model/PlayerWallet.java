package com.dimdim.stockgame.model;

import lombok.Data;

@Data
public class PlayerWallet {
    private int totalShares;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    
    public PlayerWallet() {
        this.totalShares = 100; // Começa com 100 ações
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
    }
    
    public void addShares(int amount) {
        this.totalShares += amount;
    }
    
    public void removeShares(int amount) {
        this.totalShares -= amount;
    }
    
    public boolean hasEnoughShares(int amount) {
        return this.totalShares >= amount;
    }
    
    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }
    
    public void incrementGamesWon() {
        this.gamesWon++;
    }
    
    public void incrementGamesLost() {
        this.gamesLost++;
    }
}
