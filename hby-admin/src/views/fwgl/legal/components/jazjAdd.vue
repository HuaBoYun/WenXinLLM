<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-10-23 16:23:51
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-10-23 16:29:23
 * @FilePath: \hb-admin\src\views\contract\legal\components\zcbqAdd.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="'结案总结'"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-form :model="form" :rules="rules" label-width="140px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="案件简介">
            <el-input
              v-model="form.casememo"
              type="textarea"
              rows="3"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="判决结果">
            <el-input
              v-model="form.sentenres"
              type="textarea"
              rows="3"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="经验总结">
            <el-input
              v-model="form.expersum"
              type="textarea"
              rows="3"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结案时间" prop="createtime">
            <el-date-picker
              v-model.trim="form.createtime"
              clearable
              format="yyyy-MM-dd"
              placeholder="请选择创建时间"
              style="width: 100%"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { legalCloseSumDetail, legalCloseSumSave } from '@/api/fwgl/legal'
  export default {
    data() {
      return {
        form: {
          casememo: '',
          sentenres: '',
          expersum: '',
          createtime: '',
        },
        rules: {},
        dialogFormVisible: false,
      }
    },
    methods: {
      show(data) {
        this.dialogFormVisible = true
        this.litigationid = data.litigationid
        this.arbitraid = data.arbitraid

        this.getDetail()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.form = {}
      },
      async getDetail() {
        const p = {
          litigationid: this.litigationid,
          arbitraid: this.arbitraid,
        }
        const res = await legalCloseSumDetail(p)
        this.form = res.data || {}
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        if (this.litigationid) this.form.litigationid = this.litigationid
        if (this.arbitraid) this.form.arbitraid = this.arbitraid
        const res = await legalCloseSumSave(this.form)
        // 返回date...
        if (res && res.date) {
          this.$message({
            type: 'success',
            message: '保存成功！',
          })
          this.$emit('selected', res.date)
          this.$emit('fetch')
          this.close()
        } else {
          this.$message({
            type: 'error',
            message: '保存失败！',
          })
        }
      },
    },
  }
</script>

<style lang="less" scoped></style>
