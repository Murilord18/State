package conta;

import estado.ContaEstado;
import estado.ContaEstado.*;
import estado.ContaEstadoNegativa;
import estado.ContaEstadoPendente;

public class ContaBancaria {

    private final String numeroConta;
    private final String titular;
    private double saldo;
    private ContaEstado estado;


    public ContaBancaria(String numeroConta, String titular) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = 0.0;
        this.estado = new ContaEstadoPendente(this);
    }

    // === Operações delegadas ao estado ===

    public void depositar(double valor) {
        estado.depositar(valor);
    }

    public void sacar(double valor) {
        estado.sacar(valor);
    }

    public void bloquear() {
        estado.bloquear();
    }

    public void encerrar() {
        estado.encerrar();
    }

    public void reativar() {
        estado.reativar();
    }

    public void transferir(ContaBancaria destino, double valor) {
        estado.transferir(destino, valor);
    }

    // === Método especial: saque que pode tornar saldo negativo ===
    public void sacarComChequeEspecial(double valor) {
        if (!estado.getNomeEstado().equals("ATIVA")) {
            throw new IllegalStateException("Cheque especial disponível apenas para contas ativas.");
        }
        if (valor <= 0) throw new IllegalArgumentException("Valor deve ser positivo.");
        saldo -= valor;
        System.out.printf("[ATIVA] Saque com cheque especial: R$ %.2f. Saldo: R$ %.2f%n", valor, saldo);
        if (saldo < 0) {
            System.out.println("[ATIVA] Saldo negativo detectado. Conta movida para estado NEGATIVA.");
            estado = new ContaEstadoNegativa(this);
        }
    }

    // === Getters e Setters ===

    public void setEstado(ContaEstado estado) {
        this.estado = estado;
    }

    public ContaEstado getEstado() {
        return estado;
    }

    public String getNomeEstado() {
        return estado.getNomeEstado();
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    @Override
    public String toString() {
        return String.format("ContaBancaria{numero='%s', titular='%s', saldo=R$ %.2f, estado='%s'}",
                numeroConta, titular, saldo, estado.getNomeEstado());
    }
}

