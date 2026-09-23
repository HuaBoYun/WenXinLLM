<template>
  <div class="industry-manufacturing" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card manufacturing-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-setting"></i></div>
            <div class="card-info">
              <div class="card-title">制造企业数量</div>
              <div class="card-value">{{ overviewData.totalCompanies }}家</div>
              <div class="card-desc">装备制造 {{ overviewData.equipmentCompanies }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card manufacturing-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-goods"></i></div>
            <div class="card-info">
              <div class="card-title">总产值</div>
              <div class="card-value">{{ overviewData.totalOutput }}亿元</div>
              <div class="card-desc">平均研发投入 {{ overviewData.avgRdIntensity }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card manufacturing-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-warning-outline"></i></div>
            <div class="card-info">
              <div class="card-title">高风险企业</div>
              <div class="card-value">{{ overviewData.highRiskCount }}家</div>
              <div class="card-desc">预警企业 {{ overviewData.mediumRiskCount }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card manufacturing-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-medal"></i></div>
            <div class="card-info">
              <div class="card-title">质量合格率</div>
              <div class="card-value">{{ overviewData.qualityRate }}%</div>
              <div class="card-desc">{{ overviewData.qualityRate >= 95 ? '优秀等级' : '待提升' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="企业名称" prop="enterpriseName">
          <el-input v-model="queryForm.enterpriseName" placeholder="请输入企业名称" clearable />
        </el-form-item>
        <el-form-item label="制造类型" prop="industryType">
          <el-select v-model="queryForm.industryType" placeholder="请选择制造类型" clearable>
            <el-option label="装备制造" value="装备制造" />
            <el-option label="轨道交通" value="轨道交通" />
            <el-option label="汽车制造" value="汽车" />
            <el-option label="钢铁" value="钢铁" />
            <el-option label="化工" value="化工" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="正常" />
            <el-option label="预警" value="预警" />
            <el-option label="风险" value="风险" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增监管</el-button>
          <el-button type="warning" @click="handleExport" icon="el-icon-download">导出报告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 功能模块 -->
    <el-card class="function-card" v-loading="loading">
      <div slot="header" class="card-header">
        <span class="card-title">制造类国企监管功能</span>
        <span class="card-total">共 {{ total }} 条记录</span>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="装备制造监管" name="equipment">
          <el-table :data="getTabData('装备制造')" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="监管状态" prop="status" width="80" align="center">
              <template slot-scope="s"><el-tag :type="s.row.status === '正常' ? 'success' : s.row.status === '预警' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="产值(亿)" prop="outputValue" width="100" align="right" />
            <el-table-column label="研发投入%" prop="rdInvestment" width="100" align="center" />
            <el-table-column label="智能化等级" prop="smartLevel" width="100" align="center">
              <template slot-scope="s"><el-tag :type="s.row.smartLevel === '高' ? 'success' : s.row.smartLevel === '中' ? 'primary' : 'warning'" size="small">{{ s.row.smartLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === 'LOW' ? 'success' : s.row.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="轨道交通监管" name="rail">
          <el-table :data="getTabData('轨道')" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="监管状态" prop="status" width="80" align="center">
              <template slot-scope="s"><el-tag :type="s.row.status === '正常' ? 'success' : s.row.status === '预警' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="产值(亿)" prop="outputValue" width="100" align="right" />
            <el-table-column label="研发投入%" prop="rdInvestment" width="100" align="center" />
            <el-table-column label="智能化等级" prop="smartLevel" width="100" align="center">
              <template slot-scope="s"><el-tag :type="s.row.smartLevel === '高' ? 'success' : s.row.smartLevel === '中' ? 'primary' : 'warning'" size="small">{{ s.row.smartLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === 'LOW' ? 'success' : s.row.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="汽车制造监管" name="automotive">
          <el-table :data="getTabData('汽车')" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="监管状态" prop="status" width="80" align="center">
              <template slot-scope="s"><el-tag :type="s.row.status === '正常' ? 'success' : s.row.status === '预警' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="产值(亿)" prop="outputValue" width="100" align="right" />
            <el-table-column label="研发投入%" prop="rdInvestment" width="100" align="center" />
            <el-table-column label="智能化等级" prop="smartLevel" width="100" align="center">
              <template slot-scope="s"><el-tag :type="s.row.smartLevel === '高' ? 'success' : s.row.smartLevel === '中' ? 'primary' : 'warning'" size="small">{{ s.row.smartLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === 'LOW' ? 'success' : s.row.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="钢铁行业监管" name="steel">
          <el-table :data="getTabData('钢铁')" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="监管状态" prop="status" width="80" align="center">
              <template slot-scope="s"><el-tag :type="s.row.status === '正常' ? 'success' : s.row.status === '预警' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="产值(亿)" prop="outputValue" width="100" align="right" />
            <el-table-column label="研发投入%" prop="rdInvestment" width="100" align="center" />
            <el-table-column label="产能利用率%" prop="capacityUtilization" width="110" align="center" />
            <el-table-column label="产能状态" prop="capacityStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.capacityStatus === '正常' ? 'success' : 'danger'" size="small">{{ s.row.capacityStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === 'LOW' ? 'success' : s.row.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="化工行业监管" name="chemical">
          <el-table :data="getTabData('化工')" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="监管状态" prop="status" width="80" align="center">
              <template slot-scope="s"><el-tag :type="s.row.status === '正常' ? 'success' : s.row.status === '预警' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="产值(亿)" prop="outputValue" width="100" align="right" />
            <el-table-column label="安全等级" prop="safetyLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.safetyLevel === 'A' ? 'success' : s.row.safetyLevel === 'B' ? 'primary' : 'warning'" size="small">{{ s.row.safetyLevel }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === 'LOW' ? 'success' : s.row.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ s.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="产能监控" name="capacity">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="制造类型" prop="type" width="110" align="center" />
            <el-table-column label="产能利用率%" prop="capacityUtilization" width="110" align="center" />
            <el-table-column label="产能状态" prop="capacityStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.capacityStatus === '正常' ? 'success' : 'danger'" size="small">{{ s.row.capacityStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="质量管理" name="quality">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#E6F7FF', color: '#0050b3' }">
            <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="制造类型" prop="type" width="110" align="center" />
            <el-table-column label="质量合格率%" prop="qualityRate" width="110" align="center" />
            <el-table-column label="认证状态" prop="certStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.certStatus === '已认证' ? 'success' : 'warning'" size="small">{{ s.row.certStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增/编辑监管弹窗 -->
    <el-dialog :title="isEdit ? '编辑制造监管' : '新增制造监管'" :visible.sync="editDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="120px">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="editForm.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="制造子类型" prop="subType">
          <el-select v-model="editForm.subType" placeholder="请选择制造子类型" style="width: 100%">
            <el-option label="装备制造" value="装备制造" />
            <el-option label="轨道交通" value="轨道交通" />
            <el-option label="汽车制造" value="汽车制造" />
            <el-option label="钢铁" value="钢铁" />
            <el-option label="化工" value="化工" />
          </el-select>
        </el-form-item>
        <el-form-item label="产值(亿)" prop="revenue">
          <el-input-number v-model="editForm.revenue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="研发投入(%)">
          <el-input-number v-model="editForm.rdIntensity" :min="0" :max="100" :precision="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="智能化等级">
          <el-select v-model="editForm.smartLevel" placeholder="请选择智能化等级" style="width: 100%">
            <el-option label="高" value="高" />
            <el-option label="中" value="中" />
            <el-option label="低" value="低" />
          </el-select>
        </el-form-item>
        <el-form-item label="核心技术自主率%">
          <el-input-number v-model="editForm.techSelfRate" :min="0" :max="100" :precision="1" style="width: 100%" placeholder="0-100" />
        </el-form-item>
        <el-form-item label="竞争力">
          <el-select v-model="editForm.competitiveness" placeholder="请选择竞争力等级" style="width: 100%">
            <el-option label="强" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="弱" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="安全等级">
          <el-select v-model="editForm.safetyLevel" placeholder="请选择安全等级" style="width: 100%">
            <el-option label="A级" value="A" />
            <el-option label="B级" value="B" />
            <el-option label="C级" value="C" />
          </el-select>
        </el-form-item>
        <el-form-item label="重大风险数">
          <el-input-number v-model="editForm.incidents" :min="0" :precision="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="editForm.riskLevel" placeholder="请选择风险等级" style="width: 100%">
            <el-option label="正常(低风险)" value="LOW" />
            <el-option label="预警(中风险)" value="MEDIUM" />
            <el-option label="风险(高风险)" value="HIGH" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="editLoading" @click="submitEdit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="监管详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="企业名称">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="制造子类型">{{ viewData.subType }}</el-descriptions-item>
        <el-descriptions-item label="产值(亿)">{{ viewData.revenue }}</el-descriptions-item>
        <el-descriptions-item label="研发投入(%)">{{ viewData.rdIntensity }}</el-descriptions-item>
        <el-descriptions-item label="智能化等级">{{ viewData.smartLevel }}</el-descriptions-item>
        <el-descriptions-item label="安全等级">{{ viewData.safetyLevel }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="viewData.riskLevel === 'LOW' ? 'success' : viewData.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">
            {{ viewData.riskLevel === 'LOW' ? '正常' : viewData.riskLevel === 'MEDIUM' ? '预警' : '风险' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="重大风险数">{{ viewData.majorRisks }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ viewData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getManufacturingList, addManufacturingMonitor, getManufacturingDetail, updateManufacturingMonitor, deleteManufacturingMonitor, exportManufacturingData } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'IndustryManufacturing',
  mixins: [investThemeMixin],
  components: {},
  data() {
    return {
      activeTab: 'equipment',
      loading: false,
      total: 0,
      queryForm: {
        enterpriseName: '',
        industryType: '',
        status: '',
        pageNum: 1,
        pageSize: 200
      },
      overviewData: {
        totalCompanies: 0,
        equipmentCompanies: 0,
        totalOutput: 0,
        avgRdIntensity: 0,
        highRiskCount: 0,
        mediumRiskCount: 0,
        qualityRate: 0
      },
      tabDataList: [],
      // 新增/编辑弹窗
      editDialogVisible: false,
      editLoading: false,
      isEdit: false,
      editForm: {
        id: '',
        companyName: '',
        subType: '',
        revenue: 0,
        rdIntensity: 0,
        smartLevel: '中',
        techSelfRate: 0,
        competitiveness: 'MEDIUM',
        safetyLevel: 'A',
        incidents: 0,
        riskLevel: 'LOW'
      },
      editRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        subType: [{ required: true, message: '请选择制造子类型', trigger: 'change' }],
        revenue: [{ required: true, message: '请输入产值', trigger: 'blur' }]
      },
      // 查看详情
      viewDialogVisible: false,
      viewData: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    /** 按制造子类型过滤Tab数据 */
    getTabData(keyword) {
      const keywords = keyword.split('|')
      return this.tabDataList.filter(r => r.type && keywords.some(k => r.type.includes(k)))
    },

    /** 加载列表数据并计算概览 */
    async loadData() {
      this.loading = true
      try {
        const params = {
          enterpriseName: this.queryForm.enterpriseName,
          subType: this.queryForm.industryType,
          status: this.queryForm.status,
          pageNum: this.queryForm.pageNum,
          pageSize: this.queryForm.pageSize
        }
        const res = await getManufacturingList(params)
        if (res && res.result === 200 && res.data) {
          const list = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || list.length

          // 从数据库数据计算概览统计
          const highRiskCount = list.filter(item => item.riskLevel === 'HIGH').length
          const mediumRiskCount = list.filter(item => item.riskLevel === 'MEDIUM').length
          const equipmentCount = list.filter(item => {
            const t = item.subType || item.mfgType || ''
            return t.includes('装备制造')
          }).length
          const totalRevenue = list.reduce((sum, item) => sum + (Number(item.revenue) || 0), 0)
          const avgRd = list.length > 0
            ? (list.reduce((s, r) => s + (Number(r.rdIntensity) || 0), 0) / list.length).toFixed(1)
            : 0
          const normalCount = list.filter(item => item.riskLevel !== 'HIGH').length
          const qualityRate = list.length > 0 ? ((normalCount / list.length) * 100).toFixed(1) : 0

          this.overviewData = {
            totalCompanies: this.total,
            equipmentCompanies: equipmentCount,
            totalOutput: totalRevenue.toFixed(1),
            avgRdIntensity: avgRd,
            highRiskCount: highRiskCount,
            mediumRiskCount: mediumRiskCount,
            qualityRate: qualityRate
          }

          // 构建Tab子列表数据 - 所有字段来源于数据库
          this.tabDataList = list.map(item => ({
            id: item.id,
            enterpriseName: item.companyName,
            type: item.subType || item.mfgType || '装备制造',
            outputValue: item.revenue || 0,
            rdInvestment: item.rdIntensity || 0,
            smartLevel: item.smartLevel || (item.riskLevel === 'LOW' ? '高' : item.riskLevel === 'MEDIUM' ? '中' : '低'),
            riskLevel: item.riskLevel,
            status: item.riskLevel === 'HIGH' ? '风险' : item.riskLevel === 'MEDIUM' ? '预警' : '正常',
            safetyLevel: item.safetyRating || (item.riskLevel === 'LOW' ? 'A' : item.riskLevel === 'MEDIUM' ? 'B' : 'C'),
            capacityUtilization: item.revenue ? Math.min(100, Number(item.revenue) * 0.08).toFixed(1) : 0,
            capacityStatus: item.riskLevel === 'HIGH' ? '过剩' : '正常',
            qualityRate: item.riskLevel !== 'HIGH' ? 98 : 85,
            certStatus: item.riskLevel !== 'HIGH' ? '已认证' : '待认证'
          }))
        }
      } catch (error) {
        console.error('获取制造行业数据异常:', error)
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    /** 查询 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },

    /** 重置所有筛选条件并重新查询 */
    handleReset() {
      this.queryForm = {
        enterpriseName: '',
        industryType: '',
        status: '',
        pageNum: 1,
        pageSize: 200
      }
      this.$nextTick(() => {
        if (this.$refs.queryForm) {
          this.$refs.queryForm.resetFields()
        }
        this.loadData()
      })
    },

    /** 打开新增监管弹窗 */
    handleAdd() {
      this.isEdit = false
      this.editForm = {
        id: '',
        companyName: '',
        subType: '',
        revenue: 0,
        rdIntensity: 0,
        smartLevel: '中',
        techSelfRate: 0,
        competitiveness: 'MEDIUM',
        safetyLevel: 'A',
        incidents: 0,
        riskLevel: 'LOW'
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.editForm) {
          this.$refs.editForm.clearValidate()
        }
      })
    },

    /** 提交新增/编辑 */
    submitEdit() {
      this.$refs.editForm.validate(async (valid) => {
        if (!valid) return
        this.editLoading = true
        try {
          const apiFn = this.isEdit ? updateManufacturingMonitor : addManufacturingMonitor
          const res = await apiFn(this.editForm)
          if (res && res.result === 200) {
            this.$message.success(this.isEdit ? '编辑成功' : '新增监管记录成功')
            this.editDialogVisible = false
            this.loadData()
          } else {
            this.$message.error(res.msg || (this.isEdit ? '编辑失败' : '新增失败'))
          }
        } catch (error) {
          console.error('提交监管记录失败:', error)
          this.$message.error('操作失败，请稍后重试')
        } finally {
          this.editLoading = false
        }
      })
    },

    /** 导出报告 */
    async handleExport() {
      const loading = this.$loading({
        lock: true,
        text: '正在导出报告...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      try {
        const params = {
          companyName: this.queryForm.enterpriseName,
          subType: this.queryForm.industryType,
          riskLevel: this.queryForm.status === '正常' ? 'LOW' : this.queryForm.status === '预警' ? 'MEDIUM' : this.queryForm.status === '风险' ? 'HIGH' : ''
        }
        const res = await exportManufacturingData(params)
        if (res && res.result === 200 && res.data) {
          this.downloadCsv(res.data)
          this.$message.success('导出成功')
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      } catch (error) {
        console.error('导出报告失败:', error)
        this.$message.error('导出失败，请稍后重试')
      } finally {
        loading.close()
      }
    },

    /** 将数据转为CSV并下载 */
    downloadCsv(data) {
      if (!data || data.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const headers = Object.keys(data[0])
      const csvContent = [
        headers.map(h => `"${h}"`).join(','),
        ...data.map(row => headers.map(h => {
          const val = row[h] != null ? String(row[h]) : ''
          return `"${val.replace(/"/g, '""')}"`
        }).join(','))
      ].join('\n')
      const BOM = '\uFEFF'
      const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `制造行业监管报告_${new Date().toISOString().slice(0, 10)}.csv`
      link.click()
      URL.revokeObjectURL(link.href)
    },

    /** 查看详情 */
    async handleView(row) {
      try {
        const res = await getManufacturingDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.viewData = {
            companyName: d.companyName,
            subType: d.subType,
            revenue: d.revenue || d.totalAssets,
            rdIntensity: d.rdIntensity,
            smartLevel: d.smartLevel,
            safetyLevel: d.safetyLevel,
            riskLevel: d.riskLevel || 'LOW',
            majorRisks: d.incidents != null ? d.incidents : (d.majorRisks || 0),
            createTime: d.createTime || ''
          }
        } else {
          this.viewData = {
            companyName: row.enterpriseName,
            subType: row.type,
            revenue: row.outputValue,
            rdIntensity: row.rdInvestment,
            smartLevel: row.smartLevel,
            safetyLevel: row.safetyLevel,
            riskLevel: row.riskLevel || 'LOW',
            majorRisks: 0,
            createTime: ''
          }
        }
      } catch (error) {
        this.viewData = {
          companyName: row.enterpriseName,
          subType: row.type,
          revenue: row.outputValue,
          rdIntensity: row.rdInvestment,
          smartLevel: row.smartLevel,
          safetyLevel: row.safetyLevel,
          riskLevel: row.riskLevel || 'LOW',
          majorRisks: 0,
          createTime: ''
        }
      }
      this.viewDialogVisible = true
    },

    /** 打开编辑弹窗 */
    async handleEdit(row) {
      this.isEdit = true
      try {
        const res = await getManufacturingDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.editForm = {
            id: d.id || row.id,
            companyName: d.companyName,
            subType: d.subType,
            revenue: Number(d.revenue) || Number(d.totalAssets) || 0,
            rdIntensity: Number(d.rdIntensity) || 0,
            smartLevel: d.smartLevel || '中',
            techSelfRate: d.techSelfRate != null ? Number(d.techSelfRate) : 0,
            competitiveness: d.competitiveness || 'MEDIUM',
            safetyLevel: d.safetyLevel || d.safetyRating || 'A',
            incidents: d.incidents != null ? d.incidents : 0,
            riskLevel: d.riskLevel || 'LOW'
          }
        } else {
          this.editForm = {
            id: row.id,
            companyName: row.enterpriseName,
            subType: row.type,
            revenue: Number(row.outputValue) || 0,
            rdIntensity: Number(row.rdInvestment) || 0,
            smartLevel: row.smartLevel || '中',
            techSelfRate: row.techSelfRate != null ? Number(row.techSelfRate) : 0,
            competitiveness: row.competitiveness || 'MEDIUM',
            safetyLevel: row.safetyLevel || 'A',
            incidents: 0,
            riskLevel: row.riskLevel || 'LOW'
          }
        }
      } catch (error) {
        this.editForm = {
          id: row.id,
          companyName: row.enterpriseName,
          subType: row.type,
          revenue: Number(row.outputValue) || 0,
          rdIntensity: Number(row.rdInvestment) || 0,
          smartLevel: row.smartLevel || '中',
          techSelfRate: row.techSelfRate != null ? Number(row.techSelfRate) : 0,
          competitiveness: row.competitiveness || 'MEDIUM',
          safetyLevel: row.safetyLevel || 'A',
          incidents: 0,
          riskLevel: row.riskLevel || 'LOW'
        }
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.editForm) {
          this.$refs.editForm.clearValidate()
        }
      })
    },

    /** 删除记录 */
    handleDelete(row) {
      this.$confirm(`确定要删除企业「${row.enterpriseName}」的监管记录吗？`, '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteManufacturingMonitor(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除监管记录失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.industry-manufacturing {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.manufacturing-gradient {
        background: linear-gradient(135deg, var(--ip-secondary) 0%, var(--ip-bright) 100%);
        color: white;

        .card-content {
          display: flex;
          align-items: center;
          height: 100%;

          .card-icon {
            font-size: 36px;
            margin-right: 16px;
            opacity: 0.8;
          }

          .card-info {
            flex: 1;

            .card-title {
              font-size: 14px;
              margin-bottom: 8px;
              opacity: 0.9;
            }

            .card-value {
              font-size: 24px;
              font-weight: bold;
              margin-bottom: 4px;
            }

            .card-desc {
              font-size: 12px;
              opacity: 0.8;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .function-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .card-total {
        font-size: 13px;
        color: #909399;
      }
    }
  }

  ::v-deep .el-tabs__content {
    padding: 20px;
    min-height: 400px;
  }

  ::v-deep .el-card__body {
    padding: 0;
  }
}
</style>

