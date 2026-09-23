package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.OutformField;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.Tree;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
public interface IOutformFieldService extends IService<OutformField> {

    List<Tree> getNodeAll(BigDecimal nodeId);
}
