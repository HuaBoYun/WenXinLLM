<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel style="width: 100%">
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
              <!-- <el-input
                v-model="queryForm.auditedObject"
                clearable
                placeholder="项目对象"
                v-if="item.name === '项目对象'"
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
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel style="width: 100%">
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

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目编号"
          prop="qdcode"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="项目名称" prop="projectName">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.projectName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '项目经理'"
            align="center"
            label="项目经理"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '审计类型'"
            align="center"
            label="审计类型"
            prop="sjlxName"
            show-overflow-tooltip
          >
            <!-- <template #default="{ row }">
              {{ row.projectType == 1 ? '计划内' : '归档' }}
            </template> -->
          </el-table-column>
          <el-table-column
            v-if="item.name === '计划开始时间'"
            align="center"
            label="计划开始时间"
            prop="planStarttime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划结束时间'"
            align="center"
            label="计划结束时间"
            prop="planEndtime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划年度'"
            align="center"
            label="计划年度"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '项目费用估算(元)'"
            align="center"
            label="项目费用估算(元)"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '审计方式'"
            align="center"
            label="审计方式"
            prop="auditMethod"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.auditMethod == 1
                  ? '现场'
                  : row.examineType == 2
                  ? '现场'
                  : '非现场与现场结合'
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="fpStatus"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.fpStatus == '0'
                  ? '未启动'
                  : row.fpStatus == '1'
                  ? '分配中'
                  : row.fpStatus == '2'
                  ? '已分配'
                  : '启动'
              }}
            </template>
          </el-table-column>
          <!-- <el-table-column
            v-if="item.name === '项目状态'"
            align="center"
            label="项目状态"
          >
            <template #default="{ row }">
              {{
                row.status == 1 ? '启动' : row.status == 2 ? '实施' : '未启动'
              }}
            </template>
          </el-table-column> -->
        </div>
        <el-table-column align="center" label="操作" width="240">
          <template #default="{ row }">
            <el-button type="text" @click="taskAllocation(row)">
              任务分配
            </el-button>
            <!-- <el-button type="text" @click="startUp(row)">启动</el-button> -->
            <!-- <el-button type="text" @click="personMaintain(row)">
              人员维护
            </el-button> -->
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

    <taskAllocation2
      v-if="renwuStatue2"
      ref="allocation2"
      @close="close"
    ></taskAllocation2>
    <person-maintain ref="maintain"></person-maintain>
  </div>
</template>

<script>
  import { getProjectListXmgl, projectStart } from '@/oapi/audit/project'
  import { doDelete } from '@/oapi/table'
  import { UTCformat } from '@/utils'
  import personMaintain from './components/formComponents/PersonMaintain.vue'
  import taskAllocation from './components/formComponents/taskAllocation.vue'
  import taskAllocation2 from './components/formComponents/taskAllocation2.vue'
  import IndexEdit from './components/IndexEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      IndexEdit,
      taskAllocation,
      taskAllocation2,
      personMaintain,
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
          prjoectName: undefined,
          // auditedObject: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        renwuStatue: false,
        renwuStatue2: false,
        filedAll: [
          { name: '项目名称' },
          { name: '项目经理' },
          { name: '审计类型' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '计划年度' },
          { name: '项目费用估算(元)' },
          { name: '审计方式' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-project-task-search',
        tableKey: 'oilAudit-project-task-list',
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
          // { name: '项目对象', key: 'auditedObject' },
        ]
      },
      close() {
        this.renwuStatue = false
        this.renwuStatue2 = false
        this.fetchData()
      },
      async startUp(row) {
        let res = await projectStart({ projectid: row.id })
        if (res.code === 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        }
        this.fetchData()
      },
      taskAllocation(row) {
        console.log('')
        if (row.isgc != 1) {
          this.renwuStatue = true
          this.$nextTick(() => {
            this.$refs['allocation'].showEdit(row)
          })
        } else {
          if (row.xmqd) {
            this.renwuStatue2 = true
            this.$nextTick(() => {
              this.$refs['allocation2'].showEdit(row)
            })
          }
        }
      },
      personMaintain(row) {
        this.$refs['maintain'].showEdit(row)
      },
      resetQueryForm() {
        this.queryForm = {
          prjoectName: undefined,
          auditedObject: undefined,
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        // this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getProjectListXmgl(this.queryForm)
        this.list = tlist.map((v) => {
          v.endDate = UTCformat(v.endDate)
          v.startDate = UTCformat(v.startDate)
          return v
        })

        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, true)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
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
</style>
