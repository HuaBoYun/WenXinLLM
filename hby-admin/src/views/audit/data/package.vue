<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            :inline="true"
            :rules="rules"
            ref="queryForm"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.filCode"
                placeholder="档案编号"
                v-if="item.name === '档案编号'"
                style="width: 250px"
                disabled
              />
              <el-input
                v-model="queryForm.filName"
                placeholder="档案名称"
                v-if="item.name === '档案名称'"
                style="width: 250px"
                disabled
              />
              <el-input
                v-model="queryForm.worktime"
                clearable
                placeholder="工作量"
                v-if="item.name === '工作量'"
                style="width: 250px"
              />
              <el-date-picker
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                v-model="queryForm.dateEndTime"
                placeholder="项目实际结束时间"
                v-if="item.name === '项目实际结束时间'"
                style="width: 250px"
                type="date"
              />
              <el-input
                v-model="queryForm.numPrice"
                clearable
                type="number"
                placeholder="实际费用"
                v-if="item.name === '实际费用'"
                style="width: 250px"
              />
            </el-form-item>
            <el-form-item>
              <el-button native-type="submit" type="primary" @click="toArchive">
                归档
              </el-button>
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
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column align="center" label="业务编号" prop="id" width="100" v-if="item.name === '业务编号'" />
          <el-table-column align="center" label="业务名称" prop="name" v-if="item.name === '业务名称'" />
          <el-table-column
            align="center"
            label="密级"
            v-if="item.name === '密级'"
            prop="type"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-select v-model="scope.row.type" placeholder="请选择报告类型">
                <el-option label="1级" value="1" />
                <el-option label="2级" value="2" />
                <el-option label="3级" value="3" />
                <el-option label="4级" value="4" />
                <el-option label="5级" value="5" />
              </el-select>
            </template>
          </el-table-column>
        </div>
        <el-table-column width="1" />
      </el-table>
    </el-card>
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
  </div>
</template>

<script>
  import {
    getDefaultSaveProjectNums,
    getProjectArchiveList,
    savePjData,
  } from '@/api/audit/projectData'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch
    },
    data() {
      return {
        list: [],
        listLoading: true,
        rules: {
          filCode: [
            { required: true, message: '请输入档案编号', trigger: 'blur' },
          ],
          filName: [
            { required: true, message: '请输入档案名称', trigger: 'blur' },
          ],
          dateEndTime: [
            {
              required: true,
              message: '请输入项目实际结束时间',
              trigger: 'blur',
            },
          ],
          numPrice: [
            { required: true, message: '请输入实际费用', trigger: 'blur' },
          ],
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectIdList: [],
        queryForm: {
          filCode: '',
          filName: '',
          dateEndTime: '',
          numPrice: '',
          selectedDataArr: [],
          selectedData: '',
          worktime: '',
        },
        projectId: '',
        // 筛选、表格头自定义
        filedAll: [
          { name: '业务编号' },
          { name: '业务名称' },
          { name: '密级' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-data-package-search',
        tableKey: 'audit-data-package-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '档案编号', key: 'filCode' },
          { name: '档案名称', key: 'filName' },
          { name: '工作量', key: 'worktime' },
          { name: '项目实际结束时间', key: 'dateEndTime' },
          { name: '实际费用', key: 'numPrice' },
        ]
        return fields
      },
      async toArchive() {
        const info = JSON.parse(localStorage.getItem('userInfo')).staffid
        if (info != this.projectId) {
          this.$message.error('项目负责人才能归档')
          return
        }
        this.$refs['queryForm'].validate(async (valid) => {
          if (valid) {
            if (this.queryForm.selectedDataArr.length == 0) {
              this.$message.error('请选择归档内容')
              return
            }
            this.queryForm.selectedData = ''
            let that = this
            for (let i = 0; i < this.queryForm.selectedDataArr.length; i++) {
              let item = this.queryForm.selectedDataArr[i]
              if (i !== this.queryForm.selectedDataArr.length - 1) {
                that.queryForm.selectedData += item.id + '-' + item.type + ','
              } else {
                that.queryForm.selectedData += item.id + '-' + item.type
              }
            }
            this.queryForm.selectedDataArr = []
            let nums = await getDefaultSaveProjectNums()
            const info =
              nums.data.data > 0
                ? '底稿未复核完成,确定归档吗?'
                : '确定要归档吗?'
            this.$baseConfirm(info, null, async () => {
              savePjData(this.queryForm).then((res) => {
                this.fetchData()
              })
            })
          } else {
            return false
          }
        })
      },
      handleSelectionChange(e) {
        if (e.length) {
          this.queryForm.selectedDataArr = e
        } else {
          this.queryForm.selectedDataArr = []
        }
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { archiveMenuList, project },
        } = await getProjectArchiveList({})
        this.list = archiveMenuList
        this.projectId = project.pmId
        // this.total = archiveMenuList.length
        this.listLoading = false
        this.queryForm.filCode = project.projectCode
        this.queryForm.filName = project.prjoectName
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
