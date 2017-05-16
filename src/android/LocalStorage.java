package kony.storage.plugin;

import com.konylabs.vm.LuaTable;

public class LocalStorage {

    public static final int FLOAT = 0;
    public static final int STRING = 1;
    public static final int INTEGER = 2;
    public static final int BOOLEAN = 3;
    public static final int BLOB = 4;


    private String key;
    private Integer dataType;

    private Float floatValue;
    private String stringValue;
    private Integer integerValue;
    private Boolean booleanValue;
    private LuaTable blobValue;


    LocalStorage(String key, Float floatValue) {
        this.key = key;
        this.dataType = FLOAT;
        this.floatValue = floatValue;
    }

    LocalStorage(String key, String stringValue) {
        this.key = key;
        this.dataType = STRING;
        this.stringValue = stringValue;
    }

    LocalStorage(String key, Integer integerValue) {
        this.key = key;
        this.dataType = INTEGER;
        this.integerValue = integerValue;
    }

    LocalStorage(String key, Boolean booleanValue) {
        this.key = key;
        this.dataType = BOOLEAN;
        this.booleanValue = booleanValue;
    }

    LocalStorage(String key, LuaTable blobValue) {
        this.key = key;
        this.dataType = BLOB;
        this.blobValue = blobValue;
    }

    public String getValue() {
        try {
            switch (dataType) {
                case FLOAT:
                    return floatValue == null ? "" : String.valueOf(floatValue);
                case INTEGER:
                    return integerValue == null ? "" : String.valueOf(integerValue);
                case BOOLEAN:
                    return booleanValue == null ? "" : String.valueOf(booleanValue);
                case LocalStorage.BLOB:
                    return blobValue == null ? "" : blobValue.toString();
                case LocalStorage.STRING:
                default:
                    return stringValue == null ? "" : stringValue.trim().replace("\n", "");
            }
        } catch (NullPointerException ex) {
            return "";
        }
    }
}
