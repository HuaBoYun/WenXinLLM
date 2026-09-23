<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="通知标题" prop="noticeTitle" />
      <el-table-column align="center" label="通知内容" prop="noticeContent" />
      <el-table-column
        align="center"
        label="下发人名称"
        prop="distributeName"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="agreeTZ(row)">确认</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :page-sizes="pageSizes"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <cssyDeal ref="cssyDeal" />
  </div>
</template>

<script>
  import { my_shiyi } from '@/oapi/setting/msg'
  import { formatDate } from '@/utils/index'
  import { getHomeList, agree } from '@/oapi/audit/xmpy'

  export default {
    name: 'cssy',
    components: {},
    props: {
      showTitle: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        pageSizes: [5, 10, 15, 20, 50, 100],
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
        },
        type: '',
        showModal: false,
        UEditorCloudEdit: false,
        UEditorShenPiCloudEdit: false,
      }
    },
    created() {
      this.fetchWfqdData()
    },

    mounted() {
      this.$nextTick(() => {
        if (this.$store.state.work.cssyState) {
          this.showDetail(this.$store.state.work.cssyDetails, false)
        }
      })
    },

    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchWfqdData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchWfqdData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchWfqdData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchWfqdData()
      },
      async fetchWfqdData() {
        const {
          data: { tlist, totalRecord },
        } = await getHomeList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.$emit('XMPYtotal', totalRecord)
        this.listLoading = false
      },
      async showDetail(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.cssyDeal.show(dataRow, isEdit)
      },
      handleTrack(row) {
        this.$refs['track'].showEdit(row)
      },
      agreeTZ(row) {
        agree({ id: row.id }).then((res) => {
          if (res.code == 200) {
            this.$baseMessage(res.msg, 'success')
            this.fetchWfqdData()
          }
        })
      },
    },
  }
</script>
