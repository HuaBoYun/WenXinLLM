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
      <div class="right">
        <vab-query-form>
          <vab-query-form-left-panel :span="16">
            <!-- <el-select
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
            </el-select> -->
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="8">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          ref="multipleTable"
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            v-if="isUserName"
            type="selection"
            width="55"
          ></el-table-column>

          <el-table-column align="center" label="用户姓名" prop="realname" />
          <el-table-column align="center" label="用户名" prop="username" />
          <el-table-column align="center" label="所属部门" prop="orgname" />
          <el-table-column align="center" label="审批角色" prop="jobName" />
          <!-- <el-table-column
            v-if="isUserName"
            align="center"
            label="真实姓名"
            prop="username"
          />
          <el-table-column
            v-else
            align="center"
            label="所属部门"
            prop="manageorgnames"
          /> -->
        </el-table>
        <!-- <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        /> -->
      </div>
    </div>
  </el-dialog>
</template>
<script>
import {
  copyApprovalStaffList,
} from '@/api/contract/manage'
import { deWeight2 } from '@/utils'
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
        processId: undefined,
        pageNumber: 1,
        pageSize: 100,
      },
      current: undefined,
      userId: [],
      userList: [],
      pid: 1,
    }
  },
  created() {},
  mounted() {},
  methods: {
    async show(list,processId) {
      this.current = undefined
      this.queryForm.processId = processId
      this.dialogFormVisible = true
      await this.getExecutorList()
      if(list&&list.length>0){
        this.userList = list
        this.userId = list.map((item) => item.staffid)
        await this.setSelection(list)
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
    async getExecutorList() {
      this.listLoading = true
      const {
        data: {
          staffList: tlist
        },
      } = await copyApprovalStaffList(this.queryForm)

      this.list = tlist
      // this.total = totalRecord
      this.listLoading = false
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
      this.queryForm.pid = val.id
      await this.getExecutorList()
      await this.setSelection(this.userList)
    },
    handleSelected(val) {
      if (this.isUserName) return
      this.current = val
      // this.dialogFormVisible = false
    },
    handleSelectionChange(val) {
      if (!this.isUserName) return
      let list = []
      if (!this.pid) {
        list = val
      } else {
        list = this.userList
        list = list.concat(val)
        let tList = []
        list = list.map((item) => {
          if (item) {
            tList.push(item)
          }
        })
        list = deWeight2(tList)
      }
      this.userList = list
      this.userId = list.map((item) => item.staffid)
      this.current = val
      // this.$refs.multipleTable.clearSelection();
      // this.setSelection(list)
    },
    removeTag(e) {
      let list = this.userList
      let id = this.userId
      list = list.filter((item) => item.staffid !== e)
      id = id.filter((item) => item !== e)
      this.userList = list
      this.userId = id
      this.$refs.multipleTable.clearSelection()
      this.setSelection(list)
    },
    confirm() {
      if (!this.current) {
        this.$baseMessage(
          this.isUserName ? '请选评估人员！' : '请选择执行人！',
          'error',
          'vab-hey-message-error'
        )
        return
      }
      let list = []
      this.current.forEach((item) => {
        if (item) {
          list.push(item)
        }
      })
      this.$emit('selected', list)
      this.dialogFormVisible = false
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

.lr-layout > .right {
  width: 100%;
}
</style>
