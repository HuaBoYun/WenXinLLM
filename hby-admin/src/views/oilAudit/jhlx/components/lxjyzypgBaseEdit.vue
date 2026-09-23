<template>
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
            <el-dropdown style="margin-right: 10px" v-if="!disabled">
              <el-button type="success">
                新建
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="add('', '工程')">
                  工程
                </el-dropdown-item>
                <el-dropdown-item @click.native="add('', '财务')">
                  财务
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
            <el-table-column align="center" label="排序" prop="sortNumber" />
            <el-table-column
              align="center"
              label="审计项目名称"
              prop="projectName"
            >
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
            <el-table-column
              align="center"
              label="建议科室"
              prop="departmentName"
            />
            <el-table-column
              align="center"
              label="工程类/财务类"
              prop="projectType"
            ></el-table-column>
            <el-table-column
              align="center"
              label="单位范围"
              prop="unitRange"
            ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              width="120"
              v-if="!disabled"
            >
              <template #default="scope">
                <el-button type="text" @click="edit(scope.row)">修改</el-button>
                <el-button type="text" @click="handleDelete(scope.row)">
                  删除
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
        </el-col>
      </el-form>
    </el-row>
    <lxjyzypgEdit ref="edit" @selected="selected"></lxjyzypgEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">确定</el-button>
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
  import {
    lxjyzypgBaseSave,
    lxjyzypgBaseDetail,
    getLxjyzypgBaseRelateList,
    deleteLxjyzypgBaseRelateList,
  } from '@/oapi/audit/plan'
  import { downloadFile } from '@/utils/otherUtils'
  import { exportList } from '@/api/oilAudit/jhgl/lxjyzypg'
  import lxjyzypgEdit from '@/views/oilAudit/jhlx/components/lxjyzypgEdit'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    components: { lxjyzypgEdit, SelectDepartment, typeView, ProcessList },
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        api: '/oiaudit/plan/project/evaluation/import',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 9999,
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
          itemType: '',
        },
        rules: {
          tbname: [
            {
              required: true,
              message: '请输入编号',
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
        options: [
          {
            value: '工程',
            label: '工程',
          },
          {
            value: '财务',
            label: '财务',
          },
        ],
        disabled: false,
        editId: '',
      }
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
          } = await lxjyzypgBaseDetail({ tbid: row.tbid })
          this.formData.tbname = data.tbname
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid
          this.getTableList()
          // this.fetchData()
        }
      },

      close() {
        this.formData = {
          tbname: '', //季度
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          itemType: '',
        }
        this.tableData = []
        this.dialogJdVisible = false
        this.editId = ''
        this.$emit('fetchData')
      },
      add(row, type) {
        this.$refs['edit'].showEdit('add', row, type)
      },
      edit(row) {
        this.$refs['edit'].showEdit('edit', row)
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
      async handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getTableList()
        // this.fetchData()
      },
      async handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getTableList()
        // this.fetchData()
      },
      async getTableList() {
        const arr = await getLxjyzypgBaseRelateList({
          tbid: this.formData.tbid,
          ...this.queryForm,
        })
        this.tableData = arr.data.tlist
        this.total = arr.data.totalRecord
      },
      handleDelete(row) {
        console.log('handleDelete', row)
        console.log('handleDelete', this.formData.tbid)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          if(this.formData.tbid) {
            const res = await deleteLxjyzypgBaseRelateList({ id: row.id })
            if (res.code == 1) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')
              await this.getTableList()
            }
          } else {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            let list = this.tableData
            list = list.filter((item) => item.id != row.id)
            this.tableData = list
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
            this.loading = true
            const ids = this.tableData.map((res) => res.id)
            const res = await lxjyzypgBaseSave({
              ...this.formData,
              glids: ids.toString(),
            })
            if (res && res.code === 1) {
              this.editId = res.data.data.tbid
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
              this.$emit('fetchData')
              this.close()
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
            this.loading = false
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
        this.listLoading = true
        const res = await exportList({
          ...this.queryForm,
          tbid: this.formData.tbid,
        })
        downloadFile(res, '立项建议专业评估.xlsx')
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit('detail', row)
      },
      selected(val) {
        const arr = this.tableData.filter((res) => res.id != val.id)
        this.tableData = [...arr, val]
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(181, this.editId)
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
