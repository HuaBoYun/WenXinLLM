<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="项目名称" prop="xmmc">
            <el-input
              v-model="formData.xmmc"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="被审计单位" prop="auditOrgId">
            <el-input
              v-model="formData.auditOrgId"
              clearable
              placeholder="请选择被审计单位"
              style="width: 260px"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->

        <el-col :span="12">
          <el-form-item label="被审计单位">
            <el-input
              v-model="formData.bsjdw"
              clearable
              placeholder="请选择被审计单位"
              :style="{ width: '260px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="备注" prop="bz">
            <el-input
              v-model="formData.bz"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->

      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>

    <!-- 被审计对象子组件 -->
    <Company ref="audiTree" @submit="getChildlistObj"></Company>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </el-dialog>
</template>

<script>
import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
import { jhgljhmxSaveOrUpdate } from '@/api/monitor/question'
  export default {
    name: 'jhlistTable',
    inheritAttrs: false,
    components: {
      DepartmentOptions,
      Company: () => import('./selectPerson.vue'),
    },
    props: {
      source: {
        type: String
      }
    },
    data() {
      return {
        formData: {

        },
        formDisabled: true,
        rules: {

        },
        dialogFormVisible: false,
        title: '新增',
        sjlxArr: {
          'tableData1': '11',
          'tableData2': '12',
          'tableData3': '21',
          'tableData4': '22',
          'tableData5': '23',
          'tableData6': '31',
          'tableData7': '32',
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {

    },
    methods: {
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `bsjdw`, node.name)
        //保存名称对应的ID
        this.$set(this.formData, `auditOrgId`, node.id)
      },
      getChildlistObj(val, flag) {
        console.log(val, flag)
        if (flag == 'right') {
          this.pdDx = 'yh'
          this.$set(this.formData, 'orgIdNames', val[0].realname)
          this.$set(this.formData, 'auditStaffId', val[0].staffid)
        } else {
          this.pdDx = 'bm'
          const names = val.map((res) => res.name).toString()
          const ids = val.map((res) => res.id).toString()
          this.$set(this.formData, 'orgIds', ids)
          this.$set(this.formData, 'orgIdNames', names)
          // this.$set(this.formData, 'auditOrgId', val.id)
          // this.$set(this.formData, 'orgName', val.name)
        }
        this.$forceUpdate()
      },
      showEdit(row, disabled) {
        console.log(111,row);
        this.dialogFormVisible = true
        this.formDisabled = disabled
        console.log(this.source)
        if(!row){
          return
        }
        console.log(row);
        if (row && !disabled) {
          this.title = '编辑'
        } else if (row && disabled) {
          this.title = '详细'
        }
        this.formData = {...row}
      },
      close() {
        this.formData = {

        }
        this.dialogFormVisible = false
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            console.log(this.formData)
            this.formData.sjlx = this.sjlxArr[this.source]
            const res = await jhgljhmxSaveOrUpdate({
              ...this.formData,
            })
            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(res.msg, 'error')
            }
            this.$emit('fetch-table',res.data)
            this.close()
          }
        })
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
