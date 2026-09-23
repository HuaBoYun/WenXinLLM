<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    :modal="false"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form
        ref="ruleForm"
        label-width="125px"
        :model="form"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="问题单元" prop="businessType">
            <el-input
              v-model="form.businessType"
              clearable
              placeholder="请输入问题单元"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题类型" prop="riskSource">
            <el-input
              v-model="form.riskSource"
              clearable
              placeholder="请输入问题类型"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计问题" prop="riskPoint">
            <el-input
              v-model="form.riskPoint"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入审计问题"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="重点关注事项" prop="control">
            <el-input
              v-model="form.control"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入重点关注事项"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计程序" prop="suditProcess">
            <el-input
              v-model="form.suditProcess"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入审计程序"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="所需资料" prop="bioData">
            <el-input
              v-model="form.bioData"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入所需资料"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { mergeTblNbsjAuditprogramAudit } from '@/api/workbench/auditTools'
  export default {
    name: 'form',
    data() {
      return {
        form: {
          businessType: '',
          riskSource: '',
          riskPoint: '',
          control: '',
          suditProcess: '',
          bioData: '',
          targetId: '',
        },
        title: '详细',
        dialogFormVisible: false,
        rules: {
          businessType: [
            {
              required: true,
              message: '请输入业务单元',
              trigger: 'blur',
            },
          ],
          riskPoint: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
          suditProcess: [
            {
              required: true,
              message: '请输入审计程序',
              trigger: 'blur',
            },
          ],
        },
        footer: false,
      }
    },
    created() {},
    methods: {
      showEdit(row, title, query) {
        console.log('🚀 ~ showEdit ~ query:', query)
        console.log('🚀 ~ showEdit ~ row:', row)
        if (row) {
          this.form = row.auditProGram
          if (this.form.updateTime) {
            delete this.form.updateTime
          }
        } else {
          this.form = {}
          this.form.targetId = query.targetId
          this.form.riskSource = query.targetName
        }
        if (title == 'add') {
          this.title = '新增'
          this.footer = true
        } else if (title == 'deail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '编辑'
          this.footer = true
        }
        this.form.targetId = query.targetId
        this.dialogFormVisible = true
      },
      close() {
        this.footer = false
        this.dialogFormVisible = false
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            delete this.form.createTime
            delete this.form.updateTime
            const { code, msg } = await mergeTblNbsjAuditprogramAudit(this.form)

            if (code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .model-show {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
  }
  .model-show > div {
    background: red;
    margin: 5px;
    padding: 10px;
    color: white;
    font-size: 16px;
    font-weight: 500;
  }
</style>
