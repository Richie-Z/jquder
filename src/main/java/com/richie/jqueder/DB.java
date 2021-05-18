/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richie.jqueder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Richie-PC
 */
public class DB {

    List<String> table = new ArrayList<String>();
    List<String> select = new ArrayList<String>();

    public DB(String table) {
        this.table.add(table);
    }

    public String toSql() {
        StringBuilder temp_select = new StringBuilder();
        if (!select.isEmpty()) {
            for (int i = 0; i < select.size(); i++) {
                temp_select.append(i == select.size() - 1 ? 
                        table.get(0).concat("." + select.get(i)) : 
                        table.get(0).concat("." + select.get(i).concat(",")));
            }
        }
        String select_query = temp_select.length() == 0 ?"*" : temp_select.toString();
       return "SELECT " + select_query + " FROM " + this.table.get(0)  ; 
    }

    public DB select(String... select) {
        if (select.length > 1) {
            this.select.addAll(Arrays.asList(select));
        } else {
            this.select.add(select[0]);
        }
        return this;
    }

    public static void main(String[] args) {
        DB db = new DB("migrations");
        db.select("batch", "id").select("migration");
        System.out.println(db.toSql());
    }
}
