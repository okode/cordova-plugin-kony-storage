declare module KonyStorage {
    interface KonyStorePluginStatic {

        /**
        * Obtain a list of keys stored in kony
        */
        keySet(): Promise<Array<string>>;

        /**
        * Obtain value for the key stored in kony
        * @param key
        */
        get(key: string): Promise<string>;

    }
}

declare var konyStore: KonyStorage.KonyStorePluginStatic;
