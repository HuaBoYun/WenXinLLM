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
          <el-form-item label="报告主体" prop="cluebgzt">
            <el-input
              v-model="formData.cluebgzt"
              clearable
              placeholder="请输入报告主体"
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
          <el-form-item label="级次" prop="cluejc">
            <el-input
              v-model="formData.cluejc"
              clearable
              placeholder="请输入级次"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="涉及责任人" prop="cluehandlingname">
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
      @submit="selectCompany"
    />
    <projectManage
      :modal="false"
      ref="manage"
      multiple
      @projectManage="reviewTypeSelect"
    />
  </el-dialog>
</template>

<script>
  import { wgzzAdd } from '@/api/audit/wgzz'
  import store from '@/store'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  import projectManage from '@/components/danxuanPerson.vue'
  const { baseURL } = require('@/config')
  import selectDept from '@/components/departmentSelect.vue'
  export default {
    name: 'FlawInfo',
    components: { CompanyTreeModel, projectManage, selectDept },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          cluenaber: undefined,
          cluebgzt: undefined,
          cluejc: undefined,
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
          cluebgzt: [
            {
              required: true,
              message: '请输入报告主体',
              trigger: 'blur',
            },
          ],
          cluejc: [
            {
              required: true,
              message: '请输入级次',
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
        cluehandlingList: [],
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
          const {
            clueid,
            cluenaber,
            cluebgzt,
            cluejc,
            clueunitnewid,
            clueunitnename,
            cluehandling,
            cluehandlingname,
            messagetime,
            messagemanner,
            cluesource,
            occurrencetime,
            mainclue,
          } = row
          this.formData = {
            clueid,
            cluenaber,
            cluebgzt,
            cluejc,
            clueunitnewid,
            clueunitnename,
            cluehandling,
            cluehandlingname,
            messagetime,
            messagemanner,
            cluesource,
            occurrencetime,
            mainclue,
          }

          console.log('cluehandlingname', cluehandlingname)
          console.log('cluehandling', cluehandling)
          // console.log('cluehandling', cluehandlingname.split(',').map((x, i) => {
          //   console.log('i', i)
          //   return {
          //     staffid: cluehandling.split(',')[i],
          //     username: x
          //   }
          // }))

          this.cluehandlingList = cluehandlingname.split(',').map((x, i) => {
            return {
              staffid: cluehandling.split(',')[i],
              username: x,
              realname: x,
            }
          })
          console.log('cluehandlingList', this.cluehandlingList)
        }
        if (title == 'edit') {
          console.log('修改')
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = {}
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['elForm'].resetFields()
        this.dialogFormVisible = false
        this.cluehandlingList = []
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
        this.$set(this.formData, 'clueunitnename', val.label)
        this.$set(this.formData, 'clueunitnewid', val.id)
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, 'cluehandlingname', e[0].realname)
        this.$set(this.formData, 'cluehandling', e[0].staffid)
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
