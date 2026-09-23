<template>
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
        <task-tree @getChildParam="getData" :projectId="this.projectId" />
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
              prop="riskSource"
              label="问题类型"
              align="center"
              show-overflow-tooltip
            ></el-table-column>
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
              prop="riskPoint"
              label="审计问题"
              align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column prop="renyuan" label="人员" align="center" />
            <el-table-column prop="finishtime" label="完成时间" align="center">
          <template #default="{ row }">
            {{ formatDay(row.finishtime) }}
          </template>
        </el-table-column>
            <el-table-column prop="finish" label="状态" align="center">
              <template #default="{ row }">
                  {{ row.finish == 1?'已完成':'未完成' }}
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
</template>

<script>
  import RecordListInfo from './components/RecordListInfo'
  import { getListZy, getLeftTreeZy, defCatDel } from '@/oapi/audit/preparation'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import TaskTree from './components/TaskTree'
  import { getProjectRwList } from '@/oapi/audit/project'
  import { formatDay } from '@/utils'

  export default {
    name: 'Download',
    components: { RecordListInfo, filterSearch, filterTable, TaskTree },
    mixins: [searchTableMixis],
    props: {
      //项目查看传入{isShow,projectId}
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
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
          targetId: '',
          type: 'mb',
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
        formatDay: formatDay,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [{ name: '计划名称', key: 'jhmc' }]
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      // async load(node, resove) {
      //   if (node.level === 0) {
      //     const res = await getTree(this.queryForm)
      //     resove(res.data.tree)
      //   }
      //   if (node.level === 1) {
      //     this.$emit('getChildParam', undefined)
      //   }
      //   if (node.level > 0) {
      //     const res = await getLeftTreeZy({
      //       ...this.queryForm,
      //       nodeId: node.data.id,
      //     })
      //     resove(res.data.tree && res.data.tree.length ? res.data.tree : [])
      //   }
      //   // if (node.level === 0) {
      //   //   const res = await getLeftTreeZy(this.queryForms)
      //   //   resove(res.data.tree)
      //   // }
      //   // if (node.level > 0) {
      //   //   const res = await getLeftTreeZy({
      //   //     ...this.queryForms,
      //   //     nodeId: node.data.id,
      //   //   })
      //   //   resove(res.data.tree && res.data.tree.length ? res.data.tree : [])
      //   // }
      // },

      // handleNodeClick(val, node) {
      //   let id = node.level === 1 ? undefined : node.data.id
      //   this.queryForm.targetId = id
      //   this.targetId = id
      //   this.fetchData()
      // },
      getData(id) {
        this.queryForm.targetId = id
        // this.queryForm.projectid = this.projectId
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
        } = await getProjectRwList({
          ...this.queryForm,
          projectId: this.projectId,
        })
        this.list = tlist
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
