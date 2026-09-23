<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="150px" :model="form" :rules="rules">
      <el-form-item label="跳转类型" prop="projectType">
        <!-- <el-radio-group v-model="form.projectType">
          <el-radio :label="1">内部模块</el-radio>
        </el-radio-group> -->
        内部模块
      </el-form-item>

      <el-form-item label="模块标识" prop="uniqueIdentification">
        <el-input
          v-model.trim="form.uniqueIdentification"
          :disabled="title == '编辑'"
        />
      </el-form-item>
      <el-form-item label="模块名称" prop="projectName">
        <el-input v-model.trim="form.projectName" />
      </el-form-item>
      <el-form-item label="模块图标" prop="icon">
        <Icon @getSelectIcon="getSelectIcon" :icon="form.icon"></Icon>
      </el-form-item>
      <el-form-item label="模块排序" prop="sort">
        <el-input v-model.trim="form.sort" />
      </el-form-item>
      <el-form-item label="模块颜色" prop="color">
        <el-color-picker v-model="form.color"></el-color-picker>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import Icon from './moduleIcon.vue'
  import { addModuleInfo, getDefaultModuleInfo } from '@/api/setting/system'
  export default {
    name: '',
    components: { Icon },
    data() {
      return {
        list: [],
        form: {
          uniqueIdentification: '',
          projectName: '',
          projectType: 1,
          sort: '',
          icon: '',
          color: '',
          id: '',
        },
        rules: {
          uniqueIdentification: [
            { required: true, trigger: 'blur', message: '请输入模块标识' },
          ],
          projectName: [
            { required: true, trigger: 'blur', message: '请输入模块名称' },
          ],
          icon: [
            { required: true, trigger: 'blur', message: '请选择模块图标' },
          ],
          sort: [
            { required: true, trigger: 'blur', message: '请选择模块排序' },
          ],
          color: [
            { required: true, trigger: 'blur', message: '请选择图标颜色' },
          ],
          otherProjectRoute: [
            { required: true, trigger: 'blur', message: '请填写其他项目路由' },
          ],
          projectType: [
            { required: true, trigger: 'blur', message: '请选择是否跳转引迈' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        modelTypeOptions: [],
      }
    },
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          getDefaultModuleInfo({ id: row.id }).then((res) => {
            // this.form = res.data
            Object.keys(this.form).forEach((key) => {
              this.form[key] = row[key] || ''
            })
          })
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
            addModuleInfo({
              ...this.form,
            }).then((res) => {
              if (res.code == 200) {
                this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch_data')
                this.close()
              }
            })
          }
        })
      },
      getSelectIcon(icon) {
        this.form.icon = icon
      },
      changeType() {
        this.form.otherProjectRoute = ''
        this.form.uniqueIdentification = ''
      },
    },
  }
</script>
