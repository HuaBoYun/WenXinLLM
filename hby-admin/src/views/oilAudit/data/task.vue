<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <task-tree @getChildParam="fetchDataByTree" />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-top-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.businessType"
                  clearable
                  placeholder="问题单元"
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
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-top-panel>
        </vab-query-form>
        <template>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="问题单元"
              prop="businessType"
              width="100"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.businessType }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="问题类型"
              prop="businessType"
              width="200"
            />
            <el-table-column
              align="center"
              label="审计程序"
              prop="suditProcess"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="完成时间"
              prop="finishtime"
              width="150"
              :formatter="
                (e) => {
                  return dayjs(e.finishtime).format('YYYY-MM-DD')
                }
              "
            />
            <el-table-column
              align="center"
              label="状态"
              prop="finish"
              width="100"
            >
              <template #default="{ row }">
                {{
                  row.finish == 0 ? '未完成' : row.finish == 1 ? '已完成' : ''
                }}
              </template>
            </el-table-column>
          </el-table>
        </template>
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
      <!-- <template v-else>
        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            v-for="item in options"
            :key="item.prop"
            align="center"
            :label="item.label"
            :prop="item.prop"
          />
        </el-table> -->
      <!-- </template> -->
    </div>
    <!-- <TaskTree ref="edit" @fetch-data="fetchData" /> -->
    <record-list-info ref="recordListInfo" />
  </div>
</template>

<script>
  import { getProjectData } from '@/oapi/audit/projectData'
  import { doDelete } from '@/oapi/table'
  import RecordListInfo from '@/views/oilAudit/plan/components/RecordListInfo'
  import * as dayjs from 'dayjs'
  import TaskTree from './components/TaskTree'
  export default {
    name: 'Download',
    components: { TaskTree, RecordListInfo },
    data() {
      return {
        dayjs: dayjs,
        list: [],
        //控制展示一级table
        nodeId: '',
        //离任审计模板
        options: [
          {
            label: '审计目标分类',
            prop: 'type',
            width: '100px',
          },
          {
            label: '任务总数',
            prop: 'type1',
            width: '100px',
          },
          {
            label: '已完成数',
            prop: 'finish',
            width: '100px',
          },
          {
            label: '未完成数',
            prop: 'type3',
            width: '100px',
          },
        ],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          // code: '',
          // businessType: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleIsFrist(e) {
        this.isFrist = e
      },
      resetSearch() {
        this.queryForm = {
          businessType: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      fetchDataByTree(e) {
        this.nodeId = e
        this.$forceUpdate()
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getProjectData({
          ...this.queryForm,
          targetId: this.nodeId,
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleDetail(row) {
        const info = { programId: +row.programid }
        this.$refs['recordListInfo'].showEdit(info, true)
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
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
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
