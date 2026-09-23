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
                v-model="queryForm.teamName"
                clearable
                placeholder="团队名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '团队名称'"
              ></el-input>
              <el-input
                v-model="queryForm.isCollaboration"
                clearable
                placeholder="有无合作"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '有无合作'"
              ></el-input>
              <el-input
                v-model="queryForm.serviceProject"
                clearable
                placeholder="服务项目"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '服务项目'"
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
        <el-button @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="团队名称" prop="teamName" />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="有无合作"
            prop="isCollaboration"
            v-if="item.name === '有无合作'"
          >
            <!-- <template #default="{ row }">
              {{ row.isCollaboration === 1 ? '是' : '否' }}
            </template> -->
          </el-table-column>
          <el-table-column
            align="center"
            label="委托主体"
            prop="entrustSubject"
            v-if="item.name === '委托主体'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="服务项目"
            prop="serviceProject"
            v-if="item.name === '服务项目'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="专业能力(分)"
            prop="professionalAbilityGrade"
            v-if="item.name === '专业能力(分)'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="响应效率(分)"
            prop="reactionEfficiencyGrade"
            v-if="item.name === '响应效率(分)'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="增值服务(分)"
            prop="appreciationServiceGrade"
            v-if="item.name === '增值服务(分)'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="remark"
            v-if="item.name === '备注'"
          ></el-table-column>
        </div>

        <el-table-column width="1"></el-table-column>
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
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { legalServiceTotal, fetchApi } from '@/api/fwgl/api'
  import { downloadFile } from '@/utils/otherUtils'
  const { getList } = legalServiceTotal

  export default {
    name: 'zxflfwList',
    components: { filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          teamName: '',
          isCollaboration: '',
          serviceProject: '',
        },
        filedAll: [
          { name: '团队名称' },
          { name: '有无合作' },
          { name: '委托主体' },
          { name: '服务项目' },
          { name: '专业能力(分)' },
          { name: '响应效率(分)' },
          { name: '增值服务(分)' },
          { name: '备注' },
          // { name: '总分' },
          // { name: '平均分' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-khtz-search',
        tableKey: 'fwgl-flfw-khtz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
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
          { name: '团队名称', key: 'teamName' },
          { name: '有无合作', key: 'isCollaboration' },
          { name: '服务项目', key: 'serviceProject' },
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
        this.listLoading = false
        if (type && type === 'reset') this.$refs['form'].resetFields()
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
        //debugger
        this.listLoading = true
        const res = await fetchApi(exportData, this.queryForm, true)
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
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        await this.$refs['zxflfwView'].showEdit('edit', {
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
