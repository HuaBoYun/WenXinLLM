<template>
  <el-dialog
    title="资产管理"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="asset-management">
      <!-- 租赁基本信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>租赁基本信息</span>
        </div>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="租赁编号">{{ leaseInfo.leaseNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租赁类型">{{ getLeasingTypeText(leaseInfo.leasingType) }}</el-descriptions-item>
          <el-descriptions-item label="租赁公司">{{ leaseInfo.leasingCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租赁金额">{{ formatCurrency(leaseInfo.leasingAmount) }}</el-descriptions-item>
          <el-descriptions-item label="租赁期限">{{ leaseInfo.leasingTerm || '-' }} {{ getTermUnitText(leaseInfo.termUnit) }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag :type="getStatusTagType(leaseInfo.applicationStatus)" size="small">
              {{ getStatusText(leaseInfo.applicationStatus) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 资产信息 -->
      <el-card class="asset-card" shadow="never">
        <div slot="header" class="card-header">
          <span>资产信息</span>
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="handleAddAsset">添加资产</el-button>
        </div>
        <el-table :data="assetList" border stripe size="small" v-loading="loading">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="assetCode" label="资产编号" width="120" />
          <el-table-column prop="assetName" label="资产名称" min-width="150" />
          <el-table-column prop="assetType" label="资产类型" width="100">
            <template slot-scope="scope">{{ getAssetTypeText(scope.row.assetType) }}</template>
          </el-table-column>
          <el-table-column prop="assetValue" label="资产价值" width="120" align="right">
            <template slot-scope="scope">{{ formatCurrency(scope.row.assetValue) }}</template>
          </el-table-column>
          <el-table-column prop="purchaseDate" label="购置日期" width="110" />
          <el-table-column prop="location" label="存放位置" width="120" />
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : 'warning'" size="mini">
                {{ scope.row.status === 'NORMAL' ? '正常' : '异常' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="handleViewAsset(scope.row)">查看</el-button>
              <el-button type="text" size="mini" @click="handleEditAsset(scope.row)">编辑</el-button>
              <el-button type="text" size="mini" class="danger-btn" @click="handleDeleteAsset(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="asset-summary">
          <span>资产总数: <strong>{{ assetList.length }}</strong> 项</span>
          <span>资产总值: <strong>{{ formatCurrency(totalAssetValue) }}</strong></span>
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>

    <!-- 资产编辑弹窗 -->
    <el-dialog
      :title="assetFormTitle"
      :visible.sync="assetFormVisible"
      width="600px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="assetForm" :model="assetForm" :rules="assetRules" label-width="100px">
        <el-form-item label="资产编号" prop="assetCode">
          <el-input v-model="assetForm.assetCode" placeholder="请输入资产编号" />
        </el-form-item>
        <el-form-item label="资产名称" prop="assetName">
          <el-input v-model="assetForm.assetName" placeholder="请输入资产名称" />
        </el-form-item>
        <el-form-item label="资产类型" prop="assetType">
          <el-select v-model="assetForm.assetType" placeholder="请选择资产类型" style="width: 100%">
            <el-option label="设备" value="EQUIPMENT" />
            <el-option label="车辆" value="VEHICLE" />
            <el-option label="房产" value="PROPERTY" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产价值" prop="assetValue">
          <el-input-number v-model="assetForm.assetValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="购置日期" prop="purchaseDate">
          <el-date-picker v-model="assetForm.purchaseDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="assetForm.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="资产描述" prop="description">
          <el-input v-model="assetForm.description" type="textarea" :rows="3" placeholder="请输入资产描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="assetFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAssetForm">确 定</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getFinancialLeaseDetail, getLeaseAssetList, saveLeaseAsset, deleteLeaseAsset } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'LeaseAssetDialog',
  props: {
    visible: { type: Boolean, default: false },
    leaseId: { type: [String, Number], default: null }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      leaseInfo: {},
      assetList: [],
      assetStatistics: {},
      assetFormVisible: false,
      assetFormTitle: '添加资产',
      isAssetEdit: false,
      assetForm: {
        assetId: null,
        leaseId: null,
        assetCode: '',
        assetName: '',
        assetType: '',
        assetValue: 0,
        purchaseDate: '',
        location: '',
        description: ''
      },
      assetRules: {
        assetCode: [{ required: true, message: '请输入资产编号', trigger: 'blur' }],
        assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
        assetType: [{ required: true, message: '请选择资产类型', trigger: 'change' }],
        assetValue: [{ required: true, message: '请输入资产价值', trigger: 'blur' }]
      }
    }
  },
  computed: {
    totalAssetValue() {
      return this.assetStatistics.totalValue || this.assetList.reduce((sum, item) => sum + (item.assetValue || 0), 0)
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val && this.leaseId) {
          console.log('LeaseAssetDialog: visible changed, leaseId =', this.leaseId)
          this.loadData()
        }
      },
      immediate: false
    },
    leaseId: {
      handler(val) {
        console.log('LeaseAssetDialog: leaseId changed to', val)
        if (this.dialogVisible && val) {
          this.loadData()
        }
      },
      immediate: false
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 获取租赁基本信息
        const leaseRes = await getFinancialLeaseDetail(this.leaseId)
        if (leaseRes && (leaseRes.code === 1 || leaseRes.code === 200)) {
          this.leaseInfo = leaseRes.data || {}
        }
        // 获取资产列表
        const assetRes = await getLeaseAssetList(this.leaseId)
        if (assetRes && (assetRes.code === 1 || assetRes.code === 200)) {
          const data = assetRes.data || {}
          this.assetList = data.list || []
          this.assetStatistics = data.statistics || {}
        }
      } catch (e) {
        console.error('获取数据失败:', e)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    handleAddAsset() {
      this.assetFormTitle = '添加资产'
      this.isAssetEdit = false
      this.assetForm = { assetId: null, leaseId: this.leaseId, assetCode: '', assetName: '', assetType: '', assetValue: 0, purchaseDate: '', location: '', description: '' }
      this.assetFormVisible = true
    },
    handleViewAsset(row) {
      this.assetFormTitle = '查看资产'
      this.assetForm = { ...row }
      this.assetFormVisible = true
    },
    handleEditAsset(row) {
      this.assetFormTitle = '编辑资产'
      this.isAssetEdit = true
      this.assetForm = { ...row }
      this.assetFormVisible = true
    },
    async handleDeleteAsset(row) {
      try {
        await this.$confirm('确认删除该资产?', '提示', { type: 'warning' })
        const res = await deleteLeaseAsset(row.assetId)
        if (res && (res.code === 1 || res.code === 200)) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') {
          console.error('删除失败:', e)
          this.$message.error('删除失败')
        }
      }
    },
    async submitAssetForm() {
      this.$refs.assetForm.validate(async valid => {
        if (valid) {
          try {
            const res = await saveLeaseAsset(this.assetForm)
            if (res && (res.code === 1 || res.code === 200)) {
              this.assetFormVisible = false
              this.$message.success(this.isAssetEdit ? '编辑成功' : '添加成功')
              this.loadData()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (e) {
            console.error('保存失败:', e)
            this.$message.error('保存失败')
          }
        }
      })
    },
    formatCurrency(val) {
      if (!val && val !== 0) return '-'
      return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    getLeasingTypeText(type) {
      const map = { DIRECT: '直接租赁', LEASEBACK: '售后回租', LEVERAGED: '杠杆租赁', OPERATING: '经营租赁' }
      return map[type] || type || '-'
    },
    getTermUnitText(unit) {
      const map = { M: '个月', Y: '年', D: '天' }
      return map[unit] || unit || ''
    },
    getStatusText(status) {
      const map = { PENDING: '待提交', SUBMITTED: '已提交', APPROVED: '已审批', REJECTED: '已拒绝', ACTIVE: '执行中', COMPLETED: '已完成' }
      return map[status] || status || '-'
    },
    getStatusTagType(status) {
      const map = { PENDING: 'info', SUBMITTED: 'warning', APPROVED: 'success', REJECTED: 'danger', ACTIVE: 'primary', COMPLETED: '' }
      return map[status] || 'info'
    },
    getAssetTypeText(type) {
      const map = { EQUIPMENT: '设备', VEHICLE: '车辆', PROPERTY: '房产', OTHER: '其他' }
      return map[type] || type || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-management {
  .info-card, .asset-card { margin-bottom: 15px; }
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .asset-summary {
    margin-top: 15px;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;
    span { margin-right: 30px; color: #606266; }
    strong { color: #409EFF; }
  }
  .danger-btn { color: #F56C6C; }
}
</style>

