<template>
  <div class="cwztfx-page">
    <top-filter @filter-change="onFilterChange" />
    <stat-cards :data="statData" :filters="filters" />
    <!-- <div class="custom-tabs">
      <span
        v-for="tab in tabs"
        :key="tab.name"
        :class="['custom-tab', { active: activeTab === tab.name }]"
        @click="activeTab = tab.name"
      >
        {{ tab.label }}
      </span>
    </div>
    <div class="tab-content">
      <div v-if="activeTab === 'base'">
        <div class="base-info">
          <bar-chart :data="barChartData" />
          <stat-table :data="tableData" />
        </div>
      </div>
      <div v-else-if="activeTab === 'progress'"></div>
      <div v-else-if="activeTab === 'time'"></div>
      <div v-else-if="activeTab === 'draft'"></div>
      <div v-else-if="activeTab === 'amount'"></div>
      <div v-else-if="activeTab === 'detail'"></div>
    </div> -->
  </div>
</template>

<script>
  import TopFilter from './components/TopFilter.vue'
  import StatCards from './components/StatCards.vue'
  import BarChart from './components/BarChart.vue'
  import StatTable from './components/StatTable.vue'

  export default {
    name: 'Cwztfx',
    components: { TopFilter, StatCards, BarChart, StatTable },
    data() {
      return {
        activeTab: 'base',
        tabs: [
          { label: '基本情况', name: 'base' },
          { label: '项目进度', name: 'progress' },
          { label: '时间进度', name: 'time' },
          { label: '底稿情况', name: 'draft' },
          { label: '金额统计', name: 'amount' },
          { label: '明细表', name: 'detail' },
        ],
        statData: {
          taskTotal: 162,
          taskList: [
            { label: '未启动', value: 43 },
            { label: '已启动', value: 119 },
          ],
        },
        barChartData: [
          { name: '01稽核处', value: 5 },
          { name: '02炼化处', value: 7 },
          { name: '03销售处', value: 11 },
          { name: '05基建一处', value: 13 },
          { name: '06基建二处', value: 6 },
          { name: '07工程处', value: 2 },
          { name: '08金融处', value: 8 },
          { name: '09信息处', value: 9 },
          { name: '10审计中心', value: 12 },
          { name: '11审计中心', value: 14 },
        ],
        tableData: [
          {
            planUnit: '01审计中心本部',
            execUnit: '01稽核处',
            project: 5,
            task: '-',
            total: 5,
          },
          {
            planUnit: '01审计中心本部',
            execUnit: '02炼化处',
            project: 6,
            task: 1,
            total: 7,
          },
          {
            planUnit: '01审计中心本部',
            execUnit: '03销售处',
            project: 7,
            task: 0,
            total: 7,
          },
          {
            planUnit: '01审计中心本部',
            execUnit: '05基建一处',
            project: 10,
            task: 3,
            total: 13,
          },
        ],
        filters: {},
      }
    },
    methods: {
      onFilterChange(filters) {
        console.log(filters)
        // 这里可以根据筛选条件请求数据,把值传给图表
        this.filters = filters
      },
    },
  }
</script>

<style scoped>
  .cwztfx-page {
    background: #eaf6fb;
    min-height: 100vh;
    padding: 16px;
  }
  .custom-tabs {
    display: flex;
    gap: 8px;
    margin-top: 16px;
    margin-bottom: 0;
  }
  .custom-tab {
    background: #6ea6c7;
    color: #fff;
    font-weight: bold;
    font-size: 16px;
    padding: 8px 32px;
    border-radius: 4px 4px 0 0;
    cursor: pointer;
    transition: background 0.2s, color 0.2s;
    border: none;
    outline: none;
    user-select: none;
    position: relative;
  }
  .custom-tab.active {
    background: #3b6e8c;
    color: #222;
    font-weight: 900;
  }
  .custom-tab.active::after {
    content: '';
    display: block;
    position: absolute;
    left: 0;
    right: 0;
    bottom: -4px;
    height: 4px;
    background: #eaf6fb;
    border-radius: 0 0 4px 4px;
  }
  .tab-content {
    background: #fff;
    border-radius: 0 0 4px 4px;
    padding: 24px 16px 16px 16px;
    min-height: 200px;
    margin-bottom: 16px;
  }
  .base-info {
    display: flex;
    gap: 24px;
    width: 100%;
    align-items: stretch;
  }
  .base-info > * {
    flex: 1 1 0;
    min-width: 0;
  }
</style>
