package operacoes;

import dinheiro.Nota;
import operacoes.saque.MultiploNaoEncontradoException;
import operacoes.saque.SaldoInsuficienteException;
import operacoes.saque.OperacaoSaque;
import saldo.ControleSaldo;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class CaixaEletronico {

    private ControleSaldo controladorSaldo;
    private OperacaoSaque saqueService;
    private int quantidadeSaques;
    private double valorTotalSaques;

    public CaixaEletronico() {
        this.controladorSaldo = new ControleSaldo();
        this.saqueService = new OperacaoSaque(controladorSaldo);
        this.quantidadeSaques = 0;
        this.valorTotalSaques = 0;
    }

    public void adicionaQuantidadeNota(int quantidade, Nota nota) {
        IntStream.rangeClosed(1, quantidade).forEach(qtd -> depositar(nota));
    }

    private void depositar(Nota nota) {
        controladorSaldo.addNota(nota);
    }

    public void sacar(Nota quantia) throws SaldoInsuficienteException, MultiploNaoEncontradoException {
        double valorQuantia = quantia.getValor();

        saqueService.executar(quantia);
        this.valorTotalSaques += valorQuantia;
        this.quantidadeSaques++;
    }

    public Map<Double, List<Nota>> obterNotasAgrupadas() {
        return controladorSaldo.obterNotasAgrupadas();
    }

    public double getSaldo() {
        return controladorSaldo.getSaldo();
    }

    public int getQuantidadeSaques() {
        return quantidadeSaques;
    }

    public double getValorTotalSaques() {
        return valorTotalSaques;
    }
}
