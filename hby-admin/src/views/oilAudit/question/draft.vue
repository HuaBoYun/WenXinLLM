<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
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
                disabled
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                @click="handleObject"
                style="margin-left: 10px"
                type="primary"
                size="small"
              >
                选择
              </el-button>
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
              <el-button native-type="submit" type="primary" @click="resetSearch">
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
          </el-form>
        </vab-query-form-top-panel>
      </vab-query-form>
    </el-card>

    <el-card shadow="never">
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
          <el-table-column
            align="center"
            label="审计目标"
            v-if="item.name === '审计目标'"
            prop="sheetTarget"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="被审计对象"
            v-if="item.name === '被审计对象'"
            prop="targetName"
          >
            <template #default="{ row }">
              {{ row.orgname || row.auditusername }}
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
                row.state == '2'
                  ? '复核中'
                  : row.state == '3'
                  ? '复核终止'
                  : row.state == '4'
                  ? '复核通过'
                  : row.state == '5'
                  ? '已退回'
                  : '未复核'
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
    <Company ref="audiTree" @submit="getChildlistObj"></Company>
  </div>
</template>

<script>
  import { dgDetail, dgListall } from '@/oapi/audit/question'
  import { formatDate } from '@/utils/index'
  import DraftInfo from './components/DraftInfo'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    components: {
      DraftInfo,
      Company: () => import('@/components/Company.vue'),
      filterSearch,
      filterTable
    },
    mixins: [searchTableMixis],
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
        filedAll: [
          { name: '项目名称' },
          { name: '底稿名称' },
          { name: '被审计对象' },
          { name: '审计目标' },
          { name: '拟稿人' },
          { name: '拟稿日期' },
          { name: '所属项目' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-oilAudit-question-search',
        tableKey: 'oilAudit-oilAudit-question-list',
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
          { name: '底稿编号', key: 'sheetcode' },
          { name: '底稿名称', key: 'sheetname' },
          { name: '项目名称', key: 'projectName' },
          { name: '被审计单位/对象', key: 'orgName' },
        ]
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await dgListall(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      // handleAdd() {
      //   this.$refs['edit'].showEdit('add', null)
      // },
      async handleDetail(row) {
        const data = await dgDetail({ sheetid: Number(row.sheetId) })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      // async handleEdit(row) {
      //   const data = await dgDetail({ sheetid: row.sheetid })
      //   await this.$refs['edit'].showEdit('edit', data.data)
      // },
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
        this.$refs['audiTree'].showEdit()
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
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
