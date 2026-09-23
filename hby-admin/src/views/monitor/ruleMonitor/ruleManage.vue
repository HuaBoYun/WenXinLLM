<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <LeftOrgTree @select="changeNode" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel>
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
                    placeholder="规则编码"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '规则编码'"
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
            <el-button
              type="success"
              @click="showRuleDraw(row, 'add')"
              v-if="hasAuth('GZGLaddRule')"
            >
              新建规则拖拽
            </el-button>
            <el-button
              type="success"
              @click="showRuleInfo(row, 'add')"
              v-if="hasAuth('GZGLadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column align="center" label="规则编码" sortable="custom">
              <template #default="{ row }">
                <span
                  style="color: red; cursor: pointer"
                  @click="showRuleInfo(row, 'look')"
                  v-if="hasAuth('GZGLruleInfo')"
                >
                  {{ row.rulecode }}
                </span>
                <span v-else>
                  {{ row.rulecode }}
                </span>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="规则名称"
                prop="rulename"
                v-if="item.name === '规则名称'"
              />

              <el-table-column
                align="center"
                label="优先级"
                prop="rulepriority"
                v-if="item.name === '优先级'"
              />
              <el-table-column
                align="center"
                label="执行状态"
                prop="risklevel"
                v-if="item.name === '执行状态'"
              />
              <el-table-column
                align="center"
                label="预警结果"
                v-if="item.name === '预警结果' && hasAuth('GZGLresult')"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    style="color: red"
                    @click="result(row)"
                  >
                    结果
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="状态" prop="risklevel" />
            </div>

            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="showRuleInfo(row, 'edit')"
                  v-if="hasAuth('GZGLedit')"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px" @command="handleCommand">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      @click.native="handMore(row, 'start')"
                      v-if="hasAuth('GZGLstart')"
                    >
                      启用
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handMore(row, 'action')"
                      v-if="hasAuth('GZGLaction')"
                    >
                      执行
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handMore(row, 'copy')"
                      v-if="hasAuth('GZGLcopy')"
                    >
                      复制到行业规则库
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handMore(row, 'delete')"
                      v-if="hasAuth('GZGLdelete')"
                    >
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
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
      </div>
    </div>

    <rule-info ref="ruleInfo" />
    <RuleResult ref="ruleEdit" />
    <drawModal ref="drawModal" />
  </div>
</template>

<script>
  import {
    CheckUpdate,
    CopyExchange,
    RuleDel,
    RuleExecute,
    Rulesmgmt,
    RuleUpdateStatus,
    SjmxZxsql,
  } from '@/api/monitor/rule/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import LeftOrgTree from '@/views/monitor/components/LeftOrgTree'
  import RuleInfo from '@/views/monitor/ruleMonitor/components/RuleInfo'
  import RuleResult from './components/RuleResult.vue'
  import drawModal from './components/drawModal.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'RuleManage',
    components: {
      RuleInfo,
      LeftOrgTree,
      RuleResult,
      filterTable,
      filterSearch,
      drawModal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          rulecode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        orgid: '',
        orgname: '',
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-ruleMonitor-ruleManage-search',
        tableKey: 'monitor-ruleMonitor-ruleManage-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '规则名称' },
          { name: '优先级' },
          { name: '执行状态' },
          { name: '预警结果' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      // this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
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

      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '规则编码', key: 'rulecode' }]
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      changeNode(node) {
        this.orgid = node.id
        this.orgname = node.label
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
            pageBean: { total, records },
          },
        } = await Rulesmgmt({
          ...this.queryForm,
          orgId: this.orgid,
          rulecode: this.queryForm.rulecode || '',
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })

        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      resetQueryForm() {
        this.queryForm = {
          rulecode: '',
          // name: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },

      showRuleInfo(row, flag) {
        if (flag == 'add') {
          this.$refs['ruleInfo'].showEdit(
            { orgid: this.orgid, orgname: this.orgname },
            flag
          )
        } else {
          this.$refs['ruleInfo'].showEdit(row, flag)
        }
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      handMore(row, e) {
        if (e == 'delete') {
          this.$baseConfirm('你确定要删除当前项吗', null, async () => {
            RuleDel({ selectedruleid: row.ruleid }).then((res) => {
              if (res.code == 200) {
                this.$message.success(res.msg)
                this.fetchData()
              } else {
                this.$message.error(res.msg)
              }
            })
          })
        } else if (e == 'action') {
          SjmxZxsql({ ruleid: row.ruleid }).then((res) => {
            if (res.code == 1) {
              this.$message.success(res.msg)
              this.fetchData()
            } else {
              this.$message.error(res.msg)
            }
          })
        } else if (e == 'copy') {
          const params = {
            bookid: row.connectionstrings,
            id: row.ruleid,
            orgid: row.orgid,
          }
          CopyExchange(params).then((res) => {
            if (res.code == 200) {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
          })
        } else if (e == 'start') {
          RuleUpdateStatus({ selectedruleid: row.ruleid }).then((res) => {
            if (res.code == 200) {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      },
      result(row) {
        this.$refs['ruleEdit'].showEdit(row, '规则管理')
      },
      handleEdit2(row) {
        CheckUpdate({ selectedruleid: row.ruleid }).then((res) => {})
      },
      showRuleDraw(row) {
        // this.$refs['drawModal'].showEdit(row)
        window.open('http://192.0.2.16:8090/urule/frame')
      },
    },
  }
</script>
<style scoped>
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
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 10px;
    background: #fff;
    height: 100%;
  }
  .lr-layout > .right {
    padding: 0 20px 0 0;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
