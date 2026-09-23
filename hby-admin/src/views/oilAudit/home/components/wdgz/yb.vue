<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list1">
      <el-table-column align="center" label="流程标题" prop="fullName" />
      <el-table-column align="center" label="所属流程" prop="flowName" />
      <el-table-column
        prop="status"
        label="流程状态"
        width="130"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status == 1">通过</el-tag>
          <el-tag type="danger" v-else>拒绝</el-tag>
          <!-- <el-tag type="primary" v-if="scope.row.status == 1">
                等待审核
              </el-tag>
              <el-tag type="success" v-else-if="scope.row.status == 2">
                审核通过
              </el-tag>
              <el-tag type="danger" v-else-if="scope.row.status == 3">
                审核驳回
              </el-tag>
              <el-tag type="info" v-else-if="scope.row.status == 4">
                流程撤回
              </el-tag>
              <el-tag type="info" v-else-if="scope.row.status == 5">
                审核终止
              </el-tag>
              <el-tag type="warning" v-else>等待提交</el-tag> -->
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="发起时间"
        prop="creatorTime"
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail1(row, false)">
            详情
          </el-button>
          <el-button type="text" @click="handleReback1(row)">撤回</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm1.currentPage"
      :layout="layout"
      :page-size="queryForm1.pageSize"
      :page-sizes="pageSizes"
      :total="total1"
      @current-change="handleCurrentChange1"
      @size-change="handleSizeChange1"
    />
    <!-- <el-col v-if="showTitle" :span="24">
      <h3>我的传阅</h3>
    </el-col> -->

    <Deal ref="deal" />
    <PaymentBanli ref="banlifk" @fetch-data="fetchData" />
    <ReceivingBanli ref="banlisk" @fetch-data="fetchData" />
    <ShenjiModal ref="shenji" @reload-data="fetchData" />
    <reportModal ref="report" @reload-data="fetchData" />
    <EvaluationModal ref="evaluation" @fetch-data="fetchData" />
    <PersonModal ref="person" @fetch-data="fetchData" />
    <Reference ref="reference" />
    <!-- 合同 -->
    <WdcyDeal ref="WdcyDeal" @fetchData="fetchData1" />
    <WdcyBackModal ref="WdcyBackModal" @fetchData="fetchData1" />
    <!-- 撤回 -->
    <Chyy ref="chyy" @fetchData="fetchData1" />
  </div>
</template>

<script>
  import PaymentBanli from '@//views/msg/components/operation/PaymentDetail'
  import { my_circulation, my_circulation1 } from '@/oapi/setting/msg'
  import ReceivingBanli from '@/views/msg/components/operation/ReceivingDetail'
  import Reference from '@/views/msg/components/operation/ReferenceDetail.vue'
  import Deal from '@/views/msg/components/options/Deal'
  import EvaluationModal from '@/views/msg/components/operation/evaluationsModal.vue'
  import PersonModal from '@/views/msg/components/operation/personModal.vue'
  import reportModal from '@/views/msg/components/operation/reportModal.vue'
  import ShenjiModal from '@/views/msg/components/operation/shenpiModal.vue'
  import Chyy from '@/views/msg/components/chyy.vue'
  // 合同
  import { formatDate } from '@/utils/index'
  import WdcyBackModal from '@/views/msg/components/options/WdcyBackModal.vue'
  import WdcyDeal from '@/views/msg/components/options/WdcyDeal.vue'

  export default {
    name: 'Wdcy',
    components: {
      Deal,
      PaymentBanli,
      ReceivingBanli,
      ShenjiModal,
      reportModal,
      EvaluationModal,
      PersonModal,
      Reference,
      WdcyBackModal,
      WdcyDeal,
      Chyy,
    },
    props: {
      showTitle: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        list: [], //审计
        list1: [], //合同
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        total1: 0,
        pageSizes: [5, 10, 15, 20, 50, 100],
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
        },
        queryForm1: {
          currentPage: 1,
          pageSize: 5,
        },
        type: '',
        activeName: 'second',
      }
    },
    created() {
      this.fetchData1()
    },

    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },

      handleSizeChange1(val) {
        this.queryForm1.pageSize = val
        this.fetchData()
      },
      handleCurrentChange1(val) {
        this.queryForm1.currentPage = val
        this.fetchData()
      },
      async fetchData1() {
        this.listLoading = true
        const {
          data: { list, totalRecord },
        } = await my_circulation1(this.queryForm)
        this.list1 = list
        this.total1 = totalRecord
        this.listLoading = false
      },
      // handleDetail(row) {
      //   if (row.cytype == '收款管理' || row.cytype == '付款管理') {
      //     if (row.cytype == '收款管理') {
      //       this.type = 'receiving'
      //       this.$refs['banlisk'].showEdit(row)
      //     }
      //     if (row.cytype == '付款管理') {
      //       this.type = 'payment'
      //       this.$refs['banlifk'].showEdit(row)
      //     }
      //   } else {
      //     if (row.cytype == '合同管理' && row.recordtype == 'HTGL007') {
      //       //合同范本
      //       this.type = 'sample'
      //     }
      //     if (row.cytype == '合同管理' && row.recordtype == 'HTGL002') {
      //       //合同起草
      //       this.type = 'create'
      //     }
      //     if (row.cytype == '合同管理' && row.recordtype == 'HTGL005') {
      //       //合同变更
      //       this.type = 'change'
      //     }
      //     if (row.cytype == '合同用印') {
      //       this.type = 'seal'
      //     }
      //     if (row.cytype == '合同借阅') {
      //       this.type = 'borrow'
      //     }
      //     if (row.cytype == '相对方维护') {
      //       this.type = 'opposite'
      //     }
      //     // 审计计划
      //     if (row.cytype == '计划审批') {
      //       this.type = 'IndexEdit'
      //     }
      //     if (row.cytype == '项目审批') {
      //       this.type = 'project'
      //     }
      //     if (row.cytype == '档案借阅') {
      //       this.$refs['reference'].showEdit('传阅', row)
      //       return
      //     }
      //     if (row.cytype === '审计通知书') {
      //       this.$refs['shenji'].showEdit(row, '查看')
      //       return
      //     }
      //     if (row.cytype === '审计报告复核' || row.cytype === '审计报告') {
      //       this.$refs['report'].showEdit(row, '查看')
      //       return
      //     }
      //     if (row.cytype == '审计人员审核') {
      //       this.$refs['person'].showEdit(row, '传阅查看')
      //       return
      //     }
      //     if (row.cytype === '评价审核') {
      //       this.$refs['evaluation'].showEdit('评价审核传阅', row)
      //       return
      //     }
      //     this.$refs['deal'].show(row, this.type)
      //   }
      // },
      handleDetail1(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.WdcyDeal.show(dataRow, isEdit)
      },
      handleReback1(row) {
        this.$refs['chyy'].showModal(row.id)
      },
    },
  }
</script>
