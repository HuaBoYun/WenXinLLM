package com.huabo.etl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.service.IKettleJobService;
import com.huabo.etl.service.IKettleTransService;
import com.huabo.etl.service.IXRepositoryService;
import com.huabo.etl.utils.AjaxResult;
import com.huabo.etl.utils.page.TableDataInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 资源库Controller
 *
 * @author zhibo.cao
 * @date 2021-07-12
 */
@Tag(name="资源库",description="资源库")
@RestController
@RequestMapping("/kettle/repository")
public class XRepositoryController extends BaseController {
    @Autowired
    private IXRepositoryService xRepositoryService;
    @Autowired
    private IKettleJobService iKettleJobService;
    @Autowired
    private IKettleTransService iKettleTransService;


    /**
     * 查询资源库列表
     */
    @Operation(summary = "查询所有的资源库")
    @GetMapping("/listAll")
    public AjaxResult listAll(XRepository xRepository) {
        List<XRepository> list = xRepositoryService.selectXRepositoryList(xRepository);
        return AjaxResult.success(list);
    }

    /**
     * 查询资源库列表
     */
    @Operation(summary = "查询资源库列表")
    @GetMapping("/list")
    public TableDataInfo list(XRepository xRepository) {
        startPage();
        List<XRepository> list = xRepositoryService.selectXRepositoryList(xRepository);
        return getDataTable(list);
    }

//    /**
//     *
//     * @return
//     */
//    @Operation(summary = "资源库树")
//    @GetMapping("/repositoryRoot")
//    public List<RepoTree> repositoryRoot() {
//        List<RepoTree> ztrees = xRepositoryService.selectRepoRoot(new XRepository());
//        return ztrees;
//    }


//    /**
//     * 导出资源库列表
//     */
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(XRepository xRepository) {
//        List<XRepository> list = xRepositoryService.selectXRepositoryList(xRepository);
//        ExcelUtil<XRepository> util = new ExcelUtil<XRepository>(XRepository.class);
//        return util.exportExcel(list, "资源库数据");
//    }

    /**
     * 新增保存资源库
     */
    @Operation(summary = "新增保存资源库")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody XRepository xRepository) {
        if (StrUtil.isEmpty(xRepository.getRepoName()) || StrUtil.isEmpty(xRepository.getBaseDir())) {
            return AjaxResult.error("资源库名必须存在");
        }
        XRepository selectXRepository = xRepositoryService.selectByName(xRepository.getRepoName());
        if (ObjectUtil.isNotNull(selectXRepository)) {
            return AjaxResult.error("已经存在该资源库名");
        }
        List<XRepository> xRepositories = xRepositoryService.selectXRepositoryList(null);
        for (int i = 0; i < xRepositories.size(); i++) {
            if (FileUtil.newFile(xRepository.getBaseDir()).equals(FileUtil.newFile(xRepositories.get(i).getBaseDir()))) {
                return AjaxResult.error("该目录已经有资源库了，请不要重复创建");
            }
        }
        return toAjax(xRepositoryService.insertXRepository(xRepository));
    }


    /**
     * 修改保存资源库
     */
    @Operation(summary = "修改保存资源库")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody XRepository xRepository) {
        // 资源库id
        if (ObjectUtil.isNull(xRepository.getId())) {
            return AjaxResult.error("资源库Id必须传输");
        }
        // 资源库名
        if (StrUtil.isNotEmpty(xRepository.getRepoName())) {
            XRepository selectXRepository = xRepositoryService.selectByName(xRepository.getRepoName());
            if (ObjectUtil.isNotNull(selectXRepository) && selectXRepository.getId() != xRepository.getId()) {
                return AjaxResult.error("已经存在该资源库");
            }
        }

        if (StrUtil.isNotEmpty(xRepository.getBaseDir())) {
            return AjaxResult.error("资源库路径不能修改");
        }
        xRepository.setBaseDir(null);
        return toAjax(xRepositoryService.updateXRepository(xRepository));
    }

    /**
     * 删除资源库
     */
    @Operation(summary = "删除资源库")
    @DeleteMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        List<KettleJob> jobs = iKettleJobService.selectKettleJobByRepoIds(ids);
        List<KettleTrans> trans = iKettleTransService.selectKettleTransByRepoIds(ids);
        if (CollUtil.isNotEmpty(jobs) || CollUtil.isNotEmpty(trans)) {
            return AjaxResult.error("资源库下有作业或转换，不能删除");
        }
        return toAjax(xRepositoryService.deleteXRepositoryByIds(ids));
    }
}
