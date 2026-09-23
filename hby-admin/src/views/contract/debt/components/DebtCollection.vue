<template>
  <el-dialog
    title="债权催收记录"
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

      <!-- 催收记录 -->
      <el-tab-pane label="催收记录" name="records">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddRecord">
            添加催收记录
          </el-button>
        </div>

        <el-table :data="collectionRecords" border style="width: 100%">
          <el-table-column label="催收日期" prop="collectionDate" width="120" />
          <el-table-column label="催收人员" prop="collectorName" width="120" />
          <el-table-column label="催收方式" prop="collectionMethod" width="100">
            <template slot-scope="scope">
              <el-tag :type="getCollectionMethodType(scope.row.collectionMethod)">
                {{ getCollectionMethodName(scope.row.collectionMethod) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="催收结果" prop="collectionResult" width="100">
            <template slot-scope="scope">
              <el-tag :type="getCollectionResultType(scope.row.collectionResult)">
                {{ getCollectionResultName(scope.row.collectionResult) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="承诺金额" prop="promisedAmount" width="120">
            <template slot-scope="scope">
              {{ formatMoney(scope.row.promisedAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="承诺日期" prop="promisedDate" width="120" />
          <el-table-column label="催收内容" prop="collectionContent" min-width="200" />
          <el-table-column label="操作" width="150" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleEditRecord(scope.row, scope.$index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteRecord(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 催收统计 -->
      <el-tab-pane label="催收统计" name="statistics">
        <div class="collection-statistics">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>催收次数</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ collectionSummary.totalCollections }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>成功催收</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number success">{{ collectionSummary.successfulCollections }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>承诺总金额</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number promised">{{ formatMoney(collectionSummary.totalPromisedAmount) }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>催收成功率</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number rate">{{ collectionSummary.successRate }}%</span>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>催收效果</span>
                </div>
                <div class="efficiency-item">
                  <el-progress
                    :percentage="collectionSummary.effectivenessScore"
                    :color="getEffectivenessColor(collectionSummary.effectivenessScore)"
                  />
                  <p style="margin-top: 10px;">{{ collectionSummary.effectivenessScore }}分</p>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>平均响应时间</span>
                </div>
                <div class="efficiency-item">
                  <el-tag :type="getResponseTimeType(collectionSummary.averageResponseTime)">
                    {{ collectionSummary.averageResponseTime }}天
                  </el-tag>
                  <p style="margin-top: 10px;">债务人平均响应时间</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 催收记录编辑弹窗 -->
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
            <el-form-item label="催收日期" prop="collectionDate">
              <el-date-picker
                v-model="recordEditForm.collectionDate"
                type="date"
                placeholder="选择催收日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="催收人员" prop="collectorId">
              <el-select v-model="recordEditForm.collectorId" placeholder="请选择催收人员" style="width: 100%">
                <el-option label="张三" :value="1" />
                <el-option label="李四" :value="2" />
                <el-option label="王五" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="催收方式" prop="collectionMethod">
              <el-select v-model="recordEditForm.collectionMethod" placeholder="请选择催收方式" style="width: 100%">
                <el-option label="电话催收" :value="1" />
                <el-option label="上门催收" :value="2" />
                <el-option label="书面催收" :value="3" />
                <el-option label="法律催收" :value="4" />
                <el-option label="其他方式" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="催收结果" prop="collectionResult">
              <el-select v-model="recordEditForm.collectionResult" placeholder="请选择催收结果" style="width: 100%">
                <el-option label="承诺还款" :value="1" />
                <el-option label="部分还款" :value="2" />
                <el-option label="拒绝还款" :value="3" />
                <el-option label="无法联系" :value="4" />
                <el-option label="其他" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="承诺金额" prop="promisedAmount">
              <el-input-number
                v-model="recordEditForm.promisedAmount"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承诺日期" prop="promisedDate">
              <el-date-picker
                v-model="recordEditForm.promisedDate"
                type="date"
                placeholder="选择承诺日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="催收内容" prop="collectionContent">
          <el-input
            v-model="recordEditForm.collectionContent"
            type="textarea"
            :rows="4"
            placeholder="请输入催收内容和沟通记录"
          />
        </el-form-item>

        <el-form-item label="债务人反馈" prop="debtorFeedback">
          <el-input
            v-model="recordEditForm.debtorFeedback"
            type="textarea"
            :rows="3"
            placeholder="请输入债务人反馈信息"
          />
        </el-form-item>

        <el-form-item label="下次跟进计划" prop="nextFollowUp">
          <el-input
            v-model="recordEditForm.nextFollowUp"
            type="textarea"
            :rows="2"
            placeholder="请输入下次跟进计划"
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
    getDebtCollectionData,
    saveDebtCollectionData
  } from '@/api/contract/debt'

  export default {
    name: 'DebtCollection',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'info',
        debtInfo: {},
        collectionRecords: [],
        collectionSummary: {
          totalCollections: 0,
          successfulCollections: 0,
          totalPromisedAmount: 0,
          successRate: 0,
          effectivenessScore: 0,
          averageResponseTime: 0
        },
        recordEditDialogVisible: false,
        recordEditTitle: '',
        recordEditForm: {
          collectionDate: '',
          collectorId: null,
          collectionMethod: null,
          collectionResult: null,
          promisedAmount: null,
          promisedDate: '',
          collectionContent: '',
          debtorFeedback: '',
          nextFollowUp: '',
          remarks: ''
        },
        recordEditIndex: -1,
        recordEditRules: {
          collectionDate: [
            { required: true, message: '请选择催收日期', trigger: 'change' }
          ],
          collectorId: [
            { required: true, message: '请选择催收人员', trigger: 'change' }
          ],
          collectionMethod: [
            { required: true, message: '请选择催收方式', trigger: 'change' }
          ],
          collectionResult: [
            { required: true, message: '请选择催收结果', trigger: 'change' }
          ],
          collectionContent: [
            { required: true, message: '请输入催收内容', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.debtInfo = { ...data }
        await this.loadCollectionData()
      },

      async loadCollectionData() {
        try {
          // 确保使用正确的债权ID字段
          const debtId = this.debtInfo.id || this.debtInfo.debtId
          console.log('加载催收数据，债权ID：', debtId, '债权信息：', this.debtInfo)

          if (!debtId) {
            this.$message.error('债权ID不能为空')
            return
          }

          const response = await getDebtCollectionData({ debtId: debtId })
          console.log('催收数据响应：', response)

          if (response.code === 1) {
            this.collectionRecords = response.data.records || []
            this.collectionSummary = response.data.summary || {}
            console.log('催收记录：', this.collectionRecords)
            console.log('催收统计：', this.collectionSummary)
          } else {
            this.$message.error(response.msg || '加载催收数据失败')
          }
        } catch (error) {
          console.error('加载催收数据失败:', error)
          this.$message.error('加载催收数据失败：' + error.message)
        }
      },

      handleAddRecord() {
        this.recordEditTitle = '添加催收记录'
        this.recordEditIndex = -1
        this.resetRecordEditForm()
        this.recordEditDialogVisible = true
      },

      handleEditRecord(row, index) {
        this.recordEditTitle = '编辑催收记录'
        this.recordEditIndex = index
        this.recordEditForm = { ...row }
        this.recordEditDialogVisible = true
      },

      handleDeleteRecord(index) {
        this.$confirm('确定要删除这条催收记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.collectionRecords.splice(index, 1)
          this.$message.success('删除成功')
        }).catch(() => {})
      },

      handleSaveRecordEdit() {
        this.$refs.recordEditForm.validate((valid) => {
          if (valid) {
            if (this.recordEditIndex === -1) {
              // 添加新记录
              this.collectionRecords.push({ ...this.recordEditForm })
            } else {
              // 编辑现有记录
              this.$set(this.collectionRecords, this.recordEditIndex, { ...this.recordEditForm })
            }
            this.recordEditDialogVisible = false
            this.$message.success('保存成功')
          }
        })
      },

      resetRecordEditForm() {
        this.recordEditForm = {
          collectionDate: '',
          collectorId: null,
          collectionMethod: null,
          collectionResult: null,
          promisedAmount: null,
          promisedDate: '',
          collectionContent: '',
          debtorFeedback: '',
          nextFollowUp: '',
          remarks: ''
        }
      },

      async handleSaveAll() {
        try {
          // 确保使用正确的债权ID字段
          const debtId = this.debtInfo.id || this.debtInfo.debtId
          console.log('保存催收数据，债权ID：', debtId)

          if (!debtId) {
            this.$message.error('债权ID不能为空')
            return
          }

          const data = {
            debtId: debtId,
            records: this.collectionRecords
          }
          console.log('保存催收数据：', data)

          const response = await saveDebtCollectionData(data)
          console.log('保存响应：', response)

          if (response.code === 1) {
            this.$message.success('保存成功')
            this.handleClose()
          } else {
            this.$message.error(response.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败：' + error.message)
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.activeTab = 'info'
        this.debtInfo = {}
        this.collectionRecords = []
        this.collectionSummary = {
          totalCollections: 0,
          successfulCollections: 0,
          totalPromisedAmount: 0,
          successRate: 0,
          effectivenessScore: 0,
          averageResponseTime: 0
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

      getCollectionMethodType(method) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'danger',
          5: 'info'
        }
        return typeMap[method] || 'info'
      },

      getCollectionMethodName(method) {
        const nameMap = {
          1: '电话催收',
          2: '上门催收',
          3: '书面催收',
          4: '法律催收',
          5: '其他方式'
        }
        return nameMap[method] || '未知'
      },

      getCollectionResultType(result) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'info',
          5: 'primary'
        }
        return typeMap[result] || 'info'
      },

      getCollectionResultName(result) {
        const nameMap = {
          1: '承诺还款',
          2: '部分还款',
          3: '拒绝还款',
          4: '无法联系',
          5: '其他'
        }
        return nameMap[result] || '未知'
      },

      getEffectivenessColor(score) {
        if (score >= 80) return '#67c23a'
        if (score >= 60) return '#e6a23c'
        return '#f56c6c'
      },

      getResponseTimeType(days) {
        if (days <= 3) return 'success'
        if (days <= 7) return 'warning'
        return 'danger'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
  .collection-statistics .stat-item {
    text-align: center;
    padding: 20px 0;
  }
  .collection-statistics .stat-number {
    font-size: 24px;
    font-weight: bold;
  }
  .collection-statistics .success {
    color: #67c23a;
  }
  .collection-statistics .promised {
    color: #409eff;
  }
  .collection-statistics .rate {
    color: #e6a23c;
  }
  .efficiency-item {
    text-align: center;
    padding: 20px 0;
  }
</style>
