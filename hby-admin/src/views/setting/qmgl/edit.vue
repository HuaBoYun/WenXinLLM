<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form
        ref="formData"
        label-width="140px"
        :model="formData"
        :rules="rules"
      >
        <el-col :span="24">
          <el-form-item label="签名所属人" prop="signstaffname">
            <el-input
              v-model="formData.signstaffname"
              clearable
              placeholder="请选择签名所属人"
              style="width: 90%; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="电子签名">
            <div style="text-align: right">
              <el-button type="text" @click="handleGenerate">确定</el-button>
              <el-button type="text" @click="handleReset">清除</el-button>
            </div>
            <div>
              <img
                style="width: 100%; height: 100px"
                :src="formData.signature"
                v-if="formData.signature"
                alt=""
              />
              <vue-esign
                v-else
                ref="esign"
                :height="200"
                style="border: 1px #d5d5d5 solid; width: 100%"
                :width="860"
              />
            </div>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <projectManage
      @projectManage="getChildlistPro"
      ref="manage"
      :isAll="true"
    />
  </el-dialog>
</template>

<script>
  import { addInfo, getDefaultInfo, editInfo } from '@/api/setting/qmgl'
  import projectManage from '@/components/danxuanPerson.vue'
  import vueEsign from 'vue-esign'
  export default {
    name: 'LoginPageEdit',
    components: { projectManage, vueEsign },
    data() {
      return {
        formData: {
          signid: '',
          signature: '',
          signstaff: '',
          signstaffname: '',
        },
        rules: {
          signstaffname: [
            {
              required: true,
              message: '请选择签名所属人',
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
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          getDefaultInfo({ signid: row.signid }).then((res) => {
            Object.keys(this.formData).forEach((key) => {
              this.formData[key] = res.data[key]
            })
          })
        }
        this.dialogFormVisible = true
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        this.$set(this.formData, 'signstaffname', val[0].realname)
        this.$set(this.formData, 'signstaff', val[0].staffid)
      },
      close() {
        this.formData = {
          signid: '',
          signature: '',
          signstaff: '',
          signstaffname: '',
        }
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['formData'].validate(async (valid) => {
          if (valid) {
            const fun = this.title == '添加' ? addInfo : editInfo
            const res = await fun({ ...this.formData })
            if (res.code == 1) {
              this.$baseMessage(
                `新增成功`,
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      //转换图片
      handleGenerate() {
        this.$refs.esign
          .generate()
          .then((res) => {
            this.$set(this.formData, 'signature', res)
          })
          .catch((err) => {
            console.error(err)
          })
      },
      //清空
      handleReset() {
        this.$set(this.formData, 'signature', undefined)
        this.$refs.esign.reset()
      },
    },
  }
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 100%;
    display: block;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>
