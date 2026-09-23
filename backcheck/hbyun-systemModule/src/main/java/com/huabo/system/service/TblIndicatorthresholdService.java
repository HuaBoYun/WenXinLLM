package com.huabo.system.service;

import com.huabo.system.entity.TblIndicatorthreshold;

import java.util.List;

public interface TblIndicatorthresholdService {
    List<TblIndicatorthreshold> findByIndicatorId(String thresholdid);
}
