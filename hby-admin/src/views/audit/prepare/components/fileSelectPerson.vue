<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      width="800px"
      title="下发人员"
      :close-on-click-modal="false"
      v-if="dialogVisible"
      :modal="false"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button type="success" @click="close">取消</el-button>
          <el-button type="success" @click="handleSubmit">确定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="teamName" label="小组名称"></el-table-column>
        <el-table-column prop="staff.realname" label="成员"></el-table-column>
        <el-table-column prop="stafftype" label="角色名称">
          <template slot-scope="scope">
            {{ scope.row.stafftype == 1 ? '组员' : '组长' }}
          </template>
        </el-table-column>
        <el-table-column prop="createUserName" label="创建人"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
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
    </el-dialog>
  </div>
</template>
<script>
  import { getCurrentProjectData } from '@/api/audit/home'
  import { saveFilePerson } from '@/api/audit/preparation'
  import { getPjteamUsrList } from '@/api/audit/project'
  import { UTCformat } from '@/utils'
  export default {
    data() {
      return {
        listLoading: false,
        dialogVisible: false,
        list: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        select: [],
        fileId: [],
      }
    },
    created() {},
    methods: {
      showEdit(info) {
        this.fileId = info
        this.dialogVisible = true
        this.getStartProject()
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
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.staffid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        // selection.shift()
        this.select = selection.map((item) => item.staffid)
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.list.forEach((item) => {
          this.select.forEach((id) => {
            if (item.staffid === id) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      getStartProject() {
        getCurrentProjectData().then((res) => {
          const id = res.data.pj.projectId
          getPjteamUsrList({ projectid: id }).then((res) => {
            this.list = res.data.listTeamZY
            this.list.forEach((item) => {
              item.createTime = UTCformat(item.createTime)
            })
          })
        })
      },
      handleSubmit() {
        saveFilePerson({
          attids: this.fileId.toString(),
          staffids: this.select.toString(),
        }).then((res) => {
          if ((res.msg = '成功')) {
            this.$message({
              type: 'success',
              message: '成功!',
            })
            this.dialogVisible = false
            this.select = []
          }
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogVisible = false
        this.fileId = []
        this.select = []
      },
    },
  }
</script>
