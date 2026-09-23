<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-page-header content="辅助账" @back="goBack" />
        <!-- <el-button
          type="primary"
          @click="showChangeAccountModal"
          style="margin: 0 0 19px auto !important"
        >
          切换账套
        </el-button> -->
      </vab-query-form-top-panel>
    </vab-query-form>
    <div class="system-log-container lr-layout">
      <div class="left">
        <assist-list @select="selectTable" />
      </div>
      <div class="right">
        <auxiliary-info-table
          v-if="this.showTable == '辅助信息表'"
          :leftItem="leftItem"
        />
        <auxiliary-balance-table
          v-if="this.showTable == '辅助余额表'"
          :leftItem="leftItem"
        />
        <auxiliary-table
          v-if="this.showTable == '辅助总表'"
          :leftItem="leftItem"
        />
      </div>
      <user-edit ref="edit" @fetch-data="fetchData" />
      <user-info ref="userInfo" />
    </div>
    <ChangeAccountModal
      ref="changeAccountModal"
      @fetch-data="fetchData"
    ></ChangeAccountModal>
  </div>
</template>

<script>
  import AssistList from './components/assistTree.vue'
  import AuxiliaryInfoTable from './components/auxiliaryInfoTable.vue'
  import AuxiliaryBalanceTable from './components/auxiliaryBalanceTable.vue'
  import AuxiliaryTable from './components/auxiliaryTable.vue'
  import UserInfo from '@/views/setting/auth/components/UserInfo'
  import UserEdit from '@/views/setting/auth/components/UserEdit'
  import getUserSelectedBookInfo from './../utils/getBookInfo'
  import ChangeAccountModal from './components/changeAccountModal.vue'
  // import { getAccountAssistVoucher } from '@/api/workbench/accountData/accountData'

  export default {
    name: 'User',
    components: {
      UserEdit,
      UserInfo,
      AssistList,
      AuxiliaryInfoTable,
      AuxiliaryBalanceTable,
      AuxiliaryTable,
      ChangeAccountModal,
    },
    data() {
      return {
        leftItem: '',
        showTable: '',
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        bookInfo: null,
        queryForm: {
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    async mounted() {
      // this.fetchData()
    },
    methods: {
      showChangeAccountModal() {
        this.$refs['changeAccountModal'].showEdit()
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      // async fetchData() {
      //   if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      //   const bookInfo = localStorage.getItem('bookInfo')
      //   this.bookInfo = JSON.parse(bookInfo)
      //   this.listLoading = true
      //   const params = JSON.parse(JSON.stringify(this.queryForm))
      //   params.bookYear = this.bookInfo.bookYear
      //   const type = params.type
      //   if (type) {
      //     switch (type) {
      //       case 'AMONTH':
      //         params.minMonth = params.accName[0].split('-')[1]
      //         params.maxMonth = params.accName[1].split('-')[1]
      //         delete params.status
      //         delete params.accName
      //         break
      //       case '':
      //         break
      //       default:
      //         break
      //     }
      //   }
      //   console.log(params)
      //   const {
      //     data: { list, total },
      //   } = await getAccountAssistVoucher(params)
      //   this.list = list
      //   this.total = total
      //   this.listLoading = false
      // },
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
      selectTable(item) {
        this.showTable = item.label //判断显示三张表中的哪一张表
        this.leftItem = String(item.value) //传指定的项目让表查询
        //
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
