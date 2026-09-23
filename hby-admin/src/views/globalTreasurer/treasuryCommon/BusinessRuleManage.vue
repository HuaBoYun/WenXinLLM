<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.ruleCode"
        placeholder="规则编码"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
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
        <el-option v-for="item in ruleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.businessModule"
        placeholder="业务模块"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option v-for="item in businessModuleOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.isEnabled"
        placeholder="状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
      <el-button
        v-if="multipleSelection.length > 0"
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-download"
        @click="handleExport"
      >
        导出
      </el-button>
    </div>

    <!-- 数据表格 -->
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
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则编码" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.ruleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.ruleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRuleTypeTag(row.ruleType)">
            {{ getRuleTypeName(row.ruleType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="业务模块" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.businessModule }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优先级" width="80px" align="center">
        <template slot-scope="{row}">
          <el-tag type="warning">{{ row.priorityLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="规则描述" min-width="200px">
        <template slot-scope="{row}">
          <span>{{ row.ruleDescription || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled | statusFilter">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="info" size="mini" @click="handleView(row)">
            查看
          </el-button>
          <el-button v-if="row.isEnabled==1" size="mini" type="warning" @click="handleToggleStatus(row, 0)">
            禁用
          </el-button>
          <el-button v-else size="mini" type="success" @click="handleToggleStatus(row, 1)">
            启用
          </el-button>
          <el-button v-if="row.isEnabled!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '添加业务规则' : '编辑业务规则'" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="temp.ruleCode" placeholder="请输入规则编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="temp.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="temp.ruleType" placeholder="请选择规则类型" style="width: 100%">
                <el-option v-for="item in ruleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务模块" prop="businessModule">
              <el-select v-model="temp.businessModule" placeholder="请选择业务模块" style="width: 100%">
                <el-option v-for="item in businessModuleOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priorityLevel">
              <el-input-number v-model="temp.priorityLevel" :min="1" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="temp.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="规则描述" prop="ruleDescription">
          <el-input v-model="temp.ruleDescription" type="textarea" :rows="3" placeholder="请输入规则描述" />
        </el-form-item>
        <el-form-item label="规则表达式" prop="ruleExpression">
          <el-input v-model="temp.ruleExpression" type="textarea" :rows="4" placeholder="请输入规则表达式" />
        </el-form-item>
        <el-form-item label="规则条件" prop="ruleCondition">
          <el-input v-model="temp.ruleCondition" type="textarea" :rows="3" placeholder="请输入规则条件" />
        </el-form-item>
        <el-form-item label="规则动作" prop="ruleAction">
          <el-input v-model="temp.ruleAction" placeholder="请输入规则动作" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="查看业务规则详情" :visible.sync="dialogViewVisible" width="60%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="规则编码">{{ viewData.ruleCode }}</el-descriptions-item>
        <el-descriptions-item label="规则名称">{{ viewData.ruleName }}</el-descriptions-item>
        <el-descriptions-item label="规则类型">
          <el-tag :type="getRuleTypeTag(viewData.ruleType)">
            {{ getRuleTypeName(viewData.ruleType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="业务模块">{{ viewData.businessModule }}</el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag type="warning">{{ viewData.priorityLevel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(viewData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="规则描述" :span="2">{{ viewData.ruleDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="规则表达式" :span="2">
          <pre style="white-space: pre-wrap; margin: 0;">{{ viewData.ruleExpression || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="规则条件" :span="2">
          <pre style="white-space: pre-wrap; margin: 0;">{{ viewData.ruleCondition || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="规则动作">{{ viewData.ruleAction || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBusinessRuleList, createBusinessRule, updateBusinessRule, deleteBusinessRule, batchDeleteBusinessRule, toggleBusinessRuleStatus, exportBusinessRule } from '@/api/globalTreasurer/businessRuleManage'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: 'BusinessRuleManage',
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
        pageNum: 1,
        pageSize: 20,
        ruleCode: undefined,
        ruleName: undefined,
        ruleType: undefined,
        businessModule: undefined,
        isEnabled: undefined
      },
      ruleTypeOptions: [
        { label: '验证规则', value: 'VALIDATION' },
        { label: '重复检查', value: 'DUPLICATE_CHECK' },
        { label: '业务规则', value: 'BUSINESS_RULE' },
        { label: '数据转换', value: 'DATA_TRANSFORM' },
        { label: '权限控制', value: 'PERMISSION_CONTROL' }
      ],
      businessModuleOptions: [
        { label: '支付管理', value: 'PAYMENT' },
        { label: '贷款管理', value: 'LOAN' },
        { label: '投资管理', value: 'INVESTMENT' },
        { label: '通用模块', value: 'GENERAL' },
        { label: '风险管理', value: 'RISK_MANAGEMENT' },
        { label: '财务管理', value: 'FINANCIAL_MANAGEMENT' }
      ],
      multipleSelection: [],
      temp: {
        id: undefined,
        ruleCode: '',
        ruleName: '',
        ruleDescription: '',
        ruleType: '',
        businessModule: '',
        ruleExpression: '',
        ruleCondition: '',
        ruleAction: '',
        priorityLevel: 1,
        isEnabled: 1,
        orgId: 1,
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogViewVisible: false,
      viewData: {},
      rules: {
        ruleCode: [{ required: true, message: '请输入规则编码', trigger: 'blur' }],
        ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
        businessModule: [{ required: true, message: '请选择业务模块', trigger: 'change' }],
        priorityLevel: [{ required: true, message: '请输入优先级', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    getList() {
      this.listLoading = true
      getBusinessRuleList(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data.tlist
          this.total = response.data.totalRecord
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        ruleCode: '',
        ruleName: '',
        ruleDescription: '',
        ruleType: '',
        businessModule: '',
        ruleExpression: '',
        ruleCondition: '',
        ruleAction: '',
        priorityLevel: 1,
        isEnabled: 1,
        orgId: 1,
        remark: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createBusinessRule(this.temp).then(response => {
            if (response.code === 1) {
              this.list.unshift(response.data)
              this.total++
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateBusinessRule(tempData).then(response => {
            if (response.code === 1) {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, response.data)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBusinessRule(row.id).then(response => {
          if (response.code === 1) {
            this.list.splice(index, 1)
            this.total--
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          }
        })
      })
    },
    handleBatchDelete() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.id)
        batchDeleteBusinessRule(ids).then(response => {
          if (response.code === 1) {
            this.getList()
            this.$notify({
              title: '成功',
              message: '批量删除成功',
              type: 'success',
              duration: 2000
            })
          }
        })
      })
    },
    handleToggleStatus(row, status) {
      const statusText = status === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${statusText}该规则吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleBusinessRuleStatus({ id: row.id, isEnabled: status }).then(response => {
          if (response.code === 1) {
            row.isEnabled = status
            this.$notify({
              title: '成功',
              message: `${statusText}成功`,
              type: 'success',
              duration: 2000
            })
          }
        })
      })
    },
    handleView(row) {
      this.viewData = Object.assign({}, row)
      this.dialogViewVisible = true
    },
    handleExport() {
      exportBusinessRule(this.listQuery).then(response => {
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `业务规则管理_${parseTime(new Date(), '{y}{m}{d}')}.xlsx`
        link.click()
        window.URL.revokeObjectURL(link.href)
        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    getRuleTypeName(type) {
      const typeMap = {
        'VALIDATION': '验证规则',
        'DUPLICATE_CHECK': '重复检查',
        'BUSINESS_RULE': '业务规则',
        'DATA_TRANSFORM': '数据转换',
        'PERMISSION_CONTROL': '权限控制'
      }
      return typeMap[type] || type
    },
    getRuleTypeTag(type) {
      const tagMap = {
        'VALIDATION': '',
        'DUPLICATE_CHECK': 'success',
        'BUSINESS_RULE': 'warning',
        'DATA_TRANSFORM': 'info',
        'PERMISSION_CONTROL': 'danger'
      }
      return tagMap[type] || ''
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
}
pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-family: monospace;
}
</style>