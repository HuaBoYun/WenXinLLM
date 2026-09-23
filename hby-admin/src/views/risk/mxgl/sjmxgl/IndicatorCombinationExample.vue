<template>
  <div class="indicator-combination-example">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1>指标组合分析</h1>
        <p>支持拖拽式配置和传统配置两种方式，提供直观的指标组合管理界面</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="openCombinationDialog">
          指标组合分析
        </el-button>
      </div>
    </div>

    <!-- 功能特性展示 -->
    <div class="feature-showcase">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="feature-card">
            <div class="feature-icon">
              <i class="el-icon-s-grid"></i>
            </div>
            <h3>拖拽式配置</h3>
            <p>直观的拖拽界面，支持指标的可视化配置和排序</p>
            <ul>
              <li>三栏式布局：指标库、配置区域、属性面板</li>
              <li>支持拖拽添加指标到配置区域</li>
              <li>可视化的指标依赖关系配置</li>
              <li>实时预览和参数配置</li>
            </ul>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="feature-card">
            <div class="feature-icon">
              <i class="el-icon-edit-outline"></i>
            </div>
            <h3>传统配置</h3>
            <p>保留原有的SQL配置方式，满足高级用户需求</p>
            <ul>
              <li>支持手写SQL语句配置指标</li>
              <li>SQL语法验证和格式化</li>
              <li>参数化查询支持</li>
              <li>模板库快速选择</li>
            </ul>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="feature-card">
            <div class="feature-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <h3>执行分析</h3>
            <p>强大的执行引擎，支持多种执行模式和结果分析</p>
            <ul>
              <li>顺序执行和并行执行模式</li>
              <li>依赖关系自动解析</li>
              <li>执行结果交集分析</li>
              <li>详细的执行历史记录</li>
            </ul>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 使用指南 -->
    <div class="usage-guide">
      <el-card>
        <div slot="header">
          <h2>使用指南</h2>
        </div>
        
        <el-steps :active="currentStep" finish-status="success" align-center>
          <el-step title="创建组合" description="新建指标组合，设置基本信息"></el-step>
          <el-step title="配置指标" description="使用拖拽或传统方式配置指标"></el-step>
          <el-step title="设置依赖" description="配置指标间的依赖关系"></el-step>
          <el-step title="执行分析" description="运行组合并查看分析结果"></el-step>
        </el-steps>
        
        <div class="step-content">
          <div v-if="currentStep === 0" class="step-detail">
            <h3>第一步：创建组合</h3>
            <p>点击"指标组合分析"按钮，在弹出的对话框中点击"新建组合"，填写组合的基本信息：</p>
            <ul>
              <li><strong>组合名称</strong>：为组合起一个有意义的名称</li>
              <li><strong>组合编码</strong>：唯一标识符，建议使用大写字母和下划线</li>
              <li><strong>业务分类</strong>：选择合适的业务分类</li>
              <li><strong>执行模式</strong>：选择顺序执行或并行执行</li>
            </ul>
          </div>
          
          <div v-if="currentStep === 1" class="step-detail">
            <h3>第二步：配置指标</h3>
            <p>有两种方式配置指标：</p>
            <div class="config-methods">
              <div class="method">
                <h4>拖拽式配置（推荐）</h4>
                <ul>
                  <li>从左侧指标库中拖拽指标到中间配置区域</li>
                  <li>或点击指标右侧的"+"按钮快速添加</li>
                  <li>在右侧属性面板中配置指标详细参数</li>
                  <li>支持拖拽调整指标执行顺序</li>
                </ul>
              </div>
              <div class="method">
                <h4>传统配置</h4>
                <ul>
                  <li>点击"传统配置"按钮打开配置对话框</li>
                  <li>选择数据模型指标或SQL模板</li>
                  <li>手动编写或修改SQL语句</li>
                  <li>配置参数映射和筛选条件</li>
                </ul>
              </div>
            </div>
          </div>
          
          <div v-if="currentStep === 2" class="step-detail">
            <h3>第三步：设置依赖</h3>
            <p>在拖拽配置模式下，可以方便地设置指标间的依赖关系：</p>
            <ul>
              <li>选择需要设置依赖的指标</li>
              <li>在右侧属性面板的"依赖关系"部分选择依赖的指标</li>
              <li>系统会自动调整执行顺序以满足依赖关系</li>
              <li>设置合理的超时时间避免长时间等待</li>
            </ul>
          </div>
          
          <div v-if="currentStep === 3" class="step-detail">
            <h3>第四步：执行分析</h3>
            <p>配置完成后，可以执行组合并查看分析结果：</p>
            <ul>
              <li>点击"执行"按钮启动指标组合分析</li>
              <li>系统会按照配置的执行模式和依赖关系运行</li>
              <li>查看执行历史和详细的结果数据</li>
              <li>支持结果数据的交集分析和导出</li>
            </ul>
          </div>
        </div>
        
        <div class="step-navigation">
          <el-button @click="prevStep" :disabled="currentStep === 0">上一步</el-button>
          <el-button @click="nextStep" :disabled="currentStep === 3">下一步</el-button>
          <el-button type="primary" @click="openCombinationDialog">开始使用</el-button>
        </div>
      </el-card>
    </div>

    <!-- 增强的指标组合分析对话框 -->
    <EnhancedIndicatorCombination
      :visible.sync="combinationDialogVisible"
      @close="combinationDialogVisible = false"
    />
  </div>
</template>

<script>
import EnhancedIndicatorCombination from './components/EnhancedIndicatorCombination.vue'

export default {
  name: 'IndicatorCombinationExample',
  components: {
    EnhancedIndicatorCombination
  },
  data() {
    return {
      combinationDialogVisible: false,
      currentStep: 0
    }
  },
  methods: {
    // 打开组合对话框
    openCombinationDialog() {
      this.combinationDialogVisible = true
    },
    
    // 上一步
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    
    // 下一步
    nextStep() {
      if (this.currentStep < 3) {
        this.currentStep++
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.indicator-combination-example {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .header-content {
      h1 {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }

      p {
        margin: 0;
        color: #606266;
        font-size: 14px;
      }
    }
  }

  .feature-showcase {
    margin-bottom: 30px;

    .feature-card {
      height: 100%;
      text-align: center;

      .feature-icon {
        font-size: 48px;
        color: #409eff;
        margin-bottom: 16px;
      }

      h3 {
        margin: 0 0 12px 0;
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }

      p {
        margin: 0 0 16px 0;
        color: #606266;
        font-size: 14px;
        line-height: 1.5;
      }

      ul {
        text-align: left;
        margin: 0;
        padding-left: 20px;
        color: #606266;
        font-size: 13px;

        li {
          margin-bottom: 4px;
          line-height: 1.4;
        }
      }
    }
  }

  .usage-guide {
    .step-content {
      margin: 30px 0;
      min-height: 200px;

      .step-detail {
        h3 {
          margin: 0 0 16px 0;
          font-size: 18px;
          font-weight: 600;
          color: #303133;
        }

        p {
          margin: 0 0 12px 0;
          color: #606266;
          line-height: 1.6;
        }

        ul {
          margin: 0 0 16px 0;
          padding-left: 20px;
          color: #606266;

          li {
            margin-bottom: 8px;
            line-height: 1.5;

            strong {
              color: #303133;
            }
          }
        }

        .config-methods {
          display: flex;
          gap: 30px;
          margin-top: 20px;

          .method {
            flex: 1;
            padding: 16px;
            border: 1px solid #e4e7ed;
            border-radius: 6px;
            background: #fafafa;

            h4 {
              margin: 0 0 12px 0;
              font-size: 16px;
              color: #409eff;
            }

            ul {
              margin: 0;
              padding-left: 16px;

              li {
                margin-bottom: 6px;
                font-size: 13px;
              }
            }
          }
        }
      }
    }

    .step-navigation {
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #e4e7ed;

      .el-button {
        margin: 0 8px;
      }
    }
  }
}
</style>
