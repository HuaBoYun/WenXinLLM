<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <LeftOrgTree @select="changeNode" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel>
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.solutioncode"
                    clearable
                    placeholder="方案编号"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '方案编号'"
                  />
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
                  <el-button
                    native-type="submit"
                    type="primary"
                    @click="resetSearch"
                  >
                    重置
                  </el-button>
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
            <el-button
              type="success"
              @click="handleAdd(false, 'add')"
              v-if="hasAuth('GZYJadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column align="center" label="方案编号" sortable="custom">
              <template #default="{ row }">
                <span
                  style="color: red; cursor: pointer"
                  @click="handleAdd(row, 'look')"
                  v-if="hasAuth('GZYJdetail')"
                >
                  {{ row.SOLUTIONCODE }}
                </span>
                <span v-else>{{ row.SOLUTIONCODE }}</span>
              </template>
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
                label="创建人"
                prop="REALNAME"
                v-if="item.name === '创建人'"
              />
              <el-table-column
                align="center"
                label="执行状态"
                prop="RUNSTATUS"
                v-if="item.name === '执行状态'"
              >
                <template #default="{ row }">
                  {{
                    row.RUNSTATUS == 0
                      ? '未执行'
                      : row.RUNSTATUS == 1
                      ? '已执行'
                      : ''
                  }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="创建时间"
                prop="CREATEDATE"
                v-if="item.name === '创建时间'"
                sortable="custom"
              />
              <el-table-column
                align="center"
                label="是否启用"
                prop="SOLUTIONSTATUS"
                v-if="item.name === '是否启用'"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="start(row)">
                    {{ row.SOLUTIONSTATUS == 1 ? '启用' : '禁用' }}
                  </el-button>
                </template>
              </el-table-column>

              <el-table-column
                align="center"
                label="预警结果"
                prop="data"
                v-if="item.name === '预警结果' && hasAuth('GZYJresult')"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="result(row)"
                    style="color: red"
                  >
                    结果
                  </el-button>
                </template>
              </el-table-column>
            </div>

            <el-table-column align="center" label="操作" width="120">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleAdd(row, 'edit')"
                  v-if="hasAuth('GZYJedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDown(row)"
                  v-if="hasAuth('GZYJdown')"
                >
                  执行
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="hasAuth('GZYJdelete')"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          class="pagination"
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <remind-edit ref="edit" @fetch-data="fetchData" />
    <remind-info ref="remindInfo" @fetch-data="fetchData" />
    <RuleResult ref="ruleresult" />
  </div>
</template>

<script>
  import {
    solutionDel,
    SolutionExecute,
    Solutionmgmt,
    SolutionUpdateStatus,
  } from '@/api/monitor/rule/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import LeftOrgTree from '@/views/monitor/components/LeftOrgTree'
  import RemindInfo from '@/views/monitor/ruleMonitor/components/RemindInfo'
  import RuleResult from '@/views/monitor/ruleMonitor/components/RuleResult'
  import { hasAuth } from '@/utils'

  export default {
    name: 'RuleRemind',
    components: {
      RemindInfo,
      LeftOrgTree,
      RuleResult,
      filterTable,
      filterSearch,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          solutioncode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        orgid: '',
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-ruleMonitor-ruleRemind-search',
        tableKey: 'monitor-ruleMonitor-ruleRemind-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '方案名称' },
          { name: '创建人' },
          { name: '执行状态' },
          { name: '创建时间' },
          { name: '是否启用' },
          { name: '预警结果' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      // this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
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
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '方案编号', key: 'solutioncode' }]
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
      changeNode(node) {
        this.orgid = node.id
        this.fetchData()
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
          data: {
            pageBean: { total, records },
          },
        } = await Solutionmgmt({ ...this.queryForm, orgId: this.orgid,sortFields: this.sortFields,
          sortFlag: this.sortFlag, })
        records.forEach((e) => {
          e.CREATEDATE = e.CREATEDATE.split(' ')[0]
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = {
          solutioncode: '',
          // name: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      handleAdd(row, flag) {
        this.$refs['remindInfo'].showEdit(row, flag, this.orgid)
      },
      result(row) {
        this.$refs['ruleresult'].showEdit(row, '规则预警')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        solutionDel({ selectedid: row.solutionid }).then((res) => {
          if (res.msg == '成功') {
            this.$message.success(res.msg)
            this.fetchData()
          } else {
            this.$message.error(res.msg)
          }
        })
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      start(row) {
        let params = {
          solutionid: row.SOLUTIONID,
          pageNumber: this.queryForm.pageNumber,
        }

        SolutionUpdateStatus(params).then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg)
            this.fetchData()
          } else {
            this.$message.error(res.msg)
          }
        })
      },
      handleDown(row) {
        SolutionExecute({ selectedid: row.SOLUTIONID }).then((res) => {
          if (res.msg == '成功') {
            this.$message.success(res.msg)
            this.fetchData()
          } else {
            this.$message.error(res.msg)
          }
        })
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
