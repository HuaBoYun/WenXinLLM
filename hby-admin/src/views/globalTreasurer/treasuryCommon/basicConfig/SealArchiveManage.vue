<template>
  <div class="seal-archive-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-medal"></i>
            印鉴档案管理
          </h2>
          <p class="page-description">管理企业印鉴档案信息，包括印鉴登记、使用记录、权限控制和安全管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增印鉴
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="warning" icon="el-icon-lock" @click="handleSecurity">
            安全管理
          </el-button>
        </div>
      </div>
    </div>

    <!-- 印鉴统计卡片 -->
    <div class="seal-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-medal"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总印鉴数</div>
                <div class="card-value">{{ totalSeals }}</div>
                <div class="card-change">已登记印鉴</div>
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
                <div class="card-title">在用印鉴</div>
                <div class="card-value">{{ activeSeals }}</div>
                <div class="card-change positive">正常使用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon locked-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-info">
                <div class="card-title">锁定印鉴</div>
                <div class="card-value">{{ lockedSeals }}</div>
                <div class="card-change negative">安全锁定</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usage-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日使用</div>
                <div class="card-value">{{ todayUsage }}</div>
                <div class="card-change">使用次数</div>
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
          <el-form-item label="印鉴编码">
            <el-input
              v-model="listQuery.sealCode"
              placeholder="请输入印鉴编码"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="印鉴名称">
            <el-input
              v-model="listQuery.sealName"
              placeholder="请输入印鉴名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="印鉴类型">
            <el-select
              v-model="listQuery.sealTypeId"
              placeholder="请选择印鉴类型"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="type in sealTypes"
                :key="type.sealTypeId"
                :label="type.typeName"
                :value="type.sealTypeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="持有人">
            <el-input
              v-model="listQuery.ownerName"
              placeholder="请输入持有人姓名"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isActive"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="停用" value="0" />
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
      <el-table-column label="印鉴编码" prop="sealCode" sortable="custom" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.sealCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="印鉴名称" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sealName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="印鉴类型" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sealTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="持有人" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.ownerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="职位" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.ownerPosition }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.effectiveDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="失效日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expireDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isActive === 1 ? 'success' : 'danger'">
            {{ row.isActive === 1 ? '启用' : '停用' }}
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
import { getSealArchiveList, saveOrUpdateSealArchive, deleteSealArchive, getSealArchiveStatistics, getEnabledSealTypes } from '@/api/globalTreasurer/czgg'

export default {
  name: 'SealArchiveManage',
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
        sealCode: undefined,
        sealName: undefined,
        sealTypeId: undefined,
        ownerName: undefined,
        isActive: undefined
      },
      sealTypes: [],
      totalSeals: 0,
      activeSeals: 0,
      expiredSeals: 0,
      usageCount: 0
    }
  },
  created() {
    this.getSealTypes()
    this.getList()
    this.getStatistics()
  },
  methods: {
    getSealTypes() {
      // 获取印鉴类型列表
      getEnabledSealTypes().then(response => {
        if (response.code === 1) {
          this.sealTypes = response.data || []
        }
      }).catch(() => {
        // 接口失败时使用默认数据
        this.sealTypes = [
          { sealTypeId: 1, typeName: '公章' },
          { sealTypeId: 2, typeName: '财务章' },
          { sealTypeId: 3, typeName: '法人章' },
          { sealTypeId: 4, typeName: '合同章' },
          { sealTypeId: 5, typeName: '发票章' }
        ]
      })
    },
    getStatistics() {
      // 获取统计数据
      getSealArchiveStatistics().then(response => {
        if (response.code === 1 && response.data) {
          this.totalSeals = response.data.totalSeals || 0
          this.activeSeals = response.data.activeSeals || 0
          this.expiredSeals = response.data.expiredSeals || 0
          this.usageCount = response.data.usageCount || 0
        }
      }).catch(() => {
        this.totalSeals = 0
        this.activeSeals = 0
        this.expiredSeals = 0
        this.usageCount = 0
      })
    },
    getList() {
      this.listLoading = true
      getSealArchiveList(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data || []
          this.total = response.result ? response.result.total : (response.total || 0)
        } else {
          this.$message.error(response.msg || '获取数据失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取印鉴档案列表失败:', error)
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
        deleteSealArchive(row.sealId || row.id).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.getStatistics()
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
