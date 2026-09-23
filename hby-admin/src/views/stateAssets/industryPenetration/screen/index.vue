 <template>
   <PenetrationScreen
     title="行业穿透大屏" titleIcon="el-icon-office-building"
     :kpiCards="kpiCards" :pages="pages" :companyList="companyList"
     @goto="goTo" @close="$emit('close')" @view-change="onViewChange" @company-change="onCompanyChange"
   >
     <template #default>
       <div class="chart-grid">
         <div class="chart-panel" v-for="c in chartPanels" :key="c.ref">
           <div class="chart-panel-title"><i :class="c.icon"></i> {{ c.title }}</div>
           <div :ref="c.ref" class="chart-box"></div>
         </div>
       </div>
     </template>
   </PenetrationScreen>
 </template>
 
 <script>
 import * as echarts from 'echarts'
 import PenetrationScreen from '@/views/stateAssets/components/PenetrationScreen.vue'
 import { getIndustryDistribution, getIndustryOverview, getIndustryWarningList, getSynergyMatrix } from '@/api/stateAssets/industryPenetration'

 export default {
   name: 'IndustryPenetrationScreen',
   components: { PenetrationScreen },
   data() {
     return {
       kpiCards: [
         { label: '覆盖行业数', value: '-', icon: 'el-icon-office-building', route: '/industryPenetration/industryLayout' },
         { label: '主业行业数', value: '-', icon: 'el-icon-s-cooperation', route: '/industryPenetration/industryLayout' },
         { label: '非主业行业数', value: '-', icon: 'el-icon-pie-chart', route: '/industryPenetration/industryLayout' },
         { label: '活跃预警数', value: '-', icon: 'el-icon-warning', route: '/industryPenetration/riskWarning' },
         { label: '高风险预警', value: '-', icon: 'el-icon-bell', route: '/industryPenetration/riskWarning' },
         { label: '中风险预警', value: '-', icon: 'el-icon-warning-outline', route: '/industryPenetration/riskWarning' },
       ],
       pages: [
         { label: '行业穿透首页', route: '/industryPenetration/home', icon: 'el-icon-s-home' },
         { label: '监控驾驶舱', route: '/industryPenetration/dashboard', icon: 'el-icon-odometer' },
         { label: '行业布局台账', route: '/industryPenetration/industryLayout', icon: 'el-icon-notebook-2' },
         { label: '能源行业监管', route: '/industryPenetration/energy', icon: 'el-icon-lightning' },
         { label: '金融行业监管', route: '/industryPenetration/financial', icon: 'el-icon-bank-card' },
         { label: '制造行业监管', route: '/industryPenetration/manufacturing', icon: 'el-icon-setting' },
         { label: '基础设施监管', route: '/industryPenetration/infrastructure', icon: 'el-icon-s-grid' },
         { label: '公共服务监管', route: '/industryPenetration/publicService', icon: 'el-icon-s-flag' },
         { label: '竞争力分析', route: '/industryPenetration/competitiveness', icon: 'el-icon-data-analysis' },
         { label: '产业协同分析', route: '/industryPenetration/synergy', icon: 'el-icon-share' },
         { label: '风险预警管理', route: '/industryPenetration/riskWarning', icon: 'el-icon-warning' },
         { label: '行业穿透分析', route: '/industryPenetration/drillDown', icon: 'el-icon-zoom-in' },
       ],
       companyList: [],
       chartPanels: [
         { ref: 'chart1', title: '行业资产规模分布', icon: 'el-icon-pie-chart' },
         { ref: 'chart2', title: '各行业资产规模对比', icon: 'el-icon-bar-chart' },
         { ref: 'chart3', title: '行业风险等级分布', icon: 'el-icon-warning' },
         { ref: 'chart4', title: '产业协同关系热力图', icon: 'el-icon-share' },
       ],
       _charts: [],
     }
   },
   mounted() { this.$nextTick(() => { setTimeout(() => this.loadData(), 300) }) },
   beforeDestroy() { this._charts.forEach(c => c.dispose()) },
   methods: {
     goTo(route) {
       if (!route) return
       this.$emit('close')
       this.$nextTick(() => { this.$router.push(route).catch(() => {}) })
     },
     onViewChange() { this.loadData() },
     onCompanyChange() { this.loadData() },
     async loadData() {
       await Promise.allSettled([this.loadKpi(), this.loadDistribution(), this.loadWarnings(), this.loadSynergy()])
     },

     // KPI：字段名与接口对齐
     async loadKpi() {
       try {
         const [overviewRes, warningRes] = await Promise.allSettled([
           getIndustryOverview(),
           getIndustryWarningList({ pageNumber: 1, pageSize: 100 }),
         ])
         if (overviewRes.status === 'fulfilled' && overviewRes.value && overviewRes.value.data) {
           const d = overviewRes.value.data
           // 接口字段：totalIndustries / mainIndustryCount / nonMainIndustryCount / activeWarnings
           this.kpiCards[0].value = d.totalIndustries != null ? d.totalIndustries : '-'
           this.kpiCards[1].value = d.mainIndustryCount != null ? d.mainIndustryCount : '-'
           this.kpiCards[2].value = d.nonMainIndustryCount != null ? d.nonMainIndustryCount : '-'
           this.kpiCards[3].value = d.activeWarnings != null ? d.activeWarnings : '-'
         }
         if (warningRes.status === 'fulfilled' && warningRes.value && warningRes.value.data && warningRes.value.data.tlist) {
           const list = warningRes.value.data.tlist
           this.kpiCards[4].value = list.filter(w => w.level === 'HIGH').length
           this.kpiCards[5].value = list.filter(w => w.level === 'MEDIUM').length
         }
       } catch (e) { console.error('[industry screen kpi]', e) }
     },

     // chart1（饼图）+ chart2（柱图）：接口返回 data.pieData
     async loadDistribution() {
       let pieData = [
         { name: '能源', value: 678 }, { name: '金融', value: 141 },
         { name: '制造业', value: 61.5 }, { name: '基础设施', value: 114 }, { name: '公共服务', value: 98 },
       ]
       try {
         const res = await getIndustryDistribution()
         // 接口返回 data.pieData，不是 data.distribution
         if (res && res.data && Array.isArray(res.data.pieData) && res.data.pieData.length) {
           pieData = res.data.pieData
         }
       } catch (e) { console.error('[industry screen distribution]', e) }

       const barNames = pieData.map(i => i.name)
       const barValues = pieData.map(i => i.value)

       // chart1 和 chart2 分开独立 $nextTick，避免同一 tick 内 ref 未就绪
       this.$nextTick(() => {
         this.initChart('chart1', {
           tooltip: { trigger: 'item', formatter: '{b}: {c}亿 ({d}%)' },
           legend: { bottom: 0, textStyle: { color: '#aaa' } },
           series: [{
             type: 'pie', radius: ['35%', '65%'], data: pieData,
             label: { color: '#ccc' }, itemStyle: { borderRadius: 4 },
             emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(64,158,255,0.5)' } },
           }],
         }, '/industryPenetration/industryLayout')
       })

       // chart2 单独一个 nextTick
       this.$nextTick(() => {
         this.initChart('chart2', {
           tooltip: { trigger: 'axis', formatter: params => `${params[0].name}<br/>资产规模: ${params[0].value}亿` },
           xAxis: { type: 'category', data: barNames, axisLabel: { color: '#aaa', rotate: 20 } },
           yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}亿' }, name: '亿元', nameTextStyle: { color: '#aaa' } },
           series: [{
             type: 'bar', data: barValues,
             itemStyle: {
               color: p => ['#1677FF', '#52C41A', '#FA8C16', '#722ED1', '#EB2F96'][p.dataIndex % 5],
               borderRadius: [4, 4, 0, 0],
             },
           }],
           grid: { left: 50, right: 20, bottom: 40, top: 30 },
         }, '/industryPenetration/competitiveness')
       })
     },

     // chart3：风险等级分布，level 字段为 HIGH/MEDIUM/LOW
     async loadWarnings() {
       let data = [
         { name: '低风险', value: 4 }, { name: '中风险', value: 4 },
         { name: '高风险', value: 2 },
       ]
       try {
         const res = await getIndustryWarningList({ pageNumber: 1, pageSize: 100 })
         const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
         if (list.length) {
           const countMap = { LOW: 0, MEDIUM: 0, HIGH: 0 }
           list.forEach(w => { if (countMap[w.level] !== undefined) countMap[w.level]++ })
           data = [
             { name: '低风险', value: countMap.LOW },
             { name: '中风险', value: countMap.MEDIUM },
             { name: '高风险', value: countMap.HIGH },
           ].filter(d => d.value > 0)
         }
       } catch (e) { console.error('[industry screen warnings]', e) }
       this.$nextTick(() => {
         this.initChart('chart3', {
           tooltip: { trigger: 'item', formatter: '{b}: {c}条 ({d}%)' },
           legend: { bottom: 0, textStyle: { color: '#aaa' } },
           series: [{
             type: 'pie', radius: '60%', data,
             label: { color: '#ccc' }, color: ['#52C41A', '#FA8C16', '#FF4D4F'],
             emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
           }],
         }, '/industryPenetration/riskWarning')
       })
     },

     // chart4：产业协同热力图，接口返回矩阵对象
     async loadSynergy() {
       // 默认静态数据
       let industries = ['能源行业', '金融行业', '制造行业', '基础设施', '公共服务']
       let heatData = [
         [0, 0, 88], [0, 2, 65], [0, 3, 85], [0, 4, 70],
         [1, 1, 79], [1, 2, 72], [1, 3, 82],
         [2, 3, 90], [2, 4, 76],
         [4, 4, 58],
       ]
       try {
         const res = await getSynergyMatrix()
         // 接口返回矩阵对象：{ 能源行业: { 金融行业: 65, ... }, ... }
         if (res && res.data && typeof res.data === 'object') {
           const matrix = res.data
           industries = Object.keys(matrix)
           heatData = []
           industries.forEach((row, ri) => {
             industries.forEach((col, ci) => {
               const val = matrix[row] && matrix[row][col]
               if (val) heatData.push([ri, ci, val])
             })
           })
         }
       } catch (e) { console.error('[industry screen synergy]', e) }

       this.$nextTick(() => {
         this.initChart('chart4', {
           tooltip: {
             position: 'top',
             formatter: p => `${industries[p.data[0]]} → ${industries[p.data[1]]}<br/>协同指数: ${p.data[2]}`,
           },
           grid: { left: 80, right: 20, bottom: 60, top: 20 },
           xAxis: { type: 'category', data: industries, axisLabel: { color: '#aaa', rotate: 30 }, splitArea: { show: true } },
           yAxis: { type: 'category', data: industries, axisLabel: { color: '#aaa' }, splitArea: { show: true } },
           visualMap: {
             min: 0, max: 100, calculable: true, orient: 'horizontal',
             left: 'center', bottom: 0, textStyle: { color: '#aaa' },
             inRange: { color: ['#0a1628', '#1677FF', '#52C41A'] },
           },
           series: [{
             type: 'heatmap', data: heatData,
             label: { show: true, color: '#fff', fontSize: 11 },
             emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
           }],
         }, '/industryPenetration/synergy')
       })
     },

     initChart(refName, option, clickRoute) {
       let el = this.$refs[refName]
       if (Array.isArray(el)) el = el[0]
       if (!el) return
       const existing = echarts.getInstanceByDom(el)
       if (existing) existing.dispose()
       const chart = echarts.init(el)
       chart.setOption(option)
       chart.resize()
       if (clickRoute) chart.on('click', () => this.goTo(clickRoute))
       this._charts.push(chart)
     },
   },
 }
 </script>
 
 <style lang="scss" scoped>
 .chart-grid {
   display: grid;
   grid-template-columns: 1fr 1fr;
   grid-template-rows: 1fr 1fr;
   gap: 12px;
   flex: 1;
   min-height: 0;
 }
 .chart-panel {
   background: rgba(255,255,255,0.04);
   border: 1px solid rgba(64,158,255,0.15);
   border-radius: 6px;
   padding: 10px;
   display: flex;
   flex-direction: column;
 }
 .chart-panel-title {
   font-size: 13px;
   color: #67C23A;
   margin-bottom: 6px;
   i { margin-right: 4px; }
 }
 .chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
 </style>
