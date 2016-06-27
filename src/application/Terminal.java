package application;

import dinheiro.Nota;
import dinheiro.NotaRepository;
import dinheiro.NotaRepositoryMock;
import operacoes.CaixaEletronico;
import operacoes.saque.MultiploNaoEncontradoException;
import operacoes.saque.SaldoInsuficienteException;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.mapLibraryName;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class Terminal {
    
    private Scanner teclado;
    private Float saldoInicial;
    private CaixaEletronico caixa;
    private NotaRepository notaRepository;

    public Terminal() {
        caixa = new CaixaEletronico();
        teclado = new Scanner(System.in);
        saldoInicial = 0F;
        notaRepository = new NotaRepositoryMock();
    }
    
    public void start() {
        
        while(true) {
            imprimeMenu();
            
            int opcao = teclado.nextInt();
            switch(opcao) {
                case 1:
                    imprimeReposicao();
                    processaNotasCadastradas();
                    break;
                case 2:
                    try {
                        sacar();
                    } catch(SaldoInsuficienteException | MultiploNaoEncontradoException e) {
                        out.println(e.getMessage());
                    }
                    break;
                case 3:
                    this.imprimeCabecalhoSaldo();
                    this.imprimeSaldo();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    private void sacar() throws SaldoInsuficienteException, MultiploNaoEncontradoException {
        impremeMensagemSaque();
        Nota nota = obtemQuantidadeSolicitadaSaque();
        caixa.sacar(nota);
        imprimeMensagemSaqueResultado();
    }


    private void imprimeSaldo() {
        StringBuilder mensagem = new StringBuilder();

         mensagem
            .append("\nO Saldo é: " + caixa.getSaldo())
            .append("\nQuantidade de saques: " + caixa.getQuantidadeSaques())
            .append("\nValor dos saques: " + caixa.getValorTotalSaques() + "\n")
            .append(obtemTotalNotas());

        out.println(mensagem);
    }

    private String obtemTotalNotas() {
        Map<Double, List<Nota>> notasAgrupadas = caixa.obterNotasAgrupadas();

        return notasAgrupadas
            .entrySet()
            .stream()
            .map(this::formataTotalNota)
            .collect(joining("\n"));
    }

    private String formataTotalNota(Entry<Double, List<Nota>> entry) {
        int quantidade = entry.getValue().size();
        return String.format("Total de notas de %s - %d", entry.getKey(), quantidade);
    }

    private Nota obtemQuantidadeSolicitadaSaque() {
        Double quantiaSaque = teclado.nextDouble();
        return new Nota(quantiaSaque);
    }

    private void processaNotasCadastradas() {
        List<Nota> notas = notaRepository.obterTodasAsNotas();
        notas.forEach(this::processaNota);
    }
    
    private void processaNota(Nota nota) {
        imprimeMensagemQuantidade(nota);
        int quantidade = teclado.nextInt();
        caixa.adicionaQuantidadeNota(quantidade, nota);
    }
    
    private void imprimeMensagemQuantidade(Nota nota) {
        out.print("Informa quantidade de notas de " + nota.getValor() + " reais: ");
    }
    
    private void imprimeMenu() {
        out.println("\n---------------------------------------");
        out.println("Caixa Eletrônico - Menu de Opções");
        out.println("---------------------------------------");
        out.println("1- Repor");
        out.println("2- Sacar");
        out.println("3- Consultar saldo");
        out.println("4- Fim");
        out.print("Opção: ");
    }


    private void imprimeCabecalhoSaldo() {
        out.println("\n---------------------------------------");
        out.println("\nCaixa Eletrônico - Consulta de Saldo");
        out.println("\n---------------------------------------");
    }

    private void imprimeReposicao() {
        System.out.println("\n---------------------------------------");
        System.out.println("Caixa Eletrônico - Reposição de notas");
        System.out.println("---------------------------------------");
    }
    
    private void impremeMensagemSaque() {
        out.println("\n---------------------------------------");
        out.println("Caixa Eletrônico - Saque");
        out.println("---------------------------------------");
        out.print("Quantia: ");
    }

    private void imprimeMensagemSaqueResultado() {
        out.println("\n---------------------------------------");
        out.println("\nCaixa Eletrônico - Saque");
        out.println("\n---------------------------------------");
        out.println("\nSaque realiazado com sucesso! ");
    }
}
