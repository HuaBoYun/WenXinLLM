<template>
  <div class="fund-pool-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金池管理</h2>
      <p>管理企业资金池，包括创建、配置、监控和维护资金池</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="资金池编码">
          <el-input v-model="searchForm.poolCode" placeholder="请输入资金池编码" clearable />
        </el-form-item>
        <el-form-item label="资金池名称">
          <el-input v-model="searchForm.poolName" placeholder="请输入资金池名称" clearable />
        </el-form-item>
        <el-form-item label="资金池类型">
          <el-select v-model="searchForm.poolType" placeholder="请选择资金池类型" clearable>
            <el-option label="境内" value="DOMESTIC" />
            <el-option label="境外" value="OFFSHORE" />
            <el-option label="混合" value="MIXED" />
          </el-select>
        </el-form-item>
        <el-form-item label="资金池状态">
          <el-select v-model="searchForm.poolStatus" placeholder="请选择资金池状态" clearable>
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="暂停" value="SUSPENDED" />
            <el-option label="关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="searchForm.currencyCode" placeholder="请选择币种" clearable>
            <el-option label="人民币" value="CNY" />
            <el-option label="美元" value="USD" />
            <el-option label="欧元" value="EUR" />
            <el-option label="日元" value="JPY" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增资金池</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-refresh" size="mini" @click="handleSync">同步余额</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="poolList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
        height="500"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="资金池编码" prop="poolCode" width="120" show-overflow-tooltip />
        <el-table-column label="资金池名称" prop="poolName" width="150" show-overflow-tooltip />
        <el-table-column label="资金池类型" prop="poolType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPoolTypeTag(scope.row.poolType)">
              {{ getPoolTypeText(scope.row.poolType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currencyCode" width="80" align="center" />
        <el-table-column label="总余额" prop="totalBalance" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.totalBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="可用余额" prop="availableBalance" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.availableBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="保留余额" prop="reserveBalance" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.reserveBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="使用率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="getUtilizationRate(scope.row)"
              :color="getUtilizationColor(scope.row)"
              :stroke-width="8"
              text-inside
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="poolStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.poolStatus)">
              {{ getStatusText(scope.row.poolStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="150" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" style="margin-left: 10px">
              <span class="el-dropdown-link">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="members" icon="el-icon-user">成员管理</el-dropdown-item>
                <el-dropdown-item command="rules" icon="el-icon-setting">归集规则</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">操作历史</el-dropdown-item>
                <el-dropdown-item command="sync" icon="el-icon-refresh">同步余额</el-dropdown-item>
                <el-dropdown-item command="activate" v-if="scope.row.poolStatus !== 'ACTIVE'" icon="el-icon-check">激活</el-dropdown-item>
                <el-dropdown-item command="suspend" v-if="scope.row.poolStatus === 'ACTIVE'" icon="el-icon-warning">暂停</el-dropdown-item>
                <el-dropdown-item command="close" v-if="scope.row.poolStatus !== 'CLOSED'" icon="el-icon-close">关闭</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.current"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资金池编码" prop="poolCode">
              <el-input v-model="form.poolCode" placeholder="请输入资金池编码" :disabled="form.poolId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资金池名称" prop="poolName">
              <el-input v-model="form.poolName" placeholder="请输入资金池名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资金池类型" prop="poolType">
              <el-select v-model="form.poolType" placeholder="请选择资金池类型" style="width: 100%">
                <el-option label="境内" value="DOMESTIC" />
                <el-option label="境外" value="OFFSHORE" />
                <el-option label="混合" value="MIXED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="form.currencyCode" placeholder="请选择币种" style="width: 100%">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主账户ID" prop="masterAccountId">
              <el-input v-model="form.masterAccountId" placeholder="请输入主账户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主账户号码" prop="masterAccountNumber">
              <el-input v-model="form.masterAccountNumber" placeholder="请输入主账户号码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最小余额">
              <el-input v-model="form.minBalance" placeholder="请输入最小余额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大余额">
              <el-input v-model="form.maxBalance" placeholder="请输入最大余额" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="保留余额">
              <el-input v-model="form.reserveBalance" placeholder="请输入保留余额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主资金池">
              <el-select v-model="form.masterPoolId" placeholder="请选择主资金池" clearable style="width: 100%">
                <el-option
                  v-for="pool in masterPoolOptions"
                  :key="pool.poolId"
                  :label="pool.poolName"
                  :value="pool.poolId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" :rows="3" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="资金池详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="资金池编码">{{ detailData.poolCode }}</el-descriptions-item>
        <el-descriptions-item label="资金池名称">{{ detailData.poolName }}</el-descriptions-item>
        <el-descriptions-item label="资金池类型">
          <el-tag :type="getPoolTypeTag(detailData.poolType)">
            {{ getPoolTypeText(detailData.poolType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="币种">{{ detailData.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.poolStatus)">
            {{ getStatusText(detailData.poolStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="主账户号码">{{ detailData.masterAccountNumber }}</el-descriptions-item>
        <el-descriptions-item label="总余额">{{ formatAmount(detailData.totalBalance) }}</el-descriptions-item>
        <el-descriptions-item label="可用余额">{{ formatAmount(detailData.availableBalance) }}</el-descriptions-item>
        <el-descriptions-item label="保留余额">{{ formatAmount(detailData.reserveBalance) }}</el-descriptions-item>
        <el-descriptions-item label="最小余额">{{ formatAmount(detailData.minBalance) }}</el-descriptions-item>
        <el-descriptions-item label="最大余额">{{ formatAmount(detailData.maxBalance) }}</el-descriptions-item>
        <el-descriptions-item label="使用率">
          <el-progress
            :percentage="getUtilizationRate(detailData)"
            :color="getUtilizationColor(detailData)"
            :stroke-width="8"
          />
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="3">
          {{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="3">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { fundPoolApi } from '@/api/globalTreasurer/zjjz'
import { parseTime } from '@/utils'

export default {
  name: 'FundPool',
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 资金池表格数据
      poolList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 主资金池选项
      masterPoolOptions: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        poolCode: null,
        poolName: null,
        poolType: null,
        poolStatus: null,
        currencyCode: null,
        orgId: this.$store.getters.orgId
      },
      // 搜索表单
      searchForm: {
        poolCode: '',
        poolName: '',
        poolType: '',
        poolStatus: '',
        currencyCode: ''
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        poolCode: [
          { required: true, message: '资金池编码不能为空', trigger: 'blur' }
        ],
        poolName: [
          { required: true, message: '资金池名称不能为空', trigger: 'blur' }
        ],
        poolType: [
          { required: true, message: '资金池类型不能为空', trigger: 'change' }
        ],
        currencyCode: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ],
        masterAccountId: [
          { required: true, message: '主账户ID不能为空', trigger: 'blur' }
        ],
        masterAccountNumber: [
          { required: true, message: '主账户号码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getMasterPoolOptions()
  },
  methods: {
    parseTime,
    /** 查询资金池列表 */
    getList() {
      this.loading = true
      fundPoolApi.getFundPoolPage(this.queryParams).then(response => {
        this.poolList = response.data.records
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 获取主资金池选项 */
    getMasterPoolOptions() {
      fundPoolApi.getMasterFundPools({ orgId: this.$store.getters.orgId }).then(response => {
        this.masterPoolOptions = response.data
      })
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      Object.assign(this.queryParams, this.searchForm)
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        poolCode: '',
        poolName: '',
        poolType: '',
        poolStatus: '',
        currencyCode: ''
      }
      this.queryParams = {
        current: 1,
        size: 10,
        poolCode: null,
        poolName: null,
        poolType: null,
        poolStatus: null,
        currencyCode: null,
        orgId: this.$store.getters.orgId
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加资金池'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const poolId = row.poolId || this.ids
      fundPoolApi.getFundPoolById(poolId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改资金池'
      })
    },
    /** 查看详情 */
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.poolId != null) {
            fundPoolApi.updateFundPool(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            fundPoolApi.createFundPool(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const poolIds = row.poolId || this.ids
      this.$modal.confirm('是否确认删除资金池编号为"' + poolIds + '"的数据项？').then(function() {
        return fundPoolApi.deleteFundPool(poolIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('fund-concentration/pools/export', {
        ...this.queryParams
      }, `fund_pool_${new Date().getTime()}.xlsx`)
    },
    /** 同步余额 */
    handleSync() {
      this.$modal.confirm('是否确认同步所有资金池余额？').then(() => {
        // 批量同步余额逻辑
        this.$modal.msgSuccess('同步成功')
        this.getList()
      }).catch(() => {})
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.poolId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 更多操作命令 */
    handleCommand(command, row) {
      switch (command) {
        case 'members':
          this.$router.push({ path: '/globalTreasurer/zjjz/zjccy', query: { poolId: row.poolId } })
          break
        case 'rules':
          this.$router.push({ path: '/globalTreasurer/zjjz/gjgz', query: { poolId: row.poolId } })
          break
        case 'history':
          this.handleViewHistory(row)
          break
        case 'sync':
          this.handleSyncBalance(row)
          break
        case 'activate':
          this.handleActivate(row)
          break
        case 'suspend':
          this.handleSuspend(row)
          break
        case 'close':
          this.handleClose(row)
          break
      }
    },
    /** 激活资金池 */
    handleActivate(row) {
      this.$modal.confirm('是否确认激活资金池"' + row.poolName + '"？').then(() => {
        return fundPoolApi.activateFundPool(row.poolId)
      }).then(() => {
        this.$modal.msgSuccess('激活成功')
        this.getList()
      }).catch(() => {})
    },
    /** 暂停资金池 */
    handleSuspend(row) {
      this.$prompt('请输入暂停原因', '暂停资金池', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return fundPoolApi.suspendFundPool(row.poolId, value)
      }).then(() => {
        this.$modal.msgSuccess('暂停成功')
        this.getList()
      }).catch(() => {})
    },
    /** 关闭资金池 */
    handleClose(row) {
      this.$prompt('请输入关闭原因', '关闭资金池', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return fundPoolApi.closeFundPool(row.poolId, value)
      }).then(() => {
        this.$modal.msgSuccess('关闭成功')
        this.getList()
      }).catch(() => {})
    },
    /** 同步单个资金池余额 */
    handleSyncBalance(row) {
      this.$modal.confirm('是否确认同步资金池"' + row.poolName + '"的余额？').then(() => {
        return fundPoolApi.syncFundPoolBalance(row.poolId)
      }).then(() => {
        this.$modal.msgSuccess('同步成功')
        this.getList()
      }).catch(() => {})
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        poolId: null,
        poolCode: null,
        poolName: null,
        poolType: null,
        masterPoolId: null,
        masterAccountId: null,
        masterAccountNumber: null,
        currencyCode: null,
        minBalance: null,
        maxBalance: null,
        reserveBalance: null,
        description: null,
        remark: null,
        orgId: this.$store.getters.orgId
      }
      this.resetForm('form')
    },
    /** 获取资金池类型标签 */
    getPoolTypeTag(poolType) {
      const tagMap = {
        'DOMESTIC': 'success',
        'OFFSHORE': 'warning',
        'MIXED': 'info'
      }
      return tagMap[poolType] || ''
    },
    /** 获取资金池类型文本 */
    getPoolTypeText(poolType) {
      const textMap = {
        'DOMESTIC': '境内',
        'OFFSHORE': '境外',
        'MIXED': '混合'
      }
      return textMap[poolType] || poolType
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'CLOSED': 'danger'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'SUSPENDED': '暂停',
        'CLOSED': '关闭'
      }
      return textMap[status] || status
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    /** 获取使用率 */
    getUtilizationRate(row) {
      if (!row.maxBalance || row.maxBalance <= 0) return 0
      return Math.round((row.totalBalance / row.maxBalance) * 100)
    },
    /** 获取使用率颜色 */
    getUtilizationColor(row) {
      const rate = this.getUtilizationRate(row)
      if (rate >= 90) return '#f56c6c'
      if (rate >= 70) return '#e6a23c'
      return '#67c23a'
    }
  }
}
</script>

<style scoped>
.fund-pool-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: bold;
  color: #409eff;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}
</style>
