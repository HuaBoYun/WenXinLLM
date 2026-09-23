<template>
  <el-dialog
    title="组织架构图"
    :visible.sync="dialogVisible"
    width="1200px"
    @close="handleClose"
  >
    <div class="chart-toolbar">
      <el-button-group>
        <el-button size="small" @click="zoomIn">放大</el-button>
        <el-button size="small" @click="zoomOut">缩小</el-button>
        <el-button size="small" @click="resetZoom">重置</el-button>
        <el-button size="small" @click="fullScreen">全屏</el-button>
      </el-button-group>
      
      <el-select v-model="viewMode" size="small" style="margin-left: 20px;">
        <el-option label="树形视图" value="tree"></el-option>
        <el-option label="网络视图" value="network"></el-option>
        <el-option label="层级视图" value="hierarchy"></el-option>
      </el-select>
      
      <el-checkbox v-model="showEmployeeCount" style="margin-left: 20px;">
        显示人员数量
      </el-checkbox>
      <el-checkbox v-model="showDepartmentCode">
        显示部门编码
      </el-checkbox>
    </div>
    
    <div ref="orgChart" class="org-chart-container"></div>
    
    <div class="chart-legend">
      <div class="legend-item">
        <span class="legend-color" style="background-color: #409EFF;"></span>
        <span>一级部门</span>
      </div>
      <div class="legend-item">
        <span class="legend-color" style="background-color: #67C23A;"></span>
        <span>二级部门</span>
      </div>
      <div class="legend-item">
        <span class="legend-color" style="background-color: #E6A23C;"></span>
        <span>三级部门</span>
      </div>
      <div class="legend-item">
        <span class="legend-color" style="background-color: #F56C6C;"></span>
        <span>四级及以下</span>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportChart">导出图片</el-button>
      <el-button type="success" @click="editStructure">编辑架构</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getOrganizationChart } from '@/api/enterprise/data'

export default {
  name: 'OrganizationChartDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    orgData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      viewMode: 'tree',
      showEmployeeCount: true,
      showDepartmentCode: false,
      chartInstance: null,
      zoomLevel: 1,
      orgChartData: null
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
        this.$nextTick(() => {
          this.initChart()
        })
      }
    },
    viewMode() {
      this.updateChart()
    },
    showEmployeeCount() {
      this.updateChart()
    },
    showDepartmentCode() {
      this.updateChart()
    }
  },
  methods: {
    initChart() {
      const container = this.$refs.orgChart
      if (!container) return

      getOrganizationChart({
        orgId: this.orgData.id,
        viewMode: this.viewMode,
        showEmployeeCount: this.showEmployeeCount,
        showDepartmentCode: this.showDepartmentCode
      }).then(response => {
        this.orgChartData = response.data || {}
        this.renderChart(container)
      }).catch(() => {
        container.innerHTML = '<div style="text-align: center; padding: 50px; color: #909399;">加载组织架构数据失败</div>'
      })
    },
    renderChart(container) {
      if (!this.orgChartData || !this.orgChartData.departments) {
        container.innerHTML = '<div style="text-align: center; padding: 50px; color: #909399;">暂无组织架构数据</div>'
        return
      }
      const data = this.orgChartData
      let html = '<div style="text-align: center; padding: 30px;">'
      html += `<h3>${data.orgName || ''}</h3>`
      if (data.departments && data.departments.length > 0) {
        html += '<div style="margin-top: 20px;">'
        data.departments.forEach(dept => {
          html += `<div style="display: inline-block; margin: 10px; padding: 10px; border: 1px solid #409EFF; border-radius: 4px; background: #ecf5ff;">`
          html += `${dept.name || ''}`
          if (this.showEmployeeCount && dept.employeeCount !== undefined) {
            html += `<br/><small>${dept.employeeCount}人</small>`
          }
          if (this.showDepartmentCode && dept.code) {
            html += `<br/><small>${dept.code}</small>`
          }
          html += '</div>'
        })
        html += '</div>'
      }
      html += `<p style="margin-top: 30px; color: #909399;">当前视图模式：${this.viewMode === 'tree' ? '树形视图' : this.viewMode === 'network' ? '网络视图' : '层级视图'}</p>`
      html += '</div>'
      container.innerHTML = html
    },
    updateChart() {
      this.initChart()
    },
    zoomIn() {
      this.zoomLevel = Math.min(this.zoomLevel * 1.2, 3)
      this.applyZoom()
    },
    zoomOut() {
      this.zoomLevel = Math.max(this.zoomLevel / 1.2, 0.5)
      this.applyZoom()
    },
    resetZoom() {
      this.zoomLevel = 1
      this.applyZoom()
    },
    applyZoom() {
      const container = this.$refs.orgChart
      if (container) {
        container.style.transform = `scale(${this.zoomLevel})`
      }
    },
    fullScreen() {
      const container = this.$refs.orgChart
      if (container.requestFullscreen) {
        container.requestFullscreen()
      } else if (container.webkitRequestFullscreen) {
        container.webkitRequestFullscreen()
      } else if (container.mozRequestFullScreen) {
        container.mozRequestFullScreen()
      }
    },
    exportChart() {
      this.$message.success('组织架构图导出中...')
    },
    editStructure() {
      this.$emit('edit-structure', this.orgData)
      this.handleClose()
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.chart-toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.org-chart-container {
  height: 500px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: auto;
  background-color: #fff;
  transition: transform 0.3s ease;
}

.chart-legend {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.legend-item {
  display: flex;
  align-items: center;
  margin: 0 15px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
  margin-right: 5px;
}
</style>
