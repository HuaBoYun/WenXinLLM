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
        <el-button type="primary" @click="handleAddTree">新建</el-button>
        <el-button type="primary" @click="handleEditTree">编辑</el-button>
        <el-button @click="handleDeleteTree">删除</el-button>
        <el-button @click="restTree">刷新</el-button>
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
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button type="primary" @click="handleAdd">新建</el-button>
            <el-button @click="last">上一步</el-button>
            <el-button @click="finsh">完成</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
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
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row)">修改</el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <FormDeail ref="formDeail" @fetch-data="getExecutorList" />
    <TreeEdit :templeteId="templeteId" ref="treeEdit" @success="treeSuccess" />
  </el-dialog>
</template>
<script>
  import { getDetailZy } from '@/api/audit/preparation'
  import {
    defCatListZy,
    deleteNbsjAuditprogram,
    deleteNbsjTarget,
    findNbsjTargetTree,
    findNbsjTargetTree2,
  } from '@/api/workbench/auditTools'
  import FormDeail from './formDeail.vue'
  import TreeEdit from './TreeEdit.vue'

  export default {
    name: 'tables',
    components: {
      FormDeail,
      TreeEdit,
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
        templeteId: 0,
        current: undefined,
        treeVal: undefined,
      }
    },
    created() {},
    methods: {
      treeSuccess() {
        this.getExecutorTree(this.templeteId)
        this.queryForm.targetId = ''
      },
      restTree() {
        this.getExecutorTree(this.templeteId)
      },
      async handleDetail(row) {
        const res = await getDetailZy({
          programid: row.programId,
        })
        this.$refs['formDeail'].showEdit(res.data, 'deail', this.queryForm)
      },
      show(templeteId) {
        this.list = []
        this.current = undefined
        this.dialogFormVisible = true
        this.templeteId = templeteId

        this.getExecutorTree(templeteId)
        this.getExecutorList(templeteId)
      },
      async getExecutorTree(templeteId) {
        const res = await findNbsjTargetTree({ templeteId, nodeId: '' })
        this.data = res.data.targetTree
        this.isParentID = res.data.targetTree[0].id
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
            // isParent: res.isParent,
          },
        ]
        this.rootNodeId = this.data[0].targetId
        if (res.tree.length > 0) {
          let list = []
          res.tree.map((item) => {
            list.push({ name: item.targetName, id: item.targetId })
          })

          this.data[0].childrenList = res.tree
          // this.queryForm.targetId = this.data[0].targetId
          // this.data.map((item) => {
          //   if (item.id == id) {
          //     item.children = list
          //   }
          // })
        }
        this.getExecutorList()
      },
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
        console.dir(val, 'val')
        this.treeVal = val
        this.queryForm.targetId = val.targetId
        this.queryForm.targetName = val.targetName
        // this.getTree(val.id)
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      handleAddTree() {
        if (!this.queryForm.targetId) {
          this.$baseMessage('请选择节点！', 'error', 'vab-hey-message-error')
          return
        } else {
          this.$refs['treeEdit'].show(this.queryForm.targetId, 0, 'add')
        }
      },
      handleEditTree() {
        if (!this.queryForm.targetId) {
          this.$baseMessage('请选择节点！', 'error', 'vab-hey-message-error')
          return
        } else if (this.queryForm.targetId === this.isParentID) {
          this.$baseMessage('父节点不可编辑', 'error', 'vab-hey-message-error')
          return
        } else {
          this.$refs['treeEdit'].show(this.queryForm.targetId, 0, 'edit')
        }
      },
      async handleDeleteTree() {
        if(this.treeVal && this.treeVal.childrenList.length > 0) {
          this.$baseMessage('请先删除子节点！', 'error', 'vab-hey-message-error')
          return
        }
        if (!this.queryForm.targetId) {
          this.$baseMessage('请选择节点！', 'error', 'vab-hey-message-error')
          return
        } else {
          const res = deleteNbsjTarget({
            targetId: this.queryForm.targetId,
          })
          this.$baseMessage('删除成功', 'success')
          this.getExecutorTree(this.templeteId)
        }
      },
      handleAdd() {
        if (!this.queryForm.targetId) {
          this.$baseMessage('请选择节点！', 'error', 'vab-hey-message-error')
          return
        } else {
          this.$refs['formDeail'].showEdit(null, 'add', this.queryForm)
        }
      },
      async handleEdit(row) {
        const res = await getDetailZy({
          programid: row.programId,
        })
        this.$refs['formDeail'].showEdit(res.data, 'edit', this.queryForm)
      },
      async handleDelete(row) {
        const data = deleteNbsjAuditprogram({
          programId: row.programId,
        })
        this.$baseMessage('删除成功', 'success')
        this.getExecutorList()
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      finsh() {
        this.close()
      },
      last() {
        this.close()
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
