<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      :destroy-on-close="true"
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
          :disabled="disabled"
        >
          <el-col :span="12">
            <el-form-item label="模板编号" prop="templeteCode">
              <el-input
                v-model="formData.templeteCode"
                clearable
                placeholder="请输入模板编号"
                :style="{ width: '100%' }"
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
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="适用机构" prop="temorgname">
              <el-input
                v-model="formData.temorgname"
                clearable
                placeholder="请输入适用机构"
                :style="{ width: '90%' }"
                disabled
              />
              <el-button
                type="primary"
                size="small"
                @click="$refs.organlist.show()"
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
    <OrganList ref="organlist" @selected="handleDepartmentSelected" />
    <NextTable ref="nextTable" />
  </div>
</template>

<script>
  import {
    getSjlxList,
    mergeNbsjTemplete,
    selectTempleteInfo,
  } from '@/api/workbench/auditTools'
  import NextTable from '@/views/workbench/auditTools/components/options/nextTable.vue'
  import OrganList from '@/views/workbench/auditTools/components/options/organList.vue'
  export default {
    name: 'YwcjEdit',
    components: { OrganList, NextTable },
    data() {
      return {
        getSjlxList: [],
        formData: {
          templeteCode: '',
          templeteName: '',
          tempType: '',
          templeteType: '',
          temorgname: '',
          templeteDesc: '',
        },
        rules: {},
        disabled: false,
        title: '',
        dialogFormVisible: false,
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
          tempType: [
            {
              required: true,
              message: '请选择审计类型',
              trigger: 'change',
            },
          ],
          temorgname: [
            {
              required: true,
              message: '请选择适用机构',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    created() {},
    methods: {
      async showEdit(row, flag) {
        getSjlxList().then((res) => {
          console.dir(res)
          this.sjlxList = res.data.tlist
        })
        this.formData.temorgname = ''
        if (!row) {
          this.title = '添加'
        } else if (row && flag) {
          this.disabled = true
          let res = await selectTempleteInfo(row.templeteId)
          this.formData.templeteCode = res.data.templete.templeteCode
          this.formData.templeteName = res.data.templete.templeteName
          this.formData.templeteType = res.data.templete.templeteType
          this.formData.temorgname = res.data.templete.temorgname
          this.formData.templeteDesc = res.data.templete.templeteDesc
          this.formData.templeteId = res.data.templeteId
          this.formData.orgids = res.data.templete.orgId
        } else {
          this.title = '编辑'
          let res = await selectTempleteInfo(row.templeteId)
          this.formData.templeteCode = res.data.templete.templeteCode
          this.formData.templeteName = res.data.templete.templeteName
          this.formData.templeteType = res.data.templete.templeteType
          this.formData.temorgname = res.data.templete.temorgname
          this.formData.templeteDesc = res.data.templete.templeteDesc
          this.formData.templeteId = res.data.templete.templeteId
          this.formData.orgids = res.data.templete.orgId
        }
        this.dialogFormVisible = true
      },
      next() {
        if (this.formData.templeteId) {
          this.$refs['nextTable'].show(this.formData.templeteId, 1)
        } else {
          this.$baseMessage('请先保存模板', 'error')
        }
      },
      close() {
        this.$refs['elForm'].resetFields()
        // this.formData = {}
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      handleDepartmentSelected(node) {
        let temorgname = ''
        let orgids = ''
        console.dir(node)
        node.map((item) => {
          temorgname += item.name + ','
          orgids += item.id + ','
        })
        temorgname = temorgname.substring(0, temorgname.length - 1)
        // this.formData.orgids = node.id
        this.formData.temorgname = temorgname
        this.formData.orgids = orgids
        this.$forceUpdate()
      },
      save() {
        this.formData.tempType = 1
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { msg, code, data } = await mergeNbsjTemplete(this.formData)
            if (code == 1) {
              this.formData.templeteId = data.templete.templeteId
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(msg, 'error')
            }
            this.$emit('fetch-data')
          }
        })
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
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
