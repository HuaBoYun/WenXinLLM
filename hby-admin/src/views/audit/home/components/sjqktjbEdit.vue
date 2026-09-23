<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    :append-to-body="true"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="250px" :model="formData" :rules="rules">
        <!-- <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
            label-width="120px"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
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
          <el-form-item
            label="知悉范围"
            prop="staffScopeNames"
            label-width="120px"
          >
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="企业总部内审机构名称" prop="situationName">
            <el-input
              v-model="formData.situationName"
              clearable
              placeholder="请输入企业总部内审机构名称"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="企业总部内部审计部门在谁的直接领导下开展工作"
            prop="situationLeader"
          >
            <el-radio-group v-model="formData.situationLeader">
              <el-radio label="董事长">董事长</el-radio>
              <el-radio label="总经理">总经理</el-radio>
              <el-radio label="副总经理">副总经理</el-radio>
              <el-radio label="总会计师">总会计师</el-radio>
              <el-radio label="纪委书记">纪委书记</el-radio>
              <el-radio label="其他">其他</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="是否在董事会下设立独立的审计委员会"
            prop="situationCommittee"
          >
            <el-radio-group v-model="formData.situationCommittee">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="是否已设立审计中心或区域审计中心"
            prop="situationAuditCenter"
          >
            <el-radio-group v-model="formData.situationAuditCenter">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年份" prop="situationYear">
            <el-date-picker
              v-model="formData.situationYear"
              type="year"
              placeholder="选择年"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column
              align="center"
              prop="subProject"
              label="项目"
              width="180"
            ></el-table-column>
            <el-table-column align="center" prop="name" label="其中集团总部">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.subManager"
                  @input="handleInput(scope.$index, scope.row)"
                  size="mini"
                  style="width: 90%"
                  :disabled="disabled"
                />
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="amount1"
              label="其中重要二级子公司"
            >
              <template slot-scope="scope">
                <el-input
                  @input="handleInput(scope.$index, scope.row)"
                  v-model="scope.row.subSecondaryCompany"
                  size="mini"
                  style="width: 90%"
                  :disabled="disabled"
                />
              </template>
            </el-table-column>

            <el-table-column align="center" prop="amount3" label="合计">
              <template slot-scope="scope">
                <el-input
                  @input="handleInput(scope.$index, scope.row)"
                  v-model="scope.row.subAmountTo"
                  size="mini"
                  style="width: 90%"
                  :disabled="disabled"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </span>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { saveSJQKTJB, sjqktjbDetail } from '@/api/audit/sjfx'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    components: { ZXPerson },
    data() {
      return {
        dialogFormVisible: false,
        title: '新增',
        formData: {
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        rules: {},
        tableData: [
          {
            subProject: '企业户数（户）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '二级企业户数（户）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '设置内部审计机构的企业户数（户）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '与财务部门合署的企业户数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '与纪检监察、法务、风控等非财务部门合署的企业户数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '已建立审计制度个数（个）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '其中：2021年企业修订、新建规章制度个数（个）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '企业内部审计人员（人）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '其中：专职审计人员人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '其中： 中级职称人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '高级职称人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '国际注册内部审计师人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '注册会计师人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '已从事审计工作5年以上人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '兼职审计人员人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '2021年度企业组织内审人员接受专业培训的次数（次）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '2021年度企业组织内审人员接受专业培训的人数（人）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
        ],
        initialTable: [
          {
            subProject: '企业户数（户）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '二级企业户数（户）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '设置内部审计机构的企业户数（户）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '与财务部门合署的企业户数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '与纪检监察、法务、风控等非财务部门合署的企业户数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '已建立审计制度个数（个）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '其中：2021年企业修订、新建规章制度个数（个）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '企业内部审计人员（人）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '其中：专职审计人员人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '其中： 中级职称人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '高级职称人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '国际注册内部审计师人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '注册会计师人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '已从事审计工作5年以上人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '兼职审计人员人数',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '2021年度企业组织内审人员接受专业培训的次数（次）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
          {
            subProject: '2021年度企业组织内审人员接受专业培训的人数（人）',
            subManager: '',
            subSecondaryCompany: '',
            subAmountTo: '',
          },
        ],
        disabled: false,
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
      }
    },
    // async created() {
    //   this.showMJ = couldMJ()
    //   if (this.showMJ) {
    //     // 获取密级,菜单id
    //     const res = await hasMJ('HomeSjqktjb')
    //     this.menuId = res[0].menuid
    //     // 请求密级下拉数据
    //     const res2 = await getMJ({ rightId: res[0].menuid })
    //     this.MJoption = res2.data
    //     console.log('🚀 ~ created ~ this.MJoption:', this.MJoption)
    //   }
    // },
    methods: {
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.dialogFormVisible = false
        this.tableData = this.initialTable
      },
      async show(title, row) {
        this.dialogFormVisible = true
        this.title = title
        if (row) {
          const { data } = await sjqktjbDetail({ situationId: row.id })
          this.formData = data.SituationEntity
          console.log(this.formData)
          this.formData.secrectLevelId = data.SituationEntity.secrectLevelId
          const info = data.SubclassEntities
          const arr =
            info &&
            info.map((item) => {
              return {
                ...item,
                subManager: item.subManager ? item.subManager : '',
                subSecondaryCompany: item.subSecondaryCompany
                  ? item.subSecondaryCompany
                  : '',
                subAmountTo: item.subAmountTo ? item.subAmountTo : '',
              }
            })

          this.tableData = arr
        }
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            saveSJQKTJB({
              ...this.formData,
              situationjson: JSON.stringify(this.tableData),
            }).then((res) => {
              if (res.code == 1) {
                this.dialogFormVisible = false
                this.$message.success('成功')
                this.$emit('reload')
              }
            })
          }
        })
      },
      handleInput(a, b) {
        //a是索引
        this.tableData[a] = b
        this.tableData[a].subAmountTo = (
          +(b.subManager || 0) + +(b.subSecondaryCompany || 0)
        ).toString()
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
      },
    },
  }
</script>
