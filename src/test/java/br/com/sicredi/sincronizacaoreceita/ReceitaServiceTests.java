package br.com.sicredi.sincronizacaoreceita;

import br.com.sicredi.sincronizacaoreceita.models.Conta;
import br.com.sicredi.sincronizacaoreceita.services.ReceitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@ExtendWith(MockitoExtension.class)
class ReceitaServiceTests {

    @InjectMocks
    private ReceitaService receitaService;

    private Conta conta;

    @BeforeEach
    public void setup(){
        this.conta = Conta.builder()
                .agencia("0101")
                .conta("122256")
                .saldo(100.00)
                .status("A")
                .build();
    }

    @DisplayName("Should successfully update a valid account")
    @Test
    void When_ValidAccount_Expect_True() throws InterruptedException {
        var result = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());
        assertNotEquals(false, result);
    }

    @DisplayName("Should fail on update an account with invalid agency number")
    @Test
    void When_InvalidAgency_Expect_False() throws InterruptedException {
        this.conta.setAgencia("");
        var result = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());

        assertNotEquals(true, result);
    }

    @DisplayName("Should fail on update an account with invalid account number")
    @Test
    void When_InvalidAccount_Expect_False() throws InterruptedException {
        this.conta.setConta("");
        var result = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());

        assertNotEquals(true, result);
    }

    @DisplayName("Should fail on update an account with invalid status")
    @Test
    void When_InvalidStatus_Expect_False() throws InterruptedException {
        this.conta.setStatus("");
        var result = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());

        assertNotEquals(true, result);
    }

    @DisplayName("Should pass when timeout not reached")
    @Test
    void When_TimeoutNotReached_Expect_True() {
        assertTimeout(
                ofSeconds(5),
                () -> {
                    receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());
                }
        );
    }

}
