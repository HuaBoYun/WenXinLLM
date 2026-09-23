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
        <!-- <el-col :span="12" style="height: 47px">
          <el-form-item label="项目类型" prop="itemType">
            <el-input
              v-model="formData.itemType"
              disabled
              placeholder="请选择"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="disabled"
              @click.native="showtypeView"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
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
    <lxjyzypgEdit
      ref="edit"
      @fetchData="fetchData"
      @selected="selected"
    ></lxjyzypgEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <typeView ref="typeView" @submit="setType" />
    <div
      slot="footer"
      v-if="!disabled"
      style="text-align: right; margin-top: 10px"
    >
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button type="primary" @click="ymsubmit" :disable="btnLoading">
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
    lxjyzypgBaseSave,
    lrjjzrsqDetail,
    lxjyzypgBaseDetail,
    sjdwlrsjlrListDetail,
    sjdwlrsjlrExportData,
    getLxjyzypgBaseRelateList,
    deleteLxjyzypgBaseRelateList,
  } from '@/oapi/audit/plan'
  import { exportList } from '@/api/oilAudit/jhgl/lxjyzypg'
  import { downloadFile } from '@/utils/otherUtils'
  import lxjyzypgEdit from '@/views/oilAudit/jhlx/components/lxjyzypgEdit'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    components: {
      lxjyzypgEdit,
      SelectDepartment,
      typeView,
      Resubmit: () => import('@/views/msg/components/options/Resubmit.vue'),
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/project/evaluation/import',
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
          } = await lxjyzypgBaseDetail({ tbid: row })
          this.formData.tbname = data.tbname
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid

          const arr = await getLxjyzypgBaseRelateList({ tbid: row })
          this.tableData = arr.data.tlist
        }
      },
      setType(e) {
        this.formData.itemType = e.auditType
        this.$forceUpdate()
      },
      showtypeView() {
        this.$refs['typeView'].showEdit()
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
        this.$bus.$emit('updateMsg', 0)
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

        // const arr = await sjdwlrsjlrListDetail({jdid:this.formData.jdid})
        // this.tableData=arr.data.tlist
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
        console.log('handleDelete', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteLxjyzypgBaseRelateList({ id: row.id })
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
            const res = await lxjyzypgBaseSave({
              ...this.formData,
              glids: ids.toString(),
            })
            if (res && res.code === 1) {
              // this.close()
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
