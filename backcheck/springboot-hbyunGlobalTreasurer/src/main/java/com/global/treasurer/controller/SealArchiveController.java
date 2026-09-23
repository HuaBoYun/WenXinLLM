package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TcSealArchive;
import com.global.treasurer.entity.TcSealType;
import com.global.treasurer.service.TblSealArchiveService;
import com.global.treasurer.service.TcSealTypeService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 印鉴档案管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/sealArchive/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/financial/basicConfig/sealArchive")
@Api(tags = "印鉴档案管理")
public class SealArchiveController {
    private static final Logger log = LoggerFactory.getLogger(SealArchiveController.class);

    @Autowired
    private TblSealArchiveService tblSealArchiveService;

    @Autowired(required = false)
    private TcSealTypeService tcSealTypeService;

    @Resource
    private UserProvider userProvider;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询印鉴档案列表")
    public String getList(@RequestBody(required = false) Map<String, Object> body,
                          @RequestParam(required = false) Map<String, Object> queryParams,
                          HttpServletResponse response) {
        try {
            // POST body 优先，GET query 兜底
            Map<String, Object> params = new HashMap<>();
            if (queryParams != null) params.putAll(queryParams);
            if (body != null) params.putAll(body);

            Integer pageNo = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                              (params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20);
            String sealCode = params.get("sealCode") != null ? params.get("sealCode").toString() : null;
            String sealName = params.get("sealName") != null ? params.get("sealName").toString() : null;
            String sealTypeId = params.get("sealTypeId") != null ? params.get("sealTypeId").toString() : null;
            String ownerName = params.get("ownerName") != null ? params.get("ownerName").toString() : null;
            String status = params.get("status") != null ? params.get("status").toString() : null;

            QueryWrapper<TcSealArchive> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(sealCode)) queryWrapper.like("SEAL_CODE", sealCode);
            if (StringUtils.hasText(sealName)) queryWrapper.like("SEAL_NAME", sealName);
            if (StringUtils.hasText(sealTypeId)) queryWrapper.eq("SEAL_TYPE_ID", sealTypeId);
            if (StringUtils.hasText(ownerName)) queryWrapper.like("OWNER_NAME", ownerName);
            if (StringUtils.hasText(status)) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            // 使用PageHelper分页，避免与MyBatis-Plus分页拦截器冲突
            com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);
            List<TcSealArchive> list = tblSealArchiveService.list(queryWrapper);
            com.github.pagehelper.PageInfo<TcSealArchive> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取印鉴档案列表失败", e);
            return JsonBean.error("获取印鉴档案列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    @ApiOperation("新增印鉴档案")
    public String add(@FlexibleRequestBody TcSealArchive sealArchive, HttpServletResponse response) {
        try {
            if (!StringUtils.hasText(sealArchive.getSealCode())) return JsonBean.error("印鉴编码不能为空");
            if (!StringUtils.hasText(sealArchive.getSealName())) return JsonBean.error("印鉴名称不能为空");
            // 生成主键
            sealArchive.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
            if (!StringUtils.hasText(sealArchive.getStatus())) sealArchive.setStatus("1");
            sealArchive.setCreateTime(new Date());
            sealArchive.setUpdateTime(new Date());
            boolean success = tblSealArchiveService.save(sealArchive);
            return success ? JsonBean.success("创建成功") : JsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建印鉴档案失败", e);
            return JsonBean.error("创建印鉴档案失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新印鉴档案")
    public String update(@FlexibleRequestBody TcSealArchive sealArchive, HttpServletResponse response) {
        try {
            if (sealArchive.getId() == null) return JsonBean.error("印鉴ID不能为空");
            sealArchive.setUpdateTime(new Date());
            boolean success = tblSealArchiveService.updateById(sealArchive);
            return success ? JsonBean.success("更新成功") : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新印鉴档案失败", e);
            return JsonBean.error("更新印鉴档案失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除印鉴档案")
    public String delete(@RequestParam String id, HttpServletResponse response) {
        try {
            if (!StringUtils.hasText(id)) return JsonBean.error("ID不能为空");
            boolean success = tblSealArchiveService.removeById(id);
            return success ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除印鉴档案失败", e);
            return JsonBean.error("删除印鉴档案失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取印鉴档案详情")
    public String getDetail(@RequestParam String id, HttpServletResponse response) {
        try {
            TcSealArchive sealArchive = tblSealArchiveService.getById(id);
            if (sealArchive == null) return JsonBean.error("印鉴档案不存在");
            return new JsonBean(1, "查询成功", sealArchive).toString();
        } catch (Exception e) {
            log.error("获取印鉴档案详情失败", e);
            return JsonBean.error("获取印鉴档案详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取印鉴档案统计")
    public String getStatistics(HttpServletResponse response) {
        try {
            long total = tblSealArchiveService.count();
            long active = tblSealArchiveService.count(new QueryWrapper<TcSealArchive>().eq("STATUS", "1"));
            long inactive = total - active;
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalSeals", total);
            statistics.put("activeSeals", active);
            statistics.put("lockedSeals", inactive);  // 停用的印鉴视为锁定
            statistics.put("todayUsage", 0);           // 暂无使用记录表，返回0
            return new JsonBean(1, "查询成功", statistics).toString();
        } catch (Exception e) {
            log.error("获取印鉴档案统计失败", e);
            return JsonBean.error("获取印鉴档案统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/types")
    @ApiOperation("获取印鉴类型选项（下拉框用）")
    public String getSealTypes(HttpServletResponse response) {
        try {
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            if (tcSealTypeService != null) {
                List<TcSealType> types = tcSealTypeService.list();
                for (TcSealType t : types) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", t.getId());
                    item.put("name", t.getTypeName());
                    result.add(item);
                }
            } else {
                // 兜底静态数据
                String[][] defaults = {{"1","公章"},{"2","财务章"},{"3","法人章"},{"4","合同章"},{"5","发票章"}};
                for (String[] d : defaults) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", d[0]);
                    item.put("name", d[1]);
                    result.add(item);
                }
            }
            return new JsonBean(1, "查询成功", result).toString();
        } catch (Exception e) {
            log.error("获取印鉴类型选项失败", e);
            return JsonBean.error("获取印鉴类型选项失败: " + e.getMessage());
        }
    }


    @GetMapping("/export")
    @ApiOperation("导出印鉴档案")
    public String export(@RequestParam(required = false) String sealCode,
                         @RequestParam(required = false) String sealName,
                         @RequestParam(required = false) String sealTypeId,
                         @RequestParam(required = false) String status,
                         HttpServletResponse response) {
        try {
            QueryWrapper<TcSealArchive> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(sealCode)) queryWrapper.like("SEAL_CODE", sealCode);
            if (StringUtils.hasText(sealName)) queryWrapper.like("SEAL_NAME", sealName);
            if (StringUtils.hasText(sealTypeId)) queryWrapper.eq("SEAL_TYPE_ID", sealTypeId);
            if (StringUtils.hasText(status)) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TcSealArchive> list = tblSealArchiveService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toString();
        } catch (Exception e) {
            log.error("导出印鉴档案失败", e);
            return JsonBean.error("导出印鉴档案失败: " + e.getMessage());
        }
    }
}

