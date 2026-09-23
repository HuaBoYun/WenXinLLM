package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.mapper.RiskcategoryMapper;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskcategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

import java.util.LinkedList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Slf4j
@Service
public class RiskcategoryServiceImpl extends ServiceImpl<RiskcategoryMapper, Riskcategory> implements IRiskcategoryService {

    @Autowired
    private RiskcategoryMapper riskcategoryMapper;
    @Autowired
    private IOrganizationService organizationService;


    @Override
    public Riskcategory findQYFXByOrgid(String orgid, String moduleType) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("unit", orgid);
        if (StringUtils.isNotBlank(moduleType)) {
            queryWrapper.eq("MODULETYPE", moduleType);
        }
        queryWrapper.orderByAsc("riskcatid");

        List<Riskcategory> list = baseMapper.findQYFXByOrgid(queryWrapper);
        if (list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void deleteRiskcategory(Riskcategory riskcategory) {
        riskcategoryMapper.deleteById(riskcategory.getRiskcatid());
    }

    @Override
    public List<BigDecimal> findRiskcatidByChildNode(Riskcategory riskcategory) {
        return riskcategoryMapper.selectChildCatIds(riskcategory.getRiskcatid().toString());
    }

    @Override
    public boolean findTblRiskOrganBynumber(String number, String org) {
        Boolean bo = false;
        if (riskcategoryMapper.selectRiskOrganBynumber(number,org)>0) {
            bo = true;
        }
        return bo;
    }

    @Override
    public boolean findRiskOrganByName(String name, String unit, String moduletype) {
        Boolean bo = false;
        QueryWrapper queryWrapper=new QueryWrapper();
        if (StringUtils.isNotBlank(moduletype)) {
            queryWrapper.eq("MODULETYPE", moduletype);
        }
        Integer integer = riskcategoryMapper.selectRiskOrganByName(name,unit,queryWrapper);
        if (integer>0){
            return true;
        }
        return false;
    }

    @Override
    public JsonBean riskQueryLeft(String orgid, TblOrganizationUtil organization, String treeName) {
        if (StringUtils.isEmpty(orgid)) {
            orgid = organization.getOrgid().toString();
        }
        final Organization organization1 = organizationService.getById(orgid);
        if (organization1!=null){
            orgid=organization1.getOrgid().toString();
            treeName=organization1.getOrgname();
        }
        final List<Riskcategory> tree = this.getRiskCateTreeByOrgId(orgid, "FXSJK", null);
        Map<String,Object> map=new HashMap<>();
        map.put("treeName",treeName);
        map.put("tree",tree);
        map.put("targetFrame","mainFramex");
        return new JsonBean(1,"操作成功",map);
    }


    @Override
    public List<Riskcategory> getRiskCateTreeByOrgId(String orgid, String moduletype, String riskcatName) {
        // 初始化风险类别名称列表
//        String[] typeList = new String[] { "企业风险", "业务风险", "专项风险" };
//        List<String> catNameList = new ArrayList<>(Arrays.asList(typeList));

        List<String> catNameList = new ArrayList<>();

        /*
         * 查询出来所有的风险分类的语句 riskcategory
         * */
        QueryWrapper<Riskcategory>  queryWrapper=new QueryWrapper<>();
        System.out.println("orgid=="+orgid);
        // 设置查询条件：单位ID和模块类型
        queryWrapper.eq("unit",orgid).eq("moduletype",moduletype);
        // 如果提供了风险类别名称，从列表中移除并排除
        if (StringUtils.isNotBlank(riskcatName)) {
            catNameList.remove(riskcatName);
            queryWrapper.notIn("RISKCATNAME", catNameList);
        }
        // 按风险类别ID升序排序
        queryWrapper.orderByAsc("riskcatid");
        //获取风险类别列表
        List<Riskcategory> list = this.list(queryWrapper);
        if (list==null||list.size()<=0){
            return new LinkedList<>();
        }
        // 遍历风险类别列表，根据类别名称设置是否删除标志
        for (Riskcategory cat:list) {
            switch (cat.getRiskcatname()) {
//                case "企业风险": cat.setIsdel(0); break;
//                case "业务风险": cat.setIsdel(0); break;
//                case "专项风险": cat.setIsdel(0); break;
                case "风险类型": cat.setIsdel(2); break;
                default: cat.setIsdel(1);
            }
        }
        return list.stream().filter(item -> item.getRiskcatname() != null && item.getRiskcatname().trim().equals("风险类型")).
                peek(item1 -> item1.setChildren(getChildrenCategory(item1, list))).collect(Collectors.toList());
    }

    private List<Riskcategory> getChildrenCategory(Riskcategory root, List<Riskcategory> list) {
        if (list==null||list.size()<=0){
            return new LinkedList<>();
        }
        return list.stream().filter((item) -> item.getFatherriskcatid()!=null&&item.getFatherriskcatid().equals(root.getRiskcatid())).
                peek(categoryEntity -> categoryEntity.setChildren(getChildrenCategory(categoryEntity, list))).collect(Collectors.toList());
    }

    public String findRiskcatByName(Riskcategory riskcat) {
        if (riskcat == null || riskcat.getRiskcatname() == null) {
            return "";
        }
        if(riskcat.getRiskcatname().contains("企业风险")){
            return "企业风险";
        }else if(riskcat.getRiskcatname().contains("专项风险")) {
            return "专项风险";
        }else if(riskcat.getRiskcatname().contains("业务风险")) {
            return "业务风险";
        }
        riskcat = getById(riskcat.getFatherriskcatid());
        return riskcat==null ? null : riskcat.getRiskcatname();

    }
//    @Override
//    public List<Riskcategory> getRiskCateTreeByOrgId(String orgid, String moduletype, String riskcatName) {
//
//        /*
//         * 查询出来所有的风险分类的语句 riskcategory
//         * */
//        QueryWrapper<Riskcategory>  queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("unit",orgid).eq("moduletype",moduletype).orderByAsc("riskcatid");
//        List<Riskcategory> list = this.list(queryWrapper);
//        if (list==null||list.size()<=0){
//            return new LinkedList<>();
//        }
//        return list.stream().filter(item -> item.getRiskcatname()!=null&&item.getRiskcatname().trim().equals("风险类型")).
//                peek(item1 -> item1.setChildren(getChildrenCateGrory(item1, list))).collect(Collectors.toList());
//    }
//    /*private List<Riskcategory> getChildrenCateGrory(Riskcategory root, List<Riskcategory> list) {
//        if (list==null||list.size()<=0){
//            return new LinkedList<>();
//        }
//        return list.stream().filter((item) -> item.getFatherriskcatid()!=null&&item.getFatherriskcatid().equals(root.getRiskcatid())).
//                peek(categoryEntity -> categoryEntity.setChildren(getChildrenCateGrory(categoryEntity, list))).collect(Collectors.toList());
//    }*/

    @Override
    public Riskcategory geTblRiskcategory(BigDecimal riskcatId) {

        Riskcategory cat = null;
        try {
            cat=riskcategoryMapper.selectById(riskcatId);
            //cat = riskcategoryMapper.findById(riskcatId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cat;
    }

    @Override
    public List<Riskcategory> findRiskCateByRoot(BigDecimal rootId) {
        //select * from tbl_RiskCategory a start with a.Riskcatid  = #{rootId} connect by prior a.Riskcatid = a.fatherriskcatid
//        return riskcategoryMapper.findRiskCateByRoot(rootId);
        List<Riskcategory> selectList = riskcategoryMapper.selectList(new QueryWrapper<>());
        List<Riskcategory> res = new ArrayList<>();
        List<Riskcategory> list = findRiskCateByRoot2(rootId, selectList, res);
        list.addAll(selectList.stream().filter(o -> rootId.equals(o.getRiskcatid())).collect(Collectors.toList()));
        return list;
    }

    private List<Riskcategory> findRiskCateByRoot2(BigDecimal rootId, List<Riskcategory> list, List<Riskcategory> res) {
        List<Riskcategory> collect = list.stream().filter(o -> rootId.equals(o.getFatherriskcatid())).collect(Collectors.toList());
        res.addAll(collect);
        for (Riskcategory item: collect) {
            findRiskCateByRoot2(item.getRiskcatid(), list, res);
        }
        return res;

    }

    private List<Riskcategory> findRiskCateByFatherId(BigDecimal riskcatid) {
        List<Riskcategory> res = new ArrayList<>();
        QueryWrapper<Riskcategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fatherriskcatid", riskcatid);
        List<Riskcategory> list = riskcategoryMapper.selectList(queryWrapper);
        for (Riskcategory it: list) {
            findRiskCateByFatherId(it.getRiskcatid());
        }
        return res;
    }

    @Override
    public String[] findRiskCateParentByAssPanid(String asspanid) {
        List<Riskcategory> list = riskcategoryMapper.findRiskCateParentByAssPanid(asspanid);
        String str = "";
        StringBuilder catids = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            Riskcategory cat = list.get(i);
            int isdel = 1;
            if (cat.getRiskcatname()!= null) {
                if ( cat.getRiskcatname().trim().equals("企业风险") || cat.getRiskcatname().trim().equals("业务风险") || cat.getRiskcatname().trim().equals("专项风险")) {
                    isdel = 0;
                }else if (cat.getRiskcatname().trim().equals("风险类型")) {
                    isdel = 2;
                }
            }
            str += "tree.nodes['"+ (i==0?-1:cat.getFatherriskcatid())+"_"+cat.getRiskcatid()+"']=\"text:"+cat.getRiskcatname()+";method:check("+cat.getRiskcatid()+","+(cat.getUnit()==null?-1:cat.getUnit())+","+(cat.getIsleaf()==null?0:cat.getIsleaf())+","+isdel+")\";\n";
            catids.append(cat.getRiskcatid());
            if (i < list.size()-1)
                catids.append(",");
        }
        return new String[]{str, catids.toString()};
    }

//    /*
//     * @author zuoshun
//     * @version v1.0.1
//     * @Description 通过点击分类节点可以查询其所有子节点的Id
//     * @Date 2022/8/2
//     * @param riskcatid
//     * @return java.util.List<java.math.BigDecimal>
//     * @url:
//     **/
//    @Override
//    public List<BigDecimal> findRiskcatidByChildNode(String riskcatid) {
//        List<BigDecimal> riskcatidList = baseMapper.selectChildCatIds(riskcatid);
//        riskcatidList.add(new BigDecimal(riskcatid));
//        return riskcatidList;
////        List<Riskcategory> list =this.list();
////        if (list==null||list.size()<=0){ //TODO
////            return null;
////        }
////        /*借用前面的递归查询子节点的方法*/
////        Riskcategory riskcategory = new Riskcategory();
////        riskcategory.setRiskcatid(new BigDecimal(riskcatid));
////        return getChildrenCateGrory (riskcategory, list).stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
//    }



    @Override
    public  List<BigDecimal> findRiskcatidsByChildNode1(String riskcatid) {
//      by 多数据融合改造 start
        List<Riskcategory> selectList = riskcategoryMapper.selectList(new QueryWrapper<>());
        List<Riskcategory> res = new ArrayList<>();
        BigDecimal rootId = new BigDecimal(riskcatid);
        List<Riskcategory> childNode = findRiskCateByRoot2(rootId, selectList, res);
        childNode.addAll(selectList.stream().filter(o -> rootId.equals(o.getRiskcatid())).collect(Collectors.toList()));
        List<BigDecimal> list = childNode.stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
        //获得子字节id
//        List<BigDecimal> list = riskcategoryMapper.selectChildCatIds(riskcatid);
//        by 多数据融合改造 end

//        log.info("{}",list);
//
//        String str = "";
//        //遍历list
//        for (int i = 0; i < list.size(); i++) {
//            str += list.get(i) + ",";
//        }
//        return str.equals("") ? str : str.substring(0, str.length() - 1);
        return list;
    }

    @Transactional
    @Override
    public void initRiskCategory(String orgid, String type) {
        String[] typeList = new String[] { "企业风险", "业务风险", "专项风险" };
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("riskcatname", typeList);
        queryWrapper.eq("UNIT", orgid);
        if (StringUtils.isNotEmpty(type)) {
            queryWrapper.eq("moduletype", type);
        }

        Riskcategory cat = null;
        Riskcategory cat1 = null;
        if (this.count(queryWrapper) == 0) {
        	cat = new Riskcategory();
            cat.setRiskstatus("0");
            cat.setRiskcatnumber("1");
            cat.setRiskcatname("风险类型");
            cat.setRiskcatdes("初始化");
            cat.setFatherriskcatid(new BigDecimal("0"));
            cat.setUnit(orgid);
            cat.setModuletype(type);
            this.save(cat);//保存风险类别
            BigDecimal riskcatid = cat.getRiskcatid();
            for (int i = 0; i < typeList.length; i++) {
            	cat1 = new Riskcategory();
                cat1.setRiskstatus("0");
                cat1.setRiskcatnumber("2");
                cat1.setRiskcatname(typeList[i]);
                cat1.setRiskcatdes("初始化");
                cat1.setFatherriskcatid(riskcatid);
                cat1.setUnit(orgid);
                cat1.setModuletype(type);
                this.save(cat1);//保存风险类别
            }
        }
    }


}
