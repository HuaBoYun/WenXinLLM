package com.huabo.monitor;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.service.PjjgService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.impl.CsjgServiceImpl;
import com.huabo.monitor.util.ConstClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.java2d.pipe.SpanIterator;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TblAssessServiceTest {

    @Resource
    TblAssessService  tblAssessService;
    @Resource
    TblAssessMarkMapper  tblAssessMarkMapper;

    @Resource
    TblAssessMarkVoMapper tblAssessMarkVoMapper;
    @Resource
    TblAssessTargetMapper  tblAssessTargetMapper;

    @Resource
    TblStaffMapper  staffMapper;

    @Resource
    PjjgService  pjjgService;

    @Resource
    CsjgServiceImpl csjgService;


    @Resource
    YhrPageMapper pageMapper;
    @Resource
    TblTestplanMapper  testplanMapper;
    @Test
    public void testNoGen() throws Exception {
        String result =tblAssessService.findFlowNextId(
                "TBL_ASSESS","ASSESSID","TBLCOMANY",new BigDecimal(116821),309,null,null,null

        );

        System.out.println(result);
    }

    @Test
    public void testtblAssessMarkMapper() throws Exception {
        IPage<TblAssessMark> iPage = new Page<>(2, 1);
        tblAssessMarkMapper.findMarkByOrgGroup(iPage,new BigDecimal(647047));

        System.out.println(iPage.getRecords());

    }

    @Test
    public void testMarkVoMapper() throws Exception {
        IPage<TblAssessMarkVo> iPage = new Page<>(1, 10);
        this.tblAssessMarkVoMapper.queryTblAssessMarkVoPage(iPage,new BigDecimal(647047),new BigDecimal(198328));
        System.out.println(new JsonBean(200,"",iPage));

    }

    @Test
    public void testStaffVoMapper() throws Exception {
        IPage<TblAssessStaffVo> iPage = new Page<>(2, 2);
        this.tblAssessMarkVoMapper.getStaffsBymarkidToList(iPage,new BigDecimal(647143));

        System.out.println(new JsonBean(200,"",iPage));

    }
    @Test
    public void testStaffMapper() throws Exception {
        IPage<TblStaff> iPage = new Page<>(1, 5);
        this.staffMapper.findStaffByOrgid(iPage,new BigDecimal(198328));

        System.out.println(new JsonBean(200,"",iPage));

    }

    @Test
    public void testTargetMapper() throws Exception {
        System.out.println(this.tblAssessTargetMapper.getMyOneTargetVo(new BigDecimal(658762)));

    }


    @Test
    public void testPjjgService() throws Exception {


//        IPage<Map<String,Object>> iPage=this.pjjgService.findByPageBean(1,"116821","2");
//
//        System.out.println(new JsonBean(200,"",iPage));

        String sql="select lin.*,\n" +
                "       (select count(*) from TBL_TESTTASK where PLANID=lin.TESTPLANID ) allcount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=1 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) ycount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=2 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) wcount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=3 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) bcount\n" +
                "       from(\n" +
                "SELECT distinct te.* from TBL_TESTPLAN te\n" +
                "WHERE TE.ORGID in (SELECT T.ORGID FROM TBL_ORGANIZATION T CONNECT BY PRIOR T.ORGID = T.FATHERORGID and T.ORGTYPE = 0 START WITH T.ORGID = 116821)\n" +
                "  and  TESTPLANID in ( SELECT DISTINCT PLANID from TBL_TESTTASK WHERE COMPLETESTAUS=1)\n" +
                "ORDER BY TESTPLANID\n" +
                "    ) lin";

        final List<Map<String, Object>> maps =
                this.pageMapper.queryBySqlToList(sql);

        System.out.println(maps);


    }

    @Test
    public void testplanMapper() throws Exception {

//        final TblTestplanVo oneTblTestplanVo = testplanMapper.getOneTblTestplanVo(new BigDecimal(786859));
//        System.out.println(new JsonBean(200,"",oneTblTestplanVo));

//        final List<Tree> treeListByTemid = testplanMapper.getTreeListByTemid(new BigDecimal(226576));
//
//        System.out.println(new JsonBean(200,"",treeListByTemid));

        String sql="SELECT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,EMT.CONTROLREQ,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID\n" +
                "from TBL_TESTELEMENT emt\n" +
                "         left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID\n" +
                "         LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID\n" +
                "where  TASK.PLANID=801050 and EMT.TEMPLID=226576 and EMT.TYPEID=226599";
        IPage<Map<String,Object>> iPage=new Page<>(1,5);
        this.pageMapper.getPage(iPage,sql);

        System.out.println(new JsonBean(200,"",iPage));


    }



    @Test
    public void test2() throws Exception {

        final IPage<Map<String, Object>> allWCSjCSHZ = this.csjgService.findAllWCSjCSHZ(null, 1, new BigDecimal(127822),1);
        for (Map<String, Object> record : allWCSjCSHZ.getRecords()) {
            System.out.println(record);
        }

    }



}
