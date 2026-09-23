<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :append-to-body="true"
    v-if="dialogVisible"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
            <!-- <vab-query-form-left-panel>
              <el-input
                v-model="queryForm.realname"
                placeholder="请输入用户名"
                clearable
                style="width: 50%; margin-right: 10px"
              />
              <el-button
                type="primary"
                @click="getExecutorList"
                style="margin-top: 10px !important"
              >
                查询
              </el-button>
              <el-button
                type="primary"
                @click="reset"
                style="margin-top: 10px !important"
              >
                重置
              </el-button>
            </vab-query-form-left-panel> -->
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            style="width: 100%"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              label="审计项目名称"
              prop="projectName"
            ></el-table-column>
            <el-table-column
              prop="projectType"
              label="项目类型"
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
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import { getPlanData } from '@/api/oilAudit/jhgl/lxjyzypg'

  export default {
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      const curOrgId = userInfo.currentOrg.orgid
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curOrgId,
        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        queryForm: {
          status: 6,
          pageNumber: 1,
          pageSize: 20,
          projectType: '',
        },
        current: undefined,
        reviewType: '',
        select: [],
      }
    },
    methods: {
      showEdit(type) {
        this.queryForm.projectType = type
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorList()
        this.select = []
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getPlanData(this.queryForm)
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
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.getExecutorList()
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择项目！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.select)
        this.dialogVisible = false
      },
      reset() {
        this.queryForm.realname = ''
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
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
