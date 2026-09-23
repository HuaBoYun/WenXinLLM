<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
                v-model="queryForm.dpnumber"
                clearable
                placeholder="疑点编号"
                v-if="item.name === '疑点编号'"
              />
              <el-input
                v-model="queryForm.dpname"
                clearable
                placeholder="疑点名称"
                v-if="item.name === '疑点名称'"
              />
              <el-date-picker
                v-model="queryForm.dateRange"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间范围'"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
            <el-form-item style="cursor: pointer">
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

    <el-card shadow="never">
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
          <el-button type="success" @click="handleAdd">新建</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="疑点编号"
          prop="dpnumber"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.dpnumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="疑点名称"
            v-if="item.name === '疑点名称'"
            prop="dpname"
          />
          <el-table-column
            align="center"
            label="疑点描述"
            v-if="item.name === '疑点描述'"
            prop="dpdescribe"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="测试结果"
            v-if="item.name === '测试结果'"
            prop="testresult"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="编制人"
            v-if="item.name === '编制人'"
            prop="editor"
            show-overflow-tooltip
          />
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="createId != row.createstaffid"
            >
              删除
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" @click="handleExport(row)">
                    导出
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="sendToManuscript(row)">
                    发送至底稿
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="sendToManuscriptAtt(row)">
                    发送至底稿附件
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="sendToDoubtful(row)">
                    发送至疑点
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="sendToDefect(row)">
                    发送至缺陷
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="sendToRisk(row)">
                    发送至风险
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="sendToQZD(row)">
                    发送至审计取证单
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <DoubtfulInfo ref="edit" @fetch-data="fetchData" />
    <DgList ref="dg" @fetch-data="fetchData" />

    <MyDraftInfo ref="manuscript" @fetch-data="fetchData" />
    <FlawInfo ref="flaw" @fetch-data="fetchData" />
    <!-- <FlawInfo
      v-if="flawStatus"
      @close="
        () => {
          this.flawStatus = false
        }
      "
      ref="flaw"
      @fetch-data="fetchData"
    /> -->
    <RiskInfo ref="risk" @fetch-data="fetchData" />
    <EvidenceInfo
      ref="qzd"
      @fetch-data="fetchData"
      :effectDetail="effectDetail"
    />
  </div>
</template>

<script>
  import {
    doubtfulDelete,
    doubtfulDetail,
    doubtfulList,
    sendDefect,
    sendDoubtful,
    sendManuscript,
    sendRisk,
    sendQZD,
    createImPlementDetail,
    doubtfulExport,
  } from '@/api/audit/implement'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from './components/DoubtfulInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DgList from './components/doubtful/selectDg.vue'
  import EvidenceInfo from './components/auditEvidenceInfo.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
      DoubtfulInfo,
      MyDraftInfo,
      FlawInfo,
      RiskInfo,
      DgList,
      EvidenceInfo,
    },
    data() {
      return {
        flawStatus: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          dpnumber: '',
          dpname: '',
          auditClueType: '',
          dateRange: [],
          pageNumber: 1,
          pageSize: 20,
        },
        effectDetail: {},
        // 筛选列表配置
        filedAll: [
          { name: '疑点名称' },
          { name: '疑点描述' },
          { name: '测试结果' },
          { name: '编制人' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-implement-deubtful-search',
        tableKey: 'audit-implement-deubtful-list',
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        auditClueTypeOptions: [],
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
          { name: '疑点编号', key: 'dpnumber' },
          { name: '疑点名称', key: 'dpname' },
          { name: '时间范围', key: 'dateRange' },
        ]
      },
      async sendToManuscript(row) {
        const data = await sendManuscript({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['manuscript'].showEdit('add', {}, data.data)
      },
      async sendToManuscriptAtt(row) {
        this.$refs['dg'].showEdit(row)
      },
      async sendToDoubtful(row) {
        const data = await sendDoubtful({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['edit'].showEdit('add', null, data.data)
      },
      async sendToDefect(row) {
        this.flawStatus = true

        const data = await sendDefect({
          selectIds: row.dpointid,
          type: 'nbsj',
        })

        this.$nextTick(() => {
          this.$refs['flaw'].showEdit('add', {}, {}, data.data)
        })
      },
      async sendToRisk(row) {
        const data = await sendRisk({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['risk'].showEdit('add', null, data.data)
      },
      async sendToQZD(row) {
        const data = await sendQZD({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['qzd'].showEdit('add', null, data.data)
        const result = await createImPlementDetail()
        this.effectDetail = result.data.pj
      },
      resetQueryForm() {
        this.queryForm = {
          dpnumber: '',
          dpname: '',
          auditClueType: '',
          dateRange: [],
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        const { dateRange, ...otherParams } = this.queryForm
        const [startDate, endDate] = dateRange || []
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await doubtfulList({
          ...otherParams,
          startDate: startDate || '',
          endDate: endDate || '',
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit('add')
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await doubtfulDelete({ dpointid: row.dpointid })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport(row) {
        const data = await doubtfulExport({ selectIds: row.dpointid })
        let fileName = '疑点管理.xlsx'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
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
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
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
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
