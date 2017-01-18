package com.barclays.indiacp.quorum.utils;

/**
 * Created by surajman on 1/9/17.
 *
 * --------------------- ON HOLD -----------------------
 */

import org.mapdb.*;

import java.util.concurrent.ConcurrentMap;

public class KVDao {
    DB db;
    ConcurrentMap map;

    public KVDao(String name) {
        db = DBMaker.memoryDB().make();
        map = db.hashMap(name).createOrOpen();
    }

    public KVDao(String name, String filePath) {
        db = DBMaker.fileDB(filePath).make();
        map = db.hashMap(name).createOrOpen();
    }

    public void insertKV(String key, String val) {
        map.put(key, val);
    }


}
