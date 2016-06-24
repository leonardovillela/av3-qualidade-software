package repository;

import dinheiro.Nota;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotaRepositoryMock implements NotaRepository {
    
    public List<Nota> obterTodasAsNotas() {
        return Collections.unmodifiableList(
            Arrays.asList(
                new Nota(5D),
                new Nota(10D),
                new Nota(20D),
                new Nota(50D),
                new Nota(100D)
             )
        );
    }
}
