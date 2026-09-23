<template>
  <div class="ukey-vendor-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-key"></i>
            Ukey厂商管理
          </h2>
          <p class="page-description">管理Ukey设备厂商信息，包括厂商资质、产品型号、技术支持和合作状态</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增厂商
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleCertification">
            资质认证
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 厂商统计卡片 -->
    <div class="vendor-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-key"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总厂商数</div>
                <div class="card-value">{{ totalVendors }}</div>
                <div class="card-change">已合作厂商</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon certified-icon">
                <i class="el-icon-medal"></i>
              </div>
              <div class="card-info">
                <div class="card-title">认证厂商</div>
                <div class="card-value">{{ certifiedVendors }}</div>
                <div class="card-change positive">资质认证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">活跃厂商</div>
                <div class="card-value">{{ activeVendors }}</div>
                <div class="card-change positive">正常合作</div>
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
                <div class="card-title">产品型号</div>
                <div class="card-value">{{ productModels }}</div>
                <div class="card-change">支持型号</div>
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
          <el-form-item label="厂商名称">
            <el-input
              v-model="listQuery.name"
              placeholder="请输入厂商名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="厂商类型">
            <el-select
              v-model="listQuery.vendorType"
              placeholder="请选择厂商类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="硬件厂商" value="HARDWARE" />
              <el-option label="软件厂商" value="SOFTWARE" />
              <el-option label="集成厂商" value="INTEGRATION" />
              <el-option label="服务厂商" value="SERVICE" />
            </el-select>
          </el-form-item>
          <el-form-item label="认证状态">
            <el-select
              v-model="listQuery.certificationStatus"
              placeholder="请选择认证状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="已认证" value="CERTIFIED" />
              <el-option label="待认证" value="PENDING" />
              <el-option label="未认证" value="UNCERTIFIED" />
            </el-select>
          </el-form-item>
          <el-form-item label="合作状态">
            <el-select
              v-model="listQuery.cooperationStatus"
              placeholder="请选择合作状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常合作" value="ACTIVE" />
              <el-option label="暂停合作" value="SUSPENDED" />
              <el-option label="终止合作" value="TERMINATED" />
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
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" min-width="200px">
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status === 1 ? '启用' : '禁用' }}
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
import { getUkeyVendorList, saveOrUpdateUkeyVendor, deleteUkeyVendor } from '@/api/globalTreasurer/czgg'

export default {
  name: 'UkeyVendorManage',
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
        name: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getUkeyVendorList(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data || []
          this.total = response.result ? response.result.total : (response.total || 0)
        } else {
          this.$message.error(response.msg || '获取数据失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取Ukey厂商列表失败:', error)
        this.$message.error('获取数据失败')
        this.listLoading = false
      })
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
        deleteUkeyVendor(row.id).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    }
  }
}
</script>
