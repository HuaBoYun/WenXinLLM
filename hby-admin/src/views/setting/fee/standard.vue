<template>
  <div class="fee-standard-container">
    <!-- 顶部筛选 -->
    <div class="filter-bar">
      <el-select v-model="queryForm.moduletype" placeholder="请选择模块" clearable @change="fetchData" style="width: 200px;">
        <el-option v-for="item in moduleList" :key="item.id" :label="item.projectName" :value="item.uniqueIdentification" />
      </el-select>
      <el-button type="primary" icon="el-icon-refresh" @click="fetchData" style="margin-left: 10px;">刷新</el-button>
    </div>

    <!-- 树形表格 -->
    <el-table v-loading="loading" :data="tableData" row-key="id" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" border style="width: 100%; margin-top: 15px;">
      <el-table-column prop="name" label="菜单名称" min-width="200" />
      <el-table-column prop="type" label="类型" width="80" align="center">
        <template slot-scope="{ row }">
          <el-tag v-if="row.type === 0" size="small">目录</el-tag>
          <el-tag v-else-if="row.type === 1" type="success" size="small">页面</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="计费方式" width="100" align="center">
        <template slot-scope="{ row }">
          <span v-if="row.type === 1">{{ row.feeType === 2 ? '按调用量' : '按次' }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="计费金额(元)" width="120" align="center">
        <template slot-scope="{ row }">
          <span v-if="row.type === 1 && row.feeAmount != null">{{ row.feeAmount }}</span>
          <span v-else-if="row.type === 1" style="color: #999;">未设置(使用服务默认)</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="{ row }">
          <el-tag v-if="row.type === 1 && row.feeStatus === 1" type="success" size="small">启用</el-tag>
          <el-tag v-else-if="row.type === 1 && row.feeStatus === 0" type="info" size="small">禁用</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="updateBy" label="修改人" width="100" align="center" />
      <el-table-column label="修改时间" width="160" align="center">
        <template slot-scope="{ row }">{{ row.updateTime ? formatDate(row.updateTime) : '' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template slot-scope="{ row }">
          <el-button v-if="row.type === 1" type="text" size="small" @click="handleEdit(row)">设置</el-button>
          <el-button v-if="row.type === 1 && row.feeAmount != null" type="text" size="small" style="color: #e6a23c;" @click="handleReset(row)">重置</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑弹窗 -->
    <el-dialog title="设置计费标准" :visible.sync="dialogVisible" width="650px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="菜单名称">
          <span>{{ editForm.rightName }}</span>
        </el-form-item>
        <el-form-item label="计费方式">
          <el-select v-model="editForm.feeType" placeholder="请选择">
            <el-option label="按次" :value="1" />
            <el-option label="按调用量" :value="2" />
          </el-select>
        </el-form-item>
        <!-- 按次：固定金额 -->
        <el-form-item v-if="editForm.feeType === 1" label="计费金额">
          <el-input-number v-model="editForm.feeAmount" :min="0.01" :precision="2" :step="0.1" />
          <span style="margin-left: 5px;">元/次</span>
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">不设置则使用各服务yml中配置的默认金额</div>
        </el-form-item>
        <!-- 按调用量：阶梯配置 -->
        <template v-if="editForm.feeType === 2">
          <el-form-item label="阶梯规则">
            <el-button type="text" icon="el-icon-plus" @click="addTier">添加阶梯</el-button>
          </el-form-item>
          <div v-for="(tier, idx) in editForm.tiers" :key="idx" style="display: flex; align-items: center; margin-bottom: 10px; padding-left: 90px;">
            <span style="white-space: nowrap;">调用量</span>
            <el-input-number v-model="tier.minCount" :min="0" size="small" controls-position="right" style="width: 130px; margin: 0 6px;" />
            <span>~</span>
            <el-input-number v-model="tier.maxCount" :min="0" size="small" controls-position="right" style="width: 130px; margin: 0 6px;" placeholder="不限" />
            <span style="white-space: nowrap;">次，单价</span>
            <el-input-number v-model="tier.feeAmount" :min="0" :precision="2" :step="0.1" size="small" controls-position="right" style="width: 140px; margin: 0 6px;" />
            <span style="white-space: nowrap;">元/次</span>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c; margin-left: 8px;" @click="editForm.tiers.splice(idx, 1)" />
          </div>
        </template>
        <el-form-item label="变更备注">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" placeholder="请输入变更原因" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确认保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFeeStandardList, saveFeeStandard } from '@/api/setting/fee'
import { getModuleList } from '@/api/setting/system'

export default {
  name: 'FeeStandard',
  data() {
    return {
      loading: false,
      saving: false,
      dialogVisible: false,
      moduleList: [],
      queryForm: { moduletype: '' },
      tableData: [],
      editForm: { rightId: null, rightName: '', feeType: 1, feeAmount: null, remark: '', tiers: [] },
    }
  },
  created() {
    this.fetchModuleList()
    this.fetchData()
  },
  methods: {
    async fetchModuleList() {
      try {
        const res = await getModuleList({})
        if (res.code === 1 || res.code === 200) {
          this.moduleList = res.data || []
        }
      } catch (e) { console.error(e) }
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getFeeStandardList(this.queryForm)
        if (res.code === 1 || res.code === 200) {
          this.tableData = (res.data && res.data.list) || []
        }
      } catch (e) { console.error(e) }
      this.loading = false
    },
    handleEdit(row) {
      // 映射阶梯数据：后端返回大写字段，前端用驼峰
      const tiers = (row.tiers || []).map(t => ({
        minCount: t.minCount != null ? t.minCount : (t.MIN_COUNT != null ? t.MIN_COUNT : 0),
        maxCount: t.maxCount != null ? t.maxCount : (t.MAX_COUNT != null ? t.MAX_COUNT : null),
        feeAmount: t.feeAmount != null ? t.feeAmount : (t.FEE_AMOUNT != null ? t.FEE_AMOUNT : null),
      }))
      this.editForm = {
        rightId: row.id,
        rightName: row.name,
        feeType: row.feeType || 1,
        feeAmount: row.feeAmount != null ? row.feeAmount : null,
        remark: '',
        tiers: tiers,
      }
      this.dialogVisible = true
    },
    addTier() {
      this.editForm.tiers.push({ minCount: 0, maxCount: null, feeAmount: null })
    },
    handleReset(row) {
      this.$confirm('重置后该页面将使用各服务yml中配置的默认金额计费，确认重置？', '重置计费标准', { type: 'warning' }).then(async () => {
        try {
          const res = await saveFeeStandard({ rightId: row.id, feeType: row.feeType || 1, feeAmount: null, remark: '重置为服务默认值' })
          if (res.code === 1 || res.code === 200) {
            this.$message.success('已重置为服务默认值')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '重置失败')
          }
        } catch (e) {
          this.$message.error('重置失败')
        }
      }).catch(() => {})
    },
    async handleSave() {
      this.$confirm('确认修改该计费标准？修改后即时生效。', '二次确认', { type: 'warning' }).then(async () => {
        this.saving = true
        try {
          const res = await saveFeeStandard(this.editForm)
          if (res.code === 1 || res.code === 200) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '保存失败')
          }
        } catch (e) {
          this.$message.error('保存失败')
        }
        this.saving = false
      }).catch(() => {})
    },
    formatDate(val) {
      if (!val) return ''
      const d = new Date(val)
      const pad = (n) => (n < 10 ? '0' + n : n)
      return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes())
    },
  },
}
</script>

<style lang="scss" scoped>
.fee-standard-container { padding: 20px; }
.filter-bar { display: flex; align-items: center; }
</style>
