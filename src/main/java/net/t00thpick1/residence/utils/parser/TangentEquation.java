package net.t00thpick1.residence.utils.parser;

import java.util.Map;

public class TangentEquation extends Equation {
    private Equation inside;

    public TangentEquation(Equation inside) {
        this.inside = inside;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return Math.tan(inside.calculate(variables));
    }

    public String toString() {
        return "tan(" + inside.toString() + ")";
    }
}
