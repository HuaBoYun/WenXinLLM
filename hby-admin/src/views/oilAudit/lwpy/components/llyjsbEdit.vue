<template>
  <!-- 理论研究上报 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
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
              v-model="formData.creater"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input
              v-model="formData.createdTime"
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
            <el-button type="success" @click="add()" v-if="!disabled">
              新增
            </el-button>
          </div>
          <el-table v-loading="listLoading" :data="tableData">
            <el-table-column
              align="center"
              label="序号"
              type="index"
              width="50"
            />
            <el-table-column align="center" label="研究方向" prop="direction">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.direction }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="组长" prop="zzname" />
            <el-table-column
              align="center"
              label="研究人员"
              prop="yjname"
            ></el-table-column>
            <el-table-column
              align="center"
              label="撰写人"
              prop="zxrname"
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
          <!-- <el-pagination class="pagination" background :current-page="queryForm.pageNumber" :layout="layout"
            :page-size="queryForm.pageSize" :total="total" @current-change="handleCurrentChange"
            @size-change="handleSizeChange" /> -->
        </el-col>
      </el-form>
    </el-row>
    <addllyjsbEdit ref="edit" @fetchData="fetchData"></addllyjsbEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
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
    saveLlyjsbData,
    getLlyjsbzbList,
    getLlyjsbData,
  } from '@/oapi/audit/lwpy'
  import addllyjsbEdit from '@/views/oilAudit/lwpy/components/addllyjsbEdit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    name: 'llyjsbEdit',
    components: { addllyjsbEdit, SelectDepartment, ProcessList },
    data() {
      return {
        loading: false,
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
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdTime: formatDate(new Date().toString()),
          creater: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          glids: '', //关联id
        },
        rules: {
          tbrgname: [
            {
              required: true,
              message: '请选择填报单位',
              trigger: 'change',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        title: '',
        editId: '',
      }
    },
    methods: {
      async showEdit(title, row) {
        this.dialogJdVisible = true
        this.disabled = title == 'detail'
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = true
        } else {
          this.title = '新增'
          this.disabled = false
          this.formData = { ...this.formData }
        }
        if (row) {
          this.editId = row.tbid
          const {
            data: { data },
          } = await getLlyjsbData({ tbid: row.tbid })
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdTime = data.createdate
          this.formData.creater = data.createname
          this.formData.tbid = data.tbid

          const arr = await getLlyjsbzbList({ tbid: row.tbid })
          this.tableData = arr.data.data
        }
      },
      close() {
        this.formData = {
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdTime: formatDate(new Date().toString()),
          creater: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
        }
        this.tableData = []
        this.dialogJdVisible = false
        this.title = ''
        this.$refs['ruleForm'].resetFields()
        this.editId = ''
        this.$emit('fetchData')
      },
      add(row, i) {
        if (row) {
          this.$refs['edit'].showEdit({ id: row.chid, index: i + 1 }, '编辑')
        } else {
          this.$refs['edit'].showEdit(null, '新增')
        }
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.chid }, '详情')
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
          this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          let list = this.tableData
          list = list.filter((item) => item.chid != row.chid)
          this.tableData = list
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
            const ids = this.tableData.map((res) => res.chid)
            const res = await saveLlyjsbData({
              ...this.formData,
              glids: ids.toString(),
            })
            if (res && res.code === 1) {
              this.editId = res.data.data.tbid
              this.formData.tbid = res.data.data.tbid
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
              this.$emit('fetchData')
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
      handleApproval() {
        //提交审批
        this.$refs['process'].save(190, this.editId)
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
