<!--
 * @Date: 2022-03-09 09:14:30
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-25 22:47:37
 * @FilePath: /hb-admin/src/views/setting/system/components/BhszEdit.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="140px" :model="form" :rules="rules">
      <el-form-item label="编号ID" prop="noid">
        <el-input v-model.trim="form.noid" />
      </el-form-item>
      <el-form-item label="编号名称" prop="nocode">
        <el-input v-model.trim="form.nocode" />
      </el-form-item>
      <el-form-item label="编号前缀" prop="noSuffix">
        <el-input v-model.trim="form.noSuffix" />
      </el-form-item>
      <el-form-item label="编号分隔符" prop="noSepartor">
        <el-input v-model.trim="form.noSepartor" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { updateSnSetting } from '@/api/setting/system'

  export default {
    name: 'BhszEdit',
    data() {
      return {
        form: {
          noid: undefined,
          nocode: '',
          noSuffix: '',
          noNumber: '',
          noSepartor: '',
        },
        rules: {
          nocode: [
            { required: true, trigger: 'blur', message: '请输入编号名称' },
          ],
          noSuffix: [
            { required: true, trigger: 'blur', message: '请输入编号前缀' },
          ],
          noSepartor: [
            { required: true, trigger: 'blur', message: '请输入编号分隔符' },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
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
            const res = await updateSnSetting(this.form)
            this.$baseMessage(res, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
