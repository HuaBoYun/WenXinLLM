package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblTesttemple;
import com.huabo.compliance.mapper.TblTesttempleMapper;
import com.huabo.compliance.service.ITblTesttempleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@Service
@Transactional
public class TblTesttempleServiceImpl extends ServiceImpl<TblTesttempleMapper, TblTesttemple> implements ITblTesttempleService {

}
