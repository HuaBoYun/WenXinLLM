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

     <!-- 选择组长组员子组件 -->
     <select-team ref="select" @selectTeamList="selectTeamList" :defaultExpandedH="3"></select-team>
  </div>
</template>
<script>
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
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
        /**
       * @description   组件初始化，接收参数，回显
       * @param {*}  
        * @return {*}
       */ 
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
      async showEdit(projectId, row) {
        let that = this
        let res = await proPjteamInfo({
          teamId: row.teamid,
        })
        let arr = [], a1 = []
        res.data.team.teamStaffs.forEach((item) => {
          if(item.stafftype == 1) {
            arr.push(item.staffid)
            a1.push(item.staff.realname)
          } else {
            that.leaderId = item.staffid
            that.ruleForm.leaderName = item.staff.realname
          }
        })
        this.zyStaffids = arr.join(',')
        this.ruleForm.teamName = res.data.team.teamName,
        this.ruleForm.members = a1.join(',')
        this.teamId = row.teamid
        this.projectId = projectId
        this.dialogVisible = true
      },
        /**
       * @description 选择组员，数据处理，把数组处理成字符串
       * @param {*}  
        * @return {*}
       */  
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
        /**
       * @description  打开选择组长页面
       * @param {*}  
        * @return {*}
       */  
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
        /**
       * @description  打开选择组员页面
       * @param {*}  
        * @return {*}
       */  
      showTeamMembers() {
        this.$refs['select'].showEdit('members')
      },
        /**
       * @description 关闭组件
       * @param {*}  
        * @return {*}
       */  
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.$refs['ruleForm'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
        /**
       * @description  确定按钮,保存编辑的项目方案
       * @param {*}  
        * @return {*}
       */ 
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
