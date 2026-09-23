<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="规则编码">
        <el-input v-model="queryForm.ruleCode" placeholder="请输入规则编码" clearable />
      </el-form-item>
      <el-form-item label="规则名称">
        <el-input v-model="queryForm.ruleName" placeholder="请输入规则名称" clearable />
      </el-form-item>
      <el-form-item label="控制类型">
        <el-select v-model="queryForm.controlType" placeholder="请选择控制类型" clearable>
          <el-option label="刚性控制" value="RIGID" />
          <el-option label="弹性控制" value="FLEXIBLE" />
          <el-option label="预警控制" value="WARNING" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否启用">
        <el-select v-model="queryForm.isEnabled" placeholder="请选择" clearable>
          <el-option label="启用" value="Y" />
          <el-option label="禁用" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="规则编码" prop="ruleCode" width="150" />
      <el-table-column label="规则名称" prop="ruleName" width="200" />
      <el-table-column label="控制类型" prop="controlType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.controlType === 'RIGID'" type="danger" size="small">刚性控制</el-tag>
          <el-tag v-else-if="scope.row.controlType === 'FLEXIBLE'" type="warning" size="small">弹性控制</el-tag>
          <el-tag v-else-if="scope.row.controlType === 'WARNING'" type="info" size="small">预警控制</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="控制级别" prop="controlLevel" width="100">
        <template slot-scope="scope">
          {{ controlLevelText(scope.row.controlLevel) }}
        </template>
      </el-table-column>
      <el-table-column label="控制期间" prop="controlPeriod" width="100">
        <template slot-scope="scope">
          {{ controlPeriodText(scope.row.controlPeriod) }}
        </template>
      </el-table-column>
      <el-table-column label="控制动作" prop="controlAction" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.controlAction === 'BLOCK'" type="danger" size="small">阻止</el-tag>
          <el-tag v-else-if="scope.row.controlAction === 'WARN'" type="warning" size="small">警告</el-tag>
          <el-tag v-else-if="scope.row.controlAction === 'APPROVE'" type="info" size="small">审批</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="优先级" prop="priority" width="80" align="center" />
      <el-table-column label="是否启用" prop="isEnabled" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            active-value="Y"
            inactive-value="N"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" fixed="right" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form ref="dataForm" :model="dataForm" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="dataForm.ruleCode" placeholder="请输入规则编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="dataForm.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="控制类型" prop="controlType">
              <el-select v-model="dataForm.controlType" placeholder="请选择控制类型" style="width: 100%">
                <el-option label="刚性控制" value="RIGID" />
                <el-option label="弹性控制" value="FLEXIBLE" />
                <el-option label="预警控制" value="WARNING" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制级别" prop="controlLevel">
              <el-select v-model="dataForm.controlLevel" placeholder="请选择控制级别" style="width: 100%">
                <el-option label="科目" value="SUBJECT" />
                <el-option label="组织" value="ORG" />
                <el-option label="项目" value="PROJECT" />
                <el-option label="自定义" value="CUSTOM" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="控制期间" prop="controlPeriod">
              <el-select v-model="dataForm.controlPeriod" placeholder="请选择控制期间" style="width: 100%">
                <el-option label="月度" value="MONTH" />
                <el-option label="季度" value="QUARTER" />
                <el-option label="年度" value="YEAR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制动作" prop="controlAction">
              <el-select v-model="dataForm.controlAction" placeholder="请选择控制动作" style="width: 100%">
                <el-option label="阻止" value="BLOCK" />
                <el-option label="警告" value="WARN" />
                <el-option label="审批" value="APPROVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number
                v-model="dataForm.priority"
                :min="0"
                :max="999"
                :step="1"
                controls-position="right"
                placeholder="数值越小优先级越高"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="isEnabled">
              <el-radio-group v-model="dataForm.isEnabled">
                <el-radio label="Y">启用</el-radio>
                <el-radio label="N">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 更多表单字段... -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryControlRulePage,
  addControlRule,
  modifyControlRule,
  removeControlRule,
  toggleControlRuleStatus
} from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'

export default {
  name: 'ControlRule',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        ruleCode: '',
        ruleName: '',
        controlType: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      dataForm: {},
      rules: {
        ruleCode: [{ required: true, message: '请输入规则编码', trigger: 'blur' }],
        ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        controlType: [{ required: true, message: '请选择控制类型', trigger: 'change' }],
        controlLevel: [{ required: true, message: '请选择控制级别', trigger: 'change' }],
        controlPeriod: [{ required: true, message: '请选择控制期间', trigger: 'change' }],
        controlAction: [{ required: true, message: '请选择控制动作', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 控制级别文本映射 (无字典/中文标签直接 switch) */
    controlLevelText(v) {
      const map = { SUBJECT: '科目', ORG: '组织', PROJECT: '项目', CUSTOM: '自定义' }
      return map[v] || v || '-'
    },
    /** 控制期间文本映射 */
    controlPeriodText(v) {
      const map = { MONTH: '月度', QUARTER: '季度', YEAR: '年度' }
      return map[v] || v || '-'
    },
    getList() {
      this.loading = true
      queryControlRulePage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records
          this.total = response.data.total
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    handleReset() {
      this.queryForm = {
        ruleCode: '',
        ruleName: '',
        controlType: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '新增控制规则'
      // 给新建表单填默认值: 启用 / 月度 / 警告 / 优先级 0
      this.dataForm = {
        isEnabled: 'Y',
        priority: 0,
        controlPeriod: 'MONTH',
        controlAction: 'WARN'
      }
      this.dialogVisible = true
      // 清掉上一次校验残留, 避免上次留下来的红字提示
      this.$nextTick(() => {
        if (this.$refs.dataForm) this.$refs.dataForm.clearValidate()
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑控制规则'
      this.dataForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.dataForm) this.$refs.dataForm.clearValidate()
      })
    },
    handleSubmit() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          const api = this.dataForm.ruleId ? modifyControlRule : addControlRule
          api(this.dataForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg)
              this.dialogVisible = false
              this.getList()
            }
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该控制规则吗?', '提示', {
        type: 'warning'
      }).then(() => {
        removeControlRule(row.ruleId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },
    handleStatusChange(row) {
      toggleControlRuleStatus(row.ruleId, row.isEnabled).then(response => {
        if (response.code === 1) {
          this.$message.success('操作成功')
        } else {
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        }
      })
    }
  }
}
</script>

