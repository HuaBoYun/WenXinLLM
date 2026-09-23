package com.huabo.compliance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.Tree;
import com.huabo.compliance.entity.TblAssesscategory;
import com.huabo.compliance.mapper.TblAssesscategoryMapper;
import com.huabo.compliance.service.TblAssesscategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("TblAssesscategoryService")
public class TblAssesscategoryServiceImpl implements TblAssesscategoryService {
    @Resource
    TblAssesscategoryMapper tblAssesscategoryMapper;

    @Override
    public Integer add(TblAssesscategory tblAssesscategory) {
        return tblAssesscategoryMapper.insert(tblAssesscategory);
    }


    @Override
    public void delete(String id) {
        tblAssesscategoryMapper.deleteEntity(new BigDecimal(id));
    }

    public TblAssesscategoryMapper getTblAssesscategoryDAO() {
        return tblAssesscategoryMapper;
    }

    public void setTblAssesscategoryDAO(TblAssesscategoryMapper tblAssesscategoryMapper) {
        this.tblAssesscategoryMapper = tblAssesscategoryMapper;
    }

    @Override
    public TblAssesscategory get(BigDecimal asscatid) {
        return tblAssesscategoryMapper.get(asscatid);
    }


    @Override
    public String GetTree(BigDecimal tmplId, String url) {
        List<Tree> list = new ArrayList<Tree>();
        List<TblAssesscategory> root = this.tblAssesscategoryMapper.getTreeRoot(tmplId);
        for (TblAssesscategory tblAssesscategory : root) {
            Tree tree = new Tree();
            tree.setName(tblAssesscategory.getCatname());
            tree.setId(tblAssesscategory.getAsscatid());
            tree.setTarget("mainFramex");
            //tree.setUrl(url+"&tmplId="+tmplId+"&nodeId="+tblAssesscategory.getAsscatid());
            List<Tree> childre = this.addChildren(tmplId, tblAssesscategory.getAsscatid(), url);
            if (childre.size() > 0) {
                tree.setChildren(childre);
            }
            list.add(tree);
        }
        return JSONObject.toJSONString(list);
    }

    @Override
    public String GetTree(BigDecimal tmplId, String url, String userid) {
        List<Tree> list = new ArrayList<Tree>();
        //List<TblAssesscategory> root = this.tblAssesscategoryDAO.getTreeRoot(tmplId);
        String sql = "select * from  TBL_ASSESSCATEGORY where ASSCATID in (  SELECT   DISTINCT AC.ASSCATID FROM tbl_AssessElement ae LEFT JOIN TBL_ASSELE_CATEGORY ac ON ae.ASSELEID = ac.ASSELEID";
        sql += " LEFT JOIN tbl_Assess_Mark am ON ae.ASSELEID = am.ASSELEID ";
        sql += " LEFT JOIN TBL_ASSESS_STAFF asf ON asf.assmarkid = am.assmarkid ";
        sql += " where asf.staffid=" + userid + " ) AND ASSTEMID= " + tmplId;
        List<TblAssesscategory> root = tblAssesscategoryMapper.listBySql(sql);
        for (TblAssesscategory tblAssesscategory : root) {
            Tree tree = new Tree();
            tree.setName(tblAssesscategory.getCatname());
            tree.setId(tblAssesscategory.getAsscatid());
            tree.setTarget("mainFramex");
            //	tree.setUrl(url+"&tmplId="+tmplId+"&nodeId="+tblAssesscategory.getAsscatid());
            List<Tree> childre = this.addChildren(tmplId, tblAssesscategory.getAsscatid(), url);
            if (childre.size() > 0) {
                tree.setChildren(childre);
            }
            list.add(tree);
        }
        return JSONObject.toJSONString(list);
    }


    public List<Tree> addChildren(BigDecimal tmplId, BigDecimal pId, String url) {
        List<Tree> list = new ArrayList<Tree>();
        List<TblAssesscategory> root = this.tblAssesscategoryMapper.getTreeByNodeId(pId);
        for (TblAssesscategory tblAssesscategory : root) {
            Tree tree = new Tree();
            tree.setName(tblAssesscategory.getCatname());
            tree.setId(tblAssesscategory.getAsscatid());
            tree.setTarget("mainFramex");
            //tree.setIsParent(true);
            //tree.setUrl(url+"&tmplId="+tmplId+"&nodeId="+tblAssesscategory.getAsscatid());
            /*List<Tree> trees = addChildren(tmplId, tblAssesscategory.getAsscatid(), url);
            if (trees.size() > 0) {
                tree.setChildren(trees);
            }*/
            list.add(tree);

        }
        return list;
    }


    @Override
    public List<TblAssesscategory> findByTempleId(BigDecimal tmplId) {
        return this.tblAssesscategoryMapper.findByTempleId(tmplId);
    }


    @Override
    public void addList(List<TblAssesscategory> tblAssesscategory) throws Exception{
        for (TblAssesscategory tblAssesscategory2 : tblAssesscategory) {
            this.tblAssesscategoryMapper.insertEntity(tblAssesscategory2);
        }
    }


    @Override
    public void updateList(List<TblAssesscategory> tblAssesscategory) throws Exception{
        for (TblAssesscategory tblAssesscategory2 : tblAssesscategory) {
            this.tblAssesscategoryMapper.updateEntity(tblAssesscategory2);
        }
    }


    @Override
    public void deleteListAndChildren(List<TblAssesscategory> tblAssesscategory) {
        for (TblAssesscategory tblAssesscategory2 : tblAssesscategory) {
            List<TblAssesscategory> assesscategories = this.tblAssesscategoryMapper.findAllAssesscategory(tblAssesscategory2.getAsscatid().toString());
            for (TblAssesscategory tblAssesscategory3 : assesscategories) {
                this.tblAssesscategoryMapper.deleteById(tblAssesscategory3);
            }
        }

    }


    @Override
    public void update(TblAssesscategory tblAssesscategory) throws Exception {
        this.tblAssesscategoryMapper.updateEntity(tblAssesscategory);
    }
    @Override
    public void insertEntity(TblAssesscategory tblAssesscategory) throws Exception {
        this.tblAssesscategoryMapper.insertEntity(tblAssesscategory);
    }


    @Override
    @Transactional
    public void deleteByTempleId(BigDecimal tmplId) {
        this.tblAssesscategoryMapper.deleteByTempleId(tmplId);
    }


    @Override
    public List<TblAssesscategory> getTreeRoot(BigDecimal tmplId) {
        return this.tblAssesscategoryMapper.getTreeRoot(tmplId);
    }


    @Override
    public List<TblAssesscategory> getParentList(BigDecimal id) {
        return this.tblAssesscategoryMapper.getParentList(id);
    }


    @Override
    public List<Object[]> getHengXiang(BigDecimal id) {
        return this.tblAssesscategoryMapper.getHengXiang(id);
    }


    @Override
    public List<TblAssesscategory> getTreeByNodeId(BigDecimal parentId) {
        return this.tblAssesscategoryMapper.getTreeByNodeId(parentId);
    }

}
