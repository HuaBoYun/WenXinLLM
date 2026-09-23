<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="编号" prop="pagecode">
        <el-input v-model.trim="form.pagecode" />
      </el-form-item>
      <el-form-item label="名称" prop="pagename">
        <el-input v-model.trim="form.pagename" />
      </el-form-item>
      <el-form-item label="所属机构">
        <el-input v-model.trim="form.orgname" disabled />
      </el-form-item>
      <el-form-item v-if="form.level == 2" label="类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio label="3">填报</el-radio>
          <el-radio label="4">报表</el-radio>
          <el-radio label="5">实时分析</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model.trim="form.pageDes" type="textarea" />
      </el-form-item>
      <el-form-item v-if="form.level == 2" label="URL">
        <el-input v-model.trim="form.rqurl" type="textarea" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model.trim="form.memo1" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addReportMenu } from '@/api/setting/themeRepertory'

  export default {
    name: 'ThemeEdit',
    data() {
      return {
        form: {
          pageid: undefined,
          pagecode: '',
          pagename: '',
          memo1: '',
          pageDes: '',
          unit: 1, // 所属机构id
          orgname: '', // 所属机构名称
          rqurl: undefined,
          type: 0, // 0,1用来区分主题展示和手机主题;3,4,5用来代表二级表单里的类型
          level: undefined,
          pid: undefined, //父id
        },
        rules: {
          pagecode: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          pagename: [
            { required: true, trigger: 'blur', message: '请输入名称' },
          ],
          type: [{ required: true, trigger: 'change', message: '请选择类型' }],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    computed: {
      currentOrg() {
        const orgStr = window.sessionStorage.getItem('current-org')
        if (orgStr && !this.alwaysRoot) {
          try {
            const org = JSON.parse(orgStr)
            if (org.id && org.label) {
              return org
            }
          } catch (e) {}
        }
        return {
          id: 1,
          label: '长江投资（中国）有限公司',
        }
      },
    },
    created() {},
    methods: {
      showEdit(obj, row) {
        const { type, level, pid } = obj
        this.form.type = type
        this.form.pid = pid
        if (!row) {
          this.title = '添加'
          if (level == 2) {
            // 如果是二级，type用来表示表单中的类型，默认值为3
            this.form.type = 3
          }
          this.form.unit = this.currentOrg.id
          this.form.orgname = this.currentOrg.label
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.form['pid'] = row.pagebody
        }
        this.form.level = level
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
            delete this.form.createdate
            // delete this.form.type
            delete this.form.memo2
            const { msg } = await addReportMenu(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
