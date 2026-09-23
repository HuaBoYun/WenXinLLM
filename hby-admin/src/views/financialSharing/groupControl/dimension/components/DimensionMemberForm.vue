<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
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
          <el-form-item label="成员编码" prop="memberCode">
            <el-input
              v-model="form.memberCode"
              placeholder="请输入成员编码"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成员名称" prop="memberName">
            <el-input
              v-model="form.memberName"
              placeholder="请输入成员名称"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="成员类型" prop="memberType">
            <el-select
              v-model="form.memberType"
              placeholder="请选择成员类型"
              style="width: 100%"
            >
              <el-option label="普通成员" value="NORMAL" />
              <el-option label="共享成员" value="SHARED" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否叶子节点" prop="isLeaf">
            <el-radio-group v-model="form.isLeaf">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="排序号" prop="sortNo">
            <el-input-number
              v-model="form.sortNo"
              :min="0"
              :max="9999"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="来源系统" prop="sourceSystem">
            <el-input
              v-model="form.sourceSystem"
              placeholder="请输入来源系统"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来源ID" prop="sourceId">
            <el-input
              v-model="form.sourceId"
              placeholder="请输入来源ID"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="扩展属性" prop="extendedAttrs">
            <el-input
              v-model="form.extendedAttrs"
              type="textarea"
              :rows="3"
              placeholder="请输入扩展属性(JSON格式)"
              maxlength="2000"
            />
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
import { saveDimensionMember, getDimensionMemberDetail } from '@/api/financialSharing/groupControl/dimensionMember'

export default {
  name: 'DimensionMemberForm',
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
    },
    dimensionId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      submitLoading: false,
      form: {
        memberId: '',
        dimensionId: '',
        memberCode: '',
        memberName: '',
        memberType: 'NORMAL',
        parentMemberId: null,
        isLeaf: 'Y',
        sortNo: 0,
        status: 'ACTIVE',
        sourceSystem: '',
        sourceId: '',
        extendedAttrs: ''
      },
      formRules: {
        memberCode: [
          { required: true, message: '请输入成员编码', trigger: 'blur' },
          { max: 50, message: '成员编码长度不能超过50个字符', trigger: 'blur' }
        ],
        memberName: [
          { required: true, message: '请输入成员名称', trigger: 'blur' },
          { max: 100, message: '成员名称长度不能超过100个字符', trigger: 'blur' }
        ],
        memberType: [
          { required: true, message: '请选择成员类型', trigger: 'change' }
        ],
        isLeaf: [
          { required: true, message: '请选择是否叶子节点', trigger: 'change' }
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
        add: '新增成员',
        edit: '编辑成员',
        view: '查看成员'
      }
      return titleMap[this.formType] || '成员信息'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
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
          memberId: '',
          dimensionId: this.dimensionId,
          memberCode: '',
          memberName: '',
          memberType: 'NORMAL',
          parentMemberId: this.formData?.parentMemberId || null,
          isLeaf: 'Y',
          sortNo: 0,
          status: 'ACTIVE',
          sourceSystem: '',
          sourceId: '',
          extendedAttrs: ''
        }
      } else if (this.formType === 'edit' || this.formType === 'view') {
        this.form = {
          ...this.formData,
          dimensionId: this.dimensionId
        }
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
          const res = await saveDimensionMember(this.form)
          if (res.code === 1) {
            this.$message.success(this.formType === 'add' ? '新增成功' : '修改成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(res.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存成员失败:', error)
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

