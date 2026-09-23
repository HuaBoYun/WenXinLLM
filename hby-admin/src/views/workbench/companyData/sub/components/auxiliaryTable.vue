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
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      @select-all="handleSelectAll"
      @select="handleSelection"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="科目名称" prop="accName">
        <!-- <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">
            {{ row.accName }}
          </el-button>
        </template> -->
      </el-table-column>
      <el-table-column align="center" label="辅助名称" prop="assName" />
      <el-table-column align="center" label="期间" prop="amonth" />
      <el-table-column align="center" label="摘要">
        <el-row>期初余额</el-row>
        <el-row>本期合计</el-row>
        <el-row>本年累计</el-row>
      </el-table-column>
      <el-table-column align="right" label="借方" prop="ljmd">
        <template #default="{ row }">
          <el-row>
            {{ String(row.qcmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
          <el-row>
            {{ String(row.bqmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
          <el-row>
            {{ String(row.ljmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
        </template>
      </el-table-column>
      <el-table-column align="right" label="贷方" prop="ljmc">
        <template #default="{ row }">
          <el-row>
            {{ String(row.qcmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
          <el-row>
            {{ String(row.bqmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
          <el-row>
            {{ String(row.ljmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </el-row>
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template #default="{ row }">
          <el-row>{{ row.qmdc === 'D' ? '借' : '贷' }}</el-row>
          <el-row>{{ row.qmdc === 'D' ? '借' : '贷' }}</el-row>
          <el-row>{{ row.qmdc === 'D' ? '借' : '贷' }}</el-row>
        </template>
      </el-table-column>
      <el-table-column align="right" label="余额" prop="assSjbm">
        <template #default="{ row }">
          <el-row>
            {{
              row.qmdc === 'D'
                ? String(row.qcmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
                : String(row.qcmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
          <el-row>
            {{
              row.qmdc === 'D'
                ? String(row.qmmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
                : String(row.qmmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
          <el-row>
            {{
              row.qmdc === 'D'
                ? String(row.qmmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
                : String(row.qmmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <AuxiliaryGeneralLedgerDetails ref="edit" />
    <FlawInfo ref="flaw" @fetch-data="fetchData" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData" />
    <RiskInfo ref="risk" @fetch-data="fetchData" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import AuxiliaryGeneralLedgerDetails from './AuxiliaryGeneralLedgerDetails.vue'
  import {
    getAccountAssistVoucher,
    getAccountFileList,
  } from '@/api/workbench/accountData/accountData'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from '../fly/toDiGaoFile.vue'
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
        },
        select: [],
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
        this.fetchData() //监听props变化查询表
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
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
        }
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const params = JSON.parse(JSON.stringify(this.queryForm))
        params.bookYear = this.bookInfo.bookYear
        params.typeName = this.leftItem
        const type = params.type
        if (type) {
          switch (type) {
            case 'AMONTH':
              params.minMonth = params.accName[0].split('-')[1]
              params.maxMonth = params.accName[1].split('-')[1]
              delete params.status
              delete params.accName
              break
            case '':
              break
            default:
              break
          }
        }
        const {
          data: { list, total },
        } = await getAccountAssistVoucher(params)
        this.list = list
        this.total = total
        this.listLoading = false

        this.setCheckedRows()
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
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const ids = val.map(
          (item) => `${item.assId}_${item.acid}+${item.amonth}`
        )
        ids.map((elem) => {
          if (!this.select.includes(elem)) {
            this.select.push(elem)
          }
        })
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

      async getFile(type) {
        const id = this.select.map((res) => res.split('+')[0])
        const ids = [...new Set(id)]
        const month = ids
          .map((item) => {
            return this.select
              .filter((elem) => elem.split('+')[0] === item)
              .map((elem) => elem.split('+')[1])
              .join(',')
          })
          .join('~')
        const newIds = ids.join('~').replaceAll('_', ',')

        this.listLoading = true
        const info = await getAccountFileList({
          asacIdStrs: newIds,
          amonths: month,
          exprotType: 9,
        })
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
    },
  }
</script>
