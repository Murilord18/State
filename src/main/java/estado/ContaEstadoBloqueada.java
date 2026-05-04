package estado;


import conta.ContaBancaria;

public class ContaEstadoBloqueada extends ContaEstado {

    public ContaEstadoBloqueada(ContaBancaria conta) {
        super(conta);
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        conta.setSaldo(conta.getSaldo() + valor);
        System.out.printf("[BLOQUEADA] Depósito de R$ %.2f aceito mesmo bloqueada. Saldo: R$ %.2f%n",
                valor, conta.getSaldo());
    }

    @Override
    public void sacar(double valor) {
        throw new IllegalStateException("Conta bloqueada. Saque não permitido.");
    }

    @Override
    public void bloquear() {
        throw new IllegalStateException("Conta já está bloqueada.");
    }

    @Override
    public void encerrar() {
        System.out.println("[BLOQUEADA] Conta encerrada a partir do bloqueio.");
        conta.setEstado(new ContaEstadoEncerrada(conta));
    }

    @Override
    public void reativar() {
        System.out.println("[BLOQUEADA] Conta reativada.");
        conta.setEstado(new ContaEstadoAtiva(conta));
    }

    @Override
    public void transferir(ContaBancaria destino, double valor) {
        throw new IllegalStateException("Conta bloqueada. Transferência não permitida.");
    }

    @Override
    public String getNomeEstado() {
        return "BLOQUEADA";
    }
}

