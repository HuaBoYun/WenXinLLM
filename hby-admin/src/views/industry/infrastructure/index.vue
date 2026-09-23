<template>
  <div class="industry-infrastructure" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card infra-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-house"></i></div>
            <div class="card-info">
              <div class="card-title">基础设施企业</div>
              <div class="card-value">{{ overviewData.totalCount }}家</div>
              <div class="card-desc">交通类 {{ overviewData.trafficCount }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card infra-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-s-finance"></i></div>
            <div class="card-info">
              <div class="card-title">资产总规模</div>
              <div class="card-value">{{ overviewData.totalAssets }}亿</div>
              <div class="card-desc">平均回报率 {{ overviewData.avgReturn }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card infra-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-warning"></i></div>
            <div class="card-info">
              <div class="card-title">风险预警</div>
              <div class="card-value">{{ overviewData.highRiskCount }}项</div>
              <div class="card-desc">高负债率企业占比</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card infra-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-s-check"></i></div>
            <div class="card-info">
              <div class="card-title">安全达标率</div>
              <div class="card-value">{{ overviewData.safetyRate }}%</div>
              <div class="card-desc">{{ overviewData.safetyRate >= 90 ? '优秀等级' : '待提升' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable />
        </el-form-item>
        <el-form-item label="基础设施类型" prop="infraType">
          <el-select v-model="queryForm.infraType" placeholder="请选择类型" clearable>
            <el-option label="交通" value="交通" />
            <el-option label="水利" value="水利" />
            <el-option label="能源" value="能源" />
            <el-option label="通信" value="通信" />
            <el-option label="市政" value="市政" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
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
        <span class="card-title">基础设施类国企监管功能</span>
        <span class="card-total">共 {{ total }} 条记录</span>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="交通基础设施" name="traffic">
          <el-table :data="getTabData('交通')" border size="small" :header-cell-style="{ background: '#E8F4FF', color: ipSecondary }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产总额(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资产回报率%" prop="assetReturn" width="110" align="center" />
            <el-table-column label="资产负债率%" prop="debtRatio" width="110" align="center" />
            <el-table-column label="安全评级" prop="safetyRating" width="90" align="center">
              <template slot-scope="s"><el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="隐患数" prop="majorRisks" width="80" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
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
        <el-tab-pane label="水利基础设施" name="water">
          <el-table :data="getTabData('水利')" border size="small" :header-cell-style="{ background: '#E8F4FF', color: ipSecondary }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产总额(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资产回报率%" prop="assetReturn" width="110" align="center" />
            <el-table-column label="资产负债率%" prop="debtRatio" width="110" align="center" />
            <el-table-column label="安全评级" prop="safetyRating" width="90" align="center">
              <template slot-scope="s"><el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="隐患数" prop="majorRisks" width="80" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
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
        <el-tab-pane label="能源基础设施" name="energy">
          <el-table :data="getTabData('能源')" border size="small" :header-cell-style="{ background: '#E8F4FF', color: ipSecondary }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产总额(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资产回报率%" prop="assetReturn" width="110" align="center" />
            <el-table-column label="资产负债率%" prop="debtRatio" width="110" align="center" />
            <el-table-column label="安全评级" prop="safetyRating" width="90" align="center">
              <template slot-scope="s"><el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="隐患数" prop="majorRisks" width="80" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
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
        <el-tab-pane label="通信基础设施" name="telecom">
          <el-table :data="getTabData('通信')" border size="small" :header-cell-style="{ background: '#E8F4FF', color: ipSecondary }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产总额(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资产回报率%" prop="assetReturn" width="110" align="center" />
            <el-table-column label="资产负债率%" prop="debtRatio" width="110" align="center" />
            <el-table-column label="安全评级" prop="safetyRating" width="90" align="center">
              <template slot-scope="s"><el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="隐患数" prop="majorRisks" width="80" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
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
        <el-tab-pane label="市政基础设施" name="municipal">
          <el-table :data="getTabData('市政')" border size="small" :header-cell-style="{ background: '#E8F4FF', color: ipSecondary }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产总额(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资产回报率%" prop="assetReturn" width="110" align="center" />
            <el-table-column label="资产负债率%" prop="debtRatio" width="110" align="center" />
            <el-table-column label="安全评级" prop="safetyRating" width="90" align="center">
              <template slot-scope="s"><el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="隐患数" prop="majorRisks" width="80" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
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
        <el-tab-pane label="安全生产监控" name="safety">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#E8F4FF', color: ipSecondary }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="基础设施类型" prop="infraType" width="100" align="center" />
            <el-table-column label="安全评级" prop="safetyRating" width="90" align="center">
              <template slot-scope="s"><el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag></template>
            </el-table-column>
            <el-table-column label="重大隐患数" prop="majorRisks" width="100" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="formDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="130px">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="formData.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="基础设施类型" prop="infraType">
          <el-select v-model="formData.infraType" placeholder="请选择类型" style="width: 100%">
            <el-option label="交通" value="交通" />
            <el-option label="水利" value="水利" />
            <el-option label="能源" value="能源" />
            <el-option label="通信" value="通信" />
            <el-option label="市政" value="市政" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产总额(亿)">
          <el-input-number v-model="formData.totalAssets" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="资产回报率(%)">
          <el-input-number v-model="formData.assetReturn" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="资产负债率(%)">
          <el-input-number v-model="formData.debtRatio" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="安全生产评级">
          <el-select v-model="formData.safetyRating" placeholder="请选择评级" style="width: 100%">
            <el-option label="A级(优秀)" value="A" />
            <el-option label="B级(良好)" value="B" />
            <el-option label="C级(一般)" value="C" />
            <el-option label="D级(较差)" value="D" />
          </el-select>
        </el-form-item>
        <el-form-item label="重大安全隐患数">
          <el-input-number v-model="formData.majorRisks" :min="0" :precision="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="formData.riskLevel" placeholder="请选择风险等级" style="width: 100%">
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="formLoading" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="监管详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="企业名称">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="基础设施类型">{{ viewData.infraType }}</el-descriptions-item>
        <el-descriptions-item label="资产总额(亿)">{{ viewData.totalAssets }}</el-descriptions-item>
        <el-descriptions-item label="资产回报率(%)">{{ viewData.assetReturn }}</el-descriptions-item>
        <el-descriptions-item label="资产负债率(%)">{{ viewData.debtRatio }}</el-descriptions-item>
        <el-descriptions-item label="安全生产评级">{{ viewData.safetyRating }}</el-descriptions-item>
        <el-descriptions-item label="重大隐患数">{{ viewData.majorRisks }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="viewData.riskLevel === 'LOW' ? 'success' : viewData.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">
            {{ viewData.riskLevel === 'LOW' ? '低' : viewData.riskLevel === 'MEDIUM' ? '中' : '高' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getInfrastructureList, addInfrastructureMonitor, getInfrastructureDetail, updateInfrastructureMonitor, deleteInfrastructureMonitor, exportInfrastructureData } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'IndustryInfrastructurePage',
  mixins: [investThemeMixin],
  components: {},
  data() {
    return {
      activeTab: 'traffic',
      loading: false,
      total: 0,
      queryForm: {
        companyName: '',
        infraType: '',
        riskLevel: '',
        pageNum: 1,
        pageSize: 200
      },
      overviewData: {
        totalCount: 0,
        trafficCount: 0,
        totalAssets: 0,
        avgReturn: 0,
        highRiskCount: 0,
        safetyRate: 0
      },
      tabDataList: [],
      // 表单弹窗
      formDialogVisible: false,
      formLoading: false,
      dialogTitle: '新增基础设施监管',
      isEdit: false,
      formData: {
        id: '',
        companyName: '',
        infraType: '',
        totalAssets: 0,
        assetReturn: 0,
        debtRatio: 0,
        safetyRating: 'B',
        majorRisks: 0,
        riskLevel: 'LOW'
      },
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        infraType: [{ required: true, message: '请选择基础设施类型', trigger: 'change' }]
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
    getTabData(keyword) {
      return this.tabDataList.filter(r => r.infraType && r.infraType.includes(keyword))
    },

    async loadData() {
      this.loading = true
      try {
        const params = {
          companyName: this.queryForm.companyName,
          subType: this.queryForm.infraType,
          riskLevel: this.queryForm.riskLevel,
          pageNum: this.queryForm.pageNum,
          pageSize: this.queryForm.pageSize
        }
        const res = await getInfrastructureList(params)
        if (res && res.result === 200 && res.data) {
          const list = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || list.length

          const highRiskCount = list.filter(item => item.riskLevel === 'HIGH').length
          const safeCount = list.filter(item => !item.safetyRating || item.safetyRating === 'A' || item.safetyRating === 'B').length
          const avgReturn = list.length > 0
            ? (list.reduce((s, r) => s + (Number(r.assetReturn) || 0), 0) / list.length).toFixed(1) : 0

          this.overviewData = {
            totalCount: this.total,
            trafficCount: list.filter(item => (item.infraType || '').includes('交通')).length,
            totalAssets: list.reduce((sum, item) => sum + (Number(item.totalAssets) || 0), 0).toFixed(1),
            avgReturn: avgReturn,
            highRiskCount: highRiskCount,
            safetyRate: list.length > 0 ? ((safeCount / list.length) * 100).toFixed(1) : 0
          }

          this.tabDataList = list.map(item => ({
            id: item.id,
            companyName: item.companyName,
            infraType: item.infraType || '交通',
            totalAssets: item.totalAssets || 0,
            assetReturn: item.assetReturn || 0,
            debtRatio: item.debtRatio || 0,
            safetyRating: item.safetyRating || 'B',
            majorRisks: item.majorRisks || 0,
            riskLevel: item.riskLevel === 'HIGH' ? '高' : item.riskLevel === 'MEDIUM' ? '中' : '低',
            riskLevelRaw: item.riskLevel
          }))
        }
      } catch (error) {
        console.error('获取基础设施数据异常:', error)
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },

    handleReset() {
      this.queryForm = { companyName: '', infraType: '', riskLevel: '', pageNum: 1, pageSize: 200 }
      this.$nextTick(() => {
        if (this.$refs.queryForm) this.$refs.queryForm.resetFields()
        this.loadData()
      })
    },

    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增基础设施监管'
      this.formData = { id: '', companyName: '', infraType: '', totalAssets: 0, assetReturn: 0, debtRatio: 0, safetyRating: 'B', majorRisks: 0, riskLevel: 'LOW' }
      this.formDialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },

    async handleView(row) {
      try {
        const res = await getInfrastructureDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.viewData = {
            companyName: d.companyName,
            infraType: d.subType || d.infraType,
            totalAssets: d.totalAssets,
            assetReturn: d.assetReturn,
            debtRatio: d.debtRatio,
            safetyRating: d.safetyLevel || d.safetyRating,
            majorRisks: d.incidents != null ? d.incidents : d.majorRisks,
            riskLevel: d.riskLevel || 'LOW',
            createTime: d.createTime || ''
          }
        } else {
          this.viewData = { companyName: row.companyName, infraType: row.infraType, totalAssets: row.totalAssets, assetReturn: row.assetReturn, debtRatio: row.debtRatio, safetyRating: row.safetyRating, majorRisks: row.majorRisks, riskLevel: row.riskLevelRaw || 'LOW', createTime: '' }
        }
      } catch (e) {
        this.viewData = { companyName: row.companyName, infraType: row.infraType, totalAssets: row.totalAssets, assetReturn: row.assetReturn, debtRatio: row.debtRatio, safetyRating: row.safetyRating, majorRisks: row.majorRisks, riskLevel: row.riskLevelRaw || 'LOW', createTime: '' }
      }
      this.viewDialogVisible = true
    },

    async handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑基础设施监管'
      try {
        const res = await getInfrastructureDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.formData = { id: d.id, companyName: d.companyName, infraType: d.subType || d.infraType, totalAssets: Number(d.totalAssets) || 0, assetReturn: Number(d.assetReturn) || 0, debtRatio: Number(d.debtRatio) || 0, safetyRating: d.safetyLevel || d.safetyRating || 'B', majorRisks: d.incidents != null ? d.incidents : (d.majorRisks || 0), riskLevel: d.riskLevel || 'LOW' }
        } else {
          this.formData = { id: row.id, companyName: row.companyName, infraType: row.infraType, totalAssets: Number(row.totalAssets) || 0, assetReturn: Number(row.assetReturn) || 0, debtRatio: Number(row.debtRatio) || 0, safetyRating: row.safetyRating || 'B', majorRisks: row.majorRisks || 0, riskLevel: row.riskLevelRaw || 'LOW' }
        }
      } catch (e) {
        this.formData = { id: row.id, companyName: row.companyName, infraType: row.infraType, totalAssets: Number(row.totalAssets) || 0, assetReturn: Number(row.assetReturn) || 0, debtRatio: Number(row.debtRatio) || 0, safetyRating: row.safetyRating || 'B', majorRisks: row.majorRisks || 0, riskLevel: row.riskLevelRaw || 'LOW' }
      }
      this.formDialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },

    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.formLoading = true
        try {
          // 前端字段 → 后端期望字段映射
          const submitData = {
            id: this.formData.id,
            companyName: this.formData.companyName,
            subType: this.formData.infraType,
            totalAssets: this.formData.totalAssets,
            assetReturn: this.formData.assetReturn,
            debtRatio: this.formData.debtRatio,
            safetyLevel: this.formData.safetyRating,
            incidents: this.formData.majorRisks,
            riskLevel: this.formData.riskLevel
          }
          const apiFn = this.isEdit ? updateInfrastructureMonitor : addInfrastructureMonitor
          const res = await apiFn(submitData)
          if (res && res.result === 200) {
            this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
            this.formDialogVisible = false
            this.loadData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败，请稍后重试')
        } finally {
          this.formLoading = false
        }
      })
    },

    handleDelete(row) {
      this.$confirm(`确定要删除企业「${row.companyName}」的监管记录吗？`, '删除确认', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteInfrastructureMonitor(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {})
    },

    async handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出报告...', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const params = {
          companyName: this.queryForm.companyName,
          subType: this.queryForm.infraType,
          riskLevel: this.queryForm.riskLevel
        }
        const res = await exportInfrastructureData(params)
        if (res && res.result === 200 && res.data) {
          this.downloadCsv(res.data)
          this.$message.success('导出成功')
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败，请稍后重试')
      } finally {
        loading.close()
      }
    },

    downloadCsv(data) {
      if (!data || data.length === 0) { this.$message.warning('暂无数据可导出'); return }
      const headers = Object.keys(data[0])
      const csvContent = [
        headers.map(h => `"${h}"`).join(','),
        ...data.map(row => headers.map(h => {
          const val = row[h] != null ? String(row[h]) : ''
          return `"${val.replace(/"/g, '""')}"`
        }).join(','))
      ].join('\n')
      const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `基础设施监管报告_${new Date().toISOString().slice(0, 10)}.csv`
      link.click()
      URL.revokeObjectURL(link.href)
    }
  }
}
</script>


<style lang="scss" scoped>
.industry-infrastructure {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.infra-gradient {
        background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, #096dd9 100%);
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
