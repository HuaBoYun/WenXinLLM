<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-form ref="form" label-width="100px" :model="form" :rules="rules">
        <el-form-item label="姓名" prop="username">
          <el-input
            v-model="form.username"
            readonly
            :style="{ width: 'calc(100% - 66px)' }"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleOrgUser"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="部门" prop="orgname">
          <el-input v-model.trim="form.orgname" disabled readonly />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model.trim="form.position" />
        </el-form-item>
        <el-form-item label="职称" prop="professional">
          <el-input v-model.trim="form.professional" />
        </el-form-item>
        <el-form-item label="资格证书" prop="qualification">
          <el-input v-model.trim="form.qualification" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <org-user-dialog ref="orgUserDialog" @select="handSelectOrgUser" />
  </div>
</template>

<script>
  import { nbzjSave } from '@/api/setting/specialist'
  import OrgUserDialog from './OrgUserDialog'
  export default {
    name: 'InternalEdit',
    components: { OrgUserDialog },
    data() {
      return {
        form: {
          interiorid: undefined,
          userid: undefined,
          username: '',
          orgid: undefined,
          orgname: '',
          position: '',
          professional: '',
          qualification: '',
        },
        rules: {
          username: [
            { required: true, trigger: 'blur', message: '请选择用户' },
          ],
          position: [
            { required: true, trigger: 'blur', message: '请输入职位' },
          ],
          orgname: [{ required: true, trigger: 'blur', message: '请输入部门' }],
          professional: [
            { required: true, trigger: 'blur', message: '请输入职称' },
          ],
          qualification: [
            { required: true, trigger: 'blur', message: '请输入资格证书' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
        data: [],
      }
    },
    created() {},
    methods: {
      handleOrgUser() {
        this.$refs.orgUserDialog.show()
      },
      handSelectOrgUser(data) {
        console.warn('handSelectOrgUser', data)
        this.form.userid = data.staffid
        this.form.username = data.realname
        this.form.orgid = data.orgid
        this.form.orgname = data.orgname
      },
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => (this.form[key] = row[key]))
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await nbzjSave(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
