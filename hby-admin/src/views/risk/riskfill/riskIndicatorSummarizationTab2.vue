<template>
  <div class="risk-indicator-summarization">
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          checkable
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item v-for="(item, index) in searchItem" :key="index">
            <div v-if="item.name === '公司'" style="display: flex">
              <el-input
                placeholder="请输入公司名称"
                disabled
                v-model="queryForm.companyName"
                style="margin-right: 10px"
                width="100%"
              />
              <el-button type="primary" @click="$refs.companyTree.showEdit()">
                选择
              </el-button>
            </div>
            <el-select
              v-model="queryForm.jd"
              clearable
              placeholder="请选择季度"
              v-if="item.name === '季度'"
            >
              <el-option
                v-for="option in quarterOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              ></el-option>
            </el-select>
            <el-date-picker
              v-if="item.name === '年度'"
              v-model="queryForm.year"
              type="year"
              format="yyyy"
              value-format="yyyy"
              placeholder="年度"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetSearch()" type="primary">重置</el-button>
          </el-form-item>
          <el-form-item>
            <el-tooltip
              class="item"
              effect="dark"
              content="搜索筛选"
              placement="top"
            >
              <el-popover placement="left" trigger="click">
                <filter-search
                  v-if="true"
                  :list="searchAll"
                  :name="localKey"
                  @updateSearchShow="initSearch"
                />
                <el-button slot="reference" style="height: 32px">
                  <vab-icon icon="filter" :is-custom-svg="true" />
                </el-button>
              </el-popover>
            </el-tooltip>
          </el-form-item>
          <el-form-item>
            <span
              :class="searchMore ? 'search-more is-opened' : 'search-more'"
              @click="showMore"
            >
              <span>{{ searchMore ? '收起' : '展开' }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <div class="risk-container">
      <el-button type="primary" @click="handleExport">导出</el-button>
      <!-- 表格标题 -->
      <div class="table-header">
        <h2>{{ dynamicTitle }}</h2>
        <div class="reporting-info">
          <span>填报单位：{{ reportingUnit }}</span>
          <span>填报时间：{{ reportingDate }}</span>
          <span>填报人：{{ reporter }}</span>
          <span>联系方式：{{ contact }}</span>
        </div>
      </div>

      <!-- 重大经营风险监测预警指标体系表格 -->
      <div class="risk-table-container">
        <table class="risk-table">
          <!-- 多级表头 -->
          <thead>
            <tr>
              <th rowspan="2">主要风险</th>
              <th rowspan="2">监测指标</th>
              <th colspan="3">预警阈值</th>
              <th rowspan="2">第三季度末最新值</th>
              <th rowspan="2">同比变动变化率或增减额</th>
              <th rowspan="2">环比变动变化率或增减额</th>
              <th rowspan="2">变化原因说明</th>
              <th rowspan="2">统计区间</th>
              <th rowspan="2">填报单位</th>
            </tr>
            <tr>
              <!-- 预警阈值子表头 -->
              <th>绿灯</th>
              <th>黄灯</th>
              <th>红灯</th>
            </tr>
          </thead>

          <!-- 表格内容 -->
          <tbody>
            <!-- 质量可靠性和安全环保风险 -->
            <tr>
              <td rowspan="4">质量可靠性和安全环保风险</td>
              <td>一般及以上辐射事故（含各类放射源丢失、失控、误照射等）</td>
              <td>事故次数=0</td>
              <td>一般事故次数≥1</td>
              <td>较大及以上事故次数≥1</td>
              <td>{{ getRiskDataByField('FSSG', 0) }}</td>
              <td>{{ getRiskDataByField('FSSG', 1) }}</td>
              <td>{{ getRiskDataByField('FSSG', 2) }}</td>
              <td>{{ getRiskDataByField('FSSG', 3) }}</td>
              <td>{{ getRiskDataByField('FSSG', 4) }}</td>
              <td>{{ getRiskDataByField('FSSG', 5) }}</td>
            </tr>
            <tr>
              <td>安全生产事故</td>
              <td>事故次数=0</td>
              <td>一般事故次数≥1</td>
              <td>较大及以上事故次数≥1</td>
              <td>{{ getRiskDataByField('SCSG', 0) }}</td>
              <td>{{ getRiskDataByField('SCSG', 1) }}</td>
              <td>{{ getRiskDataByField('SCSG', 2) }}</td>
              <td>{{ getRiskDataByField('SCSG', 3) }}</td>
              <td>{{ getRiskDataByField('SCSG', 4) }}</td>
              <td>{{ getRiskDataByField('SCSG', 5) }}</td>
            </tr>
            <tr>
              <td>环境事件</td>
              <td>事故次数=0</td>
              <td>一般事故次数≥1</td>
              <td>较大及以上事故次数≥1</td>
              <td>{{ getRiskDataByField('HJSJ', 0) }}</td>
              <td>{{ getRiskDataByField('HJSJ', 1) }}</td>
              <td>{{ getRiskDataByField('HJSJ', 2) }}</td>
              <td>{{ getRiskDataByField('HJSJ', 3) }}</td>
              <td>{{ getRiskDataByField('HJSJ', 4) }}</td>
              <td>{{ getRiskDataByField('HJSJ', 5) }}</td>
            </tr>
            <tr>
              <td>职业病危害事故</td>
              <td>事故次数=0</td>
              <td>一般事故次数≥1</td>
              <td>重大及以上事故次数≥1</td>
              <td>{{ getRiskDataByField('ZYB', 0) }}</td>
              <td>{{ getRiskDataByField('ZYB', 1) }}</td>
              <td>{{ getRiskDataByField('ZYB', 2) }}</td>
              <td>{{ getRiskDataByField('ZYB', 3) }}</td>
              <td>{{ getRiskDataByField('ZYB', 4) }}</td>
              <td>{{ getRiskDataByField('ZYB', 5) }}</td>
            </tr>

            <!-- 金融与债权资产经营风险 -->
            <tr>
              <td rowspan="1">金融与债权资产经营风险</td>
              <td>资产负债率及经营性现金流</td>
              <td>资产负债率≤70% 且经营性现金流为正</td>
              <td>
                资产负债率>70%
                且经营性现金流为负，但通过采取措施足以偿还一年内到期带息债务
              </td>
              <td>
                资产负债率>70%
                且经营性现金流为负，且预计无法偿还一年内到期带息债务
              </td>
              <td>{{ getRiskDataByField('ZCFZ', 0) }}</td>
              <td>{{ getRiskDataByField('ZCFZ', 1) }}</td>
              <td>{{ getRiskDataByField('ZCFZ', 2) }}</td>
              <td>{{ getRiskDataByField('ZCFZ', 3) }}</td>
              <td>{{ getRiskDataByField('ZCFZ', 4) }}</td>
              <td>{{ getRiskDataByField('ZCFZ', 5) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 指标说明 -->
      <div class="table-notes">
        <h3>指标说明：</h3>
        <p>
          1.各二级单位要围绕所负责风险做好关键风险指标的监测。自2025年一季度起，相关二级单位每季度末报送最新值。
        </p>
        <p>2. 后续可根据实际运行效果、新风险形式等情况动态优化调整指标体系。</p>
      </div>
    </div>

    <!-- 公司选择组件 -->
    <select-company
      ref="companyTree"
      @handleChooseCompany="handleChooseCompany"
    />
  </div>
</template>

<script>
  import { zdExportNew, getZdHzListNew } from '@/api/risk/monitoringFill'
  import filterSearch from '@/components/filterSearch.vue'
  import selectCompany from '@/views/internal/internalTest/components/ChooseCompanys.vue'

  export default {
    name: 'RiskIndicatorSummarizationTab2',
    components: {
      filterSearch,
      selectCompany,
    },
    data() {
      return {
        quarterOptions: [
          { value: '一季度', label: '一季度' },
          { value: '二季度', label: '二季度' },
          { value: '三季度', label: '三季度' },
          { value: '四季度', label: '四季度' },
        ],
        localKey: 'risk-riskIndicatorSummarization-tab2-search',
        tableKey: 'risk-riskIndicatorSummarization-tab2-list',
        searchItem: [],
        searchAll: this.getFiled(),
        searchMore: true,

        queryForm: {
          jd: (() => {
            const month = new Date().getMonth() + 1
            if (month >= 1 && month <= 3) return '一季度'
            if (month >= 4 && month <= 6) return '二季度'
            if (month >= 7 && month <= 9) return '三季度'
            return '四季度'
          })(),
          year: new Date().getFullYear().toString(),
          orgid: '1000',
          companyName: '',
        },
        reportingUnit: '',
        reportingDate: '',
        reporter: '',
        contact: '',
        riskData: [],
      }
    },
    computed: {
      // 动态标题
      dynamicTitle() {
        return `${this.reportingUnit || ''}${
          this.queryForm.year
        }年第${this.getQuarterNumber(this.queryForm.jd)}季度重大经营指标监测表`
      },
      // 本期表头
      currentPeriodHeader() {
        return `${this.queryForm.year}年${this.getQuarterNumber(
          this.queryForm.jd
        )}季度`
      },
      // 上期表头
      lastPeriodHeader() {
        const lastQuarter = this.getPreviousQuarter(this.queryForm.jd)
        const year =
          this.queryForm.jd === '一季度'
            ? this.queryForm.year - 1
            : this.queryForm.year
        return `${year}年${this.getQuarterNumber(lastQuarter)}季度`
      },
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.queryForm.companyName = userInfo.linkOrg.orgname
      this.queryForm.orgid = userInfo.linkOrg.orgid
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async fetchData() {
        // 调用风险监测预警指标的API
        const { code, data, msg } = await getZdHzListNew({
          ...this.queryForm,
        })
        this.riskData = data.result || []
        this.reportingUnit = data.orgName
        this.reporter = data.staffName
        this.reportingDate = data.time
      },
      // 获取当前季度
      getCurrentQuarter() {
        const month = new Date().getMonth() + 1
        if (month >= 1 && month <= 3) return '一季度'
        if (month >= 4 && month <= 6) return '二季度'
        if (month >= 7 && month <= 9) return '三季度'
        return '四季度'
      },
      // 将季度文字转换为数字
      getQuarterNumber(quarter) {
        const quarterMap = {
          一季度: '一',
          二季度: '二',
          三季度: '三',
          四季度: '四',
        }
        return quarterMap[quarter] || '一'
      },
      // 获取上一季度
      getPreviousQuarter(currentQuarter) {
        const quarterMap = {
          一季度: '四季度',
          二季度: '一季度',
          三季度: '二季度',
          四季度: '三季度',
        }
        return quarterMap[currentQuarter] || '四季度'
      },
      // 根据字段名和列索引获取数据
      getDataByField(fieldName, columnIndex) {
        if (!this.operatingData || !this.operatingData[columnIndex]) {
          return ''
        }
        return this.operatingData[columnIndex][fieldName] || ''
      },

      // 获取风险监测数据字段值 - 采用与Tab1相同的简单映射方式
      getRiskDataByField(fieldName, columnIndex) {
        if (!this.riskData || !this.riskData[columnIndex]) {
          return ''
        }
        return this.riskData[columnIndex][fieldName] || ''
      },
      // 导出功能
      async handleExport() {
        let params = {
          year: this.queryForm.year,
          jd: this.queryForm.jd,
          orgid: this.queryForm.orgid,
        }
        const data = await zdExportNew(params)
        let fileName = `重大经营指标监测.xls`
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      // 更新公司信息
      handleChooseCompany(node) {
        this.queryForm.companyName = node.label
        this.queryForm.orgid = node.id
      },
      // 重置搜索
      resetSearch() {
        this.queryForm = {
          jd: this.getCurrentQuarter(),
          year: new Date().getFullYear().toString(),
          orgid: this.queryForm.orgid,
          companyName: this.queryForm.companyName,
        }
        this.fetchData()
      },
      // 获取搜索字段
      getFiled() {
        return [
          { key: 'companyName', name: '公司', show: true },
          { key: 'jd', name: '季度', show: true },
          { key: 'year', name: '年度', show: true },
        ]
      },
      // 初始化搜索
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 初始化表格
      initTable() {
        // 表格初始化逻辑
      },
      // 展开/收起搜索
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
    },
  }
</script>

<style scoped>
  .risk-container {
    font-family: 'Microsoft YaHei', Arial, sans-serif;
    color: #333;
    margin: 0 auto;
    background-color: #fff;
  }

  .table-header {
    text-align: center;
    margin-bottom: 25px;
    border-bottom: 2px solid #999;
    padding-bottom: 15px;
  }

  .table-header h2 {
    margin-bottom: 10px;
    font-size: 22px;
  }

  .reporting-info {
    display: flex;
    justify-content: center;
    gap: 20px;
    font-size: 14px;
  }

  .risk-table-container {
    overflow-x: auto;
    margin-bottom: 30px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    padding: 10px;
  }

  .risk-table {
    width: 100%;
    border-collapse: collapse;
    min-width: 1200px;
  }

  .risk-table th,
  .risk-table td {
    border: 1px solid #dee2e6;
    padding: 12px 10px;
    text-align: center;
    font-size: 13px;
    line-height: 1.5;
    min-width: 100px;
    word-wrap: break-word;
    word-break: break-all;
  }

  .risk-table th {
    background-color: #f8f9fa;
    font-weight: bold;
  }

  .risk-category {
    background-color: #f0f9ff;
    font-weight: bold;
    writing-mode: vertical-rl;
    text-orientation: mixed;
    width: 60px;
  }

  /* 预警阈值列样式 */
  .risk-table th:nth-child(3),
  .risk-table th:nth-child(4),
  .risk-table th:nth-child(5),
  .risk-table td:nth-child(3),
  .risk-table td:nth-child(4),
  .risk-table td:nth-child(5) {
    min-width: 120px;
    text-align: center;
  }

  /* 监测指标列样式 */
  .risk-table th:nth-child(2),
  .risk-table td:nth-child(2) {
    min-width: 200px;
    text-align: center;
    padding-left: 12px;
  }

  .table-notes {
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 4px;
    border-left: 4px solid #776e6e;
  }

  .table-notes h3 {
    margin: 15px 0 10px;
    font-size: 16px;
    color: #333;
  }

  .table-notes p {
    margin: 5px 0;
    font-size: 13px;
    line-height: 1.6;
    color: #666;
  }

  .search-more {
    cursor: pointer;
    color: #409eff;
    font-size: 14px;
    display: flex;
    align-items: center;
  }

  .search-more i {
    margin-left: 5px;
    transition: transform 0.3s;
  }

  .search-more.is-opened i {
    transform: rotate(180deg);
  }

  /* 响应式处理 */
  @media (max-width: 1200px) {
    .risk-table {
      font-size: 11px;
    }

    .risk-table th,
    .risk-table td {
      padding: 6px;
      min-width: 70px;
    }
  }
</style>
