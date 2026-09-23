<!--
 * @Date: 2022-04-29 11:06:24
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-05 17:04:18
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/FlowDescription.vue
-->
<template>
  <el-table :data="list">
    <el-table-column align="center" type="selection" width="55" />
    <el-table-column align="center" label="制度名称" prop="data" width="100" />
    <el-table-column align="center" label="发文文号" prop="data" width="100" />
    <el-table-column align="center" label="发文机构" prop="data" width="100" />
    <el-table-column align="center" label="发文日期" prop="data" width="100" />
    <el-table-column align="center" label="操作">
      <template #default="{ row }">
        <el-button type="text" @click="handleDelete(row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
  export default {
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        list: [],
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    methods: {
      handleDelete() {},
    },
  }
</script>
