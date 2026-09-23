<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form :model="form" ref="form" label-width="80px" :rules="rules">
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
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList ref="process" />
  </el-dialog>
</template>

<script>
  import {
    TjspBorrow,
    SubmitRecordApproval,
    saveDadajySaveOrUpdate,
  } from '@/oapi/audit/archives'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'LcdyEdit',
    components: {
      ProcessList,
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
      }
    },
    methods: {
      showEdit(row) {
        console.log('🚀 ~ showEdit ~ row:', row)
        this.dialogFormVisible = true
        this.$nextTick(() => {
          if (!row) {
            this.title = '添加'
          } else {
            this.title = '申请借阅'
            this.form = {
              projectCode: row.qdcode,
              prjoectName: row.projectName,
              projectid: row.id,
            }
          }

          this.$refs['form'].clearValidate()
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      async save() {
        this.$refs['form'].validate(async (valid) => {
          if (!valid) {
            return false
          } else {
            let obj = { ...this.form }
            // delete obj.prjoectName
            // delete obj.projectCode

            const res = await saveDadajySaveOrUpdate(obj)
            if (res.code === 1) {
              // SubmitRecordApproval({
              //   borrowid: res.data.BorrowRecord.borrowid,
              // }).then((res) => {
              //   this.$message.success('提交成功')
              // })
              const tableId = 184
              const fromId = res.data.borrowRecord.borrowid
              this.$refs['process'].save(tableId, fromId)
              this.close()
            } else {
              this.$message.error('提交失败')
            }
          }
        })
      },
    },
  }
</script>
