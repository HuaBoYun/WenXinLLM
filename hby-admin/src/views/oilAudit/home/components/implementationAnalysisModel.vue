<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >
    <!-- <el-form
      ref="form"
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input v-model="queryForm.code" clearable placeholder="问题编号" />
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
    </el-form> -->

    <el-table :data="list">
      <el-table-column 
            align="center"
            label="项目编号"
            prop="qdcode"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.qdcode }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column 
            align="center"
            label="审计项目名称"
            prop="xmname"
          >
            <!-- <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.xmname }}
              </el-button>
            </template> -->
          </el-table-column>
          <el-table-column
            align="center"
            label="审计项目类型"
            prop="xmtype" 
          />
          <el-table-column
            align="center"
            label="被审计单位"
            prop="borgname" 
          ></el-table-column>
          <el-table-column
            align="center"
            label="项目经理"
            prop="xmjlname" 
          ></el-table-column>
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

    <Views ref="edit" @fetchData="fetchData"></Views>
  </el-dialog>
</template>
<script>
 import {
  getplanAnalysisXmList, 
} from '@/oapi/audit/plan' 
import Views from '@/views/oilAudit/plan/components/xmqdEdit.vue'

  export default {
    name: 'assignTable',
    components: {  Views },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '项目',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        assignInfo: null,
        queryForm: {
          xmnd: undefined,
          dataType: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: { 
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
        } = await getplanAnalysisXmList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleDetail(row) {
      this.$refs['edit'].showEdit(row, 'detail')
    },
      showEdit(row,type) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.xmnd = row.xmnd
          this.queryForm.dataType = type
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
      },
    },
  }
</script>

<style></style>
