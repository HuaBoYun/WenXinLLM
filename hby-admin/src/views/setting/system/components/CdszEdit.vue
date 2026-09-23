<!--
 * @Date: 2022-03-07 10:59:57
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 17:55:27
 * @FilePath: /hb-admin/src/views/setting/system/components/CdszEdit.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="140px" :model="form" :rules="rules">
      <el-form-item label="模块名称" prop="rightname">
        <el-input v-model.trim="form.rightname" />
      </el-form-item>
      <el-row v-if="title == '添加'">
        <el-col :span="12">
          <el-form-item label="是否启用" prop="indicatorstatus">
            <el-switch v-model="form.indicatorstatus" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用帮助文档" prop="rightbz">
            <el-switch v-model="form.rightbz" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="文档内容" prop="rightcontent">
        <tinymce
          v-model="form.rightcontent"
          :height="300"
          placeholder="请输入文档内容"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveMenuSetting } from '@/api/setting/system'
  import Tinymce from '@/components/Tinymce'
  import { modelTypeOptions } from '../consts'

  export default {
    name: 'CdszEdit',
    components: { Tinymce },
    data() {
      return {
        form: {
          fatherrightid: 1, //只能新建一级菜单
          rightid: undefined,
          rightname: undefined,
          indicatorstatus: 1,
          rightbz: 1,
          rightcontent: undefined,
        },
        rules: {
          rightname: [
            { required: true, trigger: 'blur', message: '请输入模块名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        modelTypeOptions,
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
            const { code, msg } = saveMenuSetting(this.form)
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>
