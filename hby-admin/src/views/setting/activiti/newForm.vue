<template>
  <div class="system-log-container">
    <div class="lr-layout">
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
                <el-select
                  v-model="queryForm.parentCatalogueId"
                  placeholder="请选择一级菜单"
                  @change="changeModule"
                >
                  <el-option
                    v-for="item in moduleLists"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
                <!-- <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.reportname"
                    v-if="item.name === '模块名称'"
                    clearable
                    placeholder="模块名称"
                  />
                  <el-input
                    v-model="queryForm.reportname"
                    v-if="item.name === '页面名称'"
                    clearable
                    placeholder="页面名称"
                  />
                </el-form-item> -->
                <!-- <el-form-item>
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
                </el-form-item> -->
                <!-- <el-form-item>
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
                </el-form-item> -->
              </el-form>
            </vab-query-form-top-panel>
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
            <el-button type="success" @click="add" v-if="hasAuth('XTZDYBDadd')">
              新增
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            row-key="id"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            style="width: 100%"
            @sort-change="sortChange"
          >
            <!-- <el-table-column label="隶属模块" prop="moduleType" align="center">
              <template #default="{ row }">
                {{ moduleName[row.moduleType] }}
              </template>
            </el-table-column> -->
            <el-table-column
              label="模块编码"
              prop="sceneCode"
              align="center"
              sortable="custom"
            />
            <div v-for="(item, index) in filedNow" :key="index">
              <!-- <el-table-column
                label="模块编码"
                prop="sceneCode"
                align="center"
                v-if="item.name === '模块编码'"
                sortable="custom"
              /> -->
              <el-table-column
                label="页面名称"
                prop="catalogueName"
                align="center"
                v-if="item.name === '页面名称'"
              />
              <el-table-column
                label="创建时间"
                prop="createdTime"
                align="center"
                v-if="item.name === '创建时间'"
              />
              <el-table-column
                label="修改时间"
                prop="updatedTime"
                align="center"
                v-if="item.name === '修改时间'"
              />
            </div>
            <el-table-column label="操作" prop="name" align="center">
              <template slot-scope="{ row }">
                <el-button
                  type="text"
                  @click="handleAddField(row)"
                  v-if="hasAuth('XTZDYBDinput')"
                >
                  录入字段
                </el-button>
                <!-- <el-button
                  type="text"
                  @click="handleAdd(row)"
                  v-if="hasAuth('XTZDYBDinput')"
                >
                  录入字段
                </el-button> -->
                <el-button
                  type="text"
                  @click="handleEditField(row)"
                  v-if="hasAuth('XTZDYBDedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="hasAuth('XTZDYBDdelete')"
                >
                  删除
                </el-button>
                <!-- <el-button
                  type="text"
                  @click="handleShowDialog(row)"
                  v-if="hasAuth('XTZDYBDview')"
                >
                  预览
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <RuleModel ref="edit" @fetch-data="fetchData" />
        <AddModel ref="add" @fetch-data="fetchData" />
        <EditModel ref="EditModel" @fetch-data="fetchData" />
      </div>
    </div>
  </div>
</template>

<script>
  import {
    getZDYTableData,
    delZDYData,
    changeListStatus,
  } from '@/api/setting/auths'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import RuleModel from './components/addRuleModel.vue' //新增模块信息
  import AddModel from './components/AddFormEdit.vue' //录入字段
  import EditModel from './components/editFormEdit.vue' //修改录入字段
  import { hasAuth } from '@/utils'
  import { getAuthList, saveModelInfo } from '@/api/setting/auths'

  export default {
    name: 'Download',
    components: { filterTable, RuleModel, AddModel, EditModel, filterSearch },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          moduleType: '',
          parentCatalogueId: '', //下拉数据的id
        },
        tableKey: 'setting-activiti-form-list',
        localKey: 'setting-activiti-form-search',
        filedAll: [
          { name: '页面名称' },
          { name: '创建时间' },
          { name: '修改时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        moduleName: {
          xtsz: '系统设置',
          htgl: '合同管理',
          fwgl: '法务管理',
          znjk: '预警平台',
          nkhg: '内控管理',
          hggl: '合规管理',
          znsj: '内部审计',
          znfx: '智能分析',
          fxgk: '风险管控',
          zhjd: '综合监督',
          sjzt: '数据中台',
        },
        sortFields: '',
        sortFlag: 'asc',
        moduleLists: [],
      }
    },
    created() {
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      // this.fetchData()
    },
    mounted() {
      this.getModuleList()
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
        let fields = [
          { name: '模块名称', key: 'unitName' },
          { name: '页面名称', key: 'employmentTermType' },
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          parentCatalogueId: '',
          pageNumber: 1,
          pageSize: 20,
          moduleType: this.moduleType,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = false
        const {
          data: { tlist },
        } = await getZDYTableData({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })

        this.list = tlist || []
        this.listLoading = false
      },
      add(row) {
        if (this.queryForm.parentCatalogueId) {
          this.$refs['edit'].showEdit({
            moduleType: this.queryForm.moduleType,
            parentCatalogueId: this.queryForm.parentCatalogueId,
          })
        } else {
          this.$baseMessage(
            '请先选择一级菜单',
            'warning',
            'vab-hey-message-warning'
          )
        }
      },
      handleAdd(row) {
        this.$refs['add'].showEdit(row)
      },
      handleAddField(row) {
        console.log(row)
        window.open(
          `http://192.0.2.16:9080?sceneCode=${row.sceneCode}&sceneId=${row.id}&editForm=false`,
          '_blank'
        )
      },
      handleEditField(row) {
        window.open(
          `http://192.0.2.16:9080?sceneCode=${row.sceneCode}&sceneId=${row.id}&editForm=true`,
          '_blank'
        )
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await delZDYData({ id: row.id })
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
      async changeStatus(row) {
        const res = await changeListStatus({
          id: row.id,
          state: row.state == 0 ? 1 : 0,
        })
        if (res.code == 200) {
          this.$baseMessage(
            '状态修改成功',
            'success',
            'vab-hey-message-success'
          )
          this.fetchData()
        }
      },

      handleEdit(row) {
        this.$refs['EditModel'].showEdit(row)
      },
      getModuleList() {
        // 获取当前的路由信息,截取模块类型
        const moduletype = this.$route.path.split('/')[2]
        this.queryForm.moduleType = moduletype
        this.moduleType = moduletype
        getAuthList({
          moduletype,
          judge: 1,
        }).then((res) => {
          this.moduleLists = res.data.rightList
          // this.fetchData()
        })
      },
      changeModule(val) {
        this.queryForm.parentCatalogueId = val
        this.fetchData()
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
