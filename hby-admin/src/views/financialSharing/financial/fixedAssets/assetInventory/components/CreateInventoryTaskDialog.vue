<template>
  <el-dialog
    title="创建盘点任务"
    :visible.sync="visible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="taskForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-form-item label="任务编号" prop="taskNumber">
        <el-input v-model="formData.taskNumber" placeholder="自动生成" disabled />
      </el-form-item>
      <el-form-item label="任务名称" prop="taskName">
        <el-input v-model="formData.taskName" placeholder="请输入任务名称" />
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="盘点类型" prop="inventoryType">
            <el-select v-model="formData.inventoryType" placeholder="请选择盘点类型" style="width: 100%">
              <el-option label="全盘" value="FULL" />
              <el-option label="抽盘" value="SAMPLE" />
              <el-option label="循环盘点" value="CYCLE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划日期" prop="plannedDate">
            <el-date-picker
              v-model="formData.plannedDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="盘点范围" prop="scope">
        <el-checkbox-group v-model="formData.scope">
          <el-checkbox label="房屋建筑物">房屋建筑物</el-checkbox>
          <el-checkbox label="机器设备">机器设备</el-checkbox>
          <el-checkbox label="运输工具">运输工具</el-checkbox>
          <el-checkbox label="电子设备">电子设备</el-checkbox>
          <el-checkbox label="办公设备">办公设备</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="负责人" prop="responsiblePerson">
        <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人" />
      </el-form-item>
      <el-form-item label="盘点说明" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入盘点说明"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CreateInventoryTaskDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      submitting: false,
      formData: {
        taskNumber: '',
        taskName: '',
        inventoryType: '',
        plannedDate: '',
        scope: [],
        responsiblePerson: '',
        description: ''
      },
      formRules: {
        taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
        inventoryType: [{ required: true, message: '请选择盘点类型', trigger: 'change' }],
        plannedDate: [{ required: true, message: '请选择计划日期', trigger: 'change' }],
        scope: [{ required: true, message: '请选择盘点范围', trigger: 'change' }],
        responsiblePerson: [{ required: true, message: '请输入负责人', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.taskForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.taskForm.resetFields()
      this.$emit('update:visible', false)
    }
  }
}
</script>

