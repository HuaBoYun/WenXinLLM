<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="联查报销单"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width=""
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="分摊" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="待摊" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="合计金额" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="单据编号" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="报销人部门" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="报销人" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="收支项目" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="币种" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="生效状态" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="交易类型" />
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item prop="organization">
            <el-date-picker
              v-model="formData.name"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="单据日期开始日期"
              end-placeholder="单据日期结束日期"
              placeholder="单据日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary">查询</el-button>
          <el-button type="primary">重置</el-button>
        </el-col>

        <el-col :span="24">
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="序号"
              prop="qdcode"
              width="100"
            ></el-table-column>
            <el-table-column
              align="center"
              label="报销单位"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="合计金额"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              align="center"
              label="单据编号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="单据日期"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="报销人部门"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="报销人"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="收支项目"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="币种"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="事由"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              align="center"
              label="交易类型"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button>修改</el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button>删除</el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
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
    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
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
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    data() {
      return {
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
        list: [],
        selectionLength: [],
        formData: {
          organization: '',
          name: '',
          code: '',
          province: '',
          city: '',
          phone: '',
          address: '',
          status: '',
          creator: '',
          createDate: '',
          updateCreator: '',
          updateDate: '',
        },
        rules: {
          organization: [
            {
              required: true,
              message: '请输入所属组织',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入金融机构名称',
              trigger: 'blur',
            },
          ],
          code: [
            {
              required: true,
              message: '请输入金融机构编码',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择启用状态',
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
        multipleSelection: [],
        isUnfoldAuditShow: false,
        total: 0,
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

      addRow() {
        this.tableData.push({
          id: new Date().getTime(),
          groupName: '',
          financialOrganization: '',
          commercialNumber: '',
          channelNumber: '',
          userName: '',
          createName: '',
          createDate: '',
          updateName: '',
          updateDate: '',
        })
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
        this.$refs['fyhjrjgBaseEdit'].showEdit('add', row, type)
      },
      edit(row) {
        this.$refs['fyhjrjgBaseEdit'].showEdit('edit', row)
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
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteLxjyzypgBaseRelateList({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            let list = this.tableData
            list = list.filter((item) => item.id != row.id)
            this.tableData = list
            // await this.fetchData()
            await this.getTableList()
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
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
              this.editId = res.data.data.tbid
              this.$message({
                message: '保存成功！',
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
        this.listLoading = false
        const res = await exportList({
          ...this.queryForm,
          tbid: this.formData.tbid,
        })
        downloadFile(res, '立项建议专业评估.xlsx')
        this.listLoading = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
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
  .show_line_box {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  .title_l {
    width: 100px;
    text-align: center;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px solid #cccccc6e;
  }
  .dialog-add {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #438560;
    margin-bottom: 20px;
  }
  .required-star {
    color: red;
  }
</style>
