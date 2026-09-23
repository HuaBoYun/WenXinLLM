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
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批次" prop="batc">
            <el-input
              v-model="formData.batc"
              placeholder="请输入批次"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="tbrgname">
            <el-input
              v-model="formData.tbrgname"
              disabled
              placeholder="请输入填报单位"
              :style="{ width: '100%' }"
            />
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
            <el-button type="success" @click="push1(true)" v-if="anstatus == 1">分配审理科人员</el-button>
            <el-button type="success" @click="push1(false)" v-if="anstatus == 0">分配督导人员</el-button>
          </div>
          <el-table
            v-loading="listLoading"
            :data="tableData"
            @select-all="handleSelectAll"
            @select="handleSelection"
            ref="multipleTable"
          >
            <el-table-column type="selection" width="55"></el-table-column>
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
            <el-table-column
              align="center"
              label="实施单位"
              prop="exePhraseUnit"
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
            <el-table-column
              align="center"
              label="审理科人员"
              prop="fpslkryname"
              v-if="anstatus == 1"
            ></el-table-column>
            <el-table-column
              align="center"
              label="督导人员"
              prop="fpksrynames"
              v-if="anstatus == 0"
            ></el-table-column>
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
        </el-col>
      </el-form>
    </el-row>
    <cwsjxmapbEdit ref="edit" @getNewData="fetchData"></cwsjxmapbEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />
    <project-manage2 @projectManage="getChildlistPro2" ref="manage2" />

    <div slot="footer" v-if="!disabled">
      <el-button type="primary" @click="close">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { sjdwlrsjlrExportData } from '@/oapi/audit/plan'
  import {
    fundAuditOutProjectSaveOrUpdate,
    fundAuditOutProjectDetail,
    fundAuditProjectList,
    fundAuditProjectDelete,
    xfksry,
    cwfpddksry,
    cwfpslkry,
  } from '@/api/monitor/question'
  import cwsjxmapbEdit from '@/views/oilAudit/jhlx/components/cwsjxmapbEdit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage1 from '@/components/danxuanPerson.vue'
  import projectManage2 from '@/components/selectPerson.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    components: {
      cwsjxmapbEdit,
      SelectDepartment,
      projectManage1,
      projectManage2,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        listLoading: false,
        tableData: [],
        formData: {
          batc: '', //批次
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDate(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          gljson: '',
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
        isSlk: false,
        select: [],
        editId: '',
        anstatus: 0,
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
        if (row) {
          this.editId = row.tbid
          const {
            data: { data },
          } = await fundAuditOutProjectDetail({ tbid: row.tbid })
          this.anstatus = data.anstatus
          this.formData.batc = data.batc
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid
          this.formData.code = data.code
          this.tableData = data.list.map((v) => {
            v.fpksrynames = v.fpksrynames || ''
            return v
          })
          // this.fetchData()
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
        }
        this.tableData = []
        this.select = []
        this.dialogJdVisible = false
      },
      add(row, i) {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleEdit(row, i) {
        this.$refs['edit'].showEdit({ id: row.id, index: i + 1 }, 'edit')
      },
      async fetchData(data) {
        const arr = JSON.parse(JSON.stringify(this.tableData))
        if (!data) {
          this.tableData = [...arr]
          return false
        }
        if (data?.index) {
          arr[data.index - 1] = data.data[0]
          this.tableData = [...arr]
        } else {
          this.tableData = [...arr, ...data.data]
        }
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
            let list = this.tableData
            list = list.filter((item) => item.id != row.id)
            this.tableData = list
            await this.fetchData()
          }
        })
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
            delete this.formData.createdate
            const ids = this.tableData.map((res) => res.id)
            const res = await fundAuditOutProjectSaveOrUpdate({
              ...this.formData,
              gljson: ids.toString(),
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
      async handleExport() {
        const data = await sjdwlrsjlrExportData({
          ...this.queryForm,
          jdid: this.formData.jdid,
        })
        let fileName = '三级单位离任审计表'
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
      push1(type) {
        this.isSlk = type
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
      async getChildlistPro1(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()
        // 审理科人员
        if(this.isSlk) {
          const res = await cwfpslkry({ ids: arr1, fpslkryname: names1, fpslkryid: ids1 })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '下发成功!',
            })
            // this.xiafa(val)
            this.select = []
            this.fetchData()
            this.multipleSelection = []
            const {
              data: { data },
            } = await fundAuditOutProjectDetail({ tbid: this.editId })
            this.tableData = data.list.map((v) => {
              v.fpksrynames = v.fpksrynames || ''
              return v
            })
          }
        } else {
          const res = await cwfpddksry({ ids: arr1, names: names1, ryids: ids1 })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '下发成功!',
            })
            this.xiafa(val)
            const {
              data: { data },
            } = await fundAuditOutProjectDetail({ tbid: this.editId })
            this.tableData = data.list.map((v) => {
              v.fpksrynames = v.fpksrynames || ''
              return v
            })
          }
        }
      },
      async getChildlistPro2(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()
        const res = await cwfpddksry({ ids: arr1, names: names1, ryids: ids1 })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '下发成功!',
          })
          this.xiafa(val)
          const {
            data: { data },
          } = await fundAuditOutProjectDetail({ tbid: this.editId })
          this.tableData = data.list.map((v) => {
            v.fpksrynames = v.fpksrynames || ''
            return v
          })
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
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
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
