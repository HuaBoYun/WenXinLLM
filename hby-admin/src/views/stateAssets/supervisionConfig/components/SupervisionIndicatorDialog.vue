<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="indicatorForm"
      :model="indicatorForm"
      :rules="indicatorRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指标名称" prop="indicatorName">
            <el-input
              v-model="indicatorForm.indicatorName"
              placeholder="请输入指标名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="指标编码" prop="indicatorCode">
            <el-input
              v-model="indicatorForm.indicatorCode"
              placeholder="请输入指标编码"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指标类型" prop="indicatorType">
            <el-select
              v-model="indicatorForm.indicatorType"
              placeholder="请选择指标类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="财务指标" value="FINANCIAL"></el-option>
              <el-option label="风险指标" value="RISK"></el-option>
              <el-option label="合规指标" value="COMPLIANCE"></el-option>
              <el-option label="运营指标" value="OPERATIONAL"></el-option>
              <el-option label="治理指标" value="GOVERNANCE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类型" prop="dataType">
            <el-select
              v-model="indicatorForm.dataType"
              placeholder="请选择数据类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="数值型" value="NUMERIC"></el-option>
              <el-option label="百分比" value="PERCENTAGE"></el-option>
              <el-option label="文本型" value="TEXT"></el-option>
              <el-option label="布尔型" value="BOOLEAN"></el-option>
              <el-option label="日期型" value="DATE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计算方式" prop="calculationMethod">
            <el-select
              v-model="indicatorForm.calculationMethod"
              placeholder="请选择计算方式"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="直接取值" value="DIRECT"></el-option>
              <el-option label="公式计算" value="FORMULA"></el-option>
              <el-option label="聚合计算" value="AGGREGATE"></el-option>
              <el-option label="比率计算" value="RATIO"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="更新频率" prop="updateFrequency">
            <el-select
              v-model="indicatorForm.updateFrequency"
              placeholder="请选择更新频率"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="实时" value="REALTIME"></el-option>
              <el-option label="每日" value="DAILY"></el-option>
              <el-option label="每周" value="WEEKLY"></el-option>
              <el-option label="每月" value="MONTHLY"></el-option>
              <el-option label="每季度" value="QUARTERLY"></el-option>
              <el-option label="每年" value="YEARLY"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="单位" prop="unit">
            <el-input
              v-model="indicatorForm.unit"
              placeholder="请输入单位（如：万元、%）"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="小数位数" prop="decimalPlaces">
            <el-input-number
              v-model="indicatorForm.decimalPlaces"
              :min="0"
              :max="6"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预警阈值" prop="warningThreshold">
            <el-input-number
              v-model="indicatorForm.warningThreshold"
              :precision="2"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="危险阈值" prop="dangerThreshold">
            <el-input-number
              v-model="indicatorForm.dangerThreshold"
              :precision="2"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指标状态" prop="status">
            <el-select
              v-model="indicatorForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="启用" value="ACTIVE"></el-option>
              <el-option label="停用" value="INACTIVE"></el-option>
              <el-option label="测试" value="TESTING"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="indicatorForm.creator"
              placeholder="请输入创建人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="数据源" prop="dataSource">
        <el-input
          v-model="indicatorForm.dataSource"
          placeholder="请输入数据源（表名或API接口）"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="计算公式" prop="calculationFormula">
        <el-input
          v-model="indicatorForm.calculationFormula"
          type="textarea"
          :rows="3"
          placeholder="请输入计算公式（支持SQL或表达式）"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="指标描述" prop="description">
        <el-input
          v-model="indicatorForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入指标描述"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="业务含义" prop="businessMeaning">
        <el-input
          v-model="indicatorForm.businessMeaning"
          type="textarea"
          :rows="2"
          placeholder="请输入业务含义"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="取值范围" prop="valueRange">
        <el-input
          v-model="indicatorForm.valueRange"
          placeholder="请输入取值范围（如：0-100）"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="适用对象" prop="applicableObjects">
        <el-checkbox-group v-model="indicatorForm.applicableObjects" :disabled="dialogType === 'view'">
          <el-checkbox label="CENTRAL_ENTERPRISE">央企</el-checkbox>
          <el-checkbox label="STATE_ENTERPRISE">国企</el-checkbox>
          <el-checkbox label="LISTED_COMPANY">上市公司</el-checkbox>
          <el-checkbox label="FINANCIAL_INSTITUTION">金融机构</el-checkbox>
          <el-checkbox label="SUBSIDIARY">子公司</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="indicatorForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        @click="handleTestFormula"
        :loading="loading"
      >
        测试公式
      </el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveSupervisionIndicator, testIndicatorFormula } from '@/api/stateAssets/supervisionConfig'

export default {
  name: 'SupervisionIndicatorDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    indicatorData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      indicatorForm: {
        id: '',
        indicatorName: '',
        indicatorCode: '',
        indicatorType: '',
        dataType: '',
        calculationMethod: '',
        updateFrequency: '',
        unit: '',
        decimalPlaces: 2,
        warningThreshold: null,
        dangerThreshold: null,
        status: 'ACTIVE',
        creator: '',
        dataSource: '',
        calculationFormula: '',
        description: '',
        businessMeaning: '',
        valueRange: '',
        applicableObjects: [],
        remarks: ''
      },
      indicatorRules: {
        indicatorName: [
          { required: true, message: '请输入指标名称', trigger: 'blur' }
        ],
        indicatorCode: [
          { required: true, message: '请输入指标编码', trigger: 'blur' }
        ],
        indicatorType: [
          { required: true, message: '请选择指标类型', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ],
        calculationMethod: [
          { required: true, message: '请选择计算方式', trigger: 'change' }
        ],
        updateFrequency: [
          { required: true, message: '请选择更新频率', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择指标状态', trigger: 'change' }
        ],
        creator: [
          { required: true, message: '请输入创建人', trigger: 'blur' }
        ],
        dataSource: [
          { required: true, message: '请输入数据源', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入指标描述', trigger: 'blur' }
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
        add: '新建监管指标',
        edit: '编辑监管指标',
        view: '查看监管指标'
      }
      return titleMap[this.dialogType] || '新建监管指标'
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
    initForm() {
      if (this.dialogType === 'add') {
        this.indicatorForm = {
          id: '',
          indicatorName: '',
          indicatorCode: '',
          indicatorType: '',
          dataType: '',
          calculationMethod: '',
          updateFrequency: '',
          unit: '',
          decimalPlaces: 2,
          warningThreshold: null,
          dangerThreshold: null,
          status: 'ACTIVE',
          creator: '',
          dataSource: '',
          calculationFormula: '',
          description: '',
          businessMeaning: '',
          valueRange: '',
          applicableObjects: [],
          remarks: ''
        }
      } else {
        this.indicatorForm = { ...this.indicatorData }
        // 确保数组字段正确初始化
        this.indicatorForm.applicableObjects = this.indicatorData.applicableObjects || []
      }
      
      this.$nextTick(() => {
        if (this.$refs.indicatorForm) {
          this.$refs.indicatorForm.clearValidate()
        }
      })
    },

    handleTestFormula() {
      if (!this.indicatorForm.calculationFormula) {
        this.$message.warning('请先输入计算公式')
        return
      }
      
      this.loading = true
      testIndicatorFormula({
        formula: this.indicatorForm.calculationFormula,
        dataSource: this.indicatorForm.dataSource
      }).then(response => {
        if (response.code === 1) {
          this.$message.success('公式测试通过')
        } else {
          this.$message.error(response.msg || '公式测试失败')
        }
        this.loading = false
      }).catch(error => {
        console.error('测试公式失败:', error)
        this.$message.error('公式测试失败')
        this.loading = false
      })
    },

    handleSubmit() {
      this.$refs.indicatorForm.validate((valid) => {
        if (valid) {
          this.loading = true
          saveSupervisionIndicator(this.indicatorForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存监管指标失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
