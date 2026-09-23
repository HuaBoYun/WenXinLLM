<!-- <template>
  <div id="app" class="page">
    <div style="margin-bottom: 20px">
      <el-form ref="form" :inline="true" label-width="0" :model="queryForm">
        <el-form-item>
          <el-select v-model="queryForm.year" placeholder="请选择">
            <el-option
              v-for="item in yearInquiryData"
              :key="item"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="fetchData">
            查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="filterTable"
        border
        :data="projectSituationTable"
        style="width: 100%"
        v-loading="listLoading"
      >
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="被审计单位" prop="orgIdNames" />
        <el-table-column label="项目名称" prop="prjoectName" />
        <el-table-column label="计划年度" prop="planYear" />
        <el-table-column label="审计类型" prop="auditType" />
        <el-table-column label="项目经理" prop="realname" />
        <el-table-column label="项目状态" prop="examineTypes" />
        <el-table-column label="项目来源" prop="projectSource" />
        <el-table-column label="项目费用估算" prop="costs" />
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :page-sizes="[5, 10]"
      />
    </div>
    <el-row :gutter="10">
      <el-col :lg="12" :md="12" :sm="24">
        <div id="chats-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <div id="chats-2" style="width: 100%; height: 300px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    entryNumber,
    projectType,
    projectSituation,
    yearInquiry,
  } from '@/api/audit/sjfx'

  export default {
    name: 'Download',
    data() {
      return {
        tableData: [
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
        ],
        queryForm: {
          year: '',
          pageNumber: 1,
          pageSize: 5,
        },
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        activeYear: 2021,
        yearInquiryData: [],
        entryNumberData: [],
        projectTypeData: [],
        projectSituationTable: [],
        listLoading: false,
      }
    },
    created() {
      this.getYearInquiry()
      this.getEntryNumber()
      this.getProjectType()
      this.getProjectSituation()
    },
    methods: {
      changeActiveYear: function (year) {
        this.activeYear = year
      },
      filterHandler(value, row, column) {
        const property = column['date']
        return row[property] === value
      },
      getYearInquiry() {
        yearInquiry().then((res) => {
          this.yearInquiryData = res.data.list
        })
      },
      getProjectSituation() {
        this.listLoading = true
        projectSituation(this.queryForm).then((res) => {
          this.projectSituationTable = res.data.list
          this.total = res.data.total
          this.listLoading = false
        })
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getProjectSituation()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getProjectSituation()
      },
      getEntryNumber() {
        entryNumber(this.queryForm).then((res) => {
          this.entryNumberData = res.data.data
          this.chats1()
        })
      },
      chats1() {
        const chats1 = echarts.init(document.getElementById('chats-1'))
        // 指定图表的配置项和数据
        chats1.setOption({
          title: {
            text: '各公司审计项目数',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          series: [
            {
              name: '项目数量',
              type: 'pie',
              top: '15%',
              radius: ['30%', '60%'],
              data: this.entryNumberData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        })
      },
      getProjectType() {
        projectType(this.queryForm).then((res) => {
          this.projectTypeData = res.data.data
          this.chats2()
        })
      },
      chats2() {
        const chats2 = echarts.init(document.getElementById('chats-2'))

        // 指定图表的配置项和数据
        chats2.setOption({
          title: {
            text: '审计项目类型',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          series: [
            {
              name: '项目数量',
              type: 'pie',
              top: '15%',
              radius: ['30%', '60%'],
              data: this.projectTypeData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        })
      },
      fetchData() {
        this.getEntryNumber()
        this.getProjectType()
        this.getProjectSituation()
      },
      resetSearch() {
        this.queryForm.year = ''
        this.getEntryNumber()
        this.getProjectType()
        this.getProjectSituation()
      },
    },
  }
</script>
<style scoped>
  h5 {
    font-size: 18px;
    margin: 2px;
    color: #333;
  }
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
</style> -->

<template>
  <div>
    <div class="content">
      <el-card style="margin-bottom: 10px" class="title-card">
        <div class="title-text">智能审计管理驾驶舱</div>
        <div class="title-actions">
          <el-button
            type="primary"
            icon="el-icon-full-screen"
            @click="openFullScreen"
            class="screen-btn"
          >
            大屏
          </el-button>
        </div>
      </el-card>
      <el-card
        style="
          width: 100%;
          height: 60px;
          margin-bottom: 10px;
          padding-top: 0 !important;
        "
        class="search_input"
      >
        <el-form :inline="true" label-width="40px">
          <el-form-item label="年份">
            <el-date-picker
              v-model="year"
              type="year"
              placeholder="选择年份"
              value-format="yyyy"
              format="yyyy"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              type="primary"
              @click="fentchAll"
            />
          </el-form-item>
        </el-form>
      </el-card>
      <el-row :gutter="10">
        <el-col :span="24">
          <el-card
            id="chats-11"
            style="
              width: 100%;
              height: 360px;
              padding: 10px;
              margin-bottom: 10px;
            "
          ></el-card>
        </el-col>
        <el-col :lg="12" :md="6" :sm="24">
          <el-card
            id="chats-13"
            style="
              width: 100%;
              height: 360px;
              padding: 10px;
              margin-bottom: 10px;
            "
          ></el-card>
        </el-col>
        <el-col :lg="12" :md="6" :sm="24">
          <el-card
            id="chats-12"
            style="
              width: 100%;
              height: 360px;
              padding: 10px;
              margin-bottom: 10px;
            "
          ></el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <el-card
            id="chats-2"
            style="
              width: 100%;
              height: 360px;
              padding: 10px;
              margin-bottom: 10px;
            "
          ></el-card>
        </el-col>
      </el-row>
      <!-- <el-col :lg="12" :md="6" :sm="24">
          <el-card
            style="
              width: 100%;
              height: 360px;
              padding: 10px;
              margin-bottom: 10px;
            "
          >
            <div
              style="
                padding: 0px 0 20px 10px;
                color: #ff8c00;
                font-weight: bold;
              "
            >
              审计项目数一览表
            </div>
            <el-table :data="list2" height="280" style="overflow-y: auto">
              <el-table-column
                align="center"
                label="被审计单位"
                prop="bsjdw"
                show-overflow-tooltip
              />
              <el-table-column align="center" label="项目数量" prop="zs" />
            </el-table>
          </el-card>
        </el-col> -->
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <el-card
            class="table-card"
            style="
              width: 100%;
              padding: 10px;
              margin-bottom: 10px;
              position: relative;
              z-index: 2;
            "
          >
            <div class="table-title">审计项目计划完成情况一览表</div>
            <div
              style="
                height: 350px;
                overflow: hidden;
                position: relative;
                z-index: 1;
              "
            >
              <el-table
                ref="tableList2"
                :data="list2"
                height="350"
                show-summary
                :summary-method="getSummaries"
                @cell-click="handleTableCellClick"
                style="width: 100%; position: relative"
              >
                <el-table-column
                  align="center"
                  label="被审计单位"
                  prop="bsjdw"
                  show-overflow-tooltip
                />
                <el-table-column
                  align="center"
                  label="已完成项目数量"
                  prop="wcs"
                  class-name="clickable-column"
                />
                <el-table-column
                  align="center"
                  label="未完成项目数量"
                  prop="wwcs"
                  class-name="clickable-column"
                />
                <el-table-column
                  align="center"
                  label="项目总数"
                  prop="zs"
                  class-name="clickable-column"
                />
              </el-table>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <el-card
            class="table-card"
            style="width: 100%; padding: 10px; margin-bottom: 10px"
          >
            <div class="table-title">审计情况表</div>
            <el-table :data="list1" max-height="500" style="width: 100%">
              <el-table-column
                align="center"
                label="项目编号"
                prop="projectCode"
                show-overflow-tooltip
                width="180"
              >
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    @click="handleProjectCodeClick(scope.row)"
                    :style="{ color: 'var(--theme-color, #1890ff)' }"
                  >
                    {{ scope.row.projectCode }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="被审计单位"
                prop="orgIdNames"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="项目名称"
                prop="prjoectName"
                show-overflow-tooltip
              />

              <el-table-column
                align="center"
                label="计划年度"
                prop="planYear"
              />
              <el-table-column
                align="center"
                label="审计类型"
                prop="auditType"
              />
              <el-table-column
                align="center"
                label="项目经理"
                prop="realname"
              />
              <el-table-column
                align="center"
                label="项目状态"
                prop="examineTypes"
              />
              <!-- 隐藏费用估算列 -->
              <!-- <el-table-column
                align="center"
                label="项目费用估算"
                prop="costs"
              /> -->
            </el-table>
            <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :page-sizes="[5, 10, 20, 50]"
              :total="total1"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <ZgwtTable ref="zgwtTable" :year="year" />
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <SjfxTable ref="sjfxTable" :year="year" />
        </el-col>
      </el-row>
    </div>

    <!-- 审计项目统一详情弹窗 -->
    <AuditProjectUnifiedDetailDialog
      :visible.sync="auditProjectUnifiedDetailVisible"
      :query-type="detailQueryType"
      :query-value="detailQueryValue"
      :year="year"
      :detail-status="detailStatus"
    />

    <!-- 审计项目编辑弹窗 -->
    <IndexEdit ref="projectEdit" @fetch-data="refreshProjectList" />

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      custom-class="screen-dialog"
      @close="handleScreenClose"
    >
      <ScreenDisplay
        v-if="screenVisible"
        :year="year"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { formatYear } from '@/utils'
  import {
    getYHData,
    getSJXMData,
    getXMZTData,
    getBNMYData,
    getXMLXData,
    getSummary,
  } from '@/api/cwztfx.js'
  import ZgwtTable from '@/components/ZgwtTable.vue'
  import SjfxTable from '@/components/SjfxTable.vue'
  import AuditProjectUnifiedDetailDialog from './AuditProjectUnifiedDetailDialog.vue'
  import IndexEdit from '@/views/audit/project/components/IndexEditNew.vue'
  import ScreenDisplay from './sjfx/ScreenDisplay.vue'
  export default {
    name: 'Download',
    components: {
      ZgwtTable,
      SjfxTable,
      AuditProjectUnifiedDetailDialog,
      IndexEdit,
      ScreenDisplay,
    },
    data() {
      return {
        list1: [],
        list2: [],
        list11: [], // 审计项目数数据
        list12: [], // 审计项目计划完成情况数据
        list13: [], // 审计项目类型数据
        list2_trend: [], // 趋势图数据
        list14: [], // 预警指标数据
        total14: 0, // 预警指标总数
        total1: 0,
        total2: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
        },
        year: '',
        themeColor: '#1890ff', // 默认蓝色
        themeColors: {
          'vab-theme-default': '#1890ff', // 淡雅蓝主题
          'vab-theme-red': '#e50113', // 红色主题
          'vab-theme-ocean': '#1890ff', // 海洋主题（蓝色）
          'vab-theme-green': '#13ce66', // 绿色主题
          'vab-theme-white': '#515a6e', // 白色主题
        },
        // 审计项目统一详情弹窗
        auditProjectUnifiedDetailVisible: false,
        detailQueryType: '', // 'company'(按公司) | 'type'(按类型) | 'status'(按状态) | 'company-status'(按公司+状态)
        detailQueryValue: '', // 具体查询值
        detailStatus: null, // 状态筛选条件,用于company-status类型
        // 大屏相关
        screenVisible: false,
      }
    },
    created() {
      this.getYHData()
      this.getSJXMData()
      this.getXMZTData()
      this.getBNMYData()
      this.getXMLXData()
      this.getSummaryData()

      this.year = formatYear(new Date()).toString()
    },
    mounted() {
      // 初始化主题颜色
      this.initThemeColor()
      // 监听主题变化
      this.observeThemeChange()

      // 在DOM渲染完成后,为合计行添加点击事件监听
      this.$nextTick(() => {
        this.addSummaryRowClickListener()
      })
    },
    beforeDestroy() {
      // 清理主题观察器,防止内存泄漏
      if (this.themeObserver) {
        this.themeObserver.disconnect()
        this.themeObserver = null
      }
    },
    methods: {
      // 初始化主题颜色
      initThemeColor() {
        const body = document.body
        let detectedTheme = 'vab-theme-ocean' // 默认主题

        // 根据body的class获取主题色
        for (const themeClass in this.themeColors) {
          if (body.classList.contains(themeClass)) {
            this.themeColor = this.themeColors[themeClass]
            detectedTheme = themeClass
            break
          }
        }

        // 设置CSS变量,使样式能够动态响应主题变化
        document.documentElement.style.setProperty(
          '--theme-color',
          this.themeColor
        )
      },
      // 监听主题变化
      observeThemeChange() {
        const targetNode = document.body
        const config = { attributes: true, attributeFilter: ['class'] }

        const observer = new MutationObserver((mutations) => {
          mutations.forEach((mutation) => {
            if (
              mutation.type === 'attributes' &&
              mutation.attributeName === 'class'
            ) {
              this.initThemeColor()
              // 主题变化后重新渲染所有图表
              this.refreshAllCharts()
            }
          })
        })

        observer.observe(targetNode, config)
        this.themeObserver = observer
      },
      // 刷新所有图表
      refreshAllCharts() {
        this.$nextTick(() => {
          if (this.list11 && this.list11.length) {
            this.render11(this.list11)
          }
          if (this.list12 && this.list12.length) {
            this.render12(this.list12)
          }
          if (this.list13 && this.list13.length) {
            this.render13(this.list13)
          }
          if (this.list2_trend && this.list2_trend.length) {
            this.render2(this.list2_trend)
          }
          if (this.list14 && this.list14.length) {
            this.render14(this.list14, this.total14)
          }
        })
      },
      // 将hex颜色转换为rgba
      hexToRgba(hex, alpha) {
        let r = 0,
          g = 0,
          b = 0
        // 处理3位hex
        if (hex.length === 4) {
          r = parseInt(hex[1] + hex[1], 16)
          g = parseInt(hex[2] + hex[2], 16)
          b = parseInt(hex[3] + hex[3], 16)
        } else if (hex.length === 7) {
          // 处理6位hex
          r = parseInt(hex.substring(1, 3), 16)
          g = parseInt(hex.substring(3, 5), 16)
          b = parseInt(hex.substring(5, 7), 16)
        }
        return `rgba(${r}, ${g}, ${b}, ${alpha})`
      },
      // 使颜色变亮
      lightenColor(hex, percent) {
        let r = parseInt(hex.substring(1, 3), 16)
        let g = parseInt(hex.substring(3, 5), 16)
        let b = parseInt(hex.substring(5, 7), 16)

        r = parseInt((r * (100 + percent)) / 100)
        g = parseInt((g * (100 + percent)) / 100)
        b = parseInt((b * (100 + percent)) / 100)

        r = r < 255 ? r : 255
        g = g < 255 ? g : 255
        b = b < 255 ? b : 255

        const rr =
          r.toString(16).length === 1 ? '0' + r.toString(16) : r.toString(16)
        const gg =
          g.toString(16).length === 1 ? '0' + g.toString(16) : g.toString(16)
        const bb =
          b.toString(16).length === 1 ? '0' + b.toString(16) : b.toString(16)

        return '#' + rr + gg + bb
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getYHData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getYHData()
      },
      getYHData() {
        getYHData({ year: this.year, ...this.queryForm }).then((res) => {
          // 修复: 接口返回的数据在 data.pageInfo.tlist 中
          if (res.code === 1 && res.data) {
            if (res.data.pageInfo && res.data.pageInfo.tlist) {
              this.list1 = res.data.pageInfo.tlist
              this.total1 = res.data.pageInfo.totalRecord
            } else if (res.data.list) {
              // 兼容旧格式
              this.list1 = res.data.list
              this.total1 = res.data.total
            }
          }
        })
      },
      getSJXMData() {
        getSJXMData({ year: this.year }).then((res) => {
          this.list2 = res.data.list
          console.log('getSJXMData 接口返回 list2:', this.list2)
          // 使用固定高度后需要重新布局表格
          this.$nextTick(() => {
            if (this.$refs['tableList2']) {
              this.$refs['tableList2'].doLayout()
            }
          })
          const info =
            res.data.list &&
            res.data.list.map((item) => {
              return { value: item.zs, name: item.bsjdw }
            })
          this.list11 = info // 保存数据用于主题切换
          this.render11(info)
        })
      },
      // 处理表格单元格点击事件
      handleTableCellClick(row, column, cell, event) {
        // column.property 对应列的 prop 属性: wcs, wwcs, zs
        const prop = column.property

        // 只处理数字列的点击
        if (prop !== 'wcs' && prop !== 'wwcs' && prop !== 'zs') {
          return
        }

        // 根据 prop 确定点击类型
        let type = ''
        if (prop === 'wcs') {
          type = 'completed'
        } else if (prop === 'wwcs') {
          type = 'uncompleted'
        } else if (prop === 'zs') {
          type = 'total'
        }

        // 普通行: 按被审计单位查询
        this.detailQueryType = 'company'
        this.detailQueryValue = row.bsjdw // 被审计单位名称

        if (type === 'completed') {
          // 已完成项目: status = 3 或 4
          this.detailQueryType = 'company-status'
          this.detailStatus = '已完成' // 后端会转换为: status IN ('3', '4')
        } else if (type === 'uncompleted') {
          // 未完成项目: status != 3 和 status != 4
          this.detailQueryType = 'company-status'
          this.detailStatus = '未完成' // 后端会转换为: status != '3' AND status != '4'
        } else if (type === 'total') {
          // 项目总数: 不筛选状态,不传递status参数
          this.detailQueryType = 'company'
          this.detailStatus = null
        }

        this.auditProjectUnifiedDetailVisible = true
      },
      // 为合计行添加点击事件监听器
      addSummaryRowClickListener() {
        // 使用MutationObserver监听DOM变化,当合计行渲染后添加点击事件
        const observer = new MutationObserver(() => {
          const tableFooter = this.$refs.tableList2?.$el.querySelector(
            '.el-table__footer-wrapper'
          )
          if (tableFooter) {
            const summaryRow = tableFooter.querySelector('tr')
            if (summaryRow) {
              // 移除旧的事件监听器
              const cells = summaryRow.querySelectorAll('td')
              cells.forEach((cell, index) => {
                // 跳过第一列(被审计单位列)
                if (index === 0) return

                // 只处理第2,3,4列(已完成、未完成、项目总数)
                if (index >= 1 && index <= 3) {
                  // 移除旧的事件监听器
                  cell.onclick = null

                  // 添加新的事件监听器
                  cell.onclick = () => {
                    // 根据列索引确定类型
                    let type = ''
                    if (index === 1) {
                      type = 'completed' // 已完成
                    } else if (index === 2) {
                      type = 'uncompleted' // 未完成
                    } else if (index === 3) {
                      type = 'total' // 项目总数
                    }

                    // 合计行: 不传companyName,查询所有单位的数据
                    if (type === 'completed') {
                      this.detailQueryType = 'status'
                      this.detailQueryValue = '已完成'
                      this.detailStatus = '已完成'
                    } else if (type === 'uncompleted') {
                      this.detailQueryType = 'status'
                      this.detailQueryValue = '未完成'
                      this.detailStatus = '未完成'
                    } else if (type === 'total') {
                      this.detailQueryType = 'company'
                      this.detailQueryValue = '' // 空字符串表示查询所有单位
                      this.detailStatus = null
                    }

                    this.auditProjectUnifiedDetailVisible = true
                  }
                }
              })

              // 停止观察
              observer.disconnect()
            }
          }
        })

        // 开始观察表格的DOM变化
        const tableEl = this.$refs.tableList2?.$el
        if (tableEl) {
          observer.observe(tableEl, { childList: true, subtree: true })
        }
      },
      // 自定义合计行方法
      getSummaries(param) {
        const { columns, data } = param
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计'
            return
          }
          const values = data.map((item) => Number(item[column.property]))
          if (!values.every((value) => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
          } else {
            sums[index] = ''
          }
        })
        return sums
      },
      getXMZTData() {
        getXMZTData({ year: this.year }).then((res) => {
          console.log('getXMZTData 接口返回:', res)
          const info =
            res.data.list &&
            res.data.list.map((item) => {
              return { value: item.sl, name: item.status }
            })
          this.list12 = info || [] // 保存数据用于主题切换
          console.log('list12 数据:', this.list12)
          this.render12(info)
        })
      },
      getBNMYData() {
        getBNMYData({ year: this.year }).then((res) => {
          let info = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

          res.data.list &&
            res.data.list.forEach((item) => {
              info[item.yf - 1] = item.sl
            })

          this.list2_trend = info // 保存数据用于主题切换
          this.render2(info)
        })
      },
      getXMLXData() {
        getXMLXData({ year: this.year }).then((res) => {
          console.log('getXMLXData 接口返回:', res)
          const info =
            res.data.data &&
            res.data.data.map((item) => {
              return { value: item.value, name: item.name }
            })
          this.list13 = info || [] // 保存数据用于主题切换
          console.log('list13 数据:', this.list13)
          this.render13(info)
        })
      },
      getSummaryData() {
        getSummary().then((res) => {
          const data = res.data
          const info = [
            {
              value: data.total,
              name: `总数 ${data.total}`,
              itemStyle: { color: this.themeColor }, // 使用动态主题色
            },
            {
              value: data.greenNum,
              name: `绿色数量 ${data.greenNum}`,
              itemStyle: { color: '#52c41a' },
            },
            {
              value: data.yellowNum,
              name: `黄色数量 ${data.yellowNum}`,
              itemStyle: { color: '#faad14' },
            },
            {
              value: data.redNum,
              name: `红色数量 ${data.redNum}`,
              itemStyle: { color: '#f5222d' },
            },
          ]
          this.list14 = info // 保存数据用于主题切换
          this.total14 = data.total
          this.render14(info, data.total)
        })
      },
      render11(data) {
        var chartDom11 = document.getElementById('chats-11')
        var myChart11 = echarts.init(chartDom11)
        var option11
        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart11.resize()
          }, 100)
        })

        // 提取x轴数据（公司名称）和y轴数据（项目数量）
        const xAxisData = data.map((item) => item.name)
        const seriesData = data.map((item) => item.value)

        option11 = {
          title: {
            text: '审计项目数',
            left: 'left',
            textStyle: {
              fontSize: 16,
              color: this.themeColor,
              fontWeight: 'bold',
            },
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: this.themeColor,
            borderWidth: 1,
            textStyle: {
              color: '#595959',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '15%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: xAxisData,
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              lineStyle: {
                color: '#d9d9d9',
              },
            },
            axisLabel: {
              color: '#595959',
              interval: 0,
              rotate: xAxisData.length > 6 ? 30 : 0, // 数据多时适当旋转
              fontSize: 11,
              formatter: function (value) {
                // 如果存在逗号,则按逗号换行显示
                if (value && value.indexOf(',') !== -1) {
                  return value.replace(/,/g, '\n')
                }
                // 如果被审计单位名称超过8个字符,也进行换行
                if (value && value.length > 8) {
                  // 每8个字符换一行
                  let result = ''
                  for (let i = 0; i < value.length; i += 8) {
                    result += value.substr(i, 8) + '\n'
                  }
                  return result.trim()
                }
                return value
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLine: {
              lineStyle: {
                color: '#d9d9d9',
              },
            },
            axisLabel: {
              color: '#595959',
            },
            splitLine: {
              lineStyle: {
                color: '#f0f0f0',
                type: 'dashed',
              },
            },
          },
          series: [
            {
              name: '项目数量',
              type: 'bar',
              data: seriesData,
              itemStyle: {
                color: this.themeColor,
                borderRadius: [4, 4, 0, 0],
              },
              barWidth: '60%',
              emphasis: {
                itemStyle: {
                  color: this.hexToRgba(this.themeColor, 0.8),
                },
              },
              label: {
                show: true,
                position: 'top',
                color: '#595959',
                fontSize: 12,
              },
            },
          ],
        }

        option11 && myChart11.setOption(option11)

        // 添加点击事件监听器
        myChart11.on('click', (params) => {
          if (params.componentType === 'series') {
            this.detailQueryType = 'company'
            this.detailQueryValue = params.name
            this.auditProjectUnifiedDetailVisible = true
          }
        })
      },
      render12(data) {
        var chartDom12 = document.getElementById('chats-12')
        var myChart12 = echarts.init(chartDom12)
        var option12
        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart12.resize()
          }, 100)
        })

        option12 = {
          title: {
            text: '审计项目计划完成情况',
            left: 'left',
            textStyle: {
              fontSize: 16,
              color: this.themeColor,
              fontWeight: 'bold',
            },
          },
          tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: this.themeColor,
            borderWidth: 1,
            textStyle: {
              color: '#595959',
            },
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            y: 'middle',
            x: 'right',
            textStyle: {
              color: '#595959',
            },
          },
          color: ['#52c41a', '#faad14', this.themeColor, '#ff4d4f'],
          series: [
            {
              name: '完成情况',
              type: 'pie',
              radius: ['45%', '70%'],
              center: ['35%', '50%'],
              data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 15,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }

        option12 && myChart12.setOption(option12)

        // 添加点击事件监听器
        myChart12.on('click', (params) => {
          if (params.componentType === 'series') {
            this.detailQueryType = 'status'
            this.detailQueryValue = params.name
            this.auditProjectUnifiedDetailVisible = true
          }
        })
      },
      render13(data) {
        var chartDom13 = document.getElementById('chats-13')
        var myChart13 = echarts.init(chartDom13)
        var option13
        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart13.resize()
          }, 100)
        })

        option13 = {
          title: {
            text: '审计项目类型',
            left: 'left',
            textStyle: {
              fontSize: 16,
              color: this.themeColor,
              fontWeight: 'bold',
            },
          },
          tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: this.themeColor,
            borderWidth: 1,
            textStyle: {
              color: '#595959',
            },
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            y: 'middle',
            x: 'right',
            textStyle: {
              color: '#595959',
            },
          },
          color: [
            this.themeColor,
            '#13c2c2',
            '#52c41a',
            '#faad14',
            '#f5222d',
            '#722ed1',
          ],
          series: [
            {
              name: '项目类型',
              type: 'pie',
              radius: ['45%', '70%'],
              center: ['35%', '50%'],
              data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 15,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }

        option13 && myChart13.setOption(option13)

        // 添加点击事件监听器
        myChart13.on('click', (params) => {
          if (params.componentType === 'series') {
            this.detailQueryType = 'type'
            this.detailQueryValue = params.name
            this.auditProjectUnifiedDetailVisible = true
          }
        })
      },
      render2(data) {
        var chartDom2 = document.getElementById('chats-2')
        var myChart2 = echarts.init(chartDom2)
        var option2
        const colors = [this.themeColor]
        option2 = {
          color: colors,
          title: {
            text: '审计项目数量趋势变化',
            left: 'left',
            textStyle: {
              fontSize: 16,
              color: this.themeColor,
              fontWeight: 'bold',
            },
          },
          xAxis: [
            {
              type: 'category',
              data: [
                '1月',
                '2月',
                '3月',
                '4月',
                '5月',
                '6月',
                '7月',
                '8月',
                '9月',
                '10月',
                '11月',
                '12月',
              ],
              axisLine: {
                lineStyle: {
                  color: '#d9d9d9',
                },
              },
              axisLabel: {
                color: '#595959',
                interval: 0, // 显示所有标签
                rotate: 0, // 不旋转
                fontSize: 11, // 稍微调小字体
              },
            },
          ],
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: this.themeColor,
              },
            },
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: this.themeColor,
            borderWidth: 1,
            textStyle: {
              color: '#595959',
            },
          },
          yAxis: {
            type: 'value',
            axisLine: {
              lineStyle: {
                color: '#d9d9d9',
              },
            },
            axisLabel: {
              color: '#595959',
            },
            splitLine: {
              lineStyle: {
                color: '#f0f0f0',
                type: 'dashed',
              },
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%', // 增加底部边距，确保x轴标签完整显示
            top: '15%',
            containLabel: true,
          },
          series: [
            {
              data,
              type: 'line',
              smooth: true,
              symbol: 'circle',
              symbolSize: 8,
              lineStyle: {
                width: 3,
                color: this.themeColor,
              },
              itemStyle: {
                color: this.themeColor,
                borderColor: '#ffffff',
                borderWidth: 2,
              },
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: this.hexToRgba(this.themeColor, 0.3),
                    },
                    {
                      offset: 1,
                      color: this.hexToRgba(this.themeColor, 0.02),
                    },
                  ],
                },
              },
            },
          ],
        }

        option2 && myChart2.setOption(option2)

        // 添加点击事件监听器 - 点击月份查看该月的项目详情
        myChart2.on('click', (params) => {
          if (params.componentType === 'series') {
            this.detailQueryType = 'month'
            this.detailQueryValue = params.name // 例如: "1月", "2月" 等
            this.auditProjectUnifiedDetailVisible = true
          }
        })
      },
      render14(data, total) {
        var chartDom14 = document.getElementById('chats-14')
        var myChart14 = echarts.init(chartDom14)
        var option14
        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart14.resize()
          }, 100)
        })

        // 过滤掉总数项，只保留绿色、黄色、红色数据
        const filteredData = data.filter((item) => !item.name.includes('总数'))

        option14 = {
          title: [
            {
              text: `预警指标汇总`,
              left: 'left',
              textStyle: {
                fontSize: 16,
                color: this.themeColor,
                fontWeight: 'bold',
              },
            },
            {
              text: `总数量: ${total}`,
              right: 'right',
              top: 'top',
              textStyle: {
                fontSize: 18,
                color: this.themeColor,
                fontWeight: 'bold',
              },
            },
          ],
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: this.themeColor,
            borderWidth: 1,
            textStyle: {
              color: '#595959',
            },
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            y: 'middle',
            x: 'right',
            textStyle: {
              color: '#595959',
            },
          },
          color: ['#52c41a', '#faad14', '#f5222d'],
          series: [
            {
              name: '预警指标',
              type: 'pie',
              radius: ['45%', '70%'],
              center: ['35%', '50%'],
              data: filteredData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 15,
                  shadowOffsetX: 0,
                  shadowColor: this.hexToRgba(this.themeColor, 0.2),
                },
                label: {
                  show: true,
                  fontSize: 15,
                  fontWeight: 'bold',
                },
              },
              label: {
                color: '#595959',
                fontSize: 12,
                formatter: function (params) {
                  // 只有当value大于0时才显示百分比
                  if (params.value > 0) {
                    return `${params.name}: ${params.value}\n(${params.percent}%)`
                  } else {
                    return `${params.name}: ${params.value}`
                  }
                },
              },
              labelLine: {
                lineStyle: {
                  color: '#d9d9d9',
                },
              },
              itemStyle: {
                borderColor: '#ffffff',
                borderWidth: 2,
                borderRadius: 4,
              },
            },
          ],
        }

        option14 && myChart14.setOption(option14)
      },
      fentchAll() {
        this.getYHData()
        this.getSJXMData()
        this.getXMZTData()
        this.getBNMYData()
        this.getXMLXData()
        this.getSummaryData()
        // 触发整改问题表组件刷新数据
        this.$nextTick(() => {
          if (this.$refs.zgwtTable) {
            this.$refs.zgwtTable.getZgwtData()
          }
          if (this.$refs.sjfxTable) {
            this.$refs.sjfxTable.getSjslData()
          }
        })
      },
      // 处理项目编号点击事件
      handleProjectCodeClick(row) {
        // 调用编辑组件的showEdit方法,传入行数据和禁用标志(true=只读模式)
        this.$refs['projectEdit'].showEdit(row, true, this.planNum)
      },
      // 刷新项目列表(编辑弹窗回调)
      refreshProjectList() {
        // 可以在这里添加刷新逻辑,如果需要的话
        // 暂时不做操作,因为审计情况表可能不需要实时刷新
      },
      // 打开全屏大屏
      openFullScreen() {
        // 打开大屏前不再刷新所有数据,直接打开大屏
        // this.fentchAll() // 暂时注释,不调用父组件的接口
        this.screenVisible = true

        // 使用浏览器全屏API
        this.$nextTick(() => {
          this.enterFullscreen()
        })
      },
      // 关闭全屏大屏
      closeFullScreen() {
        this.screenVisible = false
        // 退出浏览器全屏
        this.exitFullscreen()
      },
      // 大屏弹窗关闭事件
      handleScreenClose() {
        this.screenVisible = false
        // 退出浏览器全屏
        this.exitFullscreen()
      },
      // 进入浏览器全屏模式
      enterFullscreen() {
        const element = document.documentElement
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullscreen) {
          // Safari
          element.webkitRequestFullscreen()
        } else if (element.mozRequestFullScreen) {
          // Firefox
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      },
      // 退出浏览器全屏模式
      exitFullscreen() {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitExitFullscreen) {
          // Safari
          document.webkitExitFullscreen()
        } else if (document.mozCancelFullScreen) {
          // Firefox
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          // IE11
          document.msExitFullscreen()
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  /* 参考 czyxzl 页面的现代化浅色背景 */
  .content {
    padding: 20px;
    background: #f0f2f5;
    min-height: calc(100vh - 84px);
  }

  /* 标题卡片 - 参考 czyxzl 的页面头部样式，使用主题色 */
  .title-card {
    margin-bottom: 20px;
    background: #fff !important;
    border-radius: 4px !important;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
    border: none !important;
    position: relative;

    ::v-deep .el-card__body {
      padding: 15px 20px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }

  .title-text {
    font-size: 20px;
    color: #303133 !important;
    font-weight: bold;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 10px;
    flex: 1;
    justify-content: center;

    &::before {
      content: '\e6a3';
      font-family: element-icons !important;
      font-size: 24px;
      color: var(--theme-color, #1890ff);
    }
  }

  .title-actions {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);

    .screen-btn {
      background: var(--theme-color, #1890ff);
      border-color: var(--theme-color, #1890ff);
      font-size: 14px;
      padding: 10px 20px;
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: color-mix(in srgb, var(--theme-color, #1890ff) 80%, white);
        border-color: color-mix(
          in srgb,
          var(--theme-color, #1890ff) 80%,
          white
        );
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(64, 158, 255, 0.3);
      }
    }
  }

  /* 卡片通用样式 - 参考 czyxzl */
  .el-card {
    background: #fff !important;
    border-radius: 4px !important;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
    transition: all 0.3s;
    border: none !important;

    &:hover {
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15) !important;
      transform: translateY(-2px);
    }

    ::v-deep .el-card__body {
      padding: 20px;
    }
  }

  /* 表格卡片样式 */
  .table-card {
    background: #fff !important;
    border-radius: 4px !important;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15) !important;
      transform: translateY(-2px);
    }

    ::v-deep .el-card__body {
      padding: 20px;
    }
  }

  /* 搜索区域样式 - 参考 czyxzl，不带颜色边框 */
  .search_input {
    background: #fff !important;
    margin-bottom: 20px;
    padding: 15px 20px !important;
    border-radius: 4px !important;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
    border: none !important;

    ::v-deep .el-card__body {
      padding: 0 !important;
    }

    ::v-deep .el-form {
      margin: 0;
    }

    ::v-deep .el-form-item {
      margin-bottom: 0;
    }

    ::v-deep .el-form-item__label {
      color: #606266;
      font-weight: 500;
    }

    ::v-deep .el-input__inner {
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      transition: all 0.3s;

      &:focus {
        border-color: var(--theme-color, #1890ff);
      }
    }

    ::v-deep .el-button--primary {
      background: var(--theme-color, #1890ff);
      border-color: var(--theme-color, #1890ff);
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: color-mix(in srgb, var(--theme-color, #1890ff) 80%, white);
        border-color: color-mix(
          in srgb,
          var(--theme-color, #1890ff) 80%,
          white
        );
      }
    }
  }

  /* 表格样式优化 - 参考 czyxzl 简洁风格，使用主题色 */
  .el-table {
    background: #fff !important;
    border-radius: 4px;
    overflow: hidden;

    ::v-deep th.el-table__cell {
      background: linear-gradient(
        180deg,
        color-mix(in srgb, var(--theme-color, #1890ff) 8%, transparent) 0%,
        color-mix(in srgb, var(--theme-color, #1890ff) 15%, transparent) 100%
      ) !important;
      color: #303133 !important;
      font-weight: 600;
      border-bottom: 2px solid var(--theme-color, #1890ff) !important;

      .cell {
        color: #303133 !important;
      }
    }

    ::v-deep td.el-table__cell {
      background: #fff !important;
      color: #606266 !important;
      border-bottom: 1px solid #ebeef5 !important;
    }

    ::v-deep tr:hover > td {
      background: color-mix(
        in srgb,
        var(--theme-color, #1890ff) 5%,
        transparent
      ) !important;
    }

    ::v-deep .el-table__body tr.current-row > td {
      background: color-mix(
        in srgb,
        var(--theme-color, #1890ff) 10%,
        transparent
      ) !important;
    }

    /* 表格边框 */
    &.el-table--border {
      border: 1px solid #ebeef5 !important;

      &::after,
      &::before {
        background-color: #ebeef5 !important;
      }
    }

    ::v-deep .el-table__fixed-right::before,
    ::v-deep .el-table__fixed::before {
      background-color: #ebeef5 !important;
    }

    /* 合计行样式 */
    ::v-deep .el-table__footer-wrapper .el-table__footer .clickable-cell {
      color: var(--theme-color, #1890ff) !important;
      cursor: pointer !important;
      text-decoration: underline;
      text-decoration-style: dotted;
      text-underline-offset: 2px;

      &:hover {
        font-weight: bold;
        text-decoration-style: solid;
      }
    }

    /* 可点击列样式 - 包括普通行和合计行，使用主题色 */
    ::v-deep .clickable-column {
      cursor: pointer !important;

      .cell {
        color: var(--theme-color, #1890ff) !important;
        transition: all 0.3s ease;

        &:hover {
          font-weight: bold;
        }
      }
    }
  }

  /* 分页样式 - 参考 czyxzl 简洁风格，使用主题色 */
  .el-pagination {
    margin-top: 20px;
    text-align: center;

    ::v-deep .el-pagination__total,
    ::v-deep .el-pagination__jump {
      color: #606266;
    }

    ::v-deep .el-pager li {
      background: #fff;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      transition: all 0.3s;
      margin: 0 2px;

      &:hover {
        color: var(--theme-color, #1890ff);
        border-color: var(--theme-color, #1890ff);
      }

      &.active {
        background: var(--theme-color, #1890ff);
        color: #fff;
        border-color: var(--theme-color, #1890ff);
      }
    }

    ::v-deep button {
      background: #fff;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      transition: all 0.3s;

      &:hover:not(:disabled) {
        color: var(--theme-color, #1890ff);
        border-color: var(--theme-color, #1890ff);
      }

      &:disabled {
        opacity: 0.5;
        background: #f5f5f5;
        color: #c0c4cc;
        border-color: #dcdfe6;
      }
    }

    ::v-deep .el-pagination__sizes .el-select .el-input__inner {
      background: #fff;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
    }
  }

  /* 表格内标题样式 - 参考 czyxzl，使用主题色 */
  .table-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: bold;
    color: var(--theme-color, #1890ff) !important;
    padding: 0 0 15px 0;
    margin: 0;

    &::before {
      content: '';
      display: inline-block;
      width: 3px;
      height: 16px;
      background: var(--theme-color, #1890ff);
      border-radius: 2px;
    }
  }

  /* 图表容器样式 */
  #chats-11,
  #chats-12,
  #chats-13,
  #chats-14,
  #chats-2 {
    transition: all 0.3s;
  }

  /* 响应式调整 - 参考 czyxzl */
  @media screen and (max-width: 768px) {
    .content {
      padding: 10px;
    }

    .title-text {
      font-size: 18px;
    }

    .el-card {
      margin-bottom: 10px;
    }

    ::v-deep .el-card__body {
      padding: 12px !important;
    }
  }

  // 大屏弹窗样式
  ::v-deep .screen-dialog {
    background: transparent;
    margin: 0;
    padding: 0;

    .el-dialog__header {
      display: none;
    }

    .el-dialog__body {
      padding: 0;
      height: 100vh;
      overflow: hidden;
    }
  }
</style>
