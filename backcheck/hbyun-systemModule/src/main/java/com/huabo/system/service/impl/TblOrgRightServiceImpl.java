package com.huabo.system.service.impl;


import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblOrgRight;
import com.huabo.system.entity.TblOrgRightnew;
import com.huabo.system.mapper.TblOrgRightMapper;
import com.huabo.system.service.TblOrgRightService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TblOrgRightServiceImpl implements TblOrgRightService {

    @Resource
    private TblOrgRightMapper tblOrgRightMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public TblOrgRightnew findByTblOrgRightorgname(String token, String staffId, String rightname) {
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            List<TblOrgRightnew> list = tblOrgRightMapper.findByTblOrgRight(orgid, rightname);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TblOrgRightnew findByTblOrgRightorgid(String token, String staffId, String rightid) {
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            List<TblOrgRightnew> list = tblOrgRightMapper.findByTblOrgRightId(orgid, rightid);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String findByTblOrgRightreturnname(String token, String staffId, TblManageRight vmr) {
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            List<Object[]> list = tblOrgRightMapper.OBJlistBySql(orgid, vmr.getRightid().toString());
            if (list != null && list.size() > 0) {
                return list.get(0)[0] != null ? list.get(0)[0].toString() : null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateTblOrgRight(String token, String staffId, TblOrgRightnew orgright) {
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            orgright.setOrgid(orgid);
            tblOrgRightMapper.updateByOrgright(orgright);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTblOrgRight(TblOrgRightnew orgright) {
        this.tblOrgRightMapper.insert(orgright);
    }

    @Override
    public void updateTblOrgRights(TblOrgRightnew orgright) {
        this.tblOrgRightMapper.updateTblOrgRights(orgright);
    }

    @Override
    public void add(TblOrgRight right) {
        this.tblOrgRightMapper.insertOrgRight(right);
    }

}
