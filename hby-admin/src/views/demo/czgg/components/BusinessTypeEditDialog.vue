<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="dataForm"
      :rules="rules"
      :model="temp"
      label-position="left"
      label-width="100px"
      style="width: 500px; margin-left:50px;"
    >
      <el-form-item label="类型编码" prop="typeCode">
        <el-input v-model="temp.typeCode" placeholder="请输入类型编码" />
      </el-form-item>
      <el-form-item label="类型名称" prop="typeName">
        <el-input v-model="temp.typeName" placeholder="请输入类型名称" />
      </el-form-item>
      <el-form-item label="上级类型" prop="parentTypeId">
        <el-cascader
          v-model="temp.parentTypeId"
          :options="businessTypeTree"
          :props="cascaderProps"
          placeholder="请选择上级类型"
          clearable
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="排序" prop="sortOrder">
        <el-input-number
          v-model="temp.sortOrder"
          :min="0"
          :max="9999"
          placeholder="请输入排序"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="状态" prop="isEnabled">
        <el-radio-group v-model="temp.isEnabled">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="temp.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleSave">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateBusinessType, getBusinessTypeTree } from '@/api/globalTreasurer/czgg'

export default {
  name: 'BusinessTypeEditDialog',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      temp: {
        typeId: null,
        typeCode: '',
        typeName: '',
        parentTypeId: null,
        parentTypeName: '',
        level: 1,
        sortOrder: 0,
        isEnabled: 1,
        description: '',
        orgId: null
      },
      businessTypeTree: [],
      cascaderProps: {
        value: 'typeId',
        label: 'typeName',
        children: 'children',
        checkStrictly: true,
        emitPath: false
      },
      rules: {
        typeCode: [
          { required: true, message: '请输入类型编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: '请输入类型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        sortOrder: [
          { required: true, message: '请输入排序', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    show(row) {
      this.dialogVisible = true
      this.resetTemp()
      
      if (row) {
        this.dialogTitle = '编辑业务品种类型'
        this.temp = Object.assign({}, row)
      } else {
        this.dialogTitle = '新增业务品种类型'
        this.temp.orgId = this.$store.getters.orgId
      }
      
      this.getBusinessTypeTree()
      
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    
    resetTemp() {
      this.temp = {
        typeId: null,
        typeCode: '',
        typeName: '',
        parentTypeId: null,
        parentTypeName: '',
        level: 1,
        sortOrder: 0,
        isEnabled: 1,
        description: '',
        orgId: null
      }
    },
    
    getBusinessTypeTree() {
      getBusinessTypeTree(this.$store.getters.orgId).then(response => {
        if (response.success) {
          this.businessTypeTree = response.data
        }
      })
    },
    
    handleSave() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 设置创建/更新用户
          if (this.temp.typeId) {
            this.temp.updateUser = this.$store.getters.userId
          } else {
            this.temp.createUser = this.$store.getters.userId
            this.temp.updateUser = this.$store.getters.userId
          }
          
          saveOrUpdateBusinessType(this.temp).then(response => {
            if (response.success) {
              this.$message.success(this.temp.typeId ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.$emit('refresh')
            } else {
              this.$message.error(response.message || '保存失败')
            }
          })
        }
      })
    },
    
    handleClose() {
      this.resetTemp()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
