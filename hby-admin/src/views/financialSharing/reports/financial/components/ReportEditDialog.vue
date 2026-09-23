<template>
  <el-dialog
    title="编辑财务报表"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
    @close="resetForm">
    <div v-if="reportData" class="report-edit-container">
      <!-- 基本信息编辑 -->
      <div class="basic-info-section">
        <h3 class="section-title">基本信息</h3>
        <el-form
          ref="basicForm"
          :model="form"
          :rules="rules"
          label-width="100px"
          :inline="true">
          <el-form-item label="报表名称" prop="reportName">
            <el-input v-model="form.reportName" style="width: 200px"></el-input>
          </el-form-item>
          <el-form-item label="会计期间" prop="period">
            <el-date-picker
              v-model="form.period"
              type="month"
              placeholder="选择会计期间"
              format="yyyy-MM"
              value-format="yyyy-MM"
              style="width: 150px">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" style="width: 120px">
              <el-option label="草稿" value="draft"></el-option>
              <el-option label="已生成" value="generated"></el-option>
              <el-option label="已审核" value="reviewed"></el-option>
              <el-option label="已发布" value="published"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="2"
              style="width: 300px"
              placeholder="请输入备注信息">
            </el-input>
          </el-form-item>
        </el-form>
      </div>

      <!-- 报表数据编辑 -->
      <div class="data-edit-section">
        <h3 class="section-title">报表数据</h3>
        
        <!-- 资产负债表编辑 -->
        <div v-if="reportData.reportType === 'balance_sheet'" class="balance-sheet-edit">
          <el-tabs v-model="activeTab" type="card">
            <el-tab-pane label="资产" name="assets">
              <div class="table-edit">
                <div class="table-actions">
                  <el-button size="small" type="primary" @click="addAssetItem">添加项目</el-button>
                  <el-button size="small" @click="importAssetData">导入数据</el-button>
                </div>
                <el-table :data="balanceSheetData.assets" border size="small" style="margin-top: 12px">
                  <el-table-column prop="item" label="项目" width="200">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.item" size="mini"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="currentAmount" label="期末余额" width="150">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.currentAmount" size="mini"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="previousAmount" label="期初余额" width="150">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.previousAmount" size="mini"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" style="color: #f56c6c" @click="removeAssetItem(scope.$index)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="负债和所有者权益" name="liabilities">
              <div class="table-edit">
                <div class="table-actions">
                  <el-button size="small" type="primary" @click="addLiabilityItem">添加项目</el-button>
                  <el-button size="small" @click="importLiabilityData">导入数据</el-button>
                </div>
                <el-table :data="balanceSheetData.liabilities" border size="small" style="margin-top: 12px">
                  <el-table-column prop="item" label="项目" width="200">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.item" size="mini"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="currentAmount" label="期末余额" width="150">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.currentAmount" size="mini"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="previousAmount" label="期初余额" width="150">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.previousAmount" size="mini"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" style="color: #f56c6c" @click="removeLiabilityItem(scope.$index)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 利润表编辑 -->
        <div v-if="reportData.reportType === 'income_statement'" class="income-statement-edit">
          <div class="table-edit">
            <div class="table-actions">
              <el-button size="small" type="primary" @click="addIncomeItem">添加项目</el-button>
              <el-button size="small" @click="importIncomeData">导入数据</el-button>
            </div>
            <el-table :data="incomeStatementData" border size="small" style="margin-top: 12px">
              <el-table-column prop="item" label="项目" width="250">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.item" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="currentAmount" label="本期金额" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.currentAmount" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="previousAmount" label="上期金额" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.previousAmount" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" style="color: #f56c6c" @click="removeIncomeItem(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 现金流量表编辑 -->
        <div v-if="reportData.reportType === 'cash_flow_statement'" class="cash-flow-statement-edit">
          <div class="table-edit">
            <div class="table-actions">
              <el-button size="small" type="primary" @click="addCashFlowItem">添加项目</el-button>
              <el-button size="small" @click="importCashFlowData">导入数据</el-button>
            </div>
            <el-table :data="cashFlowStatementData" border size="small" style="margin-top: 12px">
              <el-table-column prop="item" label="项目" width="250">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.item" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="currentAmount" label="本期金额" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.currentAmount" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="previousAmount" label="上期金额" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.previousAmount" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" style="color: #f56c6c" @click="removeCashFlowItem(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <!-- 审核信息 -->
      <div v-if="form.status !== 'draft'" class="audit-info-section">
        <h3 class="section-title">审核信息</h3>
        <el-form label-width="100px">
          <el-form-item label="审核人">
            <el-input v-model="form.auditor" readonly style="width: 200px"></el-input>
          </el-form-item>
          <el-form-item label="审核时间">
            <el-input v-model="form.auditTime" readonly style="width: 200px"></el-input>
          </el-form-item>
          <el-form-item label="审核意见">
            <el-input
              v-model="form.auditComment"
              type="textarea"
              :rows="3"
              style="width: 400px"
              placeholder="请输入审核意见">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button @click="saveDraft">保存草稿</el-button>
      <el-button type="primary" :loading="loading" @click="handleConfirm">
        {{ loading ? '保存中...' : '保存' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ReportEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'assets',
      form: {
        reportName: '',
        period: '',
        status: 'draft',
        remark: '',
        auditor: '',
        auditTime: '',
        auditComment: ''
      },
      rules: {
        reportName: [
          { required: true, message: '请输入报表名称', trigger: 'blur' }
        ],
        period: [
          { required: true, message: '请选择会计期间', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      },
      balanceSheetData: {
        assets: [],
        liabilities: []
      },
      incomeStatementData: [],
      cashFlowStatementData: []
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(newVal) {
      if (newVal && this.reportData) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        reportName: this.reportData.reportName || '',
        period: this.reportData.period || '',
        status: this.reportData.status || 'draft',
        remark: this.reportData.remark || '',
        auditor: this.reportData.auditor || '',
        auditTime: this.reportData.auditTime || '',
        auditComment: this.reportData.auditComment || ''
      }
      
      // 初始化报表数据
      this.loadReportData()
    },
    loadReportData() {
      // 从reportData prop加载现有数据
      if (this.reportData.balanceSheetData) {
        this.balanceSheetData = this.reportData.balanceSheetData
      }
      if (this.reportData.incomeStatementData) {
        this.incomeStatementData = this.reportData.incomeStatementData
      }
      if (this.reportData.cashFlowStatementData) {
        this.cashFlowStatementData = this.reportData.cashFlowStatementData
      }
    },
    addAssetItem() {
      this.balanceSheetData.assets.push({
        item: '',
        currentAmount: '',
        previousAmount: ''
      })
    },
    removeAssetItem(index) {
      this.balanceSheetData.assets.splice(index, 1)
    },
    addLiabilityItem() {
      this.balanceSheetData.liabilities.push({
        item: '',
        currentAmount: '',
        previousAmount: ''
      })
    },
    removeLiabilityItem(index) {
      this.balanceSheetData.liabilities.splice(index, 1)
    },
    addIncomeItem() {
      this.incomeStatementData.push({
        item: '',
        currentAmount: '',
        previousAmount: ''
      })
    },
    removeIncomeItem(index) {
      this.incomeStatementData.splice(index, 1)
    },
    addCashFlowItem() {
      this.cashFlowStatementData.push({
        item: '',
        currentAmount: '',
        previousAmount: ''
      })
    },
    removeCashFlowItem(index) {
      this.cashFlowStatementData.splice(index, 1)
    },
    importAssetData() {
      this.$message.success('资产数据导入功能已触发')
    },
    importLiabilityData() {
      this.$message.success('负债数据导入功能已触发')
    },
    importIncomeData() {
      this.$message.success('利润表数据导入功能已触发')
    },
    importCashFlowData() {
      this.$message.success('现金流量表数据导入功能已触发')
    },
    async handleConfirm() {
      try {
        await this.$refs.basicForm.validate()
        this.loading = true
        
        const reportData = {
          ...this.form,
          reportId: this.reportData.reportId,
          reportType: this.reportData.reportType,
          balanceSheetData: this.balanceSheetData,
          incomeStatementData: this.incomeStatementData,
          cashFlowStatementData: this.cashFlowStatementData
        }
        
        // 提交数据到父组件处理保存
        this.$message.success('保存成功')
        this.$emit('confirm', reportData)
        this.handleClose()
      } catch (error) {
        if (error.message) {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },
    saveDraft() {
      this.form.status = 'draft'
      this.handleConfirm()
    },
    handleClose() {
      this.dialogVisible = false
    },
    resetForm() {
      this.$refs.basicForm && this.$refs.basicForm.resetFields()
      this.form = {
        reportName: '',
        period: '',
        status: 'draft',
        remark: '',
        auditor: '',
        auditTime: '',
        auditComment: ''
      }
      this.balanceSheetData = { assets: [], liabilities: [] }
      this.incomeStatementData = []
      this.cashFlowStatementData = []
    }
  }
}
</script>

<style lang="scss" scoped>
.report-edit-container {
  padding: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #409eff;
}

.basic-info-section {
  margin-bottom: 32px;
}

.data-edit-section {
  margin-bottom: 32px;

  .table-edit {
    .table-actions {
      margin-bottom: 12px;

      .el-button {
        margin-right: 8px;
      }
    }

    .el-table {
      border-radius: 4px;
      overflow: hidden;
    }
  }
}

.audit-info-section {
  padding-top: 24px;
  border-top: 1px solid #ebeef5;
}

.dialog-footer {
  text-align: right;
  padding: 16px 24px;
  border-top: 1px solid #ebeef5;
}
</style>
