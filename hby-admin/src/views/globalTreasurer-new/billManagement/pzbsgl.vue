<template>
  <div class="bill-identification-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-collection-tag"></i>
            票据标识管理
          </h2>
          <p class="page-description">管理票据的分类标识、标签体系，实现对票据的多维度分类和管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增标识
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchEnable">
            批量启用
          </el-button>
          <el-button type="warning" icon="el-icon-close" @click="handleBatchDisable">
            批量禁用
          </el-button>
        </div>
      </div>
    </div>

    <!-- 标识概览卡片 -->
    <div class="identification-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-collection-tag"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总标识数</div>
                <div class="card-value">{{ totalIdentifications }}</div>
                <div class="card-change">个标识</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon category-icon">
                <i class="el-icon-folder"></i>
              </div>
              <div class="card-info">
                <div class="card-title">分类标识</div>
                <div class="card-value">{{ categoryCount }}</div>
                <div class="card-change">个分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon tag-icon">
                <i class="el-icon-price-tag"></i>
              </div>
              <div class="card-info">
                <div class="card-title">标签数量</div>
                <div class="card-value">{{ tagCount }}</div>
                <div class="card-change">个标签</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon enabled-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已启用</div>
                <div class="card-value">{{ enabledCount }}</div>
                <div class="card-change positive">正常使用</div>
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
          <el-form-item label="标识编码">
            <el-input
              v-model="listQuery.identificationCode"
              placeholder="请输入标识编码"
              style="width: 150px;"
              clearable
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="标识名称">
            <el-input
              v-model="listQuery.identificationName"
              placeholder="请输入标识名称"
              style="width: 150px;"
              clearable
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="标识类型">
            <el-select
              v-model="listQuery.identificationType"
              placeholder="请选择类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="分类标识" value="CATEGORY" />
              <el-option label="标签" value="TAG" />
              <el-option label="优先级" value="PRIORITY" />
              <el-option label="风险等级" value="RISK" />
            </el-select>
          </el-form-item>
          <el-form-item label="启用状态">
            <el-select
              v-model="listQuery.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="已启用" :value="1" />
              <el-option label="已禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 标识表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="identificationList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="标识编码" prop="identificationCode" width="150" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.identificationCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标识名称" prop="identificationName" width="150" align="center">
          <template slot-scope="{row}">
            <span>
              <i v-if="row.identificationIcon" :class="row.identificationIcon" :style="{color: row.identificationColor || '#409EFF'}"></i>
              {{ row.identificationName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="标识类型" width="120" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getTypeTagType(row.identificationType)" size="mini">
              {{ row.identificationTypeName || getTypeText(row.identificationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标识颜色" width="100" align="center">
          <template slot-scope="{row}">
            <span v-if="row.identificationColor" class="color-preview" :style="{backgroundColor: row.identificationColor}"></span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="排序" prop="sortOrder" width="80" align="center" />
        <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip />
        <el-table-column label="系统内置" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.isSystem === 1 ? 'warning' : 'info'" size="mini">
              {{ row.isSystem === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="启用状态" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              :disabled="row.isSystem === 1"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="160" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleEdit(row)" :disabled="row.isSystem === 1">
              编辑
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-button type="danger" size="mini" @click="handleDelete(row)" :disabled="row.isSystem === 1">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑标识对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="100px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="标识编码" prop="identificationCode">
          <el-input v-model="temp.identificationCode" placeholder="留空则自动生成" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="标识名称" prop="identificationName">
          <el-input v-model="temp.identificationName" placeholder="请输入标识名称" />
        </el-form-item>
        <el-form-item label="标识类型" prop="identificationType">
          <el-select v-model="temp.identificationType" placeholder="请选择标识类型" style="width: 100%;">
            <el-option label="分类标识" value="CATEGORY" />
            <el-option label="标签" value="TAG" />
            <el-option label="优先级" value="PRIORITY" />
            <el-option label="风险等级" value="RISK" />
          </el-select>
        </el-form-item>
        <el-form-item label="标识颜色">
          <el-color-picker v-model="temp.identificationColor" show-alpha />
          <span style="margin-left: 10px;">{{ temp.identificationColor || '未选择' }}</span>
        </el-form-item>
        <el-form-item label="标识图标">
          <el-select v-model="temp.identificationIcon" placeholder="请选择图标" clearable style="width: 100%;">
            <el-option v-for="icon in iconOptions" :key="icon.value" :label="icon.label" :value="icon.value">
              <i :class="icon.value" style="margin-right: 8px;"></i>
              {{ icon.label }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父级标识">
          <el-select v-model="temp.parentId" placeholder="请选择父级标识（可选）" clearable style="width: 100%;">
            <el-option v-for="item in parentOptions" :key="item.identificationId" :label="item.identificationName" :value="item.identificationId" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序序号">
          <el-input-number v-model="temp.sortOrder" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入标识描述" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 标识详情对话框 -->
    <el-dialog title="标识详情" :visible.sync="dialogDetailVisible" width="600px">
      <div v-if="currentIdentification" class="identification-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="标识编码">{{ currentIdentification.identificationCode }}</el-descriptions-item>
          <el-descriptions-item label="标识名称">
            <i v-if="currentIdentification.identificationIcon" :class="currentIdentification.identificationIcon" :style="{color: currentIdentification.identificationColor}"></i>
            {{ currentIdentification.identificationName }}
          </el-descriptions-item>
          <el-descriptions-item label="标识类型">
            <el-tag :type="getTypeTagType(currentIdentification.identificationType)">
              {{ currentIdentification.identificationTypeName || getTypeText(currentIdentification.identificationType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标识颜色">
            <span v-if="currentIdentification.identificationColor" class="color-preview" :style="{backgroundColor: currentIdentification.identificationColor}"></span>
            {{ currentIdentification.identificationColor || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item label="排序序号">{{ currentIdentification.sortOrder || 0 }}</el-descriptions-item>
          <el-descriptions-item label="系统内置">
            <el-tag :type="currentIdentification.isSystem === 1 ? 'warning' : 'info'">
              {{ currentIdentification.isSystem === 1 ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="启用状态">
            <el-tag :type="currentIdentification.isEnabled === 1 ? 'success' : 'danger'">
              {{ currentIdentification.isEnabled === 1 ? '已启用' : '已禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentIdentification.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ currentIdentification.description || '无' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentIdentification.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentIdentification && currentIdentification.isSystem !== 1" type="primary" @click="handleEdit(currentIdentification)">
          编辑
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBillIdentificationPage, createBillIdentification, updateBillIdentification, deleteBillIdentification, batchEnableBillIdentification, batchDisableBillIdentification } from '@/api/globalTreasurer-new/billManagement/billIdentification'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillIdentificationManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        identificationCode: undefined,
        identificationName: undefined,
        identificationType: undefined,
        isEnabled: undefined
      },
      totalIdentifications: 0,
      categoryCount: 0,
      tagCount: 0,
      enabledCount: 0,
      identificationList: [],
      multipleSelection: [],
      currentIdentification: null,
      parentOptions: [],
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      temp: {
        identificationId: undefined,
        identificationCode: '',
        identificationName: '',
        identificationType: '',
        identificationColor: '',
        identificationIcon: '',
        parentId: null,
        sortOrder: 0,
        description: '',
        isEnabled: 1,
        remark: ''
      },
      iconOptions: [
        { label: '标签', value: 'el-icon-price-tag' },
        { label: '文件夹', value: 'el-icon-folder' },
        { label: '文档', value: 'el-icon-document' },
        { label: '星标', value: 'el-icon-star-on' },
        { label: '警告', value: 'el-icon-warning' },
        { label: '成功', value: 'el-icon-success' },
        { label: '信息', value: 'el-icon-info' },
        { label: '错误', value: 'el-icon-error' },
        { label: '时钟', value: 'el-icon-time' },
        { label: '位置', value: 'el-icon-location' }
      ],
      rules: {
        identificationName: [{ required: true, message: '标识名称不能为空', trigger: 'blur' }],
        identificationType: [{ required: true, message: '请选择标识类型', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadParentOptions()
  },
  methods: {
    getList() {
      this.listLoading = true
      getBillIdentificationPage(this.listQuery).then(response => {
        if (response.code === 1) {
          this.identificationList = response.data || []
          this.total = response.result ? response.result.total : this.identificationList.length
          this.calculateStatistics()
        } else {
          this.$message.error(response.msg || '获取数据失败')
          this.identificationList = []
          this.total = 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取票据标识列表失败:', error)
        this.identificationList = []
        this.total = 0
        this.listLoading = false
      })
    },
    loadParentOptions() {
      getBillIdentificationPage({ pageNum: 1, pageSize: 1000, isEnabled: 1 }).then(response => {
        if (response.code === 1) {
          this.parentOptions = response.data || []
        }
      }).catch(() => {})
    },
    calculateStatistics() {
      this.totalIdentifications = this.total
      this.categoryCount = this.identificationList.filter(item => item.identificationType === 'CATEGORY').length
      this.tagCount = this.identificationList.filter(item => item.identificationType === 'TAG').length
      this.enabledCount = this.identificationList.filter(item => item.isEnabled === 1).length
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        identificationCode: undefined,
        identificationName: undefined,
        identificationType: undefined,
        isEnabled: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增标识'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑标识'
      this.dialogFormVisible = true
      this.dialogDetailVisible = false
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentIdentification = row
      this.dialogDetailVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该标识?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBillIdentification([row.identificationId]).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    },
    handleStatusChange(row) {
      const action = row.isEnabled === 1 ? batchEnableBillIdentification : batchDisableBillIdentification
      action([row.identificationId]).then(response => {
        if (response.code === 1) {
          this.$message.success(row.isEnabled === 1 ? '启用成功' : '禁用成功')
        } else {
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
          this.$message.error(response.msg || '操作失败')
        }
      }).catch(() => {
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
        this.$message.error('操作失败')
      })
    },
    handleBatchEnable() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要启用的标识')
        return
      }
      const ids = this.multipleSelection.filter(item => item.isSystem !== 1).map(item => item.identificationId)
      if (ids.length === 0) {
        this.$message.warning('系统内置标识不能操作')
        return
      }
      batchEnableBillIdentification(ids).then(response => {
        if (response.code === 1) {
          this.$message.success('批量启用成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      }).catch(() => {
        this.$message.error('操作失败')
      })
    },
    handleBatchDisable() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要禁用的标识')
        return
      }
      const ids = this.multipleSelection.filter(item => item.isSystem !== 1).map(item => item.identificationId)
      if (ids.length === 0) {
        this.$message.warning('系统内置标识不能操作')
        return
      }
      batchDisableBillIdentification(ids).then(response => {
        if (response.code === 1) {
          this.$message.success('批量禁用成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      }).catch(() => {
        this.$message.error('操作失败')
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createBillIdentification(this.temp).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('新增成功')
              this.getList()
              this.loadParentOptions()
            } else {
              this.$message.error(response.msg || '新增失败')
            }
          }).catch(() => {
            this.$message.error('新增失败')
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateBillIdentification(this.temp).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('修改成功')
              this.getList()
              this.loadParentOptions()
            } else {
              this.$message.error(response.msg || '修改失败')
            }
          }).catch(() => {
            this.$message.error('修改失败')
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        identificationId: undefined,
        identificationCode: '',
        identificationName: '',
        identificationType: '',
        identificationColor: '',
        identificationIcon: '',
        parentId: null,
        sortOrder: 0,
        description: '',
        isEnabled: 1,
        remark: ''
      }
    },
    getTypeTagType(type) {
      const typeMap = {
        'CATEGORY': 'primary',
        'TAG': 'success',
        'PRIORITY': 'warning',
        'RISK': 'danger'
      }
      return typeMap[type] || 'info'
    },
    getTypeText(type) {
      const textMap = {
        'CATEGORY': '分类标识',
        'TAG': '标签',
        'PRIORITY': '优先级',
        'RISK': '风险等级'
      }
      return textMap[type] || type
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-identification-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .identification-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.category-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.tag-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.enabled-icon {
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .color-preview {
    display: inline-block;
    width: 20px;
    height: 20px;
    border-radius: 4px;
    vertical-align: middle;
    border: 1px solid #dcdfe6;
  }

  .identification-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
  }
}
</style>
