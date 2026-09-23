package com.huabo.legal.exam.modules.sys.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.legal.exam.core.api.ApiRest;
import com.huabo.legal.exam.core.api.controller.BaseController;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.core.utils.BeanMapper;
import com.huabo.legal.exam.modules.sys.user.dto.SysRoleDTO;
import com.huabo.legal.exam.modules.sys.user.entity.SysRole;
import com.huabo.legal.exam.modules.sys.user.service.SysRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 管理用户控制器
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-04-13 16:57
 */
@Tag(name="管理用户",description="管理用户")
@RestController
@RequestMapping("/exam/api/sys/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService baseService;




    /**
     * 分页查找
     * @param reqDTO
     * @return
     */
    @Operation(summary = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<SysRoleDTO>> paging(@RequestBody PagingReqDTO<SysRoleDTO> reqDTO) {

        //分页查询并转换
        IPage<SysRoleDTO> page = baseService.paging(reqDTO);
        return super.success(page);
    }

    /**
     * 查找列表，每次最多返回200条数据
     * @return
     */
    @Operation(summary = "查找列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST})
    public ApiRest<List<SysRoleDTO>> list() {

        //分页查询并转换
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

        //转换并返回
        List<SysRole> list = baseService.list(wrapper);

        //转换数据
        List<SysRoleDTO> dtoList = BeanMapper.mapList(list, SysRoleDTO.class);

        return super.success(dtoList);
    }
}
