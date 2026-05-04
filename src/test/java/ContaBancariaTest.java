import conta.ContaBancaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Testes do Padrão State - Sistema Bancário")
class ContaBancariaTest {

    private ContaBancaria conta;
    private ContaBancaria contaDestino;

    @BeforeEach
    void setUp() {
        conta = new ContaBancaria("001-1", "João Silva");
        contaDestino = new ContaBancaria("002-2", "Maria Souza");
        contaDestino.reativar(); // ativa a conta destino
    }

    // TESTES: Estado PENDENTE

    @Test
    @DisplayName("Conta criada deve iniciar no estado PENDENTE")
    void deveCriarContaComEstadoPendente() {
        assertEquals("PENDENTE", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta pendente não pode depositar")
    void contaPendenteNaoDeveDepositar() {
        assertThrows(IllegalStateException.class, () -> conta.depositar(100));
    }

    @Test
    @DisplayName("Conta pendente não pode sacar")
    void contaPendenteNaoDeveSacar() {
        assertThrows(IllegalStateException.class, () -> conta.sacar(50));
    }

    @Test
    @DisplayName("Conta pendente transita para ATIVA ao reativar")
    void contaPendenteDeveAtivarAoReativar() {
        conta.reativar();
        assertEquals("ATIVA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta pendente transita para ENCERRADA ao encerrar")
    void contaPendenteDeveEncerrarAoEncerrar() {
        conta.encerrar();
        assertEquals("ENCERRADA", conta.getNomeEstado());
    }

    // TESTES: Estado ATIVA


    @Test
    @DisplayName("Conta ativa deve aceitar depósito e atualizar saldo")
    void contaAtivaDeveAceitarDeposito() {
        conta.reativar();
        conta.depositar(500.00);
        assertEquals(500.00, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Conta ativa deve aceitar saque e descontar saldo")
    void contaAtivaDeveAceitarSaque() {
        conta.reativar();
        conta.depositar(300.00);
        conta.sacar(100.00);
        assertEquals(200.00, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Saque maior que saldo deve lançar exceção")
    void saqueMaiorQueSaldoDeveLancarExcecao() {
        conta.reativar();
        conta.depositar(100.00);
        assertThrows(IllegalStateException.class, () -> conta.sacar(200.00));
    }

    @Test
    @DisplayName("Conta ativa deve transitar para BLOQUEADA ao bloquear")
    void contaAtivaDeveBloqueioTransitarParaBloqueada() {
        conta.reativar();
        conta.bloquear();
        assertEquals("BLOQUEADA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta ativa deve transitar para ENCERRADA ao encerrar")
    void contaAtivaDeveTransitarParaEncerrada() {
        conta.reativar();
        conta.encerrar();
        assertEquals("ENCERRADA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta ativa não pode ser reativada novamente")
    void contaAtivaJaAtivaDeveLancarExcecao() {
        conta.reativar();
        assertThrows(IllegalStateException.class, conta::reativar);
    }

    @Test
    @DisplayName("Transferência entre contas ativas deve funcionar corretamente")
    void transferenciaDeveDebitarOrigemECreditarDestino() {
        conta.reativar();
        conta.depositar(1000.00);
        conta.transferir(contaDestino, 400.00);

        assertEquals(600.00, conta.getSaldo(), 0.001);
        assertEquals(400.00, contaDestino.getSaldo(), 0.001);
    }

    // TESTES: Estado BLOQUEADA

    @Test
    @DisplayName("Conta bloqueada deve aceitar depósito")
    void contaBloqueadaDeveAceitarDeposito() {
        conta.reativar();
        conta.depositar(200.00);
        conta.bloquear();
        conta.depositar(50.00);
        assertEquals(250.00, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Conta bloqueada não deve aceitar saque")
    void contaBloqueadaNaoDeveAceitarSaque() {
        conta.reativar();
        conta.depositar(200.00);
        conta.bloquear();
        assertThrows(IllegalStateException.class, () -> conta.sacar(50.00));
    }

    @Test
    @DisplayName("Conta bloqueada deve reativar ao chamar reativar()")
    void contaBloqueadaDeveReativarCorretamente() {
        conta.reativar();
        conta.bloquear();
        conta.reativar();
        assertEquals("ATIVA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta bloqueada deve transitar para ENCERRADA")
    void contaBloqueadaDeveEncerrar() {
        conta.reativar();
        conta.bloquear();
        conta.encerrar();
        assertEquals("ENCERRADA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta bloqueada não permite transferência")
    void contaBloqueadaNaoDeveTransferir() {
        conta.reativar();
        conta.depositar(500.00);
        conta.bloquear();
        assertThrows(IllegalStateException.class,
                () -> conta.transferir(contaDestino, 100.00));
    }

    // TESTES: Estado ENCERRADA

    @Test
    @DisplayName("Conta encerrada não deve aceitar depósito")
    void contaEncerradaNaoDeveAceitarDeposito() {
        conta.reativar();
        conta.encerrar();
        assertThrows(IllegalStateException.class, () -> conta.depositar(100.00));
    }

    @Test
    @DisplayName("Conta encerrada não deve aceitar saque")
    void contaEncerradaNaoDeveAceitarSaque() {
        conta.reativar();
        conta.encerrar();
        assertThrows(IllegalStateException.class, () -> conta.sacar(50.00));
    }

    @Test
    @DisplayName("Conta encerrada não pode ser reativada")
    void contaEncerradaNaoPodeSerReativada() {
        conta.reativar();
        conta.encerrar();
        assertThrows(IllegalStateException.class, conta::reativar);
    }

    @Test
    @DisplayName("Conta encerrada não pode ser encerrada novamente")
    void contaEncerradaNaoPodeSerEncerradaNovamente() {
        conta.reativar();
        conta.encerrar();
        assertThrows(IllegalStateException.class, conta::encerrar);
    }


    // TESTES: Estado NEGATIVA


    @Test
    @DisplayName("Saque com cheque especial deve transitar para estado NEGATIVA")
    void saqueComChequeEspecialDeveTransitarParaNegativa() {
        conta.reativar();
        conta.depositar(100.00);
        conta.sacarComChequeEspecial(300.00);
        assertEquals("NEGATIVA", conta.getNomeEstado());
        assertEquals(-200.00, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Depósito na conta negativa deve regularizar e torná-la ATIVA")
    void depositoNaContaNegativaDeveReativar() {
        conta.reativar();
        conta.depositar(100.00);
        conta.sacarComChequeEspecial(300.00);
        conta.depositar(200.00);
        assertEquals("ATIVA", conta.getNomeEstado());
        assertEquals(0.00, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Conta negativa não deve permitir saque")
    void contaNegativaNaoDevePermitirSaque() {
        conta.reativar();
        conta.sacarComChequeEspecial(50.00);
        assertThrows(IllegalStateException.class, () -> conta.sacar(10.00));
    }

    @Test
    @DisplayName("Conta negativa pode ser bloqueada")
    void contaNegativaPodeSerBloqueada() {
        conta.reativar();
        conta.sacarComChequeEspecial(50.00);
        conta.bloquear();
        assertEquals("BLOQUEADA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Conta negativa não pode ser encerrada sem quitar")
    void contaNegativaNaoPodeSerEncerrada() {
        conta.reativar();
        conta.sacarComChequeEspecial(50.00);
        assertThrows(IllegalStateException.class, conta::encerrar);
    }


    // TESTES: Fluxos completos


    @Test
    @DisplayName("Fluxo completo: Pendente → Ativa → Bloqueada → Ativa → Encerrada")
    void fluxoCompletoDeveTransicionarCorretamente() {
        assertEquals("PENDENTE", conta.getNomeEstado());
        conta.reativar();
        assertEquals("ATIVA", conta.getNomeEstado());
        conta.bloquear();
        assertEquals("BLOQUEADA", conta.getNomeEstado());
        conta.reativar();
        assertEquals("ATIVA", conta.getNomeEstado());
        conta.encerrar();
        assertEquals("ENCERRADA", conta.getNomeEstado());
    }

    @Test
    @DisplayName("Saldo deve permanecer consistente após múltiplas operações")
    void saldoDeveSerConsistenteAposMultiplasOperacoes() {
        conta.reativar();
        conta.depositar(1000.00);
        conta.sacar(200.00);
        conta.depositar(500.00);
        conta.sacar(150.00);
        assertEquals(1150.00, conta.getSaldo(), 0.001);
    }
}

