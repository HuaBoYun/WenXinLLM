package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.WarningResult;
import com.huabo.cybermonitor.mapper.WarningResultMapper;
import com.huabo.cybermonitor.service.IWarningResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class WarningResultServiceImpl extends ServiceImpl<WarningResultMapper, WarningResult> implements IWarningResultService {

    @Autowired
    WarningResultMapper warningResultMapper;

    @Override
    public String getLast() {
        List<WarningResult> last = warningResultMapper.getLast();
        if(last.size()>0){
            return last.get(0).getWarningid().toString();
        }
        return null;
    }
}
