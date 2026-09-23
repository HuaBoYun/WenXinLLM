<!--
 * @Author: raincoat dev@example.com
 * @Date: 2022-10-01 14:23:27
 * @LastEditors: raincoat dev@example.com
 * @LastEditTime: 2024-01-20 21:29:10
 * @FilePath: \hb-admin(master)\src\views\fwgl\flfw\components\CnflfwEdit.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="70%"
    @close="close"
  >
    <el-form
      ref="form"
      class="form-edit"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="单位名称" prop="code">
            <el-input v-model.trim="form.code" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门名称" prop="name">
            <el-input v-model.trim="form.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门负责人" prop="name">
            <el-input v-model.trim="form.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主管领导">
            <el-input v-model.trim="form.name" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="主管任职情况">
        <el-radio-group v-model="form.check">
          <el-radio label="专职" value="专职" />
          <el-radio label="兼职" value="兼职" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="机构职责">
        <el-input></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model.trim="form.remark"
          maxlength="300"
          show-word-limit
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="相关附件">
        <Attachment></Attachment>
      </el-form-item>
      <MemberList></MemberList>
      <el-row>
        <el-col :span="12">
          <el-form-item label="登记人">xxx</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登记时间">2022-02-22 22:22:22</el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="success" @click="save">保 存</el-button>
      <el-button type="primary" @click="save">发 布</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'
  import Attachment from './Attachment.vue'
  import MemberList from './MemberList.vue'

  export default {
    name: 'CnflfwEdit',
    components: { Attachment, MemberList },
    data() {
      return {
        form: {
          code: '',
          name: '',
          org: '长江集团有限公司',
          intro: '',
          remark: '',
          check: '0',
        },
        rules: {
          code: [{ required: true, trigger: 'blur', message: '请输入编号' }],
          name: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
      }
    },
    created() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} row 编辑或详情数据
       * @return {*}
       */      
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭弹窗以及清理缓存数据
       * @return {*}
       */      
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style>
  .form-edit .el-cascader {
    width: 100%;
  }
</style>
