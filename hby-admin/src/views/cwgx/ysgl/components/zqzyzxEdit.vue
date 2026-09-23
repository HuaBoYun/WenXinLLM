<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="债权转移记录"
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
          <el-form-item label="财务组织" prop="organization">
            <el-input v-model="formData.organization" placeholder="财务组织" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="转出对象" prop="name">
            <el-input v-model="formData.name" placeholder="转出对象" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对象名称" prop="name">
            <el-input v-model="formData.name" placeholder="对象名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="name">
            <el-input v-model="formData.name" placeholder="币种" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="并账日期" prop="province">
            <el-date-picker
              v-model="formData.receiptsDate"
              type="date"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="原币金额" prop="status">
            <el-input v-model="formData.status" placeholder="原币金额" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div class="title_box">
            <div class="title_item">
              <span class="title_text">进行模板迁移</span>
              <span class="title_text">(0)</span>
            </div>
            <div class="title_item">
              <span class="title_text">已选0条</span>
            </div>
          </div>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="序号"
              prop="qdcode"
              width="100"
            ></el-table-column>
            <el-table-column
              align="center"
              label="处理日期"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="交易类型"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="单据编号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="转出对象类型"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="转出对象"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              align="center"
              label="转入对象类型"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="转入对象"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="币种"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="原币金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="本币金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="处理人"
              prop="planYear"
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
          organization: [
            {
              required: true,
              message: '请输入所属组织',
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
        isUnfoldAuditShow1: false,
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
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
      },
      handleIsUnfold1() {
        this.isUnfoldAuditShow1 = !this.isUnfoldAuditShow1
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
  .title_box {
    display: flex;
    margin: 10px 0;
  }
  .title_item {
    margin-right: 15px;
  }
  .title_text {
    color: #7dc4e8;
  }
</style>
