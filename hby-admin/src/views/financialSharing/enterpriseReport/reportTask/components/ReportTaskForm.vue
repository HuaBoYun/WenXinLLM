<template>
  <el-dialog
    :title="formData.taskId ? '修改报表任务' : '新增报表任务'"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务编码" prop="taskCode">
            <el-input v-model="formData.taskCode" placeholder="请输入任务编码" maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskName">
            <el-input v-model="formData.taskName" placeholder="请输入任务名称" maxlength="200" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属表单组" prop="groupId">
            <el-select
              v-model="formData.groupId"
              placeholder="请选择所属表单组"
              style="width: 100%"
            >
              <el-option
                v-for="item in groupOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织体系">
            <el-input v-model="formData.orgSystem" placeholder="请输入组织体系" maxlength="50" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="周期类型" prop="periodType">
            <el-select v-model="formData.periodType" placeholder="请选择周期类型" style="width: 100%">
              <el-option label="年度" value="YEAR" />
              <el-option label="半年度" value="HALF_YEAR" />
              <el-option label="季度" value="QUARTER" />
              <el-option label="月度" value="MONTH" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报期间偏移">
            <el-input-number
              v-model="formData.periodOffset"
              :min="-12"
              :max="12"
              controls-position="right"
              style="width: 100%"
              placeholder="相对当前期间的偏移量"
            />
            <span class="form-tip">如-1表示上期,0表示当期,1表示下期</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="起始期间">
            <el-input v-model="formData.startPeriod" placeholder="如:202401" maxlength="20" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="终止期间">
            <el-input v-model="formData.endPeriod" placeholder="如:202412" maxlength="20" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否逐级上报">
            <el-radio-group v-model="formData.isHierarchical">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
            <span class="form-tip">逐级上报需要下级单位先完成填报</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止天数">
            <el-input-number
              v-model="formData.deadlineDays"
              :min="1"
              :max="365"
              controls-position="right"
              style="width: 100%"
              placeholder="填报截止天数"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="启用节点检查">
            <el-radio-group v-model="formData.isNodeCheck">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
            <span class="form-tip">检查是否所有节点都已完成</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用归档控制">
            <el-radio-group v-model="formData.isArchiveControl">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
            <span class="form-tip">归档后不允许修改数据</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio label="DRAFT">草稿</el-radio>
              <el-radio label="PUBLISHED">已发布</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveReportTask } from '@/api/financialSharing/enterpriseReport/reportTask'

export default {
  name: 'ReportTaskForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    groupOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      rules: {
        taskCode: [
          { required: true, message: '任务编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '任务编码长度在1到50个字符', trigger: 'blur' }
        ],
        taskName: [
          { required: true, message: '任务名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '任务名称长度在1到200个字符', trigger: 'blur' }
        ],
        groupId: [
          { required: true, message: '所属表单组不能为空', trigger: 'change' }
        ],
        periodType: [
          { required: true, message: '周期类型不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          saveReportTask(this.formData).then(response => {
            this.submitLoading = false
            if (response.code === 200) {
              this.$message.success(response.msg || '保存成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          }).catch(() => {
            this.submitLoading = false
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.$refs.form.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.form-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
  display: block;
  margin-top: 5px;
}
</style>


