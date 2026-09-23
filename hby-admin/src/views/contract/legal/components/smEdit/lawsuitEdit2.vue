<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="150px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="案号">
            <el-input
              v-model="form.proceedno"
              clearable
              placeholder="请输入案号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼阶段">
            <el-input
              v-model="form.porceedstage"
              clearable
              placeholder="请输入诉讼阶段"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审理法院">
            <el-input
              v-model="form.court"
              clearable
              placeholder="请输入审理法院"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审判长">
            <el-input
              v-model="form.presidingjudge"
              clearable
              placeholder="请输入审判长"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法院联系人">
            <el-input
              v-model="form.courtlink"
              clearable
              placeholder="请输入法院联系人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法院联系方式">
            <el-input
              v-model="form.courtcontact"
              clearable
              placeholder="请输入法院联系方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="立案时间">
            <el-date-picker
              v-model="form.filingtime"
              clearable
              placeholder="请输入立案时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开庭时间">
            <el-date-picker
              v-model="form.openingtime"
              clearable
              placeholder="请输入开庭时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缴费提醒时间">
            <el-date-picker
              v-model="form.paymentremindtime"
              clearable
              placeholder="请输入缴费提醒时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收到判决时间">
            <el-date-picker
              v-model="form.judgetiem"
              clearable
              placeholder="请输入收到判决时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外聘律师">
            <el-radio-group v-model="form.isexternallawyer">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="律所">
            <el-input
              v-model="form.lawyearword"
              clearable
              placeholder="请输入律所"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="律师">
            <el-input
              v-model="form.lawyearname"
              clearable
              placeholder="请输入律师"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="律师联系方式">
            <el-input
              v-model="form.lawyearlink"
              clearable
              placeholder="请输入律师联系方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理人">
            <el-input
              v-model="form.negotiator"
              clearable
              placeholder="请输入代理人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理人联系方式">
            <el-input
              v-model="form.negotiatorlink"
              clearable
              placeholder="请输入代理人联系方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人">
            <el-input
              v-model="form.username"
              clearable
              placeholder="请输入收款比例"
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
          <el-form-item label="发现的问题">
            <el-input
              v-model="form.casepromotion"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入发现的问题"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="存在的困难">
            <el-input
              v-model="form.existingdifficulties"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入存在的困难"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="推进的措施">
            <el-input
              v-model="form.measurespromote"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入推进的措施"
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
    proceedingsRecordModify,
    proceedingsRecordSave,
  } from '@/api/contract/legal'
  import ExecutorOptions from '@/views/contract/legal/components/options/executor'
  import * as dayjs from 'dayjs'

  export default {
    name: 'TemplateEdit',
    components: { ExecutorOptions },
    data() {
      return {
        form: {
          zxstaffid: undefined,
          proceedid: undefined,
          proceedno: undefined,
          porceedstage: undefined,
          court: undefined,
          presidingjudge: undefined,
          courtlink: undefined,
          courtcontact: undefined,
          filingtime: undefined,
          openingtime: undefined,
          paymentremindtime: undefined,
          judgetiem: undefined,
          isexternallawyer: 1,
          lawyearword: undefined,
          lawyearname: undefined,
          lawyearlink: undefined,
          negotiator: undefined,
          negotiatorlink: undefined,
          casepromotion: undefined,
          existingdifficulties: undefined,
          measurespromote: undefined,
          createtime: undefined,
          username: undefined,
          realname: undefined,
          ourcontractperson: undefined,
        },
        rules: {},
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {
      //时间处理赋值
      this.form.createtime = dayjs().format('YYYY-MM-DD')
    },
    methods: {
      showEdit(row) {
        this.form.litigationId = row.litigationId
        if (!row.proceedid) {
          this.title = '添加'
          this.form.createtime = dayjs().format('YYYY-MM-DD')
          this.form.username = '张孝昆'
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.form.createtime = dayjs(row.createtime).format('YYYY-MM-DD')
          this.form.username = row.realname
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
            if (!this.form.proceedid) {
              const { msg } = await proceedingsRecordSave(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.form.proceedid) {
              const { msg } = await proceedingsRecordModify(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.$emit('fetch-data')
            this.$emit('editadd')
            this.close()
          }
        })
      },
      //回调
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
