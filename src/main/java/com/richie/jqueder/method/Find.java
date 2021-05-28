/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richie.jqueder.method;

import com.richie.jqueder.DB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Richie-PC
 */
public class Find extends DB{
    
    public Find(String table, List select, List where) {
        super(table);
        super.select = select;
        super.where = where;
    }
    
     public String delete() {
        String sql = "DELETE FROM ".concat(super.table.get(0) + super.where.get(0));
        return sql;
    }
     
     public String update(HashMap<String, Object> hm) {
        StringBuilder sql = new StringBuilder("UPDATE " + super.table.get(0) + " SET ");
        int current_loop = 0;
        for (Map.Entry<String, Object> e : hm.entrySet()) {
            current_loop++;
            sql.append(e.getKey() + " = "
                    + DB.helper.addDoubleQuotes(e.getValue())
                    + DB.helper.checkIfOffset(current_loop, hm.size()));
        }
        sql.append(super.where.get(0));
        return sql.toString();
    }
}
