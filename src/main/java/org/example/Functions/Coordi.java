package org.example.Functions;


public record Coordi(int x, int y) {

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
