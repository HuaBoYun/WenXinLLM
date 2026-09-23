<template>
  <div class="cloud-connection-contract-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document-copy"></i>
            云连接合同管理
          </h2>
          <p class="page-description">管理云连接服务合同，包括合同签署、条款管理、履约监控和续约提醒</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增合同
          </el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleRenewalAlert">
            续约提醒
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出合同
          </el-button>
        </div>
      </div>
    </div>

    <!-- 合同统计卡片 -->
    <div class="contract-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总合同数</div>
                <div class="card-value">{{ totalContracts }}</div>
                <div class="card-change">已签署合同</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">执行中</div>
                <div class="card-value">{{ activeContracts }}</div>
                <div class="card-change positive">正常履约</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon expiring-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将到期</div>
                <div class="card-value">{{ expiringContracts }}</div>
                <div class="card-change negative">需要续约</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">合同总额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
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
          <el-form-item label="合同编号">
            <el-input
              v-model="listQuery.contractNumber"
              placeholder="请输入合同编号"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="合同名称">
            <el-input
              v-model="listQuery.contractName"
              placeholder="请输入合同名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="服务提供商">
            <el-select
              v-model="listQuery.serviceProvider"
              placeholder="请选择服务提供商"
              clearable
              style="width: 150px;"
            >
              <el-option label="阿里云" value="ALIYUN" />
              <el-option label="腾讯云" value="TENCENT_CLOUD" />
              <el-option label="华为云" value="HUAWEI_CLOUD" />
              <el-option label="百度云" value="BAIDU_CLOUD" />
              <el-option label="京东云" value="JDCLOUD" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="服务类型">
            <el-select
              v-model="listQuery.serviceType"
              placeholder="请选择服务类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="云服务器" value="ECS" />
              <el-option label="云数据库" value="RDS" />
              <el-option label="云存储" value="OSS" />
              <el-option label="CDN服务" value="CDN" />
              <el-option label="负载均衡" value="SLB" />
              <el-option label="API网关" value="API_GATEWAY" />
            </el-select>
          </el-form-item>
          <el-form-item label="合同状态">
            <el-select
              v-model="listQuery.contractStatus"
              placeholder="请选择合同状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="待审批" value="PENDING_APPROVAL" />
              <el-option label="已签署" value="SIGNED" />
              <el-option label="执行中" value="ACTIVE" />
              <el-option label="已到期" value="EXPIRED" />
              <el-option label="已终止" value="TERMINATED" />
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
      <el-table-column label="合同编号" prop="contractNumber" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.contractNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.contractName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务提供商" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getServiceProviderColor(row.serviceProvider)" size="small">
            {{ getServiceProviderName(row.serviceProvider) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="服务类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getServiceTypeColor(row.serviceType)" size="small">
            {{ getServiceTypeName(row.serviceType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="合同金额" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.contractAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="签署日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.signDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.effectiveDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expiryDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同状态" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getContractStatusColor(row.contractStatus)" size="small">
            {{ getContractStatusName(row.contractStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
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

export default {
  name: 'CloudConnectionContractManage',
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
        name: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            name: '示例数据',
            description: '这是一个示例数据',
            status: 1
          }
        ]
        this.total = 1
        this.listLoading = false
      }, 1000)
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
    }
  }
}
</script>
