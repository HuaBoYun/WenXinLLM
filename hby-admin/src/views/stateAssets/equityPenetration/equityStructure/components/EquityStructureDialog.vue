<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :disabled="dialogType === 'view'"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业ID" prop="enterpriseId">
            <el-input v-model="form.enterpriseId" placeholder="请输入企业ID" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投资方名称" prop="investorName">
            <el-input v-model="form.investorName" placeholder="请输入投资方名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="投资方类型" prop="investorType">
            <el-select v-model="form.investorType" placeholder="请选择" style="width:100%">
              <el-option label="企业" value="ENTERPRISE" />
              <el-option label="个人" value="INDIVIDUAL" />
              <el-option label="政府" value="GOVERNMENT" />
              <el-option label="基金" value="FUND" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="持股比例%" prop="shareholdingRatio">
            <el-input-number v-model="form.shareholdingRatio" :min="0" :max="100" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="持股金额(万)" prop="shareholdingAmount">
            <el-input-number v-model="form.shareholdingAmount" :min="0" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="股份类型" prop="shareType">
            <el-select v-model="form.shareType" placeholder="请选择" style="width:100%">
              <el-option label="普通股" value="COMMON" />
              <el-option label="优先股" value="PREFERRED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="表决权比例%" prop="votingRatio">
            <el-input-number v-model="form.votingRatio" :min="0" :max="100" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制类型" prop="controlType">
            <el-select v-model="form.controlType" placeholder="请选择" style="width:100%">
              <el-option label="直接" value="DIRECT" />
              <el-option label="间接" value="INDIRECT" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择" style="width:100%">
              <el-option label="正常" value="NORMAL" />
              <el-option label="质押" value="PLEDGED" />
              <el-option label="冻结" value="FROZEN" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addEquityStructure, updateEquityStructure } from '@/api/stateAssets/equityStructure'

export default {
  name: 'EquityStructureDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' }
  },
  data() {
    return {
      form: {},
      submitLoading: false,
      rules: {
        enterpriseId: [{ required: true, message: '请输入企业ID', trigger: 'blur' }],
        investorName: [{ required: true, message: '请输入投资方名称', trigger: 'blur' }],
        shareholdingRatio: [{ required: true, message: '请输入持股比例', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      const map = { add: '新增股权结构', edit: '编辑股权结构', view: '查看股权结构' }
      return map[this.dialogType] || '股权结构'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.form = { ...this.formData }
      } else {
        this.$refs.formRef && this.$refs.formRef.resetFields()
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addEquityStructure : updateEquityStructure
          const response = await apiFn(this.form)
          if (response.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('请求失败')
        } finally {
          this.submitLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>