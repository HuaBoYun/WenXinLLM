<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="data"
          default-expand-all
          :expand-on-click-node="false"
          highlight-current
          node-key="id"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <!-- <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form> -->
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
        >
          <el-table-column align="center" label="问题单元" prop="businessType">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.businessType }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="审计问题" prop="riskPoint" />
          <el-table-column align="center" label="重点关注事项" prop="control" />
          <el-table-column
            align="center"
            label="审计程序"
            prop="suditProcess"
          />
          <el-table-column align="center" label="所需资料" prop="bioData" />
        </el-table>
        <!-- 				 -->
        <!-- <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        /> -->
      </div>
    </div>
    <FormDeail ref="formDeail" @fetch-data="getExecutorList" />
  </el-dialog>
</template>
<script>
  import {
    findNbsjTargetTree,
    findNbsjTargetTree2,
    defCatListZy,
  } from '@/api/workbench/auditTools'
  import { getDetailZy } from '@/api/audit/preparation'
  import FormDeail from './formDeail'

  export default {
    name: 'tables',
    components: {
      FormDeail,
    },
    data() {
      return {
        rootNodeId: '',
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'childrenList',
          label: 'targetName',
          value: 'id',
        },
        data: [],
        queryForm: {
          targetId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        templeteId: '',
        current: undefined,
      }
    },
    created() {},
    methods: {
      async handleDetail(row) {
        const res = await getDetailZy({
          programid: row.programId,
        })
        this.$refs['formDeail'].showEdit(
          res.data,
          'deail',
          this.queryForm.targetId
        )
      },
      show(templeteId) {
        this.list = []
        this.current = undefined
        this.dialogFormVisible = true
        this.templeteId = templeteId
        this.getExecutorTree(templeteId)
        this.getExecutorList(templeteId)
      },
      // async getExecutorTree(templeteId) {
      //   const res = await findNbsjTargetTree({ templeteId, nodeId: '' })
      //   this.data = res.data.targetTree
      //   // 默认展开全部
      //   this.data.forEach((item) => {
      //     this.handleNodeClick(item)
      //   })
      // },
      async getExecutorTree(templeteId) {
        const res = await findNbsjTargetTree({ templeteId, nodeId: '' })
        console.dir(res)
        this.data = res.data.targetTree
        res.data.targetTree.map((item) => {
          if (item.id) {
            this.getTree(item.id)
          }
        })
      },
      async getTree(id) {
        const res = await findNbsjTargetTree2({
          templeteId: this.templeteId,
          nodeId: id,
        })
        this.data = [
          {
            targetId: res.targetId,
            targetName: res.treeName,
          },
        ]
        this.rootNodeId = this.data[0].targetId
        if (res.tree.length > 0) {
          let list = []
          res.tree.map((item) => {
            list.push({ name: item.targetName, id: item.targetId })
          })

          this.data[0].childrenList = res.tree
          console.dir(this.data)
          // this.queryForm.targetId = this.data[0].targetId
          this.getExecutorList()
          // this.data.map((item) => {
          //   if (item.id == id) {
          //     item.children = list
          //   }
          // })
        }
      },
      // async getTree(id) {
      //   const res = await findNbsjTargetTree2({
      //     templeteId: this.templeteId,
      //     nodeId: id,
      //   })
      //   if (res.tree.length > 0) {
      //     let list = []
      //     res.tree.map((item) => {
      //       list.push({ name: item.targetName, id: item.targetId })
      //     })
      //
      //     this.data.map((item) => {
      //       if (item.id == id) {
      //         item.children = list
      //       }
      //     })
      //   }
      // },
      async getExecutorList() {
        this.listLoading = true
        let endQueryForm = JSON.parse(JSON.stringify(this.queryForm))
        if (endQueryForm.targetId === this.rootNodeId) {
          delete endQueryForm.targetId
        }
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await defCatListZy({
          tempId: this.templeteId,
          ...endQueryForm,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.targetId = val.targetId
        // this.getTree(val.id)
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.list = []
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
