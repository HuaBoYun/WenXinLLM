package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblOrganization;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.service.TreeService;
import com.huabo.system.utils.Tree;

import lombok.extern.slf4j.Slf4j;

@Service("treeServiceImpl")
@Slf4j
public class TreeServiceImpl implements TreeService {

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Override
    public TblOrganization belongToCompany(String orgid) {
        if (StringUtils.isNotEmpty(orgid)) {
            List<TblOrganization> objs = tblOrganizationMapper.findBysqlObj(orgid);
            if (objs != null && objs.size() > 0) {
                return (TblOrganization) objs.get(0);
            }
        }
        return new TblOrganization();
    }

    @Override
    public List<Tree> findAllCompanyByTree(BigDecimal orgid) {
            List<Tree> list = new ArrayList<>();
            List<TblOrganization> root = this.tblOrganizationMapper.getRoot(orgid);
            for (TblOrganization tblOrganization : root) {
                Tree tree = new Tree();
                tree.setId(tblOrganization.getOrgid());
                tree.setName(tblOrganization.getOrgname());
                tree.setOpen(true);
                if (this.tblOrganizationMapper.getChildCount(orgid) > 0) {
                    tree.setIsParent(true);
                    tree.setChildren(getChild(tblOrganization));
                } else {
                    tree.setIsParent(false);
                }
                list.add(tree);
            }
            return list;
    }

    public List<Tree> getChild(TblOrganization organization) {
        List<Tree> list = new ArrayList<>();
        List<TblOrganization> root = this.tblOrganizationMapper.getChild(organization.getOrgid());
        for (TblOrganization tblOrganization : root) {
            Tree tree = new Tree();
            tree.setId(tblOrganization.getOrgid());
            tree.setName(tblOrganization.getOrgname());
            tree.setOpen(true);
            if (this.tblOrganizationMapper.getChildCount(tblOrganization.getOrgid()) > 0) {
                tree.setIsParent(true);
                tree.setChildren(getChild(tblOrganization));
            } else {
                tree.setIsParent(false);
            }
            list.add(tree);
        }
        return list;
    }

    @Override
    public List<Tree> findAllCompanyALL(String orgid, Integer sjb) {
            List<Tree> trees = new ArrayList<Tree>();
            //String sql = "select*  from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 start with  orgid=" + orgid + " connect by prior orgid = fatherorgid";
            List<Tree> children = new ArrayList<Tree>();
            List<TblOrganization> list = tblOrganizationMapper.findBysqlOrgids(orgid);
            for (TblOrganization tblOrganization : list) {
                if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                    continue;
                }
                //Set<TblOrganization> chil = tblOrganization.getChildren();
                Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                children = getNoteTreeGS(chil);
                Tree tree = new Tree();
                tree.setChildren(children);
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil.size() > 0 ? true : false);
                trees.add(tree);
            }
            return trees;
    }

    private List<Tree> getNoteTreeGS(Set<TblOrganization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (TblOrganization tblOrganization2 : chil) {

            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 1) {
                continue;
            }
            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() == 0 && tblOrganization2.getOrgtype() < 100) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0 && tblOrganization2.getOrgtype() > 0) {
                Tree tree = new Tree();
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                List<TblOrganization> childGS = childGS(tblOrganization2.getOrgid());
                tree.setIsParent(childGS.size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }


    public List<TblOrganization> childGS(BigDecimal nodeId) {
        return tblOrganizationMapper.findByfather(nodeId);
    }

	@Override
	public List<TblOrganization> findAllCompany(String orgid, Integer audittype) {
		// TODO Auto-generated method stub
		return null;
	}

}
