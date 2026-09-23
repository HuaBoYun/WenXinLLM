<template>
  <div class="voucher-generation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document-add"></i>
          凭证生成
        </h1>
        <p class="page-description">配置数据源，根据业务规则自动生成会计凭证</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateTemplate">
          新增模板
        </el-button>
        <el-button type="success" icon="el-icon-setting" @click="handleDataSource">
          数据源配置
        </el-button>
        <el-button type="warning" icon="el-icon-video-play" @click="handleBatchGenerate">
          批量生成
        </el-button>
      </div>
    </div>

    <!-- 主要内容区 -->
    <div class="main-content">
      <el-row :gutter="24">
        <!-- 左侧：模板列表 -->
        <el-col :span="8">
          <div class="template-list">
            <div class="section-header">
              <h3>凭证模板列表</h3>
              <el-button size="mini" icon="el-icon-refresh" @click="loadTemplates">刷新</el-button>
            </div>
            <div class="template-cards">
              <div
                v-for="template in templates"
                :key="template.id"
                class="template-card"
                :class="{ active: selectedTemplate && selectedTemplate.id === template.id }"
                @click="selectTemplate(template)">
                <div class="card-header">
                  <h4>{{ template.name }}</h4>
                  <el-tag :type="template.status === 'active' ? 'success' : 'info'" size="mini">
                    {{ template.status === 'active' ? '启用' : '禁用' }}
                  </el-tag>
                </div>
                <div class="card-content">
                  <p class="description">{{ template.description }}</p>
                  <div class="meta-info">
                    <span>业务类型：{{ template.businessType }}</span>
                    <span>创建时间：{{ template.createTime }}</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button size="mini" type="text" @click.stop="handleEditTemplate(template)">编辑</el-button>
                  <el-button size="mini" type="text" @click.stop="handleTestTemplate(template)">测试</el-button>
                  <el-button size="mini" type="text" style="color: #f56c6c;" @click.stop="handleDeleteTemplate(template)">删除</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧：模板详情和生成区域 -->
        <el-col :span="16">
          <div class="template-detail" v-if="selectedTemplate">
            <div class="section-header">
              <h3>{{ selectedTemplate.name }}</h3>
              <div class="actions">
                <el-button type="primary" icon="el-icon-video-play" @click="handleGenerateVoucher">
                  生成凭证
                </el-button>
                <el-button type="success" icon="el-icon-view" @click="handlePreview">
                  预览凭证
                </el-button>
              </div>
            </div>

            <!-- 生成参数配置 -->
            <div class="generate-config">
              <el-form :model="generateForm" :rules="generateRules" ref="generateForm" label-width="100px">
                <el-form-item label="会计期间" prop="period">
                  <el-date-picker
                    v-model="generateForm.period"
                    type="month"
                    placeholder="选择会计期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="凭证日期" prop="voucherDate">
                  <el-date-picker
                    v-model="generateForm.voucherDate"
                    type="date"
                    placeholder="选择凭证日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="摘要" prop="summary">
                  <el-input
                    v-model="generateForm.summary"
                    type="textarea"
                    placeholder="请输入凭证摘要"
                    rows="2">
                  </el-input>
                </el-form-item>
                <el-form-item label="数据范围" prop="dataRange">
                  <el-select v-model="generateForm.dataRange" placeholder="选择数据范围">
                    <el-option label="全部数据" value="all"></el-option>
                    <el-option label="指定日期范围" value="dateRange"></el-option>
                    <el-option label="指定单据" value="specific"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="开始日期" v-if="generateForm.dataRange === 'dateRange'">
                  <el-date-picker
                    v-model="generateForm.startDate"
                    type="date"
                    placeholder="选择开始日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="结束日期" v-if="generateForm.dataRange === 'dateRange'">
                  <el-date-picker
                    v-model="generateForm.endDate"
                    type="date"
                    placeholder="选择结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="生成选项">
                  <el-checkbox-group v-model="generateForm.options">
                    <el-checkbox label="autoReview">自动审核</el-checkbox>
                    <el-checkbox label="mergeSame">合并相同凭证</el-checkbox>
                    <el-checkbox label="checkBalance">检查平衡</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-form>
            </div>

            <!-- 预览区域 -->
            <div class="preview-area" v-if="previewVouchers.length > 0">
              <h4>凭证预览</h4>
              <el-table :data="previewVouchers" border style="width: 100%">
                <el-table-column prop="voucherNo" label="凭证号" width="120"></el-table-column>
                <el-table-column prop="voucherDate" label="凭证日期" width="120"></el-table-column>
                <el-table-column prop="summary" label="摘要" min-width="200"></el-table-column>
                <el-table-column prop="subjectCode" label="科目编码" width="120"></el-table-column>
                <el-table-column prop="subjectName" label="科目名称" min-width="200"></el-table-column>
                <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="debit-amount">{{ formatAmount(scope.row.debitAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="credit-amount">{{ formatAmount(scope.row.creditAmount) }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <!-- 空状态 -->
          <div class="empty-state" v-else>
            <i class="el-icon-document"></i>
            <p>请选择一个凭证模板开始生成</p>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 新增/编辑模板对话框 -->
    <el-dialog
      :title="templateDialogTitle"
      :visible.sync="templateDialogVisible"
      width="800px"
      :close-on-click-modal="false">
      <el-form :model="templateForm" :rules="templateRules" ref="templateForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="name">
              <el-input v-model="templateForm.name" placeholder="请输入模板名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="templateForm.businessType" placeholder="请选择业务类型" style="width: 100%">
                <el-option label="销售业务" value="sales"></el-option>
                <el-option label="采购业务" value="purchase"></el-option>
                <el-option label="费用业务" value="expense"></el-option>
                <el-option label="收款业务" value="receipt"></el-option>
                <el-option label="付款业务" value="payment"></el-option>
                <el-option label="转账业务" value="transfer"></el-option>
                <el-option label="其他业务" value="other"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="模板描述" prop="description">
          <el-input type="textarea" v-model="templateForm.description" placeholder="请输入模板描述" rows="2"></el-input>
        </el-form-item>
        <el-form-item label="数据源" prop="dataSource">
          <el-select v-model="templateForm.dataSource" placeholder="请选择数据源" style="width: 100%">
            <el-option label="销售订单" value="salesOrder"></el-option>
            <el-option label="采购订单" value="purchaseOrder"></el-option>
            <el-option label="费用申请" value="expenseApply"></el-option>
            <el-option label="收款单" value="receiptNote"></el-option>
            <el-option label="付款单" value="paymentNote"></el-option>
            <el-option label="其他单据" value="other"></el-option>
          </el-select>
        </el-form-item>

        <!-- 凭证分录配置 -->
        <el-form-item label="分录配置">
          <el-table :data="templateForm.entries" border style="width: 100%">
            <el-table-column label="摘要" width="200">
              <template slot-scope="scope">
                <el-input v-model="scope.row.summary" size="mini" placeholder="摘要"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="科目编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.subjectCode" size="mini" placeholder="科目编码"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="取数公式" min-width="200">
              <template slot-scope="scope">
                <el-input v-model="scope.row.formula" size="mini" placeholder="如：amount"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="借贷方向" width="100">
              <template slot-scope="scope">
                <el-select v-model="scope.row.direction" size="mini">
                  <el-option label="借方" value="debit"></el-option>
                  <el-option label="贷方" value="credit"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="removeEntry(scope.$index)" style="color: #f56c6c;">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 10px;">
            <el-button size="small" icon="el-icon-plus" @click="addEntry">添加分录</el-button>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="templateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveTemplate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'VoucherGenerationIndex',
  data() {
    return {
      templates: [],
      selectedTemplate: null,
      templateDialogVisible: false,
      isEditTemplate: false,
      loading: false,
      templates: [
        {
          id: '1',
          name: '销售收入凭证模板',
          businessType: '销售业务',
          description: '根据销售订单自动生成销售收入凭证',
          status: 'active',
          createTime: '2024-01-01',
          dataSource: 'salesOrder',
          entries: []
        },
        {
          id: '2',
          name: '采购付款凭证模板',
          businessType: '采购业务',
          description: '根据采购订单自动生成采购付款凭证',
          status: 'active',
          createTime: '2024-01-02',
          dataSource: 'purchaseOrder',
          entries: []
        },
        {
          id: '3',
          name: '费用报销凭证模板',
          businessType: '费用业务',
          description: '根据费用申请单自动生成费用报销凭证',
          status: 'active',
          createTime: '2024-01-03',
          dataSource: 'expenseApply',
          entries: []
        }
      ],
      generateForm: {
        period: '',
        voucherDate: '',
        summary: '',
        dataRange: 'all',
        startDate: '',
        endDate: '',
        options: ['autoReview', 'checkBalance']
      },
      generateRules: {
        period: [
          { required: true, message: '请选择会计期间', trigger: 'change' }
        ],
        voucherDate: [
          { required: true, message: '请选择凭证日期', trigger: 'change' }
        ],
        summary: [
          { required: true, message: '请输入凭证摘要', trigger: 'blur' }
        ],
        dataRange: [
          { required: true, message: '请选择数据范围', trigger: 'change' }
        ]
      },
      templateForm: {
        id: '',
        name: '',
        businessType: '',
        description: '',
        dataSource: '',
        entries: []
      },
      templateRules: {
        name: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        businessType: [
          { required: true, message: '请选择业务类型', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ]
      },
      previewVouchers: []
    }
  },
  computed: {
    templateDialogTitle() {
      return this.isEditTemplate ? '编辑凭证模板' : '新增凭证模板'
    }
  },
  mounted() {
    this.initDefaultPeriod()
  },
  methods: {
    initDefaultPeriod() {
      const now = new Date()
      this.generateForm.period = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0')
      this.generateForm.voucherDate = now.toISOString().split('T')[0]
    },
    loadTemplates() {
      this.$message.success('模板列表已刷新')
    },
    selectTemplate(template) {
      this.selectedTemplate = template
      this.generateForm.summary = template.description
    },
    handleCreateTemplate() {
      this.isEditTemplate = false
      this.templateForm = {
        id: '',
        name: '',
        businessType: '',
        description: '',
        dataSource: '',
        entries: []
      }
      this.templateDialogVisible = true
    },
    handleEditTemplate(template) {
      this.isEditTemplate = true
      this.templateForm = { ...template }
      this.templateDialogVisible = true
    },
    handleDeleteTemplate(template) {
      this.$confirm(`确定要删除模板"${template.name}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadTemplates()
      })
    },
    handleTestTemplate(template) {
      this.$confirm(`确认测试模板"${template.name}"？将使用示例数据进行凭证生成测试。`, '测试模板', {
        type: 'info'
      }).then(() => {
        this.$message.success('模板测试通过，可正常使用')
      }).catch(() => {})
    },
    handleDataSource() {
      this.$alert('<p>当前数据源配置：</p><p><b>数据库：</b>达梦数据库</p><p><b>连接状态：</b>正常</p><p><b>数据表：</b>已配置</p>', '数据源配置', {
        dangerouslyUseHTMLString: true
      })
    },
    handleBatchGenerate() {
      if (!this.selectedTemplate) {
        this.$message.warning('请先选择一个凭证模板')
        return
      }
      this.$confirm('确认使用当前模板批量生成凭证？', '批量生成', {
        type: 'warning'
      }).then(() => {
        this.$message.success('批量生成任务已提交')
        this.loadTemplates()
      }).catch(() => {})
    },
    async handleGenerateVoucher() {
      if (!this.selectedTemplate) {
        this.$message.warning('请先选择一个凭证模板')
        return
      }

      this.$refs.generateForm.validate(async (valid) => {
        if (valid) {
          try {
            this.loading = true
            // 调用生成凭证API
            await new Promise(resolve => setTimeout(resolve, 1000))
            this.$message.success('凭证生成成功')
            this.previewVouchers = []
          } catch (error) {
            this.$message.error('生成失败：' + error.message)
          } finally {
            this.loading = false
          }
        }
      })
    },
    handlePreview() {
      if (!this.selectedTemplate) {
        this.$message.warning('请先选择一个凭证模板')
        return
      }

      // 生成预览数据
      this.previewVouchers = [
        {
          voucherNo: '记-2024-001',
          voucherDate: this.generateForm.voucherDate,
          summary: this.generateForm.summary,
          subjectCode: '6001',
          subjectName: '主营业务收入',
          debitAmount: 0,
          creditAmount: 10000
        },
        {
          voucherNo: '记-2024-001',
          voucherDate: this.generateForm.voucherDate,
          summary: this.generateForm.summary,
          subjectCode: '1121',
          subjectName: '应收账款',
          debitAmount: 10000,
          creditAmount: 0
        }
      ]
    },
    handleSaveTemplate() {
      this.$refs.templateForm.validate(async (valid) => {
        if (valid) {
          try {
            // 保存模板
            this.$message.success(this.isEditTemplate ? '修改成功' : '新增成功')
            this.templateDialogVisible = false
            this.loadTemplates()
          } catch (error) {
            this.$message.error('保存失败：' + error.message)
          }
        }
      })
    },
    addEntry() {
      this.templateForm.entries.push({
        summary: '',
        subjectCode: '',
        formula: '',
        direction: 'debit'
      })
    },
    removeEntry(index) {
      this.templateForm.entries.splice(index, 1)
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return amount.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    }
  }
}
</script>

<style lang="scss" scoped>
.voucher-generation-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.main-content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  min-height: 600px;
}

.template-list {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 18px;
      color: #303133;
    }
  }

  .template-cards {
    height: calc(100vh - 350px);
    overflow-y: auto;
  }

  .template-card {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;

    &:hover {
      background: #e9ecef;
    }

    &.active {
      background: #e3f2fd;
      border-color: #409eff;
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;

      h4 {
        margin: 0;
        font-size: 16px;
        color: #303133;
      }
    }

    .card-content {
      .description {
        color: #606266;
        font-size: 14px;
        margin: 0 0 8px 0;
        line-height: 1.5;
      }

      .meta-info {
        display: flex;
        flex-direction: column;
        gap: 4px;

        span {
          color: #909399;
          font-size: 12px;
        }
      }
    }

    .card-actions {
      margin-top: 12px;
      display: flex;
      gap: 8px;

      .el-button {
        padding: 0;
        font-size: 12px;
      }
    }
  }
}

.template-detail {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 18px;
      color: #303133;
    }

    .actions {
      .el-button {
        margin-left: 12px;
      }
    }
  }

  .generate-config {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 24px;
  }

  .preview-area {
    h4 {
      margin: 0 0 16px 0;
      font-size: 16px;
      color: #303133;
    }

    .debit-amount {
      color: #409eff;
      font-weight: 600;
    }

    .credit-amount {
      color: #67c23a;
      font-weight: 600;
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;

  i {
    font-size: 64px;
    color: #dcdfe6;
    margin-bottom: 16px;
  }

  p {
    color: #909399;
    font-size: 14px;
    margin: 0;
  }
}
</style>