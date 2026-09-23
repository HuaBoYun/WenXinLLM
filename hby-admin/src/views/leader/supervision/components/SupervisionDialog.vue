<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="720px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="supervisionForm"
      :model="form"
      :rules="rules"
      :disabled="dialogType === 'view'"
      label-width="100px"
      label-position="right"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责人" prop="leaderName">
            <el-input v-model="form.leaderName" placeholder="请输入负责人姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属企业" prop="enterpriseName">
            <el-input v-model="form.enterpriseName" placeholder="请输入所属企业" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="监管类型" prop="supervisionType">
            <el-select v-model="form.supervisionType" placeholder="请选择监管类型" style="width: 100%;">
              <el-option label="任职资格" value="任职资格" />
              <el-option label="履职情况" value="履职情况" />
              <el-option label="合规性检查" value="合规性检查" />
              <el-option label="风险预警" value="风险预警" />
              <el-option label="专项监管" value="专项监管" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监管时间" prop="supervisionDate">
            <el-date-picker
              v-model="form.supervisionDate"
              type="date"
              placeholder="请选择监管时间"
              value-format="yyyy-MM-dd"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
              <el-option label="低风险" value="低风险" />
              <el-option label="中风险" value="中风险" />
              <el-option label="高风险" value="高风险" />
              <el-option label="极高风险" value="极高风险" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改状态" prop="rectificationStatus">
            <el-select v-model="form.rectificationStatus" placeholder="请选择整改状态" style="width: 100%;">
              <el-option label="合规" value="合规" />
              <el-option label="基本合规" value="基本合规" />
              <el-option label="不合规" value="不合规" />
              <el-option label="待整改" value="待整改" />
              <el-option label="已整改" value="已整改" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="监管人" prop="supervisor">
            <el-input v-model="form.supervisor" placeholder="请输入监管人" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="监管内容" prop="supervisionContent">
            <el-input
              v-model="form.supervisionContent"
              type="textarea"
              :rows="3"
              placeholder="请输入监管内容描述"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="发现问题" prop="findingDesc">
            <el-input
              v-model="form.findingDesc"
              type="textarea"
              :rows="3"
              placeholder="请输入发现的问题"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addLeaderSupervision, updateLeaderSupervision } from '@/api/leader/index'

export default {
  name: 'SupervisionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add'
    },
    supervisionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitLoading: false,
      form: this.getInitForm(),
      rules: {
        leaderName: [{ required: true, message: '请输入负责人姓名', trigger: 'blur' }],
        enterpriseName: [{ required: true, message: '请输入所属企业', trigger: 'blur' }],
        supervisionType: [{ required: true, message: '请选择监管类型', trigger: 'change' }],
        supervisionDate: [{ required: true, message: '请选择监管时间', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        rectificationStatus: [{ required: true, message: '请选择整改状态', trigger: 'change' }],
        supervisor: [{ required: true, message: '请输入监管人', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      const titleMap = { add: '发起监管', edit: '编辑监管', view: '监管详情' }
      return titleMap[this.dialogType] || '发起监管'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        if (this.dialogType === 'add') {
          // 新增模式：初始化表单，但保留supervisionData中预填的字段（如从Tab新增时预填supervisionType）
          const initForm = this.getInitForm()
          this.form = { ...initForm, ...this.supervisionData }
        } else {
          this.form = { ...this.supervisionData }
        }
      }
    }
  },
  methods: {
    getInitForm() {
      return {
        leaderName: '',
        enterpriseName: '',
        supervisionType: '',
        supervisionDate: '',
        riskLevel: '',
        rectificationStatus: '',
        supervisor: '',
        supervisionContent: '',
        findingDesc: ''
      }
    },
    handleSubmit() {
      this.$refs.supervisionForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          if (this.dialogType === 'add') {
            await addLeaderSupervision(this.form)
            this.$message.success('发起监管成功')
          } else {
            await updateLeaderSupervision(this.form)
            this.$message.success('更新成功')
          }
          this.$emit('refresh')
          this.handleClose()
        } catch (error) {
          this.$message.error(this.dialogType === 'add' ? '发起监管失败' : '更新失败')
          console.error('提交失败:', error)
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.$nextTick(() => {
        this.$refs.supervisionForm && this.$refs.supervisionForm.resetFields()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
