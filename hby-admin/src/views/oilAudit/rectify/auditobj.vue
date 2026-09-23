<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="人员名称"
        width="100"
        prop="realname"
        show-overflow-tooltip
      >
        <!-- <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row, '查看')" type="text">
            {{ scope.row.realname }}
          </el-button>
        </template> -->
      </el-table-column>
      <el-table-column
        align="center"
        label="性别"
        prop="gender"
        v-model="queryForm.gender"
      >
        <template slot-scope="scope">
          {{ scope.row.gender === '1' ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        width="120"
        label="出生年月"
        prop="birthday"
        v-model="queryForm.birthday"
      />
      <el-table-column
        align="center"
        label="政治面貌"
        prop="politicaloutlook"
        v-model="queryForm.politicaloutlook"
      />
      <el-table-column
        align="center"
        label="学历/学位"
        prop="education"
        width="100"
        v-model="queryForm.education"
      />
      <el-table-column
        align="center"
        label="专业"
        prop="major"
        v-model="queryForm.major"
      />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <!-- <Personnel ref="edit" /> -->
  </div>
</template>
<script>
  import {
    getObjectPageList,
    personDelete,
    personShenPi,
  } from '@/oapi/setting/personnel'
  // import Personnel from './components/personnelEdit.vue'
  export default {
    // components: { Personnel },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          realName: '',
          education: '',
          jobExperiences: '',
          jobName: '',
          major: '',
          pageNumber: 1,
          pageSize: 20,
        },
        list: [],
        listLoading: true,
        statusName: [
          '未审批',
          '审批中',
          '已退回',
          '已通过',
          '已终止',
          '',
          '已完成',
        ],
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleDetail(row) {},

      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true

        const orgid = JSON.parse(localStorage.getItem('userInfo')).linkDetp
          .orgid

        this.queryForm.orgId = ''
        // this.queryForm.orgId = orgid

        const res = await getObjectPageList(this.queryForm)

        res.data.pageInfo.forEach((element) => {
          if (element.birthday) {
            element.birthday = element.birthday.split('T')[0]
          }
          if (element.worktime) {
            element.worktime = element.worktime.split('T')[0]
          }
        })
        this.list = res.data.pageInfo
        this.total = res.data.size
        this.listLoading = false
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handlePersonShenPi(row) {
        personShenPi({
          staffid: row.staffid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.$baseMessage('操作成功', 'success')
            this.fetchData()
          }
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await personDelete({ staffId: row.staffid })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
    },
  }
</script>
