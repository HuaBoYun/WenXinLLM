<template>
  <div>
    <el-dialog
      title="项目角色-维护"
      :visible.sync="dialogVisible"
      width="50%"
      :close-on-click-modal="false"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addGroup">添加小组</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="角色名称">项目经理</el-table-column>
        <el-table-column prop="realname" label="被指派人姓名"></el-table-column>
        <el-table-column prop="createUserName" label="指派人"></el-table-column>
        <el-table-column
          prop="assigbedpmTime"
          label="指派时间"
        ></el-table-column>
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button type="text" @click="assign(row)">指派</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
        ref="multipleTable"
        :data="listTeam"
        tooltip-effect="dark"
        style="width: 100%"
        @select="handleSelection"
      >
        <!-- <el-table-column type="selection" width="55"></el-table-column> -->
        <el-table-column prop="teamName" label="小组名称"></el-table-column>
        <el-table-column prop="staff.realname" label="成员"></el-table-column>
        <el-table-column prop="stafftype" label="角色名称">
          <template slot-scope="scope">
            {{ scope.row.stafftype == 1 ? '组员' : '组长' }}
          </template>
        </el-table-column>
        <el-table-column prop="createUserName" label="创建人"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button type="text" @click="modifyGroup(row)">
              修改小组
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <add-group @getList="getNbsjTempleteListFun" ref="addgroup"></add-group>
    <modify-group
      @getList="getNbsjTempleteListFun"
      ref="modifygroup"
    ></modify-group>
    <assign @getList="getNbsjTempleteListFun" ref="assign"></assign>
  </div>
</template>
<script>
  import { getProjectDetail, getPjteamUsrList } from '@/api/audit/project'
  import addGroup from './taskAllocationChild/addGroup.vue'
  import modifyGroup from './taskAllocationChild/modifyGroup.vue'
  import assign from './taskAllocationChild/assign.vue'
  import { UTCformat } from '@/utils'

  export default {
    components: {
      addGroup,
      modifyGroup,
      assign,
    },
    data() {
      return {
        dialogVisible: false,
        list: [],
        listTeam: [],
        multipleSelection: [],
        projectId: 0,
      }
    },
    methods: {
      showEdit(row) {
        this.getNbsjTempleteListFun(row)
        this.projectId = row.projectId
        this.dialogVisible = true
      },
      async getNbsjTempleteListFun(row) {
        this.listLoading = true
        let res = await getProjectDetail({
          projectid: row.projectId,
        })
        this.list = [
          {
            realname: res.data.pj.pmStaff.realname,
            createUserName: res.data.pj.createUserName,
            assigbedpmTime: res.data.pj.assigbedpmTime,
          },
        ]
        this.list.forEach((item) => {
          item.assigbedpmTime = UTCformat(item.assigbedpmTime)
        })
        let resTeam = await getPjteamUsrList({
          projectid: row.projectId,
        })
        this.listTeam = resTeam.data.listTeamZY
        this.listTeam.forEach((item) => {
          item.createTime = UTCformat(item.createTime)
        })
        this.listLoading = false
      },
      addGroup() {
        this.$refs['addgroup'].showEdit(this.projectId)
      },
      modifyGroup(row) {
        this.$refs['modifygroup'].showEdit(this.projectId, row)
      },
      assign() {
        this.$refs['assign'].showEdit(this.projectId)
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        this.$emit('templateList', this.multipleSelection)
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
