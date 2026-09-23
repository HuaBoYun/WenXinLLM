<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="报表初始化向导"
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
        <el-steps :active="active" finish-status="success">
          <el-step title="选择报表类型"></el-step>
          <el-step title="选择查询对象"></el-step>
          <el-step title="发布为小应用"></el-step>
          <el-step title="发布为小菜单"></el-step>
          <el-step title="系统设置"></el-step>
        </el-steps>
        <template v-if="active == 1">
          <el-col :span="24">
            <el-form-item label="往来账表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">总账表</el-radio>
                <el-radio :label="2">余额表</el-radio>
                <el-radio :label="2">明细表</el-radio>
                <el-radio :label="2">客商余额表</el-radio>
                <el-radio :label="2">客商明细表</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分析报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收账龄分析</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="管理报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收欠款分析</el-radio>
                <el-radio :label="2">收款分析</el-radio>
                <el-radio :label="3">收款预测</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="其他报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收报警单</el-radio>
                <el-radio :label="2">应收收款情况查询</el-radio>
                <el-radio :label="3">收款预测</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </template>
        <template v-if="active == 2">
          <el-col :span="24" class="table-box">
            <el-table>
              <el-table-column
                align="center"
                label="序号"
                prop="qdcode"
                width="100"
              ></el-table-column>
              <el-table-column
                align="center"
                label="查询对象"
                prop="projectOrderName"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="查询对象的次序"
                prop="projectOrderName"
                show-overflow-tooltip
              />
            </el-table>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账页格式" prop="name">
              <el-input v-model="formData.name" placeholder="账页格式" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示格式" prop="name">
              <el-input v-model="formData.name" placeholder="显示格式" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示凭证号" prop="name">
              <el-checkbox v-model="formData.name" />
            </el-form-item>
          </el-col>
        </template>
        <template v-if="active == 3">
          <el-col :span="24">
            <el-tree
              :data="data"
              default-expand-all
              :props="defaultProps"
              @node-click="handleNodeClick"
            ></el-tree>
          </el-col>
        </template>
        <template v-if="active == 4">
          <el-col :span="24">
            <el-form-item label="往来账表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">总账表</el-radio>
                <el-radio :label="2">余额表</el-radio>
                <el-radio :label="2">明细表</el-radio>
                <el-radio :label="2">客商余额表</el-radio>
                <el-radio :label="2">客商明细表</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分析报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收账龄分析</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="管理报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收欠款分析</el-radio>
                <el-radio :label="2">收款分析</el-radio>
                <el-radio :label="3">收款预测</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="其他报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收报警单</el-radio>
                <el-radio :label="2">应收收款情况查询</el-radio>
                <el-radio :label="3">收款预测</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </template>
        <template v-if="active == 5">
          <el-col :span="24">
            <el-form-item label="往来账表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">总账表</el-radio>
                <el-radio :label="2">余额表</el-radio>
                <el-radio :label="2">明细表</el-radio>
                <el-radio :label="2">客商余额表</el-radio>
                <el-radio :label="2">客商明细表</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分析报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收账龄分析</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="管理报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收欠款分析</el-radio>
                <el-radio :label="2">收款分析</el-radio>
                <el-radio :label="3">收款预测</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="其他报表" prop="organization">
              <el-radio-group>
                <el-radio :label="1">应收报警单</el-radio>
                <el-radio :label="2">应收收款情况查询</el-radio>
                <el-radio :label="3">收款预测</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </template>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <!-- <el-button @click="save" type="primary">确定</el-button> -->
      <el-button style="margin-right: 10px" @click="prev" v-if="active > 1">
        上一步
      </el-button>
      <el-button type="primary" @click="next" v-if="active < 5">
        下一步
      </el-button>
      <el-button type="primary" @click="next" v-if="active === 5">
        完成
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
          organization: [
            {
              required: true,
              message: '请输入所属组织',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        editId: '',
        multipleSelection: [],
        isUnfoldAuditShow: false,
        active: 1,
        steps: 5,
        data: [
          {
            label: '一级 1',
            children: [
              {
                label: '二级 1-1',
                children: [
                  {
                    label: '三级 1-1-1',
                  },
                ],
              },
            ],
          },
        ],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
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
      next() {
        if (this.active < 6) {
          this.active++
        } else {
          alert('流程已完成')
          // 在这里执行完成时的操作
        }
      },
      prev() {
        if (this.active > 0) {
          this.active--
        }
      },
      handleNodeClick(data) {
        console.log(data)
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
  .table-box {
    margin: 15px 0;
  }
  .btn-box {
    display: flex;
    justify-content: flex-end;
    margin-top: 15px;
  }
</style>
