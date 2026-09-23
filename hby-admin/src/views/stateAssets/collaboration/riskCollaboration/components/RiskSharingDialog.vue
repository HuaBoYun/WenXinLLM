<template>
  <el-dialog
    title="风险信息共享"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="risk-sharing-container">
      <!-- 风险基本信息 -->
      <el-card class="risk-info-card">
        <div slot="header">
          <span>风险基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">企业名称：</span>
              <span class="info-value">{{ riskData.enterpriseName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">风险名称：</span>
              <span class="info-value">{{ riskData.riskName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">风险类型：</span>
              <el-tag :type="getRiskTypeTagType(riskData.riskType)">
                {{ getRiskTypeText(riskData.riskType) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">风险等级：</span>
              <el-tag :type="getRiskLevelTagType(riskData.riskLevel)">
                {{ getRiskLevelText(riskData.riskLevel) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <div class="info-item">
          <span class="info-label">风险描述：</span>
          <div class="info-content">{{ riskData.riskDescription }}</div>
        </div>
      </el-card>
      
      <!-- 共享配置 -->
      <el-card class="sharing-config-card">
        <div slot="header">
          <span>共享配置</span>
        </div>
        <el-form ref="sharingForm" :model="sharingData" :rules="sharingRules" label-width="120px">
          <el-form-item label="共享范围" prop="sharingScope">
            <el-checkbox-group v-model="sharingData.sharingScope">
              <el-checkbox label="internal_departments">内部部门</el-checkbox>
              <el-checkbox label="subsidiary_companies">下属企业</el-checkbox>
              <el-checkbox label="regulatory_agencies">监管机构</el-checkbox>
              <el-checkbox label="industry_associations">行业协会</el-checkbox>
              <el-checkbox label="external_partners">外部合作伙伴</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="共享级别" prop="sharingLevel">
            <el-radio-group v-model="sharingData.sharingLevel">
              <el-radio label="public">公开</el-radio>
              <el-radio label="restricted">限制</el-radio>
              <el-radio label="confidential">机密</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="共享内容" prop="sharingContent">
            <el-checkbox-group v-model="sharingData.sharingContent">
              <el-checkbox label="risk_description">风险描述</el-checkbox>
              <el-checkbox label="impact_analysis">影响分析</el-checkbox>
              <el-checkbox label="countermeasures">应对措施</el-checkbox>
              <el-checkbox label="lessons_learned">经验教训</el-checkbox>
              <el-checkbox label="prevention_measures">预防措施</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="共享期限" prop="sharingDuration">
            <el-select v-model="sharingData.sharingDuration" placeholder="请选择共享期限" style="width: 100%">
              <el-option label="1个月" value="1_month"></el-option>
              <el-option label="3个月" value="3_months"></el-option>
              <el-option label="6个月" value="6_months"></el-option>
              <el-option label="1年" value="1_year"></el-option>
              <el-option label="永久" value="permanent"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="共享说明" prop="sharingDescription">
            <el-input
              v-model="sharingData.sharingDescription"
              type="textarea"
              :rows="4"
              placeholder="请输入共享说明">
            </el-input>
          </el-form-item>
        </el-form>
      </el-card>
      
      <!-- 接收方列表 -->
      <el-card class="recipients-card">
        <div slot="header">
          <span>接收方列表</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="addRecipient">添加接收方</el-button>
        </div>
        <el-table :data="recipients" style="width: 100%">
          <el-table-column prop="recipientName" label="接收方名称"></el-table-column>
          <el-table-column prop="recipientType" label="接收方类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getRecipientTypeTagType(scope.row.recipientType)">
                {{ getRecipientTypeText(scope.row.recipientType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="contactPerson" label="联系人" width="120"></el-table-column>
          <el-table-column prop="contactInfo" label="联系方式" width="150"></el-table-column>
          <el-table-column prop="sharingStatus" label="共享状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getSharingStatusTagType(scope.row.sharingStatus)">
                {{ getSharingStatusText(scope.row.sharingStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="editRecipient(scope.row)">编辑</el-button>
              <el-button size="mini" type="text" @click="removeRecipient(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <!-- 共享历史 -->
      <el-card class="history-card">
        <div slot="header">
          <span>共享历史</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(history, index) in sharingHistory"
            :key="index"
            :timestamp="history.timestamp"
            :type="getHistoryType(history.type)">
            <div class="history-content">
              <div class="history-title">{{ history.title }}</div>
              <div class="history-description">{{ history.description }}</div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleShare">确认共享</el-button>
      <el-button type="warning" @click="handleRevoke">撤销共享</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'RiskSharingDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    riskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      sharingData: {
        sharingScope: ['internal_departments'],
        sharingLevel: 'restricted',
        sharingContent: ['risk_description', 'countermeasures'],
        sharingDuration: '3_months',
        sharingDescription: ''
      },
      sharingRules: {
        sharingScope: [
          { required: true, message: '请选择共享范围', trigger: 'change' }
        ],
        sharingLevel: [
          { required: true, message: '请选择共享级别', trigger: 'change' }
        ],
        sharingContent: [
          { required: true, message: '请选择共享内容', trigger: 'change' }
        ],
        sharingDuration: [
          { required: true, message: '请选择共享期限', trigger: 'change' }
        ]
      },
      recipients: [
        {
          recipientName: '风险管理部',
          recipientType: 'internal_department',
          contactPerson: '李四',
          contactInfo: '13800138001',
          sharingStatus: 'shared'
        },
        {
          recipientName: '示例云分公司',
          recipientType: 'subsidiary_company',
          contactPerson: '王五',
          contactInfo: '13800138002',
          sharingStatus: 'pending'
        }
      ],
      sharingHistory: [
        {
          timestamp: '2024-01-15 10:00:00',
          type: 'share',
          title: '风险信息共享',
          description: '向风险管理部共享了市场风险信息'
        },
        {
          timestamp: '2024-01-14 15:30:00',
          type: 'create',
          title: '创建共享配置',
          description: '创建了风险信息共享配置'
        }
      ]
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
    // 获取风险类型标签类型
    getRiskTypeTagType(type) {
      const typeMap = {
        market_risk: 'danger',
        credit_risk: 'warning',
        operational_risk: 'primary',
        liquidity_risk: 'info',
        compliance_risk: 'success'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取风险类型文本
    getRiskTypeText(type) {
      const typeMap = {
        market_risk: '市场风险',
        credit_risk: '信用风险',
        operational_risk: '操作风险',
        liquidity_risk: '流动性风险',
        compliance_risk: '合规风险'
      }
      return typeMap[type] || type
    },
    
    // 获取风险等级标签类型
    getRiskLevelTagType(level) {
      const levelMap = {
        high: 'danger',
        medium: 'warning',
        low: 'success'
      }
      return levelMap[level] || 'default'
    },
    
    // 获取风险等级文本
    getRiskLevelText(level) {
      const levelMap = {
        high: '高风险',
        medium: '中风险',
        low: '低风险'
      }
      return levelMap[level] || level
    },
    
    // 获取接收方类型标签类型
    getRecipientTypeTagType(type) {
      const typeMap = {
        internal_department: 'primary',
        subsidiary_company: 'success',
        regulatory_agency: 'warning',
        industry_association: 'info',
        external_partner: 'default'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取接收方类型文本
    getRecipientTypeText(type) {
      const typeMap = {
        internal_department: '内部部门',
        subsidiary_company: '下属企业',
        regulatory_agency: '监管机构',
        industry_association: '行业协会',
        external_partner: '外部合作伙伴'
      }
      return typeMap[type] || type
    },
    
    // 获取共享状态标签类型
    getSharingStatusTagType(status) {
      const statusMap = {
        shared: 'success',
        pending: 'warning',
        failed: 'danger',
        revoked: 'info'
      }
      return statusMap[status] || 'default'
    },
    
    // 获取共享状态文本
    getSharingStatusText(status) {
      const statusMap = {
        shared: '已共享',
        pending: '待共享',
        failed: '共享失败',
        revoked: '已撤销'
      }
      return statusMap[status] || status
    },
    
    // 获取历史记录类型
    getHistoryType(type) {
      const typeMap = {
        share: 'success',
        revoke: 'warning',
        create: 'primary',
        update: 'info'
      }
      return typeMap[type] || 'info'
    },
    
    // 添加接收方
    addRecipient() {
      this.$prompt('请输入接收方名称', '添加接收方', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.recipients.push({
          recipientName: value,
          recipientType: 'internal_department',
          contactPerson: '',
          contactInfo: '',
          sharingStatus: 'pending'
        })
      })
    },
    
    // 编辑接收方
    editRecipient(recipient) {
      this.$message.info(`编辑接收方：${recipient.recipientName}`)
      // 这里应该打开编辑对话框
    },
    
    // 删除接收方
    removeRecipient(index) {
      this.$confirm('确认删除该接收方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.recipients.splice(index, 1)
        this.$message.success('删除成功')
      })
    },
    
    // 确认共享
    handleShare() {
      this.$refs.sharingForm.validate((valid) => {
        if (valid) {
          this.$message.success('风险信息共享成功')
          this.handleClose()
        }
      })
    },
    
    // 撤销共享
    handleRevoke() {
      this.$confirm('确认撤销风险信息共享吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('风险信息共享已撤销')
        this.handleClose()
      })
    },
    
    // 关闭
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.risk-sharing-container {
  max-height: 600px;
  overflow-y: auto;
}

.risk-info-card,
.sharing-config-card,
.recipients-card,
.history-card {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  font-weight: bold;
  color: #606266;
  display: inline-block;
  width: 100px;
}

.info-value {
  color: #303133;
}

.info-content {
  margin-top: 5px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  color: #303133;
  line-height: 1.5;
}

.history-content {
  padding-left: 10px;
}

.history-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.history-description {
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
