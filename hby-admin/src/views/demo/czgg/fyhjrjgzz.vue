<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <czggTree />
      </div>
      <div class="right">
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
                <el-form-item
                  :prop="item.key"
                  v-for="(item, index) in searchItem"
                  :key="index"
                >
                  <el-select
                    v-model="queryForm.oldName"
                    placeholder="所属组织"
                    clearable
                    v-if="item.name === '所属组织'"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="金融机构名称"
                    v-if="item.name === '金融机构名称'"
                  />
                  <el-input
                    v-model="queryForm.code"
                    clearable
                    placeholder="编码"
                    v-if="item.name === '编码'"
                  />
                  <el-select
                    v-model="queryForm.type"
                    placeholder="启用状态"
                    clearable
                    v-if="item.name === '启用状态'"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
            <vab-query-form-left-panel>
              <span></span>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel>
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
              <el-button type="success" @click="handleAdd(false, false)">
                新建
              </el-button>
              <!-- <el-button type="success" @click="handleSend()">下发</el-button>
              <el-button type="danger" @click="handleBack()">撤回</el-button> -->
            </vab-query-form-right-panel>
          </vab-query-form>

          <el-table
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            @select-all="handleSelectAll"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55" />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                v-if="item.name === '序号'"
                prop="date11"
                label="序号"
                align="center"
              />
              <el-table-column
                v-if="item.name === '金融机构编码'"
                prop="date"
                label="金融机构编码"
              />
              <el-table-column
                v-if="item.name === '金融机构名称'"
                prop="date1"
                align="center"
                label="金融机构名称"
              />
              <el-table-column
                v-if="item.name === '省'"
                prop="date2"
                align="center"
                label="省"
              />
              <el-table-column
                v-if="item.name === '市'"
                align="center"
                prop="date3"
                label="市"
              />
              <el-table-column
                v-if="item.name === '电话'"
                prop="date7"
                label="电话"
                align="center"
              />
              <el-table-column
                align="center"
                label="地址"
                v-if="item.name === '地址'"
                prop="createTime"
                show-overflow-tooltip
              />
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
                  @click="handleEdit(row)"
                  :disabled="!!row.status"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      @click.native="handleManage(row)"
                      :disabled="!row.status"
                    >
                      办理
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleApproval(row)"
                      :disabled="!!row.status"
                    >
                      提交审批
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleDelete(row)"
                      :disabled="!!row.status"
                    >
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetchData="fetchData" />

    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    handleTabs,
    noticeCancel,
    reportExport,
    xiafaListNew,
    sjlxjytzSavePersonInfo,
    distributionBack,
  } from '@/oapi/audit/preparation'
  import {
    proposalNoticeList,
    proposalNoticeDelete,
  } from '@/api/monitor/question'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import projectManage from '@/components/selectPerson'
  import Edit from './components/fyhjrjgqjEdit.vue'
  import czggTree from './components/czggTree.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'fyhjrjgzz',
    components: {
      Edit,
      czggTree,
      projectManage,
      filterSearch,
      filterTable,
      ProcessList: () =>
        import('@/views/contract/contractManage/components/ProcessList'),
      WfqdDeal: () => import('@/views/msg/components/options/WfqdDeal'),
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        options: [
          {
            value: '选项1',
            label: '开启',
          },
          {
            value: '选项2',
            label: '关闭',
          },
        ],
        filedAll: [
          { name: '序号' },
          { name: '金融机构编码' },
          { name: '金融机构名称' },
          { name: '省' },
          { name: '市' },
          { name: '电话' },
          { name: '地址' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-czgg-fyhjrjgqj-search',
        tableKey: 'globalTreasurer-czgg-fyhjrjgqj-list',
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })
          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      getFiled() {
        return [
          { name: '所属组织', key: 'oldName' },
          { name: '金融机构名称', key: 'name' },
          { name: '编码', key: 'code' },
          { name: '启用状态', key: 'type' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          name: '',
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
      async toVoid(row) {
        let res = await noticeCancel({
          adviceid: row.adviceid,
        })
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      async fetchData() {
        this.listLoading = true
        const { date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (date) {
          startDate = date[0]
          endDate = date[1]
        }
        const {
          data: { tlist, totalRecord },
        } = await proposalNoticeList({ ...other, startDate, endDate })
        this.listLoading = false
        this.total = totalRecord
        // this.list = tlist.map((item) => ({
        //   ...item,
        //   createUser: item.createUser ? item.createUser.realname : "",
        // }));
        this.setCheckedRows()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
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
      handleBack() {
        if (this.select && this.select.length > 0) {
          const ids = this.select.map((res) => res.id).toString()
          distributionBack({ ids }).then((res) => {
            if (res.code == 1) {
              this.$message.success('成功')
              this.fetchData()
              this.select = []
            }
          })
        } else {
          this.$baseMessage(
            '请选择需要撤回的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.name)
        const names = val.map((res) => res.staffid)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }
        //保存下发的信息
        sjlxjytzSavePersonInfo({
          noticeId: ids.toString(),
          userIds: names.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '666',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
        // let ids = '',
        //   ryIds = val[0].staffid
        // this.multipleSelection.map((v) => {
        //   ids += v.id + ','
        // })
        // ids = ids.substring(0, ids.length - 1)
        // const { msg, code } = await proposalNoticeGetDistributeList({
        //   noticeId: ids,
        //   userIds: ryIds,
        // })
      },
      handleAdd(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, false)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await proposalNoticeDelete({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
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
        const i = this.select.findIndex((x) => x.id == row.id)
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
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
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
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(126, row.id)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 126,
        })
        this.listLoading = false
        this.$refs.wfqddeal.show(res.data, false)
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
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    min-width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 0 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
