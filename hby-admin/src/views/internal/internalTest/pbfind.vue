<template>
  <div class="system-log-container">
    <el-card shadow="never">
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
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="一级流程"
          prop="oneprocess"
          show-overflow-tooltip
        />
        <div v-for="item in filedNow" :key="item.findid">
          <el-table-column
            align="center"
            label="问题概述"
            v-if="item.name === '问题概述'"
            prop="problemmemo"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="缺陷具体描述"
            v-if="item.name === '缺陷具体描述'"
            prop="defectmemo"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="问题类别"
            v-if="item.name === '问题类别'"
            prop="problemtype"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="缺陷等级"
            v-if="item.name === '缺陷等级'"
            prop="defectlevel"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="定性依据"
            v-if="item.name === '定性依据'"
            prop="quabasis"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="主责部门"
            v-if="item.name === '主责部门'"
            prop="orgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="反馈意见"
            v-if="item.name === '反馈意见'"
            prop="feedback"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="整改计划"
            v-if="item.name === '整改计划'"
            prop="reformplan"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="预计完成时间"
            v-if="item.name === '预计完成时间'"
            prop="estfinishdate"
            show-overflow-tooltip
            #default="{ row }"
          >
            {{ formatDate(row.estfinishdate) }}
          </el-table-column>
          <el-table-column
            align="center"
            label="整改落实人"
            v-if="item.name === '整改落实人'"
            prop="realname"
            show-overflow-tooltip
          />
          <el-table-column
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
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="140">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :disabled="!!row.status"
                  @click.native="handleApproval(row)"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.status"
                  @click.native="handleIssueDelete(row)"
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
    <issueView ref="issue" @fetch-data="queryData" />
  </div>
</template>

<script>
  import { testtaskProfindList, testtaskProfindDel } from '@/api/internal/tack'
  import { candidates, submitByYmWork } from '@/api/setting/system'
  import filterTable from '@/components/filterTable.vue'
  import { formatDay } from '@/utils/index'
  import issueView from '@/views/internal/internalTest/components/issueView.vue'

  export default {
    name: 'pbfind',
    components: { filterTable, issueView },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '问题概述' },
          { name: '缺陷具体描述' },
          { name: '问题类别' },
          { name: '缺陷等级' },
          { name: '定性依据' },
          { name: '主责部门' },
          { name: '反馈意见' },
          { name: '整改计划' },
          { name: '预计完成时间' },
          { name: '整改落实人' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        tableKey: 'internal-internalTest-pbfind-list',
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
    },
    methods: {
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
      formatDate(date) {
        // 获取单元格数据
        return formatDay(date)
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
            pageBean: { records, total },
          },
        } = await testtaskProfindList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleApproval(row) {
        //提交审批
        this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
          const { data, code, msg } = await candidates({
            tableId: 47,
            fromId: row.findid,
          })

          if (data && data.candidateType) {
            const res = await submitByYmWork({
              tableId: 47,
              fromId: row.findid,
              candidateType: data.candidateType,
            })

            if (res.code === 1) {
              this.$message.success(res.msg)
              this.queryData()
            }
          }
        })
      },
      handleAdd() {
        this.$refs.issue.show({}, 'add')
      },
      handleEdit(row) {
        this.$refs.issue.show(row, 'edit')
      },
      handleIssueDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await testtaskProfindDel({ findid: row.findid })
          if (code == 200) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            this.queryData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
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
  .pagination {
    margin-bottom: 20px !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }
</style>
