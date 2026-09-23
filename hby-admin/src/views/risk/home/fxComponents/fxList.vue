<template>
  <div class="fx-list">
    <!-- 标题 -->
    <div class="title-container">
      <h3 class="chart-title">公司风险总览</h3>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <!-- 公司名称 -->
      <el-table-column
        prop="ORGNAME"
        label="公司名称"
        fixed="left"
        align="center"
      ></el-table-column>
      <el-table-column label="风险总数" align="center" width="80">
        <template #default="{ row }">
          <div v-if="row.ZS > 0">{{ row.ZS }}</div>
        </template>
      </el-table-column>
      <el-table-column label="未评估" align="center" width="80">
        <template #default="{ row }">
          <div v-if="row.WPG > 0">{{ row.WPG }}</div>
        </template>
      </el-table-column>
      <el-table-column label="已评估" align="center">
        <el-table-column label="总数" width="70" align="center">
          <template #default="{ row }">
            <div v-if="row.COUNT0 > 0">{{ row.COUNT0 }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT1" label="很低" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT1 > 0"
              class="calCount1"
              @click="handleRiskDetail(row, 'COUNT1')"
            >
              {{ row.COUNT1 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT2" label="较低" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT2 > 0"
              class="calCount2"
              @click="handleRiskDetail(row, 'COUNT2')"
            >
              {{ row.COUNT2 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT3" label="中等" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT3 > 0"
              class="calCount3"
              @click="handleRiskDetail(row, 'COUNT3')"
            >
              {{ row.COUNT3 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT4" label="较高" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT4 > 0"
              class="calCount4"
              @click="handleRiskDetail(row, 'COUNT4')"
            >
              {{ row.COUNT4 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT5" label="很高" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT5 > 0"
              class="calCount5"
              @click="handleRiskDetail(row, 'COUNT5')"
            >
              {{ row.COUNT5 }}
            </div>
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 已关闭 -->
      <el-table-column label="已关闭" align="center">
        <el-table-column label="总数" width="70" align="center">
          <template #default="{ row }">
            <div v-if="row.COUNT6 > 0">{{ row.COUNT6 }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT7" label="很低" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT7 > 0"
              class="calCount1"
              @click="handleRiskDetail(row, 'COUNT7')"
            >
              {{ row.COUNT7 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT8" label="较低" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT8 > 0"
              class="calCount2"
              @click="handleRiskDetail(row, 'COUNT8')"
            >
              {{ row.COUNT8 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT9" label="中等" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT9 > 0"
              class="calCount3"
              @click="handleRiskDetail(row, 'COUNT9')"
            >
              {{ row.COUNT9 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT10" label="较高" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT10 > 0"
              class="calCount4"
              @click="handleRiskDetail(row, 'COUNT10')"
            >
              {{ row.COUNT10 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="COUNT11" label="很高" width="70" align="center">
          <template #default="{ row }">
            <div
              v-if="row.COUNT11 > 0"
              class="calCount5"
              @click="handleRiskDetail(row, 'COUNT11')"
            >
              {{ row.COUNT11 }}
            </div>
          </template>
        </el-table-column>
      </el-table-column>
    </el-table>

    <!-- 风险详情弹窗 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="
        () => {
          dialogVisible = false
        }
      "
    >
      <el-table
        v-loading="listLoading"
        border
        stripe
        :data="riskList"
        style="width: 100%"
      >
        <el-table-column align="center" label="风险编号" prop="risknumber">
          <template #default="{ row }">
            <el-button type="text" @click="handleRead(row)">
              {{ row.risknumber }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="风险名称" prop="riskname" />
        <el-table-column align="center" label="风险领域" prop="riskcatname" />
        <el-table-column align="center" label="风险类型" prop="riskcatidname" />
        <el-table-column align="center" label="风险描述" prop="riskdes" />
        <el-table-column align="center" label="牵头责任部门" prop="zrbmName" />
        <el-table-column align="center" label="所属公司" prop="unitname" />
        <el-table-column align="center" label="所属部门" prop="linkDeptName" />
        <el-table-column align="center" label="风险等级" prop="level">
          <template #default="{ row }">
            <div
              :class="{
                calCount1: row.level === '很低',
                calCount2: row.level === '较低',
                calCount3: row.level === '中等',
                calCount4: row.level === '较高',
                calCount5: row.level === '很高',
              }"
            >
              {{ row.level }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="riskcreatedt"
          :formatter="formatDate"
        />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleDialogSizeChange"
          @current-change="handleDialogCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        ></el-pagination>
      </div>
    </el-dialog>
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import { getFxpgRlt } from '@/api/risk/home'
  import { getTjfxpgjgList } from '@/oapi/risk/index'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { formatDay } from '@/utils/index'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'

  export default {
    name: 'FxList',
    components: {
      WfqdDeal,
    },
    data() {
      return {
        tableData: [],
        dialogVisible: false,
        dialogTitle: '',
        riskList: [],
        listLoading: false,
        currentPage: 1,
        pageSize: 10,
        total: 0,
        currentOrgId: '',
        currentPgStatus: '',
        currentCxLevel: 0,
      }
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSizeChange(val) {
        this.pageSize = val
        this.loadData()
      },
      handleCurrentChange(val) {
        this.currentPage = val
        this.loadData()
      },
      async loadData() {
        const {
          data: { data },
        } = await getFxpgRlt()
        this.tableData = data
        console.log('🚀 ~ loadData ~ data:', data)
      },
      async handleRiskDetail(row, countType) {
        console.log('点击风险详情:', row, countType)

        // 根据countType确定pgStatus和cxlevel
        let pgStatus, cxlevel

        // COUNT1-COUNT5是已评估（排除已关闭），COUNT7-COUNT11是已关闭
        if (
          ['COUNT1', 'COUNT2', 'COUNT3', 'COUNT4', 'COUNT5'].includes(countType)
        ) {
          pgStatus = 'ypg' // 已评估
        } else if (
          ['COUNT7', 'COUNT8', 'COUNT9', 'COUNT10', 'COUNT11'].includes(
            countType
          )
        ) {
          pgStatus = 'ygb' // 已关闭
        }

        // 根据countType确定风险等级
        const levelMap = {
          COUNT1: 1,
          COUNT7: 1, // 很低
          COUNT2: 2,
          COUNT8: 2, // 较低
          COUNT3: 3,
          COUNT9: 3, // 中等
          COUNT4: 4,
          COUNT10: 4, // 较高
          COUNT5: 5,
          COUNT11: 5, // 很高
        }
        cxlevel = levelMap[countType]

        const levelNames = {
          1: '很低',
          2: '较低',
          3: '中等',
          4: '较高',
          5: '很高',
        }

        const statusNames = {
          ypg: '已评估',
          ygb: '已关闭',
        }

        this.currentOrgId = row.UNIT
        this.currentPgStatus = pgStatus
        this.currentCxLevel = cxlevel
        this.dialogTitle = `${row.ORGNAME}`
        this.currentPage = 1
        this.dialogVisible = true
        await this.loadRiskList()
      },
      async loadRiskList() {
        this.listLoading = true
        try {
          const params = {
            orgid: this.currentOrgId,
            pageNo: this.currentPage,
            pageSize: this.pageSize,
            pgStatus: this.currentPgStatus,
            cxlevel: this.currentCxLevel,
          }
          const {
            data: { data },
          } = await getTjfxpgjgList(params)
          if (data) {
            this.riskList = data.list || []
            this.total = data.total || 0
          }
        } catch (error) {
          console.error('加载风险列表失败:', error)
          this.$message.error('加载风险列表失败')
        } finally {
          this.listLoading = false
        }
      },
      handleDialogSizeChange(val) {
        this.pageSize = val
        this.loadRiskList()
      },
      handleDialogCurrentChange(val) {
        this.currentPage = val
        this.loadRiskList()
      },
      async handleRead(row) {
        const res = await getFlowPkInfo({
          formId: row.riskid,
          tableId: 90,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
    },
    mounted() {
      this.loadData()
    },
  }
</script>

<style scoped lang="scss">
  .fx-list {
    padding: 20px;
  }

  /* 标题样式 */
  .title-container {
    margin-bottom: 10px;
  }

  .chart-title {
    font-size: 14px;
    color: #ff8c00;
    margin: 0;
    text-align: left;
  }

  .pagination-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }

  /* 风险等级标签样式 */
  .el-tag {
    font-weight: 500;
  }

  /* 风险等级圆点样式 */
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
