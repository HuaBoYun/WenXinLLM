<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="通用报销单"
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
          <el-form-item label="报销单位" prop="organization">
            <el-input
              v-model="formData.organization"
              placeholder="报销单位"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交易类型" prop="name">
            <el-input v-model="formData.name" placeholder="交易类型" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="单据编号" prop="code">
            <el-input
              v-model="formData.code"
              placeholder="交易类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据日期" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="单据日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="币种"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报销事由" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="报销事由"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="费用承担单位" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="费用承担单位"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="费用承担部门" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="费用承担部门"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="收支项目" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="收支项目"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报销人单位" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="报销人单位"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报销人部门" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="报销人部门"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报销人" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="报销人"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="个人银行账户" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="个人银行账户"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="支付组织" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="支付组织"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位银行" prop="province">
            <el-input
              v-model="formData.province"
              placeholder="单位银行"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <div class="dialog-add">
            <div>
              <span>费用明细 ({{ tableData.length }})</span>
              <span style="margin-left: 18px">
                共 {{ selectionLength.length }}条，合计 {{ total }} 元
              </span>
            </div>
            <div>
              <el-button type="primary" @click="addRow">新增</el-button>
              <el-button type="primary">票夹</el-button>
              <el-button type="primary">票夹上传</el-button>
            </div>
          </div>
          <el-table
            :data="tableData"
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>
            <el-table-column label="收支项目">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="税率(%)">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="税金金额">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="含税金额">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template slot-scope="scope">
                <el-button
                  @click="handleClickDelete(scope.$index, scope.row)"
                  type="text"
                  size="small"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <div class="dialog-add">
            <div>
              <span>收款信息 ({{ tableData.length }})</span>
              <span style="margin-left: 18px">
                共 {{ selectionLength.length }}条，合计 {{ total }} 元
              </span>
            </div>
            <div>
              <el-button type="primary" @click="addRow">新增</el-button>
            </div>
          </div>
          <el-table
            :data="tableData"
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>

            <el-table-column label="收款对象">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="收款人">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="客户">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="供应商">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="个人银行账户">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="客商银行账户">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="结算方式">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="支付金额">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="散户">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="币种">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template slot-scope="scope">
                <el-button
                  @click="handleClickDelete(scope.$index, scope.row)"
                  type="text"
                  size="small"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <div class="dialog-add">
            <div>
              <span>分期付款明细 ({{ tableData.length }})</span>
              <span style="margin-left: 18px">
                共 {{ selectionLength.length }}条，合计 {{ total }} 元
              </span>
            </div>
            <div>
              <el-button type="primary" @click="addRow">新增</el-button>
            </div>
          </div>
          <el-table
            :data="tableData"
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>
            <el-table-column label="支付报销单编号">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="此次分期金额">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="此次分期余额">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="分期分款时间">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template slot-scope="scope">
                <el-button
                  @click="handleClickDelete(scope.$index, scope.row)"
                  type="text"
                  size="small"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
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
