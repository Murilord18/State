package estado;

import conta.ContaBancaria;

public class ContaEstadoEncerrada extends ContaEstado {

    public ContaEstadoEncerrada(ContaBancaria conta) {
        super(conta);
    }

    @Override
    public void depositar(double valor) {
        throw new IllegalStateException("Conta encerrada. Operação não permitida.");
    }

    @Override
    public void sacar(double valor) {
        throw new IllegalStateException("Conta encerrada. Operação não permitida.");
    }

    @Override
    public void bloquear() {
        throw new IllegalStateException("Conta encerrada. Não é possível bloquear.");
    }

    @Override
    public void encerrar() {
        throw new IllegalStateException("Conta já está encerrada.");
    }

    @Override
    public void reativar() {
        throw new IllegalStateException("Conta encerrada não pode ser reativada.");
    }

    @Override
    public void transferir(ContaBancaria destino, double valor) {
        throw new IllegalStateException("Conta encerrada. Transferência não permitida.");
    }

    @Override
    public String getNomeEstado() {
        return "ENCERRADA";
    }
}

