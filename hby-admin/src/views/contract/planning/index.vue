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
                v-model="queryForm.planningName"
                clearable
                placeholder="策划名称"
                v-if="item.name === '策划名称'"
              />
              <el-select
                v-model="queryForm.planningType"
                clearable
                placeholder="策划类型"
                v-if="item.name === '策划类型'"
              >
                <el-option label="初步策划" :value="1" />
                <el-option label="详细策划" :value="2" />
                <el-option label="实施策划" :value="3" />
              </el-select>
              <el-select
                v-model="queryForm.planningStatus"
                clearable
                placeholder="策划状态"
                v-if="item.name === '策划状态'"
              >
                <el-option label="草稿" :value="1" />
                <el-option label="待审核" :value="2" />
                <el-option label="已审核" :value="3" />
                <el-option label="已批准" :value="4" />
              </el-select>
              <el-input
                v-model="queryForm.plannerName"
                clearable
                placeholder="策划人"
                v-if="item.name === '策划人'"
              />
              <el-input
                v-model="queryForm.projectId"
                clearable
                placeholder="项目ID"
                v-if="item.name === '项目ID'"
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建策划</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="策划名称"
            prop="planningName"
            min-width="200"
            v-if="item.name === '策划名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.planningName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="策划类型"
            prop="planningType"
            width="100"
            v-if="item.name === '策划类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getPlanningTypeType(row.planningType)">
                {{ getPlanningTypeName(row.planningType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="项目ID"
            prop="projectId"
            width="100"
            v-if="item.name === '项目ID'"
          />
          <el-table-column
            align="center"
            label="策划人"
            prop="plannerName"
            width="100"
            v-if="item.name === '策划人'"
          />
          <el-table-column
            align="center"
            label="策划日期"
            prop="planningDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '策划日期'"
          />
          <el-table-column
            align="center"
            label="策划状态"
            prop="planningStatus"
            width="100"
            v-if="item.name === '策划状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getPlanningStatusType(row.planningStatus)">
                {{ getPlanningStatusName(row.planningStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '创建时间'"
          />
        </div>
        <el-table-column align="center" label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleReview(row)"
              v-if="row.planningStatus === 2"
            >
              审核
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handlePersonnel(row)"
            >
              人员配置
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleEquipment(row)"
            >
              设备资源
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleMaterial(row)"
            >
              材料需求
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleSchedule(row)"
            >
              时间进度
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleProcurement(row)"
            >
              采购计划
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleCoordination(row)"
            >
              部门协调
            </el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNum"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <PlanningEdit ref="edit" @fetch-data="fetchData" />
    <PlanningReview ref="review" @fetch-data="fetchData" />
    <PersonnelManagement ref="personnel" />
    <EquipmentManagement ref="equipment" />
    <MaterialManagement ref="material" />
    <ScheduleManagement ref="schedule" />
    <ProcurementManagement ref="procurement" />
    <CoordinationManagement ref="coordination" />
  </div>
</template>

<script>
  import {
    getProjectPlanningList,
    getProjectPlanningById,
    reviewProjectPlanning,
    deleteProjectPlanning,
    batchDeleteProjectPlanning
  } from '@/api/contract/planning'
  import PlanningEdit from './components/PlanningEdit'
  import PlanningReview from './components/PlanningReview'
  import PersonnelManagement from './components/PersonnelManagement'
  import EquipmentManagement from './components/EquipmentManagement'
  import MaterialManagement from './components/MaterialManagement'
  import ScheduleManagement from './components/ScheduleManagement'
  import ProcurementManagement from './components/ProcurementManagement'
  import CoordinationManagement from './components/CoordinationManagement'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { successCode } from '@/config/net.config'

  export default {
    name: 'Planning',
    components: {
      PlanningEdit,
      PlanningReview,
      PersonnelManagement,
      EquipmentManagement,
      MaterialManagement,
      ScheduleManagement,
      ProcurementManagement,
      CoordinationManagement,
      filterTable,
      filterSearch
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planningName: '',
          planningType: null,
          planningStatus: null,
          plannerName: '',
          projectId: '',
          pageNum: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-planning-search',
        tableKey: 'contract-planning-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '策划名称' },
          { name: '策划类型' },
          { name: '项目ID' },
          { name: '策划人' },
          { name: '策划日期' },
          { name: '策划状态' },
          { name: '创建时间' },
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
          { name: '策划名称', key: 'planningName' },
          { name: '策划类型', key: 'planningType' },
          { name: '策划状态', key: 'planningStatus' },
          { name: '策划人', key: 'plannerName' },
          { name: '项目ID', key: 'projectId' },
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
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
        this.queryForm.pageNum = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          planningName: '',
          planningType: null,
          planningStatus: null,
          plannerName: '',
          projectId: '',
          pageNum: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNum = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        try {
          const response = await getProjectPlanningList(this.queryForm)
          console.log('项目策划列表接口返回数据:', response)

          // 修复：使用示例云标准状态码 1 表示成功
          if (response.code === 1) {
            this.list = response.data.list || []
            this.total = response.data.total || 0
            console.log('项目策划列表数据:', this.list.length, '条记录')
          } else {
            this.$message.error(response.msg || '查询失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取项目策划列表失败:', error)
          this.$message.error('查询失败：' + error.message)
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      async handleDetail(row) {
        try {
          const response = await getProjectPlanningById(row.id)
          console.log('项目策划详情响应:', response)
          if (response.code === 1) {  // 1表示成功
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')  // 使用msg字段
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          const response = await getProjectPlanningById(row.id)
          console.log('项目策划编辑响应:', response)
          if (response.code === 1) {  // 1表示成功
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')  // 使用msg字段
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 审核处理
      async handleReview(row) {
        try {
          const response = await getProjectPlanningById(row.id)
          console.log('项目策划审核响应:', response)
          if (response.code === 1) {  // 1表示成功
            await this.$refs['review'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')  // 使用msg字段
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 人员配置
      handlePersonnel(row) {
        this.$refs['personnel'].showEdit(row)
      },
      // 设备资源
      handleEquipment(row) {
        this.$refs['equipment'].showEdit(row)
      },
      // 材料需求
      handleMaterial(row) {
        this.$refs['material'].showEdit(row)
      },
      // 时间进度
      handleSchedule(row) {
        this.$refs['schedule'].showEdit(row)
      },
      // 采购计划
      handleProcurement(row) {
        this.$refs['procurement'].showEdit(row)
      },
      // 部门协调
      handleCoordination(row) {
        this.$refs['coordination'].showEdit(row)
      },
      // 删除项目策划
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            console.log('删除项目策划，ID:', row.id)
            const response = await deleteProjectPlanning(row.id)
            console.log('删除项目策划响应:', response)

            if (this.isResponseSuccess(response)) {
              this.$baseMessage(response.msg || '删除成功', 'success')
              this.fetchData() // 刷新列表
            } else {
              this.$baseMessage(response.message || response.msg || '删除失败', 'error')
            }
          } catch (error) {
            console.error('删除项目策划失败:', error)
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 统一状态码判断方法
      isResponseSuccess(response) {
        return successCode.includes(response.code)
      },
      // 获取策划类型名称
      getPlanningTypeName(type) {
        const typeMap = {
          1: '初步策划',
          2: '详细策划',
          3: '实施策划'
        }
        return typeMap[type] || '未知'
      },
      // 获取策划类型样式
      getPlanningTypeType(type) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'success'
        }
        return typeMap[type] || 'info'
      },
      // 获取策划状态名称
      getPlanningStatusName(status) {
        const statusMap = {
          1: '草稿',
          2: '待审核',
          3: '已审核',
          4: '已批准'
        }
        return statusMap[status] || '未知'
      },
      // 获取策划状态样式
      getPlanningStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'primary',
          4: 'success'
        }
        return typeMap[status] || 'info'
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
