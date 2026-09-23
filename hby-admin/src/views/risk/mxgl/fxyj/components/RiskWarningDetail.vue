<template>
  <div class="risk-warning-detail">
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 基本信息 -->
      <el-tab-pane label="基本信息" name="basic">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="预警编码">
            {{ warningData.warningCode }}
          </el-descriptions-item>
          <el-descriptions-item label="预警类型">
            <el-tag :type="getWarningTypeTagType(warningData.warningType)">
              {{ getWarningTypeText(warningData.warningType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag :type="getWarningLevelTagType(warningData.warningLevel)">
              {{ getWarningLevelText(warningData.warningLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警状态">
            <el-tag :type="getWarningStatusTagType(warningData.warningStatus)">
              {{ getWarningStatusText(warningData.warningStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="企业名称">
            {{ warningData.companyName }}
          </el-descriptions-item>
          <el-descriptions-item label="模型ID">
            {{ warningData.modelId }}
          </el-descriptions-item>
          <el-descriptions-item label="预警值">
            {{ warningData.warningValue }}
          </el-descriptions-item>
          <el-descriptions-item label="阈值">
            {{ warningData.thresholdValue }}
          </el-descriptions-item>
          <el-descriptions-item label="预警时间" :span="2">
            {{ formatDate(warningData.warningTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="预警描述" :span="2">
            {{ warningData.warningDescription }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- 处理信息 -->
        <el-card header="处理信息" style="margin-top: 20px;" v-if="warningData.processUser">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="处理人">
              {{ warningData.processUser }}
            </el-descriptions-item>
            <el-descriptions-item label="处理时间">
              {{ formatDate(warningData.processTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="处理动作">
              {{ getProcessActionText(warningData.processAction) }}
            </el-descriptions-item>
            <el-descriptions-item label="是否误报">
              <el-tag :type="warningData.isFalsePositive === 'Y' ? 'warning' : 'success'">
                {{ warningData.isFalsePositive === 'Y' ? '是' : '否' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="处理说明" :span="2">
              {{ warningData.processNote || '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-tab-pane>

      <!-- 详细数据 -->
      <el-tab-pane label="详细数据" name="detail">
        <el-card header="预警详情">
          <div class="detail-content">
            <pre v-if="warningData.warningDetail">{{ formatJSON(warningData.warningDetail) }}</pre>
            <div v-else class="no-data">暂无详细数据</div>
          </div>
        </el-card>

        <el-card header="相关数据" style="margin-top: 20px;">
          <div class="detail-content">
            <pre v-if="warningData.relatedData">{{ formatJSON(warningData.relatedData) }}</pre>
            <div v-else class="no-data">暂无相关数据</div>
          </div>
        </el-card>
      </el-tab-pane>

      <!-- 处理历史 -->
      <el-tab-pane label="处理历史" name="history">
        <el-table :data="processHistory" border stripe>
          <el-table-column prop="processTime" label="处理时间" width="150">
            <template slot-scope="scope">
              {{ formatDate(scope.row.processTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="processUser" label="处理人" width="100" />
          <el-table-column prop="processAction" label="处理动作" width="120">
            <template slot-scope="scope">
              {{ getProcessActionText(scope.row.processAction) }}
            </template>
          </el-table-column>
          <el-table-column prop="processNote" label="处理说明" show-overflow-tooltip />
        </el-table>

        <div v-if="!processHistory.length" class="no-data">
          暂无处理历史
        </div>
      </el-tab-pane>

      <!-- 关联信息 -->
      <el-tab-pane label="关联信息" name="related">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card header="关联模型">
              <div v-if="relatedModel">
                <p><strong>模型名称：</strong>{{ relatedModel.modelName }}</p>
                <p><strong>模型类型：</strong>{{ relatedModel.modelType }}</p>
                <p><strong>模型版本：</strong>{{ relatedModel.modelVersion }}</p>
                <p><strong>准确率：</strong>{{ relatedModel.accuracy }}%</p>
              </div>
              <div v-else class="no-data">暂无关联模型信息</div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card header="关联企业">
              <div v-if="relatedCompany">
                <p><strong>企业名称：</strong>{{ relatedCompany.companyName }}</p>
                <p><strong>企业类型：</strong>{{ relatedCompany.companyType }}</p>
                <p><strong>行业分类：</strong>{{ relatedCompany.industryType }}</p>
                <p><strong>风险等级：</strong>{{ relatedCompany.riskLevel }}</p>
              </div>
              <div v-else class="no-data">暂无关联企业信息</div>
            </el-card>
          </el-col>
        </el-row>

        <el-card header="相似预警" style="margin-top: 20px;">
          <el-table :data="similarWarnings" border stripe>
            <el-table-column prop="warningCode" label="预警编码" width="140" />
            <el-table-column prop="warningType" label="预警类型" width="120" />
            <el-table-column prop="warningLevel" label="预警级别" width="100">
              <template slot-scope="scope">
                <el-tag :type="getWarningLevelTagType(scope.row.warningLevel)">
                  {{ getWarningLevelText(scope.row.warningLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="companyName" label="企业名称" show-overflow-tooltip />
            <el-table-column prop="warningTime" label="预警时间" width="150">
              <template slot-scope="scope">
                {{ formatDate(scope.row.warningTime) }}
              </template>
            </el-table-column>
          </el-table>

          <div v-if="!similarWarnings.length" class="no-data">
            暂无相似预警
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <div class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleProcess" v-if="canProcess">处理预警</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RiskWarningDetail',
  props: {
    warningData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      processHistory: [],
      relatedModel: null,
      relatedCompany: null,
      similarWarnings: []
    }
  },
  computed: {
    canProcess() {
      return this.warningData.warningStatus === 'PENDING'
    }
  },
  watch: {
    warningData: {
      handler(newVal) {
        if (newVal && newVal.warningId) {
          this.loadRelatedData()
        }
      },
      immediate: true
    }
  },
  methods: {
    // 加载关联数据
    loadRelatedData() {
      // 模拟加载处理历史
      this.processHistory = [
        {
          processTime: new Date(),
          processUser: 'admin',
          processAction: 'CONFIRM',
          processNote: '确认处理该预警'
        }
      ]

      // 模拟加载关联模型
      this.relatedModel = {
        modelName: '财务风险评估模型',
        modelType: 'FINANCIAL',
        modelVersion: '1.0',
        accuracy: 85.6
      }

      // 模拟加载关联企业
      this.relatedCompany = {
        companyName: '示例云科技有限公司',
        companyType: '有限责任公司',
        industryType: '软件和信息技术服务业',
        riskLevel: 'MEDIUM'
      }

      // 模拟加载相似预警
      this.similarWarnings = [
        {
          warningCode: 'WC001',
          warningType: 'FINANCIAL_RISK',
          warningLevel: 'HIGH',
          companyName: '测试企业A',
          warningTime: new Date()
        }
      ]
    },
    // 处理预警
    handleProcess() {
      this.$emit('process', this.warningData)
    },
    // 关闭
    handleClose() {
      this.$emit('close')
    },
    // 格式化JSON
    formatJSON(jsonStr) {
      try {
        const obj = typeof jsonStr === 'string' ? JSON.parse(jsonStr) : jsonStr
        return JSON.stringify(obj, null, 2)
      } catch (error) {
        return jsonStr
      }
    },
    // 获取预警类型标签类型
    getWarningTypeTagType(type) {
      const typeMap = {
        'FINANCIAL_RISK': 'success',
        'PROCUREMENT_RISK': 'primary',
        'CREDIT_RISK': 'warning',
        'COMPLIANCE_RISK': 'info'
      }
      return typeMap[type] || 'default'
    },
    // 获取预警类型文本
    getWarningTypeText(type) {
      const typeMap = {
        'FINANCIAL_RISK': '财务风险',
        'PROCUREMENT_RISK': '采购风险',
        'CREDIT_RISK': '信用风险',
        'COMPLIANCE_RISK': '合规风险'
      }
      return typeMap[type] || type
    },
    // 获取预警级别标签类型
    getWarningLevelTagType(level) {
      const levelMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return levelMap[level] || 'default'
    },
    // 获取预警级别文本
    getWarningLevelText(level) {
      const levelMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return levelMap[level] || level
    },
    // 获取预警状态标签类型
    getWarningStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'PROCESSED': 'success',
        'IGNORED': 'info'
      }
      return statusMap[status] || 'default'
    },
    // 获取预警状态文本
    getWarningStatusText(status) {
      const statusMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'PROCESSED': '已处理',
        'IGNORED': '已忽略'
      }
      return statusMap[status] || status
    },
    // 获取处理动作文本
    getProcessActionText(action) {
      const actionMap = {
        'CONFIRM': '确认处理',
        'IGNORE': '忽略预警',
        'ESCALATE': '升级预警',
        'FALSE_POSITIVE': '标记误报'
      }
      return actionMap[action] || action
    },
    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-warning-detail {
  .detail-content {
    max-height: 400px;
    overflow-y: auto;

    pre {
      background: #f5f5f5;
      padding: 15px;
      border-radius: 4px;
      font-size: 12px;
      line-height: 1.4;
      margin: 0;
    }
  }

  .no-data {
    text-align: center;
    color: #999;
    padding: 40px;
  }

  .dialog-footer {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .el-button {
      margin-left: 10px;
    }
  }
}
</style>
