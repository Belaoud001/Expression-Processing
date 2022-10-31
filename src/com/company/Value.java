package com.company;

public class Value  {

    private ValueType type;
    private Object data;

    public Value(ValueType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Value{" + "type=" + type + ", data=" + data + '}';
    }
}
