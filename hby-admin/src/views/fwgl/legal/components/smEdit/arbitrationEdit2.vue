<template>
  <el-dialog
    :close-on-click-modal="false"
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="150px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="日期" prop="dealdate">
            <el-date-picker
              v-model="form.dealdate"
              clearable
              placeholder="请输入日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁阶段" prop="arstage">
            <el-input
              v-model="form.arstage"
              clearable
              placeholder="请输入仲裁阶段"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁机构联系人" prop="arcontactperson">
            <el-input
              v-model="form.arcontactperson"
              clearable
              placeholder="请输入仲裁机构联系人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方代理人" prop="ourcontractperson">
            <el-input
              v-model="form.ourcontractperson"
              clearable
              placeholder="请选择我方代理人"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人">
            <el-input
              v-model="form.createname"
              clearable
              placeholder="录入人"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入时间">
            <el-input
              v-model="form.createtime"
              clearable
              placeholder="请输入录入时间"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="过程纪要">
            <el-input
              v-model="form.arrecordmode"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入过程纪要"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="form.arrecordmemo"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <executor-options ref="executor" @selected="handleSelected" />
  </el-dialog>
</template>

<script>
  import {
    ArbitrationRecordSave,
    ArbitrationRecordToModify,
  } from '@/api/fwgl/legal'
  import ExecutorOptions from '@/views/fwgl/legal/components/options/executor'

  export default {
    name: 'TemplateEdit',
    components: { ExecutorOptions },
    data() {
      return {
        form: {
          dealdate: undefined,
          arstage: undefined,
          arcontactperson: undefined,
          ourcontractperson: undefined,
          createname: '张孝昆',
          createtime: undefined,
          arrecordmode: undefined,
          arrecordmemo: undefined,
          arbitraId: undefined,
          zxstaffid: undefined,
          arrecordid: undefined,
        },
        rules: {
          dealdate: [
            {
              required: true,
              message: '请选择日期',
              trigger: 'blur',
            },
          ],
          arstage: [
            {
              required: true,
              message: '请选择仲裁阶段',
              trigger: 'blur',
            },
          ],
          arcontactperson: [
            {
              required: true,
              message: '请选择仲裁机构联系人',
              trigger: 'blur',
            },
          ],
          ourcontractperson: [
            {
              required: true,
              message: '请选择我方代理人',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {
      // const yy = new Date().getFullYear()
      // const mm =
      //   new Date().getMonth() + 1 < 10
      //     ? '0' + new Date().getMonth()
      //     : new Date().getMonth() + 1
      // const dd =
      //   new Date().getDate() < 10
      //     ? '0' + new Date().getDate()
      //     : new Date().getDate()
      // this.form.createtime = yy + '-' + mm + '-' + dd
    },
    methods: {
      time() {
        const yy = new Date().getFullYear()
        let mm = ''
        if (new Date().getMonth() + 1 < 10) {
          const mmm = new Date().getMonth() + 1
          mm = '0' + mmm
        } else {
          mm = new Date().getMonth() + 1
        }
        let dd = ''
        if (new Date().getDate() < 10) {
          const ddd = new Date().getDate()
          dd = '0' + ddd
        } else {
          dd = new Date().getDate()
        }
        this.form.createtime = yy + '-' + mm + '-' + dd
      },
      /**
       * @description: 外部打开dialog
       * @param {*} row 编辑或详情数据
       * @return {*}
       */      
      showEdit(row) {
        this.time()
        this.form.arbitraId = row.arbitraId
        if (!row.arrecordid) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
        }
        this.dialogFormVisible = true
      },
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
            const func = this.form.arrecordid
              ? ArbitrationRecordToModify
              : ArbitrationRecordSave
            const { msg } = await func(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.$emit('editadd')
            this.close()
          }
        })
      },
      /**
       * @description: 选择回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleSelected(val) {
        this.form.zxstaffid = val.staffid
        this.form.ourcontractperson = val.realname
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
