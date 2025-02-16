package test;

import main.Instruction;
import main.TuringMachine;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static main.TuringMachine.loadFromFile;
import static org.junit.Assert.*;


public class TuringMachineTest {
    static private TuringMachine tm;

    static {
        try {
            tm = loadFromFile("src/machines/palindromo.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAccept2() {
        String computed = "aacaa1";
        boolean terminou= tm.computeString(computed, true);

    }


    /**
     * printa as instruções
     */
    static private void outputInstructions(Set<Instruction> trans) {
        System.out.println("===== INSTRUCTIONS =====");
        for (Instruction instr : trans) {
            System.out.println(instr);
        }
        System.out.println();
    }
}