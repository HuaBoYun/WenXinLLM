<template>
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
            placeholder="请选择审计项目名称"
            prop="projectId"
          >
            <el-select
              style="width: 100%"
              v-model="formData.projectId"
              class="filter-item"
              @change="changeProject"
            >
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
          <el-form-item label="实施日期" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              type="date"
              placeholder="实施日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评议得分">{{ formData.score }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评议结果">
            {{ formData.result }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="考核人" prop="assessorId">
            <div style="display: flex; align-items: center">
              <el-input
                v-model="formData.assessorName"
                :style="{ width: '78%' }"
                clearable
                disabled
                placeholder="请选择考核人"
              />
              <el-button
                type="primary"
                style="margin-left: 15px"
                @click="chooseAs"
              >
                选择
              </el-button>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="复核人(科长)" prop="reviewerId">
            <el-input
              v-model="formData.reviewerName"
              clearable
              disabled
              style="width: 78%"
              placeholder="请选择复核人(科长)"
            />
            <el-button
              type="primary"
              style="margin-left: 15px"
              @click="chooseRe"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-card>
      <el-divider>审计实施方案</el-divider>
      <el-table v-loading="listLoading" :data="tableData.qualityItems">
        <el-table-column
          align="center"
          label="考核内容"
          prop="projectName"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.scoreContent : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="分值范围">
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.score ? row.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分项" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.deductReason ? row.deductReason : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="负责科室" show-overflow-tooltip>
          <template>
            <span>
              {{
                tableData.org && tableData.org.orgname
                  ? tableData.org.orgname
                  : ''
              }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card>
      <el-divider>审计底稿</el-divider>
      <el-table v-loading="listLoading" :data="tableData1.qualityItems">
        <el-table-column
          align="center"
          label="考核内容"
          prop="projectName"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.scoreContent : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="分值范围">
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.score ? row.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分项" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.deductReason ? row.deductReason : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="负责科室" show-overflow-tooltip>
          <template>
            <span>
              {{
                tableData1.org && tableData1.org.orgname
                  ? tableData1.org.orgname
                  : ''
              }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card>
      <el-divider>审计报告</el-divider>
      <el-table v-loading="listLoading" :data="tableData2.qualityItems">
        <el-table-column
          align="center"
          label="考核内容"
          prop="projectName"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.scoreContent : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="分值范围">
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.score ? row.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分项" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.deductReason ? row.deductReason : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="负责科室" show-overflow-tooltip>
          <template>
            <span>
              {{
                tableData2.org && tableData2.org.orgname
                  ? tableData2.org.orgname
                  : ''
              }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card>
      <el-divider>审计管理系统上线</el-divider>
      <el-table v-loading="listLoading" :data="tableData3.qualityItems">
        <el-table-column
          align="center"
          label="考核内容"
          prop="projectName"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.scoreContent : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="分值范围">
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.score ? row.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分项" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.deductReason ? row.deductReason : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="负责科室" show-overflow-tooltip>
          <template>
            <span>
              {{
                tableData3.org && tableData3.org.orgname
                  ? tableData3.org.orgname
                  : ''
              }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card>
      <el-divider>奖惩事项</el-divider>
      <el-table v-loading="listLoading" :data="tableData4.qualityItems">
        <el-table-column
          align="center"
          label="考核内容"
          prop="projectName"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.scoreContent : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="分值范围">
          <template #default="{ row }">
            <span>{{ row.smItem ? row.smItem.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.score ? row.score : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分项" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.deductReason ? row.deductReason : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="负责科室" show-overflow-tooltip>
          <template>
            <span>
              {{
                tableData4.org && tableData4.org.orgname
                  ? tableData4.org.orgname
                  : ''
              }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
  </el-dialog>
</template>

<script>
  import {
    getListByProject,
    saveOrUpdate,
    qualityScoreStatisticalMeterDetail,
  } from '@/oapi/audit/qualityScore'
  import { implementPlanList } from '@/oapi/audit/project'
  // import { projectStatusList } from '@/oapi/audit/analyse'
  import { baseURL } from '@/config'
  import store from '@/store'

  import projectManage from '@/components/danxuanPerson.vue'
  const token = store.getters['user/token']

  export default {
    components: { projectManage },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        listLoading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: {
          org: {},
          qualityItems: [],
        },
        tableData1: {
          org: {},
          qualityItems: [],
        },
        tableData2: {
          org: {},
          qualityItems: [],
        },
        tableData3: {
          org: {},
          qualityItems: [],
        },
        tableData4: {
          org: {},
          qualityItems: [],
        },
        formData: {
          assessorId: '',
          projectId: '',
          reviewerId: '',
          score: null,
          startTime: '',
          remark: '',
          id: '',
          assessorName: '',
          reviewerName: '',
        },
        footer: true,
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
              message: '请选择实施日期',
            },
          ],
          assessorId: [
            {
              required: true,
              message: '请选择考核人',
            },
          ],
          reviewerId: [
            {
              required: true,
              message: '请选择复核人',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        list: [],
        auditProjectSelectList: [],
        chooseType: 1,
      }
    },
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
          const { data, msg, code } = await qualityScoreStatisticalMeterDetail({
            id: row.id,
          })
          if (code != 1) return this.$message.error(msg)
          const formData = data.data
          this.getTableList(formData.project.id)
          Object.assign(this.formData, {
            result: formData.result,
            projectId: formData.project.id,
            score: formData.score,
            startTime: formData.startTime,
            remark: formData.remark,
            id: formData.id,
            assessorName: formData.assessor,
            reviewerName: formData.reviewer,
            assessorId: formData.assessorId,
            reviewerId: formData.reviewerId,
          })
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
      },
      close() {
        Object.assign(this.$data.formData, this.$options.data().formData)
        Object.assign(this.$data.tableData, this.$options.data().tableData)
        Object.assign(this.$data.tableData1, this.$options.data().tableData1)
        Object.assign(this.$data.tableData2, this.$options.data().tableData2)
        Object.assign(this.$data.tableData3, this.$options.data().tableData3)
        Object.assign(this.$data.tableData4, this.$options.data().tableData4)
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = this.formData
            delete params.scoreIds
            const res = await saveOrUpdate(params)
            if (res && res.msg === '成功') {
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
      getChildlistPro(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        if (this.chooseType == 1) {
          this.formData.assessorId = ids
          this.formData.assessorName = names
        } else {
          this.formData.reviewerId = ids
          this.formData.reviewerName = names
        }
      },
      changeProject(val) {
        Object.assign(this.$data.tableData, this.$options.data().tableData)
        Object.assign(this.$data.tableData1, this.$options.data().tableData1)
        Object.assign(this.$data.tableData2, this.$options.data().tableData2)
        Object.assign(this.$data.tableData3, this.$options.data().tableData3)
        Object.assign(this.$data.tableData4, this.$options.data().tableData4)
        this.getTableList(val)
      },
      filterResult(data) {
        let score = Number(data)
        if (score == null || score === '' || isNaN(score)) {
          console.log('无效分数')
          this.formData.result = '不合格'
        } else if (score > 85) {
          console.log('优秀')
          this.formData.result = '优秀'
        } else if (score > 70) {
          console.log('良好')
          this.formData.result = '良好'
        } else if (score >= 60) {
          console.log('合格')
          this.formData.result = '合格'
        } else {
          console.log('不合格')
          this.formData.result = '不合格'
        }
      },
      chooseAs() {
        this.$refs['manage'].showEdit()
        this.chooseType = 1
      },
      chooseRe() {
        this.$refs['manage'].showEdit()
        this.chooseType = 2
      },
      async getTableList(id) {
        this.listLoading = true
        let totalScore = 0
        let totalkoufen = 0
        const { data, code, msg } = await getListByProject({ projectId: id })
        if (code == 1) {
          data.length &&
            data.map((v) => {
              if (v.type == 1 && v.qualityItems.length) {
                this.tableData.org = v.org
                v.qualityItems.map((k) => {
                  totalkoufen += Number(k.score || 0)
                  totalScore += Number(k.smItem?.score || 0)

                  this.tableData.qualityItems.push(k)
                })
              } else if (v.type == 2 && v.qualityItems.length) {
                this.tableData1.org = v.org
                v.qualityItems.map((k) => {
                  totalkoufen += Number(k.score || 0)
                  totalScore += Number(k.smItem?.score || 0)

                  this.tableData1.qualityItems.push(k)
                })
              } else if (v.type == 3 && v.qualityItems.length) {
                this.tableData2.org = v.org
                v.qualityItems.map((k) => {
                  totalkoufen += Number(k.score || 0)
                  totalScore += Number(k.smItem?.score || 0)

                  this.tableData2.qualityItems.push(k)
                })
              } else if (v.type == 4 && v.qualityItems.length) {
                this.tableData3.org = v.org
                v.qualityItems.map((k) => {
                  totalkoufen += Number(k.score || 0)
                  totalScore += Number(k.smItem?.score || 0)

                  this.tableData3.qualityItems.push(k)
                })
              } else if (v.type == 5 && v.qualityItems.length) {
                this.tableData4.org = v.org
                v.qualityItems.map((k) => {
                  totalkoufen += Number(k.score || 0)
                  totalScore += Number(k.smItem?.score || 0)
                  this.tableData4.qualityItems.push(k)
                })
              }
            })
        } else {
          this.$message.error(msg)
        }
        console.log('🚀 ~ v.qualityItems.map ~ totalkoufen:', totalkoufen)
        console.log('🚀 ~ v.qualityItems.map ~ totalScore:', totalScore)
        this.formData.score = totalScore - totalkoufen
        this.filterResult(totalScore - totalkoufen)
        this.listLoading = false
      },
      async getDetail() {},
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .mb30 {
    margin-bottom: 30px;
  }
</style>
