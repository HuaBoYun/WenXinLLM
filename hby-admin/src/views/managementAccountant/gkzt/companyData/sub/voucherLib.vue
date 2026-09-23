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

          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.kname"
                placeholder="科目表"
                disabled
                style="width: 200px; margin-right: 10px"
              />
              <el-button type="primary" @click="openAcc">选择</el-button>
            </div>
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.num" placeholder="凭证号" />
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryForm.year"
              type="year"
              placeholder="年份"
              value-format="yyyy"
              clearable
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-select
                v-model="queryForm.minperiod"
                placeholder="会计期间开始"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="item in month1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <span style="margin: 0 5px">至</span>
              <el-select
                v-model="queryForm.maxperiod"
                placeholder="会计期间结束"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="item in month1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryForm.date"
              type="daterange"
              range-separator="至"
              start-placeholder="制单开始日期"
              end-placeholder="制单结束日期"
              value-format="yyyy-MM-dd"
              clearable
            ></el-date-picker>
          </el-form-item>
          <!-- <el-form-item>
            <el-date-picker
              v-model="queryForm.date2"
              type="daterange"
              range-separator="至"
              start-placeholder="会计开始日期"
              end-placeholder="会计结束日期"
              value-format="yyyy-MM-dd"

              clearable
            ></el-date-picker>
          </el-form-item> -->
          <el-form-item>
            <div style="display: flex">
              <el-input
                v-model="queryForm.minTotaldebit"
                placeholder="总借方金额最小值"
                clearable
                @input="validateNumber('minTotaldebit')"
              />
              <span style="margin: 0 10px">至</span>
              <el-input
                v-model="queryForm.maxTotaldebit"
                placeholder="总借方金额最大值"
                clearable
                @input="validateNumber('maxTotaldebit')"
              />
            </div>
          </el-form-item>
          <el-form-item>
            <div style="display: flex">
              <el-input
                v-model="queryForm.minTotalcredit"
                placeholder="总贷方金额最小值"
                clearable
                @input="validateNumber('minTotalcredit')"
              />
              <span style="margin: 0 10px">至</span>
              <el-input
                v-model="queryForm.maxTotalcredit"
                placeholder="总贷方金额最大值"
                clearable
                @input="validateNumber('maxTotalcredit')"
              />
            </div>
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
        <!-- <el-button
          type="primary"
          @click="showChangeAccountModal"
          style="margin: 0 0 19px auto !important"
        >
          切换账套
        </el-button> -->
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
      <!-- <el-table-column align="center" label="科目编码" prop="accid">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">
            {{ row.accid }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="科目名称" prop="accName" /> -->
      <el-table-column
        align="center"
        label="凭证日期"
        prop="prepareddate"
        :formatter="formatDate"
      />
      <el-table-column align="center" label="凭证号" prop="num" />
      <el-table-column align="center" label="会计期间" prop="period" />
      <!-- <el-table-column align="right" label="借方资金" prop="md">
        <template #default="{ row }">
          {{ String(row.md).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="贷方资金" prop="mc">
        <template #default="{ row }">
          {{ String(row.mc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="摘要" prop="explanation">
        <template #default="{ row }">
          <el-button type="text" @click="$refs.detail.showEdit(row)">
            {{ row.explanation }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="凭证类别" prop="voucherTypeName" />
      <el-table-column align="center" label="年份" prop="year" />
      <el-table-column align="center" label="出纳人员" prop="cashername" />
      <el-table-column align="center" label="审核人员" prop="checkedname" />
      <el-table-column align="center" label="主管人员" prop="managername" />
      <el-table-column align="center" label="签字日期" prop="signdate" />
      <el-table-column align="center" label="总贷方金额" prop="totalcredit" />
      <el-table-column
        align="center"
        label="总贷方金额（本位币）"
        prop="totalcreditglobal"
      />
      <el-table-column
        align="center"
        label="总贷方金额（集团）"
        prop="totalcreditgroup"
      />
      <el-table-column align="center" label="总借方金额" prop="totaldebit" />
      <el-table-column
        align="center"
        label="总借方金额（本位币）"
        prop="totaldebitglobal"
      />
      <el-table-column
        align="center"
        label="总借方金额（集团）"
        prop="totaldebitgroup"
      />
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
    <FlawInfo ref="flaw" @fetch-data="fetchData(true)" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData(true)" />
    <RiskInfo ref="risk" @fetch-data="fetchData(true)" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData(true)" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData(true)" />
    <ChangeAccountModal
      ref="changeAccountModal"
      @fetch-data="fetchData"
    ></ChangeAccountModal>
    <accTypeModel
      ref="accType"
      @selected="handleSelected"
      :pkOrg="queryForm.pkOrg"
    />
    <TreeModal ref="selectSubject" @select="handleSelectSubject" />
    <ExplanationDetail ref="detail" :pkOrg="queryForm.pkOrg" />
  </div>
</template>

<script>
  import {
    getVoucherLibList,
    getFinancialFileList,
    exportFinancialFile,
  } from '@/api/workbench/accountData/accountData'
  import { getPzkList } from '@/api/cwsc'
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
  import accTypeModel from './components/accTypeModel.vue'

  import TreeModal from '../treeModal.vue'
  import ExplanationDetail from './components/ExplanationDetail.vue'
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
      TreeModal,
      accTypeModel,
      ExplanationDetail,
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
          pkOrg: '',
          pkName: '',
          num: '',
          year: '',
          kname: '',
          pkAccAsoa: '',
          minTotaldebit: '',
          maxTotaldebit: '',
          minTotalcredit: '',
          maxTotalcredit: '',
          date: '',
          date1: '',
          date2: '',
          minperiod: '',
          maxperiod: '',
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
      // if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      // const bookInfo = localStorage.getItem('bookInfo')
      // this.bookInfo = JSON.parse(bookInfo)
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
        // 验证会计期间结束不能小于会计期间开始
        if (
          this.queryForm.minperiod &&
          this.queryForm.maxperiod &&
          Number(this.queryForm.maxperiod) < Number(this.queryForm.minperiod)
        ) {
          this.$message.error('会计期间结束不能小于会计期间开始')
          return
        }
        this.queryForm.pageNumber = 1
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

        const params = JSON.parse(JSON.stringify(this.queryForm))
        // 处理日期参数
        if (this.queryForm.date?.length === 2) {
          const [startDate, endDate] = this.queryForm.date.map(formatDay)
          params.minprepareddate = startDate
          params.maxprepareddate = endDate
          delete params.date
        }
        // 处理日期参数1
        if (this.queryForm.date1?.length === 2) {
          const [startDate, endDate] = this.queryForm.date1.map(formatDay)
          params.mintallydate = startDate
          params.maxtallydate = endDate
          delete params.date1
        }
        // 处理日期参数2
        if (this.queryForm.date2?.length === 2) {
          const [startDate, endDate] = this.queryForm.date2.map(formatDay)
          params.minperiod = startDate
          params.maxperiod = endDate
          delete params.date2
        }
        // if (
        //   !this.queryForm.minMonth &&
        //   !this.queryForm.maxMonth &&
        //   params.type == 'AMONTH'
        // ) {
        //   this.$baseMessage('请选择月份区间', 'error', 'vab-hey-message-error')

        //   return
        // }
        this.listLoading = true
        // params.bookYear = this.bookInfo.bookYear
        // const type = params.type
        // if (type) {
        //   switch (type) {
        //     case 'AMONTH':
        //       // params.minMonth = formatDay(params.accName[0]).split('-')[1]
        //       // params.maxMonth = formatDay(params.accName[1]).split('-')[1]
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
        } = await getPzkList(params)
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
      // 金额输入校验，只允许数字、小数点和负号
      validateNumber(field) {
        let value = this.queryForm[field]
        if (value) {
          // 只保留数字、小数点和负号
          value = value.replace(/[^\d.-]/g, '')
          // 确保负号只在开头
          if (value.indexOf('-') > 0) {
            value = value.replace(/-/g, '')
          }
          // 确保只有一个小数点
          const parts = value.split('.')
          if (parts.length > 2) {
            value = parts[0] + '.' + parts.slice(1).join('')
          }
          this.queryForm[field] = value
        }
      },
      reset() {
        // this.queryForm = {
        //   type: undefined,
        //   status: undefined,
        //   accName: undefined,
        //   accId: undefined,
        //   pageNumber: 1,
        //   pageSize: 20,
        //   pkOrg: '',
        //   pkName: '',
        //   num: '',
        //   year: '',
        //   kname: '',
        //   pkAccAsoa: '',
        //   minTotaldebit: '',
        //   maxTotaldebit: '',
        //   minTotalcredit: '',
        //   maxTotalcredit: '',
        //   date: '',
        //   date1: '',
        //   date2: '',
        // }
        this.queryForm = this.$options.data().queryForm
        this.fetchData()
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.pkVoucher === row.pkVoucher)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        if (val && val.length) {
          val.forEach((row) => {
            if (
              row &&
              !this.select.some((x) => x.pkVoucher === row.pkVoucher)
            ) {
              this.select.push(row)
            }
          })
        } else {
          // 取消全选
          this.list.forEach((row) => {
            const i = this.select.findIndex(
              (x) => x.pkVoucher === row.pkVoucher
            )
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
                return row.pkVoucher === item.pkVoucher
              }),
              true
            )
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

      // 导出
      async handleExport() {
        if (this.select.length === 0) {
          this.$message({
            message: '请选择待导出数据',
            type: 'error',
          })
          return
        }
        // pkVouchers: 凭证表操作 凭证主键数组，需要encode转译
        const pkVouchers = this.select.map((res) =>
          encodeURIComponent(res.pkVoucher)
        )
        this.listLoading = true
        const params = {
          exprotType: 3, // 3-导出凭证库
          pkVouchers: pkVouchers,
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
          link.download = `凭证库_${new Date().getTime()}.xlsx`
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
        // pkVouchers: 凭证表操作 凭证主键数组，需要encode转译
        const pkVouchers = this.select.map((res) =>
          encodeURIComponent(res.pkVoucher)
        )
        this.listLoading = true
        const params = {
          exprotType: 3, // 3-凭证库
          pkVouchers: pkVouchers,
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
      selectSubject() {
        this.$refs['selectSubject'].showEdit()
      },
      handleSelectSubject(data) {
        this.queryForm.pkOrg = data.pkOrg
        this.queryForm.pkName = data.name
        // this.queryForm.num = data.code
        this.fetchData()
      },
      openAcc() {
        this.$refs.accType.showEdit()
      },
      handleSelected(data) {
        this.queryForm.pkAccAsoa = data.pkAccount
          ? encodeURIComponent(data.pkAccount)
          : ''
        this.queryForm.kname = data.name
      },
    },
  }
</script>

<style scoped>
  .system-log-container >>> .el-range-separator {
    width: 10% !important;
  }
</style>
