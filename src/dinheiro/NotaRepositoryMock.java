package dinheiro;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class NotaRepositoryMock implements NotaRepository {

    private List<Nota> notas;

    public NotaRepositoryMock() {
        this.notas = Arrays.asList(new Nota(5D), new Nota(10D), new Nota(20D), new Nota(50D), new Nota(100D));
    }

    public List<Nota> obterTodasAsNotas() {
        return Collections.unmodifiableList(notas);
    }
}
