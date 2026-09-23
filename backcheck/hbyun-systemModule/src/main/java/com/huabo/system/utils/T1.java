package com.huabo.system.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.huabo.system.entity.TblOrganization;

import cn.hutool.extra.pinyin.PinyinUtil;

public class T1 {

	public static void main(String[] args) {
		TblOrganization rootOrg = new TblOrganization();
		rootOrg.setOrgid(BigDecimal.valueOf(1000));
		rootOrg.setOrgname("国资委");
		rootOrg.setOrgtype(1);
		rootOrg.setOrderid(1);
		rootOrg.setOrgTreeNames("1,1000");
		rootOrg.setFatherorgid(BigDecimal.valueOf(1));
		List<TblOrganization> childrenList = new ArrayList<TblOrganization>(0);
		
		TblOrganization chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1001));
		chilOrg.setOrgname("国资委1");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(1);
		chilOrg.setOrgTreeNames("1,1000,1001");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1000));
		childrenList.add(chilOrg);
		
		chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1002));
		chilOrg.setOrgname("国资委2");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(2);
		chilOrg.setOrgTreeNames("1,1000,1002");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1000));
		childrenList.add(chilOrg);
		
		chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1003));
		chilOrg.setOrgname("国资委3");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(3);
		chilOrg.setOrgTreeNames("1,1000,1003");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1000));
		childrenList.add(chilOrg);
		
		
		chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1004));
		chilOrg.setOrgname("国资委4");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(4);
		chilOrg.setOrgTreeNames("1,1000,1004");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1000));
		childrenList.add(chilOrg);
		
		
		chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1005));
		chilOrg.setOrgname("国资委11");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(1);
		chilOrg.setOrgTreeNames("1,1000,1001,1005");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1001));
		childrenList.add(chilOrg);
		
		chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1006));
		chilOrg.setOrgname("国资委12");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(2);
		chilOrg.setOrgTreeNames("1,1000,1001,1006");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1001));
		childrenList.add(chilOrg);
		
		chilOrg = new TblOrganization();
		chilOrg.setOrgid(BigDecimal.valueOf(1007));
		chilOrg.setOrgname("国资委111");
		chilOrg.setOrgtype(0);
		chilOrg.setOrderid(1);
		chilOrg.setOrgTreeNames("1,1000,1001,1005,1007");
		chilOrg.setFatherorgid(BigDecimal.valueOf(1005));
		childrenList.add(chilOrg);
	}

}
