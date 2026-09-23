<template>
  <!-- 审计情况统计表 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    v-if="dialogVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="24" style="margin-top: 16px">
          <el-divider>基本信息</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="单位名称" prop="unitName">
            <el-input
              v-model="formData.unitName"
              placeholder="请输入单位名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-input
              v-model="formData.gender"
              :style="{ width: '100%' }"
              placeholder="请输入性别"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期">
            <el-date-picker
              v-model="formData.birthdate"
              type="date"
              placeholder="请输入出生日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="籍贯">
            <el-input
              v-model="formData.nativePlace"
              :style="{ width: '100%' }"
              placeholder="请输入籍贯"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="民族">
            <el-input
              v-model="formData.ethnicity"
              :style="{ width: '100%' }"
              placeholder="请输入民族"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌">
            <el-input
              v-model="formData.politicalStatus"
              :style="{ width: '100%' }"
              placeholder="请输入政治面貌"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>工作信息</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="主要从事的职责">
            <el-input
              v-model="formData.primaryResponsibilities"
              :style="{ width: '100%' }"
              placeholder="请输入主要从事的职责"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务">
            <el-input
              v-model="formData.position"
              :style="{ width: '100%' }"
              placeholder="请输入职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业技术职称">
            <el-input
              v-model="formData.professionalTitle"
              :style="{ width: '100%' }"
              placeholder="请输入专业技术职称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执业资格">
            <el-input
              v-model="formData.professionalQualification"
              :style="{ width: '100%' }"
              placeholder="请输入执业资格"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否为国际注册信息系统审计师CIA">
            <el-select
              v-model="formData.isCia"
              placeholder="请选择是否为国际注册信息系统审计师CIA"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参加工作时间">
            <el-date-picker
              v-model="formData.workStartDate"
              type="date"
              placeholder="请输入参加工作时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="进入本单位时间">
            <el-date-picker
              v-model="formData.joinUnitDate"
              type="date"
              placeholder="请输入进入本单位时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="进入系统时间">
            <el-date-picker
              v-model="formData.joinSystemDate"
              type="date"
              placeholder="请输入进入系统时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="从事审计工作年限">
            <el-input
              v-model="formData.yearsOfAuditExperience"
              :style="{ width: '100%' }"
              placeholder="请输入从事审计工作年限"
              @input="inputNum($event, 'yearsOfAuditExperience')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="曾从事的其他工作">
            <el-input
              v-model="formData.previousJobs"
              :style="{ width: '100%' }"
              placeholder="请输入曾从事的其他工作"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否提拔交流人员">
            <el-select
              v-model="formData.isPromotedTransfer"
              placeholder="请选择是否提拔交流人员"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否平职交流人员">
            <el-select
              v-model="formData.isHorizontalTransfer"
              placeholder="请选择是否平职交流人员"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>教育背景</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学历">
            <el-input
              v-model="formData.education"
              :style="{ width: '100%' }"
              placeholder="请输入学历"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历专业">
            <el-input
              v-model="formData.highestEducationMajor"
              :style="{ width: '100%' }"
              placeholder="请输最高学历专业"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他学历专业">
            <el-input
              v-model="formData.otherEducationMajors"
              :style="{ width: '100%' }"
              placeholder="请输入其他学历专业"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="毕业时间">
            <el-date-picker
              v-model="formData.graduationDate"
              type="date"
              placeholder="请输入毕业时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="毕业院校">
            <el-input
              v-model="formData.university"
              :style="{ width: '100%' }"
              placeholder="请输入毕业院校"
            />
          </el-form-item>
        </el-col>
  
        <el-col :span="24" style="margin-top: 16px">
          <el-divider>联系信息</el-divider>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="办公电话-区号">
            <el-input
              v-model="formData.officePhoneAreaCode"
              :style="{ width: '100%' }"
              placeholder="请输入办公电话-区号"
              @input="inputNum($event, 'officePhoneAreaCode')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="办公电话-电话">
            <el-input
              v-model="formData.officePhoneNumber"
              :style="{ width: '100%' }"
              placeholder="请输入办公电话-电话"
              @input="inputNum($event, 'officePhoneNumber')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号码">
            <el-input
              v-model="formData.mobilePhone"
              :style="{ width: '100%' }"
              placeholder="请输入手机号码"
              @input="inputNum($event, 'mobilePhone')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电子邮箱">
            <el-input
              v-model="formData.email"
              :style="{ width: '100%' }"
              placeholder="请输入电子邮箱"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>培训情况</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="培训学时">
            <el-input
              v-model="formData.trainingHours"
              :style="{ width: '100%' }"
              placeholder="请输入培训学时"
              @input="inputNum($event, 'trainingHours')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应支付的经费（万元）">
            <el-input
              v-model="formData.trainingExpenses"
              :style="{ width: '100%' }"
              placeholder="请输入对应支付的经费（万元）"
              @input="inputNum($event, 'trainingExpenses')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>奖惩情况</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="受到表彰情况">
            <el-input
              v-model="formData.recognitions"
              :style="{ width: '100%' }"
              placeholder="请输入受到表彰情况"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="受到惩处情况">
            <el-input
              v-model="formData.punishments"
              :style="{ width: '100%' }"
              placeholder="请输入受到惩处情况"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>参与审计项目情况</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否特邀审计员">
            <el-select
              v-model="formData.isSpecialInvitedAuditor"
              placeholder="请选择是否特邀审计员"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参加本单位审计项目的次数">
            <el-input-number v-model="formData.numberOfUnitAuditProjects" :style="{ width: '100%' }" :min="0" label="请输入参加本单位审计项目的次数" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参加上级审计部门审计项目的次数">
            <el-input-number v-model="formData.numberOfSuperiorAuditProjects" :style="{ width: '100%' }" :min="0" label="请输入参加上级审计部门审计项目的次数" />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="formData.remarks"
              :style="{ width: '100%' }"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>

      </el-form>
    </el-row>

    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />
    <project-manage2 @projectManage="getChildlistPro2" ref="manage2" />
    <cwsjxmapbOutView ref="cwsjxmapbOutView" type="report" />

    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    auditPersonnelInfoGetDetail,
    auditPersonnelInfoMergeInfo,
  } from '@/api/audit/analyse'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage1 from '@/components/danxuanPerson.vue'
  import projectManage2 from '@/components/selectPerson.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  export default {
    components: {
      SelectDepartment,
      projectManage1,
      projectManage2,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        tableData: [],
        fileData: [],
        formData: {
          birthdate: '',
          createTime: '',
          education: '',
          email: '',
          ethnicity: '',
          gender: '',
          graduationDate: '',
          graduationDateEnd: '',
          graduationDateStart: '',
          highestEducationMajor: '',
          id: '',
          isCia: '',
          isHorizontalTransfer: '',
          isPromotedTransfer: '',
          isSpecialInvitedAuditor: '',
          joinSystemDate: '',
          joinSystemDateEnd: '',
          joinSystemDateStart: '',
          joinUnitDate: '',
          joinUnitDateEnd: '',
          joinUnitDateStart: '',
          mobilePhone: '',
          name: '',
          nativePlace: '',
          numberOfSuperiorAuditProjects: '',
          numberOfUnitAuditProjects: '',
          officePhoneAreaCode: '',
          officePhoneNumber: '',
          otherEducationMajors: '',
          politicalStatus: '',
          position: '',
          previousJobs: '',
          primaryResponsibilities: '',
          professionalQualification: '',
          professionalTitle: '',
          punishments: '',
          recognitions: '',
          remarks: '',
          trainingExpenses: '',
          trainingHours: '',
          unitName: '',
          university: '',
          workStartDate: '',
          workStartDateEnd: '',
          workStartDateStart: '',
          yearsOfAuditExperience: ''
        },
        rules: {
          unitName: [
            {
              required: true,
              message: '请输入单位名称',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入姓名',
              trigger: 'blur',
            },
          ],
        },
        dialogVisible: false,
        disabled: false,
        select: [],
        title: '新增',
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
     //数字输入
     inputNum(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        // if (
        //   sanitizedValue.startsWith('0') &&
        //   sanitizedValue.length > 1 &&
        //   sanitizedValue[1] !== '.'
        // ) {
        //   sanitizedValue = sanitizedValue.slice(1)
        // }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        // if (sanitizedValue === '' || sanitizedValue === '0') {
        //   sanitizedValue = ''
        // }
        // 更新输入框的值
        this.formData[key] = sanitizedValue
      },
      async showEdit(row, title) {
        console.log(row) 

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          // this.disabled = false
        } else if (title == 'add') {
          this.title = '新增'
        }
        this.disabled = title == 'detail'
        if (row) {
          const {
            data: { data },
          } = await auditPersonnelInfoGetDetail({ id: row.id })
          console.log(data)
          this.formData = data
          // this.fetchData()
        }
        this.dialogVisible = true
      },
      close() {
        this.formData = {
          createTime: '',
          education: '',
          email: '',
          ethnicity: '',
          gender: '',
          graduationDate: '',
          graduationDateEnd: '',
          graduationDateStart: '',
          highestEducationMajor: '',
          id: '',
          isCia: '',
          isHorizontalTransfer: '',
          isPromotedTransfer: '',
          isSpecialInvitedAuditor: '',
          joinSystemDate: '',
          joinSystemDateEnd: '',
          joinSystemDateStart: '',
          joinUnitDate: '',
          joinUnitDateEnd: '',
          joinUnitDateStart: '',
          mobilePhone: '',
          name: '',
          nativePlace: '',
          numberOfSuperiorAuditProjects: '',
          numberOfUnitAuditProjects: '',
          officePhoneAreaCode: '',
          officePhoneNumber: '',
          otherEducationMajors: '',
          politicalStatus: '',
          position: '',
          previousJobs: '',
          primaryResponsibilities: '',
          professionalQualification: '',
          professionalTitle: '',
          punishments: '',
          recognitions: '',
          remarks: '',
          trainingExpenses: '',
          trainingHours: '',
          unitName: '',
          university: '',
          workStartDate: '',
          workStartDateEnd: '',
          workStartDateStart: '',
          yearsOfAuditExperience: ''
        }
        this.tableData = []
        this.select = []
        this.dialogVisible = false
        this.$emit('fetchData')
      },
      add(row, i) {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleEdit(row, i) {
        this.$refs['edit'].showEdit({ id: row.id, index: i + 1 }, 'edit')
      },

      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbrgname = val.name
        this.formData.tbrgid = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await auditPersonnelInfoMergeInfo({
              ...this.formData,
            })
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.tableData = [...this.tableData, ...response.data.data]
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },

      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, 'detail')
      },
      handleCwxmDetail(row, index) {
        if (row.gljhxmlx == '23') {
          this.$refs['cwsjxmapbOutView'].showEdit({ data: row })
        } else {
          this.$refs['edit'].showEdit(row, 'edit')
        }
      },
      getChildlistPro1() {},
      getChildlistPro2() {},
      push1() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage1'].showEdit()
      },
      push2() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage2'].showEdit()
      },

      async getChildlistPro2(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()

      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        this.select = val
        // const curSelected = val.filter((x) => !!x)
        // if (curSelected && curSelected.length) {
        //   curSelected.map((row) => {
        //     if (row && !this.select.some((x) => x.id == row.id)) {
        //       this.select.push(row)
        //     }
        //   })
        // } else {
        //   this.list.map((row) => {
        //     const i = this.select.findIndex((x) => x.id == row.id)
        //     if (i >= 0) {
        //       this.select.splice(i, 1)
        //     }
        //   })
        // }
      },

      xiafa(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.name)
        const names = val.map((res) => res.staffid)
        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }

        //下发通知
        xiafaListNew({
          tableId: '579594969821253',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.select = []
            // this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.multipleSelection = []
          }
        })
      },

      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
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
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.fileData
          list.push(file.data)
          this.fileData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
