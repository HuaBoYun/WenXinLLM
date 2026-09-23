<template>
  <div class="enterprise-data" :style="themeVars">
    <!-- 企业选择 -->
    <el-card class="mb-20">
      <el-row :gutter="20" type="flex" justify="space-between" align="middle">
        <el-col :span="12">
          <div class="enterprise-selector">
            <el-input
              :value="selectedEnterpriseName"
              placeholder="请选择企业"
              readonly
              style="width: 300px;"
              @click.native="openCompanyTree"
            >
              <el-button slot="append" icon="el-icon-search" @click="openCompanyTree"></el-button>
            </el-input>
            <CompanyTreeModal
              ref="companyTreeModal"
              @selected="handleCompanySelected"
            />
          </div>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <div class="data-controls">
            <el-button type="warning" @click="handleQualityCheck" icon="el-icon-search">质量检查</el-button>
            <el-button type="info" @click="handleExport" icon="el-icon-download">数据导出</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon data-entry">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalEntries || 0 }}</div>
              <div class="statistics-label">数据录入总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon quality-score">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.qualityScore || 0 }}%</div>
              <div class="statistics-label">数据质量评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon submission-rate">
              <i class="el-icon-upload"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.submissionRate || 0 }}%</div>
              <div class="statistics-label">报送完成率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon audit-pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.pendingAudits || 0 }}</div>
              <div class="statistics-label">待审核数据</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 数据录入 -->
        <el-tab-pane label="数据录入" name="entry">
          <DataEntryTab
            ref="dataEntryTab"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 数据质量 -->
        <el-tab-pane label="数据质量" name="quality">
          <DataQualityTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 数据报送 -->
        <el-tab-pane label="数据报送" name="submission">
          <DataSubmissionTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 数据审核 -->
        <el-tab-pane label="数据审核" name="audit">
          <DataAuditTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 数据统计 -->
        <el-tab-pane label="数据统计" name="statistics">
          <DataStatisticsTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
          />
        </el-tab-pane>

        <!-- 数据备份 -->
        <el-tab-pane label="数据备份" name="backup">
          <DataBackupTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 质量检查对话框 -->
    <QualityCheckDialog
      :visible.sync="qualityDialogVisible"
      :enterprise-id="selectedEnterpriseId"
      @refresh="handleRefresh"
    />

    <!-- 数据导出对话框 -->
    <DataExportDialog
      :visible.sync="exportDialogVisible"
      :enterprise-id="selectedEnterpriseId"
    />
  </div>
</template>

<script>
import { getDataStatistics, getDataEntryStatistics, getDataQualityStatistics, getDashboardStatistics } from '@/api/enterprise/data'
import { findOrganization } from '@/api/setting/org'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import DataEntryTab from './components/DataEntryTab'
import DataQualityTab from './components/DataQualityTab'
import DataSubmissionTab from './components/DataSubmissionTab'
import DataAuditTab from './components/DataAuditTab'
import DataStatisticsTab from './components/DataStatisticsTab'
import DataBackupTab from './components/DataBackupTab'
import QualityCheckDialog from './components/QualityCheckDialog'
import DataExportDialog from './components/DataExportDialog'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'EnterpriseData',
  mixins: [investThemeMixin],
  components: {
    CompanyTreeModal,
    DataEntryTab,
    DataQualityTab,
    DataSubmissionTab,
    DataAuditTab,
    DataStatisticsTab,
    DataBackupTab,
    QualityCheckDialog,
    DataExportDialog
  },
  data() {
    return {
      selectedEnterpriseId: '',
      selectedEnterpriseName: '',
      activeTab: 'entry',
      statistics: {},
      
      // 对话框状态
      qualityDialogVisible: false,
      exportDialogVisible: false
    }
  },
  mounted() {
    // 从路由参数获取企业ID
    if (this.$route.query.enterpriseId) {
      this.selectedEnterpriseId = this.$route.query.enterpriseId
      this.selectedEnterpriseName = this.$route.query.enterpriseName || ''
      this.loadStatistics()
    } else {
      // 无路由参数时自动加载第一个企业
      this.loadFirstEnterprise()
    }

    // 从路由参数获取默认标签页
    if (this.$route.query.tab) {
      this.activeTab = this.$route.query.tab
    }
  },
  methods: {
    // 自动加载第一个企业
    async loadFirstEnterprise() {
      try {
        const res = await findOrganization({})
        if (res && res.data && res.data.length > 0) {
          const first = res.data[0]
          this.selectedEnterpriseId = String(first.id)
          this.selectedEnterpriseName = first.name || first.label || ''
          this.handleEnterpriseChange()
        }
      } catch (e) {
        console.warn('自动加载企业失败', e)
      }
    },

    // 打开企业选择对话框
    openCompanyTree() {
      this.$refs.companyTreeModal.show()
    },

    // 企业选择确认回调
    handleCompanySelected(node) {
      if (node) {
        this.selectedEnterpriseId = String(node.id)
        this.selectedEnterpriseName = node.label || node.name || ''
        this.handleEnterpriseChange()
      }
    },

    // 企业选择变化
    handleEnterpriseChange() {
      if (this.selectedEnterpriseId) {
        this.loadStatistics()
      }
    },

    // 加载统计数据
    async loadStatistics() {
      console.log('=== 开始加载统计数据 ===')
      console.log('当前选择的企业ID:', this.selectedEnterpriseId)
      console.log('当前选择的企业名称:', this.selectedEnterpriseName)
      
      if (!this.selectedEnterpriseId) {
        console.warn('企业ID为空,无法加载统计数据')
        return
      }

      try {
        // 先尝试不传enterpriseId,看能否获取到数据
        console.log('第一步: 调用 getDashboardStatistics (不传enterpriseId)')
        const dashboardRes = await getDashboardStatistics()
        
        console.log('dashboard接口响应:', dashboardRes)
        console.log('dashboard接口响应码:', dashboardRes.code)
        console.log('dashboard接口响应数据:', dashboardRes.data)
        
        if (dashboardRes && dashboardRes.data && dashboardRes.data.totalEntries > 0) {
          console.log('不传enterpriseId获取到数据,说明数据库有数据')
          const data = dashboardRes.data
          
          this.statistics = {
            totalEntries: data.totalEntries || 0,
            qualityScore: data.qualityScore || 0,
            submissionRate: data.submissionRate || 0,
            pendingAudits: data.pendingAudits || 0
          }
          
          console.log('最终设置的statistics:', this.statistics)
        } else {
          console.log('不传enterpriseId也没有数据或数据为0,尝试传enterpriseId')
          // 再尝试传enterpriseId
          const dashboardRes2 = await getDashboardStatistics(this.selectedEnterpriseId)
          console.log('传enterpriseId的响应:', dashboardRes2)
          
          if (dashboardRes2 && dashboardRes2.data) {
            const data = dashboardRes2.data
            this.statistics = {
              totalEntries: data.totalEntries || 0,
              qualityScore: data.qualityScore || 0,
              submissionRate: data.submissionRate || 0,
              pendingAudits: data.pendingAudits || 0
            }
          }
        }
      } catch (error) {
        console.error('dashboard接口调用失败:', error)
        // 如果新接口失败,使用备用方案
        await this.loadStatisticsFallback()
      }
      
      console.log('=== 统计数据加载完成 ===')
      console.log('最终显示的统计数据:', this.statistics)
    },
    
    // 备用统计加载方案
    async loadStatisticsFallback() {
      try {
        const [entryRes, qualityRes] = await Promise.allSettled([
          getDataEntryStatistics({ enterpriseId: this.selectedEnterpriseId }),
          getDataQualityStatistics({ enterpriseId: this.selectedEnterpriseId })
        ])
        
        const entryData = (entryRes.status === 'fulfilled' && entryRes.value && entryRes.value.data) ? entryRes.value.data : {}
        const qualityData = (qualityRes.status === 'fulfilled' && qualityRes.value && qualityRes.value.data) ? qualityRes.value.data : {}
        
        this.statistics = {
          totalEntries: entryData.totalEntries || entryData.TOTALENTRIES || 0,
          qualityScore: qualityData.passRate || qualityData.PASSRATE || 0,
          submissionRate: entryData.submittedCount && entryData.totalEntries
            ? Math.round((entryData.submittedCount / entryData.totalEntries) * 100)
            : 0,
          pendingAudits: entryData.submittedCount || entryData.SUBMITTEDCOUNT || 0
        }
      } catch (error) {
        console.error('备用统计加载也失败:', error)
      }
    },

    // 标签页切换
    handleTabClick(tab) {
      // 更新路由参数
      this.$router.replace({
        query: {
          ...this.$route.query,
          tab: tab.name
        }
      })
    },

    // 刷新数据（统计 + 列表）
    handleRefresh() {
      this.loadStatistics()
      // 刷新当前激活的Tab列表
      if (this.$refs.dataEntryTab) {
        this.$refs.dataEntryTab.getList()
      }
    },

    // 质量检查
    handleQualityCheck() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }
      this.qualityDialogVisible = true
    },

    // 数据导出
    handleExport() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }
      this.exportDialogVisible = true
    }
  }
}
</script>

<style scoped>
.enterprise-data {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.enterprise-selector {
  display: flex;
  align-items: center;
}

.data-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.statistics-card {
  height: 120px;
}

.statistics-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.statistics-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  margin-right: 15px;
}

.statistics-icon.data-entry {
  background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
}

.statistics-icon.quality-score {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.submission-rate {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.audit-pending {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.statistics-label {
  font-size: 12px;
  color: #909399;
}
</style>
