<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-row class="box_row">
          <el-button type="primary" @click="getExecutorTree()" v-if="false">
            刷新
          </el-button>
        </el-row>
        <el-tree
          :data="data"
          :props="defaultProps"
          :highlight-current="true"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <el-row class="box_row flex">
          <el-button type="primary" @click="handleExport">导出</el-button>
          <el-button type="primary" @click="submit()">全部提交</el-button>
        </el-row>
        <el-col :span="24">
          <span>被评价对象: {{ orgname }}</span>
          <el-divider />
        </el-col>
        <el-table
          v-loading="listLoading"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column
            align="center"
            label="要素编号"
            prop="elementNumber"
            width="100"
          />
          <el-table-column align="center" label="要素名称" prop="elementName">
            <template #default="{ row }">
              <el-button
                style="color: red"
                type="text"
                @click="$refs['ScoreInfo'].showEdit(row)"
              >
                {{ row.elementName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="标准分"
            prop="standardscore"
            width="70"
          />
          <el-table-column align="center" label="审查要点" prop="examination" />
          <el-table-column
            align="center"
            label="评价分"
            prop="score"
            width="70"
          />
          <el-table-column align="center" label="	评价依据" prop="reason" />
          <el-table-column align="center" label="	附件" prop="attname" />
          <el-table-column align="center" label="操作" show-overflow-tooltip>
            <template #default="{ row }">
              <el-row>
                <el-button type="text" @click="handleEdit(row)">评分</el-button>
                <!-- <SendBtn /> -->
              </el-row>
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
      </div>
    </div>
    <ScoreEdit ref="edit" @fetch-data="fetchData" />
    <ScoreInfo ref="ScoreInfo" />
  </el-dialog>
</template>
<script>
  // import { getList } from '@/api/systemLog'
  // import { doDelete } from '@/api/table'
  import {
    getLeftTreeByPingfen,
    getProjCatList,
    sbumitProjCat,
    pfjgExport,
  } from '@/api/internal/score'
  import TypeTree from '@/views/internal/evaluationManagement/components/TypeTree.vue'
  import ScoreEdit from '@/views/internal/evaluationManagement/components/ScoreEdit.vue'
  import ScoreInfo from '@/views/internal/evaluationManagement/components/ScoreInfo.vue'
  import SendBtn from '@/views/internal/components/SendBtn'
  export default {
    name: 'ScorekList',
    components: { SendBtn, TypeTree, ScoreEdit, ScoreInfo },
    data() {
      return {
        title: '评价',
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          assId: '',
          nodeId: '',
          orgId: '',
          tmplId: '',
          // pageNumber: 1,
          // pageSize: 5,
        },
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        orgname: '',
        dialogFormVisible: false,
        multipleSelection: [],
      }
    },
    // created() {
    //   this.fetchData()
    // },
    methods: {
      showEdit(row, project) {
        this.orgname = row.orgname
        this.queryForm.orgId = row.orgid
        this.queryForm.tmplId = project.asstemid
        this.queryForm.assId = row.assid
        this.getExecutorTree()
        this.list = this.$options.list
        this.queryData({
          orgId: row.orgid,
          tmplId: project.asstemid,
          assId: row.assid,
        })
        this.dialogFormVisible = true
      },
      async getExecutorTree() {
        const res = await getLeftTreeByPingfen(this.queryForm)
        this.data = JSON.parse(res.data)
      },
      handleSelectionChange(val) {
        console.log('val', val)
        this.multipleSelection = val
      },
      handleNodeClick(val) {
        this.queryForm.nodeId = val.id
        this.queryData()
      },
      handleSizeChange(val) {
        // this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        // this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData(params) {
        // this.queryForm.pageNumber = 1
        this.fetchData(params)
      },
      async fetchData(params = {}) {
        console.log(this.queryForm, 'this.queryForm')
        console.log(params, 'params')
        this.listLoading = true
        const {
          data: { list },
        } = await getProjCatList({ ...this.queryForm, ...params })
        this.list = list
        this.listLoading = false
      },
      async submit() {
        const { data, code, msg } = await sbumitProjCat({
          assId: this.queryForm.assId,
          orgId: this.queryForm.orgId,
        })
        if (code == 1) {
          this.$baseMessage('提交成功', 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          this.$emit('fetch-data')
        }
        this.close()
      },
      async handleExport(row) {
        if (!this.queryForm.nodeId) {
          this.$baseMessage('请选择节点', 'error', 'vab-hey-message-error')
          return
        }
        const data = await pfjgExport({
          assid: this.queryForm.assId,
          nodeId: this.queryForm.nodeId,
          orgId: this.queryForm.orgId,
        })
        let fileName = '评价评分'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handlePreview() {},
      handleReport() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      showModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm.nodeId = ''
      },
    },
  }
</script>
<style scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
  .el-table thead.is-group th.el-table__cell {
    background: #fff;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:before {
    content: '日期';
    text-align: center;
    position: absolute;
    width: 152px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:after {
    content: '配送新增';
    text-align: center;
    position: absolute;
    width: 152px;
    top: 10px;
    left: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #ebeef5;
    display: block;
    text-align: center;
    transform: rotate(38deg);
    transform-origin: top left;
    -ms-transform: rotate(38deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(38deg);
    -webkit-transform-origin: top left;
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 300px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  /* .left /deep/ .el-tree-node__label {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  } */

  .left /deep/ .el-tree {
    overflow: auto;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
