<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <!-- <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="dataTree"
          default-expand-all
          :expand-on-click-node="false"
          highlight-current
          node-key="id"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div> -->
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  :prop="item.key"
                  v-for="(item, index) in searchItem"
                  :key="index"
                >
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
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never">
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
            <template v-if="isShow">
              <el-button
                type="success"
                @click="hadnlePush"
                :disabled="select.length === 0"
              >
                下发
              </el-button>
              <el-button
                v-if="showButton"
                type="success"
                @click="$refs['projectDataInfo'].showEdit(false)"
              >
                新建
              </el-button>
            </template>
          </vab-query-form-right-panel>

          <el-table
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            :row-key="getRowKeys"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              width="48"
              v-if="isShow"
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
            <el-table-column width="1" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              v-if="isShow"
            >
              <template slot-scope="scope">
                <!-- <el-button
                  type="text"
                  @click="$refs['projectDataInfo'].showEdit(scope.row)"
                >
                  预览
                </el-button> -->
                <el-button
                  v-if="showButton"
                  type="text"
                  @click="$refs['projectDataInfo'].showEdit(scope.row, true)"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(scope.row)"
                  v-if="showButton"
                >
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
      </div>
    </div>
    <project-data-info ref="projectDataInfo" @fetch-data="fetchData" />
    <FileModal ref="file" @fetch-data="fetchData" />
    <personSelectModal ref="person" />
  </div>
</template>

<script>
  import { whetherLeader } from '@/oapi/audit/implement'
  import {
    dataProjectDel,
    findOrganizationByTreeAllss,
    getDataprojectList,
  } from '@/oapi/audit/preparation'
  import { UTCformat } from '@/utils'
  import FileModal from './components/FileList.vue'
  import personSelectModal from './components/personSelectModal'
  import ProjectDataInfo from './components/ProjectDataInfo.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    components: {
      ProjectDataInfo,
      FileModal,
      personSelectModal,
      filterSearch,
      filterTable,
    },
    mixins: [searchTableMixis],
    props: {
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
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
          datacode: '',
          dataname: '',
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        selectProjectId: [],
        showButton: true,
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
        localKey: 'oilAudit-prepare-projectData-search',
        tableKey: 'oilAudit-prepare-projectData-list',
        searchMore: true,
      }
    },
    async created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      let res11 = await whetherLeader() //判断是否为组长
      if (res11.data.ifLeader) {
        this.showButton = true
      } else {
        this.showButton = false
      }
    },
    methods: {
      getFiled() {
        return [
          { name: '资料名称', key: 'dataname' },
          { name: '资料编号', key: 'datacode' },
        ]
      },
      // handleNodeClick(val) {
      //   this.queryForm.orgid = val.id
      //   this.fetchData()
      // },
      resetQueryForm() {
        this.queryForm = {
          datacode: '',
          dataname: '',
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getDataprojectList({
          ...this.queryForm,
          projectId: this.projectId,
        })
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
        this.$refs['person'].showEdit(this.select, this.selectProjectId)
      },
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
        return row.dataId
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        this.select = selection.map((item) => item.dataId)
      },
    },
  }
</script>
<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
