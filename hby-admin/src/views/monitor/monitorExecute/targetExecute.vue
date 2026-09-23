<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <LeftOrgTree @select="changeNode" />
      </div>
      <div class="right">
        <el-card shadow="never">
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
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column
              align="center"
              label="方案编号"
              prop="SOLUTIONCODE"
              sortable="custom"
            >
              <!-- <template #default="{ row }">
            <span style="color: red; cursor: pointer" @click="showRuleInfo">
              {{ row.data }}
            </span>
          </template> -->
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="方案名称"
                prop="SOLUTIONNAME"
                v-if="item.name === '方案名称'"
              />
              <el-table-column
                align="center"
                label="执行状态"
                prop="SOLUTIONSTATUS"
                v-if="item.name === '执行状态'"
              />
              <el-table-column
                align="center"
                label="创建人"
                prop="REALNAME"
                v-if="item.name === '创建人'"
              />
              <el-table-column
                align="center"
                label="创建日期"
                prop="CREATEDATE"
                :formatter="formatDate"
                v-if="item.name === '创建日期'"
                sortable="custom"
              />
              <el-table-column
                align="center"
                label="执行粒度"
                prop="EXEFREQUNCY"
                v-if="item.name === '执行粒度'"
              >
                <template #default="{ row }">
                  <el-select
                    v-model="row.EXEFREQUNCY"
                    placeholder="请选择"
                    @change="(e) => exeChange(e, row)"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="结果"
                prop="data"
                v-if="item.name === '结果' && hasAuth('ZBZXresult')"
              >
                <template #default="{ row }">
                  <span
                    style="color: red; cursor: pointer"
                    @click="showModelResult(row)"
                  >
                    结果
                  </span>
                </template>
              </el-table-column>
            </div>

            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <el-button type="text" v-if="hasAuth('ZBZXpush')">推送</el-button>
            </el-table-column>
          </el-table>
        </el-card>

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
    </div>

    <model-edit ref="edit" @fetch-data="fetchData" />
    <model-info ref="ruleInfo" />
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />

    <model-result ref="modelResult" />
  </div>
</template>

<script>
  import {
    getTargetExecuteList,
    pushSaveUser,
    updateMonitorSolution,
  } from '@/api/monitor/exectue/index'
  import { formatDate } from '@/utils/index'
  // import FormList from '@/views/monitor/formMonitor/components/FormList'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import filterTable from '@/components/filterTable.vue'
  import LeftOrgTree from '@/views/monitor/components/LeftOrgTree'
  import ModelEdit from '@/views/monitor/modelMonitor/components/ModelEdit'
  import ModelInfo from '@/views/monitor/modelMonitor/components/ModelInfo'
  import ModelResult from './components/ModelResult'
  import { hasAuth } from '@/utils'

  export default {
    name: 'ModelManage',
    components: {
      ModelInfo,
      ModelEdit,
      LeftOrgTree,
      CompanySelectUserByTree,
      filterTable,
      ModelResult,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          // code: '',
          // name: '',
          pageNumber: 1,
          pageSize: 20,
          orgid: '',
        },
        options: [
          {
            value: '日',
            label: '日',
          },
          {
            value: '周',
            label: '周',
          },
          {
            value: '月',
            label: '月',
          },
          {
            value: '季度',
            label: '季度',
          },
          {
            value: '年',
            label: '年',
          },
        ],
        value: '',
        orgId: '',
        tableKey: 'monitor-monitorExecute-targetExecute-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '方案名称' },
          { name: '执行状态' },
          { name: '创建人' },
          { name: '创建日期' },
          { name: '执行粒度' },
          { name: '结果' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.initTable()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
            this.sortFlag = 'asc'
        } else if (order === 'descending') {
            this.sortFlag = 'desc'
        } else {
        this.sortFlag = ''
        }
        await this.fetchData()
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      changeNode(node) {
        this.orgid = node.id
        this.queryForm.orgid = node.id
        this.fetchData()
      },
      async handleExecutorSelected(e) {
        //
        const { msg, code } = await pushSaveUser({
          userid: e.staffid,
          solutionid: this.solutionid,
        })
        // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        if (code == 200) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData(this.queryForm)
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
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
        getTargetExecuteList({...this.queryForm,sortFields: this.sortFields,
          sortFlag: this.sortFlag}).then((res) => {
          this.list = res.data.pageBean.records
          this.total = res.data.pageBean.total
        })
        this.listLoading = false
      },
      async exeChange(e, row) {
        //
        const { msg, code } = await updateMonitorSolution({
          staffid: row.STAFFID,
          solutionid: row.SOLUTIONID,
          exe: e,
        })
        // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData(this.queryForm)
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      showRuleInfo() {
        this.$refs['ruleInfo'].showEdit()
      },
      showModelResult(row) {
        //
        this.$refs['modelResult'].showEdit(row, this.queryForm.orgid)
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
    },
  }
</script>
<style scoped>
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 10px;
    background: #fff;
    height: 100%;
  }
  .lr-layout > .right {
    padding: 0 20px 0 0;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
