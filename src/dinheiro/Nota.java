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
}
