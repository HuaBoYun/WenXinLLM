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
                v-model="queryForm.execuno"
                clearable
                placeholder="执行案号"
                v-if="item.name === '执行案号'"
                style="width: 140px; margin-right: 20px"
              />
              <el-input
                v-model="queryForm.execucourt"
                clearable
                placeholder="执行法院"
                v-if="item.name === '执行法院'"
                style="width: 140px; margin-right: 20px"
              />

              <el-select
                v-model="queryForm.executype"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '执行方式'"
                placeholder="执行方式"
                clearable
              >
                <el-option value="账户冻结">账户冻结</el-option>
                <el-option value="查封">查封</el-option>
                <el-option value="扣押">扣押</el-option>
                <el-option value="拍卖">拍卖</el-option>
                <el-option value="变卖">变卖</el-option>
                <el-option value="协助执行">协助执行</el-option>
                <el-option value="其它">其它</el-option>
              </el-select>
              <span v-if="item.name === '所属仲裁过程'">
                <el-input
                  v-model="queryForm.arbitraname"
                  clearable
                  placeholder="请选择关联仲裁"
                  readonly
                  :style="{ width: '70%' }"
                />
                <el-button
                  :style="{ marginLeft: '5px', height: '30px' }"
                  type="primary"
                  @click="$refs.glzc.show()"
                >
                  选择
                </el-button>
              </span>
              <span v-if="item.name === '所属诉讼过程'">
                <el-input
                  v-model="queryForm.litigationname"
                  clearable
                  placeholder="请选择关联诉讼"
                  readonly
                  :style="{ width: '70%' }"
                />
                <el-button
                  :style="{ marginLeft: '5px', height: '30px' }"
                  type="primary"
                  @click="$refs.ss.show()"
                >
                  选择
                </el-button>
              </span>

              <el-select
                v-model="queryForm.sslx"
                clearable
                style="width: 140px; margin-right: 20px"
                placeholder="所属过程"
                v-if="item.name === '所属过程'"
              >
                <el-option value="诉讼">诉讼</el-option>
                <el-option value="仲裁">仲裁</el-option>
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
        <el-table-column align="center" label="纠纷名称" prop="disputename">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.disputename }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="执行案号"
            prop="execuno"
            v-if="item.name === '执行案号'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="执行法院"
            prop="execucourt"
            v-if="item.name === '执行法院'"
          />
          <el-table-column
            align="center"
            label="执行方式"
            prop="executype"
            v-if="item.name === '执行方式'"
          />
          <el-table-column
            align="center"
            label="代理律师"
            prop="attorney"
            v-if="item.name === '代理律师'"
          />

          <el-table-column
            align="center"
            label="执行总金额（万元）"
            prop="execuamount"
            v-if="item.name === '执行总金额（万元）'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleApproval(row)"
              v-if="userInfo.staffid === row.createstaffid"
              :disabled="row.status"
            >
              提交审批
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="userInfo.staffid === row.createstaffid"
              :disabled="row.status != 0 && row.status != 2 && row.status != 3"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="userInfo.staffid === row.createstaffid"
              :disabled="row.status"
            >
              删除
            </el-button>
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
    <AccountEdit ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <zxglEdit ref="zxglAddRef" @fetch="fetchData" />
    <!-- 关联仲裁 -->
    <glzc-options ref="glzc" @selected="handleSelectGlzc" />
    <!-- 关联诉讼 -->
    <ss-options ref="ss" @selected="handleSelectSs" />
  </div>
</template>

<script>
  import { legalExecumgrList, legalExecumgrDelete } from '@/api/fwgl/legal'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import AccountEdit from '@/views/fwgl/legal/components/AccountEdit'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import zxglEdit from './components/zxglEdit.vue'
  import glzcOptions from './components/options/glzc.vue'
  import ssOptions from './components/options/ss.vue'
  export default {
    name: 'classic',
    components: {
      AccountEdit,
      filterTable,
      filterSearch,
      ProcessList,
      zxglEdit,
      glzcOptions,
      ssOptions,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        localKey: 'fwgl-legal-zxgl-search',
        tableKey: 'fwgl-legal-zxgl-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '纠纷名称' },
          { name: '执行案号' },
          { name: '执行法院' },
          { name: '执行方式' },
          { name: '执行总金额（万元）' },
          { name: '状态' },
        ],
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          litigationname: undefined,
          litigationid: undefined,
          arbitraid: undefined,
          arbitraname: undefined,
          executype: undefined,
          execucourt: undefined,
          execuno: undefined,
          sslx: undefined,
        },
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
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
          litigationname: undefined,
          litigationid: undefined,
          arbitraid: undefined,
          arbitraname: undefined,
          executype: undefined,
          execucourt: undefined,
          execuno: undefined,
          sslx: undefined,
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
          { name: '执行案号', key: 'execuno' },
          { name: '执行法院', key: 'execucourt' },
          { name: '执行方式', key: 'executype' },
          { name: '所属仲裁过程', key: 'arbitraid' },
          { name: '所属诉讼过程', key: 'litigationid' },
          { name: '所属过程', key: 'sslx' },
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
        } = await legalExecumgrList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },

      handleApproval(row) {
        //提交审批
        const tableId = 29
        const fromId = row.id
        this.$refs['process'].save(tableId, fromId)
      },
      handleEdit(row) {
        this.$refs['zxglAddRef'].show({}, '编辑', row)
      },
      handleDetail(row) {
        this.$refs['zxglAddRef'].show({}, '详情', row)
      },
      handleSelected(rowData) {
        this.tableData.unshift(rowData)
      },
      async handleDelete(row) {
        const res = await legalExecumgrDelete({ id: row.id })
        if (res && res.code === 1) {
          this.$message({
            type: 'success',
            message: '操作成功！',
          })
          this.fetchData()
        } else {
          this.$message({
            type: 'error',
            message: '操作失败！',
          })
        }
      },
      handleAdd(e) {
        this.$refs['zxglAddRef'].show({}, '新增')
      },
      handleSelectGlzc(val) {
        this.$set(this.queryForm, 'arbitraname', val.courtfirst)
        this.$set(this.queryForm, 'arbitraid', val.arbitraid)
      },
      handleSelectSs(val) {
        this.$set(this.queryForm, 'litigationname', val.disputeitem)
        this.$set(this.queryForm, 'litigationid', val.litigationid)
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
