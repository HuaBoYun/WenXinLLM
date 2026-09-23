<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
    :append-to-body="true"
  >
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        class="disabled"
        :disabled="true"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计划编号" prop="field101">
            <el-input
              v-model="formData.plancode"
              clearable
              placeholder="请输入计划编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="field102">
            <el-input
              v-model="formData.planname"
              clearable
              placeholder="请输入计划名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="field103">
            <el-input
              v-model="formData.palnyear"
              clearable
              placeholder="请输入计划年度"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类别" prop="field106">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划类别"
              :style="{ vwidth: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划费用估算" prop="field107">
            <el-input
              v-model="formData.palncost"
              clearable
              placeholder="请输入计划费用估算"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划时间" prop="field133">
            <el-date-picker
              v-model="formData.spanDate"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="field108">
            <el-input
              v-model="formData.createStaff.realname"
              clearable
              placeholder="请输入编制人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制日期" prop="field108">
            <el-input
              v-model="formData.createtime"
              clearable
              placeholder="请输入编制日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="field108">
            <el-input
              v-model="formData.auditOrgInfo.orgname"
              clearable
              placeholder="请输入被审计单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="field132">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>计划项目</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table
            :data="projectList"
            style="width: 100%; margin-bottom: 25px"
          >
            <el-table-column
              align="center"
              label="项目编号"
              prop="projectCode"
            ></el-table-column>
            <el-table-column align="center" label="项目名称" prop="prjoectName">
              <template slot-scope="scope">
                <el-input
                  v-show="scope.row.show"
                  v-model="scope.row.prjoectName"
                  size="mini"
                  style="width: 90%"
                />
                <span v-show="!scope.row.show">
                  {{ scope.row.prjoectName }}
                </span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="被审计对象"
              prop="orgIdNames"
              min-width="180px"
            ></el-table-column>
            <el-table-column
              align="center"
              label="时间安排"
              prop="sjap"
              min-width="180px"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目概述"
              prop="xmgs"
              min-width="180px"
            ></el-table-column>
            <!-- <el-table-column
              align="center"
              label="计划开始时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                {{ UTCformat(scope.row.startDate) }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="计划结束时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                {{ UTCformat(scope.row.endDate) }}
              </template>
            </el-table-column>

            <el-table-column align="center" label="是否外委" min-width="130">
              <template slot-scope="scope">
                {{ scope.row.externAlassig === 1 ? '是' : '否' }}
              </template>
            </el-table-column> -->
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(row)"
                >
                  下载
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'
  import { download } from '@/api/audit/implement'
  import { getPlanProjectListByPlanId } from '@/api/audit/plan'
  import { auditPlanViewDetail, getAuditPlanAttInfo } from '@/api/audit/plan'
  import { UTCformat } from '@/utils'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { handleDown } from '@/utils/fileHandler'
  import store from '@/store'
  export default {
    name: 'MaintainEdit',
    components: {
      ZXPerson,
    },
    data() {
      return {
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {},
        rules: {},
        field109Options: [
          {
            label: '统一信用社会编码',
            value: 1,
          },
          {
            label: '个人身份证',
            value: 2,
          },
        ],
        field111Options: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 2,
          },
        ],
        title: '查看',
        dialogFormVisible: false,
        tableData: [],
        projectList: [],
        UTCformat: UTCformat,
        showMJ: false,
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('PlanLook')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      /**
       * @description: 组件初始化
       * @param {*} row
       * @return {*}
       */
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        let params = {}
        params.planid = row.planid
        let res = await auditPlanViewDetail(params)
        console.log(res)
        this.formData = res.data.auditPlan
        this.formData.createtime = UTCformat(res.data.auditPlan.createtime)
        this.formData.spanDate = [
          UTCformat(res.data.auditPlan.starttime),
          UTCformat(res.data.auditPlan.endtime),
        ]
        //获取审计计划的附件
        let resFile = await getAuditPlanAttInfo({
          planId: row.planid,
        })
        this.tableData = resFile.data
        //获取计划项目集合
        let resProject = await getPlanProjectListByPlanId({
          planId: row.planid,
        })
        this.projectList = resProject.data.planList
        // this.projectList.forEach((item) => {
        //   item.finishtime = UTCformat(item.finishtime)
        // })
      },
      /**
       * @description: 关闭组件
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        // this.$refs['form'].resetFields()
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      /**
       * @description 保存
       * @param {*}
       * @return {*}
       */
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      // 无意义
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      // 无意义
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      /**
       * @description 保存
       * @param {*}
       * @return {*}
       */
      save1(row) {
        row.show = false
      },
      /**
       * @description 添加点击按钮
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.tableData.push({
          periodId: '',
          performanceTarget: '',
          mzyj: '',
          performanceDeductRatio: '',
          outpatientPerformance: '',
          outpatientProfit: '',
          show: true,
        })
      },
      // 无意义
      handleDelete(index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
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
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input {
      border: 0;
      background-color: #ffffff;
    }
  }
</style>
