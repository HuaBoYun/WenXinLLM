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
      <!-- <el-table-column
        prop="teamName"
        label="小组名称"
      ></el-table-column> -->
      <el-table-column
        prop="name"
        label="成员"
        align="center"
      ></el-table-column>
      <!-- <el-table-column prop="stafftype" label="角色名称" width="120">
        <template slot-scope="scope">
          {{ scope.row.stafftype == 1 ? '组员' : '组长' }}
        </template>
      </el-table-column> -->
    </el-table>
  </el-dialog>
</template>
<script>
  import {
    getPjteamUsrList,
    manageSave,
    manageSave2,
  } from '@/oapi/audit/project'
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
        realname: '',
        currentFlag: false,
        type: '',
        ZSinfo: {},
      }
    },
    methods: {
      // type == 31,32
      showEdit(multipleSelection, projectId, type, ZSinfo) {
        this.ZSinfo = ZSinfo
        this.projectId = projectId
        this.type = type
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
          id: this.projectId,
        })
        //处理接口的数据，拆分成
        let arr = []
        res.data.data.forEach((k) => {
          if (k.fzzstaffid) {
            arr.push({ name: k.fzzname, staffid: k.fzzstaffid })
          }
          if (k.teamLeaderId) {
            arr.push({
              name: k.teamLeader.realname,
              staffid: k.teamLeader.staffid,
            })
          }
          if (k.teamMembers.length > 0) {
            k.teamMembers.forEach((j) => {
              arr.push({ name: j.realname, staffid: j.staffid })
            })
          }
        })
        //加入外面带入的主审
        console.log(arr, 'arr')
        console.log(this.ZSinfo, 'this.ZSinfo')
        arr.push(this.ZSinfo)
        //去重处理
        let newArrId = []
        let newArrObj = []
        arr.forEach((item) => {
          if (!newArrId.includes(item.staffid)) {
            newArrId.push(item.staffid)
            newArrObj.push(item)
          }
        })
        this.list = [...newArrObj]
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
          this.id = val[0].staffid
          this.realname = val[0].name
        }
      },
      async save() {
        if (this.currentFlag) {
          if (!this.type) {
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
            let res = await manageSave2({
              ids: this.operateidStr,
              // projectId: this.projectId,
              ryids: this.id,
              rynames: this.realname,
              xmtype: this.type,
            })
            if (res.code == 1) {
              this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
              this.$emit('getExecutorList', this.type)
              this.dialogVisible = false
            } else {
              this.$baseMessage('分配失败', 'error', 'vab-hey-message-error')
            }
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
