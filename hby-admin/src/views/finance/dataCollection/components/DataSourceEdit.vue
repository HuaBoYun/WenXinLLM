<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    append-to-body
    :modal-append-to-body="false"
    custom-class="data-source-edit-dialog"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源名称" prop="fintext">
            <el-input v-model="formData.fintext" placeholder="请输入数据源名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库类型" prop="financedbtype">
            <el-select v-model="formData.financedbtype" placeholder="请选择数据库类型">
              <el-option label="Oracle" value="Oracle" />
              <el-option label="Mysql" value="Mysql" />
              <el-option label="SqlServer" value="SqlServer" />
              <el-option label="达梦" value="DM" />
              <el-option label="inceptor" value="inceptor" />
              <el-option label="OceanBase" value="OceanBase" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库连接" prop="financeconn">
            <el-input v-model="formData.financeconn" placeholder="请输入数据库连接" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库端口" prop="financeport">
            <el-input v-model="formData.financeport" placeholder="请输入数据库端口" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库实例" prop="financedbexpm">
            <el-input v-model="formData.financedbexpm" placeholder="请输入数据库实例" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库用户" prop="financeuser">
            <el-input v-model="formData.financeuser" placeholder="请输入数据库用户" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库密码" prop="financepwd">
            <el-input
              v-model="formData.financepwd"
              type="password"
              placeholder="请输入数据库密码"
              autocomplete="new-password"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleTestConnection">测试连接</el-button>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveDataSource, getDataSourceDetail, testDataSource } from '@/api/cwsc'

export default {
  name: 'DataSourceEdit',
  data() {
    return {
      dialogVisible: false,
      title: '新建数据源',
      formData: {
        fid: '',
        fintext: '',
        financedbtype: '',
        financeconn: '',
        financeport: '',
        financedbexpm: '',
        financeuser: '',
        financepwd: ''
      },
      rules: {
        fintext: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' }
        ],
        financedbtype: [
          { required: true, message: '请选择数据库类型', trigger: 'change' }
        ],
        financeconn: [
          { required: true, message: '请输入数据库连接', trigger: 'blur' }
        ],
        financeuser: [
          { required: true, message: '请输入数据库用户', trigger: 'blur' }
        ],
        financepwd: [
          { required: true, message: '请输入数据库密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async showEdit(row, type) {
      this.dialogVisible = true
      this.title = type === 'add' ? '新建数据源' : '编辑数据源'
      
      if (row && type === 'edit') {
        try {
          const res = await getDataSourceDetail({ fid: row.fid })
          if (res.code === 1 && res.data) {
            Object.keys(this.formData).forEach(key => {
              this.formData[key] = res.data[key] || ''
            })
          }
        } catch (error) {
          this.$message.error('加载数据源详情失败')
          console.error(error)
        }
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.formData = {
        fid: '',
        fintext: '',
        financedbtype: '',
        financeconn: '',
        financeport: '',
        financedbexpm: '',
        financeuser: '',
        financepwd: ''
      }
      this.$refs.formRef && this.$refs.formRef.clearValidate()
    },
    async handleTestConnection() {
      this.$refs.formRef.validate(async valid => {
        if (valid) {
          try {
            const params = {
              financeconn: this.formData.financeconn,
              financeport: this.formData.financeport,
              financedbexpm: this.formData.financedbexpm,
              financeuser: this.formData.financeuser,
              financepwd: this.formData.financepwd,
              financedbtype: this.formData.financedbtype
            }
            const res = await testDataSource(params)
            if (res.code === 1) {
              this.$message.success(res.msg || '连接测试成功')
            } else {
              this.$message.error(res.msg || '连接测试失败')
            }
          } catch (error) {
            this.$message.error('连接测试失败')
            console.error(error)
          }
        }
      })
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async valid => {
        if (valid) {
          try {
            const res = await saveDataSource(this.formData)
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('保存失败')
            console.error(error)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
/* 数据源编辑对话框样式 - 仅作用于当前组件 */
.data-source-edit-dialog .el-dialog__wrapper {
  z-index: 3000 !important;
}
</style>
