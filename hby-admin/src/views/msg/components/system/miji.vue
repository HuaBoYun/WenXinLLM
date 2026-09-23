<template>
  <!-- 理论研究上报 -->
  <div>
    <el-row :gutter="20">
      <!-- 左侧：原始数据 -->
      <el-col :span="12" v-if="type != '新增'">
        <el-card>
          <div slot="header">原始数据</div>
          <el-form
            ref="oldform"
            :class="{ disabled: true }"
            :disabled="disabled"
            label-width="140px"
            :model="oldform"
            :rules="rules"
          >
            <el-form-item label="密级名称" prop="levelName">
              <el-input
                v-model="oldform.levelName"
                placeholder="请输入密级名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>

            <el-form-item label="密级类型" prop="levelType">
              <el-select
                v-model="oldform.levelType"
                clearable
                placeholder="密级类型"
                style="width: 100%"
                @change="handleData"
              >
                <el-option label="功能模块" :value="1" />
                <el-option label="业务单据" :value="2" />
                <el-option label="人员" :value="3" />
                <el-option label="附件" :value="4" />
              </el-select>
            </el-form-item>

            <el-form-item
              label="密级功能范围"
              prop="secrectMenuScope"
              v-if="oldform.levelType != 4 && oldform.levelType"
            >
              <el-select
                v-model="oldform.secrectMenuScope"
                clearable
                placeholder="密级功能范围"
                style="width: 100%"
                multiple
              >
                <el-option
                  v-for="item in Option"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item
              label="密级人员范围"
              prop="secrectStaffScope"
              v-if="oldform.levelType == 2"
            >
              <el-select
                v-model="oldform.secrectStaffScope"
                clearable
                placeholder="密级人员范围"
                style="width: 100%"
                multiple
              >
                <el-option
                  v-for="item in Option1"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <!-- 右侧：编辑表单 -->
      <el-col :span="type == '新增' ? 24 : 12">
        <el-card>
          <div slot="header">新数据</div>
          <el-form
            ref="form"
            :class="{ disabled: disabled }"
            :disabled="disabled"
            label-width="140px"
            :model="form"
            :rules="rules"
          >
            <el-form-item label="密级名称" prop="levelName">
              <el-input
                v-model="form.levelName"
                placeholder="请输入密级名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>

            <el-form-item label="密级类型" prop="levelType">
              <el-select
                v-model="form.levelType"
                clearable
                placeholder="密级类型"
                style="width: 100%"
                @change="handleData"
              >
                <el-option label="功能模块" :value="1" />
                <el-option label="业务单据" :value="2" />
                <el-option label="人员" :value="3" />
                <el-option label="附件" :value="4" />
              </el-select>
            </el-form-item>

            <el-form-item
              label="密级功能范围"
              prop="secrectMenuScope"
              v-if="form.levelType != 4 && form.levelType"
            >
              <el-select
                v-model="form.secrectMenuScope"
                clearable
                placeholder="密级功能范围"
                style="width: 100%"
                multiple
              >
                <el-option
                  v-for="item in Option"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item
              label="密级人员范围"
              prop="secrectStaffScope"
              v-if="form.levelType == 2"
            >
              <el-select
                v-model="form.secrectStaffScope"
                clearable
                placeholder="密级人员范围"
                style="width: 100%"
                multiple
              >
                <el-option
                  v-for="item in Option1"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <div class="footer" style="text-align: right" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    getSelectList,
    addData,
    editData,
    detailData,
  } from '@/api/setting/mjsz'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'mjszEdit',
    components: { Resubmit },
    data() {
      return {
        form: {
          levelName: '',
          levelType: '',
          levelId: '',
          secrectMenuScope: [],
          secrectStaffScope: [],
        },
        oldform: {
          levelName: '',
          levelType: '',
          levelId: '',
          secrectMenuScope: [],
          secrectStaffScope: [],
        },
        rules: {
          tbrgname: [
            {
              required: true,
              message: '请选择填报单位',
              trigger: 'change',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        title: '',
        Option: [],
        Option1: [],
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
      }
    },

    methods: {
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        oldData,
        newData,
        type
      ) {
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
        this.disabled = title == 'detail'
        this.type = type //判断是类型 1:新增 2:修改 3:删除
        // 旧数据
        Object.keys(this.oldform).forEach((key) => {
          this.oldform[key] = oldData[key]
          this.oldform.levelType = +oldData.levelType

          this.oldform.secrectMenuScope = oldData.secrectMenuScope
            .split(',')
            .map((res) => +res)
          this.oldform.secrectStaffScope = oldData.secrectStaffScope
            .split(',')
            .map((res) => +res)
        })
        // 新数据
        Object.keys(this.form).forEach((key) => {
          this.form[key] = newData[key]
          this.form.levelType = +newData.levelType
          this.handleData()
          this.form.secrectMenuScope = newData.secrectMenuScope
            .split(',')
            .map((res) => +res)
          this.form.secrectStaffScope = newData.secrectStaffScope
            .split(',')
            .map((res) => +res)
        })
      },
      close() {
        this.form = {
          levelName: '',
          levelType: '',
          levelId: '',
          secrectMenuScope: [],
          secrectStaffScope: [],
        }
        this.oldform = {
          levelName: '',
          levelType: '',
          levelId: '',
          secrectMenuScope: [],
          secrectStaffScope: [],
        }
        this.Option = []
        this.Option1 = []
        this.$emit('fetch-data')
      },

      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const fun = this.form.levelId ? editData : addData
            const { msg, data } = await fun({
              ...this.form,
              secrectMenuScope: this.form.secrectMenuScope.toString(),
              secrectStaffScope: this.form.secrectStaffScope.toString(),
            })
            if (code === 1) {
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },
      handleData() {
        this.form.secrectMenuScope = []
        this.form.secrectStaffScope = []
        if (this.form.levelType == 4) return Promise.resolve()
        getSelectList({
          levelType: this.form.levelType,
        }).then((res) => {
          if (this.form.levelType == 1 || this.form.levelType == 3) {
            this.Option = res.data.menuList
          }
          if (this.form.levelType == 2) {
            this.Option = res.data.menuList
            this.Option1 = res.data.employeeList
          }
        })
      },
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }

  .compare-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    position: relative;
    padding-bottom: 80px;
  }

  .compare-panel {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fff;
  }

  .panel-header {
    background: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e4e7ed;
  }

  .panel-header h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
  }

  .compare-panel .el-form {
    padding: 20px;
  }

  .footer {
    /* position: absolute;
    bottom: 0;
    right: 0; */
    background: #fff;
    /* padding: 15px 20px; */
    text-align: center;
    z-index: 1000;
  }

  .color-red {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
    display: block;
  }
</style>
