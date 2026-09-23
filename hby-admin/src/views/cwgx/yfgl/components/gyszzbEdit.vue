<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="查询条件"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="145px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-row :gutter="20">
          <el-col :span="6">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane label="候选条件" name="first"></el-tab-pane>
              <el-tab-pane label="查询方案" name="second"></el-tab-pane>
            </el-tabs>
            <template v-if="activeName == 'first'">
              <el-col :span="24">
                <el-input
                  placeholder="请输入内容"
                  v-model="searchQuery"
                  class="search-input"
                  @clear="clearSearch"
                  clearable
                >
                  <el-button
                    slot="append"
                    icon="el-icon-search"
                    @click="handleSearch"
                  ></el-button>
                </el-input>
              </el-col>
              <el-col :span="24">
                <el-tree
                  :data="data"
                  :props="defaultProps"
                  @node-click="handleNodeClick"
                ></el-tree>
              </el-col>
            </template>
            <template v-if="activeName == 'second'">
              <el-col :span="24">
                <el-input
                  placeholder="请输入内容"
                  v-model="searchQuery"
                  class="search-input"
                  @clear="clearSearch"
                  clearable
                >
                  <el-button
                    slot="append"
                    icon="el-icon-search"
                    @click="handleSearch"
                  ></el-button>
                </el-input>
              </el-col>
              <el-col :span="24">
                <el-tree
                  :data="data"
                  :props="defaultProps"
                  @node-click="handleNodeClick"
                ></el-tree>
              </el-col>
            </template>
          </el-col>
          <el-col :span="18">
            <el-col :span="24" class="top_box">
              <div>查询条件</div>
              <el-button>清空值</el-button>
            </el-col>
            <el-col :span="24">
              <el-form-item prop="radioName" class="pt_box">
                <el-radio-group
                  v-model="formData.radioName"
                  @change="handleChange"
                >
                  <el-radio :label="1">普通</el-radio>
                  <el-radio :label="2">高级</el-radio>
                </el-radio-group>
              </el-form-item>
              <template v-if="formData.radioName == 1">
                <el-col :span="24">
                  <el-table v-loading="listLoading" :data="list">
                    <el-table-column label="查询对象">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.updateDate"
                          autocomplete="off"
                        ></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="查询对象的值">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.updateDate"
                          autocomplete="off"
                        ></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="包含下级">
                      <template slot-scope="scope">
                        <el-checkbox
                          v-model="scope.row.updateDate"
                        ></el-checkbox>
                      </template>
                    </el-table-column>
                    <el-table-column label="计算小计">
                      <template slot-scope="scope">
                        <el-checkbox
                          v-model="scope.row.updateDate"
                        ></el-checkbox>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-col>
                <el-col :span="24" class="col_box">
                  <el-col :span="24">
                    <el-form-item prop="name">
                      <template #label>
                        <span>财务组织</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>

                  <el-col :span="24">
                    <el-form-item prop="name">
                      <template #label>
                        <span>往来对象</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item prop="name1">
                      <template #label>
                        <span>查询范围</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>开始月份</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>结束月份</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name1">
                      <template #label>
                        <span>币种</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>单据状态</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>返回币种</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                </el-col>
              </template>

              <template v-if="formData.radioName == 2">
                <el-col :span="24">
                  <el-table v-loading="listLoading" :data="list">
                    <el-table-column label="查询对象">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.updateDate"
                          autocomplete="off"
                        ></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="查询对象的值">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.updateDate"
                          autocomplete="off"
                        ></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="包含下级">
                      <template slot-scope="scope">
                        <el-checkbox
                          v-model="scope.row.updateDate"
                        ></el-checkbox>
                      </template>
                    </el-table-column>
                    <el-table-column label="计算小计">
                      <template slot-scope="scope">
                        <el-checkbox
                          v-model="scope.row.updateDate"
                        ></el-checkbox>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-col>
                <el-col :span="24" class="col_box">
                  <el-col :span="24">
                    <el-form-item prop="name">
                      <template #label>
                        <span>财务组织</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>

                  <el-col :span="24">
                    <el-form-item prop="name">
                      <template #label>
                        <span>往来对象</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item prop="name1">
                      <template #label>
                        <span>查询范围</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>开始月份</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>结束月份</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name1">
                      <template #label>
                        <span>币种</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>单据状态</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="" prop="name">
                      <template #label>
                        <span>返回币种</span>
                        <span style="margin-left: 35px">等于</span>
                      </template>
                      <el-input v-model="formData.name" />
                    </el-form-item>
                  </el-col>
                </el-col>
              </template>
            </el-col>
          </el-col>
        </el-row>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!disabled" class="btn_box">
      <div>
        <el-button>保存方案</el-button>
        <el-button>另存方案</el-button>
      </div>
      <div>
        <el-button @click="close">取消</el-button>
        <el-button @click="save" type="primary">确定</el-button>
      </div>
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
          radioName: '1',
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
          name: [
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
        activeName: 'first',
        searchQuery: '',
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
          {
            label: '一级 2',
            children: [
              {
                label: '二级 2-1',
                children: [
                  {
                    label: '三级 2-1-1',
                  },
                ],
              },
              {
                label: '二级 2-2',
                children: [
                  {
                    label: '三级 2-2-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 3',
            children: [
              {
                label: '二级 3-1',
                children: [
                  {
                    label: '三级 3-1-1',
                  },
                ],
              },
              {
                label: '二级 3-2',
                children: [
                  {
                    label: '三级 3-2-1',
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
        list: [],
        listLoading: false,
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
      handleClick(tab, event) {
        console.log(tab, event)
        this.activeName = tab.name
      },
      handleSearch() {
        console.log('Searching for:', this.searchQuery)
      },
      clearSearch() {
        console.log('Search query cleared')
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
  .top_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .col_box {
    margin-top: 20px;
  }
  .btn_box {
    display: flex;
    justify-content: space-between;
  }
  .pt_box >>> .el-form-item__content {
    margin-left: 0px !important;
  }
</style>
