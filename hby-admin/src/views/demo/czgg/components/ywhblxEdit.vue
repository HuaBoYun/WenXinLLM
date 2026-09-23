<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item label="类型编码" prop="typeCode">
        <el-input
          v-model="form.typeCode"
          placeholder="请输入类型编码"
          :disabled="isEdit"
        />
      </el-form-item>
      
      <el-form-item label="类型名称" prop="typeName">
        <el-input
          v-model="form.typeName"
          placeholder="请输入类型名称"
        />
      </el-form-item>
      
      <el-form-item label="是否启用" prop="isEnabled">
        <el-radio-group v-model="form.isEnabled">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="类型描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入类型描述"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdatePartnerType } from '@/api/globalTreasurer/czgg'

export default {
  name: 'PartnerTypeEdit',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '新增业务伙伴类型',
      isEdit: false,
      submitLoading: false,
      form: {
        partnerTypeId: null,
        typeCode: '',
        typeName: '',
        isEnabled: 1,
        description: '',
        orgId: null
      },
      rules: {
        typeCode: [
          { required: true, message: '请输入类型编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
          { pattern: /^[A-Z][A-Z0-9_]*$/, message: '类型编码必须以大写字母开头，只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: '请输入类型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        isEnabled: [
          { required: true, message: '请选择是否启用', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 显示对话框
    showEdit(row = null) {
      this.dialogVisible = true
      this.isEdit = !!row
      this.dialogTitle = this.isEdit ? '编辑业务伙伴类型' : '新增业务伙伴类型'
      
      if (row) {
        this.form = {
          partnerTypeId: row.partnerTypeId,
          typeCode: row.typeCode,
          typeName: row.typeName,
          isEnabled: row.isEnabled,
          description: row.description,
          orgId: row.orgId
        }
      } else {
        this.resetForm()
      }
      
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    
    // 重置表单
    resetForm() {
      this.form = {
        partnerTypeId: null,
        typeCode: '',
        typeName: '',
        isEnabled: 1,
        description: '',
        orgId: this.getCurrentOrgId()
      }
    },
    
    // 获取当前组织ID
    getCurrentOrgId() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      return userInfo.linkOrg?.orgid || 1
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) {
          return false
        }
        
        this.submitLoading = true
        try {
          // 设置组织ID
          if (!this.form.orgId) {
            this.form.orgId = this.getCurrentOrgId()
          }
          
          // 设置创建/更新用户
          const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
          const userId = userInfo.staffid || 1
          
          if (this.isEdit) {
            this.form.updateUser = userId
          } else {
            this.form.createUser = userId
            this.form.updateUser = userId
          }
          
          const response = await saveOrUpdatePartnerType(this.form)
          if (response.code === 1) {
            this.$message.success(this.isEdit ? '更新成功' : '新增成功')
            this.handleClose()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.submitLoading = false
        }
      })
    },
    
    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
      this.$refs.form.clearValidate()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
