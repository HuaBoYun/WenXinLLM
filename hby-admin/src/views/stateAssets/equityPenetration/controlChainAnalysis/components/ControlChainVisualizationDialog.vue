<template>
  <el-dialog
    title="控制链可视化分析"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="visualization-container">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-button-group>
              <el-button
                :type="viewMode === 'tree' ? 'primary' : 'default'"
                @click="setViewMode('tree')"
                icon="el-icon-share"
              >
                树形图
              </el-button>
              <el-button
                :type="viewMode === 'network' ? 'primary' : 'default'"
                @click="setViewMode('network')"
                icon="el-icon-connection"
              >
                网络图
              </el-button>
              <el-button
                :type="viewMode === 'sankey' ? 'primary' : 'default'"
                @click="setViewMode('sankey')"
                icon="el-icon-sort"
              >
                桑基图
              </el-button>
            </el-button-group>
            
            <el-divider direction="vertical"></el-divider>
            
            <el-button @click="zoomIn" icon="el-icon-zoom-in">放大</el-button>
            <el-button @click="zoomOut" icon="el-icon-zoom-out">缩小</el-button>
            <el-button @click="resetZoom" icon="el-icon-refresh">重置</el-button>
            <el-button @click="exportChart" icon="el-icon-download">导出</el-button>
          </el-col>
          
          <el-col :span="8">
            <div class="filter-controls">
              <el-select
                v-model="filterLevel"
                placeholder="显示层级"
                size="small"
                style="width: 120px; margin-right: 10px;"
                @change="updateVisualization"
              >
                <el-option label="全部层级" value="all"></el-option>
                <el-option
                  v-for="level in maxLevel"
                  :key="level"
                  :label="`前${level}层`"
                  :value="level"
                ></el-option>
              </el-select>
              
              <el-input-number
                v-model="minHoldingRatio"
                :min="0"
                :max="100"
                :step="0.1"
                size="small"
                placeholder="最小持股比例"
                style="width: 140px;"
                @change="updateVisualization"
              />
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 图表容器 -->
      <div class="chart-container">
        <div ref="chartContainer" class="chart" :style="{ height: chartHeight + 'px' }"></div>
        
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-overlay">
          <el-loading
            element-loading-text="正在生成可视化图表..."
            element-loading-spinner="el-icon-loading"
          ></el-loading>
        </div>
      </div>

      <!-- 图例和信息面板 -->
      <div class="info-panel">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="never" class="legend-card">
              <div slot="header">
                <span>图例说明</span>
              </div>
              <div class="legend-content">
                <div class="legend-item">
                  <div class="legend-color" style="background-color: #409EFF;"></div>
                  <span>实际控制人</span>
                </div>
                <div class="legend-item">
                  <div class="legend-color" style="background-color: #67C23A;"></div>
                  <span>直接股东</span>
                </div>
                <div class="legend-item">
                  <div class="legend-color" style="background-color: #E6A23C;"></div>
                  <span>间接股东</span>
                </div>
                <div class="legend-item">
                  <div class="legend-color" style="background-color: #F56C6C;"></div>
                  <span>目标企业</span>
                </div>
                <div class="legend-item">
                  <div class="legend-line"></div>
                  <span>控制关系（线条粗细表示控制强度）</span>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card shadow="never" class="stats-card">
              <div slot="header">
                <span>统计信息</span>
              </div>
              <div class="stats-content">
                <el-descriptions :column="2" size="small">
                  <el-descriptions-item label="节点总数">
                    {{ chartStats.totalNodes }}
                  </el-descriptions-item>
                  <el-descriptions-item label="关系总数">
                    {{ chartStats.totalEdges }}
                  </el-descriptions-item>
                  <el-descriptions-item label="最大层级">
                    {{ chartStats.maxLevel }}
                  </el-descriptions-item>
                  <el-descriptions-item label="平均持股比例">
                    {{ chartStats.avgHoldingRatio }}%
                  </el-descriptions-item>
                  <el-descriptions-item label="最强控制链">
                    {{ chartStats.strongestChain }}%
                  </el-descriptions-item>
                  <el-descriptions-item label="复杂度评分">
                    {{ chartStats.complexityScore }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="generateReport">生成分析报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getControlChainVisualization } from '@/api/stateAssets/controlChainAnalysis'

export default {
  name: 'ControlChainVisualizationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    chainData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      chart: null,
      chartHeight: 600,
      viewMode: 'tree', // tree, network, sankey
      filterLevel: 'all',
      minHoldingRatio: 0,
      maxLevel: 10,
      chartStats: {
        totalNodes: 0,
        totalEdges: 0,
        maxLevel: 0,
        avgHoldingRatio: 0,
        strongestChain: 0,
        complexityScore: 0
      },
      visualizationData: {
        nodes: [],
        edges: [],
        categories: []
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
  watch: {
    visible(val) {
      if (val) {
        this.loadVisualizationData()
        this.$nextTick(() => {
          this.initChart()
          this.updateVisualization()
        })
      } else {
        this.destroyChart()
      }
    }
  },
  methods: {
    // 从后端加载可视化数据
    async loadVisualizationData() {
      this.loading = true
      try {
        const params = {}
        if (this.chainData && this.chainData.statId) {
          params.statId = this.chainData.statId
        }
        if (this.chainData && this.chainData.enterpriseId) {
          params.enterpriseId = this.chainData.enterpriseId
        }
        const response = await getControlChainVisualization(params)
        if (response && response.result === 200 && response.data) {
          const data = response.data
          const nodes = []
          const edges = []
          const categories = [
            { name: '实际控制人' }, { name: '直接股东' }, { name: '间接股东' }, { name: '目标企业' }
          ]

          // 递归解析后端树形结构
          const parseTreeNode = (node, level, parentId) => {
            if (!node) return
            const nodeId = node.id || node.enterpriseId || ('node_' + nodes.length)
            const nodeName = node.name || node.enterpriseName || '未知'
            let category = '间接股东'
            if (level === 0 || node.type === 'root') category = '实际控制人'
            else if (level === 1) category = '直接股东'
            else if (level === 2) category = '目标企业'

            const controlStrength = node.avgControlStrength || node.totalChains || (100 - level * 20)
            const symbolSize = Math.max(25, 60 - level * 10)
            nodes.push({
              id: nodeId,
              name: nodeName,
              category: category,
              holdingRatio: controlStrength,
              controlStrength: controlStrength,
              level: level,
              symbolSize: symbolSize
            })

            if (parentId) {
              edges.push({
                source: parentId,
                target: nodeId,
                holdingRatio: controlStrength,
                controlStrength: node.directChains || controlStrength
              })
            }

            // 递归处理子节点
            if (node.children && Array.isArray(node.children) && node.children.length > 0) {
              node.children.forEach(child => {
                parseTreeNode(child, level + 1, nodeId)
              })
            }
          }

          parseTreeNode(data, 0, null)

          // 如果只有根节点没有子节点，说明后端数据为空，使用降级方案
          if (nodes.length <= 1) {
            this.buildBasicVisualization()
          } else {
            this.visualizationData = { nodes, edges, categories }
            this.chartStats = {
              totalNodes: nodes.length,
              totalEdges: edges.length,
              maxLevel: Math.max(...nodes.map(n => n.level), 0),
              avgHoldingRatio: this.chainData.avgControlStrength || 0,
              strongestChain: nodes.length > 0 ? Math.max(...nodes.map(n => n.controlStrength)) : 0,
              complexityScore: (nodes.length * 0.5 + edges.length * 0.3).toFixed(1)
            }
            this.maxLevel = this.chartStats.maxLevel || 3
          }
        } else {
          this.buildBasicVisualization()
        }
      } catch (error) {
        console.error('获取可视化数据失败:', error)
        this.buildBasicVisualization()
      } finally {
        this.loading = false
      }
    },

    // 基于当前行数据构建基本可视化（后端无数据时的降级方案）
    buildBasicVisualization() {
      const name = (this.chainData && this.chainData.enterpriseName) || '目标企业'
      const strength = this.chainData.avgControlStrength || 50
      const totalChains = this.chainData.totalChains || 1
      const directChains = this.chainData.directChains || 1
      const indirectChains = this.chainData.indirectChains || 0

      // 构建至少3个节点确保三种图都能渲染
      const nodes = [
        { id: 'root', name: '集团总部', category: '实际控制人', holdingRatio: 100, controlStrength: 100, level: 0, symbolSize: 60 },
        { id: 'target', name: name, category: '目标企业', holdingRatio: strength || 50, controlStrength: strength || 50, level: 1, symbolSize: 45 }
      ]
      const edges = [
        { source: 'root', target: 'target', holdingRatio: strength || 50, controlStrength: strength || 50 }
      ]

      // 如果有间接控制链，增加一个中间节点
      if (indirectChains > 0) {
        nodes.push({ id: 'middle', name: '中间控股公司', category: '直接股东', holdingRatio: 80, controlStrength: 80, level: 1, symbolSize: 40 })
        nodes[1].level = 2 // target 变为第2层
        edges[0] = { source: 'root', target: 'middle', holdingRatio: 80, controlStrength: 80 }
        edges.push({ source: 'middle', target: 'target', holdingRatio: strength || 50, controlStrength: strength || 50 })
      }

      this.visualizationData = {
        nodes: nodes,
        edges: edges,
        categories: [
          { name: '实际控制人' }, { name: '直接股东' }, { name: '间接股东' }, { name: '目标企业' }
        ]
      }
      this.chartStats = {
        totalNodes: nodes.length,
        totalEdges: edges.length,
        maxLevel: this.chainData.maxChainLength || Math.max(...nodes.map(n => n.level)),
        avgHoldingRatio: strength,
        strongestChain: 100,
        complexityScore: (totalChains * 0.5).toFixed(1)
      }
      this.maxLevel = this.chartStats.maxLevel || 3
    },

    // 初始化图表
    initChart() {
      if (this.chart) {
        this.chart.dispose()
      }
      
      this.chart = echarts.init(this.$refs.chartContainer)
      
      // 监听图表事件
      this.chart.on('click', this.handleNodeClick)
      this.chart.on('dblclick', this.handleNodeDoubleClick)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 更新可视化
    updateVisualization() {
      if (!this.chart) return

      let option = {}
      
      switch (this.viewMode) {
        case 'tree':
          option = this.getTreeOption()
          break
        case 'network':
          option = this.getNetworkOption()
          break
        case 'sankey':
          option = this.getSankeyOption()
          break
      }
      
      this.chart.setOption(option, true)
    },

    // 获取树形图配置
    getTreeOption() {
      const filteredData = this.filterData()
      
      return {
        title: {
          text: `${this.chainData.enterpriseName} - 控制链树形图`,
          left: 'center',
          top: 20
        },
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            const data = params.data
            return `
              <div>
                <strong>${data.name}</strong><br/>
                类型: ${data.category}<br/>
                持股比例: ${data.holdingRatio || 0}%<br/>
                控制强度: ${data.controlStrength || 0}%<br/>
                层级: ${data.level || 0}
              </div>
            `
          }
        },
        series: [{
          type: 'tree',
          data: [filteredData.treeData],
          left: '2%',
          right: '2%',
          top: '8%',
          bottom: '20%',
          symbol: 'circle',
          symbolSize: (value, params) => {
            return Math.max(20, (params.data.controlStrength || 10) / 2)
          },
          itemStyle: {
            color: (params) => {
              const colorMap = {
                '实际控制人': '#409EFF',
                '直接股东': '#67C23A',
                '间接股东': '#E6A23C',
                '目标企业': '#F56C6C'
              }
              return colorMap[params.data.category] || '#909399'
            }
          },
          label: {
            position: 'top',
            verticalAlign: 'middle',
            align: 'center',
            fontSize: 12
          },
          leaves: {
            label: {
              position: 'bottom',
              verticalAlign: 'middle',
              align: 'center'
            }
          },
          expandAndCollapse: true,
          animationDuration: 550,
          animationDurationUpdate: 750
        }]
      }
    },

    // 获取网络图配置
    getNetworkOption() {
      const filteredData = this.filterData()
      
      return {
        title: {
          text: `${this.chainData.enterpriseName} - 控制关系网络图`,
          left: 'center',
          top: 20
        },
        tooltip: {
          formatter: (params) => {
            if (params.dataType === 'node') {
              const data = params.data
              return `
                <div>
                  <strong>${data.name}</strong><br/>
                  类型: ${data.category}<br/>
                  持股比例: ${data.holdingRatio || 0}%<br/>
                  控制强度: ${data.controlStrength || 0}%
                </div>
              `
            } else if (params.dataType === 'edge') {
              return `
                <div>
                  ${params.data.source} → ${params.data.target}<br/>
                  持股比例: ${params.data.holdingRatio}%<br/>
                  控制强度: ${params.data.controlStrength}%
                </div>
              `
            }
          }
        },
        legend: {
          data: filteredData.categories.map(cat => cat.name),
          bottom: 10
        },
        series: [{
          type: 'graph',
          layout: 'force',
          data: filteredData.nodes,
          links: filteredData.edges,
          categories: filteredData.categories,
          roam: true,
          focusNodeAdjacency: true,
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 1,
            shadowBlur: 3,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          },
          label: {
            show: true,
            position: 'right',
            formatter: '{b}'
          },
          lineStyle: {
            color: 'source',
            curveness: 0.3,
            width: (params) => {
              return Math.max(1, params.data.controlStrength / 20)
            }
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: 10
            }
          },
          force: {
            repulsion: 1000,
            gravity: 0.1,
            edgeLength: [50, 200],
            layoutAnimation: true
          }
        }]
      }
    },

    // 获取桑基图配置
    getSankeyOption() {
      const filteredData = this.filterData()
      
      return {
        title: {
          text: `${this.chainData.enterpriseName} - 控制流向桑基图`,
          left: 'center',
          top: 20
        },
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove'
        },
        series: [{
          type: 'sankey',
          data: filteredData.sankeyNodes,
          links: filteredData.sankeyLinks,
          emphasis: {
            focus: 'adjacency'
          },
          lineStyle: {
            color: 'gradient',
            curveness: 0.5
          }
        }]
      }
    },

    // 过滤数据
    filterData() {
      let filteredNodes = this.visualizationData.nodes.filter(node => {
        if (this.filterLevel !== 'all' && node.level > this.filterLevel) return false
        if (node.holdingRatio < this.minHoldingRatio) return false
        return true
      })
      let filteredEdges = this.visualizationData.edges.filter(edge => {
        return filteredNodes.some(n => n.id === edge.source) &&
               filteredNodes.some(n => n.id === edge.target)
      })

      // 构建树形数据（从根节点向下递归）
      const rootNode = filteredNodes.find(n => n.level === 0) || filteredNodes[0] || { id: 'empty', name: '无数据', level: 0 }
      const buildChildren = (nodeId) => {
        const children = filteredEdges
          .filter(e => e.source === nodeId)
          .map(e => {
            const child = filteredNodes.find(n => n.id === e.target)
            if (!child) return null
            const grandChildren = buildChildren(child.id)
            return { ...child, children: grandChildren.length > 0 ? grandChildren : undefined }
          })
          .filter(Boolean)
        return children
      }
      const treeChildren = buildChildren(rootNode.id)
      const treeData = { ...rootNode, children: treeChildren.length > 0 ? treeChildren : undefined }

      // 网络图节点需要 category 为数字索引
      const networkNodes = filteredNodes.map(n => ({
        ...n,
        symbolSize: n.symbolSize || 30,
        category: this.visualizationData.categories.findIndex(c => c.name === n.category)
      }))

      // 网络图边
      const networkEdges = filteredEdges.map(e => ({
        ...e,
        lineStyle: { width: Math.max(1, (e.controlStrength || 1) / 20) }
      }))

      // 桑基图节点和链接（value 必须 > 0，source 和 target 不能相同）
      const sankeyNodes = filteredNodes.map(n => ({ name: n.name }))
      const sankeyLinks = filteredEdges
        .map(e => {
          const sn = filteredNodes.find(n => n.id === e.source)
          const tn = filteredNodes.find(n => n.id === e.target)
          const sourceName = sn ? sn.name : e.source
          const targetName = tn ? tn.name : e.target
          // 桑基图要求 source !== target 且 value > 0
          if (sourceName === targetName) return null
          return { source: sourceName, target: targetName, value: Math.max(e.holdingRatio || 1, 1) }
        })
        .filter(Boolean)

      return {
        nodes: networkNodes,
        edges: networkEdges,
        categories: this.visualizationData.categories,
        treeData,
        sankeyNodes,
        sankeyLinks
      }
    },

    // 设置视图模式
    setViewMode(mode) {
      this.viewMode = mode
      this.updateVisualization()
    },

    // 放大
    zoomIn() {
      if (this.chart) {
        this.chart.dispatchAction({
          type: 'dataZoom',
          start: 10,
          end: 90
        })
      }
    },

    // 缩小
    zoomOut() {
      if (this.chart) {
        this.chart.dispatchAction({
          type: 'dataZoom',
          start: 0,
          end: 100
        })
      }
    },

    // 重置缩放
    resetZoom() {
      if (this.chart) {
        this.chart.dispatchAction({
          type: 'restore'
        })
      }
    },

    // 导出图表
    exportChart() {
      if (this.chart) {
        const url = this.chart.getDataURL({
          type: 'png',
          pixelRatio: 2,
          backgroundColor: '#fff'
        })
        
        const link = document.createElement('a')
        link.href = url
        link.download = `${this.chainData.enterpriseName}_控制链图.png`
        link.click()
        
        this.$message.success('图表导出成功')
      }
    },

    // 生成分析报告（导出为HTML文件下载）
    generateReport() {
      if (!this.chainData || !this.chainData.enterpriseName) {
        this.$message.warning('无可用数据生成报告')
        return
      }
      const loading = this.$loading({ lock: true, text: '正在生成分析报告...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const stats = this.chartStats
        const chain = this.chainData
        const now = new Date()
        const timeStr = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0') + '-' + String(now.getDate()).padStart(2, '0') + ' ' + String(now.getHours()).padStart(2, '0') + ':' + String(now.getMinutes()).padStart(2, '0')

        // 构建报告HTML内容
        const htmlContent = `<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>控制链分析报告 - ${chain.enterpriseName}</title>
<style>
body{font-family:'Microsoft YaHei',sans-serif;padding:40px;max-width:900px;margin:0 auto;color:#333;}
h1{text-align:center;color:#003A6C;border-bottom:2px solid #003A6C;padding-bottom:10px;}
h2{color:#0050A0;margin-top:30px;border-left:4px solid #0050A0;padding-left:10px;}
table{width:100%;border-collapse:collapse;margin:15px 0;}
th,td{border:1px solid #ddd;padding:10px;text-align:left;}
th{background:#EBF1FF;color:#0050A0;}
.summary{background:#f5f7fa;padding:20px;border-radius:6px;margin:15px 0;}
.risk-high{color:#F56C6C;font-weight:bold;}
.risk-medium{color:#E6A23C;font-weight:bold;}
.risk-low{color:#67C23A;font-weight:bold;}
.footer{text-align:center;color:#999;margin-top:40px;font-size:12px;}
</style></head><body>
<h1>控制链穿透分析报告</h1>
<div class="summary">
<p><b>企业名称：</b>${chain.enterpriseName}</p>
<p><b>报告生成时间：</b>${timeStr}</p>
<p><b>统计周期：</b>${chain.statPeriod || '未指定'}</p>
</div>
<h2>一、基本信息</h2>
<table><tr><th>指标</th><th>数值</th></tr>
<tr><td>控制链总数</td><td>${chain.totalChains || 0}</td></tr>
<tr><td>直接控制链</td><td>${chain.directChains || 0}</td></tr>
<tr><td>间接控制链</td><td>${chain.indirectChains || 0}</td></tr>
<tr><td>平均控制强度</td><td>${chain.avgControlStrength || 0}%</td></tr>
<tr><td>最大链长</td><td>${chain.maxChainLength || 0} 级</td></tr>
<tr><td>环路数</td><td>${chain.loopCount || 0}</td></tr>
<tr><td>高风险数</td><td>${chain.highRiskCount || 0}</td></tr>
</table>
<h2>二、可视化分析统计</h2>
<table><tr><th>指标</th><th>数值</th></tr>
<tr><td>节点总数</td><td>${stats.totalNodes}</td></tr>
<tr><td>关系总数</td><td>${stats.totalEdges}</td></tr>
<tr><td>最大层级</td><td>${stats.maxLevel}</td></tr>
<tr><td>复杂度评分</td><td>${stats.complexityScore}</td></tr>
</table>
<h2>三、风险评估</h2>
<p>整体风险等级：<span class="${(chain.highRiskCount || 0) > 0 ? 'risk-high' : (chain.loopCount || 0) > 0 ? 'risk-medium' : 'risk-low'}">${(chain.highRiskCount || 0) > 0 ? '高风险' : (chain.loopCount || 0) > 0 ? '中风险' : '低风险'}</span></p>
<ul>
${(chain.maxChainLength || 0) > 4 ? '<li>控制链路过长（' + chain.maxChainLength + '级），超过4级预警阈值</li>' : ''}
${(chain.loopCount || 0) > 0 ? '<li>存在' + chain.loopCount + '个控制环路，可能导致利益冲突</li>' : ''}
${(chain.highRiskCount || 0) > 0 ? '<li>存在' + chain.highRiskCount + '条高风险控制链</li>' : ''}
${(chain.avgControlStrength || 0) < 50 ? '<li>平均控制强度不足50%，存在控制权不稳定风险</li>' : ''}
</ul>
<h2>四、建议</h2>
<ul>
${(chain.maxChainLength || 0) > 3 ? '<li>建议优化管理层级，缩短控制链路</li>' : ''}
${(chain.loopCount || 0) > 0 ? '<li>建议清理交叉持股关系，消除控制环路</li>' : ''}
${(chain.highRiskCount || 0) > 0 ? '<li>建议加强高风险控制链的监控和管理</li>' : ''}
<li>建议定期进行控制链穿透分析，持续监测变化</li>
</ul>
<div class="footer">本报告由示例云控制链穿透分析系统自动生成 | ${timeStr}</div>
</body></html>`

        // 创建Blob并下载
        const blob = new Blob([htmlContent], { type: 'text/html;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = chain.enterpriseName + '_控制链分析报告_' + now.getTime() + '.html'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('分析报告已下载')
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      } finally {
        loading.close()
      }
    },

    // 节点点击事件
    handleNodeClick(params) {
      console.log('节点点击:', params.data)
    },

    // 节点双击事件
    handleNodeDoubleClick(params) {
      console.log('节点双击:', params.data)
    },

    // 窗口大小变化
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },

    // 销毁图表
    destroyChart() {
      if (this.chart) {
        window.removeEventListener('resize', this.handleResize)
        this.chart.dispose()
        this.chart = null
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    }
  },

  beforeDestroy() {
    this.destroyChart()
  }
}
</script>

<style scoped>
.visualization-container {
  height: 100%;
}

.toolbar {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.filter-controls {
  text-align: right;
}

.chart-container {
  position: relative;
  margin-bottom: 20px;
}

.chart {
  width: 100%;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
}

.info-panel {
  margin-top: 20px;
}

.legend-content {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 12px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 5px;
}

.legend-line {
  width: 20px;
  height: 2px;
  background-color: #606266;
  margin-right: 5px;
}

.stats-content {
  font-size: 12px;
}

.dialog-footer {
  text-align: right;
}
</style>
