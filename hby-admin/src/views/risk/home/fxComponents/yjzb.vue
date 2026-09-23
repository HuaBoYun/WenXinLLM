<template>
  <el-card>
    <div class="title-container">
      <h3 class="chart-title">预警指标处理</h3>
    </div>
    <el-table :data="yjzbData" border stripe style="width: 100%">
      <el-table-column
        prop="stepTitle"
        label="模型名称"
        align="center"
        min-width="200"
      ></el-table-column>
      <el-table-column prop="threshold" label="状态" align="center" width="100">
        <template #default="{ row }">
          <el-tag
            :type="getThresholdType(row.threshold)"
            :class="getThresholdClass(row.threshold)"
          >
            {{ row.threshold }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="quarter"
        label="季度"
        align="center"
        width="120"
      ></el-table-column>
      <el-table-column label="操作" align="center" width="120">
        <template #default="{ row }">
          <el-button type="text" size="small" @click="handleDetail(row)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <SqlModal ref="check"></SqlModal>
  </el-card>
</template>

<script>
  import { getYjzbData } from '@/oapi/risk/index.js'
  import SqlModal from '@/views/audit/base/components/sqlCheck.vue'

  export default {
    name: 'Yjzb',
    components: { SqlModal },
    data() {
      return {
        yjzbData: [],
        loading: false,
      }
    },
    mounted() {
      this.loadYjzbData()
    },
    methods: {
      async loadYjzbData() {
        try {
          this.loading = true
          const response = await getYjzbData()
          if (response && response.code === 1) {
            this.yjzbData = response.data.yjzb || []
          }
        } catch (error) {
          console.error('获取预警指标数据失败:', error)
          this.$message.error('获取预警指标数据失败')
        } finally {
          this.loading = false
        }
      },
      getThresholdType(threshold) {
        switch (threshold) {
          case '红':
            return 'danger'
          case '黄':
            return 'warning'
          case '绿':
            return 'success'
          default:
            return 'info'
        }
      },
      getThresholdClass(threshold) {
        switch (threshold) {
          case '红':
            return 'threshold-red'
          case '黄':
            return 'threshold-yellow'
          case '绿':
            return 'threshold-green'
          default:
            return ''
        }
      },
      handleDetail(row) {
        this.$refs['check'].show(row.sqlStr, row.bookid)
      },
    },
  }
</script>

<style scoped>
  .yjzb-container {
  }

  .title-container {
    margin-bottom: 15px;
  }

  .chart-title {
    font-size: 16px;
    color: #ff8c00;
    margin: 0;
    text-align: left;
    font-weight: bold;
  }

  .threshold-red {
    background-color: #f56c6c !important;
    border-color: #f56c6c !important;
    color: #fff !important;
  }

  .threshold-yellow {
    background-color: #e6a23c !important;
    border-color: #e6a23c !important;
    color: #fff !important;
  }

  .threshold-green {
    background-color: #67c23a !important;
    border-color: #67c23a !important;
    color: #fff !important;
  }
</style>
