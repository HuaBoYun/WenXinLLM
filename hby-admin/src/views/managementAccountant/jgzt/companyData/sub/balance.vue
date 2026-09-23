<template>
  <div class="system-log-container">
    <el-page-header content="余额表" @back="goBack" />
    <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <!-- <div>单位：长江集团有限公司</div> -->
      <!-- <div>
        货币单位：元
        <span style="margin-left: 20px">会计期间：2018年6月至2018年7月</span>
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
              @change="clearQueryForm()"
            >
              <el-option label="科目编码" value="ACCID" />
              <el-option label="科目名称" value="ACCNAME1" />
              <el-option label="期初余款-借方" value="QCMD" />
              <el-option label="期初余款-贷方" value="QCMC" />
              <el-option label="本期发生-借方" value="BQMD" />
              <el-option label="本期发生-贷方" value="BQMC" />
              <el-option label="期末余款-借方" value="QMMD" />
              <el-option label="期末余款-贷方" value="QMMC" />
              <el-option label="月份" value="AMONTH" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
              v-if="queryForm.type == 'ACCNAME1'"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
              v-if="
                queryForm.type == 'ACCID' ||
                queryForm.type == 'QCMC' ||
                queryForm.type == 'QCMD' ||
                queryForm.type == 'BQMC' ||
                queryForm.type == 'BQMD' ||
                queryForm.type == 'QMMC' ||
                queryForm.type == 'QMMD'
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
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
              v-if="
                queryForm.type == 'ACCID' ||
                queryForm.type == 'ACCNAME1' ||
                queryForm.type == 'QCMC' ||
                queryForm.type == 'QCMD' ||
                queryForm.type == 'BQMC' ||
                queryForm.type == 'BQMD' ||
                queryForm.type == 'QMMC' ||
                queryForm.type == 'QMMD'
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
            <el-date-picker
              v-model="queryForm.year"
              type="year"
              placeholder="年份"
              value-format="yyyy"
              clearable
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.period"
              placeholder="会计期间"
              style="width: 200px"
              clearable
            ></el-input>
          </el-form-item>

          <!-- <el-form-item>
            <el-input
              v-model="queryForm.pkAccasoa"
              placeholder="科目主键"
              style="width: 200px"

              clearable
            ></el-input>
          </el-form-item> -->
          <!-- 期初余额范围 -->
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.minfbeginBalanceLocal"
                placeholder="期初余额最小值"
                style="width: 200px"
                clearable
                @input="validateNumber('minfbeginBalanceLocal')"
              />
              <span style="margin: 0 5px">至</span>
              <el-input
                v-model="queryForm.maxfbeginBalanceLocal"
                placeholder="期初余额最大值"
                style="width: 200px"
                clearable
                @input="validateNumber('maxfbeginBalanceLocal')"
              />
            </div>
          </el-form-item>

          <!-- 期末余额范围 -->
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.minfendBalanceLocal"
                placeholder="期末余额最小值"
                style="width: 200px"
                clearable
                @input="validateNumber('minfendBalanceLocal')"
              />
              <span style="margin: 0 5px">至</span>
              <el-input
                v-model="queryForm.maxfendBalanceLocal"
                placeholder="期末余额最大值"
                style="width: 200px"
                clearable
                @input="validateNumber('maxfendBalanceLocal')"
              />
            </div>
          </el-form-item>

          <!-- 本币贷发生额范围 -->
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.minlocalcreditamount"
                placeholder="本期贷发生额最小值"
                style="width: 200px"
                clearable
                @input="validateNumber('minlocalcreditamount')"
              />
              <span style="margin: 0 5px">至</span>
              <el-input
                v-model="queryForm.maxlocalcreditamount"
                placeholder="本期贷发生额最大值"
                style="width: 200px"
                clearable
                @input="validateNumber('maxlocalcreditamount')"
              />
            </div>
          </el-form-item>

          <!-- 本币借发生额范围 -->
          <el-form-item>
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.minlocaldebitamount"
                placeholder="本期借发生额最小值"
                style="width: 200px"
                clearable
                @input="validateNumber('minlocaldebitamount')"
              />
              <span style="margin: 0 5px">至</span>
              <el-input
                v-model="queryForm.maxlocaldebitamount"
                placeholder="本期借发生额最大值"
                style="width: 200px"
                clearable
                @input="validateNumber('maxlocaldebitamount')"
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
      row-key="pkBalance"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="科目编码" prop="code" />
      <el-table-column align="center" label="科目名称" prop="name" />
      <el-table-column align="center" label="期间" prop="period" />
      <el-table-column align="center" label="期初余额" prop="data">
        <el-table-column align="center" label="方向" prop="qcdc">
          <template #default="{ row }">
            {{ row.beginBalanorient == '0' ? '借' : '贷' }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="金额" prop="data">
          <template #default="{ row }">
            {{
              String(row.fbeginBalanceLocal).replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="本期发生" prop="data">
        <el-table-column align="right" label="借方">
          <template #default="{ row }">
            {{
              String(row.localdebitamount || '').replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="贷方">
          <template #default="{ row }">
            {{
              String(row.localcreditamount || '').replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="本年累计" prop="data">
        <el-table-column align="right" label="借方">
          <template #default="{ row }">
            {{
              String(row.fyearDeditLocal || '').replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="贷方">
          <template #default="{ row }">
            {{
              String(row.fyearCreditLocal || '').replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="期末余额" prop="data">
        <el-table-column align="center" label="方向" prop="qmdc">
          <template #default="{ row }">
            {{ row.endBalanorient == '0' ? '借' : '贷' }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="金额" prop="data">
          <template #default="{ row }">
            {{
              String(row.fendBalanceLocal || '').replace(
                /\d(?=(?:\d{3})+\b)/g,
                '$&,'
              )
            }}
          </template>
        </el-table-column>
      </el-table-column>
      <!-- <el-table-column align="center" label="操作" show-overflow-tooltip width="120">
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
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
  </div>
</template>

<script>
  import {
    getBalanceList,
    getFinancialFileList,
    exportFinancialFile,
  } from '@/api/workbench/accountData/accountData'
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import getUserSelectedBookInfo from './../utils/getBookInfo'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from './fly/toDiGaoFile.vue'
  import { formatDay } from '@/utils'
  import TreeModal from '../treeModal.vue'
  import { getYebList } from '@/api/cwsc'
  import accTypeModel from './components/accTypeModel.vue'

  export default {
    name: 'Consult',
    components: {
      LcdyEdit,
      FlawInfo,
      DoubtfulInfo,
      RiskInfo,
      MyDraftInfo,
      DiGaoFile,
      TreeModal,
      accTypeModel,
    },
    data() {
      return {
        list: [],
        run: undefined,
        bookInfo: undefined,
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          year: '',
          maxfbeginBalanceLocal: '',
          minfbeginBalanceLocal: '',
          maxfendBalanceLocal: '',
          minfendBalanceLocal: '',
          maxlocalcreditamount: '',
          minlocalcreditamount: '',
          maxlocaldebitamount: '',
          minlocaldebitamount: '',
          period: '',
          pkAccasoa: '',
          kname: '',
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
        select: [],
        pickerOptions: {
          disabledDate(time) {
            const currentYear = new Date().getFullYear()
            const selectedYear = time.getFullYear()

            return selectedYear !== currentYear
          },
        },
      }
    },
    created() {
      // this.fetchData()
    },
    async mounted() {
      // if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      // const bookInfo = localStorage.getItem('bookInfo')
      // this.bookInfo = JSON.parse(bookInfo)
      if (this.$route.params.queryData) {
        const params = JSON.parse(JSON.stringify(this.queryForm))

        params.accId = this.$route.params.queryData.accid
        params.bqmc = this.$route.params.queryData.bqmc
        params.bqmd = this.$route.params.queryData.bqmd
        params.qcmd = this.$route.params.queryData.qcmd
        // params.accAllName = this.$route.params.queryData.accname1
        // params.wnss = this.$route.params.queryData.ayear
        params.status = '等于'
        this.run = params
      } else {
      }
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
      clearQueryForm() {
        this.queryForm.status = ''
        this.queryForm.keyword = ''
        this.queryForm.minMonth = ''
        this.queryForm.maxMonth = ''
        this.$forceUpdate()
      },
      goBack() {
        this.$router.back(-1)
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
      async fetchData(clearSelection) {
        // 发送操作完成后，清空勾选状态
        if (clearSelection) {
          this.select = []
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        }
        this.list = []

        // 验证最大值不小于最小值
        if (
          this.queryForm.maxfbeginBalanceLocal &&
          this.queryForm.minfbeginBalanceLocal &&
          Number(this.queryForm.maxfbeginBalanceLocal) <
            Number(this.queryForm.minfbeginBalanceLocal)
        ) {
          this.$message.warning('期初余额最大值不能小于最小值')
          return
        }

        if (
          this.queryForm.maxfendBalanceLocal &&
          this.queryForm.minfendBalanceLocal &&
          Number(this.queryForm.maxfendBalanceLocal) <
            Number(this.queryForm.minfendBalanceLocal)
        ) {
          this.$message.warning('期末余额最大值不能小于最小值')
          return
        }

        if (
          this.queryForm.maxlocalcreditamount &&
          this.queryForm.minlocalcreditamount &&
          Number(this.queryForm.maxlocalcreditamount) <
            Number(this.queryForm.minlocalcreditamount)
        ) {
          this.$message.warning('本币贷发生额最大值不能小于最小值')
          return
        }

        if (
          this.queryForm.maxlocaldebitamount &&
          this.queryForm.minlocaldebitamount &&
          Number(this.queryForm.maxlocaldebitamount) <
            Number(this.queryForm.minlocaldebitamount)
        ) {
          this.$message.warning('本币借发生额最大值不能小于最小值')
          return
        }

        let params = JSON.parse(JSON.stringify(this.queryForm))
        this.listLoading = true
        // if (
        //   !this.queryForm.minMonth &&
        //   !this.queryForm.maxMonth &&
        //   params.type == 'AMONTH'
        // ) {
        //   this.$baseMessage('请选择月份区间', 'error', 'vab-hey-message-error')

        //   return
        // }
        // // params.bookYear = this.bookInfo.bookYear
        // const type = params.type

        // if (type) {
        //   switch (type) {
        //     case 'ACCID':
        //       params.accId = params.keyword
        //       break
        //     case 'ACCNAME1':
        //       params.accName = params.keyword
        //       break
        //     case 'QCMC':
        //       params.qcmc = params.keyword
        //       break
        //     case 'QCMD':
        //       params.qcmd = params.keyword
        //       break
        //     case 'BQMC':
        //       params.bqmc = params.keyword
        //       break
        //     case 'BQMD':
        //       params.bqmd = params.keyword
        //       break
        //     case 'QMMC':
        //       params.qmmc = params.keyword
        //       break
        //     case 'QMMD':
        //       params.qmmd = params.keyword
        //       break
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
        // if (this.run) {
        //   params = this.run
        // }
        const { data } = await getYebList(params)
        this.list = data.records
        this.total = data.total
        this.listLoading = false
        // 仅在非清空模式下恢复勾选状态
        if (!clearSelection) {
          this.setCheckedRows()
        }
      },
      reset() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          year: '',
          maxfbeginBalanceLocal: '',
          minfbeginBalanceLocal: '',
          maxfendBalanceLocal: '',
          minfendBalanceLocal: '',
          maxlocalcreditamount: '',
          minlocalcreditamount: '',
          maxlocaldebitamount: '',
          minlocaldebitamount: '',
          period: '',
          pkAccasoa: '',
          kname: '',
        }
        this.select = []
        this.run = this.$options.data().run
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
      handleSelection(val, row) {
        console.log(row, '1111')
        const i = this.select.findIndex((x) => x.pkBalance == row.pkBalance)
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
            if (row && !this.select.some((x) => x.pkBalance == row.pkBalance)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.pkBalance == row.pkBalance)
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
                return row.pkBalance == item.pkBalance
              }),
              true
            )
          })
        })
      },
      toType(type) {
        console.log(this.select, '1111')
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
        // pkBalances: 余额表操作 余额主键数组，需要encode转译
        const pkBalances = this.select.map((res) =>
          encodeURIComponent(res.pkBalance)
        )
        this.listLoading = true
        const params = {
          exprotType: 5, // 5-科目余额表
          pkBalances: pkBalances,
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
          link.download = `余额表_${new Date().getTime()}.xlsx`
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
        // pkBalances: 余额表操作 余额主键数组，需要encode转译
        const pkBalances = this.select.map((res) =>
          encodeURIComponent(res.pkBalance)
        )
        this.listLoading = true
        const params = {
          exprotType: 5, // 5-科目余额表
          pkBalances: pkBalances,
        }
        // 如果选了组织，传pkOrg
        if (this.queryForm.pkOrg) {
          params.pkOrg = this.queryForm.pkOrg
        }
        const info = await getFinancialFileList(params)
        info.data.attname = info.data.attname + '.xlsx'
        console.log('🚀 ~ info:', info)
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
      handleSelected(data) {
        this.queryForm.pkAccasoa = data.pkAccount
          ? encodeURIComponent(data.pkAccount)
          : ''
        this.queryForm.kname = data.name
        this.fetchData()
      },
    },
  }
</script>
