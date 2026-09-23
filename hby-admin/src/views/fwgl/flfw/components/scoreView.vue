<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item
            label="事务所名称"
            label-width="140px"
            prop="businessPremisesName"
          >
            <el-input
              v-model="formData.businessPremisesName"
              clearable
              placeholder="请输入事务所名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="服务质量评分"
            label-width="140px"
            prop="serviceQualityGrade"
          >
            <el-input
              v-model="formData.serviceQualityGrade"
              clearable
              placeholder="请输入服务质量评分"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="沟通合作评分"
            label-width="140px"
            prop="communicationAndCollaborationGrade"
          >
            <el-input
              v-model="formData.communicationAndCollaborationGrade"
              clearable
              placeholder="请输入沟通合作评分"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="增值服务评分"
            label-width="140px"
            prop="appreciationServiceGrade"
          >
            <el-input
              v-model="formData.appreciationServiceGrade"
              clearable
              placeholder="请输入增值服务评分"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item
            label="考核结果"
            label-width="140px"
            prop="examineGrade"
          >
            <el-input
              v-model="formData.examineGrade"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入考核结果"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="其他意见或建议"
            label-width="140px"
            prop="proposal"
          >
            <el-input
              v-model="formData.proposal"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入其他意见或建议"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { fetchApi, legalServiceGrade } from '@/api/fwgl/api'

  const { saveOrUpdate, detail } = legalServiceGrade

  export default {
    name: 'scoreView',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        formData: {},
        footer: true,
        rules: {
          businessPremisesName: [
            {
              required: true,
              message: '请输入事务所名称',
              trigger: 'blur',
            },
          ],
          serviceQualityGrade: [
            {
              required: true,
              message: '请输入服务质量评分',
              trigger: 'blur',
            },
          ],
          communicationAndCollaborationGrade: [
            {
              required: true,
              message: '请输入沟通合作评分',
              trigger: 'blur',
            },
          ],
          appreciationServiceGrade: [
            {
              required: true,
              message: '请输入增值服务评分',
              trigger: 'blur',
            },
          ],
          examineGrade: [
            {
              required: true,
              message: '请输入考核结果',
              trigger: 'blur',
            },
          ],
          proposal: [
            {
              required: true,
              message: '请输入其他意见或建议',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data
          }
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.loading = true
        // this.formData.sex = Number(this.formData.sex)
        const res = await fetchApi(saveOrUpdate, this.formData)
        this.loading = false

        if (res && res.code === 200) {
          this.$baseMessage('保存成功', 'success')
          if (res.data) {
            this.$emit('on-save-success', { key: 'gradeId', rowItem: res.data })
          }
        } else {
          this.$baseMessage('操作失败！', 'error')
        }
        this.close()
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
</style>
