<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="核销预提控制规则-组织"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编码" prop="code">
            <el-input v-model="formData.code" placeholder="编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称" prop="name">
            <el-input v-model="formData.name" placeholder="名称" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold"
                v-if="isUnfoldAuditShow"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold" v-else class="el-icon-plus"></i>
              核销预提报销单交易类型
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="核销预提报销单交易类型" prop="creator">
                <el-input v-model="formData.creator" placeholder="交易类型" />
              </el-form-item>
            </el-col>
          </template>
        </el-col>
        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold1"
                v-if="isUnfoldAuditShow1"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold1" v-else class="el-icon-plus"></i>
              报销组织核销维度
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow1">
            <el-col :span="12">
              <el-form-item label="报销组织核销维度" prop="name">
                <el-select
                  :style="{ width: '100%' }"
                  v-model="formData.name"
                  placeholder="报销组织核销维度"
                  clearable
                >
                  <el-option
                    v-for="item in options_1"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </template>
          <el-col :span="24">
            <div class="dialog-add">
              <el-button type="primary" @click="deleteSelection">
                删除
              </el-button>
              <el-button type="primary" @click="addRow">新增</el-button>
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

              <el-table-column label="预提单明细字段编码">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.organization"
                    autocomplete="off"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="预提单明细核销维度">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.organization"
                    autocomplete="off"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="报销单明细字段编码">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.organization"
                    autocomplete="off"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="报销单明细核销维度">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.organization"
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
        formData: {
          radioName: 1,
          organization: '',
          receiptsDate: '',
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
          code: [
            {
              required: true,
              message: '请输入编码',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入名称',
              trigger: 'blur',
            },
          ],
          creator: [
            {
              required: true,
              message: '请输入交易类型',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        options_1: [
          {
            value: '1',
            label: '报销人单位',
          },
          {
            value: '2',
            label: '报销人部门',
          },
          {
            value: '3',
            label: '报销人',
          },
          {
            value: '4',
            label: '费用承担部门',
          },
          {
            value: '5',
            label: '成本中心',
          },
          {
            value: '6',
            label: '利润中心',
          },
        ],
        options_2: [
          {
            value: '1',
            label: '提示',
          },
          {
            value: '2',
            label: '控制',
          },
        ],
        disabled: false,
        editId: '',
        multipleSelection: [],
        isUnfoldAuditShow: false,
        isUnfoldAuditShow1: false,
        isUnfoldAuditShow2: false,
        activeName: 'first',
      }
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event)
      },
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

      handleChange(value) {
        console.log('选择的值:', value)
        this.formData.radioName = value
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
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
      },
      handleIsUnfold1() {
        this.isUnfoldAuditShow1 = !this.isUnfoldAuditShow1
      },
      addRow() {
        this.tableData.push({
          id: new Date().getTime(),
          department: '',
          organization: '',
        })
      },
      save() {
        this.isValid = true
        for (let i = 0; i < this.tableData.length; i++) {
          if (!this.tableData[i].department) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的部门未填写`)
            break
          }
        }
        if (this.isValid) {
          console.log('[ this.tableData ] >', this.tableData)
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
    justify-content: flex-end;
    margin-bottom: 20px;
  }
  .required-star {
    color: red;
  }
</style>
