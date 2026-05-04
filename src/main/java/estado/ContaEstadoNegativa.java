package estado;

import conta.ContaBancaria;

public class ContaEstadoNegativa extends ContaEstado {

    public ContaEstadoNegativa(ContaBancaria conta) {
        super(conta);
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        conta.setSaldo(conta.getSaldo() + valor);
        System.out.printf("[NEGATIVA] Depósito de R$ %.2f realizado. Saldo: R$ %.2f%n", valor, conta.getSaldo());
        if (conta.getSaldo() >= 0) {
            System.out.println("[NEGATIVA] Saldo regularizado! Conta reativada automaticamente.");
            conta.setEstado(new ContaEstadoAtiva(conta));
        }
    }

    @Override
    public void sacar(double valor) {
        throw new IllegalStateException("Conta negativa. Saque não permitido até regularização.");
    }

    @Override
    public void bloquear() {
        System.out.println("[NEGATIVA] Conta bloqueada por inadimplência.");
        conta.setEstado(new ContaEstadoBloqueada(conta));
    }

    @Override
    public void encerrar() {
        throw new IllegalStateException("Quite o saldo negativo antes de encerrar a conta.");
    }

    @Override
    public void reativar() {
        throw new IllegalStateException("Realize um depósito para regularizar e reativar a conta.");
    }

    @Override
    public void transferir(ContaBancaria destino, double valor) {
        throw new IllegalStateException("Conta negativa. Transferências não permitidas.");
    }

    @Override
    public String getNomeEstado() {
        return "NEGATIVA";
    }
}

