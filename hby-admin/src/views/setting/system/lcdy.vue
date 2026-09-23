<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <vab-query-form>
        <!-- <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="模块名称"
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
        </el-form>
      </vab-query-form-top-panel> -->

        <vab-query-form-right-panel class="option-row">
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
          <el-button
            type="success"
            @click="handleEdit()"
            v-if="hasAuth('XTLCDYadd')"
          >
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column align="center" label="模块名称" prop="module" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="模板描述"
            prop="remark"
            show-overflow-tooltip
            v-if="item.name === '模板描述'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            width="100"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              <el-switch
                :disabled="!hasAuth('XTLCDYstatus')"
                v-model="row.status"
                active-value="ON"
                inactive-value="OFF"
                @change="handleStatusChage(row, $event)"
              />
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="220"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleView(row)"
              v-if="hasAuth('XTLCDYview')"
            >
              查看流程定义
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="hasAuth('XTLCDYedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="hasAuth('XTLCDYdelete')"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <!-- <LcdyEdit ref="edit" @fetch-data="fetchData" /> -->
    <LcdAdd
      ref="lcdadd"
      v-if="addStatus"
      @fetch-data="fetchData"
      @close="close"
    />
    <LcdyView ref="view" />
  </div>
</template>

<script>
  import { deleteFlow, getFlowList, saveFlow } from '@/api/setting/system'
  import LcdAdd from './components/lcdy/lcdyAdd.vue'
  // import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import LcdyView from '@/views/setting/system/components/LcdyView/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Lcdy',
    components: {
      //  LcdyEdit,
      LcdyView,
      LcdAdd,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        addStatus: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-lcdy-search',
        tableKey: 'setting-system-lcdy-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '模板描述' }, { name: '状态' }], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
      },
      close() {
        this.addStatus = false
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
        } = await getFlowList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleView(row) {
        this.$refs['view'].show(row)
      },
      handleEdit(row) {
        this.addStatus = true
        if (row) {
          this.$nextTick(() => {
            this.$refs['lcdadd'].showDtails(row)
          })
        }
        // this.$refs['edit'].showEdit(row)
      },
      async handleStatusChage(row, status) {
        const params = Object.assign({}, row, { status })
        delete params.tblOrganization
        await saveFlow(params)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteFlow({ settingId: row.settingId })
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '流程编号', key: 'flownumber' },
          { name: '流程名称', key: 'flowname' },
          { name: '责任部门', key: 'departincharge' },
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
                this.queryForm[x.key] = ''
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
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
