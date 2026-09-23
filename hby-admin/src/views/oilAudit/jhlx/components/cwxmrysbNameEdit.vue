<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="编辑"
    :visible.sync="dialogJdVisible"
    width="600px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-form
      ref="ruleForm"
      label-width="140px"
      :model="formData"
      :rules="rules"
      size="mini"
      :disabled="formDisabled"
    >
      <el-form-item label="项目名称" prop="projectname">
        <el-input
          v-model="formData.projectname"
          clearable
          placeholder="请输入项目名称"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">保存</el-button>
    </div>

    <!-- 人员 -->
  </el-dialog>
</template>
<script>
  import { audit2LsaveOrUpdate3 } from '@/api/oilAudit/jhgl/jhcg'

  export default {
    name: 'cwxmrysbAssignEdit',
    components: { },
    data() {
      return {
        loading: false,
        dialogJdVisible: false,
        formDisabled: false,
        formData: {
          id: '',
          projectname: '',
        },
        rules: {
          projectname: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        },
        rows: {},
      }
    },
    methods: {
      showEdit(row) {
        // TODO
        console.log('row', row)
        this.rows = row
        this.dialogJdVisible = true
        this.formData.id = row.id
        this.formData.projectname = row.projectname
      },
      close() {
        this.formData.id = ''
        this.formData.projectname = ''
        this.dialogJdVisible = false
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const { msg, code } = await audit2LsaveOrUpdate3(this.formData)
            this.loading = false
            if (code == 1) {
              this.$baseMessage(msg, 'success')
              this.$emit('save', { ...this.formData })
              this.close()
            } else {
              this.$baseMessage(msg, 'error')
            }
          }
        })
      },
    },
  }
</script>
