package com.dimdim.stockgame.controller;

import com.dimdim.stockgame.model.Company;
import com.dimdim.stockgame.model.GameResult;
import com.dimdim.stockgame.model.PlayerWallet;
import com.dimdim.stockgame.model.StockTransaction;
import com.dimdim.stockgame.service.StockGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class StockGameController {
    
    @Autowired
    private StockGameService stockGameService;
    
    @GetMapping
    public String home(Model model) {
        List<Company> companies = stockGameService.getAllCompanies();
        PlayerWallet wallet = stockGameService.getPlayerWallet();
        
        model.addAttribute("companies", companies);
        model.addAttribute("wallet", wallet);
        return "index";
    }
    
    @PostMapping("/play")
    public String playGame(@ModelAttribute StockTransaction transaction, Model model) {
        try {
            GameResult result = stockGameService.playGame(transaction);
            List<Company> companies = stockGameService.getAllCompanies();
            PlayerWallet wallet = stockGameService.getPlayerWallet();
            
            model.addAttribute("result", result);
            model.addAttribute("companies", companies);
            model.addAttribute("wallet", wallet);
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            List<Company> companies = stockGameService.getAllCompanies();
            PlayerWallet wallet = stockGameService.getPlayerWallet();
            
            model.addAttribute("companies", companies);
            model.addAttribute("wallet", wallet);
            return "index";
        }
    }
    
    @PostMapping("/reset")
    public String resetWallet(Model model) {
        stockGameService.resetWallet();
        return "redirect:/";
    }
}
