<template>
  <el-dialog
    title="计划项目"
    :visible.sync="visible"
    width="70%"
    :close-on-click-modal="false"
    @close="closeDialog"
    append-to-body
  >
    <el-form
      ref="projectForm"
      :model="formData"
      :rules="rules"
      label-width="150px"
      :disabled="isReadonly"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectname">
            <el-input
              v-model="formData.projectname"
              placeholder="请输入项目名称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作目标" prop="targetname">
            <el-input
              v-model="formData.targetname"
              placeholder="请输入工作目标"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计类型" prop="auditType">
            <el-select
              v-model="formData.auditType"
              placeholder="请选择"
              style="width: 100%"
              @change="handleAuditTypeChange"
            >
              <el-option
                v-for="item in auditTypeOptions"
                :key="item.typeId"
                :label="item.auditType"
                :value="item.auditType"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划完成时间">
            <el-date-picker
              v-model="formData.finishtime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计对象" prop="orgidnames">
            <el-input
              v-model="formData.orgidnames"
              placeholder="请选择被审计对象"
              disabled
              style="width: 70%"
            ></el-input>
            <el-button
              type="primary"
              size="small"
              style="margin-left: 10px"
              @click="showAuditee"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外委">
            <el-select
              v-model="formData.externalassig"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in fieldOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目单位名称" prop="projectorg">
            <el-input
              v-model="formData.projectorg"
              placeholder="请选择项目单位名称"
              style="width: 70%"
              disabled
            ></el-input>
            <el-button
              type="primary"
              size="small"
              style="margin-left: 10px"
              @click="handleSelectProjectOrg"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectType">
            <el-input
              v-model="formData.projectType"
              placeholder="请输入项目类型"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批复总投资(经费:万元)">
            <el-input-number
              v-model="formData.costs"
              :min="0"
              style="width: 100%"
              placeholder="请输入批复总投资"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批复的项目起止年限" prop="appproyear">
            <el-date-picker
              v-model="formData.appproyear"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目主管部门" prop="projectmgdeptname">
            <el-input
              v-model="formData.projectmgdeptname"
              placeholder="请选择项目主管部门"
              style="width: 70%"
              disabled
            ></el-input>
            <el-button
              type="primary"
              size="small"
              style="margin-left: 10px"
              @click="handleSelectDepartment"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划审计时间" prop="startDate">
            <el-date-picker
              v-model="formData.startDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划验收时间" prop="endDate">
            <el-date-picker
              v-model="formData.endDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目单位地址" prop="projectorgaddress">
            <el-input
              v-model="formData.projectorgaddress"
              placeholder="请输入项目单位地址"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目联系人" prop="projectlinkman">
            <el-input
              v-model="formData.projectlinkman"
              placeholder="请输入项目联系人"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="projectlinktel">
            <el-input
              v-model="formData.projectlinktel"
              placeholder="请输入联系电话"
            ></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="审计实施主体" prop="implementaion">
            <el-select
              v-model="formData.implementaion"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option label="审计中心" value="审计中心"></el-option>
              <el-option label="审计中介机构" value="审计中介机构"></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="saveProject" v-if="!isReadonly">
        确 定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import dayjs from 'dayjs'

  export default {
    name: 'PlanProjectDialog',
    props: {
      value: {
        type: Boolean,
        default: false,
      },
      projectData: {
        type: Object,
        default: () => ({
          projectname: '',
          targetname: '',
          finishtime: '',
          orgidnames: '',
          orgids: '',
          externalassig: '',
          bsjtype: '',
          planprojectid: '',
          show: true,
          projectorg: '',
          projectorgid: '',
          projectType: '',
          costs: '',
          appproyear: [],
          projectmgdeptid: '',
          projectmgdeptname: '',
          startDate: '',
          endDate: '',
          projectorgaddress: '',
          projectlinkman: '',
          projectlinktel: '',
          implementaion: '',
          auditType: '',
          auditCode: '',
        }),
      },
      auditTypeOptions: {
        type: Array,
        default: () => [],
      },
      isReadonly: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        formData: {
          projectname: '',
          targetname: '',
          finishtime: '',
          orgidnames: '',
          orgids: '',
          externalassig: '',
          bsjtype: '',
          planprojectid: '',
          show: true,
          projectorg: '',
          projectorgid: '',
          projectType: '',
          costs: '',
          appproyear: [],
          projectmgdeptid: '',
          projectmgdeptname: '',
          startDate: '',
          endDate: '',
          projectorgaddress: '',
          projectlinkman: '',
          projectlinktel: '',
          implementaion: '',
          auditType: '',
          auditCode: '',
        },
        rules: {
          projectname: [
            { required: true, message: '请输入项目名称', trigger: 'change' },
          ],
          targetname: [
            { required: true, message: '请输入工作目标', trigger: 'change' },
          ],
          auditType: [
            { required: true, message: '请选择审计类型', trigger: 'change' },
          ],
          orgidnames: [
            { required: true, message: '请选择被审计对象', trigger: 'change' },
          ],
          startDate: [
            {
              required: true,
              message: '请选择计划审计时间',
              trigger: 'change',
            },
          ],
          endDate: [
            {
              required: true,
              message: '请选择计划验收时间',
              trigger: 'change',
            },
          ],
          appproyear: [
            {
              required: true,
              message: '请选择批复的项目起止年限',
              trigger: 'change',
            },
          ],
        },
        fieldOptions: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
      }
    },
    computed: {
      visible: {
        get() {
          return this.value
        },
        set(val) {
          this.$emit('input', val)
        },
      },
    },
    watch: {
      projectData: {
        handler(val) {
          this.formData = JSON.parse(JSON.stringify(val))
          // 只有当 appproyearstart 和 appproyearend 都存在时才设置 appproyear
          if (this.formData.appproyearstart && this.formData.appproyearend) {
            this.formData.appproyear = [
              this.formData.appproyearstart,
              this.formData.appproyearend,
            ]
          }
        },
        immediate: true,
        deep: true,
      },
    },
    methods: {
      showAuditee() {
        this.$emit('show-auditee')
      },
      handleSelectProjectOrg() {
        this.$emit('select-project-org')
      },
      handleSelectDepartment() {
        this.$emit('select-department')
      },
      handleAuditTypeChange(val) {
        const selectedType = this.auditTypeOptions.find(
          (item) => item.auditType === val
        )
        if (selectedType) {
          this.formData.auditCode = selectedType.auditCode
        }
        this.$emit('audit-type-change', val)
      },
      // Format date for display
      formatDate(date) {
        return date ? dayjs(date).format('YYYY-MM-DD') : ''
      },
      updateAuditee(val, data) {
        if (data == 'left') {
          const names = val.map((res) => res.label).toString()
          const ids = val.map((res) => res.id).toString()
          this.formData.orgidnames = names
          this.formData.orgids = ids
          this.formData.bsjtype = 'bm'
        } else {
          this.formData.orgidnames = val[0].realname
          this.formData.orgids = val[0].staffid
          this.formData.bsjtype = 'ry'
        }
      },
      updateProjectOrg(val) {
        const names = val.map((item) => item.name).join(',')
        const ids = val.map((item) => item.id).join(',')
        this.formData.projectorg = names
        this.formData.projectorgid = ids
      },
      updateDepartment(node) {
        this.formData.projectmgdeptname = node.label
        this.formData.projectmgdeptid = node.id
      },
      saveProject() {
        this.$refs.projectForm.validate((valid) => {
          if (valid) {
            const formData = JSON.parse(JSON.stringify(this.formData))
            // 拆分 appproyear
            if (formData.appproyear && formData.appproyear.length === 2) {
              formData.appproyearstart = this.formatDate(formData.appproyear[0])
              formData.appproyearend = this.formatDate(formData.appproyear[1])
            }

            // 删除接口不需要的字段
            delete formData.show
            this.$emit('save', formData)
            this.visible = false
          }
        })
      },
      closeDialog() {
        this.$emit('close')
      },
    },
  }
</script>

<style lang="scss" scoped>
  .el-form-item {
    margin-bottom: 22px;

    ::v-deep .el-form-item__content {
      display: flex;
      align-items: center;
    }

    ::v-deep .el-input,
    ::v-deep .el-select,
    ::v-deep .el-date-editor,
    ::v-deep .el-input-number {
      width: 100%;
    }
  }

  .el-row {
    margin-bottom: 0;
  }

  .el-col {
    margin-bottom: 0;
  }
</style>
