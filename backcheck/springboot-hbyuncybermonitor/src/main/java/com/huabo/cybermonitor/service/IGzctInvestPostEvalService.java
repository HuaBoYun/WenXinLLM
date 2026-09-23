package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctInvestPostEval;
import com.huabo.cybermonitor.util.PageResult;
import java.util.Map;

public interface IGzctInvestPostEvalService extends IService<GzctInvestPostEval> {
    PageResult<GzctInvestPostEval> selectByPage(Map<String, Object> params);
    boolean startEval(GzctInvestPostEval eval);
    boolean updateEval(GzctInvestPostEval eval);
    boolean deleteEval(String evalId);
}
