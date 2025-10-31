package com.dimdim.stockgame.controller;

import com.dimdim.stockgame.model.Company;
import com.dimdim.stockgame.model.GameResult;
import com.dimdim.stockgame.model.PlayerWallet;
import com.dimdim.stockgame.model.StockTransaction;
import com.dimdim.stockgame.service.StockGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StockGameRestController {
    
    @Autowired
    private StockGameService stockGameService;
    
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompanies() {
        return ResponseEntity.ok(stockGameService.getAllCompanies());
    }
    
    @GetMapping("/wallet")
    public ResponseEntity<PlayerWallet> getWallet() {
        return ResponseEntity.ok(stockGameService.getPlayerWallet());
    }
    
    @PostMapping("/play")
    public ResponseEntity<GameResult> play(@RequestBody StockTransaction transaction) {
        try {
            GameResult result = stockGameService.playGame(transaction);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/reset")
    public ResponseEntity<PlayerWallet> resetWallet() {
        stockGameService.resetWallet();
        return ResponseEntity.ok(stockGameService.getPlayerWallet());
    }
}
