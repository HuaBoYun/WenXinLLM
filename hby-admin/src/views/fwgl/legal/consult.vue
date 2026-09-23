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
                v-model="queryForm.contractname"
                clearable
                style="width: 140px; margin-right: 20px"
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
              />

              <el-input
                v-model="queryForm.counterpart"
                clearable
                style="width: 140px; margin-right: 20px"
                placeholder="对方谈判人"
                v-if="item.name === '对方谈判人'"
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
        <el-table-column align="center" label="对方谈判人" prop="counterpart" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="对方谈判人联系电话"
            prop="counterpartphone"
            v-if="item.name === '对方谈判人联系电话'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.counterpartphone }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="是否预设调解方案"
            prop="ispresetcase"
            v-if="item.name === '是否预设调解方案'"
          >
            <template #default="{ row }">
              <span>
                {{ row.ispresetcase === 1 ? '是' : '否' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="是否协商一致"
            prop="isaggree"
            v-if="item.name === '是否协商一致'"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.isaggree === 1 ? '是' : '否' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建日期"
            prop="createtime"
            v-if="item.name === '创建日期'"
          />
          <el-table-column
            align="center"
            label="方案报批状态"
            prop="negotiastatus"
            v-if="item.name === '方案报批状态'"
          >
            <template #default="{ row }">
              {{
                row.negotiastatus == 1
                  ? '审批中'
                  : row.negotiastatus == 2
                  ? '已退回'
                  : row.negotiastatus == 3
                  ? '已撤回'
                  : row.negotiastatus == 4
                  ? '已终止'
                  : row.negotiastatus == 5
                  ? '已跟踪'
                  : row.negotiastatus == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="结果审批状态"
            prop="schemeStatus"
            v-if="item.name === '结果审批状态'"
          >
            <template #default="{ row }">
              {{
                row.schemeStatus == 1
                  ? '审批中'
                  : row.schemeStatus == 2
                  ? '已退回'
                  : row.schemeStatus == 3
                  ? '已撤回'
                  : row.schemeStatus == 4
                  ? '已终止'
                  : row.schemeStatus == 5
                  ? '已跟踪'
                  : row.schemeStatus == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="300"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'consult')"
              v-if="userInfo.staffid === row.createstaff"
            >
              协商结果
            </el-button>
            <el-button
              type="text"
              @click="handleApproval(row)"
              v-if="userInfo.staffid === row.createstaff"
              :disabled="row.negotiastatus || row.isaggree == 2"
            >
              提交审批
            </el-button>

            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
              v-if="userInfo.staffid === row.createstaff"
              :disabled="
                row.negotiastatus != 0 &&
                row.negotiastatus != 2 &&
                row.negotiastatus != 3
              "
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="userInfo.staffid === row.createstaff"
              :disabled="row.negotiastatus"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ConsultEdit ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import {
    getnegotiatedSettlementInfoList,
    Removenegotiated,
  } from '@/api/fwgl/legal'
  import ConsultEdit from '@/views/fwgl/legal/components/ConsultEdit'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  export default {
    name: 'Consult',
    components: { ConsultEdit, filterTable, filterSearch, ProcessList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        localKey: 'fwgl-legal-consult-search',
        tableKey: 'fwgl-legal-consult-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '对方谈判人联系电话' },
          { name: '是否预设调解方案' },
          { name: '是否协商一致' },
          { name: '创建日期' },
          { name: '方案报批状态' },
          { name: '结果审批状态' },
        ],
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '698864',
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
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(21, row.negotiaid)
      },
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '698864',
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
          // { name: '合同名称', key: 'contractname' },
          { name: '对方谈判人', key: 'counterpart' },
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
        } = await getnegotiatedSettlementInfoList(this.queryForm)
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
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
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
          const { msg } = await Removenegotiated({ negotiaId: row.negotiaid })
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
