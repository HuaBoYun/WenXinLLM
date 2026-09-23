<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="cost-allocation-detail">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分摊编码" prop="allocationCode">
                  <el-input
                    v-model="form.allocationCode"
                    :disabled="isView"
                    placeholder="请输入分摊编码"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="分摊名称" prop="allocationName">
                  <el-input
                    v-model="form.allocationName"
                    :disabled="isView"
                    placeholder="请输入分摊名称"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分摊类型" prop="allocationType">
                  <el-select
                    v-model="form.allocationType"
                    :disabled="isView"
                    placeholder="请选择分摊类型"
                    style="width: 100%"
                  >
                    <el-option label="直接分摊" value="DIRECT" />
                    <el-option label="间接分摊" value="INDIRECT" />
                    <el-option label="阶梯分摊" value="STEP" />
                    <el-option label="交互分摊" value="RECIPROCAL" />
                    <el-option label="作业分摊" value="ACTIVITY" />
                    <el-option label="价值分摊" value="VALUE" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="分摊方法" prop="allocationMethod">
                  <el-select
                    v-model="form.allocationMethod"
                    :disabled="isView"
                    placeholder="请选择分摊方法"
                    style="width: 100%"
                  >
                    <el-option label="平均分摊" value="EQUAL" />
                    <el-option label="加权分摊" value="WEIGHTED" />
                    <el-option label="比例分摊" value="PROPORTIONAL" />
                    <el-option label="作业成本分摊" value="ACTIVITY_BASED" />
                    <el-option label="标准分摊" value="STANDARD" />
                    <el-option label="实际分摊" value="ACTUAL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分摊状态" prop="allocationStatus">
                  <el-select
                    v-model="form.allocationStatus"
                    :disabled="isView || isEdit"
                    placeholder="请选择分摊状态"
                    style="width: 100%"
                  >
                    <el-option label="草稿" value="DRAFT" />
                    <el-option label="活跃" value="ACTIVE" />
                    <el-option label="计算中" value="CALCULATING" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已暂停" value="SUSPENDED" />
                    <el-option label="已取消" value="CANCELLED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级" prop="priority">
                  <el-input-number
                    v-model="form.priority"
                    :disabled="isView"
                    :min="1"
                    :max="10"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分摊周期" prop="allocationPeriod">
                  <el-select
                    v-model="form.allocationPeriod"
                    :disabled="isView"
                    placeholder="请选择分摊周期"
                    style="width: 100%"
                  >
                    <el-option label="日" value="DAILY" />
                    <el-option label="周" value="WEEKLY" />
                    <el-option label="月" value="MONTHLY" />
                    <el-option label="季" value="QUARTERLY" />
                    <el-option label="年" value="YEARLY" />
                    <el-option label="自定义" value="CUSTOM" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="成本中心ID" prop="costCenterId">
                  <el-input-number
                    v-model="form.costCenterId"
                    :disabled="isView"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="分摊描述" prop="allocationDescription">
              <el-input
                v-model="form.allocationDescription"
                :disabled="isView"
                type="textarea"
                :rows="3"
                placeholder="请输入分摊描述"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 成本信息 -->
          <el-tab-pane label="成本信息" name="cost">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="总成本金额" prop="totalCostAmount">
                  <el-input-number
                    v-model="form.totalCostAmount"
                    :disabled="isView"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="已分摊金额" prop="allocatedAmount">
                  <el-input-number
                    v-model="form.allocatedAmount"
                    :disabled="true"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="剩余金额" prop="remainingAmount">
                  <el-input-number
                    v-model="form.remainingAmount"
                    :disabled="true"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="分摊比例" prop="allocationPercentage">
                  <el-input-number
                    v-model="form.allocationPercentage"
                    :disabled="isView"
                    :precision="2"
                    :min="0"
                    :max="100"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="预算金额" prop="budgetAmount">
                  <el-input-number
                    v-model="form.budgetAmount"
                    :disabled="isView"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实际金额" prop="actualAmount">
                  <el-input-number
                    v-model="form.actualAmount"
                    :disabled="true"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="差异金额" prop="varianceAmount">
                  <el-input-number
                    v-model="form.varianceAmount"
                    :disabled="true"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="差异率" prop="varianceRate">
                  <el-input-number
                    v-model="form.varianceRate"
                    :disabled="true"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 计算信息 -->
          <el-tab-pane label="计算信息" name="calculation">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计算状态" prop="calculationStatus">
                  <el-select
                    v-model="form.calculationStatus"
                    :disabled="true"
                    style="width: 100%"
                  >
                    <el-option label="待计算" value="PENDING" />
                    <el-option label="计算中" value="RUNNING" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="计算失败" value="FAILED" />
                    <el-option label="已取消" value="CANCELLED" />
                    <el-option label="已暂停" value="PAUSED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计算进度" prop="calculationProgress">
                  <el-progress
                    :percentage="form.calculationProgress || 0"
                    :status="getProgressStatus(form.calculationStatus)"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="开始时间" prop="startTime">
                  <el-date-picker
                    v-model="form.startTime"
                    :disabled="isView"
                    type="datetime"
                    placeholder="选择开始时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结束时间" prop="endTime">
                  <el-date-picker
                    v-model="form.endTime"
                    :disabled="isView"
                    type="datetime"
                    placeholder="选择结束时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计算开始时间" prop="calculationStartTime">
                  <el-input
                    v-model="form.calculationStartTime"
                    :disabled="true"
                    placeholder="计算开始时间"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计算结束时间" prop="calculationEndTime">
                  <el-input
                    v-model="form.calculationEndTime"
                    :disabled="true"
                    placeholder="计算结束时间"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计算耗时" prop="calculationDuration">
                  <el-input
                    v-model="form.calculationDuration"
                    :disabled="true"
                    placeholder="计算耗时（秒）"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="成功率" prop="successRate">
                  <el-input-number
                    v-model="form.successRate"
                    :disabled="true"
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="计算结果" prop="calculationResult">
              <el-input
                v-model="form.calculationResult"
                :disabled="true"
                type="textarea"
                :rows="3"
                placeholder="计算结果"
              />
            </el-form-item>

            <el-form-item label="错误信息" prop="errorMessage">
              <el-input
                v-model="form.errorMessage"
                :disabled="true"
                type="textarea"
                :rows="3"
                placeholder="错误信息"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 审批信息 -->
          <el-tab-pane label="审批信息" name="approval">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="审批状态" prop="approvalStatus">
                  <el-select
                    v-model="form.approvalStatus"
                    :disabled="true"
                    style="width: 100%"
                  >
                    <el-option label="待审批" value="PENDING" />
                    <el-option label="已审批" value="APPROVED" />
                    <el-option label="已拒绝" value="REJECTED" />
                    <el-option label="已取消" value="CANCELLED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="审批人" prop="approver">
                  <el-input
                    v-model="form.approver"
                    :disabled="true"
                    placeholder="审批人"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="审批时间" prop="approvalTime">
                  <el-input
                    v-model="form.approvalTime"
                    :disabled="true"
                    placeholder="审批时间"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="提交时间" prop="submitTime">
                  <el-input
                    v-model="form.submitTime"
                    :disabled="true"
                    placeholder="提交时间"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="审批意见" prop="approvalComments">
              <el-input
                v-model="form.approvalComments"
                :disabled="true"
                type="textarea"
                :rows="3"
                placeholder="审批意见"
              />
            </el-form-item>

            <el-form-item label="拒绝原因" prop="rejectionReason">
              <el-input
                v-model="form.rejectionReason"
                :disabled="true"
                type="textarea"
                :rows="3"
                placeholder="拒绝原因"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 其他信息 -->
          <el-tab-pane label="其他信息" name="other">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="创建人" prop="createdBy">
                  <el-input
                    v-model="form.createdBy"
                    :disabled="true"
                    placeholder="创建人"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建时间" prop="createdTime">
                  <el-input
                    v-model="form.createdTime"
                    :disabled="true"
                    placeholder="创建时间"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="更新人" prop="updatedBy">
                  <el-input
                    v-model="form.updatedBy"
                    :disabled="true"
                    placeholder="更新人"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="更新时间" prop="updatedTime">
                  <el-input
                    v-model="form.updatedTime"
                    :disabled="true"
                    placeholder="更新时间"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="备注" prop="remarks">
              <el-input
                v-model="form.remarks"
                :disabled="isView"
                type="textarea"
                :rows="4"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSave">保存</el-button>
      <el-button v-if="isView && canEdit" type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getCostAllocationById,
  createCostAllocation,
  updateCostAllocation,
  checkCodeExists,
  checkNameExists
} from '@/api/managementAccountant/ss/costAllocation'

export default {
  name: 'CostAllocationDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    costAllocationId: {
      type: [String, Number],
      default: null
    },
    mode: {
      type: String,
      default: 'view' // view, edit, create
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      form: {
        allocationId: null,
        allocationCode: '',
        allocationName: '',
        allocationType: '',
        allocationMethod: '',
        allocationStatus: 'DRAFT',
        allocationDescription: '',
        priority: 5,
        allocationPeriod: '',
        costCenterId: null,
        totalCostAmount: null,
        allocatedAmount: null,
        remainingAmount: null,
        allocationPercentage: null,
        budgetAmount: null,
        actualAmount: null,
        varianceAmount: null,
        varianceRate: null,
        calculationStatus: 'PENDING',
        calculationProgress: 0,
        startTime: null,
        endTime: null,
        calculationStartTime: '',
        calculationEndTime: '',
        calculationDuration: null,
        successRate: null,
        calculationResult: '',
        errorMessage: '',
        approvalStatus: 'PENDING',
        approver: '',
        approvalTime: '',
        submitTime: '',
        approvalComments: '',
        rejectionReason: '',
        createdBy: '',
        createdTime: '',
        updatedBy: '',
        updatedTime: '',
        remarks: ''
      },
      rules: {
        allocationCode: [
          { required: true, message: '请输入分摊编码', trigger: 'blur' },
          { validator: this.validateCode, trigger: 'blur' }
        ],
        allocationName: [
          { required: true, message: '请输入分摊名称', trigger: 'blur' },
          { validator: this.validateName, trigger: 'blur' }
        ],
        allocationType: [
          { required: true, message: '请选择分摊类型', trigger: 'change' }
        ],
        allocationMethod: [
          { required: true, message: '请选择分摊方法', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ]
      }
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
    },
    dialogTitle() {
      const titleMap = {
        view: '查看成本分摊',
        edit: '编辑成本分摊',
        create: '新建成本分摊'
      }
      return titleMap[this.mode] || '成本分摊详情'
    },
    isView() {
      return this.mode === 'view'
    },
    isEdit() {
      return this.mode === 'edit'
    },
    isCreate() {
      return this.mode === 'create'
    },
    canEdit() {
      return this.form.allocationStatus === 'DRAFT'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
      }
    },
    costAllocationId(val) {
      if (val && this.visible) {
        this.loadData()
      }
    }
  },
  methods: {
    // 初始化数据
    initData() {
      this.activeTab = 'basic'
      if (this.isCreate) {
        this.resetForm()
      } else if (this.costAllocationId) {
        this.loadData()
      }
    },

    // 加载数据
    async loadData() {
      if (!this.costAllocationId) return

      this.loading = true
      try {
        const response = await getCostAllocationById(this.costAllocationId)
        if (response.success) {
          this.form = { ...this.form, ...response.data }
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        allocationId: null,
        allocationCode: '',
        allocationName: '',
        allocationType: '',
        allocationMethod: '',
        allocationStatus: 'DRAFT',
        allocationDescription: '',
        priority: 5,
        allocationPeriod: '',
        costCenterId: null,
        totalCostAmount: null,
        allocatedAmount: null,
        remainingAmount: null,
        allocationPercentage: null,
        budgetAmount: null,
        actualAmount: null,
        varianceAmount: null,
        varianceRate: null,
        calculationStatus: 'PENDING',
        calculationProgress: 0,
        startTime: null,
        endTime: null,
        calculationStartTime: '',
        calculationEndTime: '',
        calculationDuration: null,
        successRate: null,
        calculationResult: '',
        errorMessage: '',
        approvalStatus: 'PENDING',
        approver: '',
        approvalTime: '',
        submitTime: '',
        approvalComments: '',
        rejectionReason: '',
        createdBy: '',
        createdTime: '',
        updatedBy: '',
        updatedTime: '',
        remarks: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },

    // 保存
    async handleSave() {
      try {
        await this.$refs.form.validate()
        
        this.loading = true
        let response
        if (this.isCreate) {
          response = await createCostAllocation(this.form)
        } else {
          response = await updateCostAllocation(this.form)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '更新成功')
          this.$emit('refresh')
          this.handleClose()
        } else {
          this.$message.error(response.message || (this.isCreate ? '创建失败' : '更新失败'))
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时不显示错误消息
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        }
      } finally {
        this.loading = false
      }
    },

    // 编辑
    handleEdit() {
      this.$emit('update:mode', 'edit')
    },

    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },

    // 验证编码
    async validateCode(rule, value, callback) {
      if (!value) {
        callback()
        return
      }

      try {
        const response = await checkCodeExists(value, this.form.allocationId)
        if (response.success && response.data) {
          callback(new Error('分摊编码已存在'))
        } else {
          callback()
        }
      } catch (error) {
        console.error('验证编码失败:', error)
        callback()
      }
    },

    // 验证名称
    async validateName(rule, value, callback) {
      if (!value) {
        callback()
        return
      }

      try {
        const response = await checkNameExists(value, this.form.allocationId)
        if (response.success && response.data) {
          callback(new Error('分摊名称已存在'))
        } else {
          callback()
        }
      } catch (error) {
        console.error('验证名称失败:', error)
        callback()
      }
    },

    // 获取进度状态
    getProgressStatus(calculationStatus) {
      const statusMap = {
        'PENDING': '',
        'RUNNING': '',
        'COMPLETED': 'success',
        'FAILED': 'exception',
        'CANCELLED': 'warning',
        'PAUSED': 'warning'
      }
      return statusMap[calculationStatus] || ''
    }
  }
}
</script>

<style scoped>
.cost-allocation-detail {
  max-height: 600px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}

.el-tabs {
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 18px;
}
</style>
