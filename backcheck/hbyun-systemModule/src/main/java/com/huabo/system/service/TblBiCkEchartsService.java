package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblBiCkEcharts;

public interface TblBiCkEchartsService {
//    List find(BigDecimal pageid);

    void delete(BigDecimal pageid);

    List<TblBiCkEcharts> getChartsPages(String toString);

}
