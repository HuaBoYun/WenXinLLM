<!--
 * @Date: 2022-04-21 09:50:13
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 20:01:52
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/RelatedContractInfo.vue
-->
<template>
  <div>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="履行内容" prop="nodecontent">
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="handleApproval2(scope.row)"
            :disabled="false"
          >
            {{ scope.row.nodecontent }}
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
      <!-- <el-table-column align="center" label="反馈意见" prop="feedback" /> -->
      <el-table-column align="center" label="是否违约" prop="iswy" />
      <el-table-column align="center" label="状态">
        <template #default="{ row }">
          {{
            !row.plannodestatus
              ? '未落实'
              : row.plannodestatus == 1 && fromPage == 'practicable'
              ? '审批中'
              : row.plannodestatus == 3 && fromPage == 'practicable'
              ? '需调整'
              : row.plannodestatus == 2 && fromPage == 'practicable'
              ? '已完成'
              : row.plannodestatus == 1 && fromPage == 'track'
              ? '待审批'
              : row.plannodestatus == 3 && fromPage == 'track'
              ? '调整中'
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
        v-if="!disabled"
      >
        <template #default="{ row }">
          <el-button
            v-if="row.plannodestatus == 1 && fromPage == 'track'"
            type="text"
            @click="handleApproval(row)"
            @fetch-data="fetchData"
            :disabled="false"
          >
            审批
          </el-button>
          <el-button
            v-if="!row.plannodestatus && fromPage == 'practicable'"
            type="text"
            @click="handleImplement(row)"
            @fetch-data="fetchData"
            :disabled="false"
          >
            落实
          </el-button>
          <el-button
            v-if="row.plannodestatus == 3 && fromPage == 'practicable'"
            type="text"
            @click="handleImplement(row)"
            @fetch-data="fetchData"
            :disabled="false"
          >
            调整
          </el-button>
          <!-- <el-button
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
    <ApprovalEdit ref="approval" @fetch-data="fetchData()" />
    <ApprovalEdit2 ref="approval2" @fetch-data="fetchData()" />
    <ImplementEdit ref="implement" @fetch-data="fetchData()" />
  </div>
</template>
<script>
import ApprovalEdit from '@/views/contract/execute/components/ApprovalEdit'
import ApprovalEdit2 from './ApprovalEdit'
import ImplementEdit from '@/views/contract/execute/components/ImplementEdit.vue'
import { getSubTrackingList } from '@/api/contract/fulfil'
import {
  implementStatusOptions,
  approvalStatusOptions,
} from '@/views/contract/consts'

export default {
  name: 'practicableList',
  components: {
    ApprovalEdit,
    ImplementEdit,
    ApprovalEdit2,
  },
  props: {
    formData: {
      type: Object,
      default: () => {},
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  // watch:{
  //   formData(val){
  //     if(val){
  //       this.queryForm.contractId = val.contractid
  //       this.fetchData()
  //     }
  //   }
  // },
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
    }
  },
  methods: {
    show(row, type) {
      this.queryForm.contractId = row.contractid
      this.fromPage = type
      this.fetchData()
    },
    clear() {
      this.list = []
      this.total = 0
      this.listLoading = false
    },
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
    handleApproval2(row) {
      this.$refs['implement'].showDetail(row)
    },
    handleApproval(row) {
      const text = this.mapPlanNodeStatus(row, this.type)
      if (text != '待审批') {
        this.$baseMessage(
          `合同履行内容${text}，不能审批！`,
          'error',
          'vab-hey-message-error'
        )
        return
      } else {
        this.$refs['approval'].showEdit(row)
      }
    },
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
    async fetchData() {
      this.listLoading = true
      const {
        data: { tlist, totalRecord },
      } = await getSubTrackingList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
  },
}
</script>
