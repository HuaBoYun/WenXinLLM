<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item v-for="(item, index) in searchItem" :key="index">
                <el-input
                  v-model="queryForm.riskNumber"
                  clearable
                  placeholder="编号"
                  v-if="item.name === '编号'"
                />
                <el-input
                  v-model="queryForm.riskclass"
                  clearable
                  :placeholder="
                    ['一', '二', '三'][
                      !queryForm.riskLevel ? 0 : queryForm.riskLevel
                    ] + '类'
                  "
                  v-if="item.name === '类'"
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
                <el-button
                  v-if="queryForm.riskLevel"
                  native-type="submit"
                  type="primary"
                  @click="goBack"
                >
                  返回
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
            <!-- <vab-query-form-right-panel :span="24">
              <el-form :inline="true" label-width="0">
                <el-form-item>
                  <file-upload
                    accept=".xls,.xlsx"
                    api="/setting/importRiskClassExcel"
                    :data="{
                      riskLevel: 1,
                      parentId: '',
                      riskNumber: '',
                      riskclass: '',
                    }"
                    :show-file-list="false"
                    @success="fetchData"
                  >
                    <el-button type="success">导入</el-button>
                  </file-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="success" @click="handleAdd">新建</el-button>
                </el-form-item>
              </el-form>
            </vab-query-form-right-panel> -->
          </el-card>
        </vab-query-form>
        <el-card shadow="never" class="secondCard">
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
              @click="handleAdd"
              v-if="hasAuth('XTFXFLadd')"
            >
              新建
            </el-button>
            <file-upload
              accept=".xls,.xlsx"
              api="/setting/importRiskClassExcel"
              :data="{
                riskLevel: 1,
                parentId: '',
                riskNumber: '',
                riskclass: '',
              }"
              :show-file-list="false"
              @success="fetchData"
              style="display: inline-block; margin-left: 10px"
              v-if="hasAuth('XTFXFLimport')"
            >
              <el-button type="success">导入</el-button>
            </file-upload>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column
              align="center"
              label="编号"
              prop="riskNumber"
              show-overflow-tooltip
              sortable="custom"
            />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                :label="
                  ['一', '二', '三'][
                    !queryForm.riskLevel ? 0 : queryForm.riskLevel
                  ] + '类'
                "
                prop="riskclass"
                show-overflow-tooltip
                v-if="item.name === '一类'"
              >
                <template #default="{ row }">
                  <span v-if="queryForm.riskLevel >= 2">
                    {{ row.riskclass }}
                  </span>
                  <el-button v-else type="text" @click="fetchLevel(row)">
                    {{ row.riskclass }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="政策依据"
                prop="policybasis"
                v-if="item.name === '政策依据'"
              />
            </div>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="100"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('XTFXFLedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="hasAuth('XTFXFLdelete')"
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
        <risk-edit ref="edit" @fetch-data="fetchData" />
        <send-model ref="sendModel" @fetch-data="fetchData" />
        <send ref="send" @fetch-data="fetchData" />
      </div>
    </div>
  </div>
</template>

<script>
  import { removeRiskClass, selectallrisk } from '@/api/setting/themeRepertory'
  import RiskEdit from '@/views/setting/themeRepertory/components/RiskEdit'
  import SendModel from '@/views/setting/themeRepertory/components/SendModel'
  import Send from '@/views/setting/themeRepertory/components/Send'
  import FileUpload from '@/components/FileUpload.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Theme',
    components: {
      Send,
      SendModel,
      RiskEdit,
      FileUpload,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          riskNumber: undefined,
          riskclass: undefined,
          pageNumber: 1,
          pageSize: 20,
          parentId: undefined,
          riskLevel: undefined,
          parentName: undefined,
        },
        queryTemp: [
          {
            parentId: undefined,
            riskLevel: undefined,
            parentName: undefined,
          },
          {
            parentId: undefined,
            riskLevel: undefined,
            parentName: undefined,
          },
          {
            parentId: undefined,
            riskLevel: undefined,
            parentName: undefined,
          },
        ],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-themeRepertory-risk-search',
        tableKey: 'setting-themeRepertory-risk-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '一类' }, { name: '政策依据' }], //所有表格项
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
      resetQueryForm() {
        const level = this.queryForm.riskLevel
          ? parseInt(this.queryForm.riskLevel)
          : 0
        this.queryForm = this.$options.data().queryForm
        if (level > 0) {
          this.queryForm.parentId = this.queryTemp[level].parentId
          this.queryForm.riskLevel = this.queryTemp[level].riskLevel
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      goBack() {
        const level = parseInt(this.queryForm.riskLevel) - 1
        this.queryForm.parentId = this.queryTemp[level].parentId
        this.queryForm.riskLevel = this.queryTemp[level].riskLevel
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
      fetchLevel(row) {
        if (row) {
          console.warn('row', row)
          const level = parseInt(row.risklevel)
          this.queryTemp[level].parentId = row.riskid
          this.queryTemp[level].riskLevel = row.risklevel
          this.queryTemp[level].parentName = row.riskclass

          this.queryForm.parentId = row.riskid
          this.queryForm.riskLevel = row.risklevel
          this.queryForm.parentName = row.riskclass
        }
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = false
        const {
          data: { pageInfo },
        } = await selectallrisk({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})

        this.list = pageInfo ? pageInfo.tlist : []
        this.total = pageInfo ? pageInfo.totalRecord : 0
        this.listLoading = false
      },
      handleAdd() {
        const level = this.queryForm.riskLevel
          ? parseInt(this.queryForm.riskLevel)
          : 0
        this.$refs['edit'].showEdit({
          parentid: this.queryTemp[level].parentId || '',
          riskparentname: this.queryTemp[level].parentName || '',
          risklevel: level + 1,
        })
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeRiskClass({ riskId: row.riskid })
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
          { name: '编号', key: 'riskNumber' },
          { name: '类', key: 'riskclass' },
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
