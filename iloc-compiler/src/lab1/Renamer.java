package src.lab1;

import java.util.*;
import static src.lab1.Category.*; // allows me to refer to static variables without prefixing them with Category


public class Renamer {
    private static final int INVALID = -1;
    private static int VRName = 0;
    private int[] SRToVR;
    private int[] PrevUse;

    public Renamer(int maxSourceRegisterNumber, List<Operations> block) {
        SRToVR = new int[maxSourceRegisterNumber + 1];
        PrevUse = new int[maxSourceRegisterNumber + 1];

        for (int i = 0; i <= maxSourceRegisterNumber; i++) {
            SRToVR[i] = INVALID;
            PrevUse[i] = Integer.MAX_VALUE;
        }

        int index = block.size();

        OperandVisitor operandVisitor = new OperandVisitor();

        for (int i = block.size() - 1; i >= 0; i--) {
            Operations op = block.get(i);
            List<Token> operands = op.accept(operandVisitor);

            for (Token O : operands) {
                processOperand(O, index);
            }

            index--;
        }
    }

    private void processOperand(Token O, int index) {
        int SR = Integer.parseInt(O.lexeme);  // Assuming lexeme represents the source register.
        if (SRToVR[SR] == INVALID) {
            SRToVR[SR] = VRName++;
            O.VR = SRToVR[SR];
            O.NU = PrevUse[SR];
            PrevUse[SR] = Integer.MAX_VALUE;
            SRToVR[SR] = INVALID;
        }
        PrevUse[SR] = index;
    }

    private class OperandVisitor implements Operations.Visitor<List<Token>> {
        @Override
        public List<Token> visitZeroOp(Token op) {
            return Arrays.asList();  // No operands for ZeroOp
        }

        @Override
        public List<Token> visitOneOp(Token op, Token first) {
            return Arrays.asList(first);
        }

        @Override
        public List<Token> visitTwoOp(Token op, Token first, Token second) {
            return Arrays.asList(first, second);
        }

        @Override
        public List<Token> visitThreeOp(Token op, Token first, Token second, Token third) {
            return Arrays.asList(first, second, third);
        }
    }
}