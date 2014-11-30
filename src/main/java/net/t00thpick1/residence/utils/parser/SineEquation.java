package net.t00thpick1.residence.utils.parser;

import java.util.Map;

public class SineEquation extends Equation {
    private Equation inside;

    public SineEquation(Equation inside) {
        this.inside = inside;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return Math.sin(inside.calculate(variables));
    }

    public String toString() {
        return "sin(" + inside.toString() + ")";
    }
}
