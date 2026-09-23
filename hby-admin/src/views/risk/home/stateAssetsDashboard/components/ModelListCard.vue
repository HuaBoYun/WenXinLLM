<template>
  <div class="model-list-card">
    <div class="card-header">
      <span class="card-title"><i class="el-icon-s-grid"></i> 模型组合列表</span>
      <div class="header-actions">
        <el-input
          v-model="searchKey"
          placeholder="搜索模型"
          prefix-icon="el-icon-search"
          clearable
          size="small"
          style="width: 200px;"
        />
        <span class="total-count">共 {{ filteredModels.length }} 个</span>
      </div>
    </div>

    <div class="tab-bar">
      <span
        v-for="tab in tabs"
        :key="tab.key"
        class="tab-item"
        :class="{ active: activeTab === tab.key }"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
        <em>{{ tab.count }}</em>
      </span>
    </div>

    <div class="card-body" v-loading="loading">
      <div v-if="filteredModels.length === 0" class="empty-wrap">
        <el-empty description="暂无模型数据" :image-size="60" />
      </div>
      <div v-else class="model-grid">
        <div
          v-for="model in filteredModels"
          :key="model.id || model.combinationId"
          class="model-card"
          :class="{ selected: selectedModelId === (model.id || model.combinationId) }"
          @click="handleSelectModel(model)"
        >
          <div class="model-header">
            <span class="model-name" :title="model.combinationName || model.name">
              {{ model.combinationName || model.name || '未命名模型' }}
            </span>
            <el-tag
              :type="getStatusType(model.status)"
              size="mini"
              class="model-status"
            >
              {{ getStatusText(model.status) }}
            </el-tag>
          </div>
          <div class="model-meta">
            <span class="meta-item">
              <i class="el-icon-folder"></i>
              {{ model.category || model.combinationCategory || '未分类' }}
            </span>
            <span class="meta-item">
              <i class="el-icon-user"></i>
              {{ model.createUser || model.creator || '-' }}
            </span>
          </div>
          <div class="model-footer">
            <span class="model-desc" :title="model.description || model.combinationDesc">
              {{ model.description || model.combinationDesc || '暂无描述' }}
            </span>
            <el-button
              type="text"
              size="mini"
              class="view-warning-btn"
              @click.stop="viewWarnings(model)"
            >
              查看预警 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ModelListCard',
  props: {
    modelList: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      activeTab: 'all',
      searchKey: '',
      selectedModelId: '',
    }
  },
  computed: {
    tabs() {
      const all = this.modelList.length
      const active = this.modelList.filter((m) => m.status === 'ACTIVE' || m.status === 1 || m.status === '1').length
      const inactive = all - active
      return [
        { key: 'all', label: '全部', count: all },
        { key: 'active', label: '启用', count: active },
        { key: 'inactive', label: '停用', count: inactive },
      ]
    },
    filteredModels() {
      let list = this.modelList
      if (this.activeTab === 'active') {
        list = list.filter((m) => m.status === 'ACTIVE' || m.status === 1 || m.status === '1')
      } else if (this.activeTab === 'inactive') {
        list = list.filter((m) => !(m.status === 'ACTIVE' || m.status === 1 || m.status === '1'))
      }
      if (this.searchKey) {
        const key = this.searchKey.toLowerCase()
        list = list.filter(
          (m) =>
            (m.combinationName || m.name || '').toLowerCase().includes(key) ||
            (m.category || '').toLowerCase().includes(key)
        )
      }
      return list
    },
  },
  methods: {
    getStatusType(status) {
      if (status === 'ACTIVE' || status === 1 || status === '1') return 'success'
      return 'info'
    },
    getStatusText(status) {
      if (status === 'ACTIVE' || status === 1 || status === '1') return '启用'
      return '停用'
    },
    handleSelectModel(model) {
      const id = model.id || model.combinationId
      this.selectedModelId = this.selectedModelId === id ? '' : id
      this.$emit('select-model', this.selectedModelId)
    },
    viewWarnings(model) {
      const id = model.id || model.combinationId
      const name = model.combinationName || model.name
      this.$router.push({
        path: '/risk/mxgl/fxyjgl',
        query: { modelId: id, modelName: name },
      }).catch(() => {})
    },
  },
}
</script>

<style lang="scss" scoped>
.model-list-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 16px;
    background: linear-gradient(90deg, #f5f7fa 0%, #ffffff 100%);
    border-bottom: 2px solid #e3f2fd;

    .card-title {
      font-size: 14px;
      font-weight: 600;
      color: #1565c0;
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 10px;

      .total-count {
        font-size: 12px;
        color: #78909c;
      }
    }
  }

  .tab-bar {
    display: flex;
    gap: 0;
    padding: 8px 16px 0;
    border-bottom: 1px solid #e8edf2;

    .tab-item {
      padding: 6px 16px;
      font-size: 13px;
      color: #607d8b;
      cursor: pointer;
      border-bottom: 2px solid transparent;
      transition: all 0.2s;
      display: flex;
      align-items: center;
      gap: 4px;

      em {
        font-style: normal;
        font-size: 11px;
        background: #eceff1;
        color: #607d8b;
        padding: 1px 5px;
        border-radius: 8px;
      }

      &.active {
        color: #1565c0;
        border-bottom-color: #1565c0;
        font-weight: 600;

        em {
          background: #bbdefb;
          color: #1565c0;
        }
      }

      &:hover:not(.active) {
        color: #1976d2;
        background: #f5f7fa;
      }
    }
  }

  .card-body {
    padding: 12px;
    min-height: 160px;
    max-height: 280px;
    overflow: auto;

    .empty-wrap {
      padding: 20px;
    }

    .model-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
      gap: 10px;
    }

    .model-card {
      border: 1px solid #e8edf2;
      border-radius: 6px;
      padding: 10px;
      cursor: pointer;
      transition: all 0.2s;
      background: #fafbfc;

      &:hover {
        border-color: #90caf9;
        box-shadow: 0 2px 8px rgba(21, 101, 192, 0.12);
        background: #f0f7ff;
      }

      &.selected {
        border-color: #1976d2;
        background: #e3f2fd;
        box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
      }

      .model-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 6px;
        gap: 6px;

        .model-name {
          font-size: 13px;
          font-weight: 600;
          color: #263238;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          flex: 1;
        }
      }

      .model-meta {
        display: flex;
        gap: 8px;
        margin-bottom: 6px;

        .meta-item {
          font-size: 11px;
          color: #90a4ae;
          display: flex;
          align-items: center;
          gap: 2px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .model-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 6px;

        .model-desc {
          font-size: 11px;
          color: #b0bec5;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          flex: 1;
        }

        .view-warning-btn {
          font-size: 11px;
          padding: 0;
          flex-shrink: 0;
          color: #1976d2;
        }
      }
    }
  }
}
</style>
