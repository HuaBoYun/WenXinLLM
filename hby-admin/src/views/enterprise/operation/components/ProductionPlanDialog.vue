<template>
  <el-dialog
    title="生产计划详情"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form :model="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划编号">
            <el-input v-model="form.planCode" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称">
            <el-input v-model="form.planName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划类型">
            <el-select v-model="form.planType" placeholder="请选择计划类型">
              <el-option label="日计划" value="daily"></el-option>
              <el-option label="周计划" value="weekly"></el-option>
              <el-option label="月计划" value="monthly"></el-option>
              <el-option label="季度计划" value="quarterly"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生产线">
            <el-select v-model="form.productionLine" placeholder="请选择生产线">
              <el-option label="生产线A" value="line_a"></el-option>
              <el-option label="生产线B" value="line_b"></el-option>
              <el-option label="生产线C" value="line_c"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划开始时间">
            <el-date-picker
              v-model="form.startDate"
              type="datetime"
              placeholder="选择开始时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划结束时间">
            <el-date-picker
              v-model="form.endDate"
              type="datetime"
              placeholder="选择结束时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="计划产量">
            <el-input v-model="form.plannedQuantity" type="number">
              <template slot="append">件</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="实际产量">
            <el-input v-model="form.actualQuantity" type="number">
              <template slot="append">件</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="完成率">
            <el-input :value="completionRate + '%'" disabled>
              <template slot="append">%</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="产品信息">
        <el-table :data="form.products" border>
          <el-table-column prop="productCode" label="产品编号" width="120"></el-table-column>
          <el-table-column prop="productName" label="产品名称" width="200"></el-table-column>
          <el-table-column prop="plannedQty" label="计划数量" width="120" align="right"></el-table-column>
          <el-table-column prop="actualQty" label="实际数量" width="120" align="right"></el-table-column>
          <el-table-column prop="completionRate" label="完成率" width="120" align="center">
            <template slot-scope="scope">
              <el-progress :percentage="scope.row.completionRate" :stroke-width="8"></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="editProduct(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      
      <el-form-item label="计划状态">
        <el-radio-group v-model="form.status">
          <el-radio label="draft">草稿</el-radio>
          <el-radio label="approved">已批准</el-radio>
          <el-radio label="executing">执行中</el-radio>
          <el-radio label="completed">已完成</el-radio>
          <el-radio label="cancelled">已取消</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remarks" :rows="3"></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="savePlan">保存计划</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ProductionPlanDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    planData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      form: {
        planCode: 'PLAN001',
        planName: '2024年1月生产计划',
        planType: 'monthly',
        productionLine: 'line_a',
        startDate: '2024-01-01 08:00:00',
        endDate: '2024-01-31 18:00:00',
        plannedQuantity: 10000,
        actualQuantity: 8500,
        status: 'executing',
        remarks: '按照订单需求制定的月度生产计划',
        products: [
          {
            productCode: 'P001',
            productName: '产品A',
            plannedQty: 5000,
            actualQty: 4200,
            completionRate: 84,
            status: 'producing'
          },
          {
            productCode: 'P002',
            productName: '产品B',
            plannedQty: 3000,
            actualQty: 2800,
            completionRate: 93,
            status: 'producing'
          },
          {
            productCode: 'P003',
            productName: '产品C',
            plannedQty: 2000,
            actualQty: 1500,
            completionRate: 75,
            status: 'producing'
          }
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
    completionRate() {
      if (!this.form.plannedQuantity || this.form.plannedQuantity === 0) {
        return 0
      }
      return Math.round((this.form.actualQuantity / this.form.plannedQuantity) * 100)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getStatusType(status) {
      const statusMap = {
        'pending': 'info',
        'producing': 'primary',
        'completed': 'success',
        'paused': 'warning',
        'cancelled': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'pending': '待生产',
        'producing': '生产中',
        'completed': '已完成',
        'paused': '已暂停',
        'cancelled': '已取消'
      }
      return textMap[status] || status
    },
    editProduct(row) {
      this.$message.info('编辑产品：' + row.productName)
    },
    savePlan() {
      this.$message.success('保存生产计划成功')
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
