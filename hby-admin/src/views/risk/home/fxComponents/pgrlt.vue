<template>
  <div class="pgrlt-container">
    <div class="pgrlt-title">风险评估热力图</div>
    <div class="pgrlt-chart">
      <!-- Y轴标签 (严重程度) -->
      <div class="y-axis">
        <div class="y-labels">
          <div class="y-label" v-for="i in 5" :key="i">{{ 6 - i }}</div>
        </div>
      </div>

      <!-- 主要表格区域 -->
      <div class="chart-main">
        <!-- 5x5 网格 -->
        <div class="grid-container">
          <div class="grid-row" v-for="row in 5" :key="row">
            <div
              class="grid-cell"
              v-for="col in 5"
              :key="col"
              :class="getCellClass(row, col)"
              :style="getCellStyle(row, col)"
              @click="handleCellClick(row, col)"
              @mouseenter="showTooltip($event, row, col)"
              @mouseleave="hideTooltip"
            >
              <!-- 蓝色圆点，只在有数据时显示 -->
              <div
                v-if="getCellValue(row, col) > 0"
                class="data-indicator"
              ></div>
            </div>
          </div>
        </div>

        <!-- X轴标签 (频率) - 移到下方 -->
        <div class="x-axis">
          <div class="x-labels">
            <div class="x-label" v-for="i in 5" :key="i">{{ i }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图例 -->
    <div class="legend">
      <div class="legend-item">
        <div class="legend-color low"></div>
        <span>低风险</span>
      </div>
      <div class="legend-item">
        <div class="legend-color medium"></div>
        <span>中风险</span>
      </div>
      <div class="legend-item">
        <div class="legend-color high"></div>
        <span>高风险</span>
      </div>
    </div>

    <!-- 自定义tooltip -->
    <div 
      v-if="tooltipVisible" 
      class="custom-tooltip"
      :style="{ left: tooltipPosition.x + 'px', top: tooltipPosition.y + 'px' }"
    >
      {{ tooltipContent }}
    </div>

    <!-- 弹窗 -->
    <el-dialog
      title="风险评估详情"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose"
    >
      <el-table
        :data="detailData"
        v-loading="detailLoading"
        style="width: 100%"
      >
        <el-table-column
          align="center"
          label="风险点编号"
          prop="risknumber"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="风险名称"
          prop="riskname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="风险点描述"
          prop="riskdes"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="发生频率" prop="frequency">
          <template #default="{ row }">
            <el-select
              v-model="row.frequency"
              disabled
              :ref="'frequency' + row.index"
            >
              <el-option
                v-for="item in frequencyOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="严重程度" prop="severity">
          <template #default="{ row }">
            <el-select
              v-model="row.severity"
              disabled
              :ref="'severity' + row.index"
            >
              <el-option
                v-for="item in frequencyOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="风险等级" prop="risklevel">
          <template #default="{ row }">
            <el-select
              v-model="row.risklevel"
              disabled
              :ref="'risklevel' + row.index"
            >
              <el-option
                v-for="item in severityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="评估人" prop="realname" />
        <el-table-column
          align="center"
          label="评估时间"
          prop="assdate"
          :formatter="formatDate"
        />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getPgrlt, getPgrltxq } from '@/api/risk/home'

  export default {
    name: 'Pgrlt',
    props: {
      orgid: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        loading: false,
        dialogVisible: false,
        detailLoading: false,
        detailData: [],
        tooltipVisible: false,
        tooltipContent: '',
        tooltipPosition: { x: 0, y: 0 },
        riskData: {
          // 新的数据格式：频率_严重程度 -> 数量
          // 例如: "1_1": 3, "1_2": 0, "2_1": 0, 等等
        },
        frequencyOptions: [
          {
            label: '很低',
            value: 1,
            color: '#52FFB7',
          },
          {
            label: '较低',
            value: 2,
            color: '#33D73B',
          },
          {
            label: '中等',
            value: 3,
            color: '#FFB500',
          },
          {
            label: '较高',
            value: 4,
            color: '#FF7F00',
          },
          {
            label: '很高',
            value: 5,
            color: '#E92129',
          },
        ],
        severityOptions: [
          {
            label: '很低',
            value: 1,
            color: '#52FFB7',
          },
          {
            label: '较低',
            value: 2,
            color: '#33D73B',
          },
          {
            label: '中等',
            value: 3,
            color: '#FFB500',
          },
          {
            label: '较高',
            value: 4,
            color: '#FF7F00',
          },
          {
            label: '很高',
            value: 5,
            color: '#E92129',
          },
        ],
      }
    },
    watch: {
      orgid: {
        handler() {
          this.fetchData()
        },
        immediate: true,
      },
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        try {
          this.loading = true
          const response = await getPgrlt({ company: this.orgid })
          if (response.code === 1 && response.data) {
            this.riskData = response.data
          }
        } catch (error) {
          console.error('获取风险数据失败:', error)
        } finally {
          this.loading = false
        }
      },

      // 计算每个格子的数值 (直接从接口返回的数据中获取)
      getCellValue(row, col) {
        const severity = 6 - row // 严重程度: 5,4,3,2,1 (从上到下)
        const frequency = col // 频率: 1,2,3,4,5 (从左到右)
        
        // 构建键名，格式为 "频率_严重程度"
        const key = `${frequency}_${severity}`
        
        // 直接从riskData中获取对应格子的数量
        return this.riskData[key] || 0
      },

      // 获取格子的CSS类名
      getCellClass(row, col) {
        // 横轴频率，纵轴严重程度
        // row=1对应严重程度5(最高), row=5对应严重程度1(最低)
        // col=1对应频率1(最低), col=5对应频率5(最高)
        const severity = 6 - row // severity: 5,4,3,2,1 (从上到下)
        const frequency = col // frequency: 1,2,3,4,5 (从左到右)

        // 绿色区域: 频率1-严重程度1, 频率1-严重程度2, 频率1-严重程度3, 频率2-严重程度1, 频率2-严重程度2
        if (
          (frequency === 1 &&
            (severity === 1 || severity === 2 || severity === 3)) ||
          (frequency === 2 && (severity === 1 || severity === 2))
        ) {
          return 'risk-low'
        }
        // 黄色区域: 频率1-严重程度4, 频率1-严重程度5, 频率2-严重程度3, 频率2-严重程度4, 频率2-严重程度5, 频率3-严重程度1, 频率3-严重程度2, 频率3-严重程度3, 频率3-严重程度4, 频率4-严重程度1, 频率4-严重程度2
        if (
          (frequency === 1 && (severity === 4 || severity === 5)) ||
          (frequency === 2 &&
            (severity === 3 || severity === 4 || severity === 5)) ||
          (frequency === 3 &&
            (severity === 1 ||
              severity === 2 ||
              severity === 3 ||
              severity === 4)) ||
          (frequency === 4 && (severity === 1 || severity === 2))
        ) {
          return 'risk-medium'
        }
        // 其余为红色
        return 'risk-high'
      },

      // 获取格子的样式
      getCellStyle(row, col) {
        // 横轴频率，纵轴严重程度
        // row=1对应严重程度5(最高), row=5对应严重程度1(最低)
        // col=1对应频率1(最低), col=5对应频率5(最高)
        const severity = 6 - row // severity: 5,4,3,2,1 (从上到下)
        const frequency = col // frequency: 1,2,3,4,5 (从左到右)

        let backgroundColor = '#4CAF50' // 绿色 - 低风险

        // 绿色区域: 频率1-严重程度1, 频率1-严重程度2, 频率1-严重程度3, 频率2-严重程度1, 频率2-严重程度2
        if (
          (frequency === 1 &&
            (severity === 1 || severity === 2 || severity === 3)) ||
          (frequency === 2 && (severity === 1 || severity === 2))
        ) {
          backgroundColor = '#4CAF50' // 绿色
        }
        // 黄色区域: 频率1-严重程度4, 频率1-严重程度5, 频率2-严重程度3, 频率2-严重程度4, 频率2-严重程度5, 频率3-严重程度1, 频率3-严重程度2, 频率3-严重程度3, 频率3-严重程度4, 频率4-严重程度1, 频率4-严重程度2
        else if (
          (frequency === 1 && (severity === 4 || severity === 5)) ||
          (frequency === 2 &&
            (severity === 3 || severity === 4 || severity === 5)) ||
          (frequency === 3 &&
            (severity === 1 ||
              severity === 2 ||
              severity === 3 ||
              severity === 4)) ||
          (frequency === 4 && (severity === 1 || severity === 2))
        ) {
          backgroundColor = '#FFEB3B' // 黄色
        }
        // 其余为红色
        else {
          backgroundColor = '#F44336' // 红色
        }

        return {
          backgroundColor,
          color: backgroundColor === '#FFEB3B' ? '#333' : '#fff', // 黄色背景用深色文字
          fontWeight: 'bold',
        }
      },

      // 处理单元格点击事件
      async handleCellClick(row, col) {
        const severity = 6 - row // 严重程度: 5,4,3,2,1 (从上到下)
        const frequency = col // 频率: 1,2,3,4,5 (从左到右)

        try {
          this.detailLoading = true
          this.dialogVisible = true

          const response = await getPgrltxq({
            company: this.orgid,
            frequency: frequency,
            severity: severity,
          })

          if (response.code === 1 && response.data) {
            // 直接使用接口返回的RiskPg数组，并为每个项目添加index
            this.detailData = (response.data.RiskPg || []).map((item, index) => ({
              ...item,
              index: index,
              // 确保数值类型正确
              frequency: parseInt(item.frequency),
              severity: parseInt(item.severity),
              risklevel: parseInt(item.risklevel)
            }))
            
            // 设置下拉框颜色
            this.$nextTick(() => {
              this.setSelectColors()
            })
          } else {
            this.detailData = []
            this.$message.warning('暂无相关数据')
          }
        } catch (error) {
          console.error('获取详细数据失败:', error)
          this.$message.error('获取数据失败，请稍后重试')
          this.detailData = []
        } finally {
          this.detailLoading = false
        }
      },

      // 关闭弹窗
      handleClose() {
        this.dialogVisible = false
        this.detailData = []
      },

      // 格式化日期
      formatDate(row, column) {
        let data = row[column.property]
        if (!data) return ''
        
        // 处理ISO日期格式
        const date = new Date(data)
        if (isNaN(date.getTime())) return data
        
        return date.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        })
      },

      // 设置下拉框颜色
      setSelectColors() {
        this.detailData.forEach((item, index) => {
          this.chageTextColor(
            item.frequency || 1,
            'frequency' + item.index
          )
          this.chageTextColor(
            item.severity || 1,
            'severity' + item.index
          )
          this.chageTextColor(
            item.risklevel || 1,
            'risklevel' + item.index
          )
        })
      },

      // 改变下拉框文字颜色
      chageTextColor($event, selectedRef) {
        const color = this.getColorByValue($event)
        // 改变下拉框颜色值
        if (this.$refs[selectedRef] && this.$refs[selectedRef].$el) {
          this.$refs[selectedRef].$el.children[0].children[0].style.color = color
        }
      },

      // 根据值获取对应颜色
      getColorByValue(value) {
        const colors = ['', '#52FFB7', '#33D73B', '#FFB500', '#FF7F00', '#E92129']
        return colors[value] || '#333'
      },

      // 显示tooltip
      showTooltip(event, row, col) {
        const value = this.getCellValue(row, col)
        if (value > 0) {
          this.tooltipContent = `数量: ${value}`
          // 使用 clientX/clientY 而不是 pageX/pageY，避免滚动影响
          this.tooltipPosition.x = event.clientX + 15
          this.tooltipPosition.y = event.clientY - 10
          this.tooltipVisible = true
        }
      },

      // 隐藏tooltip
      hideTooltip() {
        this.tooltipVisible = false
      },
    },
  }
</script>

<style scoped>
  .pgrlt-container {
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .pgrlt-title {
    font-size: 14px;
    text-align: center;
    font-weight: bold;
    margin-bottom: 20px;
    color: #ff8c00;
  }

  .pgrlt-chart {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    margin-bottom: 20px;
  }

  .y-axis {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 10px;
  }

  .y-axis-title {
    writing-mode: vertical-rl;
    text-orientation: mixed;
    font-weight: bold;
    margin-bottom: 10px;
    color: #666;
  }

  .y-labels {
    display: flex;
    flex-direction: column;
    height: 250px;
    justify-content: space-between;
    align-items: center;
  }

  .y-label {
    height: 50px;
    display: flex;
    align-items: center;
    font-weight: bold;
    color: #666;
  }

  .chart-main {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .x-axis {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 10px;
  }

  .x-labels {
    display: flex;
    width: 250px;
    justify-content: space-between;
  }

  .x-label {
    width: 50px;
    text-align: center;
    font-weight: bold;
    color: #666;
  }

  .x-axis-title {
    font-weight: bold;
    margin-top: 5px;
    color: #666;
  }

  .grid-container {
    display: flex;
    flex-direction: column;
    border: 2px solid #333;
  }

  .grid-row {
    display: flex;
  }

  .grid-cell {
    width: 50px;
    height: 50px;
    border: 1px solid #333;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    font-weight: bold;
    cursor: pointer;
    position: relative;
  }

  .cell-value {
    color: #fff;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  }

  /* 蓝色圆点样式 */
  .data-indicator {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 12px;
    height: 12px;
    background-color: #2196F3;
    border-radius: 50%;
    box-shadow: 0 0 6px rgba(33, 150, 243, 0.6);
    animation: pulse 2s infinite;
  }

  @keyframes pulse {
    0% {
      box-shadow: 0 0 4px rgba(33, 150, 243, 0.6);
    }
    50% {
      box-shadow: 0 0 8px rgba(33, 150, 243, 0.8);
    }
    100% {
      box-shadow: 0 0 4px rgba(33, 150, 243, 0.6);
    }
  }

  .legend {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 20px;
  }

  .legend-item {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .legend-color {
    width: 20px;
    height: 20px;
    border-radius: 4px;
  }

  .legend-color.low {
    background-color: #4caf50;
  }

  .legend-color.medium {
    background-color: #ffeb3b;
  }

  .legend-color.high {
    background-color: #f44336;
  }

  .risk-low {
    background-color: #4caf50 !important;
  }

  .risk-medium {
    background-color: #ffeb3b !important;
  }

  .risk-high {
    background-color: #f44336 !important;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .pgrlt-chart {
      flex-direction: column;
      align-items: center;
    }

    .y-axis {
      flex-direction: row;
      margin-right: 0;
      margin-bottom: 10px;
    }

    .y-axis-title {
      writing-mode: horizontal-tb;
      margin-right: 10px;
      margin-bottom: 0;
    }

    .y-labels {
      flex-direction: row;
      height: auto;
      width: 250px;
    }

    .legend {
      flex-direction: column;
      align-items: center;
    }
  }

  /* 自定义tooltip样式 */
  .custom-tooltip {
    position: fixed;
    background-color: rgba(0, 0, 0, 0.8);
    color: white;
    padding: 6px 10px;
    border-radius: 4px;
    font-size: 12px;
    z-index: 1000;
    pointer-events: none;
    white-space: nowrap;
  }
</style>
