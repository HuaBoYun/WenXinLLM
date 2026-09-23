<template>
  <div class="indicator-warning-panel">
    <div class="section-title">
      <div class="title-left">
        <i class="el-icon-monitor"></i>
        {{ title }}
      </div>
      <div class="title-action">
        <slot name="action" />
      </div>
    </div>

    <div class="panel-content" v-loading="indicatorLoading">
      <!-- 指标预警列表（始终展开，支持滚动） -->
      <div class="warning-indicators is-expanded">
        <div
          v-for="indicator in visibleIndicators"
          :key="indicator.indicatorCode"
          class="warning-indicator-item"
          @click="showIndicatorDetail(indicator)"
        >
          <!-- 预警红绿灯 -->
          <div class="warning-light">
            <div
              :class="['light-dot', getWarningLightClass(indicator.warningLevel)]"
              :title="getWarningLevelText(indicator.warningLevel)"
            ></div>
          </div>

          <!-- 指标名称 -->
          <div class="indicator-name" :title="indicator.indicatorName">
            {{ indicator.indicatorName }}
          </div>

          <!-- 预警值（可选显示） -->
          <div class="warning-value" v-if="indicator.showValue">
            {{ indicator.currentValue }}{{ indicator.unit }}
          </div>

          <!-- 预警级别标识 -->
          <div class="warning-badge">
            <el-tag
              :type="getWarningTagType(indicator.warningLevel)"
              size="mini"
            >
              {{ getWarningLevelText(indicator.warningLevel) }}
            </el-tag>
          </div>
        </div>

        <!-- 无数据提示 -->
        <div v-if="!indicatorLoading && warningIndicators.length === 0" class="no-data">
          <i class="el-icon-info"></i>
          <span>暂无指标预警</span>
        </div>
        <div class="no-warning-desc">所有指标均在正常范围内</div>
      </div>
    </div>

    <!-- 指标预警详情弹窗 -->
    <indicator-warning-detail
      ref="indicatorDetailDialog"
      @refresh="loadIndicatorWarnings"
    />

  </div>
</template>

<script>
import IndicatorWarningDetail from './IndicatorWarningDetail.vue'
import * as enterpriseProfileApi from '@/api/risk/enterpriseProfile'
import { getRiskWarningList } from '@/api/mxgl'

// 风险等级映射到指标预警级别
const LEVEL_MAP = { HIGH: 3, MEDIUM: 2, LOW: 1 }

export default {
  name: 'IndicatorWarningPanel',
  components: {
    IndicatorWarningDetail
  },
  props: {
    enterpriseId: {
      type: String,
      required: true
    },
    enterpriseName: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: '实时监控看版'
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 指标预警数据
      warningIndicators: [],
      indicatorLoading: false
    }
  },
  computed: {
    // 始终显示全部数据
    visibleIndicators() {
      return this.warningIndicators
    }
  },
  watch: {
    enterpriseId: {
      handler(newVal) {
        if (newVal) {
          this.loadIndicatorWarnings()
        }
      },
      immediate: true
    },
    // 跟随监管驾驶舱顶部公司切换实时刷新
    enterpriseName(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.loadIndicatorWarnings()
      }
    }
  },
  methods: {
    /**
     * 加载指标预警数据：优先调用风险预警列表接口
     * 当公司为"国务院国有资产监督管理委员会"时，不传 companyName，查询全部风险预警数据
     */
    async loadIndicatorWarnings() {
      if (!this.enterpriseId) return

      // 国资委作为监管主体，查全部数据；其他公司按公司名过滤
      const isGuoZiWei = this.enterpriseName === '国务院国有资产监督管理委员会'
      const queryCompanyName = isGuoZiWei ? '' : (this.enterpriseName || '')

      this.indicatorLoading = true
      try {
        // 优先调用风险预警管理接口
        const response = await getRiskWarningList({
          pageNum: 1,
          pageSize: 30,
          warningStatus: 'PENDING',
          companyName: queryCompanyName
        })

        if (response && response.code === 1 && response.data) {
          const list = response.data.list || []
          if (list.length > 0) {
            this.warningIndicators = list.map(w => ({
              indicatorCode:  String(w.warningId || w.id || w.warningCode || Math.random()),
              // 外层展示：从预警描述中截取"指标组合分析预警"到"风险等级"前面的内容，回退到预警标题
              indicatorName:  this.extractDescSummary(w.warningDescription || w.warningDesc || '') || w.warningTitle || w.title || w.content || '预警信息',
              currentValue:   w.warningValue != null ? String(w.warningValue) : (w.warningType || ''),
              unit:           '',
              warningLevel:   LEVEL_MAP[w.warningLevel] || 2,
              thresholdValue: w.thresholdValue != null ? String(w.thresholdValue) : '',
              showValue:      false,
              remark:         w.warningDescription || w.warningDesc || w.description || w.remark || '',
              createTime:     w.createTime || '',
              // 保留完整原始预警数据，供详情弹窗使用
              _rawWarning:    w
            }))
            return
          }
        }

        // 回退：尝试企业画像接口
        const res2 = await enterpriseProfileApi.getIndicatorWarnings({
          enterpriseId: this.enterpriseId
        })
        if (res2 && res2.code === 1 && res2.data && res2.data.length > 0) {
          this.warningIndicators = res2.data
        } else {
          this.warningIndicators = this.getMockWarningIndicators()
        }
      } catch (error) {
        console.error('加载指标预警数据失败:', error)
        this.warningIndicators = this.getMockWarningIndicators()
      } finally {
        this.indicatorLoading = false
      }
    },

    /**
     * 获取模拟预警指标数据
     */
    getMockWarningIndicators() {
      return [
        {
          indicatorCode: 'DEBT_RATIO',
          indicatorName: '资产负债率',
          currentValue: '75.2',
          unit: '%',
          warningLevel: 2, // 1:正常(绿), 2:预警(黄), 3:危险(红)
          thresholdValue: '70',
          showValue: true
        },
        {
          indicatorCode: 'CURRENT_RATIO',
          indicatorName: '流动比率',
          currentValue: '0.85',
          unit: '',
          warningLevel: 3,
          thresholdValue: '1.0',
          showValue: true
        },
        {
          indicatorCode: 'ROE',
          indicatorName: '净资产收益率',
          currentValue: '8.5',
          unit: '%',
          warningLevel: 1,
          thresholdValue: '10',
          showValue: true
        },
        {
          indicatorCode: 'CASH_FLOW',
          indicatorName: '现金流量',
          currentValue: '-125.6',
          unit: '万元',
          warningLevel: 3,
          thresholdValue: '0',
          showValue: true
        },
        {
          indicatorCode: 'PROFIT_MARGIN',
          indicatorName: '净利润率',
          currentValue: '3.2',
          unit: '%',
          warningLevel: 2,
          thresholdValue: '5',
          showValue: true
        }
      ]
    },

    /**
     * 获取预警灯样式类
     */
    getWarningLightClass(warningLevel) {
      const classMap = {
        1: 'light-green',   // 正常
        2: 'light-yellow',  // 预警
        3: 'light-red'      // 危险
      }
      return classMap[warningLevel] || 'light-gray'
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
     * 获取预警标签类型
     */
    getWarningTagType(warningLevel) {
      const typeMap = {
        1: 'success',  // 正常
        2: 'warning',  // 预警
        3: 'danger'    // 危险
      }
      return typeMap[warningLevel] || 'info'
    },

    /**
     * 显示指标详情（传递原始预警数据供详情弹窗使用）
     */
    showIndicatorDetail(indicator) {
      this.$refs.indicatorDetailDialog.showDetail(indicator, this.enterpriseId, indicator._rawWarning)
    },

    /**
     * 从预警描述中截取展示摘要：
     * 取"指标组合分析预警"到"风险等级"关键词前面的那段内容。
     * 例如描述为："指标组合分析预警：XX公司存在财务风险，风险等级：高"
     * 则截取结果为："指标组合分析预警：XX公司存在财务风险，"
     * 若描述中不含这两个关键词，则返回空字符串（由调用方回退到 warningTitle）。
     * @param {String} desc 预警描述字段值
     * @returns {String}
     */
    extractDescSummary(desc) {
      if (!desc) return ''
      const startKeyword = '指标组合分析预警'
      const endKeyword = '风险等级'
      const startIdx = desc.indexOf(startKeyword)
      if (startIdx === -1) return ''
      const endIdx = desc.indexOf(endKeyword, startIdx)
      if (endIdx === -1) {
        // 没有"风险等级"，取从起始关键词到末尾
        return desc.slice(startIdx).trim()
      }
      return desc.slice(startIdx, endIdx).trim()
    }

  }
}
</script>

<style lang="scss" scoped>
.indicator-warning-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;

  .section-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    font-size: 16px;
    font-weight: bold;
    color: #1e3c72;
    margin-bottom: 15px;

    .title-left {
      display: flex;
      align-items: center;
      gap: 8px;

      i { font-size: 18px; }
    }

    .title-action {
      display: flex;
      align-items: center;
      gap: 8px;

      // 展开/收起按钮
      .expand-toggle {
        display: inline-flex;
        align-items: center;
        gap: 4px;
        cursor: pointer;
        padding: 3px 10px;
        border-radius: 12px;
        background: #eef2ff;
        border: 1px solid #c7d2fe;
        transition: all 0.2s;
        user-select: none;

        &:hover {
          background: #e0e7ff;
          border-color: #a5b4fc;
        }

        .expand-count {
          font-size: 12px;
          font-weight: 500;
          color: #4338ca;
        }

        .expand-icon {
          font-size: 12px;
          color: #4338ca;
          transition: transform 0.25s;
        }
      }
    }
  }

  .panel-content {
    flex: 1;
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .warning-indicators {
      flex: 1;
      // 收起态：固定高度，隐藏超出部分，不显示滚动条
      overflow: hidden;
      padding-right: 5px;

      // 展开态：放开高度限制，允许滚动
      &.is-expanded {
        overflow-y: auto;
        max-height: 480px;
      }

      // 自定义滚动条样式
      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 3px;
      }

      &::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 3px;

        &:hover {
          background: #a8a8a8;
        }
      }

      .warning-indicator-item {
        display: flex;
        align-items: center;
        padding: 12px 15px;
        background: #f8f9fa;
        border-radius: 8px;
        margin-bottom: 10px;
        cursor: pointer;
        transition: all 0.3s ease;
        border-left: 3px solid transparent;

        &:hover {
          background: #e9ecef;
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          border-left-color: #1e3c72;
        }

        .warning-light {
          margin-right: 12px;

          .light-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;

            &.light-red {
              background: #ff4757;
              box-shadow: 0 0 8px rgba(255, 71, 87, 0.6);
            }

            &.light-yellow {
              background: #ffa502;
              box-shadow: 0 0 8px rgba(255, 165, 2, 0.6);
            }

            &.light-green {
              background: #2ed573;
              box-shadow: 0 0 8px rgba(46, 213, 115, 0.6);
            }

            &.light-gray {
              background: #a4b0be;
            }
          }
        }

        .indicator-name {
          flex: 1;
          font-size: 14px;
          font-weight: 500;
          color: #333;
          margin-right: 12px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .warning-value {
          font-size: 13px;
          font-weight: bold;
          color: #1e3c72;
          margin-right: 12px;
          min-width: 80px;
          text-align: right;
        }

        .warning-badge {
          margin-left: 8px;
        }
      }

      .no-data {
        text-align: center;
        padding: 40px 20px;
        color: #999;

        i {
          font-size: 24px;
          margin-right: 8px;
        }

        span {
          font-size: 14px;
        }
      }
    }
  }
}
</style>
