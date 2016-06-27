package saldo;

import dinheiro.Nota;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.*;

public class ControleSaldo {

    private List<Nota> notas;

    public ControleSaldo() {
        notas = new ArrayList<Nota>();
    }

    public void addNota(Nota nota) {
        notas.add(nota);
    }

    public void removeNotas(List<SimpleEntry<Nota, Integer>> notasAgrupadasPorQuantidade) {
        notasAgrupadasPorQuantidade
            .stream()
            .forEach(entry -> {
                Nota nota = entry.getKey();
                int quantidade = entry.getValue();
                IntStream.rangeClosed(1, quantidade).forEach(it -> this.removeNota(nota));
            });
    }

     public void removeNota(Nota notaParaRemover) {
        notas.remove(notaParaRemover);
    }
     
    public Double getSaldo() {
        return notas
            .stream()
            .mapToDouble(Nota::getValor)
            .sum();
    }
    
    public Map<Double, List<Nota>> obterNotasAgrupadas() {
        return notas
            .stream()
            .sorted((o1, o2) -> o2.getValor().compareTo(o1.getValor()))
            .collect(
                groupingBy(
                    nota -> nota.getValor(),
                    LinkedHashMap::new,
                    mapping((Nota nota) -> nota, toList())
                )
            );
    }
}
