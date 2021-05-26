/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richie.jqueder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public DB select(String... selects) {
        if (selects.length > 1) {
            for (String s : selects) {
                this.select.add(this.concatWithTable(s));
            }
        } else {
            this.select.add(this.concatWithTable(selects[0]));
        }
        return this;
    }

    public String find(Object id) {
        return this.find(id, "id");
    }

    public String find(Object id, String pk) {
        this.where.add(this.where.size() > 0
                ? " AND " + pk + " = " + id
                : " WHERE " + pk + " = " + id);
        return this.toSql();
    }

    public DB where(String pk, Object... where) {
        this.where.add(this.where.size() > 0
                ? " AND " + pk + this.defineWhere(where)
                : " WHERE " + pk + this.defineWhere(where));
        return this;
    }

    public String count(String... pk) {
        String col = pk.length == 0 ? "id" : pk[0];
        this.select.add("COUNT(".concat(col + ")"));
        return this.toSql();
    }

    public String max(String col) {
        this.select.add("MAX(".concat(col + ")"));
        return this.toSql();
    }

    public String avg(String col) {
        this.select.add("AVG(".concat(col + ")"));
        return this.toSql();
    }

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

    public String concatWithTable(String s) {
        if (s.contains(".")) {
            return s;
        }
        return this.table.get(0).concat("." + s);
    }

    public Object addDoubleQuotes(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return value;
    }

    public String insert(HashMap<String, Object> hm) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + this.table.get(0) + " (");
        int current_loop = 0;
        for (Map.Entry<String, Object> e : hm.entrySet()) {
            current_loop++;
            sql.append(e.getKey()).append(this.checkIfOffset(current_loop,hm.size()));
        }
        current_loop = 0;
        sql.append(")".concat(" VALUES ("));
        for (Map.Entry<String, Object> e : hm.entrySet()) {
            current_loop++;
            sql.append(this.addDoubleQuotes(e.getValue())).append(this.checkIfOffset(current_loop, hm.size()));
        }
        sql.append(")");
        return sql.toString();
    }

    public String delete() {
        String sql = "DELETE FROM ".concat(this.table.get(0) + this.where.get(0));
        return sql;
    }

    public String update(HashMap<String, Object> hm) {
        StringBuilder sql = new StringBuilder("UPDATE " + this.table.get(0) + " SET ");
        int current_loop = 0;
        for (Map.Entry<String, Object> e : hm.entrySet()) {
            current_loop++;
            sql.append(e.getKey() + " = "
                    + this.addDoubleQuotes(e.getValue())
                    + this.checkIfOffset(current_loop, hm.size()));
        }
        sql.append(this.where.get(0));
        return sql.toString();
    }

    public String checkIfOffset(int current, int max) {
        return current == max ? "" : ",";
    }

    public static void main(String[] args) {
        DB db = new DB("kelas");
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("kelas", 11);
        hm.put("kompetensi_keahlian", "BKP");
        System.out.println(db.where("id", 1).delete());
        System.out.println(db.where("id", 1).insert(hm));
    }
}
