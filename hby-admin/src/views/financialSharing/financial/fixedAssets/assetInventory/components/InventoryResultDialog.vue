<template>
  <el-dialog
    title="盘点结果录入"
    :visible.sync="visible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="inventory-result-container">
      <el-form
        ref="resultForm"
        :model="formData"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务编号">
              <el-input v-model="formData.taskNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="盘点日期">
              <el-date-picker
                v-model="formData.inventoryDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="assets-table">
        <h4>资产盘点清单</h4>
        <el-table
          :data="assetsList"
          border
          stripe
          max-height="400"
        >
          <el-table-column prop="assetCode" label="资产编码" width="120" />
          <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="center" />
          <el-table-column label="实盘数量" width="120" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.actualQuantity"
                :min="0"
                :max="999"
                size="small"
                controls-position="right"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column label="差异数量" width="100" align="center">
            <template slot-scope="scope">
              <span :class="getDifferenceClass(scope.row)">
                {{ getDifference(scope.row) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="盘点结果" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getResultType(scope.row)" size="small">
                {{ getResultText(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="location" label="存放地点" width="120" />
          <el-table-column label="备注" min-width="150">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.remark"
                placeholder="请输入备注"
                size="small"
              />
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'InventoryResultDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      formData: {
        taskNumber: '',
        inventoryDate: ''
      },
      assetsList: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.taskData) {
        this.formData.taskNumber = this.taskData.taskNumber || ''
        this.loadAssetsList()
      }
    }
  },
  methods: {
    loadAssetsList() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.assetsList = []
    },
    getDifference(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      return diff > 0 ? `+${diff}` : diff
    },
    getDifferenceClass(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      if (diff > 0) return 'difference-surplus'
      if (diff < 0) return 'difference-shortage'
      return 'difference-normal'
    },
    getResultText(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      if (diff > 0) return '盘盈'
      if (diff < 0) return '盘亏'
      return '正常'
    },
    getResultType(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      if (diff > 0) return 'success'
      if (diff < 0) return 'danger'
      return 'info'
    },
    handleSubmit() {
      if (!this.formData.inventoryDate) {
        this.$message.warning('请选择盘点日期')
        return
      }
      this.$emit('submit', {
        ...this.formData,
        results: this.assetsList
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-result-container {
  padding: 10px 0;
}

.assets-table {
  margin-top: 20px;

  h4 {
    margin-bottom: 10px;
    color: #303133;
  }
}

.difference-surplus {
  color: #67c23a;
  font-weight: 600;
}

.difference-shortage {
  color: #f56c6c;
  font-weight: 600;
}

.difference-normal {
  color: #909399;
}
</style>

