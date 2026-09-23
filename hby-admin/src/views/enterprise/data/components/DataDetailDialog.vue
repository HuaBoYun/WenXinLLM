<template>
  <el-dialog
    title="数据详情"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="基本信息" name="basic">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="数据ID">
            {{ dataInfo.id || dataInfo.dataEntryId || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="企业名称">
            {{ dataInfo.enterpriseName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="数据类型">
            <el-tag :type="getDataTypeTag(dataInfo.dataType)">
              {{ dataInfo.dataType || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="数据类别">
            {{ dataInfo.dataCategory || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="报告期间">
            {{ dataInfo.reportPeriod || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="报告年度">
            {{ dataInfo.reportYear || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(dataInfo.status)">
              {{ dataInfo.status || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="录入人">
            {{ dataInfo.submitter || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ dataInfo.createTime || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ dataInfo.updateTime || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="质量评分">
            <span v-if="dataInfo.qualityScore != null">{{ dataInfo.qualityScore }} 分</span>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="备注">
            {{ dataInfo.remark || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-tab-pane>
      
      <el-tab-pane label="数据内容" name="content">
        <el-table :data="dataContent" border>
          <el-table-column prop="fieldName" label="字段名称" width="200"></el-table-column>
          <el-table-column prop="fieldValue" label="字段值"></el-table-column>
          <el-table-column prop="fieldType" label="数据类型" width="120"></el-table-column>
          <el-table-column prop="unit" label="单位" width="100"></el-table-column>
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag size="small" :type="scope.row.valid ? 'success' : 'danger'">
                {{ scope.row.valid ? '有效' : '无效' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="审核记录" name="audit">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in auditHistory"
            :key="index"
            :timestamp="item.timestamp"
            :type="getTimelineType(item.result)"
          >
            <el-card>
              <h4>{{ item.auditor }} - {{ item.action }}</h4>
              <p>{{ item.comment }}</p>
              <el-tag size="small" :type="getResultType(item.result)">
                {{ item.result }}
              </el-tag>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>
      
      <el-tab-pane label="变更历史" name="history">
        <el-table :data="changeHistory" border>
          <el-table-column prop="changeTime" label="变更时间" width="180"></el-table-column>
          <el-table-column prop="changeUser" label="变更人" width="120"></el-table-column>
          <el-table-column prop="changeType" label="变更类型" width="120">
            <template slot-scope="scope">
              <el-tag size="small" :type="getChangeTypeTag(scope.row.changeType)">
                {{ scope.row.changeType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="changeContent" label="变更内容"></el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewChange(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>

    <!-- 变更详情弹窗 -->
    <el-dialog
      title="变更详情"
      :visible.sync="changeDetailVisible"
      width="600px"
      append-to-body
    >
      <el-descriptions :column="1" border v-if="currentChangeDetail">
        <el-descriptions-item label="变更时间">
          {{ currentChangeDetail.changeTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="变更人">
          {{ currentChangeDetail.changeUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="变更类型">
          <el-tag size="small" :type="getChangeTypeTag(currentChangeDetail.changeType)">
            {{ currentChangeDetail.changeType || '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="变更内容">
          {{ currentChangeDetail.changeContent || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="changeDetailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getDataEntryById, getDataEntryHistory, getAuditHistory } from '@/api/enterprise/data'

export default {
  name: 'DataDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dataInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      dataContent: [],
      auditHistory: [],
      changeHistory: [],
      loading: false,
      changeDetailVisible: false,
      currentChangeDetail: null
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
  watch: {
    visible(val) {
      if (val && this.dataInfo && this.dataInfo.id) {
        this.fetchDetailData(this.dataInfo.id)
      } else if (!val) {
        this.resetData()
      }
    }
  },
  methods: {
    /** 获取详情数据 */
    async fetchDetailData(id) {
      this.loading = true
      try {
        await Promise.all([
          this.fetchDataContent(id),
          this.fetchAuditHistory(id),
          this.fetchChangeHistory(id)
        ])
      } finally {
        this.loading = false
      }
    },
    /** 获取数据内容 */
    async fetchDataContent(id) {
      try {
        const response = await getDataEntryById(id)
        if (response.result == 200 && response.data) {
          const entry = response.data
          // 将实体字段映射为key-value展示
          const fieldMap = [
            { fieldName: '企业名称', fieldValue: entry.enterpriseName, fieldType: 'String', unit: '-', valid: true },
            { fieldName: '数据类型', fieldValue: entry.dataType, fieldType: 'String', unit: '-', valid: true },
            { fieldName: '数据分类', fieldValue: entry.dataCategory, fieldType: 'String', unit: '-', valid: true },
            { fieldName: '报告期', fieldValue: entry.reportPeriod, fieldType: 'String', unit: '-', valid: true },
            { fieldName: '报告年度', fieldValue: entry.reportYear, fieldType: 'String', unit: '年', valid: true },
            { fieldName: '状态', fieldValue: entry.status, fieldType: 'String', unit: '-', valid: true },
            { fieldName: '提交人', fieldValue: entry.submitter, fieldType: 'String', unit: '-', valid: !!entry.submitter },
            { fieldName: '提交时间', fieldValue: entry.submitTime, fieldType: 'Date', unit: '-', valid: !!entry.submitTime },
            { fieldName: '审核人', fieldValue: entry.auditor, fieldType: 'String', unit: '-', valid: !!entry.auditor },
            { fieldName: '审核时间', fieldValue: entry.auditTime, fieldType: 'Date', unit: '-', valid: !!entry.auditTime },
            { fieldName: '质量评分', fieldValue: entry.qualityScore, fieldType: 'Number', unit: '分', valid: entry.qualityScore != null },
            { fieldName: '备注', fieldValue: entry.remark, fieldType: 'String', unit: '-', valid: true }
          ]
          this.dataContent = fieldMap.filter(item => item.fieldValue != null && item.fieldValue !== '')
        }
      } catch (error) {
        console.error('获取数据内容失败:', error)
        this.dataContent = []
      }
    },
    /** 获取审核记录 */
    async fetchAuditHistory(id) {
      try {
        const response = await getAuditHistory(id)
        if (response.result == 200 && response.data) {
          const list = Array.isArray(response.data) ? response.data : (response.data.list || [])
          this.auditHistory = list.map(item => ({
            timestamp: item.auditTime || item.time || '',
            auditor: item.auditor || item.operator || '-',
            action: item.action || '-',
            result: item.result || '-',
            comment: item.remark || item.comment || ''
          }))
        }
      } catch (error) {
        console.error('获取审核记录失败:', error)
        this.auditHistory = []
      }
    },
    /** 获取变更历史 */
    async fetchChangeHistory(id) {
      try {
        const response = await getDataEntryHistory({ id: id, pageNumber: 1, pageSize: 50 })
        if (response.result == 200 && response.data) {
          const data = response.data
          const list = data.tlist || data.records || (Array.isArray(data) ? data : [])
          this.changeHistory = list.map(item => ({
            changeTime: item.updateTime || item.createTime || '',
            changeUser: item.submitter || item.auditor || '-',
            changeType: this.getChangeTypeFromStatus(item.status),
            changeContent: (item.dataType || '') + ' ' + (item.reportPeriod || '') + ' - ' + (item.status || '')
          }))
        }
      } catch (error) {
        console.error('获取变更历史失败:', error)
        this.changeHistory = []
      }
    },
    /** 根据状态推断变更类型 */
    getChangeTypeFromStatus(status) {
      const map = {
        '草稿': '数据新增',
        '已提交': '状态变更',
        '已审核': '状态变更',
        '已退回': '状态变更'
      }
      return map[status] || '数据修改'
    },
    /** 重置数据 */
    resetData() {
      this.activeTab = 'basic'
      this.dataContent = []
      this.auditHistory = []
      this.changeHistory = []
    },
    getDataTypeTag(type) {
      const typeMap = {
        '财务数据': 'success',
        '经营数据': 'primary',
        '人员数据': 'warning',
        '资产数据': 'info'
      }
      return typeMap[type] || 'info'
    },
    getStatusType(status) {
      const statusMap = {
        '待审核': 'warning',
        '审核中': 'primary',
        '已通过': 'success',
        '已驳回': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getTimelineType(result) {
      const typeMap = {
        '通过': 'success',
        '驳回': 'danger',
        '退回': 'warning'
      }
      return typeMap[result] || 'info'
    },
    getResultType(result) {
      const typeMap = {
        '通过': 'success',
        '驳回': 'danger',
        '退回': 'warning'
      }
      return typeMap[result] || 'info'
    },
    getChangeTypeTag(type) {
      const typeMap = {
        '数据新增': 'success',
        '数据修改': 'warning',
        '数据删除': 'danger',
        '状态变更': 'info'
      }
      return typeMap[type] || 'info'
    },
    viewChange(row) {
      this.currentChangeDetail = row
      this.changeDetailVisible = true
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
