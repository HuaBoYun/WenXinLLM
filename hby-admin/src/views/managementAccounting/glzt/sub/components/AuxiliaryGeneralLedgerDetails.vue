<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1600px"
    @close="close"
  >
    <!-- <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <div>单位：{{ org }}</div>
      <div>
        <span style="margin-left: 20px">科目：{{ subject }}</span>
        <span style="margin-left: 20px">期间：{{ period }}</span>
        <span style="margin-left: 20px">币别：{{ currency }}</span>
      </div>
    </div> -->
    <el-table :data="list">
      <el-table-column
        align="center"
        label="辅助编号"
        prop="accid"
      ></el-table-column>
      <el-table-column align="center" label="辅助名称" prop="abstract">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">
            {{ row.abstract }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="辅助类型" prop="accName" />
      <el-table-column align="right" label="摘要"></el-table-column>
      <el-table-column align="right" label="借方"></el-table-column>
      <el-table-column align="right" label="贷方"></el-table-column>
      <el-table-column align="right" label="余额"></el-table-column>
    </el-table>
    <BookkeepingVoucher ref="edit" />
  </el-dialog>
</template>

<script>
  import BookkeepingVoucher from './BookkeepingVoucher.vue'
  export default {
    name: 'SummanyInfo',
    components: { BookkeepingVoucher },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        org: '国有资产监督管理委员会', //单位
        subject: '[1001]库存现金', //科目
        period: '2018 年第 1月---2018 年第7月', //期间
        currency: '人民币', //币别
        list: [{ abstract: 'asdf' }],
        formData: {
          economicsContractRatio: '',
          firmEconomicsContractNumber: '',
          firmMajorDecisionNumber: '',
          firmRegulationsAuditNumber: '',
          majorDecisionRatio: '',
          regulationsAuditRatio: '',
          remark: '',
        },
        footer: true,
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
        title: '辅助总账-明细',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false

        this.footer = true
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
