package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.RiskeventMatrix;
import com.huabo.fxgl.mapper.RiskeventMatrixMapper;
import com.huabo.fxgl.service.IControlmatrixService;
import com.huabo.fxgl.service.IRiskeventMatrixService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskeventMatrixServiceImpl extends ServiceImpl<RiskeventMatrixMapper, RiskeventMatrix> implements IRiskeventMatrixService {
    @Autowired
    private IControlmatrixService controlmatrixService;

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过EVENTID去查询TBL_MATRIX_RISK
     * @Date 2022/8/11
     * @param eventid
     * @return java.util.List<com.huabo.fxgl.entity.RiskeventMatrix>
     * @url:
     **/
    @Override
    public List<RiskeventMatrix> getByEventId(String eventid) {
        QueryWrapper<RiskeventMatrix> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("RISEVEID",eventid);
        List<RiskeventMatrix> riskeventMatrixList = list(queryWrapper);
        if (riskeventMatrixList!=null&&riskeventMatrixList.size()>0){
            riskeventMatrixList.stream().map(item -> {
                item.setControlnumber(controlmatrixService.getById(item.getConmatid()).getControlnumber());
                return item;
            }).collect(Collectors.toList());
        }
        return riskeventMatrixList;
    }
}
