<template>
  <div class="audit-screen-display">
    <!-- 头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentDate }} {{ currentTime }}</div>
        <!-- 隐藏年份选择器 -->
        <!-- <div class="year-selector">
          <el-select v-model="selectedYear" placeholder="选择年份" @change="handleYearChange" size="small">
            <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year + '年'"
              :value="year">
            </el-option>
          </el-select>
        </div> -->
      </div>
      <div class="header-title">
        <div class="title-main">系统画像</div>
      </div>
      <div class="header-right">
        <el-button
          class="close-btn"
          icon="el-icon-close"
          size="small"
          @click="closeScreen"
        >
          关闭
        </el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="screen-body">
      <!-- 上半部分 (3个组件均分高度) -->
      <div class="top-section">
        <!-- 左上大表格 -->
        <div class="panel-box top-left">
          <div class="panel-title">
            <i class="el-icon-document"></i>
            <span>系统项目列表</span>
          </div>
          <div class="data-table">
            <div class="table-header">
              <div class="th th-1">审计单位</div>
              <div class="th th-2">项目名称</div>
              <div class="th th-3">审计开始时间</div>
              <div class="th th-4">审计类型</div>
              <div class="th th-5">审计来源</div>
              <div class="th th-6">审计结束时间</div>
              <div class="th th-7">未整改数量</div>
              <div class="th th-8">已整改数量</div>
              <div class="th th-9">整改总数</div>
            </div>
            <div class="table-body">
              <div ref="list1TableBody" class="scroll-container">
                <div
                  v-for="(item, index) in list1Data"
                  :key="'list1-' + index + '-' + item.type2"
                  class="tr"
                >
                  <div class="td td-1">{{ item.type1 }}</div>
                  <div class="td td-2" :title="item.type2">
                    {{ item.type2 }}
                  </div>
                  <div class="td td-3">{{ item.type3 }}</div>
                  <div class="td td-4">{{ item.type4 }}</div>
                  <div class="td td-5">{{ item.type5 }}</div>
                  <div class="td td-6">{{ item.type6 }}</div>
                  <div class="td td-7">{{ item.type7 }}</div>
                  <div class="td td-8">{{ item.type8 }}</div>
                  <div class="td td-9">{{ item.type9 }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右上组件 -->
        <div class="panel-box top-right">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>系统项目统计</span>
          </div>
          <div class="chart-container">
            <div ref="chart2" class="chart"></div>
          </div>
        </div>

        <!-- 右中组件 -->
        <div class="panel-box middle-right">
          <div class="panel-title">
            <i class="el-icon-data-line"></i>
            <span>项目数趋势变化</span>
          </div>
          <div class="chart-container">
            <div ref="chart4" class="chart"></div>
          </div>
        </div>
      </div>

      <!-- 下半部分 (3个组件均分宽度) -->
      <div class="bottom-section">
        <!-- 左下组件 -->
        <div class="panel-box bottom-left">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>系统项目数一览表</span>
          </div>
          <div class="data-table">
              <div class="table-header">
                <div class="th th-1">被审计单位</div>
                <div class="th th-2">已完成项目数量</div>
                <div class="th th-3">未完成项目数量</div>
                <div class="th th-4">项目总数</div>
              </div>
              <div class="table-body">
                <div ref="list6TableBody" class="scroll-container">
                  <div
                    v-for="(item, index) in list6Data"
                    :key="'list6-' + index + '-' + item.bsjdw"
                    class="tr"
                  >
                    <div class="td td-1">{{ item.bsjdw }}</div>
                    <div class="td td-2">{{ item.wcs }}</div>
                    <div class="td td-3">{{ item.wwcs }}</div>
                    <div class="td td-4">{{ item.zs }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 中下组件 -->
          <div class="panel-box bottom-center">
            <div class="panel-title">
              <i class="el-icon-pie-chart"></i>
              <span>系统项目完成情况</span>
            </div>
            <div class="chart-container">
              <div ref="chart3" class="chart"></div>
            </div>
          </div>

          <!-- 右下组件 -->
          <div class="panel-box bottom-right-table">
            <div class="panel-title">
              <i class="el-icon-s-data"></i>
              <span>整改问题一览表</span>
            </div>
            <div class="data-table">
              <div class="table-header">
                <div class="th th-1">主管部门</div>
                <div class="th th-2">已整改数量</div>
                <div class="th th-3">未整改数量</div>
                <div class="th th-4">整改总数</div>
                <div class="th th-5">已销号数量</div>
                <div class="th th-6">未销号数量</div>
              </div>
              <div class="table-body">
                <div ref="list7TableBody" class="scroll-container">
                  <div
                    v-for="(item, index) in list7Data"
                    :key="'list7-' + index + '-' + item.orgname"
                    class="tr"
                  >
                    <div class="td td-1">{{ item.orgname }}</div>
                    <div class="td td-2">{{ item.yzg }}</div>
                    <div class="td td-3">{{ item.wzg }}</div>
                    <div class="td td-4">{{ item.zs }}</div>
                    <div class="td td-5">{{ item.yxh }}</div>
                    <div class="td td-6">{{ item.wxh }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import * as echarts from 'echarts'

  // API接口将在后续添加
  // import { } from '@/api/setting/portrait' // 新的接口路径

  export default {
    name: 'SystemPortraitScreen',
    props: {
      year: {
        type: [String, Number],
        default: new Date().getFullYear(),
      },
    },
    data() {
      return {
        currentDate: '',
        currentTime: '',
        timer: null,

        // 年份选择
        selectedYear: this.year || new Date().getFullYear(),
        yearOptions: [],

        // 7个部分的数据 - 留空,等待新接口
        list1Data: [],
        list1OriginalData: [],
        list2Data: [],
        list3Data: [],
        list4Data: { categories: [], series: [] },
        list5Data: { categories: [], series: [] },
        list6Data: [],
        list6OriginalData: [],
        list7Data: [],
        list7OriginalData: [],

        // 3个图表实例
        chart2: null,
        chart3: null,
        chart4: null,

        // 滚动相关
        scrollTimer: null,
        scrollTimer6: null,
        scrollTimer7: null,
        scrollSpeed: 50,
      }
    },
    mounted() {
      this.updateTime()
      this.timer = setInterval(this.updateTime, 1000)

      // 初始化年份选项
      this.initYearOptions()

      // 暂时不加载数据,等待新接口
      // this.fetchAllData()
    },
    beforeDestroy() {
      if (this.timer) {
        clearInterval(this.timer)
      }
      if (this.scrollTimer) {
        clearInterval(this.scrollTimer)
      }
      if (this.scrollTimer6) {
        clearInterval(this.scrollTimer6)
      }
      if (this.scrollTimer7) {
        clearInterval(this.scrollTimer7)
      }
      // 销毁图表实例
      if (this.chart2) this.chart2.dispose()
      if (this.chart3) this.chart3.dispose()
      if (this.chart4) this.chart4.dispose()
    },
    methods: {
      updateTime() {
        const now = new Date()
        this.currentDate = now.toLocaleDateString('zh-CN')
        this.currentTime = now.toLocaleTimeString('zh-CN')
      },
      closeScreen() {
        this.$emit('close')
      },

      // 初始化年份选项（最近10年）
      initYearOptions() {
        const currentYear = new Date().getFullYear()
        this.yearOptions = []
        for (let i = 0; i < 10; i++) {
          this.yearOptions.push(currentYear - i)
        }
      },

      // 年份改变事件
      handleYearChange(year) {
        this.selectedYear = year
        // 停止所有滚动
        if (this.scrollTimer) {
          clearInterval(this.scrollTimer)
          this.scrollTimer = null
        }
        if (this.scrollTimer6) {
          clearInterval(this.scrollTimer6)
          this.scrollTimer6 = null
        }
        if (this.scrollTimer7) {
          clearInterval(this.scrollTimer7)
          this.scrollTimer7 = null
        }
        // 重新加载所有数据
        this.fetchAllData()
      },

      // 获取所有数据 - 等待新接口
      async fetchAllData() {
        console.log('SystemPortraitScreen: 等待添加新的API接口')
        // 暂时不调用任何接口
      },
    },
  }
</script>

<style lang="scss" scoped>
  .audit-screen-display {
    width: 100vw;
    height: 100vh;
    background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%);
    color: #fff;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    position: relative;

    // 添加背景装饰
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image: radial-gradient(
          circle at 20% 50%,
          rgba(64, 158, 255, 0.1) 0%,
          transparent 50%
        ),
        radial-gradient(
          circle at 80% 80%,
          rgba(103, 194, 58, 0.1) 0%,
          transparent 50%
        );
      pointer-events: none;
    }

    // 大屏头部
    .screen-header {
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 40px;
      background: rgba(10, 14, 39, 0.8);
      border-bottom: 2px solid rgba(64, 158, 255, 0.3);
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
      position: relative;
      z-index: 10;

      .header-left,
      .header-right {
        flex: 1;
      }

      .header-left {
        display: flex;
        align-items: center;
        gap: 20px;

        .current-time {
          font-size: 16px;
          color: #409eff;
          font-family: 'Courier New', monospace;
          font-weight: 500;
          letter-spacing: 1px;
        }

        .year-selector {
          ::v-deep .el-select {
            width: 120px;
          }

          ::v-deep .el-input__inner {
            background: rgba(26, 31, 58, 0.8);
            border-color: rgba(64, 158, 255, 0.3);
            color: #409eff;
            font-weight: 500;

            &:hover {
              border-color: rgba(64, 158, 255, 0.6);
            }

            &:focus {
              border-color: #409eff;
            }
          }

          ::v-deep .el-input__suffix {
            .el-select__caret {
              color: #409eff;
            }
          }
        }
      }

      .header-title {
        flex: 2;
        text-align: center;

        .title-main {
          font-size: 36px;
          font-weight: bold;
          background: linear-gradient(90deg, #409eff 0%, #67c23a 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          letter-spacing: 3px;
        }
      }

      .header-right {
        display: flex;
        justify-content: flex-end;

        .close-btn {
          background: rgba(245, 108, 108, 0.2);
          border-color: #f56c6c;
          color: #f56c6c;
          transition: all 0.3s;

          &:hover {
            background: #f56c6c;
            color: #fff;
            transform: scale(1.1);
            box-shadow: 0 0 10px rgba(245, 108, 108, 0.6);
          }
        }
      }
    }

    // 大屏主体 - 根据图片的布局结构
    .screen-body {
      flex: 1;
      display: flex;
      flex-direction: column; // 纵向布局
      padding: 20px;
      gap: 15px;
      overflow: hidden;
      position: relative;
      z-index: 1;

      // 上半部分: 3个组件均分高度
      .top-section {
        flex: 1; // 占1/2高度
        display: flex;
        gap: 15px;
        min-height: 0;

        .top-left,
        .top-right,
        .middle-right {
          flex: 1; // 3个组件均分宽度
          min-height: 0;
        }
      }

      // 下半部分: 3个组件均分宽度
      .bottom-section {
        flex: 1; // 占1/2高度
        display: flex;
        gap: 15px;
        min-height: 0;

        .bottom-left,
        .bottom-center,
        .bottom-right-table {
          flex: 1; // 3个组件均分宽度
          min-height: 0;
        }
      }

      // 面板盒子通用样式
      .panel-box {
        background: rgba(26, 31, 58, 0.6);
        border: 1px solid rgba(64, 158, 255, 0.2);
        border-radius: 8px;
        padding: 15px;
        backdrop-filter: blur(10px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        transition: all 0.3s;

        &:hover {
          border-color: rgba(64, 158, 255, 0.5);
          box-shadow: 0 6px 20px rgba(64, 158, 255, 0.2);
        }

        .panel-title {
          display: flex;
          align-items: center;
          gap: 10px;
          font-size: 16px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 12px;
          padding-bottom: 8px;
          border-bottom: 1px solid rgba(64, 158, 255, 0.2);

          i {
            font-size: 18px;
          }
        }
      }

      // 表格样式
      .data-table {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow: hidden;

        .table-header,
        .tr {
          display: flex;
          align-items: center;
          padding: 8px 0;
          border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .table-header {
          font-weight: bold;
          color: #409eff;
          border-bottom: 2px solid rgba(64, 158, 255, 0.3);
          flex-shrink: 0;
        }

        .table-body {
          flex: 1;
          overflow: hidden;
          position: relative;

          .scroll-container {
            position: relative;
            transition: none;
          }

          .tr {
            transition: none;
            min-height: 41px;
            box-sizing: border-box;

            &:hover {
              background: rgba(64, 158, 255, 0.1);
            }
          }
        }

        .th,
        .td {
          padding: 0 8px;
          font-size: 12px;
          text-align: center;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .th {
          color: #409eff;
        }

        .td {
          color: rgba(255, 255, 255, 0.9);
        }
      }

      // 第1部分表格列宽
      .section-1 {
        .th-1,
        .td-1 {
          flex: 0 0 200px;
        }
        .th-2,
        .td-2 {
          flex: 0 0 150px;
        }
        .th-3,
        .td-3 {
          flex: 1;
          min-width: 110px;
        }
        .th-4,
        .td-4 {
          flex: 1;
          min-width: 130px;
        }
        .th-5,
        .td-5 {
          flex: 1;
          min-width: 100px;
        }
        .th-6,
        .td-6 {
          flex: 1;
          min-width: 110px;
        }
        .th-7,
        .td-7 {
          flex: 1;
          min-width: 90px;
        }
        .th-8,
        .td-8 {
          flex: 1;
          min-width: 90px;
        }
        .th-9,
        .td-9 {
          flex: 1;
          min-width: 90px;
        }
      }

      // 第6部分表格列宽
      .bottom-section .panel-box:nth-child(1) {
        .th-1,
        .td-1 {
          flex: 1;
          text-align: left;
        }
        .th-2,
        .td-2 {
          flex: 0 0 130px;
        }
        .th-3,
        .td-3 {
          flex: 0 0 130px;
        }
        .th-4,
        .td-4 {
          flex: 0 0 100px;
        }
      }

      // 第7部分表格列宽
      .bottom-section .panel-box:nth-child(3) {
        .th-1,
        .td-1 {
          flex: 1;
          text-align: left;
        }
        .th-2,
        .td-2 {
          flex: 0 0 100px;
        }
        .th-3,
        .td-3 {
          flex: 0 0 100px;
        }
        .th-4,
        .td-4 {
          flex: 0 0 100px;
        }
        .th-5,
        .td-5 {
          flex: 0 0 100px;
        }
        .th-6,
        .td-6 {
          flex: 0 0 100px;
        }
      }

      // 图表容器
      .chart-panel {
        .chart-container {
          flex: 1;
          min-height: 0;

          .chart {
            width: 100%;
            height: 100%;
          }
        }
      }
    }
  }
</style>
