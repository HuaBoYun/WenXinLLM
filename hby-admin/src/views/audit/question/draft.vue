<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.sheetcode"
                clearable
                placeholder="底稿编号"
                v-if="item.name === '底稿编号'"
              />
              <el-input
                v-model="queryForm.sheetname"
                clearable
                placeholder="底稿名称"
                v-if="item.name === '底稿名称'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.orgName"
                clearable
                placeholder="被审计单位/对象"
                v-if="item.name === '被审计单位/对象'"
                style="width: 200px"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="底稿编号"
          prop="sheetCode"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.sheetCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目名称"
            v-if="item.name === '项目名称'"
            prop="projectName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="底稿名称"
            v-if="item.name === '底稿名称'"
            prop="sheetName"
          />
          <!-- <el-table-column
            align="center"
            label="审计目标"
            prop="sheetTarget"
            show-overflow-tooltip
          /> -->
          <el-table-column
            align="center"
            label="被审计对象"
            v-if="item.name === '被审计对象'"
            prop="orgIdNames"
          >
            <template #default="{ row }">
              {{ row.orgIdNames }}
            </template>
          </el-table-column>
          >

          <el-table-column
            align="center"
            label="拟稿人"
            v-if="item.name === '拟稿人'"
            prop="realname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="拟稿日期"
            v-if="item.name === '拟稿日期'"
            prop="createTime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="所属项目"
            v-if="item.name === '所属项目'"
            prop="projectName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="state"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤回'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column width="1" />
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
    <DraftInfo ref="edit" @fetch-data="fetchData" />
    <!-- 被审计对象子组件 -->
    <!-- <Company ref="audiTree" @submit="getChildlistObj"></Company> -->
    <AuditeeDialog ref="auditree" @projectManage="getAuditee" />
  </div>
</template>

<script>
  import { dgDetail, dgListall } from '@/api/audit/question'
  import { formatDate } from '@/utils/index'
  import DraftInfo from '@/views/audit/implement/components/myDraftInfo.vue'
  // import DraftInfo from './components/DraftInfo'
  import AuditeeDialog from '@/views/audit/project/components/formComponents/AuditeeDialog.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch,
      DraftInfo,
      Company: () => import('@/components/Company.vue'),
      AuditeeDialog,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sheetcode: '',
          sheetname: '',
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
          orgName: '',
          auditStaffId: '',
          auditOrgId: '',
        },
        // 筛选、表格头自定义
        filedAll: [
          { name: '项目名称' },
          { name: '底稿名称' },
          { name: '被审计对象' },
          { name: '拟稿人' },
          { name: '拟稿日期' },
          { name: '所属项目' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-question-draft-search',
        tableKey: 'audit-question-draft-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '底稿编号', key: 'sheetcode' },
          { name: '底稿名称', key: 'sheetname' },
          { name: '项目名称', key: 'projectName' },
          { name: '被审计单位/对象', key: 'orgName' },
        ]
        return fields
      },
      resetQueryForm() {
        this.queryForm = {
          sheetcode: '',
          sheetname: '',
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
          orgName: '',
          auditStaffId: '',
          auditOrgId: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await dgListall(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      // handleAdd() {
      //   this.$refs['edit'].showEdit('add', null)
      // },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await dgDetail({ sheetid: row.sheetId })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      // async handleEdit(row) {
      //   const data = await dgDetail({ sheetid: row.sheetid })
      //   await this.$refs['edit'].showEdit('edit', data.data)
      // },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      // handleDelete(row) {
      //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
      //     const { msg, code } = await riskDel({
      //       riskid: row.riskid,
      //     })
      //     if (code == 0) {
      //       this.$baseMessage(msg, 'success')
      //     } else {
      //       this.$baseMessage(msg, 'error')
      //     }
      //     await this.fetchData()
      //   })
      // },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleObject() {
        this.$refs['auditree'].showEdit()
      },
      getChildlistObj(val, flag) {
        if (flag == 'right') {
          this.pdDx = 'yh'
          this.$set(this.queryForm, 'orgName', val[0].realname)
          this.$set(this.queryForm, 'auditStaffId', val[0].staffid)
          this.$set(this.queryForm, 'auditOrgId', '')
        } else {
          this.pdDx = 'bm'
          this.$set(this.queryForm, 'auditOrgId', val.id)
          this.$set(this.queryForm, 'orgName', val.name)
          this.$set(this.queryForm, 'auditStaffId', '')
        }
        this.$forceUpdate()
      },

      getAuditee(val, data) {
        // console.log(val, data)
        if (data == 'left') {
          this.pdDx = 'bm'
          const names = val.map((res) => res.name).toString()
          const ids = val.map((res) => res.id).toString()
          this.$set(this.queryForm, 'orgName', names)
          this.$set(this.queryForm, 'auditOrgId', ids)
          this.$set(this.queryForm, 'auditStaffId', '')
        } else {
          this.pdDx = 'yh'
          this.$set(this.queryForm, 'orgName', val[0].realname)
          this.$set(this.queryForm, 'auditStaffId', val[0].staffid)
          this.$set(this.queryForm, 'auditOrgId', '')
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
