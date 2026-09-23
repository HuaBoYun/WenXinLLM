<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="风险编号" prop="riskNumber">
        <el-input v-model.trim="form.riskNumber" />
      </el-form-item>
      <el-form-item
        v-if="form.risklevel != 1"
        label="上级风险"
        prop="riskparentname"
      >
        <el-input v-model.trim="form.riskparentname" readonly />
      </el-form-item>
      <el-form-item
        :label="`${['一', '二', '三'][form.risklevel - 1]}类`"
        prop="riskclass"
      >
        <el-input v-model.trim="form.riskclass" type="textarea" />
      </el-form-item>
      <el-form-item label="政策依据" prop="policybasis">
        <el-input v-model.trim="form.policybasis" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addRiskClass, modifyRiskClass } from '@/api/setting/themeRepertory'

  export default {
    name: 'RiskEdit',
    data() {
      return {
        form: {
          riskid: undefined,
          riskNumber: '',
          riskparentname: '',
          riskclass: '',
          policybasis: '',
          parentid: this.parentId,
          risklevel: this.riskLevel,
        },
        rules: {
          riskNumber: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          riskclass: [
            { required: true, trigger: 'blur', message: '请输入一类' },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        Object.keys(this.form).forEach((key) => (this.form[key] = row[key]))
        if (!this.form.parentid) {
          this.form.parentid = ''
        }
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
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
            const bCreate = !this.form.riskid
            const func = bCreate ? addRiskClass : modifyRiskClass
            const msg = bCreate ? '添加成功' : '修改成功'
            func(this.form).then((res) => {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            })
          }
        })
      },
    },
  }
</script>
