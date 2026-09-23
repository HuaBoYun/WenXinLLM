<template>
  <!-- 5奖惩实施 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item
            label="审计项目名称"
            placeholder="请输入审计项目名称"
            prop="projectId"
          >
            <el-select v-model="formData.projectId" style="width: 100%">
              <el-option
                v-for="item in auditProjectSelectList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="实施时间" prop="startTime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.startTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责科室" prop="org.orgname">
            <el-input
              v-model="formData.org.orgname"
              clearable
              placeholder="请输入负责科室"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '15px' }"
              type="primary"
              @click="$refs.department.show()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>

      <el-table
        :data="formData.qualityItems"
        border
        show-summary
        :summary-method="getSummaries"
        style="width: 100%"
      >
        <el-table-column align="center" label="考核内容" width="250">
          <template #default="{ row }">
            <span>{{ row.smItem.scoreContent }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="score"
          label="分值范围"
          width="100"
        >
          <template #default="{ row }">
            <span>{{ row.smItem.score }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="扣分" width="100">
          <template slot-scope="scope">
            <el-input
              @input="handleBlur(scope.row)"
              v-model="scope.row.score"
              size="mini"
              :disabled="!footer"
              style="width: 90%"
            />
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deductReason" label="扣分项">
          <template slot-scope="scope">
            <el-input
              @input="handleInput(scope.$index, scope.row)"
              v-model="scope.row.deductReason"
              size="mini"
              :disabled="!footer"
              style="width: 90%"
              type="textarea"
            />
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    qualityScoreDetail,
    qualityScoreDetails,
    qualityScoreUpdate,
  } from '@/oapi/audit/qualityScore'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { implementPlanList } from '@/oapi/audit/project'
  import { addScoreDetail } from '@/oapi/audit/scoreManage'
  // import {
  //   editDataSource,
  //   getDataSourceDefaultInfo,
  //   LinkTest,
  // } from '@/api/setting/org'

  const token = store.getters['user/token']

  export default {
    components: { DepartmentOptions },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        auditProjectSelectList: [], // 审计项目名称的列表
        detailScoreData: {}, // 临时详情数据变量
        formData: {
          // 表单入参
          id: '',
          type: 5,
          projectId: null, // 当前选中的审计项目名称ID
          org: {
            // 复制科室
            orgid: null,
            orgname: '',
          },
          orgId: null,
          qualityItems: [], // 考核内容
          score: 0,
        },
        footer: true,
        initialTable: [],
        rules: {
          projectId: [
            {
              required: true,
              message: '请选择审计项目名称',
              trigger: 'change',
            },
          ],

          startTime: [
            {
              required: true,
              message: '请输入实施时间',
              trigger: 'blur',
            },
          ],
          'org.orgname': [
            {
              required: true,
              message: '请选择负责科室',
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
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      handleDepartmentSelected(node) {
        // 选择科室回调
        //保存名称
        this.$set(this.formData.org, `orgname`, node.name)
        //保存名称对应的ID
        this.$set(this.formData.org, `orgid`, node.id)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        const {
          data: { tlist },
        } = await implementPlanList({ pageNumber: 1, pageSize: 9999 })
        if (tlist.length) {
          this.auditProjectSelectList = tlist.map((item) => {
            return {
              value: item.id,
              label: item.projectName,
            }
          })
        }

        if (row) {
          // this.formData = JSON.parse(JSON.stringify(row))
          const detailData = await qualityScoreDetails({ id: row.id })
          this.formData = detailData.data.data
          this.detailScoreData = detailData.data.data.qualityItems
          this.$set(
            this.formData,
            'projectId',
            Number(detailData.data.data.projectId)
          )
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
        this.formData.qualityItems = []
        this.getAvailableScoreItems(5)
      },
      getAvailableScoreItems(type) {
        // 根据id获取评分标准（子table表单）
        qualityScoreDetail({ type: type }).then((res) => {
          // 处理数据，满足表单结构
          if (res) {
            const data = res.data.data
            data?.forEach((item, index) => {
              this.formData.qualityItems.push({
                score: this.detailScoreData[index]?.score || null, // 评议得分
                deductReason: this.detailScoreData[index]?.deductReason || '', // 扣分项
                smItem: {
                  id: item.id,
                  score: item.score,
                  scoreContent: item.scoreContent,
                  smId: item.smId,
                  sort: item.sort,
                },
              })
            })
          }
        })
      },
      close() {
        this.detailScoreData = []
        this.formData = {
          id: '',
          type: 5,
          project: {
            // 项目名称
            prjoectId: null,
            prjoectName: '',
          },
          org: {
            // 复制科室
            orgid: null,
            orgname: '',
          },
          orgId: null,
          qualityItems: [], // 考核内容
          score: 0,
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            params.orgId = this.formData.org.orgid
            delete this.formData['createUser']
            const res = await qualityScoreUpdate(params)
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
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
      async checkLink() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'orgName', val[0].realname)
      },
      // 总计 处理
      getSummaries(param) {
        // param -> formData.qualityItems
        const { columns, data } = param
        let aaScore = 0
        let allScore = this.formData.qualityItems.reduce(
          (accumulator, currentValue) => {
            return accumulator + currentValue.smItem.score
          },
          0
        )
        for (let i = 0; i < data.length; i++) {
          aaScore += +data[i].score
        }
        this.formData.score = aaScore
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '汇总总得分' + `(${allScore - aaScore + '分'})`
            return
          }
          if (index === 1) {
            sums[index] = allScore + '分'
            return
          }
          if (index === 2) {
            sums[index] = this.formData.score + '分'
            return
          }
          if (index === 3) {
            sums[index] = '/'
            return
          }
        })
        return sums
      },
      handleInput(a, b) {
        //a是索引
        this.tableData[a] = b
      },
      //输入分值判断
      handleBlur(row) {
        // 获取当前输入的值
        let value = row.score
        // 使用正则表达式验证输入值
        if (value) {
          value = value.replace(/[^\d.]/g, '') // 只允许数字和小数点
          value = value.replace(/^\./g, '') // 不允许以小数点开头
          value = value.replace(/\.{2,}/g, '.') // 不允许连续多个小数点
          value = value
            .replace('.', '$#$')
            .replace(/\./g, '')
            .replace('$#$', '.') // 只允许一个小数点
          value = value.replace(/^(\d+)\.(\d).*$/, '$1.$2') // 只允许一位小数
        }
        // 更新模型值
        row.score = value
        // console.log('🚀 ~ handleBlur ~ row:', row)
        if (row.score < 0) {
          row.score = 0
        } else if (row.score > row.smItem.score) {
          row.score = row.smItem.score
        }
      },
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
