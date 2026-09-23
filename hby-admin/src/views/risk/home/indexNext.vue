<template>
  <div class="system-log-container">
    <div style="width: 100%">
      <yjzb></yjzb>
    </div>
    <div style="width: 100%">
      <yjsyBt></yjsyBt>
    </div>
    <el-tabs type="border-card" v-model="activeName3">
      <el-tab-pane label="预警结果" name="first">
        <SJMXTable :showFooter="true"></SJMXTable>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import SJMXTable from '@/views/risk/home/components/index.vue'
  import yjzb from '@/views/risk/home/fxComponents/yjzb.vue'
  import yjsyBt from '@/views/risk/home/fxComponents/yjsyBt.vue'

  export default {
    components: {
      SJMXTable,
      yjzb,
      yjsyBt
    },
    data() {
      return {
        calendarValue: new Date(),
        activeName3: 'first',
      }
    },
  }
</script>

<style scoped>
  .container {
    background-color: #f6f8f9;
  }
  .is-selected {
    color: #1989fa;
  }
  .headTitle {
    font-size: 20px;
  }
</style>
