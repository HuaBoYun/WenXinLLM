<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="evaluationForm"
      :model="evaluationForm"
      :rules="evaluationRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估名称" prop="evaluationName">
            <el-input
              v-model="evaluationForm.evaluationName"
              placeholder="请输入评估名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估类型" prop="evaluationType">
            <el-select
              v-model="evaluationForm.evaluationType"
              placeholder="请选择评估类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="年度评估" value="ANNUAL"></el-option>
              <el-option label="季度评估" value="QUARTERLY"></el-option>
              <el-option label="专项评估" value="SPECIAL"></el-option>
              <el-option label="日常评估" value="ROUTINE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估对象" prop="organizationId">
            <el-select
              v-model="evaluationForm.organizationId"
              placeholder="请选择评估对象"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option
                v-for="org in organizations"
                :key="org.id"
                :label="org.organizationName"
                :value="org.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估期间" prop="evaluationPeriod">
            <el-input
              v-model="evaluationForm.evaluationPeriod"
              placeholder="如：2025年第一季度"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估日期" prop="evaluationDate">
            <el-date-picker
              v-model="evaluationForm.evaluationDate"
              type="date"
              placeholder="选择评估日期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估人" prop="evaluator">
            <el-input
              v-model="evaluationForm.evaluator"
              placeholder="请输入评估人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="评估指标" prop="evaluationCriteria">
        <el-table :data="evaluationCriteria" border style="width: 100%">
          <el-table-column prop="criteriaName" label="指标名称" width="200">
            <template slot-scope="scope">
              <el-input
                v-if="dialogType !== 'view'"
                v-model="scope.row.criteriaName"
                placeholder="请输入指标名称"
                size="mini"
              />
              <span v-else>{{ scope.row.criteriaName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="weight" label="权重(%)" width="120" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-if="dialogType !== 'view'"
                v-model="scope.row.weight"
                :min="0"
                :max="100"
                size="mini"
                style="width: 100%"
              />
              <span v-else>{{ scope.row.weight }}%</span>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="得分" width="120" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-if="dialogType !== 'view'"
                v-model="scope.row.score"
                :min="0"
                :max="100"
                size="mini"
                style="width: 100%"
              />
              <span v-else>{{ scope.row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="评估说明" min-width="200">
            <template slot-scope="scope">
              <el-input
                v-if="dialogType !== 'view'"
                v-model="scope.row.description"
                placeholder="请输入评估说明"
                size="mini"
              />
              <span v-else>{{ scope.row.description }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center" v-if="dialogType !== 'view'">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="removeCriteria(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button
          v-if="dialogType !== 'view'"
          type="primary"
          size="mini"
          @click="addCriteria"
          style="margin-top: 10px"
        >
          添加指标
        </el-button>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总分" prop="totalScore">
            <el-input-number
              v-model="evaluationForm.totalScore"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估等级" prop="evaluationLevel">
            <el-select
              v-model="evaluationForm.evaluationLevel"
              placeholder="请选择评估等级"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="优秀" value="EXCELLENT"></el-option>
              <el-option label="良好" value="GOOD"></el-option>
              <el-option label="合格" value="QUALIFIED"></el-option>
              <el-option label="不合格" value="UNQUALIFIED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="评估总结" prop="summary">
        <el-input
          v-model="evaluationForm.summary"
          type="textarea"
          :rows="4"
          placeholder="请输入评估总结"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="存在问题" prop="issues">
        <el-input
          v-model="evaluationForm.issues"
          type="textarea"
          :rows="3"
          placeholder="请输入存在的问题"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="改进建议" prop="suggestions">
        <el-input
          v-model="evaluationForm.suggestions"
          type="textarea"
          :rows="3"
          placeholder="请输入改进建议"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="evaluationForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        @click="calculateTotalScore"
      >
        计算总分
      </el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { savePartyEvaluation } from '@/api/stateAssets/partyBuilding'

export default {
  name: 'PartyEvaluationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    evaluationData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      organizations: [], // 组织列表
      evaluationForm: {
        id: '',
        evaluationName: '',
        evaluationType: '',
        organizationId: '',
        evaluationPeriod: '',
        evaluationDate: '',
        evaluator: '',
        totalScore: 0,
        evaluationLevel: '',
        summary: '',
        issues: '',
        suggestions: '',
        remarks: ''
      },
      evaluationCriteria: [
        {
          criteriaName: '组织建设',
          weight: 25,
          score: 0,
          description: ''
        },
        {
          criteriaName: '制度执行',
          weight: 20,
          score: 0,
          description: ''
        },
        {
          criteriaName: '活动开展',
          weight: 25,
          score: 0,
          description: ''
        },
        {
          criteriaName: '党员管理',
          weight: 20,
          score: 0,
          description: ''
        },
        {
          criteriaName: '作用发挥',
          weight: 10,
          score: 0,
          description: ''
        }
      ],
      evaluationRules: {
        evaluationName: [
          { required: true, message: '请输入评估名称', trigger: 'blur' }
        ],
        evaluationType: [
          { required: true, message: '请选择评估类型', trigger: 'change' }
        ],
        organizationId: [
          { required: true, message: '请选择评估对象', trigger: 'change' }
        ],
        evaluationPeriod: [
          { required: true, message: '请输入评估期间', trigger: 'blur' }
        ],
        evaluationDate: [
          { required: true, message: '请选择评估日期', trigger: 'change' }
        ],
        evaluator: [
          { required: true, message: '请输入评估人', trigger: 'blur' }
        ],
        evaluationLevel: [
          { required: true, message: '请选择评估等级', trigger: 'change' }
        ],
        summary: [
          { required: true, message: '请输入评估总结', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        add: '新建党建评估',
        edit: '编辑党建评估',
        view: '查看党建评估'
      }
      return titleMap[this.dialogType] || '新建党建评估'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadOrganizations()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.evaluationForm = {
          id: '',
          evaluationName: '',
          evaluationType: '',
          organizationId: '',
          evaluationPeriod: '',
          evaluationDate: '',
          evaluator: '',
          totalScore: 0,
          evaluationLevel: '',
          summary: '',
          issues: '',
          suggestions: '',
          remarks: ''
        }
        this.evaluationCriteria = [
          {
            criteriaName: '组织建设',
            weight: 25,
            score: 0,
            description: ''
          },
          {
            criteriaName: '制度执行',
            weight: 20,
            score: 0,
            description: ''
          },
          {
            criteriaName: '活动开展',
            weight: 25,
            score: 0,
            description: ''
          },
          {
            criteriaName: '党员管理',
            weight: 20,
            score: 0,
            description: ''
          },
          {
            criteriaName: '作用发挥',
            weight: 10,
            score: 0,
            description: ''
          }
        ]
      } else {
        this.evaluationForm = { ...this.evaluationData }
        this.evaluationCriteria = this.evaluationData.criteria || this.evaluationCriteria
      }
      
      this.$nextTick(() => {
        if (this.$refs.evaluationForm) {
          this.$refs.evaluationForm.clearValidate()
        }
      })
    },

    loadOrganizations() {
      // 模拟加载组织数据
      this.organizations = [
        { id: '1', organizationName: '集团党委' },
        { id: '2', organizationName: '财务部党支部' },
        { id: '3', organizationName: '人力资源部党支部' },
        { id: '4', organizationName: '技术部党支部' }
      ]
    },

    addCriteria() {
      this.evaluationCriteria.push({
        criteriaName: '',
        weight: 0,
        score: 0,
        description: ''
      })
    },

    removeCriteria(index) {
      this.evaluationCriteria.splice(index, 1)
    },

    calculateTotalScore() {
      let totalScore = 0
      let totalWeight = 0
      
      this.evaluationCriteria.forEach(criteria => {
        if (criteria.weight && criteria.score) {
          totalScore += (criteria.weight / 100) * criteria.score
          totalWeight += criteria.weight
        }
      })
      
      if (totalWeight !== 100) {
        this.$message.warning('权重总和应为100%')
        return
      }
      
      this.evaluationForm.totalScore = Math.round(totalScore * 100) / 100
      
      // 根据总分自动设置评估等级
      if (this.evaluationForm.totalScore >= 90) {
        this.evaluationForm.evaluationLevel = 'EXCELLENT'
      } else if (this.evaluationForm.totalScore >= 80) {
        this.evaluationForm.evaluationLevel = 'GOOD'
      } else if (this.evaluationForm.totalScore >= 70) {
        this.evaluationForm.evaluationLevel = 'QUALIFIED'
      } else {
        this.evaluationForm.evaluationLevel = 'UNQUALIFIED'
      }
      
      this.$message.success('总分计算完成')
    },

    handleSubmit() {
      this.$refs.evaluationForm.validate((valid) => {
        if (valid) {
          // 验证权重总和
          const totalWeight = this.evaluationCriteria.reduce((sum, criteria) => sum + (criteria.weight || 0), 0)
          if (totalWeight !== 100) {
            this.$message.error('评估指标权重总和必须为100%')
            return
          }
          
          this.loading = true
          const formData = { ...this.evaluationForm }
          formData.criteria = this.evaluationCriteria
          
          savePartyEvaluation(formData).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存党建评估失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
