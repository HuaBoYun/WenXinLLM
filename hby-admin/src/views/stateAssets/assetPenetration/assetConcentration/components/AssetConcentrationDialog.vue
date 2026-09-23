<template>
  <el-dialog
    title="资产集中度分析"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-tabs v-model="activeTab">
      <el-tab-pane label="集中度概览" name="overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <el-statistic title="总资产规模" :value="data.totalAssets" suffix="万元">
                <template slot="prefix">
                  <i class="el-icon-money" style="color: #409EFF"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <el-statistic title="集中度指数" :value="data.concentrationIndex">
                <template slot="prefix">
                  <i class="el-icon-pie-chart" style="color: #67C23A"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <el-statistic title="风险等级" :value="data.riskLevel">
                <template slot="prefix">
                  <i class="el-icon-warning" style="color: #E6A23C"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <el-statistic title="监控企业数" :value="data.enterpriseCount" suffix="家">
                <template slot="prefix">
                  <i class="el-icon-office-building" style="color: #F56C6C"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <el-tab-pane label="集中度分析" name="analysis">
        <el-table :data="data.concentrationData" border>
          <el-table-column prop="assetType" label="资产类型" width="150"></el-table-column>
          <el-table-column prop="amount" label="资产金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.amount) }}
            </template>
          </el-table-column>
          <el-table-column prop="percentage" label="占比" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.percentage }}%
            </template>
          </el-table-column>
          <el-table-column prop="concentration" label="集中度" width="120">
            <template slot-scope="scope">
              <el-tag :type="getConcentrationType(scope.row.concentration)">
                {{ scope.row.concentration }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="riskLevel" label="风险等级" width="100">
            <template slot-scope="scope">
              <el-tag :type="getRiskType(scope.row.riskLevel)">
                {{ scope.row.riskLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明"></el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="趋势分析" name="trend">
        <div id="concentrationChart" style="height: 400px; text-align: center; line-height: 400px; color: #999;">
          资产集中度趋势图表区域
        </div>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportReport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetConcentrationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    concentrationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'overview',
      data: {
        totalAssets: 50000,
        concentrationIndex: 0.75,
        riskLevel: '中等',
        enterpriseCount: 25,
        concentrationData: [
          {
            assetType: '固定资产',
            amount: 20000,
            percentage: 40.0,
            concentration: '高',
            riskLevel: '中等',
            description: '主要集中在制造业设备'
          },
          {
            assetType: '流动资产',
            amount: 15000,
            percentage: 30.0,
            concentration: '中等',
            riskLevel: '低',
            description: '现金及现金等价物'
          },
          {
            assetType: '无形资产',
            amount: 10000,
            percentage: 20.0,
            concentration: '中等',
            riskLevel: '中等',
            description: '专利技术和商标'
          },
          {
            assetType: '投资性资产',
            amount: 5000,
            percentage: 10.0,
            concentration: '低',
            riskLevel: '低',
            description: '对外投资和理财产品'
          }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getConcentrationType(concentration) {
      const typeMap = {
        '高': 'danger',
        '中等': 'warning',
        '低': 'success'
      }
      return typeMap[concentration] || 'info'
    },
    getRiskType(riskLevel) {
      const typeMap = {
        '高': 'danger',
        '中等': 'warning',
        '低': 'success'
      }
      return typeMap[riskLevel] || 'info'
    },
    formatAmount(amount) {
      return amount ? amount.toLocaleString() + '万元' : '0万元'
    },
    exportReport() {
      this.$message.success('导出资产集中度分析报告成功')
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
