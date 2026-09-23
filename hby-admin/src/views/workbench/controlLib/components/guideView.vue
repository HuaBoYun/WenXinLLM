<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="125px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="模板编号" prop="templeteCode">
              <el-input
                v-model="formData.templeteCode"
                clearable
                placeholder="请输入模板编号"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templeteName">
              <el-input
                v-model="formData.templeteName"
                clearable
                placeholder="请输入模板名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计类型" prop="templeteType">
              <el-input
                v-model="formData.templeteType"
                clearable
                placeholder="请选择审计类型"
                :style="{ width: '80%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.SJType.showEdit()"
                :disabled="disabled"
                size="small"
              >
                选择
              </el-button>
            </el-form-item>
            <!-- <el-form-item label="审计类型" prop="templeteType">
              <el-select
                v-model="formData.templeteType"
                placeholder="请选择"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in sjlxList"
                  :key="item.typeId"
                  :label="item.auditType"
                  :value="item.auditType"
                />
              </el-select>
            </el-form-item> -->
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用机构" prop="temorgname">
              <el-input
                v-model="formData.temorgname"
                clearable
                placeholder="请输入适用机构"
                :style="{ width: '80%' }"
              />
              <el-button
                type="primary"
                size="small"
                @click="$refs.audiTree.showEdit()"
                class="fl_r"
              >
                选 择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="模板说明" prop="templeteDesc">
              <el-input
                v-model="formData.templeteDesc"
                :autosize="{ minRows: 4, maxRows: 4 }"
                placeholder="请输入模板说明"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <template #footer>
        <el-button @click="next">下一步</el-button>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <!-- <OrganList ref="organlist" @selected="handleDepartmentSelected" /> -->
    <Company ref="audiTree" @select="handleDepartmentSelected" />

    <SJType ref="SJType" @submit="SJTypeSelect" />

    <NextTable ref="nextTable" />
  </div>
</template>

<script>
  import {
    createExperienceCode,
    getSjlxList,
    mergeNbsjTemplete,
    selectTempleteInfo,
  } from '@/api/workbench/auditTools'
  import NextTable from '@/views/workbench/auditTools/components/options/nextTable.vue'
  // import OrganList from '@/views/workbench/auditTools/components/options/organList.vue'
  import Company from '@/components/departments.vue'
  import SJType from '@/views/oilAudit/project/components/tree.vue'
  export default {
    name: 'YwcjEdit',
    components: { Company, NextTable, SJType },
    data() {
      return {
        sjlxList: [],
        formData: {
          templeteCode: '',
          templeteName: '',
          tempType: 2,
          templeteType: '',
          orgids: '',
          templeteDesc: '',
          orgnames: '',
        },
        rules: {
          templeteCode: [
            {
              required: true,
              message: '请输入模板编号',
              trigger: 'blur',
            },
          ],
          templeteName: [
            {
              required: true,
              message: '请输入模板名称',
              trigger: 'blur',
            },
          ],
          templeteType: [
            {
              required: true,
              message: '请选择审计类型',
              trigger: 'change',
            },
          ],
          orgids: [
            {
              required: true,
              message: '请选择适用机构',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      next() {
        if (this.formData.templeteId) {
          this.$refs['nextTable'].show(this.formData.templeteId, 0)
        } else {
          this.$baseMessage('请先保存模板', 'error')
        }
      },
      SJTypeSelect(val) {
        console.log(val, 'val')
        this.$set(this.formData, `templeteType`, val.auditType)
        // this.$set(this.formData, `sjlxId`, val.typeId)
      },
      async showEdit(row) {
        getSjlxList().then((res) => {
          console.dir(res)
          this.sjlxList = res.data.pageInfo.tlist
        })
        this.formData.orgnames = ''
        if (!row) {
          this.title = '添加'
          this.formData.templeteId = ''
          this.formData.templeteCode = ''
          this.formData.templeteName = ''
          this.formData.templeteType = ''
          this.formData.orgids = ''
          this.formData.templeteDesc = ''
          this.formData.temorgname = ''
          this.formData.orgId = ''
          createExperienceCode().then((res) => {
            this.$set(
              this.formData,
              'templeteCode',
              res.data.autoCode.toString()
            )
          })
        } else {
          this.title = '编辑'
          const {
            data: { templete: templete },
          } = await selectTempleteInfo(Object.assign({}, row).templeteId)

          this.formData.templeteId = templete.templeteId
          this.formData.templeteCode = templete.templeteCode
          this.formData.templeteName = templete.templeteName
          this.formData.templeteType = templete.templeteType
          this.formData.orgids = templete.temorgids
          this.formData.templeteDesc = templete.templeteDesc
          this.formData.temorgname = templete.temorgname
          this.formData.orgId = templete.orgId
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['elForm'].resetFields()
        // this.formData = this.$options.data().form
        this.dialogFormVisible = false
      },
      handleDepartmentSelected(val) {
        const ids = val.map((res) => res.id).toString()
        const names = val.map((res) => res.name).toString()
        this.$set(this.formData, 'orgids', ids)
        this.$set(this.formData, 'temorgname', names)
        this.$forceUpdate()
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            this.formData.tempType = 2
            const { msg, data } = await mergeNbsjTemplete(
              this.formData,
              this.formData.orgids
            )
            this.formData.templeteId = data.templete.templeteId
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            // tasync his.close()
          }
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
    },
  }
</script>
<style scoped>
  .model-show {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
  }

  .model-show > div {
    background: red;
    margin: 5px;
    padding: 10px;
    color: white;
    font-size: 16px;
    font-weight: 500;
  }

  .fl_r {
    float: right;
  }
</style>
