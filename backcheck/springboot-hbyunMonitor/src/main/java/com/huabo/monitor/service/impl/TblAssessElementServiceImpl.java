package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.monitor.oracle.entity.TblAssesselement;
import com.huabo.monitor.oracle.mapper.TblAssesselementMapper;
import com.huabo.monitor.service.TblAssessElementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("TblAssessElementService")
public class TblAssessElementServiceImpl implements TblAssessElementService {

    @Resource
    private TblAssesselementMapper tblAssesselementMapper;


    /**
     * 分页查询 TblAssesselement 列表数据
     *
     * @param pageNumber
     * @param pageSize
     * @param assesselement
     * @return
     */
    @Override
    public List<String> findByPageBean(Integer pageNumber, int pageSize, TblAssesselement assesselement) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize <= 0 || pageSize > 15) {
            pageSize = 15;
        }
        PageInfo<TblAssesselement> pageInfo = new PageInfo<>();
        pageInfo.setCondition(assesselement);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        return tblAssesselementMapper.findByPageBean(pageInfo);
    }

    /**
     * 新增 TblAssesselement
     *
     * @param tblAssesselement
     */
    @Override
    public JsonBean add(TblAssesselement tblAssesselement) {
        tblAssesselementMapper.insert(tblAssesselement);
        return ResponseFormat.retParam(1, 200, "添加成功");
    }

    /**
     * 更具id更新 TblAssesselement
     *
     * @param tblAssesselement
     * @return
     */
    @Override
    public JsonBean update(TblAssesselement tblAssesselement) {
        if (null == tblAssesselement) {
            return ResponseFormat.retParam(0, 10002, "参数为空,请检查重试");
        }
        BigDecimal asseleid = tblAssesselement.getAsseleid();
        TblAssesselement dbAssesselement = tblAssesselementMapper.selectById(asseleid);
        if (null == dbAssesselement) {
            return ResponseFormat.retParam(0, 10002, "更新数据不存在,请检查重试");
        }
        tblAssesselementMapper.updateById(tblAssesselement);
        return ResponseFormat.retParam(1, 200, "更新成功");
    }

    /**
     * 通过 id 删除 TblAssesselement 对象
     *
     * @param deleteIds
     * @return
     */
    @Override
    public JsonBean deleteByIds(List deleteIds) {
        if (CollectionUtils.isEmpty(deleteIds)) {
            return ResponseFormat.retParam(0, 10002, "参数为空,请检查重试");
        }
        tblAssesselementMapper.deleteBatchIds(deleteIds);
        return ResponseFormat.retParam(1, 200, "删除成功");
    }

    @Override
    public List<TblAssesselement> getAssEssByIn(String assessIds) {
        return null;
    }

    @Override
    public TblAssesselement get(BigDecimal id) {
        return null;
    }

    @Override
    public List<TblAssesselement> getComany(String orgid) {
        return null;
    }
}
