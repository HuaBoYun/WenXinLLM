<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
      :close-on-click-modal="false"
    >
      <CompanyTree ref="company-tree" @select="handleSelect" />
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { addhyaccredit } from '@/api/setting/org'
  import CompanyTree from './CompanyTree.vue'
  export default {
    name: 'CompanyTreeSelect',
    components: { CompanyTree },
    data() {
      return {
        title: '机构定位',
        dialogFormVisible: false,
        orgids: undefined,
        companyid: undefined,
      }
    },
    created() {},
    methods: {
      show(orgids) {
        this.$nextTick(() => {
          this.$refs['company-tree'].fetchData()
        })
        this.dialogFormVisible = true
        this.orgids = orgids
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSelect(company) {
        this.companyid = company.id
      },
      async save() {
        const { msg, code } = await addhyaccredit({
          selid: this.companyid,
          orgids: this.orgids,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        }
      },
    },
  }
</script>
