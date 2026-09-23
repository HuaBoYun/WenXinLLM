<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.planId"
                clearable
                placeholder="计划文号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '计划文号'"
              ></el-input>
              <el-input
                v-model="queryForm.contractNo"
                clearable
                placeholder="合同编号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '合同编号'"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="resetSearch()">重置</el-button>
            </el-form-item>
            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel :span="24">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="right" trigger="click">
            <filter-table
              :list="filedAll"
              :name="tableKey"
              @updateTableShow="initTable"
            />
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '计划文号'"
            align="center"
            label="计划文号"
            prop="planId"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.planId }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="合同编号"
            prop="contractNo"
            v-if="item.name === '合同编号'"
          />
          <el-table-column
            align="center"
            label="施工单位"
            prop="orgId"
            v-if="item.name === '施工单位'"
          >
            <template #default="{ row }">{{ row.org?.orgname }}</template>
          </el-table-column>
          <el-table-column
            align="center"
            label="合同金额（元）"
            prop="contractAmount"
            v-if="item.name === '合同金额（元）'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="结算金额（元）"
            prop="settlementAmount"
            v-if="item.name === '结算金额（元）'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="甲供物资金额（元）"
            prop="materialAmount"
            v-if="item.name === '甲供物资金额（元）'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="已付款总额（元）"
            prop="paidAmount"
            v-if="item.name === '已付款总额（元）'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="尾款（元）"
            prop="balancePayment"
            v-if="item.name === '尾款（元）'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="一审报审、结束时间"
            prop="firstTrialTime"
            v-if="item.name === '一审报审、结束时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="一审报审金额"
            prop="firstTrialAmount"
            v-if="item.name === '一审报审金额'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="二审报审、结束时间"
            prop="secondTrialTime"
            v-if="item.name === '二审报审、结束时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="二审报审金额"
            prop="secondTrialAmount"
            v-if="item.name === '二审报审金额'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="施工图纸设计时间"
            prop="drawingDesignTime"
            v-if="item.name === '施工图纸设计时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="合同开、竣工时间"
            prop="contractEndTime"
            v-if="item.name === '合同开、竣工时间'"
          >
            <template #default="{ row }">
              {{ row.contractStartTime }} - {{ row.contractEndTime }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="实际开、竣工时间"
            prop="workStartTime"
            v-if="item.name === '实际开、竣工时间'"
          >
            <template #default="{ row }">
              {{ row.workStartTime }} - {{ row.workEndTime }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="延期次数"
            prop="delayTimes"
            v-if="item.name === '延期次数'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="延期天数"
            prop="delayDays"
            v-if="item.name === '延期天数'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="延期原因"
            prop="delayReason"
            v-if="item.name === '延期原因'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="竣工资料存档时间"
            prop="archiveTime"
            v-if="item.name === '竣工资料存档时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="招标情况（招标方式）"
            prop="biddingSituation"
            v-if="item.name === '招标情况（招标方式）'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="合同施工内容与计划内容是否一致"
            prop="isConsistentConPlan"
            v-if="item.name === '合同施工内容与计划内容是否一致'"
          >
            <template #default="{ row }">
              {{ row.isConsistentConPlan == 1 ? '一致' : '不一致' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="合同施工内容与实际施工是否一致"
            prop="isConsistentConWork"
            v-if="item.name === '合同施工内容与实际施工是否一致'"
          >
            <template #default="{ row }">
              {{ row.isConsistentConWork == 1 ? '一致' : '不一致' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="remark"
            v-if="item.name === '备注'"
          ></el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <fkqktjView ref="edit" @fetchData="fetchData"></fkqktjView>
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import {
    designPlanInfoList,
    designPlanInfoDelete,
  } from '@/oapi/audit/information'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import fkqktjView from './components/fkqktjView'

  export default {
    components: {
      filterTable,
      filterSearch,
      fkqktjView,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planId: undefined,
          contractNo: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '计划文号' },
          { name: '合同编号' },
          { name: '施工单位' },
          { name: '合同金额（元）' },
          { name: '结算金额（元）' },
          { name: '甲供物资金额（元）' },
          { name: '已付款总额（元）' },
          { name: '尾款（元）' },
          { name: '一审报审、结束时间' },
          { name: '一审报审金额' },
          { name: '二审报审、结束时间' },
          { name: '二审报审金额' },
          { name: '施工图纸设计时间' },
          { name: '合同开、竣工时间' },
          { name: '实际开、竣工时间' },
          { name: '延期次数' },
          { name: '延期天数' },
          { name: '延期原因' },
          { name: '竣工资料存档时间' },
          { name: '招标情况（招标方式）' },
          { name: '合同施工内容与计划内容是否一致' },
          { name: '合同施工内容与实际施工是否一致' },
          { name: '备注' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zhgl-fkqktj-search',
        tableKey: 'oilAudit-zhgl-fkqktj-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '计划文号', key: 'name' },
          { name: '合同编号', key: 'code' },
        ]
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })
          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await designPlanInfoList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleExport() {},
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await designPlanInfoDelete({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          planId: undefined,
          contractNo: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
