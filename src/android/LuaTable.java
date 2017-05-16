package com.konylabs.vm;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


public class LuaTable implements Serializable {
    static final long serialVersionUID = 7748609846044439686L;
    public Hashtable map;
    public Vector list;
    public TableEnumeration enumeration;
    private Context context;

    public LuaTable(Context c) {
        this(c, 0, 0);
    }

    public LuaTable(Context c, int var1, int var2) {
        this.map = new Hashtable(var2);
        this.list = new Vector(var1);
        this.enumeration = new TableEnumeration(this);
        this.context = c;
    }

    public LuaTable(Context c, int var1, int var2, boolean var3) {
        this.map = new Hashtable(var2);
        this.list = new Vector(var1);
        if(var3) {
            this.enumeration = new TableEnumeration(this);
        } else {
            this.enumeration = null;
        }
        this.context = c;
    }

    public LuaTable(Context c, int var1, int var2, float var3, boolean var4) {
        this.map = new Hashtable(var2, var3);
        this.list = new Vector(var1);
        if(var4) {
            this.enumeration = new TableEnumeration(this);
        } else {
            this.enumeration = null;
        }
        this.context = c;
    }

    public void addAll(Vector var1) {
        int var2 = var1.size();
        this.list.ensureCapacity(this.list.size() + var2);

        for(int var3 = 0; var3 < var2; ++var3) {
            this.list.addElement(var1.elementAt(var3));
        }

        this.a(this.list.size());
    }

    public void add(Object var1) {
        this.list.ensureCapacity(this.list.size() + 1);
        this.list.addElement(var1);
        this.a(this.list.size());
    }

    public int arraySize() {
        return this.list.size();
    }

    public int hashSize() {
        return this.map.size();
    }

    public int size() {
        return this.list.size();
    }

    public Object[] getKeys() {
        Object[] var1 = null;
        if(this.map.size() > 0) {
            var1 = new Object[this.map.size()];
            Enumeration var2 = this.map.keys();

            for(int var3 = 0; var2.hasMoreElements(); ++var3) {
                var1[var3] = var2.nextElement();
            }
        }

        return var1;
    }

    public Object[] getArrayValues() {
        Object[] var1 = null;
        if(this.list.size() > 0) {
            var1 = this.list.toArray();
        }

        return var1;
    }

    public Object[] getHashValues() {
        Object[] var1 = null;
        if(this.map.size() > 0) {
            var1 = this.map.values().toArray();
        }

        return var1;
    }

    public void insert(Object var1, Object var2) {
        if(!(var1 instanceof Double)) {
            throw new RuntimeException("table.insert");
        } else {
            int var3;
            if((var3 = (int)((Double)var1).doubleValue() - 1) < this.list.size() && var3 >= 0) {
                this.list.insertElementAt(var2, var3);
                this.a(this.list.size());
            } else {
                if(var3 != this.list.size()) {
                    this.a(var1, var2);
                    return;
                }

                this.list.ensureCapacity(var3 + 20);
                this.list.addElement(var2);
                this.a(var3 + 1);
            }

        }
    }

    public void setTable(Object var1, Object var2) {
        if(var1 != null) {
            if(var2 == null) {
                var2 = LuaNil.nil;
            }

            if(var1 instanceof Double) {
                int var3;
                if((var3 = (int)((Double)var1).doubleValue() - 1) >= 0 && var3 < this.list.size()) {
                    this.list.setElementAt(var2, var3);
                } else {
                    if(var3 != this.list.size()) {
                        this.a(var1, var2);
                        return;
                    }

                    this.list.ensureCapacity(var3 + 20);
                    this.list.addElement(var2);
                    this.a(var3 + 1);
                }
            } else {
                if(var1 instanceof String) {
                    String var4 = (String)var1;
                    this.a((Object)var4.trim(), (Object)var2);
                    return;
                }

                this.a(var1, var2);
            }

        }
    }

    private void a(Object var1, Object var2) {
        if(var2 != LuaNil.nil) {
            if(!this.map.containsKey(var1) && this.enumeration != null) {
                this.enumeration.keys.addElement(var1);
            }

            this.map.put(var1, var2);
        } else {
            if(this.map.containsKey(var1) && this.enumeration != null) {
                this.enumeration.keys.removeElement(var1);
            }

            this.map.remove(var1);
        }
    }

    private void a(int var1) {
        if(this.enumeration != null) {
            boolean var2 = false;
            Object var3 = null;

            do {
                ++var1;
                Double var4 = new Double((double)var1);
                if(var2 = this.enumeration.keys.removeElement(var4)) {
                    var3 = this.map.get(var4);
                    this.map.remove(var4);
                    this.list.addElement(var3);
                }
            } while(var2);

        }
    }

    public Object getTable(Object var1) {
        if(var1 != null && var1 != LuaNil.nil) {
            Object var3;
            if(var1 instanceof Double) {
                int var2;
                if((var2 = (int)((Double)var1).doubleValue() - 1) >= 0 && var2 < this.list.size()) {
                    var3 = this.list.elementAt(var2);
                } else {
                    var3 = this.map.get(var1);
                }
            } else if(var1 instanceof String) {
                String var4 = (String)var1;
                var3 = this.map.get(var4.trim());
            } else {
                var3 = this.map.get(var1);
            }

            if(var3 == null) {
                LuaTable var5;
                var3 = (var5 = (LuaTable)this.map.get("__index")) != null?var5.getTable(var1):null;
            }

            return var3 = var3 != null?var3:LuaNil.nil;
        } else {
            return LuaNil.nil;
        }
    }

    public boolean equals(Object var1) {
        return !(var1 instanceof LuaTable)?false:((LuaTable)var1).list.equals(this.list) && ((LuaTable)var1).map.equals(this.map);
    }

    public int hashCode() {
        return this.list.hashCode() + this.map.hashCode();
    }

    private String a() {
        StringBuffer var1 = new StringBuffer("{ ");
        Object var3;
        if(this.list.size() > 0) {
            Iterator var2 = this.list.iterator();

            while(var2.hasNext()) {
                var3 = var2.next();
                var1.append(var3.toString()).append(", ");
            }
        }

        if(this.map.size() > 0) {
            Enumeration var5 = this.map.keys();
            var3 = null;
            Object var4 = null;

            while(var5.hasMoreElements()) {
                var3 = var5.nextElement();
                var4 = this.map.get(var3);
                var1.append(var3.toString()).append(" = ").append(var4.toString()).append(", ");
            }
        }

        return var1.append(" }").toString();
    }

    public String toString() {
        if(context != null) {
            return this.a();
        } else {
            LuaTable var1 = this;
            StringBuffer var2 = new StringBuffer("");
            Object var4;
            if(this.list.size() > 0) {
                var2.append(" [ ");
                Iterator var3 = this.list.iterator();
                Boolean hasNext = var3.hasNext();
                while(hasNext) {
                    var4 = var3.next();
                    var2.append(var4.toString());
                    hasNext = var3.hasNext();
                    if(hasNext) {
                        var2.append(", ");
                    }
                }

                var2.append(" ] ");
            }

            if(this.map.size() > 0) {
                var2.append(" { ");
                Enumeration var6 = this.map.keys();
                var4 = null;
                Object var5 = null;

                Boolean hasMore = var6.hasMoreElements();
                while(hasMore) {
                    var4 = var6.nextElement();
                    var5 = var1.map.get(var4);
                    String value;
                    if(var5 instanceof LuaTable) {
                        value = var5.toString();
                        value = value.isEmpty() ? "{}" : value;
                    } else if (var5 instanceof String) {
                        value = '"' + var5.toString().trim().replace("\n", "") + '"';
                    } else if (Double.class.isInstance(var5)) {
                        value = String.format("%.0f", var5);
                    } else {
                        value = var5.toString();
                    }
                    String key = '"' + var4.toString().trim().replace("\n", "") + '"';
                    var2.append(key).append(" : ").append(value);
                    hasMore = var6.hasMoreElements();
                    if(hasMore) {
                        var2.append(", ");
                    }
                }

                var2.append(" } ");
            }

            return var2.toString();
        }
    }

    private void a(String var1, StringBuffer var2) {
        Object var4;
        if(this.list.size() > 0) {
            a(var1, var2, "[");
            Iterator var3 = this.list.iterator();

            while(var3.hasNext()) {
                if((var4 = var3.next()) instanceof LuaTable) {
                    ((LuaTable)var4).a(var1, var2);
                    a(var1, var2, ", ");
                } else {
                    a(var1, var2, var4.toString() + ", ");
                }
            }

            a(var1, var2, "]");
        }

        if(this.map.size() > 0) {
            a(var1, var2, "{");
            Enumeration var6 = this.map.keys();
            var4 = null;
            Object var5 = null;

            while(var6.hasMoreElements()) {
                var4 = var6.nextElement();
                if((var5 = this.map.get(var4)) instanceof LuaTable) {
                    a(var1, var2, var4 + " : ");
                    ((LuaTable)var5).a(var1, var2);
                    a(var1, var2, ", ");
                } else {
                    a(var1, var2, var4 + " : " + var5 + ", ");
                }
            }

            a(var1, var2, "}");
        }

    }

    private void a(String var1) {
        Log.d(var1, "<------Start LuaTable: " + this.hashCode() + "------>");
        Object var3;
        if(this.list.size() > 0) {
            Iterator var2 = this.list.iterator();

            while(var2.hasNext()) {
                if((var3 = var2.next()) instanceof LuaTable) {
                    ((LuaTable)var3).a(var1);
                } else {
                    Log.d(var1, var3.toString());
                }
            }
        }

        if(this.map.size() > 0) {
            Enumeration var5 = this.map.keys();
            var3 = null;
            Object var4 = null;

            while(var5.hasMoreElements()) {
                var3 = var5.nextElement();
                if((var4 = this.map.get(var3)) instanceof LuaTable) {
                    Log.d(var1, var3 + " = ");
                    ((LuaTable)var4).a(var1);
                } else {
                    Log.d(var1, var3 + " = " + var4);
                }
            }
        }

        Log.d(var1, "<------End LuaTable: " + this.hashCode() + "------>");
    }

    public void print(String var1) {
        if(context != null) {
            StringBuffer var2 = new StringBuffer(64);
            Log.d(var1, "<------Start JS Object: ------>");
            this.a(var1, var2);
            Log.d(var1, var2.toString());
            Log.d(var1, "<------End JS Object: ------>");
        } else {
            this.a(var1);
        }
    }

    private static void a(String var0, StringBuffer var1, String var2) {
        int var4 = 0;
        int var5 = var2.length();

        while (var5 - var4 > 0) {
            int var3;
            if ((var3 = var1.capacity() - var1.length()) >= var5 - var4) {
                var1.append(var2, var4, var5);
                return;
            }

            var1.append(var2, var4, var4 + var3);
            var4 += var3;
            Log.d(var0, var1.toString());
            var1.setLength(0);
        }

    }
}
