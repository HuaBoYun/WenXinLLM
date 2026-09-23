<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <el-card shadow="never">
          <project-data-tree @getChildParam="setTree" />
        </el-card>
      </div>
      <div class="right">
        <el-card shadow="never">
          <vab-query-form class="margin-b0">
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
                    v-model="queryForm.bugnumber"
                    clearable
                    placeholder="缺陷编号"
                    v-if="item.name === '缺陷编号'"
                  />
                  <el-select
                    v-if="item.name === '缺陷级别'"
                    v-model="queryForm.bugcriid"
                    placeholder="请输入缺陷级别"
                  >
                    <el-option
                      v-for="item in bugcriidList"
                      :label="item.bugcrilevel"
                      :value="item.bugcriid"
                      :key="item.bugcriid"
                    />
                  </el-select>
                  <el-date-picker
                    v-if="item.name === '发现日期'"
                    v-model="queryForm.Date"
                    clearable
                    end-placeholder="发现结束日期"
                    format="yyyy-MM-dd"
                    range-separator="-"
                    start-placeholder="发现开始日期"
                    :style="{ width: '100%' }"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
                <!-- <el-form-item>
                <el-select
                  v-model="queryForm.bugcrilevel"
                  placeholder="请输入缺陷性质"
                >
                  <el-option label="设计缺陷" value="设计缺陷" key="设计缺陷" />
                  <el-option label="执行缺陷" value="执行缺陷" key="执行缺陷" />
                </el-select>
              </el-form-item> -->
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
            <el-button type="success" @click="handleAdd">新建</el-button>
            <el-button type="primary" @click="handleExport">导出</el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="缺陷编号"
              prop="bugnumber"
              width="100"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.bugnumber }}
                </el-button>
              </template>
            </el-table-column>
            <!-- <el-table-column
            align="center"
            label="资料名称"
            prop="businessDescription"
          /> -->
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="缺陷描述"
                v-if="item.name === '缺陷描述'"
                prop="bugdescripte"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="发现时间"
                v-if="item.name === '发现时间'"
                prop="discovertime"
                show-overflow-tooltip
                :formatter="formatDate"
              />
              <el-table-column
                align="center"
                label="缺陷性质"
                v-if="item.name === '缺陷性质'"
                prop="bugproperty"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="缺陷级别"
                v-if="item.name === '缺陷级别'"
                prop="bugcrilevel"
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
                  @click="$refs['edit'].showEdit('edit', row)"
                >
                  修改
                </el-button>
                <el-button type="text" @click="handleDelete(row)">
                  删除
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
    <flaw-info ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/implement'
  import {
    bugcriidList,
    defectDel,
    defectDetail,
    defectFileExport,
    defectList,
  } from '@/api/internal/question'
  import { formatDay, parseTime } from '@/utils/index'
  import FlawInfo from './components/FlawInfo'
  import ProjectDataTree from './components/ProjectDataTree'
  import filterSearch from "@/components/filterSearch";
  import filterTable from "@/components/filterTable";
  import { searchTableMixis } from '@/mixis/index'
  export default {
    name: 'Download',
    components: { FlawInfo, ProjectDataTree, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        bugcriidList: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          bugnumber: '',
          bugcriid: '',
          orgid: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        orgId: '',
        current: undefined,
        flagTitle: false,
        filedAll: [
          { name: '缺陷描述' },
          { name: '发现时间' },
          { name: '缺陷性质' },
          { name: '缺陷级别' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-question-flaw-search',
        tableKey: 'audit-question-flaw-list',
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.fectchBugcriidList()
      //初始化树结构
      this.getTreeData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '缺陷编号', key: 'bugnumber' },
          { name: '缺陷级别', key: 'bugcriid' },
          { name: '发现日期', key: 'Date' },
        ]
      },
      async getTreeData() {
        let res = await findOrganizationByTreeAllss()
        this.orgId = res[0].id
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          bugnumber: '',
          bugcriid: '',
          orgid: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        }
      },
      async fectchBugcriidList() {
        const res = await bugcriidList()
        this.bugcriidList = res.data.list
      },
      setTree(node) {
        console.log(node)
        let id = node.pId === 1 ? undefined : node.id
        this.queryForm.orgid = id
        this.orgId = node.id
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
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await defectList({ ...other, startDate, endDate })
        this.list = list
        this.list.forEach((item) => {
          item.discovertime = parseTime(item.discovertime, '{y}-{m}-{d}')
        })
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        // const data = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleEdit(row) {
        const data = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await defectDel({
            bugid: row.bugid,
          })
          if (code == 0) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      async handleExport(row) {
        const data = await defectFileExport({ orgId: this.orgId })
        let fileName = 'test'
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
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    /* width: 200px; */
    /* width: 15%; */
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 85%;
  }
</style>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
