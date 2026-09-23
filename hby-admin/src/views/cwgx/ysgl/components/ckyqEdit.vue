<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="催款语气"
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
          <el-form-item label="财务组织" prop="serviceCode">
            <el-input v-model="formData.serviceCode" placeholder="财务组织" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编码" prop="ipAdress">
            <el-input v-model="formData.ipAdress" placeholder="编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称" prop="port">
            <el-input v-model="formData.prot" placeholder="名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="port">
            <el-input v-model="formData.prot" placeholder="类型" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="port">
            <el-input v-model="formData.prot" placeholder="币种" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div class="dialog-add">
            <div>
              <span>催款语气单行 ({{ tableData.length }})</span>
              <span style="margin-left: 18px">
                已选 {{ selectionLength.length }}条
              </span>
            </div>
            <div>
              <el-button type="primary" @click="deleteSelection">
                删除
              </el-button>
              <el-button type="primary" @click="addRow">新增</el-button>
              <el-button type="primary" @click="copyRow">复制</el-button>
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
            <el-table-column label="上限">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createName"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="下限">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createDate"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column width="120">
              <template slot="header">
                <span class="required-star">*</span>
                催款用语
              </template>
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.commercialNumber"
                  autocomplete="off"
                ></el-input>
              </template>
            </el-table-column>

            <el-table-column label="备注" width="180">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.createDate"
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
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    components: {},
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
        internetbankData: [],
        interfaceList: [],
        selectionLength: [],
        formData: {
          serviceCode: '',
          ipAdress: '',
          port: '',
          servletService: '',
          overtime: '',
          initiateMode: '',
          weighted: '',
          internetbankType: '',
          connectionTest: '',
        },
        rules: {
          serviceCode: [
            {
              required: true,
              message: '请输入服务器编码',
              trigger: 'blur',
            },
          ],
          ipAdress: [
            {
              required: true,
              message: '请输入ip地址',
              trigger: 'blur',
            },
          ],
          port: [
            {
              required: true,
              message: '请输入端口',
              trigger: 'blur',
            },
          ],
          servletService: [
            {
              required: true,
              message: '请输入servlet服务',
              trigger: 'blur',
            },
          ],
          overtime: [
            {
              required: true,
              message: '请输入超时(秒)',
              trigger: 'blur',
            },
          ],
          initiateMode: [
            {
              required: true,
              message: '请选择启用状态',
              trigger: 'blur',
            },
          ],
          weighted: [
            {
              required: true,
              message: '请输入权重值',
              trigger: 'blur',
            },
          ],
          internetbankType: [
            {
              required: true,
              message: '请选择网银服务器类型',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        initiateModeList: [
          {
            value: '1',
            label: '停用',
          },
          {
            value: '2',
            label: '启用',
          },
        ],
        internetbankList: [
          {
            value: '1',
            label: '银企联私有版部署',
          },
          {
            value: '2',
            label: '银企联公有云',
          },
          {
            value: '3',
            label: '银企联云日志查看',
          },
        ],
        disabled: false,
        editId: '',
        multipleSelection: [],
        isUnfoldAuditShow: false,
        isUnfoldAuditShow3: false,
        tableData: [],
        selectionLength: [],
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
      copyRow() {
        if (this.selectionLength.length > 0) {
          this.selectionLength.forEach((row) => {
            // 创建新对象以避免直接引用同一内存地址的对象
            const newRow = JSON.parse(JSON.stringify(row))
            // 可根据需求决定是否清空某些字段，例如ID等
            delete newRow.id // 示例：删除原id，以便系统重新分配
            newRow.id = new Date().getTime() // 给新行分配新的ID
            this.tableData.push(newRow)
          })
          this.$message({
            type: 'success',
            message: '复制成功!',
          })
        } else {
          this.$message.error('请选择要复制的条目')
        }
      },
      handleClickDelete(index, row) {
        this.$confirm('此操作将永久删除该条目, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.tableData.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleSelectionChange(val) {
        console.log('[ val ] >', val)
        this.selectionLength = val
      },
      deleteSelection() {
        if (this.selectionLength.length > 0) {
          console.log('[ qq ] >', qq)
        } else {
          this.$message.error('请选择要删除的条目')
        }
      },
      handleIsUnfold3() {
        this.isUnfoldAuditShow3 = !this.isUnfoldAuditShow3
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
  .concation_box {
    color: rgb(90, 126, 126);
  }
  .box_title {
    margin-right: 5px;
  }
  .btn_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    text-align: right;
  }
  .btn {
    margin-right: 8px;
  }
  .selectedType {
    margin: 0 8px 0 15px;
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
