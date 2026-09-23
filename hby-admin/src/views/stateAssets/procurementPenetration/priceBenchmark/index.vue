<template>
  <div class="app-container procurement-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-coin"></i><span>价格对标分析</span></div>
      <div class="page-header-desc">采购价格与市场均价对比、偏离率分析</div>
    </div>
    <!-- 搜索区 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="采购品目" prop="itemName">
          <el-input v-model="queryForm.itemName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="品类" prop="category">
          <el-select v-model="queryForm.category" placeholder="请选择" clearable style="width:130px">
            <el-option label="工程类" value="ENGINEERING" />
            <el-option label="货物类" value="GOODS" />
            <el-option label="服务类" value="SERVICE" />
            <el-option label="IT类" value="IT" />
            <el-option label="办公设备" value="OFFICE" />
            <el-option label="工程材料" value="MATERIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-input v-model="queryForm.supplierName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 表格区 -->
    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="采购品目" prop="itemName" min-width="160" show-overflow-tooltip />
        <el-table-column label="品类" width="100" align="center">
          <template slot-scope="scope">{{ { ENGINEERING: '工程类', GOODS: '货物类', SERVICE: '服务类', IT: 'IT类', OFFICE: '办公设备', MATERIAL: '工程材料' }[scope.row.category] || scope.row.category }}</template>
        </el-table-column>
        <el-table-column label="采购价格(元)" prop="purchasePrice" width="120" align="right" />
        <el-table-column label="市场均价(元)" prop="marketPrice" width="120" align="right" />
        <el-table-column label="偏离率(%)" prop="deviationRate" width="100" align="center">
          <template slot-scope="scope">
            <span :style="{ color: Math.abs(scope.row.deviationRate) > 15 ? '#F56C6C' : '#303133' }">
              {{ scope.row.deviationRate > 0 ? '+' : '' }}{{ scope.row.deviationRate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="供应商" prop="supplierName" min-width="140" show-overflow-tooltip />
        <el-table-column label="采购日期" prop="purchaseDate" width="120" align="center" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogType === 'add' ? '新增价格对标' : '编辑价格对标'" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="采购品目" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入采购品目" />
        </el-form-item>
        <el-form-item label="品类" prop="category">
          <el-select v-model="form.category" style="width:100%">
            <el-option label="工程类" value="ENGINEERING" />
            <el-option label="货物类" value="GOODS" />
            <el-option label="服务类" value="SERVICE" />
            <el-option label="IT类" value="IT" />
            <el-option label="办公设备" value="OFFICE" />
            <el-option label="工程材料" value="MATERIAL" />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="采购价格(元)" prop="purchasePrice">
              <el-input-number v-model="form.purchasePrice" :min="0" :precision="2" style="width:100%" @change="calcDeviation" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="市场均价(元)" prop="marketPrice">
              <el-input-number v-model="form.marketPrice" :min="0" :precision="2" style="width:100%" @change="calcDeviation" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="偏离率(%)">
          <el-input :value="form.deviationRate" disabled />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-input v-model="form.supplierName" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item label="采购日期" prop="purchaseDate">
          <el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getPriceBenchmarkList, addPriceBenchmark, updatePriceBenchmark, deletePriceBenchmark } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'ProcurementPriceBenchmark',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, itemName: '', category: '', supplierName: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        itemName: [{ required: true, message: '请输入采购品目', trigger: 'blur' }],
        purchasePrice: [{ required: true, message: '请输入采购价格', trigger: 'blur' }],
        marketPrice: [{ required: true, message: '请输入市场均价', trigger: 'blur' }],
        supplierName: [{ required: true, message: '请输入供应商', trigger: 'blur' }],
      },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getPriceBenchmarkList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = { itemName: '', category: '', purchasePrice: null, marketPrice: null, deviationRate: '', supplierName: '', purchaseDate: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
    },
    calcDeviation() {
      if (this.form.purchasePrice != null && this.form.marketPrice && this.form.marketPrice > 0) {
        this.form.deviationRate = ((this.form.purchasePrice - this.form.marketPrice) / this.form.marketPrice * 100).toFixed(2)
      } else {
        this.form.deviationRate = ''
      }
      this.$forceUpdate()
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addPriceBenchmark : updatePriceBenchmark
          const res = await apiFn(this.form)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该条价格对标记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deletePriceBenchmark(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.procurement-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { margin-bottom: 0; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>