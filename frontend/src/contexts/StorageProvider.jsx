import localforage from "localforage";
import localForage from "localforage";

localForage.config({
    driver: [
        localForage.INDEXEDDB,
        localForage.LOCALSTORAGE,
        localForage.WEBSQL,
    ],
    name: 'Star Bank',
    version: 1.0,
});

export const DataStore = localforage.createInstance({
    name: "Star Bank",
    storeName: "DataStore"
});