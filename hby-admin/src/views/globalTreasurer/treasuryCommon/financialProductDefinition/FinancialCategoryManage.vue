<template>
  <div class="financial-category-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-menu"></i>
            金融分类管理
          </h2>
          <p class="page-description">管理金融产品分类体系，包括产品类别、子类别和分类规则配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增分类
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出分类
          </el-button>
        </div>
      </div>
    </div>

    <!-- 分类统计卡片 -->
    <div class="category-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总分类数</div>
                <div class="card-value">{{ totalCategories }}</div>
                <div class="card-change">已配置分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon parent-icon">
                <i class="el-icon-folder"></i>
              </div>
              <div class="card-info">
                <div class="card-title">主分类</div>
                <div class="card-value">{{ parentCategories }}</div>
                <div class="card-change positive">一级分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon sub-icon">
                <i class="el-icon-folder-opened"></i>
              </div>
              <div class="card-info">
                <div class="card-title">子分类</div>
                <div class="card-value">{{ subCategories }}</div>
                <div class="card-change">二级分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon product-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">关联产品</div>
                <div class="card-value">{{ linkedProducts }}</div>
                <div class="card-change">产品数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="分类编码">
            <el-input
              v-model="listQuery.categoryCode"
              placeholder="请输入分类编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="分类名称">
            <el-input
              v-model="listQuery.categoryName"
              placeholder="请输入分类名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="父分类">
            <el-select
              v-model="listQuery.parentCategoryId"
              placeholder="请选择父分类"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="category in parentCategories"
                :key="category.categoryId"
                :label="category.categoryName"
                :value="category.categoryId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="分类编码" prop="categoryCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.categoryCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="父分类" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.parentCategoryName || '顶级分类' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" min-width="250px" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'FinancialCategoryManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        categoryCode: undefined,
        categoryName: undefined,
        parentCategoryId: undefined,
        isEnabled: undefined
      },
      parentCategories: [],
      totalCategories: 0,
      enabledCategories: 0,
      topLevelCategories: 0
    }
  },
  created() {
    this.getParentCategories()
    this.getList()
  },
  methods: {
    getParentCategories() {
      // 获取父分类列表
      this.parentCategories = [
        { categoryId: 1, categoryName: '银行理财' },
        { categoryId: 2, categoryName: '债券投资' },
        { categoryId: 3, categoryName: '股票投资' },
        { categoryId: 4, categoryName: '基金投资' },
        { categoryId: 5, categoryName: '衍生品投资' }
      ]
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            categoryId: 1,
            categoryCode: 'BANK_WEALTH',
            categoryName: '银行理财',
            parentCategoryId: null,
            parentCategoryName: null,
            description: '银行发行的理财产品，包括保本型和非保本型理财产品',
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00',
            updateTime: '2024-09-25 15:30:00',
            createUser: 1,
            updateUser: 1
          },
          {
            categoryId: 2,
            categoryCode: 'BOND_INVEST',
            categoryName: '债券投资',
            parentCategoryId: null,
            parentCategoryName: null,
            description: '债券类投资产品，包括国债、企业债、金融债等',
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00',
            updateTime: '2024-09-25 15:30:00',
            createUser: 1,
            updateUser: 1
          },
          {
            categoryId: 6,
            categoryCode: 'CORP_BOND',
            categoryName: '企业债券',
            parentCategoryId: 2,
            parentCategoryName: '债券投资',
            description: '企业发行的债券产品',
            isEnabled: 1,
            createTime: '2024-01-15 14:30:00',
            updateTime: '2024-09-25 15:30:00',
            createUser: 1,
            updateUser: 1
          }
        ]
        this.total = 3
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>
