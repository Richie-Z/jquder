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

    List<String> table = new ArrayList<>();
    List<String> select = new ArrayList<>();
    List<String> where = new ArrayList<>();

    public DB(String table) {
        this.table.add(table);
    }

    public String toSql() {
        StringBuilder temp_select = new StringBuilder();
        if (!select.isEmpty()) {
            for (int i = 0; i < select.size(); i++) {
                temp_select.append(i == select.size() - 1
                        ? select.get(i)
                        : select.get(i).concat(","));
            }
        }
        String select_query = temp_select.length() == 0 ? "*" : temp_select.toString();
        return "SELECT " + select_query 
                + " FROM " + this.table.get(0) 
                + this.toPlainString(this.where);
    }
    
    public DB select(String... select) {
        if (select.length > 1) {
            for (String s : select) {
                this.select.add(table.get(0).concat("." + s));
            }
        } else {
            this.select.add(table.get(0).concat("." + select[0]));
        }
        return this;
    }

    public String find(String id) {
        return this.find(id, "id");
    }

    public String find(String id, String pk) {
        this.where.add(this.where.size() > 0
                ? " AND " + pk + " = " + id
                : " WHERE " + pk + " = " + id);
        return this.toSql();
    }
    
    public DB where(String pk, String... where){
        this.where.add(this.where.size() > 0 
                ? " AND " + pk + this.defineWhere(where) 
                : " WHERE " + pk + this.defineWhere(where));
        return this;
    }
    public String defineWhere(String[] a) {
        return a.length == 1 ? " = " + this.addDoubleQuotes(a[0]) :" " + a[0] + " " + this.addDoubleQuotes(a[1]);
    }
    public String toPlainString(List a) {
        StringBuilder temp = new StringBuilder();
        a.forEach(s -> {
            temp.append(s.toString());
        });
        return temp.toString();
    }
    public String addDoubleQuotes(String value) {
        return "\"" + value + "\"";
    }
    public static void main(String[] args) {
        DB db = new DB("migrations");
        db.select("batch", "id").select("migration").where("migration","LIKE","asjagdad").where("richie","tampan").find("1");
        System.out.println(db.toSql());
    }
}
