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
                v-model="queryForm.frozenblank"
                clearable
                placeholder="被冻结账户开户行"
                v-if="item.name === '被冻结账户开户行'"
              />
              <el-input
                v-model="queryForm.frozenaccount"
                clearable
                placeholder="被冻结账户账号"
                v-if="item.name === '被冻结账户账号'"
              />
              <el-input
                v-model="queryForm.accountnature"
                clearable
                placeholder="账户性质"
                v-if="item.name === '账户性质'"
              />
              <el-row v-if="item.name === '申请冻结金额'">
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.applyamount"
                    clearable
                    placeholder="最小申请冻结金额"
                  />
                </el-col>
                <el-col class="line" :span="2" style="text-align: center">
                  -
                </el-col>
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.maxApplyAmount"
                    clearable
                    placeholder="最大申请冻结金额"
                  />
                </el-col>
              </el-row>
              <el-row v-if="item.name === '实际被冻结金额'">
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.minFrozenAmount"
                    clearable
                    placeholder="最小实际被冻结金额"
                  />
                </el-col>
                <el-col class="line" :span="2" style="text-align: center">
                  -
                </el-col>
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.frozenamount"
                    clearable
                    placeholder="最大实际被冻结金额"
                  />
                </el-col>
              </el-row>
              <el-row v-if="item.name === '被扣划金额'">
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.minKouhuaAmount"
                    clearable
                    placeholder="最小被扣划金额"
                  />
                </el-col>
                <el-col class="line" :span="2" style="text-align: center">
                  -
                </el-col>
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.kouhuaamount"
                    clearable
                    placeholder="最大被扣划金额"
                  />
                </el-col>
              </el-row>
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="录入人"
                v-if="item.name === '录入人'"
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="被冻结账户开户行"
          prop="frozenblank"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="被冻结账户账号"
            prop="frozenaccount"
            v-if="item.name === '被冻结账户账号'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.frozenaccount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="诉讼阶段"
            prop="porceedstage"
            v-if="item.name === '诉讼阶段'"
          />
          <el-table-column
            align="center"
            label="冻结起始日"
            prop="startdate"
            v-if="item.name === '冻结起始日'"
          />
          <el-table-column
            align="center"
            label="冻结期届满日"
            prop="enddate"
            v-if="item.name === '冻结期届满日'"
          />
          <el-table-column
            align="center"
            label="申请冻结金额(元)"
            prop="applyamount"
            v-if="item.name === '申请冻结金额(元)'"
          />
          <el-table-column
            align="center"
            label="实际被冻结金额(元)"
            prop="frozenamount"
            v-if="item.name === '实际被冻结金额(元)'"
          />
          <el-table-column
            align="center"
            label="被扣划金额(元)"
            prop="kouhuaamount"
            v-if="item.name === '被扣划金额(元)'"
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
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <FreezeEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getfrozen, Removefrozen } from '@/api/contract/legal'
  import FreezeEdit from '@/views/contract/legal/components/FreezeEdit'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  export default {
    name: 'Freeze',
    components: { FreezeEdit, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '779387',
          frozenblank: undefined,
          frozenaccount: undefined,
          accountnature: undefined,
          minFrozenAmount: undefined,
          frozenamount: undefined,
          maxApplyAmount: undefined,
          minKouhuaAmount: undefined,
          kouhuaamount: undefined,
          realname: undefined,
        },

        localKey: 'contract-legal-freeze-search',
        tableKey: 'contract-legal-freeze-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '被冻结账户账号' },
          { name: '诉讼阶段' },
          { name: '冻结起始日' },
          { name: '冻结期届满日' },
          { name: '申请冻结金额(元)' },
          { name: '实际被冻结金额(元)' },
          { name: '被扣划金额(元)' },
        ],
      }
    },
    created() {
      this.fetchData()
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
    },
    methods: {
      // 动态筛选 动态表格 初始化数据&相关方法
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      getFiled() {
        let fields = [
          { name: '被冻结账户开户行', key: 'frozenblank' },
          { name: '被冻结账户账号', key: 'frozenaccount' },
          { name: '账户性质', key: 'accountnature' },
          { name: '申请冻结金额', key: 'applyamount' },
          { name: '实际被冻结金额', key: 'minFrozenAmount' },
          { name: '被扣划金额', key: 'minKouhuaAmount' },
          { name: '录入人', key: 'realname' },
        ]
        return fields
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
      // 动态表格开始
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
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          flowid: '779387',
          frozenblank: undefined,
          frozenaccount: undefined,
          accountnature: undefined,
          minFrozenAmount: undefined,
          frozenamount: undefined,
          maxApplyAmount: undefined,
          minKouhuaAmount: undefined,
          kouhuaamount: undefined,
          realname: undefined,
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
        const {
          date: { tlist, totalRecord },
        } = await getfrozen(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await Removefrozen({ inforId: row.inforid })
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
