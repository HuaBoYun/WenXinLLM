<template>
  <el-dialog
    :title="isEdit ? '编辑任务' : '新增任务'"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    @closed="handleClosed"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskName">
            <el-input
              v-model="form.taskName"
              placeholder="请输入任务名称"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务类型" prop="taskType">
            <el-select v-model="form.taskType" placeholder="请选择任务类型" style="width: 100%">
              <el-option label="定期报送" value="REGULAR" />
              <el-option label="专项报送" value="SPECIAL" />
              <el-option label="紧急报送" value="URGENT" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务分类" prop="taskCategory">
            <el-select v-model="form.taskCategory" placeholder="请选择任务分类" style="width: 100%">
              <el-option label="财务数据" value="FINANCIAL" />
              <el-option label="经营数据" value="OPERATIONAL" />
              <el-option label="治理数据" value="GOVERNANCE" />
              <el-option label="风险数据" value="RISK" />
              <el-option label="合规数据" value="COMPLIANCE" />
              <el-option label="绩效数据" value="PERFORMANCE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报送周期" prop="submitCycle">
            <el-select v-model="form.submitCycle" placeholder="请选择报送周期" style="width: 100%">
              <el-option label="日报" value="DAILY" />
              <el-option label="周报" value="WEEKLY" />
              <el-option label="月报" value="MONTHLY" />
              <el-option label="季报" value="QUARTERLY" />
              <el-option label="年报" value="YEARLY" />
              <el-option label="一次性" value="ONCE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker
              v-model="form.startDate"
              type="date"
              placeholder="请选择开始日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker
              v-model="form.endDate"
              type="date"
              placeholder="请选择结束日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="数据模板" prop="dataTemplate">
        <el-input
          v-model="form.dataTemplate"
          placeholder="请输入数据模板"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="任务描述" prop="taskDescription">
        <el-input
          v-model="form.taskDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入任务描述"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addTask, updateTask, validateTaskName } from '@/api/stateAssets/dataCollection'

export default {
  name: 'TaskForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    // 自定义验证规则 - 任务名称唯一性
    const validateTaskNameUnique = (rule, value, callback) => {
      if (!value) {
        callback()
        return
      }
      
      // 防抖处理
      clearTimeout(this.validateTimer)
      this.validateTimer = setTimeout(async () => {
        try {
          const params = {
            taskName: value,
            excludeId: this.isEdit ? this.form.taskId : null
          }
          const { data } = await validateTaskName(params)
          if (data.isDuplicate) {
            callback(new Error('任务名称已存在'))
          } else {
            callback()
          }
        } catch (error) {
          callback()
        }
      }, 500)
    }

    // 自定义验证规则 - 结束日期
    const validateEndDate = (rule, value, callback) => {
      if (!value) {
        callback()
        return
      }
      
      if (this.form.startDate && value < this.form.startDate) {
        callback(new Error('结束日期不能早于开始日期'))
      } else {
        callback()
      }
    }

    return {
      loading: false,
      submitLoading: false,
      validateTimer: null,
      form: {
        taskId: '',
        taskName: '',
        taskType: '',
        taskCategory: '',
        submitCycle: '',
        startDate: '',
        endDate: '',
        dataTemplate: '',
        taskDescription: '',
        remark: ''
      },
      rules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' },
          { min: 2, max: 100, message: '任务名称长度在 2 到 100 个字符', trigger: 'blur' },
          { validator: validateTaskNameUnique, trigger: 'blur' }
        ],
        taskType: [
          { required: true, message: '请选择任务类型', trigger: 'change' }
        ],
        taskCategory: [
          { required: true, message: '请选择任务分类', trigger: 'change' }
        ],
        submitCycle: [
          { required: true, message: '请选择报送周期', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { validator: validateEndDate, trigger: 'change' }
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
    'form.startDate'() {
      // 当开始日期改变时，重新验证结束日期
      if (this.form.endDate) {
        this.$refs.form.validateField('endDate')
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.isEdit && this.formData) {
        this.form = { ...this.formData }
      } else {
        this.form = {
          taskId: '',
          taskName: '',
          taskType: '',
          taskCategory: '',
          submitCycle: '',
          startDate: '',
          endDate: '',
          dataTemplate: '',
          taskDescription: '',
          remark: ''
        }
      }
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },

    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) {
          return
        }

        this.submitLoading = true
        try {
          if (this.isEdit) {
            await updateTask(this.form)
            this.$message.success('更新任务成功')
          } else {
            await addTask(this.form)
            this.$message.success('新增任务成功')
          }
          
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error(this.isEdit ? '更新任务失败' : '新增任务失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    },

    // 对话框关闭后的回调
    handleClosed() {
      // 清除定时器
      if (this.validateTimer) {
        clearTimeout(this.validateTimer)
        this.validateTimer = null
      }
      
      // 重置表单
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
