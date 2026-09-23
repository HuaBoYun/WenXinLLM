<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :expand-on-click-node="false"
          highlight-current
          node-key="id"
          :props="defaultProps"
          @node-click="handleNodeClick"
          :load="load"
          :lazy="true"
          :default-expand-all="true"
        />
      </div>
      <div class="right">
        <!-- <vab-query-form>
          <vab-query-form-left-panel>
            <span></span>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel>
            <el-button
              type="success"
              @click="handelAddBtn(false, false, '新建')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
        </vab-query-form> -->
        <!-- <el-card shadow="never">
        </el-card> -->
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-tooltip
              class="item"
              effect="dark"
              content="表格筛选"
              placement="top"
            >
              <el-popover placement="right" trigger="click">
                <filter-table
                  :list="filedAll"
                  :name="tableKey"
                  @updateTableShow="initTable"
                />
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            label="问题单元"
            prop="data"
            width="100"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleRecordList(row)">
                {{ row.businessType }}
              </el-button>
            </template>
          </el-table-column>
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              v-if="item.name === '审计问题'"
              align="center"
              label="审计问题"
              prop="riskPoint"
            />
            <el-table-column
              v-if="item.name === '重点关注事项'"
              align="center"
              label="重点关注事项"
              prop="control"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '审计程序'"
              align="center"
              label="审计程序"
              prop="suditProcess"
              show-overflow-tooltip
              width="120"
            />
            <el-table-column
              v-if="item.name === '所需资料'"
              align="center"
              label="所需资料"
              prop="bioData"
              show-overflow-tooltip
              width="120"
            />
            <el-table-column
              v-if="item.name === '分配人员'"
              align="center"
              label="分配人员"
              prop="renyuan"
              show-overflow-tooltip
              width="120"
            />
            <!-- <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handelAddBtn(row, false, '修改')">
                  修改
                </el-button>
                <el-button type="text" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column> -->
          </div>
          <el-table-column width="1" />
        </el-table>
        <el-pagination
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <!-- <record-list-info
      :targetId="targetId"
      @fetch-data="fetchData"
      ref="recordListInfo"
    /> -->
    <record-list-info ref="recordListInfo" />
  </div>
</template>

<script>
  import RecordListInfo from '@/views/audit/implement/components/RecordListInfo.vue'
  import { getListZy, getLeftTreeZy, defCatDel } from '@/api/audit/preparation'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { myTaskDisp } from '@/oapi/audit/implement.js'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, RecordListInfo },
    props: {
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
          // pid: undefined,'
          targetId: '',
          type: 'mb',
          pageNumber: 1,
          pageSize: 20,
        },
        queryForms: {
          type: 'my',
          nodeId: undefined,
        },
        // 筛选列表配置
        filedAll: [
          { name: '审计问题' },
          { name: '重点关注事项' },
          { name: '审计程序' },
          { name: '所需资料' },
          { name: '分配人员' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-prepare-guide-search',
        tableKey: 'audit-prepare-guide-list',
        searchMore: true,
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
        return [
          // { name: '计划名称', key: 'planName' },
          // { name: '计划年度', key: 'planYear' },
        ]
      },
      async load(node, resove) {
        let params = {
          ...this.queryForm
        }
        if(this.projectId) {
          params.projectId = this.projectId
        }
        if (node.level === 0) {
          const res = await getLeftTreeZy(params)
          resove(res.data.tree)
        }
        // if (node.level === 1) {

        // }
        if (node.level > 0) {
          const res = await getLeftTreeZy({
            ...params,
            nodeId: node.data.id,
          })
          resove(res.data.tree && res.data.tree.length ? res.data.tree : [])
        }
      },
      handleNodeClick(val, node) {
        let id = node.level === 1 ? undefined : node.data.id
        this.queryForm.targetId = id
        this.targetId = id
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getListZy({...this.queryForm, projectId: this.projectId})
        this.list = tlist
        this.total = totalRecord
        // const res = await getLeftTreeZy(this.queryForm)
        // this.dataTree = res.data.tree
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      // handelAddBtn(row, flag, type) {
      //   if (!this.targetId && type === '新建') {
      //     this.$baseMessage('请选择节点', 'error')
      //   } else {
      //     this.$refs['recordListInfo'].showEdit(row, flag)
      //   }
      // },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      //问题单元
      async handleRecordList(row) {
        console.log('🚀 ~ handleRecordList ~ row:', row)
        const data = await myTaskDisp({ programid: row.programId })
        this.$refs['recordListInfo'].showEdit(data.data)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
  }

  .lr-layout > .right {
    width: 95%;
  }
</style>
