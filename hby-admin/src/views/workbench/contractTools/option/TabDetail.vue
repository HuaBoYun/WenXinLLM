<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-12-26 21:22:28
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-12-27 22:35:57
 * @FilePath: \hb-admin\src\views\workbench\contractTools\option\TabDetail.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
    append-to-body
  >
    <el-form
      ref="elForm"
      label-width="120px"
      :model="formData"
      :rules="rules"
      size="medium"
    >
      <el-form-item
        label="密级"
        prop="secrectLevelId"
        :rules="[{ required: true, trigger: 'change', message: '请选择密级' }]"
        v-if="showMJ"
      >
        <el-select
          v-model="formData.secrectLevelId"
          clearable
          placeholder="密级"
          style="width: 100%"
          :disabled="!footer"
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

      <el-form-item label="知悉范围" prop="staffScopeNames" v-if="showMJ">
        <el-input
          v-model="formData.staffScopeNames"
          readonly
          placeholder="请选择知悉范围"
          :style="{ width: '90%' }"
          disabled
        />
        <el-button
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
          :disabled="!formData.secrectLevelId || !footer"
        >
          选择
        </el-button>
      </el-form-item>

      <el-divider v-if="showMJ">基本信息</el-divider>

      <el-form-item label="编号" prop="assNumber">
        <el-input
          v-model="formData.assNumber"
          clearable
          disabled
          placeholder="请输入编号"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="名称" prop="assName">
        <el-input
          v-model="formData.assName"
          clearable
          :disabled="!footer"
          placeholder="请输入名称"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="描述" prop="assDes">
        <el-input
          v-model="formData.assDes"
          :disabled="!footer"
          :autosize="{ minRows: 4, maxRows: 8 }"
          placeholder="请输入描述"
          :style="{ width: '100%' }"
          type="textarea"
        />
      </el-form-item>
    </el-form>

    <div>
      <!-- <Tab1 :assstdid="assstdid"></Tab1> -->
      <div style="margin-top: 20px; text-align: right; margin-bottom: 8px">
        <el-divider content-position="center">风险影响程度</el-divider>
        <!-- <el-button style="margin-bottom: 8px" type="success" @click="add">
            新增一行
          </el-button> -->
        <el-table :data="tab1List">
          <el-table-column
            align="center"
            label="等级"
            prop="rilevel"
            width="80px"
          ></el-table-column>
          <el-table-column
            align="center"
            label="影响程度"
            prop="infludegreedes"
          >
            <template slot-scope="scope">
              <el-input
                type="textarea"
                :disabled="!footer"
                v-model="scope.row.infludegreedes"
              ></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- <Tab2 :assstdid="assstdid"></Tab2> -->
      <div style="margin-top: 20px; text-align: right; margin-bottom: 8px">
        <el-divider content-position="center">风险发生可能性</el-divider>
        <!-- <el-button style="margin-bottom: 8px" type="success" @click="add">
            新增一行
          </el-button> -->
        <el-table :data="tab2List">
          <el-table-column
            align="center"
            label="等级"
            prop="rplevel"
            width="80px"
          >
            <!-- <template slot-scope="scope">
                <el-input v-model="scope.row.rilevel"></el-input>
              </template> -->
          </el-table-column>
          <el-table-column align="center" label="说明" prop="possdes">
            <template slot-scope="scope">
              <el-input
                type="textarea"
                :disabled="!footer"
                v-model="scope.row.possdes"
              ></el-input>
            </template>
          </el-table-column>
          <!-- <el-table-column align="center" label="操作" width="80px">
              <template slot-scope="scope">
                <el-button type="text" @click="deleteList(scope.row, scope.$index)">
                  删除
                </el-button>
                <el-button type="text" @click="handleEdit(row)" :disabled="disabled">
                  修改
                </el-button>
              </template>
            </el-table-column>-->
        </el-table>
      </div>
      <!-- <Tab3 :assstdid="assstdid"></Tab3> -->
      <el-divider content-position="center">风险级别</el-divider>
      <el-table id="heat-table" :data="tab3List">
        <el-table-column
          align="center"
          class-name="is-group"
          prop="name"
          width="200"
        />
        <el-table-column align="center" label="1(很低)" prop="poss1">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.poss1"
              :disabled="!footer"
              placeholder="请选择"
              :ref="'poss1' + scope.row.name"
              @change="chageTextColor($event, 'poss1' + scope.row.name)"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :onchange="handleChange(scope.$index, scope.row)"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="2(较低)" prop="poss2">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.poss2"
              placeholder="请选择"
              :ref="'poss2' + scope.row.name"
              :disabled="!footer"
              @change="chageTextColor($event, 'poss2' + scope.row.name)"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :onchange="handleChange(scope.$index, scope.row)"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="3(中等)" prop="poss3">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.poss3"
              placeholder="请选择"
              :ref="'poss3' + scope.row.name"
              :disabled="!footer"
              @change="chageTextColor($event, 'poss3' + scope.row.name)"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :onchange="handleChange(scope.$index, scope.row)"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="4(较高)" prop="poss4">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.poss4"
              placeholder="请选择"
              :ref="'poss4' + scope.row.name"
              :disabled="!footer"
              @change="chageTextColor($event, 'poss4' + scope.row.name)"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :onchange="handleChange(scope.$index, scope.row)"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="5(很高)" prop="poss5">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.poss5"
              placeholder="请选择"
              :ref="'poss5' + scope.row.name"
              :disabled="!footer"
              @change="chageTextColor($event, 'poss5' + scope.row.name)"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :onchange="handleChange(scope.$index, scope.row)"
                v-html="
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save" v-if="footer">确 定</el-button>
    </template>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import {
    saveAssessmentStandardInfo,
    updateAssessmentStandardInfo,
    getAssessmentStandardDefaultData,
    getRiskpgbzNo,
  } from '@/api/systemLog'
  import ZXPerson from '@/components/selectPerson.vue'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'

  export default {
    name: 'index',
    components: { ZXPerson },
    data() {
      return {
        dialogFormVisible: false,
        activeName: 'first',
        title: '',
        footer: true,
        assstdid: '',
        formData: {
          assNumber: undefined,
          assName: undefined,
          assDes: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        rules: {
          assName: [
            {
              required: true,
              message: '请输入名称',
              trigger: 'blur',
            },
          ],
          assNumber: [
            {
              required: true,
              message: '请输入编码',
              trigger: 'blur',
            },
          ],
        },
        assstdid: '',
        color: ['', '#52FFB7', '#33D73B', '#FFB500', '#FF7F00', '#E92129'],
        options: [
          {
            value: '0',
            label: '未设置',
            color: '',
          },
          {
            value: '1',
            label: '很低',
            color: '#52FFB7',
          },
          {
            value: '2',
            label: '较低',
            color: '#33D73B',
          },
          {
            value: '3',
            label: '中等',
            color: '#FFB500',
          },
          {
            value: '4',
            label: '较高',
            color: '#FF7F00',
          },
          {
            value: '5',
            label: '很高',
            color: '#E92129',
          },
        ],
        tab1List: [
          {
            rilevel: '1',
            infludegreedes: '',
          },
          {
            rilevel: '2',
            infludegreedes: '',
          },
          {
            rilevel: '3',
            infludegreedes: '',
          },
          {
            rilevel: '4',
            infludegreedes: '',
          },
          {
            rilevel: '5',
            infludegreedes: '',
          },
        ],

        tab2List: [
          {
            rplevel: '1',
            possdes: '',
          },
          {
            rplevel: '2',
            possdes: '',
          },
          {
            rplevel: '3',
            possdes: '',
          },
          {
            rplevel: '4',
            possdes: '',
          },
          {
            rplevel: '5',
            possdes: '',
          },
        ],
        tab3List: [
          {
            name: '1',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '2',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '3',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '4',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '5',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
        ],
        showMJ: false,
        MJoption: [],
      }
    },
    updated() {
      this.$nextTick(() => {
        this.tab3List.map((item, index) => {
          this.chageTextColor(item.poss1, 'poss1' + item.name)
          this.chageTextColor(item.poss2, 'poss2' + item.name)
          this.chageTextColor(item.poss3, 'poss3' + item.name)
          this.chageTextColor(item.poss4, 'poss4' + item.name)
          this.chageTextColor(item.poss5, 'poss5' + item.name)
        })
      })
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('AssessmentAssessmentStandard')
        this.menuId = res[0].menuid
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
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      chageTextColor($event, selectedRef) {
        const color = this.color[$event]
        // 改变下拉框颜色值
        const inputElement = this.$refs[selectedRef].$el.children[0].children[0]
        inputElement.style.setProperty('color', color, 'important')
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.formData = {
          assNumber: undefined,
          assName: undefined,
          assDes: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        }
        this.dialogFormVisible = false
        this.formData.assNumber = undefined
        this.formData.assName = undefined
        this.formData.assDes = undefined
        this.tab1List = [
          {
            rilevel: '1',
            infludegreedes: '',
          },
          {
            rilevel: '2',
            infludegreedes: '',
          },
          {
            rilevel: '3',
            infludegreedes: '',
          },
          {
            rilevel: '4',
            infludegreedes: '',
          },
          {
            rilevel: '5',
            infludegreedes: '',
          },
        ]
        this.tab2List = [
          {
            rplevel: '1',
            possdes: '',
          },
          {
            rplevel: '2',
            possdes: '',
          },
          {
            rplevel: '3',
            possdes: '',
          },
          {
            rplevel: '4',
            possdes: '',
          },
          {
            rplevel: '5',
            possdes: '',
          },
        ]
        this.tab3List = [
          {
            name: '1',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '2',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '3',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '4',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
          {
            name: '5',
            poss1: '1',
            poss2: '2',
            poss3: '3',
            poss4: '4',
            poss5: '5',
          },
        ]
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let infludegreeStr = []
            for (var i = 0; i < this.tab1List.length; i++) {
              delete this.tab1List[i].riskLevelMapping
              if (this.tab1List[i].infludegreedes == '') {
                this.$baseMessage(
                  '请把风险影响程度填写完整！',
                  'error',
                  'vab-hey-message-error'
                )
                infludegreeStr = []
                return
              } else {
                infludegreeStr.push({
                  ...this.tab1List[i],
                  degreeid: this.tab1List[i].degreeid
                    ? this.tab1List[i].degreeid
                    : this.assstdid,
                })
              }
            }
            let possibilityStr = []
            for (var i = 0; i < this.tab2List.length; i++) {
              if (this.tab2List[i].possdes == '') {
                this.$baseMessage(
                  '请把风险发生可能性填写完整！',
                  'error',
                  'vab-hey-message-error'
                )
                possibilityStr = []
                return
              } else {
                possibilityStr.push({
                  ...this.tab2List[i],
                  rlevelmapid: this.tab3List[i].rlevelmapid
                    ? this.tab3List[i].rlevelmapid
                    : this.assstdid,
                })
              }
            }
            let levelStr = []
            for (var i = 0; i < this.tab3List.length; i++) {
              if (
                this.tab3List[i].poss1 == '' ||
                this.tab3List[i].poss2 == '' ||
                this.tab3List[i].poss3 == '' ||
                this.tab3List[i].poss4 == '' ||
                this.tab3List[i].poss5 == ''
              ) {
                this.$baseMessage(
                  '请把风险级别填写完整！',
                  'error',
                  'vab-hey-message-error'
                )
                levelStr = []
                return
              } else {
                levelStr.push({
                  ...this.tab3List[i],
                  possid: this.tab3List[i].possid
                    ? this.tab3List[i].possid
                    : this.assstdid,
                })
              }
            }

            if (this.title == '添加') {
              const { msg } = await saveAssessmentStandardInfo({
                ...this.formData,
                possibilityStr: JSON.stringify(possibilityStr),
                infludegreeStr: JSON.stringify(infludegreeStr),
                levelStr: JSON.stringify(levelStr),
              })
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.title == '编辑') {
              const { msg } = await updateAssessmentStandardInfo({
                ...this.formData,
                possibilityStr: JSON.stringify(possibilityStr),
                infludegreeStr: JSON.stringify(infludegreeStr),
                levelStr: JSON.stringify(levelStr),
              })
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.dialogFormVisible = false
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      async showEdit(row, type) {
        //
        if (row) {
          const res = await getAssessmentStandardDefaultData({
            assstdid: row.assstdid,
          })
          this.assstdid = row.assstdid
          if (res && res.code == 1) {
            this.$set(this, 'formData', {
              assstdid: res.data.data.assstdid,
              assNumber: res.data.data.assnumber,
              assName: res.data.data.assname,
              assDes: res.data.data.assdes,
              secrectLevelId: res.data.data.secrectLevelId,
              staffScopeNames: res.data.data.staffScopeNames,
              staffScopeIds: res.data.data.staffScopeIds,
            })
            res.data.data.riskInfludegrees.length > 0
              ? (this.tab1List = res.data.data.riskInfludegrees)
              : ''
            res.data.data.possibilities.length > 0
              ? (this.tab2List = res.data.data.possibilities)
              : ''
            if (res.data.data.riskInfludegrees.length > 0) {
              let list = []

              for (var i = 0; i < res.data.data.riskInfludegrees.length; i++) {
                list.push({
                  ...res.data.data.riskInfludegrees[i].riskLevelMapping,
                  name: res.data.data.riskInfludegrees[i].rilevel,
                })
              }
              this.tab3List = list
            }
          }
        }
        if (type == 'add') {
          this.title = '添加'
          this.footer = true
          const { data } = await getRiskpgbzNo()
          this.formData.assNumber = data
        } else if (type == 'edit') {
          this.title = '编辑'
          this.footer = true
        } else {
          this.title = '查看'
          this.footer = false
        }
        this.dialogFormVisible = true
      },
      handleClick(tab, event) {},
      handleChange(a, b, selectedRef) {
        this.tab3List[a] = b
      },
    },
  }
</script>
<style lang="scss">
  // .el-table thead.is-group th.el-table__cell {
  //   background: #fff;
  // }
  // 覆盖disabled状态下的颜色
  #heat-table {
    .el-select.is-disabled .el-input__inner {
      color: inherit !important;
      background-color: transparent !important;
    }
    .el-input.is-disabled .el-input__inner {
      color: inherit !important;
      background-color: transparent !important;
    }
  }

  #heat-table thead tr:first-of-type th:first-of-type:before {
    content: '发生频率';
    text-align: center;
    position: absolute;
    width: 195px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }

  #heat-table thead tr:first-of-type th:first-of-type:after {
    content: '严重程度';
    text-align: center;
    position: absolute;
    width: 73px;
    top: 20px;
    left: 0;
  }

  #heat-table thead tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #ebeef5;
    display: block;
    text-align: center;
    transform: rotate(16deg);
    transform-origin: top left;
    -ms-transform: rotate(16deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(16deg);
    -webkit-transform-origin: top left;
  }
</style>
