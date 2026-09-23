<template>
  <el-dialog
    title="财务报表质量检查"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-if="processData">
      <!-- 质量检查概览 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>质量检查概览</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="startQualityCheck">
            开始检查
          </el-button>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="总体评分" :value="overallScore" suffix="分">
              <template slot="prefix">
                <i class="el-icon-s-data" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="检查项目" :value="checkItems.length" suffix="项">
              <template slot="prefix">
                <i class="el-icon-s-order" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="通过项目" :value="passedItems" suffix="项">
              <template slot="prefix">
                <i class="el-icon-success" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="问题项目" :value="failedItems" suffix="项">
              <template slot="prefix">
                <i class="el-icon-error" style="color: #F56C6C"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </el-card>

      <!-- 检查项目列表 -->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>检查项目详情</span>
        </div>
        <el-table :data="checkItems" border style="width: 100%">
          <el-table-column prop="category" label="检查类别" width="120">
            <template slot-scope="scope">
              <el-tag :type="getCategoryType(scope.row.category)">
                {{ scope.row.category }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="itemName" label="检查项目" min-width="200" />
          <el-table-column prop="description" label="检查内容" min-width="250" />
          <el-table-column prop="status" label="检查状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="得分" width="80">
            <template slot-scope="scope">
              <span :style="{ color: scope.row.score >= 80 ? '#67C23A' : scope.row.score >= 60 ? '#E6A23C' : '#F56C6C' }">
                {{ scope.row.score }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="issues" label="发现问题" min-width="200">
            <template slot-scope="scope">
              <span v-if="scope.row.issues" style="color: #F56C6C">{{ scope.row.issues }}</span>
              <span v-else style="color: #67C23A">无问题</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
              <el-button size="mini" type="primary" @click="recheck(scope.row)" v-if="scope.row.status === '未通过'">
                重检
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 质量检查报告 -->
      <el-card class="box-card" style="margin-top: 20px;">
        <div slot="header" class="clearfix">
          <span>质量检查报告</span>
        </div>
        <el-form :model="reportForm" label-width="120px">
          <el-form-item label="检查结论">
            <el-radio-group v-model="reportForm.conclusion">
              <el-radio label="合格">质量合格</el-radio>
              <el-radio label="基本合格">基本合格</el-radio>
              <el-radio label="不合格">质量不合格</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="主要问题">
            <el-input
              v-model="reportForm.mainIssues"
              type="textarea"
              :rows="3"
              placeholder="请描述发现的主要问题"
            />
          </el-form-item>
          <el-form-item label="改进建议">
            <el-input
              v-model="reportForm.improvements"
              type="textarea"
              :rows="3"
              placeholder="请提出改进建议"
            />
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="generateReport" :loading="loading">生成报告</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'StatementQualityCheckDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    processData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      checkItems: [
        {
          category: '准确性',
          itemName: '数据计算准确性',
          description: '检查财务数据计算是否准确，公式是否正确',
          status: '已通过',
          score: 95,
          issues: ''
        },
        {
          category: '完整性',
          itemName: '报表项目完整性',
          description: '检查报表项目是否完整，无遗漏',
          status: '已通过',
          score: 90,
          issues: ''
        },
        {
          category: '及时性',
          itemName: '报送时间及时性',
          description: '检查报表是否按时完成和报送',
          status: '未通过',
          score: 60,
          issues: '报送时间延迟2天'
        },
        {
          category: '合规性',
          itemName: '会计准则合规性',
          description: '检查是否符合相关会计准则要求',
          status: '已通过',
          score: 88,
          issues: ''
        }
      ],
      reportForm: {
        conclusion: '合格',
        mainIssues: '',
        improvements: ''
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
    overallScore() {
      if (this.checkItems.length === 0) return 0
      const total = this.checkItems.reduce((sum, item) => sum + item.score, 0)
      return Math.round(total / this.checkItems.length)
    },
    passedItems() {
      return this.checkItems.filter(item => item.status === '已通过').length
    },
    failedItems() {
      return this.checkItems.filter(item => item.status === '未通过').length
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    startQualityCheck() {
      this.$message.info('开始质量检查...')
      // 这里应该调用API开始质量检查
    },
    viewDetail(row) {
      this.$message.info(`查看 ${row.itemName} 的详细信息`)
    },
    recheck(row) {
      this.$message.info(`重新检查 ${row.itemName}`)
      // 这里应该调用API重新检查
    },
    generateReport() {
      this.loading = true
      // 这里应该调用API生成质量检查报告
      setTimeout(() => {
        this.loading = false
        this.$message.success('质量检查报告生成成功')
        this.$emit('refresh')
      }, 1000)
    },
    getCategoryType(category) {
      const typeMap = {
        '准确性': 'primary',
        '完整性': 'success',
        '及时性': 'warning',
        '合规性': 'info'
      }
      return typeMap[category] || 'default'
    },
    getStatusType(status) {
      const typeMap = {
        '已通过': 'success',
        '未通过': 'danger',
        '检查中': 'warning'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
.el-statistic {
  text-align: center;
}
</style>
