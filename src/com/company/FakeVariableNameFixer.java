package com.company;

public class FakeVariableNameFixer implements IVariableNameFixer{

    @Override
    public String fix(String variableName) {
        return variableName;
    }

}
