<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="closeTop"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-button
            type="success"
            style="margin: -2% 0 2% 94%"
            @click="add"
            :disabled="!footer"
          >
            保存
          </el-button>
        </el-col>

        <el-col :span="12">
          <el-form-item label="缺陷编号" prop="bugnumber">
            <el-input
              v-model="formData.bugnumber"
              clearable
              placeholder="请输入缺陷编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷级别" prop="bugcriid">
            <el-select
              v-model="formData.bugcriid"
              placeholder="请输入缺陷级别"
              :disabled="!footer"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in bugcriidList"
                :label="item.bugcrilevel"
                :value="item.bugcriid"
                :key="item.bugcriid"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现日期" prop="discovertime">
            <el-date-picker
              v-model="formData.discovertime"
              placeholder="请输入发现日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现人" prop="discoverperson">
            <el-input
              v-model="formData.discoverperson"
              clearable
              placeholder="请输入发现人"
              :style="{ width: '305px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否财务相关" prop="bugsource">
            <el-radio-group v-model="formData.bugsource" :disabled="!footer">
              <el-radio label="是" value="是" />
              <el-radio label="否" value="否" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否需要整改" prop="needreform">
            <el-radio-group v-model="formData.needreform" :disabled="!footer">
              <el-radio label="是" value="是" />
              <el-radio label="否" value="否" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷性质" prop="bugproperty">
            <el-select
              v-model="formData.bugproperty"
              placeholder="请输入缺陷性质"
              :disabled="!footer"
              :style="{ width: '100%' }"
            >
              <el-option label="执行缺陷" value="执行缺陷" />
              <el-option label="设计缺陷" value="设计缺陷" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入缺陷部门"
              :style="{ width: '305px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务单元" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              placeholder="请输入业务单元"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="业务描述" prop="businessDescription">
            <el-input
              v-model="formData.businessDescription"
              clearable
              type="textarea"
              :rows="2"
              placeholder="请输入业务描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="formData.needreform == '否'">
          <el-form-item label=" 不整改原因" prop="resonfornoreform">
            <el-input
              v-model="formData.resonfornoreform"
              clearable
              placeholder="请输入不整改原因"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="缺陷描述" prop="bugdescripte">
            <el-input
              v-model="formData.bugdescripte"
              clearable
              type="textarea"
              :rows="2"
              placeholder="请输入缺陷描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>违反内规</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openInterior" :disabled="!footer">
              新建
            </el-button>
          </div>
          <el-table :data="interiorList">
            <el-table-column align="center" label="制度名称" prop="rulename" />
            <el-table-column
              align="center"
              label="发文文号"
              prop="rulenumber"
            />
            <el-table-column align="center" label="发布机构" prop="orgname" />
            <el-table-column
              align="center"
              :formatter="formatDate"
              label="发布日期"
              prop="publishdate"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="delInterior(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>违反外规</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openExterior" :disabled="!footer">
              新建
            </el-button>
          </div>
          <el-table :data="externalList">
            <el-table-column align="center" label="制度名称" prop="rulename" />
            <el-table-column
              align="center"
              label="发文文号"
              prop="rulenumber"
            />
            <el-table-column align="center" label="发布机构" prop="orgname" />
            <el-table-column
              align="center"
              :formatter="formatDate"
              label="发布日期"
              prop="publishdate"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="delExternal(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col :span="24">
          <el-divider>关联缺陷</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              @click="openAssociate"
              :disabled="!footer"
            >
              新建
            </el-button>
          </div>
          <el-table :data="associateList">
            <el-table-column align="center" label="缺陷编号" prop="bugnumber" />
            <el-table-column
              align="center"
              label="缺陷描述"
              prop="bugdescripte"
            />
            <el-table-column
              align="center"
              :formatter="formatDate"
              label="发现时间"
              prop="discovertime"
            />
            <el-table-column
              align="center"
              label="发现人"
              prop="discoverperson"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button v-if="footer" type="text" @click="delAssociate(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :before-upload="handleBeforeUpload"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <!-- <el-button @click="close">取消</el-button> -->
      <el-button @click="close" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <!-- <el-button @click="close">关 闭</el-button> -->
      <el-button @click="close" type="primary">确定</el-button>
    </template>

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <ExternalTable ref="external" @selected="handleExternalSelected" />
    <InteriorTable ref="interior" @selected="handleInteriorSelected" />
    <AssociatedTable ref="associate" @selected="handleAssociateSelected" />
  </el-dialog>
</template>

<script>
  import { deleteFile, download } from '@/oapi/audit/implement'
  import {
    bugcriidList,
    createFlawCode,
    defectAdd,
    defectFileList,
    delAssociateDefect,
    delInnerCommonQxwt,
    delOuterCommonQxwt,
    getAssociateDefect,
    getInnerCommonQxwt,
    getOuterCommonQxwt,
  } from '@/oapi/audit/question'
  import store from '@/store'
  import { parseTime } from '@/utils/index'
  import DepartmentOptions from './options/department.vue'
  import ExecutorOptions from './options/executor.vue'
  import AssociatedTable from './table/AssociatedTable'
  import ExternalTable from './table/ExternalTable'
  import InteriorTable from './table/InteriorTable'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: {
      DepartmentOptions,
      ExecutorOptions,
      ExternalTable,
      InteriorTable,
      AssociatedTable,
    },
    inheritAttrs: false,

    data() {
      return {
        bugcriidList: [],
        associateList: [],
        externalList: [],
        interiorList: [],
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          bugnumber: undefined,
          discovertime: undefined,
          bugcriid: undefined,
          discoverperson: undefined,
          bugsource: '否',
          needreform: '是',
          bugproperty: undefined,
          businessDescription: undefined,
          bugdescripteion: undefined,
          orgname: undefined,
          bugdepartment: undefined,
          reporter: undefined,
          businessType: undefined,
        },
        footer: true,
        tableData: [],
        rules: {
          bugnumber: [
            {
              required: true,
              message: '请输入缺陷编号',
              trigger: 'blur',
            },
          ],
          bugcriid: [
            {
              required: true,
              message: '请选择缺陷级别',
              trigger: 'blur',
            },
          ],
          discovertime: [
            {
              required: true,
              message: '请输入发现日期',
              trigger: 'blur',
            },
          ],
          discoverperson: [
            {
              required: true,
              message: '请输入发现人',
              trigger: 'blur',
            },
          ],
          bugsource: [
            {
              required: false,
              message: '是否财务相关',
              trigger: 'blur',
            },
          ],
          bugproperty: [
            {
              required: true,
              message: '请输入缺陷性质',
              trigger: 'blur',
            },
          ],
          bugdescripte: [],
          needreform: [],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {
      this.fectchBugcriidList()
    },
    mounted() {},
    methods: {
      async fetchFile(bugid) {
        const res = await defectFileList({
          bugId: Number(bugid),
        })
        this.tableData = res.data.data || []
      },
      async fectchBugcriidList() {
        const res = await bugcriidList()
        this.bugcriidList = res.data.list
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      // 删除关联bug
      async delAssociate(row) {
        const res = await delAssociateDefect({
          bugid: this.formData.bugid,
          bugids: row.bugid,
        })
        if (res.code === 1) {
          this.$message.success('删除成功')
          this.fetchAssociate(this.formData.bugid)
        }
      },
      openAssociate() {
        if (!this.formData.bugid) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
          return
        }

        // const data = {
        //   orgId: this.formData.bugdepartment,
        //   bugcriid: this.formData.bugcriid,
        //   bugnumber: this.formData.bugnumber,
        // }
        this.$refs['associate'].showEdit(this.formData.bugid)
      },
      handleAssociateSelected(bugid) {
        this.fetchAssociate(bugid)
      },
      // 查询关联bug
      async fetchAssociate(bugid) {
        const res = await getAssociateDefect({ bugid: bugid })
        this.associateList = res.data.pageInfo.tlist
      },
      // 删除内规
      async delInterior(row) {
        const res = await delInnerCommonQxwt({
          bugid: this.formData.bugid,
          innrulids: row.innrulid,
        })
        if (res.code === 1) {
          this.$message.success('删除成功')
          this.fetchInterior(this.formData.bugid)
        }
      },
      // 删除外规
      async delExternal(row) {
        const res = await delOuterCommonQxwt({
          bugid: this.formData.bugid,
          outrulids: row.outrulid,
        })
        if (res.code === 1) {
          this.$message.success('删除成功')
          this.fetchExternal(this.formData.bugid)
        }
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      handleExternalSelected(bugid) {
        this.fetchExternal(bugid)
      },
      async fetchExternal(bugid) {
        const res = await getOuterCommonQxwt({ bugid: bugid })
        this.externalList = res.data.pageInfo.tlist
      },
      handleInteriorSelected() {
        this.fetchInterior(this.formData.bugid)
      },
      async fetchInterior(bugid) {
        const res = await getInnerCommonQxwt({ bugid: bugid })
        this.interiorList = res.data.pageInfo.tlist
      },
      openInterior() {
        if (!this.formData.bugid) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
          return
        }
        this.$refs['interior'].showEdit(this.formData.bugid)
      },
      openExterior() {
        if (!this.formData.bugid) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
          return
        }
        this.$refs['external'].showEdit(this.formData.bugid)
      },
      handleDepartmentSelected(node) {
        this.formData.orgname = node.name
        this.formData.bugdepartment = node.id
      },
      handleExecutorSelected(node) {
        this.formData.discoverperson = node.realname
        this.formData.reporter = node.staffid
      },
      showEdit(title, row, info) {
        this.formData.businessType = info
        this.$set(this.formData, 'businessType', info)
        this.dialogFormVisible = true
        if (row) {
          Object.assign(this.formData, row)
          this.fetchExternal(Number(row.bugid))
          this.fetchInterior(Number(row.bugid))
          this.fetchAssociate(Number(row.bugid))
          this.fetchFile(Number(row.bugid))
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          // console.dir(userInfo)
          // let orgName = userInfo.linkDetp.orgname
          this.formData.orgname = userInfo.linkDetp.orgname
          this.formData.bugdepartment = userInfo.linkDetp.orgid
          this.formData.discoverperson = userInfo.realname
          this.formData.reporter = userInfo.staffid
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
          this.formData.businessType = row.businesstype
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
      async close() {
        if (!this.formData.bugid) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
          return
        }
        let attids = ''
        this.tableData.map((item) => {
          attids += item.attid
          attids += ','
        })
        attids = attids.substring(0, attids.length - 1)
        const data = await defectAdd({
          ...this.formData,
          attids,
        })
        if (data.code == 1) {
          this.$emit('bugdata', [data.data.WorkReport])
          this.dialogFormVisible = false
          this.formData = {
            bugnumber: undefined,
            discovertime: undefined,
            bugcriid: undefined,
            discoverperson: undefined,
            bugsource: '否',
            needreform: '是',
            bugproperty: undefined,
            businessDescription: undefined,
            bugdescripteion: undefined,
            orgname: undefined,
            bugdepartment: undefined,
            reporter: undefined,
            businessType: undefined,
          }
          this.tableData = []
          this.footer = true
          this.associateList = []
          this.externalList = []
          this.interiorList = []
        }
      },
      closeTop() {
        this.dialogFormVisible = false
        this.formData = {
          bugnumber: undefined,
          discovertime: undefined,
          bugcriid: undefined,
          discoverperson: undefined,
          bugsource: '否',
          needreform: '是',
          bugproperty: undefined,
          businessDescription: undefined,
          bugdescripteion: undefined,
          orgname: undefined,
          bugdepartment: undefined,
          reporter: undefined,
          businessType: undefined,
        }
        this.tableData = []
        this.footer = true
        this.associateList = []
        this.externalList = []
        this.interiorList = []
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            console.dir(this.formData)
            const data = await defectAdd({
              ...this.formData,
              attids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              // this.formData = data.data.WorkReport
              this.formData.bugid = data.data.WorkReport.bugid
              this.$forceUpdate()
              console.dir(this.formData)
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            // this.$message.success('添加成功')
            // this.close()
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
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
