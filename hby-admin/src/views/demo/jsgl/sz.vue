<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <el-tree
          class="filter-tree"
          :data="dataTree"
          :props="defaultProps"
          default-expand-all
          ref="tree"
        ></el-tree>
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
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
                    placeholder="财务组织"
                    v-if="item.name === '财务组织'"
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
            </vab-query-form-left-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never" class="secondCard">
          <el-form label-width="200px">
            <el-form-item label="是否空白票据管理">
              <el-input
                v-model="rightForm.name"
                class="no-border-input"
              ></el-input>
            </el-form-item>
            <el-col :span="24">
              <div class="show_line_box">
                <div class="title_l">
                  <i
                    @click="handleIsUnfold"
                    v-if="isUnfoldAuditShow"
                    class="el-icon-minus"
                  ></i>
                  <i @click="handleIsUnfold" v-else class="el-icon-plus"></i>
                  领用方式
                </div>
                <div class="line"></div>
              </div>
              <template v-if="isUnfoldAuditShow">
                <el-form-item label="领用方式">
                  <el-input
                    v-model="rightForm.name"
                    class="no-border-input"
                  ></el-input>
                </el-form-item>
              </template>
            </el-col>

            <el-col :span="24">
              <div class="show_line_box">
                <div class="title_l">
                  <i
                    @click="handleIsUnfold2"
                    v-if="isUnfoldAuditShow2"
                    class="el-icon-minus"
                  ></i>
                  <i @click="handleIsUnfold2" v-else class="el-icon-plus"></i>
                  领用控制
                </div>
                <div class="line"></div>
              </div>
              <template v-if="isUnfoldAuditShow2">
                <el-col :span="8">
                  <el-form-item label="领用控制类型">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="是否按领用张数控制">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="是否按照未报销期限控制">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="是否按照未报销总金额控制">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最大领用张数">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最大未报销领用天数">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最大未报销总金额">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </template>
            </el-col>

            <el-col :span="24">
              <div class="show_line_box">
                <div class="title_l">
                  <i
                    @click="handleIsUnfold3"
                    v-if="isUnfoldAuditShow3"
                    class="el-icon-minus"
                  ></i>
                  <i @click="handleIsUnfold3" v-else class="el-icon-plus"></i>
                  报销设置
                </div>
                <div class="line"></div>
              </div>
              <template v-if="isUnfoldAuditShow3">
                <el-col :span="8">
                  <el-form-item label="报销方式">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="报销驱动来源">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="报销账簿">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="超额限制">
                    <el-input
                      v-model="rightForm.name"
                      class="no-border-input"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </template>
            </el-col>
          </el-form>
        </el-card>
      </div>
    </div>
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

  export default {
    name: 'sz',
    mixins: [searchTableMixis],
    components: {
      Edit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        dataTree: [
          {
            id: 1,
            label: '一级 1',
            children: [
              {
                id: 4,
                label: '二级 1-1',
                children: [
                  {
                    id: 9,
                    label: '三级 1-1-1',
                  },
                  {
                    id: 10,
                    label: '三级 1-1-2',
                  },
                ],
              },
            ],
          },
        ],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
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
        rightForm: {
          name: '否',
        },
        currProjectId: '',
        filedAll: [
          { name: '结算财务组织' },
          { name: '付款交易类型' },
          { name: '单据编号' },
          { name: '单据状态' },
          { name: '单据日期' },
          { name: '付款原币金额' },
          { name: '客户' },
          { name: '付款银行账户' },
          { name: '交易对象类型' },
          { name: '供应商' },
          { name: '收款账户类型' },
          { name: '利润中心' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-sz-search',
        tableKey: 'globalTreasurer-jsgl-sz-list',
        searchMore: false,
        isUnfoldAuditShow: true,
        isUnfoldAuditShow2: true,
        isUnfoldAuditShow3: true,
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
        return [{ name: '财务组织', key: 'jsFinance' }]
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

      handleEdit(row, disabled, type) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum, type)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await implementPlanDelete({ ids: row.id })
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
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
      },
      handleIsUnfold2() {
        this.isUnfoldAuditShow2 = !this.isUnfoldAuditShow2
      },
      handleIsUnfold3() {
        this.isUnfoldAuditShow3 = !this.isUnfoldAuditShow3
      },
    },
  }
</script>

<style scoped lang="scss">
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
    width: calc(100% - 210px);
  }
  .secondCard ::v-deep .no-border-input .el-input__inner {
    border: none !important;
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
