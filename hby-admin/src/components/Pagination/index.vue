<template>
  <div class="pagination-container">
    <el-pagination
      :background="background"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="totalNum"
      :pager-count="pagerCount"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    total: {
      required: true,
      type: [Number, String]
    },
    page: {
      type: [Number, String],
      default: 1
    },
    limit: {
      type: [Number, String],
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 30, 50]
      }
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    pagerCount: {
      type: Number,
      default: 7
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    // 强制转 Number，防止后端返回字符串导致 el-pagination 内部 Math.ceil 计算异常
    totalNum() {
      return Number(this.total) || 0
    },
    currentPage: {
      get() {
        return Number(this.page) || 1
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return Number(this.limit) || 10
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', { page: this.currentPage, limit: val })
      if (this.autoScroll) {
        this.scrollToTop()
      }
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll) {
        this.scrollToTop()
      }
    },
    scrollToTop() {
      window.scrollTo(0, 0)
    }
  }
}
</script>

<style scoped>
.pagination-container {
  padding: 32px 16px;
  text-align: center;
}
.pagination-container.hidden {
  display: none;
}
/* 确保分页组件居中显示 */
.pagination-container ::v-deep .el-pagination {
  text-align: center;
}
/* 强制显示页码与跳转，避免被全局 mobile 规则隐藏 */
.pagination-container ::v-deep .el-pager,
.pagination-container ::v-deep .el-pagination__jump {
  display: inline-block !important;
}
.pagination-container ::v-deep .el-pager li {
  display: inline-block !important;
}
/* 优化页码按钮样式 */
.pagination-container ::v-deep .el-pager li {
  background-color: #fff;
  color: #606266;
  border: 1px solid #dcdfe6;
  margin: 0 2px;
  min-width: 30px;
  height: 28px;
  line-height: 28px;
  border-radius: 2px;
  font-weight: 500;
  transition: all 0.3s;
}
.pagination-container ::v-deep .el-pager li:hover {
  color: #409eff;
  border-color: #409eff;
}
.pagination-container ::v-deep .el-pager li.active {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}
/* 优化上下页按钮样式 */
.pagination-container ::v-deep .btn-prev,
.pagination-container ::v-deep .btn-next {
  background-color: #fff;
  color: #606266;
  border: 1px solid #dcdfe6;
  border-radius: 2px;
  font-weight: 500;
}
.pagination-container ::v-deep .btn-prev:hover,
.pagination-container ::v-deep .btn-next:hover {
  color: #409eff;
  border-color: #409eff;
}
.pagination-container ::v-deep .btn-prev:disabled,
.pagination-container ::v-deep .btn-next:disabled {
  color: #c0c4cc;
  border-color: #ebeef5;
  background-color: #f4f4f5;
}
/* 优化每页条数选择器样式 */
.pagination-container ::v-deep .el-pagination__sizes {
  margin-right: 10px;
}
.pagination-container ::v-deep .el-select .el-input__inner {
  border-radius: 2px;
}
/* 优化跳转输入框样式 */
.pagination-container ::v-deep .el-pagination__jump {
  margin-left: 10px;
}
.pagination-container ::v-deep .el-pagination__jump .el-input__inner {
  border-radius: 2px;
  text-align: center;
}
</style>
