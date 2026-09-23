package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.BugCriterion;
import com.huabo.fxgl.entity.Criterion;
import com.huabo.fxgl.mapper.BugCriterionMapper;
import com.huabo.fxgl.service.IBugCriterionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.service.ICriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Service
public class BugCriterionServiceImpl extends ServiceImpl<BugCriterionMapper, BugCriterion> implements IBugCriterionService {

    @Autowired
    private ICriterionService iCriterionService;

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 通过BugId查询Criterion
    * @Date 2022/8/18
    * @param id
    * @return java.util.List<com.huabo.fxgl.entity.Criterion>
    * @url:
    **/
    @Autowired
    private BugCriterionServiceImpl bugCriterionService;

    @Autowired
    private CriterionServiceImpl criterionService;


    @Override
    public List<Criterion> getCriterionByBugId(String id) {
        QueryWrapper<BugCriterion> queryWrapper=new QueryWrapper<>();
         queryWrapper.eq("BUGID",id);
        List<BugCriterion> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
            List<BigDecimal> ids = list.stream().map(BugCriterion::getBugcriid).collect(Collectors.toList());
            return iCriterionService.listByIds(ids);
        }
        return null;
    }


	@Override
	public List<BugCriterion> findTblBugCriterionListByorgid(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}



    //@Override
   /* public List<BugCriterion> findTblBugCriterionListByorgid(String orgid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1",1);
        queryWrapper.eq("ORGID",orgid);
        return criterionService.list(queryWrapper);
    }*/
}
