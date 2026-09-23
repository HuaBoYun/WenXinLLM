<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="850px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="130px"
      v-loading="loading"
    >
      <!-- Row 1: 被投资企业 + 变动类型 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="被投资企业" prop="investeeEnterpriseName">
            <el-input
              v-model="form.investeeEnterpriseName"
              placeholder="请输入被投资企业名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变动类型" prop="changeType">
            <el-select
              v-model="form.changeType"
              placeholder="请选择变动类型"
              style="width: 100%"
              :disabled="isView"
            >
              <el-option label="股权转让" value="TRANSFER" />
              <el-option label="增资扩股" value="INCREASE" />
              <el-option label="减资" value="DECREASE" />
              <el-option label="股权质押" value="PLEDGE" />
              <el-option label="股权解押" value="UNPLEDGE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 2: 转让方 + 受让方 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="转让方" prop="transferorEnterpriseName">
            <el-input
              v-model="form.transferorEnterpriseName"
              placeholder="请输入转让方"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="受让方" prop="transfereeEnterpriseName">
            <el-input
              v-model="form.transfereeEnterpriseName"
              placeholder="请输入受让方"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 3: 变动前比例 + 变动后比例 + 变动金额 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="变动前比例%" prop="beforeShareholdingRatio">
            <el-input-number
              v-model="form.beforeShareholdingRatio"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="变动后比例%" prop="afterShareholdingRatio">
            <el-input-number
              v-model="form.afterShareholdingRatio"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="变动金额" prop="changeAmount">
            <el-input-number
              v-model="form.changeAmount"
              :min="0"
              :precision="2"
              placeholder="万元"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 4: 转让方式 + 变动日期 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="转让方式" prop="transferMethod">
            <el-select
              v-model="form.transferMethod"
              placeholder="请选择转让方式"
              style="width: 100%"
              :disabled="isView"
            >
              <el-option label="协议转让" value="AGREEMENT" />
              <el-option label="拍卖转让" value="AUCTION" />
              <el-option label="挂牌转让" value="EXCHANGE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变动日期" prop="changeDate">
            <el-date-picker
              v-model="form.changeDate"
              type="date"
              placeholder="选择变动日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 5: 风险等级 + 变动原因 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级" prop="warningLevel">
            <el-select
              v-model="form.warningLevel"
              placeholder="请选择风险等级"
              style="width: 100%"
              :disabled="isView"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变动原因" prop="changeReason">
            <el-input
              v-model="form.changeReason"
              placeholder="请输入变动原因"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 6: 备注 -->
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
          :disabled="isView"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button
        v-if="!isView"
        type="primary"
        :loading="submitLoading"
        @click="handleSubmit"
      >
        确 定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  addEquityChange,
  updateEquityChange,
  getEquityChangeById
} from '@/api/stateAssets/equityChanges'

export default {
  name: 'EquityChangeDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    changeData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      form: this.getDefaultForm(),
      rules: {
        investeeEnterpriseName: [
          { required: true, message: '请输入被投资企业名称', trigger: 'blur' }
        ],
        changeType: [
          { required: true, message: '请选择变动类型', trigger: 'change' }
        ],
        transferorEnterpriseName: [
          { required: true, message: '请输入转让方', trigger: 'blur' }
        ],
        transfereeEnterpriseName: [
          { required: true, message: '请输入受让方', trigger: 'blur' }
        ],
        beforeShareholdingRatio: [
          { required: true, message: '请输入变动前比例', trigger: 'blur' }
        ],
        afterShareholdingRatio: [
          { required: true, message: '请输入变动后比例', trigger: 'blur' }
        ],
        changeAmount: [
          { required: true, message: '请输入变动金额', trigger: 'blur' }
        ],
        transferMethod: [
          { required: true, message: '请选择转让方式', trigger: 'change' }
        ],
        changeDate: [
          { required: true, message: '请选择变动日期', trigger: 'change' }
        ],
        warningLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
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
        add: '新增股权变动',
        edit: '编辑股权变动',
        view: '查看股权变动'
      }
      return titleMap[this.dialogType] || '股权变动'
    },
    isView() {
      return this.dialogType === 'view'
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
    getDefaultForm() {
      return {
        changeId: null,
        investeeEnterpriseName: '',
        investorEnterpriseName: '',
        changeType: '',
        changeReason: '',
        beforeShareholdingRatio: 0,
        afterShareholdingRatio: 0,
        changeAmount: 0,
        transferorEnterpriseName: '',
        transfereeEnterpriseName: '',
        transferMethod: '',
        changeDate: '',
        warningLevel: 'LOW',
        remark: ''
      }
    },

    initForm() {
      if (this.dialogType === 'add') {
        this.resetForm()
      } else if (this.changeData && this.changeData.changeId) {
        this.loadChangeData()
      }
    },

    resetForm() {
      this.form = this.getDefaultForm()
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },

    async loadChangeData() {
      this.loading = true
      try {
        const response = await getEquityChangeById(this.changeData.changeId)
        if (response.result === 200) {
          this.form = { ...response.data }
        } else {
          this.$message.error(response.msg || '加载数据失败')
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          const submitData = { ...this.form }
          let response
          if (this.dialogType === 'add') {
            submitData.changeId = null
            response = await addEquityChange(submitData)
          } else {
            response = await updateEquityChange(submitData)
          }

          if (response.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.handleClose()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error(this.dialogType === 'add' ? '新增失败' : '更新失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
