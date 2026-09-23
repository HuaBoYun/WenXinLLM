<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form" :rules="rules">
      <el-form-item label="风险类型编号" prop="riskcatnumber">
        <el-input v-model.trim="form.riskcatnumber" />
      </el-form-item>
      <el-form-item label="风险类型名称" prop="riskcatname">
        <el-input v-model.trim="form.riskcatname" />
      </el-form-item>
      <el-form-item label="父风险" prop="fathername">
        <el-input v-model.trim="form.fathername" disabled />
      </el-form-item>
    </el-form>
    <template #footer>
      <!-- <el-button
        v-if="title === '编辑'"
        style="float: left"
        type="danger"
        @click="del"
      >
        删 除
      </el-button> -->
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    getCreationTypeJudge,
    createTreeDom,
    getCreationTypeDel,
    riskEdit,
    riskTypeToAdd,
  } from '@/api/risk'

  export default {
    name: 'TypeTreeAdd',
    data() {
      return {
        itemRow: {},
        form: {
          fatherriskcatid: '',
          riskcatnumber: '',
          riskcatname: '',
          moduletype: '',
          unit: '',
          fathername: '',
          name: '',
          number: '',
          child: null,
          father: null,
        },
        rules: {
          riskcatnumber: [
            { required: true, trigger: 'blur', message: '请输入风险编号' },
          ],
          riskcatname: [
            { required: true, trigger: 'blur', message: '请输入风险名称' },
          ],
          fathername: [
            { required: true, trigger: 'blur', message: '请输入父风险名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(title, row) {
        console.log('-------', row)
        if (row && row[0]) {
          this.itemRow = row[0]
        }
        if (title == 'add') {
          this.title = '添加'
          // for (let key in this.form) {
          //   if (key != 'riskcatname') {
          //     this.form[key] = row[0][key]
          //   }
          // }
          this.form.fathername = row[0].riskcatname
          // fatherriskcatid
          // const data = {
          //   fatherriskcatid: this.itemRow.fatherriskcatid,
          //   moduletype: this.itemRow.moduletype,
          //   unit: this.itemRow.unit,
          // }
          // riskTypeToAdd(data).then(res => {
          //   this.form.fathername = this.itemRow.riskcatname
          //   this.form.number = this.itemRow.riskcatnumber
          //   this.form.fatherriskcatid = res.data.fatherriskcatid
          //   this.form.moduletype = res.data.moduletype
          //   this.form.unit = res.data.unit

          // })
        } else {
          this.title = '编辑'
          this.form = Object.assign(this.form, row)
          console.log('🚀 ~ showEdit ~ row:', row)
          // this.form.child = row.child
          // this.form.father = row.father
          this.form.fathername = row.father.riskcatname
          this.form.riskcatnumber = row.child.riskcatnumber
          this.form.riskcatname = row.child.riskcatname
          this.form.unit = row.father.unit
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      del() {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const result = await getCreationTypeDel(this.form.child)
          this.$baseMessage(result.msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        })
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.title !== '编辑') {
              getCreationTypeJudge({
                moduletype: 'FXSJK',
                name: this.form.name,
                number: this.form.number,
                unit: this.form.unit,
              }).then((res) => {
                if (res.code === 1) {
                  let data = {
                    fatherriskcatid: this.itemRow.riskcatid,
                    riskcatname: this.form.riskcatname,
                    riskcatnumber: this.form.riskcatnumber,
                    unit: this.form.unit,
                    moduletype: 'FXSJK',
                    riskstatus: 0,
                    isleaf: 1,
                  }
                  createTreeDom(data).then((res1) => {
                    this.$emit('fetch-data')
                    this.dialogFormVisible = false
                  })
                }
              })

              // if (result.code == 200) {
              //   const { msg, code } = await createTreeDom({
              //     fatherriskcatid: this.form.child.fatherriskcatid,
              //     fullpath: this.form.child.fullpath,
              //     isdel: this.form.child.isdel,
              //     isleaf: this.form.child.isleaf,
              //     memo: this.form.child.memo,
              //     moduletype: this.form.child.moduletype,
              //     riskcatdes: this.form.child.riskcatdes,
              //     riskcatid: this.form.child.riskcatid,
              //     riskcatname: this.form.name,
              //     riskcatnumber: this.form.child.riskcatnumber,
              //     riskstatus: this.form.child.riskstatus,
              //     unit: this.form.unit,
              //   })
              //   this.$baseMessage(
              //     msg,
              //     code == 200 ? 'success' : 'error',
              //     `vab-hey-message-${code == 200 ? 'success' : 'error'}`
              //   )
              //   this.$emit('fetch-data')
              //   this.close()
              // } else {
              //   this.$baseMessage(result.msg, 'error', `vab-hey-message-error`)
              // }
            } else {
              const { msg, code } = await riskEdit({
                fatherriskcatid: this.form.child.fatherriskcatid,
                fullpath: this.form.child.fullpath,
                isdel: this.form.child.isdel,
                isleaf: this.form.child.isleaf,
                memo: this.form.child.memo,
                moduletype: this.form.child.moduletype,
                riskcatdes: this.form.child.riskcatdes,
                riskcatid: this.form.child.riskcatid,
                riskcatname: this.form.riskcatname,
                riskcatnumber: this.form.child.riskcatnumber,
                riskstatus: this.form.child.riskstatus,
                unit: this.form.unit,
              })
              if (code == 1) {
                this.$baseMessage(msg, 'success', `vab-hey-message-success`)
                this.$emit('fetch-data')
                this.close()
              } else {
                this.$baseMessage(msg, 'error', `vab-hey-message-error`)
              }

              this.close()
            }
          }
        })
      },
    },
  }
</script>
