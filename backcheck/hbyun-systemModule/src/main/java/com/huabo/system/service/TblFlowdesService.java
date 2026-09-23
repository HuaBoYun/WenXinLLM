package com.huabo.system.service;

import java.util.List;

import com.huabo.system.entity.TblFlowdes;

public interface TblFlowdesService {
    List<TblFlowdes> returnFlowBysqls(String currentpid);

    void delete(String toString);

    void add(TblFlowdes var1);

}
