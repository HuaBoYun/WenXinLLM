<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    @close="close"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="模块名称" prop="module">
        <el-input v-model="form.module"></el-input>
      </el-form-item>
      <el-form-item label="所属部门" prop="orgname">
        <el-input
          v-model.trim="form.orgname"
          readonly
          :style="{ width: 'calc(100% - 66px)' }"
        />
        <el-button
          v-if="!disabled"
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="handleDep"
        >
          选择
        </el-button>
      </el-form-item>
      <el-form-item label="模块描述" prop="remark">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
    </el-form>
    <depart-ment-dialog ref="depart" @select="handleSelectDep" />
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { saveFlow } from '@/api/setting/system'
  import DepartMentDialog from '@/views/setting/auth/components/DepartMentDialog'
  export default {
    components: { DepartMentDialog },
    data() {
      return {
        dialogVisible: true,
        form: {},
        disabled: false,
        title: '新增',
        rules: {
          orgname: [
            { required: true, message: '请选择所属部门', trigger: 'blur' },
          ],
          remark: [
            { required: true, message: '请填写模块描述', trigger: 'blur' },
          ],
          module: [
            { required: true, message: '请填写模块名称', trigger: 'blur' },
          ],
        },
      }
    },
    methods: {
      close() {
        this.$emit('close')
      },
      showDtails(row) {
        this.form = { ...row }
        this.form.orgname = row.tblOrganization.orgname
        this.title = '修改'
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let obj = { ...this.form }
            delete obj.tblOrganization
            const res = await saveFlow(obj)
            if (res.code === 1) {
              this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          } else {
            return false
          }
        })
      },
      handleDep() {
        this.$refs.depart.show()
        this.$refs.form.clearValidate()
      },
      handleSelectDep(data) {
        // this.form.orgid = data.id
        // this.form.orgname = data.label
        this.$set(this.form, 'orgid', data.id)
        this.$set(this.form, 'orgname', data.label)
        this.$forceUpdate()
      },
    },
  }
</script>

<style lang="scss" scoped></style>
