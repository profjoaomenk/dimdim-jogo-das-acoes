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
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("StockGameController - Testes de Controller")
class StockGameControllerTest {

    @Mock
    private StockGameService stockGameService;

    @Mock
    private Model model;

    @InjectMocks
    private StockGameController controller;

    private List<Company> mockCompanies;
    private PlayerWallet mockWallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock de empresas
        mockCompanies = Arrays.asList(
            new Company("acme", "Empresas ACME", "🏭", 100.0, "Descrição ACME"),
            new Company("stark", "Indústrias Stark", "⚡", 250.0, "Descrição Stark")
        );
        
        // Mock de carteira
        mockWallet = new PlayerWallet();
    }

    @Test
    @DisplayName("Deve retornar view index com empresas e carteira")
    void testHome() {
        // Arrange
        when(stockGameService.getAllCompanies()).thenReturn(mockCompanies);
        when(stockGameService.getPlayerWallet()).thenReturn(mockWallet);

        // Act
        String viewName = controller.home(model);

        // Assert
        assertEquals("index", viewName, "Deve retornar view 'index'");
        verify(stockGameService, times(1)).getAllCompanies();
        verify(stockGameService, times(1)).getPlayerWallet();
        verify(model, times(1)).addAttribute(eq("companies"), any());
        verify(model, times(1)).addAttribute(eq("wallet"), any());
    }

    @Test
    @DisplayName("Deve processar jogo com sucesso e retornar resultado")
    void testPlayGameSuccess() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(10);

        GameResult mockResult = new GameResult(
            "Empresas ACME", 10, 105, "HIGH", 5, "Mensagem de sucesso"
        );

        when(stockGameService.playGame(any(StockTransaction.class)))
            .thenReturn(mockResult);
        when(stockGameService.getAllCompanies()).thenReturn(mockCompanies);
        when(stockGameService.getPlayerWallet()).thenReturn(mockWallet);

        // Act
        String viewName = controller.playGame(transaction, model);

        // Assert
        assertEquals("index", viewName, "Deve retornar view 'index'");
        verify(stockGameService, times(1)).playGame(any(StockTransaction.class));
        verify(model, times(1)).addAttribute(eq("result"), any());
        verify(model, times(1)).addAttribute(eq("companies"), any());
        verify(model, times(1)).addAttribute(eq("wallet"), any());
    }

    @Test
    @DisplayName("Deve tratar exceção e mostrar mensagem de erro")
    void testPlayGameError() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("invalid");
        transaction.setQuantity(10);

        when(stockGameService.playGame(any(StockTransaction.class)))
            .thenThrow(new IllegalArgumentException("Empresa não encontrada"));
        when(stockGameService.getAllCompanies()).thenReturn(mockCompanies);
        when(stockGameService.getPlayerWallet()).thenReturn(mockWallet);

        // Act
        String viewName = controller.playGame(transaction, model);

        // Assert
        assertEquals("index", viewName, "Deve retornar view 'index'");
        verify(model, times(1)).addAttribute(eq("error"), eq("Empresa não encontrada"));
        verify(model, times(1)).addAttribute(eq("companies"), any());
        verify(model, times(1)).addAttribute(eq("wallet"), any());
    }

    @Test
    @DisplayName("Deve resetar carteira e redirecionar para home")
    void testResetWallet() {
        // Act
        String redirect = controller.resetWallet(model);

        // Assert
        assertEquals("redirect:/", redirect, "Deve redirecionar para '/'");
        verify(stockGameService, times(1)).resetWallet();
    }
}
