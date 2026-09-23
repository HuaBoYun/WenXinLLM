package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceBookService;
import com.financial.sharing.util.LegalDealUserToken;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 财务账簿管理控制器
 * 处理账簿管理、账簿类型、账簿授权等功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/book")
@CrossOrigin
public class FinanceBookController {

    @Autowired
    private FinanceBookService financeBookService;

    // ==================== 账簿管理 ====================

    /**
     * 账簿管理列表
     */
    @GetMapping("/bookInfo/getList")
    public MyJsonBean getZbglList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        try {
            // 从请求头获取token并解析租户ID
            String token = request.getHeader("token");
            if (token != null && !token.isEmpty()) {
                TblStaffUtil staff = LegalDealUserToken.parseUserToken(token);
                if (staff != null && staff.getCurrentOrg() != null && staff.getCurrentOrg().getOrgid() != null) {
                    params.put("tenantId", staff.getCurrentOrg().getOrgid().longValue());
                }
            }

            PageResult<Map<String, Object>> result = financeBookService.getZbglList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取账簿管理列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 账簿管理详情
     */
    @GetMapping("/bookInfo/detail")
    public MyJsonBean getZbglDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeBookService.getZbglDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取账簿管理详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 账簿管理新增
     */
    @PostMapping("/bookInfo/save")
    public MyJsonBean saveZbgl(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeBookService.saveZbgl(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新增账簿管理失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 账簿管理删除
     */
    @PostMapping("/bookInfo/del")
    public MyJsonBean deleteZbgl(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeBookService.deleteZbgl(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除账簿管理失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 账簿类型管理 ====================

    /**
     * 账簿类型列表
     */
    @GetMapping("/setof/getList")
    public MyJsonBean getZblxList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeBookService.getZblxList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取账簿类型列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 账簿类型详情
     */
    @GetMapping("/setof/detail")
    public MyJsonBean getZblxDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeBookService.getZblxDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取账簿类型详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 账簿类型新增
     */
    @PostMapping("/setof/save")
    public MyJsonBean saveZblx(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeBookService.saveZblx(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新增账簿类型失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 账簿类型删除
     */
    @PostMapping("/setof/del")
    public MyJsonBean deleteZblx(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeBookService.deleteZblx(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除账簿类型失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 账簿授权管理 ====================

    /**
     * 账簿授权角色
     */
    @PostMapping("/bookRole/grant")
    public MyJsonBean zbglAuthRole(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeBookService.zbglAuthRole(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("账簿授权角色失败", e);
            return MyJsonBean.errorData("授权失败: " + e.getMessage());
        }
    }

    /**
     * 账簿授权角色列表
     */
    @GetMapping("/bookRole/getRoleListByBook")
    public MyJsonBean zbglAuthRoleList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeBookService.zbglAuthRoleList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取账簿授权角色列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 取消授权
     */
    @PostMapping("/bookRole/cancel")
    public MyJsonBean zbglCancelAuthRole(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeBookService.zbglCancelAuthRole(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("取消账簿授权失败", e);
            return MyJsonBean.errorData("取消授权失败: " + e.getMessage());
        }
    }

    /**
     * 获取公司账簿授权角色列表
     */
    @GetMapping("/bookStaff/getBookList")
    public MyJsonBean getGsZbglAuthRoleList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeBookService.getGsZbglAuthRoleList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取公司账簿授权角色列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 公司账簿选择
     */
    @PostMapping("/bookStaff/selected")
    public MyJsonBean sureGsZb(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeBookService.sureGsZb(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("公司账簿选择失败", e);
            return MyJsonBean.errorData("选择失败: " + e.getMessage());
        }
    }
}