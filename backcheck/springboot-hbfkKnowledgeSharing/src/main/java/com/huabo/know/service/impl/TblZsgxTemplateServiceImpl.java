package com.huabo.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.know.entity.TblZsgxTemplate;
import com.huabo.know.entity.TblZsgxTemplateCategory;
import com.huabo.know.mapper.TblZsgxTemplateCategoryMapper;
import com.huabo.know.mapper.TblZsgxTemplateMapper;
import com.huabo.know.service.TblZsgxTemplateService;
import com.huabo.know.vo.param.TemplateListParam;
import com.huabo.know.vo.result.TemplateCategoryNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Slf4j
@Service
public class TblZsgxTemplateServiceImpl extends ServiceImpl<TblZsgxTemplateMapper, TblZsgxTemplate> implements TblZsgxTemplateService {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private TblZsgxTemplateMapper tblZsgxTemplateMapper;
    @Autowired
    private TblZsgxTemplateCategoryMapper tblZsgxTemplateCategoryMapper;


    @Override
    public JsonBean getTemplateCategoryTree() throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //查询全部模板类型
        QueryWrapper<TblZsgxTemplateCategory> queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETED",0);
        List<TblZsgxTemplateCategory> templateCategories = tblZsgxTemplateCategoryMapper.selectList(queryWrapper);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        if (CollectionUtils.isEmpty(templateCategories)) {
            resultMap.put("data", Collections.emptyList());
            return ResponseFormat.retParam(1, 200, resultMap);
        }

        //转类型
        List<TemplateCategoryNode> nodes = templateCategories.stream().map(source -> {
            TemplateCategoryNode categoryNode = new TemplateCategoryNode();
            BeanUtils.copyProperties(source, categoryNode);
            return categoryNode;
        }).sorted(Comparator.comparing(TemplateCategoryNode::getSort)).collect(Collectors.toList());

        List<TemplateCategoryNode> categoryNodeTree = this.buildCategoryTree(nodes);
        resultMap.put("data",categoryNodeTree);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    /**
     * 构建模板类型树
     * @param nodes
     * @return
     */
    private List<TemplateCategoryNode> buildCategoryTree(List<TemplateCategoryNode> nodes) {
        Map<String, TemplateCategoryNode> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TemplateCategoryNode::getId, Function.identity()));

        List<TemplateCategoryNode> rootNodes = new ArrayList<>();
        for (TemplateCategoryNode node : nodes) {
            TemplateCategoryNode parent = nodeMap.get(node.getPid());
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(node);
            } else {
                rootNodes.add(node);
            }
        }
        return rootNodes;
    }

    @Override
    public JsonBean getTemplateDetail(Integer templateNumber) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 根据number查询模板
        TblZsgxTemplate tblZsgxTemplate = this.getTemplateByNumber(templateNumber);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data",tblZsgxTemplate);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    /**
     * 根据number查询模板
     * @param number
     * @return
     */
    @Override
    public TblZsgxTemplate getTemplateByNumber(Integer number) throws Exception {
    	
        QueryWrapper<TblZsgxTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("TEMPLATE_NUMBER",number);
        return tblZsgxTemplateMapper.selectOne(queryWrapper);
    }

    @Override
    public List<TblZsgxTemplate> getTemplateByNumbers(List<Integer> numbers) {
        QueryWrapper<TblZsgxTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("TEMPLATE_NUMBER",numbers);
        return tblZsgxTemplateMapper.selectList(queryWrapper);
    }

    @Override
    public JsonBean list(TemplateListParam param) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 分页查询列表
        QueryWrapper<TblZsgxTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wp->wp.like("TEMPLATE_CATEGORY_IDS", param.getCategoryId()).or().like("TEMPLATE_CATEGORY_PARENTIDS", param.getCategoryId()));

        if (StringUtils.isNotBlank(param.getTitle())) {
            if (isNumeric(param.getTitle())) {
                //如果是数字，也根据number字段查询
                queryWrapper.and(wp->wp.eq("TEMPLATE_NUMBER",param.getTitle()).or().like("TITLE", param.getTitle()));
            } else {
                queryWrapper.like("TITLE", param.getTitle());
            }
        }
        if (param.getTemplateTypeId()!=null) {
            queryWrapper.eq("TEMPLATE_TYPE_ID",param.getTemplateTypeId());
        }
        if (StringUtils.isNotBlank(param.getVersionTag())) {
            queryWrapper.like("VERSION_TAG",param.getVersionTag());
        }

        IPage<TblZsgxTemplate> page = tblZsgxTemplateMapper.selectPage(new Page<>(param.getPageNumber(), param.getPageSize()), queryWrapper);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data",page);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
