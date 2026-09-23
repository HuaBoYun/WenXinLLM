<template>
  <el-dialog title="供应商评估" :visible.sync="dialogVisible" width="600px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" v-if="supplierData">
      <el-descriptions :column="2" border style="margin-bottom: 20px">
        <el-descriptions-item label="供应商名称">{{ supplierData.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="当前评级">{{ supplierData.rating || '未评级' }}</el-descriptions-item>
      </el-descriptions>
      <el-form-item label="质量评分" prop="qualityScore">
        <el-slider v-model="form.qualityScore" :max="100" show-input />
      </el-form-item>
      <el-form-item label="交付评分" prop="deliveryScore">
        <el-slider v-model="form.deliveryScore" :max="100" show-input />
      </el-form-item>
      <el-form-item label="价格评分" prop="priceScore">
        <el-slider v-model="form.priceScore" :max="100" show-input />
      </el-form-item>
      <el-divider />
      <el-form-item label="综合评分">
        <el-tag type="primary" size="medium">{{ computedOverall }} 分</el-tag>
        <el-tag :type="computedRatingType" size="medium" style="margin-left:10px">{{ computedRating }}级</el-tag>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">提交评估</el-button>
    </span>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'SupplierEvaluationDialog',
  props: {
    visible: { type: Boolean, default: false },
    supplierData: { type: Object, default: null }
  },
  data() {
    return {
      dialogVisible: this.visible,
      submitting: false,
      form: { qualityScore: 80, deliveryScore: 80, priceScore: 80 },
      rules: {
        qualityScore: [{ required: true, message: '请设置质量评分', trigger: 'change' }],
        deliveryScore: [{ required: true, message: '请设置交付评分', trigger: 'change' }],
        priceScore: [{ required: true, message: '请设置价格评分', trigger: 'change' }]
      }
    }
  },
  computed: {
    computedOverall() {
      return ((this.form.qualityScore + this.form.deliveryScore + this.form.priceScore) / 3).toFixed(1)
    },
    computedRating() {
      const score = parseFloat(this.computedOverall)
      if (score >= 90) return 'A'
      if (score >= 75) return 'B'
      if (score >= 60) return 'C'
      return 'D'
    },
    computedRatingType() {
      const map = { A: 'success', B: 'primary', C: 'warning', D: 'danger' }
      return map[this.computedRating] || 'info'
    }
  },
  watch: {
    visible(val) { this.dialogVisible = val },
    supplierData: {
      immediate: true,
      handler(val) {
        if (val) {
          this.form.qualityScore = val.qualityScore ? Number(val.qualityScore) : 80
          this.form.deliveryScore = val.deliveryScore ? Number(val.deliveryScore) : 80
          this.form.priceScore = val.priceScore ? Number(val.priceScore) : 80
        }
      }
    }
  },
  methods: {
    handleClose() { this.$emit('close') },
    async handleSubmit() {
      this.submitting = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/evaluate',
          method: 'post',
          data: { id: this.supplierData.id, ...this.form },
          headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
        if (res && res.result === 200) {
          this.$message.success('评估成功')
          this.$emit('success')
        } else {
          this.$message.error(res.msg || '评估失败')
        }
      } catch (e) {
        this.$message.error('评估失败')
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>
