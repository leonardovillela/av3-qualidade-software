package operacoes;

import dinheiro.Nota;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toList;

public class AgrupadorNotas {

    public static List<SimpleEntry<Nota, Integer>> agruparNotasPorQuantiaSolicitada(Map<Double, List<Nota>> notasDisponiveis, Nota quantia) {
        return notasDisponiveis
            .entrySet()
            .stream()
            .map(entry -> agrupaNotaPorQuantiaSolicitada(entry, quantia))
            .collect(toList());
    }

    private static SimpleEntry<Nota, Integer> agrupaNotaPorQuantiaSolicitada(Entry<Double, List<Nota>> notaQuantidade, Nota quantia) {
        Double valorNota = notaQuantidade.getKey();
        Integer quantidadeNotaDisponiveis = notaQuantidade.getValue().size();

        if (quantia.getValor() >= valorNota) {
            int quantidadeSolicitada = (int) Math.floor(quantia.getValor() / valorNota);

            if (quantidadeNotaDisponiveis <= quantidadeSolicitada)
                return agruparNota(valorNota, quantia, quantidadeNotaDisponiveis);
            else if (quantidadeNotaDisponiveis > quantidadeSolicitada)
                return agruparNota(valorNota, quantia, quantidadeSolicitada);
        }

        return obterNotaQuantidadeZerada(valorNota);
    }

    private static SimpleEntry<Nota, Integer> agruparNota(Double valorNota, Nota quantiaSolicitada, int quantidade) {
        quantiaSolicitada.diminuiValor(valorNota * quantidade);
        return new SimpleEntry<Nota, Integer>(new Nota(valorNota), quantidade);
    }

    private static SimpleEntry<Nota, Integer> obterNotaQuantidadeZerada(Double valorNota) {
        return new SimpleEntry<Nota, Integer>(new Nota(valorNota), 0);
    }
}
