<template>
  <div class="treasury-page-template">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i :class="pageIcon"></i>
            {{ pageTitle }}
          </h2>
          <p class="page-description">{{ pageDescription }}</p>
        </div>
        <div class="header-right">
          <slot name="header-actions">
            <el-button type="primary" icon="el-icon-plus" @click="$emit('create')">
              {{ createButtonText }}
            </el-button>
            <el-button type="success" icon="el-icon-download" @click="$emit('export')">
              导出数据
            </el-button>
          </slot>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <slot name="search-form">
          <!-- 默认搜索表单 -->
        </slot>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">{{ tableTitle }}</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="$emit('refresh')">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="$emit('table-setting')">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <slot name="table-content">
        <!-- 表格内容插槽 -->
      </slot>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          background
          :current-page="currentPage"
          :layout="paginationLayout"
          :page-size="pageSize"
          :total="total"
          :page-sizes="pageSizes"
          @current-change="$emit('page-change', $event)"
          @size-change="$emit('size-change', $event)"
        />
      </div>
    </el-card>

    <!-- 对话框插槽 -->
    <slot name="dialogs">
      <!-- 对话框内容插槽 -->
    </slot>
  </div>
</template>

<script>
export default {
  name: 'TreasuryPageTemplate',
  props: {
    pageTitle: {
      type: String,
      default: '财资管理'
    },
    pageDescription: {
      type: String,
      default: '财资业务管理功能'
    },
    pageIcon: {
      type: String,
      default: 'el-icon-setting'
    },
    createButtonText: {
      type: String,
      default: '新增'
    },
    tableTitle: {
      type: String,
      default: '数据列表'
    },
    total: {
      type: Number,
      default: 0
    },
    currentPage: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 20
    },
    paginationLayout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    pageSizes: {
      type: Array,
      default: () => [10, 20, 50, 100]
    }
  }
}
</script>

<style lang="scss" scoped>
.treasury-page-template {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;
      
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;
          
          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }
        
        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }
      
      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
  }

  .table-card {
    border-radius: 8px;
    
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
        
        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }
    
    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}
</style>
