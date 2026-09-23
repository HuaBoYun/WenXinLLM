<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <flow-category @node-change="handleNodeChange" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.flownumber"
                    clearable
                    placeholder="流程编号"
                    v-if="item.name === '流程编号'"
                  />

                  <el-input
                    v-model="queryForm.flowname"
                    clearable
                    placeholder="流程名称"
                    v-if="item.name === '流程名称'"
                  />
                  <el-input
                    v-model="queryForm.deparChargeName"
                    disabled
                    placeholder="请选择责任部门"
                    style="width: 187px"
                    v-if="item.name === '责任部门'"
                  />
                  <el-button
                    :style="{ marginLeft: '10px' }"
                    @click="$refs.department.show()"
                    v-if="item.name === '责任部门'"
                    type="primary"
                  >
                    选择
                  </el-button>
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
            </vab-query-form-top-panel>
            <!--        <vab-query-form-left-panel>-->
            <!--          <el-button size="mini" type="primary" @click="handleDeleteTreeItem">-->
            <!--            删除-->
            <!--          </el-button>-->
            <!--        </vab-query-form-left-panel>-->
            <!-- <vab-query-form-right-panel
              v-if="curNode && !curNode.children"
              :span="24"
            >
              <el-button type="success" @click="$refs.ywcjEdit.showEdit()">
                新建
              </el-button>
              <el-button
                type="success"
                @click="$refs.industryCopy.showEdit(curNode)"
              >
                从行业复制
              </el-button>
            </vab-query-form-right-panel> -->
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
            <div v-if="curNode && !curNode.children">
              <el-button
                type="success"
                @click="$refs.ywcjEdit.showEdit()"
                v-if="!hasAuth('XTYWCJadd')"
              >
                新建
              </el-button>
              <el-button
                type="success"
                @click="$refs.industryCopy.showEdit(curNode)"
                v-if="!hasAuth('XTYWCJcopyFormHY')"
              >
                从行业复制
              </el-button>
            </div>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column
              align="center"
              label="流程编号"
              prop="flownumber"
              show-overflow-tooltip
              sortable="custom"
            >
              <template #default="{ row }">
                <el-button
                  v-if="curNode && !curNode.children && hasAuth('XTYWCJdetail')"
                  type="text"
                  @click="handleDetail(row)"
                >
                  {{ row.flownumber }}
                </el-button>
                <span v-else>{{ row.flownumber }}</span>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="流程名称"
                prop="flowname"
                show-overflow-tooltip
                v-if="item.name === '流程名称'"
              />
              <el-table-column
                align="center"
                label="机构"
                prop="company"
                v-if="item.name === '机构'"
              />
              <el-table-column
                align="center"
                label="主责部门"
                prop="deparChargeName"
                v-if="item.name === '主责部门'"
              />
              <el-table-column
                align="center"
                label="创建时间"
                prop="createtime"
                v-if="item.name === '创建时间'"
                sortable="custom"
              />
              <el-table-column
                v-if="curNode && !curNode.children"
                align="center"
                label="流程状态"
                prop="firingStatus"
              >
                <template #default="{ row }">{{ mapFlowStatus(row) }}</template>
              </el-table-column>
            </div>
            <el-table-column width="1" />
            <el-table-column
              v-if="curNode && !curNode.children"
              align="center"
              label="操作"
              show-overflow-tooltip
              width="100"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('XTYWCJedit')"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px" @command="handleCommand">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      v-if="!row.firingStatus && hasAuth('XTYWCJstart')"
                      @click.native="handleSwitchStatus(row, 1)"
                    >
                      启用
                    </el-dropdown-item>
                    <el-dropdown-item
                      v-if="row.firingStatus == 1 && hasAuth('XTYWCJstop')"
                      @click.native="handleSwitchStatus(row, 2)"
                    >
                      弃用
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleDeleteTableItem(row)"
                      v-if="hasAuth('XTYWCJdelete')"
                    >
                      删除
                    </el-dropdown-item>
                    <el-dropdown-item
                      :command="{ type: 'copy', row }"
                      v-if="hasAuth('XTYWCJcopy')"
                    >
                      复制到行业
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
      </div>
      <YwcjEdit
        ref="ywcjEdit"
        :cur-row="curRow"
        :fatherflowid="queryForm.faflowid"
      />
      <copy-to-industry ref="copyToIndustry" />
      <industry-copy ref="industryCopy" @fetch-data="fetchData" />
      <Department ref="department" @selected="handleDepartmentSelected" />
    </div>
  </div>
</template>

<script>
  import {
    getBusinessList,
    deleteBusinessTree,
    switchFiringStatus,
  } from '@/api/setting/system'
  import FlowCategory from '@/views/setting/system/components/FlowCategory'
  import YwcjEdit from '@/views/setting/system/components/YwcjEdit/index.vue'
  import CopyToIndustry from '@/views/setting/system/components/CopyToIndustry'
  import IndustryCopy from '@/views/setting/system/components/IndustryCopy'
  import Department from '@/views/setting/system/components/Department.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Ywcj',
    components: {
      IndustryCopy,
      CopyToIndustry,
      YwcjEdit,
      FlowCategory,
      Department,
      filterSearch,
      filterTable,
    },
    provide() {
      return {
        fatherFetchData: this.fetchData,
      }
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          faflowid: undefined,
          flownumber: undefined,
          flowname: undefined,
          departincharge: undefined,
          deparChargeName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        curNode: null,
        curRow: null,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-ywcj-search',
        tableKey: 'setting-system-ywcj-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '流程名称' },
          { name: '机构' },
          { name: '主责部门' },
          { name: '创建时间' },
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
          faflowid: undefined,
          flownumber: undefined,
          flowname: undefined,
          departincharge: undefined,
          deparChargeName: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
      mapFlowStatus(row) {
        const { firingStatus } = row
        if (!firingStatus) {
          return '未启用'
        }
        if (firingStatus == 1) {
          return '已启用'
        }
        if (firingStatus == 2) {
          return '已弃用'
        }
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getBusinessList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.listLoading = false
        this.list = tlist
        this.total = totalRecord
      },
      async handleCommand(command) {
        const { type, row } = command
        switch (type) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit(row)
            break
        }
      },
      async handleSwitchStatus(row, goalStatus) {
        const params = {
          flowid: row.flowid,
          firing: goalStatus,
        }
        const code = await switchFiringStatus(params)
        let msg = ''
        if (code == 0) {
          msg = goalStatus == 1 ? '流程启用成功' : '流程已弃用'
        } else if (code == -1) {
          msg = '已启动其它流程无法再次启动'
        } else if (code == 1) {
          msg = '该流程没有发布工作流信息，无法启用。'
        } else if (code == 2) {
          msg = '工作流为空，请先设计流程并成功保存，再进行发布'
        } else if (code == 3) {
          msg = '工作流不符要求，请至少设计一条主线流程'
        } else if (code == 4) {
          msg = '工作流流程描述重复，请进入工作流编辑页面进行修改'
        }

        if (code == 0) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      handleNodeChange(val) {
        this.curNode = val
        this.queryForm.faflowid = val.id
        this.fetchData()
      },
      handleEdit(row) {
        this.curRow = row
        this.$refs['ywcjEdit'].showEdit(row)
      },
      handleDetail(row) {
        this.curRow = row
        this.$refs['ywcjEdit'].showDetail(row)
      },
      // async handleDeleteTreeItem() {
      //
      //   const res = await deleteBusinessTree({ flowid: this.curNode.id })
      //
      // },
      async handleDeleteTableItem(row) {
        const { msg, code } = await deleteBusinessTree({ flowid: row.flowid })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      handleDepartmentSelected(node) {
        const { checked } = node
        this.queryForm.departincharge = checked.id
        this.queryForm.deparChargeName = checked.text
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '流程编号', key: 'flownumber' },
          { name: '流程名称', key: 'flowname' },
          { name: '责任部门', key: 'departincharge' },
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
