<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="印鉴编码" prop="sealCode">
            <el-input
              v-model="form.sealCode"
              placeholder="请输入印鉴编码"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="印鉴名称" prop="sealName">
            <el-input
              v-model="form.sealName"
              placeholder="请输入印鉴名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="印鉴类型" prop="sealTypeId">
            <el-select
              v-model="form.sealTypeId"
              placeholder="请选择印鉴类型"
              style="width: 100%"
              @change="handleSealTypeChange"
            >
              <el-option
                v-for="type in sealTypes"
                :key="type.sealTypeId"
                :label="type.typeName"
                :value="type.sealTypeId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="form.status"
              placeholder="请选择状态"
              style="width: 100%"
            >
              <el-option label="有效" value="ACTIVE" />
              <el-option label="无效" value="INACTIVE" />
              <el-option label="已注销" value="CANCELLED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所有人" prop="ownerName">
            <el-input
              v-model="form.ownerName"
              placeholder="请输入所有人姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保管人" prop="keeperName">
            <el-input
              v-model="form.keeperName"
              placeholder="请输入保管人姓名"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="印鉴图片" prop="sealImage">
        <el-upload
          class="seal-uploader"
          :action="uploadUrl"
          :show-file-list="false"
          :on-success="handleImageSuccess"
          :before-upload="beforeImageUpload"
          :headers="uploadHeaders"
        >
          <img v-if="form.sealImage" :src="form.sealImage" class="seal-image">
          <i v-else class="el-icon-plus seal-uploader-icon"></i>
        </el-upload>
        <div class="upload-tip">只能上传jpg/png文件，且不超过2MB</div>
      </el-form-item>
      
      <el-form-item label="印鉴描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入印鉴描述"
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
import { saveOrUpdateSeal, getEnabledSealTypes } from '@/api/globalTreasurer/czgg'

export default {
  name: 'SealEdit',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '新增印鉴',
      isEdit: false,
      submitLoading: false,
      sealTypes: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload/image',
      uploadHeaders: {
        'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
      },
      form: {
        sealId: null,
        sealCode: '',
        sealName: '',
        sealTypeId: null,
        sealTypeName: '',
        ownerId: null,
        ownerName: '',
        keeperId: null,
        keeperName: '',
        sealImage: '',
        status: 'ACTIVE',
        description: '',
        orgId: null
      },
      rules: {
        sealCode: [
          { required: true, message: '请输入印鉴编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
          { pattern: /^[A-Z][A-Z0-9_]*$/, message: '印鉴编码必须以大写字母开头，只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        sealName: [
          { required: true, message: '请输入印鉴名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        sealTypeId: [
          { required: true, message: '请选择印鉴类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        ownerName: [
          { required: true, message: '请输入所有人姓名', trigger: 'blur' }
        ],
        keeperName: [
          { required: true, message: '请输入保管人姓名', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 显示对话框
    async showEdit(row = null) {
      this.dialogVisible = true
      this.isEdit = !!row
      this.dialogTitle = this.isEdit ? '编辑印鉴' : '新增印鉴'
      
      // 加载印鉴类型
      await this.loadSealTypes()
      
      if (row) {
        this.form = {
          sealId: row.sealId,
          sealCode: row.sealCode,
          sealName: row.sealName,
          sealTypeId: row.sealTypeId,
          sealTypeName: row.sealTypeName,
          ownerId: row.ownerId,
          ownerName: row.ownerName,
          keeperId: row.keeperId,
          keeperName: row.keeperName,
          sealImage: row.sealImage,
          status: row.status,
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
    
    // 加载印鉴类型
    async loadSealTypes() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const orgId = userInfo.linkOrg?.orgid || 1
        const response = await getEnabledSealTypes({ orgId })
        if (response.code === 1) {
          this.sealTypes = response.data || []
        }
      } catch (error) {
        console.error('加载印鉴类型失败：', error)
      }
    },
    
    // 印鉴类型变化
    handleSealTypeChange(sealTypeId) {
      const sealType = this.sealTypes.find(type => type.sealTypeId === sealTypeId)
      if (sealType) {
        this.form.sealTypeName = sealType.typeName
      }
    },
    
    // 重置表单
    resetForm() {
      this.form = {
        sealId: null,
        sealCode: '',
        sealName: '',
        sealTypeId: null,
        sealTypeName: '',
        ownerId: null,
        ownerName: '',
        keeperId: null,
        keeperName: '',
        sealImage: '',
        status: 'ACTIVE',
        description: '',
        orgId: this.getCurrentOrgId()
      }
    },
    
    // 获取当前组织ID
    getCurrentOrgId() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      return userInfo.linkOrg?.orgid || 1
    },
    
    // 图片上传成功
    handleImageSuccess(response) {
      if (response.code === 1) {
        this.form.sealImage = response.data.url
        this.$message.success('图片上传成功')
      } else {
        this.$message.error(response.msg || '图片上传失败')
      }
    },
    
    // 图片上传前验证
    beforeImageUpload(file) {
      const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPGOrPNG) {
        this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
        return false
      }
      return true
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
          
          const response = await saveOrUpdateSeal(this.form)
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

.seal-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 148px;
  height: 148px;
  display: inline-block;
}

.seal-uploader:hover {
  border-color: #409EFF;
}

.seal-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 148px;
  height: 148px;
  line-height: 148px;
  text-align: center;
}

.seal-image {
  width: 148px;
  height: 148px;
  display: block;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>
