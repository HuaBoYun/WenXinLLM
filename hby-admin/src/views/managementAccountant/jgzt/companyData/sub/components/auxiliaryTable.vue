<template>
  <div>
    <!-- <el-page-header content="辅助总表" /> -->
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
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.pkName"
                placeholder="组织"
                disabled
                style="width: 200px; margin-right: 10px"
              />
              <el-button type="primary" @click="selectSubject">选择</el-button>
            </div>
          </el-form-item>
          <!-- <el-form-item>
            <el-select
              v-model="queryForm.type"
              placeholder="类型"
              @change="handleTypeChange"
            >
              <el-option label="辅助名称" value="ASSNAME" />
              <el-option label="科目名称" value="ACCNAME" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="条件">
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.assName"
              clearable
              placeholder="关键字"
            />
          </el-form-item> -->
          <el-form-item>
            <el-input
              v-model="queryForm.assname"
              clearable
              placeholder="辅助名称"
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryForm.year"
              type="year"
              placeholder="年份"
              value-format="yyyy"
              style="width: 200px"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.assdd"
              clearable
              placeholder="描述"
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.period"
              clearable
              placeholder="会计期间"
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="queryData"
            >
              查询
            </el-button>
            <el-button native-type="submit" type="default" @click="reset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-left-panel></vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <div style="display: flex; align-items: center">
          <el-button
            type="primary"
            @click="handleExport"
            :loading="listLoading"
          >
            导出
          </el-button>
          <el-dropdown style="margin-left: 10px" v-loading="listLoading">
            <el-button type="primary">操作</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="toType('toDiGao')">
                发送至底稿
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toDiGaoFile')">
                发送至底稿附件
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toYiDian')">
                发送至疑点
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toQueXian')">
                发送至缺陷
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toFengXian')">
                发送至风险
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      @select-all="handleSelectAll"
      @select="handleSelection"
      row-key="pkAssbalance"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="科目名称" prop="name">
        <!-- <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">
            {{ row.accName }}
          </el-button>
        </template> -->
      </el-table-column>
      <el-table-column align="center" label="辅助名称" prop="assname" />
      <el-table-column align="center" label="期间" prop="period" />
      <el-table-column align="center" label="摘要">
        <el-row>期初余额</el-row>
        <el-row>本期合计</el-row>
        <el-row>本年累计</el-row>
      </el-table-column>
      <el-table-column align="right" label="借方" prop="ljmd">
        <template #default="{ row }">
          <el-row>
            {{
              row.beginBalanorient == 0
                ? String(row.beginbalancemount).replace(
                    /\d(?=(?:\d{3})+\b)/g,
                    '$&,'
                  )
                : 0
            }}
          </el-row>
          <el-row>
            {{ String(row.debitamount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
          <el-row>
            {{
              String(row.yeardebitamount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
        </template>
      </el-table-column>
      <el-table-column align="right" label="贷方" prop="ljmc">
        <template #default="{ row }">
          <el-row>
            {{
              row.beginBalanorient == 1
                ? String(row.beginbalancemount).replace(
                    /\d(?=(?:\d{3})+\b)/g,
                    '$&,'
                  )
                : 0
            }}
          </el-row>
          <el-row>
            {{ String(row.creditmount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
          <el-row>
            {{
              String(row.yearcreditmount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template #default="{ row }">
          <el-row>{{ row.beginBalanorient == '0' ? '借' : '贷' }}</el-row>
          <el-row>{{ row.endBalanorient == '0' ? '借' : '贷' }}</el-row>
          <el-row>{{ row.endBalanorient == '0' ? '借' : '贷' }}</el-row>
        </template>
      </el-table-column>
      <el-table-column align="right" label="余额" prop="assSjbm">
        <template #default="{ row }">
          <el-row>
            {{
              String(row.beginbalancemount).replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </el-row>
          <el-row>
            {{
              String(row.endbalancemount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
          <el-row>
            {{
              String(row.endbalancemount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
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
    <AuxiliaryGeneralLedgerDetails ref="edit" />
    <FlawInfo ref="flaw" @fetch-data="fetchData(true)" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData(true)" />
    <RiskInfo ref="risk" @fetch-data="fetchData(true)" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData(true)" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData(true)" />
    <TreeModal ref="selectSubject" @select="handleSelectSubject" />
  </div>
</template>

<script>
  import AuxiliaryGeneralLedgerDetails from './AuxiliaryGeneralLedgerDetails.vue'
  import {
    getAccountAssistVoucher,
    getFinancialFileList,
    exportFinancialFile,
  } from '@/api/workbench/accountData/accountData'
  import { getAuxiliaryTotalTable } from '@/api/cwsc.js'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from '../fly/toDiGaoFile.vue'
  import TreeModal from '../../treeModal.vue'
  export default {
    name: 'User',
    props: ['leftItem'],
    components: {
      AuxiliaryGeneralLedgerDetails,
      FlawInfo,
      DoubtfulInfo,
      RiskInfo,
      MyDraftInfo,
      DiGaoFile,
      TreeModal,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        bookInfo: null,
        queryForm: {
          leftMenu: '3',
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          year: '',
          assname: '',
          assdd: '',
          period: '',
        },
        select: [],
        selectRows: [],
      }
    },
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
      this.fetchData()
    },
    watch: {
      leftItem: function () {
        // this.fetchData() //监听props变化查询表
      },
    },
    methods: {
      goBack() {
        this.$router.back(-1)
      },
      handleTypeChange() {
        this.queryForm.status = undefined
        this.queryForm.accName = undefined
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
      reset() {
        this.queryForm = {
          leftMenu: '3',
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          year: '',
          assname: '',
          assdd: '',
          assdes: '',
          period: '',
        }
        this.fetchData()
      },
      async fetchData(clearSelection) {
        // 发送操作完成后，清空勾选状态
        if (clearSelection) {
          this.select = []
          this.selectRows = []
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        }
        this.list = []

        this.listLoading = true
        const params = JSON.parse(JSON.stringify(this.queryForm))
        params.pkAccassitem = this.leftItem
          ? encodeURIComponent(this.leftItem)
          : ''
        // params.bookYear = this.bookInfo.bookYear
        // params.typeName = this.leftItem
        // const type = params.type
        // if (type) {
        //   switch (type) {
        //     case 'AMONTH':
        //       params.minMonth = params.accName[0].split('-')[1]
        //       params.maxMonth = params.accName[1].split('-')[1]
        //       delete params.status
        //       delete params.accName
        //       break
        //     case '':
        //       break
        //     default:
        //       break
        //   }
        // }
        const {
          data: { records, total },
        } = await getAuxiliaryTotalTable(params)
        this.list = records
        this.total = total
        this.listLoading = false

        // 仅在非清空模式下恢复勾选状态
        if (!clearSelection) {
          this.setCheckedRows()
        }
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
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      handleSelection(val, row) {
        const id = `${row.assId}_${row.acid}+${row.amonth}`
        const i = this.select.findIndex((x) => x === id)
        if (i === -1) {
          this.select.push(id)
          this.selectRows.push(row)
        } else {
          this.select.splice(i, 1)
          const rowIndex = this.selectRows.findIndex(
            (x) => x.pkAssbalance === row.pkAssbalance
          )
          if (rowIndex >= 0) {
            this.selectRows.splice(rowIndex, 1)
          }
        }
      },
      handleSelectAll(val) {
        if (val && val.length) {
          val.forEach((row) => {
            const id = `${row.assId}_${row.acid}+${row.amonth}`
            if (!this.select.includes(id)) {
              this.select.push(id)
              this.selectRows.push(row)
            }
          })
        } else {
          // 取消全选
          this.list.forEach((row) => {
            const id = `${row.assId}_${row.acid}+${row.amonth}`
            const i = this.select.indexOf(id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
            const rowIndex = this.selectRows.findIndex(
              (x) => x.pkAssbalance === row.pkAssbalance
            )
            if (rowIndex >= 0) {
              this.selectRows.splice(rowIndex, 1)
            }
          })
        }
      },
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.list
            .filter((item) => {
              return this.select.includes(
                `${item.assId}_${item.acid}+${item.amonth}`
              )
            })
            .forEach((item) => {
              this.$refs.multipleTable.toggleRowSelection(item, undefined)
            })
        })
      },
      //发送
      toType(type) {
        if (this.select.length === 0) {
          this.$message({
            message: '请选择待发送',
            type: 'error',
          })
          return
        }
        this.getFile(type)
      },
      // 导出
      async handleExport() {
        if (this.selectRows.length === 0) {
          this.$message({
            message: '请选择待导出数据',
            type: 'error',
          })
          return
        }
        // pkAssbalances: 辅助总账操作 辅助余额主键数组，需要encode转译
        const pkAssbalances = this.selectRows.map((res) =>
          encodeURIComponent(res.pkAssbalance)
        )
        this.listLoading = true
        const params = {
          exprotType: 9, // 9-辅助总账
          pkAssbalances: pkAssbalances,
          isExport: '1', // 开启导出下载功能
        }
        // 如果选了组织，传pkOrg
        if (this.queryForm.pkOrg) {
          params.pkOrg = this.queryForm.pkOrg
        }
        try {
          const res = await exportFinancialFile(params)
          // 处理blob下载
          const blob = new Blob([res], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `辅助总账_${new Date().getTime()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败')
        } finally {
          this.listLoading = false
        }
      },

      async getFile(type) {
        // pkAssbalances: 辅助总账操作 辅助余额主键数组，需要encode转译
        const pkAssbalances = this.selectRows.map((res) =>
          encodeURIComponent(res.pkAssbalance)
        )
        this.listLoading = true
        const params = {
          exprotType: 9, // 9-辅助总账
          pkAssbalances: pkAssbalances,
        }
        // 如果选了组织，传pkOrg
        if (this.queryForm.pkOrg) {
          params.pkOrg = this.queryForm.pkOrg
        }
        const info = await getFinancialFileList(params)
        this.listLoading = false
        if (info.data) {
          if (type == 'toQueXian') {
            this.$refs['flaw'].showEdit(
              'add',
              {},
              {},
              { attachment: info.data }
            )
          }
          if (type == 'toFengXian') {
            this.$refs['risk'].showEdit('add', null, { attachment: info.data })
          }
          if (type == 'toYiDian') {
            this.$refs['doubtful'].showEdit('add', null, {
              attachment: info.data,
            })
          }
          if (type == 'toDiGao') {
            this.$refs['manuscript'].showEdit(
              'add',
              {},
              { attachment: info.data }
            )
          }
          if (type == 'toDiGaoFile') {
            this.$refs['digaoFile'].showEdit(info.data)
          }
        }
      },
      handleSelectSubject(data) {
        this.queryForm.pkOrg = data.pkOrg
        this.queryForm.pkName = data.name
        // this.queryForm.num = data.code
        this.fetchData()
      },
      selectSubject() {
        this.$refs['selectSubject'].showEdit()
      },
    },
  }
</script>
