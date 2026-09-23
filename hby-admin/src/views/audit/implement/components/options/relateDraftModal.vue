<template>
  <div class="system-log-container">
    <el-dialog
      v-if="dialogFormVisible"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="底稿编号"
          prop="sheetCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.sheetCode }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="底稿名称" prop="sheetName" />
        <!-- <el-table-column
          align="center"
          label="审计目标"
          prop="sheetTarget"
          show-overflow-tooltip
        /> -->
        <el-table-column
          align="center"
          label="被审计对象"
          prop="orgIdNames"
          show-overflow-tooltip
        >
          <!-- <template>
            {{
              projectInfo.orgIdNames
                ? projectInfo.auditStaffName
                : projectInfo.auditOrgName
            }}
          </template> -->
        </el-table-column>
        <el-table-column
          align="center"
          label="拟稿人"
          prop="realname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="审批人"
          prop="approver"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.yjfh && row.ejfh ? row.yjfh + ',' + row.ejfh : '' }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="拟稿日期"
          prop="createTime"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column
          align="center"
          label="状态"
          prop="state"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{
              row.state == 1
                ? '审批中'
                : row.state == 2
                ? '已退回'
                : row.state == 3
                ? '已撤回'
                : row.state == 4
                ? '已终止'
                : row.state == 5
                ? '已跟踪'
                : row.state == 6
                ? '已完成'
                : '未审批'
            }}
          </template>
        </el-table-column>
        >
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </el-dialog>
    <MyDraftInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>
<script>
  import { getRelateDraftModalList, myDraftDetail } from '@/api/audit/implement'
  import MyDraftInfo from '../myDraftInfo.vue'
  import { formatDate } from '@/utils/index'
  export default {
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        dialogFormVisible: false,
        title: '查看',
        info: '',
        projectId: '',
      }
    },
    components: {
      MyDraftInfo,
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
      async fetchData(info) {
        this.listLoading = true
        const params = {
          ...this.queryForm,
          operateid: this.info,
        }
        if (
          this.projectId !== '' &&
          this.projectId !== null &&
          this.projectId !== undefined
        ) {
          params.projectId = this.projectId
        }
        const {
          data: {
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getRelateDraftModalList(params)
        this.projectInfo = project
        this.list = list
        this.total = total
        this.listLoading = false
      },
      showEdit(info, projectId) {
        this.dialogFormVisible = true
        this.info = info
        this.projectId = projectId
        this.queryForm.pageNumber = 1
        this.fetchData(info)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await myDraftDetail({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.projectId = ''
      },
    },
  }
</script>
