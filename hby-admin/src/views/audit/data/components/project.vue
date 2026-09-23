<template>
  <el-row :gutter="24">
    <el-form :model="form">
      <el-col
        v-for="item in projectPlanFormOptions"
        :key="item.prop"
        :span="item.col"
      >
        <el-form-item
          :label="item.label"
          :label-width="item.labelWidth"
          :prop="item.prop"
        >
          <el-input
            v-if="item.type === 'input' || item.type === 'textarea'"
            :disabled="true"
            :type="item.type === 'textarea' ? 'textarea' : 'text'"
            :rows="3"
            v-model="form[item.prop]"
          ></el-input>
          <el-date-picker
            v-if="item.type === 'date' || item.type === 'year'"
            v-model="form[item.prop]"
            :disabled="true"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            range-separator="-"
            start-placeholder="开始日期"
            style="width: 100%"
            :type="item.type === 'date' ? 'daterange' : 'year'"
            value-format="yyyy-MM-dd"
          />
          <el-select
            v-if="item.type === 'select'"
            style="width: 100%"
            :disabled="true"
            v-model="form[item.prop]"
          ></el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>项目小组</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table :data="tableData">
          <el-table-column align="center" label="小组名称" prop="teamName" />
          <el-table-column align="center" label="组长" prop="leaderName" />
          <el-table-column align="center" label="组员" prop="zyNames" />
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-table :data="filetData">
          <el-table-column
            align="center"
            :label="item.label"
            :prop="item.prop"
            v-for="item in projectPlanFileTable"
            :key="item.prop"
          />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDown(row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-form>
  </el-row>
</template>

<script>
  import {
    download,
    getProjectPlanDetails,
    getProjectPlanTableDat,
  } from '@/api/audit/implement'
  import { projectFileList } from '@/api/audit/project'
  import {
    projectPlanFileTable,
    projectPlanFormOptions,
    projectPlanTable,
  } from './table'
  export default {
    props: {
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        form: {},
        projectPlanFormOptions: projectPlanFormOptions,
        projectPlanTable: projectPlanTable,
        projectPlanFileTable: projectPlanFileTable,
        tableData: [],
        filetData: [],
      }
    },
    mounted() {
      this.getDta()
    },
    methods: {
      async getDta() {
        let obj = {
          projectid: this.projectId || undefined,
        }
        const res = await getProjectPlanDetails(obj)
        const {
          data: { listTeam },
        } = await getProjectPlanTableDat(obj)
        const filet = await projectFileList(obj)

        this.form = res.data.pj
        this.form.auditOrgName = res.data.pj.auditStaffName
          ? res.data.pj.auditStaffName
          : res.data.pj.auditOrgName
        this.form.planname = res.data.pj.tblnbsjPlan.planname
        this.form.realName = res.data.pj.pmStaff.realname
        this.form.tbltempletezyName = res.data.pj.tbltempletezy.templeteName
        this.form.templeteName = res.data.pj.tbltemplete.templeteName
        this.form.planEndDate = res.data.pj.endDate
        this.form.planStartDate = res.data.pj.startDate
        this.form.externAlassig = res.data.pj.externAlassig === 0 ? '否' : '是'
        this.form.dateSection = [res.data.pj.startDate, res.data.pj.endDate]
        listTeam.forEach((k) => {
          let a1 = [],
            a2 = []
          k.teamStaffs.forEach((v) => {
            if (v.stafftype == 0) {
              k.leaderId = v.staffid
              k.leaderName = v.staff.realname
            } else {
              a1.push(v.staffid)
              a2.push(v.staff.realname)
            }
          })
          k.zystaffids = a1.join()
          k.zyNames = a2.join()
        })
        this.tableData = listTeam
      },
      async handleDown(row) {
        const data = await download({ attId: row.response.data.attid })
        let filename = row.name
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
    },
  }
</script>

<style lang="scss" scoped></style>
