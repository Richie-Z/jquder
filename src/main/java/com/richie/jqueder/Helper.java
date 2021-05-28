/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richie.jqueder;

import java.util.List;

/**
 *
 * @author Richie-PC
 */
public class Helper {

    public String defineWhere(Object[] a) {
        return a.length == 1
                ? " = " + this.addDoubleQuotes(a[0])
                : " " + a[0] + " " + this.addDoubleQuotes(a[1]);
    }

    public String toPlainString(List a) {
        StringBuilder temp = new StringBuilder();
        a.forEach(s -> {
            temp.append(s.toString());
        });
        return temp.toString();
    }

    public String concatWithTable(String s, String table) {
        if (s.contains(".")) {
            return s;
        }
        return table.concat("." + s);
    }

    public Object addDoubleQuotes(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return value;
    }

    public String checkIfOffset(int current, int max) {
        return current == max ? "" : ",";
    }

}
