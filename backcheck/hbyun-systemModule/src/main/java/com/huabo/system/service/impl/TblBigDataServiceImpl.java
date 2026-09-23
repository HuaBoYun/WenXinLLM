package com.huabo.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huabo.system.entity.TblCubeBbmx;
import com.huabo.system.entity.TblCubeYS202201;
import com.huabo.system.mapper.TblBiCkEchartsDao;
import com.huabo.system.mapper.TblBigDataMapper;
import com.huabo.system.mapper.TblCubeYS202201Mapper;
import com.huabo.system.service.TblBigDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TblBigDataServiceImpl implements TblBigDataService {
    @Resource
    private TblBigDataMapper tblBigDataMapper;

    @Resource
    private TblCubeYS202201Mapper tblCubeYS202201Mapper;

//TBL_CUBE_BBMX 表格的数据同步
    @Override
    public void syncBigData(String result) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(result);
        System.out.println(jsonObject);
        int codea = jsonObject.getIntValue("codea");
        String message = jsonObject.getString("message");
        boolean success = jsonObject.getBooleanValue("success");
        JSONObject data = jsonObject.getJSONObject("data");
        com.alibaba.fastjson.JSONArray value = data.getJSONArray("value");
        List<TblCubeBbmx> list = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            TblCubeBbmx tblCubeBbmx = new TblCubeBbmx();
            JSONObject jsonObject1 = value.getJSONObject(i);
            BigDecimal bigDecimal = tblBigDataMapper.selectMaxTCBID();
            if (bigDecimal == null){
                bigDecimal = new BigDecimal(1);
            }
            tblCubeBbmx.setTcbId(bigDecimal);
            tblCubeBbmx.setCodebmmeatype(jsonObject1.getString("CODE_BM_MEATYPE"));
            tblCubeBbmx.setCodeentity(jsonObject1.getString("CODE_ENTITY"));
            tblCubeBbmx.setCodeversion(jsonObject1.getString("CODE_VERSION"));
            tblCubeBbmx.setCodemeasure(jsonObject1.getString("CODE_MEASURE"));
            tblCubeBbmx.setCodemvtype(jsonObject1.getString("CODE_MVTYPE"));
            tblCubeBbmx.setCodebmtrail(jsonObject1.getString("CODE_BM_TRAIL"));
            tblCubeBbmx.setPkaccp(jsonObject1.getBigDecimal("PK_ACCP"));
            tblCubeBbmx.setPkaccm(jsonObject1.getBigDecimal("PK_ACCM"));
            tblCubeBbmx.setValue(jsonObject1.getFloatValue("VALUE"));
            tblCubeBbmx.setFzhj(jsonObject1.getFloatValue("FZHJ"));
            tblCubeBbmx.setZchj(jsonObject1.getFloatValue("ZCHJ"));
            list.add(tblCubeBbmx);
            BigDecimal bigDecimal1 = tblBigDataMapper.selectCountByPkAccp(tblCubeBbmx.getPkaccp());
            if (bigDecimal1.intValue() == 0|| bigDecimal1 ==null){
                tblBigDataMapper.insertTCB(tblCubeBbmx);
            }else {
                tblBigDataMapper.updateTCB(tblCubeBbmx);
            }
//            System.out.println(tblCubeBbmx);
        }
    }

    @Override
    public void syncBigDataTCY(String result) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(result);
        System.out.println(jsonObject);
        int codea = jsonObject.getIntValue("codea");
        String message = jsonObject.getString("message");
        boolean success = jsonObject.getBooleanValue("success");
        JSONObject data = jsonObject.getJSONObject("data");
        com.alibaba.fastjson.JSONArray value = data.getJSONArray("value");
        List<TblCubeYS202201> list = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            TblCubeYS202201 tblCubeYS202201 = new TblCubeYS202201();
            JSONObject jsonObject1 = value.getJSONObject(i);
            BigDecimal bigDecimal = tblCubeYS202201Mapper.selectMaxTCYID();
            if (bigDecimal == null){
                bigDecimal = new BigDecimal(1);
            }
            tblCubeYS202201.setTcyId(bigDecimal);
            tblCubeYS202201.setCodebmmeatype(jsonObject1.getString("CODE_BM_MEATYPE"));
            tblCubeYS202201.setValue(jsonObject1.getString("VALUE"));
            tblCubeYS202201.setCodemeasure(jsonObject1.getString("CODE_MEASURE"));
            tblCubeYS202201.setCodeentity(jsonObject1.getString("CODE_ENTITY"));
            tblCubeYS202201.setCodeversion(jsonObject1.getString("CODE_VERSION"));
            tblCubeYS202201.setCodemvtype(jsonObject1.getString("CODE_MVTYPE"));

            list.add(tblCubeYS202201);
            BigDecimal bigDecimal1 = tblCubeYS202201Mapper.selectCountByTCYID(tblCubeYS202201.getTcyId());
            if (bigDecimal1.intValue() == 0|| bigDecimal1 ==null){
                tblCubeYS202201Mapper.insertTCY(tblCubeYS202201);
            }else {
                tblCubeYS202201Mapper.updateTCY(tblCubeYS202201);
            }
//            System.out.println(tblCubeBbmx);
        }
    }
}
