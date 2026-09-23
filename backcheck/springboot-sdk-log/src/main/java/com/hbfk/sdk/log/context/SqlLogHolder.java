package com.hbfk.sdk.log.context;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 61
 * @Description: sql日志线程存储器
 */
public class SqlLogHolder {
    private static final ThreadLocal<List<String>> SQL_LOGS = ThreadLocal.withInitial(ArrayList::new);

    public static void addLog(String sqlLog) {
        SQL_LOGS.get().add(sqlLog);
    }

    public static List<String> getLogs() {
        return SQL_LOGS.get();
    }

    public static void clear() {
        SQL_LOGS.remove();
    }
}
