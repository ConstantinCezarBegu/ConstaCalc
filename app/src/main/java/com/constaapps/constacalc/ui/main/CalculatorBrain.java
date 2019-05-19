package com.constaapps.constacalc.ui.main;

import java.io.StringReader;

public class CalculatorBrain {
    public static String calculate (String formula) {
        try {

            CalcParser parser = new CalcParser(new CalcScanner(new StringReader(formula)));
            Double result = (Double) parser.parse().value;
            return result.toString();
        } catch (Exception e) {
            return "ERROR";
        }

    }
}
