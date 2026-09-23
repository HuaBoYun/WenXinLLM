<template>
  <div>
    <el-form
      ref="riskForm"
      :model="formData"
      label-width="220px"
      class="risk-form"
      :disabled="disabled"
    >
      <el-row>
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
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 基础信息 -->
      <el-divider content-position="center">基础信息</el-divider>
      <div class="form-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="年度"
              prop="riskyear"
              :rules="[
                { required: true, trigger: 'change', message: '请选择年度' },
              ]"
            >
              <el-date-picker
                v-model="formData.riskyear"
                type="year"
                value-format="yyyy"
                placeholder="选择年"
                :style="{ width: '100%' }"
                disabled
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="季度"
              prop="quartername"
              :rules="[
                { required: true, trigger: 'change', message: '请选择季度' },
              ]"
            >
              <el-select
                v-model="formData.quartername"
                placeholder="请选择季度"
                clearable
                :style="{ width: '100%' }"
                :disabled="disabled"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="填报单位" prop="linkOrgName">
              <el-input
                v-model="formData.linkOrgName"
                placeholder="请输入填报单位"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="填报部门" prop="linkDeptName">
              <el-input
                v-model="formData.linkDeptName"
                placeholder="请输入填报部门"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="填报人" prop="createname">
              <el-input
                v-model="formData.createname"
                placeholder="请输入填报人"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="填报时间" prop="createtime">
              <el-date-picker
                v-model="formData.createtime"
                type="datetime"
                placeholder="请选择填报时间"
                style="width: 100%"
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="notes">
              <el-input
                disabled
                type="textarea"
                :rows="3"
                v-model="formData.notes"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <el-divider content-position="center" class="reporting-period-divider">
        填报区间: {{ reportingPeriod }}
      </el-divider>
      <!-- 战略风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.zlString1 ||
          visibleFields.zlString2 ||
          visibleFields.zlInteger1 ||
          visibleFields.zlBigdecimal1 ||
          visibleFields.zlBigdecimal2 ||
          visibleFields.zlInteger2 ||
          visibleFields.zlBigdecimal3 ||
          visibleFields.zlInteger3 ||
          visibleFields.zlBigdecimal4 ||
          visibleFields.zlInteger4 ||
          visibleFields.zlBigdecimal5 ||
          visibleFields.zlBigdecimal6 ||
          visibleFields.zlBigdecimal7 ||
          visibleFields.zlBigdecimal8 ||
          visibleFields.zlBigdecimal9 ||
          visibleFields.zlInteger5 ||
          visibleFields.zlBigdecimal10
        "
      >
        战略风险
      </el-divider>
      <div class="form-section">
        <div class="risk-indicator" v-if="visibleFields.zlString1">
          <div class="indicator-title">宏观经济风险</div>
          <el-row :gutter="20" v-if="visibleFields.zlString1">
            <el-col :span="24">
              <el-form-item label="国内外宏观经济形势变化" prop="zlString1">
                <el-input
                  type="textarea"
                  :rows="4"
                  v-model="formData.zlString1"
                  placeholder="请输入国内外宏观经济形势变化"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="visibleFields.zlString1Des">
              <el-form-item label="具体情况说明" prop="zlString1Des">
                <el-input
                  v-model="formData.zlString1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="risk-indicator" v-if="visibleFields.zlString2">
          <div class="indicator-title">政策风险</div>
          <el-row :gutter="20" v-if="visibleFields.zlString2">
            <el-col :span="24">
              <el-form-item label="国家及行业政策变化" prop="zlString2">
                <el-input
                  type="textarea"
                  :rows="4"
                  v-model="formData.zlString2"
                  placeholder="请输入国家及行业政策变化"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="visibleFields.zlString2Des">
              <el-form-item label="具体情况说明" prop="zlString2Des">
                <el-input
                  v-model="formData.zlString2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="
            visibleFields.zlInteger1 ||
            visibleFields.zlBigdecimal1 ||
            visibleFields.zlBigdecimal2 ||
            visibleFields.zlInteger2 ||
            visibleFields.zlBigdecimal3 ||
            visibleFields.zlInteger3 ||
            visibleFields.zlBigdecimal4 ||
            visibleFields.zlInteger4 ||
            visibleFields.zlBigdecimal5 ||
            visibleFields.zlBigdecimal10
          "
        >
          <div class="indicator-title">国际化经营风险</div>
          <el-row :gutter="20">
            <el-col
              :span="12"
              v-if="visibleFields.zlInteger1"
              style="height: 80px"
            >
              <el-form-item
                label='被美国列入"实体清单"、受关税政策影响的子企业数量（个）'
                prop="zlInteger1"
              >
                <el-input
                  v-model="formData.zlInteger1"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col
              :span="12"
              v-if="visibleFields.zlInteger1Des"
              style="height: 80px"
            >
              <el-form-item label="具体情况说明" prop="zlInteger1Des">
                <el-input
                  v-model="formData.zlInteger1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal1">
              <el-form-item
                label="境外中高风险地区境外资产总额"
                prop="zlBigdecimal1"
              >
                <el-input
                  v-model="formData.zlBigdecimal1"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal1Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal1Des">
                <el-input
                  v-model="formData.zlBigdecimal1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal2">
              <el-form-item
                label="境外中高风险地区资产占比"
                prop="zlBigdecimal2"
              >
                <el-input
                  v-model="formData.zlBigdecimal2"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal2Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal2Des">
                <el-input
                  v-model="formData.zlBigdecimal2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.zlInteger2">
              <el-form-item label="境外重大建设项目逾期数量" prop="zlInteger2">
                <el-input
                  v-model="formData.zlInteger2"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlInteger2Des">
              <el-form-item label="具体情况说明" prop="zlInteger2Des">
                <el-input
                  v-model="formData.zlInteger2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal3">
              <el-form-item label="汇率损失金额" prop="zlBigdecimal3">
                <el-input
                  v-model="formData.zlBigdecimal3"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal3Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal3Des">
                <el-input
                  v-model="formData.zlBigdecimal3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.zlInteger3">
              <el-form-item label="境外重大法律诉讼案件数量" prop="zlInteger3">
                <el-input
                  v-model="formData.zlInteger3"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlInteger3Des">
              <el-form-item label="具体情况说明" prop="zlInteger3Des">
                <el-input
                  v-model="formData.zlInteger3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal4">
              <el-form-item
                label="境外重大法律诉讼案件涉案金额"
                prop="zlBigdecimal4"
              >
                <el-input
                  v-model="formData.zlBigdecimal4"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal4Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal4Des">
                <el-input
                  v-model="formData.zlBigdecimal4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col
              :span="12"
              v-if="visibleFields.zlInteger4"
              style="height: 80px"
            >
              <el-form-item
                label="境外重大合规案件（如被世行禁止参与投标、境外腐败）"
                prop="zlInteger4"
              >
                <el-input
                  v-model="formData.zlInteger4"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col
              :span="12"
              v-if="visibleFields.zlInteger4Des"
              style="height: 80px"
            >
              <el-form-item label="具体情况说明" prop="zlInteger4Des">
                <el-input
                  v-model="formData.zlInteger4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal5">
              <el-form-item
                label="境外重大合规案件涉案金额"
                prop="zlBigdecimal5"
              >
                <el-input
                  v-model="formData.zlBigdecimal5"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal5Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal5Des">
                <el-input
                  v-model="formData.zlBigdecimal5Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal10">
              <el-form-item
                label="受被美制裁印象，出现重要产品进口受限或断供数量"
                prop="zlBigdecimal10"
              >
                <el-input
                  v-model="formData.zlBigdecimal10"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div
          class="risk-indicator"
          v-if="visibleFields.zlBigdecimal6 || visibleFields.zlBigdecimal7"
        >
          <div class="indicator-title">改革与业务转型风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.zlBigdecimal6">
              <el-form-item label="主要业务板块收入" prop="zlBigdecimal6">
                <el-input
                  v-model="formData.zlBigdecimal6"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal6Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal6Des">
                <el-input
                  v-model="formData.zlBigdecimal6Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal7">
              <el-form-item
                label="主要业务收入占总收入比重"
                prop="zlBigdecimal7"
              >
                <el-input
                  v-model="formData.zlBigdecimal7"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal7Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal7Des">
                <el-input
                  v-model="formData.zlBigdecimal7Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="
            visibleFields.zlBigdecimal8 ||
            visibleFields.zlBigdecimal9 ||
            visibleFields.zlInteger5 ||
            visibleFields.zlBigdecimal11
          "
        >
          <div class="indicator-title">科技创新风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.zlBigdecimal8">
              <el-form-item label="研发投入" prop="zlBigdecimal8">
                <el-input
                  v-model="formData.zlBigdecimal8"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal8Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal8Des">
                <el-input
                  v-model="formData.zlBigdecimal8Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal9">
              <el-form-item label="研发投入占总收入比重" prop="zlBigdecimal9">
                <el-input
                  v-model="formData.zlBigdecimal9"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal9Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal9Des">
                <el-input
                  v-model="formData.zlBigdecimal9Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.zlInteger5">
              <el-form-item label="重大科技项目逾期数量" prop="zlInteger5">
                <el-input
                  v-model="formData.zlInteger5"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlInteger5Des">
              <el-form-item label="具体情况说明" prop="zlInteger5Des">
                <el-input
                  v-model="formData.zlInteger5Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.zlBigdecimal11">
              <el-form-item label="研发投入计划完成率" prop="zlBigdecimal11">
                <el-input
                  v-model="formData.zlBigdecimal11"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.zlBigdecimal11Des">
              <el-form-item label="具体情况说明" prop="zlBigdecimal11Des">
                <el-input
                  v-model="formData.zlBigdecimal11Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 财务风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.cwInteger1 ||
          visibleFields.cwBigdecimal2 ||
          visibleFields.cwInteger2 ||
          visibleFields.cwBigdecimal3 ||
          visibleFields.cwInteger3 ||
          visibleFields.cwBigdecimal4 ||
          visibleFields.cwInteger4 ||
          visibleFields.cwBigdecimal5 ||
          visibleFields.cwInteger5 ||
          visibleFields.cwBigdecimal6 ||
          visibleFields.cwBigdecimal1 ||
          visibleFields.cwBigdecimal7 ||
          visibleFields.cwBigdecimal8 ||
          visibleFields.cwBigdecimal9 ||
          visibleFields.cwBigdecimal10 ||
          visibleFields.cwBigdecimal11 ||
          visibleFields.cwBigdecimal12 ||
          visibleFields.cwBigdecimal13
        "
      >
        财务风险
      </el-divider>
      <div class="form-section">
        <div
          class="risk-indicator"
          v-if="
            visibleFields.cwInteger1 ||
            visibleFields.cwBigdecimal2 ||
            visibleFields.cwInteger2 ||
            visibleFields.cwBigdecimal3 ||
            visibleFields.cwInteger3 ||
            visibleFields.cwBigdecimal4 ||
            visibleFields.cwInteger4 ||
            visibleFields.cwBigdecimal5 ||
            visibleFields.cwInteger5 ||
            visibleFields.cwBigdecimal6 ||
            visibleFields.cwBigdecimal1 ||
            visibleFields.cwBigdecimal7 ||
            visibleFields.cwBigdecimal8 ||
            visibleFields.cwBigdecimal9 ||
            visibleFields.cwBigdecimal10 ||
            visibleFields.cwBigdecimal11 ||
            visibleFields.cwBigdecimal12 ||
            visibleFields.cwBigdecimal13 ||
            visibleFields.cwBigdecimal12Des ||
            visibleFields.cwBigdecimal13Des
          "
        >
          <div class="indicator-title">金融及金融衍生品业务风险</div>

          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwInteger1">
              <el-form-item label="对外担保业务违约事项数量" prop="cwInteger1">
                <el-input
                  v-model="formData.cwInteger1"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwInteger1Des">
              <el-form-item label="具体情况说明" prop="cwInteger1Des">
                <el-input
                  v-model="formData.cwInteger1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal2">
              <el-form-item
                label="对外担保业务违约事项金额"
                prop="cwBigdecimal2"
              >
                <el-input
                  v-model="formData.cwBigdecimal2"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal2Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal2Des">
                <el-input
                  v-model="formData.cwBigdecimal2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwInteger2">
              <el-form-item label="融资租赁业务违约数量" prop="cwInteger2">
                <el-input
                  v-model="formData.cwInteger2"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwInteger2Des">
              <el-form-item label="具体情况说明" prop="cwInteger2Des">
                <el-input
                  v-model="formData.cwInteger2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal3">
              <el-form-item label="融资租赁业务违约金额" prop="cwBigdecimal3">
                <el-input
                  v-model="formData.cwBigdecimal3"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal3Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal3Des">
                <el-input
                  v-model="formData.cwBigdecimal3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwInteger3">
              <el-form-item label="债券、股票质押回购数量" prop="cwInteger3">
                <el-input
                  v-model="formData.cwInteger3"
                  placeholder="请输入数量"
                >
                  <template slot="append">笔</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwInteger3Des">
              <el-form-item label="具体情况说明" prop="cwInteger3Des">
                <el-input
                  v-model="formData.cwInteger3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal4">
              <el-form-item label="债券、股票质押回购金额" prop="cwBigdecimal4">
                <el-input
                  v-model="formData.cwBigdecimal4"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal4Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal4Des">
                <el-input
                  v-model="formData.cwBigdecimal4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwInteger4">
              <el-form-item label="信托业务违约数量" prop="cwInteger4">
                <el-input
                  v-model="formData.cwInteger4"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwInteger4Des">
              <el-form-item label="具体情况说明" prop="cwInteger4Des">
                <el-input
                  v-model="formData.cwInteger4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal5">
              <el-form-item label="信托业务违约金额" prop="cwBigdecimal5">
                <el-input
                  v-model="formData.cwBigdecimal5"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal5Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal5Des">
                <el-input
                  v-model="formData.cwBigdecimal5Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwInteger5">
              <el-form-item label="保理业务违约数量" prop="cwInteger5">
                <el-input
                  v-model="formData.cwInteger5"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwInteger5Des">
              <el-form-item label="具体情况说明" prop="cwInteger5Des">
                <el-input
                  v-model="formData.cwInteger5Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal6">
              <el-form-item label="保理业务违约金额" prop="cwBigdecimal6">
                <el-input
                  v-model="formData.cwBigdecimal6"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal6Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal6Des">
                <el-input
                  v-model="formData.cwBigdecimal6Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwBigdecimal1">
              <el-form-item label="衍生品盈亏金额" prop="cwBigdecimal1">
                <el-input
                  v-model="formData.cwBigdecimal1"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal1Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal1Des">
                <el-input
                  v-model="formData.cwBigdecimal1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="visibleFields.cwBigdecimal7 || visibleFields.cwBigdecimal8"
        >
          <div class="indicator-title">债务风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwBigdecimal7">
              <el-form-item label="流动比率" prop="cwBigdecimal7">
                <el-input
                  v-model="formData.cwBigdecimal7"
                  placeholder="请输入流动比率"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal7Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal7Des">
                <el-input
                  v-model="formData.cwBigdecimal7Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal8">
              <el-form-item label="资产负债率" prop="cwBigdecimal8">
                <el-input
                  v-model="formData.cwBigdecimal8"
                  placeholder="请输入资产负债率"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal8Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal8Des">
                <el-input
                  v-model="formData.cwBigdecimal8Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="
            visibleFields.cwBigdecimal9 ||
            visibleFields.cwBigdecimal10 ||
            visibleFields.cwBigdecimal11 ||
            visibleFields.cwBigdecimal12 ||
            visibleFields.cwBigdecimal13 ||
            visibleFields.cwBigdecimal12Des ||
            visibleFields.cwBigdecimal13Des
          "
        >
          <div class="indicator-title">现金流风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.cwBigdecimal9">
              <el-form-item label="现金流动负债比率" prop="cwBigdecimal9">
                <el-input
                  v-model="formData.cwBigdecimal9"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal9Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal9Des">
                <el-input
                  v-model="formData.cwBigdecimal9Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal10">
              <el-form-item
                label="两金（应收账款和存货）总金额"
                prop="cwBigdecimal10"
              >
                <el-input
                  v-model="formData.cwBigdecimal10"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal10Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal10Des">
                <el-input
                  v-model="formData.cwBigdecimal10Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal11">
              <el-form-item
                label="两金（应收账款和存货）占流动资产比重"
                prop="cwBigdecimal11"
              >
                <el-input
                  v-model="formData.cwBigdecimal11"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal11Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal11Des">
                <el-input
                  v-model="formData.cwBigdecimal11Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal12">
              <el-form-item label="经营性现金流" prop="cwBigdecimal12">
                <el-input
                  v-model="formData.cwBigdecimal12"
                  placeholder="请输入经营性现金流"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal12Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal12Des">
                <el-input
                  v-model="formData.cwBigdecimal12Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal13">
              <el-form-item
                label="一年内到期带息债务是否偿还"
                prop="cwBigdecimal13"
              >
                <el-select
                  v-model="formData.cwBigdecimal13"
                  placeholder="请选择"
                  clearable
                  style="width: 100%"
                >
                  <el-option label="否" :value="0"></el-option>
                  <el-option label="是" :value="1"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.cwBigdecimal13Des">
              <el-form-item label="具体情况说明" prop="cwBigdecimal13Des">
                <el-input
                  v-model="formData.cwBigdecimal13Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 市场风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.scInteger1 ||
          visibleFields.scBigdecimal1 ||
          visibleFields.scInteger2 ||
          visibleFields.scBigdecimal2 ||
          visibleFields.scBigdecimal3 ||
          visibleFields.scBigdecimal4 ||
          visibleFields.scBigdecimal5 ||
          visibleFields.scBigdecimal6
        "
      >
        市场风险
      </el-divider>
      <div class="form-section">
        <div
          class="risk-indicator"
          v-if="
            visibleFields.scInteger1 ||
            visibleFields.scBigdecimal1 ||
            visibleFields.scInteger2 ||
            visibleFields.scBigdecimal2
          "
        >
          <div class="indicator-title">竞争风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.scInteger1">
              <el-form-item label="主要产品价格下降" prop="scInteger1">
                <el-input
                  v-model="formData.scInteger1"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scInteger1Des">
              <el-form-item label="具体情况说明" prop="scInteger1Des">
                <el-input
                  v-model="formData.scInteger1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal1">
              <el-form-item label="最大降幅" prop="scBigdecimal1">
                <el-input
                  v-model="formData.scBigdecimal1"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal1Des">
              <el-form-item label="具体情况说明" prop="scBigdecimal1Des">
                <el-input
                  v-model="formData.scBigdecimal1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.scInteger2">
              <el-form-item label="主要产品市场占有率下降" prop="scInteger2">
                <el-input
                  v-model="formData.scInteger2"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scInteger2Des">
              <el-form-item label="具体情况说明" prop="scInteger2Des">
                <el-input
                  v-model="formData.scInteger2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal2">
              <el-form-item label="最大降幅" prop="scBigdecimal2">
                <el-input
                  v-model="formData.scBigdecimal2"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal2Des">
              <el-form-item label="具体情况说明" prop="scBigdecimal2Des">
                <el-input
                  v-model="formData.scBigdecimal2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="
            visibleFields.scBigdecimal3 ||
            visibleFields.scBigdecimal4 ||
            visibleFields.scBigdecimal5 ||
            visibleFields.scBigdecimal6
          "
        >
          <div class="indicator-title">客户信用风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.scBigdecimal3">
              <el-form-item
                label="账龄三年及以上的应收账款金额"
                prop="scBigdecimal3"
              >
                <el-input
                  v-model="formData.scBigdecimal3"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal3Des">
              <el-form-item label="具体情况说明" prop="scBigdecimal3Des">
                <el-input
                  v-model="formData.scBigdecimal3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal4">
              <el-form-item label="占应收账款比重" prop="scBigdecimal4">
                <el-input
                  v-model="formData.scBigdecimal4"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal4Des">
              <el-form-item label="具体情况说明" prop="scBigdecimal4Des">
                <el-input
                  v-model="formData.scBigdecimal4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.scBigdecimal5">
              <el-form-item label="逾期应收账款" prop="scBigdecimal5">
                <el-input
                  v-model="formData.scBigdecimal5"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal5Des">
              <el-form-item label="具体情况说明" prop="scBigdecimal5Des">
                <el-input
                  v-model="formData.scBigdecimal5Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal6">
              <el-form-item label="占应收账款比重" prop="scBigdecimal6">
                <el-input
                  v-model="formData.scBigdecimal6"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.scBigdecimal6Des">
              <el-form-item label="具体情况说明" prop="scBigdecimal6Des">
                <el-input
                  v-model="formData.scBigdecimal6Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 运营风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.yyInteger1 ||
          visibleFields.yyBigdecimal1 ||
          visibleFields.yyBigdecimal2 ||
          visibleFields.yyBigdecimal3 ||
          visibleFields.yyBigdecimal4 ||
          visibleFields.yyInteger2 ||
          visibleFields.yyInteger3 ||
          visibleFields.yyInteger4 ||
          visibleFields.yyInteger5 ||
          visibleFields.yyInteger6 ||
          visibleFields.yyBigdecimal5 ||
          visibleFields.yyBigdecimal6 ||
          visibleFields.yyBigdecimal7
        "
      >
        运营风险
      </el-divider>
      <div class="form-section">
        <div
          class="risk-indicator"
          v-if="
            visibleFields.yyInteger1 ||
            visibleFields.yyBigdecimal1 ||
            visibleFields.yyBigdecimal5
          "
        >
          <div class="indicator-title">经营效益风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyInteger1">
              <el-form-item label="亏损子企业" prop="yyInteger1">
                <el-input
                  v-model="formData.yyInteger1"
                  placeholder="请输入数量"
                >
                  <template slot="append">户</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyInteger1Des">
              <el-form-item label="具体情况说明" prop="yyInteger1Des">
                <el-input
                  v-model="formData.yyInteger1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal1">
              <el-form-item label="亏损金额" prop="yyBigdecimal1">
                <el-input
                  v-model="formData.yyBigdecimal1"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal1Des">
              <el-form-item label="具体情况说明" prop="yyBigdecimal1Des">
                <el-input
                  v-model="formData.yyBigdecimal1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal5">
              <el-form-item
                label="知识产权侵权事件数量（商标等）"
                prop="yyBigdecimal5"
              >
                <el-input
                  v-model="formData.yyBigdecimal5"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="visibleFields.yyBigdecimal6 || visibleFields.yyBigdecimal7"
        >
          <div class="indicator-title">经营风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyBigdecimal6">
              <el-form-item label="两金增长百分比" prop="yyBigdecimal6">
                <el-input
                  v-model="formData.yyBigdecimal6"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal6Des">
              <el-form-item label="具体情况说明" prop="yyBigdecimal6Des">
                <el-input
                  v-model="formData.yyBigdecimal6Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal7">
              <el-form-item label="收入增长百分比" prop="yyBigdecimal7">
                <el-input
                  v-model="formData.yyBigdecimal7"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal7Des">
              <el-form-item label="具体情况说明" prop="yyBigdecimal7Des">
                <el-input
                  v-model="formData.yyBigdecimal7Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div
          class="risk-indicator"
          v-if="
            visibleFields.yyBigdecimal2 ||
            visibleFields.yyBigdecimal3 ||
            visibleFields.yyBigdecimal4
          "
        >
          <div class="indicator-title">投资风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyBigdecimal2">
              <el-form-item label="非主业项目投资" prop="yyBigdecimal2">
                <el-input
                  v-model="formData.yyBigdecimal2"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal2Des">
              <el-form-item label="具体情况说明" prop="yyBigdecimal2Des">
                <el-input
                  v-model="formData.yyBigdecimal2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal3">
              <el-form-item label="占总投资额比重" prop="yyBigdecimal3">
                <el-input
                  v-model="formData.yyBigdecimal3"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal3Des">
              <el-form-item label="具体情况说明" prop="yyBigdecimal3Des">
                <el-input
                  v-model="formData.yyBigdecimal3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal4">
              <el-form-item
                label="年度投资计划完成率（季度实际投资额/全年计划投资额）"
                prop="yyBigdecimal4"
              >
                <el-input
                  v-model="formData.yyBigdecimal4"
                  placeholder="请输入百分比"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyBigdecimal4Des">
              <el-form-item label="具体情况说明" prop="yyBigdecimal4Des">
                <el-input
                  v-model="formData.yyBigdecimal4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <!-- 安全、环保、质量风险部分 -->
        <div
          class="risk-indicator"
          v-if="
            visibleFields.yyInteger2 ||
            visibleFields.yyInteger3 ||
            visibleFields.yyInteger4 ||
            visibleFields.yyInteger5 ||
            visibleFields.yyInteger6
          "
        >
          <div class="indicator-title">安全、环保、质量风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyInteger2">
              <el-form-item label="重大安全生产事故数量" prop="yyInteger2">
                <el-input
                  v-model="formData.yyInteger2"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyInteger2Des">
              <el-form-item label="具体情况说明" prop="yyInteger2Des">
                <el-input
                  v-model="formData.yyInteger2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>

            <el-col :span="12" v-if="visibleFields.yyInteger3">
              <el-form-item
                label="重大及以上突发环境事件数量"
                prop="yyInteger3"
              >
                <el-input
                  v-model="formData.yyInteger3"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyInteger3Des">
              <el-form-item label="具体情况说明" prop="yyInteger3Des">
                <el-input
                  v-model="formData.yyInteger3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="risk-indicator" v-if="visibleFields.yyInteger4">
          <div class="indicator-title">舆情风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyInteger4">
              <el-form-item label="重大舆情事件数量" prop="yyInteger4">
                <el-input
                  v-model="formData.yyInteger4"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyInteger4Des">
              <el-form-item label="具体情况说明" prop="yyInteger4Des">
                <el-input
                  v-model="formData.yyInteger4Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div class="risk-indicator" v-if="visibleFields.yyInteger5">
          <div class="indicator-title">采购与供应链管理风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyInteger5">
              <el-form-item label="出现重要产品断供的子企业" prop="yyInteger5">
                <el-input
                  v-model="formData.yyInteger5"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyInteger5Des">
              <el-form-item label="具体情况说明" prop="yyInteger5Des">
                <el-input
                  v-model="formData.yyInteger5Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="risk-indicator" v-if="visibleFields.yyInteger6">
          <div class="indicator-title">工程项目管理风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.yyInteger6">
              <el-form-item label="境内重大建设项目逾期数量" prop="yyInteger6">
                <el-input
                  v-model="formData.yyInteger6"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.yyInteger6Des">
              <el-form-item label="具体情况说明" prop="yyInteger6Des">
                <el-input
                  v-model="formData.yyInteger6Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 法律风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.flInteger2 ||
          visibleFields.flBigdecimal1 ||
          visibleFields.flInteger1 ||
          visibleFields.flInteger3 ||
          visibleFields.flBigdecimal2 ||
          visibleFields.flBigdecimal3
        "
      >
        法律风险
      </el-divider>
      <div class="form-section">
        <div
          class="risk-indicator"
          v-if="
            visibleFields.flInteger2 ||
            visibleFields.flBigdecimal1 ||
            visibleFields.flInteger1 ||
            visibleFields.flInteger3 ||
            visibleFields.flBigdecimal2 ||
            visibleFields.flBigdecimal3
          "
        >
          <div class="indicator-title">合规风险</div>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.flInteger2">
              <el-form-item label="境内重大法律诉讼案件" prop="flInteger2">
                <el-input
                  v-model="formData.flInteger2"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flInteger2Des">
              <el-form-item label="具体情况说明" prop="flInteger2Des">
                <el-input
                  v-model="formData.flInteger2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flBigdecimal1">
              <el-form-item
                label="境内重大法律诉讼案件金额"
                prop="flBigdecimal1"
              >
                <el-input
                  v-model="formData.flBigdecimal1"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flBigdecimal1Des">
              <el-form-item label="具体情况说明" prop="flBigdecimal1Des">
                <el-input
                  v-model="formData.flBigdecimal1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.flInteger1">
              <el-form-item label="重大监管处罚数量" prop="flInteger1">
                <el-input
                  v-model="formData.flInteger1"
                  placeholder="请输入数量"
                >
                  <template slot="append">个</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flInteger1Des">
              <el-form-item label="具体情况说明" prop="flInteger1Des">
                <el-input
                  v-model="formData.flInteger1Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flBigdecimal2">
              <el-form-item label="公司年度净利润" prop="flBigdecimal2">
                <el-input
                  v-model="formData.flBigdecimal2"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flBigdecimal2Des">
              <el-form-item label="具体情况说明" prop="flBigdecimal2Des">
                <el-input
                  v-model="formData.flBigdecimal2Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12" v-if="visibleFields.flBigdecimal3">
              <el-form-item label="新增法律纠纷案件金额" prop="flBigdecimal3">
                <el-input
                  v-model="formData.flBigdecimal3"
                  placeholder="请输入金额"
                >
                  <template slot="append">万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="visibleFields.flBigdecimal3Des">
              <el-form-item label="具体情况说明" prop="flBigdecimal3Des">
                <el-input
                  v-model="formData.flBigdecimal3Des"
                  placeholder="请输入具体情况说明"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 安全环保风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.aqBigdecimal1 ||
          visibleFields.aqBigdecimal2 ||
          visibleFields.aqBigdecimal3 ||
          visibleFields.aqBigdecimal4 ||
          visibleFields.aqBigdecimal5 ||
          visibleFields.aqBigdecimal6 ||
          visibleFields.aqBigdecimal7 ||
          visibleFields.aqBigdecimal8 ||
          visibleFields.aqBigdecimal9 ||
          visibleFields.aqBigdecimal10 ||
          visibleFields.aqBigdecimal11 ||
          visibleFields.aqBigdecimal12 ||
          visibleFields.aqBigdecimal13 ||
          visibleFields.aqBigdecimal14 ||
          visibleFields.aqBigdecimal15 ||
          visibleFields.aqBigdecimal16 ||
          visibleFields.aqBigdecimal17
        "
      >
        安全环保风险
      </el-divider>

      <!-- 辐射安全 -->
      <div
        class="indicator-title"
        v-if="
          visibleFields.aqBigdecimal1 ||
          visibleFields.aqBigdecimal2 ||
          visibleFields.aqBigdecimal16 ||
          visibleFields.aqBigdecimal17
        "
      >
        辐射安全
      </div>
      <el-row :gutter="20">
        <el-col :span="12" v-if="visibleFields.aqBigdecimal1">
          <el-form-item label="一般事故次数" prop="aqBigdecimal1">
            <el-input v-model="formData.aqBigdecimal1" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal2">
          <el-form-item label="具有潜在事故风险的违规事件" prop="aqBigdecimal2">
            <el-input v-model="formData.aqBigdecimal2" placeholder="请输入数量">
              <template slot="append">个</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="indicator-title" v-if="visibleFields.aqBigdecimal16 || visibleFields.aqBigdecimal17 || visibleFields.aqBigdecimal16Des || visibleFields.aqBigdecimal17Des">核事件数量</div>
      <el-row :gutter="20">
        <el-col :span="12" v-if="visibleFields.aqBigdecimal16">
          <el-form-item label="发生1级以上核事件次数" prop="aqBigdecimal16">
            <el-input
              v-model="formData.aqBigdecimal16"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal17">
          <el-form-item label="发生0级核事件次数" prop="aqBigdecimal17">
            <el-input
              v-model="formData.aqBigdecimal17"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12" v-if="visibleFields.aqBigdecimal16Des">
          <el-form-item
            label="发生1级以上核事件次数说明"
            prop="aqBigdecimal16Des"
          >
            <el-input
              v-model="formData.aqBigdecimal16Des"
              type="textarea"
              :rows="3"
              placeholder="请输入说明"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal17Des">
          <el-form-item label="发生0级核事件次数说明" prop="aqBigdecimal17Des">
            <el-input
              v-model="formData.aqBigdecimal17Des"
              type="textarea"
              :rows="3"
              placeholder="请输入说明"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 工业安全 -->
      <div
        class="indicator-title"
        v-if="
          visibleFields.aqBigdecimal3 ||
          visibleFields.aqBigdecimal4 ||
          visibleFields.aqBigdecimal5 ||
          visibleFields.aqBigdecimal6
        "
      >
        工业安全
      </div>
      <el-row :gutter="20">
        <el-col :span="12" v-if="visibleFields.aqBigdecimal3">
          <el-form-item label="特别重大事故次数" prop="aqBigdecimal3">
            <el-input v-model="formData.aqBigdecimal3" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal4">
          <el-form-item label="重大事故次数" prop="aqBigdecimal4">
            <el-input v-model="formData.aqBigdecimal4" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal5">
          <el-form-item label="较大事故次数" prop="aqBigdecimal5">
            <el-input v-model="formData.aqBigdecimal5" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal6">
          <el-form-item label="一般事故次数" prop="aqBigdecimal6">
            <el-input v-model="formData.aqBigdecimal6" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 职业病 -->
      <div
        class="indicator-title"
        v-if="
          visibleFields.aqBigdecimal7 ||
          visibleFields.aqBigdecimal8 ||
          visibleFields.aqBigdecimal9 ||
          visibleFields.aqBigdecimal10
        "
      >
        职业病
      </div>
      <el-row :gutter="20">
        <el-col :span="12" v-if="visibleFields.aqBigdecimal7">
          <el-form-item label="特别重大事故次数" prop="aqBigdecimal7">
            <el-input v-model="formData.aqBigdecimal7" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal8">
          <el-form-item label="重大事故次数" prop="aqBigdecimal8">
            <el-input v-model="formData.aqBigdecimal8" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal9">
          <el-form-item label="较大事故次数" prop="aqBigdecimal9">
            <el-input v-model="formData.aqBigdecimal9" placeholder="请输入次数">
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal10">
          <el-form-item label="一般事故次数" prop="aqBigdecimal10">
            <el-input
              v-model="formData.aqBigdecimal10"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 环境安全 -->
      <div
        class="indicator-title"
        v-if="
          visibleFields.aqBigdecimal11 ||
          visibleFields.aqBigdecimal12 ||
          visibleFields.aqBigdecimal13 ||
          visibleFields.aqBigdecimal14 ||
          visibleFields.aqBigdecimal15
        "
      >
        环境安全
      </div>
      <el-row :gutter="20">
        <el-col :span="12" v-if="visibleFields.aqBigdecimal11">
          <el-form-item label="特别重大事故次数" prop="aqBigdecimal11">
            <el-input
              v-model="formData.aqBigdecimal11"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal12">
          <el-form-item label="重大事故次数" prop="aqBigdecimal12">
            <el-input
              v-model="formData.aqBigdecimal12"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal13">
          <el-form-item label="较大事故次数" prop="aqBigdecimal13">
            <el-input
              v-model="formData.aqBigdecimal13"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal14">
          <el-form-item label="一般事故次数" prop="aqBigdecimal14">
            <el-input
              v-model="formData.aqBigdecimal14"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="visibleFields.aqBigdecimal15">
          <el-form-item label="受到环保行政处罚次数" prop="aqBigdecimal15">
            <el-input
              v-model="formData.aqBigdecimal15"
              placeholder="请输入次数"
            >
              <template slot="append">次</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 质量风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.zlfxBigdecimal1 ||
          visibleFields.zlfxBigdecimal2 ||
          visibleFields.zlfxBigdecimal3
        "
      >
        质量风险
      </el-divider>
      <div class="form-section">
        <el-row :gutter="20">
          <el-col :span="12" v-if="visibleFields.zlfxBigdecimal1">
            <el-form-item
              label="较大及以上质量事故发生次数"
              prop="zlfxBigdecimal1"
            >
              <el-input v-model="formData.zlfxBigdecimal1" placeholder="请输入">
                <template slot="append">次</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="visibleFields.zlfxBigdecimal2">
            <el-form-item label="一般质量事故次数" prop="zlfxBigdecimal2">
              <el-input v-model="formData.zlfxBigdecimal2" placeholder="请输入">
                <template slot="append">次</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="visibleFields.zlfxBigdecimal3">
            <el-form-item label="发生质量事件次数" prop="zlfxBigdecimal3">
              <el-input v-model="formData.zlfxBigdecimal3" placeholder="请输入">
                <template slot="append">次</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <!-- 保密风险 -->
      <el-divider
        content-position="center"
        v-if="
          visibleFields.bmBigdecimal1 ||
          visibleFields.bmBigdecimal2 ||
          visibleFields.bmBigdecimal3 ||
          visibleFields.bmBigdecimal4
        "
      >
        保密风险
      </el-divider>
      <div class="form-section">
        <el-row :gutter="20">
          <el-col :span="12" v-if="visibleFields.bmBigdecimal1">
            <el-form-item label="一级资格单位未通过数量" prop="bmBigdecimal1">
              <el-input
                v-model="formData.bmBigdecimal1"
                placeholder="请输入数量"
              >
                <template slot="append">个</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="visibleFields.bmBigdecimal2">
            <el-form-item label="二级资格单位未通过数量" prop="bmBigdecimal2">
              <el-input
                v-model="formData.bmBigdecimal2"
                placeholder="请输入数量"
              >
                <template slot="append">个</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="visibleFields.bmBigdecimal3">
            <el-form-item label="三级资格单位未通过数量" prop="bmBigdecimal3">
              <el-input
                v-model="formData.bmBigdecimal3"
                placeholder="请输入数量"
              >
                <template slot="append">个</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="visibleFields.bmBigdecimal4">
            <el-form-item label="发生泄密事件次数" prop="bmBigdecimal4">
              <el-input
                v-model="formData.bmBigdecimal4"
                placeholder="请输入次数"
              >
                <template slot="append">次</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <!-- 其他风险 -->
      <el-divider content-position="center" v-if="visibleFields.qtString1">
        其他风险
      </el-divider>
      <div class="form-section">
        <el-row :gutter="20">
          <el-col :span="24" v-if="visibleFields.qtString1">
            <el-form-item
              label="其他对企业经营发展造成重大影响的风险"
              prop="qtString1"
            >
              <el-input
                v-model="formData.qtString1"
                type="textarea"
                :rows="4"
                placeholder="请描述其他风险情况"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="visibleFields.qtString1Des">
            <el-form-item label="具体情况说明" prop="qtString1Des">
              <el-input
                v-model="formData.qtString1Des"
                placeholder="请输入具体情况说明"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="deptNotes">
              <el-input
                type="textarea"
                :rows="3"
                v-model="formData.deptNotes"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <el-button type="primary" @click="submitForm">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    saveOrUpdate,
    detail,
    getRiskMonDeptList,
  } from '@/api/risk/monitoringFill'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    components: { ZXPerson, Resubmit },
    data() {
      return {
        dialogVisible: false,
        editType: '', // 'fill' 或 'detail'
        currentRow: null,
        disabled: false,
        formData: {
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          assId: undefined,
          assname: undefined,
          // 基础信息字段
          id: null,
          riskyear: '',
          quartername: '',
          notes: '',
          monitorId: null,
          createstaffid: null,
          createname: '',
          createtime: null,
          contact: '',
          secrectlevelid: null,
          staffscopeids: '',
          staffscopenames: '',
          linkOrgId: null,
          linkOrgName: '',
          linkDeptId: null,
          linkDeptName: null,
          toReportDate: null,
          reportStaffid: '',
          reportStaffName: '',
          reportstatus: 0,
          deptNotes: '', // 战略风险字段 (ZL开头)
          zlString1: '', // 国内外宏观经济形势变化
          zlString1Des: '', // 国内外宏观经济形势变化说明
          zlString2: '', // 国家及行业政策变化
          zlString2Des: '', // 国家及行业政策变化说明
          zlInteger1: '', // 被美国列入"实体清单"、受关税政策影响的子企业数量
          zlInteger1Des: '', // 被美国列入"实体清单"、受关税政策影响的子企业数量说明
          zlInteger2: '', // 境外重大建设项目逾期数量
          zlInteger2Des: '', // 境外重大建设项目逾期数量说明
          zlInteger3: '', // 境外重大法律诉讼案件数量
          zlInteger3Des: '', // 境外重大法律诉讼案件数量说明
          zlInteger4: '', // 境外重大合规案件数量
          zlInteger4Des: '', // 境外重大合规案件数量说明
          zlInteger5: '', // 重大科技项目逾期数量
          zlInteger5Des: '', // 重大科技项目逾期数量说明
          zlBigdecimal1: '', // 境外中高风险地区境外资产总额
          zlBigdecimal1Des: '', // 境外中高风险地区境外资产总额说明
          zlBigdecimal2: '', // 境外中高风险地区资产占比
          zlBigdecimal2Des: '', // 境外中高风险地区资产占比说明
          zlBigdecimal3: '', // 汇率损失金额
          zlBigdecimal3Des: '', // 汇率损失金额说明
          zlBigdecimal4: '', // 境外重大法律诉讼案件涉案金额
          zlBigdecimal4Des: '', // 境外重大法律诉讼案件涉案金额说明
          zlBigdecimal5: '', // 境外重大合规案件涉案金额
          zlBigdecimal5Des: '', // 境外重大合规案件涉案金额说明
          zlBigdecimal6: '', // 主要业务板块收入
          zlBigdecimal6Des: '', // 主要业务板块收入说明
          zlBigdecimal7: '', // 主要业务收入占总收入比重
          zlBigdecimal7Des: '', // 主要业务收入占总收入比重说明
          zlBigdecimal8: '', // 研发投入
          zlBigdecimal8Des: '', // 研发投入说明
          zlBigdecimal9: '', // 研发投入占总收入比重
          zlBigdecimal9Des: '', // 研发投入占总收入比重说明
          zlBigdecimal10: '', // 研发投入占研发支出比重
          zlBigdecimal11: '', // 研发投入计划完成率
          zlBigdecimal11Des: '', // 研发投入计划完成率说明

          // 财务风险字段 (CW开头)
          cwInteger1: '', // 对外担保业务违约事项数量
          cwInteger1Des: '', // 对外担保业务违约事项数量说明
          cwInteger2: '', // 融资租赁业务违约数量
          cwInteger2Des: '', // 融资租赁业务违约数量说明
          cwInteger3: '', // 债券、股票质押回购数量
          cwInteger3Des: '', // 债券、股票质押回购数量说明
          cwInteger4: '', // 信托业务违约数量
          cwInteger4Des: '', // 信托业务违约数量说明
          cwInteger5: '', // 保理业务违约数量
          cwInteger5Des: '', // 保理业务违约数量说明
          cwBigdecimal1: '', // 衍生品盈亏金额
          cwBigdecimal1Des: '', // 衍生品盈亏金额说明
          cwBigdecimal2: '', // 对外担保业务违约事项金额
          cwBigdecimal2Des: '', // 对外担保业务违约事项金额说明
          cwBigdecimal3: '', // 融资租赁业务违约金额
          cwBigdecimal3Des: '', // 融资租赁业务违约金额说明
          cwBigdecimal4: '', // 债券、股票质押回购金额
          cwBigdecimal4Des: '', // 债券、股票质押回购金额说明
          cwBigdecimal5: '', // 信托业务违约金额
          cwBigdecimal5Des: '', // 信托业务违约金额说明
          cwBigdecimal6: '', // 保理业务违约金额
          cwBigdecimal6Des: '', // 保理业务违约金额说明
          cwBigdecimal7: '', // 流动比率
          cwBigdecimal7Des: '', // 流动比率说明
          cwBigdecimal8: '', // 资产负债率
          cwBigdecimal8Des: '', // 资产负债率说明
          cwBigdecimal9: '', // 现金流动负债比率
          cwBigdecimal9Des: '', // 现金流动负债比率说明
          cwBigdecimal10: '', // 两金（应收账款和存货）总金额
          cwBigdecimal10Des: '', // 两金（应收账款和存货）总金额说明
          cwBigdecimal11: '', // 两金（应收账款和存货）占流动资产比重
          cwBigdecimal11Des: '', // 两金（应收账款和存货）占流动资产比重说明
          cwBigdecimal12: '', // 经营性现金流
          cwBigdecimal12Des: '', // 经营性现金流说明
          cwBigdecimal13: '', // 一年内到期带息债务是否偿还
          cwBigdecimal13Des: '', // 一年内到期带息债务是否偿还说明

          // 市场风险字段 (SC开头)
          scInteger1: '', // 主要产品价格下降
          scInteger1Des: '', // 主要产品价格下降说明
          scInteger2: '', // 主要产品市场占有率下降
          scInteger2Des: '', // 主要产品市场占有率下降说明
          scBigdecimal1: '', // 主要产品价格下降最大降幅
          scBigdecimal1Des: '', // 主要产品价格下降最大降幅说明
          scBigdecimal2: '', // 主要产品市场占有率下降最大降幅
          scBigdecimal2Des: '', // 主要产品市场占有率下降最大降幅说明
          scBigdecimal3: '', // 账龄三年及以上的应收账款金额
          scBigdecimal3Des: '', // 账龄三年及以上的应收账款金额说明
          scBigdecimal4: '', // 账龄三年及以上的应收账款占应收账款比重
          scBigdecimal4Des: '', // 账龄三年及以上的应收账款占应收账款比重说明
          scBigdecimal5: '', // 逾期应收账款
          scBigdecimal5Des: '', // 逾期应收账款说明
          scBigdecimal6: '', // 逾期应收账款占应收账款比重
          scBigdecimal6Des: '', // 逾期应收账款占应收账款比重说明

          // 运营风险字段 (YY开头)
          yyInteger1: '', // 亏损子企业
          yyInteger1Des: '', // 亏损子企业说明
          yyInteger2: '', // 重大安全生产事故数量
          yyInteger2Des: '', // 重大安全生产事故数量说明
          yyInteger3: '', // 重大及以上突发环境事件数量
          yyInteger3Des: '', // 重大及以上突发环境事件数量说明
          yyInteger4: '', // 重大舆情事件数量
          yyInteger4Des: '', // 重大舆情事件数量说明
          yyInteger5: '', // 出现重要产品断供的子企业数量
          yyInteger5Des: '', // 出现重要产品断供的子企业数量说明
          yyInteger6: '', // 境内重大建设项目逾期数量
          yyInteger6Des: '', // 境内重大建设项目逾期数量说明
          yyBigdecimal1: '', // 亏损金额
          yyBigdecimal1Des: '', // 亏损金额说明
          yyBigdecimal2: '', // 非主业项目投资
          yyBigdecimal2Des: '', // 非主业项目投资说明
          yyBigdecimal3: '', // 非主业项目投资占总投资额比重
          yyBigdecimal3Des: '', // 非主业项目投资占总投资额比重说明
          yyBigdecimal4: '', // 年度投资计划完成率
          yyBigdecimal4Des: '', // 年度投资计划完成率说明
          yyBigdecimal5: '', // 研发投入占研发支出比重
          yyBigdecimal6: '', // 两金增长百分比
          yyBigdecimal6Des: '', // 两金增长百分比说明
          yyBigdecimal7: '', // 收入增长百分比
          yyBigdecimal7Des: '', // 收入增长百分比说明

          // 法律风险字段 (FL开头)
          flInteger1: '', // 重大监管处罚数量
          flInteger1Des: '', // 重大监管处罚数量说明
          flInteger2: '', // 境内重大法律诉讼案件数量
          flInteger2Des: '', // 境内重大法律诉讼案件数量说明
          flBigdecimal1: '', // 境内重大法律诉讼案件涉案金额
          flBigdecimal1Des: '', // 境内重大法律诉讼案件涉案金额说明
          flBigdecimal2: '', // 境外重大法律诉讼案件涉案金额
          flBigdecimal2Des: '', // 境外重大法律诉讼案件涉案金额说明
          flBigdecimal3: '', // 新增法律纠纷案件金额
          flBigdecimal3Des: '', // 新增法律纠纷案件金额说明

          // 其他风险字段 (QT开头)
          qtString1: '', // 其他对企业经营发展造成重大影响的风险
          qtString1Des: '', // 其他对企业经营发展造成重大影响的风险说明
          // 风险分析字段 (ZLFX开头)
          zlfxBigdecimal1: '',
          zlfxBigdecimal2: '',
          zlfxBigdecimal3: '',
          // 保密 (BM开头)
          bmBigdecimal1: '',
          bmBigdecimal2: '',
          bmBigdecimal3: '',
          bmBigdecimal4: '', // 其他对企业经营发展造成重大影响的风险
          // 安全环保风险字段 (AQ开头)
          aqBigdecimal1: '', // 辐射安全-一般事故数
          aqBigdecimal2: '', // 辐射安全-较大事故数
          aqBigdecimal3: '', // 工业安全-特别重大事故数
          aqBigdecimal4: '', // 工业安全-重大事故数
          aqBigdecimal5: '', // 工业安全-较大事故数
          aqBigdecimal6: '', // 工业安全-一般事故数
          aqBigdecimal7: '', // 职业病-特别重大事故数
          aqBigdecimal8: '', // 职业病-重大事故数
          aqBigdecimal9: '', // 职业病-较大事故数
          aqBigdecimal10: '', // 职业病-一般事故数
          aqBigdecimal11: '', // 环境安全-特别重大事故数
          aqBigdecimal12: '', // 环境安全-重大事故数
          aqBigdecimal13: '', // 环境安全-较大事故数
          aqBigdecimal14: '', // 环境安全-一般事故数
          aqBigdecimal15: '', // 环境安全-受到环保行政处罚次数
          aqBigdecimal16: '', // 发生1级以上核事件次数
          aqBigdecimal16Des: '', // 发生1级以上核事件次数说明
          aqBigdecimal17: '', // 发生0级核事件次数
          aqBigdecimal17Des: '', // 发生0级核事件次数说明
        },
        options: [
          {
            value: '一季度',
            label: '一季度',
          },
          {
            value: '二季度',
            label: '二季度',
          },
          {
            value: '三季度',
            label: '三季度',
          },
          {
            value: '四季度',
            label: '四季度',
          },
        ],
        showMJ: false,
        MJoption: [],
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        MJoption: [],
        menuId: 0,
        showMJ: false,
        btnLoading: false,
        visibleFields: {}, // 存储字段可见性映射
      }
    },
    computed: {
      // 根据年度和季度计算填报区间
      reportingPeriod() {
        if (!this.formData.riskyear || !this.formData.quartername) {
          return '请选择年度和季度'
        }

        const year = parseInt(this.formData.riskyear)
        const quarter = this.formData.quartername

        let startDate, endDate

        switch (quarter) {
          case '一季度':
            startDate = `${year}.1.1`
            endDate = `${year}.3.31`
            break
          case '二季度':
            startDate = `${year}.1.1`
            endDate = `${year}.6.30`
            break
          case '三季度':
            startDate = `${year}.1.1`
            endDate = `${year}.9.30`
            break
          case '四季度':
            startDate = `${year}.1.1`
            endDate = `${year + 1}.12.31`
            break
          default:
            return '请选择有效季度'
        }

        return `${startDate} - ${endDate}`
      },
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
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
            this.formData.assId = undefined
            this.formData.assname = undefined
          }
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      // 显示填报弹窗
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        if (flowType) {
          this.getMJData(flowType)
        }
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.disabled = title === 'detail'
        if (formId) {
          const { data } = await detail({ id: formId })
          Object.assign(this.formData, data)
          this.formData.deptNotes = data.deptNotes
          // 处理年度格式
          if (data.riskyear) {
            this.$set(this.formData, 'riskyear', String(data.riskyear))
          }
          // 获取字段可见性数据
          await this.getRiskMonDeptList({ id: formId })
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      //获取填写的字段数据
      async getRiskMonDeptList(row) {
        try {
          const {
            data: { data },
          } = await getRiskMonDeptList({ id: row.id })

          // 创建可见字段映射
          this.visibleFields = {}

          // 遍历返回的数据，根据code值设置字段可见性
          if (data && Array.isArray(data)) {
            data.forEach((item) => {
              if (item.code) {
                this.visibleFields[item.code] = true
                // 同步显示对应的具体情况说明字段
                this.visibleFields[item.code + 'Des'] = true
              }
            })
          }

          console.log('可见字段:', this.visibleFields)
        } catch (error) {
          console.error('获取字段数据失败:', error)
        }
      },
      close() {
        this.disabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      submitForm() {
        this.$refs.riskForm.validate((valid) => {
          if (valid) {
            const submitData = {
              ...this.formData,
              cwBigdecimal11: Number(this.formData.cwBigdecimal11),
              // 如果是编辑模式，保留ID
              ...(this.currentRow && this.currentRow.id
                ? { id: this.currentRow.id }
                : {}),
            }
            // 调用保存接口
            saveOrUpdate(submitData)
              .then((response) => {
                const { code, msg } = response
                if (code === 1) {
                  this.$message.success('保存成功')
                  this.dialogVisible = false
                  // 触发父组件刷新数据
                  this.$emit('fetchData')
                } else {
                  this.$message.error(msg || '提交失败')
                }
              })
              .catch((error) => {
                console.error('提交失败:', error)
                this.$message.error('提交失败，请重试')
              })
          }
        })
      },
      resetForm() {
        if (this.$refs.riskForm) {
          this.$refs.riskForm.resetFields()
        }
        // 使用组件初始 data 的默认表单对象进行覆盖，确保所有字段保持响应式
        const defaults = this.$options.data.call(this).formData
        Object.assign(this.formData, defaults)
        // 重置可见字段与禁用状态
        this.visibleFields = {}
        this.disabled = false
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['riskForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
    },
  }
</script>

<style scoped>
  .divider-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 40px;
    margin-top: 10px;
  }

  .vertical-divider {
    width: 1px;
    height: 100%;
    background-color: #dcdfe6;
  }

  .form-section {
    margin-bottom: 20px;
  }

  .risk-indicator {
    margin-bottom: 15px;
  }

  .indicator-title {
    font-weight: bold;
    margin-bottom: 10px;
    color: #606266;
  }
  .reporting-period-divider {
    color: red !important;
  }

  .reporting-period-divider .el-divider__text {
    color: red !important;
    font-weight: bold;
  }
</style>
