package com.huabo.fxgl.service.impl;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.NbsjBugcriterion;
import com.huabo.fxgl.mapper.NbsjBugcriterionMapper;
import com.huabo.fxgl.service.INbsjBugcriterionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Service
public class NbsjBugcriterionServiceImpl extends ServiceImpl<NbsjBugcriterionMapper, NbsjBugcriterion> implements INbsjBugcriterionService {
    @Autowired
    private NbsjBugcriterionMapper nbsjBugcriterionMapper;
    


    @Override
    public NbsjBugcriterion findNbsjBugcriterionByorgid(String bugid) {
        List<NbsjBugcriterion> NbsjBugCriterionListBybugid = nbsjBugcriterionMapper.findByTblBugCriterion(bugid);
        if(NbsjBugCriterionListBybugid!=null && NbsjBugCriterionListBybugid.size()>0){
            return NbsjBugCriterionListBybugid.get(0);
        }else{
            return null;
        }
    }
}
