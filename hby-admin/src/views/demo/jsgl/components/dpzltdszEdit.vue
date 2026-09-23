<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="电票直联通道设置"
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
          <el-form-item label="服务器编码" prop="serviceCode">
            <el-input v-model="formData.serviceCode" placeholder="服务器编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="ip地址" prop="ipAdress">
            <el-input v-model="formData.ipAdress" placeholder="ip地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="端口" prop="port">
            <el-input v-model="formData.prot" placeholder="端口" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="servlet服务" prop="servletService">
            <el-input
              v-model="formData.servletService"
              placeholder="servlet服务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="超时" prop="overtime">
            <el-input v-model="formData.overtime" placeholder="超时" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用状态" prop="initiateMode">
            <el-select
              v-model="formData.initiateMode"
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in initiateModeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权重值" prop="weighted">
            <el-input v-model="formData.weighted" placeholder="权重值" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="连接状态" prop="connectionTest">
            <el-input
              v-model="formData.connectionTest"
              placeholder="连接状态"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="网银服务器类型" prop="internetbankType">
            <el-select
              v-model="formData.initiateMode"
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in internetbankList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24" class="btn_box">
          <div class="concation_box">
            <span class="box_title">银行接口类别</span>
            <span>({{ internetbankData.length }})</span>
            <span class="selectedType">已选</span>
            <span>{{ selectionLength.length }}条</span>
          </div>
          <div>
            <el-button class="btn" type="primary" @click="deleteSelection">
              删除
            </el-button>
            <el-button class="btn" type="primary" @click="addRow">
              新增
            </el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table
            :data="internetbankData"
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>
            <el-table-column label="银行接口类型">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.interfaceType"
                  placeholder="请选择"
                  :style="{ width: '100%' }"
                >
                  <el-option
                    v-for="item in interfaceList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
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

        <el-col :span="24" class="btn_box_2">
          <div class="concation_box">
            <span class="box_title_2">所属集团</span>
            <span>({{ groupData.length }})</span>
            <span class="selectedType_2">已选</span>
            <span>{{ selectionGroup.length }}条</span>
          </div>
          <div>
            <el-button class="btn" type="primary" @click="deleteGroup">
              删除
            </el-button>
            <el-button class="btn" type="primary" @click="addGroupRow">
              新增
            </el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table
            :data="groupData"
            style="width: 100%"
            @selection-change="handleGroupChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>
            <el-table-column label="集团">
              <template slot-scope="scope">
                <el-input v-model="scope.row.groupName" />
              </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="100">
              <template slot-scope="scope">
                <el-button
                  @click="handleGroupDelete(scope.$index, scope.row)"
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
        groupData: [],
        interfaceList: [],
        selectionLength: [],
        selectionGroup: [],
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
        this.internetbankData.push({
          id: new Date().getTime(),
          interfaceType: '',
        })
      },
      addGroupRow() {
        this.groupData.push({
          id: new Date().getTime(),
          groupName: '',
        })
      },
      handleClickDelete(index, row) {
        this.$confirm('此操作将永久删除该条目, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.internetbankData.splice(index, 1)
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
      handleGroupDelete(index, row) {
        this.$confirm('此操作将永久删除该条目, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.groupData.splice(index, 1)
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
      handleGroupChange(val) {
        console.log('[ val ] >', val)
        this.selectionGroup = val
      },
      deleteSelection() {
        if (this.selectionLength.length > 0) {
          console.log('[ qq ] >', qq)
        } else {
          this.$message.error('请选择要删除的条目')
        }
      },
      deleteGroup() {
        if (this.selectionGroup.length > 0) {
          console.log('[ qq ] >', qq)
        } else {
          this.$message.error('请选择要删除的条目')
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
  .box_title_2 {
    margin-right: 5px;
  }
  .btn_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    text-align: right;
  }
  .btn_box_2 {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 25px 0 15px 0;
    text-align: right;
  }
  .btn {
    margin-right: 8px;
  }
  .selectedType {
    margin: 0 8px 0 15px;
  }
</style>
