<template>
  <el-dialog
    title="应急响应管理"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="emergency-response-container">
      <!-- 风险基本信息 -->
      <el-card class="risk-info-card">
        <div slot="header">
          <span>风险基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">企业名称：</span>
              <span class="info-value">{{ riskData.enterpriseName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">风险名称：</span>
              <span class="info-value">{{ riskData.riskName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">风险等级：</span>
              <el-tag :type="getRiskLevelTagType(riskData.riskLevel)">
                {{ getRiskLevelText(riskData.riskLevel) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 应急响应配置 -->
      <el-card class="response-config-card">
        <div slot="header">
          <span>应急响应配置</span>
        </div>
        <el-form ref="responseForm" :model="responseData" :rules="responseRules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="响应级别" prop="responseLevel">
                <el-select v-model="responseData.responseLevel" placeholder="请选择响应级别" style="width: 100%">
                  <el-option label="一级响应" value="level_1"></el-option>
                  <el-option label="二级响应" value="level_2"></el-option>
                  <el-option label="三级响应" value="level_3"></el-option>
                  <el-option label="四级响应" value="level_4"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="响应类型" prop="responseType">
                <el-select v-model="responseData.responseType" placeholder="请选择响应类型" style="width: 100%">
                  <el-option label="预防性响应" value="preventive"></el-option>
                  <el-option label="控制性响应" value="control"></el-option>
                  <el-option label="恢复性响应" value="recovery"></el-option>
                  <el-option label="补救性响应" value="remedial"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="启动时间" prop="startTime">
                <el-date-picker
                  v-model="responseData.startTime"
                  type="datetime"
                  placeholder="选择启动时间"
                  style="width: 100%">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预计结束时间" prop="expectedEndTime">
                <el-date-picker
                  v-model="responseData.expectedEndTime"
                  type="datetime"
                  placeholder="选择预计结束时间"
                  style="width: 100%">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="响应目标" prop="responseObjectives">
            <el-checkbox-group v-model="responseData.responseObjectives">
              <el-checkbox label="risk_control">风险控制</el-checkbox>
              <el-checkbox label="loss_minimization">损失最小化</el-checkbox>
              <el-checkbox label="business_continuity">业务连续性</el-checkbox>
              <el-checkbox label="stakeholder_protection">利益相关者保护</el-checkbox>
              <el-checkbox label="reputation_protection">声誉保护</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="响应措施" prop="responseMeasures">
            <el-input
              v-model="responseData.responseMeasures"
              type="textarea"
              :rows="4"
              placeholder="请输入具体的响应措施">
            </el-input>
          </el-form-item>
        </el-form>
      </el-card>
      
      <!-- 响应团队 -->
      <el-card class="response-team-card">
        <div slot="header">
          <span>响应团队</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="addTeamMember">添加成员</el-button>
        </div>
        <el-table :data="responseTeam" style="width: 100%">
          <el-table-column prop="memberName" label="姓名" width="120"></el-table-column>
          <el-table-column prop="role" label="角色" width="150">
            <template slot-scope="scope">
              <el-tag :type="getRoleTagType(scope.row.role)">
                {{ getRoleText(scope.row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="department" label="部门" width="150"></el-table-column>
          <el-table-column prop="contactInfo" label="联系方式" width="150"></el-table-column>
          <el-table-column prop="responsibilities" label="职责"></el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="editMember(scope.row)">编辑</el-button>
              <el-button size="mini" type="text" @click="removeMember(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <!-- 响应进度 -->
      <el-card class="response-progress-card">
        <div slot="header">
          <span>响应进度</span>
        </div>
        <el-steps :active="currentStep" finish-status="success" align-center>
          <el-step title="响应启动" description="启动应急响应机制"></el-step>
          <el-step title="措施执行" description="执行应急响应措施"></el-step>
          <el-step title="效果评估" description="评估响应效果"></el-step>
          <el-step title="响应结束" description="结束应急响应"></el-step>
        </el-steps>
        
        <div class="progress-details">
          <el-progress
            :percentage="responseProgress"
            :status="progressStatus"
            :stroke-width="15"
            text-inside>
          </el-progress>
          <div class="progress-info">
            <span>当前阶段：{{ currentStageText }}</span>
            <span class="progress-time">已用时间：{{ elapsedTime }}</span>
          </div>
        </div>
      </el-card>
      
      <!-- 响应记录 -->
      <el-card class="response-records-card">
        <div slot="header">
          <span>响应记录</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(record, index) in responseRecords"
            :key="index"
            :timestamp="record.timestamp"
            :type="getRecordType(record.type)">
            <div class="record-content">
              <div class="record-title">{{ record.title }}</div>
              <div class="record-description">{{ record.description }}</div>
              <div v-if="record.attachments" class="record-attachments">
                <span>附件：</span>
                <el-link
                  v-for="(attachment, idx) in record.attachments"
                  :key="idx"
                  type="primary"
                  @click="downloadAttachment(attachment)">
                  {{ attachment.name }}
                </el-link>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveResponse">保存配置</el-button>
      <el-button type="success" @click="handleStartResponse">启动响应</el-button>
      <el-button type="warning" @click="handleEndResponse">结束响应</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'EmergencyResponseDialog',
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
      responseData: {
        responseLevel: 'level_2',
        responseType: 'control',
        startTime: new Date(),
        expectedEndTime: '',
        responseObjectives: ['risk_control', 'loss_minimization'],
        responseMeasures: ''
      },
      responseRules: {
        responseLevel: [
          { required: true, message: '请选择响应级别', trigger: 'change' }
        ],
        responseType: [
          { required: true, message: '请选择响应类型', trigger: 'change' }
        ],
        startTime: [
          { required: true, message: '请选择启动时间', trigger: 'change' }
        ],
        responseObjectives: [
          { required: true, message: '请选择响应目标', trigger: 'change' }
        ],
        responseMeasures: [
          { required: true, message: '请输入响应措施', trigger: 'blur' }
        ]
      },
      responseTeam: [
        {
          memberName: '张三',
          role: 'commander',
          department: '风险管理部',
          contactInfo: '13800138001',
          responsibilities: '负责应急响应的统一指挥和协调'
        },
        {
          memberName: '李四',
          role: 'coordinator',
          department: '运营部',
          contactInfo: '13800138002',
          responsibilities: '负责具体措施的执行和协调'
        },
        {
          memberName: '王五',
          role: 'specialist',
          department: '技术部',
          contactInfo: '13800138003',
          responsibilities: '提供技术支持和专业建议'
        }
      ],
      currentStep: 1,
      responseProgress: 40,
      responseRecords: [
        {
          timestamp: '2024-01-15 09:00:00',
          type: 'start',
          title: '启动应急响应',
          description: '根据风险评估结果，启动二级应急响应'
        },
        {
          timestamp: '2024-01-15 10:30:00',
          type: 'action',
          title: '执行控制措施',
          description: '实施风险控制措施，暂停相关业务操作'
        },
        {
          timestamp: '2024-01-15 14:00:00',
          type: 'update',
          title: '进度更新',
          description: '已完成40%的响应措施，风险得到初步控制',
          attachments: [
            { name: '进度报告.pdf', url: '/files/progress-report.pdf' }
          ]
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
    },
    progressStatus() {
      if (this.responseProgress === 100) return 'success'
      if (this.responseProgress >= 80) return null
      return null
    },
    currentStageText() {
      const stageMap = {
        0: '准备阶段',
        1: '响应启动',
        2: '措施执行',
        3: '效果评估',
        4: '响应结束'
      }
      return stageMap[this.currentStep] || '未知阶段'
    },
    elapsedTime() {
      return '5小时30分钟'
    }
  },
  methods: {
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
    
    // 获取角色标签类型
    getRoleTagType(role) {
      const roleMap = {
        commander: 'danger',
        coordinator: 'warning',
        specialist: 'primary',
        supporter: 'info'
      }
      return roleMap[role] || 'default'
    },
    
    // 获取角色文本
    getRoleText(role) {
      const roleMap = {
        commander: '指挥官',
        coordinator: '协调员',
        specialist: '专家',
        supporter: '支持人员'
      }
      return roleMap[role] || role
    },
    
    // 获取记录类型
    getRecordType(type) {
      const typeMap = {
        start: 'success',
        action: 'primary',
        update: 'info',
        end: 'success',
        warning: 'warning',
        error: 'danger'
      }
      return typeMap[type] || 'info'
    },
    
    // 添加团队成员
    addTeamMember() {
      this.$prompt('请输入成员姓名', '添加团队成员', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.responseTeam.push({
          memberName: value,
          role: 'supporter',
          department: '',
          contactInfo: '',
          responsibilities: ''
        })
      })
    },
    
    // 编辑成员
    editMember(member) {
      this.$message.info(`编辑成员：${member.memberName}`)
      // 这里应该打开编辑对话框
    },
    
    // 删除成员
    removeMember(index) {
      this.$confirm('确认删除该团队成员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.responseTeam.splice(index, 1)
        this.$message.success('删除成功')
      })
    },
    
    // 下载附件
    downloadAttachment(attachment) {
      this.$message.success(`正在下载：${attachment.name}`)
      // 这里应该调用下载API
    },
    
    // 保存配置
    handleSaveResponse() {
      this.$refs.responseForm.validate((valid) => {
        if (valid) {
          this.$message.success('应急响应配置保存成功')
        }
      })
    },
    
    // 启动响应
    handleStartResponse() {
      this.$confirm('确认启动应急响应吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.currentStep = 1
        this.responseProgress = 25
        this.$message.success('应急响应已启动')
        
        // 添加启动记录
        this.responseRecords.unshift({
          timestamp: new Date().toLocaleString(),
          type: 'start',
          title: '启动应急响应',
          description: '手动启动应急响应机制'
        })
      })
    },
    
    // 结束响应
    handleEndResponse() {
      this.$confirm('确认结束应急响应吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.currentStep = 4
        this.responseProgress = 100
        this.$message.success('应急响应已结束')
        
        // 添加结束记录
        this.responseRecords.unshift({
          timestamp: new Date().toLocaleString(),
          type: 'end',
          title: '结束应急响应',
          description: '应急响应措施执行完毕，风险得到有效控制'
        })
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
.emergency-response-container {
  max-height: 600px;
  overflow-y: auto;
}

.risk-info-card,
.response-config-card,
.response-team-card,
.response-progress-card,
.response-records-card {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  font-weight: bold;
  color: #606266;
  display: inline-block;
  width: 80px;
}

.info-value {
  color: #303133;
}

.progress-details {
  margin-top: 20px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
}

.progress-time {
  color: #909399;
}

.record-content {
  padding-left: 10px;
}

.record-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.record-description {
  color: #606266;
  margin-bottom: 5px;
}

.record-attachments {
  color: #909399;
  font-size: 12px;
}

.record-attachments .el-link {
  margin-left: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>
