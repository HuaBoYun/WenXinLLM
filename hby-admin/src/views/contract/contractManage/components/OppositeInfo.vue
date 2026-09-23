<!--
 * @Date: 2022-04-21 09:49:57
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 20:31:49
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/OppositeInfo.vue
-->
<template>
  <el-table :data="list">
    <el-table-column align="center" label="相对方编号" prop="counterpartno" />
    <el-table-column align="center" label="相对方名称" prop="budgetname">
      <!-- <template #default="{ row }">
        <el-button type="text" @click.stop="handleDetail(row)">
          {{ row.budgetname }}
        </el-button>
      </template> -->
    </el-table-column>
    <el-table-column
      align="center"
      label="注册资本（万元）"
      prop="totaltmoney"
    />
    <el-table-column align="center" label="法定代人" prop="projectstagegoal" />
    <!-- <el-table-column
      align="center"
      label="相对方地址"
      prop="counterpartaddress"
    />
    <el-table-column
      align="center"
      label="相对方电话"
      prop="counterpartphone"
    />
    <el-table-column align="center" label="邮编" prop="counterpartcode" /> -->
    <MaintainDetail ref="detail" />
  </el-table>
</template>
<script>
  import MaintainDetail from '@/views/contract/opposite/components/MaintainDetail'
  export default {
    name: 'OppositeInfo',
    props: {
      list: {
        type: Array,
        default: () => [],
      },
    },
    components: {
      MaintainDetail,
    },
    methods: {
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        // this.$refs['detail'].showDetail(row)
      },
    },
  }
</script>
