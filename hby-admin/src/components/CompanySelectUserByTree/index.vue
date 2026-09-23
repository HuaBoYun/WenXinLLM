<!-- 选人组件 -->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="data"
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
          <vab-query-form-left-panel :span="16">
            <el-select
              v-if="isUserName"
              v-model="userId"
              multiple
              style="width: 400px"
              @remove-tag="removeTag"
            >
              <el-option
                v-for="item in userList"
                :key="item.staffid"
                :label="item.realname"
                :value="item.staffid"
              ></el-option>
            </el-select>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="8">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <vab-query-form>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="姓名"
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
        </vab-query-form>

        <el-table
          ref="multipleTable"
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
          @selection-change="handleSelectionChange"
          @select="onSelect"
        >
          <el-table-column
            v-if="isUserName"
            type="selection"
            width="55"
          ></el-table-column>

          <el-table-column align="center" label="用户真实名" prop="realname" />
          <el-table-column
            v-if="isUserName"
            align="center"
            label="真实姓名"
            prop="username"
          />
          <el-table-column
            v-else
            align="center"
            label="所属部门"
            prop="orgname"
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
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import {
    getExecutorOptionsList,
    getExecutorOptionsTree,
    getUserList,
  } from '@/api/contract/manage'
  export default {
    name: 'ExecutorOptions',
    props: {
      isUserName: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          realname: '',
          orgid: undefined,
          status: 1,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        userId: [],
        userList: [],
        orgid: undefined,
        val: [],
      }
    },
    created() {},
    mounted() {},
    methods: {
      async show(list) {
        this.current = undefined
        this.dialogFormVisible = true
        await this.getExecutorTree()
        await this.getExecutorList()
        if (list && list.length > 0) {
          this.userList = list
          this.userId = list.map((item) => item.staffid)
          await this.setSelection(list)
          this.val = []
        }
      },
      setSelection(list) {
        this.$nextTick(() => {
          list.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.staffid == item.staffid
              }),
              true
            )
          })
        })
      },
      async getExecutorTree() {
        const res = await getExecutorOptionsTree(this.queryForm)
        this.data = res
      },
      fetchData() {
        this.getExecutorList()
      },
      resetSearch() {
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.queryForm.realname = ''
        this.getExecutorList()
      },
      async getExecutorList() {
        try {
          this.listLoading = true
          const response = await getUserList(this.queryForm)
          // 后端返回格式: { code: 1, msg: "成功", data: { total: 118, list: [...] } }
          this.list = response.data?.list || []
          this.total = response.data?.total || 0
        } finally {
          this.listLoading = false
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
      async handleNodeClick(val) {
        this.queryForm.pageNumber = 1
        this.queryForm.orgid = val.id
        await this.getExecutorList()
        await this.setSelection(this.userList)
      },
      handleSelected(val) {
        if (this.isUserName) return
        this.current = val
        // this.dialogFormVisible = false
      },
      //两数组取不相同
      getNewArr(a, b) {
        const arr = [...a, ...b]
        const newArr = arr.filter((item) => {
          return !(a.includes(item) && b.includes(item))
        })
        return newArr
      },
      handleSelectionChange(val) {
        if (!this.isUserName) return
        // let list = []

        // list = this.userList
        // let tList = []
        // list = list.map((item) => {
        //   if (item) {
        //     tList.push(item)
        //   }
        // })

        // tList = deWeight2(tList)

        // this.userList = tList
        // this.userId = tList.map((item) => item.staffid)
        // this.current = tList
        // this.$refs.multipleTable.clearSelection();
        // this.setSelection(list)
      },
      onSelect(rows, row) {
        if (!this.isUserName) return
        let selected = rows.length && rows.indexOf(row) !== -1

        let list = this.userList || []
        if (selected) {
          list.push(row)
        } else {
          list = list.filter((item) => item.staffid !== row.staffid)
        }
        this.userId = list.map((item) => item.staffid)
        this.userList = list
        this.$forceUpdate()
      },
      async removeTag(e) {
        let list = this.userList
        let id = this.userId
        list = await list.filter((item) => item && item.staffid != e)
        id = await id.filter((item) => item !== e)
        this.userList = list
        this.userId = id
        this.$forceUpdate()
        await this.$refs.multipleTable.clearSelection()
        await this.setSelection(list)
      },
      confirm() {
        if (this.isUserName) {
          if (this.userList.length < 0) {
            this.$baseMessage(
              this.isUserName ? '请选评估人员！' : '请选择执行人！',
              'error',
              'vab-hey-message-error'
            )
            return
          }
          this.$emit('selected', this.userList)
          this.userList = []
          this.userId = []
          this.dialogFormVisible = false
        } else {
          if (!this.current) {
            this.$baseMessage(
              this.isUserName ? '请选评估人员！' : '请选择执行人！',
              'error',
              'vab-hey-message-error'
            )
            return
          }
          this.$emit('selected', this.current)
          this.current = undefined
          this.dialogFormVisible = false
        }
      },
      close() {
        this.userList = []
        this.userId = []
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .left /deep/ .el-tree {
    overflow: auto;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
