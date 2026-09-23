<!--
 * @Date: 2022-04-12 11:03:39
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-24 14:27:37
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/block/SubList.vue
-->
<template>
  <div class="block">
    <el-button
      v-if="!readonly"
      class="add-btn"
      size="mini"
      type="primary"
      @click="showEdit"
    >
      新增
    </el-button>
    <el-table :data="list">
      <el-table-column
        v-for="field in fields"
        :key="field.value"
        align="center"
        :label="field.label"
        :prop="field.value"
      />
      <el-table-column v-if="showTotal" align="center" label="总价">
        <template #default="{ row }">
          {{ row.infoprice * row.infonum }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="!readonly"
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row, index }">
          <el-button type="text" @click="showEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row, index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <sub-edit
      ref="ccwp"
      :contract="contract"
      :fields="fields"
      @fetch-data="fetchData"
    />
  </div>
</template>
<script>
  import { deleteCc, getSubList } from '@/api/contract/manage'
  import SubEdit from './SubEdit.vue'
  export default {
    name: 'SubList',
    components: {
      SubEdit,
    },
    inject: ['fatherFetchItem'],
    props: {
      fields: {
        type: Array,
        default: () => [],
      },
      showTotal: {
        type: Boolean,
        default: false,
      },
      contract: {
        type: Object,
        default: () => {
          return {
            informationList: [],
            nodeList: [],
          }
        },
      },
      readonly: {
        type: Boolean,
        default: false,
      },
    },
    computed: {
      list() {
        return this.contract.informationList
      },
    },
    methods: {
      showEdit(row) {
        if (!this.contract || !this.contract.contractid) {
          this.$baseMessage(
            '请先保存合同基本信息',
            'success',
            'vab-hey-message-success'
          )
          return
        }
        this.$refs['ccwp'].showEdit({
          contractid: this.contract.contractid,
          ...row,
        })
      },
      //删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          await deleteCc({
            infoId: row.infoid,
          })
          this.fetchData()
        })
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        const { data, code } = await getSubList({
          contractId: this.contract.contractid,
        })
        if (code == 1) {
          this.$emit('data-change', data)
        }
      },
    },
  }
</script>
<style scoped>
  .block {
    margin: 16px 0;
  }
  .add-btn {
    margin-bottom: 10px;
  }
</style>
