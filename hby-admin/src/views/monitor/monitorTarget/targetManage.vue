<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <LeftOrgTree @select="changeNode" />
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
                    v-model="queryForm.kricode"
                    clearable
                    placeholder="指标编号"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '指标编号'"
                  />

                  <el-input
                    v-model="queryForm.kriname"
                    clearable
                    placeholder="指标名称"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '指标名称'"
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
                  <el-button native-type="submit" type="primary">
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
              @click="handleAdd"
              v-if="hasAuth('ZBGLadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column align="center" label="编号" prop="indicatorcode" sortable="custom">
              <template #default="{ row }">
                <span
                  style="color: red; cursor: pointer"
                  @click="handleDeatil(row)"
                  v-if="hasAuth('ZBGLdetail')"
                >
                  {{ row.indicatorcode }}
                </span>
                <span v-else>{{ row.indicatorcode }}</span>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="名称"
                prop="indicatorname"
                v-if="item.name === '名称'"
              />
              <el-table-column
                align="center"
                label="负责部门"
                prop="data"
                v-if="item.name === '负责部门'"
              />
              <el-table-column
                align="center"
                label="执行状态"
                prop="data"
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
                prop="runstatus"
                v-if="item.name === '是否启用'"
              >
                <template #default="{ row }">
                  <el-button
                    class="button-class"
                    @click="setStatus(row)"
                    v-if="hasAuth('ZBGLstatus')"
                  >
                    {{ row.indicatorstatus }}
                  </el-button>
                  <div v-else>{{ row.indicatorstatus }}</div>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="预警结果"
                prop="data"
                v-if="item.name === '预警结果' && hasAuth('ZBGLresult')"
              >
                <template #default="{ row }">
                  <span style="color: red" @click="showModelResult(row)">
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
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('ZBGLedit')"
                >
                  修改
                </el-button>
                <el-dropdown
                  style="margin-left: 10px"
                  @command="handleCommand($event, row)"
                >
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <!-- <el-dropdown-item>启用</el-dropdown-item> -->
                    <el-dropdown-item
                      command="execute"
                      v-if="hasAuth('ZBGLexecute')"
                    >
                      执行
                    </el-dropdown-item>
                    <el-dropdown-item command="copy" v-if="hasAuth('ZBGLcopy')">
                      复制到行业规则库
                    </el-dropdown-item>
                    <el-dropdown-item
                      command="remove"
                      v-if="hasAuth('ZBGLdelete')"
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
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <targetManageEdit ref="edit" @fetch-data="fetchData" />
    <target-info ref="ruleInfo" />
    <model-copy ref="modelCopy" />
    <model-result ref="modelResult" />
  </div>
</template>

<script>
  import {
    getWatchIndexList,
    kriInfoDel,
    KriInfoDisp,
    kriInfoStatus,
  } from '@/api/monitor/watch'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import LeftOrgTree from '@/views/monitor/components/LeftOrgTree'
  import ModelCopy from '@/views/monitor/monitorTarget/components/ModelCopy'
  import ModelResult from '@/views/monitor/monitorTarget/components/ModelResult'
  import TargetInfo from '@/views/monitor/monitorTarget/components/TargetInfo'
  import targetManageEdit from '@/views/monitor/monitorTarget/components/targetManageEdit'
  import { hasAuth } from '@/utils'

  export default {
    name: 'TargetManage',
    components: {
      TargetInfo,
      targetManageEdit,
      LeftOrgTree,
      ModelCopy,
      ModelResult,
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
          kricode: '',
          kriname: '',
          orgId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-monitorTarget-targetManage-search',
        tableKey: 'monitor-monitorTarget-targetManage-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '名称' },
          { name: '负责部门' },
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
          { name: '指标编号', key: 'kricode' },
          { name: '指标名称', key: 'kriname' },
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
      async handleDeatil(row) {
        //
        const data = await KriInfoDisp({
          selectProjectid: row.indicatorid,
        })
        this.$refs['edit'].show(data.data, 'detail')
      },
      async handleEdit(row) {
        //
        const data = await KriInfoDisp({
          selectProjectid: row.indicatorid,
        })
        this.$refs['edit'].show(data.data, 'edit')
      },
      async setStatus(row) {
        const { msg, code } = await kriInfoStatus({
          incid: row.indicatorid,
        })
        if (code == 200) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      showModelResult(row) {
        this.$refs['modelResult'].showEdit(row.modelid)
      },
      changeNode(node) {
        //
        this.queryForm.orgId = node.id
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
        } = await getWatchIndexList({...this.queryForm,sortFields: this.sortFields,
          sortFlag: this.sortFlag,})
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await kriInfoDel({
            selectProjectid: row.indicatorid,
          })
          if (code == 200) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      showRuleInfo(row) {
        this.$refs['ruleInfo'].showEdit()
      },
      async handleCommand(command, row) {
        switch (command) {
          case 'copy':
            this.$refs['modelCopy'].showEdit(row)
            break
          case 'execute':
            const { code, msg } = await executeModelManage({
              id: modelid,
            })
            if (code === 200 && msg === '成功') {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
            break
          case 'remove':
            this.$baseConfirm('你确定要删除当前项吗', null, async () => {
              const { msg, code } = await kriInfoDel({
                selectProjectid: row.indicatorid,
              })
              if (code == 200) {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                await this.fetchData()
              } else {
                this.$baseMessage(msg, 'error', 'vab-hey-message-error')
              }
            })
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
