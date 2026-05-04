package estado;


import conta.ContaBancaria;

public class ContaEstadoAtiva extends ContaEstado {

    public ContaEstadoAtiva(ContaBancaria conta) {
        super(conta);
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        conta.setSaldo(conta.getSaldo() + valor);
        System.out.printf("[ATIVA] Depósito de R$ %.2f realizado. Saldo: R$ %.2f%n", valor, conta.getSaldo());
    }

    @Override
    public void sacar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        if (valor > conta.getSaldo()) throw new IllegalStateException("Saldo insuficiente.");
        conta.setSaldo(conta.getSaldo() - valor);
        System.out.printf("[ATIVA] Saque de R$ %.2f realizado. Saldo: R$ %.2f%n", valor, conta.getSaldo());
    }

    @Override
    public void bloquear() {
        System.out.println("[ATIVA] Conta bloqueada.");
        conta.setEstado(new ContaEstadoBloqueada(conta));
    }

    @Override
    public void encerrar() {
        System.out.println("[ATIVA] Conta encerrada.");
        conta.setEstado(new ContaEstadoEncerrada(conta));
    }

    @Override
    public void reativar() {
        throw new IllegalStateException("Conta já está ativa.");
    }

    @Override
    public void transferir(ContaBancaria destino, double valor) {
        sacar(valor);
        destino.depositar(valor);
        System.out.printf("[ATIVA] Transferência de R$ %.2f para conta %s realizada.%n",
                valor, destino.getNumeroConta());
    }

    @Override
    public String getNomeEstado() {
        return "ATIVA";
    }
}
