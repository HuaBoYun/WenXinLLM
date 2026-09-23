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
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="item.name + '_' + index"
            >
              <el-input
                v-model="queryForm.draftNumber"
                clearable
                placeholder="底稿编号"
                v-if="item.name === '底稿编号'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="审计项目名称"
                v-if="item.name === '审计项目名称'"
              />
              <el-input
                v-model="queryForm.createUserName"
                clearable
                placeholder="拟稿人"
                v-if="item.name === '拟稿人'"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.showEdit()"
                v-if="item.name === '拟稿人'"
              >
                选择
              </el-button>
              <!-- <el-select
                v-model="queryForm.status"
                placeholder="审核状态"
                v-if="item.name === '审核状态'"
              >
                <el-option label="未复核" value="1" />
                <el-option label="复核中" value="2" />
                <el-option label="复核终止" value="3" />
                <el-option label="复核通过" value="4" />
                <el-option label="需调整" value="5" />
              </el-select> -->
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchData('reset')">
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
        <el-button type="success" @click="handleAddOrUpdate()" v-if="isShow">
          新建
        </el-button>
        <el-button type="success" @click="handleExport()">导出</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="底稿编号"
            prop="draftNumber"
            width="170"
            v-if="item.name === '底稿编号'"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleAddOrUpdate(row, true)"
                style="white-space: pre-line; line-height: 16px"
              >
                {{ row.draftNumber }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="底稿名称"
            prop="draftName"
            show-overflow-tooltip
            v-if="item.name === '底稿名称'"
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectName"
            v-if="item.name === '审计项目名称'"
          />

          <el-table-column
            align="center"
            label="审计事项"
            prop="auditMatters"
            v-if="item.name === '审计事项'"
          />
          <el-table-column
            align="center"
            label="被审计单位名称"
            prop="auditeeName"
            v-if="item.name === '被审计单位名称'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
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
        <el-table-column align="center" label="操作" width="160">
          <template #default="{ row }">
            <el-button
              v-if="isShow"
              type="text"
              :disabled="!!row.status"
              @click="handleAddOrUpdate(row)"
            >
              修改
            </el-button>
            <el-button type="text" @click="handleExport2(row)">
              导出word
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
                    :disabled="!!row.status"
                    @click="handlerDelete(row)"
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
    <Views ref="edit" @queryData="queryData"></Views>
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
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqdDeal" />
    <!-- <executor-options ref="executor" @selected="handleExecutorSelected" /> -->
    <project-manage
      @projectManage="getChildlistPro"
      ref="executor"
      :multiple="false"
    ></project-manage>
  </div>
</template>

<script>
  import {
    getList,
    handleDelete,
    myDraftExport,
    myDraftExportWord,
  } from '@/oapi/audit/newMyDraft'
  import Views from './components/newMyDraftView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  // import ExecutorOptions from './components/options/executor.vue'
  import projectManage from '@/components/selectPerson.vue'

  export default {
    components: {
      Views,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      // ExecutorOptions,
      projectManage,
    },
    props: {
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
      const fields = [
        { name: '底稿编号', key: 'draftNumber' },
        { name: '审计项目名称', key: 'projectName' },
        // { name: '拟稿人', key: 'realname' },
        // { name: '审核状态', key: 'status' },
      ] // 定义表单所有项
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
          realname: '',
          draftNumber: '',
          createUserName: '',
          createUser: '',
        },
        filedAll: [
          { name: '底稿编号' },
          { name: '底稿名称' },
          { name: '审计项目名称' },
          { name: '审计事项' },
          { name: '被审计单位名称' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: fields, //所有搜索项
        localKey: 'oilAudit-plan-sqdcbg-search',
        tableKey: 'oilAudit-plan-sqdcbg-list',
        searchNow: fields, //当前所有搜索项
        searchItem: fields.slice(0, 4), //可见搜索项
        searchMore: true,
        select: [],
        btnLoading: false,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
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
      getChildlistPro(val) {
        this.queryForm.createUserName = val[0].realname
        this.queryForm.createUser = val[0].staffid
      },
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(150, row.id)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 150,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      // 动态表格开始
      initTable() {
        this.$nextTick(() => {
          let data = localStorage.getItem(this.tableKey)
          if (data) {
            data = JSON.parse(data)
            this.filedNow = data.filter((item) => item.show)
          } else {
            this.filedNow = this.filedAll
          }
        })
      },
      initSearch() {
        this.$nextTick(() => {
          let data = localStorage.getItem(this.localKey)
          if (data) {
            data = JSON.parse(data)
            this.searchNow = data.filter((item) => item.show)
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

      async fetchData(type) {
        this.btnLoading = false
        this.listLoading = true
        if (type && type === 'reset') {
          this.queryForm = {
            pageNumber: 1,
            pageSize: 20,
            projectName: '',
            realname: '',
            draftNumber: '',
            status: '',
            createUserName: '',
            createUser: '',
          }
        }

        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getList({
          ...this.queryForm,
          projectId: this.projectId,
        })

        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },
      handleAddOrUpdate(row, disabled) {
        this.$refs['edit'].showModal(row, false, disabled)
      },
      handlerDelete(row) {
        // 删除题目
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            handleDelete({ id: row.id }).then(() => {
              this.fetchData()
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
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
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await myDraftExport({
          ...this.queryForm,
          idList: ids.toString(),
        })
        let fileName = '我的底稿'
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
      },
      async handleExport2(row) {
        const data = await myDraftExportWord({
          id: row.id,
        })
        let fileName = row.draftName + '.doc'
        let blob = new Blob([data], {
          type: 'application/msword;charset=utf-8',
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

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
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
