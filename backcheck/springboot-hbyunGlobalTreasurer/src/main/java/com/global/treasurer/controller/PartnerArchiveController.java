package com.global.treasurer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcPartnerArchive;
import com.global.treasurer.service.TcPartnerArchiveService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 合作伙伴档案管理控制器
 * 
 * @author AI Assistant
 * @date 2025-01-26
 */
@RestController
@RequestMapping("/settlement/partner-archive")
@Api(tags = "合作伙伴档案管理")
public class PartnerArchiveController {
    @Resource
    private TcPartnerArchiveService partnerArchiveService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询合作伙伴档案")
    public String getPage(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            PageInfo<TcPartnerArchive> pageInfo = partnerArchiveService.list(pageNum, pageSize, params);

            // 转换列表数据，添加前端需要的字段
            List<Map<String, Object>> convertedList = new ArrayList<>();
            for (TcPartnerArchive archive : pageInfo.getList()) {
                // 使用 FastJSON 将对象转为 Map
                String jsonStr = JSON.toJSONString(archive);
                Map<String, Object> item = JSON.parseObject(jsonStr, Map.class);
                // 添加前端字段名
                item.put("partnerNameEng", archive.getPartnerEnName());
                item.put("partnerStatus", archive.getCooperationStatus());
                convertedList.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", convertedList);
            result.put("totalRecord", pageInfo.getTotal());

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("获取合作伙伴档案详情")
    public String getDetail(@PathVariable String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TcPartnerArchive archive = partnerArchiveService.getById(id);
            if (archive == null) {
                return new JsonBean(0, "档案不存在", null).toJson();
            }

            // 调试日志：查看原始日期值
            System.out.println("=== 获取合作伙伴档案详情 ===");
            System.out.println("ID: " + archive.getId());
            System.out.println("成立日期原始值: " + archive.getEstablishmentDate());
            System.out.println("合作开始日期原始值: " + archive.getCooperationStartDate());

            // 转换为 Map 并添加前端需要的字段
            String jsonStr = JSON.toJSONString(archive);
            Map<String, Object> result = JSON.parseObject(jsonStr, Map.class);
            result.put("partnerNameEng", archive.getPartnerEnName());
            result.put("partnerStatus", archive.getCooperationStatus());

            // 格式化日期字段为 yyyy-MM-dd 格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (archive.getEstablishmentDate() != null) {
                String formattedDate = dateFormat.format(archive.getEstablishmentDate());
                result.put("establishmentDate", formattedDate);
                System.out.println("成立日期格式化后: " + formattedDate);
            } else {
                System.out.println("成立日期为空");
            }
            if (archive.getCooperationStartDate() != null) {
                String formattedDate = dateFormat.format(archive.getCooperationStartDate());
                result.put("cooperationStartDate", formattedDate);
                System.out.println("合作开始日期格式化后: " + formattedDate);
            } else {
                System.out.println("合作开始日期为空");
            }
            if (archive.getBusinessTermStart() != null) {
                result.put("businessTermStart", dateFormat.format(archive.getBusinessTermStart()));
            }
            if (archive.getBusinessTermEnd() != null) {
                result.put("businessTermEnd", dateFormat.format(archive.getBusinessTermEnd()));
            }

            System.out.println("返回结果: " + JSON.toJSONString(result));
            System.out.println("============================");

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增合作伙伴档案")
    public String add(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 字段名转换：前端字段名 → 后端字段名
            if (params.containsKey("partnerNameEng")) {
                params.put("partnerEnName", params.get("partnerNameEng"));
                params.remove("partnerNameEng");
            }
            if (params.containsKey("partnerStatus")) {
                params.put("cooperationStatus", params.get("partnerStatus"));
                params.remove("partnerStatus");
            }

            // 转换日期字段：时间戳字符串 → Date 对象
            convertTimestampToDate(params, "establishmentDate");
            convertTimestampToDate(params, "cooperationStartDate");
            convertTimestampToDate(params, "businessTermStart");
            convertTimestampToDate(params, "businessTermEnd");

            // 将 Map 转换为实体对象（使用 FastJSON）
            String jsonStr = JSON.toJSONString(params);
            TcPartnerArchive archive = JSON.parseObject(jsonStr, TcPartnerArchive.class);

            // 调试日志
            System.out.println("=== 新增合作伙伴档案 - 转换后的数据 ===");
            System.out.println("partnerCode: " + archive.getPartnerCode());
            System.out.println("partnerName: " + archive.getPartnerName());
            System.out.println("partnerEnName: " + archive.getPartnerEnName());
            System.out.println("cooperationStatus: " + archive.getCooperationStatus());
            System.out.println("establishmentDate: " + archive.getEstablishmentDate());
            System.out.println("cooperationStartDate: " + archive.getCooperationStartDate());
            System.out.println("=====================================");

            archive.setCreateUser(loginStaff.getStaffid().toString());
            archive.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = partnerArchiveService.save(archive);
            if (result) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("更新合作伙伴档案")
    public String update(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 调试日志
            System.out.println("=== 更新合作伙伴档案 - 接收到的数据 ===");
            System.out.println("原始数据: " + JSON.toJSONString(params));

            // 字段名转换：前端字段名 → 后端字段名
            if (params.containsKey("partnerNameEng")) {
                params.put("partnerEnName", params.get("partnerNameEng"));
                params.remove("partnerNameEng");
            }
            if (params.containsKey("partnerStatus")) {
                params.put("cooperationStatus", params.get("partnerStatus"));
                params.remove("partnerStatus");
            }

            // 移除虚拟字段（不是数据库字段）
            params.remove("partnerTypeName");

            // 转换日期字段：时间戳字符串 → Date 对象
            convertTimestampToDate(params, "establishmentDate");
            convertTimestampToDate(params, "cooperationStartDate");
            convertTimestampToDate(params, "businessTermStart");
            convertTimestampToDate(params, "businessTermEnd");
            convertTimestampToDate(params, "createTime");
            convertTimestampToDate(params, "updateTime");

            System.out.println("转换后数据: " + JSON.toJSONString(params));

            // 将 Map 转换为实体对象（使用 FastJSON）
            String jsonStr = JSON.toJSONString(params);
            TcPartnerArchive archive = JSON.parseObject(jsonStr, TcPartnerArchive.class);

            // 重要：手动将虚拟字段设置为 null，防止 MyBatis-Plus 在 WHERE 条件中使用
            archive.setPartnerTypeName(null);

            System.out.println("实体对象 ID: " + archive.getId());
            System.out.println("实体对象 partnerCode: " + archive.getPartnerCode());
            System.out.println("实体对象 partnerEnName: " + archive.getPartnerEnName());
            System.out.println("实体对象 cooperationStatus: " + archive.getCooperationStatus());
            System.out.println("实体对象 establishmentDate: " + archive.getEstablishmentDate());
            System.out.println("实体对象 cooperationStartDate: " + archive.getCooperationStartDate());
            System.out.println("====================================");

            archive.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = partnerArchiveService.update(archive);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 将时间戳字符串转换为 Date 对象
     */
    private void convertTimestampToDate(Map<String, Object> params, String fieldName) {
        if (params.containsKey(fieldName)) {
            Object value = params.get(fieldName);
            if (value != null && value instanceof String) {
                try {
                    long timestamp = Long.parseLong((String) value);
                    params.put(fieldName, new Date(timestamp));
                    System.out.println("转换日期字段 " + fieldName + ": " + value + " -> " + new Date(timestamp));
                } catch (NumberFormatException e) {
                    System.out.println("日期字段 " + fieldName + " 转换失败: " + value);
                }
            }
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除合作伙伴档案")
    public String delete(@PathVariable String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean result = partnerArchiveService.delete(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除合作伙伴档案")
    public String batchDelete(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            boolean result = partnerArchiveService.batchDelete(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/toggle")
    @ApiOperation("切换档案状态")
    public String toggle(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String id = (String) params.get("id");
            String isEnabled = params.get("isEnabled").toString();

            TcPartnerArchive archive = partnerArchiveService.getById(id);
            if (archive == null) {
                return new JsonBean(0, "档案不存在", null).toJson();
            }

            archive.setStatus(isEnabled);
            archive.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = partnerArchiveService.update(archive);
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/update-risk-level")
    @ApiOperation("更新风险等级")
    public String updateRiskLevel(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String id = (String) params.get("id");
            String riskLevel = (String) params.get("riskLevel");

            TcPartnerArchive archive = partnerArchiveService.getById(id);
            if (archive == null) {
                return new JsonBean(0, "档案不存在", null).toJson();
            }

            archive.setRiskLevel(riskLevel);
            archive.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = partnerArchiveService.update(archive);
            if (result) {
                return JsonBean.success("风险等级更新成功");
            } else {
                return new JsonBean(0, "风险等级更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "风险等级更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/update-credit-rating")
    @ApiOperation("更新信用评级")
    public String updateCreditRating(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String id = (String) params.get("id");
            String creditRating = (String) params.get("creditRating");

            TcPartnerArchive archive = partnerArchiveService.getById(id);
            if (archive == null) {
                return new JsonBean(0, "档案不存在", null).toJson();
            }

            archive.setCreditRating(creditRating);
            archive.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = partnerArchiveService.update(archive);
            if (result) {
                return JsonBean.success("信用评级更新成功");
            } else {
                return new JsonBean(0, "信用评级更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "信用评级更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取档案统计信息")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> statistics = partnerArchiveService.getPartnerStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/partner-types")
    @ApiOperation("获取伙伴类型列表")
    public String getPartnerTypes() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            // 返回伙伴类型列表，实际应调用TcPartnerTypeService
            return JsonBean.success(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "获取类型列表失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出档案")
    public String export(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TcPartnerArchive> list = partnerArchiveService.exportPartners(params);

            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
            result.put("exportTime", new Date());

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }
}

