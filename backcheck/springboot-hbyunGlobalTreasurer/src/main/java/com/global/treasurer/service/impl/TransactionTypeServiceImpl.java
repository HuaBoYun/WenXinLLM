package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TransactionType;
import com.global.treasurer.mapper.TransactionTypeMapper;
import com.global.treasurer.service.TransactionTypeService;
import org.springframework.stereotype.Service;

/**
 * TransactionType Service实现类
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@Service
public class TransactionTypeServiceImpl extends ServiceImpl<TransactionTypeMapper, TransactionType> implements TransactionTypeService {
}
