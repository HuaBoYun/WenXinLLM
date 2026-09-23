package com.huabo.system.service;


import java.util.List;

import com.huabo.system.entity.TblVersion;

/**
 * Created by mangel on 2015/3/14.
 */

public interface TblVersionService {

	TblVersion findbyFid(String fid);

	List<TblVersion> selectAllTblVersion();

}
