package com.huabo.monitor.mysql.entity;

import java.util.Comparator;


@SuppressWarnings("unchecked")
public class NodeIDComparatorMySql implements Comparator {
    // 按照节点编号比较
    public int compare(Object o1, Object o2) {
        int j1 = Integer.parseInt(((NodeMySql) o1).id);
        int j2 = Integer.parseInt(((NodeMySql) o2).id);
        return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));
    }
}
