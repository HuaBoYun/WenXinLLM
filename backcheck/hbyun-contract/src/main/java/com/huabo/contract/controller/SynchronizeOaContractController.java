package com.huabo.contract.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.contract.service.SynchronizeOaService;
import com.huabo.contract.service.TblCyhwUnitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OA历史合同同步控制器
 * <p>提供OA系统历史合同数据的批量同步接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="同步Oa历史合同数据",description="同步Oa历史合同数据")
public class SynchronizeOaContractController {

    @Resource
    private TblCyhwUnitService tblCyhwUnitService;

    @Resource
    private SynchronizeOaService synchronizeOaService;

    /**
     * 同步集团公司历史合同数据
     *
     * @return
     */
    @RequestMapping(value = "/oa/gourp/contract", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "同步集团公司历史合同数据")
    public JsonBean customers(HttpServletRequest request) throws Exception {
        return this.synchronizeOaService.SynchronizeGroupContractInfo();
    }

    /**
     * 发展资产历史合同导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/fzzc", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "发展资产历史合同导入")
    public JsonBean fzzc(HttpServletRequest request) throws Exception {
        //return this.tblCyhwUnitService.saveFzzcCyhwUnit();
        return this.synchronizeOaService.SynchronizeFzzcContractInfo();
    }

    /**
     * 发展资产合同审批单导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/fzzcsp", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "发展资产合同审批单导入")
    public JsonBean fzzcsp(HttpServletRequest request) throws Exception {
        return this.tblCyhwUnitService.saveFzzcSpCyhwUnit();
        //return this.synchronizeOaService.SynchronizeFzzSpcContractInfo();
    }

    /**
     * 融资租赁历史合同导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/rzzl", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "融资租赁历史合同导入")
    public JsonBean rzzl(HttpServletRequest request) throws Exception {
        //return this.tblCyhwUnitService.saveRzzlCyhwUnit();
        return this.synchronizeOaService.SynchronizeRzzlContractInfo();
    }

    /**
     * 盐业集团历史合同导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/yyjt", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "盐业集团历史合同导入")
    public JsonBean yyjt(HttpServletRequest request) throws Exception {
        //return tblCyhwUnitService.saveYyjtCyhwUnit();
        return this.synchronizeOaService.SynchronizeyyjtContractInfo();
    }

    /**
     * 富浙资产历史合同导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/fzzcht", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "富浙资产历史合同导入")
    public JsonBean fzzcht(HttpServletRequest request) throws Exception {
        //return this.tblCyhwUnitService.saveFuZhezcCyhwUnit();
        return this.synchronizeOaService.SynchronizeFuZhezcContractInfo();
    }

    /**
     * 富浙科技历史合同导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/fzkj", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "富浙科技历史合同导入")
    public JsonBean fzkj(HttpServletRequest request) throws Exception {
        //return this.tblCyhwUnitService.saveFzkjCyhwUnit();
        return this.synchronizeOaService.SynchronizFzkjContractInfo();
    }

    /**
     * 富浙资本历史合同导入
     *
     * @return
     */
    @RequestMapping(value = "/oa/child/contract/fzzb", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "富浙资本历史合同导入")
    public JsonBean fzzb(HttpServletRequest request) throws Exception {
        //return this.tblCyhwUnitService.saveFzzbCyhwUnit();
        return this.synchronizeOaService.SynchronizfzzbContractInfo();
    }

}
