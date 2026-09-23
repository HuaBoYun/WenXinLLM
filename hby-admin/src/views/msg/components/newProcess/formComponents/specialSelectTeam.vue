<template>
  <el-dialog
    append-to-body
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    v-if="dialogVisible"
    @close="close"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
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
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column prop="orgname" label="所属部门"></el-table-column>
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
  import {
    findOrganizationByTreeAllss,
    selectPerson,
  } from '@/api/audit/project'
  import { getStaffPageList } from '@/api/setting/personnel'

  export default {
    components: {},
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        flagTitle: false,
        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    methods: {
      close() {
        this.dialogVisible = false
        this.queryForm = {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      showEdit(flag) {
        if (flag == 'leader') {
          this.flagTitle = true
        } else {
          this.flagTitle = false
        }
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorTree()
        this.getExecutorList()
      },
      async getExecutorTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
        const aa = {
          id: -2,
          isParent: false,
          name: '已经入库审计人员',
          children: [],
          open: true,
          pId: -2,
          target: 'mainFramex',
          url: 'xxxxxx',
        }
        res[0].children.unshift(aa)
        this.dataTree = res
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleNodeClick(val) {
        if (val.id === -2) {
          const orgId = JSON.parse(localStorage.getItem('userInfo')).linkDetp
            .orgid

          const {
            data: {
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = await getStaffPageList({
            pageNo: 1,
            pageSize: 20,
            realName: '',
            education: '',
            jobExperiences: '',
            jobName: '',
            major: '',
            orgId,
          })
          const info = list.map((item) => {
            return { ...item, orgName: '已经入库审计人员' }
          })
          this.list = info
          this.total = total
        } else {
          this.queryForm.orgid = val.id
          this.getExecutorList()
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        if (this.flagTitle) {
          if (val.length > 1) {
            let del = val.shift()
            this.$refs.multipleTable.toggleRowSelection(del, false)
          }
          this.multipleSelection = val
        } else {
          this.multipleSelection = val
        }
      },
      save() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selectTeamList', this.multipleSelection, this.flagTitle)
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
