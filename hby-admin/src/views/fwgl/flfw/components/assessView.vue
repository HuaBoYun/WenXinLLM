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
          <el-form-item label="团队名称" label-width="140px" prop="teamName">
            <el-input
              v-model="formData.teamName"
              clearable
              placeholder="请输入团队名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="有无合作"
            label-width="140px"
            prop="isCollaboration"
          >
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isCollaboration"
              placeholder="有无合作"
              :disabled="!footer"
            >
              <el-option label="提供服务" value="提供服务" />
              <el-option label="参与报价" value="参与报价" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="委托主体"
            label-width="140px"
            prop="entrustSubject"
          >
            <el-input
              v-model="formData.entrustSubject"
              clearable
              placeholder="请输入委托主体"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="服务项目"
            label-width="140px"
            prop="serviceProject"
          >
            <el-select
              v-model="formData.serviceProject"
              :style="{ width: '100%' }"
              placeholder="请选择服务项目"
              :disabled="!footer"
            >
              <el-option label="基金及股权投资" :value="1" />
              <el-option label="债权投资（含融资租赁等业务）" :value="2" />
              <el-option label="不良资产投资" :value="3" />
              <el-option
                label="专业产业投资（含盐业及不动产领域等）"
                :value="4"
              />
              <el-option label="诉讼代理" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="专业能力（60分）"
            label-width="140px"
            prop="professionalAbilityGrade"
          >
            <el-input
              v-model="formData.professionalAbilityGrade"
              clearable
              type="number"
              placeholder="请输入专业能力（60分）"
              @input="handleNumberRules('professionalAbilityGrade', 60)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="响应效率（30分）"
            label-width="140px"
            prop="reactionEfficiencyGrade"
          >
            <el-input
              v-model="formData.reactionEfficiencyGrade"
              clearable
              type="number"
              placeholder="请输入响应效率（30分）"
              @input="handleNumberRules('reactionEfficiencyGrade', 30)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="增值服务（10分）"
            label-width="140px"
            prop="appreciationServiceGrade"
          >
            <el-input
              v-model="formData.appreciationServiceGrade"
              clearable
              type="number"
              placeholder="请输入增值服务（10分）"
              @input="handleNumberRules('appreciationServiceGrade', 10)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合计" label-width="140px" prop="total">
            <el-input
              v-model="formData.total"
              clearable
              placeholder="自动计算"
              type="number"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="平均分" label-width="140px" prop="average">
            <el-input
              v-model="formData.average"
              clearable
              placeholder="自动计算"
              type="number"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" label-width="140px" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入备注"
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
  import { legalServiceExamine, fetchApi } from '@/api/fwgl/api'
  const { saveOrUpdate, detail } = legalServiceExamine

  export default {
    name: 'SummanyInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          teamName: '',
          isCollaboration: '',
          entrustSubject: '',
          serviceProject: '',
          professionalAbilityGrade: 0,
          reactionEfficiencyGrade: 0,
          appreciationServiceGrade: 0,
          total: 0,
          average: 0,
          remark: '',
        },
        footer: true,
        rules: {
          teamName: [
            {
              required: true,
              message: '请输入团队名称',
              trigger: 'blur',
            },
          ],
          isCollaboration: [
            {
              required: true,
              message: '有无合作',
              trigger: 'blur',
            },
          ],
          entrustSubject: [
            {
              required: true,
              message: '请输入委托主体',
              trigger: 'blur',
            },
          ],
          serviceProject: [
            {
              required: true,
              message: '请输入服务项目',
              trigger: 'blur',
            },
          ],
          professionalAbilityGrade: [
            {
              required: true,
              message: '请输入分数',
              trigger: 'blur',
            },
          ],
          reactionEfficiencyGrade: [
            {
              required: true,
              message: '请输入分数',
              trigger: 'blur',
            },
          ],
          appreciationServiceGrade: [
            {
              required: true,
              message: '请输入分数',
              trigger: 'blur',
            },
          ],
          remark: [
            {
              required: true,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {
      'formData.professionalAbilityGrade': {
        handler(val) {
          this.countTotal()
        },
      },
      'formData.reactionEfficiencyGrade': {
        handler(val) {
          this.countTotal()
        },
      },
      'formData.appreciationServiceGrade': {
        handler(val) {
          this.countTotal()
        },
      },
    },
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

        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data
            this.formData.serviceProject = +res.data.serviceProject
          }
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.$nextTick(() => {
            this.formData.teamName = parentData.serviceTeamName
            this.formData.entrustSubject = parentData.workUnitName
            this.formData.serviceProject = parentData.serviceItemType
          })
        }
      },
      // 限制大小
      handleNumberRules(key, max) {
        if (this.formData[key] > max) {
          this.formData[key] = max
        } else if (this.formData[key] < 0) {
          this.formData[key] = 0
        }
      },
      /**
       * @description: 计算平均值
       * @return {*}
       */      
      countTotal() {
        this.formData.total =
          (Number(this.formData.professionalAbilityGrade) || 0) +
          (Number(this.formData.reactionEfficiencyGrade) || 0) +
          (Number(this.formData.appreciationServiceGrade) || 0)
        this.formData.average = (this.formData.total / 3).toFixed(2)
      },
      /**
       * @description: 关闭弹框清楚缓存数据
       * @return {*}
       */      
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
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            if (
              Number(this.formData.professionalAbilityGrade) <= 0 ||
              Number(this.formData.reactionEfficiencyGrade) <= 0 ||
              Number(this.formData.appreciationServiceGrade) <= 0
            ) {
              return this.$message({ type: 'error', message: '分数不能为0' })
            }
            this.loading = true
            const res = await fetchApi(saveOrUpdate, this.formData)
            this.loading = false

            if (res && res.code === 200) {
              this.$baseMessage('保存成功', 'success')
              if (res.data) {
                this.$emit('on-save-success', {
                  key: 'examineId',
                  rowItem: res.data,
                })
              }
            } else {
              this.$baseMessage('操作失败！', 'error')
            }
            this.close()
          }
        })
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
