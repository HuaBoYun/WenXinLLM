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
                v-model="queryForm.advicecoed"
                clearable
                placeholder="审计通知书编号"
                v-if="item.name === '审计通知书编号'"
              />
              <el-input
                v-model="queryForm.advicename"
                clearable
                placeholder="审计通知书名称"
                v-if="item.name === '审计通知书名称'"
              />
              <el-date-picker
                v-model="queryForm.Date"
                clearable
                end-placeholder="结束日期"
                v-if="item.name === '日期'"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                :style="{ width: '100%' }"
                type="daterange"
                value-format="yyyy-MM-dd"
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
          <el-button type="success" @click="handleSend">下发</el-button>

          <el-button type="success" @click="handleAdd(false, false)">
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />

        <el-table-column align="center" label="审计通知书编号" width="170">
          <template #default="{ row }">
            <el-button type="text" @click="handleAdd(row, true)">
              {{ row.advicecoed }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '审计通知书名称'"
            align="center"
            label="审计通知书名称"
            prop="advicename"
          />
          <el-table-column
            v-if="item.name === '创建人'"
            align="center"
            label="创建人"
            prop="tblCreater.realname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建时间'"
            align="center"
            label="创建时间"
            prop="creatrtime"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            align="center"
            label="状态"
            prop="status"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              {{
                scope.row.status == 0
                  ? '未审批'
                  : scope.row.status == 1
                  ? '已作废'
                  : scope.row.status == 2
                  ? '审批中'
                  : scope.row.status == 3
                  ? '需调整'
                  : scope.row.status == 4
                  ? '已完成'
                  : '已终止'
              }}
            </template>
          </el-table-column> -->
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleAdd(row, false)"
              :disabled="row.status !== 0 || createId != row.createstaffid"
            >
              修改
            </el-button>
            <!-- <el-button
              type="text"
              @click="handleTab(row)"
              :disabled="row.status !== 0"
            >
              审批
            </el-button> -->
            <el-button
              type="text"
              @click="toVoid(row)"
              :disabled="row.status !== 0 || createId != row.createstaffid"
            >
              作废
            </el-button>
            <!-- <el-button type="text" @click="handleExport(row)" disabled>
              导出
            </el-button> -->
            <el-button type="text" @click="$refs.sendRef.showEdit(row)">
              下发列表
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
    <NoticeInfo ref="edit" @fetch-data="fetchData" />
    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />

    <!-- 下发列表 -->
    <SendList ref="sendRef" />
  </div>
</template>

<script>
  import {
    getNoticeList,
    handleTabs,
    noticeCancel,
    reportExport,
    sjtzXF,
  } from '@/api/audit/preparation'
  import { doDelete } from '@/api/table'
  import { UTCformat } from '@/utils'
  import NoticeInfo from './components/NoticeInfo'
  import SendList from './components/SendList'
  import projectManage from '@/components/selectPerson'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, NoticeInfo, projectManage, SendList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          advicecoed: '',
          advicename: '',
          pageNumber: 1,
          pageSize: 20,
          Date: [],
        },
        select: [],
        // 筛选列表配置
        filedAll: [
          { name: '审计通知书名称' },
          { name: '创建人' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-prepare-notice-search',
        tableKey: 'audit-prepare-notice-list',
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
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
          { name: '审计通知书编号', key: 'advicecoed' },
          { name: '审计通知书名称', key: 'advicename' },
          { name: '日期', key: 'Date' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          advicecoed: '',
          advicename: '',
          pageNumber: 1,
          pageSize: 20,
          Date: [],
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
      async toVoid(row) {
        let res = await noticeCancel({
          adviceid: row.adviceid,
        })
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
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
            pageInfo: { tlist, totalRecord },
          },
        } = await getNoticeList({ ...other, startDate, endDate })
        this.list = tlist
        this.list.forEach((item) => {
          item.creatrtime = UTCformat(item.creatrtime)
        })
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },
      handleAdd(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
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
      handleTab(value) {
        handleTabs({
          adviceid: value.adviceid,
        }).then((res) => {
          if (res.msg === '成功') this.fetchData()
        })
      },
      async handleExport(row) {
        const data = await reportExport({ adviceid: row.adviceid })
        let fileName = 'test'
        let blob = new Blob([data], {
          type: 'application/msword',
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
      handleSelection(val, row) {
        console.log(row)
        const i = this.select.findIndex((x) => x.adviceid == row.adviceid)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.adviceid == row.adviceid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.adviceid == row.adviceid)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.adviceid == item.adviceid
              }),
              true
            )
          })
        })
      },
      handleSend() {
        if (this.select && this.select.length > 0) {
          this.$refs.manage.showEdit()
        } else {
          this.$baseMessage(
            '请选择需要下发的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.adviceid)
        const titles = this.select.map((res) => res.advicename)
        const names = val.map((res) => res.staffid)
        const userNames = val.map((res) => res.realname)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'znsj',
            })
          }
        }
        //下发保存
        sjtzXF({
          ids: ids.toString(),
          userids: names.toString(),
          usernames: userNames.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '219',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
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
