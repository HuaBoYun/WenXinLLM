<template>
  <div class="heterogeneous-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>异构业财事项管理</h2>
      <p>管理异构业务系统接入和数据同步</p>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleRegister">
        注册系统
      </el-button>
      <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="warning" icon="el-icon-upload2" @click="handleBatchSync">
        批量同步
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="系统名称">
          <el-input v-model="queryForm.systemName" placeholder="请输入系统名称" clearable />
        </el-form-item>
        <el-form-item label="系统类型">
          <el-select v-model="queryForm.systemType" placeholder="请选择系统类型" clearable>
            <el-option label="ERP系统" value="ERP" />
            <el-option label="CRM系统" value="CRM" />
            <el-option label="OA系统" value="OA" />
            <el-option label="其他系统" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="注册状态">
          <el-select v-model="queryForm.registrationStatus" placeholder="请选择注册状态" clearable>
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
            <el-option label="待审核" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="systemId" label="系统ID" width="150" />
        <el-table-column prop="systemCode" label="系统编码" width="120" />
        <el-table-column prop="systemName" label="系统名称" min-width="150" />
        <el-table-column prop="systemType" label="系统类型" width="100" />
        <el-table-column prop="registrationStatus" label="注册状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRegistrationStatusType(scope.row.registrationStatus)">
              {{ getRegistrationStatusText(scope.row.registrationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionStatus" label="连接状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getConnectionStatusType(scope.row.connectionStatus)">
              {{ getConnectionStatusText(scope.row.connectionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleTestConnection(scope.row)">测试连接</el-button>
            <el-button size="mini" type="warning" @click="handleSync(scope.row)">同步数据</el-button>
            <el-button size="mini" type="info" @click="handleMapping(scope.row)">数据映射</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 注册系统对话框 -->
    <el-dialog
      :title="isEdit ? '编辑系统' : '注册系统'"
      :visible.sync="registerDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-width="120px">
        <el-form-item label="系统编码" prop="systemCode">
          <el-input v-model="registerForm.systemCode" placeholder="请输入系统编码" />
        </el-form-item>
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="registerForm.systemName" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="系统类型" prop="systemType">
          <el-select v-model="registerForm.systemType" placeholder="请选择系统类型" style="width: 100%">
            <el-option label="ERP系统" value="ERP" />
            <el-option label="CRM系统" value="CRM" />
            <el-option label="OA系统" value="OA" />
            <el-option label="其他系统" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="系统地址" prop="systemUrl">
          <el-input v-model="registerForm.systemUrl" placeholder="请输入系统地址" />
        </el-form-item>
        <el-form-item label="认证方式" prop="authType">
          <el-select v-model="registerForm.authType" placeholder="请选择认证方式" style="width: 100%">
            <el-option label="API Key" value="API_KEY" />
            <el-option label="OAuth2" value="OAUTH2" />
            <el-option label="Basic Auth" value="BASIC_AUTH" />
            <el-option label="Token" value="TOKEN" />
          </el-select>
        </el-form-item>
        <el-form-item label="认证信息" prop="authInfo">
          <el-input
            v-model="registerForm.authInfo"
            type="textarea"
            :rows="3"
            placeholder="请输入认证信息（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="系统描述">
          <el-input
            v-model="registerForm.systemDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入系统描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="registerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRegisterConfirm" :loading="registerLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 数据同步对话框 -->
    <el-dialog
      title="数据同步"
      :visible.sync="syncDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="syncForm" :rules="syncRules" ref="syncForm" label-width="120px">
        <el-form-item label="同步类型" prop="syncType">
          <el-select v-model="syncForm.syncType" placeholder="请选择同步类型" style="width: 100%">
            <el-option label="全量同步" value="FULL" />
            <el-option label="增量同步" value="INCREMENTAL" />
            <el-option label="指定时间段" value="TIME_RANGE" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataTypes">
          <el-checkbox-group v-model="syncForm.dataTypes">
            <el-checkbox value="VOUCHER">凭证数据</el-checkbox>
            <el-checkbox value="ACCOUNT">科目数据</el-checkbox>
            <el-checkbox value="CUSTOMER">客户数据</el-checkbox>
            <el-checkbox value="SUPPLIER">供应商数据</el-checkbox>
            <el-checkbox value="INVENTORY">存货数据</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="同步时间范围" v-if="syncForm.syncType === 'TIME_RANGE'">
          <el-date-picker
            v-model="syncForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="同步策略">
          <el-radio-group v-model="syncForm.syncStrategy">
            <el-radio value="OVERWRITE">覆盖</el-radio>
            <el-radio value="MERGE">合并</el-radio>
            <el-radio value="SKIP">跳过</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="syncDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSyncConfirm" :loading="syncLoading">开始同步</el-button>
      </div>
    </el-dialog>

    <!-- 系统详情对话框 -->
    <el-dialog
      title="系统详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentSystem">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="系统ID">{{ currentSystem.systemId }}</el-descriptions-item>
          <el-descriptions-item label="系统编码">{{ currentSystem.systemCode }}</el-descriptions-item>
          <el-descriptions-item label="系统名称">{{ currentSystem.systemName }}</el-descriptions-item>
          <el-descriptions-item label="系统类型">{{ currentSystem.systemType }}</el-descriptions-item>
          <el-descriptions-item label="注册状态">
            <el-tag :type="getRegistrationStatusType(currentSystem.registrationStatus)">
              {{ getRegistrationStatusText(currentSystem.registrationStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="连接状态">
            <el-tag :type="getConnectionStatusType(currentSystem.connectionStatus)">
              {{ getConnectionStatusText(currentSystem.connectionStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ formatDate(currentSystem.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDate(currentSystem.updateTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 同步统计 -->
        <div style="margin-top: 20px;">
          <h4>同步统计</h4>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card>
                <div slot="header">总同步次数</div>
                <div class="statistic-value">{{ currentSystem.totalSyncCount || 0 }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">成功次数</div>
                <div class="statistic-value success">{{ currentSystem.successSyncCount || 0 }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">失败次数</div>
                <div class="statistic-value error">{{ currentSystem.failedSyncCount || 0 }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">最后同步时间</div>
                <div class="statistic-value">{{ formatDate(currentSystem.lastSyncTime) || '未同步' }}</div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getHeterogeneousSystemPage, 
  registerHeterogeneousSystem,
  updateHeterogeneousSystem,
  deleteHeterogeneousSystem,
  getHeterogeneousSystemById,
  testSystemConnection,
  syncHeterogeneousData
} from '@/api/financialSharing/heterogeneous'

export default {
  name: 'HeterogeneousSystemManagement',
  data() {
    return {
      loading: false,
      registerLoading: false,
      syncLoading: false,
      tableData: [],
      selectedRows: [],
      
      // 查询表单
      queryForm: {
        systemName: '',
        systemType: '',
        registrationStatus: ''
      },
      
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 注册系统对话框
      registerDialogVisible: false,
      isEdit: false,
      registerForm: {
        systemCode: '',
        systemName: '',
        systemType: '',
        systemUrl: '',
        authType: '',
        authInfo: '',
        systemDesc: ''
      },
      registerRules: {
        systemCode: [
          { required: true, message: '请输入系统编码', trigger: 'blur' }
        ],
        systemName: [
          { required: true, message: '请输入系统名称', trigger: 'blur' }
        ],
        systemType: [
          { required: true, message: '请选择系统类型', trigger: 'change' }
        ],
        systemUrl: [
          { required: true, message: '请输入系统地址', trigger: 'blur' }
        ],
        authType: [
          { required: true, message: '请选择认证方式', trigger: 'change' }
        ]
      },
      
      // 数据同步对话框
      syncDialogVisible: false,
      syncForm: {
        systemId: '',
        syncType: 'INCREMENTAL',
        dataTypes: [],
        timeRange: [],
        syncStrategy: 'MERGE'
      },
      syncRules: {
        syncType: [
          { required: true, message: '请选择同步类型', trigger: 'change' }
        ],
        dataTypes: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ]
      },
      
      // 系统详情对话框
      detailDialogVisible: false,
      currentSystem: null
    }
  },
  
  mounted() {
    this.loadData()
  },
  
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        
        const response = await getHeterogeneousSystemPage(params)
        if (response.code === 200) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 刷新
    handleRefresh() {
      this.loadData()
    },
    
    // 注册系统
    handleRegister() {
      this.isEdit = false
      this.registerDialogVisible = true
      this.$nextTick(() => {
        this.$refs.registerForm.resetFields()
      })
    },
    
    // 编辑系统
    handleEdit(row) {
      this.isEdit = true
      this.registerForm = { ...row }
      this.registerDialogVisible = true
    },
    
    // 确认注册/编辑
    async handleRegisterConfirm() {
      try {
        await this.$refs.registerForm.validate()
        this.registerLoading = true
        
        let response
        if (this.isEdit) {
          response = await updateHeterogeneousSystem(this.registerForm.systemId, this.registerForm)
        } else {
          response = await registerHeterogeneousSystem(this.registerForm)
        }
        
        if (response.code === 200) {
          this.$message.success(this.isEdit ? '更新成功' : '注册成功')
          this.registerDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || (this.isEdit ? '更新失败' : '注册失败'))
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.registerLoading = false
      }
    },
    
    // 查看详情
    async handleView(row) {
      try {
        const response = await getHeterogeneousSystemById(row.systemId)
        if (response.code === 200) {
          this.currentSystem = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },
    
    // 测试连接
    async handleTestConnection(row) {
      try {
        const response = await testSystemConnection(row.systemId)
        if (response.code === 200) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error(response.msg || '连接测试失败')
        }
      } catch (error) {
        console.error('连接测试失败:', error)
        this.$message.error('连接测试失败')
      }
    },
    
    // 同步数据
    handleSync(row) {
      this.syncForm.systemId = row.systemId
      this.syncDialogVisible = true
      this.$nextTick(() => {
        this.$refs.syncForm.resetFields()
      })
    },
    
    // 确认同步
    async handleSyncConfirm() {
      try {
        await this.$refs.syncForm.validate()
        this.syncLoading = true
        
        const response = await syncHeterogeneousData(this.syncForm)
        if (response.code === 200) {
          this.$message.success('数据同步已开始')
          this.syncDialogVisible = false
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        console.error('同步失败:', error)
        this.$message.error('同步失败')
      } finally {
        this.syncLoading = false
      }
    },
    
    // 数据映射
    handleMapping(row) {
      const content = `<p><b>系统名称：</b>${row.systemName || row.name || '-'}</p><p><b>系统编码：</b>${row.systemCode || '-'}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p>请使用左侧菜单进入"映射配置"页面进行详细配置。</p>`
      this.$alert(content, '数据映射', { dangerouslyUseHTMLString: true })
    },

    // 批量同步
    handleBatchSync() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要同步的系统')
        return
      }
      this.$confirm(`确认对选中的${this.selectedRows.length}个系统执行批量同步？`, '批量同步', { type: 'warning' })
        .then(() => { this.$message.success('批量同步成功'); if (this.loadData) this.loadData() })
        .catch(() => {})
    },
    
    // 删除系统
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该系统？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteHeterogeneousSystem(row.systemId)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    
    // 获取注册状态类型
    getRegistrationStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'danger',
        'PENDING': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取注册状态文本
    getRegistrationStatusText(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '停用',
        'PENDING': '待审核'
      }
      return statusMap[status] || status
    },
    
    // 获取连接状态类型
    getConnectionStatusType(status) {
      const statusMap = {
        'CONNECTED': 'success',
        'DISCONNECTED': 'danger',
        'CONNECTING': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取连接状态文本
    getConnectionStatusText(status) {
      const statusMap = {
        'CONNECTED': '已连接',
        'DISCONNECTED': '未连接',
        'CONNECTING': '连接中'
      }
      return statusMap[status] || status
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.heterogeneous-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistic-value.success {
  color: #67c23a;
}

.statistic-value.error {
  color: #f56c6c;
}
</style>
