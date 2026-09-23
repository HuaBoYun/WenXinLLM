<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="18">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.contracttype"
                    clearable
                    placeholder="合同类型"
                    v-if="item.name === '合同类型'"
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
            <!-- <el-button
              native-type="submit"
              type="success"
              @click="handleFlowSelect"
              v-if="hasAuth('HTLCSZselcet')"
            >
              选择流程
            </el-button> -->
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            row-key="typeId"
            highlight-current-row
            @current-change="handleRowChange"
            @sort-change="sortChange"
            :default-expand-all="false"
            :tree-props="{ children: 'children', hasChildren: '' }"
          >
            <el-table-column
              align="left"
              label="合同类型"
              prop="typeName"
              show-overflow-tooltip
            />
            <!-- <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="管理流程ID"
                prop="settingid"
                show-overflow-tooltip
                v-if="item.name === '管理流程ID'"
                sortable="custom"
              />
            </div>  -->
            <el-table-column align="center" label="操作" show-overflow-tooltip>
              <template #default="{ row }">
                <el-dropdown>
                  <el-button v-if="hasAuth('XTLCSJlcjsj')" type="text">
                    流程设计
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="jump(row, 5)">
                      合同订立
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="jump(row, 7)">
                      合同变更
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <el-dropdown>
                  <el-button
                    style="margin-left: 20px"
                    type="text"
                    v-if="hasAuth('XTLCSJlcList')"
                  >
                    流程列表
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="openModal(row, 5)">
                      合同订立
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="openModal(row, 7)">
                      合同变更
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <ChooseFlow ref="choose" @selected="handleFlowSelected" />
        <ProcessList ref="process" @fetchData="fetchData" />
      </div>
    </div>
  </div>
</template>

<script>
  import {
    getContractSettingList,
    saveAssociateContract,
    SSOToJNFD,
  } from '@/api/setting/system'
  import { doDelete } from '@/api/table'
  import ChooseFlow from './components/ChooseFlow.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'
  import ProcessList from './components/ProcessListTemplate.vue'
  import store from '@/store'

  export default {
    name: 'Htlcsz',
    components: { ChooseFlow, filterSearch, filterTable, ProcessList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          contracttype: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        currentRow: undefined,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-htlcsz-search',
        tableKey: 'setting-system-htlcsz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [], //所有表格项
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
        this.queryForm = this.$options.data().queryForm
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
        const { data } = await getContractSettingList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = data
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleRowChange(row) {
        this.currentRow = row
      },
      handleFlowSelect() {
        if (!this.currentRow) {
          this.$message.error('请选择合同流程')
          return
        }
        this.$refs['choose'].show()
      },
      async handleFlowSelected(data) {
        const { msg, code } = await saveAssociateContract({
          settingId: data.settingId,
          typeId: this.currentRow.typeId,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      handleSetting(row) {
        this.currentRow = row
        this.$refs['choose'].show()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '合同类型', key: 'contracttype' }]
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
      jump(row, tableId) {
        console.log(row, tableId)
        const token = store.getters['user/token']
        SSOToJNFD()
          .then((result) => {
            window.open(
              // 'http://localhost:3000/workFlow/flowEngine?token=' +
              // 'https://192.0.2.14:9100/workFlow/flowEngine?token=' +
              'https://www.example.com/workFlow/flowEngine?token=' +
                result.data.ymToken +
                '&showzdy=zdy' +
                '&tableId=' +
                tableId +
                '&typeId=' +
                row.typeId +
                '&xtoken=' +
                token
            )
          })
          .catch((err) => {})
      },
      openModal(row, tableId) {
        this.$refs['process'].show({}, tableId, row.typeId)
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
