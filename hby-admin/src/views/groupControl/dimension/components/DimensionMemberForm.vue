<template>
  <el-dialog
    :title="memberId ? '编辑成员' : '新增成员'"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="成员编码" prop="memberCode">
            <el-input v-model="form.memberCode" placeholder="请输入成员编码" :disabled="!!memberId" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成员名称" prop="memberName">
            <el-input v-model="form.memberName" placeholder="请输入成员名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="父节点" prop="parentMemberId">
            <el-tree-select
              v-model="form.parentMemberId"
              :data="memberTree"
              :props="treeProps"
              placeholder="请选择父节点"
              clearable
              check-strictly
              :disabled="!!parentId"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序号" prop="sortNo">
            <el-input-number v-model="form.sortNo" :min="0" :max="9999" style="width: 100%" />
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

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDimensionMemberDetail, saveDimensionMember, getDimensionMemberTree } from '@/api/groupControl/dimensionMember'

export default {
  name: 'DimensionMemberForm',
  props: {
    visible: { type: Boolean, default: false },
    memberId: { type: String, default: null },
    dimensionId: { type: String, default: null },
    parentId: { type: String, default: null }
  },
  data() {
    return {
      dialogVisible: this.visible,
      submitting: false,
      memberTree: [],
      treeProps: {
        children: 'children',
        label: 'memberName',
        value: 'memberId'
      },
      form: {
        memberId: null,
        dimensionId: null,
        memberCode: '',
        memberName: '',
        parentMemberId: null,
        sortNo: 0,
        status: 'ACTIVE',
        description: ''
      },
      rules: {
        memberCode: [{ required: true, message: '请输入成员编码', trigger: 'blur' }],
        memberName: [{ required: true, message: '请输入成员名称', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    }
  },
  created() {
    this.form.dimensionId = this.dimensionId
    if (this.parentId) {
      this.form.parentMemberId = this.parentId
    }
    if (this.memberId) {
      this.loadDetail()
    }
    this.loadMemberTree()
  },
  methods: {
    loadDetail() {
      getDimensionMemberDetail({ memberId: this.memberId }).then(response => {
        if (response.code === 1 && response.data) {
          this.form = { ...response.data }
        }
      })
    },
    loadMemberTree() {
      if (this.dimensionId) {
        getDimensionMemberTree({ dimensionId: this.dimensionId }).then(response => {
          if (response.code === 1) {
            this.memberTree = response.data || []
          }
        })
      }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitting = true
          saveDimensionMember(this.form).then(response => {
            if (response.code === 1) {
              this.$message.success('保存成功')
              this.$emit('success')
              this.handleClose()
            }
            this.submitting = false
          }).catch(() => {
            this.submitting = false
          })
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.form.resetFields()
    }
  }
}
</script>

