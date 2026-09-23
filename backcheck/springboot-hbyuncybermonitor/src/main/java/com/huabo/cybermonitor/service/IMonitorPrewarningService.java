package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.MonitorPrewarning;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-08-12
 */
public interface IMonitorPrewarningService extends IService<MonitorPrewarning> {

    List<MonitorPrewarning> getResultBySignId(BigDecimal modelId, BigDecimal solutionResultId);
}
