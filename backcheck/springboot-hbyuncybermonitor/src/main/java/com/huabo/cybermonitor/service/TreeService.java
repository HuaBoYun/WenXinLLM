package com.huabo.cybermonitor.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Tree;


public interface TreeService extends IService<Tree> {

    public List<Organization> findOrgTreeObjByHY();

    List<Organization> findOrgTreeObjByHY(String orgid);

    Organization belongToCompany(String orgid);

    /**
     * 获得树的操作 规则tree
     * @param nodeId
     * @return
     */
    List<Tree> getTree(BigDecimal nodeId);

    /**
     * @author tyb
     * @date 2016-1-18 下午5:10:09
     * 查看是否是有审计部
     * userOrgid:用户所属orgid
     * toOrgid:当前点击组织机构
     */
    public Boolean isSJByOrgId(String userOrgid,String toOrgid);

}
