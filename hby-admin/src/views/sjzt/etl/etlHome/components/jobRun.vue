<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover" style="height: 150px">
      <!-- <template #header>
        <span>
          <vab-icon icon="donut-chart-fill" />
          专项法务服务费统计
        </span>
      </template> -->
      <div class="title">作业运行数量</div>
      <div class="content">
        <p>{{ this.jobRun }}</p>
      </div>
    </el-card>
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'

  export default {
    components: {
      VabChart,
    },
    props: ['jobRun'],
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
      }
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 300px !important;
  }

  .title {
    text-align: center;
    font-size: 18px;
    font-weight: 600;
  }

  .content {
    height: 265px;
    padding-top: 35px;
  }
  p {
    line-height: 30px;
    text-align: center;
    font-size: 20px;
    font-weight: bold;
  }
</style>
