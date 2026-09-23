<template>
  <div class="seal-usage-record">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document"></i>
            印鉴使用记录
          </h2>
          <p class="page-description">查看和管理印鉴使用记录，包括使用时间、使用人员、业务场景和审批状态</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-search" @click="handleAdvancedSearch">
            高级查询
          </el-button>
          <el-button type="success" icon="el-icon-pie-chart" @click="handleStatistics">
            使用统计
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出记录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 使用统计卡片 -->
    <div class="usage-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总使用次数</div>
                <div class="card-value">{{ totalUsages }}</div>
                <div class="card-change">历史记录</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon today-icon">
                <i class="el-icon-date"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日使用</div>
                <div class="card-value">{{ todayUsages }}</div>
                <div class="card-change positive">当日统计</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon month-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">本月使用</div>
                <div class="card-value">{{ monthUsages }}</div>
                <div class="card-change">月度统计</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon user-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-info">
                <div class="card-title">使用人数</div>
                <div class="card-value">{{ activeUsers }}</div>
                <div class="card-change">活跃用户</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="记录编号">
            <el-input
              v-model="listQuery.recordNumber"
              placeholder="请输入记录编号"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="印鉴编码">
            <el-input
              v-model="listQuery.sealCode"
              placeholder="请输入印鉴编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="印鉴名称">
            <el-input
              v-model="listQuery.sealName"
              placeholder="请输入印鉴名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="使用人员">
            <el-input
              v-model="listQuery.operatorName"
              placeholder="请输入使用人员"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="业务类型">
            <el-select
              v-model="listQuery.businessType"
              placeholder="请选择业务类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="资金划转" value="FUND_TRANSFER" />
              <el-option label="投资交易" value="INVESTMENT_TRANSACTION" />
              <el-option label="票据业务" value="BILL_BUSINESS" />
              <el-option label="合同签署" value="CONTRACT_SIGNING" />
              <el-option label="授权审批" value="AUTHORIZATION_APPROVAL" />
              <el-option label="其他业务" value="OTHER_BUSINESS" />
            </el-select>
          </el-form-item>
          <el-form-item label="使用状态">
            <el-select
              v-model="listQuery.usageStatus"
              placeholder="请选择使用状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="成功" value="SUCCESS" />
              <el-option label="失败" value="FAILED" />
              <el-option label="待审核" value="PENDING" />
              <el-option label="已撤销" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="记录编号" prop="recordNumber" sortable="custom" align="center" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.recordNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="印鉴名称" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.sealName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用人员" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.operatorName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="业务类型" min-width="120" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getBusinessTypeColor(row.businessType)" size="small">
            {{ getBusinessTypeName(row.businessType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="业务单号" min-width="140" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.businessNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用金额" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.usageAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用时间" min-width="140" align="center">
        <template slot-scope="{row}">
          <span>{{ row.usageTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="80" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getUsageStatusColor(row.usageStatus)" size="small">
            {{ getUsageStatusName(row.usageStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="120" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.remark }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { generateSealUsageRecordData, handleApiError } from '@/utils/mockData'
import { getSealUsageRecordList } from '@/api/globalTreasurer/czgg'

export default {
  name: 'SealUsageRecord',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        recordNumber: undefined,
        sealCode: undefined,
        sealName: undefined,
        operatorName: undefined,
        businessType: undefined,
        usageStatus: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 尝试调用真实API
        const response = await getSealUsageRecordList(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || response.totalRecord || this.list.length
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('印鉴使用记录API调用失败，使用模拟数据:', error)
        const mockResponse = handleApiError(error, generateSealUsageRecordData, 6)
        this.list = mockResponse.data.tlist
        this.total = mockResponse.data.totalRecord

        // 显示友好提示
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    // 业务类型名称映射
    getBusinessTypeName(type) {
      const typeMap = {
        'FUND_TRANSFER': '资金划转',
        'INVESTMENT_TRADE': '投资交易',
        'BILL_BUSINESS': '票据业务',
        'CONTRACT_SIGN': '合同签署'
      }
      return typeMap[type] || type
    },
    // 业务类型颜色映射
    getBusinessTypeColor(type) {
      const colorMap = {
        'FUND_TRANSFER': 'primary',
        'INVESTMENT_TRADE': 'success',
        'BILL_BUSINESS': 'warning',
        'CONTRACT_SIGN': 'danger'
      }
      return colorMap[type] || 'info'
    },
    // 使用状态名称映射
    getUsageStatusName(status) {
      const statusMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'PENDING': '待审核',
        'CANCELLED': '已撤销'
      }
      return statusMap[status] || status
    },
    // 使用状态颜色映射
    getUsageStatusColor(status) {
      const colorMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'PENDING': 'warning',
        'CANCELLED': 'info'
      }
      return colorMap[status] || 'info'
    }
  }
}
</script>
