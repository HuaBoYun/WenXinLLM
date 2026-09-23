<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1600px"
    @close="close"
  >
    <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <div></div>
      <div>
        日期：{{ date }}
        <span style="margin-left: 20px">附件数：{{ numberOfAttachments }}</span>
        <span style="margin-left: 20px">凭证号：{{ voucherNo }}</span>
      </div>
    </div>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="摘要"
        prop="lineText"
      ></el-table-column>
      <el-table-column align="center" label="科目" prop="accNameOne" />
      <el-table-column align="right" label="借方" prop="md"></el-table-column>
      <el-table-column align="right" label="贷方" prop="mc"></el-table-column>
      <div slot="append" class="end">
        <span>财务主管：{{ list.length > 0 ? list[0].cwzh : '' }}</span>
        <span>记 账 人：{{ list.length > 0 ? list[0].jzr : '' }}</span>
        <span>出 纳 人：{{ list.length > 0 ? list[0].cnr : '' }}</span>
        <span>审 核 人：{{ list.length > 0 ? list[0].shr : '' }}</span>
        <span>制 单 人：{{ list.length > 0 ? list[0].zdr : '' }}</span>
      </div>
    </el-table>
  </el-dialog>
</template>

<script>
  import { addFLSH } from '@/api/fwgl/zzxx'
  import { bookkeepingDetail } from '@/api/workbench/accountData/accountData'
  import getUserSelectedBookInfo from './../../utils/getBookInfo'
  export default {
    name: 'SummanyInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        voucherNo: '0001', //凭证号
        numberOfAttachments: 1, //附件数
        date: '2018-06-12', //日期
        formData: {},
        row: {},
        listLoading: true,
        footer: true,
        list: [],
        rules: {
          entercoed: [
            {
              required: true,
              message: '请输入进场纪要编号',
              trigger: 'blur',
            },
          ],
          entername: [
            {
              required: true,
              message: '请输入进场纪要名称',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '记账凭证',
      }
    },

    computed: {},
    watch: {},
    created() {},
    async mounted() {
      // if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      // const bookInfo = localStorage.getItem('bookInfo')
      // this.bookInfo = JSON.parse(bookInfo)
      // this.fetchData()
    },
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        //
        this.row = row
        //
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.list = []
      },
      async fetchData() {
        this.listLoading = true
        const date = this.row.pzDate.slice(0, 10)
        // const year = date.getFullYear()
        const info = {
          book: this.bookInfo.acctId,
          pzDate: date,
          pzh: this.row.pzh,
          year: this.bookInfo.bookYear,
        }
        //
        const { data } = await bookkeepingDetail(info)
        this.list = data
        this.voucherNo = this.row.pzh
        this.numberOfAttachments = data&&data.length>0&&data[0].fj
        this.date = date
        //
        this.listLoading = false
      },
      // add() {
      //
      //   addFLSH(this.formData).then((res) => {
      //     if (res.msg == '成功') {
      //       this.dialogFormVisible = false
      //       this.$emit('addList', res.data)
      //     }
      //   })
      // },
    },
  }
</script>
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }

  .end {
    padding: 15px 50px;
    display: flex;
  }
  .end span {
    flex: 1;
  }
</style>
