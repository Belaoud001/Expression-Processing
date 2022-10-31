package com.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ExpressionParser {

    private static List<Token> tokens;
    private static int currentToken;

    private static CompiledExpression.Node levelOne() throws Exception {
        CompiledExpression.Node leftSide = levelTwo();

        while (true) {
            if (currentToken >= tokens.size())
                return leftSide;

            Token token = tokens.get(currentToken);

            if (token.getType() != TokenType.OR)
                break;

            currentToken++;

            CompiledExpression.Node newNode = new CompiledExpression.Node(token, null);

            newNode.leftNode = leftSide;
            newNode.rightNode = levelTwo();
            leftSide = newNode;
        }

        return leftSide;
    }

    private static CompiledExpression.Node levelTwo() throws Exception {
        CompiledExpression.Node leftSide = levelThree();

        while (true) {
            if (currentToken >= tokens.size())
                return leftSide;

            Token token = tokens.get(currentToken);

            if (token.getType() != TokenType.AND)
                break;

            currentToken++;

            CompiledExpression.Node newNode = new CompiledExpression.Node(token, null);

            newNode.leftNode = leftSide;
            newNode.rightNode = levelThree();
            leftSide = newNode;
        }
        return leftSide;
    }

    private static CompiledExpression.Node levelThree() throws Exception {
        CompiledExpression.Node leftSide = levelFour();

        if (currentToken >= tokens.size())
            return leftSide;

        Token token = tokens.get(currentToken);

        if (token.getType() != TokenType.EQUAL && token.getType() != TokenType.NOT_EQUAL)
            return leftSide;

        currentToken++;

        CompiledExpression.Node newNode = new CompiledExpression.Node(token, null);

        newNode.leftNode = leftSide;
        newNode.rightNode = levelFour();

        return newNode;
    }

    private static CompiledExpression.Node levelFour() throws Exception {
        CompiledExpression.Node leftSide = levelFive();

        if (currentToken >= tokens.size())
            return leftSide;

        Token token = tokens.get(currentToken);

        switch (token.getType()) {
            case LESS:
            case LESS_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
                CompiledExpression.Node newNode = new CompiledExpression.Node(token, null);

                newNode.leftNode = leftSide;
                newNode.rightNode = levelFive();

                return newNode;
            default:
                break;
        }

        return leftSide;
    }

    private static CompiledExpression.Node levelFive() throws Exception {
        CompiledExpression.Node leftSide = levelSix();

        while (true) {
            if (currentToken >= tokens.size())
                return leftSide;

            Token token = tokens.get(currentToken);

            switch (token.getType()) {
                case ADD:
                case SUBTRACT:
                    currentToken++;

                    CompiledExpression.Node newNode = new CompiledExpression.Node(token, null);

                    newNode.leftNode = leftSide;
                    newNode.rightNode = levelSix();

                    leftSide = newNode;

                    break;
                default:
                    return leftSide;
            }
        }
    }

    private static CompiledExpression.Node levelSix() throws Exception {
        CompiledExpression.Node leftSide = levelSeven();

        while (true) {
            if (currentToken >= tokens.size())
                return leftSide;

            Token token = tokens.get(currentToken);

            switch (token.getType()) {
                case MULTIPLY:
                case DIVIDE:
                case MODULO:
                    currentToken++;

                    CompiledExpression.Node newNode = new CompiledExpression.Node(token, null);

                    newNode.leftNode = leftSide;
                    newNode.rightNode = levelSeven();

                    leftSide = newNode;
                    break;
                default:
                    return leftSide;
            }
        }
    }

    private static CompiledExpression.Node levelSeven() throws Exception {

        if (currentToken >= tokens.size())
            throw new Exception("Syntax Error ...");

        Token token = tokens.get(currentToken++);

        if (token.getType() == TokenType.NUMBER)
            return new CompiledExpression.Node(null, new Value(ValueType.NUMBER, token.getValue()));

        if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            CompiledExpression.Node inside = levelOne();

            token = tokens.get(currentToken++);

            if (token.getType() != TokenType.RIGHT_PARENTHESIS)
                throw new Exception("')' is expected ...");

            return inside;
        }

        if(token.getType() == TokenType.VARIABLE)
            return new CompiledExpression.Node(token, null);

        throw new Exception("Syntax Error ...");

    }

    public static CompiledExpression.Node parseTokens(List<Token> tokens) throws Exception {
        ExpressionParser.tokens = tokens;

        return levelOne();
    }

    public static void main(String[] args) {
        Map<String, Value> variables = new HashMap<>();

        try {
            CompiledExpression compiledExpression = new CompiledExpression("x + 1 + 100", new FakeVariableNameFixer());

            variables.put("x", new Value(ValueType.NUMBER, 1.0));
            System.out.println(compiledExpression.evaluate(variables));

        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
