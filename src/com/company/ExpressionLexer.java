package com.company;

import java.util.ArrayList;

public class ExpressionLexer {

    private static String currentString = "";
    private static int currentPosition = 0;

    private static String nextChar() {
        String expression = "";

        expression += (char) currentString.charAt(currentPosition++);

        return expression;
    }

    private static double nextDouble() {
        String numberTokens = ".0123456789";
        String character = nextChar();
        StringBuilder number = new StringBuilder();

        while (numberTokens.contains(character)) {
            number.append(character);

            if (!thereIsMore())
                break;

            character = nextChar();
        }

        if (!numberTokens.contains(character) || thereIsMore())
            ungetc();

        return Double.parseDouble(number.toString());
    }

    private static void ungetc() {
        --currentPosition;
    }

    private static boolean thereIsMore() {
        return currentPosition < currentString.length();
    }

    private static Token next() throws Exception {
        if (!thereIsMore())
            return new Token(TokenType.EOF);

        String character = nextChar();

        while (thereIsMore() && character.equals(" ") || character.equals("\n"))
            character = nextChar();

        if (character.equals(" "))
            return new Token(TokenType.EOF);

        String numberTokens = ".0123456789";

        switch (character) {
            case "+":
                return new Token(TokenType.ADD);
            case "-":
                return new Token(TokenType.SUBTRACT);
            case "*":
                return new Token(TokenType.MULTIPLY);
            case "/":
                return new Token(TokenType.DIVIDE);
            case "%":
                return new Token(TokenType.MODULO);
            case "=":
                return new Token(TokenType.EQUAL);
            case "(":
                return new Token(TokenType.LEFT_PARENTHESIS);
            case ")":
                return new Token(TokenType.RIGHT_PARENTHESIS);
            case "<": {
                if (thereIsMore()) {
                    String secondChar = nextChar();
                    if (secondChar.equals("="))
                        return new Token(TokenType.LESS_EQUAL);
                    else
                        ungetc();
                }
                return new Token(TokenType.LESS);
            }
            case ">": {
                if (thereIsMore()) {
                    String secondChar = nextChar();
                    if (secondChar.equals("="))
                        return new Token(TokenType.GREATER_EQUAL);
                    else
                        ungetc();
                }
                return new Token(TokenType.GREATER);
            }
            case "!": {
                if (thereIsMore()) {
                    String secondChar = nextChar();
                    if (secondChar.equals("="))
                        return new Token(TokenType.NOT_EQUAL);
                    else
                        ungetc();
                }
                return new Token(TokenType.NOT);
            }

        }

        if (numberTokens.contains(character)) {
            ungetc();
            double value = nextDouble();

            return new Token(value, TokenType.NUMBER);
        }

        if (character.charAt(0) == '\'') {
            StringBuilder string = new StringBuilder();

            character = nextChar();
            while (character.charAt(0) != '\'') {
                string.append(character);
                if (!thereIsMore())
                    break;
                character = nextChar();
            }

            return new Token(string.toString(), TokenType.STRING);
        }

        if (Character.isLetter(character.charAt(0))) {
            StringBuilder string = new StringBuilder(character);

            character = nextChar();
            while (Character.isLetter(character.charAt(0)) || Character.isDigit(character.charAt(0)) || character.charAt(0) == '.') {
                string.append(character);

                if (!thereIsMore())
                    break;
                character = nextChar();
            }

            if(thereIsMore())
                ungetc();

            if (string.toString().equals("AND"))
                return new Token(TokenType.AND);
            else if (string.toString().equals("OR"))
                return new Token(TokenType.OR);
            else if (string.toString().equals("NOT"))
                return new Token(TokenType.NOT);
            else
                return new Token(string.toString(), TokenType.VARIABLE);
        }

        throw new Exception("Unknown token");
    }

    public static ArrayList<Token> lexerProcess(String expression) throws Exception {
        init(expression);

        ArrayList<Token> tokens = new ArrayList<>();
        Token token = next();

        while (token.getType() != TokenType.EOF) {
            tokens.add(token);
            token = next();
        }

        return tokens;
    }

    private static void init(String expression) {
        currentString = expression;
        currentPosition = 0;
    }
}