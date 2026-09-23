package com.huabo.system.service;

import com.huabo.system.entity.TblMyTask;
import com.huabo.system.entity.TblStaff;

import java.util.List;

public interface QyWeiXinService {

	int sendMessage(String val);

	int sendMessage(TblMyTask tblMyTask);

	int sendMessage(List<TblMyTask> tasks);

	TblStaff getNextStaff(String val, String companyId);

	String getLocalSystemToken(String userName);
}
