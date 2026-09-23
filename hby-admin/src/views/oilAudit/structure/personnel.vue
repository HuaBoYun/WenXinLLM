<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.realName"
              clearable
              placeholder="名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.education"
              clearable
              placeholder="学历"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.jobExperiences"
              clearable
              placeholder="经验"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.jobName"
              clearable
              placeholder="岗位"
            />
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.major" clearable placeholder="专业" />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleEdit(false, '新建')">
          新建
        </el-button>
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="人员名称"
        width="100"
        prop="realname"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row, '查看')" type="text">
            {{ scope.row.realname }}
          </el-button>
        </template>
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
        width="100"
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
      <el-table-column
        align="center"
        label="状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ statusName[row.aprStatus] || '未审核' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.aprStatus"
            type="text"
            @click="handlePersonShenPi(scope.row)"
          >
            提交审批
          </el-button>
          <el-button
            :disabled="scope.row.aprStatus"
            type="text"
            @click="handleEdit(scope.row, '修改')"
          >
            修改
          </el-button>

          <el-button
            :disabled="scope.row.aprStatus"
            type="text"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
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
    <Personnel ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" />
  </div>
</template>
<script>
  import {
    getStaffPageList,
    personDelete,
    personShenPi,
  } from '@/oapi/setting/personnel'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import Personnel from './components/personnelEdit.vue'
  export default {
    components: { Personnel, ProcessList },
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
          pageNo: 1,
          pageSize: 20,
        },
        list: [],
        listLoading: true,
        statusName: [
          '未审批',
          '审批中',
          '已退回',
          '已撤回',
          '已终止',
          '',
          '已完成',
        ],
      }
    },
    created() {
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
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

        this.queryForm.orgId = orgid

        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getStaffPageList(this.queryForm)

        list.forEach((element) => {
          if (element.birthday) {
            element.birthday = element.birthday.split('T')[0]
          }
          if (element.worktime) {
            element.worktime = element.worktime.split('T')[0]
          }
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handlePersonShenPi(row) {
        const tableId = 14
        const fromId = row.staffid
        this.$refs['process'].save(tableId, fromId)
        // if (this.statusName[row.aprStatus] != '未审批' && row.aprStatus) {
        //   this.$baseMessage('流程进行中', 'error')
        // } else {
        //   this.$refs['process'].show(row, 14)
        // }
        // personShenPi({
        //   staffid: row.staffid,
        // }).then((res) => {
        //   if (res.msg === '成功') {
        //     this.$baseMessage('操作成功', 'success')
        //     this.fetchData()
        //   }
        // })
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
