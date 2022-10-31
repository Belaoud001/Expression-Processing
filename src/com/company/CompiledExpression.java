package com.company;

import java.util.List;
import java.util.Map;

public class CompiledExpression {

    public static class Node {
        public Token token;
        public Value value;
        public Node leftNode;
        public Node rightNode;

        public Node(Token token, Value value) {
            this.token = token;
            this.value = value;
        }
    }

    private Node root;

    public CompiledExpression(String expression, IVariableNameFixer fixer) throws Exception {
        List<Token> tokens = ExpressionLexer.lexerProcess(expression);

        System.out.println(tokens);

        for (Token token : tokens)
            if (token.getType() == TokenType.VARIABLE)
                token.setName(fixer.fix(token.getName()));

        root = ExpressionParser.parseTokens(tokens);
    }

    public Value evaluate(Map<String, Value> variables) throws Exception {
        return evaluateHelper(root, variables);
    }

    private Value evaluateHelper(Node node, Map<String, Value> variables) throws Exception {

        if (node.token != null) {
            // operation or variable node
            if (node.token.getType() == TokenType.VARIABLE) {
                // variable node
                return variables.get(node.token.getName());
            } else {
                // operation node
                return doOperation(
                        evaluateHelper(node.leftNode, variables),
                        evaluateHelper(node.rightNode, variables),
                        node.token.getType()
                );
            }
        } else {
            // value node
            return node.value;
        }
    }

    private static Value doOperation(Value value1, Value value2, TokenType operation) throws Exception {
        if (value1.getType() == ValueType.NUMBER && value1.getType() == value2.getType()) {
            double v1 = (double) value1.getData();
            double v2 = (double) value2.getData();

            switch (operation) {
                case ADD:
                    return new Value(ValueType.NUMBER, v1 + v2);
                case SUBTRACT:
                    return new Value(ValueType.NUMBER, v1 - v2);
                case MULTIPLY:
                    return new Value(ValueType.NUMBER, v1 * v2);
                case DIVIDE:
                    return new Value(ValueType.NUMBER, v1 / v2);
                case LESS:
                    return new Value(ValueType.BOOL, v1 < v2);
                case GREATER:
                    return new Value(ValueType.BOOL, v1 > v2);
                case GREATER_EQUAL:
                    return new Value(ValueType.BOOL, v1 >= v2);
                case LESS_EQUAL:
                    return new Value(ValueType.BOOL, v1 <= v2);
                case EQUAL:
                    return new Value(ValueType.BOOL, v1 == v2);
                case NOT_EQUAL:
                    return new Value(ValueType.BOOL, v1 != v2);
                case MODULO:
                    return new Value(ValueType.NUMBER, v1 % v2);
                default:
                    throw new Exception("Incompatible types ...");
            }
        }

        if (value1.getType() == ValueType.BOOL && value2.getType() == ValueType.BOOL) {
            boolean b1 = (boolean)value1.getData();
            boolean b2 = (boolean)value2.getData();

            switch (operation) {
                case AND:
                    return new Value(ValueType.BOOL, b1 && b2);
                case OR:
                    return new Value(ValueType.BOOL, b1 || b2);
                default:
                    throw new Exception("Incompatible types ...");
            }
        }

        throw new Exception("Incompatible types ...");
    }

}
