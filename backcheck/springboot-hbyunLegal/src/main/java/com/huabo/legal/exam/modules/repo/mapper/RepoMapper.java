package com.huabo.legal.exam.modules.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.legal.exam.modules.repo.dto.RepoDTO;
import com.huabo.legal.exam.modules.repo.dto.response.RepoRespDTO;
import com.huabo.legal.exam.modules.repo.entity.Repo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
* <p>
* 题库Mapper
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
public interface RepoMapper extends BaseMapper<Repo> {

    /**
     * 分页查询题库
     * @param page
     * @param query
     * @return
     */
    //IPage<RepoRespDTO> paging(Page page, @Param("query") RepoDTO query);
	List<RepoRespDTO> paging(@Param("query") RepoDTO query);

    /**
     * 更新统计数量
     * @param repoId
     */
    void refreshStat(@Param("repoId") String repoId);

}
