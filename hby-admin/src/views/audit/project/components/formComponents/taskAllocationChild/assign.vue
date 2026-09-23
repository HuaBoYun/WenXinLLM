<template>
  <div>
    <el-dialog
      title="项目角色-角色分配"
      :visible.sync="dialogVisible"
      width="40%"
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
        <el-form-item label="角色">
          <el-input v-model="ruleForm.role" disabled></el-input>
        </el-form-item>
        <el-form-item label="人员指派" prop="name">
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
      </el-form>
    </el-dialog>

    <!-- 选择组长组员子组件 -->
    <select-team ref="select" @selectTeamList="selectTeamList" :defaultExpandedH="3"></select-team>
  </div>
</template>
<script>
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import { proPmModi } from '@/api/audit/project'

  export default {
    components: { selectTeam },
    data() {
      return {
        ruleForm: {
          role: '项目经理',
          leaderName: '',
        },
        leaderId: '',
        projectId: '',
        dialogVisible: false,
        rules: {
          name: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
          ],
        },
        rows: {}
      }
    },
    methods: {
        /**
       * @description   组件初始化
       * @param {*}  
        * @return {*}
       */ 
      showEdit(projectId, row, rows) {
        this.rows = rows
        this.projectId = projectId
        this.leaderId = row.pmId
        this.ruleForm.leaderName = row.realname
        this.dialogVisible = true
      },
        /**
       * @description  选择组员，数据处理，数组处理成字符串
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
            // 拿到组员id字符串
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
       * @description  打开 选择组长页面
       * @param {*}  
        * @return {*}
       */  
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
        /**
       * @description  确定按钮，分配任务
       * @param {*}  
        * @return {*}
       */  
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        let res = await proPmModi({
          pmId: this.leaderId,
          projectid: this.projectId,
        })
        console.log(res)
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.$emit('getList', this.rows)
          this.dialogVisible = false
        } else {
          this.$baseMessage('分配失败', 'error', 'vab-hey-message-error')
        }
      },
    },
  }
</script>
<style scoped lang="scss"></style>
