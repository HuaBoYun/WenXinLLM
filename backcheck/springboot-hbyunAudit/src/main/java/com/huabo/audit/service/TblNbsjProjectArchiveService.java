package com.huabo.audit.service;

import java.util.List;

import com.huabo.audit.oracle.entity.ArchiveMenuList;
import com.huabo.audit.oracle.entity.TblNbsjArchiveEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;

public interface TblNbsjProjectArchiveService {
	public List<ArchiveMenuList> getArchiveMenuList();
	
	public void save(TblNbsjArchiveEntity objTblNbsjArchive);
	
	public List<TblNbsjArchiveEntity> getTblNbsjArchiveList(TblNbsjProject objTblnbsjProject);
}
