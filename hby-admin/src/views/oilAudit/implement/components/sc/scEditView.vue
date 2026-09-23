<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      @close="beforeClose"
      width="1400px"
      :close-on-click-modal="false"
      :modal="false"
    > 
            <vab-query-form>
              <vab-query-form-left-panel :span="12">
               
              </vab-query-form-left-panel>
              <vab-query-form-right-panel :span="12"> 
                <el-button
                type="primary" 
                  @click="addView()"
                >
                  新建
                </el-button> 
              </vab-query-form-right-panel>
            </vab-query-form>
            <el-table
               
              v-loading="listLoading"
              ref="multipleTable"
              :data="list"
              tooltip-effect="dark" 
              style="width: 100%"
            >
   
              <el-table-column
                label="记事本"
                prop="notepad"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                label="督导意见"
                prop="supervisionOpinions"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                label="备注"
                prop="remarks"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="220"
            >
              <template #default="{ row }">
                <el-button type="text" @click="edit(row)">
                编辑
                </el-button>
                <el-button type="text" @click="deletes(row)">
                  刪除
                </el-button>
                <!-- <el-button type="text" @click="checkRelate(row)">
                  审减内容
                </el-button>  -->
              </template>
            </el-table-column>
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
    <scAdd ref="scAdd" @fetch="fetch"/>
  </div>
</template>
<script>
  import { getMyTaskReviewList ,myTaskdelete} from '@/api/audit/implement'
  import { getProjectTree } from '@/api/audit/project' 
  import scAdd from './scAdd'
  export default { 
    components: { scAdd  },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        rootId: '', //根节点ID
        dialogVisible: false,
        list: [],
        dataTree: [],
        rowData: {},
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        type:'31',
        projectId:'',
        row:null
      }
    },
    methods: {
      beforeClose() {
        this.$emit('close')
      },
      
      showEdit(row) { 
        this.dialogVisible = true
        this.current = undefined 
        this.row = row  
        this.getList(row)
      },
      fetch(){
        this.getList(this.row)
      },
      getList(row){ 
        this.listLoading = true
        let data = {
          projectId: row.projectId,
          templateId: row.templateId, 
          typeNameId:row.scId
        }
        console.log(data)
        getMyTaskReviewList({ ...data,   }).then((res) => {
          if (res.code == 1) {
            this.list = res.data  
          }
          this.listLoading = false
        })
      },
      // getExecutorList() {
      //   this.listLoading = true
      //   let data = {
      //     projectId: this.projectId,
      //     targetId: this.targetId,
      //   }
      //   getProjectRwList({ ...data, ...this.queryForm }).then((res) => {
      //     if (res.code == 1) {
      //       this.list = res.data.pageInfo.tlist
      //       this.total = res.data.pageInfo.totalRecord
      //     }
      //     this.listLoading = false
      //   })
      // },
      // handleSizeChange(val) {
      //   this.queryForm.pageSize = val
      //   this.getExecutorList()
      // },
      // handleCurrentChange(val) {
      //   this.queryForm.pageNumber = val
      //   this.getExecutorList()
      // },
      handleNodeClick(val) {
        if (val.id === this.rootId) {
          this.targetId = undefined
        } else {
          this.targetId = val.id
        }
        // this.queryForm.nodeId = val.id
        // this.getExecutorTree()
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        this.multipleSelection = val
      },
      handleSelectionAll(val) {
        //
        this.multipleSelection = val
      },
      addView(){
        this.$refs.scAdd.showEdit('add',null,this.row)
      },
      edit(row){
        this.$refs.scAdd.showEdit('edit',row,this.row)
      },
      save() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }

        // this.$emit('projectManage', this.multipleSelection)
        this.$refs['allocationChild'].showEdit(
          this.multipleSelection.map(item=>{return {...item,programid: this.type == '31'?item.gcxmzjzjbid:item.jsxmtzwcqkid}}),
          this.projectId,
          this.type
        )
        // this.dialogVisible = false
      },
      deletes(row){
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await myTaskdelete({ id: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetch()
        })
      },
      close(){
        this.$emit('close')
      }
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  // ::v-deep thead {
  //   .el-table-column--selection {
  //     .el-checkbox__inner {
  //       display: none !important;
  //     }
  //   }
  // }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
