package com.global.treasurer.service.impl;

import com.global.treasurer.dto.CommercialBillDTO;
import com.global.treasurer.dto.CommercialBillQueryDTO;
import com.global.treasurer.entity.TblCommercialBill;
import com.global.treasurer.mapper.CommercialBillMapper;
import com.global.treasurer.service.ICommercialBillService;
import com.global.treasurer.vo.CommercialBillVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商业汇票Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class CommercialBillServiceImpl extends ServiceImpl<CommercialBillMapper, TblCommercialBill>
        implements ICommercialBillService {
    private static final Logger log = LoggerFactory.getLogger(CommercialBillServiceImpl.class);

    @Resource
    private CommercialBillMapper commercialBillMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public PageInfo<CommercialBillVO> selectCommercialBillList(CommercialBillQueryDTO queryDTO) {
        log.info("查询商业汇票列表, 参数: {}", queryDTO);

        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<CommercialBillVO> list = commercialBillMapper.selectCommercialBillList(queryDTO);

        return new PageInfo<>(list);
    }

    @Override
    public CommercialBillVO selectCommercialBillById(Long billId) {
        log.info("查询商业汇票详情, billId: {}", billId);
        return commercialBillMapper.selectCommercialBillById(billId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblCommercialBill insertCommercialBill(CommercialBillDTO dto) {
        log.info("新增商业汇票, 参数: {}", dto);

        TblCommercialBill entity = new TblCommercialBill();
        BeanUtils.copyProperties(dto, entity);

        // 生成ID
        entity.setBillId(snowflakeIdWorker.nextId());
        entity.setBillStatus("ISSUED");
        entity.setDeleteFlag(0);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        commercialBillMapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblCommercialBill updateCommercialBill(CommercialBillDTO dto) {
        log.info("修改商业汇票, 参数: {}", dto);

        TblCommercialBill entity = new TblCommercialBill();
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());

        commercialBillMapper.updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCommercialBillByIds(Long[] billIds) {
        log.info("删除商业汇票, billIds: {}", billIds);
        return commercialBillMapper.deleteCommercialBillByIds(billIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean issueBill(Map<String, Object> issueData) {
        log.info("开立汇票, 参数: {}", issueData);

        TblCommercialBill entity = new TblCommercialBill();
        entity.setBillId(snowflakeIdWorker.nextId());
        entity.setBillNo((String) issueData.get("billNo"));
        entity.setBillType((String) issueData.get("billType"));
        entity.setBillAmount((java.math.BigDecimal) issueData.get("billAmount"));
        entity.setDrawerName((String) issueData.get("drawerName"));
        entity.setPayeeName((String) issueData.get("payeeName"));
        entity.setAcceptorName((String) issueData.get("acceptorName"));
        entity.setBillStatus("ISSUED");
        entity.setDeleteFlag(0);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        return commercialBillMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptBill(Long billId, Map<String, Object> acceptData) {
        log.info("承兑汇票, billId: {}, 参数: {}", billId, acceptData);
        return commercialBillMapper.updateBillStatus(billId, "ACCEPTED") > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean endorseBill(Long billId, Map<String, Object> endorseData) {
        log.info("背书汇票, billId: {}, 参数: {}", billId, endorseData);
        return commercialBillMapper.updateBillStatus(billId, "ENDORSED") > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean discountBill(Long billId, Map<String, Object> discountData) {
        log.info("贴现汇票, billId: {}, 参数: {}", billId, discountData);
        return commercialBillMapper.updateBillStatus(billId, "DISCOUNTED") > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean matureBill(Long billId) {
        log.info("到期处理汇票, billId: {}", billId);
        return commercialBillMapper.updateBillStatus(billId, "MATURED") > 0;
    }
}
