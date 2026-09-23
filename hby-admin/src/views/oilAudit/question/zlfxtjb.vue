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
                v-model="queryForm.pc"
                clearable
                placeholder="批次"
                v-if="item.name === '批次'"
              />
              <el-input
                v-model="queryForm.projectname"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.auditorgname"
                clearable
                placeholder="被审计单位名称"
                v-if="item.name === '被审计单位名称'"
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
          label="序号"
          type="index"
          width="80">
        </el-table-column>
        <el-table-column
          align="center"
          label="批次"
          prop="pc"
          width="140"
        >
          <!-- <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.pc }}
            </el-button>
          </template> -->
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            width="240"
            label="项目承担单位（实施单位）"
            v-if="item.name === '项目承担单位（实施单位）'"
            prop="ssdw"
          />
          <el-table-column
            align="center"
            width="240"
            label="被审计单位名称"
            v-if="item.name === '被审计单位名称'"
            prop="auditorgname"
          />
          <el-table-column
            align="center"
            width="240"
            label="审计项目名称"
            v-if="item.name === '审计项目名称'"
            prop="projectname"
          />
          <el-table-column
            align="center"
            label="项目级别（二级机构/三级机构）"
            v-if="item.name === '项目级别（二级机构/三级机构）'"
            prop=""
          >
          </el-table-column>
          >
          <el-table-column
            align="center"
            label="业务领域"
            v-if="item.name === '业务领域'"
            prop=""
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="具体业务"
            v-if="item.name === '具体业务'"
            prop=""
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            width="140"
            label="经责科负责人"
            v-if="item.name === '经责科负责人'"
            prop="ksfzr"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            width="240"
            label="审计组人数"
            v-if="item.name === '审计组人数'"
            prop="xmzcy"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            width="140"
            label="现场审计开始日期"
            v-if="item.name === '现场审计开始日期'"
            prop="xcsrarttime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            width="140"
            label="实际现场结束日期"
            v-if="item.name === '实际现场结束日期'"
            prop="xcendtime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="实际工作天数"
            v-if="item.name === '实际工作天数'"
            prop="sj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="投入资源（人日）"
            v-if="item.name === '投入资源（人日）'"
            prop="trzy"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="工效比"
            v-if="item.name === '工效比'"
            prop="gxb"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="复合底稿数量"
            v-if="item.name === '复合底稿数量'"
            prop="fhdg"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="问题底稿数量"
            v-if="item.name === '问题底稿数量'"
            prop="wtdg"
            show-overflow-tooltip
          />
        </div>

        <el-table-column width="1" />
      </el-table>
    </el-card>

    <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
    <!-- <DraftInfo ref="edit" @fetch-data="fetchData" /> -->
    <!-- 项目级别（二级机构/三级机构）子组件 -->
    <!-- <Company ref="audiTree" @submit="getChildlistObj"></Company> -->
  </div>
</template>

<script>
  import { sjglQutlitgetlist } from '@/oapi/audit/implement'
  import { formatDate, formatDay } from '@/utils/index'
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
          pc: '',
          projectname: '',
          auditorgname: '',
        },
        filedAll: [
          { name: '项目承担单位（实施单位）' },
          { name: '被审计单位名称' },
          { name: '审计项目名称' },
          // { name: '项目级别（二级机构/三级机构）' },
          // { name: '业务领域' },
          // { name: '具体业务' },
          { name: '经责科负责人' },
          { name: '审计组人数' },
          { name: '现场审计开始日期' },
          { name: '实际现场结束日期' },
          { name: '实际工作天数' },
          { name: '投入资源（人日）' },
          { name: '工效比' },
          { name: '复合底稿数量' },
          { name: '问题底稿数量' },
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
          { name: '批次', key: 'pc' },
          { name: '项目名称', key: 'projectname' },
          { name: '被审计单位名称', key: 'auditorgname' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          pc: '',
          projectname: '',
          auditorgname: '',
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
        const { data } = await sjglQutlitgetlist(this.queryForm)
        this.list = data
        this.total = data.length
        this.listLoading = false
      },
      // handleAdd() {
      //   this.$refs['edit'].showEdit('add', null)
      // },
      async handleDetail(row) {
        // const data = await dgDetail({ sheetid: Number(row.sheetId) })
        await this.$refs['edit'].showEdit('detail', row)
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
