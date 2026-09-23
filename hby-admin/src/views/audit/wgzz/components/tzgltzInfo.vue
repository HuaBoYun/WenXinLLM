<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="线索编号" prop="cluenaber">
            <el-input
              v-model="formData.cluenaber"
              clearable
              placeholder="请输入线索编号"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及单位" prop="clueunitnewid">
            <el-input
              v-model="formData.clueunitnename"
              clearable
              placeholder="请输入涉及单位"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.companyTreeModel.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="涉及责任人" prop="cluehandling">
            <el-input
              v-model="formData.cluehandlingname"
              clearable
              placeholder="请输入涉及责任人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.manage.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报送时间" prop="messagetime">
            <el-date-picker
              v-model="formData.messagetime"
              value-format="yyyy-MM-dd"
              placeholder="请输入报送时间"
              :style="{ width: '256px' }"
              type="date"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报送方式" prop="messagemanner">
            <el-input
              v-model="formData.messagemanner"
              clearable
              placeholder="请输入报送方式"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="线索来源" prop="cluesource">
            <el-input
              v-model="formData.cluesource"
              clearable
              placeholder="请输入线索来源"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发生时间" prop="occurrencetime">
            <el-date-picker
              v-model="formData.occurrencetime"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              :style="{ width: '256px' }"
              type="date"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主要问题线索" prop="mainclue">
            <el-input
              v-model="formData.mainclue"
              clearable
              placeholder="请输入主要问题线索"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-row>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="初步核实" name="first">
          <el-table :data="firstList">
            <el-table-column
              align="center"
              label="线索编号"
              prop="cluenaber"
              #default="{ row }"
            >
              <el-button type="text" @click="shbgDetail(row)">
                {{ row.cluenaber }}
              </el-button>
            </el-table-column>

            <el-table-column
              align="center"
              label="核实内容"
              prop="verifycontent"
            />
            <el-table-column
              align="center"
              label="初步核实开展过程情况"
              prop="verifyconkzgcqk"
            />
            <el-table-column
              align="center"
              label="初步核实结果"
              prop="verifyconhsjg"
            />

            <el-table-column
              align="center"
              label="工作建议"
              prop="cluegzjy"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="状态"
              prop="status"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{
                  row.status == 1
                    ? '审批中'
                    : row.status == 2
                    ? '需调整'
                    : row.status == 3
                    ? '已撤销'
                    : row.status == 4
                    ? '已终止'
                    : row.status == 5
                    ? '已跟踪'
                    : row.status == 6
                    ? '已完成'
                    : '未审批'
                }}
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
        </el-tab-pane>
        <el-tab-pane label="违规核查" name="second">
          <el-table :data="secondList">
            <el-table-column
              align="center"
              label="线索编号"
              prop="cluenaber"
              #default="{ row }"
            >
              <el-button type="text" @click="wghsDetail(row)">
                {{ row.cluenaber }}
              </el-button>
            </el-table-column>
            <el-table-column
              align="center"
              label="核实内容"
              prop="verifycontent"
            />
            <el-table-column align="center" label="创建人" prop="creator" />
            <el-table-column
              align="center"
              label="创建时间"
              prop="impcreateusername"
            />

            <el-table-column
              align="center"
              label="状态"
              prop="status"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{
                  row.status == 1
                    ? '审批中'
                    : row.status == 2
                    ? '需调整'
                    : row.status == 3
                    ? '已撤销'
                    : row.status == 4
                    ? '已终止'
                    : row.status == 5
                    ? '已跟踪'
                    : row.status == 6
                    ? '已完成'
                    : '未审批'
                }}
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background
            :current-page="queryForm2.pageNumber"
            :layout="layout"
            :page-size="queryForm2.pageSize"
            :total="total2"
            @current-change="handleCurrentChange2"
            @size-change="handleSizeChange2"
          />
        </el-tab-pane>
      </el-tabs>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <!-- <CompanyTreeModel
      ref="companyTreeModel"
      @selected="selectCompany"
      :lable="'涉及单位'"
    /> -->
    <selectDept
      ref="companyTreeModel"
      title="选择下发人员"
      @projectManages="selectCompany"
    />
    <projectManage
      :modal="false"
      ref="manage"
      @reviewTypeSelect="reviewTypeSelect"
    />
    <shbgEdit ref="shbgEdit" />
    <wghsEdit ref="wghsEdit" />
  </el-dialog>
</template>

<script>
import { wgzzAdd, shbgList, wghsList } from '@/api/audit/wgzz'
import store from '@/store'
import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
const { baseURL } = require('@/config')
import selectDept from '@/components/CompanyAndDeptSelect/selectDept'
import shbgEdit from './shbgEdit'
import wghsEdit from './wghsEdit'

export default {
  name: 'FlawInfo',
  components: { CompanyTreeModel, projectManage, selectDept, shbgEdit, wghsEdit },
  inheritAttrs: false,
  props: [],
  data() {
    return {
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        clueNaber: '',
        verifycontent: '',
        pageNumber: 1,
        pageSize: 20,
      },
      total2: 0,
      queryForm2: {
        clueNaber: '',
        // verifycontent: '',
        pageNumber: 1,
        pageSize: 20,
      },
      activeName: 'first',
      firstList: [],
      secondList: [],
      baseApi: baseURL,
      api: '/audit/fileManage/upload',
      headers: {
        token: store.getters['user/token'],
      },
      formData: {
        cluenaber: undefined,
        clueunitnewid: undefined,
        clueunitnename: undefined,
        cluehandling: undefined,
        cluehandlingname: undefined,
        messagetime: undefined,
        messagemanner: undefined,
        cluesource: undefined,
        occurrencetime: undefined,
        mainclue: undefined,
      },
      footer: true,
      rules: {
        cluenaber: [
          {
            required: true,
            message: '请输入线索编号',
            trigger: 'blur',
          },
        ],
        clueunitnewid: [
          {
            required: true,
            message: '请选择涉及单位',
            trigger: 'blur',
          },
        ],
        cluehandling: [
          {
            required: true,
            message: '请选择涉及责任人',
            trigger: 'blur',
          },
        ],
        messagetime: [
          {
            required: true,
            message: '请输入报送时间',
            trigger: 'blur',
          },
        ],
        messagemanner: [
          {
            required: true,
            message: '请输入报送方式',
            trigger: 'blur',
          },
        ],
        cluesource: [
          {
            required: true,
            message: '请输入线索来源',
            trigger: 'blur',
          },
        ],
        occurrencetime: [
          {
            required: true,
            message: '请输入发生时间',
            trigger: 'blur',
          },
        ],
        mainclue: [
          {
            required: true,
            message: '请输入主要问题线索',
            trigger: 'blur',
          },
        ],
      },
      dialogFormVisible: false,
      title: '新增',
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    showEdit(title, row) {
      this.dialogFormVisible = true
      console.log(row)
      if (row) {
        this.formData = Object.assign(this.formData, row)
      }
      if (title == 'edit') {
        console.log('修改')
        this.title = '编辑'
      } else if (title == 'detail') {
        this.title = '详细'

        this.queryForm.clueNaber = row.cluenaber
        this.queryForm2.clueNaber = row.cluenaber
        this.fetchData()
        this.fetchData2()
        this.footer = false
      } else {
        this.title = '新增'
        this.formData = {}
      }
    },
    /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
      const {
        data: {
          pageInfo: { tlist: list, totalRecord: total },
        },
      } = await shbgList(this.queryForm)
      this.firstList = list
      this.total = total
    },
    handleSizeChange2(val) {
      this.queryForm2.pageSize = val
      this.fetchData2()
    },
    handleCurrentChange2(val) {
      this.queryForm2.pageNumber = val
      this.fetchData2()
    },
    async fetchData2() {
      const {
        data: {
          pageInfo: { tlist: list, totalRecord: total },
        },
      } = await wghsList(this.queryForm2)
      this.secondList = list
      this.total2 = total
    },
    handleClick() {},
    /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
      this.$refs['elForm'].resetFields()
      this.dialogFormVisible = false
      this.footer = true
    },
    add() {
      this.$refs['elForm'].validate(async (valid) => {
        if (valid) {
          const data = await wgzzAdd(this.formData)
          console.log(data)

          if (data.code == 1) {
            this.$baseMessage('保存成功', 'success')
            this.formData = {}
            this.$emit('fetch-data')
            this.close()
          } else {
            this.$baseMessage(data.msg, 'error')
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    selectCompany(val) {
      let orgidnames = val.map((item) => item.label).join(',')
      let orgids = val.map((item) => item.id).join(',')
      this.$set(this.formData, 'clueunitnename', orgidnames)
      this.$set(this.formData, 'clueunitnewid', orgids)
      this.$refs['ruleForm'].clearValidate()
    },
    reviewTypeSelect(e) {
      console.log(e)
      this.$set(this.formData, 'cluehandlingname', e.id[0].realname)
      this.$set(this.formData, 'cluehandling', e.id[0].staffid)
      this.$refs['ruleForm'].clearValidate()
    },
    shbgDetail(row) {
      this.$refs['shbgEdit'].showEdit('detail', row)
    },
    wghsDetail(row) {
      this.$refs['wghsEdit'].showEdit('detail', row)
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
