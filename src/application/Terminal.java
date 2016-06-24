package application;

import dinheiro.Nota;
import repository.NotaRepository;
import repository.NotaRepositoryMock;
import saldo.CaixaEletronico;
import saldo.SaldoInsuficienteException;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.System.out;

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
                    processaNotasCadastradas();
                    break;
                case 2:
                    try {
                        sacar();
                    } catch(SaldoInsuficienteException e) {
                        out.println(e.getMessage());
                    }
                    break;
                case 3:
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

    private void sacar() throws SaldoInsuficienteException {
        impremeMensagemSaque();
        Nota nota = obtemQuantidadeSolicitadaSaque();
        caixa.sacar(nota);
        imprimeMensagemSaqueResultado();
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
        adicionaQuantidadeNota(quantidade, nota);
    }
    
    private void adicionaQuantidadeNota(int quantidade, Nota nota) {
        Stream.of(quantidade).forEach(qtd -> caixa.depositar(nota));
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
    
    private void imprimeSaldo() {
        out.println("\n---------------------------------------");
        out.println("\nCaixa Eletrônico - Consulta de Saldo");
        out.println("\n---------------------------------------");
        out.println(caixa.getSaldo());
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
