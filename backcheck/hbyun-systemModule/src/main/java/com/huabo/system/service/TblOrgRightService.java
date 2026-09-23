package com.huabo.system.service;



import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblOrgRight;
import com.huabo.system.entity.TblOrgRightnew;


public interface TblOrgRightService {


	TblOrgRightnew findByTblOrgRightorgname(String token, String staffId, String rightname);

	TblOrgRightnew findByTblOrgRightorgid(String token, String staffId, String toString);

	String findByTblOrgRightreturnname(String token, String staffId, TblManageRight vmr);

	void updateTblOrgRight(String token, String staffId, TblOrgRightnew orgright);

    void saveTblOrgRight(TblOrgRightnew orgright);

	void updateTblOrgRights(TblOrgRightnew orgright);

    void add(TblOrgRight right);
}
