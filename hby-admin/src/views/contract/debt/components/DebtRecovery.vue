<template>
  <el-dialog
    title="债权回收登记"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 债权信息 -->
      <el-tab-pane label="债权信息" name="info">
        <el-form
          ref="infoForm"
          :model="debtInfo"
          label-width="120px"
          :disabled="true"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="项目名称">
                <el-input v-model="debtInfo.projectName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="债权类型">
                <el-tag :type="getDebtTypeType(debtInfo.debtType)">
                  {{ getDebtTypeName(debtInfo.debtType) }}
                </el-tag>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="债权ID">
                <el-input v-model="debtInfo.debtId" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="债务人">
                <el-input v-model="debtInfo.debtorName" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="债权金额">
                <el-input :value="formatMoney(debtInfo.debtAmount)" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="已回收金额">
                <el-input :value="formatMoney(debtInfo.recoveredAmount)" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="剩余金额">
                <el-input :value="formatMoney(debtInfo.remainingAmount)" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>

      <!-- 回收记录 -->
      <el-tab-pane label="回收记录" name="records">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddRecord">
            添加回收记录
          </el-button>
        </div>

        <el-table :data="recoveryRecords" border style="width: 100%">
          <el-table-column label="回收日期" prop="recoveryDate" width="120" />
          <el-table-column label="回收金额" prop="recoveryAmount" width="120">
            <template slot-scope="scope">
              {{ formatMoney(scope.row.recoveryAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="回收方式" prop="recoveryMethod" width="100">
            <template slot-scope="scope">
              <el-tag :type="getRecoveryMethodType(scope.row.recoveryMethod)">
                {{ getRecoveryMethodName(scope.row.recoveryMethod) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="回收人员" prop="recoveryPersonName" width="120" />
          <el-table-column label="凭证号码" prop="voucherNumber" width="150" />
          <el-table-column label="银行信息" prop="bankInfo" width="200" />
          <el-table-column label="备注" prop="remarks" min-width="200" />
          <el-table-column label="操作" width="150" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleEditRecord(scope.row, scope.$index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteRecord(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 回收统计 -->
      <el-tab-pane label="回收统计" name="statistics">
        <div class="recovery-statistics">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>回收次数</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ recoverySummary.totalRecoveries }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>累计回收金额</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number recovered">{{ formatMoney(recoverySummary.totalRecoveredAmount) }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>平均回收金额</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ formatMoney(recoverySummary.averageRecoveryAmount) }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>最大单次回收</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number max">{{ formatMoney(recoverySummary.maxSingleRecovery) }}</span>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>回收进度</span>
                </div>
                <div class="efficiency-item">
                  <el-progress
                    :percentage="recoverySummary.recoveryProgress"
                    :color="getRecoveryProgressColor(recoverySummary.recoveryProgress)"
                  />
                  <p style="margin-top: 10px;">{{ recoverySummary.recoveryProgress }}%</p>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>回收效率</span>
                </div>
                <div class="efficiency-item">
                  <el-tag :type="getRecoveryEfficiencyType(recoverySummary.recoveryEfficiency)">
                    {{ getRecoveryEfficiencyText(recoverySummary.recoveryEfficiency) }}
                  </el-tag>
                  <p style="margin-top: 10px;">{{ recoverySummary.recoveryEfficiency }}%</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 回收记录编辑弹窗 -->
    <el-dialog
      :title="recordEditTitle"
      :visible.sync="recordEditDialogVisible"
      width="60%"
      append-to-body
    >
      <el-form
        ref="recordEditForm"
        :model="recordEditForm"
        :rules="recordEditRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="回收日期" prop="recoveryDate">
              <el-date-picker
                v-model="recordEditForm.recoveryDate"
                type="date"
                placeholder="选择回收日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="回收金额" prop="recoveryAmount">
              <el-input-number
                v-model="recordEditForm.recoveryAmount"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="回收方式" prop="recoveryMethod">
              <el-select v-model="recordEditForm.recoveryMethod" placeholder="请选择回收方式" style="width: 100%">
                <el-option label="银行转账" :value="1" />
                <el-option label="现金支付" :value="2" />
                <el-option label="支票支付" :value="3" />
                <el-option label="承兑汇票" :value="4" />
                <el-option label="其他方式" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="回收人员" prop="recoveryPersonId">
              <el-select v-model="recordEditForm.recoveryPersonId" placeholder="请选择回收人员" style="width: 100%">
                <el-option label="张三" :value="1" />
                <el-option label="李四" :value="2" />
                <el-option label="王五" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="凭证号码" prop="voucherNumber">
              <el-input v-model="recordEditForm.voucherNumber" placeholder="请输入凭证号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行信息" prop="bankInfo">
              <el-input v-model="recordEditForm.bankInfo" placeholder="请输入银行信息" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="付款人信息" prop="payerInfo">
          <el-input
            v-model="recordEditForm.payerInfo"
            type="textarea"
            :rows="2"
            placeholder="请输入付款人信息"
          />
        </el-form-item>

        <el-form-item label="回收说明" prop="recoveryDescription">
          <el-input
            v-model="recordEditForm.recoveryDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入回收说明"
          />
        </el-form-item>

        <el-form-item label="相关附件" prop="attachments">
          <el-input
            v-model="recordEditForm.attachments"
            placeholder="请输入相关附件信息"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="recordEditForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="recordEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRecordEdit">确定</el-button>
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
    getDebtRecoveryData,
    saveDebtRecoveryData
  } from '@/api/contract/debt'

  export default {
    name: 'DebtRecovery',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'info',
        debtInfo: {},
        recoveryRecords: [],
        recoverySummary: {
          totalRecoveries: 0,
          totalRecoveredAmount: 0,
          averageRecoveryAmount: 0,
          maxSingleRecovery: 0,
          recoveryProgress: 0,
          recoveryEfficiency: 0
        },
        recordEditDialogVisible: false,
        recordEditTitle: '',
        recordEditForm: {
          recoveryDate: '',
          recoveryAmount: null,
          recoveryMethod: null,
          recoveryPersonId: null,
          voucherNumber: '',
          bankInfo: '',
          payerInfo: '',
          recoveryDescription: '',
          attachments: '',
          remarks: ''
        },
        recordEditIndex: -1,
        recordEditRules: {
          recoveryDate: [
            { required: true, message: '请选择回收日期', trigger: 'change' }
          ],
          recoveryAmount: [
            { required: true, message: '请输入回收金额', trigger: 'blur' }
          ],
          recoveryMethod: [
            { required: true, message: '请选择回收方式', trigger: 'change' }
          ],
          recoveryPersonId: [
            { required: true, message: '请选择回收人员', trigger: 'change' }
          ],
          voucherNumber: [
            { required: true, message: '请输入凭证号码', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.debtInfo = { ...data }
        await this.loadRecoveryData()
      },

      async loadRecoveryData() {
        try {
          const response = await getDebtRecoveryData({ debtId: this.debtInfo.id })
          if (response.code === 1) {
            this.recoveryRecords = response.data.records || []
            this.recoverySummary = response.data.summary || {}
          } else {
            this.$message.error(response.msg || '加载回收数据失败')
          }
        } catch (error) {
          console.error('加载回收数据失败:', error)
          this.$message.error('加载回收数据失败')
        }
      },

      handleAddRecord() {
        this.recordEditTitle = '添加回收记录'
        this.recordEditIndex = -1
        this.resetRecordEditForm()
        this.recordEditDialogVisible = true
      },

      handleEditRecord(row, index) {
        this.recordEditTitle = '编辑回收记录'
        this.recordEditIndex = index
        this.recordEditForm = { ...row }
        this.recordEditDialogVisible = true
      },

      handleDeleteRecord(index) {
        this.$confirm('确定要删除这条回收记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.recoveryRecords.splice(index, 1)
          this.$message.success('删除成功')
        }).catch(() => {})
      },

      handleSaveRecordEdit() {
        this.$refs.recordEditForm.validate((valid) => {
          if (valid) {
            if (this.recordEditIndex === -1) {
              // 添加新记录
              this.recoveryRecords.push({ ...this.recordEditForm })
            } else {
              // 编辑现有记录
              this.$set(this.recoveryRecords, this.recordEditIndex, { ...this.recordEditForm })
            }
            this.recordEditDialogVisible = false
            this.$message.success('保存成功')
          }
        })
      },

      resetRecordEditForm() {
        this.recordEditForm = {
          recoveryDate: '',
          recoveryAmount: null,
          recoveryMethod: null,
          recoveryPersonId: null,
          voucherNumber: '',
          bankInfo: '',
          payerInfo: '',
          recoveryDescription: '',
          attachments: '',
          remarks: ''
        }
      },

      async handleSaveAll() {
        try {
          const data = {
            debtId: this.debtInfo.id,
            records: this.recoveryRecords
          }
          const response = await saveDebtRecoveryData(data)
          if (response.code === 1) {
            this.$message.success('保存成功')
            this.handleClose()
          } else {
            this.$message.error(response.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.activeTab = 'info'
        this.debtInfo = {}
        this.recoveryRecords = []
        this.recoverySummary = {
          totalRecoveries: 0,
          totalRecoveredAmount: 0,
          averageRecoveryAmount: 0,
          maxSingleRecovery: 0,
          recoveryProgress: 0,
          recoveryEfficiency: 0
        }
      },

      formatMoney(amount) {
        if (!amount) return '0.00'
        return parseFloat(amount).toLocaleString('zh-CN', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        })
      },

      getDebtTypeType(type) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'danger'
        }
        return typeMap[type] || 'info'
      },

      getDebtTypeName(type) {
        const nameMap = {
          1: '工程款',
          2: '材料款',
          3: '设备款',
          4: '其他'
        }
        return nameMap[type] || '未知'
      },

      getRecoveryMethodType(method) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'info',
          5: 'danger'
        }
        return typeMap[method] || 'info'
      },

      getRecoveryMethodName(method) {
        const nameMap = {
          1: '银行转账',
          2: '现金支付',
          3: '支票支付',
          4: '承兑汇票',
          5: '其他方式'
        }
        return nameMap[method] || '未知'
      },

      getRecoveryProgressColor(percentage) {
        if (percentage >= 80) return '#67c23a'
        if (percentage >= 60) return '#e6a23c'
        return '#f56c6c'
      },

      getRecoveryEfficiencyType(efficiency) {
        if (efficiency >= 80) return 'success'
        if (efficiency >= 60) return 'warning'
        return 'danger'
      },

      getRecoveryEfficiencyText(efficiency) {
        if (efficiency >= 80) return '高效'
        if (efficiency >= 60) return '一般'
        return '低效'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
  .recovery-statistics .stat-item {
    text-align: center;
    padding: 20px 0;
  }
  .recovery-statistics .stat-number {
    font-size: 24px;
    font-weight: bold;
  }
  .recovery-statistics .recovered {
    color: #67c23a;
  }
  .recovery-statistics .max {
    color: #409eff;
  }
  .efficiency-item {
    text-align: center;
    padding: 20px 0;
  }
</style>
