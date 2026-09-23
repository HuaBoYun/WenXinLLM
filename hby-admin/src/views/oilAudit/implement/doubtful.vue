<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
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
    </el-card>
  </vab-query-form>
  
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
      <el-button type="success" @click="handleAdd">新建</el-button>
    </vab-query-form-right-panel>
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
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <!-- <el-dropdown-item @click.native="handleDelete(row)">
                删除
              </el-dropdown-item> -->
              <el-dropdown-item @click.native="sendToManuscript(row)">
                发送至底稿
              </el-dropdown-item>
              <el-dropdown-item @click.native="sendToManuscriptAtt(row)">
                发送至底稿附件
              </el-dropdown-item>
              <el-dropdown-item @click.native="sendToDoubtful(row)">
                发送至疑点
              </el-dropdown-item>
              <el-dropdown-item @click.native="sendToDefect(row)">
                发送至缺陷
              </el-dropdown-item>
              <el-dropdown-item @click.native="sendToRisk(row)">
                发送至风险
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

    <MyDraftInfo ref="manuscript" @fetch-data="fetchData" />
    <DoubtfulInfo2 ref="doubtful" @fetch-data="fetchData" />
    <FlawInfo
      v-if="flawStatus"
      @close="
        () => {
          this.flawStatus = true
        }
      "
      ref="flaw"
      @fetch-data="fetchData"
    />
    <RiskInfo ref="risk" @fetch-data="fetchData" />
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
    sendManuscriptGzdg,
    sendRisk,
  } from '@/oapi/audit/implement'
  import RiskInfo from '@/views/oilAudit/question/components/RiskInfo'
  import DoubtfulInfo2 from './components/doubtful/DoubtfulInfo'
  import FlawInfo from './components/doubtful/FlawInfo'
  import DoubtfulInfo from './components/DoubtfulInfo'
  import MyDraftInfo from './components/myDraftInfo'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    components: {
      DoubtfulInfo,
      MyDraftInfo,
      DoubtfulInfo2,
      FlawInfo,
      RiskInfo, filterSearch, filterTable
    },
    mixins: [searchTableMixis],
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
          pageNumber: 1,
          pageSize: 20,
        },
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
        localKey: 'oilAudit-implement-doubtful-search',
        tableKey: 'oilAudit-implement-doubtful-list',
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
          { name: '疑点编号', key: 'dpnumber' },
          { name: '疑点名称', key: 'dpname' },
        ]
      },
      async sendToManuscript(row) {
        const data = await sendManuscript({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['manuscript'].showEdit('疑点', data.data)
      },
      async sendToManuscriptAtt(row) {
        const data = await sendManuscriptGzdg({
          auditStaff: row.editor,
          // auditedUnit: row.auditedunit,
          selectIds: row.dpointid,
          type: 'nbsj',
        })
      },
      async sendToDoubtful(row) {
        const data = await sendDoubtful({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['doubtful'].showEdit(data.data)
      },
      async sendToDefect(row) {
        this.flawStatus = true

        const data = await sendDefect({
          selectIds: row.dpointid,
          type: 'nbsj',
        })

        this.$nextTick(() => {
          this.$refs['flaw'].showEdit(data.data)
        })
      },
      async sendToRisk(row) {
        const data = await sendRisk({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['risk'].showEdit('发送至风险', data.data)
      },
      resetQueryForm() {
        this.queryForm = {
          dpnumber: '',
          dpname: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
        doubtfulList(this.queryForm).then(res => {
          const {
            data: {
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = res
          this.list = list
          this.total = total
        }).finally(() => {
          this.listLoading = false
        })
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add')
      },
      async handleDetail(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
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
.system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  
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
    width: 100%;
  }
</style>