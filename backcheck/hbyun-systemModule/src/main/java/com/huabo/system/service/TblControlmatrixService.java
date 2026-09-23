package com.huabo.system.service;

import com.huabo.system.entity.TblControlmatrix;

public interface TblControlmatrixService {

    TblControlmatrix getControlmatrix(String tcmId);

    void modify(TblControlmatrix var1);

    void updateMatrix(TblControlmatrix com);

	void insertMatrix(TblControlmatrix com);
}
