<template>
  <div class="organization-structure-tab">
    <!-- 组织架构统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon total">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ organizationStatistics.totalOrganizations || 0 }}</div>
              <div class="statistics-label">组织总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon levels">
              <i class="el-icon-menu"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ organizationStatistics.maxLevel || 0 }}</div>
              <div class="statistics-label">最大层级</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon staff">
              <i class="el-icon-user"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ organizationStatistics.totalStaff || 0 }}</div>
              <div class="statistics-label">总人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon efficiency">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ organizationStatistics.avgEfficiency || 0 }}%</div>
              <div class="statistics-label">平均效率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能操作区 -->
    <el-card class="mb-20">
      <div slot="header" class="card-header">
        <span class="card-title">组织架构管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="handleAddOrganization" icon="el-icon-plus">新增组织</el-button>
          <el-button @click="handleBatchOperation" icon="el-icon-setting">批量操作</el-button>
          <el-button @click="handleViewChart" icon="el-icon-data-board">架构图</el-button>
          <el-button @click="handleExport" icon="el-icon-download">导出</el-button>
          <el-button @click="refreshData" icon="el-icon-refresh">刷新</el-button>
        </div>
      </div>

      <!-- 查询条件 -->
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="组织名称">
          <el-input v-model="queryForm.orgName" placeholder="请输入组织名称" clearable style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="组织类型">
          <el-select v-model="queryForm.orgType" placeholder="请选择组织类型" clearable style="width: 150px;">
            <el-option label="总部" value="总部"></el-option>
            <el-option label="分公司" value="分公司"></el-option>
            <el-option label="子公司" value="子公司"></el-option>
            <el-option label="部门" value="部门"></el-option>
            <el-option label="科室" value="科室"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="组织状态">
          <el-select v-model="queryForm.orgStatus" placeholder="请选择组织状态" clearable style="width: 120px;">
            <el-option label="正常" value="正常"></el-option>
            <el-option label="筹建" value="筹建"></el-option>
            <el-option label="撤销" value="撤销"></el-option>
            <el-option label="合并" value="合并"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="queryForm.leader" placeholder="请输入负责人" clearable style="width: 150px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 组织架构树形表格 -->
      <el-table
        :data="organizationList"
        v-loading="loading"
        row-key="orgId"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="orgName" label="组织名称" min-width="250" show-overflow-tooltip>
          <template slot-scope="scope">
            <span class="org-name">
              <i :class="getOrgIcon(scope.row.orgType)"></i>
              {{ scope.row.orgName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="orgCode" label="组织编码" width="150"></el-table-column>
        <el-table-column prop="orgType" label="组织类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getOrgTypeTag(scope.row.orgType)" size="small">
              {{ scope.row.orgType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgLevel" label="层级" width="80" align="center"></el-table-column>
        <el-table-column prop="leader" label="负责人" width="120"></el-table-column>
        <el-table-column prop="leaderPosition" label="职务" width="120"></el-table-column>
        <el-table-column prop="actualStaffCount" label="人员数量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.actualStaffCount || 0 }}</span>
            <span v-if="scope.row.staffEstablishment" class="text-muted">
              /{{ scope.row.staffEstablishment }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="orgStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.orgStatus)" size="small">
              {{ scope.row.orgStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="establishmentTime" label="成立时间" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.establishmentTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button @click="handleView(scope.row)" type="text" size="small">查看</el-button>
            <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'addChild', row: scope.row}">新增下级</el-dropdown-item>
                <el-dropdown-item :command="{action: 'move', row: scope.row}">移动</el-dropdown-item>
                <el-dropdown-item :command="{action: 'analysis', row: scope.row}">效能分析</el-dropdown-item>
                <el-dropdown-item :command="{action: 'staff', row: scope.row}">人员管理</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 组织类型分布图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">组织类型分布</span>
          </div>
          <div id="orgTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">人员分布统计</span>
          </div>
          <div id="staffDistributionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 管理幅度分析 -->
    <el-card class="mb-20">
      <div slot="header" class="card-header">
        <span class="card-title">管理幅度分析</span>
        <el-tag type="info" size="small">建议管理幅度：5-8人</el-tag>
      </div>
      <el-table :data="managementSpanData" style="width: 100%">
        <el-table-column prop="orgName" label="组织名称" min-width="200"></el-table-column>
        <el-table-column prop="leader" label="负责人" width="120"></el-table-column>
        <el-table-column prop="managementSpan" label="管理幅度" width="120" align="center">
          <template slot-scope="scope">
            <span :class="getSpanClass(scope.row.managementSpan)">
              {{ scope.row.managementSpan }}人
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="efficiency" label="管理效率" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="parseFloat(scope.row.efficiency || 0)" :stroke-width="8"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="suggestion" label="建议" min-width="200">
          <template slot-scope="scope">
            <span :class="getSuggestionClass(scope.row.managementSpan)">
              {{ getSuggestionText(scope.row.managementSpan) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 组织详情对话框 -->
    <OrganizationDetailDialog
      :visible.sync="orgDetailVisible"
      :organization-data="currentOrganization"
      @refresh="refreshData"
    />

    <!-- 组织编辑对话框 -->
    <OrganizationEditDialog
      :visible.sync="orgEditVisible"
      :organization-data="currentOrganization"
      :parent-org-id="parentOrgId"
      :is-edit="isEdit"
      @refresh="refreshData"
    />

    <!-- 组织架构图对话框 -->
    <OrganizationChartDialog
      :visible.sync="orgChartVisible"
      :enterprise-id="enterpriseId"
    />

    <!-- 效能分析对话框 -->
    <OrganizationAnalysisDialog
      :visible.sync="orgAnalysisVisible"
      :organization-data="currentOrganization"
    />

    <!-- 人员管理对话框 -->
    <StaffManagementDialog
      :visible.sync="staffManagementVisible"
      :organization-data="currentOrganization"
    />
  </div>
</template>

<script>
import { organizationStructureApi } from '@/api/enterprise/data'
import OrganizationDetailDialog from './OrganizationDetailDialog'
import OrganizationEditDialog from './OrganizationEditDialog'
import OrganizationChartDialog from './OrganizationChartDialog'
import OrganizationAnalysisDialog from './OrganizationAnalysisDialog'
import StaffManagementDialog from './StaffManagementDialog'

export default {
  name: 'OrganizationStructureTab',
  components: {
    OrganizationDetailDialog,
    OrganizationEditDialog,
    OrganizationChartDialog,
    OrganizationAnalysisDialog,
    StaffManagementDialog
  },
  props: {
    enterpriseId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      organizationStatistics: {},
      organizationList: [],
      managementSpanData: [],
      selectedOrganizations: [],
      queryForm: {
        enterpriseId: '',
        orgName: '',
        orgType: '',
        orgStatus: '',
        leader: ''
      },
      orgDetailVisible: false,
      orgEditVisible: false,
      orgChartVisible: false,
      orgAnalysisVisible: false,
      staffManagementVisible: false,
      currentOrganization: {},
      parentOrgId: '',
      isEdit: false,
      orgTypeChart: null,
      staffDistributionChart: null
    }
  },
  watch: {
    enterpriseId: {
      handler(newVal) {
        if (newVal) {
          this.queryForm.enterpriseId = newVal
          this.loadData()
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadOrganizationList(),
        this.loadManagementSpanData(),
        this.loadChartData()
      ])
    },

    async loadStatistics() {
      try {
        const response = await organizationStructureApi.getStatistics(this.enterpriseId)
        this.organizationStatistics = response.data || {}
      } catch (error) {
        console.error('加载组织架构统计失败:', error)
      }
    },

    async loadOrganizationList() {
      this.loading = true
      try {
        const response = await organizationStructureApi.getOrganizationTree(this.enterpriseId)
        this.organizationList = response.data || []
      } catch (error) {
        console.error('加载组织架构列表失败:', error)
        this.$message.error('加载组织架构列表失败')
      } finally {
        this.loading = false
      }
    },

    async loadManagementSpanData() {
      try {
        const response = await organizationStructureApi.getManagementSpanAnalysis(this.enterpriseId)
        this.managementSpanData = response.data || []
      } catch (error) {
        console.error('加载管理幅度数据失败:', error)
      }
    },

    async loadChartData() {
      try {
        const [typeResponse, staffResponse] = await Promise.all([
          organizationStructureApi.getOrganizationTypeDistribution(this.enterpriseId),
          organizationStructureApi.getStaffDistribution(this.enterpriseId)
        ])
        
        this.updateOrgTypeChart(typeResponse.data || [])
        this.updateStaffDistributionChart(staffResponse.data || [])
      } catch (error) {
        console.error('加载图表数据失败:', error)
      }
    },

    initCharts() {
      this.$nextTick(() => {
        this.orgTypeChart = this.$echarts.init(document.getElementById('orgTypeChart'))
        this.staffDistributionChart = this.$echarts.init(document.getElementById('staffDistributionChart'))
      })
    },

    updateOrgTypeChart(data) {
      if (!this.orgTypeChart) return
      
      const option = {
        title: {
          text: '组织类型分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '组织类型',
          type: 'pie',
          radius: '60%',
          data: data.map(item => ({
            name: item.orgType,
            value: item.count
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.orgTypeChart.setOption(option)
    },

    updateStaffDistributionChart(data) {
      if (!this.staffDistributionChart) return
      
      const option = {
        title: {
          text: '人员分布统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.orgName),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '人员数量',
          type: 'bar',
          data: data.map(item => item.staffCount),
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.staffDistributionChart.setOption(option)
    },

    handleQuery() {
      this.loadOrganizationList()
    },

    handleReset() {
      this.queryForm = {
        enterpriseId: this.enterpriseId,
        orgName: '',
        orgType: '',
        orgStatus: '',
        leader: ''
      }
      this.handleQuery()
    },

    handleSelectionChange(selection) {
      this.selectedOrganizations = selection
    },

    handleAddOrganization() {
      this.currentOrganization = { enterpriseId: this.enterpriseId }
      this.parentOrgId = ''
      this.isEdit = false
      this.orgEditVisible = true
    },

    handleView(row) {
      this.currentOrganization = row
      this.orgDetailVisible = true
    },

    handleEdit(row) {
      this.currentOrganization = { ...row }
      this.parentOrgId = row.parentOrgId
      this.isEdit = true
      this.orgEditVisible = true
    },

    async handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'addChild':
          this.currentOrganization = { enterpriseId: this.enterpriseId }
          this.parentOrgId = row.orgId
          this.isEdit = false
          this.orgEditVisible = true
          break
        case 'move':
          await this.moveOrganization(row)
          break
        case 'analysis':
          this.currentOrganization = row
          this.orgAnalysisVisible = true
          break
        case 'staff':
          this.currentOrganization = row
          this.staffManagementVisible = true
          break
        case 'delete':
          await this.deleteOrganization(row)
          break
      }
    },

    async moveOrganization(row) {
      // 实现组织移动逻辑
      this.$message.info('组织移动功能开发中')
    },

    async deleteOrganization(row) {
      try {
        await this.$confirm('确认删除该组织吗？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await organizationStructureApi.deleteOrganization(row.orgId)
        this.$message.success('删除成功')
        this.refreshData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    handleBatchOperation() {
      if (this.selectedOrganizations.length === 0) {
        this.$message.warning('请选择要操作的组织')
        return
      }
      // 实现批量操作逻辑
    },

    handleViewChart() {
      this.orgChartVisible = true
    },

    handleExport() {
      // 实现导出逻辑
    },

    refreshData() {
      this.loadData()
    },

    getOrgIcon(orgType) {
      const iconMap = {
        '总部': 'el-icon-office-building',
        '分公司': 'el-icon-house',
        '子公司': 'el-icon-school',
        '部门': 'el-icon-menu',
        '科室': 'el-icon-folder'
      }
      return iconMap[orgType] || 'el-icon-folder'
    },

    getOrgTypeTag(orgType) {
      const tagMap = {
        '总部': 'danger',
        '分公司': 'warning',
        '子公司': 'success',
        '部门': '',
        '科室': 'info'
      }
      return tagMap[orgType] || ''
    },

    getStatusTag(status) {
      const tagMap = {
        '正常': 'success',
        '筹建': 'warning',
        '撤销': 'danger',
        '合并': 'info'
      }
      return tagMap[status] || ''
    },

    getSpanClass(span) {
      if (span <= 4) return 'text-warning'
      if (span >= 9) return 'text-danger'
      return 'text-success'
    },

    getSuggestionClass(span) {
      if (span <= 4) return 'text-warning'
      if (span >= 9) return 'text-danger'
      return 'text-success'
    },

    getSuggestionText(span) {
      if (span <= 4) return '管理幅度偏小，建议适当增加下属'
      if (span >= 9) return '管理幅度偏大，建议适当减少下属或增设中层'
      return '管理幅度合理'
    },

    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleDateString('zh-CN')
    }
  }
}
</script>

<style scoped>
.organization-structure-tab {
  padding: 20px;
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
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.statistics-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.statistics-icon.levels {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.staff {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.efficiency {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.statistics-info {
  flex: 1;
}

.statistics-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.card-actions {
  display: flex;
  gap: 10px;
}

.query-form {
  margin-bottom: 20px;
}

.org-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.text-muted {
  color: #909399;
}

.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
