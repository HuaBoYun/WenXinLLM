<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.ruleName"
        placeholder="规则名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.ruleType"
        placeholder="规则类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option label="风险控制" value="RISK_CONTROL" />
        <el-option label="业务审批" value="APPROVAL" />
        <el-option label="数据验证" value="VALIDATION" />
        <el-option label="计算规则" value="CALCULATION" />
        <el-option label="通知规则" value="NOTIFICATION" />
      </el-select>
      <el-select
        v-model="listQuery.ruleEngine"
        placeholder="规则引擎"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="Drools" value="DROOLS" />
        <el-option label="Groovy" value="GROOVY" />
        <el-option label="JavaScript" value="JAVASCRIPT" />
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
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-upload2"
        @click="handleBatchImport"
      >
        批量导入
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="info"
        icon="el-icon-cpu"
        @click="handleRuleTest"
      >
        规则测试
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
      <el-table-column label="规则编码" prop="ruleCode" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.ruleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则名称" prop="ruleName" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.ruleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则类型" prop="ruleType" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag :type="getRuleTypeTagType(row.ruleType)">
            {{ getRuleTypeText(row.ruleType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="规则引擎" prop="ruleEngine" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getRuleEngineTagType(row.ruleEngine)">
            {{ row.ruleEngine }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="优先级" prop="priority" align="center" width="80">
        <template slot-scope="{row}">
          <el-tag :type="getPriorityTagType(row.priority)">
            {{ row.priority }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="版本号" prop="version" align="center" width="80">
        <template slot-scope="{row}">
          <span class="version-text">v{{ row.version }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效时间" prop="effectiveTime" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.effectiveTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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
          <el-button type="info" size="mini" @click="handleExecute(row)">
            执行
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
    <BusinessRuleEditDialog
      ref="editDialog"
      @refresh="getList"
    />

    <!-- 规则测试对话框 -->
    <BusinessRuleTestDialog
      ref="testDialog"
    />

    <!-- 规则执行对话框 -->
    <BusinessRuleExecuteDialog
      ref="executeDialog"
    />

    <!-- 批量导入对话框 -->
    <BusinessRuleBatchImportDialog
      ref="batchImportDialog"
      @refresh="getList"
    />
  </div>
</template>

<script>
import { getBusinessRuleList, deleteBusinessRule, batchDeleteBusinessRule, updateBusinessRuleStatus } from '@/api/globalTreasurer/czgg'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import BusinessRuleEditDialog from './components/BusinessRuleEditDialog'
import BusinessRuleTestDialog from './components/BusinessRuleTestDialog'
import BusinessRuleExecuteDialog from './components/BusinessRuleExecuteDialog'
import BusinessRuleBatchImportDialog from './components/BusinessRuleBatchImportDialog'

export default {
  name: 'BusinessRuleManagement',
  components: { 
    Pagination, 
    BusinessRuleEditDialog, 
    BusinessRuleTestDialog, 
    BusinessRuleExecuteDialog, 
    BusinessRuleBatchImportDialog 
  },
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
        ruleName: '',
        ruleType: '',
        ruleEngine: '',
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
      getBusinessRuleList(this.listQuery).then(response => {
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
    handleExecute(row) {
      this.$refs.executeDialog.show(row)
    },
    handleDelete(row) {
      this.$confirm('确定要删除该业务规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBusinessRule(row.ruleId).then(response => {
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
        const ids = this.multipleSelection.map(item => item.ruleId)
        batchDeleteBusinessRule(ids).then(response => {
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
      this.$confirm(`确定要${statusText}该业务规则吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateBusinessRuleStatus({
          id: row.ruleId,
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
    handleRuleTest() {
      this.$refs.testDialog.show()
    },
    handleBatchImport() {
      this.$refs.batchImportDialog.show()
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    getRuleTypeText(type) {
      const map = {
        'RISK_CONTROL': '风险控制',
        'APPROVAL': '业务审批',
        'VALIDATION': '数据验证',
        'CALCULATION': '计算规则',
        'NOTIFICATION': '通知规则'
      }
      return map[type] || type
    },
    getRuleTypeTagType(type) {
      const map = {
        'RISK_CONTROL': 'danger',
        'APPROVAL': 'warning',
        'VALIDATION': 'info',
        'CALCULATION': 'success',
        'NOTIFICATION': 'primary'
      }
      return map[type] || ''
    },
    getRuleEngineTagType(engine) {
      const map = {
        'DROOLS': 'success',
        'GROOVY': 'warning',
        'JAVASCRIPT': 'info'
      }
      return map[engine] || ''
    },
    getPriorityTagType(priority) {
      if (priority >= 90) return 'danger'
      if (priority >= 70) return 'warning'
      if (priority >= 50) return 'primary'
      return 'info'
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
.version-text {
  font-weight: bold;
  color: #409EFF;
}
</style>
