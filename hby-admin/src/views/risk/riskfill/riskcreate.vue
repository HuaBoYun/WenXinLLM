<template>
  <!-- 三级单位及成员离任审计 -->
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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.jd"
                clearable
                placeholder="季度"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '季度'"
              ></el-input>
              <el-date-picker
                v-if="item.name === '年度'"
                v-model="queryForm.nd"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年度"
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <!-- <el-table-column
          width="48"
          align="center"
          type="selection"
          :reserve-selection="true"
        ></el-table-column> -->
        <el-table-column align="center" label="季度" prop="quartername">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.quartername }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="填报单位"
            prop="linkOrgName"
            v-if="item.name === '填报单位'"
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="createname"
            v-if="item.name === '创建人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createtime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="status"
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
        <el-table-column align="center" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!+row.status || createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!+row.status"
                    @click="handleDeal(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!+row.status || createId != row.createstaffid"
                    @click="handleEdit(row, 1)"
                  >
                    下发
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="
                      !!+row.status ||
                      btnLoading ||
                      createId != row.createstaffid
                    "
                    @click="handleSubmit(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!+row.status || createId != row.createstaffid"
                    @click="handleDelete(row)"
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
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <cetateEdit ref="add" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { getCreateList, createDelete } from '@/api/risk/create.js'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { baseURL } from '@/config'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import cetateEdit from './cetateEdit.vue'
  import store from '@/store'
  const token = store.getters['user/token']

  export default {
    components: {
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      cetateEdit,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
        headers: { token },
        select: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          jd: '',
          oldJob: '',
          pageNumber: 1,
          pageSize: 20,
          nd: '',
        },
        filedAll: [
          { name: '填报单位' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-riskfill-riskcreate-search',
        tableKey: 'risk-riskfill-riskcreate-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '季度', key: 'jd' },
          { name: '年度', key: 'nd' },
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
        this.listLoading = true
        try {
          const {
            data: { pageInfo },
            code,
          } = await getCreateList(this.queryForm)
          this.list = pageInfo.list
          this.total = pageInfo.total
        } catch (error) {
          console.error('获取数据失败:', error)
        } finally {
          this.listLoading = false
        }
        this.setCheckedRows()
      },
      handleEdit(row, type) {
        // if (!type) {
        //   if (this.createId != row.createstaffid) {
        //     return this.$message.error('只有创建人可以操作')
        //   }
        // }
        this.$refs['add'].showEdit(row, 'edit', type)
      },
      handleDetail(row) {
        this.$refs['add'].showEdit(row, 'detail')
      },
      handleAdd() {
        this.$refs['add'].showEdit('', 'add')
      },
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await createDelete({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        const pageSize = this.queryForm.pageSize
        this.queryForm = {
          jd: '',
          oldJob: '',
          createType: 1,
          pageNumber: 1,
          pageSize,
          nd: '',
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 212,
        })
        /*************  ✨ Codeium Command ⭐  *************/
        /**
         * @description: 提交审批的处理函数
         * @param {Object} row - 当前行的数据对象
         * @throws {Error} 如果当前用户不是创建人则抛出错误信息
         * @returns {void}
         * @description: 该函数首先检查当前用户是否为创建人，如果不是则显示错误信息；如果是，则进行提交审批的操作。
         * 在提交过程中，将弹出确认框，确认后调用流程处理组件的 save 方法进行提交，并在提交过程中显示加载状态。
         * 提交完成后，无论成功或失败，都会关闭加载状态。
         */

        /******  410b3e50-c929-4294-ac9c-b689d1b54249  *******/
        this.$refs.wfqddeal.show(res.data, false)
      },

      handleSubmit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
            this.btnLoading = true
            this.$refs['process'].save(212, row.id)
          })
        } catch (error) {
        } finally {
          this.btnLoading = false
        }
      },
      hadnlePush() {
        if (this.select && this.select.length > 0) {
          this.$refs.person.showEdit()
        } else {
          this.$baseMessage(
            '请选择需要下发的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await sjdwlrsjlrExportData({
          ...this.queryForm,
          ids: ids.join(),
        })
        let fileName = '三级单位离任审计表'
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
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.quartername)
        const names = val.map((res) => res.staffid)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }
        //下发保存
        sjdwlrsjlrDistributeFund({
          auditIds: ids.toString(),
          userIds: names.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '1395',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
      },
      handleSelection(val, row) {
        if (!row) return
        const index = this.select.findIndex((x) => x.id === row.id)
        if (val) {
          if (index === -1) {
            this.select.push(row)
          }
        } else {
          if (index > -1) {
            this.select.splice(index, 1)
          }
        }
      },
      handleSelectAll(val) {
        if (val && val.length) {
          val.forEach((row) => {
            if (!this.select.some((x) => x.id === row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.forEach((row) => {
            const index = this.select.findIndex((x) => x.id === row.id)
            if (index > -1) {
              this.select.splice(index, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          if (!this.$refs.multipleTable) return
          // 清除之前的选择状态
          this.$refs.multipleTable.clearSelection()
          // 设置新的选择状态
          this.select.forEach((row) => {
            const matchedRow = this.list.find((item) => item.id === row.id)
            if (matchedRow) {
              this.$refs.multipleTable.toggleRowSelection(matchedRow, true)
            }
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
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
