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
          prop="draftNumber"
          width="170"
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
        />
        <el-table-column
          align="center"
          label="审计项目名称"
          prop="projectName"
        />

        <el-table-column align="center" label="审计事项" prop="auditMatters" />
        <el-table-column
          align="center"
          label="被审计单位名称"
          prop="auditeeName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
        ></el-table-column>
      </el-table>
      <!-- <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      /> -->
    </el-dialog>
    <MyDraftInfo ref="edit"></MyDraftInfo>
  </div>
</template>
<script>
  import { getMyManuscriptListByType } from '@/oapi/audit/implement'
  import { formatDate } from '@/utils/index'
  import MyDraftInfo from '@/views/oilAudit/implement/components/newMyDraftView.vue'
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
        projectInfo: {},
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
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async fetchData(info) {
        this.listLoading = true
        const { data } = await getMyManuscriptListByType({ typeId: info })
        this.list = data.MyManuscriptList
        // this.total = total
        this.listLoading = false
      },
      showEdit(info) {
        this.dialogFormVisible = true
        this.fetchData(info)
      },
      handleAddOrUpdate(row, disabled) {
        this.$refs['edit'].showModal(row, false, disabled)
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
