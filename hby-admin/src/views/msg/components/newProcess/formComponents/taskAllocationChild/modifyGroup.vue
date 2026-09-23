<template>
  <div>
    <el-dialog
      title="项目方案-修改小组"
      :visible.sync="dialogVisible"
      width="40%"
      @close="close"
      :close-on-click-modal="false"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">返 回</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="小组名称" prop="teamName">
          <el-input v-model="ruleForm.teamName"></el-input>
        </el-form-item>
        <el-form-item label="小组组长" prop="leaderName">
          <el-input
            v-model="ruleForm.leaderName"
            style="width: 330px"
            disabled
          ></el-input>
          <el-button
            style="margin-left: 3px"
            @click="showGroupLeader"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="组员" prop="members">
          <el-input
            v-model="ruleForm.members"
            disabled
            style="width: 330px"
          ></el-input>
          <el-button
            style="margin-left: 3px"
            @click="showTeamMembers"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>
  </div>
</template>
<script>
  import selectTeam from '../../formComponents/selectTeam.vue'
  import { projectPjteamList, proPjteamInfo } from '@/api/audit/project'

  export default {
    components: {
      selectTeam,
    },
    data() {
      return {
        ruleForm: {
          teamName: '',
          leaderName: '',
          members: '',
        },
        zyStaffids: 0,
        leaderId: 0,
        projectId: 0,
        dialogVisible: false,
        rules: {
          teamName: [
            { required: true, message: '请输入小组名称', trigger: 'blur' },
          ],
          leaderName: [
            { required: true, message: '请选择组长名称', trigger: 'change' },
          ],
          members: [
            { required: true, message: '请选择组员名称', trigger: 'change' },
          ],
        },
      }
    },

    methods: {
      async showEdit(projectId, row) {
        let res = await proPjteamInfo({
          teamId: row.teamid,
        })
        let arr = []
        res.data.team.teamStaffs.forEach((item) => {
          arr.push(item.staffid)
        })
        this.zyStaffids = arr.join(',')
        this.ruleForm = {
          teamName: res.data.team.teamName,
          leaderName: res.data.team.leaderName,
          members: res.data.team.zyNames,
        }
        this.teamId = row.teamid
        this.projectId = projectId
        this.leaderId = res.data.team.leaderId
        this.dialogVisible = true
      },
      selectTeamList(val, flagTitle) {
        if (flagTitle) {
          this.leaderId = val[0].staffid
          this.ruleForm.leaderName = val[0].realname
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.ruleForm.members = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      showTeamMembers() {
        this.$refs['select'].showEdit('members')
      },
      close() {
        this.$refs['ruleForm'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let res = await projectPjteamList({
              leaderid: this.leaderId,
              projectid: this.projectId,
              teamName: this.ruleForm.teamName,
              teamId: this.teamId,
              zystaffids: this.zyStaffids,
            })
            if (res.code == 1) {
              this.$baseMessage(res.msg, 'success')
              this.$emit('getList', { projectId: this.projectId })
              this.dialogVisible = false
            } else {
              this.$baseMessage(res.msg, 'error')
            }
          }
        })
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
