<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
                v-model="queryForm.dataname"
                clearable
                placeholder="资料名称"
                v-if="item.name === '资料名称'"
              />
              <el-input
                v-model="queryForm.datacode"
                clearable
                placeholder="资料编号"
                v-if="item.name === '资料编号'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
            <el-form-item style="cursor: pointer">
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

    <el-card shadow="never">
      <vab-query-form>
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
          <el-button
            type="success"
            @click="hadnlePush"
            :disabled="select.length === 0"
          >
            下发
          </el-button>
          <el-button
            type="success"
            @click="$refs['projectDataInfo'].showEdit(false)"
          >
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>

      <el-table
        v-loading="listLoading"
        :data="list"
        ref="multipleTable"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          width="48"
          type="selection"
          :reserve-selection="true"
        ></el-table-column>
        <el-table-column align="center" label="资料编号">
          <template #default="{ row }">
            <el-button type="text" @click="handleAdd(row, true)">
              {{ row.projectDatapreId }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '资料名称'"
            align="center"
            label="资料名称"
            prop="dataName"
          />

          <el-table-column
            v-if="item.name === '所属项目'"
            align="center"
            label="所属项目"
            prop="projectname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建人'"
            align="center"
            label="创建人"
            prop="username"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建时间'"
            align="center"
            label="创建时间"
            prop="dataDate"
            show-overflow-tooltip
          />
        </div>
        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template slot-scope="scope">
            <!-- <el-button
              type="text"
              @click="$refs['projectDataInfo'].showEdit(scope.row)"
            >
              预览
            </el-button> -->
            <el-button
              type="text"
              @click="$refs['projectDataInfo'].showEdit(scope.row, true)"
            >
              修改
            </el-button>
            <el-button type="text" @click="handleDelete(scope.row)">
              删除
            </el-button>
            <el-button type="text" @click="handleFile(scope.row)">
              附件列表
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <project-data-info ref="projectDataInfo" @fetch-data="fetchData" />
    <FileModal ref="file" @fetch-data="fetchData" />
    <!-- <personSelectModal ref="person" /> -->
    <select-team
      ref="select"
      @selectTeamList="selectTeamList"
      :defaultExpandedH="3"
    ></select-team>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      @fetch-data="fetchData"
    />
  </div>
</template>

<script>
  import { whetherLeader } from '@/api/audit/implement'
  import {
    dataProjectDel,
    findOrganizationByTreeAllss,
    getDataprojectList,
  } from '@/api/audit/preparation'
  import { saveProjectData } from '@/api/audit/project'
  import { UTCformat } from '@/utils'
  import FileModal from './components/FileList.vue'
  import personSelectModal from './components/personSelectModal'
  import ProjectDataInfo from './components/ProjectDataInfo'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import projectManage from '@/components/selectPerson'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
      ProjectDataInfo,
      FileModal,
      personSelectModal,
      selectTeam,
      projectManage,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        selectProjectId: [],
        showButton: true,
        // 筛选列表配置
        filedAll: [
          { name: '资料名称' },
          { name: '所属项目' },
          { name: '创建人' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-prepare-projectData-search',
        tableKey: 'audit-prepare-projectData-list',
        searchMore: true,
      }
    },
    async created() {
      this.fetchData()
      let res11 = await whetherLeader() //判断是否为组长
      if (res11.data.ifLeader) {
        this.showButton = true
      } else {
        this.showButton = false
      }
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '资料名称', key: 'dataname' },
          { name: '资料编号', key: 'datacode' },
        ]
      },
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
      },
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
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getDataprojectList(this.queryForm)
        this.list = tlist
        this.list.forEach((item) => {
          item.dataDate = UTCformat(item.dataDate)
        })
        this.total = totalRecord
        const res = await findOrganizationByTreeAllss(this.queryForm)
        this.dataTree = res
        this.listLoading = false
      },
      handleAdd(row, falg) {
        this.$refs['projectDataInfo'].showEdit(row, false)
      },
      // handleEdit(row) {
      //   this.$refs['edit'].showEdit(row, true)
      // },
      handleFile(row) {
        this.$refs['file'].showEdit(row)
      },
      hadnlePush() {
        // this.$refs['person'].showEdit(this.select, this.selectProjectId)
        // this.$refs['select'].showEdit('members')
        this.$refs.manage.showEdit()
      },
      selectTeamList(val) {
        saveProjectData({
          dataId: this.select.map((item) => item.id).toString(),
          projectId: this.select[0].projectid,
          staffId: val.map((item) => item.staffid).toString(),
        }).then((res) => {
          if (res.code == 1) {
            this.$refs.multipleTable.clearSelection()
            this.$baseMessage('下发成功', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      getChildlistPro(val) {
        saveProjectData({
          dataId: this.select.map((item) => item.id).toString(),
          projectId: this.select[0].projectid,
          staffId: val.map((res) => res.staffid).toString(),
        }).then((res) => {
          if (res.code == 1) {
            this.$baseMessage('下发成功', 'success', 'vab-hey-message-success')

            // 为每条选中的数据创建下发记录
            const xiafaPromises = this.select.map((row) => {
              const arr = val.map((item) => {
                return {
                  formId: row.id,
                  distributionTitle: row.projectDatapreId, // 使用季度名称或默认标题
                  reciver: item.staffid,
                  isread: 0,
                  moduleType: 'znsj',
                }
              })
              // 为每条数据调用xiafa方法
              return xiafaListNew({
                tableId: '217',
                jsondistribution: JSON.stringify(arr),
              })
            })

            // 等待所有下发操作完成
            Promise.all(xiafaPromises)
              .then((responses) => {
                const allSuccess = responses.every(
                  (response) => response.msg === '成功'
                )
                if (allSuccess) {
                  this.$baseMessage('批量下发通知成功', 'success')
                } else {
                  this.$baseMessage('部分下发通知失败', 'warning')
                }
                this.fetchData()
                // 清空选择
                this.$refs.multipleTable.clearSelection()
              })
              .catch((error) => {
                console.error('下发通知失败:', error)
                this.$baseMessage('下发通知失败', 'error')
                this.fetchData()
              })
          }
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await dataProjectDel({ dataId: row.id })
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
      // 2、设置row-key
      getRowKeys(row) {
        return row.id
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        this.select = selection
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
