<template>
  <div class="system-log-container">
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
                v-model="queryForm.assNumnber"
                clearable
                placeholder="评价项目编号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '评价项目编号'"
              />
              <el-input
                v-model="queryForm.assName"
                clearable
                placeholder="评价项目名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '评价项目名称'"
              />
              <el-date-picker
                v-model="queryForm.Date"
                clearable
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="yyyy-MM-dd"
                range-separator="-"
                type="daterange"
                value-format="yyyy-MM-dd"
                style="width: 200px; margin-right: 10px"
                v-if="item.name === '时间'"
              />
            </el-form-item>

            <!-- <el-form-item>
	            <el-date-picker
	              v-model="queryForm.field134"
	              clearable
	              end-placeholder="结束时间"
	              format="yyyy-MM-dd"
	              range-separator="-"
	              start-placeholder="结束时间"
	              :style="{ width: '100%' }"
	              type="daterange"
	              value-format="yyyy-MM-dd"
	            />
	          </el-form-item> -->
            <span>
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
                  :class="searchMore ? 'search-more is-opened' : 'search-more'"
                  @click="showMore"
                >
                  <span>{{ searchMore ? '收起' : '展开' }}</span>
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-form-item>
            </span>
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
              :key="filterKey"
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
            align="center"
            label="评价项目编号"
            prop="assessid"
            #default="{ row }"
            v-if="item.name === '评价项目编号'"
          >
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.assessid }}
            </el-button>
          </el-table-column>
          <!-- <TableColumns :item="item">
            <template #default="{ row }" v-if="item.field === 'templename'">
              <el-button
                type="text"
                @click="$refs['EvaluateModel'].showEdit(row.asstemid)"
              >
                {{ row.templename }}
              </el-button>
            </template>
          </TableColumns> -->

          <el-table-column
            align="center"
            label="评价项目名称"
            prop="assessname"
            show-overflow-tooltip
            v-if="item.name === '评价项目名称'"
          />
          <el-table-column
            align="center"
            label="评价模板"
            prop="templename"
            show-overflow-tooltip
            v-if="item.name === '评价模板'"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="$refs['EvaluateModel'].showEdit(row.asstemid)"
              >
                {{ row.templename }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="评价期限"
            prop="startdate"
            show-overflow-tooltip
            v-if="item.name === '评价期限'"
          >
            <template #default="{ row }">
              {{ row.startdate }} - {{ row.enddate }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="发起日期"
            prop="assstartday"
            show-overflow-tooltip
            v-if="item.name === '发起日期'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="assstatus"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{ status[+row.assstatus] }}
            </template>
          </el-table-column>
          <!-- <el-table-column
            align="center"
            label="审批状态"
            prop="status"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '需调整'
                  : row.status == 3
                  ? '已撤销'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column> -->
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="160"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.assstatus > 1 || createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleAuthorization(row)"
                    v-if="row.assstatus <= 1"
                    :disabled="
                      createId != row.createstaffid && createId != row.leaderid
                    "
                  >
                    授权
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleAuthorization(row, true)"
                    v-if="row.assstatus > 1"
                  >
                    授权查看
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleStart(row)"
                    v-if="row.assstatus <= 1"
                    :disabled="
                      createId != row.createstaffid && createId != row.leaderid
                    "
                  >
                    启动
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    v-if="row.assstatus <= 1"
                    :disabled="createId != row.createstaffid"
                  >
                    删除
                  </el-button>
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
    <ProjectView ref="edit" @fetch-data="fetchData" />
    <EvaluateModel ref="EvaluateModel" />
    <AuthorizationModal ref="AuthorizationModal" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    deleteProject,
    getProjectList,
    startProject,
    getDefaultRenderData,
  } from '@/api/internal/project'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import AuthorizationModal from '@/views/internal/evaluationManagement/components/authorizationModal.vue'
  import EvaluateModel from '@/views/internal/evaluationManagement/components/EvaluateModel'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'
  import TableColumns from '@/components/customForm/TableColumns.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'Project',
    components: {
      ProjectView,
      EvaluateModel,
      AuthorizationModal,
      filterTable,
      filterSearch,
      TableColumns,
      ProcessList,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        status: ['', '已立项', '已启动', '已完成', '已完成'],
        total: 0,
        queryForm: {
          assName: '',
          assNumnber: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '评价项目编号' },
          { name: '评价项目名称' },
          { name: '评价模板' },
          { name: '评价期限' },
          { name: '发起日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-evaluationManagement-project-search',
        tableKey: 'internal-evaluationManagement-project-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filterKey: 'a',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    async created() {
      // await this.fetchCustomerFormData()
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        //提交审批
        this.$refs['process'].save(42, row.assid)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '评价项目编号', key: 'assNumnber' },
          { name: '评价项目名称', key: 'assName' },
          { name: '时间', key: 'Date' },
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
        console.log(this.searchMore)
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
      resetQueryForm() {
        this.queryForm = {
          assName: '',
          assNumnber: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      async fetchCustomerFormData() {
        getDefaultRenderData({
          sceneCode: 'nkgl-pjlx',
        }).then((res) => {
          this.filedAll = res.data || []
          this.filterKey = 'filterKey_' + (Math.random() + 1) * 100 // 为了解决组件不更新的问题，改变组件的key值强制更新
          this.initTable()
        })
      },
      async fetchData() {
        this.listLoading = true
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDates = ''
        if (Date) {
          startDate = Date[0]
          endDates = Date[1]
        }
        const {
          data: { pageBean },
        } = await getProjectList({ ...other, startDate, endDates })
        this.list = pageBean.records
        this.total = pageBean.total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      /*************  ✨ Codeium Command ⭐  *************/
      /**
       * @description: 打开编辑
       * @param {Object} row 当前行数据
       * @param {String} type  edit  preview

/******  c7e219bb-9092-4e5d-bcd5-a25a11a9aa38  *******/
      handleEdit(row, type) {
        // if (!type) {
        //   if (this.createId != row.createstaffid) {
        //     return this.$message.error('只有创建人可以操作')
        //   }
        // }
        this.$refs['edit'].showEdit(row, type)
      },
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteProject({ assid: row.assid })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          await this.fetchData()
        })
      },
      handleAuthorization(row, isActive) {
        console.log(row)
        this.$refs['AuthorizationModal'].showEdit(row, isActive)
      },
      handleStart(row) {
        this.$baseConfirm('你确定要启动立项吗', null, async () => {
          const { msg, code } = await startProject({ assId: row.assid })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            // 调用xiafa方法
            xiafaListNew({
              tableId: '177',
              jsondistribution: row.jsonString,
            }).then((response) => {
              if (response.msg == '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.fetchData()
              }
            })
          }
        })
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
