<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="900px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <!-- <div class="lr-layout"> -->
      <vab-query-form>
        <vab-query-form-left-panel :span="18">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.projectcode"
                clearable
                placeholder="项目编码"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.projectname"
                clearable
                placeholder="项目名称"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="getExecutorList"
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
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        ref="table"
        :data="list"
        tooltip-effect="dark"
        @selection-change="handleSelection"
        style="width: 100%"
        key="code"
      >
        <el-table-column type="selection" width="100"></el-table-column>
        <el-table-column align="center" label="项目编码" prop="projectcode" />
        <el-table-column
          align="center"
          label="项目名称"
          prop="projectname"
        ></el-table-column>
        <el-table-column
          align="center"
          label="承办人"
          prop="undertakestaff.realname"
        />
        <el-table-column
          align="center"
          label="创建时间"
          prop="createtime"
          :formatter="formatDate"
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
      <!-- </div> -->
    </div>
  </el-dialog>
</template>
<script>
  import { formatDate } from '@/utils/index'
  import { contractProList } from '@/api/contract/project'

  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
    },
    components: {},
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dialogVisible: false,
        list: [],
        multipleSelection: [],
        queryForm: {
          projectname: '',
          projectcode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        reviewType: '',
      }
    },
    methods: {
      resetSearch() {
        this.queryForm.projectname = ''
        this.queryForm.projectcode = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.getExecutorList()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      showEdit(e) {
        if (e) {
          this.reviewType = e
        }
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorList()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await contractProList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleSelection(val) {
        // 单选
        if (val.length > 1) {
          this.$refs.table.clearSelection()
          this.$refs.table.toggleRowSelection(val.pop())
        }
        this.multipleSelection = val
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (this.multipleSelection.length === 0) {
          this.$baseMessage(
            '请选择项目编码！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        console.log(this.multipleSelection, 'getProjectCode')
        this.$emit('getProjectCode', this.multipleSelection)
        this.dialogVisible = false
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
</style>
