package com.company;

public class Token {

    private String name;
    private Double value;
    private TokenType type;

    public Token(String name, TokenType type) {
        this.name = name;
        this.type = type;
    }

    public Token(Double value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public Token(TokenType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("{ Type : %s , name : %s, value : %f }", type, name, value);
    }
}
