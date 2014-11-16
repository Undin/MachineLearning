package com.ifmo.machinelearning.homework5;

import gnu.trove.map.hash.TLongIntHashMap;

/**
 * Created by warrior on 16.11.14.
 */
public class IdConverter {

    private IdConverter() {
    }

    public static final int NO_ENTRY_VALUE = -1;

    private static final int ITEM_EXPECTED_NUMBER = 3500;
    private static final int USER_EXPECTED_NUMBER = 5500;
    private static final float LOAD_FACTOR = 0.5F;

    private static final TLongIntHashMap ITEM_FROM_REAL_ID = new TLongIntHashMap((int) (ITEM_EXPECTED_NUMBER / LOAD_FACTOR), LOAD_FACTOR, 0L, NO_ENTRY_VALUE);
    private static final TLongIntHashMap USER_FROM_REAL_ID = new TLongIntHashMap((int) (USER_EXPECTED_NUMBER / LOAD_FACTOR), LOAD_FACTOR, 0L, NO_ENTRY_VALUE);

    public static int fromItemRealId(long realId) {
        int id = ITEM_FROM_REAL_ID.get(realId);
        if (id == NO_ENTRY_VALUE) {
            id = ITEM_FROM_REAL_ID.size();
            ITEM_FROM_REAL_ID.put(realId, id);
        }
        return id;
    }

    public static int getItemId(long realId) {
        return ITEM_FROM_REAL_ID.get(realId);
    }

    public static int fromUserRealId(long realId) {
        int id = USER_FROM_REAL_ID.get(realId);
        if (id == NO_ENTRY_VALUE) {
            id = USER_FROM_REAL_ID.size();
            USER_FROM_REAL_ID.put(realId, id);
        }
        return id;
    }

    public static int getUserId(long realId) {
        return USER_FROM_REAL_ID.get(realId);
    }

    public static int userNumber() {
        return USER_FROM_REAL_ID.size();
    }

    public static int itemNumber() {
        return ITEM_FROM_REAL_ID.size();
    }
}
