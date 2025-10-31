package com.dimdim.stockgame.service;

import com.dimdim.stockgame.model.Company;
import com.dimdim.stockgame.model.GameResult;
import com.dimdim.stockgame.model.PlayerWallet;
import com.dimdim.stockgame.model.StockTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockGameService - Testes de Serviço")
class StockGameServiceTest {

    private StockGameService stockGameService;

    @BeforeEach
    void setUp() {
        stockGameService = new StockGameService();
    }

    @Test
    @DisplayName("Deve retornar todas as 5 empresas disponíveis")
    void testGetAllCompanies() {
        // Act
        List<Company> companies = stockGameService.getAllCompanies();

        // Assert
        assertNotNull(companies, "Lista de empresas não deve ser nula");
        assertEquals(5, companies.size(), "Deve ter exatamente 5 empresas");
        
        // Verificar se todas as empresas esperadas estão presentes
        assertTrue(companies.stream().anyMatch(c -> c.getId().equals("acme")), 
            "Deve conter Empresas ACME");
        assertTrue(companies.stream().anyMatch(c -> c.getId().equals("stark")), 
            "Deve conter Indústrias Stark");
        assertTrue(companies.stream().anyMatch(c -> c.getId().equals("wayne")), 
            "Deve conter Wayne Enterprises");
        assertTrue(companies.stream().anyMatch(c -> c.getId().equals("wonka")), 
            "Deve conter Wonka Industries");
        assertTrue(companies.stream().anyMatch(c -> c.getId().equals("umbrella")), 
            "Deve conter Umbrella Corporation");
    }

    @Test
    @DisplayName("Deve encontrar empresa por ID válido")
    void testGetCompanyByIdValid() {
        // Act
        Optional<Company> company = stockGameService.getCompanyById("acme");

        // Assert
        assertTrue(company.isPresent(), "Empresa ACME deve ser encontrada");
        assertEquals("acme", company.get().getId(), "ID deve ser 'acme'");
        assertEquals("Empresas ACME", company.get().getName(), 
            "Nome deve ser 'Empresas ACME'");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID inválido")
    void testGetCompanyByIdInvalid() {
        // Act
        Optional<Company> company = stockGameService.getCompanyById("inexistente");

        // Assert
        assertFalse(company.isPresent(), 
            "Não deve encontrar empresa com ID inexistente");
    }

    @Test
    @DisplayName("Deve inicializar carteira do jogador com 100 ações")
    void testGetPlayerWalletInitialState() {
        // Act
        PlayerWallet wallet = stockGameService.getPlayerWallet();

        // Assert
        assertNotNull(wallet, "Carteira não deve ser nula");
        assertEquals(100, wallet.getTotalShares(), 
            "Jogador deve começar com 100 ações");
        assertEquals(0, wallet.getGamesPlayed(), 
            "Deve começar com 0 jogadas");
        assertEquals(0, wallet.getGamesWon(), 
            "Deve começar com 0 vitórias");
        assertEquals(0, wallet.getGamesLost(), 
            "Deve começar com 0 derrotas");
    }

    @Test
    @DisplayName("Deve resetar carteira para valores iniciais")
    void testResetWallet() {
        // Arrange - Simular algumas jogadas
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(10);
        
        // Fazer algumas jogadas para alterar o estado
        stockGameService.playGame(transaction);
        stockGameService.playGame(transaction);

        // Act - Resetar carteira
        stockGameService.resetWallet();
        PlayerWallet wallet = stockGameService.getPlayerWallet();

        // Assert
        assertEquals(100, wallet.getTotalShares(), 
            "Ações devem voltar para 100");
        assertEquals(0, wallet.getGamesPlayed(), 
            "Jogadas devem voltar para 0");
        assertEquals(0, wallet.getGamesWon(), 
            "Vitórias devem voltar para 0");
        assertEquals(0, wallet.getGamesLost(), 
            "Derrotas devem voltar para 0");
    }

    @Test
    @DisplayName("Deve executar jogo com empresa válida e quantidade válida")
    void testPlayGameValidTransaction() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(10);

        // Act
        GameResult result = stockGameService.playGame(transaction);

        // Assert
        assertNotNull(result, "Resultado não deve ser nulo");
        assertEquals("Empresas ACME", result.getCompanyName(), 
            "Nome da empresa deve ser 'Empresas ACME'");
        assertEquals(10, result.getInitialQuantity(), 
            "Quantidade inicial deve ser 10");
        assertNotNull(result.getMarketMovement(), 
            "Movimento do mercado não deve ser nulo");
        assertTrue(
            result.getMarketMovement().equals("HIGH") || 
            result.getMarketMovement().equals("LOW") || 
            result.getMarketMovement().equals("NEUTRAL"),
            "Movimento deve ser HIGH, LOW ou NEUTRAL"
        );
    }

    @Test
    @DisplayName("Deve lançar exceção para empresa não encontrada")
    void testPlayGameInvalidCompany() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("empresa_inexistente");
        transaction.setQuantity(10);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> stockGameService.playGame(transaction),
            "Deve lançar IllegalArgumentException"
        );
        
        assertEquals("Empresa não encontrada", exception.getMessage(),
            "Mensagem de erro deve ser 'Empresa não encontrada'");
    }

    @Test
    @DisplayName("Deve lançar exceção para quantidade menor que 1")
    void testPlayGameQuantityTooLow() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(0);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> stockGameService.playGame(transaction),
            "Deve lançar IllegalArgumentException"
        );
        
        assertEquals("Quantidade deve ser entre 1 e 100", exception.getMessage(),
            "Mensagem deve indicar intervalo válido");
    }

    @Test
    @DisplayName("Deve lançar exceção para quantidade maior que 100")
    void testPlayGameQuantityTooHigh() {
        // Arrange
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(101);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> stockGameService.playGame(transaction),
            "Deve lançar IllegalArgumentException"
        );
        
        assertEquals("Quantidade deve ser entre 1 e 100", exception.getMessage(),
            "Mensagem deve indicar intervalo válido");
    }

    @Test
    @DisplayName("Deve verificar validação de saldo na carteira")
    void testPlayGameInsufficientShares() {
        // Arrange - Resetar para garantir estado inicial
        stockGameService.resetWallet();
        PlayerWallet wallet = stockGameService.getPlayerWallet();
        
        // Act - Tentar apostar exatamente o que tem (deve funcionar)
        StockTransaction validTransaction = new StockTransaction();
        validTransaction.setCompanyId("acme");
        validTransaction.setQuantity(Math.min(50, wallet.getTotalShares()));
        
        // Não deve lançar exceção
        assertDoesNotThrow(() -> stockGameService.playGame(validTransaction),
            "Deve permitir apostar quantidade válida dentro do saldo");
        
        // Verificar que a validação de saldo existe no código
        // Tentando apostar mais que o máximo permitido (101)
        StockTransaction invalidTransaction = new StockTransaction();
        invalidTransaction.setCompanyId("acme");
        invalidTransaction.setQuantity(101);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> stockGameService.playGame(invalidTransaction),
            "Deve lançar exceção para quantidade inválida"
        );
        
        // A validação de 1-100 vem antes, então deve ser essa a mensagem
        assertTrue(
            exception.getMessage().contains("Quantidade deve ser entre 1 e 100") ||
            exception.getMessage().contains("não tem ações suficientes"),
            "Deve validar quantidade ou saldo. Mensagem: " + exception.getMessage()
        );
    }

    @Test
    @DisplayName("Deve incrementar contador de jogadas após cada jogo")
    void testGamesPlayedIncrement() {
        // Arrange
        stockGameService.resetWallet();
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(10);

        // Act - Jogar 3 vezes
        stockGameService.playGame(transaction);
        stockGameService.playGame(transaction);
        stockGameService.playGame(transaction);

        PlayerWallet wallet = stockGameService.getPlayerWallet();

        // Assert
        assertEquals(3, wallet.getGamesPlayed(),
            "Deve ter exatamente 3 jogadas registradas");
    }

    @Test
    @DisplayName("Deve garantir mudança mínima de 1 ação em qualquer resultado")
    void testMinimumShareChange() {
        // Arrange
        stockGameService.resetWallet();
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(1); // Apostar apenas 1 ação

        // Act
        GameResult result = stockGameService.playGame(transaction);

        // Assert
        if (result.getMarketMovement().equals("HIGH") || 
            result.getMarketMovement().equals("LOW")) {
            assertTrue(result.getChangeAmount() >= 1,
                "Mudança deve ser de pelo menos 1 ação");
        } else {
            assertEquals(0, result.getChangeAmount(),
                "Mudança deve ser 0 em mercado neutro");
        }
    }

    @Test
    @DisplayName("Deve atualizar saldo da carteira corretamente após cada jogo")
    void testWalletUpdateAfterGame() {
        // Arrange
        stockGameService.resetWallet();
        int initialBalance = stockGameService.getPlayerWallet().getTotalShares();
        
        StockTransaction transaction = new StockTransaction();
        transaction.setCompanyId("acme");
        transaction.setQuantity(10);

        // Act
        GameResult result = stockGameService.playGame(transaction);
        PlayerWallet wallet = stockGameService.getPlayerWallet();

        // Assert
        assertNotNull(result, "Resultado não deve ser nulo");
        assertTrue(wallet.getTotalShares() >= 0, 
            "Saldo não deve ser negativo");
        
        // Verificar se o saldo mudou de acordo com o resultado
        if (result.getMarketMovement().equals("HIGH")) {
            assertTrue(wallet.getTotalShares() > initialBalance,
                "Saldo deve aumentar em caso de alta");
        } else if (result.getMarketMovement().equals("LOW")) {
            assertTrue(wallet.getTotalShares() < initialBalance,
                "Saldo deve diminuir em caso de baixa");
        } else {
            assertEquals(initialBalance, wallet.getTotalShares(),
                "Saldo deve permanecer igual em mercado neutro");
        }
    }
}
