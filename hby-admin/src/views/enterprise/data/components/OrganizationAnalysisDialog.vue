<template>
  <el-dialog
    title="组织架构分析"
    :visible.sync="dialogVisible"
    width="1200px"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="结构概览" name="overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="组织层级" :value="analysisData.levels || 0" suffix="级">
              <template slot="prefix">
                <i class="el-icon-s-grid" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="部门总数" :value="analysisData.departmentCount || 0" suffix="个">
              <template slot="prefix">
                <i class="el-icon-office-building" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="员工总数" :value="analysisData.employeeCount || 0" suffix="人">
              <template slot="prefix">
                <i class="el-icon-user" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="管理跨度" :value="analysisData.managementSpan || 0" suffix="人">
              <template slot="prefix">
                <i class="el-icon-s-custom" style="color: #F56C6C"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-divider content-position="left">层级分布</el-divider>
        <el-table :data="levelDistribution" border>
          <el-table-column prop="level" label="层级" width="100"></el-table-column>
          <el-table-column prop="departmentCount" label="部门数量" width="120"></el-table-column>
          <el-table-column prop="employeeCount" label="员工数量" width="120"></el-table-column>
          <el-table-column prop="avgSpan" label="平均管理跨度" width="150"></el-table-column>
          <el-table-column prop="efficiency" label="效率指数" width="120">
            <template slot-scope="scope">
              <el-progress
                :percentage="scope.row.efficiency"
                :color="getEfficiencyColor(scope.row.efficiency)"
                :stroke-width="8"
                text-inside
              ></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="suggestion" label="优化建议"></el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="部门分析" name="department">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="8">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索部门"
              clearable
              @keyup.enter.native="searchDepartment"
            >
              <el-button slot="append" icon="el-icon-search" @click="searchDepartment"></el-button>
            </el-input>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filterLevel" placeholder="选择层级" clearable>
              <el-option label="全部层级" value=""></el-option>
              <el-option label="一级部门" value="1"></el-option>
              <el-option label="二级部门" value="2"></el-option>
              <el-option label="三级部门" value="3"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="sortBy" placeholder="排序方式">
              <el-option label="按员工数量" value="employeeCount"></el-option>
              <el-option label="按效率指数" value="efficiency"></el-option>
              <el-option label="按成本" value="cost"></el-option>
            </el-select>
          </el-col>
        </el-row>
        
        <el-table :data="departmentAnalysis" border>
          <el-table-column prop="departmentName" label="部门名称" width="200"></el-table-column>
          <el-table-column prop="level" label="层级" width="80"></el-table-column>
          <el-table-column prop="employeeCount" label="员工数" width="100"></el-table-column>
          <el-table-column prop="subDepartments" label="下级部门" width="100"></el-table-column>
          <el-table-column prop="efficiency" label="效率指数" width="120">
            <template slot-scope="scope">
              <el-tag :type="getEfficiencyTag(scope.row.efficiency)">
                {{ scope.row.efficiency }}%
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="cost" label="运营成本" width="120">
            <template slot-scope="scope">
              {{ scope.row.cost }} 万元
            </template>
          </el-table-column>
          <el-table-column prop="manager" label="负责人" width="120"></el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewDepartmentDetail(scope.row)">
                详情
              </el-button>
              <el-button type="text" size="small" @click="optimizeDepartment(scope.row)">
                优化建议
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="人员分析" name="personnel">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="analysis-card">
              <h4>年龄结构</h4>
              <div ref="ageChart" style="height: 200px;"></div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="analysis-card">
              <h4>学历分布</h4>
              <div ref="educationChart" style="height: 200px;"></div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="analysis-card">
              <h4>职级分布</h4>
              <div ref="positionChart" style="height: 200px;"></div>
            </div>
          </el-col>
        </el-row>
        
        <el-divider content-position="left">关键指标</el-divider>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="平均年龄" :value="personnelData.avgAge || 0" suffix="岁">
              <template slot="prefix">
                <i class="el-icon-time" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="本科以上比例" :value="personnelData.degreeRatio || 0" suffix="%">
              <template slot="prefix">
                <i class="el-icon-reading" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="管理人员比例" :value="personnelData.managerRatio || 0" suffix="%">
              <template slot="prefix">
                <i class="el-icon-s-custom" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="人员流动率" :value="personnelData.turnoverRate || 0" suffix="%">
              <template slot="prefix">
                <i class="el-icon-refresh" style="color: #F56C6C"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <el-tab-pane label="优化建议" name="optimization">
        <el-alert
          title="组织架构优化建议"
          type="info"
          :closable="false"
          style="margin-bottom: 20px;"
        >
          <template slot="description">
            基于当前组织架构分析，系统为您提供以下优化建议
          </template>
        </el-alert>
        
        <el-timeline>
          <el-timeline-item
            v-for="(suggestion, index) in optimizationSuggestions"
            :key="index"
            :type="getSuggestionType(suggestion.priority)"
          >
            <el-card>
              <h4>{{ suggestion.title }}</h4>
              <p>{{ suggestion.description }}</p>
              <div style="margin-top: 10px;">
                <el-tag size="small" :type="getPriorityTag(suggestion.priority)">
                  {{ suggestion.priority }}优先级
                </el-tag>
                <span style="margin-left: 10px; color: #909399;">
                  预期效果：{{ suggestion.expectedEffect }}
                </span>
              </div>
              <div style="margin-top: 10px;">
                <el-button size="small" type="primary" @click="implementSuggestion(suggestion)">
                  采纳建议
                </el-button>
                <el-button size="small" @click="viewSuggestionDetail(suggestion)">
                  查看详情
                </el-button>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportAnalysis">导出分析报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'OrganizationAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'overview',
      searchKeyword: '',
      filterLevel: '',
      sortBy: 'employeeCount'
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
    levelDistribution() {
      return this.analysisData.levelDistribution || [
        { level: '一级', departmentCount: 5, employeeCount: 50, avgSpan: 10, efficiency: 85, suggestion: '适当增加管理层级' },
        { level: '二级', departmentCount: 15, employeeCount: 150, avgSpan: 10, efficiency: 78, suggestion: '优化部门设置' },
        { level: '三级', departmentCount: 30, employeeCount: 300, avgSpan: 10, efficiency: 72, suggestion: '减少管理层级' }
      ]
    },
    departmentAnalysis() {
      return this.analysisData.departmentAnalysis || [
        { departmentName: '技术部', level: 1, employeeCount: 50, subDepartments: 3, efficiency: 85, cost: 500, manager: '张三' },
        { departmentName: '市场部', level: 1, employeeCount: 30, subDepartments: 2, efficiency: 78, cost: 300, manager: '李四' }
      ]
    },
    personnelData() {
      return this.analysisData.personnelData || {
        avgAge: 32,
        degreeRatio: 75,
        managerRatio: 15,
        turnoverRate: 8
      }
    },
    optimizationSuggestions() {
      return this.analysisData.suggestions || [
        {
          title: '优化管理层级',
          description: '当前组织层级过多，建议减少中间管理层，提高决策效率',
          priority: '高',
          expectedEffect: '提升决策效率20%'
        },
        {
          title: '调整部门设置',
          description: '部分部门职能重叠，建议合并相似职能部门',
          priority: '中',
          expectedEffect: '降低运营成本15%'
        }
      ]
    }
  },
  methods: {
    getEfficiencyColor(efficiency) {
      if (efficiency >= 80) return '#67C23A'
      if (efficiency >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    getEfficiencyTag(efficiency) {
      if (efficiency >= 80) return 'success'
      if (efficiency >= 60) return 'warning'
      return 'danger'
    },
    getSuggestionType(priority) {
      const typeMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'info'
      }
      return typeMap[priority] || 'info'
    },
    getPriorityTag(priority) {
      const priorityMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'info'
      }
      return priorityMap[priority] || 'info'
    },
    searchDepartment() {
      this.$message.info('搜索部门：' + this.searchKeyword)
    },
    viewDepartmentDetail(row) {
      this.$message.info('查看部门详情：' + row.departmentName)
    },
    optimizeDepartment(row) {
      this.$message.info('优化建议：' + row.departmentName)
    },
    implementSuggestion(suggestion) {
      this.$message.success('建议已采纳：' + suggestion.title)
    },
    viewSuggestionDetail(suggestion) {
      this.$message.info('查看建议详情：' + suggestion.title)
    },
    exportAnalysis() {
      this.$message.success('分析报告导出中...')
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.analysis-card {
  padding: 20px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  height: 100%;
}

.analysis-card h4 {
  margin: 0 0 15px 0;
  color: #303133;
  text-align: center;
}
</style>
