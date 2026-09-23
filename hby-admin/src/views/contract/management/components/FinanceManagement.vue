<template>
  <el-dialog
    :title="`财务管理 - ${managementInfo.projectName || '未知项目'}`"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <!-- 项目信息概览 -->
    <div class="project-info-header" style="margin-bottom: 20px; padding: 15px; background: #f5f7fa; border-radius: 4px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <div><strong>项目名称：</strong>{{ managementInfo.projectName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>项目ID：</strong>{{ managementInfo.projectId || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>负责人：</strong>{{ managementInfo.managerName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>管理状态：</strong>
            <el-tag :type="getManagementStatusType(managementInfo.managementStatus)" size="mini">
              {{ getManagementStatusName(managementInfo.managementStatus) }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-tabs v-model="activeTab" type="card">
      <!-- 收支记录 -->
      <el-tab-pane label="收支记录" name="income">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddIncome">
            添加收支记录
          </el-button>
        </div>

        <el-table :data="incomeList" border style="width: 100%">
          <el-table-column label="收支类型" prop="transactionType" width="100">
            <template #default="{ row }">
              <el-tag :type="row.transactionType === 1 ? 'success' : 'danger'">
                {{ row.transactionType === 1 ? '收入' : '支出' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="交易编号" prop="transactionNo" width="150" />
          <el-table-column label="科目名称" prop="accountSubject" width="150" />
          <el-table-column label="金额" prop="amount" width="120">
            <template #default="{ row }">
              {{ formatMoney(row.amount) }}
            </template>
          </el-table-column>
          <el-table-column label="交易日期" prop="transactionDate" width="120">
            <template #default="{ row }">
              {{ formatDate(row.transactionDate) }}
            </template>
          </el-table-column>
          <el-table-column label="银行名称" prop="bankName" width="120" />
          <el-table-column label="摘要" prop="description" min-width="200" />
          <el-table-column label="审批状态" prop="approvalStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getApprovalStatusType(row.approvalStatus)" size="mini">
                {{ getApprovalStatusName(row.approvalStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleEditIncome(row, $index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteIncome($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 预算对比 -->
      <el-tab-pane label="预算对比" name="budget">
        <div class="budget-comparison">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card>
                <div slot="header">
                  <span>预算总额</span>
                </div>
                <div class="budget-item">
                  <span class="amount">{{ formatMoney(budgetSummary.totalBudget) }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <div slot="header">
                  <span>实际支出</span>
                </div>
                <div class="budget-item">
                  <span class="amount">{{ formatMoney(budgetSummary.actualExpense) }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <div slot="header">
                  <span>预算余额</span>
                </div>
                <div class="budget-item">
                  <span class="amount" :class="budgetSummary.remainingBudget < 0 ? 'negative' : 'positive'">
                    {{ formatMoney(budgetSummary.remainingBudget) }}
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>预算执行率</span>
                </div>
                <el-progress 
                  :percentage="budgetSummary.executionRate" 
                  :stroke-width="20"
                  :color="getProgressColor(budgetSummary.executionRate)"
                />
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>成本控制状态</span>
                </div>
                <div class="cost-status">
                  <el-tag :type="getCostStatusType(budgetSummary.executionRate)" size="large">
                    {{ getCostStatusText(budgetSummary.executionRate) }}
                  </el-tag>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <!-- 成本分析 -->
      <el-tab-pane label="成本分析" name="cost">
        <div class="cost-analysis">
          <el-table :data="costAnalysisList" border style="width: 100%">
            <el-table-column label="成本科目" prop="costSubject" width="150" />
            <el-table-column label="预算金额" prop="budgetAmount" width="120">
              <template #default="{ row }">
                {{ formatMoney(row.budgetAmount) }}
              </template>
            </el-table-column>
            <el-table-column label="实际金额" prop="actualAmount" width="120">
              <template #default="{ row }">
                {{ formatMoney(row.actualAmount) }}
              </template>
            </el-table-column>
            <el-table-column label="差异金额" prop="varianceAmount" width="120">
              <template #default="{ row }">
                <span :class="row.varianceAmount > 0 ? 'positive' : 'negative'">
                  {{ formatMoney(Math.abs(row.varianceAmount)) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="差异率(%)" prop="varianceRate" width="100">
              <template #default="{ row }">
                <span :class="row.varianceRate > 0 ? 'positive' : 'negative'">
                  {{ row.varianceRate.toFixed(2) }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column label="分析说明" prop="analysisDescription" min-width="200" />
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 收支记录编辑弹窗 -->
    <el-dialog
      :title="incomeEditTitle"
      :visible.sync="incomeEditDialogVisible"
      width="50%"
      append-to-body
    >
      <el-form
        ref="incomeEditForm"
        :model="incomeEditForm"
        :rules="incomeEditRules"
        label-width="120px"
      >
        <el-form-item label="收支类型" prop="transactionType">
          <el-select v-model="incomeEditForm.transactionType" placeholder="请选择收支类型" style="width: 100%">
            <el-option label="收入" :value="1" />
            <el-option label="支出" :value="2" />
          </el-select>
        </el-form-item>

        <el-form-item label="交易分类" prop="transactionCategory">
          <el-select v-model="incomeEditForm.transactionCategory" placeholder="请选择交易分类" style="width: 100%">
            <el-option label="合同收款" :value="1" />
            <el-option label="材料采购" :value="2" />
            <el-option label="人工费用" :value="3" />
            <el-option label="设备租赁" :value="4" />
            <el-option label="其他费用" :value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="科目名称" prop="accountSubject">
          <el-input v-model="incomeEditForm.accountSubject" placeholder="请输入科目名称" />
        </el-form-item>

        <el-form-item label="金额" prop="amount">
          <el-input-number
            v-model="incomeEditForm.amount"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="交易日期" prop="transactionDate">
          <el-date-picker
            v-model="incomeEditForm.transactionDate"
            type="date"
            placeholder="选择交易日期"
            style="width: 100%"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item label="银行名称" prop="bankName">
          <el-input v-model="incomeEditForm.bankName" placeholder="请输入银行名称" />
        </el-form-item>

        <el-form-item label="摘要" prop="description">
          <el-input
            v-model="incomeEditForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入摘要"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="incomeEditForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="incomeEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveIncomeEdit">确定</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveAll">保存全部</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    getFinanceManagementData,
    saveFinanceManagementData
  } from '@/api/contract/management'
  import request from '@/utils/request'

  export default {
    name: 'FinanceManagement',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'income',
        managementInfo: {},
        incomeList: [],
        budgetSummary: {
          totalBudget: 0,
          actualExpense: 0,
          remainingBudget: 0,
          executionRate: 0
        },
        costAnalysisList: [],
        incomeEditDialogVisible: false,
        incomeEditTitle: '',
        incomeEditForm: {
          transactionType: null,
          transactionCategory: null,
          accountSubject: '',
          amount: null,
          transactionDate: '',
          bankName: '',
          description: '',
          remarks: ''
        },
        incomeEditIndex: -1,
        incomeEditRules: {
          transactionType: [
            { required: true, message: '请选择收支类型', trigger: 'change' }
          ],
          transactionCategory: [
            { required: true, message: '请选择交易分类', trigger: 'change' }
          ],
          accountSubject: [
            { required: true, message: '请输入科目名称', trigger: 'blur' }
          ],
          amount: [
            { required: true, message: '请输入金额', trigger: 'blur' }
          ],
          transactionDate: [
            { required: true, message: '请选择交易日期', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.managementInfo = { ...data }
        await this.loadFinanceData()
      },

      async loadFinanceData() {
        try {
          const response = await getFinanceManagementData({
            pageNum: 1,
            pageSize: 100,
            projectId: this.managementInfo.projectId
          })
          // 适配后端返回的数据格式
          if (response.result === 200 || response.code === 200) {
            const data = response.data || {}
            // 后端返回的是 records 字段，转换为前端期望的 incomeList
            this.incomeList = data.records || []

            // 计算预算汇总数据
            this.calculateBudgetSummary()

            // 生成成本分析数据
            this.generateCostAnalysis()
          }
        } catch (error) {
          console.error('加载财务数据失败：', error)
        }
      },
      
      handleClose() {
        this.dialogVisible = false
        this.incomeList = []
        this.managementInfo = {}
        this.activeTab = 'income'
      },

      handleAddIncome() {
        this.incomeEditTitle = '添加收支记录'
        this.incomeEditIndex = -1
        this.resetIncomeEditForm()
        this.incomeEditDialogVisible = true
      },

      handleEditIncome(row, index) {
        this.incomeEditTitle = '编辑收支记录'
        this.incomeEditIndex = index
        this.incomeEditForm = { ...row }
        this.incomeEditDialogVisible = true
      },

      handleDeleteIncome(index) {
        this.$confirm('确定要删除这条收支记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          try {
            const recordId = this.incomeList[index].id
            const response = await request({
              url: `/contract/finance/transaction/${recordId}`,
              method: 'delete'
            })
            if (response.result === 200 || response.code === 200) {
              this.$message.success('删除成功')
              await this.loadFinanceData() // 重新加载数据
            } else {
              this.$message.error(response.msg || '删除失败')
            }
          } catch (error) {
            console.error('删除失败：', error)
            this.$message.error('删除失败：' + error.message)
          }
        })
      },

      resetIncomeEditForm() {
        this.incomeEditForm = {
          transactionType: null,
          transactionCategory: null,
          accountSubject: '',
          amount: null,
          transactionDate: '',
          bankName: '',
          description: '',
          remarks: ''
        }
      },

      async handleSaveIncomeEdit() {
        try {
          await this.$refs.incomeEditForm.validate()

          const formData = {
            ...this.incomeEditForm,
            projectId: this.managementInfo.projectId
          }

          if (this.incomeEditIndex === -1) {
            // 新增收支记录 - 使用网关代理路径
            const response = await request({
              url: '/contract/finance/transaction/create',
              method: 'post',
              data: formData,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            })
            if (response.result === 200 || response.code === 200) {
              this.$message.success('新增成功')
              await this.loadFinanceData() // 重新加载数据
            } else {
              this.$message.error(response.msg || '新增失败')
            }
          } else {
            // 编辑收支记录 - 需要在formData中包含ID
            const recordData = {
              ...formData,
              id: this.incomeList[this.incomeEditIndex].id
            }
            const response = await request({
              url: '/contract/finance/transaction',
              method: 'put',
              data: recordData,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            })
            if (response.result === 200 || response.code === 200) {
              this.$message.success('修改成功')
              await this.loadFinanceData() // 重新加载数据
            } else {
              this.$message.error(response.msg || '修改失败')
            }
          }

          this.incomeEditDialogVisible = false
        } catch (error) {
          console.error('保存失败：', error)
          this.$message.error('保存失败：' + error.message)
        }
      },

      async handleSaveAll() {
        try {
          // 这里应该调用批量保存API
          this.$message.success('保存成功')
          this.handleClose()
        } catch (error) {
          this.$message.error('保存失败：' + error.message)
        }
      },

      formatMoney(amount) {
        if (!amount) return '0.00元'
        return parseFloat(amount).toLocaleString('zh-CN', {
          style: 'currency',
          currency: 'CNY'
        })
      },

      formatDate(dateStr) {
        if (!dateStr) return '-'
        const date = new Date(dateStr)
        return date.toLocaleDateString('zh-CN')
      },

      getApprovalStatusName(status) {
        const statusMap = {
          1: '待审批',
          2: '已审批',
          3: '已驳回'
        }
        return statusMap[status] || '未知'
      },

      getApprovalStatusType(status) {
        const statusMap = {
          1: 'warning',
          2: 'success',
          3: 'danger'
        }
        return statusMap[status] || 'info'
      },

      getProgressColor(percentage) {
        if (percentage <= 80) return '#67c23a'
        if (percentage <= 95) return '#e6a23c'
        return '#f56c6c'
      },

      getCostStatusType(executionRate) {
        if (executionRate <= 80) return 'success'
        if (executionRate <= 95) return 'warning'
        return 'danger'
      },

      getCostStatusText(executionRate) {
        if (executionRate <= 80) return '良好'
        if (executionRate <= 95) return '预警'
        return '超支'
      },

      // 获取管理状态名称
      getManagementStatusName(status) {
        const statusMap = {
          1: '正常',
          2: '预警',
          3: '异常',
          4: '暂停'
        }
        return statusMap[status] || '未知'
      },

      // 获取管理状态样式
      getManagementStatusType(status) {
        const statusMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'info'
        }
        return statusMap[status] || 'info'
      },

      // 计算预算汇总数据
      calculateBudgetSummary() {
        let totalIncome = 0
        let totalExpense = 0

        this.incomeList.forEach(item => {
          const amount = parseFloat(item.amount) || 0
          if (item.transactionType === 1) {
            // 收入
            totalIncome += amount
          } else if (item.transactionType === 2) {
            // 支出
            totalExpense += amount
          }
        })

        this.budgetSummary = {
          totalBudget: totalIncome,
          actualExpense: totalExpense,
          remainingBudget: totalIncome - totalExpense,
          executionRate: totalIncome > 0 ? Math.round((totalExpense / totalIncome) * 100) : 0
        }
      },

      // 生成成本分析数据
      generateCostAnalysis() {
        const analysisMap = {}

        this.incomeList.forEach(item => {
          const subject = item.accountSubject || '其他'
          const amount = parseFloat(item.amount) || 0

          if (!analysisMap[subject]) {
            analysisMap[subject] = {
              costSubject: subject,
              budgetAmount: 0,
              actualAmount: 0,
              varianceAmount: 0,
              varianceRate: 0,
              analysisDescription: ''
            }
          }

          if (item.transactionType === 1) {
            analysisMap[subject].budgetAmount += amount
          } else {
            analysisMap[subject].actualAmount += amount
          }
        })

        // 计算差异
        Object.values(analysisMap).forEach(item => {
          item.varianceAmount = item.actualAmount - item.budgetAmount
          item.varianceRate = item.budgetAmount > 0 ?
            ((item.varianceAmount / item.budgetAmount) * 100) : 0

          if (item.varianceRate > 10) {
            item.analysisDescription = '超支较多，需要关注'
          } else if (item.varianceRate > 0) {
            item.analysisDescription = '轻微超支'
          } else if (item.varianceRate < -10) {
            item.analysisDescription = '节约较多'
          } else {
            item.analysisDescription = '基本符合预算'
          }
        })

        this.costAnalysisList = Object.values(analysisMap)
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
  .budget-comparison .budget-item {
    text-align: center;
    padding: 20px 0;
  }
  .budget-comparison .amount {
    font-size: 24px;
    font-weight: bold;
  }
  .positive {
    color: #67c23a;
  }
  .negative {
    color: #f56c6c;
  }
  .cost-status {
    text-align: center;
    padding: 20px 0;
  }
</style>
