<template>
  <div class="financial-radar-chart-card">
    <div class="card-header">
      <i class="el-icon-data-analysis"></i>
      <h4>财务能力雷达图</h4>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="displayData" class="radar-container">
        <!-- 雷达图 -->
        <div class="radar-chart" ref="radarChart"></div>
        
        <!-- 雷达图说明 -->
        <div class="radar-info">
          <div class="info-title">
            <i class="el-icon-info"></i>
            财务能力分析
          </div>
          <div class="info-content">
            <div class="info-grid">
              <div class="info-card">
                <div class="card-label">综合评分</div>
                <div class="card-value highlight">{{ calculateOverallScore() }}分</div>
              </div>
              <div class="info-card">
                <div class="card-label">评价等级</div>
                <div class="card-value" :class="getScoreClass()">{{ getScoreLevel() }}</div>
              </div>
              <div class="info-card">
                <div class="card-label">数据时间</div>
                <div class="card-value">{{ formatDate(getCurrentPeriod()) }}</div>
              </div>
              <div class="info-card">
                <div class="card-label">优势维度</div>
                <div class="card-value strength">{{ getStrongestDimension() }}</div>
              </div>
              <div class="info-card wide">
                <div class="card-label">改进建议</div>
                <div class="card-value suggestion">{{ getImprovementSuggestion() }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无财务雷达图数据</span>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FinancialRadarChartCard',
  props: {
    radarData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      chart: null,
      // 模拟数据缓存，避免 computed 每次返回新对象导致 watcher 无限触发
      mockData: {
        dimensions: [
          { dimensionName: '盈利能力', score: 85, maxScore: 100 },
          { dimensionName: '偿债能力', score: 72, maxScore: 100 },
          { dimensionName: '营运能力', score: 68, maxScore: 100 },
          { dimensionName: '发展能力', score: 79, maxScore: 100 },
          { dimensionName: '现金流量', score: 81, maxScore: 100 },
          { dimensionName: '资产质量', score: 76, maxScore: 100 },
          { dimensionName: '风险控制', score: 83, maxScore: 100 }
        ],
        overallScore: 77,
        overallLevel: '良好',
        periodDate: null
      },
      _resizeHandler: null
    }
  },
  computed: {
    /**
     * 显示数据 - 优先使用真实数据，否则使用模拟数据
     */
    displayData() {
      console.log('FinancialRadarChartCard - radarData:', this.radarData)
      if (this.radarData && this.radarData.radarData) {
        console.log('使用真实雷达图数据:', this.radarData.radarData)
        return this.radarData.radarData
      }

      console.log('使用模拟雷达图数据')
      // 返回缓存的模拟数据，避免每次返回新对象触发 watcher 无限循环
      return this.mockData
    }
  },
  watch: {
    displayData: {
      handler(newVal, oldVal) {
        // 避免相同引用重复初始化
        if (newVal === oldVal) return
        this.$nextTick(() => {
          this.initChart()
        })
      },
      deep: false
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
    // 监听窗口大小变化
    this._resizeHandler = () => {
      if (this.chart) {
        this.chart.resize()
      }
    }
    window.addEventListener('resize', this._resizeHandler)
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
    if (this._resizeHandler) {
      window.removeEventListener('resize', this._resizeHandler)
      this._resizeHandler = null
    }
  },
  methods: {
    /**
     * 初始化雷达图
     */
    initChart() {
      try {
        if (!this.$refs.radarChart || !this.displayData) {
          console.log('雷达图初始化条件不满足:', {
            hasRef: !!this.$refs.radarChart,
            hasData: !!this.displayData
          })
          return
        }

        if (this.chart) {
          this.chart.dispose()
        }

        this.chart = echarts.init(this.$refs.radarChart)

        const radarChartData = this.displayData

      // 处理真实数据格式
      const dimensions = radarChartData.dimensions || []

      if (!Array.isArray(dimensions) || dimensions.length === 0) {
        console.warn('雷达图维度数据为空或格式错误')
        return
      }

      // 处理不同格式的数据
      const processedDimensions = dimensions.map(item => {
        if (typeof item === 'string') {
          // 处理字符串格式的数据，如 "@{dimension=ASSET_QUALITY; dimensionName=资产质量; level=优秀; maxScore=100; score=100.00}"
          const matches = item.match(/dimensionName=([^;]+).*?score=([^}]+)/)
          if (matches) {
            return {
              dimensionName: matches[1],
              score: parseFloat(matches[2]) || 0,
              maxScore: 100
            }
          }
          return null
        }
        return item
      }).filter(Boolean)

      const indicator = processedDimensions.map(item => ({
        name: this.getDisplayName(item?.dimensionName),
        max: item?.maxScore || 100
      }))
      const values = processedDimensions.map(item => item?.score || 0)

      console.log('雷达图数据处理:', {
        dimensions,
        indicator,
        values
      })

      const option = {
        radar: {
          indicator: indicator,
          radius: '75%', // 增大雷达图半径，减少空白
          center: ['50%', '50%'], // 居中显示
          splitNumber: 4,
          axisName: {
            color: '#666',
            fontSize: 10
          },
          splitLine: {
            lineStyle: {
              color: '#e8e8e8'
            }
          },
          splitArea: {
            areaStyle: {
              color: ['rgba(102, 126, 234, 0.05)', 'rgba(102, 126, 234, 0.1)']
            }
          }
        },
        series: [{
          type: 'radar',
          data: [{
            value: values,
            name: '财务能力',
            areaStyle: {
              color: 'rgba(102, 126, 234, 0.3)'
            },
            lineStyle: {
              color: '#667eea',
              width: 2
            },
            itemStyle: {
              color: '#667eea'
            }
          }]
        }],
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            try {
              const data = params.data
              if (!data || !data.value) return ''

              let result = `<div style="font-size: 12px;">`
              result += `<div style="font-weight: bold; margin-bottom: 5px;">${data.name || '财务能力'}</div>`

              if (data.value && Array.isArray(data.value)) {
                data.value.forEach((value, index) => {
                  const indicatorName = params.radar?.indicator?.[index]?.name || `指标${index + 1}`
                  result += `<div>${indicatorName}: ${value}分</div>`
                })
              }

              result += `</div>`
              return result
            } catch (error) {
              console.error('雷达图tooltip错误:', error)
              return '数据加载中...'
            }
          }
        }
      }

      this.chart.setOption(option)
      } catch (error) {
        console.error('雷达图初始化错误:', error)
        // 显示错误提示
        if (this.chart) {
          this.chart.showLoading({
            text: '数据加载失败',
            color: '#c23531',
            textColor: '#000',
            maskColor: 'rgba(255, 255, 255, 0.8)',
            zlevel: 0
          })
        }
      }
    },

    /**
     * 计算综合评分
     */
    calculateOverallScore() {
      if (!this.displayData) return 0
      return this.displayData.overallScore || 0
    },

    /**
     * 获取评分等级
     */
    getScoreLevel() {
      const score = this.calculateOverallScore()
      if (score >= 90) return '优秀'
      if (score >= 80) return '良好'
      if (score >= 70) return '中等'
      if (score >= 60) return '及格'
      return '较差'
    },

    /**
     * 获取评分样式类
     */
    getScoreClass() {
      const score = this.calculateOverallScore()
      if (score >= 80) return 'score-excellent'
      if (score >= 70) return 'score-good'
      if (score >= 60) return 'score-average'
      return 'score-poor'
    },

    /**
     * 获取当前期间
     */
    getCurrentPeriod() {
      if (!this.displayData) return null
      return this.displayData.periodDate
    },

    /**
     * 获取维度显示名称
     */
    getDisplayName(dimensionName) {
      if (!dimensionName) return '未知维度'

      const nameMap = {
        'EFFICIENCY': '效率能力',
        'GROWTH': '经营增长',
        'OPERATIONAL': '营运能力',
        'PROFITABILITY': '盈利能力',
        'SOLVENCY': '偿债能力',
        'ASSET_QUALITY': '资产质量',
        'COMPLIANCE': '合规能力',
        'ORGANIZATION': '组织能力',
        'SUPPLEMENT': '补充指标'
      }

      // 如果是中文名称，直接返回
      if (typeof dimensionName === 'string' && /[\u4e00-\u9fa5]/.test(dimensionName)) {
        return dimensionName
      }

      // 如果是英文代码，返回对应的中文名称
      return nameMap[dimensionName] || dimensionName || '未知维度'
    },

    /**
     * 获取最强维度
     */
    getStrongestDimension() {
      if (!this.displayData || !this.displayData.dimensions) return '暂无数据'

      const dimensions = this.displayData.dimensions
      if (!Array.isArray(dimensions) || dimensions.length === 0) return '暂无数据'

      // 处理不同格式的数据
      const processedDimensions = dimensions.map(item => {
        if (typeof item === 'string') {
          const matches = item.match(/dimensionName=([^;]+).*?score=([^}]+)/)
          if (matches) {
            return {
              dimensionName: matches[1],
              score: parseFloat(matches[2]) || 0
            }
          }
          return null
        }
        return item
      }).filter(Boolean)

      // 找到得分最高的维度
      const strongest = processedDimensions.reduce((max, current) => {
        return (current.score || 0) > (max.score || 0) ? current : max
      }, processedDimensions[0])

      return this.getDisplayName(strongest.dimensionName)
    },

    /**
     * 获取改进建议
     */
    getImprovementSuggestion() {
      if (!this.displayData || !this.displayData.dimensions) return '暂无建议'

      const dimensions = this.displayData.dimensions
      if (!Array.isArray(dimensions) || dimensions.length === 0) return '暂无建议'

      // 处理不同格式的数据
      const processedDimensions = dimensions.map(item => {
        if (typeof item === 'string') {
          const matches = item.match(/dimensionName=([^;]+).*?score=([^}]+)/)
          if (matches) {
            return {
              dimensionName: matches[1],
              score: parseFloat(matches[2]) || 0
            }
          }
          return null
        }
        return item
      }).filter(Boolean)

      // 找到得分最低的维度
      const weakest = processedDimensions.reduce((min, current) => {
        return (current.score || 0) < (min.score || 0) ? current : min
      }, processedDimensions[0])

      const weakestName = this.getDisplayName(weakest.dimensionName)
      const score = weakest.score || 0

      // 根据最弱维度给出多条详细建议
      const suggestions = {
        '盈利能力': [
          '优化产品结构，提升高毛利产品占比',
          '加强成本控制，降低运营费用率',
          '提升定价策略，增强议价能力',
          '拓展盈利渠道，发展增值服务',
          '实施精益生产，减少浪费和损耗',
          '加强供应商管理，降低采购成本',
          '优化销售渠道，提高销售效率',
          '推进自动化改造，降低人工成本',
          '加强品牌建设，提升产品溢价能力',
          '开发新产品线，创造新的利润增长点'
        ],
        '偿债能力': [
          '优化债务结构，降低短期债务比例',
          '增强现金流管理，提升资金周转',
          '建立应急资金储备，增强抗风险能力',
          '合理控制负债规模，保持财务稳健',
          '拓展融资渠道，降低融资成本',
          '加强信用管理，提升信用评级',
          '优化还款计划，避免集中到期风险',
          '建立银企关系，获得更好融资条件',
          '推进资产证券化，盘活存量资产',
          '加强财务预算管理，提前规划资金需求'
        ],
        '营运能力': [
          '加快存货周转，减少资金占用',
          '优化应收账款管理，缩短回款周期',
          '提升资产使用效率，盘活闲置资产',
          '完善供应链管理，降低运营成本',
          '实施JIT生产模式，减少库存积压',
          '建立客户信用评估体系，降低坏账风险',
          '推进数字化管理，提升运营透明度',
          '优化业务流程，消除运营瓶颈',
          '加强设备维护管理，提高设备利用率',
          '建立供应商评价体系，优化供应商结构'
        ],
        '发展能力': [
          '加大研发投入，增强创新能力',
          '拓展市场渠道，扩大业务规模',
          '优化人才结构，提升团队能力',
          '建立战略合作，实现协同发展',
          '推进产业链整合，增强竞争优势',
          '加强知识产权保护，构建技术壁垒',
          '探索新商业模式，创造新增长点',
          '建立创新激励机制，激发创新活力',
          '加强市场调研，把握发展机遇',
          '推进国际化战略，拓展海外市场'
        ],
        '现金流量': [
          '优化收付款政策，改善现金流时点',
          '加强预算管理，提升资金使用效率',
          '建立多元化融资渠道，降低资金成本',
          '完善现金流预测，提前防范风险',
          '推行现金池管理，统一调配资金',
          '建立应收账款保理业务，加速回款',
          '优化付款周期，合理利用商业信用',
          '加强现金流监控，建立预警机制',
          '推进电子支付，提高资金周转效率',
          '建立季节性资金调配机制，应对波动'
        ],
        '资产质量': [
          '定期评估资产价值，及时处置不良资产',
          '优化资产配置结构，提升资产收益率',
          '加强资产维护管理，延长使用寿命',
          '建立资产质量监控体系，防范风险',
          '推进资产数字化管理，提升管理效率',
          '建立资产减值准备机制，审慎评估风险',
          '加强固定资产投资决策，避免盲目扩张',
          '推进资产轻量化运营，提高资产周转',
          '建立资产处置机制，及时清理低效资产',
          '加强无形资产管理，保护知识产权价值'
        ],
        '风险控制': [
          '完善内控制度，建立风险防范机制',
          '加强合规管理，降低法律风险',
          '建立风险预警系统，及时识别风险',
          '定期开展风险评估，持续优化管控',
          '建立风险管理委员会，统筹风险管理',
          '推进风险量化管理，提升管控精度',
          '加强信息安全管理，防范网络风险',
          '建立应急预案机制，提升应对能力',
          '加强第三方风险管控，防范连带风险',
          '推进风险文化建设，提升全员风险意识'
        ],
        '效率能力': [
          '优化业务流程，提升运营效率',
          '引入数字化工具，提高工作效率',
          '加强员工培训，提升专业能力',
          '建立绩效考核体系，激发工作积极性'
        ],
        '经营增长': [
          '制定清晰的增长战略，明确发展方向',
          '拓展新市场，寻找增长机会',
          '加强品牌建设，提升市场影响力',
          '优化产品组合，满足市场需求'
        ],
        '合规能力': [
          '建立完善的合规管理体系',
          '定期开展合规培训，提升合规意识',
          '加强内部审计，及时发现问题',
          '建立合规风险预警机制'
        ],
        '组织能力': [
          '优化组织架构，提升管理效率',
          '建立人才培养体系，提升团队能力',
          '完善激励机制，激发员工潜能',
          '加强企业文化建设，增强凝聚力'
        ],
        '补充指标': [
          '关注关键业务指标，持续监控',
          '建立数据分析体系，支持决策',
          '定期评估指标有效性，优化指标体系',
          '加强跨部门协作，提升整体效能'
        ]
      }

      const dimensionSuggestions = suggestions[weakestName] || ['关注薄弱环节，持续改进']

      if (score >= 80) {
        // 高分情况下提供保持和提升建议
        const excellentSuggestions = [
          '保持现有优势，持续优化管理流程',
          '关注行业发展趋势，提前布局新机遇',
          '建立标杆管理体系，树立行业典范',
          '加强风险预警机制，防范潜在风险',
          '推进数字化转型，提升管理效率'
        ]
        const selected = this.getRandomSuggestions(excellentSuggestions, 3)
        return `各项指标表现优秀，建议：${selected.join('；')}`
      } else if (score >= 60) {
        // 中等分数选择3-4条建议
        const selectedSuggestions = this.getRandomSuggestions(dimensionSuggestions, 4)
        return `${weakestName}有待提升，建议：${selectedSuggestions.join('；')}`
      } else {
        // 低分情况选择4-5条建议，并添加紧急性提示
        const selectedSuggestions = this.getRandomSuggestions(dimensionSuggestions, 5)
        const urgentActions = [
          '制定专项改进计划',
          '设立阶段性目标',
          '加强监控频率',
          '寻求专业咨询'
        ]
        const urgentAction = this.getRandomSuggestions(urgentActions, 1)[0]
        return `${weakestName}急需改进，建议：${selectedSuggestions.join('；')}；${urgentAction}`
      }
    },

    /**
     * 随机选择建议的辅助方法
     */
    getRandomSuggestions(suggestions, count) {
      const shuffled = [...suggestions].sort(() => 0.5 - Math.random())
      return shuffled.slice(0, Math.min(count, suggestions.length))
    },

    /**
     * 格式化日期
     */
    formatDate(timestamp) {
      if (!timestamp) return '--'
      try {
        const date = new Date(timestamp)
        return date.toLocaleDateString('zh-CN')
      } catch (error) {
        return '--'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-radar-chart-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(102, 126, 234, 0.1);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 6px 30px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
  }

  .card-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 2px solid #e8f4fd;

    i {
      font-size: 18px;
      color: #667eea;
    }

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #667eea;
      letter-spacing: 0.5px;
    }
  }

  .card-content {
    flex: 1;
    overflow: hidden;

    .radar-container {
      height: 100%;
      display: flex;
      flex-direction: column;

      .radar-chart {
        height: 280px; // 恢复雷达图高度
        flex-shrink: 0;
      }

      .radar-info {
        margin-top: 40px; // 继续增加上边距，让标签更向下移动
        padding: 12px;
        background: #f8fafe;
        border-radius: 8px;
        border: 1px solid #e8f4fd;

        .info-title {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 12px;
          font-weight: 600;
          color: #333;
          margin-bottom: 8px;

          i {
            color: #667eea;
          }
        }

        .info-content {
          .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 8px; // 减少卡片间距

            .info-card {
              background: linear-gradient(135deg, #e8f5e8 0%, #c8e6c9 100%);
              border-radius: 10px;
              padding: 12px;
              text-align: center;
              border: 1px solid rgba(76, 175, 80, 0.3);
              transition: all 0.3s ease;
              box-shadow: 0 2px 8px rgba(76, 175, 80, 0.2);
              position: relative;
              overflow: hidden;

              &::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent);
                transition: left 0.5s;
              }

              &:hover {
                background: linear-gradient(135deg, #c8e6c9 0%, #a5d6a7 100%);
                border-color: rgba(76, 175, 80, 0.5);
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);

                &::before {
                  left: 100%;
                }
              }

              &.wide {
                grid-column: 1 / -1; // 改进建议占满整行
              }

              .card-label {
                font-size: 11px;
                color: #2e7d32; // 深绿色标签
                margin-bottom: 6px;
                font-weight: 600;
                text-shadow: 0 1px 1px rgba(255,255,255,0.5);
              }

              .card-value {
                font-weight: 700;
                color: #1b5e20; // 更深的绿色数值
                font-size: 14px;
                line-height: 1.3;
                text-shadow: 0 1px 2px rgba(255,255,255,0.3);

                &.highlight {
                  color: #667eea;
                  font-weight: 700;
                  font-size: 16px;
                }

                &.score-excellent {
                  color: #67c23a;
                  font-weight: 700;
                }

                &.score-good {
                  color: #409eff;
                  font-weight: 700;
                }

                &.score-average {
                  color: #e6a23c;
                  font-weight: 700;
                }

                &.score-poor {
                  color: #f56c6c;
                  font-weight: 700;
                }

                &.strength {
                  color: #67c23a;
                  font-weight: 700;
                }

                &.suggestion {
                  color: #409eff;
                  font-size: 12px;
                  line-height: 1.4;
                  text-align: left;
                  padding: 4px 0;
                }
              }
            }
          }
        }
      }
    }

    .no-data {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #999;
      font-size: 12px;
      padding: 40px 20px;

      i {
        font-size: 16px;
      }
    }
  }
}
</style>
