<template>
  <el-dialog
    :append-to-body="true"
    title="审计任务清单"
    :visible.sync="dialogFormVisible"
    @close="close"
    :close-on-click-modal="false"
    fullscreen
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <!-- <el-tree
            ref="tree"
            :check-strictly="true"
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :load="load"
            :lazy="true"
          /> -->
          <task-tree @getChildParam="getData" :projectId="projectId" />
        </div>
        <div class="right">
          <el-card shadow="never">
            <el-table v-loading="listLoading" :data="list">
              <el-table-column
                align="center"
                label="问题单元"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="$refs['recordListInfo'].showEdit(row, true)"
                  >
                    {{ row.businessType }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                prop="suditProcess"
                align="center"
                label="审计程序"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="control"
                label="重点关注事项"
                align="center"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                align="center"
                label="完成时间"
                prop="finishtime"
                show-overflow-tooltip
                :formatter="formatDate"
              />
              <el-table-column
                align="center"
                label="状态"
                prop="finish"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  {{ row.finish === 1 ? '已完成' : '未完成' }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="督导意见" prop="executionSituation">
                <div slot-scope="{ row }">
                  <el-input
                    v-model="row.executionSituation"
                    clearable
                    type="textarea"
                    :rows="1"
                    autosize
                  ></el-input>
                </div>
              </el-table-column>
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="220"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="checkRelate(row)">
                    查看关联底稿
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
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
      <record-list-info
        :targetId="targetId"
        @fetch-data="fetchData"
        ref="recordListInfo"
      />
    </div>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="handleSubmit" type="primary" :loading="loading">
        确定
      </el-button>
    </div>
    <relateDraftModalList ref="modal" />
  </el-dialog>
</template>

<script>
  import RecordListInfo from '@/views/oilAudit/prepare/components/RecordListInfo'
  import { getListZy, getLeftTreeZy, defCatDel } from '@/oapi/audit/preparation'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import TaskTree from '@/views/oilAudit/prepare/components/TaskTree'
  import { getProjectRwList } from '@/oapi/audit/project'
  import { saveOrUpdateList } from '@/oapi/audit/task'
  import relateDraftModalList from '@/views/oilAudit/implement/components/relateDraftModal.vue'
  import { formatDate } from '@/utils/index'

  export default {
    name: 'guideDetails',
    components: { RecordListInfo, filterSearch, filterTable, TaskTree, relateDraftModalList },
    mixins: [searchTableMixis],
    props: {
      //项目查看传入
      isShow: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dataTree: [],
        nodeId: 0,
        targetId: 0,
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: (data, node) => {
            if (node === 0) {
              return false
            } else {
              return !data.isParent
            }
          },
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        queryForms: {
          type: 'my',
          nodeId: undefined,
        },
        filedAll: [
          { name: '审计问题' },
          { name: '重点关注事项' },
          { name: '审计程序' },
          { name: '所需资料' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-prepare-guide-search',
        tableKey: 'oilAudit-prepare-guide-list',
        searchMore: true,
        dialogFormVisible: false,
        loading: false,
        row: null,
        projectId: ''
      }
    },
    created() {
      
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      showEdit(row, type) {
        console.log(row, type)
        this.row = row
        if(type == 'SJDD') {
          this.row.id = row.projectId
          this.projectId = row.projectId
        } else {
          this.projectId = row.id 
        }
        this.dialogFormVisible = true
        this.fetchData()
        this.initTable()
        this.searchNow = this.getFiled()
        this.searchItem = this.searchNow.slice(0, 4)
        this.initSearch()
      },
      handleSubmit() {
          const arrTemp = this.list.filter((item) => !item.index) //全部有输入框的行
          console.log(arrTemp)
          const entityList = arrTemp.map((item) => ({
            executionSituation: item.executionSituation,
            typeid: item.authId,
            projectid:this.row.id,
            rwid: item.sjddrw?.rwid || undefined,
          }))
          console.log(entityList)
          this.listLoading = true
          this.loading = true
          saveOrUpdateList(entityList)
            .then(() => {
              this.$baseMessage('保存成功', 'success')
              this.$emit('queryData')
              this.close()
            })
            .catch((res) => {
              this.$baseMessage(res.msg, 'error')
            })
            .finally(() => {
              this.listLoading = false
              this.loading = false
            })
      },
      close() {
        this.dialogFormVisible = false
      },
      getFiled() {
        return [{ name: '计划名称', key: 'jhmc' }]
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      getData(id) {
        this.queryForm.targetId = id
        this.targetId = id
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getProjectRwList({projectId: this.row.id, ...this.queryForm})
        this.list = tlist.map((i,index) =>  ({ 'executionSituation': i.sjddrw?.executionSituation, ...i}))
        this.total = totalRecord
        // const res = await getLeftTreeZy(this.queryForm)
        // this.dataTree = res.data.tree
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handelAddBtn(row, flag, type) {
        if (!this.targetId && type === '新建') {
          this.$baseMessage('请选择节点', 'error')
        } else {
          this.$refs['recordListInfo'].showEdit(row, flag)
        }
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await defCatDel({ programId: row.programId })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      checkRelate(row) {
        this.$refs['modal'].showEdit(row.operateid)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    overflow-x: scroll;
  }

  .lr-layout > .right {
    flex: 1;
    width: calc(100% - 210px);
  }
</style>
