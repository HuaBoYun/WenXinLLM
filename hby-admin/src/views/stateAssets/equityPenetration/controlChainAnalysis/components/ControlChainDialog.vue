<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="chainForm"
      :model="chainForm"
      :rules="chainRules"
      label-width="120px"
      :disabled="dialogType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="chainForm.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="统计周期" prop="statPeriod">
            <el-select v-model="chainForm.statPeriod" placeholder="请选择统计周期" style="width: 100%;">
              <el-option label="月度" value="月度"></el-option>
              <el-option label="季度" value="季度"></el-option>
              <el-option label="年度" value="年度"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="控制链总数" prop="totalChains">
            <el-input-number v-model="chainForm.totalChains" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="直接控制链" prop="directChains">
            <el-input-number v-model="chainForm.directChains" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="间接控制链" prop="indirectChains">
            <el-input-number v-model="chainForm.indirectChains" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="平均链长" prop="avgChainLength">
            <el-input-number v-model="chainForm.avgChainLength" :min="0" :precision="2" :step="0.1" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最大链长" prop="maxChainLength">
            <el-input-number v-model="chainForm.maxChainLength" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="平均控制强度" prop="avgControlStrength">
            <el-input-number v-model="chainForm.avgControlStrength" :min="0" :max="100" :precision="2" :step="0.1" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="环路数" prop="loopCount">
            <el-input-number v-model="chainForm.loopCount" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="高风险数" prop="highRiskCount">
            <el-input-number v-model="chainForm.highRiskCount" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="中风险数" prop="mediumRiskCount">
            <el-input-number v-model="chainForm.mediumRiskCount" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="低风险数" prop="lowRiskCount">
            <el-input-number v-model="chainForm.lowRiskCount" :min="0" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="submitLoading"
      >
        {{ dialogType === 'add' ? '确定' : '保存' }}
      </el-button>
      <el-button
        v-if="dialogType === 'analyze'"
        type="success"
        @click="handleAnalyze"
        :loading="analyzeLoading"
      >
        开始分析
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  addControlChain,
  updateControlChain,
  startControlChainAnalysis
} from '@/api/stateAssets/controlChainAnalysis'

export default {
  name: 'ControlChainDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    chainData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view, analyze
    }
  },
  data() {
    return {
      submitLoading: false,
      analyzeLoading: false,
      chainForm: {
        statId: '',
        enterpriseName: '',
        totalChains: 0,
        directChains: 0,
        indirectChains: 0,
        avgChainLength: 0,
        maxChainLength: 0,
        avgControlStrength: 0,
        loopCount: 0,
        highRiskCount: 0,
        mediumRiskCount: 0,
        lowRiskCount: 0,
        statPeriod: ''
      },
      chainRules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        statPeriod: [
          { required: true, message: '请选择统计周期', trigger: 'change' }
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
        add: '新增控制链统计',
        edit: '编辑控制链统计',
        view: '查看控制链统计',
        analyze: '分析控制链'
      }
      return titleMap[this.dialogType] || '控制链统计'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.dialogType !== 'add') {
        this.chainForm = { ...this.chainData }
      } else {
        this.chainForm = {
          statId: '',
          enterpriseName: '',
          totalChains: 0,
          directChains: 0,
          indirectChains: 0,
          avgChainLength: 0,
          maxChainLength: 0,
          avgControlStrength: 0,
          loopCount: 0,
          highRiskCount: 0,
          mediumRiskCount: 0,
          lowRiskCount: 0,
          statPeriod: ''
        }
      }
      this.$nextTick(() => {
        this.$refs.chainForm && this.$refs.chainForm.clearValidate()
      })
    },

    // 提交表单
    handleSubmit() {
      this.$refs.chainForm.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          if (this.dialogType === 'add') {
            await addControlChain(this.chainForm)
            this.$message.success('新增成功')
          } else {
            await updateControlChain(this.chainForm)
            this.$message.success('保存成功')
          }
          this.handleClose()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 开始分析
    handleAnalyze() {
      this.$refs.chainForm.validate(async (valid) => {
        if (!valid) return

        this.analyzeLoading = true
        try {
          await startControlChainAnalysis({
            statId: this.chainForm.statId,
            ...this.chainForm
          })
          this.$message.success('分析任务已启动')
          this.handleClose()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('启动分析失败')
        } finally {
          this.analyzeLoading = false
        }
      })
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.submitLoading = false
      this.analyzeLoading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
