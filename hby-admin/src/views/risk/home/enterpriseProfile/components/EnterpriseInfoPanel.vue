<template>
  <div class="enterprise-info-panel">
    <div class="panel-header">
      <div class="title-section">
        <h1 class="main-title"><i class="el-icon-s-home"></i> 监管驾驶舱</h1>

        <!-- 树形公司选择器 -->
        <div class="enterprise-selector">
          <el-popover
            v-model="treePopVisible"
            placement="bottom-start"
            trigger="click"
            popper-class="company-tree-popper"
            :width="280"
          >
            <!-- 触发按钮 -->
            <div
              slot="reference"
              class="tree-select-trigger"
              :class="{ 'is-loading': treeLoading }"
            >
              <i class="el-icon-office-building trigger-icon"></i>
              <span class="trigger-text">{{ selectedEnterpriseName || '选择公司' }}</span>
              <i :class="treePopVisible ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" class="arrow-icon"></i>
            </div>

            <!-- 搜索框 -->
            <div class="tree-search-wrap">
              <el-input
                v-model="treeFilterText"
                placeholder="搜索公司..."
                size="small"
                prefix-icon="el-icon-search"
                clearable
              />
            </div>

            <!-- 树形组织架构 -->
            <el-tree
              ref="orgTree"
              v-loading="treeLoading"
              :data="treeData"
              :props="treeProps"
              :filter-node-method="filterNode"
              :highlight-current="true"
              :default-expanded-keys="defaultExpandedKeys"
              node-key="id"
              @node-click="handleTreeNodeClick"
              class="org-tree"
            >
              <span slot-scope="{ node, data }" class="tree-node">
                <i :class="data.children && data.children.length ? 'el-icon-folder' : 'el-icon-document'" class="node-icon"></i>
                <span class="node-label" :title="node.label">{{ node.label }}</span>
              </span>
            </el-tree>
          </el-popover>
        </div>
      </div>

      <div class="action-section">
        <el-button
          type="primary"
          icon="el-icon-refresh"
          @click="handleRefresh"
          :loading="loading"
        >
          刷新数据
        </el-button>
        <el-button
          type="success"
          icon="el-icon-download"
          @click="handleExport"
        >
          导出数据
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-full-screen"
          @click="handleFullscreen"
          title="全屏展示"
        >
          全屏展示
        </el-button>
        <div class="update-time">
          最后更新：{{ updateTime }}
        </div>
      </div>
    </div>

    <div class="panel-content" v-if="enterpriseData" style="display: none;"></div>
    <div v-else-if="loading" class="loading-state"></div>
    <div v-else class="empty-state"></div>
  </div>
</template>

<script>
import { exportEnterpriseData } from '@/api/risk/enterpriseProfile'
import { findOrganizationData } from '@/api/setting/org'

export default {
  name: 'EnterpriseInfoPanel',
  props: {
    enterpriseData: { type: Object, default: null },
    loading:        { type: Boolean, default: false },
    externalEnterpriseId: { type: [String, Number], default: null }
  },
  data() {
    return {
      // 树形数据
      treeData: [],
      treeLoading: false,
      treeProps: { children: 'children', label: 'label' },
      treePopVisible: false,
      treeFilterText: '',
      defaultExpandedKeys: [],
      // 当前选中
      selectedEnterpriseId: null,
      selectedEnterpriseName: '',
      updateTime: '',
      _timeTimer: null
    }
  },
  watch: {
    enterpriseData: {
      handler(v) {
        if (v && v.systemStatus) this.updateTime = v.systemStatus.lastUpdateTime
      },
      immediate: true
    },
    externalEnterpriseId: {
      handler(val) {
        if (val != null) {
          this.selectedEnterpriseId = String(val)
          this._syncNameFromTree()
        }
      },
      immediate: true
    },
    treeFilterText(val) {
      this.$refs.orgTree && this.$refs.orgTree.filter(val)
    }
  },
  mounted() {
    this.loadTree()
    this.updateCurrentTime()
    this._timeTimer = setInterval(this.updateCurrentTime, 60000)
  },
  beforeDestroy() {
    if (this._timeTimer) { clearInterval(this._timeTimer); this._timeTimer = null }
  },
  methods: {
    /* 加载树形组织架构 */
    async loadTree() {
      this.treeLoading = true
      try {
        const res = await findOrganizationData({ nodeId: '' })
        if (res && Array.isArray(res) && res.length > 0) {
          this.treeData = this._formatTree(res)
          // 默认展开第一层
          if (this.treeData[0]) this.defaultExpandedKeys = [this.treeData[0].id]
          // 同步当前选中公司的名称
          this._syncNameFromTree()
          // 如果还没有选中，默认选第一个
          if (!this.selectedEnterpriseId && this.treeData[0]) {
            this.selectedEnterpriseId = String(this.treeData[0].id)
            this.selectedEnterpriseName = this.treeData[0].label
            this.$emit('enterprise-ready', this.selectedEnterpriseId)
          }
        }
      } catch (e) {
        console.error('[loadTree]', e)
      } finally {
        this.treeLoading = false
      }
    },

    _formatTree(nodes) {
      return (nodes || []).map(n => ({
        id: String(n.id),
        label: n.name || n.label,
        isParent: n.isParent,
        children: n.children && n.children.length ? this._formatTree(n.children) : []
      }))
    },

    _syncNameFromTree() {
      if (!this.selectedEnterpriseId || !this.treeData.length) return
      const found = this._findNode(this.treeData, this.selectedEnterpriseId)
      if (found) this.selectedEnterpriseName = found.label
    },

    _findNode(nodes, id) {
      for (const n of nodes) {
        if (String(n.id) === String(id)) return n
        if (n.children && n.children.length) {
          const r = this._findNode(n.children, id)
          if (r) return r
        }
      }
      return null
    },

    filterNode(value, data) {
      if (!value) return true
      return data.label.includes(value)
    },

    handleTreeNodeClick(data) {
      this.selectedEnterpriseId = String(data.id)
      this.selectedEnterpriseName = data.label
      this.treePopVisible = false
      this.treeFilterText = ''
      this.$emit('enterprise-change', this.selectedEnterpriseId)
    },

    handleRefresh() { this.$emit('refresh') },

    async handleExport() {
      try {
        const response = await exportEnterpriseData({ enterpriseId: this.selectedEnterpriseId, exportType: 'excel' })
        if (response.code === 1) {
          this.$message.success('导出成功')
          if (response.data && response.data.downloadUrl) window.open(response.data.downloadUrl)
        } else {
          this.$message.error(response.msg)
        }
      } catch (e) {
        this.$message.error('导出失败')
      }
    },

    handleFullscreen() {
      try {
        const el = document.querySelector('.enterprise-profile-dashboard') || document.documentElement
        if (!document.fullscreenElement) {
          ;(el.requestFullscreen || el.webkitRequestFullscreen || el.mozRequestFullScreen).call(el)
          this.$message.success('已进入全屏，按ESC退出')
        } else {
          ;(document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen).call(document)
        }
      } catch (e) {
        this.$message.error('全屏操作失败')
      }
    },

    formatNumber(num) {
      if (!num) return '0'
      return Number(num).toLocaleString()
    },

    updateCurrentTime() {
      if (!this.updateTime) this.updateTime = new Date().toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.enterprise-info-panel {
  background: transparent;
  border-radius: 0;
  padding: 14px 20px;
  box-shadow: none;
  height: auto;
  display: flex;
  flex-direction: column;

  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0;

    .title-section {
      display: flex;
      align-items: center;
      gap: 16px;

      .main-title {
        font-size: 20px;
        font-weight: 800;
        color: #fff;
        margin: 0;
        letter-spacing: 2px;
        text-shadow: 0 2px 8px rgba(0,0,0,0.35);
        display: flex;
        align-items: center;
        gap: 6px;

        i { color: #d4891a; font-size: 20px; }
      }

      .enterprise-selector {
        // 树形触发按钮
        .tree-select-trigger {
          display: inline-flex;
          align-items: center;
          gap: 6px;
          min-width: 200px;
          max-width: 280px;
          height: 32px;
          padding: 0 10px;
          background: rgba(255,255,255,0.12);
          border: 1px solid rgba(255,255,255,0.3);
          border-radius: 4px;
          cursor: pointer;
          transition: all .2s;
          &:hover { background: rgba(255,255,255,0.2); border-color: rgba(255,255,255,0.5); }
          .trigger-icon { color: #d4891a; font-size: 14px; flex-shrink: 0; }
          .trigger-text  { color: #fff; font-size: 13px; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
          .arrow-icon    { color: rgba(255,255,255,0.7); font-size: 12px; flex-shrink: 0; }
          &.is-loading .trigger-text { opacity: .5; }
        }
      }
    }

    .action-section {
      display: flex;
      align-items: center;
      gap: 10px;

      .update-time {
        font-size: 12px;
        color: rgba(255,255,255,0.6);
      }
    }
  }

  .panel-content {
    flex: 1;
    display: flex;
    gap: 30px;

    .basic-info {
      flex: 1;

      .enterprise-name {
        font-size: 20px;
        font-weight: bold;
        color: #2a5298;
        margin-bottom: 10px;
      }

      .enterprise-details {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;

        .detail-item {
          display: flex;
          align-items: center;
          gap: 5px;
          font-size: 14px;
          color: #666;

          i {
            color: #1e3c72;
          }
        }
      }
    }

    .key-metrics {
      display: flex;
      gap: 15px;

      .metric-card {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 15px;
        border-radius: 8px;
        text-align: center;
        min-width: 120px;

        .metric-value {
          font-size: 20px;
          font-weight: bold;
          margin-bottom: 5px;
        }

        .metric-label {
          font-size: 12px;
          opacity: 0.9;
        }
      }
    }
  }

  .loading-state,
  .empty-state {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>

<!-- 弹出层样式（不加 scoped） -->
<style lang="scss">
.company-tree-popper {
  padding: 0 !important;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.2);
  overflow: hidden;

  .tree-search-wrap {
    padding: 8px 10px;
    border-bottom: 1px solid #f0f0f0;
    background: #fafafa;
    .el-input { width: 100%; }
  }

  .org-tree {
    max-height: 360px;
    overflow-y: auto;
    padding: 6px 0;
    .tree-node {
      display: flex;
      align-items: center;
      gap: 5px;
      font-size: 13px;
      .node-icon  { color: #d4891a; font-size: 14px; }
      .node-label { max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    }
    .el-tree-node__content:hover { background: #f0f7ff; }
    .el-tree-node.is-current > .el-tree-node__content {
      background: #e6f0ff;
      color: #1a56db;
      font-weight: 600;
    }
  }
}
</style>
