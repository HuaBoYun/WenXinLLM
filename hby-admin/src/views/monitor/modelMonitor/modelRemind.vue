<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <left-org-tree @select="selectOrganize" />
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
                  <el-button @click="resetSearch">重置</el-button>
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
              @click="handleAdd"
              v-if="hasAuth('MXYJadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list"  @sort-change="sortChange">
            <el-table-column align="center" label="方案编号"  sortable="custom">
              <template #default="{ row, $index }">
                <span
                  style="color: red; cursor: pointer"
                  @click="showRemindInfo($index)"
                  v-if="hasAuth('MXYJdetail')"
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
                prop="runstatus"
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
                <template #default="{ row, $index }">
                  <el-button
                    type="danger"
                    @click="changeModelStatus($index)"
                    v-if="hasAuth('MXYJstatus')"
                  >
                    {{ row.solutionstatus }}
                  </el-button>
                  <div v-else>{{ row.solutionstatus }}</div>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="预警结果"
                prop="data"
                v-if="item.name === '预警结果'"
              >
                <template #default="{ $index }">
                  <span
                    style="color: red; cursor: pointer"
                    @click="showModelResult($index)"
                    v-if="hasAuth('MXYJresult')"
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
              <template #default="{ $index }">
                <el-button
                  type="text"
                  @click="handleEdit($index)"
                  v-if="hasAuth('MXYJedit')"
                >
                  修改
                </el-button>
                <el-dropdown
                  style="margin-left: 10px"
                  @command="handleCommand($event, $index)"
                >
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      command="execute"
                      v-if="hasAuth('MXYJexecute')"
                    >
                      执行
                    </el-dropdown-item>
                    <el-dropdown-item
                      command="remove"
                      v-if="hasAuth('MXYJdelete')"
                    >
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
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
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <remind-edit ref="edit" @fetch-data="fetchData" />
    <remind-info ref="remindInfo" />
    <remind-result ref="remindResult" />
  </div>
</template>

<script>
  import { getModelAddInfo } from '@/api/monitor/model'
  import {
    changeModelRemindStatus,
    executeModelRemind,
    getModelRemindList,
    removeModelRemind,
  } from '@/api/monitor/model/remind'
  import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import RemindEdit from '@/views/monitor/modelMonitor/components/RemindEdit'
  import RemindInfo from '@/views/monitor/modelMonitor/components/RemindInfo'
  import RemindResult from '@/views/monitor/modelMonitor/components/RemindResult'
  import LeftOrgTree from '../components/LeftOrgTree'
  import { hasAuth } from '@/utils'

  export default {
    name: 'ModelRemind',
    components: {
      RemindInfo,
      RemindEdit,
      LeftOrgTree,
      RemindResult,
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
          solutionname: '',
          orgId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        addInfo: {},
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-modelMonitor-modelRemind-search',
        tableKey: 'monitor-modelMonitor-modelRemind-list',
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
      this.getAddInfo()
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
                this.queryForm[x.key] = ''
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
      async getAddInfo() {
        const { code, msg, data } = await getModelAddInfo()
        if (code === 200 && msg === '跳转成功') {
          this.addInfo = data
        }
      },
      selectOrganize({ id, label }) {
        this.queryForm = {
          solutioncode: '',
          solutionname: '',
          orgId: id,
          pageNumber: 1,
          pageSize: 20,
        }
        this.organize = {
          id,
          name: label,
        }
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm.solutioncode = ''
        this.queryForm.solutionname = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const { code, msg, data } = await getModelRemindList({...this.queryForm,sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        if (code === 200 && msg === '成功') {
          const {
            pageBean: { records, total },
          } = data
          this.total = total
          this.list = records
          // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', {
          ...this.addInfo,
          orgid: this.organize.id,
        })
      },
      handleEdit(index) {
        const model = this.list[index]
        this.$refs['edit'].showEdit('edit', {
          ...model,
          ...this.addInfo,
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      showRemindInfo(index) {
        const detail = this.list[index]
        this.$refs['remindInfo'].showEdit({ ...detail, ...this.addInfo })
      },
      showModelResult(index) {
        const { solutionid } = this.list[index]
        this.$refs['remindResult'].showEdit(solutionid)
      },
      async handleCommand(command, index) {
        const model = this.list[index]
        const { solutionid, orgid } = model
        // 执行
        if (command === 'execute') {
          const { code, msg } = await executeModelRemind({
            id: solutionid,
          })
          if (code === 200 && msg === '成功') {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        }
        // 删除
        else if (command === 'remove') {
          this.$baseConfirm('是否删除该模型', null, async () => {
            const { code, msg } = await removeModelRemind({
              orgId: orgid,
              selectedid: solutionid,
            })
            if ((code === 200, msg === '成功')) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
          })
        }
      },
      async changeModelStatus(index) {
        const model = this.list[index]
        const { solutionid } = model
        const { code, msg } = await changeModelRemindStatus({
          solutionid,
        })
        if (code === 200 && msg === '成功') {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
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
