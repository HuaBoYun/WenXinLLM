<template>
  <el-dialog
    title="代理结算账户设置"
    :visible.sync="dialogFormVisible"
    width="1000px"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="资金组织" prop="code">
            <el-input v-model="formData.code" :disabled="disabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理结算账户" prop="jxName">
            <el-input v-model="formData.jxName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户单位" prop="jxName">
            <el-input v-model="formData.jxName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行类别" prop="jxName">
            <el-input v-model="formData.jxName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户银行" prop="jxName">
            <el-input v-model="formData.jxName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收付属性" prop="jsType">
            <el-select
              v-model="formData.code"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in jxTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="jsType">
            <el-select
              v-model="formData.code"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in jxTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用状态" prop="jsType">
            <el-select
              v-model="formData.code"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in jxTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold"
                v-if="isUnfoldAuditShow"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold" v-else class="el-icon-plus"></i>
              操作信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="录入人" prop="province">
                <el-input
                  v-model="formData.province"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="录入时间" prop="province">
                <el-date-picker
                  v-model="formData.confimrDate"
                  type="date"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="确认人" prop="city">
                <el-input v-model="formData.city" :style="{ width: '100%' }" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="确认时间" prop="province">
                <el-date-picker
                  v-model="formData.confimrDate"
                  type="date"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </template>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: 'sxlbEdit',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        isUnfoldAuditShow: false,
        formData: {
          code: '',
          name: '',
          code: '',
          province: '',
          city: '',
          phone: '',
          address: '',
          status: '',
          creator: '',
          createDate: '',
          updateCreator: '',
          updateDate: '',
        },
        rules: {
          code: [
            {
              required: true,
              message: '请输入编码',
              trigger: 'blur',
            },
          ],
          jxName: [
            {
              required: true,
              message: '请输入结息名称',
              trigger: 'blur',
            },
          ],
          jsType: [
            {
              required: true,
              message: '请输入结息方式',
              trigger: 'blur',
            },
          ],
          jsr: [
            {
              required: true,
              message: '请输入结息日',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择启用状态',
              trigger: 'blur',
            },
          ],
        },
        jxTypeList: [
          {
            value: '1',
            label: '按年结',
          },
          {
            value: '2',
            label: '按半年结',
          },
          {
            value: '3',
            label: '按季度结',
          },
          {
            value: '4',
            label: '按月结',
          },
        ],
        optionsStatus: [
          {
            value: '1',
            label: '开启',
          },
          {
            value: '2',
            label: '关闭',
          },
        ],
      }
    },
    created() {},

    methods: {
      handleOpenDlJszhszDialog() {
        this.dialogFormVisible = true
      },
      save() {
        this.dialogFormVisible = false
      },
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
      },
    },
  }
</script>
<style scoped>
  .dialog-add {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;
  }
  .required-star {
    color: red;
  }
  .show_line_box {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  .title_l {
    width: 100px;
    text-align: center;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px solid #cccccc6e;
  }
  .el-icon-minus {
    cursor: pointer;
  }
  .el-icon-plus {
    cursor: pointer;
  }
</style>
