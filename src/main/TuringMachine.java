package main;

import java.util.*;
import java.io.*;

import static main.TuringMachineSimulator.*;

public class TuringMachine {
    private List<Map<Character, Instruction>> transition;
    public static char blank;
    public static char spacer;
    private Map<String, Integer> blocks;


    /**
     * Construtor
     *
     * @param k           numero de estados
     * @param transition  transicoes
     * @param blank       caractere branco
     * @param spacer      caractere delim

     */
    public TuringMachine(int k, Set<Instruction> transition, char blank, char spacer, Map<String, Integer> blocks) {
        this.blank = blank;
        this.spacer = spacer;
        this.transition = new ArrayList<>(k);
        this.blocks = blocks;

        for (int i = 0; i < k; i++) {
            this.transition.add(new HashMap<>());
        }
        for (Instruction instr : transition) {
            checkInstruction(instr);
            this.transition.get(instr.p).put(instr.a, instr);
        }
    }

    /**
     * salva o bloco e o estado inicial
     * @param input linha lida do arquivo
     * @param blocks variavel para salvar
     */
    private static void addBlock(String input, Map<String, Integer> blocks) {
        // A entrada deve ser no formato "bloco <nome> <primeiro estado>"
        String[] parts = input.split("\\s+");

        if (parts.length != 3) {
            System.out.println("Erro: A entrada não está no formato correto.");
            return;
        }

        // O nome do bloco
        String nomeBloco = parts[1];

        // O primeiro estado (convertido para inteiro)
        int primeiroEstado;
        try {
            primeiroEstado = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Erro: O primeiro estado deve ser um número inteiro.");
            return;
        }
        blocks.put(nomeBloco, primeiroEstado);
    }

    /**
     * carrega a maquina por um arquivo
     * @param filename nome do arquivo
     * @return retorna construtor
     * @throws IOException
     */
    public static TuringMachine loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        Set<Instruction> transitions = new HashSet<>();
        char blank = '-';
        char spacer = '|';
        int numStates = 0;
        Map<String, Integer> blocks = new HashMap<>();  // Mapa para armazenar os blocos

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith(";")) continue; // Ignorar comentários

            if (line.startsWith("bloco")) {
                // Se a linha começar com "bloco", chama o método addBlock
                addBlock(line, blocks);
                continue;
            }

            if (!line.isBlank()) {
                // Processa transições
                String[] parts = line.split(",");
                if (parts.length != 5) continue;

                int p = Integer.parseInt(parts[0].trim());
                char a = parts[1].trim().charAt(0);
                char b = parts[2].trim().charAt(0);
                Instruction.Direction m = getDirection(parts);

                int q;
                if (parts[4].trim().equalsIgnoreCase("retorne")) {
                    q = -1; // Indica que esse é um estado de retorno (fim do bloco)
                } else {
                    q = Integer.parseInt(parts[4].trim());
                }

                transitions.add(new Instruction(p, a, b, m, q));
                numStates = Math.max(numStates, p + 1);
                if (q != -1) numStates = Math.max(numStates, q + 1);
            }
        }
        reader.close();
        return new TuringMachine(numStates, transitions, blank, spacer, blocks);
    }

    /**
     * método auxiliar para retornar direcao
     * @param parts string que vai ser lida
     * @return a direcao a ser armazenada já formatada
     */
    private static Instruction.Direction getDirection(String[] parts) {
        Instruction.Direction m;
        String direction = parts[3].trim().toUpperCase();

        m = switch (direction) {
            case "L" -> Instruction.Direction.L;
            case "R" -> Instruction.Direction.R;
            case "N", "*" -> Instruction.Direction.N;
            default -> throw new IllegalArgumentException("Direção inválida: " + direction);
        };
        return m;
    }


    /**
     * Processa a string de entrada. Exibe cada descrição instantânea no formato 'uspsav', onde 'u' e 'v' são
     * strings no alfabeto da fita, com 'uav' representando o conteúdo da fita, 'a' é o símbolo atual sob a
     * cabeça de leitura/gravação, 'p' é o estado atual e 's' é o caractere separador.
     *
     * @param w a string de entrada
     * @return o ID (em formato de String) computado por esta Máquina de Turing para a string de entrada
     */

    public boolean computeString(String w, boolean shouldPrint) {
        ID id = new ID(w);
        if (shouldPrint) {
            printFormattedOutput(id); // Primeira saída antes do loop
        }

        int stepCount = 0;
        Instruction nextInstr = getInstruction(id);

        while (nextInstr != null && stepCount < 500) {
            // Se o estado de destino for -1, significa que devemos parar
            if (nextInstr.q == -1) {
                return true; // Estado final atingido, aceito
            }

            id.applyInstruction(nextInstr);
            if (shouldPrint) {
                printFormattedOutput(id); // Exibe o estado a cada passo
            }

            nextInstr = getInstruction(id);
            stepCount++;

            if (stepCount == 500) {
                System.out.println("Atingido o limite de 500 computações. Deseja continuar?");
                System.out.println("[1] Continuar ");
                System.out.println("[2] Sair");

                Scanner scanner = new Scanner(System.in);
                int escolha = scanner.nextInt();
                scanner.nextLine();

                if (escolha == 1) {
                    stepCount = 0;
                } else if (escolha == 2) {
                    return false;
                }
            }
        }

      //não encontrou estado final
        return false;
    }

//faz a mesma coisa que a anterior mas com implementacao de seek
    public boolean computeStringGivenNumStates(ID id, int numStates) {
        if (id == null) {
            System.out.println("Erro: O estado inicial da fita não pode ser nulo.");
            return false;
        }
        int stepCount = 0;
        printFormattedOutput(id); // Primeira saída antes do loop

        Instruction nextInstr = getInstruction(id);
        int k = 1;
        while (nextInstr != null && stepCount < 500 && k < numStates) {
            if (nextInstr.q == -1) {
                return true;
            }
            id.applyInstruction(nextInstr);
            printFormattedOutput(id); // Exibe o estado a cada passo
            nextInstr = getInstruction(id);
            k++;
            stepCount++;
            if (stepCount == 500) {
                System.out.println("Atingido o limite de 500 computações.");
                return false;
            }
        }
        return false; //não encontrou estado final
    }

//formata e imprime a configuração instantanea
    private void printFormattedOutput(ID id) {
        int estado = id.state;
        String bloco = "main"; // Bloco padrão

        // Verifica se o estado atual é um estado inicial de algum bloco
        for (Map.Entry<String, Integer> entry : blocks.entrySet()) {
            if (entry.getValue() == estado) {
                bloco = entry.getKey(); // Define o bloco correspondente ao estado
                break; // Sai do loop ao encontrar a primeira correspondência
            }
        }

        // Obter a fita completa
        String tape = id.contents.toString();

        // Definir os limites da fita à esquerda e direita
        int pos = id.position;
        int startLeft = Math.max(0, pos - 20);
        int endRight = Math.min(tape.length(), pos + 21);

        // Obter partes da fita
        String leftTape = tape.substring(startLeft, pos);
        String rightTape = tape.substring(pos + 1, endRight);

        // Garantir que a saída tenha 20 caracteres à esquerda e à direita
        leftTape = String.format("%20s", leftTape); // Preenche com espaços à esquerda
        rightTape = String.format("%-20s", rightTape); // Preenche com espaços à direita

        // Cabeçote no formato |S|
        String cabecote = leftDelim + id.getHead() + rightDelim; // Substituí os delimitadores por colchetes

        // Imprimir a saída formatada
        System.out.println(String.format("%s.%04d: %s%s%s", bloco, estado, leftTape, cabecote, rightTape));
    }




    /**
     * @param instr the instruction to check
     * @throws IllegalArgumentException if the given instruction contains invalid
     *                                  states, symbols, or directions
     */
    private void checkInstruction(Instruction instr) {
        if (!isState(instr.p) || !isState(instr.q)) {
            throw new IllegalArgumentException("Instruction's symbol does not exist");
        }
    }

    private boolean isState(int p) {
        return (-1 <= p && p < transition.size());
    }

    /**
     * @param id o ID
     * @return a próxima instrução a ser executada no ID, ou null se nenhuma existir (ou seja, este é um ID de parada)

     */
    private Instruction getInstruction(ID id) {
        // Tenta obter a instrução específica para o caractere lido
        Instruction instr = transition.get(id.state).get(id.getHead());

        // Se não encontrar, tenta obter uma instrução genérica para qualquer caractere '*'
        if (instr == null) {
            instr = transition.get(id.state).get('*');
        }

        return instr;
    }


    /**
     * uma instância de ID representa uma descrição instantânea de uma Máquina de Turing.
     */
    public static class ID {
        private static StringBuilder contents; // the string contents of the tape
        private int state; // the current state of the tape
        private static int position; // the current position of the read/write head

        /**
         * construtor
         *
         * @param contents the current
         */
        ID(String contents) {
            this.contents = new StringBuilder(contents);
            this.state = 0;
            this.position = 0;
        }

        /**
         * realiza a instrucao
         *
         * @param instr a instrucao a ser realizada
         */
        void applyInstruction(Instruction instr) {
            // Verifica se a instrução tem 'a' como '*' (coringa), então qualquer caractere é válido
            char currentChar = contents.charAt(position);
            if (instr.a != '*' && instr.a != currentChar) {
                return; // Ignora a instrução se o caractere atual não corresponde e não for '*'
            }

            // Se 'b' for '*', mantém o mesmo caractere que estava na posição
            char charToWrite = (instr.b == '*') ? currentChar : instr.b;
            contents.setCharAt(position, charToWrite);

            // Atualiza o estado da máquina
            state = instr.q;

            // Move a cabeça de leitura/gravação conforme a direção da instrução
            if (instr.m == Instruction.Direction.L) {
                if (position == 0) {
                    contents.insert(0, blank); // Adiciona espaço em branco à esquerda se estiver na borda
                } else {
                    position--;
                }
            } else if (instr.m == Instruction.Direction.R) {
                if (position == contents.length() - 1) {
                    contents.append(blank); // Adiciona espaço em branco à direita se estiver na borda
                }
                position++;
            }
            // Se a direção for 'N', a posição não muda.
        }



        /**
         * @return o simbolo que está na cabeça
         */
        static public char getHead() {
            if (contents == null || contents.length() == 0) {
                return '\0';
            }
            return contents.charAt(position);
        }

        public String toString() {
            return contents.substring(0, position) + spacer + state + spacer + contents.substring(position);
        }
    }


}
