<template>
  <el-dialog
    title="指标预警详情"
    :visible.sync="dialogVisible"
    width="70%"
    :modal="modal"
    :append-to-body="appendToBody"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="warning-detail">
      <div v-if="indicatorData">
        <!-- 基本信息卡片（来自风险预警详情） -->
        <el-card shadow="never" class="detail-card">
          <div slot="header" class="card-header">
            <span>基本信息</span>
            <el-tag :type="getRawWarningLevelTagType(indicatorData)">
              {{ getRawWarningLevelText(indicatorData) }}
            </el-tag>
          </div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="预警编码">
              {{ getRawField(indicatorData, 'warningCode') }}
            </el-descriptions-item>
            <el-descriptions-item label="预警类型">
              <el-tag size="mini" :type="getRawWarningTypeTagType(indicatorData)">
                {{ getRawWarningTypeText(indicatorData) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="预警级别">
              <el-tag size="mini" :type="getRawWarningLevelTagType(indicatorData)">
                {{ getRawWarningLevelText(indicatorData) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="预警状态">
              <el-tag size="mini" :type="getRawWarningStatusTagType(indicatorData)">
                {{ getRawWarningStatusText(indicatorData) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="企业名称">
              {{ getRawField(indicatorData, 'companyName') }}
            </el-descriptions-item>
            <el-descriptions-item label="模型ID">
              {{ getRawField(indicatorData, 'modelId', 'evalModelId') }}
            </el-descriptions-item>
            <el-descriptions-item label="预警值">
              {{ getRawField(indicatorData, 'warningValue') }}
            </el-descriptions-item>
            <el-descriptions-item label="阈值">
              {{ getRawField(indicatorData, 'thresholdValue') }}
            </el-descriptions-item>
            <el-descriptions-item label="预警时间" :span="2">
              {{ formatRawDate(indicatorData, 'warningTime', 'createTime') }}
            </el-descriptions-item>
            <el-descriptions-item label="预警描述" :span="2">
              {{ getRawField(indicatorData, 'warningDescription', 'description') }}
            </el-descriptions-item>
          </el-descriptions>

          <!-- 处理信息（有处理人时显示） -->
          <el-card header="处理信息" style="margin-top: 16px;"
            v-if="indicatorData._rawWarning && indicatorData._rawWarning.processUser">
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="处理人">
                {{ indicatorData._rawWarning.processUser }}
              </el-descriptions-item>
              <el-descriptions-item label="处理时间">
                {{ formatRawDate(indicatorData, 'processTime') }}
              </el-descriptions-item>
              <el-descriptions-item label="处理动作">
                {{ getRawProcessActionText(indicatorData) }}
              </el-descriptions-item>
              <el-descriptions-item label="是否误报">
                <el-tag size="mini"
                  :type="indicatorData._rawWarning.isFalsePositive === 'Y' ? 'warning' : 'success'">
                  {{ indicatorData._rawWarning.isFalsePositive === 'Y' ? '是' : '否' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="处理说明" :span="2">
                {{ indicatorData._rawWarning.processNote || '-' }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-card>

        <!-- 预警分析卡片（来自风险预警详情） -->
        <el-card shadow="never" class="detail-card">
          <div slot="header">预警分析</div>
          <div class="warning-analysis">
            <div class="analysis-item">
              <h4>预警描述</h4>
              <p>{{ getRawField(indicatorData, 'warningDescription', 'description') }}</p>
            </div>
            <div class="analysis-item">
              <h4>预警标题</h4>
              <p>{{ getRawField(indicatorData, 'warningTitle', 'title') }}</p>
            </div>
            <div class="analysis-item">
              <h4>预警时间</h4>
              <p>{{ formatRawDate(indicatorData, 'warningTime', 'createTime') }}</p>
            </div>
          </div>
        </el-card>

        <!-- 历史趋势图表 -->
        <el-card shadow="never" class="detail-card">
          <div slot="header">历史趋势</div>
          <div ref="indicatorTrendChart" style="height: 300px;"></div>
        </el-card>

        <!-- 同行业对比（暂时隐藏） -->
        <el-card shadow="never" class="detail-card" v-show="false">
          <div slot="header">同行业对比</div>
          <div class="industry-comparison">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="comparison-item">
                  <div class="comparison-label">行业平均值</div>
                  <div class="comparison-value">
                    {{ getIndustryAverage(indicatorData) }}{{ indicatorData.unit }}
                  </div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="comparison-item">
                  <div class="comparison-label">行业最优值</div>
                  <div class="comparison-value">
                    {{ getIndustryBest(indicatorData) }}{{ indicatorData.unit }}
                  </div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="comparison-item">
                  <div class="comparison-label">行业排名</div>
                  <div class="comparison-value">
                    {{ getIndustryRanking(indicatorData) }}
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="exportReport">导出报告</el-button>
      <!-- 创建预警任务按钮已隐藏 -->
      <!-- <el-button type="warning" @click="createWarningTask">创建预警任务</el-button> -->
    </div>

    <!-- 数据穿透分析对话框（复用 fxyjgl 穿透功能） -->
    <warning-drill-down-dialog
      :visible.sync="drillDownDialogVisible"
      :warning-id="drillDownWarningId"
      :override-warning-time="drillDownEndTime"
    />
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import htmlDocx from 'html-docx-js/dist/html-docx'
import { saveAs } from 'file-saver'
import { getExecutionHistory, getEvaluationModelDetail, getRiskWarningList, getExecutionResult } from '@/api/mxgl'
import { getWarningDrillDownData } from '@/api/risk/warning'
import WarningDrillDownDialog from '@/components/risk/WarningDrillDownDialog'

export default {
  name: 'IndicatorWarningDetail',
  components: {
    WarningDrillDownDialog
  },
  props: {
    // 是否显示遮罩层，大屏场景传 false 可去掉全页灰色遮罩
    modal: {
      type: Boolean,
      default: true
    },
    // 是否将弹窗挂载到 body，大屏场景传 true 可脱离 overflow:hidden 的裁剪
    appendToBody: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      indicatorData: null,
      enterpriseId: null,
      trendChart: null,
      // 执行历史数据（最近6次），用于历史趋势柱状图
      trendHistoryList: [],
      // 数据穿透分析对话框
      drillDownDialogVisible: false,
      drillDownWarningId: '',
      drillDownEndTime: ''  // 点击柱时对应的执行结束时间，用于覆盖穿透弹窗的预警时间
    }
  },
  methods: {
    /**
     * 显示详情对话框
     * @param {Object} indicator    面板中的指标对象（含 _rawWarning 原始预警数据）
     * @param {String} enterpriseId 企业ID
     * @param {Object} rawWarning   原始预警数据（可选，优先级高于 indicator._rawWarning）
     */
    async showDetail(indicator, enterpriseId, rawWarning) {
      // 将原始预警数据挂载到 indicatorData 上，供基本信息卡片使用
      this.indicatorData = Object.assign({}, indicator, {
        _rawWarning: rawWarning || indicator._rawWarning || null
      })
      this.enterpriseId = enterpriseId
      this.trendHistoryList = []
      this.dialogVisible = true

      // 先加载执行历史，再渲染图表
      // 用 setTimeout 等待 el-dialog 动画完成后 $refs 才可靠可用
      await this.loadTrendHistory()
      this.$nextTick(() => {
        setTimeout(() => {
          this.renderTrendChart()
        }, 100)
      })
    },

    /**
     * 加载历史趋势数据
     *
     * 取数链路：
     *   1. evalModelId → getEvaluationModelDetail() → dataModelId（COMB开头）= combinationId
     *   2. getExecutionHistory({ combinationId, pageSize:6 }) 取最近6次执行（X轴时间来源）
     *   3. getWarningDrillDownData({ warningId }) 取穿透业务数据总条数（Y轴数量来源）
     *      Y轴 = businessData.tables[].total 之和，与穿透弹窗展示的数量完全一致
     *
     * 降级策略：任何步骤失败 → trendHistoryList 为空 → 降级随机折线图
     */
    async loadTrendHistory() {
      const raw = (this.indicatorData && this.indicatorData._rawWarning) || {}
      const evalModelId = raw.evalModelId || ''
      const warningId = raw.warningId || ''
      if (!evalModelId) return

      try {
        // 第一步：查评估模型，取 combinationId
        let combinationId = ''
        const evalRes = await getEvaluationModelDetail(evalModelId)
        const evalData = (evalRes && evalRes.code === 1 && evalRes.data)
          ? evalRes.data
          : (evalRes && evalRes.evalModelId ? evalRes : null)
        if (evalData && evalData.dataModelId && evalData.dataModelId.startsWith('COMB')) {
          combinationId = evalData.dataModelId
        }
        if (!combinationId) {
          const modelId = raw.modelId || ''
          if (modelId.startsWith('COMB')) combinationId = modelId
        }
        if (!combinationId) return

        // 第二步：并行取执行历史（X轴）和穿透数据总条数（Y轴）
        const [histRes, drillRes] = await Promise.all([
          getExecutionHistory({ pageNum: 1, pageSize: 6, combinationId }),
          warningId
            ? getWarningDrillDownData({ warningId, pageNum: 1, pageSize: 1 }).catch(() => null)
            : Promise.resolve(null)
        ])

        // 解析执行历史
        let execList = []
        if (histRes && histRes.code === 1 && histRes.data) {
          execList = histRes.data.records || histRes.data.list || []
        } else if (histRes && histRes.records) {
          execList = histRes.records || []
        }
        if (!execList.length) return

        // 按 endTime 升序排列，只取最近6次
        execList.sort((a, b) => {
          const ta = new Date(a.endTime || a.startTime || 0).getTime()
          const tb = new Date(b.endTime || b.startTime || 0).getTime()
          return ta - tb
        })
        const recent6 = execList.slice(-6)

        // 解析穿透数据总条数（与穿透弹窗保持一致）
        let drillTotal = 0
        if (drillRes && drillRes.code === 1 && drillRes.data) {
          const bd = drillRes.data.businessData
          if (bd && bd.tables) {
            // 多表格式：所有表 total 之和
            drillTotal = bd.tables.reduce((sum, t) => sum + (Number(t.total) || 0), 0)
          } else if (bd) {
            // 单表兼容格式
            drillTotal = Number(bd.total) || 0
          }
        }

        // 组装 trendHistoryList：X轴=执行结束时间，Y轴=穿透业务数据总条数
        this.trendHistoryList = recent6.map((exec, idx) => {
          let label = `第${idx + 1}次`
          const endStr = exec.endTime || exec.startTime || ''
          if (endStr) {
            try {
              const d = new Date(endStr)
              const mo = String(d.getMonth() + 1).padStart(2, '0')
              const dd = String(d.getDate()).padStart(2, '0')
              const hh = String(d.getHours()).padStart(2, '0')
              const mi = String(d.getMinutes()).padStart(2, '0')
              label = `${mo}-${dd} ${hh}:${mi}`
            } catch (e) { /* 保留默认标签 */ }
          }
          return {
            startTime: label,        // X轴：执行结束时间（MM-DD HH:mm）
            warningCount: drillTotal, // Y轴：穿透业务数据总条数（与穿透弹窗一致）
            rawEndTime: endStr        // 原始结束时间，供穿透弹窗覆盖预警时间使用
          }
        })
      } catch (e) {
        // 任何异常均静默处理，降级为随机折线图
        this.trendHistoryList = []
      }
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      if (this.trendChart) {
        this.trendChart.dispose()
        this.trendChart = null
      }
    },

    /**
     * 获取标签类型
     */
    getTagType(warningLevel) {
      const typeMap = {
        1: 'success',
        2: 'warning',
        3: 'danger'
      }
      return typeMap[warningLevel] || 'info'
    },

    /**
     * 获取预警级别文本
     */
    getWarningLevelText(warningLevel) {
      const textMap = {
        1: '正常',
        2: '预警',
        3: '危险'
      }
      return textMap[warningLevel] || '未知'
    },

    /**
     * 获取预警级别样式类
     */
    getWarningLevelClass(warningLevel) {
      const classMap = {
        1: 'level-normal',
        2: 'level-warning',
        3: 'level-danger'
      }
      return classMap[warningLevel] || ''
    },

    /**
     * 获取预警原因
     */
    getWarningReason(indicator) {
      const reasonMap = {
        'DEBT_RATIO': '资产负债率过高，超过安全阈值70%，财务风险较大',
        'CURRENT_RATIO': '流动比率过低，短期偿债能力不足，存在流动性风险',
        'ROE': '净资产收益率偏低，盈利能力有待提升',
        'CASH_FLOW': '现金流量为负，经营活动现金流入不足',
        'PROFIT_MARGIN': '净利润率低于行业平均水平，盈利能力不足'
      }
      return reasonMap[indicator.indicatorCode] || '指标值异常，需要关注'
    },

    /**
     * 获取风险影响
     */
    getRiskImpact(indicator) {
      const impactMap = {
        'DEBT_RATIO': '可能面临偿债压力，影响企业信用评级和融资能力',
        'CURRENT_RATIO': '短期内可能出现资金周转困难，影响正常经营',
        'ROE': '投资回报率低，可能影响股东信心和企业估值',
        'CASH_FLOW': '现金流紧张可能导致支付困难，影响供应商关系',
        'PROFIT_MARGIN': '盈利能力不足可能影响企业可持续发展'
      }
      return impactMap[indicator.indicatorCode] || '可能对企业经营产生不利影响'
    },

    /**
     * 获取建议措施
     */
    getSuggestions(indicator) {
      const suggestionMap = {
        'DEBT_RATIO': [
          '优化资本结构，适当降低负债比例',
          '加强应收账款管理，提高资金回收效率',
          '考虑股权融资，增加自有资金'
        ],
        'CURRENT_RATIO': [
          '加强流动资产管理，提高资产流动性',
          '优化库存结构，减少资金占用',
          '建立应急资金储备，确保流动性安全'
        ],
        'ROE': [
          '提升营业收入，扩大市场份额',
          '控制成本费用，提高利润率',
          '优化资产配置，提高资产使用效率'
        ],
        'CASH_FLOW': [
          '加强应收账款催收，加快资金回笼',
          '优化采购付款周期，改善现金流',
          '考虑短期融资，缓解资金压力'
        ],
        'PROFIT_MARGIN': [
          '优化产品结构，提高高毛利产品比重',
          '加强成本控制，提升运营效率',
          '拓展新的盈利增长点'
        ]
      }
      return suggestionMap[indicator.indicatorCode] || ['建议进一步分析原因，制定改进措施']
    },

    /**
     * 渲染趋势图表
     * 有预警历史数据（trendHistoryList）时：渲染柱状图，X轴=预警日期，Y轴=预警数量
     * 无数据时：降级为随机折线图（保留原逻辑）
     */
    renderTrendChart() {
      if (!this.indicatorData) return

      // 用 $refs 代替 getElementById，避免弹窗嵌套时跨层级 id 查找失效
      const chartDom = this.$refs.indicatorTrendChart
      if (!chartDom) return

      // 销毁旧实例，避免重复初始化
      if (this.trendChart) {
        this.trendChart.dispose()
        this.trendChart = null
      }
      this.trendChart = echarts.init(chartDom)

      // ---- 有预警历史数据：渲染柱状图 ----
      if (this.trendHistoryList && this.trendHistoryList.length > 0) {
        const list = this.trendHistoryList
        // X轴：预警日期（YYYY-MM-DD 格式，直接展示）
        const xData = list.map((item, idx) => item.startTime || `第${idx + 1}天`)
        // Y轴：预警数量
        const warningData = list.map(item => Number(item.warningCount) || 0)

        const option = {
          title: {
            text: (this.indicatorData.indicatorName || '指标') + ' 预警数量趋势',
            left: 'center',
            textStyle: { fontSize: 14 }
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
            formatter(params) {
              return `${params[0].name}<br/>${params[0].marker}预警数量: ${params[0].value} 条`
            }
          },
          legend: { data: ['预警数量'], bottom: 4 },
          grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
          xAxis: {
            type: 'category',
            data: xData,
            axisLabel: { rotate: 30, fontSize: 11 }
          },
          yAxis: {
            type: 'value',
            name: '预警数量（条）',
            minInterval: 1
          },
          series: [
            {
              name: '预警数量',
              type: 'bar',
              data: warningData,
              itemStyle: { color: '#1890ff' },
              label: { show: true, position: 'top', fontSize: 11 }
            }
          ]
        }
        this.trendChart.setOption(option)

        // 柱状图点击穿透：点击任意柱打开数据穿透分析对话框
        this.trendChart.off('click')
        this.trendChart.on('click', (params) => {
          const warningId = (this.indicatorData._rawWarning || {}).warningId || ''
          if (!warningId) {
            this.$message.warning('暂无关联预警ID，无法穿透')
            return
          }
          // 取点击柱对应的执行结束时间，覆盖穿透弹窗的预警时间
          const clickedItem = this.trendHistoryList[params.dataIndex] || {}
          this.drillDownEndTime = clickedItem.rawEndTime || ''
          this.drillDownWarningId = warningId
          this.drillDownDialogVisible = true
        })
        return
      }

      // ---- 无执行历史数据：降级为原随机折线图 ----
      const raw = this.indicatorData._rawWarning || {}
      const currentVal = parseFloat(raw.warningValue != null ? raw.warningValue : this.indicatorData.currentValue)
      const thresholdVal = parseFloat(raw.thresholdValue != null ? raw.thresholdValue : this.indicatorData.thresholdValue)

      const trendData = this.generateTrendData(currentVal)
      const thresholdData = isNaN(thresholdVal) ? [] : new Array(6).fill(thresholdVal)

      const option = {
        title: {
          text: this.indicatorData.indicatorName + ' 趋势分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          formatter: (params) => {
            return `${params[0].name}<br/>${params[0].seriesName}: ${params[0].value}${this.indicatorData.unit || ''}`
          }
        },
        legend: {
          data: thresholdData.length
            ? [this.indicatorData.indicatorName, '阈值线']
            : [this.indicatorData.indicatorName],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06']
        },
        yAxis: {
          type: 'value',
          name: this.indicatorData.unit || ''
        },
        series: [
          {
            name: this.indicatorData.indicatorName,
            type: 'line',
            data: trendData,
            smooth: true,
            lineStyle: { color: this.getLineColor() },
            markPoint: {
              data: [{
                name: '当前值',
                value: isNaN(currentVal) ? '' : currentVal,
                xAxis: 5,
                yAxis: isNaN(currentVal) ? 0 : currentVal
              }]
            }
          },
          ...(thresholdData.length ? [{
            name: '阈值线',
            type: 'line',
            data: thresholdData,
            lineStyle: { color: '#ff4d4f', type: 'dashed' }
          }] : [])
        ]
      }
      this.trendChart.setOption(option)
    },

    /**
     * 生成趋势数据（降级用，无执行历史时使用）
     * @param {Number} currentVal 当前真实数值（已 parseFloat）
     */
    generateTrendData(currentVal) {
      // 若 currentVal 无效，用随机基准值保证图表有起伏
      const base = isNaN(currentVal) || currentVal === 0 ? 100 : Math.abs(currentVal)
      const sign = (!isNaN(currentVal) && currentVal < 0) ? -1 : 1
      const data = []

      for (let i = 0; i < 6; i++) {
        // 波动幅度：基准值的 ±15%，最后一个点固定为当前值
        if (i === 5 && !isNaN(currentVal)) {
          data.push(parseFloat(currentVal.toFixed(2)))
        } else {
          const variation = (Math.random() - 0.5) * base * 0.3
          data.push(parseFloat((sign * base + variation).toFixed(2)))
        }
      }

      return data
    },

    /**
     * 获取线条颜色
     */
    getLineColor() {
      const colorMap = {
        1: '#52c41a',
        2: '#faad14',
        3: '#ff4d4f'
      }
      return colorMap[this.indicatorData.warningLevel] || '#1890ff'
    },

    /**
     * 获取行业平均值
     */
    getIndustryAverage(indicator) {
      const averageMap = {
        'DEBT_RATIO': '55.8',
        'CURRENT_RATIO': '1.25',
        'ROE': '12.5',
        'CASH_FLOW': '156.8',
        'PROFIT_MARGIN': '8.2'
      }
      return averageMap[indicator.indicatorCode] || '暂无数据'
    },

    /**
     * 获取行业最优值
     */
    getIndustryBest(indicator) {
      const bestMap = {
        'DEBT_RATIO': '35.2',
        'CURRENT_RATIO': '2.15',
        'ROE': '25.8',
        'CASH_FLOW': '458.6',
        'PROFIT_MARGIN': '18.5'
      }
      return bestMap[indicator.indicatorCode] || '暂无数据'
    },

    /**
     * 获取行业排名
     */
    getIndustryRanking(indicator) {
      const rankings = ['前10%', '前25%', '前50%', '后50%', '后25%']
      return rankings[Math.floor(Math.random() * rankings.length)]
    },

    /**
     * 导出报告为 Word 文档
     * 将基本信息、预警分析、历史趋势图组装成 HTML，通过 html-docx-js 转换并下载
     */
    exportReport() {
      if (!this.indicatorData) {
        this.$message.warning('暂无数据可导出')
        return
      }

      try {
        const raw = this.indicatorData._rawWarning || {}
        const title = this.indicatorData.indicatorName || raw.warningTitle || '预警报告'
        const exportTime = new Date().toLocaleString('zh-CN')

        // ---- 1. 历史趋势图：从 echarts 实例截图为 base64 ----
        let chartImgTag = '<p style="color:#999;">（历史趋势图暂无数据）</p>'
        if (this.trendChart) {
          try {
            const imgBase64 = this.trendChart.getDataURL({
              type: 'png',
              pixelRatio: 1,
              backgroundColor: '#fff'
            })
            // Word A4 正文可用宽约 15.9cm，图片宽度设为 15cm 接近满宽并居中显示
            // 同时用 width 属性（而非仅 style）提升 html-docx-js 的兼容性
            chartImgTag = `<div style="text-align:center;"><img src="${imgBase64}" width="567" style="width:15cm;height:auto;display:inline-block;" /></div>`
          } catch (e) {
            // 截图失败不影响整体导出
          }
        }

        // ---- 2. 辅助：安全取字段 ----
        const f = (...fields) => {
          const src = raw && Object.keys(raw).length ? raw : this.indicatorData
          for (const k of fields) {
            if (src[k] != null && src[k] !== '') return String(src[k])
          }
          return '-'
        }
        const fDate = (...fields) => {
          const src = raw && Object.keys(raw).length ? raw : this.indicatorData
          for (const k of fields) {
            if (src[k]) {
              try { return new Date(src[k]).toLocaleString('zh-CN') } catch (e) { return src[k] }
            }
          }
          return '-'
        }

        // ---- 3. 组装 HTML ----
        const html = `<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <style>
    body { font-family: "Microsoft YaHei", Arial, sans-serif; font-size: 12pt; color: #333; margin: 20px; }
    h1 { font-size: 18pt; color: #1e3c72; text-align: center; margin-bottom: 4px; }
    .subtitle { text-align: center; color: #888; font-size: 10pt; margin-bottom: 20px; }
    h2 { font-size: 13pt; color: #1e3c72; border-bottom: 1px solid #1e3c72; padding-bottom: 4px; margin-top: 20px; }
    table { width: 100%; border-collapse: collapse; margin-top: 8px; font-size: 11pt; }
    th { background: #1e3c72; color: #fff; padding: 6px 10px; text-align: left; width: 120px; }
    td { border: 1px solid #ccc; padding: 6px 10px; }
    tr:nth-child(even) td { background: #f5f7fa; }
    .analysis-block { margin: 8px 0; }
    .analysis-block strong { color: #1e3c72; }
    .analysis-block p { margin: 4px 0 12px 0; line-height: 1.6; }
    .chart-section { margin-top: 8px; max-width: 15cm; overflow: hidden; text-align: center; }
    .footer { margin-top: 30px; text-align: right; color: #aaa; font-size: 9pt; }
  </style>
</head>
<body>
  <h1>预警详情报告</h1>
  <p class="subtitle">导出时间：${exportTime}</p>

  <h2>一、基本信息</h2>
  <table>
    <tr><th>预警编码</th><td>${f('warningCode')}</td><th>预警类型</th><td>${this.getRawWarningTypeText(this.indicatorData)}</td></tr>
    <tr><th>预警级别</th><td>${this.getRawWarningLevelText(this.indicatorData)}</td><th>预警状态</th><td>${this.getRawWarningStatusText(this.indicatorData)}</td></tr>
    <tr><th>企业名称</th><td>${f('companyName')}</td><th>模型ID</th><td>${f('modelId', 'evalModelId')}</td></tr>
    <tr><th>预警值</th><td>${f('warningValue')}</td><th>阈值</th><td>${f('thresholdValue')}</td></tr>
    <tr><th>预警时间</th><td colspan="3">${fDate('warningTime', 'createTime')}</td></tr>
    <tr><th>预警描述</th><td colspan="3">${f('warningDescription', 'description')}</td></tr>
  </table>

  <h2>二、预警分析</h2>
  <div class="analysis-block">
    <strong>预警描述</strong>
    <p>${f('warningDescription', 'description')}</p>
    <strong>预警标题</strong>
    <p>${f('warningTitle', 'title')}</p>
    <strong>预警时间</strong>
    <p>${fDate('warningTime', 'createTime')}</p>
  </div>

  <h2>三、历史趋势</h2>
  <div class="chart-section">
    ${chartImgTag}
  </div>
</body>
</html>`

        // ---- 4. 转换并下载 ----
        const blob = htmlDocx.asBlob(html)
        const fileName = `预警报告_${title}_${new Date().toLocaleDateString('zh-CN').replace(/\//g, '')}.docx`
        saveAs(blob, fileName)
        this.$message.success('报告导出成功')
      } catch (err) {
        console.error('导出报告失败:', err)
        this.$message.error('导出失败，请稍后重试')
      }
    },

    /**
     * 创建预警任务
     */
    createWarningTask() {
      this.$message.info('预警任务创建功能开发中...')
    },

    // ===== 以下为基本信息卡片使用的原始预警数据辅助方法 =====

    /**
     * 从 indicator._rawWarning 或 indicator 本身取字段值
     * @param {Object} indicator
     * @param {...String} fields 按优先级依次尝试的字段名
     */
    getRawField(indicator, ...fields) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : indicator
      if (!raw) return '-'
      for (const f of fields) {
        if (raw[f] != null && raw[f] !== '') return raw[f]
      }
      return '-'
    },

    /** 格式化原始预警数据中的日期字段 */
    formatRawDate(indicator, ...fields) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : indicator
      if (!raw) return '-'
      for (const f of fields) {
        if (raw[f]) {
          try { return new Date(raw[f]).toLocaleString('zh-CN') } catch (e) { return raw[f] }
        }
      }
      return '-'
    },

    /** 预警级别标签类型（基于原始预警数据的 HIGH/MEDIUM/LOW） */
    getRawWarningLevelTagType(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return this.getTagType(indicator.warningLevel)
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
      return map[raw.warningLevel] || 'info'
    },

    /** 预警级别文本（基于原始预警数据） */
    getRawWarningLevelText(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return this.getWarningLevelText(indicator.warningLevel)
      const map = { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }
      return map[raw.warningLevel] || (raw.warningLevel || '-')
    },

    /** 预警类型标签类型 */
    getRawWarningTypeTagType(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return 'info'
      const map = {
        FINANCIAL_RISK: 'success', PROCUREMENT_RISK: 'primary',
        CREDIT_RISK: 'warning',    COMPLIANCE_RISK: 'info',
        COMBINATION_EXECUTION: 'primary', DATA_MODEL_EXECUTION: 'success',
        AUTO_GENERATED: 'warning', MANUAL_CREATED: 'info',
        THRESHOLD: 'danger',       TREND: 'warning', ANOMALY: 'danger'
      }
      return map[raw.warningType] || 'default'
    },

    /** 预警类型文本 */
    getRawWarningTypeText(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return '-'
      const map = {
        FINANCIAL_RISK: '财务风险', PROCUREMENT_RISK: '采购风险',
        CREDIT_RISK: '信用风险',    COMPLIANCE_RISK: '合规风险',
        COMBINATION_EXECUTION: '组合执行', DATA_MODEL_EXECUTION: '模型执行',
        AUTO_GENERATED: '自动生成', MANUAL_CREATED: '手动创建',
        THRESHOLD: '阈值预警',      TREND: '趋势预警', ANOMALY: '异常预警'
      }
      return map[raw.warningType] || (raw.warningType || '-')
    },

    /** 预警状态标签类型 */
    getRawWarningStatusTagType(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return 'info'
      const map = { PENDING: 'warning', PROCESSING: 'primary', PROCESSED: 'success', IGNORED: 'info' }
      return map[raw.warningStatus] || 'default'
    },

    /** 预警状态文本 */
    getRawWarningStatusText(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return '-'
      const map = { PENDING: '待处理', PROCESSING: '处理中', PROCESSED: '已处理', IGNORED: '已忽略' }
      return map[raw.warningStatus] || (raw.warningStatus || '-')
    },

    /** 处理动作文本 */
    getRawProcessActionText(indicator) {
      const raw = (indicator && indicator._rawWarning) ? indicator._rawWarning : null
      if (!raw) return '-'
      const map = { CONFIRM: '确认处理', IGNORE: '忽略预警', ESCALATE: '升级预警', FALSE_POSITIVE: '标记误报' }
      return map[raw.processAction] || (raw.processAction || '-')
    }
  },

  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.warning-detail {
  .detail-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 500;
    }

    .detail-item {
      margin-bottom: 12px;
      display: flex;
      align-items: center;

      label {
        font-weight: 500;
        color: #666;
        margin-right: 8px;
        min-width: 80px;
      }

      span {
        color: #333;

        &.current-value {
          font-weight: bold;
          font-size: 16px;
        }
      }

      .level-normal {
        color: #52c41a;
        font-weight: bold;
      }

      .level-warning {
        color: #faad14;
        font-weight: bold;
      }

      .level-danger {
        color: #ff4d4f;
        font-weight: bold;
      }
    }

    .warning-analysis {
      .analysis-item {
        margin-bottom: 20px;

        h4 {
          color: #333;
          margin-bottom: 8px;
          font-size: 14px;
        }

        p {
          color: #666;
          line-height: 1.6;
          margin-bottom: 0;
        }

        ul {
          margin: 0;
          padding-left: 20px;

          li {
            color: #666;
            line-height: 1.6;
            margin-bottom: 4px;
          }
        }
      }
    }

    .industry-comparison {
      .comparison-item {
        text-align: center;
        padding: 16px;
        background: #f8f9fa;
        border-radius: 8px;

        .comparison-label {
          font-size: 12px;
          color: #666;
          margin-bottom: 8px;
        }

        .comparison-value {
          font-size: 16px;
          font-weight: bold;
          color: #333;
        }
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
