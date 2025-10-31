package com.dimdim.stockgame.controller;

import com.dimdim.stockgame.model.Company;
import com.dimdim.stockgame.model.GameResult;
import com.dimdim.stockgame.model.PlayerWallet;
import com.dimdim.stockgame.model.StockTransaction;
import com.dimdim.stockgame.service.StockGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("StockGameRestController - Testes de API REST")
class StockGameRestControllerTest {

    @Mock
    private StockGameService stockGameService;

    @InjectMocks
    private StockGameRestController restController;

    private List<Company> mockCompanies;
    private PlayerWallet mockWallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        mockCompanies = Arrays.asList(
            new Company("acme", "Empresas ACME", "🏭", 100.0, "Descrição ACME"),
            new Company("stark", "Indústrias Stark", "⚡", 250.0, "Descrição Stark")
        );
        
        mockWallet = new PlayerWallet();
    }

    @Test
    @DisplayName("GET /api/companies deve retornar lista de empresas com status 200")
    void testGetCompanies() {
        // Arrange
        when(stockGameService.getAllCompanies()).thenReturn(mockCompanies);

        // Act
        ResponseEntity<List<Company>> response = restController.getCompanies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
            "Status deve ser 200 OK");
        assertNotNull(response.getBody(), "Body não deve ser nulo");
        assertEquals(2, response.getBody().size(), 
            "Deve retornar 2 empresas");
        verify(stockGameService, times(1)).getAllCompanies();
    }

    @Test
    @DisplayName("GET /api/wallet deve retornar carteira com status 200")
    void testGetWallet() {
        // Arrange
        when(stockGameService.getPlayerWallet()).thenReturn(mockWallet);

        // Act
        ResponseEntity<PlayerWallet> response = restController.getWallet();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
            "Status deve ser 200 OK");
        assertNotNull(response.getBody(), "Body não deve ser nulo");
        assertEquals(100, response.getBody().getTotalShares(), 
            "Deve ter 100 ações");
        verify(stockGameService, times(1)).getPlayerWallet();
    }

    @Test
    @DisplayName("POST /api/play com transação válida deve retornar resultado com status 200")
    void testPlaySuccess() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(10);

        GameResult mockResult = new GameResult(
            "Empresas ACME", 10, 105, "HIGH", 5, "Sucesso"
        );

        when(stockGameService.playGame(any(StockTransaction.class)))
            .thenReturn(mockResult);

        // Act
        ResponseEntity<GameResult> response = restController.play(transaction);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
            "Status deve ser 200 OK");
        assertNotNull(response.getBody(), "Body não deve ser nulo");
        assertEquals("Empresas ACME", response.getBody().getCompanyName(), 
            "Nome da empresa deve ser 'Empresas ACME'");
        verify(stockGameService, times(1)).playGame(any(StockTransaction.class));
    }

    @Test
    @DisplayName("POST /api/play com transação inválida deve retornar status 400")
    void testPlayError() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("invalid");
        transaction.setQuantity(10);

        when(stockGameService.playGame(any(StockTransaction.class)))
            .thenThrow(new IllegalArgumentException("Erro"));

        // Act
        ResponseEntity<GameResult> response = restController.play(transaction);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
            "Status deve ser 400 BAD_REQUEST");
        verify(stockGameService, times(1)).playGame(any(StockTransaction.class));
    }

    @Test
    @DisplayName("POST /api/reset deve resetar carteira e retornar nova carteira com status 200")
    void testResetWallet() {
        // Arrange
        PlayerWallet resetWallet = new PlayerWallet();
        when(stockGameService.getPlayerWallet()).thenReturn(resetWallet);

        // Act
        ResponseEntity<PlayerWallet> response = restController.resetWallet();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
            "Status deve ser 200 OK");
        assertNotNull(response.getBody(), "Body não deve ser nulo");
        assertEquals(100, response.getBody().getTotalShares(), 
            "Deve ter 100 ações após reset");
        verify(stockGameService, times(1)).resetWallet();
        verify(stockGameService, times(1)).getPlayerWallet();
    }
}
