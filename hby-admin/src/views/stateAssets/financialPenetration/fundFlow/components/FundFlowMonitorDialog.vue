<template>
  <el-dialog
    title="资金流向监控预警"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="monitor-dialog-container">
      <!-- 监控统计 -->
      <el-row :gutter="20" class="mb-20">
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">监控状态</div>
              <div class="stat-value" style="color: #409EFF;">{{ monitorData.status || '正常' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">异常次数</div>
              <div class="stat-value" style="color: #F56C6C;">{{ monitorData.abnormalCount || 0 }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">预警等级</div>
              <div class="stat-value" style="color: #E6A23C;">{{ monitorData.warningLevel || '低' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">最后更新</div>
              <div class="stat-value" style="color: #67C23A;">{{ monitorData.lastUpdate || '今天' }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 监控规则 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>监控规则配置</span>
        </div>
        <el-table :data="monitorRules" stripe border>
          <el-table-column prop="ruleName" label="规则名称" min-width="150" />
          <el-table-column prop="ruleType" label="规则类型" width="120" align="center" />
          <el-table-column prop="threshold" label="阈值" width="100" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'">
                {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="editRule(scope.row)">编辑</el-button>
              <el-button size="mini" type="text" @click="deleteRule(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 预警记录 -->
      <el-card>
        <div slot="header">
          <span>预警记录</span>
        </div>
        <el-table :data="warningRecords" stripe border>
          <el-table-column prop="warningTime" label="预警时间" width="160" align="center" />
          <el-table-column prop="warningType" label="预警类型" width="120" align="center" />
          <el-table-column prop="warningLevel" label="预警等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getWarningLevelType(scope.row.warningLevel)">
                {{ scope.row.warningLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningContent" label="预警内容" min-width="200" />
          <el-table-column prop="handlingStatus" label="处理状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.handlingStatus === 'RESOLVED' ? 'success' : 'warning'">
                {{ scope.row.handlingStatus === 'RESOLVED' ? '已处理' : '待处理' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 编辑规则弹窗 -->
    <el-dialog
      title="编辑监控规则"
      :visible.sync="editDialogVisible"
      width="500px"
      append-to-body
    >
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="规则名称">
          <el-input v-model="editForm.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="规则类型">
          <el-select v-model="editForm.ruleType" placeholder="请选择规则类型">
            <el-option label="金额监控" value="金额监控"></el-option>
            <el-option label="频率监控" value="频率监控"></el-option>
            <el-option label="路径监控" value="路径监控"></el-option>
            <el-option label="时间监控" value="时间监控"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="阈值">
          <el-input v-model="editForm.threshold" placeholder="请输入阈值" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="启用" value="ACTIVE"></el-option>
            <el-option label="禁用" value="INACTIVE"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRule">保存</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { monitorFundFlowRealtime, updateMonitorRule, deleteMonitorRule } from '@/api/stateAssets/fundFlow'

export default {
  name: 'FundFlowMonitorDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      monitorData: {},
      monitorRules: [],
      warningRecords: [],
      editDialogVisible: false,
      editForm: {
        id: '',
        ruleName: '',
        ruleType: '',
        threshold: '',
        status: 'ACTIVE'
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadMonitorData()
      }
    }
  },
  methods: {
    loadMonitorData() {
      this.loading = true
      const id = this.data.fundFlowId || this.data.id || this.data.partyId
      monitorFundFlowRealtime({ fundFlowId: id })
        .then((response) => {
          if (response && response.data) {
            const d = response.data
            this.monitorData = {
              status: d.status || '正常',
              abnormalCount: d.abnormalCount || 0,
              warningLevel: d.warningLevel || '低',
              lastUpdate: d.lastUpdate || '-'
            }
            this.monitorRules = d.monitorRules || []
            this.warningRecords = d.warningRecords || []
          }
          this.loading = false
        })
        .catch(() => {
          this.loading = false
          this.$message.error('监控数据加载失败')
        })
    },
    editRule(row) {
      this.editForm = {
        id: row.id || row.alertId || '',
        ruleName: row.ruleName || '',
        ruleType: row.ruleType || '',
        threshold: row.threshold || '',
        status: row.status || 'ACTIVE'
      }
      this.editDialogVisible = true
    },
    saveRule() {
      if (!this.editForm.ruleName) {
        this.$message.warning('请输入规则名称')
        return
      }
      updateMonitorRule(this.editForm).then(res => {
        if (res && (res.result === 200 || res.data === true)) {
          this.$message.success('规则更新成功')
          this.editDialogVisible = false
          this.loadMonitorData()
        } else {
          this.$message.error(res.msg || '更新失败')
        }
      }).catch(() => {
        this.$message.error('更新规则失败')
      })
    },
    deleteRule(row) {
      this.$confirm('确认删除该监控规则？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const id = row.id || row.alertId
        deleteMonitorRule(id).then(res => {
          if (res && (res.result === 200 || res.data === true)) {
            this.$message.success('删除成功')
            this.loadMonitorData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        }).catch(() => {
          this.$message.error('删除规则失败')
        })
      }).catch(() => {})
    },
    getWarningLevelType(level) {
      const typeMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger'
      }
      return typeMap[level] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.monitor-dialog-container {
  padding: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.stat-item {
  text-align: center;
  padding: 10px;
}
.stat-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 18px;
  font-weight: bold;
}
.dialog-footer {
  text-align: right;
}
</style>