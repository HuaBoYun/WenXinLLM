<template>
  <!-- 三级单位离任审计 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="新增"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="code">
            <el-input
              v-model="formData.code"
              placeholder="请输入编号"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批次" prop="batc">
            <el-input
              v-model="formData.batc"
              placeholder="请输入批次"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="tbrgname">
            <el-input
              v-model="formData.tbrgname"
              disabled
              placeholder="请输入填报单位"
              :style="{ width: '80%' }"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              :disabled="disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input
              v-model="formData.createname"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input
              v-model="formData.createdate"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="margin-bottom: 5px; display: flex; justify-content: right"
          >
            <el-button
              type="success"
              @click="handleExport()"
              style="margin-right: 20px"
            >
              导出
            </el-button>
            <el-button
              type="success"
              @click="add()"
              v-if="!disabled"
              style="margin-right: 20px"
            >
              新增
            </el-button>
            <el-button
              type="success"
              @click="push1()"
              style="margin-right: 20px"
            >
              下发科室负责人
            </el-button>
            <el-button
              type="success"
              @click="push2()"
              style="margin-right: 20px"
            >
              下发项目组人员
            </el-button>
            <el-button type="success" @click="push3()" v-if="!disabled">
              批量增加助审
            </el-button>
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
            >
              <el-button type="success" v-if="!disabled">导入</el-button>
            </el-upload> -->
          </div>
          <el-table
            v-loading="listLoading"
            :data="tableData1"
            @selection-change="handleSelectAll"
            @select="handleSelection"
            ref="multipleTable"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              align="center"
              label="编号"
              prop="code"
            ></el-table-column>
            <el-table-column align="center" label="项目名称" prop="name">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.name }}
                </el-button>
              </template>
            </el-table-column>
            <!-- <el-table-column
              align="center"
              label="实施单位"
              prop="exePhraseUnit"
            /> -->
            <el-table-column
              align="center"
              label="主审"
              prop="approver"
            />
            <el-table-column
              align="center"
              label="助审"
              prop="assistApprover"
            />
            <el-table-column
              align="center"
              label="被审计单位"
              prop="auditUnit"
            ></el-table-column>
            <el-table-column
              align="center"
              label="小组"
              prop="auditGroup"
            ></el-table-column>
            <el-table-column align="center" label="状态" prop="xfstatus">
              <template #default="{ row }">
                {{
                  row.xfstatus == '1'
                    ? '已上报'
                    : row.xfstatus == '2'
                    ? '退回'
                    : ''
                }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              width="120"
              v-if="!disabled"
            >
              <template #default="scope">
                <el-button
                  type="text"
                  @click="handleEdit(scope.row, scope.$index)"
                >
                  修改
                </el-button>
                <el-button type="text" @click="handleDelete(scope.row)">
                  删除
                </el-button>
                <el-button
                  v-if="scope.row.gljhxmlx == '23'"
                  type="text"
                  @click="handleView(scope.row)"
                >
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- <el-pagination
            class="pagination"
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          /> -->
          <el-col :span="24" style="margin-top: 15px">
            <el-divider>文件上传</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="!disabled">
              <!-- <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :file-list="tableData"
              >
                <el-button type="success">上传</el-button>
              </el-upload> -->
              <el-upload
                style="text-align: right; margin-bottom: 5px"
                class="upload-demo"
                :show-file-list="false"
                action=""
                :headers="headers"
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :file-list="fileList"
                :before-upload="handleBeforeUpload"
                :multiple="true"
              >
                <div v-if="!disabled" style="margin-right: 10px">
                  <el-button type="success">点击上传</el-button>
                </div>
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDowns(row)"
                    :disabled="false"
                  >
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handlePreviewFile(row)"
                    :disabled="false"
                  >
                    预览
                  </el-button>
                  <el-button type="text" @click="handleDeleteFile(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-col>
      </el-form>
    </el-row>
    <cwsjxmapbEdit ref="edit" @getNewData="handleAdd"></cwsjxmapbEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />
    <project-manage2 @projectManage="getChildlistPro2" ref="manage2" />
    <project-manage3 @projectManage="getChildlistPro3" ref="manage3" />
    <cwsjxmapbOutView ref="cwsjxmapbOutView" type="proname" />
    <ProcessList ref="process" @fetchData="close" />
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-if="!disabled">确定</el-button>
      <el-button type="primary" @click="handleBack">退回</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { download } from '@/oapi/audit/report'
  import {
    fundAuditOutProjectSaveOrUpdate,
    fundAuditOutProjectDetail,
    fundAuditProjectDelete,
    xfksry,
    xfxmzry,
    xfzsrys,
    fundAuditProjectXmzsbth,
    fundFileList,
    fundFileDel,
    fundAuditOutProjectExport,
  } from '@/api/monitor/question'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import cwsjxmapbEdit from '@/views/oilAudit/jhlx/components/cwsjxmapbEdit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage1 from '@/components/selectPerson.vue'
  import projectManage2 from '@/components/selectPerson.vue'
  import projectManage3 from '@/components/selectPerson.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import cwsjxmapbOutView from './cwsjxmapbOutView.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      cwsjxmapbEdit,
      SelectDepartment,
      projectManage1,
      projectManage2,
      projectManage3,
      cwsjxmapbOutView,
      ProcessList,
    },
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        listLoading: false,
        tableData: [],
        tableData1: [],
        formData: {
          batc: '', //批次
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDate(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          gljson: '',
          code: '',
        },
        rules: {
          batc: [
            {
              required: true,
              message: '请输入批次',
              trigger: 'blur',
            },
          ],
          tbrgname: [
            {
              required: true,
              message: '请输入填报单位',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        select: [],
        rows: {},
        editId: '',
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      async showEdit(row, title) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'
        this.rows = row
        if (row) {
          this.editId = row.tbid
          const {
            data: { data },
          } = await fundAuditOutProjectDetail({ tbid: row.tbid })
          this.formData.batc = data.batc
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid
          this.formData.code = data.code

          this.tableData1 = data.list
          // this.fetchData()

          let res = await fundFileList({ tbid: row.tbid })
          this.tableData = res.data.list
        }
      },
      close() {
        this.formData = {
          batc: '', //批次
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDate(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          gljson: '',
          code: '',
        }
        this.tableData = []
        this.tableData1 = []
        this.select = []
        this.dialogJdVisible = false
        this.editId = ''
        this.$emit('fetchData')
      },
      add(row, i) {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleEdit(row, i) {
        this.$refs['edit'].showEdit({ id: row.id, index: i + 1 }, 'edit')
      },
      handleBack(row) {
        if (this.select.length > 0) {
          let ids = this.select.map((k) => k.id)
          this.$baseConfirm('你确定要退回当前项吗', null, () => {
            fundAuditProjectXmzsbth({ ids: ids.join() }).then((res) => {
              if (res.code == 1) {
                this.$baseMessage('成功', 'success', 'vab-hey-message-success')
                this.fetchData()
                this.handleBackXiafa()
              }
            })
          })
        } else {
          this.$message({
            message: '请选择需要退回的项目',
            type: 'error',
          })
        }
      },
      handleBackXiafa() {
        const aRR = []
        this.select.forEach((res) => {
          if (res.xfsmzryids) {
            const info = res.xfsmzryids.split(',')
            info.forEach((item) => {
              aRR.push({
                formId: res.id,
                distributionTitle: res.name,
                isread: 0,
                reciver: item,
                moduleType: 'yqns',
              })
            })
          }
        })
        //下发通知
        xiafaListNew({
          tableId: '1466',
          jsondistribution: JSON.stringify(aRR),
        }).then((res) => {
          if (res.msg == '成功') {
            this.select = []
            this.fetchData()
          }
        })
      },
      handleAdd(data) {
        const arr = JSON.parse(JSON.stringify(this.tableData1))
        if (!data) {
          this.tableData1 = [...arr]
          return false
        }
        if (data?.index) {
          arr[data.index - 1] = data.data[0]
          this.tableData1 = [...arr]
        } else {
          this.tableData1 = [...arr, ...data.data]
        }
      },
      async fetchData() {
        const {
          data: { data },
        } = await fundAuditOutProjectDetail({ tbid: this.rows.tbid })
        this.formData.batc = data.batc
        this.formData.tbrgname = data.tbrgname
        this.formData.tbrgid = data.tbrgid
        this.formData.createdate = data.createdate
        this.formData.createname = data.createname
        this.formData.tbid = data.tbid
        this.formData.code = data.code

        this.tableData1 = data.list

        // const arr = JSON.parse(JSON.stringify(this.tableData))
        // if (!data) {
        //   this.tableData = [...arr]
        //   return false
        // }
        // if (data?.index) {
        //   arr[data.index - 1] = data.data[0]
        //   this.tableData = [...arr]
        // } else {
        //   this.tableData = [...arr, ...data.data]
        // }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await fundAuditProjectDelete({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            let list = this.tableData1
            list = list.filter((item) => item.id != row.id)
            this.tableData1 = list
            // await this.fetchData()
          }
        })
      },
      handleView(row) {
        this.$refs['cwsjxmapbOutView'].showEdit({ data: row })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbrgname = val.label
        this.formData.tbrgid = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)

            delete this.formData.createdate
            const ids = this.tableData1.map((res) => res.id)
            const res = await fundAuditOutProjectSaveOrUpdate({
              ...this.formData,
              gljson: ids.toString(),
              attids,
            })
            if (res && res.code === 1) {
              this.editId = res.data.data.tbid
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },
      // handleSuccess(response) {
      //   if (response.code == 1) {
      //     this.tableData = [...this.tableData, ...response.data.data]
      //     this.$baseMessage('导入成功', 'success')
      //   } else {
      //     this.$baseMessage(response.msg, 'error')
      //   }
      // },
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await fundAuditOutProjectExport({
          ...this.queryForm,
          tbid: this.formData.tbid,
          idList: ids.toString(),
        })
        let fileName = '财务审计项目安排表'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, 'detail')
      },
      // handleSelection(val) {
      //   if (val.length > 1) {
      //     let del = val.shift()
      //     this.$refs.multipleTable.toggleRowSelection(del, false)
      //   }
      //   this.select = val
      // },
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
      push3() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage3'].showEdit()
      },
      async getChildlistPro1(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()
        const res = await xfksry({ ids: arr1, names: names1, ryids: ids1 })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '下发成功!',
          })
        }

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
          tableId: '579594840944709',
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
      async getChildlistPro2(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()
        const res = await xfxmzry({ ids: arr1, names: names1, ryids: ids1 })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '下发成功!',
          })
        }

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
          tableId: '1466',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.select = []
            this.fetchData()
            this.multipleSelection = []
          }
        })
      },
      async getChildlistPro3(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()

        const res = await xfzsrys({ ids: arr1, names: names1, ryids: ids1 })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '新增成功!',
          })
          this.select = []
          this.fetchData()
          this.multipleSelection = []
        }
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

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDeleteFile(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await fundFileDel({ attid: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(124, this.editId)
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }

        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
