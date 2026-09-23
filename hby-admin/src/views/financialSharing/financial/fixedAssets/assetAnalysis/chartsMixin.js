import * as echarts from 'echarts'

export default {
  data() {
    return {
      charts: {
        structure: null,
        depreciation: null,
        value: null,
        utilization: null
      }
    }
  },
  beforeDestroy() {
    // 销毁所有图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    // 初始化资产结构分析图表
    initStructureChart() {
      const chartDom = document.getElementById('structureChart')
      if (!chartDom) return
      
      this.charts.structure = echarts.init(chartDom)
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [
          {
            name: '资产类别',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 45680000, name: '房屋建筑物' },
              { value: 32450000, name: '机器设备' },
              { value: 18920000, name: '运输工具' },
              { value: 15630000, name: '电子设备' },
              { value: 13000000, name: '办公设备' }
            ]
          }
        ]
      }
      
      this.charts.structure.setOption(option)
    },

    // 初始化折旧趋势分析图表
    initDepreciationChart() {
      const chartDom = document.getElementById('depreciationChart')
      if (!chartDom) return
      
      this.charts.depreciation = echarts.init(chartDom)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['月度折旧']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}万'
          }
        },
        series: [
          {
            name: '月度折旧',
            type: 'line',
            smooth: true,
            data: [320, 332, 301, 334, 390, 330, 320, 345, 360, 375, 390, 410],
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
              ])
            }
          }
        ]
      }
      
      this.charts.depreciation.setOption(option)
    },

    // 初始化价值变动分析图表
    initValueChart() {
      const chartDom = document.getElementById('valueChart')
      if (!chartDom) return
      
      this.charts.value = echarts.init(chartDom)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['原值', '净值', '累计折旧']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}万'
          }
        },
        series: [
          {
            name: '原值',
            type: 'line',
            data: [12568, 12568, 12568, 12568, 12568, 12568, 12568, 12568, 12568, 12568, 12568, 12568]
          },
          {
            name: '净值',
            type: 'line',
            data: [8000, 7800, 7600, 7400, 7200, 7000, 6800, 6600, 6400, 6200, 6000, 5800]
          },
          {
            name: '累计折旧',
            type: 'line',
            data: [4568, 4768, 4968, 5168, 5368, 5568, 5768, 5968, 6168, 6368, 6568, 6768]
          }
        ]
      }
      
      this.charts.value.setOption(option)
    },

    // 初始化利用率分析图表
    initUtilizationChart() {
      const chartDom = document.getElementById('utilizationChart')
      if (!chartDom) return
      
      this.charts.utilization = echarts.init(chartDom)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          max: 100,
          axisLabel: {
            formatter: '{value}%'
          }
        },
        yAxis: {
          type: 'category',
          data: ['财务部', '行政部', '销售部', '研发部', '生产部']
        },
        series: [
          {
            name: '利用率',
            type: 'bar',
            data: [65, 72, 78, 85, 92],
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#83bff6' },
                { offset: 1, color: '#188df0' }
              ])
            }
          }
        ]
      }
      
      this.charts.utilization.setOption(option)
    }
  }
}

