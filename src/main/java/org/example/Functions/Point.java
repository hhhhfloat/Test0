package org.example.Functions;


public record Point(int x, int y) {

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
