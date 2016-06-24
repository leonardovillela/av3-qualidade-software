package repository;

import dinheiro.Nota;

import java.util.List;

public interface NotaRepository {
    List<Nota> obterTodasAsNotas();
}
