<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.sealName"
                clearable
                placeholder="用印名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '用印名称'"
              ></el-input>
              <el-input
                v-model="queryForm.sealNum"
                clearable
                placeholder="用印枚数"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '用印枚数'"
                @keypress.native="handleSealNumKeypress"
                @input="handleSealNumInput"
              ></el-input>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <!-- <el-table-column align="center" label="用印名称" prop="sealName">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.sealName }}
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column type="selection" width="55" />
        <el-table-column
          align="center"
          label="序号"
          prop="sort"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="用印枚数"
            prop="sealNum"
            v-if="item.name === '用印枚数'"
          />
          <el-table-column
            align="center"
            label="用印部门"
            prop="sealWorkUnitName"
            v-if="item.name === '用印部门'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="经办人"
            prop="transactorName"
            v-if="item.name === '经办人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="发往单位"
            prop="sendorg"
            v-if="item.name === '发往单位'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="用印事由"
            prop="sealReasons"
            v-if="item.name === '用印事由'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="用印单位"
            prop="sealWorkUnitName"
            v-if="item.name === '用印单位'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="签批人"
            prop="transactorName"
            v-if="item.name === '签批人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="remark"
            v-if="item.name === '备注'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="日期"
            prop="createdTime"
            v-if="item.name === '日期'"
          ></el-table-column>

          <el-table-column
            align="center"
            label="用印名称"
            prop="sealName"
            v-if="item.name === '用印名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row, 'detail')">
                {{ row.sealName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="state"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤回'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleUp(row)">上移</el-button>
            <el-button type="text" @click="handleDown(row)">下移</el-button>
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
    <yxsydViews ref="edit" @fetchData="fetchData"></yxsydViews>
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import yxsydViews from './components/yxsydViews.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import {
    getYXSYTZList,
    YXSYTZListUp,
    YXSYTZListDown,
    exportYZSYTZ,
  } from '@/oapi/ypns_zhgl/yxsyd'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/setting/system.js'
  export default {
    components: {
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      yxsydViews,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          sealName: null,
          sealNum: null,
        },
        filedAll: [
          { name: '日期' },
          { name: '用印单位' },
          { name: '经办人' },
          { name: '发往单位' },
          { name: '用印事由' },
          { name: '用印单位' },
          { name: '用印名称' },
          { name: '用印枚数' },
          { name: '签批人' },
          { name: '备注' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zhgl-yzsytz-search',
        tableKey: 'oilAudit-zhgl-yzsytz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        select: [],
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 按键时只允许输入数字
      handleSealNumKeypress(event) {
        const charCode = event.which ? event.which : event.keyCode
        // 只允许数字键 0-9
        if (charCode < 48 || charCode > 57) {
          event.preventDefault()
        }
      },
      // 处理用印枚数输入，过滤非数字字符
      handleSealNumInput(value) {
        if (value) {
          // 只保留数字字符
          const numStr = String(value).replace(/[^0-9]/g, '')
          if (numStr !== String(value)) {
            this.$nextTick(() => {
              this.queryForm.sealNum = numStr
            })
          }
        }
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '用印名称', key: 'name' },
          { name: '用印枚数', key: 'code' },
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
        } = await getYXSYTZList({
          ...this.queryForm,
          sealNum: Number(this.queryForm.sealNum),
        })
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
        this.setCheckedRows()
      },
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteInfo({ id: row.id })
          if (res.code == 200) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          createType: 1,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(38, row.id)
      },
      async handleApprovalDetail(row) {
        // 办理
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 38,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      async handleUp(row) {
        const res = await YXSYTZListUp({ id: row.id })
        if (res.code == 200) {
          this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          await this.fetchData()
        }
        if (res.code == 408) {
          this.$baseMessage(res.msg, 'error', 'vab-hey-message-error')
          return
        }
      },
      async handleDown(row) {
        const res = await YXSYTZListDown({ id: row.id })
        if (res.code == 200) {
          this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          await this.fetchData()
        }
        if (res.code == 408) {
          this.$baseMessage(res.msg, 'error', 'vab-hey-message-error')
          return
        }
      },
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const res = await exportYZSYTZ({ ...this.queryForm, ids })
        downloadFile(res, '印章使用台账.xlsx')
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
    },
  }
</script>

<style scoped lang="scss">
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
