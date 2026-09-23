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
                v-model="queryForm.disputeno"
                clearable
                placeholder="登记编号"
                v-if="item.name === '登记编号'"
                style="width: 140px; margin-right: 20px"
              />

              <el-input
                v-model="queryForm.disputeitem"
                clearable
                placeholder="纠纷名称"
                v-if="item.name === '纠纷名称'"
                style="width: 140px; margin-right: 20px"
              />

              <el-input
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
                style="width: 140px; margin-right: 20px"
              />

              <el-select
                v-model="queryForm.disputetype"
                clearable
                style="width: 140px; margin-right: 20px"
                placeholder="纠纷类型"
                v-if="item.name === '纠纷类型'"
              >
                <el-option value="一般纠纷">一般纠纷</el-option>
                <el-option value="重大纠纷">重大纠纷</el-option>
              </el-select>

              <el-input
                v-model="queryForm.plaintiff"
                clearable
                placeholder="原告"
                v-if="item.name === '原告'"
                style="width: 140px; margin-right: 20px"
              />

              <el-input
                v-model="queryForm.defendant"
                clearable
                placeholder="被告"
                v-if="item.name === '被告'"
                style="width: 140px; margin-right: 20px"
              />

              <el-input
                v-model="queryForm.companyName"
                clearable
                placeholder="公司"
                readonly
                v-if="item.name === '公司'"
                style="width: 140px; margin-right: 20px"
                @click.native="showGroupLeader"
              />
              <!-- <el-select
                v-model="queryForm.company"
                placeholder="公司"
                v-if="item.name === '公司'"
              >
                <el-option :value="1" label="公司1"></el-option>
                <el-option :value="2" label="公司2"></el-option>
                <el-option :value="3" label="公司3"></el-option>
                <el-option :value="4" label="公司4"></el-option>
                <el-option :value="5" label="公司5"></el-option>
                <el-option :value="6" label="公司6"></el-option>
                <el-option :value="7" label="公司7"></el-option>
              </el-select> -->
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="登记编号" prop="disputeno" />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="纠纷名称"
            prop="disputeitem"
            v-if="item.name === '纠纷名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.disputeitem }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          />
          <el-table-column
            align="center"
            label="纠纷类型"
            prop="disputetype"
            v-if="item.name === '纠纷类型'"
          />
          <!-- <el-table-column
            align="center"
            label="代理律师"
            prop="attorney"
            v-if="item.name === '代理律师'"
          /> -->
          <el-table-column
            align="center"
            label="是否紧急"
            prop="isuegent"
            v-if="item.name === '是否紧急'"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.isuegent === 1 ? '是' : '否' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="起诉类型"
            prop="whethersued"
            v-if="item.name === '起诉类型'"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.whethersued === 1 ? '起诉' : '被诉' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="办结时间"
            prop="lastdealdate"
            v-if="item.name === '办结时间'"
          />
          <el-table-column
            align="center"
            label="所属公司"
            prop="orgname"
            v-if="item.name === '所属公司'"
          />
          <el-table-column
            align="center"
            label="所属阶段"
            v-if="item.name === '所属阶段'"
          >
            <template slot-scope="scope">
              <!--          <span>{{ scope.row.whethersued === 1 ? '起诉' : '被诉' }}</span>-->
              <span v-if="scope.row.cLOSECOUNT > 0">已结案</span>
              <span v-else-if="scope.row.zCCOUNT > 0">仲裁中</span>
              <span v-else-if="scope.row.sSCOUNT > 0">诉讼中</span>
              <span v-else-if="scope.row.xSCOUNT > 0">协商中</span>
              <span v-else>纠纷中</span>
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleAddTo(row)"
              v-if="userInfo.staffid === row.createstaff"
            >
              添加案例库
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
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
  </div>
</template>

<script>
  import { changeStatus, getlegal } from '@/api/fwgl/legal'
  import { doDelete } from '@/api/table'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import AccountEdit from '@/views/fwgl/legal/components/AccountEdit'
  export default {
    name: 'Account',
    components: { AccountEdit, filterTable, filterSearch, CompanySelectModal },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        localKey: 'fwgl-legal-account-search',
        tableKey: 'fwgl-legal-account-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '纠纷名称' },
          { name: '合同名称' },
          { name: '纠纷类型' },
          // { name: '代理律师' },
          { name: '是否紧急' },
          { name: '起诉类型' },
          { name: '办结时间' },
          { name: '所属阶段' },
          { name: '所属公司' },
        ],
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '',
          disputeno: undefined,
          disputeitem: undefined,
          contractname: undefined,
          disputetype: undefined,
          plaintiff: undefined,
          defendant: undefined,
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
    methods: {
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          flowid: '779392',
          disputeno: undefined,
          disputeitem: undefined,
          contractname: undefined,
          disputetype: undefined,
          plaintiff: undefined,
          defendant: undefined,
        }
      },
      showGroupLeader() {
        this.$refs.companySelect.show({
          labelKey: 'companyName',
          idKey: 'companyId',
          // idKey: 'company',
          title: '公司',
        })
      },
      /**
       * @description: 选择公司部门回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.queryForm, val.idKey, val.id)
        this.$set(this.queryForm, val.labelKey, val.label)
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
          { name: '登记编号', key: 'disputeno' },
          { name: '纠纷名称', key: 'disputeitem' },
          { name: '合同名称', key: 'contractname' },
          { name: '纠纷类型', key: 'disputetype' },
          { name: '原告', key: 'plaintiff' },
          { name: '被告', key: 'defendant' },
          { name: '公司', key: 'company' },
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
          } else this.searchNow = this.searchAll
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
        } = await getlegal(this.queryForm)
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
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleAddTo(row) {
        this.listLoading = true
        changeStatus({ disputeid: row.disputeid }).then((res) => {
          if (res.code == 1) {
            this.$message({
              type: 'success',
              message: '添加成功',
            })
            this.listLoading = false
          }
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
