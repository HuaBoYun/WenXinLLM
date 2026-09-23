<template>
  <div class="system-log-container">
    <el-col v-if="showTitle" :span="24">
      <h3>我的待办</h3>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="流程标题" prop="fullName" />
      <el-table-column align="center" label="所属流程" prop="flowName" />
      <el-table-column
        prop="status"
        label="流程状态"
        width="130"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.status == 1">等待审核</el-tag>
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
          <el-tag type="warning" v-else>等待提交</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="发起时间" prop="startTime">
        <template slot-scope="scope">
          {{ scope.row.startTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="200"
      >
        <template #default="{ row }">
          <el-button type="text" @click="showDetail(row, false)">
            详情
          </el-button>
          <el-button
            type="text"
            :disabled="[1, 2, 5].indexOf(row.status) > -1"
            @click="showDetail(row, true)"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            :disabled="row.status != 4"
            @click="deleteData(row)"
          >
            删除
          </el-button>
          <el-button
            type="text"
            :disabled="row.status == 2 || row.status == 4"
            @click="chehui(row)"
          >
            撤销
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :page-sizes="pageSizes"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <WfqdDeal ref="wfqddeal" />

    <!-- <reportModal
      ref="report"
      @reload-data="reload"
      :UEditorCloudEdit="this.UEditorCloudEdit"
    /> -->
  </div>
</template>

<script>
  import {
    ymWorkActionsWithdraw,
    ymWorkActionsDelete,
  } from '@/api/contract/manage'
  import { my_faqi } from '@/api/setting/msg'
  import { formatDate } from '@/utils/index'
  import PapersDetails from '@/views/msg/components/operation/papersDetails'
  import PaymentBanli from '@/views/msg/components/operation/PaymentDetail'
  import ReceivingBanli from '@/views/msg/components/operation/ReceivingDetail'
  import Reference from '@/views/msg/components/operation/ReferenceDetail.vue'
  import Track from '@/views/msg/components/options/Track'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import dayjs from 'dayjs'
  export default {
    name: 'Wddb',
    components: {
      PaymentBanli,
      ReceivingBanli,
      Track,
      WfqdDeal,
      PapersDetails,
      Reference,
    },
    props: {
      showTitle: {
        type: Boolean,
        default: false,
      },
    },
    filters: {
      formatDate(time) {
        return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
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
          currentPage: 1,
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
      this.$bus.$on('updateMsg', (type) => {
        if (type === 0) {
          this.fetchWfqdData()
          this.$refs['wfqddeal'].close()
        }
      })

      this.$nextTick(() => {
        if (this.$store.state.work.wfqdState) {
          this.showDetail(this.$store.state.work.wfqdDetails, false)
        }
      })
    },

    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetUEditorStatus() {
        this.UEditorCloudEdit = false
        this.UEditorShenPiCloudEdit = false
      },
      reload() {
        this.fetchWfqdData()
        this.resetUEditorStatus()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchWfqdData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchWfqdData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.currentPage = val
        this.fetchWfqdData()
      },
      queryData() {
        this.queryForm.currentPage = 1
        this.fetchWfqdData()
      },
      async fetchWfqdData() {
        this.listLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_faqi(this.queryForm)
          console.log('🚀 ~ fetchWfqdData ~ list:', list)
          this.list = list
          this.total = totalCount
          // 通知父组件更新数量
          this.$emit('update-count', totalCount)
        } catch (error) {
          console.error('获取我发起的数据失败:', error)
        } finally {
          this.listLoading = false
        }
      },
      async showDetail(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.wfqddeal.show(dataRow, isEdit)
      },
      async chehui(row) {
        this.$baseConfirm(
          '是否确定撤销，撤销后流程退回到初始节点，需重新发起审批！',
          null,
          async () => {
            this.listLoading = true
            try {
              const res = await ymWorkActionsWithdraw({
                id: row.id,
                flowId: row.flowId,
              })
              if (res.code == 1) {
                this.$message.success('撤销成功')
                this.fetchWfqdData()
              } else {
                this.listLoading = false
              }
            } catch (error) {
              console.error('撤销失败:', error)
              this.listLoading = false
            }
          }
        )
      },
      async deleteData(row) {
        this.$confirm('是否确认删除?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const res = await ymWorkActionsDelete({
            id: row.id,
            flowId: row.flowId,
          })
          if (res && res.code == 1) {
            this.$message.success('流程删除成功')
            this.fetchWfqdData()
          } else {
            this.$message.error('流程删除失败')
          }
        })
      },
      handleTrack(row) {
        this.$refs['track'].showEdit(row)
      },
      async showEdit(title, row, info, data, hamId) {
        // this.fectchBugcriidList()
        //从疑点管理过来时，data就会有值，用于回填附件信息
        if (data) {
          let list = []
          list.push(data.attachment)
          this.tableData = list
        }

        //我的底稿-新建跳转专用
        if (hamId) {
          if (this.showMJ && this.MJoption.length == 0) {
            // 获取密级,菜单id
            const res = await hasMJ(hamId)
            this.menuId = res[0].menuid
            // 请求密级下拉数据
            const res2 = await getMJ({ rightId: res[0].menuid })
            console.log('🚀 ~ showEdit ~ res3:', res2)
            this.MJoption = res2.data
          }
        }

        this.dialogFormVisible = true
        if (row) {
          console.log('🚀 ~ showEdit ~ row:', row)
          // 先获取缺陷等级列表
          await this.fectchBugcriidList(row.defecttype)
          // 再设置表单数据
          Object.keys(this.formData).forEach(
            (key) => (this.formData[key] = row[key])
          )
          this.formData.bugid = row.bugid
          this.formData.secrectLevelId = row.secrectLevelId
          this.formData.staffScopeNames = row.staffScopeNames
          this.formData.staffScopeIds = row.staffScopeIds
          this.formData.discovertime = formatDay(this.formData.discovertime)
          this.fetchFile(row.bugid)
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.bugdepartment = userInfo.linkDetp.orgid
          this.formData.discoverperson = userInfo.realname
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
          // this.formData.businessType = row.businessType
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          createFlawCode().then((res) => {
            this.$set(this.formData, 'bugnumber', res.data.autoCode.toString())
          })
        }
      },
    },
  }
</script>
