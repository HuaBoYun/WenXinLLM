<template>
  <div class="system-log-container">
    <el-page-header content="资产负债表" @back="goBack" />
    <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <div>单位：{{ this.bookInfo.orgName }}</div>
      <div>
        货币单位：元
        <span style="margin-left: 20px">
          会计期间：{{ this.bookInfo.bookYear }}年
        </span>
      </div>
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
            <el-select v-model="queryForm.date" placeholder="月份">
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
              <el-option label="4" value="4" />
              <el-option label="5" value="5" />
              <el-option label="6" value="6" />
              <el-option label="7" value="7" />
              <el-option label="8" value="8" />
              <el-option label="9" value="9" />
              <el-option label="10" value="10" />
              <el-option label="11" value="11" />
              <el-option label="12" value="12" />
              <!-- <el-option label="月份" value="AMONTH" /> -->
            </el-select>
          </el-form-item>
          <!-- <el-form-item>
            <el-select
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
            <el-date-picker
              v-model="queryForm.accName"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              v-if="queryForm.type == 'AMONTH'"
            ></el-date-picker>
          </el-form-item> -->
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
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

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="资产" prop="zc" />
      <el-table-column align="center" label="行次" prop="lineno1" width="80%" />
      <el-table-column align="right" label="年初数">
        <template #default="{ row }">
          {{ String(row.qcs1).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="期末数" prop="qms1">
        <template #default="{ row }">
          {{ String(row.qms1).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="负债和所有者权益（股东权益）"
        prop="qy"
      >
        <!-- <el-table-column align="right" label="借方">
          <template #default="{ row }">
            {{ String(row.bqmd).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </template>
        </el-table-column>
        <el-table-column align="right" label="贷方">
          <template #default="{ row }">
            {{ String(row.bqmc).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
          </template>
        </el-table-column> -->
      </el-table-column>
      <el-table-column align="center" label="行次" prop="lineno1" width="80%" />
      <el-table-column align="center" label="年初数">
        <template #default="{ row }">
          {{ String(row.qcs2).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="期末数">
        <template #default="{ row }">
          {{ String(row.qms2).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
        <!-- <el-table-column align="center" label="方向" prop="qmdc">
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
        </el-table-column> -->
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
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import { getBalanceList } from '@/api/workbench/companyData/statement'
  import { doDelete } from '@/api/table'
  import getUserSelectedBookInfo from './../utils/getBookInfo'

  export default {
    name: 'Consult',
    components: {},
    data() {
      return {
        bookInfo: {
          bookYear: '',
          orgName: '',
        },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      // this.fetchData()
    },
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
      this.fetchData()
    },
    methods: {
      goBack() {
        this.$router.back(-1)
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
      async fetchData() {
        this.listLoading = true
        const params = JSON.parse(JSON.stringify(this.queryForm))
        params.bookYear = this.bookInfo.bookYear
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
          data: { list, total },
        } = await getBalanceList(params)
        // const {
        //   data: { list },
        // } = await getBalanceList(params)
        this.list = list[0].zcfzb_list
        this.total = total
        this.listLoading = false
      },
      reset() {
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
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
    },
  }
</script>
