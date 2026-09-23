package com.global.treasurer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblBondCategory;
import com.global.treasurer.mapper.TblBondCategoryMapper;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债券类别管理Controller（全球司库模块）
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@RestController
@RequestMapping("/financing/bond-category")
@Api(tags = "债券类别管理")
public class BondCategoryController {

    private static final Logger log = LoggerFactory.getLogger(BondCategoryController.class);

    @Autowired
    private TblBondCategoryMapper tblBondCategoryMapper;

    @PostMapping("/page")
    @ApiOperation("分页查询债券类别列表")
    public String getBondCategoryPage(@RequestParam Map<String, Object> params) {
        try {
            log.info("分页查询债券类别列表, params: {}", params);

            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String categoryName = (String) params.get("categoryName");
            String categoryCode = (String) params.get("categoryCode");
            String status = params.get("status") != null ? params.get("status").toString() : null;

            // 使用MyBatis-Plus的分页查询
            PageHelper.startPage(pageNum, pageSize);
            List<TblBondCategory> list = tblBondCategoryMapper.selectByCondition(categoryName, categoryCode, status);
            PageInfo<TblBondCategory> pageInfo = new PageInfo<>(list);

            // 构建前端期望的响应格式
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());

            // 将数据库的STATUS("1"/"0")转换为前端的status("1"/"0")
            List<TblBondCategory> rows = (List<TblBondCategory>) data.get("rows");
            for (TblBondCategory category : rows) {
                if (category.getStatus() != null) {
                    category.setStatus(category.getStatus().equals("1") ? "1" : "0");
                }
            }

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("分页查询债券类别列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询债券类别详情")
    public String getBondCategoryDetail(@PathVariable Long id) {
        try {
            log.info("查询债券类别详情, id: {}", id);
            TblBondCategory category = tblBondCategoryMapper.selectById(id);

            if (category != null && category.getStatus() != null) {
                category.setStatus(category.getStatus().equals("1") ? "1" : "0");
            }

            return new JsonBean(1, "查询成功", category).toString();
        } catch (Exception e) {
            log.error("查询债券类别详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/add")
    @ApiOperation("新增债券类别")
    public String addBondCategory(@FlexibleRequestBody TblBondCategory category) {
        try {
            log.info("新增债券类别, category: {}", category);

            // 设置默认值
            Date now = new Date();
            category.setCreatedTime(now);
            category.setUpdatedTime(now);

            // 设置默认值
            if (category.getParentId() == null) {
                category.setParentId(0L); // 默认父节点为0
            }
            if (category.getCategoryLevel() == null) {
                category.setCategoryLevel(1); // 默认一级
            }
            if (category.getSortOrder() == null) {
                category.setSortOrder(0); // 默认排序
            }
            if (category.getDeleteFlag() == null) {
                category.setDeleteFlag(0); // 默认未删除
            }

            // 设置默认状态
            if (category.getStatus() == null) {
                category.setStatus("1"); // 默认启用
            }

            tblBondCategoryMapper.insert(category);

            log.info("新增成功，ID: {}", category.getCategoryId());
            return new JsonBean(1, "新增成功", category).toString();
        } catch (Exception e) {
            log.error("新增债券类别失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新债券类别")
    public String updateBondCategory(@FlexibleRequestBody TblBondCategory category) {
        try {
            log.info("更新债券类别, category: {}", category);

            category.setUpdatedTime(new Date());
            tblBondCategoryMapper.updateById(category);

            return new JsonBean(1, "更新成功", category).toString();
        } catch (Exception e) {
            log.error("更新债券类别失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除债券类别")
    public String deleteBondCategory(@PathVariable Long id) {
        try {
            log.info("删除债券类别, id: {}", id);
            tblBondCategoryMapper.deleteById(id);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除债券类别失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/toggle-status")
    @ApiOperation("切换债券类别状态")
    public String toggleBondCategoryStatus(@RequestParam Map<String, Object> params) {
        try {
            Long id = Long.parseLong(params.get("id").toString());
            String status = params.get("status").toString();
            log.info("切换债券类别状态, id: {}, status: {}", id, status);

            TblBondCategory category = tblBondCategoryMapper.selectById(id);
            if (category != null) {
                category.setStatus(status);
                category.setUpdatedTime(new Date());
                tblBondCategoryMapper.updateById(category);
            }

            return new JsonBean(1, "状态切换成功").toString();
        } catch (Exception e) {
            log.error("切换债券类别状态失败", e);
            return new JsonBean(0, "状态切换失败: " + e.getMessage()).toString();
        }
    }
}
