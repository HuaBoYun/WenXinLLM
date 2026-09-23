<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px; font-size: 14px">
        <span style="font-weight: bold">修改项:</span>
        {{ operationMemo }}
      </div>
    </el-card>
    <el-row :gutter="20">
      <!-- 左侧：原始数据 -->
      <!-- <el-col :span="12" v-if="type != '新增'">
        <el-card>
          <div slot="header">原始数据</div>
          <el-form
            ref="oldmenuForm"
            :class="{ disabled: true }"
            :disabled="disabled"
            label-width="140px"
            :model="oldmenuForm"
            :rules="rules"
          >
            <el-col :span="24">
              <el-form-item label="角色名称" prop="rname">
                <el-input v-model.trim="oldmenuForm.rname" />
              </el-form-item>
              <el-form-item label="是否启用">
                <el-radio-group v-model="oldmenuForm.rstatus">
                  <el-radio
                    v-for="(item, index) in rstatusoptions"
                    :key="index"
                    :label="item.value"
                  >
                    {{ item.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="描述">
                <el-input v-model.trim="oldmenuForm.rdesc" type="textarea" />
              </el-form-item>
            </el-col>
          </el-form>
        </el-card>
      </el-col> -->
      <!-- 右侧：编辑表单 -->
      <el-col :span="24">
        <el-card>
          <div slot="header">新数据</div>
          <el-form
            ref="menuForm"
            :class="{ disabled: disabled }"
            :disabled="disabled"
            label-width="140px"
            :model="menuForm"
            :rules="rules"
          >
            <el-col :span="24">
              <el-form-item label="角色名称" prop="rname">
                <el-input v-model.trim="menuForm.rname" />
              </el-form-item>
              <el-form-item label="是否启用">
                <el-radio-group v-model="menuForm.rstatus">
                  <el-radio
                    v-for="(item, index) in rstatusoptions"
                    :key="index"
                    :label="item.value"
                  >
                    {{ item.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="描述">
                <el-input v-model.trim="menuForm.rdesc" type="textarea" />
              </el-form-item>
            </el-col>
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
  import { roleSave } from '@/api/setting/auth'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'RoleEdit',
    components: { Resubmit },
    data() {
      return {
        menuForm: {
          rid: undefined,
          rname: '',
          rstatus: 1,
          rdesc: '',
        },
        oldmenuForm: {
          rid: undefined,
          rname: '',
          rstatus: 1,
          rdesc: '',
        },
        ruleMenu: {
          rname: [
            { required: true, trigger: 'blur', message: '请输入角色名称' },
          ],
        },
        title: '添加',
        rstatusoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 0,
            label: '否',
          },
        ],
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
        operationMemo: undefined,
      }
    },

    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        oldData,
        newData,
        type,
        operationMemo,
        targetId
      ) {
        this.disabled = title == 'detail'
        this.type = type //判断是类型 1:新增 2:修改 3:删除
        // this.getTreeselect()
        console.log(operationMemo, 'operationMemo')
        this.menuForm = {
          rid: newData.rid,
          rname: newData.rname,
          rstatus: newData.rstatus == 1 ? 1 : 0,
          rdesc: newData.rdesc,
          targetid: targetId,
        }
        this.oldmenuForm = {
          rid: oldData.rid,
          rname: oldData.rname,
          rstatus: oldData.rstatus == 1 ? 1 : 0,
          rdesc: oldData.rdesc,
        }
        this.operationMemo = operationMemo || undefined
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
      },
      close() {
        this.$refs['menuForm'].resetFields()
        this.menuForm = this.$options.data().menuForm
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['menuForm'].validate(async (valid) => {
          if (valid) {
            const { msg, data } = await roleSave(this.menuForm)

            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      async ymsubmit() {
        this.$refs['menuForm'].validate(async (valid) => {
          if (valid) {
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
