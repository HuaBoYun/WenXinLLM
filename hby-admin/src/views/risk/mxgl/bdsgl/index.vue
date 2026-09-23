<template>
  <div class="expression-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>表达式管理</h2>
      <p>管理业务规则表达式，支持可视化编辑、语法验证和执行测试</p>
    </div>

    <!-- 功能导航卡片 -->
    <div class="function-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="function-card" shadow="hover" @click.native="openExpressionDialog">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="card-info">
                <h3>表达式管理</h3>
                <p>创建、编辑和管理业务规则表达式</p>
                <div class="card-stats">
                  <span>总表达式: {{ statistics.totalCount || 0 }}</span>
                  <span>已启用: {{ statistics.enabledCount || 0 }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="function-card" shadow="hover" @click.native="openExpressionEditor">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="card-info">
                <h3>可视化编辑器</h3>
                <p>拖拽式表达式构建器，支持复杂逻辑</p>
                <div class="card-stats">
                  <span>模板数: {{ statistics.templateCount || 0 }}</span>
                  <span>类型数: {{ statistics.typeCount || 0 }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="function-card" shadow="hover" @click.native="openExpressionTest">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-info">
                <h3>表达式测试</h3>
                <p>验证表达式语法和执行效果</p>
                <div class="card-stats">
                  <span>系统预置: {{ statistics.systemCount || 0 }}</span>
                  <span>分类数: {{ statistics.categoryCount || 0 }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速统计 -->
    <div class="quick-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ recentStats.totalExpressions }}</div>
            <div class="stat-label">总表达式数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ recentStats.totalExecutions }}</div>
            <div class="stat-label">总执行次数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ recentStats.activeTemplates }}</div>
            <div class="stat-label">活跃模板数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ recentStats.avgComplexity }}</div>
            <div class="stat-label">平均复杂度</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activity">
      <el-card>
        <div slot="header" class="card-header">
          <span>最近活动</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="refreshActivity">刷新</el-button>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="activity in recentActivities"
            :key="activity.id"
            :timestamp="activity.time"
            :type="activity.type"
          >
            <div class="activity-content">
              <h4>{{ activity.title }}</h4>
              <p>{{ activity.description }}</p>
              <div class="activity-meta">
                <el-tag size="mini" :type="getActivityTagType(activity.action)">{{ activity.action }}</el-tag>
                <span class="activity-user">{{ activity.user }}</span>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <!-- 表达式管理对话框 -->
    <ExpressionDialog
      :visible.sync="expressionDialogVisible"
      @refresh="loadStatistics"
    />

    <!-- 表达式编辑器对话框 -->
    <ExpressionEditorDialog
      :visible.sync="expressionEditorVisible"
      @refresh="loadStatistics"
    />

    <!-- 表达式测试对话框 -->
    <ExpressionTestDialog
      :visible.sync="expressionTestVisible"
      @refresh="loadStatistics"
    />
  </div>
</template>

<script>
import {
  getExpressionStatistics,
  getExpressionTypeStatistics,
  getExpressionCategoryStatistics,
  getExpressionList
} from '@/api/mxgl'
import ExpressionDialog from './components/ExpressionDialog'
import ExpressionEditorDialog from './components/ExpressionEditorDialog'
import ExpressionTestDialog from './components/ExpressionTestDialog'

export default {
  name: 'ExpressionManagement',
  components: {
    ExpressionDialog,
    ExpressionEditorDialog,
    ExpressionTestDialog
  },
  data() {
    return {
      // 统计信息
      statistics: {
        totalCount: 0,
        enabledCount: 0,
        templateCount: 0,
        systemCount: 0,
        typeCount: 0,
        categoryCount: 0
      },
      // 快速统计
      recentStats: {
        totalExpressions: 0,
        totalExecutions: 0,
        activeTemplates: 0,
        avgComplexity: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          title: '创建新表达式',
          description: '创建了采购金额验证表达式',
          action: '创建',
          user: '张三',
          time: '2025-01-21 10:30',
          type: 'success'
        },
        {
          id: 2,
          title: '更新表达式模板',
          description: '更新了风险评估模板表达式',
          action: '更新',
          user: '李四',
          time: '2025-01-21 09:15',
          type: 'primary'
        },
        {
          id: 3,
          title: '执行表达式测试',
          description: '测试了供应商评级表达式',
          action: '测试',
          user: '王五',
          time: '2025-01-21 08:45',
          type: 'info'
        }
      ],
      // 对话框状态
      expressionDialogVisible: false,
      expressionEditorVisible: false,
      expressionTestVisible: false
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadRecentStats()
  },
  methods: {
    // 加载统计信息
    async loadStatistics() {
      try {
        const response = await getExpressionStatistics()
        if (response.code === 1) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('获取表达式统计信息失败:', error)
      }
    },

    // 加载快速统计
    async loadRecentStats() {
      try {
        // 获取表达式列表统计
        const listResponse = await getExpressionList({
          pageNum: 1,
          pageSize: 1,
          isEnabled: 'Y'
        })
        if (listResponse.code === 1) {
          this.recentStats.totalExpressions = listResponse.data.total || 0
        }

        // 获取类型统计
        const typeResponse = await getExpressionTypeStatistics()
        if (typeResponse.code === 1) {
          const typeStats = typeResponse.data || []
          this.recentStats.totalExecutions = typeStats.reduce((sum, item) => sum + (item.TOTAL_USAGE || 0), 0)
        }

        // 获取分类统计
        const categoryResponse = await getExpressionCategoryStatistics()
        if (categoryResponse.code === 1) {
          const categoryStats = categoryResponse.data || []
          this.recentStats.activeTemplates = categoryStats.length
          const avgComplexity = categoryStats.reduce((sum, item) => sum + (item.AVG_COMPLEXITY || 0), 0) / categoryStats.length
          this.recentStats.avgComplexity = avgComplexity ? avgComplexity.toFixed(1) : 0
        }
      } catch (error) {
        console.error('获取快速统计失败:', error)
      }
    },

    // 打开表达式管理对话框
    openExpressionDialog() {
      this.expressionDialogVisible = true
    },

    // 打开表达式编辑器
    openExpressionEditor() {
      this.expressionEditorVisible = true
    },

    // 打开表达式测试
    openExpressionTest() {
      this.expressionTestVisible = true
    },

    // 刷新活动
    refreshActivity() {
      // TODO: 实现刷新最近活动
      this.$message.success('活动已刷新')
    },

    // 获取活动标签类型
    getActivityTagType(action) {
      const typeMap = {
        '创建': 'success',
        '更新': 'primary',
        '删除': 'danger',
        '测试': 'info',
        '执行': 'warning'
      }
      return typeMap[action] || 'info'
    }
  }
}
</script>

<style scoped>
.expression-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 500;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.function-cards {
  margin-bottom: 30px;
}

.function-card {
  cursor: pointer;
  transition: all 0.3s;
  height: 140px;
}

.function-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.card-icon {
  margin-right: 20px;
}

.card-icon i {
  font-size: 48px;
  color: #409eff;
}

.card-info h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 500;
}

.card-info p {
  margin: 0 0 10px 0;
  color: #606266;
  font-size: 14px;
}

.card-stats {
  display: flex;
  gap: 15px;
}

.card-stats span {
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  padding: 2px 8px;
  border-radius: 4px;
}

.quick-stats {
  margin-bottom: 30px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  color: white;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.recent-activity {
  margin-bottom: 30px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-content h4 {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.activity-content p {
  margin: 0 0 10px 0;
  color: #606266;
  font-size: 14px;
}

.activity-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.activity-user {
  font-size: 12px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .function-cards .el-col {
    margin-bottom: 20px;
  }
  
  .quick-stats .el-col {
    margin-bottom: 15px;
  }
  
  .card-content {
    flex-direction: column;
    text-align: center;
  }
  
  .card-icon {
    margin-right: 0;
    margin-bottom: 15px;
  }
}
</style>
