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
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.workUnitName"
                clearable
                placeholder="单位名称"
                readonly
                v-if="item.name === '单位名称'"
                style="width: 140px; margin-right: 20px"
                @click.native="showGroupLeader"
              />

              <el-select
                :style="{ width: '100%' }"
                v-model="queryForm.employmentTermType"
                placeholder="聘用类型"
                v-if="item.name === '聘用类型'"
              >
                <el-option label="常年法律顾问" :value="1" />
                <el-option label="专项法律顾问" :value="2" />
              </el-select>

              <el-date-picker
                v-model="queryForm.fillInBeginDate"
                placeholder="填报开始时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '填报开始时间'"
              />
              <el-date-picker
                v-model="queryForm.fillInEndDate"
                placeholder="填报结束时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '填报结束时间'"
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
              <el-button @click="fetchData('reset')" type="primary">
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
        <el-button @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="序号" type="index" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="法律顾问机构名称"
            prop="organizationName"
            v-if="item.name === '法律顾问机构名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row, 'detail')">
                {{ row.organizationName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="服务团队"
            prop="serviceTeamName"
            v-if="item.name === '服务团队'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="团队负责人"
            prop="leaderName"
            v-if="item.name === '团队负责人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="联系方式"
            prop="leaderContact"
            v-if="item.name === '联系方式'"
          ></el-table-column>
          <!-- <el-table-column
            align="center"
            label="费用"
            prop="expense"
            v-if="item.name === '费用'"
          />
          <el-table-column
            align="center"
            label="填报人"
            prop="creatorName"
            v-if="item.name === '填报人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="填报时间"
            prop="createdTime"
            v-if="item.name === '填报时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审核人"
            prop="auditPersonName"
            v-if="item.name === '审核人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审核时间"
            prop="auditTime"
            v-if="item.name === '审核时间'"
          ></el-table-column> -->
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <!-- <el-button type="text">评价</el-button> -->
            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
              v-if="String(userInfo.staffid) === String(row.creator)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="String(userInfo.staffid) === String(row.creator)"
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
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <zxflfwView ref="zxflfwView" @fetch-data="fetchData" />
    <!-- 单位 -->
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import zxflfwView from './components/zxflfwView.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import { legalService, fetchApi } from '@/api/fwgl/api'
  import { downloadFile } from '@/utils/otherUtils'
  const { getList, doDelete, exportData } = legalService

  export default {
    name: 'zxflfwList',
    components: { zxflfwView, filterTable, filterSearch, CompanySelectModal },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          workUnitName: undefined,
          employmentTermType: undefined,
          fillInBeginDate: undefined,
          fillInEndDate: undefined,
        },
        filedAll: [
          { name: '法律顾问机构名称' },
          { name: '服务团队' },
          { name: '团队负责人' },
          { name: '联系方式' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-zxflfw-search',
        tableKey: 'fwgl-flfw-zxflfw-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
      }
    },
    created() {
      this.fetchData()
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '单位名称', key: 'workUnitName' },
          { name: '聘用类型', key: 'employmentTermType' },
          { name: '填报开始时间', key: 'fillInBeginDate' },
          { name: '填报结束时间', key: 'fillInEndDate' },
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
       * @param {*} type
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true
        if (type && type === 'reset') {
          this.$refs['form'].resetFields()
          this.queryForm.workUnitName = ''
          this.queryForm.workUnitId = ''
        }
        this.queryForm.lawServiceType = 2
        const {
          data: { tlist, totalRecord },
        } = await fetchApi(getList, this.queryForm)
        this.listLoading = false
        this.list = tlist
        this.total = totalRecord
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        this.listLoading = true

        const exportParams = {
          // 必填参数：法律服务类型（2=专项法律服务）
          lawServiceType: 2,
          // 导出固定分页参数
          pageNumber: 1,
          pageSize: 20000,
          // 可选参数：根据查询条件传递
          unitName: this.queryForm.workUnitName || '',
          employmentTermType: this.queryForm.employmentTermType || 2,
          fillInBeginDate: this.queryForm.fillInBeginDate,
          fillInEndDate: this.queryForm.fillInEndDate,
          // 注意：token 不在此处传递，transData 函数会自动添加
        }

        const res = await fetchApi(exportData, exportParams, true)
        this.listLoading = false
        downloadFile(res, '专项法律服务列表.xlsx')
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['zxflfwView'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        await this.$refs['zxflfwView'].showEdit('detail', {
          id: row.lawServiceId,
        })
      },
      async handleEdit(row, title) {
        await this.$refs['zxflfwView'].showEdit(title, {
          id: row.lawServiceId,
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await fetchApi(doDelete, {
            id: row.lawServiceId,
            lawServiceType: 2,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      // 选完后处理
      handleCompanyTreeSelected(val) {
        this.$set(this.queryForm, val.idKey, val.id)
        this.$set(this.queryForm, val.labelKey, val.label)
      },
      // 选公司
      showGroupLeader() {
        this.$refs.companySelect.show({
          labelKey: 'workUnitName',
          idKey: 'workUnitId',
          // idKey: 'company',
          title: '单位',
        })
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
