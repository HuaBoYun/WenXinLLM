<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.configCode"
        placeholder="配置编码"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.configName"
        placeholder="配置名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.bankName"
        placeholder="银行名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.connectType"
        placeholder="连接类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option label="HTTP" value="HTTP" />
        <el-option label="HTTPS" value="HTTPS" />
        <el-option label="FTP" value="FTP" />
        <el-option label="SFTP" value="SFTP" />
      </el-select>
      <el-select
        v-model="listQuery.isEnabled"
        placeholder="状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="启用" value="1" />
        <el-option label="禁用" value="0" />
      </el-select>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        查询
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >
        新增
      </el-button>
      <el-button
        v-show="multipleSelection.length > 0"
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置编码" prop="configCode" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.configCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="配置名称" prop="configName" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.configName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="银行名称" prop="bankName" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.bankName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="连接类型" prop="connectType" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getConnectTypeTagType(row.connectType)">
            {{ row.connectType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="服务器地址" prop="serverUrl" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.serverUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column label="认证类型" prop="authType" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getAuthTypeTagType(row.authType)">
            {{ getAuthTypeText(row.authType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="测试状态" prop="testStatus" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getTestStatusTagType(row.testStatus)">
            {{ getTestStatusText(row.testStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="isEnabled" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="info" size="mini" @click="handleTestConnection(row)">
            测试
          </el-button>
          <el-button
            v-if="row.isEnabled === 1"
            size="mini"
            type="warning"
            @click="handleModifyStatus(row, 0)"
          >
            禁用
          </el-button>
          <el-button
            v-else
            size="mini"
            type="success"
            @click="handleModifyStatus(row, 1)"
          >
            启用
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNumber"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 编辑对话框 -->
    <BankConnectConfigEditDialog
      ref="editDialog"
      @refresh="getList"
    />
  </div>
</template>

<script>
import { getBankConnectConfigList, deleteBankConnectConfig, batchDeleteBankConnectConfig, updateBankConnectConfigStatus, testBankConnection } from '@/api/globalTreasurer/czgg'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import BankConnectConfigEditDialog from './components/BankConnectConfigEditDialog'

export default {
  name: 'BankConnectConfigManagement',
  components: { Pagination, BankConnectConfigEditDialog },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        pageNumber: 1,
        pageSize: 20,
        configCode: '',
        configName: '',
        bankName: '',
        connectType: '',
        isEnabled: null
      },
      multipleSelection: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getBankConnectConfigList(this.listQuery).then(response => {
        if (response.success) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNumber = 1
      this.getList()
    },
    handleCreate() {
      this.$refs.editDialog.show()
    },
    handleUpdate(row) {
      this.$refs.editDialog.show(row)
    },
    handleDelete(row) {
      this.$confirm('确定要删除该银企直连配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBankConnectConfig(row.configId).then(response => {
          if (response.success) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确定要删除选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.configId)
        batchDeleteBankConnectConfig(ids).then(response => {
          if (response.success) {
            this.$message.success('批量删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        })
      })
    },
    handleModifyStatus(row, status) {
      const statusText = status === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${statusText}该银企直连配置吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateBankConnectConfigStatus({
          id: row.configId,
          isEnabled: status,
          updateUser: this.$store.getters.userId
        }).then(response => {
          if (response.success) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            this.$message.error(response.message || `${statusText}失败`)
          }
        })
      })
    },
    handleTestConnection(row) {
      this.$confirm('确定要测试该银企直连配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在测试连接...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        
        testBankConnection(row.configId).then(response => {
          loading.close()
          if (response.success) {
            this.$message.success('连接测试成功')
            this.getList()
          } else {
            this.$message.error(response.message || '连接测试失败')
          }
        }).catch(() => {
          loading.close()
          this.$message.error('连接测试失败')
        })
      })
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    getConnectTypeTagType(type) {
      const map = {
        'HTTP': 'info',
        'HTTPS': 'success',
        'FTP': 'warning',
        'SFTP': 'primary'
      }
      return map[type] || ''
    },
    getAuthTypeText(type) {
      const map = {
        'CERT': '证书认证',
        'TOKEN': '令牌认证',
        'SIGN': '签名认证'
      }
      return map[type] || type
    },
    getAuthTypeTagType(type) {
      const map = {
        'CERT': 'success',
        'TOKEN': 'warning',
        'SIGN': 'info'
      }
      return map[type] || ''
    },
    getTestStatusText(status) {
      const map = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'PENDING': '待测试'
      }
      return map[status] || status
    },
    getTestStatusTagType(status) {
      const map = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'PENDING': 'info'
      }
      return map[status] || ''
    }
  }
}
</script>

<style scoped>
.filter-container {
  padding-bottom: 10px;
}
.filter-item {
  display: inline-block;
  vertical-align: middle;
  margin-bottom: 10px;
  margin-right: 10px;
}
</style>
