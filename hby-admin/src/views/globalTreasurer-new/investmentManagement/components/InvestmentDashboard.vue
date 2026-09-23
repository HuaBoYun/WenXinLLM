<template>
  <div class="investment-dashboard">
    <el-row :gutter="16" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="(stat, index) in statistics" :key="index">
        <statistics-card
          :title="stat.title"
          :value="stat.value"
          :prefix="stat.prefix"
          :suffix="stat.suffix"
          :icon="stat.icon"
          :icon-color="stat.iconColor"
          :icon-bg-color="stat.iconBgColor"
          :trend="stat.trend"
          :decimals="stat.decimals"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import StatisticsCard from '@/components/StatisticsCard'

export default {
  name: 'InvestmentDashboard',
  components: {
    StatisticsCard,
  },
  props: {
    statistics: {
      type: Array,
      required: true,
      // statistics: [
      //   {
      //     title: '总投资金额',
      //     value: 1000000,
      //     prefix: '¥',
      //     suffix: '元',
      //     icon: 'el-icon-money',
      //     iconColor: '#67C23A',
      //     iconBgColor: '#F0F9FF',
      //     trend: { type: 'up', value: '+12.5%' },
      //     decimals: 2
      //   }
      // ]
    },
  },
}
</script>

<style scoped>
.investment-dashboard {
  width: 100%;
}

.stats-row {
  margin-bottom: 16px;
}

.stats-row .el-col {
  margin-bottom: 16px;
}
</style>

