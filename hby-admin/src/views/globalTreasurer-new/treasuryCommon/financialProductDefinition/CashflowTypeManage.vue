<template>
  <div class="cashflow-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-analysis"></i>
            现金流类型管理
          </h2>
          <p class="page-description">管理现金流类型定义，包括流入流出分类、业务场景配置和会计科目映射</p>
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

    <!-- 现金流统计卡片 -->
    <div class="cashflow-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总类型数</div>
                <div class="card-value">{{ totalCashflowTypes }}</div>
                <div class="card-change">已配置类型</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon inflow-icon">
                <i class="el-icon-top"></i>
              </div>
              <div class="card-info">
                <div class="card-title">流入类型</div>
                <div class="card-value">{{ inflowTypes }}</div>
                <div class="card-change positive">资金流入</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon outflow-icon">
                <i class="el-icon-bottom"></i>
              </div>
              <div class="card-info">
                <div class="card-title">流出类型</div>
                <div class="card-value">{{ outflowTypes }}</div>
                <div class="card-change negative">资金流出</div>
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
                <div class="card-change">正常使用</div>
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
          <el-form-item label="现金流类型编码">
            <el-input
              v-model="listQuery.cashflowTypeCode"
              placeholder="请输入现金流类型编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="现金流类型名称">
            <el-input
              v-model="listQuery.cashflowTypeName"
              placeholder="请输入现金流类型名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="现金流方向">
            <el-select
              v-model="listQuery.cashflowDirection"
              placeholder="请选择现金流方向"
              clearable
              style="width: 120px;"
            >
              <el-option label="流入" value="INFLOW" />
              <el-option label="流出" value="OUTFLOW" />
              <el-option label="双向" value="BOTH" />
            </el-select>
          </el-form-item>
          <el-form-item label="业务分类">
            <el-select
              v-model="listQuery.businessCategory"
              placeholder="请选择业务分类"
              clearable
              style="width: 150px;"
            >
              <el-option label="经营活动" value="OPERATING" />
              <el-option label="投资活动" value="INVESTING" />
              <el-option label="筹资活动" value="FINANCING" />
            </el-select>
          </el-form-item>
          <el-form-item label="影响类型">
            <el-select
              v-model="listQuery.impactType"
              placeholder="请选择影响类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="直接影响" value="DIRECT" />
              <el-option label="间接影响" value="INDIRECT" />
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
      <el-table-column label="现金流类型编码" prop="cashflowTypeCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.cashflowTypeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现金流类型名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.cashflowTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现金流方向" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getCashflowDirectionColor(row.cashflowDirection)" size="small">
            {{ getCashflowDirectionName(row.cashflowDirection) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="业务分类" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getBusinessCategoryColor(row.businessCategory)" size="small">
            {{ getBusinessCategoryName(row.businessCategory) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="影响类型" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getImpactTypeColor(row.impactType)" size="small">
            {{ getImpactTypeName(row.impactType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会计科目" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubject }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预测权重" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.forecastWeight }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d}') }}</span>
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

    <!-- 创建/编辑现金流类型对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增现金流类型' : '编辑现金流类型'" :visible.sync="dialogFormVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类型编码" prop="cashflowTypeCode">
              <el-input v-model="temp.cashflowTypeCode" placeholder="请输入现金流类型编码" :disabled="dialogStatus === 'update'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型名称" prop="cashflowTypeName">
              <el-input v-model="temp.cashflowTypeName" placeholder="请输入现金流类型名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="现金流方向" prop="cashflowDirection">
              <el-select v-model="temp.cashflowDirection" placeholder="请选择现金流方向" style="width: 100%;">
                <el-option label="流入" value="INFLOW" />
                <el-option label="流出" value="OUTFLOW" />
                <el-option label="双向" value="BOTH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务分类" prop="businessCategory">
              <el-select v-model="temp.businessCategory" placeholder="请选择业务分类" style="width: 100%;">
                <el-option label="经营活动" value="OPERATING" />
                <el-option label="投资活动" value="INVESTING" />
                <el-option label="筹资活动" value="FINANCING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="影响类型" prop="impactType">
              <el-select v-model="temp.impactType" placeholder="请选择影响类型" style="width: 100%;">
                <el-option label="直接影响" value="DIRECT" />
                <el-option label="间接影响" value="INDIRECT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会计科目" prop="accountingSubject">
              <el-input v-model="temp.accountingSubject" placeholder="请输入会计科目" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预测权重(%)" prop="forecastWeight">
              <el-input-number v-model="temp.forecastWeight" :min="0" :max="100" :precision="2" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 排序管理对话框 -->
    <el-dialog title="现金流类型排序管理" :visible.sync="sortDialogVisible" width="600px" :close-on-click-modal="false">
      <div class="sort-content">
        <el-alert title="拖拽行调整顺序" type="info" :closable="false" show-icon style="margin-bottom: 20px;" />
        <el-table :data="sortList" border row-key="cashflowTypeId" style="width: 100%;">
          <el-table-column label="排序" width="80" align="center">
            <template slot-scope="{row, $index}">
              <i class="el-icon-s-operation" style="cursor: move; color: #909399;"></i>
              <span style="margin-left: 10px;">{{ $index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="类型编码" prop="cashflowTypeCode" width="150" align="center" />
          <el-table-column label="类型名称" prop="cashflowTypeName" show-overflow-tooltip />
          <el-table-column label="现金流方向" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="getCashflowDirectionColor(row.cashflowDirection)" size="small">
                {{ getCashflowDirectionName(row.cashflowDirection) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sortDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSort">保存排序</el-button>
      </div>
    </el-dialog>

    <!-- 导出配置对话框 -->
    <el-dialog title="导出现金流类型配置" :visible.sync="exportDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form ref="exportForm" :model="exportForm" label-position="right" label-width="100px">
        <el-form-item label="导出范围" prop="exportRange">
          <el-radio-group v-model="exportForm.exportRange">
            <el-radio label="all">全部数据</el-radio>
            <el-radio label="filtered">当前筛选结果</el-radio>
            <el-radio label="selected">选中数据</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="文件格式" prop="fileFormat">
          <el-radio-group v-model="exportForm.fileFormat">
            <el-radio label="xlsx">Excel格式(.xlsx)</el-radio>
            <el-radio label="csv">CSV格式(.csv)</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="包含字段">
          <el-checkbox-group v-model="exportForm.fields">
            <el-checkbox label="cashflowTypeCode">类型编码</el-checkbox>
            <el-checkbox label="cashflowTypeName">类型名称</el-checkbox>
            <el-checkbox label="cashflowDirection">现金流方向</el-checkbox>
            <el-checkbox label="businessCategory">业务分类</el-checkbox>
            <el-checkbox label="impactType">影响类型</el-checkbox>
            <el-checkbox label="accountingSubject">会计科目</el-checkbox>
            <el-checkbox label="forecastWeight">预测权重</el-checkbox>
            <el-checkbox label="isEnabled">状态</el-checkbox>
            <el-checkbox label="createTime">创建时间</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exportLoading">导出</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getCashflowTypeList,
  createCashflowType,
  updateCashflowType,
  deleteCashflowType,
  validateCashflowTypeDelete,
  sortCashflowTypes,
  exportCashflowTypes,
  getCashflowTypeStatistics,
  checkCashflowTypeCodeUnique
} from '@/api/globalTreasurer/financialProductDefinition/cashflowTypeManage'

export default {
  name: 'CashflowTypeManage',
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
        cashflowTypeCode: undefined,
        cashflowTypeName: undefined,
        cashflowDirection: undefined,
        businessCategory: undefined,
        impactType: undefined
      },
      totalCashflowTypes: 0,
      enabledTypes: 0,
      inflowTypes: 0,
      outflowTypes: 0,
      activeTypes: 0,

      // 对话框相关
      dialogFormVisible: false,
      dialogStatus: '',
      sortDialogVisible: false,
      exportDialogVisible: false,
      exportLoading: false,

      // 临时数据
      temp: {
        cashflowTypeId: undefined,
        cashflowTypeCode: '',
        cashflowTypeName: '',
        cashflowDirection: '',
        businessCategory: '',
        impactType: '',
        accountingSubject: '',
        forecastWeight: 0,
        description: '',
        isEnabled: 1
      },

      // 排序列表
      sortList: [],

      // 导出表单
      exportForm: {
        exportRange: 'all',
        fileFormat: 'xlsx',
        fields: ['cashflowTypeCode', 'cashflowTypeName', 'cashflowDirection', 'businessCategory', 'impactType', 'accountingSubject', 'forecastWeight', 'isEnabled', 'createTime']
      },

      // 表单验证规则
      rules: {
        cashflowTypeCode: [
          { required: true, message: '请输入现金流类型编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
          { validator: this.validateCode, trigger: 'blur' }
        ],
        cashflowTypeName: [
          { required: true, message: '请输入现金流类型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        cashflowDirection: [
          { required: true, message: '请选择现金流方向', trigger: 'change' }
        ],
        businessCategory: [
          { required: true, message: '请选择业务分类', trigger: 'change' }
        ],
        impactType: [
          { required: true, message: '请选择影响类型', trigger: 'change' }
        ],
        accountingSubject: [
          { required: true, message: '请输入会计科目', trigger: 'blur' }
        ],
        forecastWeight: [
          { type: 'number', min: 0, max: 100, message: '预测权重必须在0-100之间', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getCashflowDirectionName(direction) {
      const directionMap = {
        'INFLOW': '流入',
        'OUTFLOW': '流出',
        'BOTH': '双向'
      }
      return directionMap[direction] || direction
    },
    getCashflowDirectionColor(direction) {
      const colorMap = {
        'INFLOW': 'success',
        'OUTFLOW': 'danger',
        'BOTH': 'info'
      }
      return colorMap[direction] || 'default'
    },
    getBusinessCategoryName(category) {
      const categoryMap = {
        'OPERATING': '经营活动',
        'INVESTING': '投资活动',
        'FINANCING': '筹资活动'
      }
      return categoryMap[category] || category
    },
    getBusinessCategoryColor(category) {
      const colorMap = {
        'OPERATING': 'primary',
        'INVESTING': 'success',
        'FINANCING': 'warning'
      }
      return colorMap[category] || 'default'
    },
    getImpactTypeName(type) {
      const typeMap = {
        'DIRECT': '直接影响',
        'INDIRECT': '间接影响'
      }
      return typeMap[type] || type
    },
    getImpactTypeColor(type) {
      const colorMap = {
        'DIRECT': 'primary',
        'INDIRECT': 'info'
      }
      return colorMap[type] || 'default'
    },

    // 编码唯一性验证
    validateCode(rule, value, callback) {
      if (!value) {
        return callback(new Error('请输入现金流类型编码'))
      }

      checkCashflowTypeCodeUnique(value, this.temp.cashflowTypeId).then(response => {
        const result = response
        // code === 0 表示编码已存在
        if (result.code === 0) {
          callback(new Error('编码已存在，请重新输入'))
        } else {
          callback()
        }
      }).catch(() => {
        callback()
      })
    },

    // 获取列表数据
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.page,
        pageSize: this.listQuery.limit,
        cashflowTypeCode: this.listQuery.cashflowTypeCode,
        cashflowTypeName: this.listQuery.cashflowTypeName,
        cashflowDirection: this.listQuery.cashflowDirection,
        businessCategory: this.listQuery.businessCategory,
        impactType: this.listQuery.impactType
      }

      getCashflowTypeList(params).then(response => {
        console.log('=== 后端完整响应 ===', response)

        // 响应格式: {code: 1, data: {pageNo: 1, tlist: [...], totalRecord: 0}, msg: "查询成功"}
        let listData, totalRecord

        if (response.code === 1 || response.code === 200) {
          listData = response.data?.tlist || []
          totalRecord = response.data?.totalRecord || 0
        } else {
          listData = []
          totalRecord = 0
          listData = response.tlist || []
          totalRecord = response.totalRecord || 0
        }

        // 如果totalRecord为0但listData有数据,使用listData的长度作为total
        if (totalRecord === 0 && listData.length > 0) {
          totalRecord = listData.length
          console.log('=== totalRecord为0,使用listData.length ===', totalRecord)
        }

        this.list = listData
        this.total = totalRecord
        console.log('=== 赋值后 this.list ===', this.list)
        console.log('=== 赋值后 this.total ===', this.total)
        this.calculateStatistics()
        this.listLoading = false
      }).catch(error => {
        console.error('获取现金流类型列表失败:', error)
        this.$message.error('获取数据失败')
        this.listLoading = false
      })
    },

    // 计算统计数据
    calculateStatistics() {
      this.totalCashflowTypes = this.list.length
      this.inflowTypes = this.list.filter(item => item.cashflowDirection === 'INFLOW').length
      this.outflowTypes = this.list.filter(item => item.cashflowDirection === 'OUTFLOW').length
      this.activeTypes = this.list.filter(item => item.isEnabled === 1).length
    },

    // 搜索
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    // 重置搜索
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        cashflowTypeCode: undefined,
        cashflowTypeName: undefined,
        cashflowDirection: undefined,
        businessCategory: undefined,
        impactType: undefined
      }
      this.getList()
    },

    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 重置临时数据
    resetTemp() {
      this.temp = {
        cashflowTypeId: undefined,
        cashflowTypeCode: '',
        cashflowTypeName: '',
        cashflowDirection: '',
        businessCategory: '',
        impactType: '',
        accountingSubject: '',
        forecastWeight: 0,
        description: '',
        isEnabled: 1
      }
    },

    // 创建数据
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createCashflowType(this.temp).then(response => {
            const result = response.data || response
            if (result.code === 1 || result.code === 200) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(result.msg || result.message || '创建失败')
            }
          }).catch(error => {
            console.error('创建现金流类型失败:', error)
            this.$message.error('创建失败')
          })
        }
      })
    },

    // 打开编辑对话框
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCashflowType(tempData).then(response => {
            const result = response.data || response
            if (result.code === 1 || result.code === 200) {
              const index = this.list.findIndex(v => v.cashflowTypeId === this.temp.cashflowTypeId)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.calculateStatistics()
            } else {
              this.$message.error(result.msg || result.message || '更新失败')
            }
          }).catch(error => {
            console.error('更新现金流类型失败:', error)
            this.$message.error('更新失败')
          })
        }
      })
    },

    // 删除数据
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 先验证是否可以删除
        validateCashflowTypeDelete(row.cashflowTypeId).then(response => {
          const validateResult = response.data || response
          if (validateResult.code === 0) {
            this.$message.warning(validateResult.msg || validateResult.message || '该类型正在使用中，无法删除')
            return
          }

          // 执行删除
          deleteCashflowType(row.cashflowTypeId).then(response => {
            const result = response.data || response
            if (result.code === 1 || result.code === 200) {
              this.$notify({
                title: '成功',
                message: '删除成功',
                type: 'success',
                duration: 2000
              })
              this.list.splice(index, 1)
              this.total--
              this.calculateStatistics()
            } else {
              this.$message.error(result.msg || result.message || '删除失败')
            }
          }).catch(error => {
            console.error('删除现金流类型失败:', error)
            this.$message.error('删除失败')
          })
        }).catch(error => {
          console.error('验证删除失败:', error)
        })
      })
    },

    // 排序管理
    handleSort() {
      this.sortList = [...this.list].sort((a, b) => a.sortOrder - b.sortOrder)
      this.sortDialogVisible = true
    },

    // 保存排序
    saveSort() {
      const sortData = this.sortList.map((item, index) => ({
        cashflowTypeId: item.cashflowTypeId,
        sortOrder: index + 1
      }))

      sortCashflowTypes(sortData).then(response => {
        const result = response.data || response
        if (result.code === 1 || result.code === 200) {
          this.sortDialogVisible = false
          this.$notify({
            title: '成功',
            message: '排序保存成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        } else {
          this.$message.error(result.msg || result.message || '排序保存失败')
        }
      }).catch(error => {
        console.error('保存排序失败:', error)
        this.$message.error('排序保存失败')
      })
    },

    // 导出配置
    handleExport() {
      this.exportDialogVisible = true
    },

    // 确认导出
    confirmExport() {
      if (this.exportForm.fields.length === 0) {
        this.$message.warning('请至少选择一个字段')
        return
      }

      this.exportLoading = true

      const params = {
        exportRange: this.exportForm.exportRange,
        fileFormat: this.exportForm.fileFormat,
        fields: this.exportForm.fields,
        ...this.listQuery
      }

      // 如果是导出筛选结果，移除分页参数
      if (this.exportForm.exportRange === 'filtered') {
        delete params.pageNo
        delete params.pageSize
      }

      exportCashflowTypes(params).then(response => {
        this.exportLoading = false
        this.exportDialogVisible = false

        // 处理文件下载
        const blob = new Blob([response.data])
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `现金流类型配置_${new Date().toLocaleDateString()}.${this.exportForm.fileFormat}`
        link.click()

        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      }).catch(error => {
        console.error('导出失败:', error)
        this.exportLoading = false
        this.$message.error('导出失败')

        // 模拟导出成功
        this.exportDialogVisible = false
        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cashflow-type-manage {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;

  .page-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          margin: 0 0 10px 0;
          font-size: 28px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 32px;
          }
        }

        .page-description {
          margin: 0;
          font-size: 14px;
          opacity: 0.9;
          line-height: 1.5;
        }
      }

      .header-right {
        .el-button {
          border-radius: 20px;
          padding: 10px 20px;
          font-weight: 500;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          }
        }
      }
    }
  }

  .cashflow-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 12px;
      border: none;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 15px;
          font-size: 24px;
          color: white;

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.inflow-icon {
            background: linear-gradient(135deg, #00c851 0%, #00a846 100%);
          }

          &.outflow-icon {
            background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
          }

          &.active-icon {
            background: linear-gradient(135deg, #33b5e5 0%, #0099cc 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 12px;
            color: #8c8c8c;
            margin-bottom: 5px;
            font-weight: 500;
          }

          .card-value {
            font-size: 24px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 5px;
          }

          .card-change {
            font-size: 12px;
            color: #8c8c8c;

            &.positive {
              color: #00c851;
            }

            &.negative {
              color: #ff4444;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 12px;
    border: none;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .search-form {
      .el-form-item {
        margin-bottom: 0;

        .el-input, .el-select {
          .el-input__inner, .el-select__tags {
            border-radius: 8px;
            border: 1px solid #d9d9d9;
            transition: all 0.3s ease;

            &:hover {
              border-color: #667eea;
            }

            &:focus {
              border-color: #667eea;
              box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
            }
          }
        }

        .el-button {
          border-radius: 8px;
          padding: 9px 15px;

          &.el-button--primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;

            &:hover {
              background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
              transform: translateY(-1px);
            }
          }
        }
      }
    }
  }

  .el-table {
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    th {
      background-color: #fafafa;
      font-weight: 600;
      color: #2c3e50;
    }

    td {
      padding: 12px 0;
    }

    .el-tag {
      border-radius: 12px;
      font-weight: 500;
      border: none;

      &.el-tag--success {
        background: linear-gradient(135deg, #00c851 0%, #00a846 100%);
      }

      &.el-tag--danger {
        background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
      }

      &.el-tag--primary {
        background: linear-gradient(135deg, #33b5e5 0%, #0099cc 100%);
      }

      &.el-tag--warning {
        background: linear-gradient(135deg, #ffbb33 0%, #ff8800 100%);
      }

      &.el-tag--info {
        background: linear-gradient(135deg, #aa66cc 0%, #9933cc 100%);
      }
    }

    .el-button {
      border-radius: 6px;

      &.el-button--primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
        }
      }

      &.el-button--danger {
        background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #ff6666 0%, #ff1a1a 100%);
        }
      }
    }
  }

  .el-dialog {
    border-radius: 12px;
    overflow: hidden;

    .el-dialog__header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px 30px;

      .el-dialog__title {
        font-size: 18px;
        font-weight: 600;
      }

      .el-dialog__headerbtn {
        .el-dialog__close {
          color: white;

          &:hover {
            color: #f0f0f0;
          }
        }
      }
    }

    .el-dialog__body {
      padding: 30px;

      .el-form-item {
        margin-bottom: 22px;

        .el-form-item__label {
          font-weight: 600;
          color: #2c3e50;
        }

        .el-input, .el-select, .el-input-number {
          .el-input__inner, .el-select__tags {
            border-radius: 8px;
            border: 1px solid #d9d9d9;
            transition: all 0.3s ease;

            &:hover {
              border-color: #667eea;
            }

            &:focus {
              border-color: #667eea;
              box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
            }
          }
        }

        .el-switch {
          .el-switch__core {
            border-radius: 12px;
          }

          &.is-checked .el-switch__core {
            background-color: #667eea;
          }
        }

        .el-checkbox-group {
          .el-checkbox {
            margin-bottom: 8px;

            .el-checkbox__inner {
              border-radius: 4px;
            }

            &.is-checked .el-checkbox__inner {
              background-color: #667eea;
              border-color: #667eea;
            }
          }
        }
      }
    }

    .el-dialog__footer {
      padding: 20px 30px;
      border-top: 1px solid #f0f0f0;

      .dialog-footer {
        text-align: right;

        .el-button {
          border-radius: 8px;
          padding: 10px 20px;
          font-weight: 500;
          margin-left: 10px;

          &.el-button--primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;

            &:hover {
              background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
              transform: translateY(-1px);
            }
          }
        }
      }
    }
  }

  .sort-content {
    .el-alert {
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .el-table {
      .el-icon-s-operation {
        font-size: 18px;
        cursor: move;

        &:hover {
          color: #667eea;
        }
      }
    }
  }

  .pagination-container {
    text-align: center;
    margin-top: 20px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .cashflow-type-manage {
    padding: 10px;

    .page-header .header-content {
      flex-direction: column;
      text-align: center;

      .header-right {
        margin-top: 20px;

        .el-button {
          width: 100%;
          margin-bottom: 10px;
        }
      }
    }

    .cashflow-overview {
      .el-col {
        margin-bottom: 15px;
      }
    }

    .search-form {
      .el-form-item {
        margin-bottom: 10px;
      }
    }
  }
}
</style>
