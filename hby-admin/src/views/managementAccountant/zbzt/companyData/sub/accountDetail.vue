<template>
  <div class="system-log-container">
    <el-page-header content="明细账" @back="goBack" />
    <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <!-- <div>单位：长江集团有限公司</div> -->
      <!-- <div>
        科目：[1001]库存现金
        <span style="margin-left: 20px">期间：2018年6月 - 2018年7月</span>
        <span style="margin-left: 20px">币别：人民币</span>
      </div> -->
    </div>
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
          <!-- <el-form-item>
            <el-select
              v-model="queryForm.type"
              placeholder="类型"
              @change="handleTypeChange"
            >
              <el-option label="日期" value="PZ_DATE" />
              <el-option label="摘要" value="LINETEXT" />
              <el-option label="借方金额" value="MD" />
              <el-option label="贷方金额" value="MC" />
              <el-option label="月份" value="AMONTH" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
              v-if="
                queryForm.type == 'LINETEXT' || queryForm.type == 'ACCNAME1'
              "
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
            <el-input v-model="input" v-if="queryForm.type == 2" />
            <el-select
              @change="$forceUpdate()"
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
              @input="$forceUpdate()"
              v-model="queryForm.accName"
              clearable
              placeholder="关键字"
              v-if="
                queryForm.type == 'PZ_DATE' ||
                queryForm.type == 'LINETEXT' ||
                queryForm.type == 'ACCNAME1' ||
                queryForm.type == 'MD' ||
                queryForm.type == 'MC'
              "
            />
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
          </el-form-item> -->
          <el-form-item>
            <el-input
              v-model="queryForm.explanation"
              placeholder="摘要内容"
              style="width: 200px"
              clearable
            ></el-input>
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="queryForm.gvnum"
              placeholder="凭证号"
              style="width: 200px"
              clearable
            ></el-input>
          </el-form-item>

          <!-- <el-form-item>
            <el-input v-model="queryForm.managervname" placeholder="记账人姓名"
              style="width: 200px" clearable></el-input>
          </el-form-item>

          <el-form-item>
            <el-input v-model="queryForm.nov" placeholder="凭证编码"  style="width: 200px"
              clearable></el-input>
          </el-form-item> -->
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-date-picker
                v-model="queryForm.minyearv"
                type="year"
                placeholder="会计年度开始年份"
                clearable
                value-format="yyyy"
                style="width: 200px"
              />
              <span style="margin: 0 5px">至</span>
              <el-date-picker
                v-model="queryForm.maxyearv"
                type="year"
                placeholder="会计年度结束年份"
                clearable
                value-format="yyyy"
                style="width: 200px"
              />
            </div>
          </el-form-item>
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-select
                v-model="queryForm.minperiodv"
                placeholder="开始月份"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="item in month1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  @change="setMinMonth"
                ></el-option>
              </el-select>
              <span style="margin: 0 5px">至</span>
              <el-select
                v-model="queryForm.maxperiodv"
                placeholder="结束月份"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="item in month2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
          </el-form-item>

          <el-form-item>
            <el-date-picker
              v-model="queryForm.prepareddateRange"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              clearable
            />
          </el-form-item>

          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.mindebitamount"
                placeholder="借方金额最小值"
                style="width: 200px"
                clearable
                @input="validateNumber('mindebitamount')"
              />
              <span style="margin: 0 5px">至</span>
              <el-input
                v-model="queryForm.maxdebitamount"
                placeholder="借方金额最大值"
                style="width: 200px"
                clearable
                @input="validateNumber('maxdebitamount')"
              />
            </div>
          </el-form-item>

          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.mincreditamount"
                placeholder="贷方金额最小值"
                style="width: 200px"
                clearable
                @input="validateNumber('mincreditamount')"
              />
              <span style="margin: 0 5px">至</span>
              <el-input
                v-model="queryForm.maxcreditamount"
                placeholder="贷方金额最大值"
                style="width: 200px"
                clearable
                @input="validateNumber('maxcreditamount')"
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
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-left-panel>
        <!-- <span
          style="color: red; cursor: pointer"
          @click="openAcc()"
          v-show="account"
        >
          {{ account && account.accid }}-{{ account && account.accname1 }}
          <i class="el-icon-caret-bottom"></i>
        </span> -->
      </vab-query-form-left-panel>
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
      row-key="pkDetail"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="日期" prop="prepareddatev">
        <template #default="{ row }">
          {{ row.prepareddatev.slice(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="凭证号" prop="gvnum" />
      <el-table-column align="center" label="摘要" prop="explanation">
        <template #default="{ row }">
          <el-button type="text" @click="$refs.detail.showEdit(row)">
            {{ row.explanation }}
          </el-button>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" label="摘要" prop="explanation">
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row)"
            v-if="row.lineText != '期初'"
          >
            {{ row.lineText }}
          </el-button>
          <span v-else>{{ row.lineText }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
        align="center"
        label="科目名称"
        prop="glBalanceVr.name"
      />
      <el-table-column
        align="center"
        label="科目编号"
        prop="glBalanceVr.code"
      />
      <el-table-column align="right" label="借方金额">
        <template #default="{ row }">
          {{ String(row.debitamount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="贷方金额">
        <template #default="{ row }">
          {{ String(row.creditamount).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="" prop="data" #default="{ row }">
        {{ row.glBalanceVr.endBalanorient == '0' ? '借' : '贷' }}
      </el-table-column>
      <el-table-column
        align="right"
        label="余额"
        prop="glBalanceVr.fendBalanceLocal"
      >
        <!-- {{
          row.qmdc === 'C'
            ? String(row.glBalanceVr).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            : String(row.glBalanceVr).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
        }} -->
      </el-table-column>
      <!-- <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-dropdown style="margin-left: 10px" @command="handleCommand(row)">
            <el-button type="text">操作</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>发送至底稿</el-dropdown-item>
              <el-dropdown-item>发送至底稿附件</el-dropdown-item>
              <el-dropdown-item>发送至疑点</el-dropdown-item>
              <el-dropdown-item>发送至缺陷</el-dropdown-item>
              <el-dropdown-item>发送至风险</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column> -->
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
    <BookkeepingVoucher ref="edit" />
    <FlawInfo ref="flaw" @fetch-data="fetchData(true)" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData(true)" />
    <RiskInfo ref="risk" @fetch-data="fetchData(true)" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData(true)" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData(true)" />
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
  import BookkeepingVoucher from './components/BookkeepingVoucher.vue'
  import accTypeModel from './components/accTypeModel.vue'
  import {
    getAccountDetailList,
    getFinancialFileList,
    exportFinancialFile,
  } from '@/api/workbench/accountData/accountData'
  import getUserSelectedBookInfo from './../utils/getBookInfo'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from './fly/toDiGaoFile.vue'
  import { formatDay } from '@/utils'
  import TreeModal from '../treeModal.vue'
  import { getAuxiliaryDetailTable } from '@/api/cwsc'
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
      accTypeModel,
      TreeModal,
      ExplanationDetail,
    },
    data() {
      return {
        account: {},
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          kname: '',
          pkAccasoa: '',
          year: '',
          dataoriginflag: '',
          explanation: '',
          fplanid: '',
          gvnum: '',
          managervname: '',
          maxcreditamount: '',
          maxdebitamount: '',
          maxperiodv: '',
          maxprepareddatev: '',
          maxyearv: '',
          mincreditamount: '',
          mindebitamount: '',
          minperiodv: '',
          minprepareddatev: '',
          minyearv: '',
          nov: '',
          pkAccountingbook: '',
          pkVoucher: '',
          prepareddateRange: [],
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
        selectRows: [],
      }
    },

    async created() {},
    async mounted() {
      if (this.$route.query?.pkOrg) {
        this.queryForm.pkOrg = this.$route.query.pkOrg || ''
        this.queryForm.pkName = this.$route.query.pkName || ''
        this.queryForm.pkAccasoa =
          encodeURIComponent(this.$route.query.pkAccasoa) || ''
        this.queryForm.kname = this.$route.query.name || ''
        this.queryForm.minyearv = this.$route.query.year || ''
        this.queryForm.maxyearv = this.$route.query.year || ''
        this.queryForm.minperiodv = this.$route.query.period || ''
        this.queryForm.maxperiodv = this.$route.query.period || ''
        this.fetchData()
      }
      // if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      // const bookInfo = localStorage.getItem('bookInfo')
      // this.bookInfo = JSON.parse(bookInfo)
      this.fetchData()
    },
    methods: {
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
      handleSelected(val) {
        console.log(val, 'val')
        // this.queryForm.type = "ACCID"
        this.queryForm.pkAccasoa = val.pkAccount
          ? encodeURIComponent(val.pkAccount)
          : ''
        this.queryForm.kname = val.name
        this.fetchData()
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
        if (
          this.queryForm.prepareddateRange &&
          this.queryForm.prepareddateRange.length == 2
        ) {
          this.queryForm.minprepareddatev = this.queryForm.prepareddateRange[0]
          this.queryForm.maxprepareddatev = this.queryForm.prepareddateRange[1]
        } else {
          this.queryForm.minprepareddatev = ''
          this.queryForm.maxprepareddatev = ''
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
        // if (!this.queryForm.pkOrg) {
        //   this.$message.warning('请选择组织')
        //   return
        // }
        const params = JSON.parse(JSON.stringify(this.queryForm))
        // 删除不需要传递给后端的参数
        delete params.prepareddateRange
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
        } = await getAuxiliaryDetailTable(params)
        // this.account = account
        // this.queryForm.accid = account && account.accid
        this.list = records
        this.total = total
        this.listLoading = false

        // 仅在非清空模式下恢复勾选状态
        if (!clearSelection) {
          this.setCheckedRows()
        }
      },
      reset() {
        this.queryForm = {
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          kname: '',
          pkAccasoa: '',
          year: '',
          dataoriginflag: '',
          explanation: '',
          fplanid: '',
          gvnum: '',
          managervname: '',
          maxcreditamount: '',
          maxdebitamount: '',
          maxperiodv: '',
          maxprepareddatev: '',
          maxyearv: '',
          mincreditamount: '',
          mindebitamount: '',
          minperiodv: '',
          minprepareddatev: '',
          minyearv: '',
          nov: '',
          pkAccountingbook: '',
          pkVoucher: '',
          prepareddateRange: [],
        }
        this.fetchData()
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
      tranlateData(data) {
        const arr = data.toFixed(2)
        const info = String(arr).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
        return info
      },
      handleSelection(val, row) {
        const id = row.pkDetail
        const i = this.select.findIndex((x) => x === id)
        if (i === -1) {
          this.select.push(id)
          this.selectRows.push(row)
        } else {
          this.select.splice(i, 1)
          const rowIndex = this.selectRows.findIndex(
            (x) => x.pkDetail === row.pkDetail
          )
          if (rowIndex >= 0) {
            this.selectRows.splice(rowIndex, 1)
          }
        }
      },
      handleSelectAll(val) {
        if (val && val.length) {
          val.forEach((row) => {
            const id = row.pkDetail
            if (!this.select.includes(id)) {
              this.select.push(id)
              this.selectRows.push(row)
            }
          })
        } else {
          // 取消全选
          this.list.forEach((row) => {
            const id = row.pkDetail
            const i = this.select.indexOf(id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
            const rowIndex = this.selectRows.findIndex(
              (x) => x.pkDetail === row.pkDetail
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
              return this.select.includes(item.pkDetail)
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
        // pkDetails: 明细账操作 明细主键数组，需要encode转译
        const pkDetails = this.selectRows.map((res) =>
          encodeURIComponent(res.pkDetail)
        )
        this.listLoading = true
        const params = {
          exprotType: 4, // 4-明细账
          pkDetails: pkDetails,
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
          link.download = `明细账_${new Date().getTime()}.xlsx`
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
        // pkDetails: 明细账操作 明细主键数组，需要encode转译
        const pkDetails = this.selectRows.map((res) =>
          encodeURIComponent(res.pkDetail)
        )
        this.listLoading = true
        const params = {
          exprotType: 4, // 4-明细账
          pkDetails: pkDetails,
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
      openAcc() {
        this.$refs.accType.showEdit()
      },
    },
  }
</script>

<style scoped>
  .system-log-container >>> .el-range-separator {
    width: 10% !important;
  }
</style>
