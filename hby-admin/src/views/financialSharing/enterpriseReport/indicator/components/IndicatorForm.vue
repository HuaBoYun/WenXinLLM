<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      :model="form"
      :rules="formRules"
      ref="formRef"
      label-width="120px"
      :disabled="formType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指标编码" prop="indicatorCode">
            <el-input
              v-model="form.indicatorCode"
              placeholder="请输入指标编码"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="指标名称" prop="indicatorName">
            <el-input
              v-model="form.indicatorName"
              placeholder="请输入指标名称"
              maxlength="200"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指标类型" prop="indicatorType">
            <el-select
              v-model="form.indicatorType"
              placeholder="请选择指标类型"
              style="width: 100%"
              @change="handleTypeChange"
            >
              <el-option label="货币" value="CURRENCY" />
              <el-option label="数量" value="QUANTITY" />
              <el-option label="价格" value="PRICE" />
              <el-option label="百分比" value="PERCENT" />
              <el-option label="文本" value="TEXT" />
              <el-option label="枚举" value="ENUM" />
              <el-option label="参照" value="REFERENCE" />
              <el-option label="日期" value="DATE" />
              <el-option label="长文本" value="LONGTEXT" />
              <el-option label="附件" value="ATTACHMENT" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="汇总属性" prop="aggregateType">
            <el-select
              v-model="form.aggregateType"
              placeholder="请选择汇总属性"
              style="width: 100%"
            >
              <el-option label="求和" value="SUM" />
              <el-option label="平均" value="AVG" />
              <el-option label="最大" value="MAX" />
              <el-option label="最小" value="MIN" />
              <el-option label="不汇总" value="NONE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据长度" prop="dataLength">
            <el-input-number
              v-model="form.dataLength"
              :min="1"
              :max="50"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据精度" prop="dataPrecision">
            <el-input-number
              v-model="form.dataPrecision"
              :min="0"
              :max="10"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="form.indicatorType === 'ENUM'">
        <el-col :span="24">
          <el-form-item label="枚举值" prop="enumValues">
            <el-input
              v-model="form.enumValues"
              type="textarea"
              :rows="3"
              placeholder="请输入枚举值(JSON数组格式,如:[&quot;选项1&quot;,&quot;选项2&quot;])"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="form.indicatorType === 'REFERENCE'">
        <el-col :span="12">
          <el-form-item label="参照维度" prop="referenceDimension">
            <el-select
              v-model="form.referenceDimension"
              placeholder="请选择参照维度"
              style="width: 100%"
              filterable
            >
              <el-option
                v-for="dimension in dimensionList"
                :key="dimension.dimensionId"
                :label="dimension.dimensionName"
                :value="dimension.dimensionId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表单链接" prop="formLink">
            <el-input
              v-model="form.formLink"
              placeholder="请输入表单链接"
              maxlength="200"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="填报说明" prop="fillInstruction">
            <el-input
              v-model="form.fillInstruction"
              type="textarea"
              :rows="3"
              placeholder="请输入填报说明"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="formType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="submitLoading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveIndicator } from '@/api/financialSharing/enterpriseReport/indicator'
import { getDimensionList } from '@/api/financialSharing/groupControl/dimension'

export default {
  name: 'IndicatorForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formType: {
      type: String,
      default: 'add'
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      submitLoading: false,
      dimensionList: [],
      form: {
        indicatorId: '',
        indicatorCode: '',
        indicatorName: '',
        indicatorType: '',
        aggregateType: 'SUM',
        dataLength: 18,
        dataPrecision: 2,
        enumValues: '',
        referenceDimension: '',
        formLink: '',
        fillInstruction: '',
        status: 'ACTIVE'
      },
      formRules: {
        indicatorCode: [
          { required: true, message: '请输入指标编码', trigger: 'blur' },
          { max: 50, message: '指标编码长度不能超过50个字符', trigger: 'blur' }
        ],
        indicatorName: [
          { required: true, message: '请输入指标名称', trigger: 'blur' },
          { max: 200, message: '指标名称长度不能超过200个字符', trigger: 'blur' }
        ],
        indicatorType: [
          { required: true, message: '请选择指标类型', trigger: 'change' }
        ],
        aggregateType: [
          { required: true, message: '请选择汇总属性', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增指标',
        edit: '编辑指标',
        view: '查看指标'
      }
      return titleMap[this.formType] || '指标信息'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
        this.fetchDimensionList()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })

      if (this.formType === 'add') {
        this.form = {
          indicatorId: '',
          indicatorCode: '',
          indicatorName: '',
          indicatorType: '',
          aggregateType: 'SUM',
          dataLength: 18,
          dataPrecision: 2,
          enumValues: '',
          referenceDimension: '',
          formLink: '',
          fillInstruction: '',
          status: 'ACTIVE'
        }
      } else if (this.formType === 'edit' || this.formType === 'view') {
        this.form = { ...this.formData }
      }
    },

    // 查询维度列表
    async fetchDimensionList() {
      try {
        const res = await getDimensionList({ pageNumber: 1, pageSize: 100 })
        if (res.code === 1) {
          this.dimensionList = res.data.records || []
        }
      } catch (error) {
        console.error('查询维度列表失败:', error)
      }
    },

    // 指标类型改变
    handleTypeChange(type) {
      // 根据类型设置默认值
      if (type === 'CURRENCY' || type === 'PRICE') {
        this.form.dataLength = 18
        this.form.dataPrecision = 2
        this.form.aggregateType = 'SUM'
      } else if (type === 'QUANTITY') {
        this.form.dataLength = 18
        this.form.dataPrecision = 4
        this.form.aggregateType = 'SUM'
      } else if (type === 'PERCENT') {
        this.form.dataLength = 10
        this.form.dataPrecision = 4
        this.form.aggregateType = 'AVG'
      } else if (type === 'TEXT' || type === 'ENUM' || type === 'REFERENCE' || type === 'DATE') {
        this.form.dataLength = 200
        this.form.dataPrecision = 0
        this.form.aggregateType = 'NONE'
      } else if (type === 'LONGTEXT' || type === 'ATTACHMENT') {
        this.form.dataLength = 2000
        this.form.dataPrecision = 0
        this.form.aggregateType = 'NONE'
      }
    },

    // 提交表单
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) {
          return false
        }

        this.submitLoading = true
        try {
          const res = await saveIndicator(this.form)
          if (res.code === 1) {
            this.$message.success(this.formType === 'add' ? '新增成功' : '修改成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(res.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存指标失败:', error)
          this.$message.error('保存失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped lang="scss">
.dialog-footer {
  text-align: right;
}
</style>

