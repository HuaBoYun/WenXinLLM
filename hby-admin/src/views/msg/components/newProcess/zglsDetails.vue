<template>
  <div>
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
          <el-divider>整改内容</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="formData.tblReforms" :key="tableFlag">
            <el-table-column align="center" label="问题编号" prop="code">
              <template #default="{ row }">
                <el-button type="text" @click="practicable(row, '查看')">
                  {{ row.code }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="问题详情" prop="details" />
            <el-table-column align="center" label="问题来源" prop="source" />
            <el-table-column align="center" label="发现人" prop="discoverer" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
              v-if="footer"
            >
              <template #default="{ row }">
                <el-button type="text" @click="practicable(row, '编辑')">
                  编辑
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-top: 10px">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <!-- <el-button v-if="footer" type="success">上传</el-button> -->
            </el-upload>
          </div>
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
                <!-- <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button type="primary" @click="add()">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

    <projectList ref="list" @selected="handleProjectSelected" />
    <contentTable ref="content" @selected="handleContentSelected" />
    <practicableForm ref="practicable" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    createSchemeCode,
    delSolutionAttInfo,
    getSolutionAttInfo,
    mergeSolutionInfo,
    getReformByid,
  } from '@/api/audit/rectify'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import store from '@/store'
  import projectList from '@/views/audit/rectify/components/options/projectList'
  import contentTable from '@/views/audit/rectify/components/table/contentTable'
  import practicableForm from '@/views/audit/rectify/components/form/practicableForm'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: {
      projectList,
      contentTable,
      practicableForm,
      Resubmit,
    },
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
        //提交
        visible: false,
        fzforms: {
          branchStrs: '',
        },
        fzform1: {
          branchStrs: '',
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
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
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleProjectSelected(node) {
        //
        const formData = this.formData
        formData.prjoectName = node.prjoectName
        formData.projectid = node.projectId
        this.formData = { ...formData }
        // this.queryForm.reporter = node.staffid
      },
      showEdit(title, row, fromId, flowtaskinfoflowid, ymFromId, status) {
        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status
        this.dialogFormVisible = true
        // this.formData = {}
        //
        if (row) {
          const { relatedProject, createStaff, ...other } = row.solution
          this.formData = {
            realname: createStaff ? createStaff.realname : '',
            prjoectName: relatedProject ? relatedProject.prjoectName : '',
            projectid: relatedProject ? relatedProject.projectId : '',
            ...other,
          }
          this.getFileList(row.solution.solutionid)
        }
        if (title == 'edit') {
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
          //
          this.formData.realname = userInfo.realname
        }
        //
      },
      async getFileList(solutinid) {
        const data = await getSolutionAttInfo({ solutionid: solutinid })

        if (data.code == '1') {
          this.tableData = data.data || []
        } else {
          this.tableData = []
        }
      },
      close() {
        this.formData = {}

        this.$bus.$emit('updateMsg', 0)
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
                staffid: this.staffid,
              })
            }

            if (data.code == 1) {
              this.formData = {}
            }
            this.$emit('fetch-data')
            this.close()
          } else {
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
      async practicable(row, type) {
        const data = await getReformByid({
          solutionid: this.formData.solutionid,
          reformid: row.reformid,
        })
        await this.$refs['practicable'].showEdit(data.data, type)
      },

      //提交
      async ymsubmit() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
