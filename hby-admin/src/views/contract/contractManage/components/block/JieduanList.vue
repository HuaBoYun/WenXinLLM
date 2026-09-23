<!--
 * @Date: 2022-04-12 11:03:39
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-31 20:54:28
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/block/JieduanList.vue
-->
<template>
  <div class="block">
    <div style="text-align: right">
      <el-button
        v-if="!readonly"
        class="add-btn"
        size="mini"
        type="primary"
        @click="showHtjdEdit()"
      >
        新增
      </el-button>
    </div>

    <el-table :data="list">
      <el-table-column
        align="center"
        label="付款方向"
        prop="performanceCategory"
      >
        <template slot-scope="{ row }">
          {{ renderPerformance(row) }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="履行内容"
        prop="nodecontent"
      ></el-table-column>
      <el-table-column
        align="center"
        label="预计开始时间"
        prop="planstartdate"
      />
      <el-table-column align="center" label="预计结束时间" prop="planenddate" />
      <el-table-column
        v-if="contract.dctype != '无'"
        align="center"
        :label="'预计' + contract.dctype + '时间'"
        prop="nodeplanpaydate"
      />
      <el-table-column
        v-if="contract.dctype != '无'"
        align="center"
        :label="contract.dctype + '比例'"
        prop="nodepost"
      />
      <el-table-column
        v-if="contract.dctype != '无'"
        align="center"
        :label="contract.dctype + '金额(元)'"
        prop="nodemoney"
      />
      <el-table-column align="center" label="经办部门" prop="orgname" />
      <el-table-column align="center" label="经办人" prop="realname" />
      <el-table-column
        v-if="!readonly"
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="showHtjdEdit(row)">修改</el-button>
          <el-button type="text" @click="handleHtjdDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <htjd-edit ref="htjd" :contract="contract" @fetch-data="fetchData" />
  </div>
</template>
<script>
  import { deleteJd, getJdDetail, getNodeList } from '@/api/contract/manage'
  import HtjdEdit from './JieduanEdit.vue'
  export default {
    name: 'JieduanList',
    components: {
      HtjdEdit,
    },
    inject: ['fatherFetchItem'],
    props: {
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
      XDFList: {
        type: Object,
        default: () => {
          return {}
        },
      },
    },
    data() {
      return {
        list: this.contract.nodeList || [],
      }
    },
    // computed: {
    //   list() {
    //     return this.contract.nodeList
    //   },
    // },
    watch: {
      // eslint-disable-next-line func-names
      'contract.nodeList': function (val, oldVal) {
        this.fetchData()
      },
    },
    methods: {
      //过滤数据
      renderPerformance(e) {
        switch (e.performanceCategory) {
          case '1':
            return '付款'
          case '2':
            return '收款'
          // case '3':
          //   return '工期'
          // case '4':
          //   return '服务期'
          // case '5':
          //   return '交付成果'
          // case '6':
          //   return '其它'
          default:
            return ''
        }
      },
      //前置校验
      async showHtjdEdit(row) {
        if (!this.contract || !this.contract.contractid) {
          this.$baseMessage(
            '请先到页面底部保存合同基本信息后，再新增合同阶段信息',
            'error',
            'vab-hey-message-error'
          )
          return
        }

        let data = row || {}
        if (row && row.nodeid) {
          const res = await getJdDetail({
            nodeId: row.nodeid,
          })
          data = res.data
        }
        this.$refs['htjd'].showEdit({
          contractid: this.contract.contractid,
          ...data,
          dctype: this.contract.dctype,
          XDFList: this.XDFList,
        })
      },
      //删除的前置校验
      handleHtjdDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          await deleteJd({
            nodeId: row.nodeid,
          })
          this.fetchData()
        })
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        console.log('fetchData', this.contract)
        if (!this.contract.contractid) return
        const { data, code } = await getNodeList({
          contractId: this.contract.contractid,
        })
        if (code == 1) {
          this.list = data
          // this.$emit('data-change', data)
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
