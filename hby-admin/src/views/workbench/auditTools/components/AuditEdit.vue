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
          <el-col :span="12" v-if="showMJ">
            <el-form-item
              label="密级"
              prop="secrectLevelId"
              :rules="[
                { required: true, trigger: 'change', message: '请选择密级' },
              ]"
            >
              <el-select
                v-model="formData.secrectLevelId"
                clearable
                placeholder="密级"
                style="width: 100%"
                @change="changeMJ"
              >
                <el-option
                  v-for="item in MJoption"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showMJ">
            <el-form-item label="知悉范围" prop="staffScopeNames">
              <el-input
                v-model="formData.staffScopeNames"
                disabled
                placeholder="请选择知悉范围"
                :style="{ width: '76%' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider></el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指引编号" prop="templeteCode">
              <el-input
                v-model="formData.templeteCode"
                clearable
                placeholder="请输入指引编号"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指引名称" prop="templeteName">
              <el-input
                v-model="formData.templeteName"
                clearable
                placeholder="请输入指引名称"
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
          <el-col :span="12">
            <el-form-item label="适用机构" prop="temorgname">
              <el-input
                v-model="formData.temorgname"
                clearable
                placeholder="请输入适用机构"
                :style="{ width: '80%' }"
                disabled
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
    <NextTable ref="nextTable" />
    <Company ref="audiTree" @select="handleDepartmentSelected" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    createAuditCode,
    getSjlxList,
    mergeNbsjTemplete,
    selectTempleteInfo,
    getTypeListPage,
  } from '@/oapi/workbench/auditTools.js'
  import NextTable from '@/views/workbench/controlLib/components/nextTable.vue'
  import OrganList from '@/views/workbench/auditTools/components/options/organList.vue'
  import Company from '@/components/departments.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'

  export default {
    name: 'YwcjEdit',
    components: { OrganList, NextTable, Company, ZXPerson },
    data() {
      return {
        sjlxList: [],
        formData: {
          templeteCode: '',
          templeteName: '',
          tempType: 0,
          templeteType: '',
          templeteDesc: '',
          temorgname: '',
          staffScopeIds: '',
          staffScopeNames: '',
          secrectLevelId: '',
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
          temorgname: [
            {
              required: true,
              message: '请选择适用机构',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        showMJ: false,
        menuId: '',
        MJoption: [],
      }
    },
    async created() {
      // this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('BaseConfigExperience')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async showEdit(row) {
        this.dialogFormVisible = true
        getTypeListPage().then((res) => {
          this.sjlxList = res.data.pageInfo.tlist
        })
        if (!row) {
          this.title = '添加'
          createAuditCode().then((res) => {
            this.formData.templeteCode = res.data.autoCode.toString()
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
          this.formData.templeteDesc = templete.templeteDesc
          this.formData.temorgname = templete.temorgname
          this.formData.orgids = templete.orgId
          this.formData.secrectLevelId = templete.secrectLevelId
          this.formData.staffScopeIds = templete.staffScopeIds
          this.formData.staffScopeNames = templete.staffScopeNames
        }
      },
      next() {
        if (this.formData.templeteId) {
          this.$refs['nextTable'].show(this.formData.templeteId, 0)
        } else {
          this.$baseMessage('请先保存模板', 'error')
        }
      },
      close() {
        this.formData = {
          templeteCode: '',
          templeteName: '',
          tempType: 0,
          templeteType: '',
          templeteDesc: '',
          temorgname: '',
          staffScopeIds: '',
          staffScopeNames: '',
          secrectLevelId: '',
        }
        this.dialogFormVisible = false
      },

      handleDepartmentSelected(val) {
        const ids = val.map((res) => res.id).toString()
        const names = val.map((res) => res.name).toString()
        this.$set(this.formData, 'orgids', ids)
        this.$set(this.formData, 'temorgname', names)
        this.$forceUpdate()
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { msg, code, data } = await mergeNbsjTemplete(this.formData)
            if (code == 1) {
              this.$baseMessage('保存成功', 'success')
              console.dir(data.templete.templeteId)
              this.formData.templeteId = data.templete.templeteId
              console.dir(this.formData)
              // this.dialogFormVisible = false
            } else {
              this.$baseMessage(msg, 'error')
            }

            this.$emit('fetch-data')
            // this.close()
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
