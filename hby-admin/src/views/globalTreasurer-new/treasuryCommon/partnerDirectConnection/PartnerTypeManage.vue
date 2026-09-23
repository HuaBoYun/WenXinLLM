<template>
  <div class="partner-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user-solid"></i>
            合作伙伴类型管理
          </h2>
          <p class="page-description">管理合作伙伴类型分类，包括类型定义、权限配置、业务范围和风险等级</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增类型
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 类型统计卡片 -->
    <div class="type-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-user-solid"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总类型数</div>
                <div class="card-value">{{ totalPartnerTypes }}</div>
                <div class="card-change">已配置类型</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon bank-icon">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="card-info">
                <div class="card-title">金融机构</div>
                <div class="card-value">{{ financialTypes }}</div>
                <div class="card-change positive">银行类型</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon business-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">商业伙伴</div>
                <div class="card-value">{{ businessTypes }}</div>
                <div class="card-change">业务类型</div>
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
                <div class="card-title">启用类型</div>
                <div class="card-value">{{ activeTypes }}</div>
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
          <el-form-item label="类型编码">
            <el-input
              v-model="listQuery.typeCode"
              placeholder="请输入类型编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="类型名称">
            <el-input
              v-model="listQuery.typeName"
              placeholder="请输入类型名称"
              style="width: 200px;"
              clearable
            />
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
      <el-table-column label="类型编码" prop="typeCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.typeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.typeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" min-width="300px">
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevelLimit)" size="small">
            {{ row.riskLevelLimit || '-' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime | parseTime('{y}-{m}-{d}') }}</span>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类型编码" prop="typeCode">
              <el-input v-model="temp.typeCode" placeholder="请输入类型编码" :disabled="dialogStatus === 'update'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型名称" prop="typeName">
              <el-input v-model="temp.typeName" placeholder="请输入类型名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入类型描述" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevelLimit">
              <el-select v-model="temp.riskLevelLimit" placeholder="请选择风险等级" style="width: 100%">
                <el-option label="AAA级" value="AAA" />
                <el-option label="AA级" value="AA" />
                <el-option label="A级" value="A" />
                <el-option label="BBB级" value="BBB" />
                <el-option label="BB级" value="BB" />
                <el-option label="B级" value="B" />
                <el-option label="C级" value="C" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sortOrder">
              <el-input-number v-model="temp.sortOrder" :min="0" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-radio-group v-model="temp.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否需要审批">
              <el-switch v-model="temp.requireApproval" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="业务范围">
          <el-checkbox-group v-model="temp.businessScope">
            <el-checkbox label="PAYMENT">支付结算</el-checkbox>
            <el-checkbox label="LOAN">授信贷款</el-checkbox>
            <el-checkbox label="INVESTMENT">投资理财</el-checkbox>
            <el-checkbox label="INSURANCE">保险业务</el-checkbox>
            <el-checkbox label="GUARANTEE">担保业务</el-checkbox>
            <el-checkbox label="OTHER">其他业务</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remarks" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 排序管理对话框 -->
    <el-dialog title="排序管理" :visible.sync="sortDialogVisible" width="600px">
      <el-table :data="sortedList" border style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="typeCode" label="类型编码" align="center" />
        <el-table-column prop="typeName" label="类型名称" align="center" />
        <el-table-column label="排序" align="center" width="120">
          <template slot-scope="{row, $index}">
            <el-input-number v-model="row.sortOrder" :min="0" size="small" @change="updateSortOrder(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="{row, $index}">
            <el-button type="text" @click="moveUp($index)" :disabled="$index === 0">上移</el-button>
            <el-button type="text" @click="moveDown($index)" :disabled="$index === sortedList.length - 1">下移</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sortDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSortOrder">保存排序</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="合作伙伴类型详情" :visible.sync="detailDialogVisible" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="类型编码">{{ currentDetail.typeCode }}</el-descriptions-item>
        <el-descriptions-item label="类型名称">{{ currentDetail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentDetail.description }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelColor(currentDetail.riskLevelLimit)">{{ currentDetail.riskLevelLimit }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="排序">{{ currentDetail.sortOrder }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentDetail.isEnabled === 1 ? 'success' : 'danger'">
            {{ currentDetail.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否需要审批">
          {{ currentDetail.requireApproval ? '是' : '否' }}
        </el-descriptions-item>
        <el-descriptions-item label="业务范围" :span="2">
          <el-tag v-for="scope in currentDetail.businessScope" :key="scope" style="margin-right: 8px">
            {{ getBusinessScopeName(scope) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentDetail.createTime | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ currentDetail.updateTime | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getPartnerTypeList,
  getPartnerTypeDetail,
  addPartnerType,
  updatePartnerType,
  deletePartnerType,
  updateTypeStatus,
  updateSortOrder,
  getStatistics,
  exportTypes,
  batchDeletePartnerType,
  togglePartnerType
} from '@/api/globalTreasurer/partnerDirectConnectionPartnerTypeManage'

export default {
  name: 'PartnerTypeManage',
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
        typeCode: undefined,
        typeName: undefined,
        isEnabled: undefined
      },

      // 统计数据
      totalPartnerTypes: 0,
      financialTypes: 0,
      businessTypes: 0,
      activeTypes: 0,

      // 弹窗相关
      dialogVisible: false,
      dialogTitle: '',
      dialogStatus: '',
      temp: {
        partnerTypeId: undefined,
        typeCode: undefined,
        typeName: undefined,
        description: undefined,
        riskLevelLimit: undefined,
        sortOrder: 0,
        isEnabled: 1,
        requireApproval: false,
        businessScope: [],
        remarks: undefined
      },

      // 排序弹窗
      sortDialogVisible: false,
      sortedList: [],

      // 详情弹窗
      detailDialogVisible: false,
      currentDetail: {},

      // 表单验证规则
      rules: {
        typeCode: [
          { required: true, message: '请输入类型编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: '请输入类型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        description: [
          { max: 500, message: '描述不能超过 500 个字符', trigger: 'blur' }
        ],
        riskLevelLimit: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        sortOrder: [{ required: true, type: 'number', message: '请输入排序号', trigger: 'blur' }],
        isEnabled: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    // 获取列表数据
    async getList() {
      this.listLoading = true
      try {
        const query = {
          pageNum: this.listQuery.page,
          pageSize: this.listQuery.limit,
          typeCode: this.listQuery.typeCode,
          typeName: this.listQuery.typeName,
          isEnabled: this.listQuery.isEnabled
        }
        console.log('查询参数:', query)
        const response = await getPartnerTypeList(query)
        console.log('查询响应:', response)
        if (response.code === 1) {
          this.list = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取列表失败')
        }
      } catch (error) {
        console.error('获取列表失败:', error)
        this.$message.error('获取列表失败')
      } finally {
        this.listLoading = false
      }
    },

    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getStatistics()
        if (response.code === 1) {
          const stats = response.data
          this.totalPartnerTypes = stats.totalTypes || 0
          this.financialTypes = stats.financialTypes || 0
          this.businessTypes = stats.businessTypes || 0
          this.activeTypes = stats.activeTypes || 0
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 搜索
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    // 重置
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        typeCode: undefined,
        typeName: undefined,
        isEnabled: undefined
      }
      this.getList()
    },

    // 新增
    handleCreate() {
      this.dialogTitle = '新增合作伙伴类型'
      this.dialogStatus = 'create'
      this.temp = {
        partnerTypeId: undefined,
        typeCode: undefined,
        typeName: undefined,
        description: undefined,
        riskLevelLimit: undefined,
        sortOrder: 0,
        isEnabled: 1,
        requireApproval: false,
        businessScope: [],
        remarks: undefined
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 编辑
    handleUpdate(row) {
      this.dialogTitle = '编辑合作伙伴类型'
      this.dialogStatus = 'update'
      this.temp = Object.assign({}, row)
      this.temp.businessScope = row.businessScope || []
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 创建数据
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await addPartnerType(this.temp)
            if (response.code === 1) {
              this.dialogVisible = false
              this.$message.success('创建成功')
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.message || '创建失败')
            }
          } catch (error) {
            console.error('创建失败:', error)
            this.$message.error('创建失败')
          }
        }
      })
    },

    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await updatePartnerType(this.temp)
            if (response.code === 1) {
              this.dialogVisible = false
              this.$message.success('更新成功')
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败')
          }
        }
      })
    },

    // 删除
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deletePartnerType({ partnerTypeId: row.partnerTypeId })
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 切换状态
    handleToggleStatus(row) {
      const newStatus = row.isEnabled === 1 ? 0 : 1
      const statusText = newStatus === 1 ? '启用' : '禁用'

      this.$confirm(`确定要${statusText}该类型吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await togglePartnerType({
            partnerTypeId: row.partnerTypeId,
            isEnabled: newStatus
          })
          if (response.code === 1) {
            this.$message.success(`${statusText}成功`)
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.message || `${statusText}失败`)
          }
        } catch (error) {
          console.error(`${statusText}失败:`, error)
          this.$message.error(`${statusText}失败`)
        }
      })
    },

    // 排序管理
    handleSort() {
      this.sortedList = [...this.list].sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
      this.sortDialogVisible = true
    },

    // 上移
    moveUp(index) {
      if (index > 0) {
        const temp = this.sortedList[index]
        this.sortedList.splice(index, 1)
        this.sortedList.splice(index - 1, 0, temp)
        this.updateAllSortOrders()
      }
    },

    // 下移
    moveDown(index) {
      if (index < this.sortedList.length - 1) {
        const temp = this.sortedList[index]
        this.sortedList.splice(index, 1)
        this.sortedList.splice(index + 1, 0, temp)
        this.updateAllSortOrders()
      }
    },

    // 更新排序号
    updateSortOrder(row) {
      // 实时更新排序
    },

    // 更新所有排序号
    updateAllSortOrders() {
      this.sortedList.forEach((item, index) => {
        item.sortOrder = index * 10
      })
    },

    // 保存排序
    async saveSortOrder() {
      try {
        const sortData = {
          sortList: this.sortedList.map(item => ({
            partnerTypeId: item.partnerTypeId,
            sortOrder: item.sortOrder
          }))
        }
        console.log('发送排序数据:', sortData)
        console.log('sortedList:', this.sortedList)
        const response = await updateSortOrder(sortData)
        console.log('排序响应:', response)
        if (response.code === 1) {
          this.sortDialogVisible = false
          this.$message.success('排序保存成功')
          this.getList()
        } else {
          this.$message.error(response.message || '排序保存失败')
        }
      } catch (error) {
        console.error('排序保存失败:', error)
        this.$message.error('排序保存失败')
      }
    },

    // 导出配置
    async handleExport() {
      try {
        const response = await exportTypes(this.listQuery)
        if (response.code === 1) {
          this.$message.success('导出成功')
          // 这里可以处理文件下载
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 查看详情
    async handleView(row) {
      try {
        const response = await getPartnerTypeDetail({ partnerTypeId: row.partnerTypeId })
        if (response.code === 1) {
          this.currentDetail = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.message || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },

    // 辅助方法
    getRiskLevelColor(level) {
      const colorMap = {
        'AAA': 'success',
        'AA': 'success',
        'A': 'primary',
        'BBB': 'warning',
        'BB': 'warning',
        'B': 'danger',
        'C': 'danger'
      }
      return colorMap[level] || ''
    },

    getBusinessScopeName(scope) {
      const nameMap = {
        'PAYMENT': '支付结算',
        'LOAN': '授信贷款',
        'INVESTMENT': '投资理财',
        'INSURANCE': '保险业务',
        'GUARANTEE': '担保业务',
        'OTHER': '其他业务'
      }
      return nameMap[scope] || scope
    }
  }
}
</script>
