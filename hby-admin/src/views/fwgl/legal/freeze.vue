<template>
  <div class="system-log-container">
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
                v-model="queryForm.frozenblank"
                clearable
                placeholder="被冻结账户开户行"
                style="width: 160px; margin-right: 20px"
                v-if="item.name === '被冻结账户开户行'"
              />

              <el-input
                v-model="queryForm.frozenaccount"
                clearable
                placeholder="被冻结账户账号"
                style="width: 160px; margin-right: 20px"
                v-if="item.name === '被冻结账户账号'"
              />

              <el-input
                v-model="queryForm.accountnature"
                clearable
                placeholder="账户性质"
                style="width: 160px; margin-right: 20px"
                v-if="item.name === '账户性质'"
              />

              <el-row
                v-if="item.name === '申请冻结金额'"
                style="margin-right: 20px"
              >
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.applyamount"
                    clearable
                    placeholder="最小申请冻结金额"
                    style="width: 160px; margin-right: 11px"
                  />
                </el-col>
                <el-col class="line" :span="2" style="text-align: center">
                  -
                </el-col>
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.maxApplyAmount"
                    clearable
                    style="width: 160px"
                    placeholder="最大申请冻结金额"
                  />
                </el-col>
              </el-row>

              <el-row
                v-if="item.name === '实际被冻结金额'"
                style="margin-right: 20px"
              >
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.minFrozenAmount"
                    clearable
                    style="width: 160px; margin-right: 11px"
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
                    style="width: 160px"
                    placeholder="最大实际被冻结金额"
                  />
                </el-col>
              </el-row>

              <el-row
                v-if="item.name === '被扣划金额'"
                style="margin-right: 20px"
              >
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.minKouhuaAmount"
                    clearable
                    style="width: 160px; margin-right: 11px"
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
                    style="width: 160px"
                    placeholder="最大被扣划金额"
                  />
                </el-col>
              </el-row>

              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="录入人"
                style="width: 140px; margin-right: 20px"
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
        </vab-query-form-left-panel>
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
            v-if="item.name === '冻结起始日'"
            label="冻结起始日"
            prop="startdate"
          />
          <el-table-column
            align="center"
            v-if="item.name === '冻结期届满日'"
            label="冻结期届满日"
            prop="enddate"
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
  import { getfrozen, Removefrozen } from '@/api/fwgl/legal'
  import FreezeEdit from '@/views/fwgl/legal/components/FreezeEdit'
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
        localKey: 'fwgl-legal-freeze-search',
        tableKey: 'fwgl-legal-freeze-list',
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
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
      this.initSearch()
    },
    methods: {
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
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
      /**
       * @description: 展开收起查询条件
       * @return {*}
       */
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
          { name: '申请冻结金额', key: 'ApplyAmount' },
          { name: '实际被冻结金额', key: 'frozenamount' },
          { name: '被扣划金额', key: 'kouhuaamount' },
          { name: '录入人', key: 'realname' },
        ]
        return fields
      },
      /**
       * @description: 从上一次缓存中获取搜索项初始化
       * @return {*}
       */
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
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
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
      /**
       * @description: 重置并请求
       * @return {*}
       */      
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
       * @description: 请求数据
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
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
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
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
