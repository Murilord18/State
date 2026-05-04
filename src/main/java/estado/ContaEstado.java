package estado;

public abstract class ContaEstado {

    protected conta.ContaBancaria conta;

    public ContaEstado(conta.ContaBancaria conta) {
        this.conta = conta;
    }

    public abstract void depositar(double valor);
    public abstract void sacar(double valor);
    public abstract void bloquear();
    public abstract void encerrar();
    public abstract void reativar();
    public abstract void transferir(conta.ContaBancaria destino, double valor);

    public abstract String getNomeEstado();
}
