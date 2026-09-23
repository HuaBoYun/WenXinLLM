<template>
  <el-dialog
    title="折旧预览"
    :visible.sync="visible"
    width="1200px"
    @close="handleClose"
  >
    <div class="preview-header">
      <el-descriptions :column="4" border size="small">
        <el-descriptions-item label="计提期间">{{ previewData.period }}</el-descriptions-item>
        <el-descriptions-item label="资产数量">{{ previewData.assetCount }}</el-descriptions-item>
        <el-descriptions-item label="折旧总额">{{ formatAmount(previewData.totalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="计提方式">{{ previewData.calculateType }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="preview-table">
      <el-table
        :data="previewList"
        v-loading="loading"
        border
        stripe
        max-height="450"
      >
        <el-table-column prop="assetCode" label="资产编码" width="120" />
        <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="资产类别" width="120" />
        <el-table-column prop="depreciationMethod" label="折旧方法" width="120" />
        <el-table-column prop="originalValue" label="资产原值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.originalValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="accumulatedDepreciation" label="累计折旧" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.accumulatedDepreciation) }}
          </template>
        </el-table-column>
        <el-table-column prop="currentDepreciation" label="本期折旧" width="120" align="right">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold">
              {{ formatAmount(scope.row.currentDepreciation) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="netBookValue" label="账面净值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.netBookValue) }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="confirming">确认计提</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DepreciationPreviewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    previewData: {
      type: Object,
      default: () => ({})
    },
    previewList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loading: false,
      confirming: false
    }
  },
  methods: {
    handleConfirm() {
      this.$emit('confirm')
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.preview-header {
  margin-bottom: 20px;
}

.preview-table {
  margin-bottom: 20px;
}
</style>

