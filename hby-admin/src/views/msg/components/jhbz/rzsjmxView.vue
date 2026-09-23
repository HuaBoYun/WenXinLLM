<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="tbname">
            <el-input
              v-model="formData.tbname"
              placeholder="请输入编号"
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

        <el-col :span="12" style="height: 47px">
          <el-form-item label="创建人">
            <el-input
              v-model="formData.createname"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
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
              @click="add()"
              v-if="!disabled"
              style="margin-right: 20px"
            >
              新增
            </el-button>
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
            >
              <el-button type="success" v-if="!disabled">导入</el-button>
            </el-upload>
            <el-button
              type="success"
              @click="handleExport"
              v-if="title != '新增'"
              style="margin-left: 20px"
            >
              导出
            </el-button>
          </div>
          <el-table v-loading="listLoading" :data="tableData">
            <el-table-column align="center" label="项目名称" prop="projectName">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.projectName }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="审计情况" prop="auditInfo" />
            <el-table-column
              align="center"
              label="审计时间"
              prop="auditTime"
            ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              width="120"
              v-if="!disabled"
            >
              <template #default="scope">
                <el-button type="text" @click="add(scope.row, scope.$index)">
                  修改
                </el-button>
                <el-button type="text" @click="handleDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="pagination"
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </el-col>
      </el-form>
    </el-row>
    <rzsjmxView
      ref="edit"
      @fetchData="fetchData"
      @selected="selected"
    ></rzsjmxView>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <div
      slot="footer"
      v-if="!disabled"
      style="text-align: right; margin-top: 10px"
    >
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    sjdwlrsjlrList,
    sjdwlrsjlrDelete,
    lrjjzesqJdList,
    rzsjmxBaseSave,
    rzsjmxBaseDetail,
    rzsjmxBaseRelateListDetail,
    rzsjmxBaseRelateListDelete,
    lrjjzrsqDetail,
    sjdwlrsjlrListDetail,
    sjdwlrsjlrExportData,
  } from '@/oapi/audit/plan'
  import { exportList } from '@/api/oilAudit/jhgl/rzsjmx'
  import rzsjmxView from '@/views/oilAudit/lrjjzr/components/rzsjmxView.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    components: {
      rzsjmxView,
      SelectDepartment,
      Resubmit: () => import('@/views/msg/components/options/Resubmit.vue'),
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/interim/audit/import',
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
          tbname: '', //季度
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          glids: '', //关联id
          projectType: '',
          remarks: '',
        },
        rules: {
          jdname: [
            {
              required: true,
              message: '请选择编号',
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
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        btnLoading: false,
      }
    },
    mounted() {},
    methods: {
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'
        //流程相关
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (row) {
          const {
            data: { data },
          } = await rzsjmxBaseDetail({ tbid: row })
          this.formData.tbname = data.tbname
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid

          const arr = await rzsjmxBaseRelateListDetail({
            tbid: row,
            ...this.queryForm,
          })
          this.tableData = arr.data.tlist
          this.total = arr.data.totalRecord
        }
      },
      close() {
        this.formData = {
          tbname: '',
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          projectType: '',
          remarks: '',
        }
        this.tableData = []
        this.dialogJdVisible = false
        this.$bus.$emit('updateMsg', 0)
      },
      setType(e) {
        this.formData.projectType = e.auditType
        this.$forceUpdate()
      },
      add(row, i) {
        if (row) {
          this.$refs['edit'].showEdit({ id: row.id, index: i + 1 })
        }
        this.$refs['edit'].showEdit()
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

        // const arr = await sjdwlrsjlrListDetail({tbid:this.formData.tbid})
        // this.tableData=arr.data.tlist
      },
      async handleSizeChange(val) {
        this.queryForm.pageSize = val
        const arr = await rzsjmxBaseRelateListDetail({
          tbid: this.formData.tbid,
          ...this.queryForm,
        })
        this.tableData = arr.data.tlist
        this.total = arr.data.totalRecord
      },
      async handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        const arr = await rzsjmxBaseRelateListDetail({
          tbid: this.formData.tbid,
          ...this.queryForm,
        })
        this.tableData = arr.data.tlist
        this.total = arr.data.totalRecord
      },
      handleDelete(row) {
        console.log('handleDelete', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await rzsjmxBaseRelateListDelete({ ids: row.id })
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
        this.formData.tbrgname = val.label
        this.formData.tbrgid = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const ids = this.tableData.map((res) => res.id)
            const res = await rzsjmxBaseSave({
              ...this.formData,
              glids: ids.toString(),
            })
            if (res && res.code === 1) {
              this.$emit('fetchData')
              this.$message({
                message: '成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '失败',
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
        const data = await exportList({
          ...this.queryForm,
          tbid: this.formData.tbid,
        })
        // downloadFile(res, '立项建议专业评估.xlsx')
        let filename = '任中审计明细.xlsx'
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
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, '详情')
      },
      selected(val) {
        const arr = this.tableData.filter((res) => res.id != val.id)
        this.tableData = [...arr, val]
      },
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
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
