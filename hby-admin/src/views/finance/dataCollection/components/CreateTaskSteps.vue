<template>
  <el-dialog
    title="新建采集任务"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <!-- 步骤条 -->
    <el-steps :active="activeStep" finish-status="success" align-center style="margin-bottom: 30px">
      <el-step title="基本信息" />
      <el-step title="采集配置" />
      <el-step title="临时配置" />
      <el-step title="转化规则" />
      <el-step title="确认提交" />
    </el-steps>

    <!-- 步骤1: 基本信息 -->
    <div v-show="activeStep === 0">
      <el-form
        ref="step1FormRef"
        :model="formData"
        :rules="step1Rules"
        label-width="120px"
      >
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="formData.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务分类" prop="taskCategory">
          <el-radio-group v-model="formData.taskCategory">
            <el-radio label="FINANCE">财务数据</el-radio>
            <el-radio label="BUSINESS">业务数据</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="采集类型" prop="collectionType">
          <el-select v-model="formData.collectionType" placeholder="请选择采集类型">
            <el-option label="全量采集" value="FULL" />
            <el-option label="增量采集" value="INCREMENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
    </div>

    <!-- 步骤2: 采集配置 -->
    <div v-show="activeStep === 1">
      <el-form
        ref="step2FormRef"
        :model="formData"
        :rules="step2Rules"
        label-width="120px"
      >
        <el-form-item label="采集方案" prop="planId">
          <el-select v-model="formData.planId" placeholder="请选择采集方案">
            <el-option-group
              v-for="group in fversionList"
              :key="group.fid"
              :label="group.handtext"
            >
              <el-option
                v-for="item in group.childrenList"
                :key="item.fid"
                :label="item.handtext"
                :value="item.fid"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="数据源" prop="dataSourceId">
          <el-select v-model="formData.dataSourceId" placeholder="请选择数据源">
            <el-option
              v-for="item in dataSourceList"
              :key="item.fid"
              :label="item.fintext"
              :value="item.fid"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </div>

    <!-- 步骤3: 临时配置 -->
    <div v-show="activeStep === 2">
      <el-alert
        title="临时配置说明"
        type="info"
        description="可以临时修改字段映射和过滤条件,不影响原方案配置。配置后仅对当前采集任务生效。"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form label-width="120px">
        <el-form-item label="是否使用临时配置">
          <el-switch v-model="formData.useTempConfig" />
        </el-form-item>
        <template v-if="formData.useTempConfig">
          <el-form-item label="临时配置名称">
            <el-input v-model="formData.tempConfigName" placeholder="请输入临时配置名称" style="width: 400px" />
          </el-form-item>
          <el-form-item label="字段映射配置">
            <TempFieldMappingConfig ref="tempFieldMappingConfig" v-model="formData.fieldMappings" />
          </el-form-item>
        </template>
      </el-form>
    </div>

    <!-- 步骤4: 转化规则 -->
    <div v-show="activeStep === 3">
      <el-alert
        title="转化规则说明"
        type="info"
        description="配置采集完成后自动执行的转化规则,确保依赖数据及时生成。支持直接映射、条件转换、分组聚合等多种转化方式。"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form label-width="120px">
        <el-form-item label="是否自动转化">
          <el-switch v-model="formData.autoTransform" />
          <span style="margin-left: 10px; color: #909399; font-size: 12px">
            开启后,采集完成将自动执行配置的转化规则
          </span>
        </el-form-item>
        <template v-if="formData.autoTransform">
          <el-form-item label="错误处理策略">
            <el-radio-group v-model="formData.onError">
              <el-radio label="CONTINUE">继续执行(跳过错误规则)</el-radio>
              <el-radio label="STOP">停止执行(遇错即停)</el-radio>
              <el-radio label="ROLLBACK">回滚所有(撤销所有转化)</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="数据验证">
            <el-switch v-model="formData.validateData" />
            <span style="margin-left: 10px; color: #909399; font-size: 12px">
              开启后将验证转化后的数据完整性
            </span>
          </el-form-item>
          <el-form-item label="转化规则配置">
            <TransformRuleConfig ref="transformRuleConfig" v-model="formData.transformRules" />
          </el-form-item>
        </template>
      </el-form>
    </div>

    <!-- 步骤5: 确认提交 -->
    <div v-show="activeStep === 4">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务名称">{{ formData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="任务分类">
          {{ formData.taskCategory === 'FINANCE' ? '财务数据' : '业务数据' }}
        </el-descriptions-item>
        <el-descriptions-item label="采集类型">
          {{ formData.collectionType === 'FULL' ? '全量采集' : '增量采集' }}
        </el-descriptions-item>
        <el-descriptions-item label="采集方案">
          {{ getPlanName(formData.planId) }}
        </el-descriptions-item>
        <el-descriptions-item label="数据源">
          {{ getDataSourceName(formData.dataSourceId) }}
        </el-descriptions-item>
        <el-descriptions-item label="临时配置">
          {{ formData.useTempConfig ? '是' : '否' }}
        </el-descriptions-item>
        <el-descriptions-item label="自动转化">
          {{ formData.autoTransform ? '是' : '否' }}
        </el-descriptions-item>
        <el-descriptions-item label="转化规则数量">
          {{ formData.transformRules.length }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ formData.remark || '无' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button v-if="activeStep > 0" @click="handlePrevStep">上一步</el-button>
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="activeStep < 4" type="primary" @click="handleNextStep">下一步</el-button>
      <el-button v-if="activeStep === 4" type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { startCollection } from '@/api/finance/dataCollection'
import { getCwbbxxList, getDataSourceList } from '@/api/cwsc'
import TempFieldMappingConfig from './TempFieldMappingConfig.vue'
import TransformRuleConfig from './TransformRuleConfig.vue'

export default {
  name: 'CreateTaskSteps',
  components: {
    TempFieldMappingConfig,
    TransformRuleConfig
  },
  data() {
    return {
      dialogVisible: false,
      activeStep: 0,
      submitting: false,
      formData: {
        taskName: '',
        taskCategory: 'FINANCE',
        collectionType: 'FULL',
        planId: '',
        dataSourceId: '',
        remark: '',
        // 步骤3: 临时配置
        useTempConfig: false,
        tempConfigName: '',
        fieldMappings: [],
        // 步骤4: 转化规则
        autoTransform: false,
        onError: 'CONTINUE',
        validateData: true,
        transformRules: []
      },
      step1Rules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        taskCategory: [
          { required: true, message: '请选择任务分类', trigger: 'change' }
        ],
        collectionType: [
          { required: true, message: '请选择采集类型', trigger: 'change' }
        ]
      },
      step2Rules: {
        planId: [
          { required: true, message: '请选择采集方案', trigger: 'change' }
        ],
        dataSourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ]
      },
      fversionList: [],
      dataSourceList: []
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      this.activeStep = 0
      this.loadFVersionList()
      this.loadDataSourceList()
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.activeStep = 0
      this.formData = {
        taskName: '',
        taskCategory: 'FINANCE',
        collectionType: 'FULL',
        planId: '',
        dataSourceId: '',
        remark: '',
        useTempConfig: false,
        tempConfigName: '',
        fieldMappings: [],
        autoTransform: false,
        onError: 'CONTINUE',
        validateData: true,
        transformRules: []
      }
      this.$refs.step1FormRef && this.$refs.step1FormRef.clearValidate()
      this.$refs.step2FormRef && this.$refs.step2FormRef.clearValidate()
    },
    handlePrevStep() {
      if (this.activeStep > 0) {
        this.activeStep--
      }
    },
    handleNextStep() {
      if (this.activeStep === 0) {
        this.$refs.step1FormRef.validate(valid => {
          if (valid) {
            this.activeStep++
          }
        })
      } else if (this.activeStep === 1) {
        this.$refs.step2FormRef.validate(valid => {
          if (valid) {
            this.activeStep++
          }
        })
      } else if (this.activeStep === 2) {
        // 步骤3: 临时配置验证
        if (this.formData.useTempConfig) {
          if (!this.formData.tempConfigName) {
            this.$message.warning('请输入临时配置名称')
            return
          }
          if (this.$refs.tempFieldMappingConfig && !this.$refs.tempFieldMappingConfig.validate()) {
            return
          }
        }
        this.activeStep++
      } else if (this.activeStep === 3) {
        // 步骤4: 转化规则验证
        if (this.formData.autoTransform) {
          if (this.formData.transformRules.length === 0) {
            this.$message.warning('请至少添加一条转化规则')
            return
          }
          if (this.$refs.transformRuleConfig && !this.$refs.transformRuleConfig.validate()) {
            return
          }
        }
        this.activeStep++
      }
    },
    async handleSubmit() {
      this.submitting = true
      try {
        const res = await startCollection(this.formData)
        if (res.code === 1) {
          this.$message.success('采集任务创建成功')
          this.handleClose()
          this.$emit('refresh')
        } else {
          this.$message.error(res.msg || '创建失败')
        }
      } catch (error) {
        this.$message.error('创建失败')
        console.error(error)
      } finally {
        this.submitting = false
      }
    },
    async loadFVersionList() {
      try {
        // 加载所有采集方案列表
        const res = await getCwbbxxList({})
        if (res.code === 1 && res.data) {
          // 显示所有有子节点的采集方案(不再根据任务分类过滤)
          this.fversionList = res.data.filter(item =>
            item.childrenList && item.childrenList.length > 0
          )
        }
      } catch (error) {
        console.error('加载财务版本列表失败', error)
      }
    },
    async loadDataSourceList() {
      try {
        const res = await getDataSourceList({})
        if (res.code === 1 && res.data) {
          this.dataSourceList = res.data.records || res.data || []
        }
      } catch (error) {
        console.error('加载数据源列表失败', error)
      }
    },
    getPlanName(planId) {
      for (const group of this.fversionList) {
        const plan = group.childrenList.find(item => item.fid === planId)
        if (plan) {
          return plan.handtext
        }
      }
      return planId
    },
    getDataSourceName(dataSourceId) {
      const dataSource = this.dataSourceList.find(item => item.fid === dataSourceId)
      return dataSource ? dataSource.fintext : dataSourceId
    }
  }
}
</script>

<style scoped>
.el-steps {
  margin-bottom: 30px;
}
</style>

