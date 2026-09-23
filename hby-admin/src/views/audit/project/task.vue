<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.prjoectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.auditedObject"
                clearable
                placeholder="项目对象"
                v-if="item.name === '项目对象'"
              />
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
            <el-form-item style="cursor: pointer">
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <vab-query-form>
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
        </vab-query-form-right-panel>
      </vab-query-form>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目编号"
          prop="projectCode"
          width="200"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.projectCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '项目名称'"
            align="center"
            label="项目名称"
            prop="prjoectName"
          />
          <el-table-column
            v-if="item.name === '被审计对象'"
            align="center"
            label="被审计对象"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.orgIdNames }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '开始时间'"
            align="center"
            label="开始时间"
            prop="startDate"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '结束时间'"
            align="center"
            label="结束时间"
            prop="endDate"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="fpStatus"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.fpStatus == '3'
                  ? '启动'
                  : row.fpStatus == '1'
                  ? '分配中'
                  : row.fpStatus == '2'
                  ? '已分配'
                  : '未启动'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="240">
          <template #default="{ row }">
            <el-button type="text" @click="taskAllocation(row)">
              任务分配
            </el-button>
            <el-button type="text" @click="startUp(row)">启动</el-button>
            <el-button type="text" @click="personMaintain(row)">
              人员维护
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <IndexEdit ref="edit" @fetch-data="fetchData" />
    <task-allocation
      v-if="renwuStatue"
      ref="allocation"
      @close="close"
    ></task-allocation>
    <person-maintain ref="maintain"></person-maintain>
  </div>
</template>

<script>
  import { getProjectListXmgl, projectStart } from '@/api/audit/project'
  import { doDelete } from '@/api/table'
  import { UTCformat } from '@/utils'
  import personMaintain from './components/formComponents/PersonMaintain.vue'
  import taskAllocation from './components/formComponents/taskAllocation.vue'
  import IndexEdit from './components/IndexEditNew'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
      IndexEdit,
      taskAllocation,
      personMaintain,
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
          prjoectName: '',
          auditedObject: '',
        },
        renwuStatue: false,
        // 筛选列表配置
        filedAll: [
          { name: '项目名称' },
          { name: '被审计对象' },
          { name: '开始时间' },
          { name: '结束时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-project-task-search',
        tableKey: 'audit-project-task-list',
        searchMore: true,
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
      getFiled() {
        return [
          { name: '项目名称', key: 'prjoectName' },
          { name: '项目对象', key: 'auditedObject' },
        ]
      },
      /**
       * @description  关闭组件
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.renwuStatue = false
        this.fetchData()
      },
      /**
       * @description 任务启动，调用接口，刷新列表
       * @param {*}
       * @return {*}
       */
      async startUp(row) {
        let res = await projectStart({ projectid: row.projectId })
        if (res.code === 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          xiafaListNew({
            tableId: '213',
            jsondistribution: row.jsonString,
          }).then((res) => {
            if (res.msg == '成功') {
              this.$baseMessage(res.msg, 'success')
              this.fetchData()
            }
          })
        }
      },
      /**
       * @description  打开 任务分配  页面
       * @param {*}
       * @return {*}
       */
      taskAllocation(row) {
        this.renwuStatue = true
        this.$nextTick(() => {
          this.$refs['allocation'].showEdit(row)
        })
      },
      /**
       * @description 打开 人员维护  页面
       * @param {*}
       * @return {*}
       */
      personMaintain(row) {
        this.$refs['maintain'].showEdit(row)
      },
      /**
       * @description  重置筛选，把筛选条件清空
       * @param {*}
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          prjoectName: '',
          auditedObject: '',
        }
      },
      /**
       * @description  重置按钮，点击触发的函数
       * @param {*}
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description   分页，选择每页几条数据，查询每页多少条数据
       * @param {*}
       * @return {*}
       */
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
       * @description  分页，选择页码，查询第几页的数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description  查询按钮，回到第一页，查询数据
       * @param {*}
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description  查询接口，条件查询
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getProjectListXmgl(this.queryForm)
        this.list = tlist.map((v) => {
          v.endDate = UTCformat(v.endDate)
          v.startDate = UTCformat(v.startDate)
          return v
        })

        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description   新建按钮触发，唤起新建弹框
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      /**
       * @description  详情/编辑 按钮触发，唤起 详情/编辑 弹框
       * @param {*}
       * @return {*}
       */
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      /**
       * @description 删除按钮触发，删除当前行数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      /**
       * @description 无意义
       * @param {*}
       * @return {*}
       */
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      /**
       * @description  无意义
       * @param {*}
       * @return {*}
       */
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
