<template>
  <el-dialog
    title="经济责任审计结果报告"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-top-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.document"
              clearable
              placeholder="文号"
            />
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.title" clearable placeholder="标题" />
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- <el-table-column align="center" label="序号" type="index" /> -->
      <el-table-column align="center" label="序号" type="index" />
      <el-table-column align="center" label="文号" prop="document">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetailZ(row)">
            {{ row.document }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="标题" prop="title" />
      <el-table-column
        align="center"
        label="项目名称"
        prop="projectName"
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
    <!-- <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template> -->
    <jjzrsjjgbgView ref="table7View" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import {
    jjzrsjjgbgList,
    jjzrsjjgbgDelete,
    reportExport,
  } from '@/oapi/audit/report'
  import jjzrsjjgbgView from './jjzrsjjgbgView.vue'

  export default {
    components: {
      jjzrsjjgbgView,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          document: '',
          title: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    methods: {
      handleDetailZ(row) {
        this.$refs['table7View'].showEdit(row, 'detail')
      },
      async showEdit(row) {
        this.fetchData()
        this.dialogVisible = true
      },
      resetQueryForm() {
        this.queryForm = {
          document: '',
          title: '',
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
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await jjzrsjjgbgList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      async save() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择一条数据')
          return
        }
        this.$emit('seTtable', this.multipleSelection)
        this.dialogVisible = false
      },
      close() {
        this.resetQueryForm()
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
