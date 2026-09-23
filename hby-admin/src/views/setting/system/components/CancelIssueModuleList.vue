<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogTableVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
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
              v-model="queryForm.orgname"
              clearable
              placeholder="公司名称"
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
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        align="center"
        label="公司编号"
        prop="orgnumber"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="公司名称"
        prop="orgname"
        show-overflow-tooltip
      />
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
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { getCancelIssueModuleList } from '@/api/setting/system'
  export default {
    name: 'CancelIssueList',
    data() {
      return {
        title: '取消下发',
        dialogTableVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          orgname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        moduleId: undefined,
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getCancelIssueModuleList({ moduleId: this.moduleId })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      showTable(row) {
        this.dialogTableVisible = true
        this.moduleId = row.modelId
        this.fetchData()
      },
      close() {
        this.multipleSelection = []
        this.dialogTableVisible = false
      },
      confirm() {
        this.$emit('selected', this.multipleSelection, this.moduleId)
      },
      handleSelectionChange(data) {
        this.multipleSelection = data
      },
    },
  }
</script>
