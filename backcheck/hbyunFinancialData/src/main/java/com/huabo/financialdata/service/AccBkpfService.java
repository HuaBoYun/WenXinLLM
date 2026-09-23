package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.entity.TblAccBkpf;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfRequestVo;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfResponseVo;

import java.util.List;

/**
 * <p>
 * 凭证库  接口类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-22
 */
public interface AccBkpfService {

    //获取凭证库数据
    ApiResponse<PageInfo<AccBkpfResponseVo>> getList(String token, AccBkpfRequestVo accBkpfRequestVo);

    //获取记录凭证
    ApiResponse<List<TblAccBkpf>> findAccBkpfAmonth(String token, String pzh, String month, String book, String year) throws Exception;
}
