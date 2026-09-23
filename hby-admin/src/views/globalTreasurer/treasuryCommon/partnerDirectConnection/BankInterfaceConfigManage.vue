<template>
  <div class="bank-interface-config-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-connection"></i>
            银行接口配置管理
          </h2>
          <p class="page-description">管理银行接口配置信息，包括接口地址、认证方式、参数配置和版本管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增接口
          </el-button>
          <el-button type="success" icon="el-icon-connection" @click="handleTestAll">
            批量测试
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 接口统计卡片 -->
    <div class="interface-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-connection"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总接口数</div>
                <div class="card-value">{{ totalInterfaces }}</div>
                <div class="card-change">已配置接口</div>
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
                <div class="card-title">正常接口</div>
                <div class="card-value">{{ activeInterfaces }}</div>
                <div class="card-change positive">运行正常</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon error-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">异常接口</div>
                <div class="card-value">{{ errorInterfaces }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon test-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后测试</div>
                <div class="card-value">{{ lastTestTime }}</div>
                <div class="card-change">接口测试</div>
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
          <el-form-item label="接口编码">
            <el-input
              v-model="listQuery.interfaceCode"
              placeholder="请输入接口编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="接口名称">
            <el-input
              v-model="listQuery.interfaceName"
              placeholder="请输入接口名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="银行编码">
            <el-select
              v-model="listQuery.bankCode"
              placeholder="请选择银行"
              clearable
              style="width: 150px;"
            >
              <el-option label="工商银行" value="ICBC" />
              <el-option label="建设银行" value="CCB" />
              <el-option label="农业银行" value="ABC" />
              <el-option label="中国银行" value="BOC" />
              <el-option label="交通银行" value="BOCOM" />
              <el-option label="招商银行" value="CMB" />
              <el-option label="浦发银行" value="SPDB" />
            </el-select>
          </el-form-item>
          <el-form-item label="接口类型">
            <el-select
              v-model="listQuery.interfaceType"
              placeholder="请选择接口类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="账户查询" value="ACCOUNT_QUERY" />
              <el-option label="余额查询" value="BALANCE_QUERY" />
              <el-option label="交易查询" value="TRANSACTION_QUERY" />
              <el-option label="转账交易" value="TRANSFER" />
              <el-option label="对账文件" value="RECONCILIATION" />
              <el-option label="状态通知" value="NOTIFICATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="连接状态">
            <el-select
              v-model="listQuery.connectionStatus"
              placeholder="请选择连接状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="CONNECTED" />
              <el-option label="断开" value="DISCONNECTED" />
              <el-option label="异常" value="ERROR" />
              <el-option label="维护中" value="MAINTENANCE" />
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
      <el-table-column label="接口编码" prop="interfaceCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.interfaceCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接口名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.interfaceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="银行名称" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getBankColor(row.bankCode)" size="small">
            {{ getBankName(row.bankCode) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="接口类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getInterfaceTypeColor(row.interfaceType)" size="small">
            {{ getInterfaceTypeName(row.interfaceType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="接口地址" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.interfaceUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column label="连接状态" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getConnectionStatusColor(row.connectionStatus)" size="small">
            {{ getConnectionStatusName(row.connectionStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="超时时间" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.timeoutSeconds }}秒</span>
        </template>
      </el-table-column>
      <el-table-column label="重试次数" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.retryCount }}次</span>
        </template>
      </el-table-column>
      <el-table-column label="最后测试时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lastTestTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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
  name: 'BankInterfaceConfigManage',
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
