<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="分配"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-table
      :data="tableData"
      ref="multipleTable"
      style="width: 100%"
      @selection-change="handleSelection"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="用户ID" prop="staffid" />
      <el-table-column label="用户真实名" prop="realname" />
    </el-table>

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">确定</el-button>
    </div>
  
  </el-dialog>
</template>
<script>
  import projectManage from '@/components/danxuanPerson.vue'
  import projectManage1 from '@/components/selectPerson.vue'
  import { manageSave2 } from '@/oapi/audit/project'

  export default {
    name: 'gcxmrysbAssignEdit',
    components: { projectManage, projectManage1 },
    data() {
      return {
        loading: false,
        dialogJdVisible: false,
        ids: '',
        xmtype: '',
        tableData: [],
        select: [],
        projects: [],
      }
    },
    methods: {
      showEdit(row) {
        console.log('row', row)
        this.dialogJdVisible = true
        this.xmtype = row.propsData.gljhxmlx
        if (this.xmtype == '31') {
          this.ids = row.projects.map(x => x.gcxmzjzjbid).join(',')
        } else {
          this.ids = row.projects.map(x => x.jsxmtzwcqkid).join(',')
        }
        this.projects = row.propsData.projects
        if (row.propsData.assistApprover && row.propsData.assistApproverId) {
          const assNames = row.propsData.assistApprover.split(',')
          const assIds = row.propsData.assistApproverId.split(',')
          const len = assIds.length
          if (len > 0) {
            for (let i = 0; i < len; i++) {
              this.tableData.push({
                realname: assNames[i],
                staffid: assIds[i]
              })
            }
          }
        }
        if(row.propsData.approver && row.propsData.approverId){
          this.tableData.push({
            realname: row.propsData.approver,
            staffid: row.propsData.approverId
          })
        }
      },
      close() {
        this.id = ''
        this.xmtype = ''
        this.tableData = []
        this.select = []
        this.dialogJdVisible = false
      },
      handleSelection(val) {
        this.current = val
        // if (val.length > 1) {
        //   let del = val.shift()
        //   this.$refs.multipleTable.toggleRowSelection(del, false)
        // }
        this.select = val
      },
      async save() {
        if (!this.select || !this.select.length) return this.$baseMessage('请选择人员！', 'error', 'vab-hey-message-error')
        console.log('this.select', this.select)
        let ryids = [], rynames = []
        this.select.map(v => {
          ryids.push(v.staffid)
          rynames.push(v.realname)
        })
        const params = {
          ids: this.ids,
          ryids: ryids.join(','),
          rynames: rynames.join(','),
          xmtype: this.xmtype,
        }
        console.log('params', params)
        this.$baseConfirm(`确定要分配${params.rynames}吗`, null, async () => {
          this.loading = true
          const { msg, code } = await manageSave2(params)
          this.loading = false
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            this.$emit('save', 1)
            this.close()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
        
      }
    }
  }
</script>