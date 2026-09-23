<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.applyName"
                clearable
                placeholder="申请人姓名"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '申请人姓名'"
              ></el-input>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
        <el-table-column align="center" label="申请人姓名" prop="ipAddress">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.staff.realName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '员工编号'"
            align="center"
            label="员工编号"
            prop="staffCode"
          />
          <el-table-column
            v-if="item.name === '联系电话'"
            align="center"
            label="联系电话"
            prop="contactPhone"
            show-overflow-tooltip
          />

          <el-table-column
            v-if="item.name === '申请人单位'"
            align="center"
            label="申请人单位"
            prop="applyBelongGroupName"
          />

          <el-table-column
            v-if="item.name === '申请人部门'"
            align="center"
            label="申请人部门"
            prop=""
          >
            <template #default="{ row }">{{ row.staff.workUnitName }}</template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '服务类型'"
            align="center"
            label="服务类型"
            prop="serviceType"
            show-overflow-tooltip
          >
            <template slot-scope="{ row }">
              {{ typeList[row.serviceType] }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '邮箱后缀'"
            align="center"
            label="邮箱后缀"
            prop="mailSuffix"
          >
            <template slot-scope="{ row }">
              {{ postfix[row.mailSuffix] }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '首选登录名'"
            align="center"
            label="首选登录名"
            prop="loginName"
          />
          <el-table-column
            v-if="item.name === '备选登录名1'"
            align="center"
            label="备选登录名1"
            prop="loginNameOne"
          />
          <el-table-column
            v-if="item.name === '备选登录名2'"
            align="center"
            label="备选登录名2"
            prop="loginNameTwo"
          />
          <el-table-column
            v-if="item.name === '备选登录名3'"
            align="center"
            label="备选登录名3"
            prop="loginNameThree"
          />

          <el-table-column
            v-if="item.name === '变更类型'"
            align="center"
            label="变更类型"
            prop="changeType"
            show-overflow-tooltip
          >
            <template slot-scope="{ row }">
              {{ changeType !== null ? changeType[row.changeType] : '无' }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '申请人邮箱'"
            align="center"
            label="申请人邮箱"
            prop="applyMail"
          />

          <el-table-column
            v-if="item.name === '变更内容'"
            align="center"
            label="变更内容"
            prop="changeContent"
          />

          <el-table-column
            v-if="item.name === '注销原因'"
            align="center"
            label="注销原因"
            prop="logoutReason"
          />
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="state"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤回'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
              :disabled="!!row.state"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.state"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.state"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    @click="handleDelete(row)"
                    type="text"
                    :disabled="!!row.state"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <EMAILview ref="edit" @fetchData="fetchData"></EMAILview>
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import EMAILview from './components/emailViews.vue'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getList, deleteInfo } from '@/oapi/ypns_zhgl/zsyyxgl'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { getFlowPkInfo } from '@/api/setting/system.js'

  export default {
    components: {
      EMAILview,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          applyName: null,
        },
        filedAll: [
          { name: '员工编号' },
          { name: '联系电话' },
          { name: '申请人单位' },
          { name: '申请人部门' },
          { name: '服务类型' },
          { name: '邮箱后缀' },
          { name: '首选登录名' },
          { name: '备选登录名1' },
          { name: '备选登录名2' },
          { name: '备选登录名3' },
          { name: '变更类型' },
          { name: '申请人邮箱' },
          { name: '变更内容' },
          { name: '注销原因' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zhgl-email-search',
        tableKey: 'oilAudit-zhgl-email-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        typeList: ['开通邮箱', '信息变更', '注销邮箱'],
        postfix: ['petrochina.com.cn', 'cnpc.com.cn'],
        changeType: ['密码', '组织机构', '其他'],
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(143, row.id)
      },
      // 办理
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 143,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '申请人姓名', key: 'name' }]
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
        const {
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleExport() {},
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteInfo({ id: row.id })
          if (res.code == 200) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          createType: 1,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
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
