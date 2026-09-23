<template>
  <el-dialog
    title="绩效评估"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-tabs v-model="activeTab">
      <el-tab-pane label="评估信息" name="info">
        <el-form :model="form" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="评估对象">
                <el-input v-model="form.assessmentTarget"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="评估类型">
                <el-select v-model="form.assessmentType" placeholder="请选择评估类型">
                  <el-option label="月度评估" value="monthly"></el-option>
                  <el-option label="季度评估" value="quarterly"></el-option>
                  <el-option label="年度评估" value="annual"></el-option>
                  <el-option label="项目评估" value="project"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="评估周期">
                <el-date-picker
                  v-model="form.assessmentPeriod"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="评估人">
                <el-input v-model="form.assessor"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="评估说明">
            <el-input type="textarea" v-model="form.description" :rows="3"></el-input>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="评估指标" name="indicators">
        <el-table :data="form.indicators" border>
          <el-table-column prop="indicatorName" label="指标名称" width="200"></el-table-column>
          <el-table-column prop="weight" label="权重" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.weight }}%
            </template>
          </el-table-column>
          <el-table-column prop="targetValue" label="目标值" width="120" align="right"></el-table-column>
          <el-table-column prop="actualValue" label="实际值" width="120" align="right"></el-table-column>
          <el-table-column prop="score" label="得分" width="100" align="center">
            <template slot-scope="scope">
              <span :class="getScoreClass(scope.row.score)">
                {{ scope.row.score }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="completionRate" label="完成率" width="120" align="center">
            <template slot-scope="scope">
              <el-progress :percentage="scope.row.completionRate" :stroke-width="8"></el-progress>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="editIndicator(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="评估结果" name="result">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card>
              <el-statistic title="总得分" :value="form.totalScore">
                <template slot="prefix">
                  <i class="el-icon-medal" style="color: #409EFF"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-statistic title="评估等级" :value="form.rating">
                <template slot="prefix">
                  <i class="el-icon-star-on" style="color: #E6A23C"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-statistic title="完成率" :value="form.overallCompletionRate" suffix="%">
                <template slot="prefix">
                  <i class="el-icon-success" style="color: #67C23A"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
        </el-row>
        
        <el-card style="margin-top: 20px;">
          <div slot="header">
            <span>评估总结</span>
          </div>
          <div class="assessment-summary">
            <h4>优势方面：</h4>
            <p>{{ form.strengths }}</p>
            
            <h4>改进方面：</h4>
            <p>{{ form.improvements }}</p>
            
            <h4>建议措施：</h4>
            <p>{{ form.recommendations }}</p>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="saveAssessment">保存评估</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PerformanceAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assessmentData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'info',
      form: {
        assessmentTarget: '销售部门',
        assessmentType: 'quarterly',
        assessmentPeriod: ['2024-01-01', '2024-03-31'],
        assessor: '张三',
        description: '2024年第一季度销售部门绩效评估',
        totalScore: 85.6,
        rating: 'A',
        overallCompletionRate: 92.3,
        strengths: '销售目标完成率高，客户满意度提升明显，团队协作能力强。',
        improvements: '新客户开发力度不够，产品知识培训需要加强。',
        recommendations: '加强新客户开发培训，定期组织产品知识学习，建立客户关系管理制度。',
        indicators: [
          {
            indicatorName: '销售目标完成率',
            weight: 30,
            targetValue: 1000,
            actualValue: 1150,
            score: 95,
            completionRate: 115
          },
          {
            indicatorName: '客户满意度',
            weight: 25,
            targetValue: 90,
            actualValue: 88,
            score: 85,
            completionRate: 98
          },
          {
            indicatorName: '新客户开发数量',
            weight: 20,
            targetValue: 50,
            actualValue: 35,
            score: 70,
            completionRate: 70
          },
          {
            indicatorName: '团队协作评分',
            weight: 15,
            targetValue: 85,
            actualValue: 90,
            score: 95,
            completionRate: 106
          },
          {
            indicatorName: '培训完成率',
            weight: 10,
            targetValue: 100,
            actualValue: 95,
            score: 90,
            completionRate: 95
          }
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
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 70) return 'score-average'
      return 'score-poor'
    },
    editIndicator(row) {
      this.$message.info('编辑指标：' + row.indicatorName)
    },
    saveAssessment() {
      this.$message.success('保存绩效评估成功')
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.assessment-summary {
  line-height: 1.8;
}
.assessment-summary h4 {
  color: #409EFF;
  margin-top: 15px;
  margin-bottom: 8px;
}
.assessment-summary p {
  margin-bottom: 10px;
  text-indent: 2em;
}
.score-excellent {
  color: #67C23A;
  font-weight: bold;
}
.score-good {
  color: #409EFF;
  font-weight: bold;
}
.score-average {
  color: #E6A23C;
  font-weight: bold;
}
.score-poor {
  color: #F56C6C;
  font-weight: bold;
}
</style>
