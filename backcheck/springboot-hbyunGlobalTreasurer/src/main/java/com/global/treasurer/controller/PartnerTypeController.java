package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcPartnerType;
import com.global.treasurer.service.TcPartnerTypeService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 合作伙伴类型管理控制器
 * 
 * @author AI Assistant
 * @date 2025-01-26
 */
@RestController
@RequestMapping("/settlement/partner-type")
@Api(tags = "合作伙伴类型管理")
public class PartnerTypeController {
    @Resource
    private TcPartnerTypeService partnerTypeService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询合作伙伴类型")
    public String getPage(@FlexibleRequestBody Map<String, Object> params) {
        try {
            System.out.println("========== 分页查询开始 ==========");
            System.out.println("接收到的参数: " + params);

            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            System.out.println("pageNum: " + pageNum + ", pageSize: " + pageSize);
            System.out.println("typeCode: " + params.get("typeCode"));
            System.out.println("typeName: " + params.get("typeName"));
            System.out.println("isEnabled: " + params.get("isEnabled"));

            List<TcPartnerType> list = partnerTypeService.list(pageNum, pageSize, params);
            int total = partnerTypeService.count(params);

            System.out.println("查询结果数量: " + list.size() + ", 总记录数: " + total);
            System.out.println("========== 分页查询结束 ==========");

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", list);
            result.put("totalRecord", total);
            
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/getDetail")
    @ApiOperation("获取合作伙伴类型详情")
    public String getDetail(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            Long id = Long.parseLong(params.get("partnerTypeId").toString());
            TcPartnerType partnerType = partnerTypeService.getById(id);
            
            if (partnerType == null) {
                return new JsonBean(0, "类型不存在", null).toJson();
            }
            return JsonBean.success(partnerType);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/add")
    @ApiOperation("新增合作伙伴类型")
    public String add(@FlexibleRequestBody TcPartnerType partnerType) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            partnerType.setCreateUser(loginStaff.getStaffid().longValue());
            partnerType.setUpdateUser(loginStaff.getStaffid().longValue());
            partnerType.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());

            boolean result = partnerTypeService.save(partnerType);
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

    @PostMapping("/update")
    @ApiOperation("更新合作伙伴类型")
    public String update(@FlexibleRequestBody TcPartnerType partnerType) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            partnerType.setUpdateUser(loginStaff.getStaffid().longValue());
            
            boolean result = partnerTypeService.update(partnerType);
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

    @PostMapping("/delete")
    @ApiOperation("删除合作伙伴类型")
    public String delete(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = Long.parseLong(params.get("partnerTypeId").toString());
            boolean result = partnerTypeService.delete(id);
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

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除合作伙伴类型")
    public String batchDelete(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            List<Long> longIds = new ArrayList<>();
            for (String id : ids) {
                longIds.add(Long.parseLong(id));
            }
            boolean result = partnerTypeService.batchDelete(longIds);
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

    @PostMapping("/toggle")
    @ApiOperation("切换合作伙伴类型状态")
    public String toggle(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = Long.parseLong(params.get("partnerTypeId").toString());
            Integer isEnabled = Integer.parseInt(params.get("isEnabled").toString());

            TcPartnerType partnerType = partnerTypeService.getById(id);
            if (partnerType == null) {
                return new JsonBean(0, "类型不存在", null).toJson();
            }

            partnerType.setIsEnabled(isEnabled);
            partnerType.setUpdateUser(loginStaff.getStaffid().longValue());

            boolean result = partnerTypeService.update(partnerType);
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

    @PostMapping("/updateSortOrder")
    @ApiOperation("更新排序顺序")
    public String updateSortOrder(@FlexibleRequestBody Map<String, Object> params) {
        try {
            System.out.println("接收到的参数: " + params);

            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> sortList = (List<Map<String, Object>>) params.get("sortList");

            System.out.println("sortList: " + sortList);

            if (sortList == null || sortList.isEmpty()) {
                return new JsonBean(0, "排序数据不能为空", null).toJson();
            }

            for (Map<String, Object> item : sortList) {
                Long id = Long.parseLong(item.get("partnerTypeId").toString());
                Integer sortOrder = Integer.parseInt(item.get("sortOrder").toString());

                TcPartnerType partnerType = partnerTypeService.getById(id);
                if (partnerType != null) {
                    partnerType.setSortOrder(sortOrder);
                    partnerType.setUpdateUser(loginStaff.getStaffid().longValue());
                    partnerTypeService.update(partnerType);
                }
            }

            return JsonBean.success("排序更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "排序更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/getStatistics")
    @ApiOperation("获取统计信息")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> statistics = partnerTypeService.getStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出类型配置")
    public String export(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取所有类型数据
            List<TcPartnerType> list = partnerTypeService.getAllEnabled();

            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
            result.put("exportTime", new Date());

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/getAllEnabled")
    @ApiOperation("获取所有启用的类型")
    public String getAllEnabled() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TcPartnerType> list = partnerTypeService.getAllEnabled();

            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}

