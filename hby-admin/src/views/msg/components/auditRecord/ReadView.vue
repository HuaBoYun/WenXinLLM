<template>
  <div>
    <el-form
      :model="form"
      ref="form"
      label-width="80px"
      :rules="rules"
      :disabled="!footer"
    >
      <el-form-item label="项目编号" prop="projectCode">
        <el-input v-model.trim="form.projectCode" :disabled="true" />
      </el-form-item>
      <el-form-item label="项目名称" prop="prjoectName">
        <el-input v-model.trim="form.prjoectName" :disabled="true" />
      </el-form-item>
      <el-form-item label="借阅日期" prop="borrowDate">
        <el-date-picker
          v-model="form.borrowDate"
          type="date"
          placeholder="借阅日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          :style="{ width: '100%' }"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="归还日期" prop="backDate">
        <el-date-picker
          v-model="form.backDate"
          type="date"
          placeholder="归还日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="借阅事由" prop="memo">
        <el-input v-model.trim="form.memo" type="textarea" />
      </el-form-item>
    </el-form>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button type="primary" @click="add">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
  </div>
</template>

<script>
  import { saveDadajySaveOrUpdate, getDajyDetail } from '@/oapi/audit/archives'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import * as dayjs from 'dayjs'

  export default {
    name: 'LcdyEdit',
    components: {
      Resubmit,
    },
    data() {
      return {
        rules: {
          projectCode: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
          ],
          prjoectName: [
            {
              required: true,
              message: '请输入活动名称',
              trigger: 'blur',
            },
          ],
          borrowDate: [
            {
              required: true,
              message: '请选择借阅日期',
              trigger: 'blur',
            },
          ],
          backDate: [
            {
              required: true,
              message: '请选择归还日期',
              trigger: 'blur',
            },
          ],
          memo: [
            {
              required: true,
              message: '请输入借阅事由',
              trigger: 'blur',
            },
          ],
        },
        form: {
          projectCode: '',
          prjoectName: '',
          borrowDate: '',
          projectId: '',
          backDate: '',
          memo: '',
        },
        title: '',
        dialogFormVisible: false,
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        footer: false,
      }
    },
    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        console.log('🚀 ~ title:', title)
        this.footer = title === 'edit'
        this.fromId = formId
        this.ymFromId = ymFromId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.fromIdcopy = formId
        this.status = status
        this.fetchData({ id: formId })
      },
      async fetchData(row) {
        console.log('🚀 ~ fetchData ~ row:', row)
        this.loading = true
        const { data, code } = await getDajyDetail({ borrowId: row.id })
        this.loading = false
        if (code == 1) {
          Object.assign(this.form, data)
          this.form.prjoectName = data.prjoectname
          this.form.projectCode = data.projectcode
          this.form.borrowDate = dayjs(data.createDate).format('YYYY-MM-DD')
          this.form.backDate = dayjs(data.returnDate).format('YYYY-MM-DD')
        } else {
          this.$message.error(res.msg || '操作失败！')
        }
      },
      ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.$bus.$emit('updateMsg', 0)
      },
      async add() {
        this.$refs['form'].validate(async (valid) => {
          if (!valid) {
            return false
          } else {
            let obj = { ...this.form }
            delete obj.projectcode
            delete obj.prjoectname
            delete obj.createDate
            delete obj.returnDate

            const res = await saveDadajySaveOrUpdate(obj)
            if (res.code === 1) {
              this.$message.success('保存成功')
            } else {
              this.$message.error('保存失败')
            }
          }
        })
      },
    },
  }
</script>
