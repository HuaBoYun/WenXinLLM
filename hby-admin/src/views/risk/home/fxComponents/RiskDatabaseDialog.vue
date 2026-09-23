<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="title"
    width="1200px"
    :close-on-click-modal="false"
    append-to-body
    @close="handleClose"
  >
    <el-form
      :inline="true"
      :model="queryForm"
      size="mini"
      style="margin-bottom: 10px"
    >
      <el-form-item>
        <el-input
          v-model="queryForm.risknumber"
          clearable
          placeholder="风险编号"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.riskname"
          clearable
          placeholder="风险名称"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.riskcatidname"
          clearable
          placeholder="风险类型"
        />
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="queryForm.size"
          clearable
          placeholder="风险等级"
          style="width: 120px"
        >
          <el-option label="很低" value="1" />
          <el-option label="较低" value="2" />
          <el-option label="中等" value="3" />
          <el-option label="较高" value="4" />
          <el-option label="很高" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="queryForm.isEvaluated"
          clearable
          placeholder="是否评估"
          style="width: 120px"
        >
          <el-option label="是" value="是" />
          <el-option label="否" value="否" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.unitname"
          clearable
          placeholder="公司名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="queryData">查询</el-button>
        <el-button @click="handleResetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%"
      max-height="800"
    >
      <el-table-column
        align="center"
        width="140"
        label="风险编号"
        prop="risknumber"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.risknumber }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="风险名称"
        prop="riskname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="风险描述"
        prop="riskdes"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="风险领域"
        prop="riskcatname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="风险类型"
        prop="riskcatidname"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="风险等级" min-width="90">
        <template #default="{ row }">
          <div :class="riskLevelClass(row)">
            {{ formatRiskLevel(row) }}
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否评估" min-width="90">
        <template #default="{ row }">
          {{ formatIsEvaluated(row) }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="公司名称"
        prop="unitname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="所属部门"
        prop="superiorDepartment"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建人"
        prop="staffname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="riskcreatedt"
        show-overflow-tooltip
        :formatter="formatDate"
      />
    </el-table>

    <el-pagination
      background
      style="margin-top: 10px; text-align: right"
      :current-page="queryForm.pageNo"
      layout="total, sizes, prev, pager, next, jumper"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <RiskEdit ref="edit" @fetch-data="fetchData" />
  </el-dialog>
</template>

<script>
  import { getRiskLedger } from '@/api/risk'
  import { formatDay } from '@/utils/index'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'

  export default {
    name: 'RiskDatabaseDialog',
    components: {
      RiskEdit,
    },
    data() {
      return {
        dialogVisible: false,
        title: '风险库列表',
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          risknumber: '',
          riskname: '',
          riskcatname: '',
          riskcatidname: '',
          size: '',
          isEvaluated: '',
          unitname: '',
          pageNo: 1,
          pageSize: 10,
          year: '',
        },
      }
    },
    methods: {
      show(params = {}) {
        this.dialogVisible = true
        // 支持传入筛选参数
        if (params.riskcatname) {
          this.queryForm.riskcatname = params.riskcatname
          this.title = `风险库列表 - ${params.riskcatname}`
        }
        if (params.unitname) {
          this.queryForm.unitname = params.unitname
        }
        if (params.riskcatidname) {
          this.queryForm.riskcatidname = params.riskcatidname
        }
        if (params.size) {
          this.queryForm.size = String(params.size)
        }
        if (params.isEvaluated) {
          this.queryForm.isEvaluated = params.isEvaluated
        }
        if (params.year) {
          this.queryForm.year = params.year
        }
        this.fetchData()
      },
      handleClose() {
        this.resetQueryForm()
        this.list = []
        this.title = '风险库列表'
      },
      formatDate(row, column) {
        let data = row[column.property]
        return formatDay(data)
      },
      getRiskLevelNumber(row) {
        const map = {
          很低: 1,
          较低: 2,
          中等: 3,
          较高: 4,
          很高: 5,
        }
        const size = Number(row.size)
        if (Number.isFinite(size) && size >= 1 && size <= 5) {
          return size
        }
        if (typeof row.level === 'number' && row.level >= 1 && row.level <= 5) {
          return row.level
        }
        if (typeof row.level === 'string') {
          if (map[row.level]) return map[row.level]
          const level = Number(row.level)
          if (Number.isFinite(level) && level >= 1 && level <= 5) {
            return level
          }
        }
        return 0
      },
      formatRiskLevel(row) {
        const levelMap = {
          1: '很低',
          2: '较低',
          3: '中等',
          4: '较高',
          5: '很高',
        }
        const levelNumber = this.getRiskLevelNumber(row)
        return levelMap[levelNumber] || '未评估'
      },
      riskLevelClass(row) {
        const levelNumber = this.getRiskLevelNumber(row)
        const classMap = {
          1: 'calCount1',
          2: 'calCount2',
          3: 'calCount3',
          4: 'calCount4',
          5: 'calCount5',
        }
        return classMap[levelNumber] || ''
      },
      formatIsEvaluated(row) {
        return this.getRiskLevelNumber(row) > 0 ? '是' : '否'
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      handleResetQuery() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        try {
          const params = {
            ...this.queryForm,
          }
          const {
            data: {
              pageBean: { list, total },
            },
          } = await getRiskLedger(params)
          this.list = (list || []).sort(
            (a, b) => this.getRiskLevelNumber(b) - this.getRiskLevelNumber(a)
          )
          this.total = total
        } catch (error) {
          console.error('获取风险库数据失败:', error)
        } finally {
          this.listLoading = false
        }
      },
      resetQueryForm() {
        this.queryForm = {
          risknumber: '',
          riskname: '',
          riskcatname: '',
          riskcatidname: '',
          size: '',
          isEvaluated: '',
          unitname: '',
          pageNo: 1,
          pageSize: 10,
          year: '',
        }
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, '', true)
      },
    },
  }
</script>

<style scoped lang="scss">
  .calCount1 {
    position: relative;
    cursor: pointer;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #52ffb7;
    }
  }
  .calCount2 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #33d73b;
    }
  }
  .calCount3 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ffb500;
    }
  }
  .calCount4 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ff7f00;
    }
  }
  .calCount5 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #e92129;
    }
  }
</style>
