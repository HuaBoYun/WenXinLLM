<template>
  <div class="system-log-container">
    <div class="lr-layout">
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
                <el-form-item>
                  <el-select
                    v-model="queryForm.moduletype"
                    placeholder="请选择模块"
                    @change="fetchData"
                  >
                    <!-- <el-option label="系统设置" value="xtsz" />
                <el-option label="合同管理" value="htgl" />
                <el-option label="法务管理" value="fwgl" />
                <el-option label="预警平台" value="znjk" />
                <el-option label="内控管理" value="nkhg" />
                <el-option label="合规管理" value="hggl" />
                <el-option label="内部审计" value="znsj" />
                <el-option label="智能分析" value="znfx" />
                <el-option label="风险管控" value="fxgk" />
                <el-option label="综合监督" value="zhjd" />
                <el-option label="数据中台" value="sjzt" /> -->
                    <el-option
                      v-for="item in moduleLists"
                      :key="item.id"
                      :label="item.projectName"
                      :value="item.uniqueIdentification"
                    />
                  </el-select>
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
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            row-key="id"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            style="width: 100%"
          >
            <el-table-column label="菜单名称" prop="name" />

            <div v-for="(item, index) in filedNow" :key="index">
              <!-- <el-table-column
            label="状态"
            prop="visible"
            v-if="item.name === '状态'"
          >
            <template slot-scope="{ row }">
              {{ row.visible === 1 ? '正常' : row.visible === 0 ? '禁用' : '' }}
            </template>
          </el-table-column> -->
              <el-table-column
                label="是否有表单"
                prop="visible"
                v-if="item.name === '是否有表单'"
              ></el-table-column>
            </div>
            <el-table-column label="操作" prop="name">
              <template slot-scope="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="row.type == 1"
                >
                  新建自定义方案
                </el-button>
                <!-- <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="row.type == 1"
            >
              修改
            </el-button> -->
                <el-button
                  type="text"
                  @click="handleShowDialog(row)"
                  v-if="row.type == 1"
                >
                  预览
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <FormEdit
          ref="edit"
          @fetch-data="fetchData"
          :key="queryForm.moduletype"
        />
      </div>
    </div>
  </div>
</template>

<script>
  import { delAuthInfo, getAuthList, updAuthsStatus } from '@/api/setting/auths'
  import filterTable from '@/components/filterTable.vue'
  import FormEdit from './components/AddFormEdit.vue'
  import { getModuleList } from '@/api/setting/system'

  export default {
    name: 'Download',
    components: { filterTable, FormEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          moduletype: 'xtsz',
          judge: 1,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-activiti-form-search',
        tableKey: 'setting-activiti-form-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '是否有表单' }], //所有表格项
        filedNow: [],
      }
    },
    created() {
      this.fetchData()
      this.moduleList()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
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
      async fetchData() {
        this.listLoading = true
        const { data } = await getAuthList(this.queryForm)
        if (data.rightList) {
          this.list = data.rightList
        } else {
          this.list = []
        }

        this.listLoading = false
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, this.queryForm.moduletype)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await delAuthInfo({ rightId: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleUpdStatus(row) {
        this.$baseConfirm(
          `你确定要${row.visible === 1 ? '禁用' : '启用'}当前项吗`,
          null,
          async () => {
            const { msg } = await updAuthsStatus({
              rightId: row.id,
              visible: row.visible === 0 ? 1 : 0,
            })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        )
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      moduleList() {
        getModuleList({}).then((res) => {
          console.log(1111111, res)
          this.moduleLists = res.data
        })
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '模块', key: 'moduletype' }]
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
