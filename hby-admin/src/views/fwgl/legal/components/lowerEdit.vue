<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="'添加代理人'"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-form :model="form" :rules="rules" label-width="140px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="是否外聘律师">
            <el-radio-group v-model="form.isattorney">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理人" v-if="form.isattorney === 1">
            <el-input v-model="form.attorney" clearable style="width: 80%" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="代理人" v-if="form.isattorney === 0">
            <el-input v-model="form.attorney" style="width: 80%" readonly />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleLawerSelect"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话">
            <el-input v-model="form.attorneyphont" style="width: 80%" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="律所">
            <el-input v-model="form.lawFirm" style="width: 80%" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 我方律师 -->
    <!-- <executor-options ref="executor" @selected="handleSelected" /> -->
    <project-manage
      @projectManage="getChildlistPro"
      ref="executor"
    ></project-manage>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { legalAttorneySave, legalAttorneyDetail } from '@/api/fwgl/legal'
  // import ExecutorOptions from '@/views/fwgl/legal/components/options/executor'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  export default {
    name: '',
    components: { projectManage },
    data() {
      return {
        form: {
          isattorney: 1,
        },
        rules: {},
        queryData: {},
        dialogFormVisible: false,
      }
    },
    methods: {
      showModal(data) {
        this.dialogFormVisible = true

        this.queryData = data
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.form = {
          isattorney: 1,
        }
      },
      handleLawerSelect() {
        this.$refs.executor.showEdit()
      },
      /**
       * @description: 选择回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleSelected(val) {
        this.$set(this.form, 'attorneystaffid', val.staffid)
        this.$set(this.form, 'attorney', val.realname)
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.form.disputeid = this.id
        this.form = { ...this.form, ...this.queryData }

        const res = await legalAttorneySave(this.form)
        // 返回date...
        if (res && res.date) {
          this.$message({
            type: 'success',
            message: '保存成功！',
          })
          this.$emit('fetch-lawer-list')
          this.close()
        } else {
          this.$message({
            type: 'error',
            message: '保存失败！',
          })
        }
      },
      /**
       * @description: 选择回调
       * @param {*} val
       * @return {*}
       */      
      async getChildlistPro(val) {
        this.$set(this.form, 'attorney', val[0].realname)
        this.$set(this.form, 'attorneystaffid', val[0].staffid)
      },
    },
  }
</script>

<style lang="less" scoped></style>
