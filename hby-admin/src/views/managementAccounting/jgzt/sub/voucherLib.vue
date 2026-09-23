<template>
  <div class="system-log-container">
    <el-page-header content="凭证库" @back="goBack" />
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
              <el-option label="凭证号" value="PZH" />
              <el-option label="摘要" value="LINETEXT" />
              <el-option label="科目编码" value="ACCID" />
              <el-option label="科目名称" value="ACCNAME1" />
              <el-option label="凭证日期" value="PZ_DATE" />
              <el-option label="借方金额" value="MD" />
              <el-option label="贷方金额" value="MC" />
              <el-option label="月份" value="AMONTH" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="queryForm.status"
              placeholder="条件"
              v-if="
                queryForm.type == 'PZH' ||
                queryForm.type == 'LINETEXT' ||
                queryForm.type == 'ACCNAME1' ||
                queryForm.type == 'ACCID'
              "
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
            <!-- <el-input
              v-model="queryForm.accName"
              v-if="queryForm.type == 'ACCID'"
              placeholder="请选择科目"
            /> -->
            <el-select
              v-model="queryForm.status"
              placeholder="条件"
              v-if="
                queryForm.type == 'PZ_DATE' ||
                queryForm.type == 'MD' ||
                queryForm.type == 'MC'
              "
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="大于" value="大于" />
              <el-option label="小于" value="小于" />
              <el-option label="大于等于" value="大于等于" />
              <el-option label="小于等于" value="小于等于" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.accName"
              clearable
              placeholder="关键字"
              v-if="
                queryForm.type == 'PZH' ||
                queryForm.type == 'LINETEXT' ||
                queryForm.type == 'ACCNAME1' ||
                queryForm.type == 'PZ_DATE' ||
                queryForm.type == 'MD' ||
                queryForm.type == 'MC'
              "
            />
            <el-input
              v-model="queryForm.accId"
              clearable
              placeholder="关键字"
              v-if="queryForm.type == 'ACCID'"
            />
            <!-- <el-date-picker
              v-model="queryForm.accName"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              :picker-options="pickerOptions"
              v-if="queryForm.type == 'AMONTH'"
            ></el-date-picker> -->
          </el-form-item>
          <el-form-item>
            <el-select
              v-if="queryForm.type == 'AMONTH'"
              v-model="queryForm.minMonth"
              clearable
              placeholder="月份"
              @change="setMinMonth"
            >
              <el-option
                v-for="item in month1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select
              v-if="queryForm.type == 'AMONTH'"
              v-model="queryForm.maxMonth"
              placeholder="月份"
              clearable
              @change="setMaxMonth"
            >
              <el-option
                v-for="item in month2"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
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
        <el-button
          type="primary"
          @click="showChangeAccountModal"
          style="margin: 0 0 19px auto !important"
        >
          切换账套
        </el-button>
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
      ref="multipleTable"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="科目编码" prop="accid">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">
            {{ row.accid }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="科目名称" prop="accName" />
      <el-table-column
        align="center"
        label="凭证日期"
        prop="pzDate"
        :formatter="formatDate"
      />
      <el-table-column align="center" label="凭证号" prop="pzh" />
      <el-table-column align="center" label="摘要" prop="lineText" />
      <el-table-column align="right" label="借方资金" prop="md">
        <template #default="{ row }">
          {{ String(row.md).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="贷方资金" prop="mc">
        <template #default="{ row }">
          {{ String(row.mc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="凭证类型" prop="pzType" />
      <el-table-column align="center" label="附件数" prop="fj" />
      <el-table-column align="center" label="月份" prop="amonth" />
      <el-table-column align="center" label="财务主管" prop="cwzh" />
      <el-table-column align="center" label="记账人" prop="jzr" />
      <el-table-column align="center" label="出纳人" prop="cnr" />
      <el-table-column align="center" label="审核人" prop="shr" />
      <el-table-column align="center" label="制单人" prop="zdr" />
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
    <bookkeeping-voucher ref="edit" />
    <FlawInfo ref="flaw" @fetch-data="fetchData" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData" />
    <RiskInfo ref="risk" @fetch-data="fetchData" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData" />
    <ChangeAccountModal
      ref="changeAccountModal"
      @fetch-data="fetchData"
    ></ChangeAccountModal>
  </div>
</template>

<script>
  import {
    getVoucherLibList,
    getAccountFileList,
  } from '@/api/workbench/accountData/accountData'
  import { doDelete } from '@/api/table'
  import getUserSelectedBookInfo from './../utils/getBookInfo'
  import BookkeepingVoucher from './components/BookkeepingVoucher.vue'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from './fly/toDiGaoFile.vue'
  import { formatDay } from '@/utils'
  import ChangeAccountModal from './components/changeAccountModal.vue'

  export default {
    name: 'Consult',
    components: {
      BookkeepingVoucher,
      FlawInfo,
      DoubtfulInfo,
      RiskInfo,
      MyDraftInfo,
      DiGaoFile,
      ChangeAccountModal,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        bookInfo: null,
        queryForm: {
          type: undefined,
          status: undefined,
          accName: undefined,
          accId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        month1: [
          { label: '1月', value: '1' },
          { label: '2月', value: '2' },
          { label: '3月', value: '3' },
          { label: '4月', value: '4' },
          { label: '5月', value: '5' },
          { label: '6月', value: '6' },
          { label: '7月', value: '7' },
          { label: '8月', value: '8' },
          { label: '9月', value: '9' },
          { label: '10月', value: '10' },
          { label: '11月', value: '11' },
          { label: '12月', value: '12' },
        ],
        month2: [
          { label: '1月', value: '1' },
          { label: '2月', value: '2' },
          { label: '3月', value: '3' },
          { label: '4月', value: '4' },
          { label: '5月', value: '5' },
          { label: '6月', value: '6' },
          { label: '7月', value: '7' },
          { label: '8月', value: '8' },
          { label: '9月', value: '9' },
          { label: '10月', value: '10' },
          { label: '11月', value: '11' },
          { label: '12月', value: '12' },
        ],
        pickerOptions: {
          disabledDate(time) {
            const currentYear = new Date().getFullYear()
            const selectedYear = time.getFullYear()

            return selectedYear !== currentYear
          },
        },
        select: [],
      }
    },
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
      if (this.$route.params.queryData) {
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.queryForm.type = 'PZH'
        this.queryForm.status = '等于'
        this.queryForm.accName = this.$route.params.queryData.pzh
        this.queryForm.wnss = this.$route.params.queryData.ayear
      }
      this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      setMinMonth(e) {
        console.log(e)
        let commom = [
          { label: '1月', value: '1' },
          { label: '2月', value: '2' },
          { label: '3月', value: '3' },
          { label: '4月', value: '4' },
          { label: '5月', value: '5' },
          { label: '6月', value: '6' },
          { label: '7月', value: '7' },
          { label: '8月', value: '8' },
          { label: '9月', value: '9' },
          { label: '10月', value: '10' },
          { label: '11月', value: '11' },
          { label: '12月', value: '12' },
        ]
        this.month2 = commom.splice(e - 1)
        this.queryForm.maxMonth = ''
        this.$forceUpdate()
      },
      setMaxMonth(e) {
        this.$forceUpdate()
        console.log(e)
      },
      showChangeAccountModal() {
        this.$refs['changeAccountModal'].showEdit()
      },
      goBack() {
        this.$router.back(-1)
      },
      handleTypeChange() {
        this.queryForm.status = undefined
        this.queryForm.accName = undefined
        this.queryForm.minMonth = ''
        this.queryForm.maxMonth = ''
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
        const params = JSON.parse(JSON.stringify(this.queryForm))
        if (
          !this.queryForm.minMonth &&
          !this.queryForm.maxMonth &&
          params.type == 'AMONTH'
        ) {
          this.$baseMessage('请选择月份区间', 'error', 'vab-hey-message-error')

          return
        }
        this.listLoading = true
        params.bookYear = this.bookInfo.bookYear
        const type = params.type
        if (type) {
          switch (type) {
            case 'AMONTH':
              // params.minMonth = formatDay(params.accName[0]).split('-')[1]
              // params.maxMonth = formatDay(params.accName[1]).split('-')[1]
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
        } = await getVoucherLibList(params)
        this.list = list
        this.total = total
        this.listLoading = false

        this.setCheckedRows()
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        // this.$router.push('/workbench/companyData/accountData/voucherLib')
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      reset() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          type: undefined,
          status: undefined,
          accName: undefined,
        }
        this.fetchData()
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleSelection(val, row) {
        const id = `${row.glh}_${row.entryId}`
        const i = this.select.findIndex((x) => x === id)
        if (i === -1) {
          this.select.push(id)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const ids = val.map((item) => `${item.glh}_${item.entryId}`)
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
              return this.select.includes(`${item.glh}_${item.entryId}`)
            })
            .forEach((item) => {
              this.$refs.multipleTable.toggleRowSelection(item, undefined)
            })
        })
      },
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
        const id = this.select.map((res) => res.split('_')[0])
        const ids = [...new Set(id)]
        const month = ids
          .map((item) => {
            return this.select
              .filter((elem) => elem.split('_')[0] === item)
              .map((elem) => elem.split('_')[1])
              .join(',')
          })
          .join('~')

        this.listLoading = true
        const info = await getAccountFileList({
          pzh: ids.toString(),
          entryId: month,
          exprotType: 3,
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

<style scoped>
  .system-log-container >>> .el-range-separator {
    width: 10% !important;
  }
</style>
