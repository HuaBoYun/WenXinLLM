<template>
  <el-dialog
    title="项目角色-列表"
    :visible.sync="dialogVisible"
    width="50%"
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
      <el-table-column prop="stafftype" label="角色名称">
        <template slot-scope="scope">
          {{ scope.row.stafftype == 1 ? '组员' : '组长' }}
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" label="创建人"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
    </el-table>
  </el-dialog>
</template>
<script>
  import { getPjteamUsrList, manageSave } from '@/api/audit/project'
  import template from '@/views/contract/contractManage/template.vue'
  import { UTCformat } from '@/utils'

  export default {
    components: { template },
    data() {
      return {
        dialogVisible: false,
        list: [],
        multipleSelection: [],
        projectId: '',
        operateidStr: '',
        id: '',
        currentFlag: false,
      }
    },
    methods: {
      showEdit(multipleSelection, projectId) {
        this.projectId = projectId

        let arr = []
        multipleSelection.forEach((item) => {
          arr.push(item.programid)
        })
        this.operateidStr = arr.join(',')

        this.getNbsjTempleteListFun()
        this.dialogVisible = true
      },
      async getNbsjTempleteListFun() {
        this.listLoading = true
        let res = await getPjteamUsrList({
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
        if (this.multipleSelection) {
          this.currentFlag = true
          this.id = val[0].id
        }
      },
      async save() {
        if (this.currentFlag) {
          let res = await manageSave({
            ids: this.operateidStr,
            projectId: this.projectId,
            teamStaffId: this.id,
          })
          if (res.code == 1) {
            this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
            this.$emit('getExecutorList')
            this.dialogVisible = false
          } else {
            this.$baseMessage('分配失败', 'error', 'vab-hey-message-error')
          }
        } else {
          this.$baseMessage(
            '请选择分配人员！',
            'error',
            'vab-hey-message-error'
          )
        }
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
