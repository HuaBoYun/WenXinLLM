<template>
  <div>
    <!-- <el-page-header content="辅助余额表" /> -->
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
              <el-option label="辅助编码" value="ASSID" />
              <el-option label="期初借方余额" value="QCMD" />
              <el-option label="期初贷方余额" value="QCMC" />
              <el-option label="借方发生额" value="BQMD" />
              <el-option label="贷方发生额" value="BQMC" />
              <el-option label="期末借方余额" value="QMMC" />
              <el-option label="期末贷方余额" value="QMMD" />
            </el-select>
          </el-form-item> -->
          <!-- <span
            v-if="
              queryForm.type == null ||
              queryForm.type == 'ASSNAME' ||
              queryForm.type == 'ASSID'
            "
          >
            <el-form-item>
              <el-select v-model="queryForm.status" placeholder="条件">
                <el-option label="等于" value="等于" />
                <el-option label="不等于" value="不等于" />
                <el-option label="包含" value="包含" />
                <el-option label="不包含" value="不包含" />
              </el-select>
            </el-form-item>
          </span>
          <span
            v-if="
              queryForm.type == 'QCMD' ||
              queryForm.type == 'QCMC' ||
              queryForm.type == 'BQMD' ||
              queryForm.type == 'BQMC' ||
              queryForm.type == 'QMMC' ||
              queryForm.type == 'QMMD'
            "
          >
            <el-form-item>
              <el-select v-model="queryForm.status" placeholder="条件">
                <el-option label="等于" value="等于" />
                <el-option label="不等于" value="不等于" />
                <el-option label="大于" value="大于" />
                <el-option label="小于" value="小于" />
                <el-option label="大于等于" value="大于等于" />
                <el-option label="小于等于" value="小于等于" />
              </el-select>
            </el-form-item>
          </span>
          <span v-if="queryForm.type == 'QCDC'">
            <el-form-item>
              <el-select v-model="queryForm.status" placeholder="条件">
                <el-option label="等于" value="等于" />
                <el-option label="不等于" value="不等于" />
              </el-select>
            </el-form-item>
          </span>
          <span v-if="queryForm.type != 'QCDC'">
            <el-form-item>
              <el-input
                v-model="queryForm.assName"
                clearable
                placeholder="关键字"
              />
            </el-form-item>
          </span>
          <span v-if="queryForm.type == 'QCDC'">
            <el-form-item>
              <el-select v-model="queryForm.assName" placeholder="条件">
                <el-option label="借" value="D" />
                <el-option label="贷" value="C" />
              </el-select>
            </el-form-item>
          </span> -->
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
      v-loading="listLoading"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
      :data="list"
      row-key="pkAssbalance"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="辅助编号" prop="pkAssbalance" />
      <el-table-column align="center" label="辅助名称" prop="assname" />
      <el-table-column align="center" label="原币借发生额" prop="debitamount" />
      <el-table-column align="center" label="原币贷发生额" prop="creditmount" />
      <el-table-column
        align="center"
        label="本年累计借发生额"
        prop="yeardebitamount"
      />
      <el-table-column
        align="center"
        label="本年累计贷发生额"
        prop="yearcreditmount"
      />
      <!-- <el-table-column align="center" label="期初余额">
        <el-table-column align="center" label="方向" prop="qcdc">
          <template #default="{ row }">
            {{ row.qcdc == 'D' ? '借' : '贷' }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="金额" prop="data">
          <template #default="{ row }">
            {{
              row.qcdc == 'D'
                ? String(row.qcmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
                : String(row.qcmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="本期发生">
        <el-table-column align="right" label="借方">
          <template #default="{ row }">
            {{ String(row.bqmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="贷方">
          <template #default="{ row }">
            {{ String(row.bqmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="期末余额">
        <el-table-column align="center" label="方向" prop="qmdc">
          <template #default="{ row }">
            {{ row.qmdc == 'D' ? '借' : '贷' }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="金额" prop="data">
          <template #default="{ row }">
            {{
              row.qmdc == 'D'
                ? String(row.qmmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
                : String(row.qmmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,')
            }}
          </template>
        </el-table-column>
      </el-table-column> -->
      <!-- <el-table-column
        align="center"
        label="备注"
        prop="data"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="状态" prop="data" /> -->
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
    <FlawInfo ref="flaw" @fetch-data="fetchData(true)" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData(true)" />
    <RiskInfo ref="risk" @fetch-data="fetchData(true)" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData(true)" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData(true)" />
    <TreeModal ref="selectSubject" @select="handleSelectSubject" />
  </div>
</template>

<script>
  import {
    getAccountAssistVoucher,
    getFinancialFileList,
    exportFinancialFile,
  } from '@/api/workbench/accountData/accountData'
  import { getAuxiliaryBalanceTable } from '@/api/cwsc'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from '../fly/toDiGaoFile.vue'
  import TreeModal from '../../treeModal.vue'
  export default {
    name: 'AuxiliaryBalanceTable',
    props: ['leftItem'],
    components: {
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
          leftMenu: '2',
          type: undefined,
          status: undefined,
          assname: undefined,
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          year: '',
          period: '',
          assdd: '',
          assdes: '',
        },
        select: [],
      }
    },
    async mounted() {
      // if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
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
        this.queryForm.assname = undefined
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
          leftMenu: '2',
          type: undefined,
          status: undefined,
          assname: undefined,
          pageNumber: 1,
          pageSize: 20,
          pkOrg: '',
          pkName: '',
          year: '',
          period: '',
          assdd: '',
          assdes: '',
        }
        this.fetchData()
      },
      async fetchData(clearSelection) {
        // 发送操作完成后，清空勾选状态
        if (clearSelection) {
          this.select = []
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        }
        this.list = []

        this.listLoading = true
        const params = JSON.parse(JSON.stringify(this.queryForm))
        // params.bookYear = this.bookInfo.bookYear
        params.pkAccassitem = this.leftItem
          ? encodeURIComponent(this.leftItem)
          : ''
        // const type = params.type
        // if (type) {
        //   switch (type) {
        //     case 'AMONTH':
        //       params.minMonth = params.assname[0].split('-')[1]
        //       params.maxMonth = params.assname[1].split('-')[1]
        //       delete params.status
        //       delete params.assname
        //       break
        //     case '':
        //       break
        //     default:
        //       break
        //   }
        // }
        const {
          data: { records, total },
        } = await getAuxiliaryBalanceTable(params)
        // const {
        //   data: { list, total },
        // } = await getAccountAssistVoucher(params)
        this.list = records
        this.total = total
        this.listLoading = false
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
        const i = this.select.findIndex(
          (x) => x.pkAssbalance === row.pkAssbalance
        )
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
              !this.select.some((x) => x.pkAssbalance === row.pkAssbalance)
            ) {
              this.select.push(row)
            }
          })
        } else {
          // 取消全选
          this.list.forEach((row) => {
            const i = this.select.findIndex(
              (x) => x.pkAssbalance === row.pkAssbalance
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
                return row.pkAssbalance === item.pkAssbalance
              }),
              true
            )
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
        if (this.select.length === 0) {
          this.$message({
            message: '请选择待导出数据',
            type: 'error',
          })
          return
        }
        // pkAssbalances: 辅助余额表操作 辅助余额主键数组，需要encode转译
        const pkAssbalances = this.select.map((res) =>
          encodeURIComponent(res.pkAssbalance)
        )
        this.listLoading = true
        const params = {
          exprotType: 8, // 8-辅助余额表
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
          link.download = `辅助余额表_${new Date().getTime()}.xlsx`
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
        // pkAssbalances: 辅助余额表操作 辅助余额主键数组，需要encode转译
        const pkAssbalances = this.select.map((res) =>
          encodeURIComponent(res.pkAssbalance)
        )
        this.listLoading = true
        const params = {
          exprotType: 8, // 8-辅助余额表
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
