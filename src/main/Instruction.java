package main;

/**
 * A instância de Instruction funciona como um elemento de transição da
 * função da MT
 */
public class Instruction {
    int p;
    char a;
    char b;
    Direction m;
    int q;

    public enum Direction {L, R, N}

    /**
     * Cria uma nova instrução no formato: p, a -> b, m, q
     *
     * @param p estado inicial
     * @param a símbolo de entrada
     * @param b símbolo final (para o qual "a" será alterado)
     * @param m direção para mover a cabeça de leitura/gravação
     * @param q estado final
     */

    public Instruction(int p, char a, char b, Direction m, int q) {
        this.p = p;
        this.a = a;
        this.b = b;
        this.m = m;
        this.q = q;
    }

    /**
     * @return uma string na forma '(p, a, b, m, q)'
     */
    public String toString() {
        String ret = "(" + p + ", " + a + ", " + b + ", ";
        return switch (m) {
            case L -> ret + "L, " + q + ")";
            case R -> ret + "R, " + q + ")";
            case N -> ret + "N, " + q + ")";
        };
    }
}
