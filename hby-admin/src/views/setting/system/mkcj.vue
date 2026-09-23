<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="18">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.modelNo"
                    clearable
                    placeholder="模块编号"
                    v-if="item.name === '模块编号'"
                  />

                  <el-input
                    v-model="queryForm.modelName"
                    clearable
                    placeholder="模块名称"
                    v-if="item.name === '模块名称'"
                  />

                  <el-select
                    v-model="queryForm.modelStatus"
                    clearable
                    filterable
                    placeholder="模块状态"
                    v-if="item.name === '模块状态'"
                  >
                    <el-option
                      v-for="item in modelStatusOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
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
          <vab-query-form-right-panel class="option-row">
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
              @click="handleEdit()"
              v-if="hasAuth('XTMKCJadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column align="center" label="模块编号" prop="modelNo" sortable="custom"/>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="模块名称"
                prop="modelName"
                v-if="item.name === '模块名称'"
              />
              <el-table-column
                align="center"
                label="所属模块"
                v-if="item.name === '所属模块'"
              >
                <template #default="{ row }">
                  {{ transModelType(row.modelType) }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="模块状态"
                v-if="item.name === '模块状态'"
              >
                <template #default="{ row }">
                  {{ transModelStatus(row.modelStatus) }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="创建日期"
                prop="createTime"
                v-if="item.name === '创建日期'"
                sortable="custom"
              />
              <el-table-column
                align="center"
                label="创建人"
                prop="createPersonName"
                v-if="item.name === '创建人'"
              />
            </div>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="240"
            >
              <template #default="{ row }">
                <!-- <el-button
            v-if="row.modelStatus != 2"
            type="text"
            @click="handleEdit(row)"
          >
            修改
          </el-button> -->
                <!-- 未启用可以启用也可以弃用，已启用可以弃用也可以下发，已弃用不能再做任何操作 -->
                <!-- <template v-if="row.modelStatus == 0">
            <el-button type="text" @click="handleStatus(row, 1)">
              启用
            </el-button>
            <el-button type="text" @click="handleStatus(row, 2)">
              弃用
            </el-button>
          </template>
          <template v-if="row.modelStatus == 1">
            <el-button type="text" @click="handleStatus(row, 2)">
              弃用
            </el-button>
            <el-button type="text" @click="handleTree(row)">下发</el-button>
          </template>
          <template>
            <el-button type="text" @click="handleTable(row)">
              取消下发
            </el-button>
          </template> -->
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('XTMKCJedit')"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      @click.native="handleStatus(row, 1)"
                      v-if="hasAuth('XTMKCJstart')"
                    >
                      启用
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleStatus(row, 2)"
                      v-if="hasAuth('XTMKCJstop')"
                    >
                      弃用
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleTree(row)"
                      v-if="hasAuth('XTMKCJdown')"
                    >
                      下发
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleTable(row)"
                      v-if="hasAuth('XTMKCJcanceldown')"
                    >
                      取消下发
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
        <IssueTree ref="tree" @checked="handleChecked" />
        <MkcjEdit ref="edit" @fetch-data="fetchData" />
        <CancelIssueModuleList ref="table" @selected="handleSelected" />
      </div>
    </div>
  </div>
</template>

<script>
  import {
    getModelList,
    issueModule,
    cancelIssueModule,
    updateModuleStatus,
  } from '@/api/setting/system'
  import CancelIssueModuleList from './components/CancelIssueModuleList.vue'
  import MkcjEdit from '@/views/setting/system/components/MkcjEdit'
  import IssueTree from './components/IssueTree.vue'
  import { modelTypeOptions, modelStatusOptions } from './consts'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Mkcj',
    components: {
      MkcjEdit,
      CancelIssueModuleList,
      IssueTree,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          // modelNo: '',
          // modelName: '',
          // modelType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        modelTypeOptions,
        modelStatusOptions,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-mkcj-search',
        tableKey: 'setting-system-mkcj-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '模块名称' },
          { name: '所属模块' },
          { name: '模块状态' },
          { name: '创建日期' },
          { name: '创建人' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
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
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      transModelType(type) {
        const res = this.modelTypeOptions.filter((item) => {
          return item.value == type
        })
        if (res.length) {
          return res[0].label
        } else {
          return type
        }
      },
      transModelStatus(status) {
        const res = this.modelStatusOptions.filter((item) => {
          return item.value == status
        })
        if (res.length) return res[0].label
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
        } = await getModelList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleEdit(row) {
        if (row && row.modelStatus != 0) {
          const label = this.transModelStatus(row.modelStatus)
          this.$baseMessage(
            `该业务模块${label}无法修改`,
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$refs['edit'].showEdit(row)
      },
      handleTree(row) {
        const { modelStatus } = row
        let msg = undefined
        if (modelStatus == 2) {
          msg = '流程模块已弃用无法下发'
        } else if (modelStatus == 0) {
          msg = '流程模块未启用无法下发'
        }
        if (msg) {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['tree'].showTree(row)
      },
      handleTable(row) {
        this.$refs['table'].showTable(row)
      },
      async handleIssued(ids, moduleId) {
        const { code, msg } = await issueModule({
          moduleId,
          orgId: ids.join(','),
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        }
      },
      async handleCancelIssued(ids, moduleId) {
        const { code, msg } = await cancelIssueModule({
          moduleId,
          orgId: ids.join(','),
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        }
      },
      async handleStatus(row, goalStatus) {
        const { modelStatus } = row
        let msg = undefined
        if (goalStatus == 1 && modelStatus == 1) {
          msg = '流程模块已启用无法再次启用'
        } else if (goalStatus == 2 && modelStatus == 2) {
          msg = '流程模块已弃用无法再次弃用'
        } else if (goalStatus == 1 && modelStatus == 2) {
          msg = '流程模块已弃用无法再次启用'
        }
        if (msg) {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          return
        }

        const res = await updateModuleStatus({
          moduleId: row.modelId,
          moduleStatus: goalStatus,
        })
        if (res.code == 1) {
          this.fetchData()
        }
      },
      handleSelected(data, moduleId) {
        const ids = data.map((item) => {
          return item.orgid
        })

        this.handleCancelIssued(ids, moduleId)
      },
      handleChecked(data, moduleId) {
        const ids = data.map((item) => {
          return item.value
        })

        this.handleIssued(ids, moduleId)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '模块编号', key: 'modelNo' },
          { name: '模块名称', key: 'modelName' },
          { name: '模块状态', key: 'modelStatus' },
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
    },
  }
</script>
<style scoped lang="scss">
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
    width: 250px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
