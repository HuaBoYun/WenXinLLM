<template>
  <el-dialog
    :visible.sync="dialogVisible"
    title="选择评价缺陷"
    width="80%"
    :close-on-click-modal="false"
    :append-to-body="true"
  >
    <div class="system-log-container">
      <vab-query-form>
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.bugnumber"
                clearable
                placeholder="缺陷编号"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.defectsname"
                clearable
                placeholder="缺陷名称"
              />
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="queryForm.Date"
                clearable
                end-placeholder="发生结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="发生开始日期"
                :style="{ width: '100%' }"
                type="daterange"
                value-format="yyyy-MM-dd"
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
          </el-form>
        </vab-query-form-top-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="缺陷编号"
          prop="bugnumber"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.bugnumber }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="缺陷名称"
          prop="defectsname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="缺陷描述"
          prop="bugdescripte"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="发生时间"
          prop="discovertime"
          show-overflow-tooltip
          :formatter="formatDate"
        />

        <el-table-column
          align="center"
          label="缺陷等级"
          prop="bugcrilevel"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="状态" prop="status">
          <template #default="{ row }">
            {{
              row.status == 1
                ? '审批中'
                : row.status == 2
                ? '已退回'
                : row.status == 3
                ? '已撤回'
                : row.status == 4
                ? '已终止'
                : row.status == 5
                ? '已跟踪'
                : row.status == 6
                ? '已完成'
                : '未审批'
            }}
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
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="confirmSelection">确 定</el-button>
    </div>
    <flaw-info ref="edit" @fetch-data="fetchData" />
  </el-dialog>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/implement'
  import {
    bugcriidList,
    defectDel,
    defectDetail,
    defectFileExport,
    defectList,
  } from '@/api/audit/question'
  import { formatDay, parseTime } from '@/utils/index'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  export default {
    name: 'Flaw',
    components: { FlawInfo },
    data() {
      return {
        dialogVisible: false,
        list: [],
        bugcriidList: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          bugnumber: '',
          bugcriid: '',
          orgid: '',
          defectsname: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        orgId: '',
        current: undefined,
        flagTitle: false,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        selectedFlaws: [], // 存储选中的缺陷
        // 存储所有已选择的数据，用于跨页多选
        allSelectedData: [],
      }
    },
    created() {
      // 初始化方法保留
    },
    methods: {
      /**
       * @description: 显示弹窗
       */
      showDialog() {
        this.dialogVisible = true
        // 清空所有选中数据
        this.allSelectedData = []
        this.fetchData()
        this.fectchBugcriidList()
        this.getTreeData()
      },

      /**
       * @description: 处理表格选择变化
       */
      handleSelectionChange(selection) {
        console.log('handleSelectionChange - selected:', selection)
        this.selectedFlaws = selection
      },

      /**
       * @description: 处理单行选择
       */
      handleSelection(val, row) {
        const i = this.allSelectedData.findIndex((x) => x.bugnumber == row.bugnumber)
        if (i < 0) {
          this.allSelectedData.push(row)
        } else {
          this.allSelectedData.splice(i, 1)
        }
      },

      /**
       * @description: 处理全选
       */
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.allSelectedData.some((x) => x.bugnumber == row.bugnumber)) {
              this.allSelectedData.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.allSelectedData.findIndex((x) => x.bugnumber == row.bugnumber)
            if (i >= 0) {
              this.allSelectedData.splice(i, 1)
            }
          })
        }
      },

      /**
       * @description: 确认选择
       */
      confirmSelection() {
        this.$emit('confirm', this.allSelectedData)
        this.closeDialog()
      },

      /**
       * @description: 关闭弹窗
       */
      closeDialog() {
        this.dialogVisible = false
        // 清空所有选中数据
        this.allSelectedData = []
        this.selectedFlaws = []
      },

      async getTreeData() {
        let res = await findOrganizationByTreeAllss()
        this.orgId = res[0].id
      },
      resetSearch() {
        this.resetQueryForm()
        // 重置搜索时清空所有选中数据
        this.allSelectedData = []
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      async fectchBugcriidList() {
        const res = await bugcriidList()
        this.bugcriidList = res.data.list
      },
      setTree(node) {
        let id = node.pId === 1 ? undefined : node.id
        this.queryForm.orgid = id
        this.orgId = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
        this.btnLoading = false
        this.listLoading = true
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        try {
          const {
            data: {
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = await defectList({ ...other, startDate, endDate })
          this.list = list
          this.list.forEach((item) => {
            if (item.discovertime) {
              item.discovertime = parseTime(item.discovertime, '{y}-{m}-{d}')
            }
          })
          this.total = total

          // 翻页后恢复选中状态
          this.$nextTick(() => {
            this.restoreSelection()
          })
        } catch (error) {
          console.error('Error fetching data:', error)
        } finally {
          this.listLoading = false
        }
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        const { data } = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('detail', data.bug)
      },
      async handleExport(row) {
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const data = await defectFileExport({
          ...other,
          startDate,
          endDate,
          orgId: this.orgId,
        })
        let fileName = '评价缺陷.xlsx'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
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
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },

      /**
       * @description: 翻页的时候回显已勾选的数据
       * @return {*}
       */
      restoreSelection() {
        this.$nextTick(() => {
          this.allSelectedData.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.bugnumber == item.bugnumber
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
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    /* width: 200px; */
    width: 15%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 85%;
  }
</style>
