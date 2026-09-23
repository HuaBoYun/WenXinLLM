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
                v-model="queryForm.jobName"
                clearable
                placeholder="岗位名称"
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
        highlight-current-row
        @current-change="handleCurrentChange2"
      >
        <!-- <el-table-column align="center" type="selection" width="55" /> -->
        <el-table-column align="center" label="岗位名称" prop="jobname" > 
        </el-table-column>
        <el-table-column align="center" label="公司名称" prop="orgName" > 
        </el-table-column>
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
    </div>
  </el-dialog>
</template>
<script>
  import { jobList  } from '@/api/setting/auth'
  export default {
    name: 'jobList',
    props: {},
    components: {
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          jobName:'',
          orgIds: '',
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    created() {},
    methods: {
      show(orgIds) {
        this.queryForm.orgIds = orgIds
        this.queryData()
        this.dialogFormVisible = true
      },
      handleDetail(row) {
        this.$refs['detail'].showDetail(row)
      },
      resetQueryForm() {
        this.queryForm.jobName = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          pageInfo: { tlist, totalRecord },
        } = await jobList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleCurrentChange2(val){
        this.current = val
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择职位！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
