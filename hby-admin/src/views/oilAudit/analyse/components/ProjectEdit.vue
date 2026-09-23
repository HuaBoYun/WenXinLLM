<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="140px" :model="form" :rules="rules">
      <el-col :span="12">
        <el-form-item label="所属审计计划" prop="tblnbsjPlan.planname">
          <span>{{ form.tblnbsjPlan.planname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="计划项目" prop="pprojectName">
          <span>{{ form.pprojectName }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目编号" prop="projectCode">
          <sapn>{{ form.projectCode }}</sapn>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目名称" prop="prjoectName">
          <span>{{ form.prjoectName }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="工作目标" prop="targetName">
          <span>{{ form.targetName }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目对象" prop="orgName">
          <span>{{ form.orgName }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="计划年度" prop="planYear">
          <span>{{ form.planYear }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="审计类型" prop="auditType">
          <span>{{ form.auditType }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目来源" prop="disputecours">
          <span>{{ form.projectSource }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目经理" prop="zxstaffname">
          <span>{{ form.pmStaff.realname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目计划时间" prop="isUegent">
          <span>
            {{ formatTime(form.startDate) + '-' + formatTime(form.endDate) }}
          </span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="审计模板" prop="templeteName">
          <span>{{ form.templeteName }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目费用估算(元)" prop="costs">
          <span>{{ form.costs }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="审计指引" prop="tbltempletezy">
          <span>{{ form.tbltempletezy.templeteName }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="审计方式" prop="proSjfs">
          <sapn>{{ form.proSjfs }}</sapn>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否外委" prop="externAlassig">
          <span>{{ form.externAlassig }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="审计目标和范围" prop="purpose">
          <span>{{ form.purpose }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="审计内容和重点" prop="scopes">
          <span>{{ form.scopes }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="审计程序和方法" prop="pursuant">
          <span>{{ form.pursuant }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="对专家和外部审计结果的利用" prop="comments">
          <span>{{ form.comments }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="其他有关内容" prop="proDesc">
          <span>{{ form.proDesc }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>项目小组</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table :data="planList">
          <el-table-column align="center" label="小组名称" prop="teamName" />
          <el-table-column align="center" label="组长" prop="leaderName" />
          <el-table-column align="center" label="组员" prop="zyNames" />
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table :data="fileList">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
        </el-table>
      </el-col>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getProjectPjteamList, projectFileList } from '@/oapi/audit/project'
  import { doEdit } from '@/oapi/table'
  import { parseTime } from '@/utils/index'
  export default {
    name: 'LcdyEdit',
    data() {
      return {
        planList: [],
        form: {
          // code: '',
          // name: '',
          // org: '长江集团有限公司',
          // intro: '',
          // remark: '',
          tblnbsjPlan: '',
          pprojectName: '',
          projectCode: '',
          prjoectName: '',
          targetName: '',
          orgName: '',
          planYear: '',
          auditType: '',
          projectSource: '',
          pmStaff: '',
          templeteName: '',
          costs: '',
          tbltempletezy: '',
          proSjfs: '',
          externAlassig: '',
          purpose: '',
          scopes: '',
          pursuant: '',
          comments: '',
          proDesc: '',
        },
        rules: {
          code: [{ required: true, trigger: 'blur', message: '请输入编号' }],
          name: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        fileList: [],
        dialogFormVisible: false,
        options: [
          {
            value: '1',
            label: '党委办公室',
          },
          {
            value: '2',
            label: '审计监察部',
          },
          {
            value: '3',
            label: '风险管理部',
          },
          {
            value: '4',
            label: '总会办公室',
          },
          {
            value: '5',
            label: '总经理办公室',
          },
          {
            value: '6',
            label: '人力资源部',
          },
          {
            value: '7',
            label: '合同管理部',
          },
          {
            value: '8',
            label: '安全管理部',
          },
          {
            value: '9',
            label: '设备运营部',
          },
          {
            value: '10',
            label: '工程管理部',
          },
          {
            value: '11',
            label: '综合管理部',
          },
          {
            value: '12',
            label: '信息运维管理部',
          },
        ],
      }
    },
    created() {},
    methods: {
      formatTime(val) {
        return parseTime(val, '{y}/{m}/{d}')
      },
      async showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '详情'
          this.form = Object.assign({}, row)
          // Object.keys(this.form).forEach((key) => {
          //   this.form[key] = row[key]
          // })
          this.form.tblnbsjPlan = row.tblnbsjPlan ? row.tblnbsjPlan : ''
          // 附件
          let resFile = await projectFileList({
            projectId: this.form.projectId,
          })
          this.fileList = resFile.data.data
          // 项目列表
          let resPlanProject = await getProjectPjteamList({
            projectid: this.form.projectId,
          })
          this.planList = resPlanProject.data.listTeam
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
