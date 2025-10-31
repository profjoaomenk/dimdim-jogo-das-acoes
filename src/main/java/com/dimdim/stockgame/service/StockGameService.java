package com.dimdim.stockgame.service;

import com.dimdim.stockgame.model.Company;
import com.dimdim.stockgame.model.GameResult;
import com.dimdim.stockgame.model.PlayerWallet;
import com.dimdim.stockgame.model.StockTransaction;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Service
@SessionScope
public class StockGameService {
    
    private final List<Company> companies;
    private final Random random;
    private PlayerWallet playerWallet;
    
    public StockGameService() {
        this.random = new Random();
        this.companies = initializeCompanies();
        this.playerWallet = new PlayerWallet(); // Inicia com 100 ações
    }
    
    private List<Company> initializeCompanies() {
        return Arrays.asList(
            new Company("acme", "Empresas ACME", "🏭", 100.00, 
                "Produtos inovadores desde 1949"),
            new Company("stark", "Indústrias Stark", "⚡", 250.00, 
                "Tecnologia de ponta e defesa"),
            new Company("wayne", "Wayne Enterprises", "🦇", 300.00, 
                "Conglomerado de Gotham City"),
            new Company("wonka", "Wonka Industries", "🍫", 150.00, 
                "Doces e chocolate premium"),
            new Company("umbrella", "Umbrella Corporation", "☂️", 180.00, 
                "Farmacêutica e biotecnologia")
        );
    }
    
    public List<Company> getAllCompanies() {
        return new ArrayList<>(companies);
    }
    
    public Optional<Company> getCompanyById(String id) {
        return companies.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();
    }
    
    public PlayerWallet getPlayerWallet() {
        return playerWallet;
    }
    
    public void resetWallet() {
        this.playerWallet = new PlayerWallet();
    }
    
    public GameResult playGame(StockTransaction transaction) {
        Optional<Company> companyOpt = getCompanyById(transaction.getCompanyId());
        
        if (companyOpt.isEmpty()) {
            throw new IllegalArgumentException("Empresa não encontrada");
        }
        
        Company company = companyOpt.get();
        int quantity = transaction.getQuantity();
        
        // Validar quantidade
        if (quantity < 1 || quantity > 100) {
            throw new IllegalArgumentException("Quantidade deve ser entre 1 e 100");
        }
        
        // Verificar se o jogador tem ações suficientes
        if (!playerWallet.hasEnoughShares(quantity)) {
            throw new IllegalArgumentException(
                String.format("Você não tem ações suficientes! Você possui apenas %d ações.", 
                    playerWallet.getTotalShares())
            );
        }
        
        // Simular movimento do mercado (33% cada opção)
        int marketRoll = random.nextInt(3); // 0, 1 ou 2
        String movement;
        int changeAmount;
        String message;
        
        switch (marketRoll) {
            case 0: // Alta
                movement = "HIGH";
                changeAmount = Math.max(1, quantity / 2);
                playerWallet.addShares(changeAmount);
                playerWallet.incrementGamesPlayed();
                playerWallet.incrementGamesWon();
                message = String.format(
                    "🎉 Parabéns! O mercado teve ALTA! " +
                    "Você ganhou %d %s de %s. " +
                    "Total de ações na carteira: %d!",
                    changeAmount, 
                    changeAmount == 1 ? "ação" : "ações",
                    company.getName(), 
                    playerWallet.getTotalShares()
                );
                break;
            case 1: // Baixa
                movement = "LOW";
                changeAmount = Math.max(1, Math.min(quantity / 2, quantity - 1));
                if (quantity == 1) {
                    changeAmount = 1;
                }
                playerWallet.removeShares(changeAmount);
                playerWallet.incrementGamesPlayed();
                playerWallet.incrementGamesLost();
                message = String.format(
                    "📉 Que pena! O mercado teve BAIXA! " +
                    "Você perdeu %d %s de %s. " +
                    "Total de ações na carteira: %d.",
                    changeAmount,
                    changeAmount == 1 ? "ação" : "ações",
                    company.getName(), 
                    playerWallet.getTotalShares()
                );
                break;
            default: // Sem alterações
                movement = "NEUTRAL";
                changeAmount = 0;
                playerWallet.incrementGamesPlayed();
                message = String.format(
                    "📊 Mercado ESTÁVEL! " +
                    "Suas %d %s de %s permaneceram sem alterações. " +
                    "Total de ações na carteira: %d.",
                    quantity,
                    quantity == 1 ? "ação" : "ações",
                    company.getName(),
                    playerWallet.getTotalShares()
                );
                break;
        }
        
        int finalQuantity = playerWallet.getTotalShares();
        
        return new GameResult(
            company.getName(),
            quantity,
            finalQuantity,
            movement,
            changeAmount,
            message
        );
    }
}
