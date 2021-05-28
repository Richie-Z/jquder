/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richie.jqueder;

import com.richie.jqueder.method.Find;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Richie-PC
 */
public class DB {

    public List<String> table = new ArrayList<>();
    public List<String> select = new ArrayList<>();
    public List<String> where = new ArrayList<>();
    public static final Helper helper = new Helper();

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
                + helper.toPlainString(this.where);
    }

    private DB select(String... selects) {
        if (selects.length > 1) {
            for (String s : selects) {
                this.select.add(helper.concatWithTable(s, this.table.get(0)));
            }
        } else {
            this.select.add(helper.concatWithTable(selects[0], this.table.get(0)));
        }
        return this;
    }

    private Find find(Object id) {
        return this.find(id, "id");
    }

    private Find find(Object id, String pk) {
        this.where.add(this.where.size() > 0
                ? " AND " + pk + " = " + id
                : " WHERE " + pk + " = " + id);
        return new Find(this.table.get(0),this.select,this.where);
    }

    private DB where(String pk, Object... where) {
        this.where.add(this.where.size() > 0
                ? " AND " + pk + helper.defineWhere(where)
                : " WHERE " + pk + helper.defineWhere(where));
        return this;
    }

    private String count(String... pk) {
        String col = pk.length == 0 ? "id" : pk[0];
        this.select.add("COUNT(".concat(col + ")"));
        return this.toSql();
    }

    private String max(String col) {
        this.select.add("MAX(".concat(col + ")"));
        return this.toSql();
    }

    private String avg(String col) {
        this.select.add("AVG(".concat(col + ")"));
        return this.toSql();
    }

    private String insert(HashMap<String, Object> hm) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + this.table.get(0) + " (");
        int current_loop = 0;
        for (Map.Entry<String, Object> e : hm.entrySet()) {
            current_loop++;
            sql.append(e.getKey()).append(helper.checkIfOffset(current_loop, hm.size()));
        }
        current_loop = 0;
        sql.append(")".concat(" VALUES ("));
        for (Map.Entry<String, Object> e : hm.entrySet()) {
            current_loop++;
            sql.append(helper.addDoubleQuotes(e.getValue())).append(helper.checkIfOffset(current_loop, hm.size()));
        }
        sql.append(")");
        return sql.toString();
    }

    public static void main(String[] args) {
        DB db = new DB("kelas");
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("kelas", 11);
        hm.put("kompetensi_keahlian", "BKP");      
        System.out.println(db.find(1).delete());
        System.out.println(db.find(1).update(hm));
        
    }
}
