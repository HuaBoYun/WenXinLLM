<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="姓名" prop="lawyerName">
            <el-input
              v-model="formData.lawyerName"
              clearable
              placeholder="请输入姓名"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="sex">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.sex"
              placeholder="性别"
              :disabled="!footer"
            >
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="identityCard">
            <el-input
              v-model="formData.identityCard"
              clearable
              placeholder="请输入身份证号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="formData.phone"
              clearable
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务" prop="position">
            <el-input
              v-model="formData.position"
              clearable
              placeholder="请输入职务"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学历" prop="education">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.education"
              placeholder="学历"
              :disabled="!footer"
            >
              <el-option label="博士研究生" value="博士研究生" />
              <el-option label="硕士研究生" value="硕士研究生" />
              <el-option label="大学本科" value="大学本科" />
              <el-option label="大学专科" value="大学专科" />
              <el-option label="中专" value="中专" />
              <el-option label="大学及以下" value="大学及以下" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起始年份" prop="startYear">
            <el-date-picker
              style="width: 100%"
              v-model="formData.startYear"
              placeholder="选择法律工作起始年份"
              type="date"
              :disabled="!footer"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="社会兼职" prop="communityPartTimeWork">
            <el-input
              v-model="formData.communityPartTimeWork"
              clearable
              placeholder="请输入其他社会兼职"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专长领域" prop="expertiseDomain">
            <el-input
              v-model="formData.expertiseDomain"
              clearable
              placeholder="请输入专长领域"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="律师执业证号" prop="lawOccupation">
            <el-input
              v-model="formData.lawOccupation"
              clearable
              placeholder="请输入法律职业资格证书编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="简历" prop="biographicalNotes">
            <el-input
              v-model="formData.biographicalNotes"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入简历"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { legalServiceLawyer, fetchApi } from '@/api/fwgl/api'

  const { saveOrUpdate, detail } = legalServiceLawyer

  export default {
    name: 'experienceView',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        formData: {},
        footer: true,
        rules: {
          lawyerName: [
            {
              required: true,
              message: '请输入姓名',
              trigger: 'blur',
            },
          ],
          sex: [
            {
              required: true,
              message: '请输入性别',
              trigger: 'blur',
            },
          ],
          identityCard: [
            {
              required: true,
              message: '请输入身份证',
              trigger: 'blur',
            },
          ],
          phone: [
            {
              required: true,
              message: '请输入联系电话',
              trigger: 'blur',
            },
          ],
          position: [
            {
              required: true,
              message: '请输入职务',
              trigger: 'blur',
            },
          ],
          education: [
            {
              required: true,
              message: '请输入学历',
              trigger: 'blur',
            },
          ],
          startYear: [
            {
              required: true,
              message: '请输入起始年份',
              trigger: 'blur',
            },
          ],
          communityPartTimeWork: [
            {
              required: true,
              message: '请输入社会兼职',
              trigger: 'blur',
            },
          ],
          expertiseDomain: [
            {
              required: true,
              message: '请输入专长领域',
              trigger: 'blur',
            },
          ],
          lawOccupation: [
            {
              required: true,
              message: '请输入法律职业',
              trigger: 'blur',
            },
          ],
          biographicalNotes: [
            {
              required: true,
              message: '请输入简历',
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
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data
            this.formData.sex = res.data.sex.toString()
          }
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹框并清理缓存数据
       * @return {*}
       */      
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },     
      /**
       * @description: 保存表单
       * @return {*}
       */      
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.formData.sex = Number(this.formData.sex)
            const res = await fetchApi(saveOrUpdate, this.formData)
            this.loading = false

            if (res && res.code === 200) {
              this.$baseMessage('保存成功', 'success')
              if (res.data) {
                this.$emit('on-save-success', {
                  key: 'lawyerId',
                  rowItem: res.data,
                })
              }
            } else {
              this.$baseMessage('操作失败！', 'error')
            }
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
