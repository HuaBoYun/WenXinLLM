<template>
  <el-dialog
    title="选择SQL模板"
    :visible.sync="visible"
    width="1000px"
    :before-close="handleClose"
    append-to-body
  >
    <div class="template-search">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索模板名称"
            prefix-icon="el-icon-search"
            @input="handleSearch"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-select v-model="filterType" placeholder="模板类型" @change="handleSearch" clearable>
            <el-option label="全部" value="" />
            <el-option label="查询" value="SELECT" />
            <el-option label="插入" value="INSERT" />
            <el-option label="更新" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="filterCategory" placeholder="分类" @change="handleSearch" clearable>
            <el-option label="全部" value="" />
            <el-option label="基础查询" value="BASIC" />
            <el-option label="统计分析" value="ANALYSIS" />
            <el-option label="报表查询" value="REPORT" />
            <el-option label="维护操作" value="MAINTENANCE" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="loadTemplates" icon="el-icon-refresh">刷新</el-button>
        </el-col>
      </el-row>
    </div>
    
    <div v-loading="loading" class="template-table">
      <el-table
        :data="templateList"
        border
        style="width: 100%"
        @row-click="handleRowClick"
        highlight-current-row
        max-height="400"
      >
        <el-table-column prop="templateName" label="模板名称" width="200" show-overflow-tooltip />
        <el-table-column prop="templateType" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.templateType)" size="small">
            {{ getTypeText(scope.row.templateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="分类" width="100">
        <template slot-scope="scope">
          {{ getCategoryText(scope.row.category) }}
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="usageCount" label="使用次数" width="100" />
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'Y' ? 'success' : 'danger'" size="mini">
            {{ scope.row.status === 'Y' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="selectTemplate(scope.row)" :disabled="scope.row.status !== 'Y'">
            选择
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
    
    <el-divider content-position="left">SQL预览</el-divider>
    <div v-if="selectedTemplate" class="sql-preview">
      <h4>{{ selectedTemplate.templateName }}</h4>
      <p><strong>描述：</strong>{{ selectedTemplate.description }}</p>
      <el-input
        v-model="selectedTemplate.sqlContent"
        type="textarea"
        :rows="8"
        readonly
      />
    </div>
    <div v-else class="empty-preview">
      <p>请选择一个模板查看SQL内容</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="confirmSelect" :disabled="!selectedTemplate">确定选择</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getSqlTemplateList } from '@/api/mxgl'

export default {
  name: 'SqlTemplateSelectDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      searchKeyword: '',
      filterType: '',
      filterCategory: '',
      selectedTemplate: null,
      templateList: [],
      total: 0,
      queryForm: {
        pageNum: 1,
        pageSize: 20,
        templateName: '',
        templateType: '',
        category: '',
        isEnabled: 'Y'
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadTemplates()
      }
    }
  },
  methods: {
    // 加载模板列表
    async loadTemplates() {
      this.loading = true
      try {
        console.log('📊 SQL模板选择 - 查询参数:', this.queryForm)
        const response = await getSqlTemplateList(this.queryForm)
        console.log('📊 SQL模板选择 - 接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.templateList = response.data.records || []
          this.total = response.data.total || 0
          console.log('✅ 模板列表加载成功(标准格式):', this.templateList.length, '条数据')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.templateList = response.records || []
          this.total = response.total || 0
          console.log('✅ 模板列表加载成功(直接格式):', this.templateList.length, '条数据')
        } else {
          this.$message.error(response.msg || '加载模板列表失败')
          console.error('❌ 模板列表加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载模板列表失败:', error)
        this.$message.error('加载模板列表失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索处理
    handleSearch() {
      this.queryForm.pageNum = 1
      this.queryForm.templateName = this.searchKeyword
      this.queryForm.templateType = this.filterType
      this.queryForm.category = this.filterCategory
      this.loadTemplates()
    },

    // 分页处理
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNum = 1
      this.loadTemplates()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.loadTemplates()
    },

    // 行点击
    handleRowClick(row) {
      this.selectedTemplate = row
    },

    // 选择模板
    selectTemplate(template) {
      this.selectedTemplate = template
      // 🔥 直接选择模板并关闭对话框
      this.$emit('select', template)
      this.handleClose()
    },

    // 确认选择
    confirmSelect() {
      if (this.selectedTemplate) {
        this.$emit('select', this.selectedTemplate)
        // 🔥 选择后关闭对话框
        this.handleClose()
      }
    },

    // 关闭对话框
    handleClose() {
      this.selectedTemplate = null
      this.searchKeyword = ''
      this.filterType = ''
      this.filterCategory = ''
      // 🔥 重置查询表单
      this.queryForm = {
        pageNum: 1,
        pageSize: 20,
        templateName: '',
        templateType: '',
        category: '',
        isEnabled: 'Y'
      }
      this.$emit('update:visible', false)
    },

    // 获取类型颜色
    getTypeColor(type) {
      const colors = {
        'SELECT': 'primary',
        'INSERT': 'success',
        'UPDATE': 'warning',
        'DELETE': 'danger'
      }
      return colors[type] || 'info'
    },

    // 获取类型文本
    getTypeText(type) {
      const texts = {
        'SELECT': '查询',
        'INSERT': '插入',
        'UPDATE': '更新',
        'DELETE': '删除'
      }
      return texts[type] || type
    },

    // 获取分类文本
    getCategoryText(category) {
      const texts = {
        'BASIC': '基础查询',
        'ANALYSIS': '统计分析',
        'REPORT': '报表查询',
        'MAINTENANCE': '维护操作'
      }
      return texts[category] || category
    }
  }
}
</script>

<style scoped>
.template-search {
  margin-bottom: 20px;
}

.template-table {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: center;
  margin: 20px 0;
}

.sql-preview {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-top: 10px;
}

.sql-preview h4 {
  color: #409EFF;
  margin-bottom: 10px;
}

.empty-preview {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}

.el-table__row {
  cursor: pointer;
}

.el-table__row:hover {
  background-color: #f5f7fa;
}
</style>
