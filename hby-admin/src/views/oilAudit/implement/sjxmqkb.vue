<template>
  <div class="system-log-container">
    <!-- <vab-query-form>
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
                v-model="queryForm.name"
                clearable
                placeholder="项目名称"
                style="margin-right: 20px"
                v-if="item.name === '项目名称'"
              ></el-input>

              <template v-if="item.name === '被审计单位'">
                <el-input
                  v-model="queryForm.auditUnit"
                  clearable
                  placeholder="请选择被审计单位"
                  :style="{ width: '196px' }"
                  disabled
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="$refs['department'].show()"
                  size="small"
                >
                  选择
                </el-button>
              </template>
              <el-date-picker
                v-model="queryForm.sceneApproveStaerTime"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd "
                placeholder="开始时间"
                :style="{ width: '100%' }"
                v-if="item.name === '开始时间'"
              ></el-date-picker>
              <el-date-picker
                v-model="queryForm.sceneApproveEndTime"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="结束时间"
                :style="{ width: '100%' }"
                v-if="item.name === '结束时间'"
              ></el-date-picker>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>

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
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form> -->

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
        <el-button type="success" @click="handleAdd" v-if="isShow">
          新建
        </el-button>
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="项目名称"
          prop="implementPlanEntities.projectName"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '审计组'"
            align="center"
            label="审计组"
            prop="auditGroup"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.implementPlanEntities.xmqd
                  ? row.implementPlanEntities.xmqd.auditGroup
                  : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '实施单位'"
            align="center"
            label="实施单位"
            prop="exePhraseUnit"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.implementPlanEntities.xmqd
                  ? row.implementPlanEntities.xmqd.ssorgname
                  : ''
              }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '被审计单位'"
            align="center"
            label="被审计单位"
            prop="implementPlanEntities.auditOrgName"
            show-overflow-tooltip
          />

          <el-table-column
            v-if="item.name === '组长'"
            align="center"
            label="组长"
          >
            <template #default="{ row }">
              {{
                row.implementPlanEntities.teams.length
                  ? row.implementPlanEntities.teams[0].teamLeader.realname
                  : ''
              }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '主审'"
            align="center"
            label="主审"
            prop="implementPlanEntities.zsname"
          />
          <el-table-column
            v-if="item.name === '助审'"
            align="center"
            label="助审"
          >
            <template #default="{ row }">
              {{
                row.implementPlanEntities.teams.length
                  ? row.implementPlanEntities.teams[0].teamMembers[0].realname
                  : ''
              }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '现场审计开始时间'"
            align="center"
            label="现场审计开始时间"
            prop="sceneApproveStaerTime"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.implementPlanEntities.xmqd
                  ? row.implementPlanEntities.xmqd.xcsrarttime
                  : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '计划现场工作时间'"
            align="center"
            label="计划现场工作时间"
            prop="planSceneApproveStaerTime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划现场结束日期'"
            align="center"
            label="计划现场结束日期"
            prop="planSceneApproveEndTime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '实际现场结束日期'"
            align="center"
            label="实际现场结束日期"
            prop="sceneApproveEndTime"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.implementPlanEntities.xmqd
                  ? row.implementPlanEntities.xmqd.xcendtime
                  : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '时间'"
            align="center"
            label="时间"
            prop="weekDate"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '本周工作'"
            align="center"
            label="本周工作"
            prop="weekWork"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '下周工作'"
            align="center"
            label="下周工作"
            prop="nextWeekWork"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              v-if="isShow"
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleManage(row)"
                    :disabled="!row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.status || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.status"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <!-- <el-dropdown-item>
                  <el-button type="text" @click="handExcel(row)">
                    导出
                  </el-button>
                </el-dropdown-item> -->
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- <SJXMQKBview ref="edit" @fetchData="fetchData"></SJXMQKBview> -->
    <sjxmqkbView ref="edit" @fetchData="fetchData" />
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
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import {
    getProjectWeerklyList,
    weerklyDel,
    weerklyExport,
  } from '@/oapi/audit/implement'
  import SJXMQKBview from './components/sjxmqkbView.vue'
  import sjxmqkbView from './components/sjxmqkbNewView.vue'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'

  export default {
    components: {
      SJXMQKBview,
      sjxmqkbView,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      DepartmentOptions,
    },
    props: {
      //项目查看传参
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '审计组' },
          { name: '实施单位' },
          { name: '被审计单位' },
          { name: '组长' },
          { name: '主审' },
          { name: '助审' },
          { name: '现场审计开始时间' },
          // { name: '计划现场工作时间' },
          // { name: '计划现场结束日期' },
          { name: '实际现场结束日期' },
          { name: '时间' },
          { name: '本周工作' },
          { name: '下周工作' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-implement-sjxmqkb-search',
        tableKey: 'oilAudit-implement-sjxmqkb-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        select: [],
        btnLoading: false,
      }
    },
    created() {
      this.fetchData()

      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(156, row.id)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 156,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '项目名称', key: 'name' },
          { name: '被审计单位', key: 'auditUnit' },
          { name: '开始时间', key: 'sceneApproveStaerTime' },
          { name: '结束时间', key: 'sceneApproveEndTime' },
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
          console.log(this.filedNow)
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
        this.btnLoading = false
        this.listLoading = true
        const params = {
          ...this.queryForm,
          projectId: this.projectId,
        }
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getProjectWeerklyList(params)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      async handleExport() {
        let arr = this.select.map((k) => {
          return k.id
        })
        if (arr.length > 0) {
          const data = await weerklyExport({
            ...this.queryForm,
            ids: arr.join(','),
          })
          let fileName = '审计项目运行情况表'
          let blob = new Blob([data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          })
          if (window.navigator.msSaveOrOpenBlob) {
            navigator.msSaveBlob(blob, fileName)
          } else {
            let link = document.createElement('a')
            link.href = window.URL.createObjectURL(blob)
            link.download = fileName
            link.click()
            // 释放内存
            window.URL.revokeObjectURL(link.href)
          }
        } else {
          this.$baseMessage('请选择需要导出的数据', 'info')
        }
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await weerklyDel({
            id: row.id,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      isDate(row, col, val) {
        // 获取单元格数据
        return formatDate(val)
      },
      handleDepartmentSelected(node) {
        this.$set(this.queryForm, 'auditUnit', node.label)
        this.$set(this.queryForm, 'auditUnitId', node.id)
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
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
