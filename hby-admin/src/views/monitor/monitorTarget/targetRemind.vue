<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <left-org-tree @select="changeNode" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
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

                  <el-input
                    v-model="queryForm.solutionname"
                    clearable
                    placeholder="方案名称"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '方案名称'"
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
              @click="showRemindInfo('add')"
              v-if="hasAuth('ZBYJadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column align="center" label="方案编号"    sortable="custom">
              <template #default="{ row }">
                <span
                  style="color: red; cursor: pointer"
                  @click="showRemindInfo('look', row)"
                  v-if="hasAuth('ZBYJdetail')"
                >
                  {{ row.solutioncode }}
                </span>
                <span v-else>{{ row.solutioncode }}</span>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="方案名称"
                prop="solutionname"
                v-if="item.name === '方案名称'"
              />
              <el-table-column
                align="center"
                label="创建人"
                prop="staffid"
                v-if="item.name === '创建人'"
              />
              <el-table-column
                align="center"
                label="执行状态"
                prop="solutionstatus"
                v-if="item.name === '执行状态'"
              />
              <el-table-column
                align="center"
                label="创建时间"
                prop="createdate"
                v-if="item.name === '创建时间'"
                sortable="custom"
              />
              <el-table-column
                align="center"
                label="是否启用"
                prop="solutionstatus"
                v-if="item.name === '是否启用'"
              >
                <template #default="{ row }">
                  <el-button
                    class="button-class"
                    @click="setStatus(row)"
                    v-if="hasAuth('ZBYJstatus')"
                  >
                    {{ row.solutionstatus }}
                  </el-button>
                  <div v-else>{{ row.solutionstatus }}</div>
                </template>
              </el-table-column>

              <el-table-column
                align="center"
                label="预警结果"
                prop="data1"
                v-if="item.name === '预警结果' && hasAuth('ZBYJresult')"
              >
                <template #default="{ row }">
                  <span
                    style="color: red; cursor: pointer"
                    @click="showModelResult(row)"
                  >
                    结果{{ row.name }}
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
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="showRemindInfo('edit', row)"
                  v-if="hasAuth('ZBYJedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="changeStatus(row)"
                  v-if="hasAuth('ZBYJexecute')"
                >
                  执行
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="hasAuth('ZBYJdelete')"
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
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <RemindInfo ref="remindinfo" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getErrorIndexList,
    SolutionDel,
    kriInfoStatus,
  } from '@/api/monitor/watch'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import LeftOrgTree from '@/views/monitor/components/LeftOrgTree'
  import RemindInfo from '@/views/monitor/monitorTarget/components/TargetInfo'
  import { hasAuth } from '@/utils'

  export default {
    name: 'TargetRemind',
    components: { RemindInfo, LeftOrgTree, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          orgId: '',
          solutionname: '',
          solutioncode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-monitorTarget-targetRemind-search',
        tableKey: 'monitor-monitorTarget-targetRemind-list',
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
        let fields = [
          { name: '方案编号', key: 'solutioncode' },
          { name: '方案名称', key: 'solutionname' },
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
      async setStatus(row) {
        const { msg, code } = await kriInfoStatus({
          incid: row.solutionid,
        })
        if (code == 200) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      changeNode(node) {
        this.queryForm.orgId = node.id
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm.solutionname = ''
        this.queryForm.solutioncode = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
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
        } = await getErrorIndexList({...this.queryForm,sortFields: this.sortFields,
          sortFlag: this.sortFlag,})
        records.forEach((element) => {
          element.createdate = element.createdate.split(' ')[0]
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      showRemindInfo(flag, row) {
        this.$refs['remindinfo'].showEdit(flag, row)
      },
      async changeStatus(row) {
        const { code, msg } = await executeModelManage({
          id: row.id,
        })
        if (code === 200 && msg === '成功') {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await SolutionDel({
            selectedid: row.solutionid,
          })
          if (code == 200) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      showModelResult(row) {
        this.$refs['modelResult'].showEdit(row.modelid)
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
