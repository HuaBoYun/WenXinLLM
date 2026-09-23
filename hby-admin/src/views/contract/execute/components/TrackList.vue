<!--
 * @Date: 2022-03-31 15:03:53
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-24 23:09:33
 * @FilePath: /hb-admin/src/views/contract/execute/components/TrackList.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="履行内容" prop="nodecontent">
        <template #default="{ row }">
          <el-button @click="handleApproval(row)" type="text">
            {{ row.nodecontent }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="履行开始时间"
        prop="planstartdate"
      />
      <el-table-column align="center" label="履行结束时间" prop="planenddate" />
      <el-table-column
        align="center"
        label="预计收付款时间"
        prop="nodeplanpaydate"
      />
      <el-table-column align="center" label="收付款比例" prop="nodepost" />
      <!-- <el-table-column align="center" label="反馈意见" prop="feedback" />-->
      <el-table-column align="center" label="状态">
        <template #default="{ row }">
          {{
            !row.plannodestatus
              ? '未落实'
              : row.plannodestatus == 1 && fromPage == 'practicable'
              ? '已落实'
              : row.plannodestatus == 3 && fromPage == 'practicable'
              ? '已撤回'
              : row.plannodestatus == 2 && fromPage == 'practicable'
              ? '已完成'
              : row.plannodestatus == 1 && fromPage == 'track'
              ? '未落实'
              : row.plannodestatus == 3 && fromPage == 'track'
              ? '已撤回'
              : row.plannodestatus == 2 && fromPage == 'track'
              ? '已完成'
              : '已完成'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
        v-if="fromPage !== 'track'"
      >
        <template #default="{ row }">
          <el-button
            v-if="fromPage == 'practicable'"
            type="text"
            @click="handleImplement(row)"
            @fetch-data="fetchData"
            :disabled="row.plannodestatus == 2 || row.plannodestatus == 1"
          >
            落实
          </el-button>
          <el-button
            v-if="fromPage == 'practicable'"
            type="text"
            @click="handleReloads(row)"
            @fetch-data="fetchData"
            :disabled="
              row.plannodestatus == 2 ||
              row.plannodestatus == 3 ||
              !row.plannodestatus
            "
          >
            完成
          </el-button>
          <el-button
            v-if="fromPage == 'practicable'"
            type="text"
            @click="handleReload(row)"
            @fetch-data="fetchData"
            :disabled="
              row.plannodestatus == 2 ||
              row.plannodestatus == 3 ||
              !row.plannodestatus
            "
          >
            撤回
          </el-button>

          <!-- <el-button
            v-if="row.plannodestatus == 1 && fromPage == 'track'"
            type="text"
            @click="handleApproval(row)"
            @fetch-data="fetchData"
          >
            审批
          </el-button>
          <el-button
            v-if="!row.plannodestatus && fromPage == 'practicable'"
            type="text"
            @click="handleImplement(row)"
            @fetch-data="fetchData"
          >
            落实
          </el-button>
          <el-button
            v-if="row.plannodestatus == 3 && fromPage == 'practicable'"
            type="text"
            @click="handleImplement(row)"
            @fetch-data="fetchData"
          >
            调整
          </el-button>
          <el-button
            v-if="
              row.plannodestatus &&
              row.plannodestatus != 0 &&
              row.plannodestatus != 1
            "
          >
            完成
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
    <ApprovalEdit ref="approval" @fetch-data="fetchData()" />
    <ImplementEdit ref="implement" @fetch-data="fetchData()" />
  </el-dialog>
</template>
<script>
  import {
    getSubTrackingList,
    reloadTrack,
    completeSpNode,
  } from '@/api/contract/fulfil'
  import {
    approvalStatusOptions,
    implementStatusOptions,
  } from '@/views/contract/consts'
  import ApprovalEdit from './ApprovalEdit'
  import ImplementEdit from './ImplementEdit.vue'
  export default {
    name: 'FileList',
    components: { ApprovalEdit, ImplementEdit },
    props: {
      type: {
        type: String,
        default: 'sp',
      },
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          nodeId: undefined,
          contractId: undefined,
          planStatus: undefined,
          feedback: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        dialogVisible: false,
        title: '',
        fromPage: '',
      }
    },
    created() {},
    methods: {
      //过滤数据
      mapPlanNodeStatus(row, type) {
        const options =
          type == 'sp' ? approvalStatusOptions : implementStatusOptions
        let text = ''
        if (!row.plannodestatus) {
          text = '未落实'
        } else {
          const res = options.filter((item) => {
            return item.value === row.plannodestatus
          })
          text = res[0].label
        }
        return text
      },
      show(row, page) {
        this.dialogVisible = true
        this.queryForm.contractId = row.contractid
        this.fromPage = page
        this.fetchData()
      },
      handleApproval(row) {
        const text = this.mapPlanNodeStatus(row, this.type)
        // if (text != '待审批') {
        //   this.$baseMessage(
        //     `合同履行内容${text}，不能审批！`,
        //     'error',
        //     'vab-hey-message-error'
        //   )
        //   return
        // } else {
        //   this.$refs['approval'].showEdit(row)
        // }
        // this.$refs['approval'].showEdit(row)
        this.$refs['implement'].showDetail(row)
      },
      //回调
      handleImplement(row) {
        const text = this.mapPlanNodeStatus(row, '落实')
        if (text == '审批中' || text == '已完成') {
          this.$baseMessage(
            `合同履行内容${text}，不能落实！`,
            'error',
            'vab-hey-message-error'
          )
          return
        } else {
          this.$refs['implement'].showEdit(row)
        }
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getSubTrackingList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.$emit('fetch-data') // 刷新列表的状态
      },
      close() {
        this.dialogVisible = false
      },
      async handleReload(row) {
        await reloadTrack({
          planStatus: 3,
          nodeId: row.nodeid,
          feedback: '已撤回',
        })
        await this.fetchData()
      },
      async handleReloads(row) {
        await completeSpNode({
          contractId: this.queryForm.contractId,
          nodeId: row.nodeid,
        })
        await this.fetchData()
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['implement'].showEdit(row)
      },
    },
  }
</script>
