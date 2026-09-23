package com.huabo.legal.exam.modules.repo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.core.utils.BeanMapper;
import com.huabo.legal.exam.modules.qu.dto.QuDTO;
import com.huabo.legal.exam.modules.repo.dto.RepoDTO;
import com.huabo.legal.exam.modules.repo.dto.response.RepoRespDTO;
import com.huabo.legal.exam.modules.repo.entity.Repo;
import com.huabo.legal.exam.modules.repo.mapper.RepoMapper;
import com.huabo.legal.exam.modules.repo.service.RepoService;
import com.huabo.legal.util.PageResult;

import org.springframework.stereotype.Service;

/**
* <p>
* 语言设置 服务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Service
public class RepoServiceImpl extends ServiceImpl<RepoMapper, Repo> implements RepoService {

    @Override
    public PageResult<RepoRespDTO> paging(PagingReqDTO<RepoDTO> reqDTO) {
        PageInfo<RepoRespDTO> objectPageInfo = PageMethod.startPage(reqDTO.getCurrent(), reqDTO.getSize())
				.doSelectPageInfo(() -> baseMapper.paging(reqDTO.getParams()));
		return new PageResult<RepoRespDTO>().build(objectPageInfo);
     }

    @Override
    public void save(RepoDTO reqDTO) {

        //复制参数
        Repo entity = new Repo();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }

    @Override
    public void refreshStat(String repoId) {
        baseMapper.refreshStat(repoId);
    }


    @Override
    public String findByName(String name) {
        QueryWrapper<Repo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Repo::getTitle, name);
        Repo repo = this.getOne(wrapper);

        if(repo!=null){
            return repo.getId();
        }

        return null;
    }
}
