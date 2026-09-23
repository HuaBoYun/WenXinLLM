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
      <el-form-item label="控制期间">
        <el-select v-model="queryForm.controlPeriod" placeholder="请选择控制期间" clearable>
          <el-option label="月度" value="MONTH" />
          <el-option label="季度" value="QUARTER" />
          <el-option label="年度" value="YEAR" />
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增策略</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="规则编码" prop="ruleCode" width="150" />
      <el-table-column label="规则名称" prop="ruleName" width="200" />
      <el-table-column label="控制类型" prop="controlType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.controlType === 'RIGID'" type="danger">刚性控制</el-tag>
          <el-tag v-else-if="scope.row.controlType === 'FLEXIBLE'" type="warning">弹性控制</el-tag>
          <el-tag v-else-if="scope.row.controlType === 'WARNING'" type="info">预警控制</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="控制级别" prop="controlLevel" width="120" />
      <el-table-column label="控制期间" prop="controlPeriod" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.controlPeriod === 'MONTH'">月度</span>
          <span v-else-if="scope.row.controlPeriod === 'QUARTER'">季度</span>
          <span v-else-if="scope.row.controlPeriod === 'YEAR'">年度</span>
        </template>
      </el-table-column>
      <el-table-column label="阈值类型" prop="thresholdType" width="100" />
      <el-table-column label="阈值" prop="thresholdValue" width="120" align="right" />
      <el-table-column label="预警比例(%)" prop="warningRatio" width="120" align="right" />
      <el-table-column label="优先级" prop="priority" width="80" />
      <el-table-column label="是否启用" prop="isEnabled" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isEnabled === 'Y'" type="success">启用</el-tag>
          <el-tag v-else type="info">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="handleTest(scope.row)">测试</el-button>
          <el-button size="mini" type="text" @click="handleCopy(scope.row)">复制</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false">
      <el-form ref="dataForm" :model="dataForm" :rules="rules" label-width="140px">
        <el-tabs v-model="activeTab">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
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
                    <el-option label="科目级" value="SUBJECT" />
                    <el-option label="组织级" value="ORG" />
                    <el-option label="项目级" value="PROJECT" />
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
                <el-form-item label="优先级" prop="priority">
                  <el-input-number v-model="dataForm.priority" :min="0" :max="999" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 阈值配置 -->
          <el-tab-pane label="阈值配置" name="threshold">
            <el-row>
              <el-col :span="12">
                <el-form-item label="阈值类型" prop="thresholdType">
                  <el-select v-model="dataForm.thresholdType" placeholder="请选择阈值类型" style="width: 100%">
                    <el-option label="金额" value="AMOUNT" />
                    <el-option label="比例" value="RATIO" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="阈值" prop="thresholdValue">
                  <el-input-number v-model="dataForm.thresholdValue" :precision="2" :min="0" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="预警比例(%)" prop="warningRatio">
                  <el-input-number v-model="dataForm.warningRatio" :precision="2" :min="0" :max="100" style="width: 100%" />
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
            <el-row v-if="dataForm.controlAction === 'APPROVE'">
              <el-col :span="24">
                <el-form-item label="审批流程ID">
                  <el-input v-model="dataForm.approvalWorkflowId" placeholder="请输入审批流程ID" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="是否启用">
                  <el-radio-group v-model="dataForm.isEnabled">
                    <el-radio label="Y">启用</el-radio>
                    <el-radio label="N">禁用</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 维度配置 -->
          <el-tab-pane label="维度配置" name="dimension">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddDimension">添加维度</el-button>
            <el-table :data="dataForm.dimensionConfigs" border style="margin-top: 10px">
              <el-table-column label="维度编码" prop="dimensionCode" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.dimensionCode" placeholder="维度编码" />
                </template>
              </el-table-column>
              <el-table-column label="维度名称" prop="dimensionName" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.dimensionName" placeholder="维度名称" />
                </template>
              </el-table-column>
              <el-table-column label="维度类型" prop="dimensionType" width="150">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.dimensionType" placeholder="维度类型">
                    <el-option label="组织" value="ORG" />
                    <el-option label="科目" value="SUBJECT" />
                    <el-option label="项目" value="PROJECT" />
                    <el-option label="自定义" value="CUSTOM" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="控制方式" prop="controlMethod" width="180">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.controlMethod" placeholder="控制方式">
                    <el-option label="组控制" value="GROUP" />
                    <el-option label="累计控制" value="ACCUMULATE" />
                    <el-option label="年内顺延" value="CARRYFORWARD" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="是否必填" prop="required" width="100">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.required" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="handleDeleteDimension(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog title="测试控制策略" :visible.sync="testDialogVisible" width="600px">
      <el-form :model="testForm" label-width="120px">
        <el-form-item label="组织ID">
          <el-input v-model="testForm.testOrgId" placeholder="请输入组织ID" />
        </el-form-item>
        <el-form-item label="科目编码">
          <el-input v-model="testForm.testSubjectCode" placeholder="请输入科目编码" />
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="testForm.testPeriod" placeholder="请输入期间，如202601" />
        </el-form-item>
        <el-form-item label="测试金额">
          <el-input-number v-model="testForm.testAmount" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div v-if="testResult" class="test-result">
        <el-divider>测试结果</el-divider>
        <pre>{{ testResult }}</pre>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="executeTest">执行测试</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog title="复制控制策略" :visible.sync="copyDialogVisible" width="500px">
      <el-form :model="copyForm" label-width="120px">
        <el-form-item label="新规则编码">
          <el-input v-model="copyForm.newRuleCode" placeholder="请输入新规则编码" />
        </el-form-item>
        <el-form-item label="新规则名称">
          <el-input v-model="copyForm.newRuleName" placeholder="请输入新规则名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="executeCopy">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryControlStrategyPage, queryControlStrategyByRuleId, saveControlStrategy, testControlStrategy, copyControlStrategy } from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'

export default {
  name: 'ControlStrategy',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      activeTab: 'basic',
      queryForm: {
        ruleCode: '',
        ruleName: '',
        controlType: '',
        controlPeriod: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      dataForm: {
        dimensionConfigs: [],
        exceptionUsers: []
      },
      rules: {
        ruleCode: [{ required: true, message: '请输入规则编码', trigger: 'blur' }],
        ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        controlType: [{ required: true, message: '请选择控制类型', trigger: 'change' }],
        controlLevel: [{ required: true, message: '请选择控制级别', trigger: 'change' }]
      },
      testDialogVisible: false,
      testForm: {},
      testResult: '',
      copyDialogVisible: false,
      copyForm: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      queryControlStrategyPage(this.queryForm).then(response => {
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
        controlPeriod: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '新增控制策略'
      this.dataForm = {
        isEnabled: 'Y',
        priority: 0,
        dimensionConfigs: [],
        exceptionUsers: []
      }
      this.activeTab = 'basic'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑控制策略'
      queryControlStrategyByRuleId(row.ruleId).then(response => {
        if (response.code === 1) {
          this.dataForm = response.data
          if (!this.dataForm.dimensionConfigs) {
            this.dataForm.dimensionConfigs = []
          }
          if (!this.dataForm.exceptionUsers) {
            this.dataForm.exceptionUsers = []
          }
          this.activeTab = 'basic'
          this.dialogVisible = true
        }
      })
    },
    handleSubmit() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          saveControlStrategy(this.dataForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg)
              this.dialogVisible = false
              this.getList()
            }
          })
        }
      })
    },
    handleAddDimension() {
      this.dataForm.dimensionConfigs.push({
        dimensionCode: '',
        dimensionName: '',
        dimensionType: '',
        controlMethod: '',
        required: false
      })
    },
    handleDeleteDimension(index) {
      this.dataForm.dimensionConfigs.splice(index, 1)
    },
    handleTest(row) {
      this.testForm = {
        ruleId: row.ruleId,
        testOrgId: '',
        testSubjectCode: '',
        testPeriod: '',
        testAmount: 0
      }
      this.testResult = ''
      this.testDialogVisible = true
    },
    executeTest() {
      queryControlStrategyByRuleId(this.testForm.ruleId).then(response => {
        if (response.code === 1) {
          const config = response.data
          testControlStrategy(
            config,
            this.testForm.testAmount.toString(),
            this.testForm.testOrgId,
            this.testForm.testSubjectCode,
            this.testForm.testPeriod
          ).then(res => {
            if (res.code === 1) {
              this.testResult = res.data
            }
          })
        }
      })
    },
    handleCopy(row) {
      this.copyForm = {
        ruleId: row.ruleId,
        newRuleCode: '',
        newRuleName: ''
      }
      this.copyDialogVisible = true
    },
    executeCopy() {
      if (!this.copyForm.newRuleCode || !this.copyForm.newRuleName) {
        this.$message.warning('请输入新规则编码和名称')
        return
      }
      copyControlStrategy(
        this.copyForm.ruleId,
        this.copyForm.newRuleCode,
        this.copyForm.newRuleName
      ).then(response => {
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.copyDialogVisible = false
          this.getList()
        }
      })
    }
  }
}
</script>

<style scoped>
.test-result {
  margin-top: 20px;
}
.test-result pre {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>


