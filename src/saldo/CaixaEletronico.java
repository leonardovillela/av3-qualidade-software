package saldo;

import dinheiro.Nota;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class CaixaEletronico {

    private ControleSaldo controladorSaldo;
    private int quantidadeSaques;
    private double valorTotalSaques;

    public CaixaEletronico() {
        this.controladorSaldo = new ControleSaldo();
        this.quantidadeSaques = 0;
        this.valorTotalSaques = 0;
    }
    
    public String getSaldo() {
        StringBuilder mensagem = new StringBuilder();
        
        return mensagem
            .append("\nO Saldo é: " + controladorSaldo.getSaldo())
            .append("\nQuantidade de saques: " + quantidadeSaques)
            .append("\nValor dos saques: " + valorTotalSaques)
            .append(obtemTotalNotas())
            .toString();
    }
    
    private String obtemTotalNotas() {
        Map<String, List<Nota>> notasAgrupadas = controladorSaldo.obterNotasAgrupadas();
        
        return notasAgrupadas
            .entrySet()
            .stream()
            .map(this::formataTotalNota)
            .collect(joining("\n"));
    }
    
    private String formataTotalNota(Entry<String, List<Nota>> entry) {
        int quantidade = entry.getValue().size();
        return String.format("\nTotal de notas de %s - %d", entry.getKey(), quantidade);
    }

    public void depositar(List<Nota> notas) {
        controladorSaldo.addNotas(notas);
    }
    
    public void depositar(Nota nota) {
        controladorSaldo.addNota(nota);
    }

    public void sacar(Nota quantia) throws SaldoInsuficienteException {
        if (podeSacar(quantia)) {
            List<SimpleEntry<Nota, Integer>> notasAgrupadas = this.obterNotasAgrupadasPorQuantidade(quantia);
            this.removeNotas(notasAgrupadas);
            this.valorTotalSaques = calculaValorTotalSaques(notasAgrupadas);
            this.quantidadeSaques++;
        }
        else
            throw new SaldoInsuficienteException("Não é possivel realizar o saque na conta, saldo insuficiente.");
    }

    private Double calculaValorTotalSaques(List<SimpleEntry<Nota, Integer>> notasAgrupadas) {
        return notasAgrupadas
            .stream()
            .mapToDouble(this::calculaValorNotaAgrupada)
            .sum();
    }

    private double calculaValorNotaAgrupada(SimpleEntry<Nota, Integer> notaAgrupada) {
        Nota nota = notaAgrupada.getKey();
        int quantidade = notaAgrupada.getValue();

        return nota.getValor() * quantidade;
    }

    private boolean podeSacar(Nota quantia) {
        return quantia.getValor() > 0 && controladorSaldo.getSaldo() >= quantia.getValor();
    }

    private void removeNotas(List<SimpleEntry<Nota, Integer>> notasAgrupadasPorQuantidade) {
        controladorSaldo.removeNotas(notasAgrupadasPorQuantidade);
    }

    private List<SimpleEntry<Nota, Integer>> obterNotasAgrupadasPorQuantidade(Nota quantiaOriginal) {
        Map<String, List<Nota>> notasDisponiveis = controladorSaldo.obterNotasAgrupadas();
        return notasDisponiveis
            .entrySet()
            .stream()
            .map(entry -> {
                Double valorNota = Double.parseDouble(entry.getKey());
                List<Nota> notasInseridas = entry.getValue();

                if (quantiaOriginal.getValor() >= valorNota) {
                    int quantidadeNota = (int) Math.floor(quantiaOriginal.getValor() / valorNota);

                    if(notasInseridas.size() < quantidadeNota)
                        quantidadeNota = notasInseridas.size();

                    return new SimpleEntry<Nota, Integer>(new Nota(valorNota), quantidadeNota);
                }

                return new SimpleEntry<Nota, Integer>(new Nota(valorNota), 0);
            })
            .collect(toList());
    }
}
