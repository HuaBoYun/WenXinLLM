<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <!-- <div class="left">
        <manage-tree @changeNode="changeNode"></manage-tree>
      </div> -->
      <div class="right">
        <vab-query-form>
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
                    placeholder="发文文号"
                    v-if="item.name === '发文文号'"
                  />
                  <el-input
                    v-model="queryForm.rulename"
                    clearable
                    placeholder="制度名称"
                    v-if="item.name === '制度名称'"
                  />
                  <!-- <el-input
                    v-model="queryForm.innruletype"
                    clearable
                    placeholder="类别"
                    v-if="item.name === '类别'"
                  /> -->
                  <el-date-picker
                    v-model="queryForm.Date"
                    align="right"
                    end-placeholder="生效结束时间"
                    range-separator="至"
                    start-placeholder="生效开始时间"
                    type="daterange"
                    unlink-panels
                    value-format="yyyy-MM-dd"
                    v-if="item.name === '生效时间'"
                  />
                  <!-- <el-select
                    v-model="queryForm.status"
                    placeholder="状态"
                    v-if="item.name === '状态'"
                  >
                    <el-option label="草稿" value="草稿"></el-option>
                    <el-option
                      label="发布待审核"
                      value="发布待审核"
                    ></el-option>
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option
                      label="发布审核拒绝"
                      value="发布审核拒绝"
                    ></el-option>
                    <el-option label="已修订" value="已修订"></el-option>
                    <el-option label="已废止" value="已废止"></el-option>
                    <el-option
                      label="废止待审核"
                      value="废止待审核"
                    ></el-option>
                    <el-option
                      label="废止审核拒绝"
                      value="废止审核拒绝"
                    ></el-option>
                  </el-select> -->
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

                <!-- <el-form-item>
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="对方谈判人"
                  />
                </el-form-item> -->
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
            <el-button type="success" @click="handleAdd">新建</el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="发文文号" prop="rulenumber">
              <template #default="{ row }">
                <el-button @click="handleView(row, true)" type="text">
                  {{ row.rulenumber }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="制度名称"
                prop="rulename"
                v-if="item.name === '制度名称'"
              />
              <el-table-column
                align="center"
                label="生效日期"
                prop="publishdate"
                :formatter="formatDate"
                v-if="item.name === '生效日期'"
              />
            </div>
            <!-- <el-table-column align="center" label="类别" prop="innruletype" /> -->
            <!-- <el-table-column align="center" label="状态" prop="status" /> -->
            <!-- <el-table-column align="center" label="是否协商一致" prop="data" />
            <el-table-column align="center" label="创建日期" prop="data" /> -->
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="180"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">修改</el-button>
                <el-button type="text" @click="preview(row)">预览</el-button>
                <el-button type="text" @click="handleDelete(row)">
                  删除
                </el-button>
                <!-- <el-button type="text" @click="exportFile(row)">下载</el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          class="pagination"
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <ManageEdit ref="edit" @fetch-data="fetchData" />
    <LawPreview ref="preview" />
  </div>
</template>

<script>
  import {
    deleteInnerRuleInfo,
    getInnerRulePageList,
    exportInnerRuleInfo,
    selectInnerRuleInfo,
  } from '@/api/contract/htzd'
  import { UTCformat } from '@/utils/index'
  import LawPreview from './components/LawPreview'
  import ManageTree from './components/ManageTree'
  import ManageEdit from './ManageEdit.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Consult',
    components: {
      ManageEdit,
      LawPreview,
      ManageTree,
      filterTable,
      filterSearch,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },

        localKey: 'contract-htzd-index-search',
        tableKey: 'contract-htzd-index-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [{ name: '制度名称' }, { name: '生效日期' }],
      }
    },
    created() {
      this.fetchData()
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
    },
    methods: {
      // 动态筛选 动态表格 初始化数据&相关方法
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      getFiled() {
        let fields = [
          { name: '发文文号', key: 'rulecode' },
          { name: '制度名称', key: 'rulename' },
          { name: '类别', key: 'innruletype' },
          { name: '生效时间', key: 'Date' },
          { name: '状态', key: 'status' },
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
      //预览
      async preview(row) {
        const res = await selectInnerRuleInfo(row.innrulid)
        if (res.data.attList.length == 0) {
          this.$message({
            type: 'info',
            message: '该制度没有附件',
          })
          return
        }
        const { data } = await getPrivewAttInfo({
          attId: res.data.attList[0].attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      //导出
      async exportFile(row) {
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
        this.queryForm = {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      //回调
      changeNode(node) {
        this.queryForm.publishorg = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
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
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleView(row, flag) {
        this.$refs['edit'].showEdit(row, flag)
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
  }

  .lr-layout > .right {
    flex: 1;
  }
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
