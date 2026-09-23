<template>
  <el-dialog
    title="人员"
    :visible.sync="dialogVisible"
    width="50%"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="teamName" label="小组名称"></el-table-column>
      <el-table-column prop="staff.realname" label="成员"></el-table-column>
      <el-table-column prop="stafftype" label="角色名称" width="120">
        <template slot-scope="scope">
          {{
            scope.row.stafftype == 1
              ? '组员'
              : scope.row.stafftype == 3
              ? '项目主审'
              : '组长'
          }}
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" label="创建人"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
    </el-table>
  </el-dialog>
</template>
<script>
  import { getPjteamUsrList2, manageSave } from '@/api/audit/project'
  import { UTCformat } from '@/utils'

  export default {
    data() {
      return {
        dialogVisible: false,
        list: [],
        multipleSelection: [],
        projectId: '',
        operateidStr: '',
        id: '',
        currentFlag: false,
        type: '',
      }
    },
    methods: {
      showEdit(multipleSelection, projectId, type) {
        this.projectId = projectId

        this.getNbsjTempleteListFun()
        this.dialogVisible = true
        this.type = type
      },
      async getNbsjTempleteListFun() {
        this.listLoading = true
        let res = await getPjteamUsrList2({
          projectid: this.projectId,
        })
        this.list = res.data.listTeamZY
        this.list.forEach((item) => {
          item.createTime = UTCformat(item.createTime)
        })

        this.listLoading = false
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }

        this.multipleSelection = val
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择人员！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManage', this.multipleSelection, this.type)
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
