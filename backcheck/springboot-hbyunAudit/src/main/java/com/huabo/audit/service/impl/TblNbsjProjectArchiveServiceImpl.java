package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huabo.audit.oracle.entity.ArchiveMenuList;
import com.huabo.audit.oracle.entity.TblNbsjArchiveEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.service.TblNbsjProjectArchiveService;
@Service
public class TblNbsjProjectArchiveServiceImpl implements TblNbsjProjectArchiveService {

	@Autowired
    private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Override
	public List<ArchiveMenuList> getArchiveMenuList() {
		return ArchiveMenuList.getArchiveMenuList();
	}

	@Override
	public void save(TblNbsjArchiveEntity objTblNbsjArchive) {
		try {
			tblNbsjProjectMapper.insertEntityar(objTblNbsjArchive);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<TblNbsjArchiveEntity> getTblNbsjArchiveList(TblNbsjProject objTblnbsjProject) {
		
		BigDecimal projectid = objTblnbsjProject.getProjectId();
		
		return tblNbsjProjectMapper.getTblNbsjArchiveList(projectid);
	}

}
