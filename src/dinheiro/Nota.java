package dinheiro;

public class Nota {
    private Double valor;

    public Nota(Double valor) {
        this.valor = valor;
    }

    public Nota(String valor) {
        this.valor = new Double(valor);
    }

    public Double getValor() {
        return this.valor;
    }

    public void diminuiValor(Double valor) {
        this.valor -= valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nota nota = (Nota) o;

        return valor.equals(nota.valor);

    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }
}
