package com.huabo.legal.exam.modules.repo.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.legal.exam.core.api.ApiRest;
import com.huabo.legal.exam.core.api.controller.BaseController;
import com.huabo.legal.exam.core.api.dto.BaseIdReqDTO;
import com.huabo.legal.exam.core.api.dto.BaseIdsReqDTO;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.core.utils.BeanMapper;
import com.huabo.legal.exam.modules.qu.dto.request.QuRepoBatchReqDTO;
import com.huabo.legal.exam.modules.qu.service.QuRepoService;
import com.huabo.legal.exam.modules.repo.dto.RepoDTO;
import com.huabo.legal.exam.modules.repo.dto.response.RepoRespDTO;
import com.huabo.legal.exam.modules.repo.entity.Repo;
import com.huabo.legal.exam.modules.repo.service.RepoService;
import com.huabo.legal.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
* <p>
* 题库控制器
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:25
*/
@Tag(name="题库",description="题库")
@RestController
@RequestMapping("/exam/api/repo")
public class RepoController extends BaseController {

    @Autowired
    private RepoService baseService;

    @Autowired
    private QuRepoService quRepoService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @Operation(summary = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody RepoDTO reqDTO) {
        baseService.save(reqDTO);
        return super.success();
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @Operation(summary = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }

    /**
    * 查找详情
    * @param reqDTO
    * @return
    */
    @Operation(summary = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<RepoDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
        Repo entity = baseService.getById(reqDTO.getId());
        RepoDTO dto = new RepoDTO();
        BeanUtils.copyProperties(entity, dto);
        return super.success(dto);
    }

    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @Operation(summary = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<PageResult<RepoRespDTO>> paging(@RequestBody PagingReqDTO<RepoDTO> reqDTO) {
        return super.success(baseService.paging(reqDTO));
    }

    /**
     * 批量操作
     * @param reqDTO
     * @return
     */
    @Operation(summary = "批量操作", description = "批量加入或从题库移除")
    @RequestMapping(value = "/batch-action", method = { RequestMethod.POST})
    public ApiRest paging(@RequestBody QuRepoBatchReqDTO reqDTO) {

        //分页查询并转换
        quRepoService.batchAction(reqDTO);
        return super.success();
    }

    /**
     * 查找列表，每次最多返回200条数据
     * @param reqDTO
     * @return
     */
    @Operation(summary = "查找列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST})
    public ApiRest<List<RepoDTO>> list(@RequestBody RepoDTO reqDTO) {

        //分页查询并转换
        QueryWrapper<Repo> wrapper = new QueryWrapper<>();

        //转换并返回
        List<Repo> list = baseService.list(wrapper);

        //转换数据
        List<RepoDTO> dtoList = BeanMapper.mapList(list, RepoDTO.class);

        return super.success(dtoList);
    }
}
