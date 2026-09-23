<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="作业编码" prop="activityCode">
            <el-input
              v-model="form.activityCode"
              placeholder="请输入作业编码"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="作业名称" prop="activityName">
            <el-input
              v-model="form.activityName"
              placeholder="请输入作业名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="作业类型" prop="activityType">
            <el-select
              v-model="form.activityType"
              placeholder="请选择作业类型"
              style="width: 100%"
            >
              <el-option label="生产作业" value="1" />
              <el-option label="辅助作业" value="2" />
              <el-option label="管理作业" value="3" />
              <el-option label="销售作业" value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成本动因" prop="costDriver">
            <el-select
              v-model="form.costDriver"
              placeholder="请选择成本动因"
              style="width: 100%"
            >
              <el-option label="机器小时" value="machine_hours" />
              <el-option label="人工小时" value="labor_hours" />
              <el-option label="产品数量" value="product_quantity" />
              <el-option label="订单数量" value="order_quantity" />
              <el-option label="检验次数" value="inspection_times" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="动因数量" prop="driverQuantity">
            <el-input-number
              v-model="form.driverQuantity"
              :precision="2"
              :step="1"
              :min="0"
              style="width: 100%"
              placeholder="请输入动因数量"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总成本" prop="totalCost">
            <el-input-number
              v-model="form.totalCost"
              :precision="2"
              :step="1000"
              :min="0"
              style="width: 100%"
              placeholder="请输入总成本"
              @change="calculateUnitCost"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="单位成本" prop="unitCost">
            <el-input-number
              v-model="form.unitCost"
              :precision="4"
              :step="0.01"
              :min="0"
              style="width: 100%"
              placeholder="单位成本（自动计算）"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属部门" prop="department">
            <el-input
              v-model="form.department"
              placeholder="请输入所属部门"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="成本中心" prop="costCenter">
            <el-select
              v-model="form.costCenter"
              placeholder="请选择成本中心"
              style="width: 100%"
              filterable
            >
              <el-option
                v-for="item in costCenterOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="form.status"
              placeholder="请选择状态"
              style="width: 100%"
            >
              <el-option label="活跃" value="1" />
              <el-option label="暂停" value="2" />
              <el-option label="停用" value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="作业描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入作业描述"
        />
      </el-form-item>
      
      <!-- 成本构成明细 -->
      <el-form-item label="成本构成">
        <el-table
          :data="form.costItems"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="costType"
            label="成本类型"
            width="120"
          >
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.costType"
                placeholder="选择类型"
                size="small"
              >
                <el-option label="直接材料" value="direct_material" />
                <el-option label="直接人工" value="direct_labor" />
                <el-option label="制造费用" value="manufacturing_overhead" />
                <el-option label="其他费用" value="other_cost" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            prop="amount"
            label="金额"
            width="150"
          >
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.amount"
                :precision="2"
                :step="100"
                :min="0"
                size="small"
                style="width: 100%"
                @change="updateTotalCost"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
          >
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.remark"
                placeholder="备注"
                size="small"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="80"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                style="color: #f56c6c"
                @click="removeCostItem(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button
          type="text"
          icon="el-icon-plus"
          style="margin-top: 10px"
          @click="addCostItem"
        >
          添加成本项
        </el-button>
      </el-form-item>
      
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button
        type="primary"
        :loading="buttonLoading"
        @click="save"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateActivityCost, getActivityCostById } from '@/api/financialSharing/specialCost'
import { getCostCenterOptions } from '@/api/financialSharing/costCenter'

export default {
  name: 'ActivityCostEdit',
  data() {
    return {
      dialogVisible: false,
      buttonLoading: false,
      isEdit: false,
      title: '',
      form: {
        id: null,
        activityCode: '',
        activityName: '',
        activityType: '',
        costDriver: '',
        driverQuantity: 0,
        totalCost: 0,
        unitCost: 0,
        department: '',
        costCenter: '',
        status: '1',
        description: '',
        remark: '',
        costItems: []
      },
      rules: {
        activityCode: [
          { required: true, message: '请输入作业编码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        activityName: [
          { required: true, message: '请输入作业名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        activityType: [
          { required: true, message: '请选择作业类型', trigger: 'change' }
        ],
        costDriver: [
          { required: true, message: '请选择成本动因', trigger: 'change' }
        ],
        driverQuantity: [
          { required: true, message: '请输入动因数量', trigger: 'blur' },
          { type: 'number', min: 0, message: '动因数量必须大于等于0', trigger: 'blur' }
        ],
        totalCost: [
          { required: true, message: '请输入总成本', trigger: 'blur' },
          { type: 'number', min: 0, message: '总成本必须大于等于0', trigger: 'blur' }
        ]
      },
      costCenterOptions: []
    }
  },
  created() {
    this.loadCostCenterOptions()
  },
  methods: {
    async showEdit(row) {
      this.dialogVisible = true
      this.isEdit = !!row
      this.title = row ? '编辑作业成本' : '新增作业成本'
      
      if (row) {
        this.buttonLoading = true
        const { code, data } = await getActivityCostById(row.id)
        if (code === 200) {
          this.form = { ...data }
          if (!this.form.costItems) {
            this.form.costItems = []
          }
        }
        this.buttonLoading = false
      } else {
        this.form = {
          id: null,
          activityCode: '',
          activityName: '',
          activityType: '',
          costDriver: '',
          driverQuantity: 0,
          totalCost: 0,
          unitCost: 0,
          department: '',
          costCenter: '',
          status: '1',
          description: '',
          remark: '',
          costItems: []
        }
      }
      
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    close() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    async save() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.buttonLoading = true
          
          const { code, msg } = await saveOrUpdateActivityCost(this.form)
          if (code === 200) {
            this.$baseMessage(msg, 'success')
            this.$emit('fetch-data')
            this.close()
          } else {
            this.$baseMessage(msg, 'error')
          }
          this.buttonLoading = false
        }
      })
    },
    calculateUnitCost() {
      if (this.form.driverQuantity > 0) {
        this.form.unitCost = this.form.totalCost / this.form.driverQuantity
      } else {
        this.form.unitCost = 0
      }
    },
    addCostItem() {
      this.form.costItems.push({
        costType: '',
        amount: 0,
        remark: ''
      })
    },
    removeCostItem(index) {
      this.form.costItems.splice(index, 1)
      this.updateTotalCost()
    },
    updateTotalCost() {
      this.form.totalCost = this.form.costItems.reduce((sum, item) => sum + (item.amount || 0), 0)
      this.calculateUnitCost()
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'amount') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0).toFixed(2)
          } else {
            sums[index] = '0.00'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    async loadCostCenterOptions() {
      try {
        const { code, data } = await getCostCenterOptions()
        if (code === 200) {
          this.costCenterOptions = data.map(item => ({
            label: item.centerName,
            value: item.centerId
          }))
        }
      } catch (error) {
        console.error('加载成本中心选项失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
