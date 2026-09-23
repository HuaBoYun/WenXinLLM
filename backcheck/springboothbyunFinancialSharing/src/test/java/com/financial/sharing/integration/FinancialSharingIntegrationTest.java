package com.financial.sharing.integration;

import com.financial.sharing.SpringbootHbyunFinancialSharingApplication;
import com.financial.sharing.util.MyJsonBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 财务共享系统集成测试
 * 
 * @author Financial Sharing System
 * @since 2024-01-01
 */
@SpringBootTest(classes = SpringbootHbyunFinancialSharingApplication.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class FinancialSharingIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试总账模块API
     */
    @Test
    public void testGeneralLedgerModule() throws Exception {
        setUp();
        
        // 测试获取科目余额
        MvcResult result = mockMvc.perform(get("/financial/generalLedger/subjectBalance")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MyJsonBean response = objectMapper.readValue(responseContent, MyJsonBean.class);
        
        assertEquals("200", response.getCode());
        assertEquals("查询成功", response.getMsg());
        assertNotNull(response.getData());
        
        System.out.println("总账模块测试通过: " + responseContent);
    }

    /**
     * 测试固定资产模块API
     */
    @Test
    public void testFixedAssetsModule() throws Exception {
        setUp();
        
        // 测试获取资产卡片列表
        MvcResult result = mockMvc.perform(get("/financial/fixedAssets/assetCards")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MyJsonBean response = objectMapper.readValue(responseContent, MyJsonBean.class);
        
        assertEquals("200", response.getCode());
        assertEquals("查询成功", response.getMsg());
        assertNotNull(response.getData());
        
        System.out.println("固定资产模块测试通过: " + responseContent);
    }

    /**
     * 测试成本中心模块API
     */
    @Test
    public void testCostCenterModule() throws Exception {
        setUp();
        
        // 测试获取成本中心列表
        MvcResult result = mockMvc.perform(get("/financial/costCenter/centers")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MyJsonBean response = objectMapper.readValue(responseContent, MyJsonBean.class);
        
        assertEquals("200", response.getCode());
        assertEquals("查询成功", response.getMsg());
        assertNotNull(response.getData());
        
        System.out.println("成本中心模块测试通过: " + responseContent);
    }

    /**
     * 测试内部结算模块API
     */
    @Test
    public void testInternalSettlementModule() throws Exception {
        setUp();
        
        // 测试获取内部交易列表
        MvcResult result = mockMvc.perform(get("/financial/internalSettlement/transactions")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MyJsonBean response = objectMapper.readValue(responseContent, MyJsonBean.class);
        
        assertEquals("200", response.getCode());
        assertEquals("查询成功", response.getMsg());
        assertNotNull(response.getData());
        
        System.out.println("内部结算模块测试通过: " + responseContent);
    }

    /**
     * 测试报表分析模块API
     */
    @Test
    public void testReportsModule() throws Exception {
        setUp();
        
        // 测试获取财务报表列表
        MvcResult result = mockMvc.perform(get("/financial/reports/financialReports")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MyJsonBean response = objectMapper.readValue(responseContent, MyJsonBean.class);
        
        assertEquals("200", response.getCode());
        assertEquals("查询成功", response.getMsg());
        assertNotNull(response.getData());
        
        System.out.println("报表分析模块测试通过: " + responseContent);
    }

    /**
     * 测试事项会计中台模块API
     */
    @Test
    public void testPlatformModules() throws Exception {
        setUp();
        
        // 测试影响因素API
        MvcResult result1 = mockMvc.perform(get("/financial/common/influenceFactors")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent1 = result1.getResponse().getContentAsString();
        System.out.println("影响因素模块测试通过: " + responseContent1);

        // 测试会计科目API
        MvcResult result2 = mockMvc.perform(get("/financial/common/accountSubjects")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent2 = result2.getResponse().getContentAsString();
        System.out.println("会计科目模块测试通过: " + responseContent2);

        // 测试业务事项API
        MvcResult result3 = mockMvc.perform(get("/financial/matter/businessTransactions")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent3 = result3.getResponse().getContentAsString();
        System.out.println("业务事项模块测试通过: " + responseContent3);
    }

    /**
     * 测试系统统计数据API
     */
    @Test
    public void testSystemStatistics() throws Exception {
        setUp();
        
        // 测试各模块统计数据
        String[] statisticsUrls = {
            "/financial/generalLedger/statistics",
            "/financial/fixedAssets/statistics",
            "/financial/costCenter/statistics",
            "/financial/internalSettlement/statistics",
            "/financial/reports/statistics"
        };

        for (String url : statisticsUrls) {
            MvcResult result = mockMvc.perform(get(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            MyJsonBean response = objectMapper.readValue(responseContent, MyJsonBean.class);
            
            assertEquals("200", response.getCode());
            assertEquals("查询成功", response.getMsg());
            assertNotNull(response.getData());
            
            System.out.println("统计数据测试通过 [" + url + "]: " + responseContent);
        }
    }

    /**
     * 测试系统健康检查
     */
    @Test
    public void testSystemHealth() throws Exception {
        setUp();
        
        // 测试系统是否正常启动
        MvcResult result = mockMvc.perform(get("/actuator/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("系统健康检查通过: " + responseContent);
    }
}
