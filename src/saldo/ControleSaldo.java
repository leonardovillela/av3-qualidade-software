package saldo;

import dinheiro.Nota;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class ControleSaldo {

    private List<Nota> notas;

    public ControleSaldo() {
        notas = new ArrayList<Nota>();
    }
    
    public void addNotas(List<Nota> notas) {
        notas.forEach(notas::add);
    }
    
    public void addNota(Nota nota) {
        notas.add(nota);
    }

    public void removeNotas(List<SimpleEntry<Nota, Integer>> notasAgrupadasPorQuantidade) {
        notasAgrupadasPorQuantidade
            .stream()
            .collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue))
            .forEach((nota, quantidade) -> Stream.of(quantidade).forEach(it -> this.removeNota(nota)));
    }

     public void removeNota(Nota notaParaRemover) {
        notas.removeIf(nota -> nota.getValor().equals(notaParaRemover.getValor()));
    }
     
    public Double getSaldo() {
        return notas
            .stream()
            .mapToDouble(Nota::getValor)
            .sum();
    }
    
    public Map<String, List<Nota>> obterNotasAgrupadas() {
        return notas
            .stream()
            .sorted((o1, o2) -> o1.getValor().compareTo(o2.getValor()))
            .collect(
                groupingBy(
                    nota -> nota.getValor().toString(), 
                    mapping((Nota nota) -> nota, toList())
                )
            );
    }
}
