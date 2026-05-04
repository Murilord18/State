package estado;

import conta.ContaBancaria;

public class ContaEstadoPendente extends ContaEstado {

    public ContaEstadoPendente(ContaBancaria conta) {
        super(conta);
    }

    @Override
    public void depositar(double valor) {
        throw new IllegalStateException("Conta pendente. Ative a conta antes de depositar.");
    }

    @Override
    public void sacar(double valor) {
        throw new IllegalStateException("Conta pendente. Ative a conta antes de sacar.");
    }

    @Override
    public void bloquear() {
        throw new IllegalStateException("Conta pendente não pode ser bloqueada.");
    }

    @Override
    public void encerrar() {
        System.out.println("[PENDENTE] Solicitação de abertura rejeitada. Conta encerrada.");
        conta.setEstado(new ContaEstadoEncerrada(conta));
    }

    @Override
    public void reativar() {
        System.out.println("[PENDENTE] Conta validada e ativada com sucesso.");
        conta.setEstado(new ContaEstadoAtiva(conta));
    }

    @Override
    public void transferir(ContaBancaria destino, double valor) {
        throw new IllegalStateException("Conta pendente. Ative a conta antes de transferir.");
    }

    @Override
    public String getNomeEstado() {
        return "PENDENTE";
    }
}

