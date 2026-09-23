<!--
 * @Date: 2022-04-21 09:50:13
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 20:01:52
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/RelatedContractInfo.vue
-->
<template>
  <el-table :data="list">
    <el-table-column align="center" label="合同编号" prop="contractno" />
    <el-table-column align="center" label="合同名称" prop="contractname">
      <!-- <template #default="{ row }">
        <el-button type="text" @click="handleDetail(row)">
          {{ row.contractname }}
        </el-button>
      </template> -->
    </el-table-column>
    <el-table-column align="center" label="合同类型" prop="contracttype" />
    <el-table-column align="center" label="合同金额" prop="contractmoney" />
    <el-table-column align="center" label="创建日期" prop="createtime" />
    <!-- <CreateDetail v-if="showDetail" ref="detail" /> -->
  </el-table>
</template>
<script>
  // import CreateDetail from './contractsEdit/CreateDetail.vue'
  export default {
    name: 'RelatedContractInfo',
    // components: {
    //   CreateDetail,
    // },
    props: {
      list: {
        type: Array,
        default: () => [],
      },
    },
    methods: {
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.showDetail = true
        let self = this
        this.$nextTick(function () {
          self.$refs['detail'].showDetail(row, row.contracttype)
        })
      },
    },
  }
</script>
