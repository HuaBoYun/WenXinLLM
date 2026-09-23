<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="合并模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable @change="handleQuery">
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
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
        <el-button type="primary" icon="el-icon-s-operation" :loading="calculating" @click="handleCalculate">
          计算股权结构
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
      </el-col>
    </el-row>

    <!-- 切换视图 -->
    <el-radio-group v-model="viewType" class="mb8">
      <el-radio-button label="tree">树形视图</el-radio-button>
      <el-radio-button label="table">表格视图</el-radio-button>
    </el-radio-group>

    <!-- 树形视图 -->
    <div v-if="viewType === 'tree'" v-loading="loading">
      <el-tree
        :data="treeData"
        :props="treeProps"
        node-key="orgId"
        default-expand-all
        :expand-on-click-node="false"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span class="node-label">
            <span class="org-name">{{ data.orgName }}</span>
            <span class="org-id">({{ data.orgId }})</span>
          </span>
          <span class="node-info">
            <el-tag v-if="data.level === 1" type="danger" size="mini">顶层</el-tag>
            <el-tag v-else type="success" size="mini">L{{ data.level }}</el-tag>
            <span class="ratio-info">
              <span v-if="data.directHoldingRatio && data.directHoldingRatio > 0" class="direct-ratio">
                直接: {{ data.directHoldingRatio }}%
              </span>
              <span v-if="data.indirectHoldingRatio && data.indirectHoldingRatio > 0" class="indirect-ratio">
                间接: {{ data.indirectHoldingRatio }}%
              </span>
              <span class="total-ratio">
                综合: {{ data.totalHoldingRatio }}%
              </span>
            </span>
          </span>
        </span>
      </el-tree>
    </div>

    <!-- 表格视图 -->
    <el-table v-if="viewType === 'table'" v-loading="loading" :data="tableData" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="层级" prop="level" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.level === 1" type="danger" size="mini">L{{ scope.row.level }}</el-tag>
          <el-tag v-else type="success" size="mini">L{{ scope.row.level }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="组织ID" prop="orgId" width="150" />
      <el-table-column label="组织名称" prop="orgName" width="200" />
      <el-table-column label="父组织ID" prop="parentOrgId" width="150" />
      <el-table-column label="直接持股比例(%)" prop="directHoldingRatio" width="140" align="right">
        <template slot-scope="scope">
          <span v-if="scope.row.directHoldingRatio && scope.row.directHoldingRatio > 0" class="direct-ratio-text">
            {{ scope.row.directHoldingRatio }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="间接持股比例(%)" prop="indirectHoldingRatio" width="140" align="right">
        <template slot-scope="scope">
          <span v-if="scope.row.indirectHoldingRatio && scope.row.indirectHoldingRatio > 0" class="indirect-ratio-text">
            {{ scope.row.indirectHoldingRatio }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="综合持股比例(%)" prop="totalHoldingRatio" width="140" align="right">
        <template slot-scope="scope">
          <span class="total-ratio-text">{{ scope.row.totalHoldingRatio }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="股权路径" prop="path" min-width="200" show-overflow-tooltip />
      <el-table-column label="计算时间" prop="calculationDate" width="160" />
    </el-table>
  </div>
</template>

<script>
import { calculateEquityStructure, getEquityStructureList, getEquityStructureTree, deleteEquityStructure } from '@/api/financialSharing/consolidationReport/equityStructure'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'

export default {
  name: 'EquityStructure',
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: ''
      },
      // 加载状态
      loading: false,
      // 计算中状态
      calculating: false,
      // 视图类型：tree/table
      viewType: 'tree',
      // 树形数据
      treeData: [],
      // 表格数据
      tableData: [],
      // 树形配置
      treeProps: {
        children: 'children',
        label: 'orgName'
      },
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
    /** 查询按钮 */
    handleQuery() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm.modelId = ''
      this.treeData = []
      this.tableData = []
    },
    /** 加载数据 */
    loadData() {
      this.loading = true

      if (this.viewType === 'tree') {
        // 加载树形数据
        getEquityStructureTree({ modelId: this.queryForm.modelId }).then(res => {
          this.loading = false
          if (res.code === 200) {
            this.treeData = res.data || []
            if (this.treeData.length === 0) {
              this.$message.info('暂无股权结构数据,请先计算')
            }
          } else {
            this.$message.error(res.msg || '查询失败')
          }
        }).catch(() => {
          this.loading = false
          this.$message.error('查询失败')
        })
      } else {
        // 加载表格数据
        getEquityStructureList({ modelId: this.queryForm.modelId }).then(res => {
          this.loading = false
          if (res.code === 200) {
            this.tableData = res.data || []
            if (this.tableData.length === 0) {
              this.$message.info('暂无股权结构数据,请先计算')
            }
          } else {
            this.$message.error(res.msg || '查询失败')
          }
        }).catch(() => {
          this.loading = false
          this.$message.error('查询失败')
        })
      }
    },
    /** 计算股权结构 */
    handleCalculate() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }

      this.$confirm('确认计算该模型的股权结构吗?计算将覆盖原有数据', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.calculating = true
        calculateEquityStructure({ modelId: this.queryForm.modelId }).then(res => {
          this.calculating = false
          if (res.code === 200) {
            this.$message.success('计算成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '计算失败')
          }
        }).catch(() => {
          this.calculating = false
          this.$message.error('计算失败')
        })
      }).catch(() => {})
    },
    /** 删除按钮 */
    handleDelete() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }

      this.$confirm('确认删除该模型的股权结构数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteEquityStructure({ modelId: this.queryForm.modelId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.treeData = []
            this.tableData = []
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 刷新按钮 */
    handleRefresh() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      this.loadData()
    }
  },
  watch: {
    viewType() {
      if (this.queryForm.modelId) {
        this.loadData()
      }
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.node-label {
  display: flex;
  align-items: center;
}

.org-name {
  font-weight: bold;
  margin-right: 8px;
}

.org-id {
  color: #909399;
  font-size: 12px;
}

.node-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ratio-info {
  display: flex;
  gap: 10px;
  font-size: 12px;
}

.direct-ratio {
  color: #67C23A;
}

.indirect-ratio {
  color: #E6A23C;
}

.total-ratio {
  color: #409EFF;
  font-weight: bold;
}

.direct-ratio-text {
  color: #67C23A;
}

.indirect-ratio-text {
  color: #E6A23C;
}

.total-ratio-text {
  color: #409EFF;
  font-weight: bold;
}
</style>


