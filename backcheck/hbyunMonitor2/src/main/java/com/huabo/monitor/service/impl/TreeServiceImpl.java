package com.huabo.monitor.service.impl;

import com.huabo.monitor.config.DateBaseConfig;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.mapper.TblOrganizationMapper;
import com.huabo.monitor.mysql.entity.TblOrganizationMySql;
import com.huabo.monitor.mysql.mapper.TblOrganizationMySqlMapper;
import com.huabo.monitor.service.TreeService;

import com.huabo.monitor.util.Tree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("treeServiceImpl")
@Slf4j
public class TreeServiceImpl implements TreeService {

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
    private TblOrganizationMySqlMapper tblOrganizationMySqlMapper;


    @Override
    public TblOrganization belongToCompany(String orgid) {
        if (StringUtils.isNotEmpty(orgid)) {

            //Object result = this.tblOrganizationMapper.excuteFunReturnUnique(orgid);

            List<TblOrganization> objs = tblOrganizationMapper.findBysqlObj(orgid);
            //  List<TblOrganization> list = this.toTblOrganizations((TblOrganization) objs);
            if (objs != null && objs.size() > 0) {
                return (TblOrganization) objs.get(0);
            }
        }

        return new TblOrganization();
    }

    @Override
    public TblOrganizationMySql belongToMySqlCompany(String orgid) {
        if (StringUtils.isNotEmpty(orgid)) {

            //Object result = this.tblOrganizationMapper.excuteFunReturnUnique(orgid);

            List<TblOrganizationMySql> objs = tblOrganizationMySqlMapper.findBysqlObj(orgid);
            //  List<TblOrganization> list = this.toTblOrganizations((TblOrganization) objs);
            if (objs != null && objs.size() > 0) {
                return (TblOrganizationMySql) objs.get(0);
            }
        }

        return new TblOrganizationMySql();
    }

    @Override
    public List<Tree> findAllCompanyByTree(BigDecimal orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
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
        } else {
            List<Tree> list = new ArrayList<>();
            List<TblOrganizationMySql> root = this.tblOrganizationMySqlMapper.getRoot(orgid);
            for (TblOrganizationMySql tblOrganization : root) {
                Tree tree = new Tree();
                tree.setId(tblOrganization.getOrgid());
                tree.setName(tblOrganization.getOrgname());
                tree.setOpen(true);
                if (this.tblOrganizationMySqlMapper.getChildCount(orgid) > 0) {
                    tree.setIsParent(true);
                    tree.setChildren(getMySqlChild(tblOrganization));
                } else {
                    tree.setIsParent(false);
                }
                list.add(tree);
            }
            return list;
        }
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

    public List<Tree> getMySqlChild(TblOrganizationMySql organizationMySql) {
        List<Tree> list = new ArrayList<>();
        List<TblOrganizationMySql> root = this.tblOrganizationMySqlMapper.getChild(organizationMySql.getOrgid());
        for (TblOrganizationMySql tblOrganization : root) {
            Tree tree = new Tree();
            tree.setId(tblOrganization.getOrgid());
            tree.setName(tblOrganization.getOrgname());
            tree.setOpen(true);
            if (this.tblOrganizationMySqlMapper.getChildCount(tblOrganization.getOrgid()) > 0) {
                tree.setIsParent(true);
                tree.setChildren(getMySqlChild(tblOrganization));
            } else {
                tree.setIsParent(false);
            }
            list.add(tree);
        }
        return list;
    }

    @Override
    public List<TblOrganization> findAllCompany(String orgid, Integer audittype) {
        TblOrganization o = this.belongToCompany(orgid);
        List<TblOrganization> list = new ArrayList();

        List<TblOrganization> objs = this.tblOrganizationMapper.findBysqlOrgid(orgid);
        //List<Object[]> objs = this.tblOrganizationMapper.findBysqlOrgid(orgid);
//        String demp = "┣";
//        int num = 1;
//        int num1 = 1;
//
//        for(int i = 0; i < objs.size(); ++i) {
//            //Object[] obj = (Object[])objs.get(i);
//            TblOrganization organization = objs.get(i);
//            if (Integer.parseInt(organization.get[0].toString()) > num1) {
//                ++num;
//                num1 = Integer.parseInt(obj[0].toString());
//            } else if (Integer.parseInt(obj[0].toString()) < num1) {
//                num1 = Integer.parseInt(obj[0].toString());
//                num = num1;
//            }
//
//            String str1 = "";
//
//            for(int j = 1; j < num; ++j) {
//                str1 = str1 + "　";
//            }
//
//            TblOrganization organ = new TblOrganization();
//            organ.setOrgid(new BigDecimal(obj[1].toString()));
//            organ.setOrgname(str1 + demp + obj[2].toString());
//            list.add(organ);
//            if ((audittype == null || audittype.equals("")) && obj[1].toString().equals(o.getOrgid().toString())) {
//                break;
//            }
//        }

        return objs;
    }

    @Override
    public List<TblOrganizationMySql> findAllMySqlCompany(String orgid, Integer audittype) {
        TblOrganization o = this.belongToCompany(orgid);
        List<TblOrganizationMySql> list = new ArrayList();

        List<TblOrganizationMySql> objs = this.tblOrganizationMySqlMapper.findBysqlOrgid(orgid);
        //List<Object[]> objs = this.tblOrganizationMapper.findBysqlOrgid(orgid);
//        String demp = "┣";
//        int num = 1;
//        int num1 = 1;
//
//        for(int i = 0; i < objs.size(); ++i) {
//            //Object[] obj = (Object[])objs.get(i);
//            TblOrganization organization = objs.get(i);
//            if (Integer.parseInt(organization.get[0].toString()) > num1) {
//                ++num;
//                num1 = Integer.parseInt(obj[0].toString());
//            } else if (Integer.parseInt(obj[0].toString()) < num1) {
//                num1 = Integer.parseInt(obj[0].toString());
//                num = num1;
//            }
//
//            String str1 = "";
//
//            for(int j = 1; j < num; ++j) {
//                str1 = str1 + "　";
//            }
//
//            TblOrganization organ = new TblOrganization();
//            organ.setOrgid(new BigDecimal(obj[1].toString()));
//            organ.setOrgname(str1 + demp + obj[2].toString());
//            list.add(organ);
//            if ((audittype == null || audittype.equals("")) && obj[1].toString().equals(o.getOrgid().toString())) {
//                break;
//            }
//        }

        return objs;
    }

    @Override
    public List<Tree> findAllCompanyALL(String orgid, Integer sjb) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
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
        } else {
            List<Tree> trees = new ArrayList<Tree>();
            //String sql = "select*  from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 start with  orgid=" + orgid + " connect by prior orgid = fatherorgid";
            List<Tree> children = new ArrayList<Tree>();
            List<TblOrganizationMySql> list = tblOrganizationMySqlMapper.findBysqlOrgids(orgid);
            for (TblOrganizationMySql tblOrganization : list) {
                if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                    continue;
                }
                //Set<TblOrganization> chil = tblOrganization.getChildren();
                Set<TblOrganizationMySql> chil = tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization.getOrgid());
                children = getMySqlNoteTreeGS(chil);
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

    private List<Tree> getMySqlNoteTreeGS(Set<TblOrganizationMySql> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (TblOrganizationMySql tblOrganization2 : chil) {

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
                List<TblOrganizationMySql> childGS = childMySqlGS(tblOrganization2.getOrgid());
                tree.setIsParent(childGS.size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }

    public List<TblOrganization> childGS(BigDecimal nodeId) {
        return tblOrganizationMapper.findByfather(nodeId);
    }

    public List<TblOrganizationMySql> childMySqlGS(BigDecimal nodeId) {
        return tblOrganizationMySqlMapper.findByfather(nodeId);
    }

    @Override
    public List<TblOrganization> findOrgTreeObjByHY(String orgid) {
        List<TblOrganization> list = optionHYOrgTree(orgid);
        return list;
    }

    @Override
    public List<TblOrganizationMySql> findMySqlOrgTreeObjByHY(String orgid) {
        List<TblOrganizationMySql> list = optionMySqlHYOrgTree(orgid);
        return list;
    }

    private List<TblOrganization> optionHYOrgTree(String orgid) {
            List<TblOrganization> list = tblOrganizationMapper.findByOrgis(orgid);
            //List<TblOrganization>  list = new ArrayList<TblOrganization>();
//        for (int i = 0; i < objs.size(); i++) {
//            Object[] obj = objs.get(i);
//            TblOrganization org = new TblOrganization();
//            org.setOrgid(obj[0]!=null?new BigDecimal(obj[0].toString()):null);
//            String str = "┣";
//            String str1 = "";
//            for (int j = 1; j < Integer.parseInt(obj[8].toString()); j++) {
//                str1 += "　";
//            }
//            org.setOrgname(obj[1]!=null?str1+str+obj[1].toString():"");
//            org.setFatherorgid(obj[2]!=null?new BigDecimal(obj[2].toString()):null);
//            org.setOrgnumber(obj[3]!=null?obj[3].toString():"");
//            org.setOrgmeno(obj[4]!=null?obj[4].toString():"");
//            org.setMemo(obj[5]!=null?obj[5].toString():"");
//            org.setIcode(obj[6]!=null?obj[6].toString():"");
//            org.setOrgtype(obj[7]!=null?Integer.parseInt(obj[7].toString()):0);
//            list.add(org);
//        }
            return list;
    }

    private List<TblOrganizationMySql> optionMySqlHYOrgTree(String orgid) {
            List<TblOrganizationMySql> list = tblOrganizationMySqlMapper.findByOrgis(orgid);
            //List<TblOrganization>  list = new ArrayList<TblOrganization>();
//        for (int i = 0; i < objs.size(); i++) {
//            Object[] obj = objs.get(i);
//            TblOrganization org = new TblOrganization();
//            org.setOrgid(obj[0]!=null?new BigDecimal(obj[0].toString()):null);
//            String str = "┣";
//            String str1 = "";
//            for (int j = 1; j < Integer.parseInt(obj[8].toString()); j++) {
//                str1 += "　";
//            }
//            org.setOrgname(obj[1]!=null?str1+str+obj[1].toString():"");
//            org.setFatherorgid(obj[2]!=null?new BigDecimal(obj[2].toString()):null);
//            org.setOrgnumber(obj[3]!=null?obj[3].toString():"");
//            org.setOrgmeno(obj[4]!=null?obj[4].toString():"");
//            org.setMemo(obj[5]!=null?obj[5].toString():"");
//            org.setIcode(obj[6]!=null?obj[6].toString():"");
//            org.setOrgtype(obj[7]!=null?Integer.parseInt(obj[7].toString()):0);
//            list.add(org);
//        }
            return list;
    }

//    @Override
//    public List<TblOrganization> findAllCompany(String orgid, Integer audittype) {
//
//        return null;
//    }

//    private List<TblOrganization> toTblOrganizations(TblOrganization objs) {
//        if (objs != null && objs.size() > 0) {
//            List<TblOrganization> list = new ArrayList();
//
//            for(int i = 0; i < objs.size(); ++i) {
//                Object[] obj = (Object[])objs.get(i);
//                TblOrganization org = new TblOrganization();
//                org.setOrgid(obj[0] != null ? new BigDecimal(obj[0].toString()) : null);
//                org.setOrgname(obj[1] != null ? obj[1].toString() : "");
//                org.setFatherorgid(obj[2] != null ? new BigDecimal(obj[2].toString()) : null);
//                org.setOrgnumber(obj[3] != null ? obj[3].toString() : "");
//                org.setOrgmeno(obj[4] != null ? obj[4].toString() : "");
//                org.setMemo(obj[5] != null ? obj[5].toString() : "");
//                org.setIcode(obj[6] != null ? obj[6].toString() : "");
//                org.setOrgtype(obj[7] != null ? Integer.parseInt(obj[7].toString()) : 0);
//                org.setStatus(obj[8] != null ? Integer.parseInt(obj[8].toString()) : null);
//                org.setIszy(obj[9] != null ? obj[9].toString() : "");
//                list.add(org);
//            }
//
//            return list;
//        } else {
//            return null;
//        }
//    }


}
