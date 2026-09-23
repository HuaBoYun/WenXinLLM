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
                v-model="queryForm.practiceExamineName"
                clearable
                placeholder="姓名"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '姓名'"
              />

              <el-input
                v-model="queryForm.practiceExamineBelongGroupName"
                disabled
                placeholder="请选择所属单位"
                :style="{ width: '140px', 'margin-right': '10px' }"
                v-if="item.name === '所属单位'"
              />
              <el-button
                v-if="item.name === '所属单位'"
                :style="{ marginRight: '10px' }"
                type="primary"
                @click="
                  $refs.companySelect.show({
                    labelKey: 'unit',
                    idKey: 'unitId',
                    title: '单位',
                  })
                "
              >
                选择
              </el-button>

              <el-select
                :style="{ width: '100%' }"
                v-model="queryForm.examineGrade"
                placeholder="考核结果"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '考核结果'"
              >
                <el-option label="称职" :value="0" />
                <el-option label="不称职" :value="1" />
                <el-option label="基本称职" :value="2" />
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
              <el-button @click="resetSearch">重置</el-button>
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
        <el-table-column align="center" label="姓名" prop="practiceExamineName">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.practiceExamineName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="已执业年限"
            prop="practiceAgeLimit"
            v-if="item.name === '已执业年限'"
          />
          <el-table-column
            align="center"
            label="所属单位"
            prop="practiceExamineBelongGroupName"
            v-if="item.name === '所属单位'"
          />
          <el-table-column
            align="center"
            label="考核结果"
            prop="examineGrade"
            v-if="item.name === '考核结果'"
          >
            <template #default="{ row }">
              <span v-if="row.examineGrade == 0">称职</span>
              <span v-if="row.examineGrade == 1">不称职</span>
              <span v-if="row.examineGrade == 2">基本称职</span>
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            label="创建人"
            prop="creatorName"
            v-if="item.name === '创建人'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="createdTime"
            v-if="item.name === '创建时间'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
            <!-- <el-button type="text">导出</el-button> -->
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
    <zykhView ref="zykhView" @fetchData="fetchData" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
  </div>
</template>

<script>
  import { getZXKHList, deleteZXKHList, exportZYKH } from '@/api/fwgl/gsls'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import zykhView from './components/zykhView.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import { downloadFile } from '@/utils/otherUtils'

  export default {
    name: 'NormalReportList',
    components: { zykhView, filterTable, filterSearch, CompanySelectModal },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          examineGrade: '',
          pageNumber: 1,
          pageSize: 20,
          practiceExamineBelongGroup: '',
        },
        filedAll: [
          { name: '已执业年限' },
          { name: '所属单位' },
          { name: '考核结果' },
          { name: '创建人' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-gsls-zykh-search',
        tableKey: 'fwgl-gsls-zykh-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
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
      /**
       * @description: 选择公司部门回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.queryForm, 'practiceExamineBelongGroup', val.id)
        this.$set(this.queryForm, 'practiceExamineBelongGroupName', val.label)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '姓名', key: 'practiceExamineName' },
          { name: '所属单位', key: 'practiceExamineBelongGroup' },
          { name: '考核结果', key: 'examineGrade' },
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
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          examineGrade: '',
          pageNumber: 1,
          pageSize: 20,
          practiceExamineBelongGroup: '',
        }
        this.fetchData()
      },
      /**
       * @description: 重置并请求
       * @return {*}
       */      
      resetSearch() {
        this.resetQueryForm()
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
          data: { tlist, totalRecord },
        } = await getZXKHList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 导出
       * @return {*}
       */      
      async handleExport() {
        this.listLoading = true
        const res = await exportZYKH(this.queryForm)
        downloadFile(res, '执业考核列表.xlsx')
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['zykhView'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['zykhView'].showEdit('detail', row)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['zykhView'].showEdit('edit', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteZXKHList({ id: row.practiceExamineId })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
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
