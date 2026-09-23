<template>
  <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :before-close="handleClose" :close-on-click-modal="false">
    <!-- VIEW mode -->
    <div v-if="dialogType === 'view'">
      <el-divider content-position="left">基本信息</el-divider>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="企业名称">{{ formData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="控制方">{{ formData.controllerName }}</el-descriptions-item>
        <el-descriptions-item label="控制类型">
          <el-tag :type="{ DIRECT:'success', INDIRECT:'primary', MIXED:'warning', PROXY:'danger' }[formData.controlType]" size="small">
            {{ { DIRECT:'直接控制', INDIRECT:'间接控制', MIXED:'混合控制', PROXY:'代理控制' }[formData.controlType] }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="控制强度">
          <el-progress :percentage="formData.controlStrength" :color="formData.controlStrength >= 80 ? '#f56c6c' : formData.controlStrength >= 60 ? '#e6a23c' : '#67c23a'" style="width:120px;display:inline-block" />
          <span style="margin-left:8px">{{ formData.controlStrength }}%</span>
        </el-descriptions-item>
        <el-descriptions-item label="链路长度">
          <el-tag size="small" :type="formData.chainLength > 5 ? 'danger' : 'primary'">{{ formData.chainLength }}级</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="节点数量">{{ formData.nodeCount }}</el-descriptions-item>
        <el-descriptions-item label="稳定性">
          <el-tag :type="{ HIGH:'success', MEDIUM:'primary', LOW:'warning', UNSTABLE:'danger' }[formData.stabilityLevel]" size="small">
            {{ { HIGH:'高稳定', MEDIUM:'中稳定', LOW:'低稳定', UNSTABLE:'不稳定' }[formData.stabilityLevel] }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="环路检测">
          <el-tag :type="formData.hasLoop ? 'danger' : 'success'" size="small">{{ formData.hasLoop ? '存在环路' : '无环路' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="{ LOW:'success', MEDIUM:'warning', HIGH:'danger' }[formData.riskLevel]" size="small">
            {{ { LOW:'低风险', MEDIUM:'中风险', HIGH:'高风险' }[formData.riskLevel] }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最后分析时间">{{ formData.lastAnalysisTime }}</el-descriptions-item>
      </el-descriptions>

      <!-- 控制链路径 -->
      <el-divider content-position="left">控制链路径</el-divider>
      <el-steps v-if="pathSteps.length" direction="vertical" :active="pathSteps.length" finish-status="success" style="padding:0 20px">
        <el-step v-for="(node, i) in pathSteps" :key="i" :title="node.name" :description="node.isStart ? '起始节点' : node.isEnd ? '终端节点' : '中间节点'" />
      </el-steps>
      <el-empty v-else description="暂无路径数据" :image-size="60" />

      <!-- 风险提示 -->
      <el-divider content-position="left">风险提示</el-divider>
      <el-alert v-for="(r, i) in riskTips" :key="i" :title="r" type="warning" show-icon :closable="false" style="margin-bottom:8px" />
      <el-alert v-if="!riskTips.length" title="当前控制链无明显风险" type="success" show-icon :closable="false" />
    </div>

    <!-- ADD/EDIT mode -->
    <el-form v-else ref="form" :model="form" :rules="rules" label-width="110px" size="small">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="form.enterpriseName" placeholder="请输入被控制企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制方" prop="controllerName">
            <el-input v-model="form.controllerName" placeholder="请输入控制方名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="控制类型" prop="controlType">
            <el-select v-model="form.controlType" placeholder="请选择" style="width:100%">
              <el-option label="直接控制" value="DIRECT" />
              <el-option label="间接控制" value="INDIRECT" />
              <el-option label="混合控制" value="MIXED" />
              <el-option label="代理控制" value="PROXY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制强度(%)" prop="controlStrength">
            <el-input-number v-model="form.controlStrength" :min="0" :max="100" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="链路长度" prop="chainLength">
            <el-input-number v-model="form.chainLength" :min="1" :max="20" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="节点数量" prop="nodeCount">
            <el-input-number v-model="form.nodeCount" :min="2" :max="30" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="稳定性" prop="stabilityLevel">
            <el-select v-model="form.stabilityLevel" placeholder="请选择" style="width:100%">
              <el-option label="高稳定" value="HIGH" />
              <el-option label="中稳定" value="MEDIUM" />
              <el-option label="低稳定" value="LOW" />
              <el-option label="不稳定" value="UNSTABLE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="form.riskLevel" placeholder="请选择" style="width:100%">
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否有环路">
            <el-radio-group v-model="form.hasLoop">
              <el-radio :label="0">无</el-radio>
              <el-radio :label="1">有</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-select v-model="form.status" placeholder="请选择" style="width:100%">
              <el-option label="有效" value="ACTIVE" />
              <el-option label="无效" value="INACTIVE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="控制路径" prop="chainPath">
        <el-input v-model="form.chainPath" placeholder="如: 国务院国资委->示例集团->国华物流" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="handleClose">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="loading" @click="handleSubmit">
        {{ dialogType === 'add' ? '新增' : '更新' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addControlChain, updateControlChain } from '@/api/stateAssets/controlChain'

export default {
  name: 'ControlChainDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'view' }
  },
  data() {
    return {
      loading: false,
      form: {
        chainId: null,
        enterpriseId: '',
        enterpriseName: '',
        controllerName: '',
        controllerId: '',
        controlType: '',
        controlStrength: 0,
        chainLength: 1,
        nodeCount: 2,
        stabilityLevel: '',
        hasLoop: 0,
        riskLevel: '',
        chainPath: '',
        status: 'ACTIVE',
        remark: ''
      },
      rules: {
        enterpriseName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        controllerName: [{ required: true, message: '请输入控制方名称', trigger: 'blur' }],
        controlType: [{ required: true, message: '请选择控制类型', trigger: 'change' }],
        controlStrength: [{ required: true, message: '请输入控制强度', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      return { add: '新增控制链分析', edit: '编辑控制链分析', view: '控制链详情' }[this.dialogType] || '控制链详情'
    },
    pathSteps() {
      const path = this.formData.chainPath || ''
      if (!path) return []
      return path.split('->').map((name, i, arr) => ({
        name: name.trim(),
        isStart: i === 0,
        isEnd: i === arr.length - 1
      }))
    },
    riskTips() {
      if (!this.formData) return []
      const tips = []
      if (this.formData.hasLoop) tips.push('检测到控制环路，可能存在交叉持股或循环控制风险')
      if (this.formData.controlStrength < 50) tips.push(`控制强度仅${this.formData.controlStrength}%，低于50%安全线，存在失控风险`)
      if (this.formData.chainLength > 5) tips.push(`控制链路长度${this.formData.chainLength}级，超过5级预警阈值，管理穿透力不足`)
      if (this.formData.stabilityLevel === 'UNSTABLE') tips.push('控制稳定性为"不稳定"，需重点关注股权变动')
      return tips
    }
  },
  watch: {
    visible(val) {
      if (val && this.dialogType === 'edit' && this.formData) {
        this.form = { ...this.formData }
      } else if (val && this.dialogType === 'add') {
        this.resetForm()
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (!valid) return
        this.loading = true
        const apiCall = this.dialogType === 'add' ? addControlChain : updateControlChain
        const submitData = { ...this.form }
        if (this.dialogType === 'add') {
          submitData.chainId = null
        }
        apiCall(submitData).then((response) => {
          if (response && response.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.warning(response?.msg || '操作失败')
          }
        }).catch((error) => {
          console.error('提交失败:', error)
          this.$message.error('请求失败')
        }).finally(() => {
          this.loading = false
        })
      })
    },
    handleClose() {
      this.dialogVisible = false
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    resetForm() {
      this.form = {
        chainId: null,
        enterpriseId: '',
        enterpriseName: '',
        controllerName: '',
        controllerId: '',
        controlType: '',
        controlStrength: 0,
        chainLength: 1,
        nodeCount: 2,
        stabilityLevel: '',
        hasLoop: 0,
        riskLevel: '',
        chainPath: '',
        status: 'ACTIVE',
        remark: ''
      }
      if (this.$refs.form) {
        this.$nextTick(() => { this.$refs.form.clearValidate() })
      }
    }
  }
}
</script>