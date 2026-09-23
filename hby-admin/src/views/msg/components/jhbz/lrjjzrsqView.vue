<template>
  <!-- 三级单位离任审计 -->
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
          <el-form-item label="季度" prop="jdname">
            <el-select
              v-model="formData.jdname"
              placeholder="请选择季度"
              clearable
              :style="{ width: '100%' }"
              :disabled="disabled"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
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
            <el-table-column align="center" label="姓名" prop="name">
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
            <el-table-column align="center" label="原职务" prop="oldJob" />
            <el-table-column
              align="center"
              label="原行政级别"
              prop="oldLevel"
            ></el-table-column>
            <el-table-column align="center" label="原单位" prop="oldOrg">
              <template #default="{ row }">
                {{ row.oldOrg?.orgname }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              width="120"
              v-if="!disabled"
            >
              <template #default="{ row }">
                <el-button type="text" @click="add(row)">修改</el-button>
                <el-button type="text" @click="handleDelete(row)">
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
            :page-sizes="[5, 10, 20, 50]"
          />
        </el-col>
      </el-form>
    </el-row>
    <lrjjzrsqView ref="edit" @fetchData="fetchData"></lrjjzrsqView>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />

    <!-- <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">
        确定
      </el-button>
    </div> -->

    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
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
    lrjjzrsqSave,
    lrjjzrsqDetail,
    sjdwlrsjlrListDetail,
    sjdwlrsjlrExportData,
  } from '@/oapi/audit/plan'
  import lrjjzrsqView from '@/views/oilAudit/lrjjzr/components/lrjjzrsqView.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    components: { lrjjzrsqView, SelectDepartment, Resubmit },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 1000,
          jdid: '',
        },
        total: 0,
        listLoading: false,
        tableData: [],
        formData: {
          jdname: '', //季度
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdTime: formatDate(new Date().toString()),
          creater: JSON.parse(localStorage.getItem('userInfo')).realname,
          jdid: '',
          glids: '', //关联id
        },
        rules: {
          jdname: [
            {
              required: true,
              message: '请选择季度',
              trigger: 'change',
            },
          ],
          tbrgname: [
            {
              required: true,
              message: '请输入填报单位',
              trigger: 'change',
            },
          ],
        },
        dialogJdVisible: false,
        options: [
          {
            value: '一季度',
            label: '一季度',
          },
          {
            value: '二季度',
            label: '二季度',
          },
          {
            value: '三季度',
            label: '三季度',
          },
          {
            value: '四季度',
            label: '四季度',
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
    mounted() {
      // this.getOption()
    },
    methods: {
      // changeTbdw(val) {
      //   if (val) {
      //     const itemTemp = this.options.find((item) => val === item.value)
      //     this.formData.tbrgname = itemTemp.org.orgname
      //   } else {
      //     this.formData.tbrgname = ''
      //   }
      //   this.fetchData()
      // },
      // async getOption() {
      //   const {
      //     data: { tlist },
      //   } = await lrjjzesqJdList({
      //     pageSize: 9999,
      //     pageNumber: 1,
      //   })
      //   console.log(tlist,'tlist')
      //   this.options = tlist
      // },
      async showEdit(
        row,
        title,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'
        //审批相关参数
        this.fromId = row.jdid
        this.ymFromId = ymFromId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.fromIdcopy = row.jdid
        this.status = status
        if (row) {
          const {
            data: { data },
          } = await lrjjzrsqDetail({ jdid: row.jdid })
          this.formData.jdname = data.jdname
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdTime = data.createdate
          this.formData.creater = data.createname
          this.formData.jdid = data.jdid
          this.queryForm.jdid = data.jdid
          this.getList(row.jdid)
        }
      },
      async getList(jdid) {
        const arr = await sjdwlrsjlrListDetail({ jdid, ...this.queryForm })
        this.tableData = arr.data.tlist
        this.total = arr.data.totalRecord
      },
      close() {
        this.formData = {
          jdname: '', //季度
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdTime: formatDate(new Date().toString()),
          creater: JSON.parse(localStorage.getItem('userInfo')).realname,
          jdid: '',
        }
        this.tableData = []
        this.$bus.$emit('updateMsg', 0)
      },
      add(row) {
        if (row) {
          this.$refs['edit'].showEdit({ id: row.id })
        } else {
          this.$refs['edit'].showEdit()
        }
      },
      async fetchData(res) {
        const arr = JSON.parse(JSON.stringify(this.tableData))
        console.log(res, 'res')
        console.log(this.tableData, 'this.tableData')
        // 子组件传递的格式是 { data: [...], index: ... }
        const data = res.data || res
        this.tableData = [...arr, ...data]
        console.log(this.tableData, ' this.tableData111')
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getList()
      },
      handleDelete(row) {
        console.log('handleDelete', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await sjdwlrsjlrDelete({ ids: row.id })
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
            const res = await lrjjzrsqSave({
              ...this.formData,
              glids: ids.toString(),
            })
            if (res && res.code === 1) {
              // this.close()
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
        this.$refs['edit'].showEdit({ id: row.id }, '详情')
      },
      ymsubmit() {
        try {
          this.$refs['ruleForm'].validate((valid) => {
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
