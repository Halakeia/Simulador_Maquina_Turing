package main;

import java.util.Scanner;
import java.io.*;

public class TuringMachineSimulator {
    public static String leftDelim = "(";
    public static String rightDelim = ")";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String option, programFile, input;

        System.out.println("""
                Simulador de Máquina de Turing ver 1.0
                Desenvolvido como trabalho prático para a disciplina de Teoria da Computação
                Raíssa Faria Ferreira, IFMG, 2025.""");

        while (true) {

            do {
                System.out.print("prompt> ");
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Encerrando o programa...");
                    scanner.close();
                    return;
                } else if (input.startsWith("simturing -head ")) {
                    // Atualiza os delimitadores quando o comando é dado
                    updateHeadDelim(input);
                }
            } while (!verify(input));

            programFile = getProgramFile(input);
            option = getOption(input);

            // 🚨 Garante que -r e -v não podem ser usados juntos
            if (option.contains("-r") && option.contains("-v")) {
                System.err.println("Erro: As opções -r e -v são mutuamente exclusivas.");
                continue;
            }
            try {
                TuringMachine tm = TuringMachine.loadFromFile("src/machines/" + programFile + ".txt");
                String cadeia;
                boolean accepted;

                switch (option) {
                    case "-r":
                        System.out.print("Forneça a palavra: ");
                        cadeia = scanner.nextLine();
                        accepted = tm.computeString(cadeia, false);
                        AcceptedOrNot(accepted);
                        break;

                    case "-v":
                        System.out.print("Forneça a palavra: ");
                        cadeia = scanner.nextLine();
                        System.out.println("Mostrando passo a passo...");
                        accepted = tm.computeString(cadeia, true);
                        AcceptedOrNot(accepted);
                        break;

                    case "-s":
                        System.out.print("Forneça a palavra: ");
                        cadeia = scanner.nextLine();

                        TuringMachine.ID estadoAtual = new TuringMachine.ID(cadeia); // Inicializa o estado

                        while (true) {
                            System.out.print("Forneça a quantidade n de computações: ");
                            int n = scanner.nextInt();
                            scanner.nextLine(); // Consumir a quebra de linha

                            boolean terminou = tm.computeStringGivenNumStates(estadoAtual, n);

                            if (terminou) {
                                AcceptedOrNot(terminou);
                                break;
                            } else {
                                System.out.println("Execução pausada. Deseja:");
                                System.out.println("[1] Executar mais n computações");
                                System.out.println("[2] Executar até o fim");
                                System.out.println("[3] Sair");

                                int escolha = scanner.nextInt();
                                scanner.nextLine(); // Consumir a quebra de linha

                                if (escolha == 2) {
                                    accepted = tm.computeStringGivenNumStates(estadoAtual, Integer.MAX_VALUE); // Executa até o final
                                    AcceptedOrNot(accepted);
                                    break;
                                } else if (escolha == 3) {
                                    System.out.println("Execução interrompida pelo usuário.");
                                    break;
                                }
                            }

                        }
                        break;

                    case "-head":
                        System.out.println("Cabeçote atual: " + TuringMachine.ID.getHead());
                        break;

                    default:
                        System.out.println("Opção não reconhecida.");
                        break;
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar o arquivo da Máquina de Turing: " + e.getMessage());
            }
        }
    }

    public static void AcceptedOrNot(boolean input) {
        if (!input) {
            System.out.println("====> Cadeia recusada");
        } else {
            System.out.println("====> Cadeia aceita");
        }
    }

    public static void updateHeadDelim(String input) {
        // Extrai os delimitadores entre aspas
        int startQuote = input.indexOf("\"");
        int endQuote = input.lastIndexOf("\"");

        if (startQuote != -1 && endQuote != -1 && endQuote > startQuote) {
            String delimiters = input.substring(startQuote + 1, endQuote); // Pega o conteúdo entre aspas
            if (delimiters.length() == 2) {
                leftDelim = String.valueOf(delimiters.charAt(0));  // Atualiza delimitador esquerdo
                rightDelim = String.valueOf(delimiters.charAt(1)); // Atualiza delimitador direito
                System.out.println("Delimitadores de cabeçote alterados para: " + leftDelim + " e " + rightDelim);
            } else {
                System.out.println("Erro: O comando -head deve ter exatamente dois caracteres entre as aspas.");
            }
        }
    }

    public static boolean verify(String input) {
        if (!input.startsWith("simturing")) {
            System.err.println("Erro: O número de argumentos está errado.");
            return false;
        }

        String[] args = input.split("\\s+");

        if (args.length < 2) {
            System.err.println("Erro: O número de argumentos está errado.");
            return false;
        }
        return true;
    }

    public static String getOption(String input) {
        String[] args = input.split("\\s+");

        StringBuilder options = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            options.append(args[i]).append(" ");
        }

        return options.toString().trim();
    }

    public static String getProgramFile(String input) {
        String[] args = input.split("\\s+");
        return args[args.length - 1];
    }
}
