<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form class="margin-b0">
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
              v-model="queryForm.projectName"
              clearable
              placeholder="审计项目名称"
              :style="{ width: '256px' }"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.bsusinessAffiliation"
              clearable
              placeholder="审计事项"
              :style="{ width: '256px' }"
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      ref="multipleTable"
      tooltip-effect="dark"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="关联工作底稿编号"
        prop="sheetCode"
        show-overflow-tooltip
        width="160"
      />
      <el-table-column
        align="center"
        label="审计事项"
        prop="businessAffiliation"
        width="100"
        show-overflow-tooltip
      ></el-table-column>

      <el-table-column
        align="center"
        label="审计项目名称"
        show-overflow-tooltip
        prop="projectName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="被审计单位"
        show-overflow-tooltip
        prop="orgIdNames"
        width="220"
      ></el-table-column>
      <el-table-column
        align="center"
        label="审计发现"
        prop="auditDiscoverable"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="发现人"
        prop="realname"
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
  </el-dialog>
</template>

<script>
  import { summaryAuditList } from '@/api/zgzz/index.js'
  export default {
    name: 'modal',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        title: '关联审计',
        queryForm: {
          projectName: '',
          findPeople: '',
          realname: '',
          recStatus: '',
          status: '',
          bsusinessAffiliation: '',
          pageNumber: 1,
          pageSize: 10,
        },
        list: [],
        current: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
      }
    },
    methods: {
      async showEdit() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        const {
          data: { tlist, totalRecord },
        } = await summaryAuditList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          findPeople: '',
          realname: '',
          recStatus: '',
          status: '',
          bsusinessAffiliation: '',
          pageNumber: 1,
          pageSize: 10,
          projectName: '',
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

      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.list = []
        this.footer = true
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        if (this.current.length == 0) {
          this.$baseMessage(
            '请选择审计项目！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selectList', this.current)
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
