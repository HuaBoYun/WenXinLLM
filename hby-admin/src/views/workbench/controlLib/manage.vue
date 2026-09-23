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
                v-model="queryForm.rulecode"
                clearable
                placeholder="制度编号"
                v-if="item.name === '制度编号'"
              />
              <el-input
                v-model="queryForm.rulename"
                clearable
                placeholder="制度名称"
                v-if="item.name === '制度名称'"
              />
              <el-date-picker
                v-model="queryForm.Date"
                align="right"
                end-placeholder="生效结束时间"
                range-separator="至"
                start-placeholder="生效开始时间"
                v-if="item.name === '生效时间'"
                type="daterange"
                unlink-panels
                value-format="yyyy-MM-dd"
              />
              <el-select
                v-model="queryForm.status"
                placeholder="状态"
                v-if="item.name === '状态'"
              >
                <el-option label="草稿" value="草稿"></el-option>
                <el-option label="发布待审核" value="发布待审核"></el-option>
                <el-option label="已发布" value="已发布"></el-option>
                <el-option
                  label="发布审核拒绝"
                  value="发布审核拒绝"
                ></el-option>
                <el-option label="已修订" value="已修订"></el-option>
                <el-option label="已废止" value="已废止"></el-option>
                <el-option label="废止待审核" value="废止待审核"></el-option>
                <el-option
                  label="废止审核拒绝"
                  value="废止审核拒绝"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="searchData"
              >
                查询
              </el-button>
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
          <el-button type="success" @click="handleAdd" v-if="isFw">
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <div class="lr-layout">
        <div class="left">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            default-expand-all
            @node-click="handleNodeClick"
          ></el-tree>
        </div>
        <div class="right">
          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="制度编号" prop="rulecode">
              <template #default="{ row }">
                <el-button @click="handleView(row, true)" type="text">
                  {{ row.rulecode }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                v-if="item.name === '制度名称'"
                label="制度名称"
                prop="rulename"
              />
              <el-table-column
                align="center"
                v-if="item.name === '制度类型'"
                label="制度类型"
                prop="zdtype"
              />
              <!-- <el-table-column align="center" label="类别" prop="innruletype" /> -->
              <el-table-column
                align="center"
                v-if="item.name === '生效日期'"
                label="生效日期"
                prop="publishdate"
                :formatter="formatDate"
              />
              <el-table-column
                align="center"
                v-if="item.name === '状态'"
                label="状态"
                prop="status"
              />
            </div>
            <!-- <el-table-column align="center" label="是否协商一致" prop="data" />
          <el-table-column align="center" label="创建日期" prop="data" /> -->
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="180"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="isFw"
                  :disabled="createId != row.createstaffid"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="isFw"
                  :disabled="createId != row.createstaffid"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
    </el-card>

    <ManageEdit ref="edit" @fetch-data="fetchData" />
    <LawPreview ref="preview" />
    <manageView ref="manage" />
  </div>
</template>

<script>
  import {
    deleteInnerRuleInfo,
    exportInnerRuleInfo,
    getInnerRulePageList,
    getInnerRuleType,
  } from '@/api/workbench/auditTools'
  import { UTCformat } from '@/utils/index'
  import LawPreview from './components/LawPreview'
  import ManageTree from './components/ManageTree'
  import ManageEdit from './ManageEdit.vue'
  import manageView from '@/views/workbench/controlLib/components/manageView.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Consult',
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
      ManageEdit,
      ManageTree,
      LawPreview,
      manageView,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
        queryForm: {
          rulecode: '',
          rulename: '',
          Date: [],
          status: '',
          innruletype: '',
          pageNumber: 1,
          pageSize: 20,
        },
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        isFw: false,
        // 筛选列表配置
        filedAll: [
          { name: '制度名称' },
          { name: '制度类型' },
          { name: '生效日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-controlLib-manage-search',
        tableKey: 'workbench-controlLib-manage-list',
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    watch: {
      '$route.name': {
        handler(val) {
          switch (val) {
            case 'groupmanage':
              this.queryForm.innruletype = '集团管理制度'
              break
            case 'sharesmanage':
              this.queryForm.innruletype = '矿办制度'
              break
            case 'companymanage':
              this.queryForm.innruletype = '公司管理制度'
              break
          }
        },
        immediate: true,
      },
    },
    created() {
      this.fetchData()
      this.getTree()
      //判断权限是否有经责科.展示不同title
      let userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (userInfo.roleNames.includes('审计法务部人员')) {
        this.isFw = true
      } else {
        this.isFw = false
      }
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    //离开页面清空localStorage.setItem('workbench')
    beforeRouteLeave(to, from, next) {
      localStorage.removeItem('workbench')
      next()
    },
    methods: {
      searchData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      getFiled() {
        return [
          { name: '制度编号', key: 'rulecode' },
          { name: '制度名称', key: 'rulename' },
          { name: '生效时间', key: 'Date' },
          { name: '状态', key: 'status' },
        ]
      },
      async getTree() {
        this.listLoading = true
        const {
          data: { listType },
        } = await getInnerRuleType({
          innruletype: this.queryForm.innruletype,
        })
        var arr = listType.map((type) => {
          return {
            label: type,
            id: type, // 可以使用其他唯一标识符
          }
        })
        this.treeData = [
          {
            label: '制度类型',
            id: '制度类型',
            children: arr,
          },
        ]
        this.listLoading = false
      },
      //节点查询
      handleNodeClick(data) {
        this.queryForm.pageNumber = 1
        // 根据点击的节点进行数据筛选
        if (data.label == '制度类型') {
          this.queryForm.zdtype = ''
        } else {
          this.queryForm.zdtype = data.label
        }
        this.fetchData()
      },
      preview(row) {
        console.dir(row)
        this.$refs['preview'].showEdit(row.bodyinfo)
      },
      async exportFile(row) {
        console.dir(row)
        const data = await exportInnerRuleInfo(row.innrulid)
        let fileName = row.rulename
        let blob = new Blob([data], {
          type: 'application/msword;charset=utf-8',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      resetQueryForm() {
        this.queryForm.rulecode = ''
        this.queryForm.rulename = ''
        this.queryForm.Date = []
        this.queryForm.status = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.queryForm.zdtype = ''
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      changeNode(node) {
        console.dir(node)
        this.queryForm.publishorg = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
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
        const { Date, ...other } = this.queryForm
        let starttime = undefined
        let endtime = undefined
        if (Date) {
          starttime = Date[0]
          endtime = Date[1]
        }
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getInnerRulePageList({ ...other, starttime, endtime })
        this.list = tlist
        this.total = totalRecord
        // await this.getTree()
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, this.queryForm.innruletype)
      },
      handleView(row, flag) {
        console.log(row)
        this.$refs['edit'].showEdit(row, this.queryForm.innruletype)
        // this.$refs['manage'].show(row.innrulid)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteInnerRuleInfo(row.innrulid)
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
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    height: 800px;
    overflow: auto;
  }

  .lr-layout > .right {
    flex: 1;
  }

  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
