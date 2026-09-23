<template>
  <div class="app-container">
    <!-- 生成参数表单 -->
    <el-card class="mb8">
      <div slot="header">
        <span>抵消凭证生成</span>
      </div>
      <el-form :model="generateForm" :inline="true" label-width="100px">
        <el-form-item label="合并模型">
          <el-select v-model="generateForm.modelId" placeholder="请选择合并模型" style="width: 200px">
            <el-option
              v-for="item in modelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="generateForm.period" placeholder="请输入期间(如202401)" style="width: 200px" />
        </el-form-item>
        <el-form-item label="凭证日期">
          <el-date-picker
            v-model="generateForm.voucherDate"
            type="date"
            placeholder="选择凭证日期"
            value-format="yyyy-MM-dd"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="重新生成">
          <el-switch v-model="generateForm.regenerate" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-s-operation" :loading="generating" @click="handleGenerate">
            生成凭证
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="合并模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable>
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="请输入期间" clearable />
      </el-form-item>
      <el-form-item label="凭证号">
        <el-input v-model="queryForm.voucherNo" placeholder="请输入凭证号" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已确认" value="CONFIRMED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-check" @click="handleConfirm">确认凭证</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" @click="handleDelete">删除凭证</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="凭证号" prop="voucherNo" width="150" />
      <el-table-column label="期间" prop="period" width="100" />
      <el-table-column label="凭证日期" prop="voucherDate" width="120" />
      <el-table-column label="模板名称" prop="templateName" width="150" />
      <el-table-column label="分录类型" prop="entryType" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.entryType === 'DEBIT'" type="success" size="mini">借方</el-tag>
          <el-tag v-else type="warning" size="mini">贷方</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="科目编码" prop="accountCode" width="120" />
      <el-table-column label="科目名称" prop="accountName" width="150" />
      <el-table-column label="金额" prop="amount" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.amount | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="摘要" prop="description" min-width="150" show-overflow-tooltip />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
          <el-tag v-else type="success" size="mini">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNum"
      :limit.sync="queryForm.pageSize"
      @pagination="loadData"
    />
  </div>
</template>

<script>
import { generateVouchers, getVoucherList, deleteVouchers, confirmVouchers } from '@/api/financialSharing/consolidationReport/eliminationVoucher'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'EliminationVoucher',
  components: {
    Pagination
  },
  filters: {
    numberFormat(value) {
      if (!value) return '0.00'
      return parseFloat(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  data() {
    return {
      // 生成参数
      generateForm: {
        modelId: '',
        period: '',
        voucherDate: '',
        regenerate: false
      },
      // 生成中状态
      generating: false,
      // 查询参数
      queryForm: {
        modelId: '',
        period: '',
        voucherNo: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 表格数据
      tableData: [],
      // 总记录数
      total: 0,
      // 模型选项
      modelOptions: []
    }
  },
  created() {
    this.loadModelOptions()
  },
  methods: {
    /** 加载模型选项 */
    loadModelOptions() {
      getModelList({ status: 'ACTIVE', pageNum: 1, pageSize: 1000 }).then(res => {
        if (res.code === 200 && res.data && res.data.list) {
          this.modelOptions = res.data.list.map(item => ({
            value: item.modelId,
            label: item.modelName
          }))
        }
      })
    },
    /** 生成凭证 */
    handleGenerate() {
      if (!this.generateForm.modelId) {
        this.$message.warning('请选择合并模型')
        return
      }
      if (!this.generateForm.period) {
        this.$message.warning('请输入期间')
        return
      }

      const confirmMsg = this.generateForm.regenerate
        ? '确认重新生成抵消凭证吗?将删除该模型和期间的所有旧凭证'
        : '确认生成抵消凭证吗?'

      this.$confirm(confirmMsg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.generating = true
        generateVouchers(this.generateForm).then(res => {
          this.generating = false
          if (res.code === 200) {
            this.$message.success(res.msg || '生成成功')
            // 自动查询生成的凭证
            this.queryForm.modelId = this.generateForm.modelId
            this.queryForm.period = this.generateForm.period
            this.loadData()
          } else {
            this.$message.error(res.msg || '生成失败')
          }
        }).catch(() => {
          this.generating = false
          this.$message.error('生成失败')
        })
      }).catch(() => {})
    },
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelId: '',
        period: '',
        voucherNo: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.tableData = []
      this.total = 0
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getVoucherList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1 || res.code === 200) {
          this.tableData = res.data.list || res.data.records || []
          this.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.tableData = []
          this.total = 0
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.tableData = []
        this.total = 0
      })
    },
    /** 确认凭证 */
    handleConfirm() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      if (!this.queryForm.period) {
        this.$message.warning('请先输入期间')
        return
      }

      this.$confirm('确认该模型和期间的所有抵消凭证吗?确认后不可修改', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        confirmVouchers({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period
        }).then(res => {
          if (res.code === 200) {
            this.$message.success('确认成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '确认失败')
          }
        })
      }).catch(() => {})
    },
    /** 删除凭证 */
    handleDelete() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      if (!this.queryForm.period) {
        this.$message.warning('请先输入期间')
        return
      }

      this.$confirm('确认删除该模型和期间的所有抵消凭证吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteVouchers({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period
        }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 刷新按钮 */
    handleRefresh() {
      this.loadData()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


