package operacoes.saque;

import dinheiro.Nota;
import saldo.ControleSaldo;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import static operacoes.AgrupadorNotas.*;

public class OperacaoSaque {

    private ControleSaldo controladorSaldo;

    public OperacaoSaque(ControleSaldo controleSaldo) {
        this.controladorSaldo = controleSaldo;
    }

    public void executar(Nota quantia) throws MultiploNaoEncontradoException, SaldoInsuficienteException {
        if (podeSacar(quantia)) {
            List<SimpleEntry<Nota, Integer>> notasAgrupadas =
                    agruparNotasPorQuantiaSolicitada(controladorSaldo.obterNotasAgrupadas(), quantia);
            this.validaSeQuantidadeSolicitadaEhMultiplo(notasAgrupadas);
            this.removeNotas(notasAgrupadas);
        }
        else
            throw new SaldoInsuficienteException("Não é possivel realizar o saque na conta, saldo insuficiente.");
    }

    private void validaSeQuantidadeSolicitadaEhMultiplo(List<SimpleEntry<Nota, Integer>> notasAgrupadas) throws MultiploNaoEncontradoException {
        boolean existeMultiplos = notasAgrupadas
                .stream()
                .anyMatch(entry -> entry.getValue() > 0);

        if (!existeMultiplos)
            throw new MultiploNaoEncontradoException("Quantidade de células não é múltiplo do valor solicitado");
    }


    private boolean podeSacar(Nota quantia) {
        return quantia.getValor() > 0 && controladorSaldo.getSaldo() >= quantia.getValor();
    }

    private void removeNotas(List<SimpleEntry<Nota, Integer>> notasAgrupadasPorQuantidade) {
        controladorSaldo.removeNotas(notasAgrupadasPorQuantidade);
    }
}
