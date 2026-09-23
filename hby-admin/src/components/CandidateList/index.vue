<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <div>
      <vab-query-form>
        <vab-query-form-left-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.keyword"
                clearable
                placeholder="用户名"
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
        </vab-query-form-left-panel>
        <vab-query-form-right-panel>
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>

        <!-- <el-table-column align="center" label="头像" prop="headIcon" /> -->
        <el-table-column align="center" label="姓名" prop="fullName" />
        <!-- <el-table-column align="center" label="所属部门" prop="organize" /> -->
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.currentPage"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </el-dialog>
</template>
<script>
  import { getCandidatesList } from '@/api/setting/system'
  export default {
    name: 'CandidateList',
    props: {},
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          keyword: '',
          tableId: '',
          fromId: '',
          nodeCode: '',
          currentPage: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    created() {},
    methods: {
      show(candidateData) {
        if (candidateData) {
          this.queryForm.tableId = candidateData.tableId || ''
          this.queryForm.flowId = candidateData.flowId || ''
          this.queryForm.fromId = candidateData.fromId
          this.queryForm.nodeCode = candidateData.nodeId
          this.queryData()
        }
        this.dialogFormVisible = true
      },
      resetQueryForm() {
        this.queryForm.keyword = ''
        this.queryForm.currentPage = 1
        this.queryForm.pageSize = 20
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, totalCount },
        } = await getCandidatesList(this.queryForm)
        this.list = list
        this.total = totalCount
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.currentPage = val
        this.fetchData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      confirm() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择候选人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
