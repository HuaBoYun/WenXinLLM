<template>
  <el-dialog
    title="被审计单位"
    :visible.sync="dialogVisible"
    width="1400px"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <company-tree ref="leftList" @select="handleTreeSelect" />
        </div>
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button type="primary" @click="saveTree">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
            <vab-query-form-left-panel>
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
            </vab-query-form-left-panel>
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            ref="multipleTable"
            :data="list"
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
  import { orgList } from '@/api/audit/implement'
  import {
    findOrganizationByTreeAllss,
    selectPerson,
  } from '@/api/audit/project'
  import CompanyTree from '@/components/CompanyTree.vue'
  export default {
    components: { CompanyTree },
    data() {
      return {
        listLoading: false,
        selectTree: {},
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',

        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
          isLeaf: 'isParent',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: undefined,
        },
        current: undefined,
        currentTree: undefined,
        select: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.current = undefined
        this.currentTree = undefined
        this.select = []
        this.queryForm.realname = undefined
        this.getExecutorList()
      },
      handleTreeSelect(v) {
        this.queryForm.orgid = v.id
        this.selectTree = v
        this.currentTree = v
        this.queryForm.pageNumber = 1
        this.getExecutorList()
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

      handleNodeClick(val) {
        this.currentTree = val
        this.selectTree = val
        this.queryForm.pageNumber = 1
        this.queryForm.orgid = val.id
        this.getExecutorList()
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
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
      },
      save() {
        if (!this.select) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.select, 'right')
        this.dialogVisible = false
      },
      saveTree() {
        if (!this.currentTree) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.selectTree, 'left')
        this.dialogVisible = false
      },
      reset() {
        this.queryForm.realname = ''
        this.getExecutorList()
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
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 70px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
