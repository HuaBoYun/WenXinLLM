package com.huabo.cybermonitor.service.impl;



import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.RedisFinalUtis;
import com.huabo.cybermonitor.entity.Organization;


import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.mapper.OrganizationMapper;
import com.huabo.cybermonitor.mapper.TreeServiceMapper;
import com.huabo.cybermonitor.service.TreeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TreeServiceImpl  extends ServiceImpl<TreeServiceMapper, Tree> implements TreeService {

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    TreeServiceMapper  treeServiceMapper;

    @Override
    public List<Organization> findOrgTreeObjByHY() {
        return null;
    }

    @Override
    public List<Organization> findOrgTreeObjByHY(String orgid) {
        List<Organization> list = optionHYOrgTree(orgid);
        return list;
    }


    @Override
    public List<Tree> getTree(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try{
            if(jedis.exists(RedisFinalUtis.ORGDEPTLIST+nodeId)){
                String str = jedis.get(RedisFinalUtis.ORGDEPTLIST+nodeId);
                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            }else{
                List<Tree> trees = new ArrayList<Tree>();
                QueryWrapper qw = new QueryWrapper();
                qw.eq("fatherorgid",nodeId);
                qw.eq("status",0);
                qw.orderByAsc("orderid");
                List<Organization> list = organizationMapper.selectList(qw);
                for (Organization tblOrganization : list) {
                    Tree tree = new Tree();
                    tree.setName(tblOrganization.getOrgname());
                    tree.setId(tblOrganization.getOrgid());
                    tree.setpId(tblOrganization.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                    if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype().equals("0")) {
                        continue;
                    }
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().equals("1")) {
                        continue;
                    }
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().equals(0)) {
                        trees.add(tree);
                    }
                }
                return trees;

            }
        }finally{
            JedisUtil.returnResource(jedis);
        }
    }



    private List<Organization> optionHYOrgTree(String orgid) {
        //   List<Object[]> objs = organizationMapper.findOrganizationByOrgid(orgid);
        List<Organization>  list = new ArrayList<Organization>();
//        for (int i = 0; i < objs.size(); i++) {
//            Object[] obj = objs.get(i);
//            Organization org = new Organization();
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
//            org.setOrgtype(new BigDecimal(obj[7]!=null?Integer.parseInt(obj[7].toString()):0));
//            list.add(org);
//        }
        return list;
    }

    @Override
    public boolean save(Tree entity) {
        return super.save(entity);
    }



    @Override
    public Boolean isSJByOrgId(String userOrgid,String toOrgid) {



        Long num = treeServiceMapper.getORGANIZATIONCountByorgId(userOrgid);
        if (num == 0) {
            return false;
        }else{
            Organization org = belongToCompany(userOrgid);
            String sql1;
            String orgid=String.valueOf(org.getOrgid());

            Long num1 = treeServiceMapper.getORGANIZATIONCountByorgIdAndToOrgID(toOrgid,orgid);
            if(num1 != 0){return true;}else{return false;}
        }
    }

    @Override
    public Organization belongToCompany(String orgid){
        if (StringUtils.isNotEmpty(orgid)) {
            String sql;

            List<Organization> list = treeServiceMapper.getORGANIZATIONByOrgID(orgid);
            //List<Organization> list= toOrganizations(objs);
            if (list != null && list.size()>0) {
                return list.get(0);
            }
        }
        return new Organization();

    }

    private List<Organization> toOrganizations(List<Object[]> objs){
        if (objs!=null && objs.size()>0) {
            List<Organization> list = new ArrayList<Organization>();
            for (int i = 0; i < objs.size(); i++) {
                Object[] obj = objs.get(i);
                Organization org = new Organization();
                org.setOrgid(obj[0]!=null?new BigDecimal(obj[0].toString()):null);
                org.setOrgname(obj[1]!=null?obj[1].toString():"");
                org.setFatherorgid(obj[2]!=null?new BigDecimal(obj[2].toString()):null);
                org.setOrgnumber(obj[3]!=null?obj[3].toString():"");
                org.setOrgmeno(obj[4]!=null?obj[4].toString():"");
                org.setMemo(obj[5]!=null?obj[5].toString():"");
                org.setIcode(obj[6]!=null?obj[6].toString():"");
                org.setOrgtype(new BigDecimal(obj[7]!=null?Integer.parseInt(obj[7].toString()):0));
                org.setStatus(new BigDecimal(obj[8]!=null?Integer.parseInt(obj[8].toString()):null));
                org.setIszy(obj[9]!=null?obj[9].toString():"");
                list.add(org);
            }
            return list;
        }
        return null;
    }

}
