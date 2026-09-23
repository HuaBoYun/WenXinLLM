<template>
  <el-dialog
    title="选择项目"
    :visible.sync="dialogVisible"
    width="1400px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
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
                v-model="queryForm.prjoectName"
                clearable
                placeholder="项目名称"
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
        </vab-query-form-top-panel>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        @select="handleSelection"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="项目名称"
          prop="prjoectName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="项目编号"
          prop="projectCode"
        ></el-table-column>
        <el-table-column align="center" label="项目来源" prop="projectSource" />
        <el-table-column align="center" label="计划开始时间" prop="startDate" />
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
  import { loadProjectData } from '@/api/audit/structure'
  import { UTCformat } from '@/utils'
  export default {
    components: {},
    name: 'xxx',
    props: ['personId'],
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
          staffid: this.personId.auditors,
        },
        staffid: '',
        listLoading: false,
        list: [],
        dialogVisible: false,
        multipleSelection: [],
        current: undefined,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const { ...other } = this.queryForm
        const {
          data
          // data: {
          //   pageInfo: { tlist: list, totalRecord: total },
          // },
        } = await loadProjectData({ ...other })
        console.log(data)
        this.list = data.pageInfo? data.pageInfo.tlist : []
        this.list.forEach((item) => {
          item.startDate = UTCformat(item.startDate)
        })
        this.total = data.pageInfo? data.pageInfo.totalRecord : 0
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        // this.resetQueryForm()
        ;(this.queryForm = {
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
          staffid: this.staffid,
        }),
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
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      showEdit(id) {
        this.staffid = id
        this.queryForm.staffid = id
        this.dialogVisible = true
        this.current = undefined
        this.fetchData()
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {
        if (!this.current) {
          this.$baseMessage('请选择项目', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('handleProjectInfo', this.multipleSelection)
        this.dialogVisible = false
      },
      handleSelection(val) {
        this.current = val
        // if (this.flagTitle) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
        // } else {
        //   this.multipleSelection = val
        // }
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
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
