<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="100px"
        :model="formData"
        size="medium"
        :rules="rules"
        :disabled="disabled"
      >
        <el-col :span="12">
          <el-form-item label="问题单元" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              placeholder="请输入问题单元"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题类型">
            <el-input
              v-model="formData.riskSource"
              clearable
              placeholder="请输入问题类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计问题" prop="riskPoint">
            <el-input
              v-model="formData.riskPoint"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入审计问题"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="重点关注事项" prop="control">
            <el-input
              v-model="formData.control"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入重点关注事项"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计程序" prop="suditProcess">
            <el-input
              v-model="formData.suditProcess"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入审计程序"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="所需资料">
            <el-input
              v-model="formData.bioData"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入所需资料"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">关闭</el-button>
      <el-button type="primary" @click="save" v-if="!disabled">确认</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { defCatAdd, getDetailZy } from '@/oapi/audit/preparation'

export default {
  components: {},
  inheritAttrs: false,
  props: {
    targetId: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      formData: {},
      dialogFormVisible: false,
      title: '',
      programId: '',
      disabled: false,
      // targetId: this.targetId,
      rules: {
        businessType: [
          { required: true, message: '请输入问题单元', trigger: 'change' },
        ],
        riskPoint: [
          { required: true, message: '请输入审计问题', trigger: 'change' },
        ],
        suditProcess: [
          { required: true, message: '请输入审计程序', trigger: 'change' },
        ],
      },
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    async showEdit(row, flag) {
      if (!row) {
        this.title = '新建'
        this.disabled = false
        this.formData = {}
        this.programId = ''
      } else if (row && flag) {
        this.title = '详情'
        this.disabled = true
        let res = await getDetailZy({
          programid: row.programId,
        })
        this.formData = res.data.auditProGram
      } else {
        this.title = '编辑'
        this.disabled = false
        let res = await getDetailZy({
          programid: row.programId,
        })
        this.programId = res.data.auditProGram.programId
        this.formData = res.data.auditProGram
      }

      this.dialogFormVisible = true
    },
    save() {
      this.$refs['elForm'].validate(async (valid) => {
        if (valid) {
          let res = await defCatAdd({
            businessType: this.formData.businessType,
            riskSource: this.formData.riskSource,
            riskPoint: this.formData.riskPoint,
            control: this.formData.control,
            suditProcess: this.formData.suditProcess,
            bioData: this.formData.bioData,
            programId: this.programId,
            targetId: this.targetId,
          })
          if (res.code == 1) {
            this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
            this.close()
          }
        }
        this.$emit('fetch-data')
      })
    },
    close() {
      this.$refs['elForm'].resetFields()
      this.formData = this.$options.data().formData
      this.dialogFormVisible = false
      // this.$emit('fetch-data')
    },
  },
}
</script>
<style scoped>
.el-form-item__content span {
  font-size: 14px;
  font-weight: 500;
  color: darkgray;
}
/* ::v-deep .el-dialog {
    height: 78vh;
    overflow: auto;
  } */
</style>
