package com.huabo.legal.exam.modules.repo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.repo.dto.RepoDTO;
import com.huabo.legal.exam.modules.repo.dto.response.RepoRespDTO;
import com.huabo.legal.exam.modules.repo.entity.Repo;
import com.huabo.legal.util.PageResult;

/**
* <p>
* 题库业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
public interface RepoService extends IService<Repo> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
	PageResult<RepoRespDTO> paging(PagingReqDTO<RepoDTO> reqDTO);


    /**
     * 保存
     * @param reqDTO
     */
    void save(RepoDTO reqDTO);

    /**
     * 更新统计数量
     * @param repoId
     */
    void refreshStat(String repoId);


    /**
     * 根据名称查找题库
     * @param name
     * @return
     */
    String findByName(String name);
}
