<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    :close-on-click-modal="false"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="方案编号" prop="solutioncode">
            <el-input
              v-model="formData.solutioncode"
              clearable
              placeholder="请输入方案编号"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案名称" prop="solutionname">
            <el-input
              v-model="formData.solutionname"
              clearable
              placeholder="请输入方案名称"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="关联审计项目" prop="prjoectName">
            <el-input
              v-model="formData.prjoectName"
              clearable
              placeholder="请输入关联审计项目"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请输入创建人"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改截止时间" prop="enddate">
            <el-date-picker
              v-model="formData.enddate"
              value-format="yyyy-MM-dd"
              :style="{ width: '256px' }"
              type="date"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位整改责任人" prop="bsjdwzfr">
            <el-input
              v-model="formData.bsjdwzfr"
              clearable
              placeholder="请选择一级复核人"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="projectManager1"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改联络人" prop="zgllr">
            <el-input
              v-model="formData.zgllr"
              clearable
              placeholder="请选择二级复核人"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="projectManager2"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="memo">
            <el-input
              v-model="formData.memo"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>整改清单</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px"></div>
          <el-table :data="formData.tblReforms" :key="tableFlag">
            <el-table-column align="center" label="问题编号" prop="code" />
            <el-table-column align="center" label="问题详情" prop="details" />
            <el-table-column align="center" label="问题来源" prop="source" />
            <el-table-column align="center" label="发现人" prop="discoverer" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
              v-if="footer"
            ></el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-top: 10px">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
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
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <projectList ref="list" @selected="handleProjectSelected" />
    <contentTable ref="content" @selected="handleContentSelected" />
    <!-- 复核人选择 -->
    <projectManage
      :modal="false"
      ref="manage"
      @reviewTypeSelect="reviewTypeSelect"
    />
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    createSchemeCode,
    delSolutionAttInfo,
    getSolutionAttInfo,
    mergeSolutionInfo,
    issuePersonliable,
  } from '@/api/audit/rectify'
  import store from '@/store'
  import projectList from '@/views/audit/rectify/components/options/projectList'
  import contentTable from '@/views/audit/rectify/components/table/contentTable'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'

  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: { projectList, contentTable, projectManage },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          solutioncode: undefined,
          projectid: undefined,
          solutionname: undefined,
          prjoectName: undefined,
          realname: undefined,
          endDate: undefined,
          memo: undefined,
          tblReforms: [],
        },
        footer: true,
        tableFlag: false,
        tableData: [],
        rules: {
          solutioncode: [
            {
              required: true,
              message: '请输入方案编号',
              trigger: 'blur',
            },
          ],
          solutionname: [
            {
              required: true,
              message: '请输入方案名称',
              trigger: 'blur',
            },
          ],
          prjoectName: [
            {
              required: true,
              message: '请输入关联审计项目',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请输入创建人',
              trigger: 'blur',
            },
          ],
          enddate: [
            {
              required: true,
              message: '请输入截止时间',
              trigger: 'blur',
            },
          ],
          memo: [
            {
              required: false,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        staffid: '',
        arr: [],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      addItem() {
        this.$refs['content'].show(this.formData)
      },
      handleContentSelected(node) {
        let formData = this.formData
        if (this.formData.tblReforms) {
          formData.tblReforms = [...this.formData.tblReforms, ...node]
        } else {
          formData.tblReforms = node
        }
        this.formData = formData
        this.tableFlag = true
        console.log('form', this.formData)
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleProjectSelected(node) {
        // console.log(node)
        const formData = this.formData
        formData.prjoectName = node.prjoectName
        formData.projectid = node.projectId
        this.formData = { ...formData }
        // this.queryForm.reporter = node.staffid
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.formData = {}
  
        if (row) {
          const { relatedProject, createStaff, ...other } = row.solution
          this.formData = {
            realname: createStaff ? createStaff.realname : '',
            prjoectName: relatedProject ? relatedProject.prjoectName : '',
            projectid: relatedProject ? relatedProject.projectId : '',
            zgllr: row.solution.reformUser&&row.solution.reformUser.realname,
            reformuserid: row.solution.reformUser&&row.solution.reformUser.staffid,
            ...other,
          }
    
          console.log("fwdjaadsasd",this.formData)
          this.getFileList(row.solution.solutionid)
        }
        if (title == 'edit') {
          console.log('修改')
          this.title = '编辑'
          this.staffid = row.solution.createStaff.staffid
          this.arr = row.solution.tblReforms
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = {}
          createSchemeCode().then((res) => {
            this.$set(
              this.formData,
              'solutioncode',
              res.data.autoCode.toString()
            )
          })
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          // console.log('adddd')
          this.formData.realname = userInfo.realname
        }
      },
      async getFileList(solutinid) {
        const data = await getSolutionAttInfo({ solutionid: solutinid })
        console.log(data)

        if (data.code == '1') {
          this.tableData = data.data || []
        } else {
          this.tableData = []
        }
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''

            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })

            attids = attids.substring(0, attids.length - 1)
            let list = this.formData.tblReforms || []
            let reformid = ''

            // const list = arr.filter(item=>)
            let list1 = list.filter(
              (item) => !this.arr.some((ele) => ele.code === item.code)
            )
            list1.map((item) => {
              reformid += item.problemid
              reformid += ','
            })
            console.log(this.formData, 'formData')
            reformid = reformid.substring(0, reformid.length - 1)
            if (this.title == '新增') {
              var data = await mergeSolutionInfo({
                attids,
                reformid,
                endDate: this.formData.enddate.split('T')[0],
                projectid: this.formData.projectid,
                memo: this.formData.memo,
                runstatus: this.formData.runstatus,
                solutioncode: this.formData.solutioncode,
                solutionid: this.formData.solutionid,
                solutionname: this.formData.solutionname,
              })
            } else {
              var data = await issuePersonliable({
                // ...this.formData,
                attids,
                reformid,
                endDate: this.formData.enddate.split('T')[0],
                projectid: this.formData.projectid,
                memo: this.formData.memo,
                runstatus: this.formData.runstatus,
                solutioncode: this.formData.solutioncode,
                solutionid: this.formData.solutionid,
                solutionname: this.formData.solutionname,
                staffid: this.staffid,
                zgllr: this.formData.zgllr,
                reformuserid: this.formData.reformuserid,
                bsjdwzrrid: this.formData.bsjdwzrrid,
                bsjdwzfr: this.formData.bsjdwzfr,
              })
            }

            if (data.code == 1) {
              this.formData = {}
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
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
      async handleDelete(row) {
        let list = this.tableData || []
        list = list.filter((item) => item.attid != row.attid)
        await delSolutionAttInfo({ attid: row.attid })
        this.tableData = list
      },
      handleDelte2(row) {
        let list = this.formData.tblReforms || []
        list = list.filter((item) => item.problemid != row.problemid)
        this.tableFlag = false
        // await delSolutionAttInfo({ attid: row.attid })
        this.formData.tblReforms = list
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData || []
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      reviewTypeSelect(e) {
        let name = e.reviewType === 'bsjdwzrrid' ? 'bsjdwzfr' : 'zgllr'
        this.$set(this.formData, `${name}`, e.id[0].realname)
        this.$set(this.formData, `${e.reviewType}`, e.id[0].staffid)
        this.$refs['ruleForm'].clearValidate()
      },
      projectManager1() {
        this.$refs['manage'].showEdit('bsjdwzrrid')
      },
      projectManager2() {
        this.$refs['manage'].showEdit('reformuserid')
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
