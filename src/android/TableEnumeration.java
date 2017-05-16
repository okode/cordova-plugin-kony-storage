package com.konylabs.vm;


import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

public class TableEnumeration implements Serializable, Enumeration {
    static final long serialVersionUID = 8980316118785864039L;
    public LuaTable table;
    public Vector keys = new Vector(10);
    public int index = 1;

    public TableEnumeration(LuaTable var1) {
        this.table = var1;
    }

    public boolean hasMoreElements() {
        return this.index <= this.table.size() + this.keys.size();
    }

    public Object nextElement() throws NoSuchElementException {
        if(this.index <= this.table.list.size()) {
            return new Double((double)(this.index++));
        } else {
            int var1 = this.index - this.table.list.size();
            ++this.index;
            if(var1 <= this.keys.size()) {
                return this.keys.elementAt(var1 - 1);
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public void reset() {
        this.index = 1;
    }
}
