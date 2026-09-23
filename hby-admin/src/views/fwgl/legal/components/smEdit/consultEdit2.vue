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
      <el-form
        ref="form"
        label-width="150px"
        :model="form"
        :rules="rules"
        :disabled="allDisabled"
      >
        <el-col :span="12">
          <el-form-item label="谈判时间" prop="negotiationtime">
            <el-date-picker
              v-model="form.negotiationtime"
              clearable
              placeholder="请输入谈判时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="谈判方式" prop="negotiationmode">
            <el-input
              v-model="form.negotiationmode"
              clearable
              placeholder="请输入谈判方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方谈判人" prop="recordcounterpart">
            <el-input
              v-model="form.recordcounterpart"
              clearable
              placeholder="请输入对方谈判人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="我方谈判人" prop="zxstaffname">
            <el-input
              v-model="form.zxstaffname"
              clearable
              placeholder="请选择我方谈判人"
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
          <el-form-item label="法院名称">
            <el-input
              v-model="form.courtname"
              clearable
              placeholder="请输入法院名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法院承办人">
            <el-input
              v-model="form.courtparter"
              clearable
              placeholder="请输入法院承办人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法院联系方式">
            <el-input
              v-model="form.courtlink"
              clearable
              placeholder="请输入法院联系方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人">
            <el-input
              v-model="form.createname"
              clearable
              placeholder="请输入录入人"
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
          <el-form-item label="协商记录">
            <el-input
              v-model="form.negotiationrecord"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入协商记录"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="form.negetiationmemoe"
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
      <el-button type="primary" @click="save" v-if="!allDisabled">
        确 定
      </el-button>
    </template>
    <executor-options ref="executor" @selected="handleSelected" />
  </el-dialog>
</template>

<script>
  import {
    LegalnegotiatedRecordSave,
    negotiatedRecordModify,
  } from '@/api/fwgl/legal'
  import ExecutorOptions from '@/views/fwgl/legal/components/options/executor'
  import * as dayjs from 'dayjs'
  export default {
    name: 'TemplateEdit',
    components: { ExecutorOptions },
    data() {
      return {
        form: {
          zxstaffid: undefined,
          netotiaId: undefined,
          negotiationtime: undefined,
          negotiationmode: undefined,
          recordcounterpart: undefined,
          zxstaffname: undefined,
          courtname: undefined,
          courtparter: undefined,
          courtlink: undefined,
          createname: undefined,
          createtime: undefined,
          negotiationrecord: undefined,
          negetiationmemoe: undefined,
          recordid: undefined,
        },
        rules: {
          negotiationtime: [
            {
              required: true,
              message: '请选择谈判时间',
              trigger: 'blur',
            },
          ],
          negotiationmode: [
            {
              required: true,
              message: '请输入谈判方式',
              trigger: 'blur',
            },
          ],
          recordcounterpart: [
            {
              required: true,
              message: '请输入对方谈判人',
              trigger: 'blur',
            },
          ],
          zxstaffname: [
            {
              required: true,
              message: '请选择我方谈判人',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        allDisabled: false,
      }
    },
    created() {
      const yy = new Date().getFullYear()
      const mm =
        new Date().getMonth() + 1 < 10
          ? '0' + new Date().getMonth()
          : new Date().getMonth() + 1
      const dd =
        new Date().getDate() < 10
          ? '0' + new Date().getDate()
          : new Date().getDate()
      this.form.createtime = yy + '-' + mm + '-' + dd
    },
    methods: {
      showEdit(row, title) {
        if (title == 'detail') {
          this.title = '详情'
          this.allDisabled = true
        } else if (title == 'edit') {
          this.title = '编辑'
        } else {
          this.title = '新增'
        }
        this.form.negotiaid = row.netotiaId
        if (!row.recordid) {
          this.form.createtime = dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss')
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.createname = userInfo.realname
        } else {
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.form.recordcounterpart = row.recordcounterpart
          this.form.zxstaffid = row.ournegotiator
          this.form.zxstaffname = row.wFTPR
          this.form.createname = row.lRR
          //this.form.lRRID = row.createstaff
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
        this.allDisabled = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (!this.form.recordid) {
              const { msg } = await LegalnegotiatedRecordSave(this.form)
              this.$baseMessage(
                msg || '新增成功',
                'success',
                'vab-hey-message-success'
              )
            } else if (this.form.recordid) {
              const { msg } = await negotiatedRecordModify(this.form)
              this.$baseMessage(
                msg || '修改成功',
                'success',
                'vab-hey-message-success'
              )
            }
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
        this.form.zxstaffname = val.realname
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
