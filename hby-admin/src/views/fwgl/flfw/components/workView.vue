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
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="法律服务名称" prop="lawServiceName">
            <el-input
              v-model="formData.lawServiceName"
              clearable
              placeholder="请输入法律服务名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务项目类型" prop="serviceProjectType">
            <el-select
              v-model="formData.serviceProjectType"
              :style="{ width: '100%' }"
              placeholder="请选择服务项目类型"
              :disabled="!footer"
            >
              <el-option label="基金及股权投资" value="基金及股权投资" />
              <el-option
                label="债权投资（含融资租赁等业务）"
                value="债权投资（含融资租赁等业务）"
              />
              <el-option label="不良资产投资" value="不良资产投资" />
              <el-option
                label="专业产业投资（含盐业及不动产领域等）"
                value="专业产业投资（含盐业及不动产领域等）"
              />
              <el-option label="诉讼代理" value="诉讼代理" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="金额" prop="money">
            <el-input
              v-model="formData.money"
              type="number"
              clearable
              placeholder="请输入涉及标的金额（万元）"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="承办律所" prop="undertakeLawOffice">
            <el-input
              v-model="formData.undertakeLawOffice"
              clearable
              placeholder="请输入承办律所"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主办律师" prop="hostLawOffice">
            <el-select
              v-model="formData.hostLawOffice"
              placeholder="请选择主办律师"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in lawyerIdList"
                :key="item.lawyerId"
                :label="item.lawyerName"
                :value="item.lawyerId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="服务效果及评价"
            prop="serviceEffectivenessOfEvaluate"
          >
            <el-input
              v-model="formData.serviceEffectivenessOfEvaluate"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入法律服务效果及评价"
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
  import { legalServiceWorkRecord, fetchApi } from '@/api/fwgl/api'

  const { saveOrUpdate, detail } = legalServiceWorkRecord

  export default {
    name: 'SummanyInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          lawServiceName: null,
          serviceProjectType: null,
          money: 0,
          undertakeLawOffice: null,
          hostLawOffice: null,
          serviceEffectivenessOfEvaluate: null,
        },
        lawyerIdList: [],
        footer: true,
        rules: {
          lawServiceName: [
            {
              required: true,
              message: '请输入法律服务名称',
              trigger: 'blur',
            },
          ],
          serviceProjectType: [
            {
              required: true,
              message: '请输入服务项目类型',
              trigger: 'blur',
            },
          ],
          money: [
            {
              required: true,
              message: '请输入涉及标的金额（万元）',
              trigger: 'blur',
            },
          ],
          undertakeLawOffice: [
            {
              required: true,
              message: '请输入承办律所',
              trigger: 'blur',
            },
          ],
          hostLawOffice: [
            {
              required: true,
              message: '请输入主办律师',
              trigger: 'blur',
            },
          ],
          serviceEffectivenessOfEvaluate: [
            {
              required: true,
              message: '请输入法律服务效果及评价',
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
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @param {*} parentData 其他数据
       * @return {*}
       */   
      async showEdit(title, row, parentData) {
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.formData.undertakeLawOffice = parentData.organizationName
          this.lawyerIdList = JSON.parse(
            JSON.stringify(parentData.lawyerIdList)
          )
        })
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            res.data.hostLawOffice = Number(res.data.hostLawOffice)
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
        this.formData.money = Number(this.formData.money)
        const res = await fetchApi(saveOrUpdate, this.formData)
        this.loading = false

        if (res && res.code === 200) {
          this.$baseMessage('保存成功', 'success')
          if (res.data) {
            this.$emit('on-save-success', {
              key: 'workRecordId',
              rowItem: res.data,
            })
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
