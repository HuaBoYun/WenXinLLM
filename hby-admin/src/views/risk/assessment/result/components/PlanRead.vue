<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="评估计划编号" prop="plancode">
              {{ formData.plancode }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估计划名称" prop="planName">
              {{ formData.planName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划开始时间" prop="startDate">
              {{ formData.startDate }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束时间" prop="endDate">
              {{ formData.endDate }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估标准" prop="assname">
              {{ formData.assname }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <div v-if="formData.planType === '1'">年度计划</div>
              <div v-if="formData.planType === '2'">临时性计划</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制定机构" prop="orgname">
              {{ formData.orgname }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="录入人员" prop="realname">
              {{ formData.realname }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评估计划描述" prop="planDes">
              {{ formData.planDes }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>评估的风险信息</el-divider>
          </el-col>
          <el-col :span="24">
            <el-table :data="list">
              <el-table-column
                align="center"
                label="风险点编号"
                prop="risk.risknumber"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="风险点描述"
                prop="risk.riskdes"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="评估人员"
                prop="reporter"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  <div v-for="user in row.tblRiskRiskMarking" :key="user.id">
                    {{
                      user.staff.realname + '(' + (user.assweight || 0) + '%)'
                    }}
                  </div>
                </template>
              </el-table-column>
              <!-- <el-table-column align="center" label="操作">
                <template #default="{ row }">
                  <el-button type="text" @click="handleDeleteRisk(row)">
                    删除风险
                  </el-button>
                  <el-button type="text" @click="handleSetStaff(row)">
                    设置评估人员
                  </el-button>
                  <el-button type="text" @click="handleSetWeight(row)">
                    设置权重
                  </el-button>
                </template>
              </el-table-column> -->
            </el-table>
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <!-- <div style="text-align: right; margin-bottom: 5px">
              <el-button type="success">上传</el-button>
            </div> -->
            <el-table :data="tableData">
              <el-table-column align="center" label="附件名称" prop="attname" />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="attsize"
              />
              <el-table-column align="center" label="创建人" prop="uploader" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDownload(row)">
                    下载
                  </el-button>
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
  import { riTaskInfo } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { downFieldById } from '@/api/risk/riskEvents'
  export default {
    name: 'PlanRead',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        formData: {
          plancode: undefined,
          planName: undefined,
          startDate: null,
          endDate: null,
          assname: undefined,
          planType: undefined,
          orgname: undefined,
          realname: undefined,
          planDes: undefined,
          field111: null,
        },
        list: [],
        tableData: [],
        rules: {
          plancode: [
            {
              required: true,
              message: '请输入评估计划编号',
              trigger: 'blur',
            },
          ],
          planName: [
            {
              required: true,
              message: '请输入评估计划名称',
              trigger: 'blur',
            },
          ],
          startDate: [
            {
              required: true,
              message: '请选择计划开始时间',
              trigger: 'change',
            },
          ],
          endDate: [
            {
              required: true,
              message: '请选择计划结束时间',
              trigger: 'change',
            },
          ],
          assname: [
            {
              required: true,
              message: '请选择评估标准',
              trigger: 'change',
            },
          ],
          planType: [
            {
              required: true,
              message: '请选择计划类型',
              trigger: 'change',
            },
          ],
          orgname: [],
          realname: [],
          planDes: [],
        },
        assnameOptions: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        planTypeOptions: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          //此接口通用？
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      save() {},
      showRead(row) {
        this.title = '查看'
        riTaskInfo({ planId: row.assplanid }).then((res) => {
          this.formData.plancode = res.data.RiskAssplan.plancode
          this.formData.planName = res.data.RiskAssplan.planName
          this.formData.startDate = res.data.RiskAssplan.startDate
          this.formData.endDate = res.data.RiskAssplan.endDate
          this.formData.assname = res.data.RiskAssplan.assessmentstd.assname
          this.formData.planType = res.data.RiskAssplan.planType
          this.formData.orgname = res.data.RiskAssplan.organization.memo
          this.formData.planDes = res.data.RiskAssplan.plandes

          this.list = res.data.RiskAssplan.riskAssplanRiskList

          this.tableData = res.data.RiskAssplan.tblAttachments || []
          this.loading = false
        })
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSetStaff(row) {
        console.log('set staff', row)
      },
      handleSetWeight(row) {
        console.log('set weight', row)
      },
      async handleDownload(row) {
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleDeleteRisk(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
    },
  }
</script>
<style></style>
