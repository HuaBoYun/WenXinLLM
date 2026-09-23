<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="60%"
    :before-close="handleClose"
    class="check-create-dialog"
  >
    <el-form
      :model="formData"
      :rules="formRules"
      ref="createForm"
      label-width="120px"
      class="create-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="盘点名称" prop="checkName">
            <el-input
              v-model="formData.checkName"
              placeholder="请输入盘点名称"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="盘点类型" prop="checkType">
            <el-select v-model="formData.checkType" placeholder="请选择盘点类型" style="width: 100%">
              <el-option label="全盘" value="FULL" />
              <el-option label="抽盘" value="PARTIAL" />
              <el-option label="循环盘点" value="CYCLE" />
              <el-option label="动态盘点" value="DYNAMIC" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="盘点日期" prop="checkDate">
            <el-date-picker
              v-model="formData.checkDate"
              type="date"
              placeholder="选择盘点日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="盘点仓库" prop="warehouseId">
            <el-select v-model="formData.warehouseId" placeholder="请选择仓库" style="width: 100%">
              <el-option label="主仓库" :value="2001" />
              <el-option label="分仓库" :value="2002" />
              <el-option label="原料仓库" :value="2003" />
              <el-option label="成品仓库" :value="2004" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="formData.checkType === 'PARTIAL'">
        <el-col :span="12">
          <el-form-item label="抽盘比例" prop="sampleRate">
            <el-input-number
              v-model="formData.sampleRate"
              :min="1"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
            <span style="margin-left: 8px; color: #909399;">%</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="抽盘方式" prop="sampleMethod">
            <el-select v-model="formData.sampleMethod" placeholder="请选择抽盘方式" style="width: 100%">
              <el-option label="随机抽样" value="RANDOM" />
              <el-option label="ABC分类抽样" value="ABC" />
              <el-option label="金额排序抽样" value="AMOUNT" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="formData.checkType === 'CYCLE'">
        <el-col :span="12">
          <el-form-item label="循环周期" prop="cycleDays">
            <el-input-number
              v-model="formData.cycleDays"
              :min="1"
              :max="365"
              style="width: 100%"
            />
            <span style="margin-left: 8px; color: #909399;">天</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="循环分组" prop="cycleGroup">
            <el-select v-model="formData.cycleGroup" placeholder="请选择循环分组" style="width: 100%">
              <el-option label="A组（高价值）" value="A" />
              <el-option label="B组（中价值）" value="B" />
              <el-option label="C组（低价值）" value="C" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="存货范围" prop="inventoryScope">
        <el-radio-group v-model="formData.inventoryScope">
          <el-radio label="ALL">全部存货</el-radio>
          <el-radio label="CATEGORY">按分类</el-radio>
          <el-radio label="SPECIFIC">指定存货</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item v-if="formData.inventoryScope === 'CATEGORY'" label="存货分类" prop="categoryIds">
        <el-select
          v-model="formData.categoryIds"
          multiple
          placeholder="请选择存货分类"
          style="width: 100%"
        >
          <el-option label="原材料" value="1001" />
          <el-option label="半成品" value="1002" />
          <el-option label="产成品" value="1003" />
          <el-option label="商品" value="1004" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.inventoryScope === 'SPECIFIC'" label="指定存货" prop="inventoryIds">
        <el-select
          v-model="formData.inventoryIds"
          multiple
          filterable
          placeholder="请选择存货"
          style="width: 100%"
        >
          <el-option
            v-for="item in inventoryOptions"
            :key="item.inventoryId"
            :label="item.inventoryName"
            :value="item.inventoryId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="盘点人员" prop="checkerIds">
        <el-select
          v-model="formData.checkerIds"
          multiple
          placeholder="请选择盘点人员"
          style="width: 100%"
        >
          <el-option label="盘点员1" value="U001" />
          <el-option label="盘点员2" value="U002" />
          <el-option label="盘点员3" value="U003" />
          <el-option label="盘点员4" value="U004" />
        </el-select>
      </el-form-item>

      <el-form-item label="备注说明" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入备注说明"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        {{ isEdit ? '保存' : '创建' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CheckCreateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    checkData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      formData: {
        checkName: '',
        checkType: '',
        checkDate: '',
        warehouseId: '',
        sampleRate: 10,
        sampleMethod: '',
        cycleDays: 30,
        cycleGroup: '',
        inventoryScope: 'ALL',
        categoryIds: [],
        inventoryIds: [],
        checkerIds: [],
        description: ''
      },
      formRules: {
        checkName: [
          { required: true, message: '请输入盘点名称', trigger: 'blur' }
        ],
        checkType: [
          { required: true, message: '请选择盘点类型', trigger: 'change' }
        ],
        checkDate: [
          { required: true, message: '请选择盘点日期', trigger: 'change' }
        ],
        warehouseId: [
          { required: true, message: '请选择盘点仓库', trigger: 'change' }
        ],
        checkerIds: [
          { required: true, message: '请选择盘点人员', trigger: 'change' }
        ]
      },
      inventoryOptions: [
        { inventoryId: 3001, inventoryName: '存货1' },
        { inventoryId: 3002, inventoryName: '存货2' },
        { inventoryId: 3003, inventoryName: '存货3' },
        { inventoryId: 3004, inventoryName: '存货4' },
        { inventoryId: 3005, inventoryName: '存货5' }
      ]
    }
  },
  computed: {
    isEdit() {
      return !!(this.checkData && this.checkData.checkId)
    },
    dialogTitle() {
      return this.isEdit ? '编辑盘点任务' : '创建盘点任务'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initFormData()
      } else {
        this.resetForm()
      }
    }
  },
  methods: {
    initFormData() {
      if (this.isEdit) {
        // 编辑模式，填充数据
        this.formData = { ...this.checkData }
      } else {
        // 新增模式，设置默认值
        this.formData.checkDate = new Date().toISOString().split('T')[0]
      }
    },

    resetForm() {
      this.$refs.createForm && this.$refs.createForm.resetFields()
      this.formData = {
        checkName: '',
        checkType: '',
        checkDate: '',
        warehouseId: '',
        sampleRate: 10,
        sampleMethod: '',
        cycleDays: 30,
        cycleGroup: '',
        inventoryScope: 'ALL',
        categoryIds: [],
        inventoryIds: [],
        checkerIds: [],
        description: ''
      }
    },

    handleSubmit() {
      this.$refs.createForm.validate(async (valid) => {
        if (!valid) return

        this.submitting = true
        try {
          // 调用API创建盘点任务
          this.$emit('create', this.formData)
          this.handleClose()
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.submitting = false
        }
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.check-create-dialog {
  .create-form {
    .el-form-item {
      margin-bottom: 20px;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
