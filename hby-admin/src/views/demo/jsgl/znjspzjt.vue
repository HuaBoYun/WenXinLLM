<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <el-form ref="jtForm" :model="jtForm" label-width="160px">
          <el-row :gutter="14" style="display: flex; justify-content: flex-end">
            <template v-if="isUpdate">
              <el-button type="primary" @click="handleSave">保存</el-button>
              <el-button @click="handleCancel">取消</el-button>
            </template>
            <template v-else>
              <el-button type="success" @click="handleUpdate">修改</el-button>
            </template>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="所属集团">
                <el-input
                  v-if="isUpdate"
                  v-model="jtForm.membershipGroup"
                ></el-input>
                <span v-else>{{ '集团A' }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所有单据新增自动提交">
                <el-checkbox
                  v-if="isUpdate"
                  v-model="jtForm.membershipGroup"
                ></el-checkbox>
                <span v-else>{{ '否' }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所有单据自动结算">
                <el-checkbox
                  v-if="isUpdate"
                  v-model="jtForm.membershipGroup"
                ></el-checkbox>
                <span v-else>{{ '否' }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="24">
              <div class="show_line_box">
                <div class="title_l">
                  <i
                    @click="handleIsUnfold"
                    v-if="isUnfoldAuditShow"
                    class="el-icon-minus"
                  ></i>
                  <i @click="handleIsUnfold" v-else class="el-icon-plus"></i>
                  操作信息
                </div>
                <div class="line"></div>
              </div>
              <template v-if="isUnfoldAuditShow">
                <el-col :span="12">
                  <el-form-item label="最后修改人">
                    <el-input
                      v-if="isUpdate"
                      v-model="jtForm.membershipGroup"
                    ></el-input>
                    <span v-else>{{ '否' }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="最后修改时间">
                    <el-input
                      v-if="isUpdate"
                      v-model="jtForm.membershipGroup"
                    ></el-input>
                    <span v-else>{{ '2025-02-24 14：59' }}</span>
                  </el-form-item>
                </el-col>
              </template>
            </el-col>
          </el-row>
        </el-form>
        <!-- <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.jsFinance"
                clearable
                placeholder="所属组织"
                v-if="item.name === '所属组织'"
              />
              <el-date-picker
                v-model="queryForm.day"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '时间'"
              ></el-date-picker>
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="收款财务组织"
                v-if="item.name === '收款财务组织'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="付款财务组织"
                v-if="item.name === '付款财务组织'"
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
        </vab-query-form-left-panel> -->
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel style="width: 100%">
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
        <el-button type="success" v-if="isUpdate" @click="handleEdit">
          新建
        </el-button>
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column
          align="center"
          label="币种"
          prop="currency"
          width="160"
        >
          <template slot-scope="scope">
            <el-input
              v-if="scope.row.editing"
              v-model="scope.row.currency"
            ></el-input>
            <span v-else>{{ scope.row.currency }}</span>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '新增后自动提交'"
            align="center"
            label="新增后自动提交"
            prop="addAutoSubmit"
            show-overflow-tooltip
            width="180"
          >
            <template slot-scope="scope">
              <el-checkbox
                v-if="scope.row.editing"
                v-model="scope.row.addAutoSubmit"
              ></el-checkbox>
              <span v-else>{{ scope.row.addAutoSubmit }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '来源单据类型'"
            align="center"
            width="150"
            show-overflow-tooltip
          >
            <template slot="header" slot-scope="scope">
              <span style="color: red">*</span>
              <span>来源单据类型</span>
            </template>
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.sourceReceiptsType"
              ></el-input>
              <span v-else>{{ scope.row.sourceReceiptsType }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '自动结算'"
            align="center"
            label="自动结算"
            prop="autoSettlement"
            show-overflow-tooltip
            width="150"
          >
            <template slot-scope="scope">
              <el-checkbox
                v-if="scope.row.editing"
                v-model="scope.row.autoSettlement"
              ></el-checkbox>
              <span v-else>{{ scope.row.autoSettlement }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '来源交易类型'"
            align="center"
            label="来源交易类型"
            prop="sourceDealType"
            show-overflow-tooltip
            width="160"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.sourceDealType"
              ></el-input>
              <span v-else>{{ scope.row.sourceDealType }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '结算方式'"
            align="center"
            label="结算方式"
            prop="settlementWay"
            show-overflow-tooltip
            width="180"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.settlementWay"
              ></el-input>
              <span v-else>{{ scope.row.settlementWay }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '公司/个人'"
            align="center"
            label="公司/个人"
            prop="company"
            show-overflow-tooltip
            width="150"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.company"
              ></el-input>
              <span v-else>{{ scope.row.company }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '结算方式默认值'"
            align="center"
            label="结算方式默认值"
            prop="settlementMethodDefault"
            show-overflow-tooltip
            width="150"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.settlementMethodDefault"
              ></el-input>
              <span v-else>{{ scope.row.settlementMethodDefault }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '银行账户默认值'"
            align="center"
            label="银行账户默认值"
            prop="bankAccountDefault"
            show-overflow-tooltip
            width="140"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.bankAccountDefault"
              ></el-input>
              <span v-else>{{ scope.row.bankAccountDefault }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '银行账户备选值'"
            align="center"
            label="银行账户备选值"
            prop="bankAccountAlternative"
            show-overflow-tooltip
            width="180"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.bankAccountAlternative"
              ></el-input>
              <span v-else>{{ scope.row.bankAccountAlternative }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '资金计划项目默认值'"
            align="center"
            label="资金计划项目默认值"
            prop="capitalPlanProjectDefault"
            show-overflow-tooltip
            width="140"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.capitalPlanProjectDefault"
              ></el-input>
              <span v-else>{{ scope.row.capitalPlanProjectDefault }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '现金流量项目默认值'"
            align="center"
            label="现金流量项目默认值"
            prop="cashFlowItemDefault"
            show-overflow-tooltip
            width="180"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.cashFlowItemDefault"
              ></el-input>
              <span v-else>{{ scope.row.cashFlowItemDefault }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '摘要默认值'"
            align="center"
            label="摘要默认值"
            prop="summaryDefault"
            show-overflow-tooltip
            width="180"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.summaryDefault"
              ></el-input>
              <span v-else>{{ scope.row.summaryDefault }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '附言默认值'"
            align="center"
            label="附言默认值"
            prop="postscriptDefault"
            show-overflow-tooltip
            width="160"
          >
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.editing"
                v-model="scope.row.postscriptDefault"
              ></el-input>
              <span v-else>{{ scope.row.postscriptDefault }}</span>
            </template>
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            <el-button type="text" @click="handleDelete(scope.$index)">
              删除
            </el-button>
            <el-button type="text" @click="handleInsertRow(scope.$index)">
              插入行
            </el-button>
            <!-- <el-button
              type="text"
              @click="handleEdit(scope.row, false)"
              :disabled="!!scope.row.spzt"
            >
              修改
            </el-button> -->
            <!-- <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    @click="handleDelete(scope.row)"
                    type="text"
                    :disabled="!!scope.row.spzt"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown> -->
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
    <Edit ref="edit" @fetch-data="fetchData"></Edit>
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    fpzyksry,
  } from '@/oapi/audit/project'
  import Edit from './components/zjfkEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import Template from '@/views/contract/contractManage/template.vue'

  export default {
    name: 'znjspzjt',
    mixins: [searchTableMixis],
    components: {
      Edit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        editIndex: null,
        isUpdate: false,
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        jtForm: {
          membershipGroup: undefined,
        },
        queryForm: {
          jsFinance: undefined,
          receiptsNo: undefined,
          day: undefined,
          supplier: undefined,
          startStatus: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '币种' },
          { name: '新增后自动提交' },
          { name: '来源单据类型' },
          { name: '自动结算' },
          { name: '来源交易类型' },

          { name: '结算方式' },
          { name: '公司/个人' },
          { name: '结算方式默认值' },
          { name: '银行账户默认值' },

          { name: '银行账户备选值' },
          { name: '资金计划项目默认值' },
          { name: '现金流量项目默认值' },
          { name: '摘要默认值' },

          { name: '附言默认值' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-znjspzjt-search',
        tableKey: 'globalTreasurer-jsgl-znjspzjt-list',
        searchMore: false,
        select: [],
        optionsStatus: [
          {
            value: '1',
            label: '开启',
          },
          {
            value: '2',
            label: '关闭',
          },
        ],
        isUnfoldAuditShow: false,
        activeName: 'second',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
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
          { name: '所属组织', key: 'jsFinance' },
          { name: '时间', key: 'day' },
          { name: '收款财务组织', key: 'receiptsNo' },
          { name: '付款财务组织', key: 'supplier' },
        ]
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
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
        let { ...other } = this.queryForm
        const {
          data: { tlist, totalRecord, currProjectId },
        } = await implementPlanList({
          ...other,
        })
        this.listLoading = false
        return
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.planNum = tlist[0].projectCode
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },
      handleUpdate() {
        this.isUpdate = true
      },
      handleSave() {
        let hasEmpty = false
        this.list.forEach((row) => {
          if (row.editing && row.sourceReceiptsType.trim() === '') {
            hasEmpty = true
            return
          }
          if (row.editing) {
            this.$set(row, 'editing', false) // 结束编辑状态
          }
        })

        if (hasEmpty) {
          this.$message.error('请先完成所有编辑行的输入或取消编辑')
          return
        }
        this.isUpdate = false
      },
      handleCancel() {
        this.isUpdate = false
        this.list = []
      },
      handleEdit() {
        this.list.push({
          currency: '',
          addAutoSubmit: '',
          sourceReceiptsType: '',
          autoSettlement: '',
          sourceDealType: '',
          settlementWay: '',
          company: '',
          settlementMethodDefault: '',
          bankAccountDefault: '',
          bankAccountAlternative: '',
          capitalPlanProjectDefault: '',
          cashFlowItemDefault: '',
          summaryDefault: '',
          postscriptDefault: '',
          editing: true,
        })
      },
      handleDelete(index) {
        this.list.splice(index, 1)
        // this.$baseConfirm('你确定要删除当前项吗', null, async () => {
        //   const { msg, code } = await implementPlanDelete({ ids: row.id })
        //   if (code == 1) {
        //     this.$baseMessage(msg, 'success')
        //     await this.fetchData()
        //   } else {
        //     this.$baseMessage(msg, 'error')
        //   }
        // })
      },
      handleInsertRow(index) {
        this.list.splice(index, 0, {
          currency: '',
          addAutoSubmit: '',
          sourceReceiptsType: '',
          autoSettlement: '',
          sourceDealType: '',
          settlementWay: '',
          company: '',
          settlementMethodDefault: '',
          bankAccountDefault: '',
          bankAccountAlternative: '',
          capitalPlanProjectDefault: '',
          cashFlowItemDefault: '',
          summaryDefault: '',
          postscriptDefault: '',
          editing: true,
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      color(row) {
        if (row.id == this.currProjectId) {
          return { color: '#7fcf7c' }
        } else {
          return { color: '' }
        }
      },
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      handleClick(tab, event) {
        console.log(tab, event)
      },
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
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
  .show_line_box {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  .title_l {
    width: 100px;
    text-align: center;
    cursor: pointer;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px dashed #cccccc6e;
  }
</style>
