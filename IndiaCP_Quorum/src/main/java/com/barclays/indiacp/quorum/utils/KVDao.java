package com.barclays.indiacp.quorum.utils;

/**
 * Created by surajman on 1/9/17.
 */

import org.mapdb.*;

import java.util.concurrent.ConcurrentMap;

public class KVDao {
    DB db;
    ConcurrentMap map;

    public KVDao(String mapName) {
        db = DBMaker.memoryDB().make();
        map = db.hashMap(mapName).createOrOpen();
    }

    public KVDao(String mapName, String dbPath) {
        db = DBMaker.fileDB(dbPath).make();
        map = db.hashMap(mapName).createOrOpen();
    }

    public ConcurrentMap map() {return map;}






}
