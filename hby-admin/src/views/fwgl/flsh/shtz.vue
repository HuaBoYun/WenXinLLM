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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-select
                :style="{ width: '100%' }"
                v-model="queryForm.auditType"
                placeholder="类型"
                clearable
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '类型'"
              >
                <el-option
                  v-for="item in auditTypeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>

              <el-input
                v-model="queryForm.name"
                clearable
                placeholder="名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '名称'"
              />

              <!-- <el-input
                v-model="queryForm.status"
                clearable
                placeholder="状态"
                style="width: 140px; margin-right: 20px"
                type="Number"
                v-if="item.name === '状态'"
              /> -->
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
              <el-button @click="fetchData('reset')" type="primary">
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="编号" prop="institutionAuditId">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.institutionAuditId }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="类型"
            prop="auditType"
            v-if="item.name === '类型'"
          >
            <template #default="{ row }">
              <div>
                {{
                  row.auditType === 1
                    ? '制度审核'
                    : row.auditType === 2
                    ? '经营事项审核'
                    : ''
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="名称"
            prop="auditName"
            v-if="item.name === '名称'"
          />
          <el-table-column
            align="center"
            label="起草人"
            prop="creatorName"
            v-if="item.name === '起草人'"
          />
          <el-table-column
            align="creatorName"
            label="起草部门"
            prop="workUnitName"
            v-if="item.name === '起草部门'"
          />
          <el-table-column
            align="center"
            label="起草时间"
            prop="createdTime"
            v-if="item.name === '起草时间'"
          />

          <el-table-column
            align="center"
            label="状态"
            prop="state"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              <span v-if="row.auditType == 1">
                {{
                  row.state == 1
                    ? '审批中'
                    : row.state == 2
                    ? '已退回'
                    : row.state == 3
                    ? '已通过'
                    : row.state == 4
                    ? '已终止'
                    : row.state == 5
                    ? '已跟踪'
                    : row.state == 6
                    ? '已完成'
                    : '未审批'
                }}
              </span>
              <span v-else>
                {{
                  row.status == 1
                    ? '审批中'
                    : row.status == 2
                    ? '已退回'
                    : row.status == 3
                    ? '已通过'
                    : row.status == 4
                    ? '已终止'
                    : row.status == 5
                    ? '已跟踪'
                    : row.status == 6
                    ? '已完成'
                    : '未审批'
                }}
              </span>
            </template>
          </el-table-column>
        </div>

        <el-table-column width="1" />
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
    <zdshView ref="zdshView" @fetch-data="fetchData" />
    <jysxshView ref="jysxshView" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { legalReviewAudit, fetchApi } from '@/api/fwgl/api'
  import { downloadFile } from '@/utils/otherUtils'
  import zdshView from './components/zdshView.vue'
  import jysxshView from './components/jysxshView.vue'

  const { getList, exportData } = legalReviewAudit

  export default {
    name: 'NormalReportList',
    components: { filterTable, filterSearch, zdshView, jysxshView },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          auditType: '',
          name: '',
          status: '',
          pageNumber: 1,
          pageSize: 20,
        },
        auditTypeList: [
          { label: '制度审核', value: 1 },
          { label: '经营事项审核', value: 2 },
        ],
        filedAll: [
          { name: '类型' },
          { name: '名称' },
          { name: '起草人' },
          { name: '起草部门' },
          { name: '起草时间' },

          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flsh-shtz-search',
        tableKey: 'fwgl-flsh-shtz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '类型', key: 'auditType' },
          { name: '名称', key: 'name' },
          { name: '状态', key: 'status' },
        ]
        return fields
      },
      /**
       * @description: 从上一次缓存中获取搜索项初始化
       * @return {*}
       */
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
      /**
       * @description: 展开收起查询条件
       * @return {*}
       */
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
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
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @param {*} type
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = false
        if (type && type === 'reset') {
          // 手动重置所有搜索字段
          this.queryForm.auditType = ''
          this.queryForm.name = ''
          this.queryForm.status = ''
          this.queryForm.pageNumber = 1
          this.queryForm.pageSize = 20
          // 重置表单验证状态
          if (this.$refs['form']) {
            this.$refs['form'].resetFields()
          }
        }
        const {
          data: { tlist, totalRecord },
        } = await fetchApi(getList, this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        this.listLoading = true
        const res = await fetchApi(exportData, this.queryForm, true)
        downloadFile(res, '审核台账列表.xlsx')
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['shtzView'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        if (row.auditType == 1) {
          await this.$refs['zdshView'].showEdit('detail', {
            id: row.institutionAuditId,
          })
        } else {
          await this.$refs['jysxshView'].showEdit('detail', {
            id: row.institutionAuditId,
          })
        }
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['shtzView'].showEdit('edit', { entermeeting: '1' })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        // this.$baseConfirm('你确定要删除当前项吗', null, async () => {
        //   const { msg } = await doDelete({ ids: row.id })
        //   this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        //   await this.fetchData()
        // })
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
