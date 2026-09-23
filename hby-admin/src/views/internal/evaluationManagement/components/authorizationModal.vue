<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-press-escape="false"
    >
      <el-col :span="24">
        <el-divider>
          项目名称: {{ this.assessname }} &emsp;&emsp;&emsp;评价编号:
          {{ this.assessid }}
        </el-divider>
      </el-col>
      <el-table :data="tableData">
        <el-table-column align="center" label="评价对象编号" prop="orgnumber" />
        <el-table-column align="center" label="评价对象" prop="orgname" />
        <el-table-column align="center" label="发起人" prop="name">
          {{ asssponsor }}
        </el-table-column>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleAuthorization2(row)"
              v-if="!isActive"
            >
              授权
            </el-button>
            <el-button
              type="text"
              @click="handleAuthorization2(row, true)"
              v-if="isActive"
            >
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
      <template #footer>
        <el-button @click="close">关 闭</el-button>
      </template>
    </el-dialog>
    <AuthorizationModal2 ref="AuthorizationModal2" />
  </div>
</template>

<script>
  import { getAuthorizationModalData } from '@/api/internal/project'
  import AuthorizationModal2 from '@/views/internal/evaluationManagement/components/authorizationModal2.vue'
  export default {
    name: 'TrackList',
    components: { AuthorizationModal2 },
    data() {
      return {
        isActive: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 3,
        tableData: [],
        title: '授权',
        dialogFormVisible: false,
        assid: '', // 用于请求授权列表
        assessname: '',
        assessid: '',
        asssponsor: '',
        secrectLevelId: '',
      }
    },
    created() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageBean,
            project: { assessname, assessid, asssponsor },
          },
        } = await getAuthorizationModalData({
          ...this.queryForm,
          selectedPlans: +this.assid,
        })

        this.tableData = pageBean.records
        this.total = pageBean.total
        this.listLoading = false
        this.assessname = assessname
        this.assessid = assessid
        this.asssponsor = asssponsor
      },
      showEdit(row, isActive) {
        console.log('🚀 ~ showEdit ~ row:', row)
        this.dialogFormVisible = true
        this.assid = row.assid
        this.isActive = isActive
        this.secrectLevelId = row.secrectLevelId
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.$emit('fetchData')
      },
      handleAuthorization2(row, isActive) {
        const info = {
          orgId: row.orgid,
          assId: this.assid,
          secrectLevelId: this.secrectLevelId,
        }
        console.log('🚀 ~ handleAuthorization2 ~ info:', info)
        this.$refs['AuthorizationModal2'].showEdit(info, isActive)
      },
    },
  }
</script>
