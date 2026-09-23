<template>
  <div class="drag-indicator-example">
    <div class="page-header">
      <h2>拖拽式指标创建示例</h2>
      <p>演示如何使用拖拽方式创建指标，支持表单模式和拖拽模式两种方式</p>
    </div>

    <div class="example-content">
      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="openIndicatorDialog">
          打开指标配置对话框
        </el-button>
        <el-button @click="showUsageGuide">
          使用指南
        </el-button>
      </div>

      <!-- 功能说明 -->
      <div class="feature-description">
        <el-card>
          <div slot="header">
            <span>功能特性</span>
          </div>
          <div class="features">
            <div class="feature-item">
              <i class="el-icon-edit"></i>
              <div class="feature-content">
                <h4>表单模式</h4>
                <p>传统的表单填写方式，适合熟悉SQL的用户直接编写</p>
              </div>
            </div>
            <div class="feature-item">
              <i class="el-icon-s-grid"></i>
              <div class="feature-content">
                <h4>拖拽模式</h4>
                <p>可视化的拖拽操作，从字段库拖拽字段到SQL构建器中</p>
              </div>
            </div>
            <div class="feature-item">
              <i class="el-icon-data-line"></i>
              <div class="feature-content">
                <h4>字段库</h4>
                <p>包含常用字段、表字段和函数库，支持搜索和分类</p>
              </div>
            </div>
            <div class="feature-item">
              <i class="el-icon-view"></i>
              <div class="feature-content">
                <h4>实时预览</h4>
                <p>实时生成SQL语句，支持格式化和验证</p>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 使用步骤 -->
      <div class="usage-steps">
        <el-card>
          <div slot="header">
            <span>使用步骤</span>
          </div>
          <el-steps :active="currentStep" direction="vertical">
            <el-step title="选择创建模式">
              <div slot="description">
                在"新建指标"标签页中，选择"表单模式"或"拖拽模式"
              </div>
            </el-step>
            <el-step title="配置基本信息">
              <div slot="description">
                填写指标名称、指标编码和描述信息
              </div>
            </el-step>
            <el-step title="构建SQL语句">
              <div slot="description">
                表单模式：直接编写SQL<br>
                拖拽模式：从字段库拖拽字段到SQL构建器
              </div>
            </el-step>
            <el-step title="预览和验证">
              <div slot="description">
                预览生成的SQL语句，进行格式化和语法验证
              </div>
            </el-step>
            <el-step title="保存指标">
              <div slot="description">
                点击"添加到组合"按钮，将指标添加到指标组合中
              </div>
            </el-step>
          </el-steps>
        </el-card>
      </div>

      <!-- 示例数据 -->
      <div class="example-data">
        <el-card>
          <div slot="header">
            <span>示例指标</span>
          </div>
          <el-table :data="exampleIndicators" border>
            <el-table-column prop="indicatorName" label="指标名称" />
            <el-table-column prop="indicatorCode" label="指标编码" />
            <el-table-column prop="description" label="描述" />
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" @click="previewSQL(scope.row)">
                  预览SQL
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>

    <!-- 指标配置对话框 -->
    <IndicatorConfigDialog
      :visible.sync="indicatorDialogVisible"
      :combination="mockCombination"
      @close="indicatorDialogVisible = false"
    />

    <!-- SQL预览对话框 -->
    <el-dialog
      title="SQL预览"
      :visible.sync="sqlPreviewVisible"
      width="60%"
    >
      <pre class="sql-preview">{{ previewSQLContent }}</pre>
      <div slot="footer">
        <el-button @click="sqlPreviewVisible = false">关闭</el-button>
        <el-button type="primary" @click="copySQL">复制</el-button>
      </div>
    </el-dialog>

    <!-- 使用指南对话框 -->
    <el-dialog
      title="使用指南"
      :visible.sync="guideVisible"
      width="70%"
    >
      <div class="usage-guide">
        <h3>拖拽模式使用指南</h3>

        <h4>1. 打开拖拽构建器</h4>
        <ul>
          <li>在"新建指标"标签页中选择"拖拽模式"</li>
          <li>点击"使用拖拽"按钮打开独立的构建器窗口</li>
          <li>也可以在表单模式中点击"切换到拖拽模式"</li>
        </ul>

        <h4>2. 字段库使用</h4>
        <ul>
          <li><strong>常用字段</strong>：包含企业ID、企业名称、金额等常用字段</li>
          <li><strong>表字段</strong>：选择数据表后显示该表的所有字段</li>
          <li><strong>函数库</strong>：包含聚合函数、字符串函数、日期函数等</li>
          <li><strong>SQL模板</strong>：预定义的SQL模板，可快速应用</li>
        </ul>

        <h4>3. SQL构建器使用</h4>
        <ul>
          <li><strong>SELECT子句</strong>：拖拽字段到此处，可设置别名</li>
          <li><strong>FROM子句</strong>：拖拽表名或手动输入</li>
          <li><strong>WHERE子句</strong>：拖拽字段添加条件，支持AND/OR连接</li>
          <li><strong>GROUP BY子句</strong>：拖拽需要分组的字段</li>
          <li><strong>ORDER BY子句</strong>：拖拽需要排序的字段</li>
        </ul>

        <h4>4. 操作技巧</h4>
        <ul>
          <li>使用搜索功能快速找到需要的字段</li>
          <li>点击字段也可以直接添加到SELECT子句</li>
          <li>点击"格式化"按钮美化SQL语句</li>
          <li>点击"验证"按钮检查SQL语法</li>
          <li>在预览面板中实时查看生成的SQL</li>
          <li>支持键盘快捷键：Ctrl+S保存、Ctrl+R重置、F5预览</li>
        </ul>

        <h4>5. 全屏体验</h4>
        <ul>
          <li>独立的全屏对话框，提供更大的操作空间</li>
          <li>三栏式布局：字段库、SQL构建器、预览配置</li>
          <li>实时统计显示：字段数量、条件数量等</li>
          <li>支持参数配置和基本信息设置</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="guideVisible = false">知道了</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import IndicatorConfigDialog from './components/IndicatorConfigDialog.vue'

export default {
  name: 'DragIndicatorExample',
  components: {
    IndicatorConfigDialog
  },
  data() {
    return {
      indicatorDialogVisible: false,
      sqlPreviewVisible: false,
      guideVisible: false,
      currentStep: 0,
      previewSQLContent: '',
      
      // 模拟组合数据
      mockCombination: {
        combinationId: 'COMB_001',
        combinationName: '示例指标组合',
        description: '用于演示拖拽式指标创建功能'
      },
      
      // 示例指标数据
      exampleIndicators: [
        {
          indicatorName: '企业总资产',
          indicatorCode: 'TOTAL_ASSETS',
          description: '企业总资产金额',
          sqlContent: `SELECT 
    ENTERPRISE_ID,
    ENTERPRISE_NAME,
    SUM(ASSETS) AS TOTAL_ASSETS
FROM 
    TBL_FINANCIAL_DATA
WHERE 
    STATUS = 'ACTIVE'
GROUP BY 
    ENTERPRISE_ID, ENTERPRISE_NAME
ORDER BY 
    TOTAL_ASSETS DESC`
        },
        {
          indicatorName: '营业收入增长率',
          indicatorCode: 'REVENUE_GROWTH_RATE',
          description: '企业营业收入同比增长率',
          sqlContent: `SELECT 
    ENTERPRISE_ID,
    ENTERPRISE_NAME,
    (CURRENT_REVENUE - LAST_YEAR_REVENUE) / LAST_YEAR_REVENUE * 100 AS GROWTH_RATE
FROM 
    TBL_FINANCIAL_DATA
WHERE 
    YEAR = EXTRACT(YEAR FROM SYSDATE)
ORDER BY 
    GROWTH_RATE DESC`
        },
        {
          indicatorName: '风险评估等级',
          indicatorCode: 'RISK_LEVEL',
          description: '企业综合风险评估等级',
          sqlContent: `SELECT 
    ENTERPRISE_ID,
    ENTERPRISE_NAME,
    OVERALL_RISK_LEVEL,
    COUNT(*) AS ASSESSMENT_COUNT
FROM 
    TBL_RISK_ASSESSMENT
WHERE 
    ASSESSMENT_STATUS = '已完成'
GROUP BY 
    ENTERPRISE_ID, ENTERPRISE_NAME, OVERALL_RISK_LEVEL
ORDER BY 
    ENTERPRISE_ID`
        }
      ]
    }
  },
  methods: {
    // 打开指标配置对话框
    openIndicatorDialog() {
      this.indicatorDialogVisible = true
    },
    
    // 显示使用指南
    showUsageGuide() {
      this.guideVisible = true
    },
    
    // 预览SQL
    previewSQL(indicator) {
      this.previewSQLContent = indicator.sqlContent
      this.sqlPreviewVisible = true
    },
    
    // 复制SQL
    copySQL() {
      navigator.clipboard.writeText(this.previewSQLContent).then(() => {
        this.$message.success('SQL已复制到剪贴板')
      }).catch(() => {
        this.$message.error('复制失败')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.drag-indicator-example {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;

  .page-header {
    text-align: center;
    margin-bottom: 30px;

    h2 {
      color: #303133;
      margin-bottom: 10px;
    }

    p {
      color: #606266;
      font-size: 14px;
    }
  }

  .example-content {
    max-width: 1200px;
    margin: 0 auto;

    .action-buttons {
      text-align: center;
      margin-bottom: 30px;

      .el-button {
        margin: 0 10px;
      }
    }

    .feature-description,
    .usage-steps,
    .example-data {
      margin-bottom: 30px;

      .features {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;

        .feature-item {
          display: flex;
          align-items: flex-start;
          gap: 12px;

          i {
            font-size: 24px;
            color: #409eff;
            margin-top: 4px;
          }

          .feature-content {
            h4 {
              margin: 0 0 8px 0;
              color: #303133;
              font-size: 16px;
            }

            p {
              margin: 0;
              color: #606266;
              font-size: 14px;
              line-height: 1.5;
            }
          }
        }
      }
    }
  }

  .sql-preview {
    background: #f5f7fa;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    padding: 16px;
    font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
    font-size: 12px;
    line-height: 1.5;
    color: #303133;
    white-space: pre-wrap;
    max-height: 400px;
    overflow-y: auto;
  }

  .usage-guide {
    h3 {
      color: #303133;
      margin-bottom: 20px;
    }

    h4 {
      color: #409eff;
      margin: 16px 0 8px 0;
    }

    ul {
      margin: 0 0 16px 0;
      padding-left: 20px;

      li {
        margin-bottom: 8px;
        line-height: 1.5;
        color: #606266;

        strong {
          color: #303133;
        }
      }
    }
  }
}
</style>
